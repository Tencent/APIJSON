create table "Function"
(
    id          bigint       not null
        constraint "Function_pkey"
            primary key,
    name        varchar(20)  not null,
    arguments   varchar(100),
    demo        text         not null,
    detail      varchar(1000),
    date        timestamp(6) not null,
    back        varchar(45),
    requestlist varchar(45),
    "userId"    bigint      default 0,
    type        varchar(45) default 'Object'::character varying
);

comment on table "Function" is '远程函数。强制在启动时校验所有demo是否能正常运行通过';

comment on column "Function".name is '方法名';

comment on column "Function".arguments is '参数列表，每个参数的类型都是 String。
用 , 分割的字符串 比 [JSONArray] 更好，例如 array,item ，更直观，还方便拼接函数。';

comment on column "Function".demo is '可用的示例。';

comment on column "Function".detail is '详细描述';

comment on column "Function".date is '创建时间';

comment on column "Function".back is '返回类型';

comment on column "Function".requestlist is 'Request 的 id 列表';

comment on column "Function"."userId" is '用户id';

comment on column "Function".type is '返回类型';

alter table "Function"
    owner to postgres;

INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (3, 'countArray', 'array', '{"array": [1, 2, 3]}', '获取数组长度。没写调用键值对，会自动补全 "result()": "countArray(array)"', '2018-10-13 08:23:23.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (4, 'countObject', 'object', '{"object": {"key0": 1, "key1": 2}}', '获取对象长度。', '2018-10-13 08:23:23.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (5, 'isContain', 'array,value', '{"array": [1, 2, 3], "value": 2}', '判断是否数组包含值。', '2018-10-13 08:23:23.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (6, 'isContainKey', 'object,key', '{"key": "id", "object": {"id": 1}}', '判断是否对象包含键。', '2018-10-13 08:30:31.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (7, 'isContainValue', 'object,value', '{"value": 1, "object": {"id": 1}}', '判断是否对象包含值。', '2018-10-13 08:30:31.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (8, 'getFromArray', 'array,position', '{"array": [1, 2, 3], "result()": "getFromArray(array,1)"}', '根据下标获取数组里的值。position 传数字时直接作为值，而不是从所在对象 request 中取值', '2018-10-13 08:30:31.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (9, 'getFromObject', 'object,key', '{"key": "id", "object": {"id": 1}}', '根据键获取对象里的值。', '2018-10-13 08:30:31.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (10, 'deleteCommentOfMomen', 'momentId', '{"momentId": 1}', '根据动态 id 删除它的所有评论', '2019-08-17 18:46:56.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (11, 'verifyIdList', 'array', '{"array": [1, 2, 3], "result()": "verifyIdList(array)"}', '校验类型为 id 列表', '2019-08-17 19:58:33.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (12, 'verifyURLList', 'array', '{"array": ["http://123.com/1.jpg", "http://123.com/a.png", "http://www.abc.com/test.gif"], "result()": "verifyURLList(array)"}', '校验类型为 URL 列表', '2019-08-17 19:58:33.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (13, 'getWithDefault', 'value,defaultValue', '{"value": null, "defaultValue": 1}', '如果 value 为 null，则返回 defaultValue', '2019-08-20 15:26:36.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (14, 'removeKey', 'key', '{"key": "s", "key2": 2}', '从对象里移除 key', '2019-08-20 15:26:36.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (15, 'getFunctionDemo', null, '{}', '获取远程函数的 Demo', '2019-08-20 15:26:36.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (16, 'getFunctionDetail', null, '{}', '获取远程函数的详情', '2019-08-20 15:26:36.000000', null, null, 0, 'Object');