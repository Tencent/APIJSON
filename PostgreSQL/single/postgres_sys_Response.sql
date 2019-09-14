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