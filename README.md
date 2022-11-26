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

![img](https://tcs-devops.aliyuncs.com/storage/112o42fb67f3226bf1ba31143f8e3983d0d3?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IjYxYmQ0MTk3YWM0MDEyNWFmMGY1ZmJlMyIsImV4cCI6MTY2OTk2MjcxMiwiaWF0IjoxNjY5MzU3OTEyLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm80MmZiNjdmMzIyNmJmMWJhMzExNDNmOGUzOTgzZDBkMyJ9.c0kLsYpkpHzkVchL8ZxzUiPf0uvD3G-dsrIc1_zxBl0)

### 别名

格式: 

Sys_user_role:sur  xxx表名:别名

Comment:cArray[]

#### 实现思路

当时参考了作者的示例: 注册流程. 看到绕过校验, 可以将多条json语句组装在一起, 批量执行. 于是就想如何实现一个json支持不同操作,并支持事物. 

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

![image](https://user-images.githubusercontent.com/12228225/202369747-396a4062-c70d-407e-abfa-c333d4c89bee.png)

详细实现请参见: https://github.com/Tencent/APIJSON/issues/468

3、完善 "[@Explain](https://github.com/Explain)"

如果没有执行计划,则返回sql语句. 能够在 reponse返回值中, 看到json中执行的每条sql,方便排错

![img](https://tcs-devops.aliyuncs.com/storage/112oc4589a6d650755c05381097fe9f2294f?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9jNDU4OWE2ZDY1MDc1NWMwNTM4MTA5N2ZlOWYyMjk0ZiJ9.WySq2UEIS5fMySWRqtKiaXzR4wwtQVxrtbSp98vHmx4)

4、@version支持

定义不同场景的 新增、修改、删除等执行规则. 请通过version版本区分

Request表是通过tag、method、version来保证唯一.

![image-20221125144359453](/Users/xy/Library/Application Support/typora-user-images/image-20221125144359453.png)

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

![image-20221125152519359](/Users/xy/Library/Application Support/typora-user-images/image-20221125152519359.png)

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

![img](https://tcs-devops.aliyuncs.com/storage/112oc4589a6d650755c05381097fe9f2294f?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9jNDU4OWE2ZDY1MDc1NWMwNTM4MTA5N2ZlOWYyMjk0ZiJ9.WySq2UEIS5fMySWRqtKiaXzR4wwtQVxrtbSp98vHmx4)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112oa2437cfa419471ebdb7ea62dec97ee21?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9hMjQzN2NmYTQxOTQ3MWViZGI3ZWE2MmRlYzk3ZWUyMSJ9.ulNYQKUnvWMIZCpsc-caImZMsF4h21Vq5WdJ03hwjgc)

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

![img](https://tcs-devops.aliyuncs.com/storage/112o8c94c21ab8f2f473337a53b369ea8db4?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm84Yzk0YzIxYWI4ZjJmNDczMzM3YTUzYjM2OWVhOGRiNCJ9.DoQZ7uoFEoUonbut23NPp_uzVTZ851PlzZ5FlSZWKGY)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112o9671ece47b1834ac147cc01cd3900ece?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm85NjcxZWNlNDdiMTgzNGFjMTQ3Y2MwMWNkMzkwMGVjZSJ9.EPqYf1F_RooQF2c5PtrzPdMYCkYZ8Tng4p0Nobgymgc)

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

![img](https://tcs-devops.aliyuncs.com/storage/112o12df4490ee563a2a5a36da580823528a?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm8xMmRmNDQ5MGVlNTYzYTJhNWEzNmRhNTgwODIzNTI4YSJ9.shAM0ABXqPJTlFYVMU8UjxngUE3jdWgficdNcHgp2Ho)

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

![img](https://tcs-devops.aliyuncs.com/storage/112o69fb17f5eb1c109501063a0501e068ef?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm82OWZiMTdmNWViMWMxMDk1MDEwNjNhMDUwMWUwNjhlZiJ9.1lyjMFAM-y9l5Mu6vlgXRv6S0xRwxoMDdrPTzs8LTbQ)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112oe59ad49145a7d0a4f100ba717261ee91?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IjYxYmQ0MTk3YWM0MDEyNWFmMGY1ZmJlMyIsImV4cCI6MTY2OTk0ODM1MiwiaWF0IjoxNjY5MzQzNTUyLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9lNTlhZDQ5MTQ1YTdkMGE0ZjEwMGJhNzE3MjYxZWU5MSJ9.bOOaskAmRczI9U87uNNIqB1pQxHlkmeZDzYAd_zmaMI)

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

![img](https://tcs-devops.aliyuncs.com/storage/112oc0e2d9666d0e6ae5bba40dd10e86ff8b?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9jMGUyZDk2NjZkMGU2YWU1YmJhNDBkZDEwZTg2ZmY4YiJ9.WiHevTQ80WuelszQxRrsQYWQD4u_60rfj6tsZn9d4vY)

mysql8执行sql语句:

![img](https://tcs-devops.aliyuncs.com/storage/112o13624f7ea9b1bbbb31bc4cc83cd96ae6?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm8xMzYyNGY3ZWE5YjFiYmJiMzFiYzRjYzgzY2Q5NmFlNiJ9.ZSCMLnTzj7BCi4nxZXSYohKVfHWNR--G5WXv9qE4R1c)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112oae3f5de5b703c6334747befb9ab05349?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IjYxYmQ0MTk3YWM0MDEyNWFmMGY1ZmJlMyIsImV4cCI6MTY2OTk0ODc5OSwiaWF0IjoxNjY5MzQzOTk5LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9hZTNmNWRlNWI3MDNjNjMzNDc0N2JlZmI5YWIwNTM0OSJ9.xVaHGnOULNO4uec94RadSs5K8iG0QZVyGD-FvlNraOY)

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

![img](https://tcs-devops.aliyuncs.com/storage/112of22704b782cc932d4f51a144aaddbe76?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9mMjI3MDRiNzgyY2M5MzJkNGY1MWExNDRhYWRkYmU3NiJ9.ejWQb3yK60XBm3zRm4PU077l3lu2jsHjqX5ij0tvS7g)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112o331a15eb0b89f8788c2e1369e9ead38a?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IjYxYmQ0MTk3YWM0MDEyNWFmMGY1ZmJlMyIsImV4cCI6MTY2OTk0ODg3MSwiaWF0IjoxNjY5MzQ0MDcxLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm8zMzFhMTVlYjBiODlmODc4OGMyZTEzNjllOWVhZDM4YSJ9.NZjO2ndpyIW5CdQ3giGL4ylkclWC4VRNtJePIB7hBTs)

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

![img](https://tcs-devops.aliyuncs.com/storage/112o97e7879481ef209d27cd689881ba2dae?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm85N2U3ODc5NDgxZWYyMDlkMjdjZDY4OTg4MWJhMmRhZSJ9.1FsB785CT9VOnKf-Z6NYu6Ouy3DiL0SJm5G0iSHnO94)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112o7b2de6f66df479e2b38f53e25da56b04?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IjYxYmQ0MTk3YWM0MDEyNWFmMGY1ZmJlMyIsImV4cCI6MTY2OTk0ODk1NywiaWF0IjoxNjY5MzQ0MTU3LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm83YjJkZTZmNjZkZjQ3OWUyYjM4ZjUzZTI1ZGE1NmIwNCJ9.Nxi3u1TMTYE92FuAnz4srLBLeW8fHt-T0gXIOguAu-Q)

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

![img](https://tcs-devops.aliyuncs.com/storage/112o2da6826648bb92fe52d292e6f98194fe?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm8yZGE2ODI2NjQ4YmI5MmZlNTJkMjkyZTZmOTgxOTRmZSJ9.z9t8h2jF9HYrnr-86j61zQmIuzNquNZ1LWD-mKFyOa8)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112o0d8df9fbfdc94b6b6f338f080cd4bc19?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IjYxYmQ0MTk3YWM0MDEyNWFmMGY1ZmJlMyIsImV4cCI6MTY2OTk0OTAwOCwiaWF0IjoxNjY5MzQ0MjA4LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm8wZDhkZjlmYmZkYzk0YjZiNmYzMzhmMDgwY2Q0YmMxOSJ9.3jOg9eJMTRZXIQh3ZFnTsuhu1N4CnoM3CR7t3I6rIn4)

#### heads 单个子查询

普通获取数量

会执行校验流程, Access、Request需要配置鉴权信息:

![img](https://tcs-devops.aliyuncs.com/storage/112oe091e1d4376a6fcb62cbce0383cdddf9?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9lMDkxZTFkNDM3NmE2ZmNiNjJjYmNlMDM4M2NkZGRmOSJ9._BSyl8VzN15CTAxr_J1ud0tzYCBQwGQWqaiBRvmau8Q)

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

![img](https://tcs-devops.aliyuncs.com/storage/112offc8ee52b3bd03af5012ae9832ed7800?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9mZmM4ZWU1MmIzYmQwM2FmNTAxMmFlOTgzMmVkNzgwMCJ9.aPpEd5GM5iTIs149Xc2kxovMu82HzbZUHd3L7yrtkQE)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112o1ad0c28563c50c245ee39d399fcbf817?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IjYxYmQ0MTk3YWM0MDEyNWFmMGY1ZmJlMyIsImV4cCI6MTY2OTk0OTE3MywiaWF0IjoxNjY5MzQ0MzczLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm8xYWQwYzI4NTYzYzUwYzI0NWVlMzlkMzk5ZmNiZjgxNyJ9.Wc4X-gAhJhKtFDIc15i8Y-JFb6BYy4EBVGNQ0fq2_SE)

#### heads 多个子查询

会执行校验流程, Access、Request需要配置鉴权信息:

![img](https://tcs-devops.aliyuncs.com/storage/112od6c06bedd0338403de83a3da034862b6?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9kNmMwNmJlZGQwMzM4NDAzZGU4M2EzZGEwMzQ4NjJiNiJ9.c4OVSUGncl_anepEZN1jzHmk7FiIRY-UcRj6EOn0ySk)

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

![img](https://tcs-devops.aliyuncs.com/storage/112oa28b0fce57d06fea1386164f4183f4df?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IiIsImV4cCI6MTY2OTk0NTcwNiwiaWF0IjoxNjY5MzQwOTA2LCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9hMjhiMGZjZTU3ZDA2ZmVhMTM4NjE2NGY0MTgzZjRkZiJ9.7UtbXd2E6tU6aSicO_LoEhQfPvViN2SCrF2-UdhkHyk)

mysql5.7执行结果:

![img](https://tcs-devops.aliyuncs.com/storage/112od2bd40102e4250e79caeae832dcacd77?Signature=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJBcHBJRCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9hcHBJZCI6IjVlNzQ4MmQ2MjE1MjJiZDVjN2Y5YjMzNSIsIl9vcmdhbml6YXRpb25JZCI6IjYxYmQ0MTk3YWM0MDEyNWFmMGY1ZmJlMyIsImV4cCI6MTY2OTk0OTIzMywiaWF0IjoxNjY5MzQ0NDMzLCJyZXNvdXJjZSI6Ii9zdG9yYWdlLzExMm9kMmJkNDAxMDJlNDI1MGU3OWNhZWFlODMyZGNhY2Q3NyJ9.0mgwZshZSKSxxkak2mJfj8LHrIyjVj2NTKghOVJ0-J0)



### delete、put 支持子查询

https://github.com/Tencent/APIJSON/issues/471

静态变量做全局处理，特殊接口用 Operation.MUST id/id{}/id{}@ 做自定义处理。

之所以默认必传，是因为安全意识不够、编码粗心大意的人太多了，所以要有一个底线保障，尽可能避免安全隐患。

1、全局配置 为 PUT, DELETE 强制要求必须有 id/id{}/id{}@ 条件

AbstractVerifier.IS_UPDATE_MUST_HAVE_ID_CONDITION = true; // true: 必须有

![image-20221126145415115](/Users/xy/Library/Application Support/typora-user-images/image-20221126145415115.png)

2、细粒度控制

![image-20221125154603033](/Users/xy/Library/Application Support/typora-user-images/image-20221125154603033.png)

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

![image-20221125161720199](/Users/xy/Library/Application Support/typora-user-images/image-20221125161720199.png)

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

![image-20221126145714808](/Users/xy/Library/Application Support/typora-user-images/image-20221126145714808.png)

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

![image-20221126151726450](/Users/xy/Library/Application Support/typora-user-images/image-20221126151726450.png)

#### bug修复

删除操作 主表 和 子查询 是同一张表
mysql8以下 非with-as表达式 会报错:
"msg": "You can't specify target table 'User' for update in FROM clause",

需要调整sql语句,将子查询包一层(select * from (子查询) as xxx)
DELETE FROM `housekeeping`.`User`
WHERE ( (`username` IN (SELECT * FROM (SELECT `username` FROM `housekeeping`.`User` WHERE ( (`username` = 'test1') )) as a) ) )

![image](https://user-images.githubusercontent.com/12228225/203517122-3d5b3b90-9780-4e05-b633-b264c575757a.png)

![image](https://user-images.githubusercontent.com/12228225/203517315-4ab11545-4285-4737-92a3-cfd1494e2652.png)

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

![image-20221125161821101](/Users/xy/Library/Application Support/typora-user-images/image-20221125161821101.png)