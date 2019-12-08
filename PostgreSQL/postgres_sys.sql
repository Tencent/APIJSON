create table "_Visit"
(
    model   varchar(15)  not null,
    id      bigint       not null,
    operate smallint     not null,
    date    timestamp(6) not null
);

comment on column "_Visit".operate is '1-增
2-删
3-改
4-查';

alter table "_Visit"
    owner to postgres;


create table "Access"
(
    id     integer                                                                                 not null
        constraint access_pk
            primary key,
    debug  integer     default 0                                                                   not null,
    name   varchar(50) default '实际表名，例如 apijson_user'::character varying                           not null,
    alias  text,
    get    text        default '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]'::text not null,
    head   text        default '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]'::text not null,
    gets   text        default '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]'::text            not null,
    heads  text        default '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]'::text            not null,
    post   text        default '["OWNER", "ADMIN"]'::text                                          not null,
    put    text        default '["OWNER", "ADMIN"]'::text                                          not null,
    delete text        default '["OWNER", "ADMIN"]'::text                                          not null,
    date   text        default CURRENT_TIMESTAMP                                                   not null
);

comment on column "Access".id is '唯一标识';

comment on column "Access".debug is '是否为调试表，只允许在开发环境使用，测试和线上环境禁用';

comment on column "Access".alias is '外部调用的表别名，例如 User';

comment on column "Access".get is '允许 get 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]
用 JSON 类型不能设置默认值，反正权限对应的需求是明确的，也不需要自动转 JSONArray。
TODO: 直接 LOGIN,CONTACT,CIRCLE,OWNER 更简单，反正是开发内部用，不需要复杂查询。';

comment on column "Access".head is '允许 head 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]';

comment on column "Access".gets is '允许 gets 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]';

comment on column "Access".heads is '允许 heads 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]';

comment on column "Access".post is '允许 post 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]';

comment on column "Access".put is '允许 put 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]';

comment on column "Access".delete is '允许 delete 的角色列表，例如 ["LOGIN", "CONTACT", "CIRCLE", "OWNER"]';

comment on column "Access".date is '创建时间';

alter table "Access"
    owner to postgres;

create unique index access_alias_uindex
    on "Access" (alias);

INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (1, 1, 'Access', '', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2019-07-21 12:21:36');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (2, 1, 'Table', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2018-11-28 16:38:14');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (3, 1, 'Column', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2018-11-28 16:38:14');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (4, 1, 'Function', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2018-11-28 16:38:15');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (5, 1, 'Request', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2018-11-28 16:38:14');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (6, 1, 'Response', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2018-11-28 16:38:15');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (7, 1, 'Document', null, '["LOGIN", "ADMIN"]', '["LOGIN", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["LOGIN", "ADMIN"]', '["OWNER", "ADMIN"]', '2018-11-28 16:38:15');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (8, 1, 'TestRecord', null, '["LOGIN", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '2018-11-28 16:38:15');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (9, 0, 'Test', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2018-11-28 16:38:15');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (10, 1, 'PgAttribute', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2018-11-28 16:38:14');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (11, 1, 'PgClass', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[]', '[]', '[]', '2018-11-28 16:38:14');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (12, 0, 'Login', null, '[]', '[]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[ "ADMIN"]', '[ "ADMIN"]', '["ADMIN"]', '2018-11-28 16:29:48');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (13, 0, 'Verify', null, '[]', '[]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '[ "ADMIN"]', '["ADMIN"]', '2018-11-28 16:29:48');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (14, 0, 'apijson_user', 'User', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN","LOGIN","OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["ADMIN"]', '2018-11-28 16:28:53');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (15, 0, 'apijson_privacy', 'Privacy', '[]', '[]', '["OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["UNKNOWN","LOGIN","OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["ADMIN"]', '2018-11-28 16:29:48');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (16, 0, 'Moment', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '2018-11-28 16:29:19');
INSERT INTO sys."Access" (id, debug, name, alias, get, head, gets, heads, post, put, delete, date) VALUES (17, 0, 'Comment', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '["OWNER", "ADMIN"]', '2018-11-28 16:29:19');
create table apijson_privacy
(
    id             bigint         not null
        constraint apijson_privacy_pkey
            primary key,
    certified      smallint       not null,
    phone          varchar(64)    not null,
    balance        numeric(10, 2) not null,
    _password      varchar(20)    not null,
    "_payPassword" varchar(32)    not null
);

comment on table apijson_privacy is '用户隐私信息表。
对安全要求高，不想泄漏真实名称。对外名称为 Privacy';

comment on column apijson_privacy.id is '唯一标识';

comment on column apijson_privacy.certified is '已认证';

comment on column apijson_privacy.phone is '手机号，仅支持 11 位数的。不支持 +86 这种国家地区开头的。如果要支持就改为 VARCHAR(14)';

comment on column apijson_privacy.balance is '余额';

comment on column apijson_privacy._password is '登录密码';

comment on column apijson_privacy."_payPassword" is '支付密码';

alter table apijson_privacy
    owner to postgres;

create index "phone_UNIQUE"
    on apijson_privacy (phone);

INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (38710, 1, '13000038710', 33376.00, 'apijson', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (70793, 0, '13000070793', 56000.00, 'apijson', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82001, 1, '13000082001', 98826.90, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82002, 1, '13000082002', 6817.23, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82003, 1, '13000082003', 2000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82004, 0, '13000082004', 2000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82005, 0, '13000082005', 1923.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82006, 0, '13000082006', 2000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82009, 0, '13000082009', 2000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82012, 0, '13000082012', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82020, 0, '12345678900', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82021, 0, '12345678901', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82022, 0, '12345678902', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82023, 0, '12345678903', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82024, 0, '12345678904', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82025, 0, '12345678905', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82026, 0, '12345678906', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82027, 0, '12345678907', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82028, 0, '12345678908', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82029, 0, '12345678909', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82030, 0, '12345678910', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82031, 0, '12345678911', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82032, 0, '12345678912', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82033, 0, '12345678913', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82034, 0, '12345678914', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82035, 0, '12345678915', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82036, 0, '12345678916', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82037, 0, '12345678917', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82038, 0, '12345678918', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82039, 0, '12345678919', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82040, 0, '13000082019', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82041, 0, '13000082015', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82042, 0, '13000082016', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82043, 0, '13000082017', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82044, 0, '13000082018', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82045, 0, '13000082020', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82046, 0, '13000082010', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82047, 0, '13000082021', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82048, 0, '13000038711', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82049, 0, '13000038712', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82050, 0, '13000038713', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82051, 0, '13000038714', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82052, 0, '13000038715', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82053, 0, '13000038720', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82054, 0, '13000038721', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82055, 0, '13000082030', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82056, 0, '13000082040', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82057, 0, '13000038730', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82058, 0, '13000038750', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82059, 0, '13000082033', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82060, 0, '13000082050', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (90814, 1, '13000090814', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (93793, 1, '13000093793', 3000.00, 'apijson', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (93794, 0, '99999999999', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490109742863, 0, '13000082100', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490109845208, 0, '13000082101', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490420651686, 0, '13000038716', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490427139175, 0, '13000038717', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490427577823, 0, '13000082102', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490584952968, 0, '13000038790', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490973670928, 0, '13000082051', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1492936169722, 0, '13000093794', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493480142628, 0, '13000038888', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493747512860, 0, '13000038777', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493747777770, 0, '13000038778', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493748594003, 0, '13000038779', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493748615711, 0, '13000038780', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493749090643, 0, '13000038781', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493836043151, 0, '13000038999', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493883110132, 0, '13000039999', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493890214167, 0, '13000031000', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493890303473, 0, '13000031001', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493890303474, 0, '13000088888', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1497792972314, 0, '13000082111', 0.00, '654321', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1499057230629, 0, '13000082011', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1500825221910, 0, '13000099999', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1502639062900, 0, '13000082222', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1502639424119, 0, '13000082333', 0.00, '12345678', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1507220582167, 0, '13000011111', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1508072071492, 0, '13000071492', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1508072105320, 0, '13000082008', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1508072160401, 0, '13000082007', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1508072202871, 0, '13000082031', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1510495628760, 0, '13000082000', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1511407581570, 0, '17610725819', 0.00, '123123', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1511761906715, 0, '13708222312', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1511965911349, 0, '13000083333', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1512387063078, 0, '15858585858', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1512531601485, 0, '18210847727', 0.00, '5816136', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1514623064133, 0, '13000038725', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1514625918255, 0, '13000038726', 255.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1514626163032, 0, '13000038727', 4951.37, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1514858422969, 0, '13000082041', 164.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1515565976140, 0, '15009257563', 0.00, 'qazwsx', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1518218350585, 0, '18663689263', 0.00, 'cherish751220', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1519778917280, 0, '15000536915', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1520242280259, 0, '18917212395', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1521274648008, 0, '18989491914', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1521371722416, 0, '13000088889', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1521374327542, 0, '13000056789', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1523626157302, 0, '15603313259', 0.00, '15603313259', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1523935772553, 0, '15603313258', 0.00, '15603313258', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1524042900591, 0, '15222297100', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1524298730523, 0, '17854217949', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1524878698882, 0, '13917451840', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1525677515673, 0, '13390935538', 10000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1527495857924, 0, '13142033345', 15.00, 'qweasd', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1527498229991, 0, '13142033342', 0.00, 'qweasd', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1527821445610, 0, '13142033346', 0.00, 'qweasd', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528250827953, 0, '15122820115', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528254173621, 0, '15225556855', 200.00, 'lmt970208', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528255497767, 0, '15822798927', 0.00, '111111', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528264711016, 0, '15620878773', 0.00, '111111', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528339692804, 0, '15122541683', 0.00, '568599', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528344980598, 0, '15188899797', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528356470041, 0, '15620878772', 0.00, '111111', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1531969715979, 0, '13800138000', 10000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1532188114543, 0, '13977757845', 20360.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1532439021068, 0, '18779607703', 0.00, '15879684798qq', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1533835176109, 0, '13977757846', 1700.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1534926301956, 0, '17602120205', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1538504264944, 0, '13000087654', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1538504500574, 0, '13000087655', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1538987952996, 0, '18662327672', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1541583762603, 0, '18689846285', 0.00, 'jyt123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1544276209348, 0, '13000087656', 1050.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1544503822963, 0, '13000082968', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1545707526805, 0, '13533039558', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1545895694424, 0, '13533039550', 357.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1547177436600, 0, '18980210241', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1548068043688, 0, '17181595855', 0.00, '0812563993gg', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1553095415917, 0, '13185236871', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1553527700480, 0, '13189758117', 0.00, '3802489', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1559129626356, 0, '13000000000', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1560409157504, 0, '18030546471', 0.00, '123456789', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1561539257158, 0, '15870873323', 0.00, '123qwe', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1563605318975, 0, '13590330481', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1566064943195, 0, '13111182001', 0.00, '6733', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1566065271307, 0, '13111182002', 0.00, '6733', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1566065319823, 0, '13111182003', 0.00, '6733', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1566065621308, 0, '13111182008', 0.00, '6733', '123456');
create table apijson_user
(
    id              bigint   not null
        constraint apijson_user_pkey
            primary key,
    sex             smallint not null,
    name            varchar(20),
    tag             varchar(45),
    head            varchar(300),
    "contactIdList" jsonb,
    "pictureList"   jsonb,
    date            timestamp(6)
);

comment on table apijson_user is '用户公开信息表。
对安全要求高，不想泄漏真实名称。对外名称为 User';

comment on column apijson_user.id is '唯一标识';

comment on column apijson_user.sex is '性别：
0-男
1-女';

comment on column apijson_user.name is '名称';

comment on column apijson_user.tag is '标签';

comment on column apijson_user.head is '头像url';

comment on column apijson_user."contactIdList" is '联系人id列表';

comment on column apijson_user."pictureList" is '照片列表';

comment on column apijson_user.date is '创建日期';

alter table apijson_user
    owner to postgres;

INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (38710, 0, 'TommyLemon', 'Android&Java', 'http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000', '[82003, 82005, 90814, 82004, 82009, 82002, 82044, 93793, 70793]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1511407581570, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82002, 82003, 82005, 82006, 82021, 82023, 82036, 82033]', '[]', '2017-11-23 03:26:21.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82001, 1, '测试账号', 'Dev', 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82012, 82003, 93794, 82006, 38710, 82004]', '["http://common.cnblogs.com/images/icon_weibo_24.png"]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1532439021068, 0, 'huxiaofan', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[38710, 82002, 82003, 82006, 82021]', null, '2018-07-24 13:30:21.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82045, 0, 'Green', null, 'http://common.cnblogs.com/images/wechat.png', '[82001, 82002, 82003, 1485246481130]', '[]', '2017-03-04 10:22:39.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82005, 1, 'Jan', 'AG', 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82001, 38710, 1532439021068]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82046, 0, 'Team', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[38710, 82002, 1485246481130]', '[]', '2017-03-04 15:11:17.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1534926301956, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82003, 82002, 82025]', null, '2018-08-22 08:25:01.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1525677515673, 0, 'APIJSONUser', null, 'http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000', '[82003, 82002, 38710]', null, '2018-05-07 07:18:35.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82002, 1, 'Happy~', 'iOS', 'http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000', '[82005, 82001, 38710]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82012, 0, 'Steve', 'FEWE', 'http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000', '[82004, 82002, 93793]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1531969715979, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82002, 82003, 82005]', null, '2018-07-19 03:08:35.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82043, 0, 'Holiday', null, 'http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000', '[70793, 82006]', '[]', '2017-03-04 10:05:04.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1528264711016, 0, '梦', null, 'http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000', '[82021, 1528250827953]', null, '2018-06-06 05:58:31.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1544276209348, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82002, 38710]', null, '2018-12-08 13:36:49.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82003, 1, 'Wechat', null, 'http://common.cnblogs.com/images/wechat.png', '[82001, 93793]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82041, 0, 'Holo', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[38710, 82001]', '[]', '2017-03-04 09:59:34.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82058, 0, 'StupidBird', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82001, 82002]', '[]', '2017-03-12 11:23:04.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82055, 1, 'Solid', null, 'http://static.oschina.net/uploads/user/19/39085_50.jpg', '[38710, 82002]', '[]', '2017-03-11 15:04:00.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (70793, 0, 'Strong', 'djdj', 'http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000', '[38710, 82002]', '["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg", "http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg", "https://camo.githubusercontent.com/788c0a7e11a", "https://camo.githubusercontent.com/f513f67"]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1544503822963, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[93793, 82003]', null, '2018-12-11 04:50:22.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1514625918255, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82002, 93793]', null, '2017-12-30 09:25:18.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1527495857924, 0, 'account', null, 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2510057322,2452415311&fm=27&gp=0.jpg', '[1527821445610, 82012]', null, '2018-05-28 08:24:17.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1528339692804, 1, '568599', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[1528250827953, 1528264711016]', null, '2018-06-07 02:48:12.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1524042900591, 1, '哈哈哈', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82002, 82003]', null, '2018-04-18 09:15:00.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1514858422969, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[93793, 82056]', null, '2018-01-02 02:00:22.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1490973670928, 1, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[70793, 93793]', '[]', '2017-03-31 15:21:10.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1490427139175, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[38710, 70793]', '[]', '2017-03-25 07:32:19.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1515565976140, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82003, 82021]', null, '2018-01-10 06:32:56.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1523626157302, 1, 'Charlie_brown', '', 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[1523935772553, 93793]', null, '2018-04-13 13:29:17.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1528254173621, 1, 'A', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82001, 38710]', null, '2018-06-06 03:02:53.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1523935772553, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[1523626157302]', null, '2018-04-17 03:29:32.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1512531601485, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82001]', '[]', '2017-12-06 03:40:01.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1553095415917, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82001]', null, '2019-03-20 15:23:35.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1528255497767, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82002]', null, '2018-06-06 03:24:57.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1520242280259, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[82002]', null, '2018-03-05 09:31:20.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1528250827953, 1, 'limengt', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[1528264711016]', null, '2018-06-06 02:07:07.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1527821445610, 0, 'accountt', null, 'http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000', '[1527495857924]', null, '2018-06-01 02:50:45.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82044, 1, 'Love', null, 'http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000', '[82006]', '[]', '2017-03-04 10:20:27.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1527498229991, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[1499057230629]', null, '2018-05-28 09:03:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1528356470041, 0, 'aaaa', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[1528339692804]', null, '2018-06-07 07:27:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82040, 1, 'Dream', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[70793]', '[]', '2017-03-02 16:44:26.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1490420651686, 1, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[70793]', '[]', '2017-03-25 05:44:11.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1559129626356, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[1507220582167]', null, '2019-05-29 11:33:46.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1533835176109, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[1532188114543]', null, '2018-08-09 17:19:36.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1490109742863, 1, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-03-21 15:22:22.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1500825221910, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-07-23 15:53:41.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493890214167, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-04 09:30:14.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1507220582167, 0, 'APIJSONUser', '通过APIJSONAuto的图像化界面注册，按Enter而不是Register', 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-10-05 16:23:02.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1497792972314, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-06-18 13:36:12.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1511761906715, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-11-27 05:51:46.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1524298730523, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', null, '2018-04-21 08:18:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493747512860, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-02 17:51:52.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493748615711, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-02 18:10:15.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493480142628, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-04-29 15:35:42.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1499057230629, 0, '一二三', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-07-03 04:47:10.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1512387063078, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-12-04 11:31:03.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1511965911349, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-11-29 14:31:51.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1508072071492, 0, '赵钱孙李', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-10-15 12:54:31.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493749090643, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-02 18:18:10.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493748594003, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-02 18:09:54.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82004, 0, 'Tommy', 'fasef', 'http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82006, 1, 'Meria', null, 'http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82009, 0, 'God', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (93793, 0, 'Mike', 'GES', 'http://static.oschina.net/uploads/user/48/96331_50.jpg', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (93794, 0, 'Lemon', null, 'http://static.oschina.net/uploads/user/48/97721_50.jpg?t=1451544779000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82020, 0, 'ORANGE', null, 'http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82021, 1, 'Tommy', null, 'http://static.oschina.net/uploads/user/19/39085_50.jpg', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82022, 0, 'Internet', null, 'http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82023, 0, 'No1', null, 'http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1508072105320, 1, '周吴郑王', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-10-15 12:55:05.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82024, 0, 'Lemon', null, 'http://static.oschina.net/uploads/user/427/855532_50.jpg?t=1435030876000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82025, 1, 'Tommy', null, 'http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82026, 0, 'iOS', null, 'http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82027, 0, 'Yong', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82028, 1, 'gaeg', null, 'http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82029, 0, 'GASG', null, 'http://common.cnblogs.com/images/wechat.png', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82030, 1, 'Fun', null, 'http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82031, 0, 'Lemon', null, 'http://static.oschina.net/uploads/user/48/96331_50.jpg', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82032, 0, 'Stack', 'fasdg', 'http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82033, 1, 'GAS', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82034, 1, 'Jump', null, 'http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82035, 1, 'Tab', null, 'http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82036, 0, 'SAG', null, 'http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1502639062900, 0, 'TESLA', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-08-13 15:44:22.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82037, 0, 'Test', null, 'http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82038, 0, 'Battle', null, 'http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1502639424119, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-08-13 15:50:24.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82039, 1, 'Everyday', null, 'http://common.cnblogs.com/images/icon_weibo_24.png', '[]', '[]', '2017-02-19 13:57:56.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1508072202871, 0, '七八九十', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-10-15 12:56:42.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82042, 1, 'Why', null, 'http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000', '[]', '[]', '2017-03-04 10:04:33.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82047, 0, 'Tesla', null, 'http://common.cnblogs.com/images/wechat.png', '[]', '[]', '2017-03-04 16:02:05.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82048, 0, 'Moto', null, 'http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000', '[]', '[]', '2017-03-04 16:04:02.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82049, 0, 'ITMan', null, 'http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000', '[]', '[]', '2017-03-05 09:51:51.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82050, 0, 'Parl', null, 'http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000', '[]', '[]', '2017-03-05 09:52:52.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82051, 0, 'Girl', null, 'http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000', '[]', '[]', '2017-03-05 09:53:37.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82052, 0, 'Unbrella', null, 'http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000', '[]', '[]', '2017-03-05 09:57:54.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82053, 0, 'Alice', null, 'http://common.cnblogs.com/images/wechat.png', '[]', '[]', '2017-03-05 15:25:42.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82054, 0, 'Harvey', null, 'http://static.oschina.net/uploads/user/19/39085_50.jpg', '[]', '[]', '2017-03-06 12:29:03.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82056, 1, 'IronMan', null, 'http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000', '[]', '[]', '2017-03-11 15:32:25.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1490584952968, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-03-27 03:22:32.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82057, 0, 'NullPointerExeption', null, 'http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000', '[]', '[]', '2017-03-12 06:01:23.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82059, 1, 'He&She', null, 'http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000', '[]', '[]', '2017-03-19 14:49:15.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (82060, 1, 'Anyway~', null, 'http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000', '[]', '[]', '2017-03-21 14:10:18.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493836043151, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-03 18:27:23.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1508072160401, 0, '四五六', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-10-15 12:56:00.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (90814, 0, '007', null, 'http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000', '[]', '[]', '2017-02-01 11:21:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1510495628760, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-11-12 14:07:08.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1490109845208, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-03-21 15:24:05.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1490427577823, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-03-25 07:39:37.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493747777770, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-02 17:56:17.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493890303473, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-04 09:31:43.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493890303474, 0, 'Test Post', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-06-12 15:50:44.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1493883110132, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-05-04 07:31:50.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1492936169722, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', '[]', '[]', '2017-04-23 08:29:29.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1553527700480, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-03-25 15:28:20.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1566065271307, 0, 'new APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-08-17 18:08:04.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1566065621308, 0, 'new APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-08-17 18:13:55.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1563605318975, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-07-20 06:48:38.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1514623064133, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2017-12-30 08:37:44.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1561539257158, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-06-26 08:54:17.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1514626163032, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2017-12-30 09:29:23.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1545895694424, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-12-27 07:28:14.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1566064943195, 0, 'new APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-08-17 18:02:36.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1538504500574, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-10-02 18:21:40.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1538987952996, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-10-08 08:39:13.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1532188114543, 0, '宁旭', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-07-21 15:48:34.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1528344980598, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-06-07 04:16:20.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1560409157504, 0, '上邪', null, '最好的时光', null, null, '2019-06-13 06:59:17.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1524878698882, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-04-28 01:24:58.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1566065319823, 0, 'new APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-08-17 18:08:53.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1545707526805, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-12-25 03:12:06.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1519778917280, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-02-28 00:48:37.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1548068043688, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-01-21 10:54:03.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1541583762603, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-11-07 09:42:42.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1521371722416, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-03-18 11:15:22.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1547177436600, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2019-01-11 03:30:36.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1521274648008, 0, 'Kiro', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-03-17 08:17:28.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1538504264944, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-10-02 18:17:44.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1521374327542, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-03-18 11:58:47.000000');
INSERT INTO sys.apijson_user (id, sex, name, tag, head, "contactIdList", "pictureList", date) VALUES (1518218350585, 0, 'APIJSONUser', null, 'https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png', null, null, '2018-02-09 23:19:10.000000');
create table "Comment"
(
    id         bigint           not null
        constraint "Comment_pkey"
            primary key,
    "toId"     bigint default 0 not null,
    "userId"   bigint           not null,
    "momentId" bigint           not null,
    date       timestamp(6),
    content    varchar(1000)    not null
);

comment on table "Comment" is '评论';

comment on column "Comment".id is '唯一标识';

comment on column "Comment"."toId" is '被回复的id';

comment on column "Comment"."userId" is '评论人id';

comment on column "Comment"."momentId" is '动态id';

comment on column "Comment".date is '创建日期';

comment on column "Comment".content is '内容';

alter table "Comment"
    owner to postgres;

INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (4, 0, 38710, 470, '2017-02-01 11:20:50.000000', 'This is a Content...-4');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (13, 0, 82005, 58, '2017-02-01 11:20:50.000000', 'This is a Content...-13');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (22, 221, 82001, 470, '2017-02-01 11:20:50.000000', '测试修改评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (44, 0, 82003, 170, '2017-02-01 11:20:50.000000', 'This is a Content...-44');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (45, 0, 93793, 301, '2017-02-01 11:20:50.000000', 'This is a Content...-45');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (47, 4, 70793, 470, '2017-02-01 11:20:50.000000', 'This is a Content...-47');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (51, 45, 82003, 301, '2017-02-01 11:20:50.000000', 'This is a Content...-51');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (54, 0, 82004, 170, '2017-02-01 11:20:50.000000', 'This is a Content...-54');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (68, 0, 82005, 371, '2017-02-01 11:20:50.000000', 'This is a Content...-68');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (76, 45, 93793, 301, '2017-02-01 11:20:50.000000', 'This is a Content...-76');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (77, 13, 93793, 58, '2017-02-01 11:20:50.000000', 'This is a Content...-77');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (97, 13, 82006, 58, '2017-02-01 11:20:50.000000', 'This is a Content...-97');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (99, 44, 70793, 170, '2017-02-01 11:20:50.000000', 'This is a Content...-99');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (110, 0, 93793, 371, '2017-02-01 11:23:24.000000', 'This is a Content...-110');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (114, 0, 82001, 371, '2017-03-02 05:56:06.000000', 'This is a Content...-114');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (115, 0, 38710, 371, '2017-03-02 05:56:06.000000', 'This is a Content...-115');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (116, 0, 70793, 371, '2017-03-02 05:56:06.000000', 'This is a Content...-116');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (120, 0, 93793, 301, '2017-03-02 05:56:06.000000', 'This is a Content...-110');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (124, 0, 82001, 301, '2017-03-02 05:56:06.000000', 'This is a Content...-114');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (157, 0, 93793, 371, '2017-02-01 11:20:50.000000', 'This is a Content...-157');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (158, 0, 93793, 301, '2018-07-12 17:28:23.000000', 'This is a Content...-157');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (160, 0, 82001, 235, '2017-03-02 05:56:06.000000', 'This is a Content...-160');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (162, 0, 93793, 12, '2017-03-06 05:03:45.000000', 'This is a Content...-162');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (163, 0, 82001, 235, '2017-03-02 05:56:06.000000', 'This is a Content...-163');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (164, 0, 93793, 12, '2017-03-06 05:03:45.000000', 'This is a Content...-164');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (167, 0, 82001, 58, '2017-03-25 11:48:41.000000', 'Nice!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (168, 1490442545077, 82001, 235, '2017-03-25 11:49:14.000000', '???');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (172, 162, 82001, 12, '2017-03-25 12:22:58.000000', 'OK');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (173, 0, 38710, 58, '2017-03-25 12:25:13.000000', 'Good');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (175, 0, 38710, 12, '2017-03-25 12:26:53.000000', 'Java is the best program language!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (176, 166, 38710, 15, '2017-03-25 12:28:03.000000', 'thank you');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (178, 0, 38710, 511, '2017-03-25 12:30:55.000000', 'wbw');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (182, 110, 82001, 371, '2017-03-26 06:12:52.000000', 'hahaha');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (188, 97, 82001, 58, '2017-03-26 07:21:32.000000', '1646');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (190, 0, 82001, 58, '2017-03-26 07:22:13.000000', 'dbdj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (206, 54, 82001, 170, '2017-03-29 03:04:23.000000', 'ejej');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (209, 13, 82001, 58, '2017-03-29 03:05:59.000000', 'hehj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (300, 97, 82001, 58, '2017-03-29 03:06:07.000000', 'hj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (301, 194, 82001, 235, '2017-03-29 03:06:24.000000', 'jj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (4001, 0, 82001, 58, '2017-03-29 08:39:52.000000', 'I would like to say …');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490776944301, 0, 82001, 58, '2017-03-29 08:42:24.000000', 'hello');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490776966828, 173, 82001, 58, '2017-03-29 08:42:46.000000', 'me too');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490777905437, 0, 82001, 543, '2017-03-29 08:58:25.000000', 'rr');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490778122719, 175, 82001, 12, '2017-03-29 09:02:02.000000', 'Yeah! I think so!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490778494751, 1490778122719, 82001, 12, '2017-03-29 09:08:14.000000', 'reply Android82001');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490778681337, 166, 82001, 12, '2017-03-29 09:11:21.000000', 'gg');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490780759866, 99, 82001, 170, '2017-03-29 09:45:59.000000', '99');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490781009548, 51, 82001, 301, '2017-03-29 09:50:09.000000', '3');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490781032005, 45, 82001, 301, '2017-03-29 09:50:32.000000', '93793');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490781817044, 209, 38710, 58, '2017-03-29 10:03:37.000000', '82001');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490781850893, 1490776966828, 38710, 58, '2017-03-29 10:04:10.000000', 'haha!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490781857242, 190, 38710, 58, '2017-03-29 10:04:17.000000', 'nice');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490781865407, 1490781857242, 38710, 58, '2017-03-29 10:04:25.000000', 'wow');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490781899147, 197, 38710, 12, '2017-03-29 10:04:59.000000', 'kaka');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490794439561, 1490778681337, 82001, 12, '2017-03-29 13:33:59.000000', 'gg?');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490794610632, 172, 82001, 12, '2017-03-29 13:36:50.000000', 'All right');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490794937137, 1490794919957, 82001, 12, '2017-03-29 13:42:17.000000', 'All right ok ok');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490794953438, 1490794937137, 82001, 12, '2017-03-29 13:42:33.000000', 'All right ok ok ll');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490796241178, 0, 38710, 58, '2017-03-29 14:04:01.000000', 'Anything else?');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490796629591, 175, 38710, 12, '2017-03-29 14:10:29.000000', 'well');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490798710678, 110, 38710, 371, '2017-03-29 14:45:10.000000', '110');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490800971064, 175, 38710, 12, '2017-03-29 15:22:51.000000', 'I do');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490848396072, 175, 82001, 12, '2017-03-30 04:33:16.000000', 'Lemon');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490848581424, 166, 82001, 12, '2017-03-30 04:36:21.000000', '82001ejej');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490850764448, 162, 82001, 12, '2017-03-30 05:12:44.000000', '-162');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490850844016, 0, 82001, 12, '2017-03-30 05:14:04.000000', 'I like it');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490850876656, 1490800971064, 82001, 12, '2017-03-30 05:14:36.000000', 'I do so');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490854894566, 175, 82001, 12, '2017-03-30 06:21:34.000000', 'it does be a good lang');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863443219, 1490850844016, 82002, 12, '2017-03-30 08:44:03.000000', 'me too!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863469638, 0, 82002, 15, '2017-03-30 08:44:29.000000', 'Just do it');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863507114, 4, 82003, 470, '2017-03-30 08:45:07.000000', 'yes');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863563124, 0, 82003, 704, '2017-03-30 08:46:03.000000', 'I want one');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863651493, 0, 70793, 595, '2017-03-30 08:47:31.000000', 'wow');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863661426, 1490780759866, 70793, 170, '2017-03-30 08:47:41.000000', '66');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863676989, 0, 70793, 12, '2017-03-30 08:47:56.000000', 'Shy');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863711703, 0, 70793, 511, '2017-03-30 08:48:31.000000', 'I hope I can join');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863717947, 178, 70793, 511, '2017-03-30 08:48:37.000000', 'what?');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863783276, 1490863711703, 93793, 511, '2017-03-30 08:49:43.000000', 'haha welcome');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863903900, 0, 82006, 470, '2017-03-30 08:51:43.000000', 'SOGA');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863915675, 0, 82006, 235, '2017-03-30 08:51:55.000000', 'Good boy');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863938712, 0, 82006, 12, '2017-03-30 08:52:18.000000', 'Handsome!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490863978239, 1490796241178, 82006, 58, '2017-03-30 08:52:58.000000', 'there still remains a question…');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864016738, 0, 82006, 511, '2017-03-30 08:53:36.000000', 'I want to have a try!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864023700, 0, 82006, 543, '2017-03-30 08:53:43.000000', 'oops');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864039264, 0, 82006, 551, '2017-03-30 08:53:59.000000', 'Wonderful!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864152008, 0, 82006, 58, '2017-03-30 08:55:52.000000', 'U R ugly( ´?` )');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864254400, 1490863915675, 82044, 235, '2017-03-30 08:57:34.000000', 'And I have no idea');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864276824, 0, 82044, 12, '2017-03-30 08:57:56.000000', 'Oh my God!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864292184, 1490864152008, 82044, 58, '2017-03-30 08:58:12.000000', 'haha!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864379424, 1490863938712, 82001, 12, '2017-03-30 08:59:39.000000', 'Thank you~');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490864400210, 1490864276824, 82001, 12, '2017-03-30 09:00:00.000000', 'Amazing, isnt it?');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490874908570, 1490864023700, 82055, 543, '2017-03-30 11:55:08.000000', 'yeah');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490874930994, 1490777905437, 82055, 543, '2017-03-30 11:55:30.000000', 'yy');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490874968779, 0, 82055, 12, '2017-03-30 11:56:08.000000', 'I love it');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490875033494, 0, 82055, 301, '2017-03-30 11:57:13.000000', 'More Comments');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490875040761, 158, 82055, 301, '2017-03-30 11:57:20.000000', '157');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490875046704, 120, 82055, 301, '2017-03-30 11:57:26.000000', '110');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490875660259, 1490863469638, 82055, 15, '2017-03-30 12:07:40.000000', 'I prove wht you said(??????)');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490879678127, 0, 82001, 543, '2017-03-30 13:14:38.000000', 'Baby you are a firework!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490973736662, 1490973715568, 70793, 170, '2017-03-31 15:22:16.000000', 'Hello, I am a fresh man');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1490973890875, 1490864039264, 93793, 551, '2017-03-31 15:24:50.000000', 'While I donot think so…');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491014830404, 1490864016738, 82001, 511, '2017-04-01 02:47:10.000000', 'Have a nice day!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491119615611, 1490864023700, 82001, 543, '2017-04-02 07:53:35.000000', '$$');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491119670185, 68, 82001, 371, '2017-04-02 07:54:30.000000', 'Leave a word');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491119695580, 0, 82001, 371, '2017-04-02 07:54:55.000000', 'leave a word');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491130701902, 0, 38710, 511, '2017-04-02 10:58:21.000000', 'Thanks for your supports (-^?^-)');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491209763162, 0, 82001, 1491200468898, '2017-04-03 08:56:03.000000', 'How do you do');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491277552385, 0, 82001, 58, '2017-04-04 03:45:52.000000', 'Seven');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491312438951, 1490863651493, 82001, 595, '2017-04-04 13:27:18.000000', 'WaKaKa!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491709064513, 0, 82001, 551, '2017-04-09 03:37:44.000000', 'soga');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491740899179, 0, 82001, 470, '2017-04-09 12:28:19.000000', 'www');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491798370749, 0, 82002, 551, '2017-04-10 04:26:10.000000', 'Nice!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491798499667, 115, 82002, 371, '2017-04-10 04:28:19.000000', 'I do not understand…');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1491830543193, 0, 82001, 170, '2017-04-10 13:22:23.000000', 'What is the hell?');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1492932228287, 1491209763162, 38710, 1491200468898, '2017-04-23 07:23:48.000000', 'fine,thanks');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1493094307810, 0, 82001, 551, '2017-04-25 04:25:04.000000', '删除或修改数据请先创建，不要动原来的，谢谢');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1493094307910, 0, 82001, 551, '2017-04-25 04:26:04.000000', '用POST新增数据');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1493186363132, 1490850764448, 82001, 12, '2017-04-26 05:59:23.000000', 'sndnd');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1502632433970, 0, 82002, 1493835799335, '2017-08-13 13:53:53.000000', 'just have fun!');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1508053783278, 0, 82001, 1508053762227, '2017-10-15 07:49:43.000000', '可以的');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1508072695833, 0, 82003, 1508072633830, '2017-10-15 13:04:55.000000', '心疼地抱住自己(๑´ㅂ`๑)');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1508227456407, 0, 82001, 15, '2017-10-17 08:04:16.000000', 'hsh');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1508227498578, 1491798370749, 82001, 551, '2017-10-17 08:04:58.000000', 'g');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1508462026394, 1490850844016, 82001, 12, '2017-10-20 01:13:46.000000', '欧');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1508492585904, 1508462026394, 82001, 12, '2017-10-20 09:43:05.000000', 'my god');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1509003045509, 0, 82001, 1508072633830, '2017-10-26 07:30:45.000000', 'hhh');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1509346549158, 0, 82001, 170, '2017-10-30 06:55:49.000000', '呵呵');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1509346556395, 0, 82001, 170, '2017-10-30 06:55:56.000000', '测试');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1509346606036, 0, 82001, 15, '2017-10-30 06:56:46.000000', '测');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1509518079106, 0, 82001, 1508073178489, '2017-11-01 06:34:39.000000', '哦哦哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1510795816462, 162, 82001, 12, '2017-11-16 01:30:16.000000', '赞');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1510795933629, 0, 82001, 1508073178489, '2017-11-16 01:32:13.000000', 'cc');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1510813284894, 0, 82001, 12, '2017-11-16 06:21:24.000000', 'asdasdasdas');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1510813295700, 162, 82001, 12, '2017-11-16 06:21:35.000000', 'adsdasdasdasd');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1511374269759, 99, 82001, 170, '2017-11-22 18:11:09.000000', '记录里');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1511374274194, 0, 82001, 170, '2017-11-22 18:11:14.000000', '哦哦哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1511407695342, 0, 1511407581570, 371, '2017-11-23 03:28:15.000000', '好的');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1511407702981, 157, 1511407581570, 371, '2017-11-23 03:28:22.000000', '你好');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1511878024415, 0, 1511761906715, 12, '2017-11-28 14:07:04.000000', '你今年');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1511878031610, 1511878024415, 1511761906715, 12, '2017-11-28 14:07:11.000000', '不鸟你');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1512035094555, 0, 82001, 12, '2017-11-30 09:44:54.000000', '呵呵呵');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1512035117021, 0, 82001, 32, '2017-11-30 09:45:17.000000', '图片看不了啊');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1512039030970, 1512035117021, 82001, 32, '2017-11-30 10:50:30.000000', '一般九宫格图片都是压缩图，分辨率在300*300左右，加载很快，点击放大后才是原图，1080P左右');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1512531859019, 0, 1512531601485, 1512314438990, '2017-12-06 03:44:19.000000', '666');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1512533520832, 1512531859019, 38710, 1512314438990, '2017-12-06 04:12:00.000000', '嘿嘿');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1513656045399, 0, 82001, 1508072633830, '2017-12-19 04:00:45.000000', '444444');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514425796195, 0, 82001, 1513094436910, '2017-12-28 01:49:56.000000', '一起');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514473034425, 1514425796195, 93793, 1513094436910, '2017-12-28 14:57:14.000000', '干啥？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514478784653, 0, 82001, 1513094436910, '2017-12-28 16:33:04.000000', 'bug很多');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514506206319, 1514478784653, 38710, 1513094436910, '2017-12-29 00:10:06.000000', '碰到哪些了呢？欢迎指出，尽快解决^_^');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514617131036, 0, 82005, 1513094436910, '2017-12-30 06:58:51.000000', '口子');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514858592813, 0, 82001, 1514858533480, '2018-01-02 02:03:12.000000', '铁人');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514858640958, 0, 38710, 1514858533480, '2018-01-02 02:04:00.000000', '斯塔克工业');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514858707767, 0, 70793, 1514858533480, '2018-01-02 02:05:07.000000', '壕友乎？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514960713300, 0, 82001, 1513094436910, '2018-01-03 06:25:13.000000', '1');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1514960744185, 1512531859019, 82001, 1512314438990, '2018-01-03 06:25:44.000000', '哇');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1515057852156, 0, 82001, 58, '2018-01-04 09:24:12.000000', '你说');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1515057857464, 0, 82001, 58, '2018-01-04 09:24:17.000000', '你说');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1515057861094, 0, 82001, 58, '2018-01-04 09:24:21.000000', '蓉蓉');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1515057864174, 1515057857464, 82001, 58, '2018-01-04 09:24:24.000000', '哦轻松');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1515057869554, 0, 82001, 58, '2018-01-04 09:24:29.000000', ',王者荣耀');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1515313792063, 162, 82001, 12, '2018-01-07 08:29:52.000000', 'you');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1515313823155, 164, 82001, 12, '2018-01-07 08:30:23.000000', 'you');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516190557098, 0, 82001, 1513094436910, '2018-01-17 12:02:37.000000', '哦婆婆');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516629533520, 0, 82001, 1508072633830, '2018-01-22 13:58:53.000000', '小臭臭');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516686985310, 0, 82001, 1516086423441, '2018-01-23 05:56:25.000000', 'hologram');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516687072270, 1516629533520, 82001, 1508072633830, '2018-01-23 05:57:52.000000', '咯我就');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516687437251, 1516686985310, 82001, 1516086423441, '2018-01-23 06:03:57.000000', '你家里好哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516691119239, 1516686985310, 38710, 1516086423441, '2018-01-23 07:05:19.000000', '我喜欢Hololens嘿嘿');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516780129884, 0, 82001, 1516086423441, '2018-01-24 07:48:49.000000', 'aaa');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516783920998, 0, 82001, 1513094436910, '2018-01-24 08:52:00.000000', '这个是实时的吗');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516785657724, 0, 82001, 1516086423441, '2018-01-24 09:20:57.000000', 'hj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516805226757, 1516785657724, 38710, 1516086423441, '2018-01-24 14:47:06.000000', '滑稽？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516805340593, 1516783920998, 38710, 1513094436910, '2018-01-24 14:49:00.000000', '看怎么定义 实时 。这个是仿微信朋友圈列表和QQ空间说说详情，在线同步的，但没做推送，所以不是QQ微信聊天那种即时通讯。');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516843720270, 1516780129884, 82001, 1516086423441, '2018-01-25 01:28:40.000000', 'ghj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516862537978, 1515057869554, 70793, 58, '2018-01-25 06:42:17.000000', '绝地逃亡吃鸡');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516931850067, 0, 82001, 1516086423441, '2018-01-26 01:57:30.000000', '1111111111111');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516951734010, 1514506206319, 82001, 1513094436910, '2018-01-26 07:28:54.000000', '火锅');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516951754620, 0, 82001, 1513094436910, '2018-01-26 07:29:14.000000', '凤飞飞刚刚好');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1516951826863, 0, 82001, 170, '2018-01-26 07:30:26.000000', '黑珍珠');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1517193267472, 1513656045399, 82001, 1508072633830, '2018-01-29 02:34:27.000000', '1');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1517193278459, 0, 82001, 1508072633830, '2018-01-29 02:34:38.000000', '112');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1517229342303, 0, 82001, 1516086423441, '2018-01-29 12:35:42.000000', '几号抢的');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1517234768450, 1517229342303, 93793, 1516086423441, '2018-01-29 14:06:08.000000', '9号');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1517303775429, 1490863903900, 82001, 470, '2018-01-30 09:16:15.000000', '？？？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1517375165233, 0, 82001, 1508053762227, '2018-01-31 05:06:05.000000', '666');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1517730034960, 0, 82001, 170, '2018-02-04 07:40:34.000000', '陌陌陌陌');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1518365470893, 44, 82001, 170, '2018-02-11 16:11:10.000000', '野蜂飞舞');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1518614899681, 0, 82001, 301, '2018-02-14 13:28:19.000000', 'https://goo.gl/search/JJB+Sports
JJB Sports,');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1519719341810, 0, 82001, 1516086423441, '2018-02-27 08:15:41.000000', '我也想抢一张');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1519793574249, 1519719341810, 93793, 1516086423441, '2018-02-28 04:52:54.000000', '哈哈，春运都过了啊');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1519813825959, 0, 82001, 1516086423441, '2018-02-28 10:30:25.000000', '距P民');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1519974842508, 0, 82001, 1516086423441, '2018-03-02 07:14:02.000000', '1111');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1519974868848, 1516691119239, 82001, 1516086423441, '2018-03-02 07:14:28.000000', '1111');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1519979533242, 0, 82001, 1508072633830, '2018-03-02 08:32:13.000000', 'hj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1520231250819, 0, 82001, 12, '2018-03-05 06:27:30.000000', '浑身难受呢');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1520264640815, 0, 70793, 1520242333325, '2018-03-05 15:44:00.000000', '兰博基尼');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1520330788006, 0, 1520242280259, 1514017444961, '2018-03-06 10:06:28.000000', '八组');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1520578883309, 0, 82001, 12, '2018-03-09 07:01:23.000000', '我用流量');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1520699466219, 1520578883309, 82001, 12, '2018-03-10 16:31:06.000000', '壕');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1522074343188, 1513656045399, 82001, 1508072633830, '2018-03-26 14:25:43.000000', 'rrrrr');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1522074360206, 1519979533242, 82001, 1508072633830, '2018-03-26 14:26:00.000000', 'tttt');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1522657767636, 120, 82001, 301, '2018-04-02 08:29:27.000000', '云画');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1522741138316, 1517193278459, 82001, 1508072633830, '2018-04-03 07:38:58.000000', '哦哦哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1522986959852, 1508072695833, 82001, 1508072633830, '2018-04-06 03:55:59.000000', '！？？？？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1523936378484, 0, 1523935772553, 1523936332614, '2018-04-17 03:39:38.000000', '不错不错哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524032180807, 1519719341810, 82001, 1516086423441, '2018-04-18 06:16:20.000000', '你好啊');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524032244441, 1519974842508, 82001, 1516086423441, '2018-04-18 06:17:24.000000', '干嘛，单身吗?');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524032255755, 1519974842508, 82001, 1516086423441, '2018-04-18 06:17:35.000000', '单身到底吗？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524032299622, 0, 82001, 1516086423441, '2018-04-18 06:18:19.000000', '别给我得怂');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524032305810, 1524032299622, 82001, 1516086423441, '2018-04-18 06:18:25.000000', '你好');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524178495587, 0, 1524042900591, 1524178455305, '2018-04-19 22:54:55.000000', '嘻嘻');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524178500568, 1524178495587, 1524042900591, 1524178455305, '2018-04-19 22:55:00.000000', '哈哈哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524190367904, 0, 38710, 1524178455305, '2018-04-20 02:12:47.000000', '你头像用的是本地的路径，只有你能看到，别人看不到哦，可以换一个url');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524190412418, 1524190367904, 38710, 1524178455305, '2018-04-20 02:13:32.000000', '我的资料>编辑>改下备注');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524190941111, 1524032244441, 82003, 1516086423441, '2018-04-20 02:22:21.000000', '单身约吗？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524190981549, 1522657767636, 82003, 301, '2018-04-20 02:23:01.000000', '这个6');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524191012552, 0, 82003, 1524178455305, '2018-04-20 02:23:32.000000', '早上好小姐姐');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524214012015, 1524190367904, 1524042900591, 1524178455305, '2018-04-20 08:46:52.000000', '怎么换url');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524235902970, 1524214012015, 82003, 1524178455305, '2018-04-20 14:51:42.000000', '在我的资料界面编辑备注');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524297798490, 0, 82001, 1513094436910, '2018-04-21 08:03:18.000000', 'gg');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524461430874, 1519979533242, 82001, 1508072633830, '2018-04-23 05:30:30.000000', '哦哦哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524461436914, 0, 82001, 1508072633830, '2018-04-23 05:30:36.000000', '莫');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524461441914, 0, 82001, 1508072633830, '2018-04-23 05:30:41.000000', '默默');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524488068926, 1524178500568, 82001, 1524178455305, '2018-04-23 12:54:28.000000', '哦哦哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524582671132, 1524461441914, 82003, 1508072633830, '2018-04-24 15:11:11.000000', '陌陌');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524582716289, 1524461441914, 70793, 1508072633830, '2018-04-24 15:11:56.000000', '脉脉');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524798402799, 0, 1523626157302, 1524178455305, '2018-04-27 03:06:42.000000', '能不能把本地的图片传到服务器，这样大家都能看到了，用url换头像不太习惯');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524799118232, 0, 1523626157302, 1512314438990, '2018-04-27 03:18:38.000000', '这些图片是怎么发上去的呢？我发动态只有默认的两张图');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524843908458, 1524799118232, 82001, 1512314438990, '2018-04-27 15:45:08.000000', '在HttpRequest.addMoment中加的，因为APIJSON的Server Demo没做图片存储，所以目前只能自己传图片的url，可以百度图片上找哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524844181029, 1524798402799, 82001, 1524178455305, '2018-04-27 15:49:41.000000', '确实有这样的问题，但这个Demo仅供展示APIJSON的接口数据增删改查的能力，又拍云，七牛等平台又需要对接及付费，所以Demo暂时不提供哈，需要的话可以自己搞。建议先把图片上传到又拍云等平台，拿回url再传到自己的服务器^_^');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1524844222775, 1524798402799, 82001, 1524178455305, '2018-04-27 15:50:22.000000', '目前也可以百度一张图，把对应的url传上去，大家就都能看到了哈哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1525658333654, 0, 82001, 1513094436910, '2018-05-07 01:58:53.000000', 'q');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1527821844576, 0, 1527821445610, 1527821296110, '2018-06-01 02:57:24.000000', '好不好用啊');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1527821876802, 1527821844576, 1527495857924, 1527821296110, '2018-06-01 02:57:56.000000', '当然好用啊');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1527837906576, 0, 38710, 1527830331780, '2018-06-01 07:25:06.000000', '哇，好漂亮');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1527837965006, 0, 82002, 1527830474378, '2018-06-01 07:26:05.000000', '像平板电脑哈哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1527862540820, 0, 1527495857924, 1527830331780, '2018-06-01 14:15:40.000000', '谢谢你');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1527862609352, 1527837965006, 1527495857924, 1527830474378, '2018-06-01 14:16:49.000000', 'ㄟ(≧◇≦)ㄏ');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528105592852, 0, 82001, 1516086423441, '2018-06-04 09:46:32.000000', 'aaaaa');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528250648974, 0, 82001, 1523936332614, '2018-06-06 02:04:08.000000', 'hshdv');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528270188205, 0, 1528250827953, 1527830474378, '2018-06-06 07:29:48.000000', '这个图片是怎么发出来的啊，我发动态就只是那两张默认图片');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528278187969, 0, 82001, 470, '2018-06-06 09:43:07.000000', '啊啊啊啊');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528289446172, 0, 82001, 1528269988360, '2018-06-06 12:50:46.000000', '因为没做前端上传和后端保存图片的功能，APIJSONApp主要是用来展示APIJSON的自动化接口的');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528289580140, 0, 38710, 1528274037224, '2018-06-06 12:53:00.000000', '这两张图片的url错了哦，都是网页url，所以小图加载不出来，只能点击后用WebView查看');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528327885509, 1528289580140, 1528250827953, 1528274037224, '2018-06-06 23:31:25.000000', '噢噢，没想到你能这么快回复，谢谢');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528333168328, 0, 82001, 1514017444961, '2018-06-07 00:59:28.000000', 'zj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528333174811, 0, 82001, 1514017444961, '2018-06-07 00:59:34.000000', 'xj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528393204569, 1528270188205, 38710, 1527830474378, '2018-06-07 17:40:04.000000', '把接口里的pictureList的值改下，里面包含图片url');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528463152459, 1528463135762, 1528339692804, 1528462217322, '2018-06-08 13:05:52.000000', '我想去');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528463165903, 0, 1528339692804, 1528462217322, '2018-06-08 13:06:05.000000', '我想去');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528516951218, 0, 82001, 1528462217322, '2018-06-09 04:02:31.000000', '这里能约到小姐姐算我输୧(๑•̀⌄•́๑)૭');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528554476310, 0, 82001, 1516086423441, '2018-06-09 14:27:56.000000', 'thS');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528676480604, 0, 1528339692804, 1528356421201, '2018-06-11 00:21:20.000000', 'nihshs');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528677257985, 0, 1528339692804, 1528676875139, '2018-06-11 00:34:17.000000', 'aaa');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528679814166, 0, 1528339692804, 1528676875139, '2018-06-11 01:16:54.000000', '12');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528681265496, 1528516951218, 1528339692804, 1528462217322, '2018-06-11 01:41:05.000000', '你输了有什么惩罚吗？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528698907535, 0, 82001, 1516086423441, '2018-06-11 06:35:07.000000', 'yhbv');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528703971675, 1528681265496, 82001, 1528462217322, '2018-06-11 07:59:31.000000', '一起陪小姐姐出游*。٩(ˊωˋ*)و✧');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528799506317, 1516805340593, 82001, 1513094436910, '2018-06-12 10:31:46.000000', '摩恩');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528972547638, 0, 82001, 1528462217322, '2018-06-14 10:35:47.000000', '古古怪怪');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1528972555336, 0, 82001, 1528462217322, '2018-06-14 10:35:55.000000', '合计怕v就怕vi');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1529730035521, 0, 82001, 1527830331780, '2018-06-23 05:00:35.000000', '还有别的吗？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1530528524447, 0, 38710, 1528269988360, '2018-07-02 10:48:44.000000', '所以HttpRequest里写死了两张图片url，你可以改下');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1531063660028, 0, 82003, 1531062713966, '2018-07-08 15:27:40.000000', '这是哪里啊？我也想去');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1531412238453, 0, 82001, 1528356378455, '2018-07-12 16:17:18.000000', '去啊');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1531412264667, 0, 82003, 1528356378455, '2018-07-12 16:17:44.000000', '去哪呢？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1531887938362, 1531063660028, 82001, 1531062713966, '2018-07-18 04:25:38.000000', '是呀');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1531894411487, 0, 82001, 1520242333325, '2018-07-18 06:13:31.000000', 'sssx');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1531983163150, 0, 1531969715979, 1531969818357, '2018-07-19 06:52:43.000000', 'http://q18idc.com');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1532057419100, 0, 38710, 1531969818357, '2018-07-20 03:30:19.000000', '可以加上标题哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533008631299, 1532057419100, 82001, 1531969818357, '2018-07-31 03:43:51.000000', '加上');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533120405110, 1516780129884, 82001, 1516086423441, '2018-08-01 10:46:45.000000', 'eeeeee');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533120420498, 1528105592852, 82001, 1516086423441, '2018-08-01 10:47:00.000000', 'eeeeeee');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533186909764, 0, 82001, 1531969818357, '2018-08-02 05:15:09.000000', 'hello');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533187733941, 0, 82001, 1508072633830, '2018-08-02 05:28:53.000000', '好好');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533188056603, 1531887938362, 82001, 1531062713966, '2018-08-02 05:34:16.000000', '顺带');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533195207026, 0, 82001, 1531062713966, '2018-08-02 07:33:27.000000', 'JJ');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533202426013, 1533186909764, 82003, 1531969818357, '2018-08-02 09:33:46.000000', 'world');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533202917743, 1533186909764, 82001, 1531969818357, '2018-08-02 09:41:57.000000', '00');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533629063261, 0, 82001, 1531969818357, '2018-08-07 08:04:23.000000', '大鸡鸡');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533631893738, 0, 82001, 1531969818357, '2018-08-07 08:51:33.000000', '哈哈哈哈哈哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533809879340, 1533186909764, 82001, 1531969818357, '2018-08-09 10:17:59.000000', '434');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533889646344, 0, 82001, 1508072491570, '2018-08-10 08:27:26.000000', '11111111');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533902815448, 0, 82001, 1531969818357, '2018-08-10 12:06:55.000000', '很不要吃');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1533902902749, 0, 82001, 1531969818357, '2018-08-10 12:08:22.000000', '性能还可以');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1534053913157, 1524190941111, 1508072160401, 1516086423441, '2018-08-12 06:05:13.000000', '怎么约？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1534128014211, 0, 82001, 1520242333325, '2018-08-13 02:40:14.000000', 'zxxx');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1534412022857, 0, 82001, 1531969818357, '2018-08-16 09:33:42.000000', 'dgf');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1534684074665, 1531983163150, 82001, 1531969818357, '2018-08-19 13:07:54.000000', 'ggggg');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1534684209052, 110, 82001, 371, '2018-08-19 13:10:09.000000', '44444444444444444444444444');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1534926143012, 0, 82001, 1508053762227, '2018-08-22 08:22:23.000000', '治标不治本在不在不在不');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1534926149638, 1517375165233, 82001, 1508053762227, '2018-08-22 08:22:29.000000', '把标准版申报表上班设备');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1534992151350, 0, 82001, 1516086423441, '2018-08-23 02:42:31.000000', '你咋不');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535279823332, 0, 82001, 1520242333325, '2018-08-26 10:37:03.000000', '斤斤计较');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535279827983, 0, 82001, 1520242333325, '2018-08-26 10:37:07.000000', '斤斤计较');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535598334136, 1534992151350, 82003, 1516086423441, '2018-08-30 03:05:34.000000', '啥？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535600352436, 0, 82001, 1520242333325, '2018-08-30 03:39:12.000000', '6666666');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535600430479, 0, 82001, 1520242333325, '2018-08-30 03:40:30.000000', '法拉利');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535785537390, 1535279823332, 82003, 1520242333325, '2018-09-01 07:05:37.000000', '不好哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535785585222, 1534926143012, 82003, 1508053762227, '2018-09-01 07:06:25.000000', '啥？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535963519864, 0, 82001, 1535781636403, '2018-09-03 08:31:59.000000', 'gghhh');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1535963525135, 1535963519864, 82001, 1535781636403, '2018-09-03 08:32:05.000000', 'gyuji');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1536337000073, 1516686985310, 82001, 1516086423441, '2018-09-07 16:16:40.000000', 'heh');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1536378833060, 0, 82001, 1508072633830, '2018-09-08 03:53:53.000000', '真的嘛');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1536469270492, 0, 82001, 1528356496939, '2018-09-09 05:01:10.000000', '这是啥表情？Σ(ŎдŎ|||)ﾉﾉ');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1536805661269, 0, 70793, 1536805585275, '2018-09-13 02:27:41.000000', '6s再战一年');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1537373307627, 0, 82001, 1516086423441, '2018-09-19 16:08:27.000000', '。。。');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1537410620002, 0, 82001, 1536805585275, '2018-09-20 02:30:20.000000', '不一样');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1537710348414, 0, 82001, 1516086423441, '2018-09-23 13:45:48.000000', 'hhj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1537710359760, 0, 82001, 1516086423441, '2018-09-23 13:45:59.000000', '锵锵锵');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1537857324518, 0, 82001, 1536805585275, '2018-09-25 06:35:24.000000', '嗯呢');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1537857334299, 1537857324518, 82001, 1536805585275, '2018-09-25 06:35:34.000000', '嗯嗯');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1539252343243, 1539252313711, 82001, 15, '2018-10-11 10:05:43.000000', 'dxdf');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1539252350604, 1539252337210, 82001, 15, '2018-10-11 10:05:50.000000', 'djdnjd');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1539868250267, 1531063660028, 82001, 1531062713966, '2018-10-18 13:10:50.000000', '555555555555555555');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1539868258868, 1533188056603, 82001, 1531062713966, '2018-10-18 13:10:58.000000', '555555555');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1539868269471, 1539868250267, 82001, 1531062713966, '2018-10-18 13:11:09.000000', '4444444444444');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1539868275645, 1531887938362, 82001, 1531062713966, '2018-10-18 13:11:15.000000', '22222222222222222');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1539960436993, 0, 82001, 1539868023868, '2018-10-19 14:47:16.000000', '111');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1541496033857, 0, 82001, 301, '2018-11-06 09:20:33.000000', '能解决');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1541651688961, 1539960436993, 82001, 1539868023868, '2018-11-08 04:34:48.000000', '哈哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1541815269164, 0, 82001, 1541667945772, '2018-11-10 02:01:09.000000', '11');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1543193682067, 0, 1528339692804, 1528269822710, '2018-11-26 00:54:42.000000', 'ss');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1544496611006, 0, 82001, 15, '2018-12-11 02:50:11.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1544496618728, 0, 82001, 15, '2018-12-11 02:50:18.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1544496620126, 0, 82001, 15, '2018-12-11 02:50:20.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1544503960414, 1537410620002, 1544503822963, 1536805585275, '2018-12-11 04:52:40.000000', '664984');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1545527888416, 0, 82001, 1553096819293, '2018-12-23 01:18:08.000000', 'hello');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1545527898986, 1545527888416, 82001, 1553096819293, '2018-12-23 01:18:18.000000', 'world');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1545527923036, 1545527888416, 82001, 1553096819293, '2018-12-23 01:18:43.000000', '还差还差还差');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1545918307310, 0, 82001, 15, '2018-12-27 13:45:07.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1545927001999, 1545895875719, 82001, 1553096819293, '2018-12-27 16:10:02.000000', '哦哦哦www');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1545961973331, 0, 82001, 15, '2018-12-28 01:52:53.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1546050359778, 0, 82001, 15, '2018-12-29 02:25:59.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1546050386785, 0, 82001, 1516086423441, '2018-12-29 02:26:26.000000', '不鸟你');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1546934145366, 0, 82001, 15, '2019-01-08 07:55:45.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1546935903414, 0, 82001, 15, '2019-01-08 08:25:03.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551061659233, 0, 82001, 15, '2019-02-25 02:27:39.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551080228544, 0, 82001, 1553096819293, '2019-02-25 07:37:08.000000', '几何画板');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551080263524, 0, 82001, 1516086423441, '2019-02-25 07:37:43.000000', '你就看看');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551146130246, 0, 82001, 15, '2019-02-26 01:55:30.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551325753556, 0, 82001, 15, '2019-02-28 03:49:13.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551409858047, 0, 82001, 301, '2019-03-01 03:10:58.000000', '您');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551430453424, 0, 82001, 1553096819293, '2019-03-01 08:54:13.000000', '啊啊');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551430465241, 1551430453424, 82001, 1553096819293, '2019-03-01 08:54:25.000000', '123');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551430474490, 1551430453424, 82001, 1553096819293, '2019-03-01 08:54:34.000000', '444');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551430485689, 1551430453424, 82001, 1553096819293, '2019-03-01 08:54:45.000000', '品牌');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551430485828, 0, 82001, 1553096819293, '2019-03-01 08:54:45.000000', '品牌');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551430579358, 1516691119239, 82001, 1516086423441, '2019-03-01 08:56:19.000000', '555555');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551683545557, 1490863711703, 82001, 511, '2019-03-04 07:12:25.000000', '科技');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1551777156494, 0, 82001, 15, '2019-03-05 09:12:36.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1552215780595, 0, 82001, 15, '2019-03-10 11:03:00.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1552297994119, 0, 82001, 1512314438990, '2019-03-11 09:53:14.000000', '小米');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1552298015880, 1524799118232, 82001, 1512314438990, '2019-03-11 09:53:35.000000', '评论真的假的');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1552378943273, 0, 82001, 1551251234369, '2019-03-12 08:22:23.000000', '123');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1552381508342, 0, 82001, 15, '2019-03-12 09:05:08.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1552532948487, 0, 82001, 15, '2019-03-14 03:09:08.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1552980434725, 0, 82001, 15, '2019-03-19 07:27:14.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553053346095, 0, 82001, 1553096819293, '2019-03-20 03:42:26.000000', '启');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553053362233, 1551430485689, 82001, 1553096819293, '2019-03-20 03:42:42.000000', '集合vyih');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553065269448, 0, 82001, 1553096819293, '2019-03-20 07:01:09.000000', '3333');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553094325098, 0, 82001, 1550825876665, '2019-03-20 15:05:25.000000', '哦哦哦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553095251058, 0, 82001, 1552879777083, '2019-03-20 15:20:51.000000', '1111');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553233020780, 0, 82001, 15, '2019-03-22 05:37:00.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553433076832, 0, 82001, 1552879777083, '2019-03-24 13:11:16.000000', 'ggg');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553676470274, 0, 82001, 1551184480247, '2019-03-27 08:47:50.000000', 'hhhhhh');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553739854431, 0, 82001, 1539868023868, '2019-03-28 02:24:14.000000', '。。。。');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553837780458, 0, 82001, 1548145750536, '2019-03-29 05:36:20.000000', '王八蛋');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1553837842352, 0, 82001, 1541557989440, '2019-03-29 05:37:22.000000', '小赤佬');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554051650026, 0, 82001, 15, '2019-03-31 17:00:50.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554263554592, 0, 82001, 15, '2019-04-03 03:52:34.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554263558562, 0, 82001, 15, '2019-04-03 03:52:38.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554286215772, 0, 82001, 301, '2019-04-03 10:10:15.000000', 'hjkj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554309729462, 0, 82001, 1554263554668, '2019-04-03 16:42:09.000000', '没有我');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554309735842, 1554309729462, 82001, 1554263554668, '2019-04-03 16:42:15.000000', '哦下午');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554372265517, 0, 82001, 1554263554668, '2019-04-04 10:04:25.000000', '测试');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554373265740, 0, 82001, 543, '2019-04-04 10:21:05.000000', '那你');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554373282941, 0, 82001, 543, '2019-04-04 10:21:22.000000', '。');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554374987602, 1554372265517, 82001, 1554263554668, '2019-04-04 10:49:47.000000', '@jj');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554548838567, 0, 82001, 1554263558104, '2019-04-06 11:07:18.000000', 'Yyhgy');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554564631059, 0, 82001, 15, '2019-04-06 15:30:31.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554564640129, 0, 82001, 15, '2019-04-06 15:30:40.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554629321427, 0, 82001, 15, '2019-04-07 09:28:41.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554704296003, 0, 82001, 1554564640071, '2019-04-08 06:18:16.000000', '吃');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554707281257, 0, 82001, 1554688976001, '2019-04-08 07:08:01.000000', '要');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554777053552, 0, 82001, 15, '2019-04-09 02:30:53.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554864037995, 0, 82001, 15, '2019-04-10 02:40:37.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554965019923, 0, 82001, 15, '2019-04-11 06:43:39.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1554976816197, 0, 82001, 1554688976001, '2019-04-11 10:00:16.000000', '到现在');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555136064488, 0, 82001, 1555080161904, '2019-04-13 06:14:24.000000', '。。。');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555136143476, 0, 82001, 1554564639808, '2019-04-13 06:15:43.000000', '。。。');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555140354828, 0, 82001, 15, '2019-04-13 07:25:54.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555414582495, 0, 82001, 15, '2019-04-16 11:36:22.000000', '新增一条评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555414840945, 0, 82001, 15, '2019-04-16 11:40:40.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555414849052, 0, 82001, 15, '2019-04-16 11:40:49.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555414888850, 0, 82001, 15, '2019-04-16 11:41:28.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555557851140, 1512035117021, 82001, 32, '2019-04-18 03:24:11.000000', '1545456');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555557860778, 1512035117021, 82001, 32, '2019-04-18 03:24:20.000000', '11564546');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555557868075, 0, 82001, 32, '2019-04-18 03:24:28.000000', '121212312456');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555557877452, 1555557868075, 82001, 32, '2019-04-18 03:24:37.000000', '123121545645');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1555572659357, 0, 82001, 15, '2019-04-18 07:30:59.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556073175252, 0, 82001, 1555842172364, '2019-04-24 02:32:55.000000', '卡罗拉');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556088728850, 0, 82001, 1555660645705, '2019-04-24 06:52:08.000000', '啦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556088739682, 0, 82001, 1555842172364, '2019-04-24 06:52:19.000000', '叫姐姐');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556088750041, 1556088739682, 82001, 1555842172364, '2019-04-24 06:52:30.000000', '哼╯^╰');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556109617193, 0, 82001, 15, '2019-04-24 12:40:17.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556179911249, 0, 82001, 1555146101956, '2019-04-25 08:11:51.000000', '莫有样在真');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556179917534, 1556179911249, 82001, 1555146101956, '2019-04-25 08:11:57.000000', '你以为走咯');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556179921272, 1556179911249, 82001, 1555146101956, '2019-04-25 08:12:01.000000', '匿名');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556187596511, 0, 82001, 15, '2019-04-25 10:19:56.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556192437849, 0, 82001, 1555826433407, '2019-04-25 11:40:37.000000', '哈哈哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556264166448, 0, 82001, 15, '2019-04-26 07:36:06.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556434477631, 0, 82001, 1556416532140, '2019-04-28 06:54:37.000000', '叶圣陶');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556438316917, 0, 82001, 15, '2019-04-28 07:58:36.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556447474223, 0, 82001, 15, '2019-04-28 10:31:14.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556447629955, 0, 82001, 1556447474076, '2019-04-28 10:33:49.000000', '啦啦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556447642188, 0, 82001, 1556447473966, '2019-04-28 10:34:02.000000', '啦啦');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556606726466, 0, 82001, 15, '2019-04-30 06:45:26.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556608158926, 0, 82001, 1556607959204, '2019-04-30 07:09:18.000000', '佛祖');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556852777588, 0, 82001, 15, '2019-05-03 03:06:17.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556953335513, 0, 82001, 1556387217941, '2019-05-04 07:02:15.000000', '嘻嘻嘻');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556953340357, 1556953335513, 82001, 1556387217941, '2019-05-04 07:02:20.000000', '额额额');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556987652879, 0, 82001, 15, '2019-05-04 16:34:12.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556987738892, 0, 82001, 15, '2019-05-04 16:35:38.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1556988311205, 0, 82001, 15, '2019-05-04 16:45:11.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557024488300, 1556953340357, 82001, 1556387217941, '2019-05-05 02:48:08.000000', '222');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557133819296, 0, 82001, 1555146150581, '2019-05-06 09:10:19.000000', '988');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557239093145, 0, 82001, 15, '2019-05-07 14:24:53.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557254398378, 0, 82002, 15, '2019-05-07 18:39:58.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557254443156, 0, 82001, 15, '2019-05-07 18:40:43.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557254466593, 0, 82001, 15, '2019-05-07 18:41:06.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557254474819, 0, 82001, 15, '2019-05-07 18:41:14.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557367770352, 0, 82001, 1508072633830, '2019-05-09 02:09:30.000000', '哈哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557396836045, 0, 82001, 15, '2019-05-09 10:13:56.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557415832512, 0, 82001, 15, '2019-05-09 15:30:32.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557707040335, 0, 82001, 1550825876665, '2019-05-13 00:24:00.000000', '帅吗？');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557754720666, 0, 82001, 1557754680146, '2019-05-13 13:38:40.000000', '卧槽(*｀へ´*)');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557754765785, 0, 70793, 1557754680146, '2019-05-13 13:39:25.000000', '链接发下');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557754909538, 1557754765785, 82012, 1557754680146, '2019-05-13 13:41:49.000000', 'https://baijiahao.baidu.com/s?id=1633129683262867786&wfr=spider&for=pc&isFailFlag=1');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1557838424004, 0, 1508072105320, 1557754680146, '2019-05-14 12:53:44.000000', '666');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558059734104, 0, 82001, 15, '2019-05-17 02:22:14.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558059843115, 0, 82001, 15, '2019-05-17 02:24:03.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558150056102, 0, 82001, 1557754680146, '2019-05-18 03:27:36.000000', '哈哈哈');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558251372701, 0, 82001, 15, '2019-05-19 07:36:12.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558275517613, 0, 82001, 15, '2019-05-19 14:18:37.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558275862700, 0, 82001, 15, '2019-05-19 14:24:22.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558440302601, 0, 82001, 1557415707105, '2019-05-21 12:05:02.000000', '咔咔咔');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558440307225, 0, 82001, 1557415707105, '2019-05-21 12:05:07.000000', 'i我看');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558440318878, 0, 82001, 1555146144094, '2019-05-21 12:05:18.000000', '哦啊就是计算机');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558440335644, 0, 82001, 1557754680146, '2019-05-21 12:05:35.000000', '就是就是觉得奖学金');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558440349435, 0, 82001, 1557754680146, '2019-05-21 12:05:49.000000', '解决');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558440357141, 1558440335644, 82001, 1557754680146, '2019-05-21 12:05:57.000000', '惊声尖叫额');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1558444517410, 0, 82001, 15, '2019-05-21 13:15:17.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559479971167, 0, 82001, 15, '2019-06-02 12:52:51.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559512310810, 0, 82001, 1559479693309, '2019-06-02 21:51:50.000000', 'gggh');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559512315277, 1559512310810, 82001, 1559479693309, '2019-06-02 21:51:55.000000', 'ghhh');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559531009662, 0, 82001, 15, '2019-06-03 03:03:29.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559532427131, 0, 82001, 15, '2019-06-03 03:27:07.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559533505750, 0, 82001, 15, '2019-06-03 03:45:05.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559533513284, 0, 82001, 15, '2019-06-03 03:45:13.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559553148600, 0, 82001, 1559479693309, '2019-06-03 09:12:28.000000', 'cbnxn');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559570720109, 0, 82001, 1555146144094, '2019-06-03 14:05:20.000000', '你妹');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559616306248, 0, 82001, 15, '2019-06-04 02:45:06.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559617707946, 0, 82001, 15, '2019-06-04 03:08:27.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559628052667, 0, 82001, 1559479693309, '2019-06-04 06:00:52.000000', '发放');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559729189116, 0, 82001, 1559479693309, '2019-06-05 10:06:29.000000', '你真的牛逼');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1559731136559, 0, 82001, 15, '2019-06-05 10:38:56.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1560090719699, 0, 82001, 15, '2019-06-09 14:31:59.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1560168492719, 0, 82001, 15, '2019-06-10 12:08:12.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1560239593640, 0, 82001, 1559479693309, '2019-06-11 07:53:13.000000', '11');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1560262370938, 0, 82001, 15, '2019-06-11 14:12:50.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1560482772803, 0, 82001, 15, '2019-06-14 03:26:12.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1560482961067, 0, 82001, 1555842172364, '2019-06-14 03:29:21.000000', '3');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1560619772678, 0, 82001, 15, '2019-06-15 17:29:32.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1560736990682, 0, 82001, 15, '2019-06-17 02:03:10.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1561018625380, 1559512310810, 82001, 1559479693309, '2019-06-20 08:17:05.000000', 'ggggg');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1561364926819, 0, 82001, 15, '2019-06-24 08:28:46.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1561533263049, 1559729189116, 82001, 1559479693309, '2019-06-26 07:14:23.000000', '6666');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1561630666744, 0, 82001, 15, '2019-06-27 10:17:46.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1561949926841, 0, 82001, 15, '2019-07-01 02:58:46.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1561975421299, 1555136064488, 82001, 1555080161904, '2019-07-01 10:03:41.000000', '是不是');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562134664290, 0, 82001, 15, '2019-07-03 06:17:44.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562226345097, 0, 82001, 15, '2019-07-04 07:45:45.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562248764054, 1559729189116, 82001, 1559479693309, '2019-07-04 13:59:24.000000', 'fdd');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562307396087, 0, 82001, 1559479693309, '2019-07-05 06:16:36.000000', '我顶我顶');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562392250526, 0, 82001, 1559479693309, '2019-07-06 05:50:50.000000', '发个方
刚刚方法');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562392269174, 1559512310810, 82001, 1559479693309, '2019-07-06 05:51:09.000000', '古古怪怪');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562392274283, 1559553148600, 82001, 1559479693309, '2019-07-06 05:51:14.000000', '工业园');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562392278403, 1562307396087, 82001, 1559479693309, '2019-07-06 05:51:18.000000', '凤阳花鼓');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562495721307, 0, 82001, 15, '2019-07-07 10:35:21.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562662400473, 0, 82001, 15, '2019-07-09 08:53:20.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562684469790, 0, 82001, 1559125514307, '2019-07-09 15:01:09.000000', '发广告');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562684482831, 1562684469790, 82001, 1559125514307, '2019-07-09 15:01:22.000000', '厉害厉害');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562684488544, 0, 82001, 1559125514307, '2019-07-09 15:01:28.000000', '发过火');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562736873122, 1534926143012, 82001, 1508053762227, '2019-07-10 05:34:33.000000', '早睡早起');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562814013816, 0, 82001, 1562556720665, '2019-07-11 03:00:13.000000', 'xxxx');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1562814021942, 1562814013816, 82001, 1562556720665, '2019-07-11 03:00:21.000000', 'xxxx');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1563506794618, 0, 82001, 15, '2019-07-19 03:26:34.000000', '测试新增评论');
INSERT INTO sys."Comment" (id, "toId", "userId", "momentId", date, content) VALUES (1563641514400, 0, 82001, 1563605336326, '2019-07-20 16:51:54.000000', '年轻人不要想不开');
create table "Document"
(
    id       bigint       not null
        constraint "Document_pkey"
            primary key,
    "userId" bigint       not null,
    version  smallint     not null,
    name     varchar(100) not null,
    url      varchar(250) not null,
    request  text         not null,
    response text,
    header   text,
    date     timestamp
);

