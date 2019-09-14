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

