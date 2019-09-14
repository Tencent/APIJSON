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