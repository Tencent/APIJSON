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