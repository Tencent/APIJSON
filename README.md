# 功能点

### 一个json(事务)同时支持新增、修改、删除、查询、别名

https://github.com/Tencent/APIJSON/issues/468

#### 使用说明

json支持多种方式定义method

第一种:

 "@post","@put","@delete","@head","@get","@gets","@head","@heads"

"@post": ["Moment","Comment[]"] , 值为数组格式,  每个value = key

需要保证每个key唯一, 唯一判断标准:

key = Moment 

key= Moment[] 

会判断为相同key. 请通过别名区分, 别名格式: Sys_user_role:sur  xxx表名:别名

```
{
   "@post": ["Moment","Comment:cArray[]","User:u"], // 分发到 POST 请求对应的解析处理
   "Moment": {
     // TODO 其它字段
   },
   "Comment:cArray[]": [
      {
        // TODO 其它字段
      }
   ],
   "@get": ["User"], // 分发到 GET 请求对应的解析处理
   "User:u": {
     // TODO 其它字段
   },
   "Privacy": { // 按 URL 对应的默认方法处理
     // TODO 其它字段
   }
}

对于没有显式声明操作方法的，直接用 URL(/get, /post 等) 对应的默认操作方法

```

第二种:

对象内定义"@method": "GET", value大写

```
{
	"sql@": {
    	"@method": "GET",
        "with": true,
        "from": "Sys_role",
        "Sys_role": {
          "@column": "id",
          "role_name": "角色1"
        }
    },
    "Sys_user_role:sur[]": {
    	"@method": "GET",
        "Sys_user_role": {
            "role_id{}@": "sql"
        }
    },
    "Sys_role_permission:srp[]": {
    	"@method": "GET",
        "Sys_role_permission": {
            "role_id{}@": "sql"
        }
    },
    "@explain": true
}
```

#### 解析顺序

1) 对象内 "@method"
2) "@post","@put","@delete"
3) 对于没有显式声明操作方法的，直接用 URL(/get, /post 等) 对应的默认操作方法

#### tag自动生成规则

/**
 * { "xxx:aa":{ "@tag": "" }}
 * 生成规则:
 * 1、@tag存在,tag=@tag
 * 2、@tag不存在
 * 1)、存在别名
 * key=对象: tag=key去除别名
 * key=数组: tag=key去除别名 + []
 * 2)、不存在别名
 * tag=key
 * tag=key + []
 */