comment on table "Document" is '测试用例文档
后端开发者在测试好后，把选好的测试用例上传，这样就能共享给前端/客户端开发者';

comment on column "Document".id is '唯一标识';

comment on column "Document"."userId" is '用户id
		应该用adminId，只有当登录账户是管理员时才能操作文档。
		需要先建Admin表，新增登录等相关接口。';

comment on column "Document".version is '接口版本号
		<=0 - 不限制版本，任意版本都可用这个接口
		>0 - 在这个版本添加的接口';

comment on column "Document".name is '接口名称';

comment on column "Document".url is '请求地址';

comment on column "Document".request is '请求
		用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。';

comment on column "Document".response is '标准返回结果JSON
		用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。';

comment on column "Document".header is '请求头 Request Header：
		key: value //注释';

comment on column "Document".date is '创建时间';

alter table "Document"
    owner to postgres;

INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1, 0, 1, '登录', '/login', '{"type": 0, "phone": "13000082001", "version": 1, "password": "123456"}', null, null, '2017-11-26 07:35:19.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (2, 0, 1, '注册(先获取验证码type:1)', '/register', '{
    "Privacy": {
        "phone": "13000083333",
        "_password": "123456"
    },
    "User": {
        "name": "APIJSONUser"
    },
    "verify": "6840"
}', '{"code":412,"msg":"手机号或验证码错误！"}', null, '2017-11-26 06:56:10.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (3, 0, 1, '退出登录', '/logout', '{}', null, null, '2017-11-26 09:36:10.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511689914598, 0, 1, '获取用户隐私信息', '/gets', '{"tag": "Privacy", "Privacy": {"id": 82001}}', '{"Privacy":{"id":82001,"more":true,"certified":1,"phone":13000082001,"balance":9835.11},"code":200,"msg":"success"}', null, '2017-11-26 09:51:54.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511796155276, 0, 1, '获取验证码', '/post/verify', '{"type": 0, "phone": "13000082001"}', '{"Verify":{"id":1533396718012,"type":0,"phone":13000082001,"verify":4995,"date":"2018-08-04 23:31:58.0"},"tag":"Verify","code":200,"msg":"success"}', null, '2017-11-27 15:22:35.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511796208669, 0, 1, '检查验证码是否存在', '/heads/verify', '{"type": 0, "phone": "13000082001"}', '{"Verify":{"code":200,"msg":"success","count":1},"code":200,"msg":"success"}', null, '2017-11-27 15:23:28.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511796589078, 0, 1, '修改登录密码(先获取验证码type:2)-手机号+验证码', '/put/password', '{"verify": "10322", "Privacy": {"phone": "13000082001", "_password": "666666"}}', '{"code":412,"msg":"手机号或验证码错误！"}', null, '2017-11-27 15:29:49.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511796882183, 0, 1, '充值(需要支付密码)/提现', '/put/balance', '{"tag": "Privacy", "Privacy": {"id": 82001, "balance+": 100.15, "_payPassword": "123456"}}', '{"Privacy":{"code":200,"msg":"success","id":82001,"count":1},"code":200,"msg":"success"}', null, '2017-11-27 15:34:42.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511963330794, 0, 2, '获取文档列表(即在线解析网页上的共享)-API调用方式', '/get', '{
    "Document[]": {
        "Document": {
            "@role": "login",
            "@order": "version-,date-"
        }
    }
}', null, null, '2017-11-29 13:48:50.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511963677324, 0, 1, '获取用户', '/get', '{"User": {"id": 82001}}', '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,70793],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', null, '2017-11-29 13:54:37.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511963722969, 0, 1, '获取用户列表("id{}":contactIdList)-朋友页', '/get', '{
    "User[]": {
        "count": 10,
        "page": 0,
        "User": {
            "@column": "id,sex,name,tag,head",
            "@order": "name+",
            "id{}": [
                82002,
                82004,
                70793
            ]
        }
    }
}', '{"User[]":[{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},{"id":82004,"sex":0,"name":"Tommy","tag":"fasef","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000"}],"code":200,"msg":"success"}', null, '2017-11-29 13:55:22.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511963990071, 0, 1, '获取动态Moment+User+praiseUserList', '/get', '{
    "Moment": {
        "id": 15
    },
    "User": {
        "id@": "Moment/userId",
        "@column": "id,name,head"
    },
    "User[]": {
        "count": 10,
        "User": {
            "id{}@": "Moment/praiseUserIdList",
            "@column": "id,name"
        }
    }
}', '{"Moment":{"id":15,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON is a JSON Transmission Structure Protocol…","praiseUserIdList":[82055,82002,38710],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82002,"name":"Happy~"},{"id":82055,"name":"Solid"}],"code":200,"msg":"success"}', null, '2017-11-29 13:59:50.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511964176688, 0, 1, '获取评论列表-动态详情页Comment+User', '/get', '{
    "[]": {
        "count": 20,
        "page": 0,
        "Comment": {
            "@order": "date+",
            "momentId": 15
        },
        "User": {
            "id@": "/Comment/userId",
            "@column": "id,name,head"
        }
    }
}', '{"[]":[{"Comment":{"id":176,"toId":166,"userId":38710,"momentId":15,"date":"2017-03-25 20:28:03.0","content":"thank you"},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"}},{"Comment":{"id":1490863469638,"toId":0,"userId":82002,"momentId":15,"date":"2017-03-30 16:44:29.0","content":"Just do it"},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"}},{"Comment":{"id":1490875660259,"toId":1490863469638,"userId":82055,"momentId":15,"date":"2017-03-30 20:07:40.0","content":"I prove wht you said(??????)"},"User":{"id":82055,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1508227456407,"toId":0,"userId":82001,"momentId":15,"date":"2017-10-17 16:04:16.0","content":"hsh"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1509346606036,"toId":0,"userId":82001,"momentId":15,"date":"2017-10-30 14:56:46.0","content":"测"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1525933255901,"userId":82001,"momentId":15,"date":"2018-05-10 14:20:55.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1527949266037,"userId":82001,"momentId":15,"date":"2018-06-02 22:21:06.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528339777338,"userId":82001,"momentId":15,"date":"2018-06-07 10:49:37.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528366915282,"userId":82001,"momentId":15,"date":"2018-06-07 18:21:55.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528366931410,"userId":82001,"momentId":15,"date":"2018-06-07 18:22:11.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528392773597,"userId":82001,"momentId":15,"date":"2018-06-08 01:32:53.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529034360708,"userId":82001,"momentId":15,"date":"2018-06-15 11:46:00.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529078537044,"userId":82001,"momentId":15,"date":"2018-06-16 00:02:17.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529401004622,"userId":82001,"momentId":15,"date":"2018-06-19 17:36:44.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529401505690,"userId":82001,"momentId":15,"date":"2018-06-19 17:45:05.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529468113356,"userId":82001,"momentId":15,"date":"2018-06-20 12:15:13.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529724026842,"userId":82001,"momentId":15,"date":"2018-06-23 11:20:26.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529909214303,"userId":82001,"momentId":15,"date":"2018-06-25 14:46:54.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1530276831779,"userId":82001,"momentId":15,"date":"2018-06-29 20:53:51.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1531365764793,"userId":82001,"momentId":15,"date":"2018-07-12 11:22:44.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}}],"code":200,"msg":"success"}', null, '2017-11-29 14:02:56.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511967853339, 0, 1, '获取动态列表Moment+User+User:parise[]+Comment[]', '/get', '{     "[]": {         "count": 5,         "page": 0,         "Moment": {             "@order": "date+"         },         "User": {             "id@": "/Moment/userId",             "@column": "id,name,head"         },         "User[]": {             "count": 10,             "User": {                 "id{}@": "[]/Moment/praiseUserIdList",                 "@column": "id,name"             }         },         "[]": {             "count": 6,             "Comment": {                 "@order": "date+",                 "momentId@": "[]/Moment/id"             },             "User": {                 "id@": "/Comment/userId",                 "@column": "id,name"             }         }     } }', '{"[]":[{"Moment":{"id":301,"userId":93793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-301","praiseUserIdList":[38710,93793,82003,82005,82040,82055,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":93793,"name":"Mike","head":"http://static.oschina.net/uploads/user/48/96331_50.jpg"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82040,"name":"Dream"},{"id":82055,"name":"Solid"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":45,"toId":0,"userId":93793,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-45"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":51,"toId":45,"userId":82003,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-51"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":76,"toId":45,"userId":93793,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-76"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":120,"toId":0,"userId":93793,"momentId":301,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-110"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":124,"toId":0,"userId":82001,"momentId":301,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-114"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490781009548,"toId":51,"userId":82001,"momentId":301,"date":"2017-03-29 17:50:09.0","content":"3"},"User":{"id":82001,"name":"测试改名"}}]},{"Moment":{"id":58,"userId":90814,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-435","praiseUserIdList":[38710,82003,82005,93793,82006,82044,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg"]},"User":{"id":90814,"name":"007","head":"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82001,"name":"测试改名"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82006,"name":"Meria"},{"id":82044,"name":"Love"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":13,"toId":0,"userId":82005,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-13"},"User":{"id":82005,"name":"Jan"}},{"Comment":{"id":77,"toId":13,"userId":93793,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-77"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":97,"toId":13,"userId":82006,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-97"},"User":{"id":82006,"name":"Meria"}},{"Comment":{"id":167,"userId":82001,"momentId":58,"date":"2017-03-25 19:48:41.0","content":"Nice!"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":173,"userId":38710,"momentId":58,"date":"2017-03-25 20:25:13.0","content":"Good"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":188,"toId":97,"userId":82001,"momentId":58,"date":"2017-03-26 15:21:32.0","content":"1646"},"User":{"id":82001,"name":"测试改名"}}]},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"User[]":[{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82006,"name":"Meria"},{"id":82040,"name":"Dream"},{"id":90814,"name":"007"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":68,"toId":0,"userId":82005,"momentId":371,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-68"},"User":{"id":82005,"name":"Jan"}},{"Comment":{"id":157,"userId":93793,"momentId":371,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-157"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":110,"toId":0,"userId":93793,"momentId":371,"date":"2017-02-01 19:23:24.0","content":"This is a Content...-110"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":114,"toId":0,"userId":82001,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-114"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":115,"toId":0,"userId":38710,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-115"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":116,"toId":0,"userId":70793,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-116"},"User":{"id":70793,"name":"Strong"}}]},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"User[]":[{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82044,"name":"Love"}],"[]":[{"Comment":{"id":44,"toId":0,"userId":82003,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-44"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":54,"toId":0,"userId":82004,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-54"},"User":{"id":82004,"name":"Tommy"}},{"Comment":{"id":99,"toId":44,"userId":70793,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-99"},"User":{"id":70793,"name":"Strong"}},{"Comment":{"id":206,"toId":54,"userId":82001,"momentId":170,"date":"2017-03-29 11:04:23.0","content":"ejej"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490780759866,"toId":99,"userId":82001,"momentId":170,"date":"2017-03-29 17:45:59.0","content":"99"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490863661426,"toId":1490780759866,"userId":70793,"momentId":170,"date":"2017-03-30 16:47:41.0","content":"66"},"User":{"id":70793,"name":"Strong"}}]},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"},"User[]":[{"id":82001,"name":"测试改名"}],"[]":[{"Comment":{"id":4,"toId":0,"userId":38710,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-4"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":22,"toId":221,"userId":82001,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"测试修改评论"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":47,"toId":4,"userId":70793,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-47"},"User":{"id":70793,"name":"Strong"}},{"Comment":{"id":1490863507114,"toId":4,"userId":82003,"momentId":470,"date":"2017-03-30 16:45:07.0","content":"yes"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":1490863903900,"toId":0,"userId":82006,"momentId":470,"date":"2017-03-30 16:51:43.0","content":"SOGA"},"User":{"id":82006,"name":"Meria"}},{"Comment":{"id":1491740899179,"toId":0,"userId":82001,"momentId":470,"date":"2017-04-09 20:28:19.0","content":"www"},"User":{"id":82001,"name":"测试改名"}}]}],"code":200,"msg":"success"}', null, '2017-11-29 15:04:13.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511969181103, 0, 1, '添加朋友', '/put', '{
    "User": {
        "id": 82001,
        "contactIdList+": [93793]
    },
    "tag": "User"
}', '{"User":{"id":82001,"contactIdList+":[93793],"@role":"owner"},"code":409,"msg":"PUT User, contactIdList:93793 已存在！"}', null, '2017-11-29 15:26:21.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511969417632, 0, 1, '点赞/取消点赞', '/put', '{
    "Moment": {
        "id": 15,
        "praiseUserIdList-": [
            82001
        ]
    },
    "tag": "Moment"
}', '{"Moment":{"code":200,"msg":"success","id":15,"count":1},"code":200,"msg":"success"}', null, '2017-11-29 15:30:17.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511969630371, 0, 1, '新增评论', '/post', '{     "Comment": {         "momentId": 15,         "content": "测试新增评论"     },     "tag": "Comment" }', '{"Comment":{"code":200,"msg":"success","id":1533140610714,"count":1},"code":200,"msg":"success"}', null, '2017-11-29 15:33:50.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511970009071, 0, 1, '新增动态', '/post', '{     "Moment": {         "content": "测试新增动态",         "pictureList": ["http://static.oschina.net/uploads/user/48/96331_50.jpg"         ]     },     "tag": "Moment" }', '{"Moment":{"code":200,"msg":"success","id":1533140610716,"count":1},"code":200,"msg":"success"}', null, '2017-11-29 15:40:09.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1511970224332, 0, 1, '修改用户信息', '/put', '{
     "User": {
         "id": 82001,
         "name": "测试账号"
     },
     "tag": "User"
 }', '{"User":{"code":200,"msg":"success","id":82001,"count":1},"code":200,"msg":"success"}', null, '2017-11-29 15:43:44.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1512216131854, 0, 1, '获取文档列表(即在线解析网页上的文档)-表和字段、请求格式限制', '/get', '{
    "[]": {
        "Table": {
            "TABLE_SCHEMA": "sys",
            "TABLE_TYPE": "BASE TABLE",
            "TABLE_NAME!$": [
                "\\_%",
                "sys\\_%",
                "system\\_%"
            ],
            "@order": "TABLE_NAME+",
            "@column": "TABLE_NAME,TABLE_COMMENT"
        },
        "Column[]": {
            "Column": {
                "TABLE_NAME@": "[]/Table/TABLE_NAME",
                "@column": "COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT"
            }
        }
    },
    "Request[]": {
        "Request": {
            "@order": "version-,method-"
        }
    }
}', null, null, '2017-12-02 12:02:11.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521901518764, 0, 2, '功能符(对象关键词): ⑤从pictureList获取第0张图片：', '/get', '{     "User": {         "id": 38710,         "@position": 0,         "firstPicture()": "getFromArray(pictureList,@position)"     } }', '{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0","@position":0,"firstPicture":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"},"code":200,"msg":"success"}', null, '2018-03-24 14:25:18.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521901610783, 0, 2, '功能符(对象关键词): ④查询 按userId分组、id最大值>=100 的Moment数组', '/get', '{"[]":{"count":10,"Moment":{"@column":"userId;max(id):maxId","@group":"userId","@having":"maxId>=100"}}}', '{"[]":[{"Moment":{"userId":38710,"maxId":1537025707417}},{"Moment":{"userId":70793,"maxId":551}},{"Moment":{"userId":82001,"maxId":1537025634931}},{"Moment":{"userId":82002,"maxId":1531062713966}},{"Moment":{"userId":82003,"maxId":1536805585275}},{"Moment":{"userId":82045,"maxId":1508073178489}},{"Moment":{"userId":82056,"maxId":1514858533480}},{"Moment":{"userId":93793,"maxId":1516086423441}},{"Moment":{"userId":1520242280259,"maxId":1520242333325}},{"Moment":{"userId":1523626157302,"maxId":1523936332614}}],"code":200,"msg":"success"}', null, '2018-03-24 14:26:50.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521901682845, 0, 2, '功能符(对象关键词): ③查询按userId分组的Moment数组', '/get', '{"[]":{"count":10,"Moment":{"@column":"userId,id","@group":"userId,id"}}}', '{"[]":[{"Moment":{"userId":38710,"id":235}},{"Moment":{"userId":38710,"id":470}},{"Moment":{"userId":38710,"id":511}},{"Moment":{"userId":38710,"id":595}},{"Moment":{"userId":38710,"id":704}},{"Moment":{"userId":38710,"id":1491200468898}},{"Moment":{"userId":38710,"id":1493835799335}},{"Moment":{"userId":38710,"id":1512314438990}},{"Moment":{"userId":38710,"id":1513094436910}},{"Moment":{"userId":38710,"id":1537025625613}}],"code":200,"msg":"success"}', null, '2018-03-24 14:28:02.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521901746808, 0, 2, '功能符(对象关键词): ②查询按 name降序、id默认顺序 排序的User数组', '/get', '{"[]":{"count":10,"User":{"@column":"name,id","@order":"name-,id"}}}', '{"[]":[{"User":{"name":"赵钱孙李","id":1508072071492}},{"User":{"name":"测试改名","id":82001}},{"User":{"name":"梦","id":1528264711016}},{"User":{"name":"宁旭","id":1532188114543}},{"User":{"name":"四五六","id":1508072160401}},{"User":{"name":"哈哈哈","id":1524042900591}},{"User":{"name":"周吴郑王","id":1508072105320}},{"User":{"name":"七八九十","id":1508072202871}},{"User":{"name":"一二三","id":1499057230629}},{"User":{"name":"Yong","id":82027}}],"code":200,"msg":"success"}', null, '2018-03-24 14:29:06.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521901787202, 0, 2, '功能符(对象关键词): ①只查询id,sex,name这几列并且请求结果也按照这个顺序', '/get', '{"User":{"@column":"id,sex,name","id":38710}}', '{"User":{"id":38710,"sex":0,"name":"TommyLemon"},"code":200,"msg":"success"}', null, '2018-03-24 14:29:47.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521902033331, 0, 2, '功能符(数组关键词): ③查询User数组和对应的User总数', '/get', '{"[]":{"query":2,"count":5,"User":{}},"total@":"/[]/total"}', '{"[]":[{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82021,82006,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}}],"total":121,"code":200,"msg":"success"}', null, '2018-03-24 14:33:53.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521902069870, 0, 2, '功能符(数组关键词): ②查询第3页的User数组，每页5个', '/get', '{"[]":{"count":5,"page":3,"User":{}}}', '{"[]":[{"User":{"id":82025,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82026,"sex":0,"name":"iOS","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82027,"sex":0,"name":"Yong","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82028,"sex":1,"name":"gaeg","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82029,"sex":0,"name":"GASG","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}}],"code":200,"msg":"success"}', null, '2018-03-24 14:34:29.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521902110679, 0, 2, '功能符(数组关键词): ①查询User数组，最多5个', '/get', '{"[]":{"count":5,"User":{}}}', '{"[]":[{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82021,82006,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}}],"code":200,"msg":"success"}', null, '2018-03-24 14:35:10.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521903761688, 0, 2, '功能符(逻辑运算): ③ ! 非运算', '/head', '{"User":{"id!{}":[82001,38710]}}', '{"User":{"code":200,"msg":"success","count":119},"code":200,"msg":"success"}', null, '2018-03-24 15:02:41.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521903828409, 0, 2, '功能符(逻辑运算): ② | 或运算', '/head', '{"User":{"id|{}":">90000,<=80000"}}', '{"User":{"code":200,"msg":"success","count":72},"code":200,"msg":"success"}', null, '2018-03-24 15:03:48.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521903882829, 0, 2, '功能符(逻辑运算): ① & 与运算', '/head', '{"User":{"id&{}":">80000,<=90000"}}', '{"User":{"code":200,"msg":"success","count":49},"code":200,"msg":"success"}', null, '2018-03-24 15:04:42.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904098110, 0, 2, '功能符: 减少 或 去除', '/put/balance', '{
    
    "Privacy": {
        "id": 82001,
        "balance+": -100,
        "_payPassword": "123456"
    },"tag": "Privacy"
}', '{"Privacy":{"code":200,"msg":"success","id":82001,"count":1},"code":200,"msg":"success"}', null, '2018-03-24 15:08:18.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904162065, 0, 2, '功能符: 增加 或 扩展', '/put', '{
    "Moment": {
        "id": 15,
        "praiseUserIdList+": [
            82001
        ]
    },
    "tag": "Moment"
}', '{"Moment":{"code":200,"msg":"success","id":15,"count":1},"code":200,"msg":"success"}', null, '2018-03-24 15:09:22.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904337053, 0, 2, '功能符: 新建别名', '/get', '{"Comment":{"@column":"id,toId:parentId","id":51}}', '{"Comment":{"id":51,"parentId":45},"code":200,"msg":"success"}', null, '2018-03-24 15:12:17.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904394041, 0, 2, '功能符: 正则匹配', '/get', '{"User[]":{"count":3,"User":{"name?":"^[0-9]+$"}}}', '{"User[]":[{"id":90814,"sex":0,"name":"007","head":"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":1528339692804,"sex":1,"name":"568599","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[1528250827953,1528264711016],"date":"2018-06-07 10:48:12.0"}],"code":200,"msg":"success"}', null, '2018-03-24 15:13:14.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904437583, 0, 2, '功能符: 模糊搜索', '/get', '{"User[]":{"count":3,"User":{"name$":"%m%"}}}', '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":82004,"sex":0,"name":"Tommy","tag":"fasef","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82006,"sex":1,"name":"Meria","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', null, '2018-03-24 15:13:57.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904547991, 0, 2, '功能符: 引用赋值', '/get', '{"Moment":{
   "userId":38710
},
"User":{
   "id@":"/Moment/userId"
}}', '{"Moment":{"id":235,"userId":38710,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg","http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000"]},"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', null, '2018-03-24 15:15:47.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904617126, 0, 2, '功能符: 远程调用函数', '/get', '{     "Moment": {         "id": 301,         "@column": "userId,praiseUserIdList",         "isPraised()": "isContain(praiseUserIdList,userId)"     } }', '{"Moment":{"userId":93793,"praiseUserIdList":[38710,93793,82003,82005,82040,82055,82002,82001],"isPraised":true},"code":200,"msg":"success"}', null, '2018-03-24 15:16:57.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904653621, 0, 2, '功能符: 包含选项范围', '/get', '{"User[]":{"count":3,"User":{"contactIdList<>":38710}}}', '{"User[]":[{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710,1532439021068],"pictureList":[],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', null, '2018-03-24 15:17:33.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904698934, 0, 2, '功能符: 匹配条件范围', '/get', '{"User[]":{"count":3,"User":{"id{}":"<=80000,>90000"}}}', '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":90814,"sex":0,"name":"007","head":"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', null, '2018-03-24 15:18:18.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521904756673, 0, 2, '功能符: 查询数组', '/get', '{"User[]":{"count":3,"User":{}}}', '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', null, '2018-03-24 15:19:16.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521905263827, 0, 2, '操作方法(DELETE):  删除数据', '/delete', '{
   "Moment":{
     "id":120
   },
   "tag":"Moment"
}', '{"Moment":{"code":404,"msg":"可能对象不存在！","id":120,"count":0},"Comment":{"code":404,"msg":"可能对象不存在！","count":0},"code":200,"msg":"success"}', null, '2018-03-24 15:27:43.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521905599149, 0, 2, '操作方法(PUT):  修改数据，只修改所传的字段', '/put', '{
   "Moment":{
     "id":235,
     "content":"APIJSON,let interfaces and documents go to hell !"
   },
   "tag":"Moment"
}', '{"Moment":{"code":200,"msg":"success","id":235,"count":1},"code":200,"msg":"success"}', null, '2018-03-24 15:33:19.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521905680679, 0, 2, '操作方法(POST):  新增数据', '/post', '{     "Moment": {         "content": "APIJSON,let interfaces and documents go to hell !"     },     "tag": "Moment" }', '{"Moment":{"code":200,"msg":"success","id":1538112282445,"count":1},"code":200,"msg":"success"}', null, '2018-03-24 15:34:40.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521905787849, 0, 2, '操作方法(HEADS):  安全/私密获取数量，用于获取银行卡数量等 对安全性要求高的数据总数', '/heads', '{
    "Login": {
        "userId": 38710,"type":1
    },
    "tag": "Login"
}', '{"Login":{"code":200,"msg":"success","count":1},"code":200,"msg":"success"}', null, '2018-03-24 15:36:27.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521905868718, 0, 2, '操作方法(GETS):  安全/私密获取数据，用于获取钱包等 对安全性要求高的数据', '/gets', '{
    "Privacy": {
        "id": 82001
    },
    "tag": "Privacy"
}', '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":9832.86},"code":200,"msg":"success"}', null, '2018-03-24 15:37:48.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521905895590, 0, 2, '操作方法(HEAD):  普通获取数量，可用浏览器调试', '/head', '{
   "Moment":{
     "userId":38710
   }
}', '{"Moment":{"code":200,"msg":"success","count":10},"code":200,"msg":"success"}', null, '2018-03-24 15:38:15.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521905913187, 0, 2, '操作方法(GET):  普通获取数据，可用浏览器调试', '/get', '{
   "Moment":{
     "id":235
   }
}', '{"Moment":{"id":235,"userId":38710,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg","http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000"]},"code":200,"msg":"success"}', null, '2018-03-24 15:38:33.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521906240331, 0, 2, 'User发布的Moment列表，每个Moment包括 1.发布者User 2.前3条Comment: ③不查已获取的User', '/get', '{
   "[]":{
     "page":0,
     "count":3, 
     "Moment":{
       "userId":38710
     },
     "Comment[]":{
       "count":3,
       "Comment":{
         "momentId@":"[]/Moment/id"
       }
     }
   }
}', '{"[]":[{"Moment":{"id":235,"userId":38710,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg","http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000"]},"Comment[]":[{"id":160,"toId":0,"userId":82001,"momentId":235,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-160"},{"id":163,"toId":0,"userId":82001,"momentId":235,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-163"},{"id":168,"toId":1490442545077,"userId":82001,"momentId":235,"date":"2017-03-25 19:49:14.0","content":"???"}]},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"Comment[]":[{"id":4,"toId":0,"userId":38710,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-4"},{"id":22,"toId":221,"userId":82001,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"测试修改评论"},{"id":47,"toId":4,"userId":70793,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-47"}]},{"Moment":{"id":511,"userId":38710,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[70793,93793,82001],"pictureList":["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067"]},"Comment[]":[{"id":178,"userId":38710,"momentId":511,"date":"2017-03-25 20:30:55.0","content":"wbw"},{"id":1490863711703,"toId":0,"userId":70793,"momentId":511,"date":"2017-03-30 16:48:31.0","content":"I hope I can join"},{"id":1490863717947,"toId":178,"userId":70793,"momentId":511,"date":"2017-03-30 16:48:37.0","content":"what?"}]}],"code":200,"msg":"success"}', null, '2018-03-24 15:44:00.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521906265959, 0, 2, 'User发布的Moment列表，每个Moment包括 1.发布者User 2.前3条Comment: ②省去重复的User', '/get', '{
   "User":{
     "id":38710
   },
   "[]":{
     "page":0,
     "count":3, 
     "Moment":{
       "userId":38710
     }, 
     "Comment[]":{
       "count":3,
       "Comment":{
         "momentId@":"[]/Moment/id"
       }
     }
   }
}', '{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"[]":[{"Moment":{"id":235,"userId":38710,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg","http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000"]},"Comment[]":[{"id":160,"toId":0,"userId":82001,"momentId":235,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-160"},{"id":163,"toId":0,"userId":82001,"momentId":235,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-163"},{"id":168,"toId":1490442545077,"userId":82001,"momentId":235,"date":"2017-03-25 19:49:14.0","content":"???"}]},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"Comment[]":[{"id":4,"toId":0,"userId":38710,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-4"},{"id":22,"toId":221,"userId":82001,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"测试修改评论"},{"id":47,"toId":4,"userId":70793,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-47"}]},{"Moment":{"id":511,"userId":38710,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[70793,93793,82001],"pictureList":["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067"]},"Comment[]":[{"id":178,"userId":38710,"momentId":511,"date":"2017-03-25 20:30:55.0","content":"wbw"},{"id":1490863711703,"toId":0,"userId":70793,"momentId":511,"date":"2017-03-30 16:48:31.0","content":"I hope I can join"},{"id":1490863717947,"toId":178,"userId":70793,"momentId":511,"date":"2017-03-30 16:48:37.0","content":"what?"}]}],"code":200,"msg":"success"}', null, '2018-03-24 15:44:25.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521906517000, 0, 2, 'User发布的Moment列表，每个Moment包括 1.发布者User 2.前3条Comment: ①指定id', '/get', '{
    "[]": {
        "page": 0,
        "count": 3,
        "Moment":{"userId":38710}, "User":{"id":38710} ,
        "Comment[]": {
            "count": 3,
            "Comment": {
                "momentId@": "[]/Moment/id"
            }
        }
    }
}', '{"[]":[{"Moment":{"id":235,"userId":38710,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg","http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000"]},"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"Comment[]":[{"id":160,"toId":0,"userId":82001,"momentId":235,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-160"},{"id":163,"toId":0,"userId":82001,"momentId":235,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-163"},{"id":168,"toId":1490442545077,"userId":82001,"momentId":235,"date":"2017-03-25 19:49:14.0","content":"???"}]},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"Comment[]":[{"id":4,"toId":0,"userId":38710,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-4"},{"id":22,"toId":221,"userId":82001,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"测试修改评论"},{"id":47,"toId":4,"userId":70793,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-47"}]},{"Moment":{"id":511,"userId":38710,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[70793,93793,82001],"pictureList":["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067"]},"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"Comment[]":[{"id":178,"userId":38710,"momentId":511,"date":"2017-03-25 20:30:55.0","content":"wbw"},{"id":1490863711703,"toId":0,"userId":70793,"momentId":511,"date":"2017-03-30 16:48:31.0","content":"I hope I can join"},{"id":1490863717947,"toId":178,"userId":70793,"momentId":511,"date":"2017-03-30 16:48:37.0","content":"what?"}]}],"code":200,"msg":"success"}', null, '2018-03-24 15:48:37.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907009307, 0, 2, 'Moment列表，每个Moment包括 1.发布者User 2.前3条Comment', '/get', '{
   "[]":{
     "page":0, 
     "count":3, 
     "Moment":{}, 
     "User":{
       "id@":"/Moment/userId"
     },
     "Comment[]":{
       "count":3,
       "Comment":{
         "momentId@":"[]/Moment/id"
       }
     }
   }
}', '{"[]":[{"Moment":{"id":12,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[70793,93793,82044,82040,82055,90814,38710,82002,82006,1508072105320,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067","http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067"]},"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},"Comment[]":[{"id":162,"toId":0,"userId":93793,"momentId":12,"date":"2017-03-06 13:03:45.0","content":"This is a Content...-162"},{"id":164,"toId":0,"userId":93793,"momentId":12,"date":"2017-03-06 13:03:45.0","content":"This is a Content...-164"},{"id":172,"toId":162,"userId":82001,"momentId":12,"date":"2017-03-25 20:22:58.0","content":"OK"}]},{"Moment":{"id":15,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON is a JSON Transmission Structure Protocol…","praiseUserIdList":[82002,70793,38710,93793,82001],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},"Comment[]":[{"id":176,"toId":166,"userId":38710,"momentId":15,"date":"2017-03-25 20:28:03.0","content":"thank you"},{"id":1490863469638,"toId":0,"userId":82002,"momentId":15,"date":"2017-03-30 16:44:29.0","content":"Just do it"},{"id":1490875660259,"toId":1490863469638,"userId":82055,"momentId":15,"date":"2017-03-30 20:07:40.0","content":"I prove wht you said(??????)"}]},{"Moment":{"id":32,"userId":82002,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[38710,82002,82001],"pictureList":["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"},"Comment[]":[{"id":1512035117021,"toId":0,"userId":82001,"momentId":32,"date":"2017-11-30 17:45:17.0","content":"图片看不了啊"},{"id":1512039030970,"toId":1512035117021,"userId":82001,"momentId":32,"date":"2017-11-30 18:50:30.0","content":"一般九宫格图片都是压缩图，分辨率在300*300左右，加载很快，点击放大后才是原图，1080P左右"},{"id":1555557851140,"toId":1512035117021,"userId":82001,"momentId":32,"date":"2019-04-18 11:24:11.0","content":"1545456"}]}],"code":200,"msg":"success","time:start/duration/end":"1556501639028/18/1556501639046","query:depth/max":"5/5","sql:generate/cache/execute/maxExecute":"15/9/6/200"}', null, '2018-03-24 15:56:49.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907303539, 0, 2, 'User列表', '/get', '{
   "User[]":{
     "page":0,
     "count":3, 
     "User":{
       "sex":0
     }
   }
}', '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试账号","tag":"Dev","head":"https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Logo.png","contactIdList":[82034,82006,82030,82021,82038],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success","time:start/duration/end":"1556501639031/9/1556501639040","query:depth/max":"3/5","sql:generate/cache/execute/maxExecute":"3/2/1/200"}', null, '2018-03-24 16:01:43.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907317870, 0, 2, 'Moment和对应的User', '/get', '{
   "Moment":{
     "userId":38710
   }, 
   "User":{
     "id":38710
   }
}', '{"Moment":{"id":235,"userId":38710,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg","http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000"]},"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success","time:start/duration/end":"1556501639026/16/1556501639042","query:depth/max":"1/5","sql:generate/cache/execute/maxExecute":"2/0/2/200"}', null, '2018-03-24 16:01:57.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907333044, 0, 2, 'User', '/get', '{
   "User":{
     "id":38710
   }
}', '{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success","time:start/duration/end":"1556501639010/3/1556501639013","query:depth/max":"1/5","sql:generate/cache/execute/maxExecute":"1/0/1/200"}', null, '2018-03-24 16:02:13.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907333046, 0, 2, 'Moment INNER JOIN User LEFT JOIN Comment', '/get', '{
    "[]": {
        "count": 10,
        "page": 0,
        "join": "&/User/id@,</Comment/momentId@",
        "Moment": {
            "@order": "date+"
        },
        "User": {
            "name?": [
                "a",
                "t"
            ],
            "id@": "/Moment/userId",
            "@column": "id,name,head"
        },
        "Comment": {
            "momentId@": "/Moment/id",
            "@column": "id,momentId,content"
        }
    }
}', '{"[]":[{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":99,"momentId":170,"content":"This is a Content...-99"}},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":99,"momentId":170,"content":"This is a Content...-99"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":182,"momentId":371,"content":"hahaha"}},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":99,"momentId":170,"content":"This is a Content...-99"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":182,"momentId":371,"content":"hahaha"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":182,"momentId":371,"content":"hahaha"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":182,"momentId":371,"content":"hahaha"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":182,"momentId":371,"content":"hahaha"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":182,"momentId":371,"content":"hahaha"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":182,"momentId":371,"content":"hahaha"}}],"code":200,"msg":"success","time:start/duration/end":"1556501638957/51/1556501639008","query:depth/max":"3/5","sql:generate/cache/execute/maxExecute":"30/27/3/200"}', null, '2018-03-24 16:02:43.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907333047, 0, 2, '获取粉丝的动态列表', '/get', '{     "Moment[]": {         "join": "&/User/id@",         "Moment": {},         "User": {             "id@": "/Moment/userId",             "contactIdList<>": 82001,             "@column": "id"         }     } }', '{"Moment[]":[{"id":32,"userId":82002,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[38710,82002,82001],"pictureList":["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},{"id":1508053762227,"userId":82003,"date":"2017-10-15 15:49:22.0","content":"我也试试","praiseUserIdList":[1515565976140,82001],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1508072491570,"userId":82002,"date":"2017-10-15 21:01:31.0","content":"有点冷~","praiseUserIdList":[82001,82002],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1508073178489,"userId":82045,"date":"2017-10-15 21:12:58.0","content":"发动态","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1514017444961,"userId":82002,"date":"2017-12-23 16:24:04.0","content":"123479589679","praiseUserIdList":[82002,1520242280259,82001,70793,1524042900591,1528264711016],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1531062713966,"userId":82002,"date":"2018-07-08 23:11:53.0","content":"云南好美啊( ◞˟૩˟)◞","praiseUserIdList":[82001,82005,38710,70793,93793,82003,1531969715979],"pictureList":["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072366455&di=c0d4b15b2c4b70aad49e6ae747f60742&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F3%2F57a2a41f57d09.jpg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072499167&di=5b5621d117edbc5d344a03ba0a6b580b&imgtype=0&src=http%3A%2F%2Fi0.szhomeimg.com%2FUploadFiles%2FBBS%2F2006%2F08%2F05%2F24752199_79122.91.jpg"]},{"id":1536805585275,"userId":82003,"date":"2018-09-13 10:26:25.0","content":"iPhone Xs发布了，大家怎么看？","praiseUserIdList":[82002,82005,70793,82003,82001],"pictureList":["https://pic1.zhimg.com/80/v2-e129b40810070443add1c28e6185c894_hd.jpg"]},{"id":1545564986045,"userId":82003,"date":"2018-12-23 19:36:26.0","content":"测试新增动态","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/user/48/96331_50.jpg"]},{"id":1548145750536,"userId":82003,"date":"2019-01-22 16:29:10.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[82001],"pictureList":[]}],"code":200,"msg":"success","time:start/duration/end":"1556501638963/39/1556501639002","query:depth/max":"3/5","sql:generate/cache/execute/maxExecute":"20/16/4/200"}', null, '2018-03-24 16:03:13.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907546128, 0, 2, '获取类似微信朋友圈的动态列表', '/get', '{
    "[]": {
        "page": 0,
        "count": 2,
        "Moment": {
            "content$": "%a%"
        },
        "User": {
            "id@": "/Moment/userId",
            "@column": "id,name,head"
        },
        "Comment[]": {
            "count": 2,
            "Comment": {
                "momentId@": "[]/Moment/id"
            }
        }
    }
}', '{"[]":[{"Moment":{"id":12,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[70793,93793,82044,82040,82055,90814,38710,82002,82006,1508072105320,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067","http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment[]":[{"id":162,"toId":0,"userId":93793,"momentId":12,"date":"2017-03-06 13:03:45.0","content":"This is a Content...-162"},{"id":164,"toId":0,"userId":93793,"momentId":12,"date":"2017-03-06 13:03:45.0","content":"This is a Content...-164"}]},{"Moment":{"id":15,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON is a JSON Transmission Structure Protocol…","praiseUserIdList":[82002,70793,38710,93793,82001],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment[]":[{"id":176,"toId":166,"userId":38710,"momentId":15,"date":"2017-03-25 20:28:03.0","content":"thank you"},{"id":1490863469638,"toId":0,"userId":82002,"momentId":15,"date":"2017-03-30 16:44:29.0","content":"Just do it"}]}],"code":200,"msg":"success","time:start/duration/end":"1556501638962/38/1556501639000","query:depth/max":"5/5","sql:generate/cache/execute/maxExecute":"8/4/4/200"}', null, '2018-03-24 16:05:46.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907570451, 0, 2, '获取动态及发布者用户', '/get', '{
    "Moment": {},
    "User": {
        "id@": "Moment/userId"
    }
}', '{"Moment":{"id":12,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON,let interfaces and documents go to hell !","praiseUserIdList":[70793,93793,82044,82040,82055,90814,38710,82002,82006,1508072105320,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067","http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067"]},"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success","time:start/duration/end":"1556501638952/40/1556501638992","query:depth/max":"1/5","sql:generate/cache/execute/maxExecute":"2/0/2/200"}', null, '2018-03-24 16:06:10.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907587429, 0, 2, '获取用户列表', '/get', '{
    "[]": {
        "count": 3,
        "User": {
            "@column": "id,name"
        }
    }
}', '{"[]":[{"User":{"id":38710,"name":"TommyLemon"}},{"User":{"id":70793,"name":"Strong"}},{"User":{"id":82001,"name":"测试账号"}}],"code":200,"msg":"success","time:start/duration/end":"1556501638960/37/1556501638997","query:depth/max":"3/5","sql:generate/cache/execute/maxExecute":"3/2/1/200"}', null, '2018-03-24 16:06:27.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1521907601298, 0, 2, '获取用户', '/get', '{
  "User":{
  }
}', '{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success","time:start/duration/end":"1556501638955/17/1556501638972","query:depth/max":"1/5","sql:generate/cache/execute/maxExecute":"1/0/1/200"}', null, '2018-03-24 16:06:41.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1569382296303, 82001, 2, '登录', '/login', '{
    "type": 0,
    "phone": "13000082001",
    "password": "123456",
    "version": 1,
    "remember": false,
    "defaults": {
        "@database": "MYSQL",
        "@schema": "sys"
    }
}', null, '', '2019-09-25 03:31:36.000000');
INSERT INTO sys."Document" (id, "userId", version, name, url, request, response, header, date) VALUES (1569382305979, 82001, 2, '测试查询', '/get', '{
    "User": {
        "id": 82001
    },
    "[]": {
        "Comment": {
            "userId@": "User/id"
        }
    }
}', null, '', '2019-09-25 03:31:45.000000');
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
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (10, 'deleteCommentOfMoment', 'momentId', '{"momentId": 1}', '根据动态 id 删除它的所有评论', '2019-08-17 18:46:56.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (11, 'verifyIdList', 'array', '{"array": [1, 2, 3], "result()": "verifyIdList(array)"}', '校验类型为 id 列表', '2019-08-17 19:58:33.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (12, 'verifyURLList', 'array', '{"array": ["http://123.com/1.jpg", "http://123.com/a.png", "http://www.abc.com/test.gif"], "result()": "verifyURLList(array)"}', '校验类型为 URL 列表', '2019-08-17 19:58:33.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (13, 'getWithDefault', 'value,defaultValue', '{"value": null, "defaultValue": 1}', '如果 value 为 null，则返回 defaultValue', '2019-08-20 15:26:36.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (14, 'removeKey', 'key', '{"key": "s", "key2": 2}', '从对象里移除 key', '2019-08-20 15:26:36.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (15, 'getFunctionDemo', null, '{}', '获取远程函数的 Demo', '2019-08-20 15:26:36.000000', null, null, 0, 'Object');
INSERT INTO sys."Function" (id, name, arguments, demo, detail, date, back, requestlist, "userId", type) VALUES (16, 'getFunctionDetail', null, '{}', '获取远程函数的详情', '2019-08-20 15:26:36.000000', null, null, 0, 'Object');
create table "Login"
(
    id       bigint       not null
        constraint "Login_pkey"
            primary key,
    "userId" bigint       not null,
    type     smallint     not null,
    date     timestamp(6) not null
);

comment on table "Login" is '@deprecated，登录信息存session';

comment on column "Login".id is '唯一标识';

comment on column "Login"."userId" is '用户id';

comment on column "Login".type is '类型
0-密码登录
1-验证码登录';

comment on column "Login".date is '创建日期';

alter table "Login"
    owner to postgres;

INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488365732208, 0, 0, '2017-03-01 10:55:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488379391681, 1488378558927, 0, '2017-03-01 14:43:11.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488379908786, 1488378449469, 0, '2017-03-01 14:51:48.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488379961820, 1488379935755, 0, '2017-03-01 14:52:41.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488386227319, 1488380023998, 0, '2017-03-01 16:37:07.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488387166592, 1488378449469, 0, '2017-03-01 16:52:46.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488423710531, 1488423676823, 0, '2017-03-02 03:01:50.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488428867991, 1488428734202, 0, '2017-03-02 04:27:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488473261705, 1488473066471, 0, '2017-03-02 16:47:41.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488516623869, 1488378449469, 0, '2017-03-03 04:50:23.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488540997715, 1488540991808, 0, '2017-03-03 11:36:37.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488541075533, 1488541028865, 0, '2017-03-03 11:37:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488541560585, 1488541531131, 0, '2017-03-03 11:46:00.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488569711657, 1488569508197, 0, '2017-03-03 19:35:11.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488569782719, 1488569732797, 0, '2017-03-03 19:36:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488569807192, 1488569798561, 0, '2017-03-03 19:36:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488572273190, 1488572225956, 0, '2017-03-03 20:17:53.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488572823466, 1488569798561, 0, '2017-03-03 20:27:03.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488572844863, 1488572838263, 0, '2017-03-03 20:27:24.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488572852849, 1488572838263, 0, '2017-03-03 20:27:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488572994566, 1488572838263, 0, '2017-03-03 20:29:54.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488602587483, 1488602583693, 0, '2017-03-04 04:43:07.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488602732477, 1488602583693, 0, '2017-03-04 04:45:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488602739644, 1488569508197, 0, '2017-03-04 04:45:39.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488616804093, 82012, 0, '2017-03-04 09:30:21.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488619853762, 82012, 0, '2017-03-04 09:30:53.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488619853763, 1488621574081, 0, '2017-03-04 09:59:34.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488621807871, 1488621574081, 0, '2017-03-04 10:03:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488621807872, 1488621574081, 0, '2017-03-04 10:03:43.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488621827734, 1488621574081, 0, '2017-03-04 10:03:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488621827735, 1488621574081, 0, '2017-03-04 10:04:03.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488621846267, 1488621574081, 0, '2017-03-04 10:04:06.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488621846268, 1488621873562, 0, '2017-03-04 10:04:33.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488621876782, 1488621873562, 0, '2017-03-04 10:04:36.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488621876783, 1488621904086, 0, '2017-03-04 10:05:04.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488622533567, 1488621904086, 0, '2017-03-04 10:15:33.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488622533568, 1488622827857, 0, '2017-03-04 10:20:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488622831418, 1488622827857, 0, '2017-03-04 10:20:31.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488622831419, 1488473066471, 0, '2017-03-04 10:21:52.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488622919890, 1488473066471, 0, '2017-03-04 10:21:59.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488622919891, 1488622959038, 0, '2017-03-04 10:22:39.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488623021260, 1488622959038, 0, '2017-03-04 10:23:41.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488623021261, 1488622959038, 0, '2017-03-04 10:25:02.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488623107782, 1488622959038, 0, '2017-03-04 10:25:07.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488623107783, 1488622959038, 0, '2017-03-04 14:23:31.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488638599393, 1488622959038, 0, '2017-03-04 14:43:19.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488638599394, 1488622959038, 0, '2017-03-04 15:07:50.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488640073476, 1488622959038, 0, '2017-03-04 15:07:53.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488640255126, 1488640277910, 0, '2017-03-04 15:11:18.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488640325578, 1488640277910, 0, '2017-03-04 15:12:05.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488640325579, 1488640277910, 0, '2017-03-04 15:12:08.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488640330490, 1488640277910, 0, '2017-03-04 15:12:10.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488640330491, 1488640277910, 0, '2017-03-04 15:59:25.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488643309485, 1488640277910, 0, '2017-03-04 16:01:49.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488643309486, 1488643325534, 0, '2017-03-04 16:02:05.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488643330578, 1488643325534, 0, '2017-03-04 16:02:10.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488643414031, 1488643442503, 0, '2017-03-04 16:04:02.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488643446184, 1488643442503, 0, '2017-03-04 16:04:06.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488645359252, 82012, 0, '2017-03-04 16:43:41.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488645825647, 82012, 0, '2017-03-04 16:43:45.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488645825648, 82012, 0, '2017-03-04 16:44:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488645964496, 82012, 0, '2017-03-04 16:46:04.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488645964497, 82012, 0, '2017-03-04 16:46:06.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488645968694, 82012, 0, '2017-03-04 16:46:08.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707458563, 1488643442503, 0, '2017-03-05 09:51:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707494290, 1488643442503, 0, '2017-03-05 09:51:34.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707494291, 1488707511472, 0, '2017-03-05 09:51:51.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707514358, 1488707511472, 0, '2017-03-05 09:51:54.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707514359, 1488707511472, 1, '2017-03-05 09:52:15.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707539344, 1488707511472, 0, '2017-03-05 09:52:19.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707539345, 1488707572184, 0, '2017-03-05 09:52:52.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707575181, 1488707572184, 0, '2017-03-05 09:52:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707575182, 1488707617655, 0, '2017-03-05 09:53:37.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707626071, 1488707617655, 0, '2017-03-05 09:53:46.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707626072, 1488707617655, 0, '2017-03-05 09:53:52.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707635801, 1488707617655, 0, '2017-03-05 09:53:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707635802, 1488707617655, 0, '2017-03-05 09:57:26.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707850222, 1488707617655, 0, '2017-03-05 09:57:30.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707850223, 1488707874944, 0, '2017-03-05 09:57:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707877660, 1488707874944, 0, '2017-03-05 09:57:57.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707877661, 1488707874944, 1, '2017-03-05 09:58:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488707915649, 1488707874944, 0, '2017-03-05 09:58:35.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488727516722, 1488727542397, 0, '2017-03-05 15:25:42.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488727548031, 1488727542397, 0, '2017-03-05 15:25:48.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488803302239, 1488727542397, 0, '2017-03-06 12:28:24.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488803306640, 1488727542397, 0, '2017-03-06 12:28:26.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488803306641, 1488803343874, 0, '2017-03-06 12:29:04.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488803346374, 1488803343874, 0, '2017-03-06 12:29:06.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488803346375, 1488803343874, 0, '2017-03-06 15:06:09.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1488812773144, 1488803343874, 0, '2017-03-06 15:06:13.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489244600336, 1489244640435, 0, '2017-03-11 15:04:00.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489244647438, 1489244640435, 0, '2017-03-11 15:04:07.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489244647439, 1489244640435, 1, '2017-03-11 15:04:25.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489244669153, 1489244640435, 0, '2017-03-11 15:04:29.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489246281612, 1489244640435, 0, '2017-03-11 15:31:37.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489246300085, 1489244640435, 0, '2017-03-11 15:31:40.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489246300086, 1489244640435, 0, '2017-03-11 15:32:00.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489246323014, 1489244640435, 0, '2017-03-11 15:32:03.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489246323015, 1489246345610, 0, '2017-03-11 15:32:25.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489246350667, 1489246345610, 0, '2017-03-11 15:32:30.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489298452742, 1488727542397, 0, '2017-03-12 06:01:02.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489298464822, 1488727542397, 0, '2017-03-12 06:01:04.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489298464823, 1489298483829, 0, '2017-03-12 06:01:23.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489298490008, 1489298483829, 0, '2017-03-12 06:01:30.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489298490009, 82005, 0, '2017-03-12 06:02:12.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489298931649, 82005, 0, '2017-03-12 06:08:53.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489298971069, 82005, 0, '2017-03-12 06:09:31.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489298971070, 82005, 0, '2017-03-12 06:09:40.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489299084011, 82005, 0, '2017-03-12 06:11:24.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489299139305, 90814, 0, '2017-03-12 06:12:23.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489317763943, 1489317784114, 0, '2017-03-12 11:23:04.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489317856607, 1489317784114, 0, '2017-03-12 11:24:16.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489934937901, 1489934955220, 0, '2017-03-19 14:49:15.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1489934967736, 1489934955220, 0, '2017-03-19 14:49:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490105370959, 1490105418731, 0, '2017-03-21 14:10:18.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490105432172, 1490105418731, 0, '2017-03-21 14:10:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490109211714, 1490109742863, 0, '2017-03-21 15:22:23.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490109746858, 1490109742863, 0, '2017-03-21 15:22:26.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490109746859, 1490109845208, 0, '2017-03-21 15:24:05.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490109847412, 1490109845208, 0, '2017-03-21 15:24:07.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490109847413, 1490109845208, 1, '2017-03-21 15:25:39.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490109943463, 1490109845208, 0, '2017-03-21 15:25:43.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490420549513, 1488707874944, 0, '2017-03-25 05:43:30.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490420612726, 1488707874944, 0, '2017-03-25 05:43:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490420612727, 1490420651686, 0, '2017-03-25 05:44:11.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490420694018, 1490420651686, 0, '2017-03-25 05:44:54.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490425995551, 1490427139175, 0, '2017-03-25 07:32:19.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427142481, 1490427139175, 0, '2017-03-25 07:32:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427142482, 1490427139175, 0, '2017-03-25 07:32:25.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427147907, 1490427139175, 0, '2017-03-25 07:32:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427147908, 1490427139175, 1, '2017-03-25 07:32:46.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427169820, 1490427139175, 0, '2017-03-25 07:32:49.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427169821, 1490427139175, 1, '2017-03-25 07:36:09.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427460928, 1490427139175, 0, '2017-03-25 07:37:40.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427550424, 1490427577823, 0, '2017-03-25 07:39:37.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490427581040, 1490427577823, 0, '2017-03-25 07:39:41.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490584927873, 1490584952968, 0, '2017-03-27 03:22:33.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490584967616, 1490584952968, 0, '2017-03-27 03:22:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490585175679, 1490585192903, 0, '2017-03-27 03:26:33.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490585175680, 1490585226494, 0, '2017-03-27 03:27:06.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490585175681, 1490586230028, 0, '2017-03-27 03:43:50.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490586242829, 1490586230028, 0, '2017-03-27 03:44:02.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490586242830, 1490586417277, 0, '2017-03-27 03:46:57.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490586420868, 1490586417277, 0, '2017-03-27 03:47:00.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490586420869, 1490587219677, 0, '2017-03-27 04:00:20.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490587222853, 1490587219677, 0, '2017-03-27 04:00:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490587222854, 1490587219677, 0, '2017-03-27 04:00:30.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490587232018, 1490587219677, 0, '2017-03-27 04:00:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490763654616, 1489317784114, 0, '2017-03-29 05:01:03.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490763665719, 1489317784114, 0, '2017-03-29 05:01:05.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490763665720, 1490763680229, 0, '2017-03-29 05:01:20.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490763684749, 1490763680229, 0, '2017-03-29 05:01:24.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490763684750, 1490763889677, 0, '2017-03-29 05:04:49.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490763892907, 1490763889677, 0, '2017-03-29 05:04:52.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490763892908, 1490763889677, 1, '2017-03-29 05:09:04.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490764146839, 1490763889677, 0, '2017-03-29 05:09:06.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490764146840, 1490763889677, 0, '2017-03-29 05:09:17.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490764160920, 1490763889677, 0, '2017-03-29 05:09:20.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490796426168, 1490796536716, 0, '2017-03-29 14:08:56.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490796539768, 1490796536716, 0, '2017-03-29 14:08:59.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490796539769, 1490796536716, 1, '2017-03-29 14:09:25.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490796612462, 1490796536716, 0, '2017-03-29 14:10:12.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490796612463, 1490796536716, 0, '2017-03-29 14:10:14.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490797130482, 1490796536716, 0, '2017-03-29 14:18:50.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490797130483, 1490796536716, 0, '2017-03-29 14:21:17.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490799078694, 1490796536716, 0, '2017-03-29 14:51:18.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490799078695, 1490796536716, 0, '2017-03-29 15:04:49.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863478648, 82003, 0, '2017-03-30 08:44:40.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863574695, 82003, 0, '2017-03-30 08:46:14.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863574696, 82005, 0, '2017-03-30 08:46:16.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863617906, 82005, 0, '2017-03-30 08:46:57.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863617907, 70793, 1, '2017-03-30 08:47:12.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863735458, 70793, 0, '2017-03-30 08:48:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863735459, 93793, 0, '2017-03-30 08:49:01.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863793209, 93793, 0, '2017-03-30 08:49:53.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490863793210, 82006, 0, '2017-03-30 08:50:07.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490864162242, 82006, 0, '2017-03-30 08:56:02.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490864162243, 82044, 1, '2017-03-30 08:56:39.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490864359458, 82044, 0, '2017-03-30 08:59:19.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490874790302, 82040, 0, '2017-03-30 11:53:14.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490874856899, 82040, 0, '2017-03-30 11:54:16.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490874856900, 82055, 0, '2017-03-30 11:54:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490875711368, 82055, 0, '2017-03-30 12:08:31.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490875711369, 82056, 0, '2017-03-30 12:08:37.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490875721491, 82056, 0, '2017-03-30 12:08:41.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490875721492, 82060, 0, '2017-03-30 12:08:43.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490875725467, 82060, 0, '2017-03-30 12:08:45.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490875725468, 1490875855144, 0, '2017-03-30 12:10:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490875857334, 1490875855144, 0, '2017-03-30 12:10:57.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490880027108, 82054, 0, '2017-03-30 13:20:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490880030859, 82054, 0, '2017-03-30 13:20:30.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490880030860, 1490880220255, 0, '2017-03-30 13:23:40.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490880236865, 1490880220255, 0, '2017-03-30 13:23:56.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490880236866, 1490880254410, 0, '2017-03-30 13:24:14.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490880256555, 1490880254410, 0, '2017-03-30 13:24:16.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490973548451, 1490973670928, 0, '2017-03-31 15:21:11.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490974102842, 1490973670928, 0, '2017-03-31 15:28:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490974212206, 70793, 0, '2017-03-31 15:30:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1490974347168, 70793, 0, '2017-03-31 15:32:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491014963729, 82049, 0, '2017-04-01 02:49:29.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491014970908, 82049, 0, '2017-04-01 02:49:30.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491014970909, 82051, 0, '2017-04-01 02:49:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491014975055, 82051, 0, '2017-04-01 02:49:35.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491014975056, 1490420651686, 0, '2017-04-01 02:49:37.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491014978929, 1490420651686, 0, '2017-04-01 02:49:38.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491014978930, 1491015049274, 0, '2017-04-01 02:50:49.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491015064226, 1491015049274, 0, '2017-04-01 02:51:04.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491015064227, 70793, 0, '2017-04-01 02:57:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491130974601, 82049, 0, '2017-04-02 11:03:06.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491130988304, 82049, 0, '2017-04-02 11:03:08.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491130988305, 82050, 0, '2017-04-02 11:03:10.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491130992091, 82050, 0, '2017-04-02 11:03:12.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491130992092, 1490420651686, 0, '2017-04-02 11:03:13.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491130996226, 1490420651686, 0, '2017-04-02 11:03:16.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491130996227, 1491131016872, 0, '2017-04-02 11:03:37.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491131020967, 1491131016872, 0, '2017-04-02 11:03:40.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491131114629, 1491131208618, 0, '2017-04-02 11:06:48.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491131215621, 1491131208618, 0, '2017-04-02 11:06:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491131215622, 1491131208618, 0, '2017-04-02 12:32:26.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491136484469, 1491131208618, 0, '2017-04-02 12:34:44.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491137049692, 1491137170621, 0, '2017-04-02 12:46:10.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491137175158, 1491137170621, 0, '2017-04-02 12:46:15.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491137175159, 70793, 0, '2017-04-02 12:46:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210186666, 82046, 0, '2017-04-03 09:05:37.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210340156, 82046, 0, '2017-04-03 09:05:40.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210340157, 82041, 0, '2017-04-03 09:05:41.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210344197, 82041, 0, '2017-04-03 09:05:44.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210344198, 1491210361659, 1, '2017-04-03 09:06:23.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210385826, 1491210361659, 0, '2017-04-03 09:06:25.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210385827, 1491210948591, 0, '2017-04-03 09:15:48.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210951970, 1491210948591, 0, '2017-04-03 09:15:51.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210951971, 1491210948591, 1, '2017-04-03 09:16:01.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210964359, 1491210948591, 0, '2017-04-03 09:16:04.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210964360, 1491210948591, 0, '2017-04-03 09:16:07.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491210969546, 1491210948591, 0, '2017-04-03 09:16:09.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491231490642, 82003, 0, '2017-04-03 14:58:13.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491231560497, 82003, 0, '2017-04-03 14:59:20.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491231560498, 93793, 0, '2017-04-03 14:59:31.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491231602048, 93793, 0, '2017-04-03 15:00:02.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491231602049, 93793, 0, '2017-04-03 15:09:42.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491232187135, 93793, 0, '2017-04-03 15:09:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491278106043, 1490109742863, 0, '2017-04-04 03:55:15.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491278117918, 1490109742863, 0, '2017-04-04 03:55:17.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491278117919, 1490427577823, 0, '2017-04-04 03:55:19.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491278121481, 1490427577823, 0, '2017-04-04 03:55:21.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491278121482, 1491278203315, 0, '2017-04-04 03:56:43.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491283571224, 1491278203315, 0, '2017-04-04 05:26:11.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491283708324, 1491210314249, 1, '2017-04-04 05:28:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491283800948, 1491210314249, 0, '2017-04-04 05:30:00.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491706177615, 1491706263570, 0, '2017-04-09 02:51:03.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491713830487, 1491713931091, 0, '2017-04-09 04:58:51.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491713972850, 1491713931091, 0, '2017-04-09 04:59:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491728210153, 1490427139175, 0, '2017-04-09 08:56:53.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491728216317, 1490427139175, 0, '2017-04-09 08:56:56.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491728216318, 82054, 0, '2017-04-09 08:56:58.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491728221137, 82054, 0, '2017-04-09 08:57:01.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491728221138, 1491728303733, 0, '2017-04-09 08:58:23.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491728316688, 1491728303733, 0, '2017-04-09 08:58:36.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491798585269, 1490420651686, 0, '2017-04-10 04:30:17.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491798619163, 1490420651686, 0, '2017-04-10 04:30:19.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491798619164, 1491015049274, 0, '2017-04-10 04:30:21.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491798623156, 1491015049274, 0, '2017-04-10 04:30:23.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491798623157, 1491798705995, 0, '2017-04-10 04:31:46.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491798824157, 1491798705995, 0, '2017-04-10 04:33:44.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491830822528, 1491830893899, 0, '2017-04-10 13:28:14.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491831356223, 1491830893899, 0, '2017-04-10 13:35:56.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491838437130, 1491838521279, 0, '2017-04-10 15:35:21.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491838535040, 1491838521279, 0, '2017-04-10 15:35:35.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491917352614, 1491728303733, 0, '2017-04-11 13:29:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491917364596, 1491728303733, 0, '2017-04-11 13:29:24.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491917364597, 1491917447333, 0, '2017-04-11 13:30:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1491917916123, 1491917447333, 0, '2017-04-11 13:38:36.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492865677465, 82058, 0, '2017-04-22 12:54:45.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492865687807, 82058, 0, '2017-04-22 12:54:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492865687808, 1492866224074, 0, '2017-04-22 13:03:44.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492866227861, 1492866224074, 0, '2017-04-22 13:03:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492866227862, 1492866224074, 0, '2017-04-22 13:03:52.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492866235005, 1492866224074, 0, '2017-04-22 13:03:55.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492866235006, 1492866322486, 0, '2017-04-22 13:05:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492866325550, 1492866322486, 0, '2017-04-22 13:05:25.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492936150349, 1492936169722, 0, '2017-04-23 08:29:30.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492936172897, 1492936169722, 0, '2017-04-23 08:29:32.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492936172898, 1492936169722, 0, '2017-04-23 08:33:44.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492936427137, 1492936169722, 0, '2017-04-23 08:33:47.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492936427138, 1492936169722, 0, '2017-04-23 08:37:29.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1492936651770, 1492936169722, 0, '2017-04-23 08:37:31.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493456282571, 90814, 0, '2017-04-29 08:58:09.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493457036233, 90814, 0, '2017-04-29 09:10:36.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480121888, 1490427139175, 0, '2017-04-29 15:35:26.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480129111, 1490427139175, 0, '2017-04-29 15:35:29.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480129112, 1493480142628, 0, '2017-04-29 15:35:42.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480148564, 1493480142628, 0, '2017-04-29 15:35:48.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480148565, 1493480142628, 0, '2017-04-29 15:35:54.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480156757, 1493480142628, 0, '2017-04-29 15:35:56.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480156758, 90814, 0, '2017-04-29 15:36:01.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480162695, 90814, 0, '2017-04-29 15:36:02.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480162696, 93793, 0, '2017-04-29 15:36:06.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493480189011, 93793, 0, '2017-04-29 15:36:29.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493747501699, 1493747512860, 0, '2017-05-02 17:51:53.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493747519493, 1493747512860, 0, '2017-05-02 17:51:59.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493747519494, 1493747777770, 0, '2017-05-02 17:56:17.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493747780534, 1493747777770, 0, '2017-05-02 17:56:20.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493748571679, 1493748594003, 0, '2017-05-02 18:09:54.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493748596459, 1493748594003, 0, '2017-05-02 18:09:56.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493748596460, 1493748615711, 0, '2017-05-02 18:10:15.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493748617966, 1493748615711, 0, '2017-05-02 18:10:17.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493748617967, 1493749090643, 0, '2017-05-02 18:18:10.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493749100206, 1493749090643, 0, '2017-05-02 18:18:20.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493836047659, 1493836043151, 0, '2017-05-03 18:27:27.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493836049490, 1493836043151, 0, '2017-05-03 18:27:29.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493883116023, 1493883110132, 0, '2017-05-04 07:31:56.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493883118007, 1493883110132, 0, '2017-05-04 07:31:58.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493890214303, 1493890214167, 0, '2017-05-04 09:30:14.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493890216183, 1493890214167, 0, '2017-05-04 09:30:16.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493890699755, 1493890303473, 0, '2017-05-04 09:38:19.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493890702129, 1493890303473, 0, '2017-05-04 09:38:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493891565732, 82001, 0, '2017-05-04 09:52:45.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493891782837, 82001, 0, '2017-05-04 09:56:22.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493891784591, 82002, 0, '2017-05-04 09:56:24.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493891793881, 82002, 0, '2017-05-04 09:56:33.000000');
INSERT INTO sys."Login" (id, "userId", type, date) VALUES (1493891806372, 38710, 1, '2017-05-04 09:56:46.000000');
create table "Moment"
(
    id                 bigint not null
        constraint "Moment_pkey"
            primary key,
    "userId"           bigint not null,
    date               timestamp(6),
    content            varchar(300),
    "praiseUserIdList" jsonb  not null,
    "pictureList"      jsonb  not null
);

comment on table "Moment" is '动态';

comment on column "Moment".id is '唯一标识';

comment on column "Moment"."userId" is '用户id';

comment on column "Moment".date is '创建日期';

comment on column "Moment".content is '内容';

comment on column "Moment"."praiseUserIdList" is '点赞的用户id列表';

comment on column "Moment"."pictureList" is '图片列表';

alter table "Moment"
    owner to postgres;

INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (12, 70793, '2017-02-08 08:06:11.000000', 'APIJSON,let interfaces and documents go to hell !', '[70793, 93793, 82044, 82040, 82055, 90814, 38710, 82002, 82006, 1508072105320, 82001]', '["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg", "http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg", "https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067", "http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png", "https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067", "https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067", "https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067", "https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (32, 82002, '2017-02-08 08:06:11.000000', null, '[38710, 82002, 82001]', '["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067", "https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067", "http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (58, 90814, '2017-02-01 11:14:31.000000', 'This is a Content...-435', '[38710, 82003, 82005, 93793, 82006, 82044, 82001]', '["http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (170, 70793, '2017-02-01 11:14:31.000000', 'This is a Content...-73', '[82044, 82002, 82001]', '["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg", "http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg", "http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (301, 93793, '2017-02-01 11:14:31.000000', 'This is a Content...-301', '[38710, 93793, 82003, 82005, 82040, 82055, 82002, 82001]', '["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (371, 82002, '2017-02-01 11:14:31.000000', 'This is a Content...-371', '[90814, 93793, 82003, 82005, 82006, 82040, 82002, 82001]', '["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg", "http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg", "https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067", "http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg", "http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (470, 38710, '2017-02-01 11:14:31.000000', 'This is a Content...-470', '[82001]', '["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (511, 38710, '2017-02-08 08:06:11.000000', null, '[70793, 93793, 82001]', '["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067", "http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg", "https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067", "http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg", "http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg", "https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (543, 93793, '2017-02-08 08:06:11.000000', null, '[82001]', '["https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067", "https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067", "http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg", "http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg", "http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (551, 70793, '2017-02-08 08:06:11.000000', 'test', '[82001]', '["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (595, 38710, '2017-03-05 05:29:19.000000', null, '[70793, 82002, 82001]', '["http://common.cnblogs.com/images/icon_weibo_24.png", "http://static.oschina.net/uploads/user/19/39085_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (704, 38710, '2017-03-12 09:39:44.000000', 'APIJSON is a JSON Transmission Structure Protocol…', '[82003, 82002, 82001]', '["http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000", "http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1491200468898, 38710, '2017-04-03 06:21:08.000000', 'APIJSON, let interfaces go to hell!', '[82001]', '["http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000", "http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1493835799335, 38710, '2017-05-03 18:23:19.000000', 'APIJSON is a JSON Transmission Structure Protocol…', '[82002, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1508053762227, 82003, '2017-10-15 07:49:22.000000', '我也试试', '[1515565976140, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1508072491570, 82002, '2017-10-15 13:01:31.000000', '有点冷~', '[82001, 82002]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1548145750829, 82003, '2019-01-22 08:29:10.000000', '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1549017200628, 82001, '2019-02-01 10:33:20.000000', '说点什么吧~快捷键', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1508072633830, 93793, '2017-10-15 13:03:53.000000', '天凉了
有男朋友的抱男盆友
有女朋友的抱女朋友
而我就比较牛逼了
我不冷。', '[82005, 82002, 70793, 38710, 82045, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1508073178489, 82045, '2017-10-15 13:12:58.000000', '发动态', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1512314438990, 38710, '2017-12-03 15:20:38.000000', 'APIJSON iOS-Swift版发布，自动生成请求代码,欢迎使用^_^ 
 https://github.com/TommyLemon/APIJSON', '[82001, 82002, 70793, 1512531601485]', '["https://images2018.cnblogs.com/blog/660067/201712/660067-20171203231829476-1202860128.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1513094436910, 38710, '2017-12-12 16:00:36.000000', 'APIJSON-Python已发布，欢迎体验^_^
https://github.com/TommyLemon/APIJSON', '[82005, 82001]', '["https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/APIJSON_Auto_get.jpg", "https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/APIJSON_Auto_code.jpg", "https://raw.githubusercontent.com/TommyLemon/APIJSON/master/picture/APIJSON_Auto_doc.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1514017444961, 82002, '2017-12-23 08:24:04.000000', '123479589679', '[82002, 1520242280259, 82001, 70793, 1524042900591, 1528264711016]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1514858533480, 82056, '2018-01-02 02:02:13.000000', 'I am the Iron Man', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1516086423441, 93793, '2018-01-16 07:07:03.000000', '抢到票了，开心ପ( ˘ᵕ˘ ) ੭ ☆', '[93793, 38710, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1520242333325, 1520242280259, '2018-03-05 09:32:13.000000', '法拉利', '[1520242280259, 70793, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1523935589834, 1523626157302, '2018-04-17 03:26:29.000000', 'by第一条动态', '[]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1523936332614, 1523626157302, '2018-04-17 03:38:52.000000', 'by第二条', '[82001, 1523935772553]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1524178455305, 1524042900591, '2018-04-19 22:54:15.000000', '早上好啊', '[1524042900591, 38710, 82003, 82001, 1523626157302]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1524298780222, 1524298730523, '2018-04-21 08:19:40.000000', 'e说点什么吧~', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1524826652626, 1524298730523, '2018-04-27 10:57:32.000000', '说点什么吧~哈哈哈', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1527498273102, 1527498229991, '2018-05-28 09:04:33.000000', '说点什么吧~yui', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1527821296110, 1527495857924, '2018-06-01 02:48:16.000000', '这是我的商品1号', '[1527821445610, 82003, 82001]', '["http://pic31.nipic.com/20130710/13151003_093759013311_2.jpg", "https://cbu01.alicdn.com/img/ibank/2013/514/580/740085415_2101098104.310x310.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1527830331780, 1527495857924, '2018-06-01 05:18:51.000000', '各种购物袋', '[38710, 82002, 1527495857924, 82003]', '["https://cbu01.alicdn.com/img/ibank/2018/292/335/8058533292_57202994.310x310.jpg", "https://cbu01.alicdn.com/img/ibank/2018/089/747/8586747980_1843977904.310x310.jpg", "https://cbu01.alicdn.com/img/ibank/2016/025/123/3012321520_471514049.310x310.jpg", "https://cbu01.alicdn.com/img/ibank/2017/729/995/4800599927_69233977.310x310.jpg", "https://cbu01.alicdn.com/img/ibank/2016/377/263/3755362773_609022431.310x310.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1527830474378, 1527495857924, '2018-06-01 05:21:14.000000', '电视机', '[1527495857924]', '["https://cbu01.alicdn.com/img/ibank/2017/231/077/4524770132_781046171.310x310.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528251335464, 1528250827953, '2018-06-06 02:15:35.000000', 'meiyou', '[1528250827953]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528260617722, 1528255497767, '2018-06-06 04:50:17.000000', '。。。。', '[]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528269822710, 1528264711016, '2018-06-06 07:23:42.000000', 'hhhhhhh', '[1528250827953, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528269988360, 1528250827953, '2018-06-06 07:26:28.000000', '为什么发动态默认会有这两张图片啊？不可以选择自己的图片', '[1528250827953]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528274037224, 1528250827953, '2018-06-06 08:33:57.000000', '说点什么吧~hgdsryh', '[]', '["https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E4%BA%91%E5%8D%97%E9%A3%8E%E6%99%AF%E5%9B%BE%E7%89%87&step_word=&hs=0&pn=0&spn=0&di=163958046450&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=1403824732%2C1921310327&os=1571651475%2C3950546936&simid=3426978648%2C550887139&adpicid=0&lpn=0&ln=1985&fr=&fmq=1528273681226_R&fm=result&ic=0&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F3%2F57a2a41f57d09.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Botg9aaa_z%26e3Bv54AzdH3Fowssrwrj6_kt2_88an88_8_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0&islist=&querylist=", "https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E4%BA%91%E5%8D%97%E9%A3%8E%E6%99%AF%E5%9B%BE%E7%89%87&step_word=&hs=0&pn=12&spn=0&di=105575240210&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=832573604%2C2847830718&os=1862795828%2C1682403963&simid=4268934412%2C608274877&adpicid=0&lpn=0&ln=1985&fr=&fmq=1528273681226_R&fm=result&ic=0&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fi0.szhomeimg.com%2FUploadFiles%2FBBS%2F2006%2F08%2F05%2F24752199_79122.91.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fkkf_z%26e3Bfzi54j_z%26e3Bv54AzdH3Fna-ccbaa-1jpwts-d90cd8ll-a-8_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0&islist=&querylist="]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528356378455, 1528264711016, '2018-06-07 07:26:18.000000', '去旅游嘛～', '[82001]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528356421201, 1528264711016, '2018-06-07 07:27:01.000000', '(ง •̀_•́)ง', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528356496939, 1528356470041, '2018-06-07 07:28:16.000000', '(๑•ั็ω•็ั๑)', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528462217322, 1528339692804, '2018-06-08 12:50:17.000000', '有没有小姐姐准备端午出游的？
地点:北戴河', '[]', '["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072366455&di=c0d4b15b2c4b70aad49e6ae747f60742&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F3%2F57a2a41f57d09.jpg", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072499167&di=5b5621d117edbc5d344a03ba0a6b580b&imgtype=0&src=http%3A%2F%2Fi0.szhomeimg.com%2FUploadFiles%2FBBS%2F2006%2F08%2F05%2F24752199_79122.91.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1528676875139, 1528339692804, '2018-06-11 00:27:55.000000', '123456', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1531062713966, 82002, '2018-07-08 15:11:53.000000', '云南好美啊( ◞˟૩˟)◞', '[82001, 82005, 38710, 70793, 93793, 82003, 1531969715979]', '["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072366455&di=c0d4b15b2c4b70aad49e6ae747f60742&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F3%2F57a2a41f57d09.jpg", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072499167&di=5b5621d117edbc5d344a03ba0a6b580b&imgtype=0&src=http%3A%2F%2Fi0.szhomeimg.com%2FUploadFiles%2FBBS%2F2006%2F08%2F05%2F24752199_79122.91.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1531969818357, 1531969715979, '2018-07-19 03:10:18.000000', 'http://q18idc.com', '[1531969715979, 82001, 38710, 1534926301956]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1533908589726, 1533835176109, '2018-08-10 13:43:09.000000', '我的', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1535781636403, 1532188114543, '2018-09-01 06:00:36.000000', '这是一个测试', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1536064087117, 1532188114543, '2018-09-04 12:28:07.000000', '说点什么吧~奥哈达', '[]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1536805585275, 82003, '2018-09-13 02:26:25.000000', 'iPhone Xs发布了，大家怎么看？', '[82002, 82005, 70793, 82003]', '["https://pic1.zhimg.com/80/v2-e129b40810070443add1c28e6185c894_hd.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1539868023868, 82001, '2018-10-18 13:07:03.000000', '说点什么吧~3', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1540459349460, 82001, '2018-10-25 09:22:29.000000', '说点什么吧~而你', '[]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1540459361744, 82001, '2018-10-25 09:22:41.000000', '说点什么吧~哦哦', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1540634282433, 82001, '2018-10-27 09:58:02.000000', 'https://gss2.bdstatic.com/-fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=c31ae7219525bc313f5009ca3fb6e6d4/42a98226cffc1e17646dbede4690f603728de90b.jpg', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1541557989440, 38710, '2018-11-07 02:33:09.000000', '自动化接口和文档 APIJSON 3.0.0 发布
https://www.oschina.net/news/101548/apijson-3-0-released', '[82001]', '["https://raw.githubusercontent.com/TommyLemon/StaticResources/master/APIJSON_Auto_get.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1541667920272, 82001, '2018-11-08 09:05:20.000000', '说点什么吧~？？？', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1541667945772, 82001, '2018-11-08 09:05:45.000000', '说点什么吧~都有', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1541684010904, 82001, '2018-11-08 13:33:30.000000', '说点什么吧~A man can be destroyed but not defeated', '[82002, 38710, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1544265482923, 82001, '2018-12-08 10:38:02.000000', 'APIJSON is a JSON Transmission Structure Protocol…', '[82002, 82003, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1544276121218, 82001, '2018-12-08 13:35:21.000000', '说点什么吧~ajhs', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1544276216289, 1544276209348, '2018-12-08 13:36:56.000000', '说点什么吧~ey', '[1544276209348]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1544497353863, 82001, '2018-12-11 03:02:33.000000', 'APIJSON,let interfaces and documents go to hell !', '[82001]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1544497355630, 82001, '2018-12-11 03:02:35.000000', 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1544537838100, 82001, '2018-12-11 14:17:18.000000', '说点什么吧~logo灭了也可', '[]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1545187924367, 1544503822963, '2018-12-19 02:52:04.000000', '说哼哼唧唧点什么吧~', '[1544503822963, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1545490282104, 82001, '2018-12-22 14:51:22.000000', '说点什么吧~apijson', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1545560428650, 82001, '2018-12-23 10:20:28.000000', 'APIJSON,let interfaces and documents go to hell !', '[82001]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1545564986045, 82003, '2018-12-23 11:36:26.000000', '测试新增动态', '[82001]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1545819572448, 82002, '2018-12-26 10:19:32.000000', 'APIJSON,let interfaces and documents go to hell !', '[82001]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1545819572637, 82002, '2018-12-26 10:19:32.000000', '测试新增动态', '[82001]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1545819752556, 82001, '2018-12-26 10:22:32.000000', '说点什么吧~hello world', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1546934384440, 82001, '2019-01-08 07:59:44.000000', 'APIJSON,let interfaces and documents go to hell !', '[82001]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1547436860950, 82001, '2019-01-14 03:34:20.000000', '测试新增动态', '[82001]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1547479596460, 82001, '2019-01-14 15:26:36.000000', 'APIJSON,let interfaces and documents go to hell !', '[82001]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1547559758939, 82002, '2019-01-15 13:42:38.000000', 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1548145750536, 82003, '2019-01-22 08:29:10.000000', 'APIJSON,let interfaces and documents go to hell !', '[82001]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1548236953106, 82001, '2019-01-23 09:49:13.000000', '{@}', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1548300581350, 82002, '2019-01-24 03:29:41.000000', 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1548599361342, 82001, '2019-01-27 14:29:21.000000', '说点什么吧~好)不会徐下节课斜挎包', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1548599375288, 82001, '2019-01-27 14:29:35.000000', '说点什么吧~告诉v是v就瞎几把想表达华西坝你撒几哈', '[82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1548822634246, 82002, '2019-01-30 04:30:34.000000', 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1548822634518, 82002, '2019-01-30 04:30:34.000000', '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096399488, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1557721145964, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560093312615, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560093312921, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1558891221463, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1558891221788, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096269459, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094319618, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560093326180, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560092284174, 82003, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560092284508, 82003, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094319934, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560093326539, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094350737, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560092302827, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560092303319, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094351045, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094254821, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094329746, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560093270103, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560093270358, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096270069, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094330083, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094255148, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096575905, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096208964, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094315580, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096419027, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094315886, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096209271, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094334838, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560094335132, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096419392, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096295559, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120326228, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096295867, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096576289, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560690276182, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560096399446, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120257647, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120257326, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1560690275764, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120218108, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120224955, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120326572, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120386111, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120385763, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120405667, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561120405325, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561630616406, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1561630616075, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (235, 38710, '2017-02-08 08:06:11.000000', 'APIJSON,let interfaces and documents go to hell !', '[82001]', '["http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg", "http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1568458531964, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1568458532641, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (15, 70793, '2017-02-08 08:06:11.000000', 'APIJSON is a JSON Transmission Structure Protocol…', '[82002, 70793, 38710, 93793, 82001]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1568461871469, 82001, null, 'APIJSON,let interfaces and documents go to hell !', '[]', '[]');
INSERT INTO sys."Moment" (id, "userId", date, content, "praiseUserIdList", "pictureList") VALUES (1568461872933, 82001, null, '测试新增动态', '[]', '["http://static.oschina.net/uploads/user/48/96331_50.jpg"]');
create table "Praise"
(
    id         bigint not null
        constraint "Praise_pkey"
            primary key,
    "momentId" bigint not null,
    "userId"   bigint not null,
    date       timestamp(6)
);

comment on table "Praise" is '如果对Moment写安全要求高，可以将Moment内praiserUserIdList分离到Praise表中，作为userIdList。
权限注解也改下：
@MethodAccess(
		PUT = {OWNER, ADMIN}
		)
class Moment {
       …
}

@MethodAccess(
		PUT = {LOGIN, CONTACT, CIRCLE, OWNER, ADMIN}
		)
 class Praise {
       …
 }
';

comment on column "Praise".id is '动态id';

comment on column "Praise"."momentId" is '唯一标识';

comment on column "Praise"."userId" is '用户id';

comment on column "Praise".date is '点赞时间';

alter table "Praise"
    owner to postgres;

INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (1, 12, 82001, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (2, 15, 82002, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (3, 32, 82003, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (4, 58, 82004, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (5, 170, 82005, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (6, 235, 82006, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (7, 301, 82007, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (8, 371, 82008, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (9, 470, 82009, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (10, 511, 82010, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (11, 543, 82011, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (12, 551, 82012, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (13, 594, 82013, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (14, 595, 82014, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (15, 704, 82015, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (16, 1491200468898, 82016, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (17, 1491277116776, 82017, '2017-11-19 13:02:30.000000');
INSERT INTO sys."Praise" (id, "momentId", "userId", date) VALUES (18, 1493835799335, 82018, '2017-11-19 13:02:30.000000');
create table "Request"
(
    id        bigint      not null
        constraint "Request_pkey"
            primary key,
    version   smallint    not null,
    method    varchar(10),
    tag       varchar(20) not null,
    structure jsonb       not null,
    detail    varchar(10000),
    date      timestamp(6)
);

comment on table "Request" is '最好编辑完后删除主键，这样就是只读状态，不能随意更改。需要更改就重新加上主键。

每次启动服务器时加载整个表到内存。
这个表不可省略，model内注解的权限只是客户端能用的，其它可以保证即便服务端代码错误时也不会误删数据。';

comment on column "Request".id is '唯一标识';

comment on column "Request".version is 'GET,HEAD可用任意结构访问任意开放内容，不需要这个字段。
其它的操作因为写入了结构和内容，所以都需要，按照不同的version选择对应的structure。

自动化版本管理：
Request JSON最外层可以传 "version":Integer 。
1.未传或 <= 0，用最新版。 "@order":"version-"
2.已传且 > 0，用version以上的可用版本的最低版本。 "@order":"version+", "version{}":">={version}"';

comment on column "Request".method is '只限于GET,HEAD外的操作方法。';

comment on column "Request".tag is '标签';

comment on column "Request".structure is '结构';

comment on column "Request".detail is '详细说明';

comment on column "Request".date is '创建时间';

alter table "Request"
    owner to postgres;

INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (1, 1, 'POST', 'register', '{"User": {"UPDATE": {"id@": "Privacy/id"}, "DISALLOW": "id", "NECESSARY": "name"}, "Privacy": {"UNIQUE": "phone", "VERIFY": {"phone~": "phone"}, "DISALLOW": "id", "NECESSARY": "_password,phone"}}', 'UNIQUE校验phone是否已存在。VERIFY校验phone是否符合手机号的格式', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (2, 1, 'POST', 'Moment', '{"INSERT": {"@role": "OWNER", "pictureList": [], "praiseUserIdList": []}, "UPDATE": {"verifyIdList-()": "verifyIdList(praiseUserIdList)", "verifyURLList-()": "verifyURLList(pictureList)"}, "DISALLOW": "id"}', 'INSERT当没传pictureList和praiseUserIdList时用空数组[]补全，保证不会为null', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (3, 1, 'POST', 'Comment', '{"UPDATE": {"@role": "OWNER"}, "DISALLOW": "id", "NECESSARY": "momentId,content"}', '必须传userId,momentId,content，不允许传id', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (4, 1, 'PUT', 'User', '{"INSERT": {"@role": "OWNER"}, "DISALLOW": "phone", "NECESSARY": "id"}', '必须传id，不允许传phone。INSERT当没传@role时用OWNER补全', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (5, 1, 'DELETE', 'Moment', '{"Moment": {"INSERT": {"@role": "OWNER"}, "UPDATE": {"commentCount()": "deleteCommentOfMoment(id)"}, "NECESSARY": "id"}}', null, '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (6, 1, 'DELETE', 'Comment', '{"INSERT": {"@role": "OWNER"}, "UPDATE": {"childCount()": "deleteChildComment(id)"}, "NECESSARY": "id"}', 'disallow没必要用于DELETE', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (8, 1, 'PUT', 'User-phone', '{"User": {"INSERT": {"@role": "OWNER"}, "UPDATE": {"@combine": "_password"}, "DISALLOW": "!", "NECESSARY": "id,phone,_password"}}', '! 表示其它所有，这里指necessary所有未包含的字段', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (14, 1, 'POST', 'Verify', '{"DISALLOW": "!", "NECESSARY": "phone,verify"}', '必须传phone,verify，其它都不允许传', '2017-02-18 14:20:43.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (15, 1, 'GETS', 'Verify', '{"NECESSARY": "phone"}', '必须传phone', '2017-02-18 14:20:43.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (16, 1, 'HEADS', 'Verify', '{}', '允许任意内容', '2017-02-18 14:20:43.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (17, 1, 'PUT', 'Moment', '{"DISALLOW": "userId,date", "NECESSARY": "id"}', null, '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (21, 1, 'HEADS', 'Login', '{"DISALLOW": "!", "NECESSARY": "userId,type"}', null, '2017-02-18 14:20:43.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (22, 1, 'GETS', 'User', '{}', '允许传任何内容，除了表对象', '2017-02-18 14:20:43.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (23, 1, 'PUT', 'Privacy', '{"INSERT": {"@role": "OWNER"}, "NECESSARY": "id"}', 'INSERT当没传@role时用OWNER补全', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (25, 1, 'PUT', 'Praise', '{"NECESSARY": "id"}', '必须传id', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (26, 1, 'DELETE', 'Comment[]', '{"Comment": {"INSERT": {"@role": "OWNER"}, "NECESSARY": "id{}"}}', 'DISALLOW没必要用于DELETE', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (27, 1, 'PUT', 'Comment[]', '{"Comment": {"INSERT": {"@role": "OWNER"}, "NECESSARY": "id{}"}}', 'DISALLOW没必要用于DELETE', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (28, 1, 'PUT', 'Comment', '{"INSERT": {"@role": "OWNER"}, "NECESSARY": "id"}', '这里省略了Comment，因为tag就是Comment，Parser.getCorrectRequest会自动补全', '2017-02-01 11:19:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (29, 1, 'GETS', 'login', '{"Privacy": {"DISALLOW": "id", "NECESSARY": "phone,_password"}}', null, '2017-10-15 10:04:52.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (30, 1, 'PUT', 'balance+', '{"Privacy": {"VERIFY": {"balance+&{}": ">=1,<=100000"}, "DISALLOW": "!", "NECESSARY": "id,balance+"}}', '验证balance+对应的值是否满足>=1且<=100000', '2017-10-21 08:48:34.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (31, 1, 'PUT', 'balance-', '{"Privacy": {"UPDATE": {"@combine": "_password"}, "VERIFY": {"balance-&{}": ">=1,<=10000"}, "DISALLOW": "!", "NECESSARY": "id,balance-,_password"}}', 'UPDATE强制把_password作为WHERE条件', '2017-10-21 08:48:34.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (32, 2, 'GETS', 'Privacy', '{"INSERT": {"@role": "OWNER"}, "DISALLOW": "_password,_payPassword", "NECESSARY": "id"}', null, '2017-06-12 16:05:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (33, 2, 'GETS', 'Privacy-CIRCLE', '{"Privacy": {"UPDATE": {"@role": "CIRCLE", "@column": "phone"}, "DISALLOW": "!", "NECESSARY": "id"}}', null, '2017-06-12 16:05:51.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (35, 2, 'POST', 'Document', '{"Document": {"INSERT": {"@role": "OWNER"}, "DISALLOW": "id", "NECESSARY": "userId,name,url,request"}, "TestRecord": {"INSERT": {"@role": "OWNER"}, "UPDATE": {"documentId@": "Document/id"}, "DISALLOW": "id,documentId", "NECESSARY": "userId,response"}}', null, '2017-11-26 08:34:41.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (36, 2, 'PUT', 'Document', '{"DISALLOW": "userId", "NECESSARY": "id"}', null, '2017-11-26 08:35:15.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (37, 2, 'DELETE', 'Document', '{"INSERT": {"@role": "OWNER"}, "UPDATE": {"TestRecord": {"@role": "OWNER", "documentId@": "Document/id"}}, "DISALLOW": "!", "NECESSARY": "id"}', null, '2017-11-26 00:36:20.000000');
INSERT INTO sys."Request" (id, version, method, tag, structure, detail, date) VALUES (38, 2, 'POST', 'TestRecord', '{"DISALLOW": "id", "NECESSARY": "userId,documentId,response"}', null, '2018-06-16 23:44:36.000000');
create table "Response"
(
    id        bigint      not null
        constraint "Response_pkey"
            primary key,
    method    varchar(10),
    model     varchar(20) not null,
    structure text        not null,
    detail    varchar(10000),
    date      timestamp(6)
);

comment on table "Response" is '每次启动服务器时加载整个表到内存。';

comment on column "Response".id is '唯一标识';

comment on column "Response".method is '方法';

comment on column "Response".model is '表名，table是SQL关键词不能用';

comment on column "Response".structure is '结构';

comment on column "Response".detail is '详细说明';

comment on column "Response".date is '创建日期';

alter table "Response"
    owner to postgres;

create index "id_UNIQUE"
    on "Response" (id);

INSERT INTO sys."Response" (id, method, model, structure, detail, date) VALUES (1, 'GET', 'User', '{"put": {"extra": "Response works! Test:He(She) is lazy and wrote nothing here"}, "remove": "phone"}', null, '2017-05-22 12:36:47.000000');
INSERT INTO sys."Response" (id, method, model, structure, detail, date) VALUES (2, 'DELETE', 'Comment', '{"remove": "Comment:child"}', null, '2017-05-03 17:51:26.000000');
INSERT INTO sys."Response" (id, method, model, structure, detail, date) VALUES (3, 'DELETE', 'Moment', '{"remove": "Comment"}', null, '2017-05-03 17:51:26.000000');
create table "Test"
(
    id smallint not null
        constraint "Test_pkey"
            primary key
);

comment on table "Test" is '测试及验证用的表，可以用 SELECT condition替代 SELECT * FROM Test WHERE condition，这样就不需要这张表了';

alter table "Test"
    owner to postgres;

INSERT INTO sys."Test" (id) VALUES (1);
create table "TestRecord"
(
    id           bigint       not null
        constraint "TestRecord_pkey"
            primary key,
    "userId"     bigint       not null,
    "documentId" bigint       not null,
    response     text         not null,
    date         timestamp(6) not null,
    compare      text,
    standard     text
);

comment on table "TestRecord" is '测试记录
主要用于保存自动化接口回归测试';

comment on column "TestRecord".id is '唯一标识';

comment on column "TestRecord"."userId" is '用户id';

comment on column "TestRecord"."documentId" is '测试用例文档id';

comment on column "TestRecord".response is '接口返回结果JSON';

comment on column "TestRecord".date is '创建日期';

comment on column "TestRecord".compare is '对比结果';

comment on column "TestRecord".standard is 'response 的校验标准，是一个 JSON 格式的 AST ，描述了正确 Response 的结构、里面的字段名称、类型、长度、取值范围 等属性。';

alter table "TestRecord"
    owner to postgres;

INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520087199083, 82001, 1519526273822, '{"[]":[{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82006,"sex":1,"name":"Meria","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82021,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82025,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82028,"sex":1,"name":"gaeg","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82030,"sex":1,"name":"Fun","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82033,"sex":1,"name":"GAS","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82034,"sex":1,"name":"Jump","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82035,"sex":1,"name":"Tab","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82039,"sex":1,"name":"Everyday","head":"http://common.cnblogs.com/images/icon_weibo_24.png","contactIdList":[],"pictureList":[],"date":"2017-02-19 21:57:56.0"}},{"User":{"id":82040,"sex":1,"name":"Dream","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-03 00:44:26.0"}},{"User":{"id":82042,"sex":1,"name":"Why","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-04 18:04:33.0"}},{"User":{"id":82044,"sex":1,"name":"Love","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82006],"pictureList":[],"date":"2017-03-04 18:20:27.0"}},{"User":{"id":82055,"sex":1,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[38710,82002],"pictureList":[],"date":"2017-03-11 23:04:00.0"}},{"User":{"id":82056,"sex":1,"name":"IronMan","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-03-11 23:32:25.0"}},{"User":{"id":82059,"sex":1,"name":"He&She","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-19 22:49:15.0"}},{"User":{"id":82060,"sex":1,"name":"Anyway~","head":"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000","contactIdList":[],"pictureList":[],"date":"2017-03-21 22:10:18.0"}},{"User":{"id":1490109742863,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-21 23:22:22.0"}},{"User":{"id":1490420651686,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-25 13:44:11.0"}},{"User":{"id":1490973670928,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793,93793],"pictureList":[],"date":"2017-03-31 23:21:10.0"}},{"User":{"id":1508072105320,"sex":1,"name":"周吴郑王","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:55:05.0"}}],"code":200,"msg":"success"}', '2018-03-03 14:26:39.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520087202299, 82001, 1519368532249, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":21927.05},"code":200,"msg":"success"}', '2018-03-03 14:26:42.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520087205329, 82001, 1511969630372, '{"Comment":{"code":200,"msg":"success","id":1520087181598,"count":1},"code":200,"msg":"success"}', '2018-03-03 14:26:45.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520088798322, 82001, 1519526273822, '{"Document[]":[{"id":1519526273822,"userId":82001,"version":2,"compare":2,"name":"gets请求","url":"/gets","request":"{
    "Privacy": {
        "id": 82001
    },
    "tag": "Privacy"
}","date":"2018-02-25 10:37:53.0"},{"id":1519368532249,"userId":82001,"version":2,"compare":1,"name":"login请求","url":"/login","request":"{
    "type": 0,
    "phone": "13000082001",
    "password": "123456",
    "version": 1
}","date":"2018-02-23 14:48:52.0"},{"id":1516325614520,"userId":82001,"version":2,"compare":0,"name":"get请求","url":"/get","request":"        {
          "[]":{
            "User":{
              "sex":1
            }
          }
        }
      ","date":"2018-01-19 09:33:34.0"},{"id":1511963330795,"userId":0,"version":2,"compare":0,"name":"获取文档列表(即在线解析网页上的共享)-API调用方式","url":"/get","request":"{
    "Document[]": {
        "Document": {
            "@role": "login",
            "@order": "version-,date-"
        }
    }
}","date":"2017-11-29 21:48:50.0"},{"id":1512216131855,"userId":0,"version":1,"compare":0,"name":"获取文档列表(即在线解析网页上的文档)-表和字段、请求格式限制","url":"/get","request":"{
    "[]": {
        "Table": {
            "TABLE_SCHEMA": "sys",
            "TABLE_TYPE": "BASE TABLE",
            "TABLE_NAME!$": [
                "\\_%",
                "sys\\_%",
                "system\\_%"
            ],
            "@order": "TABLE_NAME+",
            "@column": "TABLE_NAME,TABLE_COMMENT"
        },
        "Column[]": {
            "Column": {
                "TABLE_NAME@": "[]/Table/TABLE_NAME",
                "@column": "COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT"
            }
        }
    },
    "Request[]": {
        "Request": {
            "@order": "version-,method-"
        }
    }
}","date":"2017-12-02 20:02:11.0"},{"id":1511970224333,"userId":0,"version":1,"compare":3,"name":"修改用户信息","url":"/put","request":"{
    "User": {
        "id": 82001,
        "name": "测试改名"
    },
    "tag": "User"
}","date":"2017-11-29 23:43:44.0"},{"id":1511970009072,"userId":0,"version":1,"compare":0,"name":"新增动态","url":"/post","request":"{
    "Moment": {
        "userId": 82001,
        "content": "测试新增动态",
        "pictureList": ["http://static.oschina.net/uploads/user/48/96331_50.jpg"
        ]
    },
    "tag": "Moment"
}","date":"2017-11-29 23:40:09.0"},{"id":1511969630372,"userId":0,"version":1,"compare":4,"name":"新增评论","url":"/post","request":"{
    "Comment": {
        "userId": 82001,
        "momentId": 15,
        "content": "测试新增评论"
    },
    "tag": "Comment"
}","date":"2017-11-29 23:33:50.0"},{"id":1511969417633,"userId":0,"version":1,"compare":0,"name":"点赞/取消点赞","url":"/put","request":"{
    "Moment": {
        "id": 15,
        "praiseUserIdList-": [
            82001
        ]
    },
    "tag": "Moment"
}","date":"2017-11-29 23:30:17.0"},{"id":1511969181104,"userId":0,"version":1,"compare":0,"name":"添加朋友","url":"/put","request":"{
    "User": {
        "id": 82001,
        "contactIdList+": [93793]
    },
    "tag": "User"
}","date":"2017-11-29 23:26:21.0"},{"id":1511967853340,"userId":0,"version":1,"compare":0,"name":"获取动态列表Moment+User+User:parise[]+Comment[]","url":"/get","request":"{
    "[]": {
        "count": 5,
        "page": 0,
        "Moment": {
            "@order": "date-"
        },
        "User": {
            "id@": "/Moment/userId",
            "@column": "id,name,head"
        },
        "User[]": {
            "count": 10,
            "User": {
                "id{}@": "[]/Moment/praiseUserIdList",
                "@column": "id,name"
            }
        },
        "[]": {
            "count": 6,
            "Comment": {
                "@order": "date+",
                "momentId@": "[]/Moment/id"
            },
            "User": {
                "id@": "/Comment/userId",
                "@column": "id,name"
            }
        }
    }
}","date":"2017-11-29 23:04:13.0"},{"id":1511964176689,"userId":0,"version":1,"compare":0,"name":"获取评论列表-动态详情页Comment+User","url":"/get","request":"{
    "[]": {
        "count": 20,
        "page": 0,
        "Comment": {
            "@order": "date+",
            "momentId": 15
        },
        "User": {
            "id@": "/Comment/userId",
            "@column": "id,name,head"
        }
    }
}","date":"2017-11-29 22:02:56.0"},{"id":1511963990072,"userId":0,"version":1,"compare":0,"name":"获取动态Moment+User+praiseUserList","url":"/get","request":"{
    "Moment": {
        "id": 15
    },
    "User": {
        "id@": "Moment/userId",
        "@column": "id,name,head"
    },
    "User[]": {
        "count": 10,
        "User": {
            "id{}@": "Moment/praiseUserIdList",
            "@column": "id,name"
        }
    }
}","date":"2017-11-29 21:59:50.0"},{"id":1511963722970,"userId":0,"version":1,"compare":0,"name":"获取用户列表("id{}":contactIdList)-朋友页","url":"/get","request":"{
    "User[]": {
        "count": 10,
        "page": 0,
        "User": {
            "@column": "id,sex,name,tag,head",
            "@order": "name+",
            "id{}": [
                82002,
                82004,
                70793
            ]
        }
    }
}","date":"2017-11-29 21:55:22.0"},{"id":1511963677325,"userId":0,"version":1,"compare":1,"name":"获取用户","url":"/get","request":"{"User": {"id": 82001}}","date":"2017-11-29 21:54:37.0"},{"id":1511796882184,"userId":0,"version":1,"compare":0,"name":"充值(需要支付密码)/提现","url":"/put/balance","request":"{"tag": "Privacy", "Privacy": {"id": 82001, "balance+": 100.15, "_payPassword": "123456"}}","date":"2017-11-27 23:34:42.0"},{"id":1511796589079,"userId":0,"version":1,"compare":0,"name":"修改登录密码(先获取验证码type:2)-手机号+验证码","url":"/put/password","request":"{"verify": "10322", "Privacy": {"phone": "13000082001", "_password": "666666"}}","date":"2017-11-27 23:29:49.0"},{"id":1511796208670,"userId":0,"version":1,"compare":0,"name":"检查验证码是否存在","url":"/heads/verify","request":"{"type": 0, "phone": "13000082001"}","date":"2017-11-27 23:23:28.0"},{"id":1511796155277,"userId":0,"version":1,"compare":0,"name":"获取验证码","url":"/post/verify","request":"{"type": 0, "phone": "13000082001"}","date":"2017-11-27 23:22:35.0"},{"id":3,"userId":0,"version":1,"compare":0,"name":"退出登录","url":"/logout","request":"{}","date":"2017-11-26 17:56:10.0"},{"id":1511689914599,"userId":0,"version":1,"compare":0,"name":"获取用户隐私信息","url":"/gets","request":"{"tag": "Privacy", "Privacy": {"id": 82001}}","date":"2017-11-26 17:51:54.0"},{"id":1,"userId":0,"version":1,"compare":0,"name":"登录","url":"/login","request":"{"type": 0, "phone": "13000082001", "version": 1, "password": "123456"}","date":"2017-11-26 15:35:19.0"},{"id":2,"userId":0,"version":1,"compare":0,"name":"注册(先获取验证码type:1)","url":"/register","request":"{
    "Privacy": {
        "phone": "13000083333",
        "_password": "123456"
    },
    "User": {
        "name": "APIJSONUser"
    },
    "verify": "6840"
}"}],"code":200,"msg":"success"}', '2018-03-03 14:53:18.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520088802486, 82001, 1511970224333, '{"Comment":{"code":200,"msg":"success","id":1520088770429,"count":1},"code":200,"msg":"success"}', '2018-03-03 14:53:22.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520088813403, 82001, 1511969630372, '{"Moment":{"id":15,"praiseUserIdList-":[82001]},"code":417,"msg":"PUT Moment, praiseUserIdList:82001 不存在！"}', '2018-03-03 14:53:33.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520088985525, 82001, 1511963677325, '{"code":412,"msg":"手机号或验证码错误！"}', '2018-03-03 14:56:25.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520089316891, 82001, 1519368532249, '{"User":{"code":200,"msg":"success","id":82001,"count":1},"code":200,"msg":"success"}', '2018-03-03 15:01:56.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520089324108, 82001, 1511970224333, '{"Comment":{"code":200,"msg":"success","id":1520089307634,"count":1},"code":200,"msg":"success"}', '2018-03-03 15:02:04.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520089326569, 82001, 1511969630372, '{"[]":[{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82006,"sex":1,"name":"Meria","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82021,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82025,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82028,"sex":1,"name":"gaeg","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82030,"sex":1,"name":"Fun","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82033,"sex":1,"name":"GAS","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82034,"sex":1,"name":"Jump","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82035,"sex":1,"name":"Tab","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82039,"sex":1,"name":"Everyday","head":"http://common.cnblogs.com/images/icon_weibo_24.png","contactIdList":[],"pictureList":[],"date":"2017-02-19 21:57:56.0"}},{"User":{"id":82040,"sex":1,"name":"Dream","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-03 00:44:26.0"}},{"User":{"id":82042,"sex":1,"name":"Why","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-04 18:04:33.0"}},{"User":{"id":82044,"sex":1,"name":"Love","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82006],"pictureList":[],"date":"2017-03-04 18:20:27.0"}},{"User":{"id":82055,"sex":1,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[38710,82002],"pictureList":[],"date":"2017-03-11 23:04:00.0"}},{"User":{"id":82056,"sex":1,"name":"IronMan","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-03-11 23:32:25.0"}},{"User":{"id":82059,"sex":1,"name":"He&She","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-19 22:49:15.0"}},{"User":{"id":82060,"sex":1,"name":"Anyway~","head":"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000","contactIdList":[],"pictureList":[],"date":"2017-03-21 22:10:18.0"}},{"User":{"id":1490109742863,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-21 23:22:22.0"}},{"User":{"id":1490420651686,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-25 13:44:11.0"}},{"User":{"id":1490973670928,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793,93793],"pictureList":[],"date":"2017-03-31 23:21:10.0"}},{"User":{"id":1508072105320,"sex":1,"name":"周吴郑王","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:55:05.0"}}],"code":200,"msg":"success"}', '2018-03-03 15:02:06.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520089328891, 82001, 1511963677325, '{"code":412,"msg":"手机号或验证码错误！"}', '2018-03-03 15:02:08.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520089695229, 82001, 2, '{"Document[]":[{"id":1519526273822,"userId":82001,"version":2,"name":"gets请求","url":"/gets","request":"{
    "Privacy": {
        "id": 82001
    },
    "tag": "Privacy"
}","response":"{"Document[]":[{"id":1519526273822,"userId":82001,"version":2,"compare":2,"name":"gets请求","url":"/gets","request":"{
    "Privacy": {
        "id": 82001
    },
    "tag": "Privacy"
}","date":"2018-02-25 10:37:53.0"},{"id":1519368532249,"userId":82001,"version":2,"compare":1,"name":"login请求","url":"/login","request":"{
    "type": 0,
    "phone": "13000082001",
    "password": "123456",
    "version": 1
}","date":"2018-02-23 14:48:52.0"},{"id":1516325614520,"userId":82001,"version":2,"compare":0,"name":"get请求","url":"/get","request":"        {
          "[]":{
            "User":{
              "sex":1
            }
          }
        }
      ","date":"2018-01-19 09:33:34.0"},{"id":1511963330795,"userId":0,"version":2,"compare":0,"name":"获取文档列表(即在线解析网页上的共享)-API调用方式","url":"/get","request":"{
    "Document[]": {
        "Document": {
            "@role": "login",
            "@order": "version-,date-"
        }
    }
}","date":"2017-11-29 21:48:50.0"},{"id":1512216131855,"userId":0,"version":1,"compare":0,"name":"获取文档列表(即在线解析网页上的文档)-表和字段、请求格式限制","url":"/get","request":"{
    "[]": {
        "Table": {
            "TABLE_SCHEMA": "sys",
            "TABLE_TYPE": "BASE TABLE",
            "TABLE_NAME!$": [
                "\\_%",
                "sys\\_%",
                "system\\_%"
            ],
            "@order": "TABLE_NAME+",
            "@column": "TABLE_NAME,TABLE_COMMENT"
        },
        "Column[]": {
            "Column": {
                "TABLE_NAME@": "[]/Table/TABLE_NAME",
                "@column": "COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT"
            }
        }
    },
    "Request[]": {
        "Request": {
            "@order": "version-,method-"
        }
    }
}","date":"2017-12-02 20:02:11.0"},{"id":1511970224333,"userId":0,"version":1,"compare":3,"name":"修改用户信息","url":"/put","request":"{
    "User": {
        "id": 82001,
        "name": "测试改名"
    },
    "tag": "User"
}","date":"2017-11-29 23:43:44.0"},{"id":1511970009072,"userId":0,"version":1,"compare":0,"name":"新增动态","url":"/post","request":"{
    "Moment": {
        "userId": 82001,
        "content": "测试新增动态",
        "pictureList": ["http://static.oschina.net/uploads/user/48/96331_50.jpg"
        ]
    },
    "tag": "Moment"
}","date":"2017-11-29 23:40:09.0"},{"id":1511969630372,"userId":0,"version":1,"compare":4,"name":"新增评论","url":"/post","request":"{
    "Comment": {
        "userId": 82001,
        "momentId": 15,
        "content": "测试新增评论"
    },
    "tag": "Comment"
}","date":"2017-11-29 23:33:50.0"},{"id":1511969417633,"userId":0,"version":1,"compare":0,"name":"点赞/取消点赞","url":"/put","request":"{
    "Moment": {
        "id": 15,
        "praiseUserIdList-": [
            82001
        ]
    },
    "tag": "Moment"
}","date":"2017-11-29 23:30:17.0"},{"id":1511969181104,"userId":0,"version":1,"compare":0,"name":"添加朋友","url":"/put","request":"{
    "User": {
        "id": 82001,
        "contactIdList+": [93793]
    },
    "tag": "User"
}","date":"2017-11-29 23:26:21.0"},{"id":1511967853340,"userId":0,"version":1,"compare":0,"name":"获取动态列表Moment+User+User:parise[]+Comment[]","url":"/get","request":"{
    "[]": {
        "count": 5,
        "page": 0,
        "Moment": {
            "@order": "date-"
        },
        "User": {
            "id@": "/Moment/userId",
            "@column": "id,name,head"
        },
        "User[]": {
            "count": 10,
            "User": {
                "id{}@": "[]/Moment/praiseUserIdList",
                "@column": "id,name"
            }
        },
        "[]": {
            "count": 6,
            "Comment": {
                "@order": "date+",
                "momentId@": "[]/Moment/id"
            },
            "User": {
                "id@": "/Comment/userId",
                "@column": "id,name"
            }
        }
    }
}","date":"2017-11-29 23:04:13.0"},{"id":1511964176689,"userId":0,"version":1,"compare":0,"name":"获取评论列表-动态详情页Comment+User","url":"/get","request":"{
    "[]": {
        "count": 20,
        "page": 0,
        "Comment": {
            "@order": "date+",
            "momentId": 15
        },
        "User": {
            "id@": "/Comment/userId",
            "@column": "id,name,head"
        }
    }
}","date":"2017-11-29 22:02:56.0"},{"id":1511963990072,"userId":0,"version":1,"compare":0,"name":"获取动态Moment+User+praiseUserList","url":"/get","request":"{
    "Moment": {
        "id": 15
    },
    "User": {
        "id@": "Moment/userId",
        "@column": "id,name,head"
    },
    "User[]": {
        "count": 10,
        "User": {
            "id{}@": "Moment/praiseUserIdList",
            "@column": "id,name"
        }
    }
}","date":"2017-11-29 21:59:50.0"},{"id":1511963722970,"userId":0,"version":1,"compare":0,"name":"获取用户列表("id{}":contactIdList)-朋友页","url":"/get","request":"{
    "User[]": {
        "count": 10,
        "page": 0,
        "User": {
            "@column": "id,sex,name,tag,head",
            "@order": "name+",
            "id{}": [
                82002,
                82004,
                70793
            ]
        }
    }
}","date":"2017-11-29 21:55:22.0"},{"id":1511963677325,"userId":0,"version":1,"compare":1,"name":"获取用户","url":"/get","request":"{"User": {"id": 82001}}","date":"2017-11-29 21:54:37.0"},{"id":1511796882184,"userId":0,"version":1,"compare":0,"name":"充值(需要支付密码)/提现","url":"/put/balance","request":"{"tag": "Privacy", "Privacy": {"id": 82001, "balance+": 100.15, "_payPassword": "123456"}}","date":"2017-11-27 23:34:42.0"},{"id":1511796589079,"userId":0,"version":1,"compare":0,"name":"修改登录密码(先获取验证码type:2)-手机号+验证码","url":"/put/password","request":"{"verify": "10322", "Privacy": {"phone": "13000082001", "_password": "666666"}}","date":"2017-11-27 23:29:49.0"},{"id":1511796208670,"userId":0,"version":1,"compare":0,"name":"检查验证码是否存在","url":"/heads/verify","request":"{"type": 0, "phone": "13000082001"}","date":"2017-11-27 23:23:28.0"},{"id":1511796155277,"userId":0,"version":1,"compare":0,"name":"获取验证码","url":"/post/verify","request":"{"type": 0, "phone": "13000082001"}","date":"2017-11-27 23:22:35.0"},{"id":3,"userId":0,"version":1,"compare":0,"name":"退出登录","url":"/logout","request":"{}","date":"2017-11-26 17:56:10.0"},{"id":1511689914599,"userId":0,"version":1,"compare":0,"name":"获取用户隐私信息","url":"/gets","request":"{"tag": "Privacy", "Privacy": {"id": 82001}}","date":"2017-11-26 17:51:54.0"},{"id":1,"userId":0,"version":1,"compare":0,"name":"登录","url":"/login","request":"{"type": 0, "phone": "13000082001", "version": 1, "password": "123456"}","date":"2017-11-26 15:35:19.0"},{"id":2,"userId":0,"version":1,"compare":0,"name":"注册(先获取验证码type:1)","url":"/register","request":"{
    "Privacy": {
        "phone": "13000083333",
        "_password": "123456"
    },
    "User": {
        "name": "APIJSONUser"
    },
    "verify": "6840"
}"}],"code":200,"msg":"success"}","date":"2018-02-25 10:37:53.0"},{"id":1519368532249,"userId":82001,"version":2,"name":"login请求","url":"/login","request":"{
    "type": 0,
    "phone": "13000082001",
    "password": "123456",
    "version": 1
}","response":"{"User":{"code":200,"msg":"success","id":82001,"count":1},"code":200,"msg":"success"}","date":"2018-02-23 14:48:52.0"},{"id":1516325614520,"userId":82001,"version":2,"name":"get请求","url":"/get","request":"        {
          "[]":{
            "User":{
              "sex":1
            }
          }
        }
      ","date":"2018-01-19 09:33:34.0"},{"id":1511963330795,"userId":0,"version":2,"name":"获取文档列表(即在线解析网页上的共享)-API调用方式","url":"/get","request":"{
    "Document[]": {
        "Document": {
            "@role": "login",
            "@order": "version-,date-"
        }
    }
}","date":"2017-11-29 21:48:50.0"},{"id":1512216131855,"userId":0,"version":1,"name":"获取文档列表(即在线解析网页上的文档)-表和字段、请求格式限制","url":"/get","request":"{
    "[]": {
        "Table": {
            "TABLE_SCHEMA": "sys",
            "TABLE_TYPE": "BASE TABLE",
            "TABLE_NAME!$": [
                "\\_%",
                "sys\\_%",
                "system\\_%"
            ],
            "@order": "TABLE_NAME+",
            "@column": "TABLE_NAME,TABLE_COMMENT"
        },
        "Column[]": {
            "Column": {
                "TABLE_NAME@": "[]/Table/TABLE_NAME",
                "@column": "COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_COMMENT"
            }
        }
    },
    "Request[]": {
        "Request": {
            "@order": "version-,method-"
        }
    }
}","date":"2017-12-02 20:02:11.0"},{"id":1511970224333,"userId":0,"version":1,"name":"修改用户信息","url":"/put","request":"{
    "User": {
        "id": 82001,
        "name": "测试改名"
    },
    "tag": "User"
}","date":"2017-11-29 23:43:44.0"},{"id":1511970009072,"userId":0,"version":1,"name":"新增动态","url":"/post","request":"{
    "Moment": {
        "userId": 82001,
        "content": "测试新增动态",
        "pictureList": ["http://static.oschina.net/uploads/user/48/96331_50.jpg"
        ]
    },
    "tag": "Moment"
}","date":"2017-11-29 23:40:09.0"},{"id":1511969630372,"userId":0,"version":1,"name":"新增评论","url":"/post","request":"{
    "Comment": {
        "userId": 82001,
        "momentId": 15,
        "content": "测试新增评论"
    },
    "tag": "Comment"
}","date":"2017-11-29 23:33:50.0"},{"id":1511969417633,"userId":0,"version":1,"name":"点赞/取消点赞","url":"/put","request":"{
    "Moment": {
        "id": 15,
        "praiseUserIdList-": [
            82001
        ]
    },
    "tag": "Moment"
}","date":"2017-11-29 23:30:17.0"},{"id":1511969181104,"userId":0,"version":1,"name":"添加朋友","url":"/put","request":"{
    "User": {
        "id": 82001,
        "contactIdList+": [93793]
    },
    "tag": "User"
}","date":"2017-11-29 23:26:21.0"},{"id":1511967853340,"userId":0,"version":1,"name":"获取动态列表Moment+User+User:parise[]+Comment[]","url":"/get","request":"{
    "[]": {
        "count": 5,
        "page": 0,
        "Moment": {
            "@order": "date-"
        },
        "User": {
            "id@": "/Moment/userId",
            "@column": "id,name,head"
        },
        "User[]": {
            "count": 10,
            "User": {
                "id{}@": "[]/Moment/praiseUserIdList",
                "@column": "id,name"
            }
        },
        "[]": {
            "count": 6,
            "Comment": {
                "@order": "date+",
                "momentId@": "[]/Moment/id"
            },
            "User": {
                "id@": "/Comment/userId",
                "@column": "id,name"
            }
        }
    }
}","date":"2017-11-29 23:04:13.0"},{"id":1511964176689,"userId":0,"version":1,"name":"获取评论列表-动态详情页Comment+User","url":"/get","request":"{
    "[]": {
        "count": 20,
        "page": 0,
        "Comment": {
            "@order": "date+",
            "momentId": 15
        },
        "User": {
            "id@": "/Comment/userId",
            "@column": "id,name,head"
        }
    }
}","date":"2017-11-29 22:02:56.0"},{"id":1511963990072,"userId":0,"version":1,"name":"获取动态Moment+User+praiseUserList","url":"/get","request":"{
    "Moment": {
        "id": 15
    },
    "User": {
        "id@": "Moment/userId",
        "@column": "id,name,head"
    },
    "User[]": {
        "count": 10,
        "User": {
            "id{}@": "Moment/praiseUserIdList",
            "@column": "id,name"
        }
    }
}","date":"2017-11-29 21:59:50.0"},{"id":1511963722970,"userId":0,"version":1,"name":"获取用户列表("id{}":contactIdList)-朋友页","url":"/get","request":"{
    "User[]": {
        "count": 10,
        "page": 0,
        "User": {
            "@column": "id,sex,name,tag,head",
            "@order": "name+",
            "id{}": [
                82002,
                82004,
                70793
            ]
        }
    }
}","date":"2017-11-29 21:55:22.0"},{"id":1511963677325,"userId":0,"version":1,"name":"获取用户","url":"/get","request":"{"User": {"id": 82001}}","date":"2017-11-29 21:54:37.0"},{"id":1511796882184,"userId":0,"version":1,"name":"充值(需要支付密码)/提现","url":"/put/balance","request":"{"tag": "Privacy", "Privacy": {"id": 82001, "balance+": 100.15, "_payPassword": "123456"}}","date":"2017-11-27 23:34:42.0"},{"id":1511796589079,"userId":0,"version":1,"name":"修改登录密码(先获取验证码type:2)-手机号+验证码","url":"/put/password","request":"{"verify": "10322", "Privacy": {"phone": "13000082001", "_password": "666666"}}","date":"2017-11-27 23:29:49.0"},{"id":1511796208670,"userId":0,"version":1,"name":"检查验证码是否存在","url":"/heads/verify","request":"{"type": 0, "phone": "13000082001"}","date":"2017-11-27 23:23:28.0"},{"id":1511796155277,"userId":0,"version":1,"name":"获取验证码","url":"/post/verify","request":"{"type": 0, "phone": "13000082001"}","date":"2017-11-27 23:22:35.0"},{"id":3,"userId":0,"version":1,"name":"退出登录","url":"/logout","request":"{}","date":"2017-11-26 17:56:10.0"},{"id":1511689914599,"userId":0,"version":1,"name":"获取用户隐私信息","url":"/gets","request":"{"tag": "Privacy", "Privacy": {"id": 82001}}","date":"2017-11-26 17:51:54.0"},{"id":1,"userId":0,"version":1,"name":"登录","url":"/login","request":"{"type": 0, "phone": "13000082001", "version": 1, "password": "123456"}","date":"2017-11-26 15:35:19.0"},{"id":2,"userId":0,"version":1,"name":"注册(先获取验证码type:1)","url":"/register","request":"{
    "Privacy": {
        "phone": "13000083333",
        "_password": "123456"
    },
    "User": {
        "name": "APIJSONUser"
    },
    "verify": "6840"
}"}],"code":200,"msg":"success"}', '2018-03-03 15:08:15.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520090219049, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":22628.1},"code":200,"msg":"success"}', '2018-03-03 15:16:59.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520090220914, 82001, 1516325614520, '{"[]":[{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82006,"sex":1,"name":"Meria","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82021,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82025,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82028,"sex":1,"name":"gaeg","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82030,"sex":1,"name":"Fun","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82033,"sex":1,"name":"GAS","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82034,"sex":1,"name":"Jump","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82035,"sex":1,"name":"Tab","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82039,"sex":1,"name":"Everyday","head":"http://common.cnblogs.com/images/icon_weibo_24.png","contactIdList":[],"pictureList":[],"date":"2017-02-19 21:57:56.0"}},{"User":{"id":82040,"sex":1,"name":"Dream","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-03 00:44:26.0"}},{"User":{"id":82042,"sex":1,"name":"Why","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-04 18:04:33.0"}},{"User":{"id":82044,"sex":1,"name":"Love","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82006],"pictureList":[],"date":"2017-03-04 18:20:27.0"}},{"User":{"id":82055,"sex":1,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[38710,82002],"pictureList":[],"date":"2017-03-11 23:04:00.0"}},{"User":{"id":82056,"sex":1,"name":"IronMan","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-03-11 23:32:25.0"}},{"User":{"id":82059,"sex":1,"name":"He&She","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-19 22:49:15.0"}},{"User":{"id":82060,"sex":1,"name":"Anyway~","head":"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000","contactIdList":[],"pictureList":[],"date":"2017-03-21 22:10:18.0"}},{"User":{"id":1490109742863,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-21 23:22:22.0"}},{"User":{"id":1490420651686,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-25 13:44:11.0"}},{"User":{"id":1490973670928,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793,93793],"pictureList":[],"date":"2017-03-31 23:21:10.0"}},{"User":{"id":1508072105320,"sex":1,"name":"周吴郑王","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:55:05.0"}}],"code":200,"msg":"success"}', '2018-03-03 15:17:00.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520090222151, 82001, 1519368532249, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-03-03 15:17:02.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520090430596, 82001, 1516325614520, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-03-03 15:20:30.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520090633508, 82001, 1516325614520, '{"code":412,"msg":"手机号或验证码错误！"}', '2018-03-03 15:23:53.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520091069489, 82001, 1516325614520, '{"code":412,"msg":"手机号或验证码错误！"}', '2018-03-03 15:31:09.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520091190755, 82001, 1519368532249, '{"code":412,"msg":"手机号或验证码错误！"}', '2018-03-03 15:33:10.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520091349017, 82001, 1519526273822, '{"code":412,"msg":"手机号或验证码错误！"}', '2018-03-03 15:35:49.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520091550804, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":22628.1},"code":200,"msg":"success"}', '2018-03-03 15:39:10.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520091551472, 82001, 1519368532249, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-03-03 15:39:11.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520091552062, 82001, 1516325614520, '{"[]":[{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82006,"sex":1,"name":"Meria","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82021,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82025,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82028,"sex":1,"name":"gaeg","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82030,"sex":1,"name":"Fun","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82033,"sex":1,"name":"GAS","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82034,"sex":1,"name":"Jump","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82035,"sex":1,"name":"Tab","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82039,"sex":1,"name":"Everyday","head":"http://common.cnblogs.com/images/icon_weibo_24.png","contactIdList":[],"pictureList":[],"date":"2017-02-19 21:57:56.0"}},{"User":{"id":82040,"sex":1,"name":"Dream","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-03 00:44:26.0"}},{"User":{"id":82042,"sex":1,"name":"Why","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-04 18:04:33.0"}},{"User":{"id":82044,"sex":1,"name":"Love","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82006],"pictureList":[],"date":"2017-03-04 18:20:27.0"}},{"User":{"id":82055,"sex":1,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[38710,82002],"pictureList":[],"date":"2017-03-11 23:04:00.0"}},{"User":{"id":82056,"sex":1,"name":"IronMan","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-03-11 23:32:25.0"}},{"User":{"id":82059,"sex":1,"name":"He&She","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-19 22:49:15.0"}},{"User":{"id":82060,"sex":1,"name":"Anyway~","head":"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000","contactIdList":[],"pictureList":[],"date":"2017-03-21 22:10:18.0"}},{"User":{"id":1490109742863,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-21 23:22:22.0"}},{"User":{"id":1490420651686,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-25 13:44:11.0"}},{"User":{"id":1490973670928,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793,93793],"pictureList":[],"date":"2017-03-31 23:21:10.0"}},{"User":{"id":1508072105320,"sex":1,"name":"周吴郑王","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:55:05.0"}}],"code":200,"msg":"success"}', '2018-03-03 15:39:12.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173915650, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":27134.85},"code":200,"msg":"success"}', '2018-03-04 14:31:55.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520091900844, 82001, 1516325614520, '{"[]":[{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82006,"sex":1,"name":"Meria","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82021,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82025,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82028,"sex":1,"name":"gaeg","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82030,"sex":1,"name":"Fun","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82033,"sex":1,"name":"GAS","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82034,"sex":1,"name":"Jump","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82035,"sex":1,"name":"Tab","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82039,"sex":1,"name":"Everyday","head":"http://common.cnblogs.com/images/icon_weibo_24.png","contactIdList":[],"pictureList":[],"date":"2017-02-19 21:57:56.0"}},{"User":{"id":82040,"sex":1,"name":"Dream","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-03 00:44:26.0"}},{"User":{"id":82042,"sex":1,"name":"Why","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-04 18:04:33.0"}},{"User":{"id":82044,"sex":1,"name":"Love","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82006],"pictureList":[],"date":"2017-03-04 18:20:27.0"}},{"User":{"id":82055,"sex":1,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[38710,82002],"pictureList":[],"date":"2017-03-11 23:04:00.0"}},{"User":{"id":82056,"sex":1,"name":"IronMan","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-03-11 23:32:25.0"}},{"User":{"id":82059,"sex":1,"name":"He&She","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-19 22:49:15.0"}},{"User":{"id":82060,"sex":1,"name":"Anyway~","head":"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000","contactIdList":[],"pictureList":[],"date":"2017-03-21 22:10:18.0"}},{"User":{"id":1490109742863,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-21 23:22:22.0"}},{"User":{"id":1490420651686,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-25 13:44:11.0"}},{"User":{"id":1490973670928,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793,93793],"pictureList":[],"date":"2017-03-31 23:21:10.0"}},{"User":{"id":1508072105320,"sex":1,"name":"周吴郑王","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:55:05.0"}}],"code":200,"msg":"success"}', '2018-03-03 15:45:00.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520092115128, 82001, 1520091922118, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-03-03 15:48:35.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520092224195, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":22628.1},"code":200,"msg":"success"}', '2018-03-03 15:50:24.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520092385815, 82001, 1520091922118, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-03-03 15:53:05.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520092411345, 82001, 1516325614520, '{"[]":[{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82006,"sex":1,"name":"Meria","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82021,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82025,"sex":1,"name":"Tommy","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82028,"sex":1,"name":"gaeg","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82030,"sex":1,"name":"Fun","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82033,"sex":1,"name":"GAS","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82034,"sex":1,"name":"Jump","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82035,"sex":1,"name":"Tab","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82039,"sex":1,"name":"Everyday","head":"http://common.cnblogs.com/images/icon_weibo_24.png","contactIdList":[],"pictureList":[],"date":"2017-02-19 21:57:56.0"}},{"User":{"id":82040,"sex":1,"name":"Dream","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-03 00:44:26.0"}},{"User":{"id":82042,"sex":1,"name":"Why","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-04 18:04:33.0"}},{"User":{"id":82044,"sex":1,"name":"Love","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82006],"pictureList":[],"date":"2017-03-04 18:20:27.0"}},{"User":{"id":82055,"sex":1,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[38710,82002],"pictureList":[],"date":"2017-03-11 23:04:00.0"}},{"User":{"id":82056,"sex":1,"name":"IronMan","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-03-11 23:32:25.0"}},{"User":{"id":82059,"sex":1,"name":"He&She","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[],"pictureList":[],"date":"2017-03-19 22:49:15.0"}},{"User":{"id":82060,"sex":1,"name":"Anyway~","head":"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000","contactIdList":[],"pictureList":[],"date":"2017-03-21 22:10:18.0"}},{"User":{"id":1490109742863,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-21 23:22:22.0"}},{"User":{"id":1490420651686,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793],"pictureList":[],"date":"2017-03-25 13:44:11.0"}},{"User":{"id":1490973670928,"sex":1,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[70793,93793],"pictureList":[],"date":"2017-03-31 23:21:10.0"}},{"User":{"id":1508072105320,"sex":1,"name":"周吴郑王","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:55:05.0"}}],"code":200,"msg":"success"}', '2018-03-03 15:53:31.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520092528119, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":22628.1},"code":200,"msg":"success"}', '2018-03-03 15:55:28.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520092670553, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":22628.1},"code":200,"msg":"success"}', '2018-03-03 15:57:50.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520092819799, 82001, 1520091922118, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-03-03 16:00:19.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173868568, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":26734.25},"code":200,"msg":"success"}', '2018-03-04 14:31:08.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173870784, 82001, 1512216131855, '{"[]":[{"Table":{"TABLE_NAME":"apijson_privacy","TABLE_COMMENT":"用户隐私信息表。
对安全要求高，不想泄漏真实名称。对外名称为 Privacy"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"certified","COLUMN_TYPE":"tinyint(2)","IS_NULLABLE":"NO","COLUMN_COMMENT":"已认证"},{"COLUMN_NAME":"phone","COLUMN_TYPE":"bigint(11)","IS_NULLABLE":"NO","COLUMN_COMMENT":"手机号，仅支持 11 位数的。不支持 +86 这种国家地区开头的。如果要支持就改为 VARCHAR(14)"},{"COLUMN_NAME":"balance","COLUMN_TYPE":"decimal(10,2)","IS_NULLABLE":"NO","COLUMN_COMMENT":"余额"},{"COLUMN_NAME":"_password","COLUMN_TYPE":"varchar(20)","IS_NULLABLE":"NO","COLUMN_COMMENT":"登录密码"},{"COLUMN_NAME":"_payPassword","COLUMN_TYPE":"int(6)","IS_NULLABLE":"NO","COLUMN_COMMENT":"支付密码"}]},{"Table":{"TABLE_NAME":"apijson_user","TABLE_COMMENT":"用户公开信息表。
对安全要求高，不想泄漏真实名称。对外名称为 User"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"sex","COLUMN_TYPE":"tinyint(2)","IS_NULLABLE":"NO","COLUMN_COMMENT":"性别：
0-男
1-女"},{"COLUMN_NAME":"name","COLUMN_TYPE":"varchar(20)","IS_NULLABLE":"YES","COLUMN_COMMENT":"名称"},{"COLUMN_NAME":"tag","COLUMN_TYPE":"varchar(45)","IS_NULLABLE":"YES","COLUMN_COMMENT":"标签"},{"COLUMN_NAME":"head","COLUMN_TYPE":"varchar(300)","IS_NULLABLE":"YES","COLUMN_COMMENT":"头像url"},{"COLUMN_NAME":"contactIdList","COLUMN_TYPE":"json","IS_NULLABLE":"YES","COLUMN_COMMENT":"联系人id列表"},{"COLUMN_NAME":"pictureList","COLUMN_TYPE":"json","IS_NULLABLE":"YES","COLUMN_COMMENT":"照片列表"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"YES","COLUMN_COMMENT":"创建日期"}]},{"Table":{"TABLE_NAME":"Comment","TABLE_COMMENT":"评论"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"toId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"YES","COLUMN_COMMENT":"被回复的id"},{"COLUMN_NAME":"userId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"评论人id"},{"COLUMN_NAME":"momentId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"动态id"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"YES","COLUMN_COMMENT":"创建日期"},{"COLUMN_NAME":"content","COLUMN_TYPE":"varchar(1000)","IS_NULLABLE":"NO","COLUMN_COMMENT":"内容"}]},{"Table":{"TABLE_NAME":"Document","TABLE_COMMENT":"测试用例文档
后端开发者在测试好后，把选好的测试用例上传，这样就能共享给前端/客户端开发者"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"userId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"用户id
应该用adminId，只有当登录账户是管理员时才能操作文档。
需要先建Admin表，新增登录等相关接口。"},{"COLUMN_NAME":"version","COLUMN_TYPE":"tinyint(4)","IS_NULLABLE":"NO","COLUMN_COMMENT":"接口版本号
<=0 - 不限制版本，任意版本都可用这个接口
>0 - 在这个版本添加的接口"},{"COLUMN_NAME":"name","COLUMN_TYPE":"varchar(50)","IS_NULLABLE":"NO","COLUMN_COMMENT":"接口名称"},{"COLUMN_NAME":"url","COLUMN_TYPE":"varchar(250)","IS_NULLABLE":"NO","COLUMN_COMMENT":"请求地址"},{"COLUMN_NAME":"request","COLUMN_TYPE":"text","IS_NULLABLE":"NO","COLUMN_COMMENT":"请求
用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。"},{"COLUMN_NAME":"response","COLUMN_TYPE":"text","IS_NULLABLE":"YES","COLUMN_COMMENT":"标准返回结果JSON
用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"YES","COLUMN_COMMENT":"创建日期"}]},{"Table":{"TABLE_NAME":"Login","TABLE_COMMENT":"@deprecated，登录信息存session"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"userId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"用户id"},{"COLUMN_NAME":"type","COLUMN_TYPE":"tinyint(2)","IS_NULLABLE":"NO","COLUMN_COMMENT":"类型
0-密码登录
1-验证码登录"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"NO","COLUMN_COMMENT":"创建日期"}]},{"Table":{"TABLE_NAME":"Moment","TABLE_COMMENT":"动态"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"userId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"用户id"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"YES","COLUMN_COMMENT":"创建日期"},{"COLUMN_NAME":"content","COLUMN_TYPE":"varchar(300)","IS_NULLABLE":"YES","COLUMN_COMMENT":"内容"},{"COLUMN_NAME":"praiseUserIdList","COLUMN_TYPE":"json","IS_NULLABLE":"NO","COLUMN_COMMENT":"点赞的用户id列表"},{"COLUMN_NAME":"pictureList","COLUMN_TYPE":"json","IS_NULLABLE":"NO","COLUMN_COMMENT":"图片列表"}]},{"Table":{"TABLE_NAME":"Praise","TABLE_COMMENT":"如果对Moment写安全要求高，可以将Moment内praiserUserIdList分离到Praise表中，作为userIdList。
权限注解也改下：
@MethodAccess(
		PUT = {OWNER, ADMIN}
		)
class Moment {
       …
}

@MethodAccess(
		PUT = {LOGIN, CONTACT, CIRCLE, OWNER, ADMIN}
		)
 class Praise {
       …
 }
"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"动态id"},{"COLUMN_NAME":"momentId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"userId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"用户id"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"YES","COLUMN_COMMENT":"点赞时间"}]},{"Table":{"TABLE_NAME":"Request","TABLE_COMMENT":"最好编辑完后删除主键，这样就是只读状态，不能随意更改。需要更改就重新加上主键。

每次启动服务器时加载整个表到内存。
这个表不可省略，model内注解的权限只是客户端能用的，其它可以保证即便服务端代码错误时也不会误删数据。"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"version","COLUMN_TYPE":"tinyint(4)","IS_NULLABLE":"NO","COLUMN_COMMENT":"GET,HEAD可用任意结构访问任意开放内容，不需要这个字段。
其它的操作因为写入了结构和内容，所以都需要，按照不同的version选择对应的structure。

自动化版本管理：
Request JSON最外层可以传  “version”:Integer 。
1.未传或 <= 0，用最新版。 “@order”:”version-“
2.已传且 > 0，用version以上的可用版本的最低版本。 “@order”:”version+”, “version{}”:”>={version}”"},{"COLUMN_NAME":"method","COLUMN_TYPE":"varchar(10)","IS_NULLABLE":"YES","COLUMN_COMMENT":"只限于GET,HEAD外的操作方法。"},{"COLUMN_NAME":"tag","COLUMN_TYPE":"varchar(20)","IS_NULLABLE":"NO","COLUMN_COMMENT":"标签"},{"COLUMN_NAME":"structure","COLUMN_TYPE":"json","IS_NULLABLE":"NO","COLUMN_COMMENT":"结构"},{"COLUMN_NAME":"detail","COLUMN_TYPE":"varchar(10000)","IS_NULLABLE":"YES","COLUMN_COMMENT":"详细说明"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"YES","COLUMN_COMMENT":"创建日期"}]},{"Table":{"TABLE_NAME":"Response","TABLE_COMMENT":"每次启动服务器时加载整个表到内存。"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"method","COLUMN_TYPE":"varchar(10)","IS_NULLABLE":"YES","COLUMN_COMMENT":"方法"},{"COLUMN_NAME":"model","COLUMN_TYPE":"varchar(20)","IS_NULLABLE":"NO","COLUMN_COMMENT":"表名，table是SQL关键词不能用"},{"COLUMN_NAME":"structure","COLUMN_TYPE":"json","IS_NULLABLE":"NO","COLUMN_COMMENT":"结构"},{"COLUMN_NAME":"detail","COLUMN_TYPE":"varchar(10000)","IS_NULLABLE":"YES","COLUMN_COMMENT":"详细说明"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"YES","COLUMN_COMMENT":"创建日期"}]},{"Table":{"TABLE_NAME":"Test","TABLE_COMMENT":"测试及验证用的表，可以用 SELECT condition替代 SELECT * FROM Test WHERE condition，这样就不需要这张表了"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"tinyint(2)","IS_NULLABLE":"NO","COLUMN_COMMENT":""}]},{"Table":{"TABLE_NAME":"TestRecord","TABLE_COMMENT":"测试记录
主要用于保存自动化接口回归测试"},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"userId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"用户id"},{"COLUMN_NAME":"documentId","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"测试用例文档id"},{"COLUMN_NAME":"response","COLUMN_TYPE":"text","IS_NULLABLE":"NO","COLUMN_COMMENT":"接口返回结果JSON"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"NO","COLUMN_COMMENT":"创建日期"}]},{"Table":{"TABLE_NAME":"Verify","TABLE_COMMENT":""},"Column[]":[{"COLUMN_NAME":"id","COLUMN_TYPE":"bigint(15)","IS_NULLABLE":"NO","COLUMN_COMMENT":"唯一标识"},{"COLUMN_NAME":"type","COLUMN_TYPE":"int(2)","IS_NULLABLE":"NO","COLUMN_COMMENT":"类型：
0-登录
1-注册
2-修改登录密码
3-修改支付密码"},{"COLUMN_NAME":"phone","COLUMN_TYPE":"bigint(11)","IS_NULLABLE":"NO","COLUMN_COMMENT":"手机号"},{"COLUMN_NAME":"verify","COLUMN_TYPE":"int(6)","IS_NULLABLE":"NO","COLUMN_COMMENT":"验证码"},{"COLUMN_NAME":"date","COLUMN_TYPE":"timestamp","IS_NULLABLE":"NO","COLUMN_COMMENT":"创建时间"}]}],"Request[]":[{"id":36,"version":2,"method":"PUT","tag":"Document","structure":{"ADD":{"@role":"owner"},"DISALLOW":"userId","NECESSARY":"id"},"date":"2017-11-26 16:35:15.0"},{"id":35,"version":2,"method":"POST","tag":"Document","structure":{"DISALLOW":"id","NECESSARY":"userId,name,url,request"},"date":"2017-11-26 16:34:41.0"},{"id":32,"version":2,"method":"GETS","tag":"Privacy","structure":{"ADD":{"@role":"owner"},"DISALLOW":"_password,_payPassword","NECESSARY":"id"},"date":"2017-06-13 00:05:51.0"},{"id":33,"version":2,"method":"GETS","tag":"Privacy-CIRCLE","structure":{"Privacy":{"DISALLOW":"!","NECESSARY":"id","PUT":{"@column":"phone","@role":"CIRCLE"}}},"date":"2017-06-13 00:05:51.0"},{"id":37,"version":2,"method":"DELETE","tag":"Document","structure":{"ADD":{"@role":"owner"},"DISALLOW":"!","NECESSARY":"id"},"date":"2017-11-26 16:36:20.0"},{"id":4,"version":1,"method":"PUT","tag":"User","structure":{"ADD":{"@role":"owner"},"DISALLOW":"phone","NECESSARY":"id"},"detail":"必须传id，不允许传phone。ADD当没传@role时用owner补全","date":"2017-02-01 19:19:51.0"},{"id":8,"version":1,"method":"PUT","tag":"User-phone","structure":{"User":{"ADD":{"@role":"owner"},"DISALLOW":"!","NECESSARY":"id,phone,_password","PUT":{"@condition":"_password"}}},"detail":"! 表示其它所有，这里指necessary所有未包含的字段","date":"2017-02-01 19:19:51.0"},{"id":17,"version":1,"method":"PUT","tag":"Moment","structure":{"DISALLOW":"userId,date","NECESSARY":"id"},"date":"2017-02-01 19:19:51.0"},{"id":23,"version":1,"method":"PUT","tag":"Privacy","structure":{"ADD":{"@role":"owner"},"NECESSARY":"id"},"detail":"ADD当没传@role时用owner补全","date":"2017-02-01 19:19:51.0"},{"id":25,"version":1,"method":"PUT","tag":"Praise","structure":{"NECESSARY":"id"},"detail":"必须传id","date":"2017-02-01 19:19:51.0"},{"id":27,"version":1,"method":"PUT","tag":"Comment[]","structure":{"Comment":{"ADD":{"@role":"owner"},"NECESSARY":"id{}"}},"detail":"DISALLOW没必要用于DELETE","date":"2017-02-01 19:19:51.0"},{"id":28,"version":1,"method":"PUT","tag":"Comment","structure":{"ADD":{"@role":"owner"},"NECESSARY":"id"},"detail":"这里省略了Comment，因为tag就是Comment，Parser.getCorrectRequest会自动补全","date":"2017-02-01 19:19:51.0"},{"id":30,"version":1,"method":"PUT","tag":"balance+","structure":{"Privacy":{"VERIFY":{"balance+&{}":">=1,<=100000"},"DISALLOW":"!","NECESSARY":"id,balance+"}},"detail":"验证balance+对应的值是否满足>=1且<=100000","date":"2017-10-21 16:48:34.0"},{"id":31,"version":1,"method":"PUT","tag":"balance-","structure":{"Privacy":{"VERIFY":{"balance-&{}":">=1,<=10000"},"DISALLOW":"!","NECESSARY":"id,balance-,_password","PUT":{"@condition":"_password"}}},"detail":"PUT强制把_password作为WHERE条件","date":"2017-10-21 16:48:34.0"},{"id":1,"version":1,"method":"POST","tag":"register","structure":{"Privacy":{"UNIQUE":"phone","VERIFY":{"phone?":"phone"},"DISALLOW":"id","NECESSARY":"_password,phone"},"User":{"DISALLOW":"id","NECESSARY":"name","PUT":{"id@":"Privacy/id"}}},"detail":"UNIQUE校验phone是否已存在。VERIFY校验phone是否符合手机号的格式","date":"2017-02-01 19:19:51.0"},{"id":2,"version":1,"method":"POST","tag":"Moment","structure":{"ADD":{"praiseUserIdList":[],"pictureList":[]},"DISALLOW":"id","NECESSARY":"userId,pictureList"},"detail":"ADD当没传pictureList和praiseUserIdList时用空数组[]补全，保证不会为null","date":"2017-02-01 19:19:51.0"},{"id":3,"version":1,"method":"POST","tag":"Comment","structure":{"DISALLOW":"id","NECESSARY":"userId,momentId,content"},"detail":"必须传userId,momentId,content，不允许传id","date":"2017-02-01 19:19:51.0"},{"id":14,"version":1,"method":"POST","tag":"Verify","structure":{"DISALLOW":"!","NECESSARY":"phone,verify"},"detail":"必须传phone,verify，其它都不允许传","date":"2017-02-18 22:20:43.0"},{"id":38,"version":1,"method":"POST","tag":"TestRecord","structure":{"DISALLOW":"id","NECESSARY":"userId,documentId,response"},"date":"2017-11-26 16:34:41.0"},{"id":16,"version":1,"method":"HEADS","tag":"Verify","structure":{},"detail":"允许任意内容","date":"2017-02-18 22:20:43.0"},{"id":21,"version":1,"method":"HEADS","tag":"Login","structure":{"DISALLOW":"!","NECESSARY":"userId,type"},"date":"2017-02-18 22:20:43.0"},{"id":15,"version":1,"method":"GETS","tag":"Verify","structure":{"NECESSARY":"phone"},"detail":"必须传phone","date":"2017-02-18 22:20:43.0"},{"id":22,"version":1,"method":"GETS","tag":"User","structure":{},"detail":"允许传任何内容，除了表对象","date":"2017-02-18 22:20:43.0"},{"id":29,"version":1,"method":"GETS","tag":"login","structure":{"Privacy":{"DISALLOW":"id","NECESSARY":"phone,_password"}},"date":"2017-10-15 18:04:52.0"},{"id":5,"version":1,"method":"DELETE","tag":"Moment","structure":{"Moment":{"ADD":{"@role":"owner"},"NECESSARY":"id"},"PUT":{"Comment":{"@role":"admin","momentId@":"Moment/id"}}},"date":"2017-02-01 19:19:51.0"},{"id":6,"version":1,"method":"DELETE","tag":"Comment","structure":{"Comment":{"ADD":{"@role":"owner"},"NECESSARY":"id"},"PUT":{"Comment:child":{"toId@":"Comment/id","@role":"admin"}}},"detail":"disallow没必要用于DELETE","date":"2017-02-01 19:19:51.0"},{"id":26,"version":1,"method":"DELETE","tag":"Comment[]","structure":{"Comment":{"ADD":{"@role":"owner"},"NECESSARY":"id{}"}},"detail":"DISALLOW没必要用于DELETE","date":"2017-02-01 19:19:51.0"}],"code":200,"msg":"success"}', '2018-03-04 14:31:10.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173872976, 82001, 1511970224333, '{"User":{"code":200,"msg":"success","id":82001,"count":1},"code":200,"msg":"success"}', '2018-03-04 14:31:12.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173873719, 82001, 1511970009072, '{"Moment":{"code":200,"msg":"success","id":1520173865791,"count":1},"code":200,"msg":"success"}', '2018-03-04 14:31:13.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173875424, 82001, 1511969181104, '{"User":{"id":82001,"contactIdList+":[93793],"@role":"owner"},"code":409,"msg":"PUT User, contactIdList:93793 已存在！"}', '2018-03-04 14:31:15.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173877914, 82001, 1511969630372, '{"Comment":{"code":200,"msg":"success","id":1520173865916,"count":1},"code":200,"msg":"success"}', '2018-03-04 14:31:17.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173878817, 82001, 1511967853340, '{"[]":[{"Moment":{"id":1520173760960,"userId":82001,"date":"2018-03-04 22:29:20.0","content":"测试新增动态","praiseUserIdList":[],"pictureList":["http://static.oschina.net/uploads/user/48/96331_50.jpg"]},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Moment":{"id":1520173720451,"userId":82001,"date":"2018-03-04 22:28:40.0","content":"测试新增动态","praiseUserIdList":[],"pictureList":["http://static.oschina.net/uploads/user/48/96331_50.jpg"]},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Moment":{"id":1520173672910,"userId":82001,"date":"2018-03-04 22:27:52.0","content":"测试新增动态","praiseUserIdList":[],"pictureList":["http://static.oschina.net/uploads/user/48/96331_50.jpg"]},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Moment":{"id":1520173667947,"userId":82001,"date":"2018-03-04 22:27:47.0","content":"测试新增动态","praiseUserIdList":[],"pictureList":["http://static.oschina.net/uploads/user/48/96331_50.jpg"]},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Moment":{"id":1520173666384,"userId":82001,"date":"2018-03-04 22:27:46.0","content":"测试新增动态","praiseUserIdList":[],"pictureList":["http://static.oschina.net/uploads/user/48/96331_50.jpg"]},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}}],"code":200,"msg":"success"}', '2018-03-04 14:31:18.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173879493, 82001, 1511964176689, '{"[]":[{"Comment":{"id":176,"toId":166,"userId":38710,"momentId":15,"date":"2017-03-25 20:28:03.0","content":"thank you"},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"}},{"Comment":{"id":1490863469638,"toId":0,"userId":82002,"momentId":15,"date":"2017-03-30 16:44:29.0","content":"Just do it"},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"}},{"Comment":{"id":1490875660259,"toId":1490863469638,"userId":82055,"momentId":15,"date":"2017-03-30 20:07:40.0","content":"I prove wht you said(??????)"},"User":{"id":82055,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1508227456407,"toId":0,"userId":82001,"momentId":15,"date":"2017-10-17 16:04:16.0","content":"hsh"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1509346606036,"toId":0,"userId":82001,"momentId":15,"date":"2017-10-30 14:56:46.0","content":"测"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520086403693,"userId":82001,"momentId":15,"date":"2018-03-03 22:13:23.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520086647789,"userId":82001,"momentId":15,"date":"2018-03-03 22:17:27.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520086858159,"userId":82001,"momentId":15,"date":"2018-03-03 22:20:58.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520087089611,"userId":82001,"momentId":15,"date":"2018-03-03 22:24:49.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520087181598,"userId":82001,"momentId":15,"date":"2018-03-03 22:26:21.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520088770429,"userId":82001,"momentId":15,"date":"2018-03-03 22:52:50.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520089307634,"userId":82001,"momentId":15,"date":"2018-03-03 23:01:47.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520089508692,"userId":82001,"momentId":15,"date":"2018-03-03 23:05:08.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520089597667,"userId":82001,"momentId":15,"date":"2018-03-03 23:06:37.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520089619339,"userId":82001,"momentId":15,"date":"2018-03-03 23:06:59.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520089621208,"userId":82001,"momentId":15,"date":"2018-03-03 23:07:01.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520171621453,"userId":82001,"momentId":15,"date":"2018-03-04 21:53:41.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520171658156,"userId":82001,"momentId":15,"date":"2018-03-04 21:54:18.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520171697409,"userId":82001,"momentId":15,"date":"2018-03-04 21:54:57.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520171714260,"userId":82001,"momentId":15,"date":"2018-03-04 21:55:14.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}}],"code":200,"msg":"success"}', '2018-03-04 14:31:19.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173881709, 82001, 1511796208670, '{"code":408,"msg":"验证码已过期！"}', '2018-03-04 14:31:21.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173883243, 82001, 1511689914599, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":26834.4},"code":200,"msg":"success"}', '2018-03-04 14:31:23.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173889466, 82001, 1, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-03-04 14:31:29.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173922657, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":27235},"code":200,"msg":"success"}', '2018-03-04 14:32:02.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520173935008, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":27435.3},"code":200,"msg":"success"}', '2018-03-04 14:32:15.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520174017925, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":29939.05},"code":200,"msg":"success"}', '2018-03-04 14:33:37.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520174362871, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":30039.2},"code":200,"msg":"success"}', '2018-03-04 14:39:22.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520176947082, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28699.05},"code":200,"msg":"success"}', '2018-03-04 15:22:27.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520177104344, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28709.2},"code":200,"msg":"success"}', '2018-03-04 15:25:04.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520177328250, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28809.35},"code":200,"msg":"success"}', '2018-03-04 15:28:48.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520177426424, 82001, 1520177221844, '{"[]":[{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033,93794],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82004,"sex":0,"name":"Tommy","tag":"fasef","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82009,"sex":0,"name":"God","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82012,"sex":0,"name":"Steve","tag":"FEWE","head":"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000","contactIdList":[82004,82002,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82020,"sex":0,"name":"ORANGE","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82022,"sex":0,"name":"Internet","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82023,"sex":0,"name":"No1","head":"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82024,"sex":0,"name":"Lemon","head":"http://static.oschina.net/uploads/user/427/855532_50.jpg?t=1435030876000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82026,"sex":0,"name":"iOS","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82027,"sex":0,"name":"Yong","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82029,"sex":0,"name":"GASG","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82031,"sex":0,"name":"Lemon","head":"http://static.oschina.net/uploads/user/48/96331_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82032,"sex":0,"name":"Stack","tag":"fasdg","head":"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82036,"sex":0,"name":"SAG","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82037,"sex":0,"name":"Test","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82038,"sex":0,"name":"Battle","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82041,"sex":0,"name":"Holo","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[38710,82001],"pictureList":[],"date":"2017-03-04 17:59:34.0"}},{"User":{"id":82043,"sex":0,"name":"Holiday","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[70793,82006],"pictureList":[],"date":"2017-03-04 18:05:04.0"}},{"User":{"id":82045,"sex":0,"name":"Green","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,82002,82003,1485246481130],"pictureList":[],"date":"2017-03-04 18:22:39.0"}},{"User":{"id":82046,"sex":0,"name":"Team","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[38710,82002,1485246481130],"pictureList":[],"date":"2017-03-04 23:11:17.0"}},{"User":{"id":82047,"sex":0,"name":"Tesla","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[],"pictureList":[],"date":"2017-03-05 00:02:05.0"}},{"User":{"id":82048,"sex":0,"name":"Moto","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-03-05 00:04:02.0"}},{"User":{"id":82049,"sex":0,"name":"ITMan","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-03-05 17:51:51.0"}},{"User":{"id":82050,"sex":0,"name":"Parl","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-03-05 17:52:52.0"}},{"User":{"id":82051,"sex":0,"name":"Girl","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-03-05 17:53:37.0"}},{"User":{"id":82052,"sex":0,"name":"Unbrella","head":"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000","contactIdList":[],"pictureList":[],"date":"2017-03-05 17:57:54.0"}},{"User":{"id":82053,"sex":0,"name":"Alice","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[],"pictureList":[],"date":"2017-03-05 23:25:42.0"}},{"User":{"id":82054,"sex":0,"name":"Harvey","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-03-06 20:29:03.0"}},{"User":{"id":82057,"sex":0,"name":"NullPointerExeption","head":"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000","contactIdList":[],"pictureList":[],"date":"2017-03-12 14:01:23.0"}},{"User":{"id":82058,"sex":0,"name":"StupidBird","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,82002],"pictureList":[],"date":"2017-03-12 19:23:04.0"}},{"User":{"id":90814,"sex":0,"name":"007","head":"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":93793,"sex":0,"name":"Mike","tag":"GES","head":"http://static.oschina.net/uploads/user/48/96331_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":93794,"sex":0,"name":"Lemon","head":"http://static.oschina.net/uploads/user/48/97721_50.jpg?t=1451544779000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":1490109845208,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-21 23:24:05.0"}},{"User":{"id":1490427139175,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[38710,70793],"pictureList":[],"date":"2017-03-25 15:32:19.0"}},{"User":{"id":1490427577823,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-25 15:39:37.0"}},{"User":{"id":1490584952968,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-27 11:22:32.0"}},{"User":{"id":1492936169722,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-04-23 16:29:29.0"}},{"User":{"id":1493480142628,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-04-29 23:35:42.0"}},{"User":{"id":1493747512860,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 01:51:52.0"}},{"User":{"id":1493747777770,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 01:56:17.0"}},{"User":{"id":1493748594003,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 02:09:54.0"}},{"User":{"id":1493748615711,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 02:10:15.0"}},{"User":{"id":1493749090643,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 02:18:10.0"}},{"User":{"id":1493836043151,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-04 02:27:23.0"}},{"User":{"id":1493883110132,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-04 15:31:50.0"}},{"User":{"id":1493890214167,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-04 17:30:14.0"}},{"User":{"id":1493890303473,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-04 17:31:43.0"}},{"User":{"id":1493890303474,"sex":0,"name":"Test Post","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-06-12 23:50:44.0"}},{"User":{"id":1493890303475,"sex":0,"name":"Test Post","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-06-12 23:51:23.0"}},{"User":{"id":1497792972314,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-06-18 21:36:12.0"}},{"User":{"id":1497792972315,"sex":0,"name":"一二三","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-06-25 18:42:33.0"}},{"User":{"id":1499057230629,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-07-03 12:47:10.0"}},{"User":{"id":1500825221910,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-07-23 23:53:41.0"}},{"User":{"id":1502638023483,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-08-13 23:27:03.0"}},{"User":{"id":1502639062900,"sex":0,"name":"TESLA","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-08-13 23:44:22.0"}},{"User":{"id":1502639424119,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-08-13 23:50:24.0"}},{"User":{"id":1507220582167,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-06 00:23:02.0"}},{"User":{"id":1508072071492,"sex":0,"name":"赵钱孙李","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:54:31.0"}},{"User":{"id":1508072160401,"sex":0,"name":"四五六","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:56:00.0"}},{"User":{"id":1508072202871,"sex":0,"name":"七八九十","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:56:42.0"}},{"User":{"id":1510495628760,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-11-12 22:07:08.0"}},{"User":{"id":1511407581570,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82002,82003,82005,82006,82021,82023,82036,82033],"pictureList":[],"date":"2017-11-23 11:26:21.0"}},{"User":{"id":1511761906715,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-11-27 13:51:46.0"}},{"User":{"id":1511965911349,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-11-29 22:31:51.0"}},{"User":{"id":1512387063078,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-12-04 19:31:03.0"}},{"User":{"id":1512531601485,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001],"pictureList":[],"date":"2017-12-06 11:40:01.0"}},{"User":{"id":1514623064133,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","date":"2017-12-30 16:37:44.0"}},{"User":{"id":1514625918255,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82002,93793],"date":"2017-12-30 17:25:18.0"}},{"User":{"id":1514626163032,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","date":"2017-12-30 17:29:23.0"}},{"User":{"id":1514858422969,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[93793,82056],"date":"2018-01-02 10:00:22.0"}},{"User":{"id":1515565976140,"sex":0,"name":"APIJSONUser","head":"傻","contactIdList":[82003,82021],"date":"2018-01-10 14:32:56.0"}},{"User":{"id":1518218350585,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","date":"2018-02-10 07:19:10.0"}},{"User":{"id":1519778917280,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","date":"2018-02-28 08:48:37.0"}}],"code":200,"msg":"success"}', '2018-03-04 15:30:26.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520680590422, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28709.2},"code":200,"msg":"success"}', '2018-03-10 11:16:30.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520681233526, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28809.35},"code":200,"msg":"success"}', '2018-03-10 11:27:13.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520681278572, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28809.35,"test":"gda"},"code":200,"msg":"success"}', '2018-03-10 11:27:58.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520681315261, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28809.35},"code":200,"msg":"success"}', '2018-03-10 11:28:35.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520681361520, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28809.35,"test":"sfgr"},"code":200,"msg":"success"}', '2018-03-10 11:29:21.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520681493656, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28809.35},"code":200,"msg":"success"}', '2018-03-10 11:31:33.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520681641244, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28809},"code":200,"msg":"success"}', '2018-03-10 11:34:01.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520681770042, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28808.5},"code":200,"msg":"success"}', '2018-03-10 11:36:10.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520682056396, 82001, 1520680656479, '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名2","tag":"APIJSON User","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,70793,82034,93793,82021,82033,93794],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":82004,"sex":0,"name":"Tommy","tag":"fasef","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82009,"sex":0,"name":"God","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82012,"sex":0,"name":"Steve","tag":"FEWE","head":"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000","contactIdList":[82004,82002,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82020,"sex":0,"name":"ORANGE","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82022,"sex":0,"name":"Internet","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82023,"sex":0,"name":"No1","head":"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82024,"sex":0,"name":"Lemon","head":"http://static.oschina.net/uploads/user/427/855532_50.jpg?t=1435030876000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82026,"sex":0,"name":"iOS","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82027,"sex":0,"name":"Yong","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82029,"sex":0,"name":"GASG","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82031,"sex":0,"name":"Lemon","head":"http://static.oschina.net/uploads/user/48/96331_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82032,"sex":0,"name":"Stack","tag":"fasdg","head":"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82036,"sex":0,"name":"SAG","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82037,"sex":0,"name":"Test","head":"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82038,"sex":0,"name":"Battle","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82041,"sex":0,"name":"Holo","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[38710,82001],"pictureList":[],"date":"2017-03-04 17:59:34.0"},{"id":82043,"sex":0,"name":"Holiday","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[70793,82006],"pictureList":[],"date":"2017-03-04 18:05:04.0"},{"id":82045,"sex":0,"name":"Green","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,82002,82003,1485246481130],"pictureList":[],"date":"2017-03-04 18:22:39.0"},{"id":82046,"sex":0,"name":"Team","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[38710,82002,1485246481130],"pictureList":[],"date":"2017-03-04 23:11:17.0"},{"id":82047,"sex":0,"name":"Tesla","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[],"pictureList":[],"date":"2017-03-05 00:02:05.0"},{"id":82048,"sex":0,"name":"Moto","head":"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000","contactIdList":[],"pictureList":[],"date":"2017-03-05 00:04:02.0"},{"id":82049,"sex":0,"name":"ITMan","head":"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000","contactIdList":[],"pictureList":[],"date":"2017-03-05 17:51:51.0"},{"id":82050,"sex":0,"name":"Parl","head":"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000","contactIdList":[],"pictureList":[],"date":"2017-03-05 17:52:52.0"},{"id":82051,"sex":0,"name":"Girl","head":"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000","contactIdList":[],"pictureList":[],"date":"2017-03-05 17:53:37.0"},{"id":82052,"sex":0,"name":"Unbrella","head":"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000","contactIdList":[],"pictureList":[],"date":"2017-03-05 17:57:54.0"},{"id":82053,"sex":0,"name":"Alice","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[],"pictureList":[],"date":"2017-03-05 23:25:42.0"},{"id":82054,"sex":0,"name":"Harvey","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-03-06 20:29:03.0"},{"id":82057,"sex":0,"name":"NullPointerExeption","head":"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000","contactIdList":[],"pictureList":[],"date":"2017-03-12 14:01:23.0"},{"id":82058,"sex":0,"name":"StupidBird","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,82002],"pictureList":[],"date":"2017-03-12 19:23:04.0"},{"id":90814,"sex":0,"name":"007","head":"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":93793,"sex":0,"name":"Mike","tag":"GES","head":"http://static.oschina.net/uploads/user/48/96331_50.jpg","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":93794,"sex":0,"name":"Lemon","head":"http://static.oschina.net/uploads/user/48/97721_50.jpg?t=1451544779000","contactIdList":[],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":1490109845208,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-21 23:24:05.0"},{"id":1490427139175,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[38710,70793],"pictureList":[],"date":"2017-03-25 15:32:19.0"},{"id":1490427577823,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-25 15:39:37.0"},{"id":1490584952968,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-03-27 11:22:32.0"},{"id":1492936169722,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-04-23 16:29:29.0"},{"id":1493480142628,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-04-29 23:35:42.0"},{"id":1493747512860,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 01:51:52.0"},{"id":1493747777770,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 01:56:17.0"},{"id":1493748594003,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 02:09:54.0"},{"id":1493748615711,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 02:10:15.0"},{"id":1493749090643,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-03 02:18:10.0"},{"id":1493836043151,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-04 02:27:23.0"},{"id":1493883110132,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-04 15:31:50.0"},{"id":1493890214167,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-04 17:30:14.0"},{"id":1493890303473,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-05-04 17:31:43.0"},{"id":1493890303474,"sex":0,"name":"Test Post","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-06-12 23:50:44.0"},{"id":1493890303475,"sex":0,"name":"Test Post","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-06-12 23:51:23.0"},{"id":1497792972314,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-06-18 21:36:12.0"},{"id":1497792972315,"sex":0,"name":"一二三","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-06-25 18:42:33.0"},{"id":1499057230629,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-07-03 12:47:10.0"},{"id":1500825221910,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-07-23 23:53:41.0"},{"id":1502638023483,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-08-13 23:27:03.0"},{"id":1502639062900,"sex":0,"name":"TESLA","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-08-13 23:44:22.0"},{"id":1502639424119,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-08-13 23:50:24.0"},{"id":1507220582167,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-06 00:23:02.0"},{"id":1508072071492,"sex":0,"name":"赵钱孙李","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:54:31.0"},{"id":1508072160401,"sex":0,"name":"四五六","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:56:00.0"},{"id":1508072202871,"sex":0,"name":"七八九十","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-10-15 20:56:42.0"},{"id":1510495628760,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-11-12 22:07:08.0"},{"id":1511407581570,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82002,82003,82005,82006,82021,82023,82036,82033],"pictureList":[],"date":"2017-11-23 11:26:21.0"},{"id":1511761906715,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-11-27 13:51:46.0"},{"id":1511965911349,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-11-29 22:31:51.0"},{"id":1512387063078,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[],"pictureList":[],"date":"2017-12-04 19:31:03.0"},{"id":1512531601485,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001],"pictureList":[],"date":"2017-12-06 11:40:01.0"},{"id":1514623064133,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","date":"2017-12-30 16:37:44.0"},{"id":1514625918255,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82002,93793],"date":"2017-12-30 17:25:18.0"},{"id":1514626163032,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","date":"2017-12-30 17:29:23.0"},{"id":1514858422969,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[93793,82056],"date":"2018-01-02 10:00:22.0"},{"id":1515565976140,"sex":0,"name":"APIJSONUser","head":"傻","contactIdList":[82003,82021],"date":"2018-01-10 14:32:56.0"},{"id":1518218350585,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","date":"2018-02-10 07:19:10.0"},{"id":1519778917280,"sex":0,"name":"APIJSONUser","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","date":"2018-02-28 08:48:37.0"}],"code":200,"msg":"success"}', '2018-03-10 11:40:56.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520682589957, 82001, 1520682468690, '{"Moment":{"id":1520086403692,"userId":82001,"date":"2018-03-03 22:13:23.0","content":"测试新增动态","praiseUserIdList":[],"pictureList":["http://static.oschina.net/uploads/user/48/96331_50.jpg"]},"User":{"id":82001,"name":"测试改名","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"},"code":200,"msg":"success"}', '2018-03-10 11:49:49.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1520682742215, 82001, 1519526273822, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":28808},"code":200,"msg":"success"}', '2018-03-10 11:52:22.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1532879615717, 82001, 1521907333048, '{"User-id[]":[82002,82003,82005,82041,82045,82058,1512531601485,1528254173621],"Moment[]":[{"id":32,"userId":82002,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[38710,82002,82001],"pictureList":["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},{"id":1508053762227,"userId":82003,"date":"2017-10-15 15:49:22.0","content":"我也试试","praiseUserIdList":[1515565976140,82001],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1508072491570,"userId":82002,"date":"2017-10-15 21:01:31.0","content":"有点冷~","praiseUserIdList":[82001,82002],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1508073178489,"userId":82045,"date":"2017-10-15 21:12:58.0","content":"发动态","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1514017444961,"userId":82002,"date":"2017-12-23 16:24:04.0","content":"123479589679","praiseUserIdList":[82002,1520242280259,82001,70793,1524042900591,1528264711016],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1531062713966,"userId":82002,"date":"2018-07-08 23:11:53.0","content":"云南好美啊( ◞˟૩˟)◞","praiseUserIdList":[82001,82005,38710,70793,93793,82003,1531969715979],"pictureList":["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072366455&di=c0d4b15b2c4b70aad49e6ae747f60742&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F3%2F57a2a41f57d09.jpg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072499167&di=5b5621d117edbc5d344a03ba0a6b580b&imgtype=0&src=http%3A%2F%2Fi0.szhomeimg.com%2FUploadFiles%2FBBS%2F2006%2F08%2F05%2F24752199_79122.91.jpg"]}],"code":200,"msg":"success"}', '2018-07-29 15:53:35.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1532879625816, 82001, 1521907333047, '{"[]":[{"Moment":{"id":301,"userId":93793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-301","praiseUserIdList":[38710,93793,82003,82005,82040,82055,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"Comment":{"id":45,"momentId":301,"content":"This is a Content...-45"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":58,"userId":90814,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-435","praiseUserIdList":[38710,82003,82005,93793,82006,82044,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg"]},"Comment":{"id":13,"momentId":58,"content":"This is a Content...-13"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":68,"momentId":371,"content":"This is a Content...-68"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":44,"momentId":170,"content":"This is a Content...-44"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"},"Comment":{"id":4,"momentId":470,"content":"This is a Content...-4"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":12,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"1111534034","praiseUserIdList":[70793,93793,82044,82040,82055,90814,38710,82002,82006,1508072105320,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067","http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":162,"momentId":12,"content":"This is a Content...-162"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":15,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON is a JSON Transmission Structure Protocol…","praiseUserIdList":[82055,82002,38710],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":176,"momentId":15,"content":"thank you"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":543,"userId":93793,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[82001],"pictureList":["https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"Comment":{"id":1490777905437,"momentId":543,"content":"rr"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":32,"userId":82002,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[38710,82002,82001],"pictureList":["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":1512035117021,"momentId":32,"content":"图片看不了啊"},"join":"&/User/id@,</Comment/momentId@"},{"Moment":{"id":551,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"test","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":1490864039264,"momentId":551,"content":"Wonderful!"},"join":"&/User/id@,</Comment/momentId@"}],"code":200,"msg":"success"}', '2018-07-29 15:53:45.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140211536, 82001, 1521907303540, '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[1520242280259,82030,82025,82003,93793,82002,1531969715979,82006,82005],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-08-01 16:16:51.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140331684, 82001, 1521905895591, '{"Moment":{"code":200,"msg":"success","count":11},"code":200,"msg":"success"}', '2018-08-01 16:18:51.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140553624, 82001, 1521903828410, '{"User":{"code":200,"msg":"success","count":70},"code":200,"msg":"success"}', '2018-08-01 16:22:33.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140565380, 82001, 1511796208670, '{"Verify":{"code":200,"msg":"success","count":1},"code":200,"msg":"success"}', '2018-08-01 16:22:45.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140569777, 82001, 1511796155277, '{"Verify":{"id":1533140566615,"type":0,"phone":13000082001,"verify":10867,"date":"2018-08-02 00:22:46.0"},"tag":"Verify","code":200,"msg":"success"}', '2018-08-01 16:22:49.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140576601, 82001, 1511796155277, '{"Verify":{"id":1533140571205,"type":0,"phone":13000082001,"verify":2383,"date":"2018-08-02 00:22:51.0"},"tag":"Verify","code":200,"msg":"success"}', '2018-08-01 16:22:56.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140600338, 82001, 1511967853340, '{"[]":[{"Moment":{"id":301,"userId":93793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-301","praiseUserIdList":[38710,93793,82003,82005,82040,82055,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":93793,"name":"Mike","head":"http://static.oschina.net/uploads/user/48/96331_50.jpg"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82002,"name":"Happy~"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82040,"name":"Dream"},{"id":82055,"name":"Solid"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":45,"toId":0,"userId":93793,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-45"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":51,"toId":45,"userId":82003,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-51"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":76,"toId":45,"userId":93793,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-76"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":120,"toId":0,"userId":93793,"momentId":301,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-110"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":124,"toId":0,"userId":82001,"momentId":301,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-114"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490781009548,"toId":51,"userId":82001,"momentId":301,"date":"2017-03-29 17:50:09.0","content":"3"},"User":{"id":82001,"name":"测试改名"}}]},{"Moment":{"id":58,"userId":90814,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-435","praiseUserIdList":[38710,82003,82005,93793,82006,82044,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg"]},"User":{"id":90814,"name":"007","head":"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82001,"name":"测试改名"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82006,"name":"Meria"},{"id":82044,"name":"Love"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":13,"toId":0,"userId":82005,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-13"},"User":{"id":82005,"name":"Jan"}},{"Comment":{"id":77,"toId":13,"userId":93793,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-77"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":97,"toId":13,"userId":82006,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-97"},"User":{"id":82006,"name":"Meria"}},{"Comment":{"id":167,"userId":82001,"momentId":58,"date":"2017-03-25 19:48:41.0","content":"Nice!"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":173,"userId":38710,"momentId":58,"date":"2017-03-25 20:25:13.0","content":"Good"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":188,"toId":97,"userId":82001,"momentId":58,"date":"2017-03-26 15:21:32.0","content":"1646"},"User":{"id":82001,"name":"测试改名"}}]},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"User[]":[{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82006,"name":"Meria"},{"id":82040,"name":"Dream"},{"id":90814,"name":"007"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":68,"toId":0,"userId":82005,"momentId":371,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-68"},"User":{"id":82005,"name":"Jan"}},{"Comment":{"id":157,"userId":93793,"momentId":371,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-157"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":110,"toId":0,"userId":93793,"momentId":371,"date":"2017-02-01 19:23:24.0","content":"This is a Content...-110"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":114,"toId":0,"userId":82001,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-114"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":115,"toId":0,"userId":38710,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-115"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":116,"toId":0,"userId":70793,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-116"},"User":{"id":70793,"name":"Strong"}}]},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"User[]":[{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82044,"name":"Love"}],"[]":[{"Comment":{"id":44,"toId":0,"userId":82003,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-44"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":54,"toId":0,"userId":82004,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-54"},"User":{"id":82004,"name":"Tommy"}},{"Comment":{"id":99,"toId":44,"userId":70793,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-99"},"User":{"id":70793,"name":"Strong"}},{"Comment":{"id":206,"toId":54,"userId":82001,"momentId":170,"date":"2017-03-29 11:04:23.0","content":"ejej"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490780759866,"toId":99,"userId":82001,"momentId":170,"date":"2017-03-29 17:45:59.0","content":"99"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490863661426,"toId":1490780759866,"userId":70793,"momentId":170,"date":"2017-03-30 16:47:41.0","content":"66"},"User":{"id":70793,"name":"Strong"}}]},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"},"User[]":[{"id":82001,"name":"测试改名"}],"[]":[{"Comment":{"id":4,"toId":0,"userId":38710,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-4"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":22,"toId":221,"userId":82001,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"测试修改评论"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":47,"toId":4,"userId":70793,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-47"},"User":{"id":70793,"name":"Strong"}},{"Comment":{"id":1490863507114,"toId":4,"userId":82003,"momentId":470,"date":"2017-03-30 16:45:07.0","content":"yes"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":1490863903900,"toId":0,"userId":82006,"momentId":470,"date":"2017-03-30 16:51:43.0","content":"SOGA"},"User":{"id":82006,"name":"Meria"}},{"Comment":{"id":1491740899179,"toId":0,"userId":82001,"momentId":470,"date":"2017-04-09 20:28:19.0","content":"www"},"User":{"id":82001,"name":"测试改名"}}]}],"code":200,"msg":"success"}', '2018-08-01 16:23:20.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140607459, 82001, 1511964176689, '{"[]":[{"Comment":{"id":176,"toId":166,"userId":38710,"momentId":15,"date":"2017-03-25 20:28:03.0","content":"thank you"},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"}},{"Comment":{"id":1490863469638,"toId":0,"userId":82002,"momentId":15,"date":"2017-03-30 16:44:29.0","content":"Just do it"},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"}},{"Comment":{"id":1490875660259,"toId":1490863469638,"userId":82055,"momentId":15,"date":"2017-03-30 20:07:40.0","content":"I prove wht you said(??????)"},"User":{"id":82055,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1508227456407,"toId":0,"userId":82001,"momentId":15,"date":"2017-10-17 16:04:16.0","content":"hsh"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1509346606036,"toId":0,"userId":82001,"momentId":15,"date":"2017-10-30 14:56:46.0","content":"测"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1520332892486,"userId":82001,"momentId":15,"date":"2018-03-06 18:41:32.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1525933236313,"userId":82001,"momentId":15,"date":"2018-05-10 14:20:36.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1525933255901,"userId":82001,"momentId":15,"date":"2018-05-10 14:20:55.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1527949266037,"userId":82001,"momentId":15,"date":"2018-06-02 22:21:06.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528339777338,"userId":82001,"momentId":15,"date":"2018-06-07 10:49:37.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528366915282,"userId":82001,"momentId":15,"date":"2018-06-07 18:21:55.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528366931410,"userId":82001,"momentId":15,"date":"2018-06-07 18:22:11.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528392773597,"userId":82001,"momentId":15,"date":"2018-06-08 01:32:53.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529034360708,"userId":82001,"momentId":15,"date":"2018-06-15 11:46:00.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529078537044,"userId":82001,"momentId":15,"date":"2018-06-16 00:02:17.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529401004622,"userId":82001,"momentId":15,"date":"2018-06-19 17:36:44.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529401505690,"userId":82001,"momentId":15,"date":"2018-06-19 17:45:05.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529468113356,"userId":82001,"momentId":15,"date":"2018-06-20 12:15:13.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529724026842,"userId":82001,"momentId":15,"date":"2018-06-23 11:20:26.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529909214303,"userId":82001,"momentId":15,"date":"2018-06-25 14:46:54.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}}],"code":200,"msg":"success"}', '2018-08-01 16:23:27.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140610051, 82001, 1511969630372, '{"Comment":{"code":200,"msg":"success","id":1533140601544,"count":1},"code":200,"msg":"success"}', '2018-08-01 16:23:30.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140615913, 82001, 1521905895591, '{"Moment":{"code":200,"msg":"success","count":19},"code":200,"msg":"success"}', '2018-08-01 16:23:35.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140617466, 82001, 1521905868719, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":9832.86},"code":200,"msg":"success"}', '2018-08-01 16:23:37.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140619001, 82001, 1521905680680, '{"Moment":{"code":200,"msg":"success","id":1533140610656,"count":1},"code":200,"msg":"success"}', '2018-08-01 16:23:39.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140623032, 82001, 1521904756674, '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[1520242280259,82030,82025,82003,93793,82002,1531969715979,82006,82005],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-08-01 16:23:43.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140626067, 82001, 1521904653622, '{"User[]":[{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-08-01 16:23:46.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140629710, 82001, 1521903761689, '{"User":{"code":200,"msg":"success","count":117},"code":200,"msg":"success"}', '2018-08-01 16:23:49.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140633031, 82001, 1521902110680, '{"[]":[{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[1520242280259,82030,82025,82003,93793,82002,1531969715979,82006,82005],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}}],"code":200,"msg":"success"}', '2018-08-01 16:23:53.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140636466, 82001, 1521902033332, '{"[]":[{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[1520242280259,82030,82025,82003,93793,82002,1531969715979,82006,82005],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}}],"total":119,"code":200,"msg":"success"}', '2018-08-01 16:23:56.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140640270, 82001, 1521901682846, '{"[]":[{"Moment":{"userId":38710,"id":235}},{"Moment":{"userId":38710,"id":470}},{"Moment":{"userId":38710,"id":511}},{"Moment":{"userId":38710,"id":595}},{"Moment":{"userId":38710,"id":704}},{"Moment":{"userId":38710,"id":1491200468898}},{"Moment":{"userId":38710,"id":1493835799335}},{"Moment":{"userId":38710,"id":1512314438990}},{"Moment":{"userId":38710,"id":1513094436910}},{"Moment":{"userId":38710,"id":1533140171134}}],"code":200,"msg":"success"}', '2018-08-01 16:24:00.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140662591, 82001, 1511970009072, '{"Moment":{"code":200,"msg":"success","id":1533140610716,"count":1},"code":200,"msg":"success"}', '2018-08-01 16:24:22.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140664191, 82001, 1511969630372, '{"Comment":{"code":200,"msg":"success","id":1533140610714,"count":1},"code":200,"msg":"success"}', '2018-08-01 16:24:24.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140666518, 82001, 1511963677325, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[1520242280259,82030,82025,82003,93793,82002,1531969715979,82006,82005],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-08-01 16:24:26.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140668867, 82001, 1511689914599, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":9833.01},"code":200,"msg":"success"}', '2018-08-01 16:24:28.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140694117, 82001, 1511963990072, '{"Moment":{"id":15,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON is a JSON Transmission Structure Protocol…","praiseUserIdList":[82055,82002,38710,82001],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82055,"name":"Solid"}],"code":200,"msg":"success"}', '2018-08-01 16:24:54.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533140697885, 82001, 1511963990072, '{"Moment":{"id":15,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"APIJSON is a JSON Transmission Structure Protocol…","praiseUserIdList":[82055,82002,38710],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82002,"name":"Happy~"},{"id":82055,"name":"Solid"}],"code":200,"msg":"success"}', '2018-08-01 16:24:57.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533396720929, 82001, 1511796155277, '{"Verify":{"id":1533396718012,"type":0,"phone":13000082001,"verify":4995,"date":"2018-08-04 23:31:58.0"},"tag":"Verify","code":200,"msg":"success"}', '2018-08-04 15:32:00.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533396731930, 82001, 1521901682846, '{"[]":[{"Moment":{"userId":38710,"id":235}},{"Moment":{"userId":38710,"id":470}},{"Moment":{"userId":38710,"id":511}},{"Moment":{"userId":38710,"id":595}},{"Moment":{"userId":38710,"id":704}},{"Moment":{"userId":38710,"id":1491200468898}},{"Moment":{"userId":38710,"id":1493835799335}},{"Moment":{"userId":38710,"id":1512314438990}},{"Moment":{"userId":38710,"id":1513094436910}},{"Moment":{"userId":38710,"id":1533395051809}}],"code":200,"msg":"success"}', '2018-08-04 15:32:11.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533396739099, 82001, 1511689914599, '{"Privacy":{"id":82001,"certified":1,"phone":13000082001,"balance":9835.11},"code":200,"msg":"success"}', '2018-08-04 15:32:19.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1533396867840, 82001, 1511964176689, '{"[]":[{"Comment":{"id":176,"toId":166,"userId":38710,"momentId":15,"date":"2017-03-25 20:28:03.0","content":"thank you"},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"}},{"Comment":{"id":1490863469638,"toId":0,"userId":82002,"momentId":15,"date":"2017-03-30 16:44:29.0","content":"Just do it"},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"}},{"Comment":{"id":1490875660259,"toId":1490863469638,"userId":82055,"momentId":15,"date":"2017-03-30 20:07:40.0","content":"I prove wht you said(??????)"},"User":{"id":82055,"name":"Solid","head":"http://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1508227456407,"toId":0,"userId":82001,"momentId":15,"date":"2017-10-17 16:04:16.0","content":"hsh"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1509346606036,"toId":0,"userId":82001,"momentId":15,"date":"2017-10-30 14:56:46.0","content":"测"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1525933255901,"userId":82001,"momentId":15,"date":"2018-05-10 14:20:55.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1527949266037,"userId":82001,"momentId":15,"date":"2018-06-02 22:21:06.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528339777338,"userId":82001,"momentId":15,"date":"2018-06-07 10:49:37.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528366915282,"userId":82001,"momentId":15,"date":"2018-06-07 18:21:55.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528366931410,"userId":82001,"momentId":15,"date":"2018-06-07 18:22:11.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1528392773597,"userId":82001,"momentId":15,"date":"2018-06-08 01:32:53.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529034360708,"userId":82001,"momentId":15,"date":"2018-06-15 11:46:00.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529078537044,"userId":82001,"momentId":15,"date":"2018-06-16 00:02:17.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529401004622,"userId":82001,"momentId":15,"date":"2018-06-19 17:36:44.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529401505690,"userId":82001,"momentId":15,"date":"2018-06-19 17:45:05.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529468113356,"userId":82001,"momentId":15,"date":"2018-06-20 12:15:13.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529724026842,"userId":82001,"momentId":15,"date":"2018-06-23 11:20:26.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1529909214303,"userId":82001,"momentId":15,"date":"2018-06-25 14:46:54.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1530276831779,"userId":82001,"momentId":15,"date":"2018-06-29 20:53:51.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}},{"Comment":{"id":1531365764793,"userId":82001,"momentId":15,"date":"2018-07-12 11:22:44.0","content":"测试新增评论"},"User":{"id":82001,"name":"测试改名","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg"}}],"code":200,"msg":"success"}', '2018-08-04 15:34:27.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534152309932, 82001, 1521905895591, '{"Moment":{"code":200,"msg":"success","count":9},"code":200,"msg":"success"}', '2018-08-13 09:25:09.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534152344226, 82001, 1521903828410, '{"User":{"code":200,"msg":"success","count":71},"code":200,"msg":"success"}', '2018-08-13 09:25:44.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534152350031, 82001, 1521903761689, '{"User":{"code":200,"msg":"success","count":118},"code":200,"msg":"success"}', '2018-08-13 09:25:50.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534152388437, 82001, 1521905895591, '{"Moment":{"code":200,"msg":"success","count":10},"code":200,"msg":"success"}', '2018-08-13 09:26:28.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534152406351, 82001, 1521902033332, '{"[]":[{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82030,82025,82003,93793,82006,82002,1520242280259,82005,1531969715979,1532188114543,82024],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}}],"total":120,"code":200,"msg":"success"}', '2018-08-13 09:26:46.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534152424378, 82001, 1521901682846, '{"[]":[{"Moment":{"userId":38710,"id":235}},{"Moment":{"userId":38710,"id":470}},{"Moment":{"userId":38710,"id":511}},{"Moment":{"userId":38710,"id":595}},{"Moment":{"userId":38710,"id":704}},{"Moment":{"userId":38710,"id":1491200468898}},{"Moment":{"userId":38710,"id":1493835799335}},{"Moment":{"userId":38710,"id":1512314438990}},{"Moment":{"userId":38710,"id":1513094436910}},{"Moment":{"userId":38710,"id":1534152251941}}],"code":200,"msg":"success"}', '2018-08-13 09:27:04.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534241166066, 82001, 1521901682846, '{"[]":[{"Moment":{"userId":38710,"id":235}},{"Moment":{"userId":38710,"id":470}},{"Moment":{"userId":38710,"id":511}},{"Moment":{"userId":38710,"id":595}},{"Moment":{"userId":38710,"id":704}},{"Moment":{"userId":38710,"id":1491200468898}},{"Moment":{"userId":38710,"id":1493835799335}},{"Moment":{"userId":38710,"id":1512314438990}},{"Moment":{"userId":38710,"id":1513094436910}},{"Moment":{"userId":38710,"id":1534241152403}}],"code":200,"msg":"success"}', '2018-08-14 10:06:06.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534477342518, 82001, 1521907303540, '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82030,82025,82003,93793,82006,82002,1520242280259,82005,1531969715979,82024],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-08-17 03:42:22.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534776475640, 82001, 1521901610784, '{"[]":[{"Moment":{"userId":38710,"maxId":1534776429151}},{"Moment":{"userId":70793,"maxId":551}},{"Moment":{"userId":82001,"maxId":1534776370291}},{"Moment":{"userId":82002,"maxId":1531062713966}},{"Moment":{"userId":82003,"maxId":1508053762227}},{"Moment":{"userId":82045,"maxId":1508073178489}},{"Moment":{"userId":82056,"maxId":1514858533480}},{"Moment":{"userId":93793,"maxId":1516086423441}},{"Moment":{"userId":1520242280259,"maxId":1520242333325}},{"Moment":{"userId":1523626157302,"maxId":1523936332614}}],"code":200,"msg":"success"}', '2018-08-20 14:47:55.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1534780207036, 82001, 1521907333047, '{"[]":[{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"},"Comment":{"id":47,"momentId":470,"content":"This is a Content...-47"}},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"},"Comment":{"id":47,"momentId":470,"content":"This is a Content...-47"}},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":99,"momentId":170,"content":"This is a Content...-99"}},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"},"Comment":{"id":47,"momentId":470,"content":"This is a Content...-47"}},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":99,"momentId":170,"content":"This is a Content...-99"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":115,"momentId":371,"content":"This is a Content...-115"}},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"Comment":{"id":99,"momentId":170,"content":"This is a Content...-99"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":115,"momentId":371,"content":"This is a Content...-115"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":115,"momentId":371,"content":"This is a Content...-115"}},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"Comment":{"id":115,"momentId":371,"content":"This is a Content...-115"}}],"code":200,"msg":"success"}', '2018-08-20 15:50:07.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1536484351109, 82001, 1511963677325, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82034,82021,82006,82005,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-09-09 09:12:31.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1536484428955, 82001, 1511963677325, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82034,82021,82006,82005,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-09-09 09:13:48.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025782935, 82001, 1521901746809, '{"[]":[{"User":{"name":"赵钱孙李","id":1508072071492}},{"User":{"name":"测试改名","id":82001}},{"User":{"name":"梦","id":1528264711016}},{"User":{"name":"宁旭","id":1532188114543}},{"User":{"name":"四五六","id":1508072160401}},{"User":{"name":"哈哈哈","id":1524042900591}},{"User":{"name":"周吴郑王","id":1508072105320}},{"User":{"name":"七八九十","id":1508072202871}},{"User":{"name":"一二三","id":1499057230629}},{"User":{"name":"Yong","id":82027}}],"code":200,"msg":"success"}', '2018-09-15 15:36:22.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025788659, 82001, 1521901682846, '{"[]":[{"Moment":{"userId":38710,"id":235}},{"Moment":{"userId":38710,"id":470}},{"Moment":{"userId":38710,"id":511}},{"Moment":{"userId":38710,"id":595}},{"Moment":{"userId":38710,"id":704}},{"Moment":{"userId":38710,"id":1491200468898}},{"Moment":{"userId":38710,"id":1493835799335}},{"Moment":{"userId":38710,"id":1512314438990}},{"Moment":{"userId":38710,"id":1513094436910}},{"Moment":{"userId":38710,"id":1537025625613}}],"code":200,"msg":"success"}', '2018-09-15 15:36:28.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025791504, 82001, 1521901610784, '{"[]":[{"Moment":{"userId":38710,"maxId":1537025707417}},{"Moment":{"userId":70793,"maxId":551}},{"Moment":{"userId":82001,"maxId":1537025634931}},{"Moment":{"userId":82002,"maxId":1531062713966}},{"Moment":{"userId":82003,"maxId":1536805585275}},{"Moment":{"userId":82045,"maxId":1508073178489}},{"Moment":{"userId":82056,"maxId":1514858533480}},{"Moment":{"userId":93793,"maxId":1516086423441}},{"Moment":{"userId":1520242280259,"maxId":1520242333325}},{"Moment":{"userId":1523626157302,"maxId":1523936332614}}],"code":200,"msg":"success"}', '2018-09-15 15:36:31.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025795194, 82001, 1521902033332, '{"[]":[{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82021,82006,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}}],"total":121,"code":200,"msg":"success"}', '2018-09-15 15:36:35.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025798273, 82001, 1521903828410, '{"User":{"code":200,"msg":"success","count":72},"code":200,"msg":"success"}', '2018-09-15 15:36:38.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025799810, 82001, 1521903761689, '{"User":{"code":200,"msg":"success","count":119},"code":200,"msg":"success"}', '2018-09-15 15:36:39.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025801244, 82001, 1521902110680, '{"[]":[{"User":{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82021,82006,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"}},{"User":{"id":82003,"sex":1,"name":"Wechat","head":"http://common.cnblogs.com/images/wechat.png","contactIdList":[82001,93793],"pictureList":[],"date":"2017-02-01 19:21:50.0"}}],"code":200,"msg":"success"}', '2018-09-15 15:36:41.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025812404, 82001, 1521904653622, '{"User[]":[{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82002,"sex":1,"name":"Happy~","tag":"iOS","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000","contactIdList":[82005,82001,38710],"pictureList":[],"date":"2017-02-01 19:21:50.0"},{"id":82005,"sex":1,"name":"Jan","tag":"AG","head":"http://my.oschina.net/img/portrait.gif?t=1451961935000","contactIdList":[82001,38710,1532439021068],"pictureList":[],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-09-15 15:36:52.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025813024, 82001, 1521904617127, '{"Moment":{"userId":93793,"praiseUserIdList":[38710,93793,82003,82005,82040,82055,82002,82001],"isPraised":true},"code":200,"msg":"success"}', '2018-09-15 15:36:53.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025814838, 82001, 1521904756674, '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82021,82006,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-09-15 15:36:54.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025828828, 82001, 1521907303540, '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82021,82006,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-09-15 15:37:08.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025834254, 82001, 1521907333048, '{"User-id[]":[82002,82003,82005,82041,82045,82058,1512531601485,1528254173621],"Moment[]":[{"id":32,"userId":82002,"date":"2017-02-08 16:06:11.0","praiseUserIdList":[38710,82002,82001],"pictureList":["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},{"id":1508053762227,"userId":82003,"date":"2017-10-15 15:49:22.0","content":"我也试试","praiseUserIdList":[1515565976140],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1508072491570,"userId":82002,"date":"2017-10-15 21:01:31.0","content":"有点冷~","praiseUserIdList":[82001,82002],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1508073178489,"userId":82045,"date":"2017-10-15 21:12:58.0","content":"发动态","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1514017444961,"userId":82002,"date":"2017-12-23 16:24:04.0","content":"123479589679","praiseUserIdList":[82002,1520242280259,82001,70793,1524042900591,1528264711016],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"]},{"id":1531062713966,"userId":82002,"date":"2018-07-08 23:11:53.0","content":"云南好美啊( ◞˟૩˟)◞","praiseUserIdList":[82001,82005,38710,70793,93793,82003,1531969715979],"pictureList":["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072366455&di=c0d4b15b2c4b70aad49e6ae747f60742&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F3%2F57a2a41f57d09.jpg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531072499167&di=5b5621d117edbc5d344a03ba0a6b580b&imgtype=0&src=http%3A%2F%2Fi0.szhomeimg.com%2FUploadFiles%2FBBS%2F2006%2F08%2F05%2F24752199_79122.91.jpg"]},{"id":1536805585275,"userId":82003,"date":"2018-09-13 10:26:25.0","content":"iPhone Xs发布了，大家怎么看？","praiseUserIdList":[82002,82005,70793,82003,82001],"pictureList":["https://pic1.zhimg.com/80/v2-e129b40810070443add1c28e6185c894_hd.jpg"]}],"code":200,"msg":"success"}', '2018-09-15 15:37:14.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025841443, 82001, 1511967853340, '{"[]":[{"Moment":{"id":301,"userId":93793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-301","praiseUserIdList":[38710,93793,82003,82005,82040,82055,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":93793,"name":"Mike","head":"http://static.oschina.net/uploads/user/48/96331_50.jpg"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82040,"name":"Dream"},{"id":82055,"name":"Solid"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":45,"toId":0,"userId":93793,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-45"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":51,"toId":45,"userId":82003,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-51"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":76,"toId":45,"userId":93793,"momentId":301,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-76"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":120,"toId":0,"userId":93793,"momentId":301,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-110"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":124,"toId":0,"userId":82001,"momentId":301,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-114"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490781009548,"toId":51,"userId":82001,"momentId":301,"date":"2017-03-29 17:50:09.0","content":"3"},"User":{"id":82001,"name":"测试改名"}}]},{"Moment":{"id":58,"userId":90814,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-435","praiseUserIdList":[38710,82003,82005,93793,82006,82044,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg"]},"User":{"id":90814,"name":"007","head":"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000"},"User[]":[{"id":38710,"name":"TommyLemon"},{"id":82001,"name":"测试改名"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82006,"name":"Meria"},{"id":82044,"name":"Love"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":13,"toId":0,"userId":82005,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-13"},"User":{"id":82005,"name":"Jan"}},{"Comment":{"id":77,"toId":13,"userId":93793,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-77"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":97,"toId":13,"userId":82006,"momentId":58,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-97"},"User":{"id":82006,"name":"Meria"}},{"Comment":{"id":167,"userId":82001,"momentId":58,"date":"2017-03-25 19:48:41.0","content":"Nice!"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":173,"userId":38710,"momentId":58,"date":"2017-03-25 20:25:13.0","content":"Good"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":188,"toId":97,"userId":82001,"momentId":58,"date":"2017-03-26 15:21:32.0","content":"1646"},"User":{"id":82001,"name":"测试改名"}}]},{"Moment":{"id":371,"userId":82002,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-371","praiseUserIdList":[90814,93793,82003,82005,82006,82040,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"]},"User":{"id":82002,"name":"Happy~","head":"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000"},"User[]":[{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82003,"name":"Wechat"},{"id":82005,"name":"Jan"},{"id":82006,"name":"Meria"},{"id":82040,"name":"Dream"},{"id":90814,"name":"007"},{"id":93793,"name":"Mike"}],"[]":[{"Comment":{"id":68,"toId":0,"userId":82005,"momentId":371,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-68"},"User":{"id":82005,"name":"Jan"}},{"Comment":{"id":157,"userId":93793,"momentId":371,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-157"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":110,"toId":0,"userId":93793,"momentId":371,"date":"2017-02-01 19:23:24.0","content":"This is a Content...-110"},"User":{"id":93793,"name":"Mike"}},{"Comment":{"id":114,"toId":0,"userId":82001,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-114"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":115,"toId":0,"userId":38710,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-115"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":116,"toId":0,"userId":70793,"momentId":371,"date":"2017-03-02 13:56:06.0","content":"This is a Content...-116"},"User":{"id":70793,"name":"Strong"}}]},{"Moment":{"id":170,"userId":70793,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-73","praiseUserIdList":[82044,82002,82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]},"User":{"id":70793,"name":"Strong","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000"},"User[]":[{"id":82001,"name":"测试改名"},{"id":82002,"name":"Happy~"},{"id":82044,"name":"Love"}],"[]":[{"Comment":{"id":44,"toId":0,"userId":82003,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-44"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":54,"toId":0,"userId":82004,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-54"},"User":{"id":82004,"name":"Tommy"}},{"Comment":{"id":99,"toId":44,"userId":70793,"momentId":170,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-99"},"User":{"id":70793,"name":"Strong"}},{"Comment":{"id":206,"toId":54,"userId":82001,"momentId":170,"date":"2017-03-29 11:04:23.0","content":"ejej"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490780759866,"toId":99,"userId":82001,"momentId":170,"date":"2017-03-29 17:45:59.0","content":"99"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":1490863661426,"toId":1490780759866,"userId":70793,"momentId":170,"date":"2017-03-30 16:47:41.0","content":"66"},"User":{"id":70793,"name":"Strong"}}]},{"Moment":{"id":470,"userId":38710,"date":"2017-02-01 19:14:31.0","content":"This is a Content...-470","praiseUserIdList":[82001],"pictureList":["http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png"]},"User":{"id":38710,"name":"TommyLemon","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"},"User[]":[{"id":82001,"name":"测试改名"}],"[]":[{"Comment":{"id":4,"toId":0,"userId":38710,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-4"},"User":{"id":38710,"name":"TommyLemon"}},{"Comment":{"id":22,"toId":221,"userId":82001,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"测试修改评论"},"User":{"id":82001,"name":"测试改名"}},{"Comment":{"id":47,"toId":4,"userId":70793,"momentId":470,"date":"2017-02-01 19:20:50.0","content":"This is a Content...-47"},"User":{"id":70793,"name":"Strong"}},{"Comment":{"id":1490863507114,"toId":4,"userId":82003,"momentId":470,"date":"2017-03-30 16:45:07.0","content":"yes"},"User":{"id":82003,"name":"Wechat"}},{"Comment":{"id":1490863903900,"toId":0,"userId":82006,"momentId":470,"date":"2017-03-30 16:51:43.0","content":"SOGA"},"User":{"id":82006,"name":"Meria"}},{"Comment":{"id":1491740899179,"toId":0,"userId":82001,"momentId":470,"date":"2017-04-09 20:28:19.0","content":"www"},"User":{"id":82001,"name":"测试改名"}}]}],"code":200,"msg":"success"}', '2018-09-15 15:37:21.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1537025843337, 82001, 1511963677325, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,82012,82028,82021,82006,82030,82035],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-09-15 15:37:23.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1538112293306, 82001, 1521907303540, '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-09-28 05:24:53.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1538112310565, 82001, 1521905680680, '{"Moment":{"code":200,"msg":"success","id":1538112282445,"count":1},"code":200,"msg":"success"}', '2018-09-28 05:25:10.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1538112311450, 82001, 1521904756674, '{"User[]":[{"id":38710,"sex":0,"name":"TommyLemon","tag":"Android&Java","head":"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","contactIdList":[82003,82005,90814,82004,82009,82002,82044,93793,70793],"pictureList":["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000","http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"}],"code":200,"msg":"success"}', '2018-09-28 05:25:11.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1538731040138, 82001, 1511963677325, '{"User":{"id":82001,"sex":0,"name":"测试改名","tag":"APIJSON User","head":"https://static.oschina.net/uploads/user/19/39085_50.jpg","contactIdList":[82025,82024,82003,93793,70793],"pictureList":["http://common.cnblogs.com/images/icon_weibo_24.png"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-10-05 09:17:20.000000', null, null);
INSERT INTO sys."TestRecord" (id, "userId", "documentId", response, date, compare, standard) VALUES (1538731046687, 82001, 1521907570452, '{"Moment":{"id":12,"userId":70793,"date":"2017-02-08 16:06:11.0","content":"1111534034","praiseUserIdList":[70793,93793,82044,82040,82055,90814,38710,82002,82006,1508072105320],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067","http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067","https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067","https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067"]},"User":{"id":70793,"sex":0,"name":"Strong","tag":"djdj","head":"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000","contactIdList":[38710,82002],"pictureList":["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg","http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg","https://camo.githubusercontent.com/788c0a7e11a","https://camo.githubusercontent.com/f513f67"],"date":"2017-02-01 19:21:50.0"},"code":200,"msg":"success"}', '2018-10-05 09:17:27.000000', null, null);
create table "Verify"
(
    id     bigint                                 not null
        constraint "Verify_pkey"
            primary key,
    type   integer                                not null,
    phone  varchar(11)                            not null,
    verify varchar(32)                            not null,
    date   timestamp(6) default CURRENT_TIMESTAMP not null
);

comment on column "Verify".id is '唯一标识';

comment on column "Verify".type is '类型：
0-登录
1-注册
2-修改登录密码
3-修改支付密码';

comment on column "Verify".phone is '手机号';

comment on column "Verify".verify is '验证码';

comment on column "Verify".date is '创建时间';

alter table "Verify"
    owner to postgres;

INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1527950171719, 1, '13000084444', '10375', '2018-06-02 14:36:11.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528250810515, 1, '15122820115', '2586', '2018-06-06 02:06:50.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528254139866, 1, '15225556855', '8912', '2018-06-06 03:02:19.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528255485691, 1, '15822798927', '2101', '2018-06-06 03:24:45.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528264687329, 1, '15620878773', '3991', '2018-06-06 05:58:07.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528269508031, 1, '18616024605', '4901', '2018-06-06 07:18:28.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528289406640, 1, '13142033348', '3005', '2018-06-06 12:50:06.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528327843188, 0, '15122820115', '4912', '2018-06-06 23:30:43.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528330720259, 2, '15122820115', '5267', '2018-06-07 00:18:40.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528339646013, 1, '15122541683', '6112', '2018-06-07 02:47:26.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528344962707, 1, '15188899797', '4540', '2018-06-07 04:16:02.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528345364195, 2, '15122541683', '10500', '2018-06-07 04:22:44.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528345372998, 0, '15122541683', '9940', '2018-06-07 04:22:53.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528356342784, 2, '15620878773', '2076', '2018-06-07 07:25:42.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528356449927, 1, '15620878772', '4733', '2018-06-07 07:27:29.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528411937273, 0, '15620878772', '7375', '2018-06-07 22:52:17.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1531793525394, 1, '15629184762', '9737', '2018-07-17 02:12:05.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1531969702694, 1, '13800138000', '8213', '2018-07-19 03:08:22.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1531983017848, 2, '13800138000', '1552', '2018-07-19 06:50:17.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1532188103364, 1, '13977757845', '8753', '2018-07-21 15:48:23.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1532439015405, 1, '18779607703', '10136', '2018-07-24 13:30:15.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533450371245, 1, '18911061423', '5795', '2018-08-05 06:26:11.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533618759900, 1, '13977757843', '10204', '2018-08-07 05:12:39.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533627819054, 1, '13107695518', '7515', '2018-08-07 07:43:39.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533693421836, 1, '15901373410', '4884', '2018-08-08 01:57:01.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533698902309, 1, '18664900086', '3654', '2018-08-08 03:28:22.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533723898511, 1, '8881816', '5272', '2018-08-08 10:24:58.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533835163777, 1, '13977757846', '9332', '2018-08-09 17:19:23.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534142797624, 2, '13977757845', '4136', '2018-08-13 06:46:37.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534142802751, 0, '13977757845', '5754', '2018-08-13 06:46:42.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534671951719, 1, '13000082023', '5869', '2018-08-19 09:45:51.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534671960833, 1, '13000082013', '6169', '2018-08-19 09:46:00.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534671980295, 1, '13000082032', '10171', '2018-08-19 09:46:20.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534672028733, 0, '13000093793', '6478', '2018-08-19 09:47:08.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534898613829, 1, '17755531490', '3961', '2018-08-22 00:43:33.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534926287534, 1, '17602120205', '5297', '2018-08-22 08:24:47.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1535345181813, 1, '13000082022', '8145', '2018-08-27 04:46:21.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1536220749108, 1, '13241042199', '9916', '2018-09-06 07:59:09.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1536583466723, 1, '18013819609', '2483', '2018-09-10 12:44:26.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1536805630726, 0, '13000070793', '1798', '2018-09-13 02:27:10.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537515268266, 2, '13000038710', '4171', '2018-09-21 07:34:28.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537515280163, 2, '13000038713', '5877', '2018-09-21 07:34:40.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537515287973, 2, '13000038714', '10441', '2018-09-21 07:34:47.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537515337629, 1, '13000033333', '7353', '2018-09-21 07:35:37.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537521279290, 1, '13000049499', '2854', '2018-09-21 09:14:39.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1538114970328, 1, '15855512382', '10359', '2018-09-28 06:09:30.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1538504245612, 1, '13000087654', '10791', '2018-10-02 18:17:25.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1538504485798, 1, '13000087655', '4776', '2018-10-02 18:21:25.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1538987940551, 1, '18662327672', '1800', '2018-10-08 08:39:00.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1539076064496, 1, '15094295280', '3361', '2018-10-09 09:07:44.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1539076102284, 1, '15094395280', '1562', '2018-10-09 09:08:22.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1540364623910, 1, '13000085001', '4382', '2018-10-24 07:03:43.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1540966375865, 1, '13122091271', '3880', '2018-10-31 06:12:55.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541427928139, 0, '13759127249', '5913', '2018-11-05 14:25:28.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541500666143, 1, '15280239960', '9142', '2018-11-06 10:37:46.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541510152560, 1, '13000099999', '1097', '2018-11-06 13:15:52.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541510270190, 1, '13000077777', '8192', '2018-11-06 13:17:50.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541583746916, 1, '18689846285', '8724', '2018-11-07 09:42:26.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541724338244, 0, '13000038710', '8283', '2018-11-09 00:45:38.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541757538733, 1, '17717112856', '2268', '2018-11-09 09:58:58.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542261432641, 1, '15800506515', '2586', '2018-11-15 05:57:12.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542265654497, 1, '18010001000', '5666', '2018-11-15 07:07:34.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542337959344, 1, '13000012345', '4981', '2018-11-16 03:12:39.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542352629436, 1, '13000082002', '10212', '2018-11-16 07:17:09.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542548523509, 1, '13317833374', '8921', '2018-11-18 13:42:03.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542702268195, 1, '123123', '8055', '2018-11-20 08:24:28.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542840424025, 1, '13818118257', '7126', '2018-11-21 22:47:04.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1543377157089, 1, '18622250185', '6620', '2018-11-28 03:52:37.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1543966631575, 1, '13466260815', '5835', '2018-12-04 23:37:11.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544276193603, 1, '13000087656', '4078', '2018-12-08 13:36:33.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544276277509, 2, '13000087656', '9356', '2018-12-08 13:37:57.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544276305800, 0, '13000087656', '8708', '2018-12-08 13:38:25.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544276475231, 1, '15988125475', '8940', '2018-12-08 13:41:15.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544503797981, 1, '13000082968', '6965', '2018-12-11 04:49:57.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545238881566, 1, '13166059778', '4434', '2018-12-19 17:01:21.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545269417538, 1, '18124099720', '4882', '2018-12-20 01:30:17.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545464407106, 1, '17755015200', '3870', '2018-12-22 07:40:07.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545707514503, 1, '13533039558', '3941', '2018-12-25 03:11:54.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545895656481, 1, '13533039550', '4968', '2018-12-27 07:27:36.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1546701633801, 1, '13534201057', '8487', '2019-01-05 15:20:33.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1547177422559, 1, '18980210241', '7012', '2019-01-11 03:30:22.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548068010027, 1, '17181595855', '10716', '2019-01-21 10:53:30.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548310439111, 1, '13059203278', '9438', '2019-01-24 06:13:59.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548398132694, 1, '15050529772', '9276', '2019-01-25 06:35:32.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548742004597, 1, '13738007826', '6318', '2019-01-29 06:06:44.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548742060130, 1, '13000082001', '8387', '2019-01-29 06:07:40.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548742124507, 6, '13000082001', '4901', '2019-01-29 06:08:44.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548742151361, 10, '13000082001', '8513', '2019-01-29 06:09:11.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1549548031095, 4, '13000082001', '1234', '2019-09-14 22:00:31.115000');