![image](https://user-images.githubusercontent.com/12228225/204079184-06dd08a7-95a3-4a46-8e05-f062fa406847.png)


#### 建议

1. 一个json包含不同操作方法, url method 使用 /post, /put
2. value为JSONArray, 建议通过"@post" 方式配置, 如果没有配置,执行 3

#### Request表 配置说明

这只是我的配置仅供参考, 后续 测试会用到:

```
单条新增:
POST   User_address     {"MUST":"addr","UPDATE": {"@role": "OWNER,ADMIN","childFunTest-()": "childFunTest(addr)"}, "REFUSE": "id"}
批量新增:
POST User_address[]  {"User_address[]": [{"MUST":"addr","REFUSE": "id"}], "UPDATE": {"@role": "OWNER,ADMIN","childFunTest-()": "childFunTest(addr)"}}
单条修改:
PUT User_address   {"User_address":{ "MUST":"id","REFUSE": "userId", "UPDATE": {"@role": "OWNER,ADMIN","childFunTest-()": "childFunTest(addr)"}} }
批量修改:
PUT User_address[]  {"User_address[]": [{"MUST": "id","REFUSE": "userId"}], "UPDATE": {"@role": "OWNER,ADMIN"}}
删除:
DELETE User_address   {"User_address":{ "MUST":"id{}","REFUSE": "!", "INSERT": {"@role": "OWNER,ADMIN"}} }

```
![image](https://user-images.githubusercontent.com/12228225/204079438-8f352496-4b73-4b72-88c0-914894335074.png)



### 别名

格式: 

Sys_user_role:sur  xxx表名:别名

Comment:cArray[]

#### 实现思路

当时参考了作者的示例: 注册流程. 看到绕过校验, 可以将多条json语句组装在一起, 批量执行. 于是就想如何实现一个json支持不同操作方法,并支持事物. 

通过分析源码, 熟悉了校验流程、json解析执行流程、json解析生成sql语句流程、一些兼容、校验规则等

经过和作者讨论, 很感谢作者提供了相关解决方案和思路. 逐步理解了apijson的设计原理和框架结构.

一个json(事务)同时支持新增、修改、删除、查询、别名, 实现思路如下:

1、校验模块

将json解析成对象、临时变量、子查询、别名、tag等

并将method 添加到 json对象属性中.

```
"Sys_role": {
    	"@method": "PUT",
		"id": "6aedce0d-2a29-4fbe-aeed-0ba935ca6b41",
        "id{}@": "sql",
        "role_code": "code-subrange-4",
        "role_name": "角色-subrange-4"
    }
```

2、对象解析

用对象属性@method , 替换 Parser 的 method

3、事物支持

### 后续优化建议

1、独立定义一个url method, 通过解析不同method执行不同流程

和已有method区分开,避免歧义

2、最外层新增传参 "transaction": true 来指定开启事务
目前是url put、post来控制开启事物, 以及提交的时候 在 AbstractParser onCommit 判断 transactionIsolation (4 : 开启事物, 0: 非事物请求) 

![image](https://user-images.githubusercontent.com/12228225/204079532-26d9cd4b-d2d7-4c73-9f78-f425bbbcf623.png)

详细实现请参见: https://github.com/Tencent/APIJSON/issues/468

3、完善 "[@Explain](https://github.com/Explain)"

如果没有执行计划,则返回sql语句. 能够在 reponse返回值中, 看到json中执行的每条sql,方便排错

![image](https://user-images.githubusercontent.com/12228225/204079543-be464f67-a80f-4a33-87ea-d1870908e642.png)

4、@version支持

定义不同场景的 新增、修改、删除等执行规则. 请通过version版本区分

Request表是通过tag、method、version来保证唯一.

![image](https://user-images.githubusercontent.com/12228225/204079562-00449c38-42b1-4d9c-b562-2d56c77e6218.png)

5、前置函数

前置函数能够将json语句, 加入到 当前事物中.

例如:  像数组一样,解析成每一条语句去执行.

### mysql8 with-as表达式

#### 前提条件

1、mysql版本: 8+

2、mysql-connector-java: 8.0.31

版本支持 with-as即可

3、druid: 1.2.15

版本支持 with-as即可

4、去掉 durid wall配置

delete子查询,  druid wall 拦截器报错 sql injection violation

![image](https://user-images.githubusercontent.com/12228225/204079572-19a4f50c-3bf3-4f9e-9677-6aa191276fef.png)

#### 测试案例

#### 查询单个range ref引用

```
// 测试 mysql8 with as表达式
// 用户表
// 用户角色表
// 角色表
// 示例一 单个range ref引用
{
	"sql@": {
    	"@method": "GET",
        "with": true,
        "from": "Sys_role",
        "Sys_role": {
          "@column": "id",
          "role_name": "角色1"
        }
    },
    "Sys_user_role:sur[]": {
    	"@method": "GET",
        "Sys_user_role": {
            "role_id{}@": "sql"
        }
    },
    "Sys_role_permission:srp[]": {
    	"@method": "GET",
        "Sys_role_permission": {
            "role_id{}@": "sql"
        }
    },
    "@explain": true
}  

// 第二种写法
{
	"@get": ["sql@","Sys_user_role:sur[]","Sys_role_permission:srp[]"],
	"sql@": {
        "with": true,
        "from": "Sys_role",
        "Sys_role": {
          "@column": "id",
          "role_name": "角色1"
        }
    },
    "Sys_user_role:sur[]": {
        "Sys_user_role": {
            "role_id{}@": "sql"
        }
    },
    "Sys_role_permission:srp[]": {
        "Sys_role_permission": {
            "role_id{}@": "sql"
        }
    },
    "@explain": true
} 
```

mysql8执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079581-bf835db2-30ae-4265-bda2-ebf34c0d9e77.png)

mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079594-3ebc73a0-836e-4073-9aa4-acb665fe8d52.png)


#### 查询多个range ref引用

```
{
    "sql@": {
        "@method": "GET", 
        "with": true, 
        "from": "Sys_role", 
        "Sys_role": {
            "@column": "id", 
            "role_name": "角色1"
        }
    }, 
    "sql_user@": {
        "@method": "GET", 
        "with": true, 
        "from": "Sys_user", 
        "Sys_user": {
            "@column": "id", 
            "id": "f0894db2-6940-4d89-a5b2-4405d0ad0c8f"
        }
    }, 
    "Sys_user_role:sur[]": {
        "@method": "GET", 
        "Sys_user_role": {
            "role_id{}@": "sql", 
            "user_id{}@": "sql_user"
        }
    }, 
    "Sys_role_permission:srp[]": {
        "@method": "GET", 
        "Sys_role_permission": {
            "role_id{}@": "sql"
        }
    }, 
    "@explain": true
}
```

mysql8执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079603-2ba224a3-3174-491a-a71b-7656c97d0146.png)

mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079611-155f6a33-ad56-4d03-8e5d-6f44c3649051.png)

#### delete子查询

```j s
{
	"sql@": {
		"@method": "GET",
        "with": true,
        "from": "Sys_role_permission",
        "Sys_role_permission": {
          "@column": "id",
		  "role_id{}": ["e7129d5f-b07e-4996-9965-9528e370a393"]
        }
    },
    "Sys_role_permission": {
    	"@method": "DELETE",
        "id{}@": "sql"
    },
    "explan": true
}

```

![image](https://user-images.githubusercontent.com/12228225/204079615-25185be5-a296-488f-9a13-98fb2b99a9d5.png)

mysql8执行sql语句:

```
WITH  `sql` AS (SELECT `id` FROM `housekeeping`.`Sys_role_permission` WHERE  (  (`role_id` IN ('68877ee9-4cf4-4f32-86e6-16c505ca3b21'))  ) ) DELETE FROM `housekeeping`.`Sys_role_permission` WHERE  (  (`id` IN ( SELECT * FROM `sql`) )  ) 

Plain Text
```

mysql5.7执行结果:

```
DELETE FROM `housekeeping`.`Sys_role_permission` WHERE  (  (`id` IN ( SELECT * FROM (SELECT `id` FROM `housekeeping`.`Sys_role_permission` WHERE  (  (`role_id` IN ('20d337bb-9886-455f-8dce-f1cadab0ec4f'))  ) ) AS `sql`) )  ) 



Plain Text
```

#### update子查询

```
{
   "sql@": {
		"@method": "GET",
        "with": true,
        "from": "Sys_role_permission",
        "Sys_role_permission": {
          "@column": "role_id",
		  "id{}": ["6aedce0d-2a29-4fbe-aeed-0ba935ca6b41"]
        }
    },
    "Sys_role": {
    	"@method": "PUT",
		"id": "6aedce0d-2a29-4fbe-aeed-0ba935ca6b41",
        "id{}@": "sql",
        "role_code": "code-subrange-4",
        "role_name": "角色-subrange-4"
    },
    "@explain": true
}

第二种写法
{
	"@get": ["sql@"],
    "sql@": {
        "with": true,
        "from": "Sys_role_permission",
        "Sys_role_permission": {
          "@column": "role_id",
		  "id{}": ["c95ef2d6-bf14-42b0-bb87-038cee8c78f1"]
        }
    },
    "@put": ["Sys_role"],
    "Sys_role": {
		"id": "0bb92d96-8ca6-469e-91e8-60308ce5b835",
        "id{}@": "sql",
        "role_code": "code-subrange-4",
        "role_name": "角色-subrange-4"
    },
    "@explain": true
}
```

mysql8执行sql语句:

![image](https://user-images.githubusercontent.com/12228225/204079628-8536b4be-8078-42a5-b3f7-460159372a8a.png)


mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079633-df9175bc-703f-4997-95f6-85bbc1134b0b.png)

#### GETS 单条子查询

会执行校验流程

```
http://localhost:8675/lowCodePlatform/forms/api/gets

{
	"sql@": {
        "with": true,
        "from": "Sys_user_role",
        "Sys_user_role": {
          "@column": "role_id",
		  "user_id": "4732209c-5785-4827-b532-5092f154fd94"
        }
    },
    "Sys_role[]": {
        "Sys_role": {
            "id{}@": "sql"
        },
        "page": 0,
        "count": 10,
        "query": 2
    },
    "tag":"Sys_role[]",
    "total@": "/Sys_role[]/total",
    "@explain": true
}

第二种写法
{
	"@gets": ["sql@","Sys_role[]"],
	"sql@": {
        "with": true,
        "from": "Sys_user_role",
        "Sys_user_role": {
          "@column": "role_id",
		  "user_id": "4732209c-5785-4827-b532-5092f154fd94"
        }
    },
    "Sys_role[]": {
        "Sys_role": {
            "id{}@": "sql"
        },
        "page": 0,
        "count": 10,
        "query": 2
    },
    "tag":"Sys_role[]",
    "total@": "/Sys_role[]/total",
    "@explain": true
}

```

Access、Request需要配置鉴权信息:

![image](https://user-images.githubusercontent.com/12228225/204079649-510a047b-2b8e-44d2-a32a-f6ea0e7f6a74.png)


mysql8执行sql语句:

![image](https://user-images.githubusercontent.com/12228225/204079657-6e62872a-2f29-478e-a29b-bcb0a92781a6.png)

mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079878-a9885b86-5a44-4ba2-b837-66adc43b07d3.png)

#### GETS多条子查询

会执行校验流程

```
http://localhost:8675/lowCodePlatform/forms/api/gets

{
	"sql@": {
    	"@method": "GETS",
        "with": true,
        "from": "Sys_role",
        "Sys_role": {
          "@column": "id",
          "role_name": "超级管理员"
        }
    },
    "sql_user@": {
    	"@method": "GETS",
        "with": true,
        "from": "Sys_user",
        "Sys_user": {
          "@column": "id",
          "id": "4732209c-5785-4827-b532-5092f154fd94"
        }
    },
    "Sys_user_role:sur[]": {
    	"@method": "GETS",
        "Sys_user_role": {
            "role_id{}@": "sql",
            "user_id{}@": "sql_user"
        }
    },
    "Sys_role_permission:srp[]": {
    	"@method": "GETS",
        "Sys_role_permission": {
            "role_id{}@": "sql"
        }
    },
    "@explain": true
}

```

mysql8执行sql语句:

![image](https://user-images.githubusercontent.com/12228225/204079892-bc71eb65-cfbd-4c3c-bda9-4b31902058ba.png)

mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079897-521a763f-bb08-44af-92c6-5e4117fe9d33.png)

#### head 单个子查询

普通获取数量, get/head不执行校验流程

```
http://localhost:8675/lowCodePlatform/forms/api/head
{
	"sql@": {
		"@method": "GET",
        "with": true,
        "from": "Sys_user_role",
        "Sys_user_role": {
          "@column": "role_id",
		  "user_id": "4732209c-5785-4827-b532-5092f154fd94"
        }
    },
    "Sys_role": {
    	"@method": "head",
        "id{}@": "sql"
    },
    "@explain": true
}

```

mysql8执行sql语句:

![image](https://user-images.githubusercontent.com/12228225/204079903-e397a78a-1849-4678-ac41-0611165a1de1.png)

mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079908-1efb5b28-889d-4d9b-b4f9-5092925888c9.png)

#### head 多个子查询

普通获取数量, get/head不执行校验流程

```
{
	"sql@": {
    	"@method": "GET",
        "with": true,
        "from": "Sys_role",
        "Sys_role": {
          "@column": "id",
          "role_name": "超级管理员"
        }
    },
    "sql_user@": {
    	"@method": "GET",
        "with": true,
        "from": "Sys_user",
        "Sys_user": {
          "@column": "id",
          "id": "4732209c-5785-4827-b532-5092f154fd94"
        }
    },
    "Sys_user_role": {
        "@method": "HEAD",
        "role_id{}@": "sql",
            "user_id{}@": "sql_user"
    },
    "Sys_role_permission": {
        "@method": "HEAD",
        "role_id{}@": "sql"
    },
    "@explain": true
}

```

mysql8执行sql语句:

![image](https://user-images.githubusercontent.com/12228225/204079919-5fba8f87-56d8-4d7d-b457-4a2505f27d1e.png)

mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079932-1e040caf-57fd-45a7-afa5-b26bdce83fba.png)

#### heads 单个子查询

普通获取数量

会执行校验流程, Access、Request需要配置鉴权信息:
![image](https://user-images.githubusercontent.com/12228225/204079942-d790a3c0-eb46-4512-bb58-45a16894608a.png)

```
http://localhost:8675/lowCodePlatform/forms/api/heads

{
	"sql@": {
		"@method": "GET",
        "with": true,
        "from": "Sys_user_role",
        "Sys_user_role": {
          "@column": "role_id",
		  "user_id": "4732209c-5785-4827-b532-5092f154fd94"
        }
    },
    "Sys_role": {
    	"@method": "heads",
        "id{}@": "sql"
    },
    "@explain": true
}

```

mysql8执行sql语句:

![image](https://user-images.githubusercontent.com/12228225/204079952-976fa9b6-4a11-40ad-a2c7-6f901b186670.png)

mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079959-6bf95b45-5f35-474e-b428-b51bcb5b500d.png)

#### heads 多个子查询

会执行校验流程, Access、Request需要配置鉴权信息:

![image](https://user-images.githubusercontent.com/12228225/204079967-a48f4f50-6e6b-476b-a281-b072ef8a352d.png)

普通获取数量

```
{
	"sql@": {
    	"@method": "GET",
        "with": true,
        "from": "Sys_role",
        "Sys_role": {
          "@column": "id",
          "role_name": "超级管理员"
        }
    },
    "sql_user@": {
    	"@method": "GET",
        "with": true,
        "from": "Sys_user",
        "Sys_user": {
          "@column": "id",
          "id": "4732209c-5785-4827-b532-5092f154fd94"
        }
    },
    "Sys_user_role": {
        "@method": "HEADS",
        "role_id{}@": "sql",
        "user_id{}@": "sql_user"
    },
    "Sys_role_permission": {
        "@method": "HEADS",
        "role_id{}@": "sql"
    },
    "@explain": true
}

```

mysql8执行sql语句:

![image](https://user-images.githubusercontent.com/12228225/204079980-c93ef595-0c4b-42a7-a3b3-1e7402d3cb13.png)

mysql5.7执行结果:

![image](https://user-images.githubusercontent.com/12228225/204079987-878d5937-3f42-4f59-93dc-b5a840f5548c.png)

### delete、put 支持子查询

https://github.com/Tencent/APIJSON/issues/471

静态变量做全局处理，特殊接口用 Operation.MUST id/id{}/id{}@ 做自定义处理。

之所以默认必传，是因为安全意识不够、编码粗心大意的人太多了，所以要有一个底线保障，尽可能避免安全隐患。

1、全局配置 为 PUT, DELETE 强制要求必须有 id/id{}/id{}@ 条件

AbstractVerifier.IS_UPDATE_MUST_HAVE_ID_CONDITION = true; // true: 必须有

![image](https://user-images.githubusercontent.com/12228225/204080001-eef4ee65-0ad0-4a41-93ba-9b16cd1c2e0e.png)

2、细粒度控制

![image](https://user-images.githubusercontent.com/12228225/204080012-f7d781e9-0a53-461f-84db-3d6ecb167e20.png)

#### 使用说明

```
// 条件删除
{
    "User:del": {
        "username": "test3"
    },
    "tag": "User",
    "explain": true
}

// 引用id{}@删除
{
	"sql@": {
		"@method": "GET",
        "with": true,
        "from": "Sys_user_role",
        "Sys_user_role": {
          "@column": "user_id",
		  "role_id{}": ["023e1880-c0d4-4e7c-ae6c-7703199c2daf"]
        }
    },
    "Sys_user:aa": {
    	"@method": "DELETE",
        "id{}@": "sql"
    },
    "explan": true
}
// 子查询条件删除
http://localhost:8675/lowCodePlatform/forms/api/delete
{
	"sql@": {
		"@method": "GET",
        "with": true,
        "from": "User",
        "User": {
          "@column": "username",
		  "username": "test-3"
        }
    },
    "User": {
        "username{}@": "sql"
    },
    "explan": true
}

第二种写法:
{
	"@get": ["sql@"],
	"sql@": {
        "with": true,
        "from": "User",
        "User": {
          "@column": "username",
		  "username": "test4"
        }
    },
    "User": {
        "username{}@": "sql"
    },
    "explan": true
}


```



开启id删除, 删除失败:

```
{
	"@get": ["sql@"],
	"sql@": {
        "with": true,
        "from": "User",
        "User": {
          "@column": "username",
		  "username": "test4"
        }
    },
    "User": {
        "username{}@": "sql"
    },
    "explan": true
}
```

![image](https://user-images.githubusercontent.com/12228225/204080043-6614457c-a0ed-45b3-a26a-e75126dbb486.png)

开启id删除、id引用 删除成功

```
{
	"sql@": {
		"@method": "GET",
        "with": true,
        "from": "Sys_user_role",
        "Sys_user_role": {
          "@column": "user_id",
		  "role_id{}": ["0bb92d96-8ca6-469e-91e8-60308ce5b835"]
        }
    },
    "Sys_user:aa": {
    	"@method": "DELETE",
        "id{}@": "sql"
    },
    "explan": true
}
```
![image](https://user-images.githubusercontent.com/12228225/204080050-e6f04fe6-319e-45b7-b1b2-bf4cda4ab2db.png)

PUT 子查询 修改

```
{
   "sql@": {
		"@method": "GET",
        "with": true,
        "from": "Sys_role_permission",
        "Sys_role_permission": {
          "@column": "role_id",
		  "id{}": ["ba2634f8-0bdc-4b50-9c5e-47786b1536ef"]
        }
    },
    "Sys_role": {
    	"@method": "PUT",
        "id{}@": "sql",
        "role_code": "code-subrange-5",
        "role_name": "角色-subrange-5"
    },
    "@explain": true
}
```

![image](https://user-images.githubusercontent.com/12228225/204080072-8f605595-cd8c-474b-975f-4ac97fb92a26.png)

#### bug修复

删除操作 主表 和 子查询 是同一张表
mysql8以下 非with-as表达式 会报错:
"msg": "You can't specify target table 'User' for update in FROM clause",

需要调整sql语句,将子查询包一层(select * from (子查询) as xxx)
DELETE FROM `housekeeping`.`User`
WHERE ( (`username` IN (SELECT * FROM (SELECT `username` FROM `housekeeping`.`User` WHERE ( (`username` = 'test1') )) as a) ) )

![image](https://user-images.githubusercontent.com/12228225/204080126-e1f7c82a-2f09-409d-b3f2-fe25badea180.png)

![image](https://user-images.githubusercontent.com/12228225/204080131-0c15404d-3045-4d01-bd89-d2a1f1fa0360.png)


### must、refuses判断、delete、PUT支持 ref

```
{
	"sql@": {
		"@method": "GET",
        "with": true,
        "from": "Sys_role_permission",
        "Sys_role_permission": {
          "@column": "id",
		  "role_id{}": ["94f79f0b-331b-4cc5-bfc0-ebfc47d00f13"]
        }
    },
    "Sys_role_permission": {
    	"@method": "DELETE",
        "id{}@": "sql"
    },
    "explan": true
}
```

![image](https://user-images.githubusercontent.com/12228225/204080150-28972226-37e0-4280-962a-83f7ac12d37c.png)
