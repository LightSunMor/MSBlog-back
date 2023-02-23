-- auto-generated definition
create table ms_link
(
    id          bigint auto_increment
        primary key,
    name        varchar(256)       null,
    logo        varchar(256)       null,
    description varchar(512)       null,
    address     varchar(128)       null comment '网站地址',
    status      char   default '2' null comment '审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)',
    create_by   bigint             null,
    create_time datetime           null,
    update_by   bigint             null,
    update_time datetime           null,
    del_flag    int(1) default 0   null comment '删除标志（0代表未删除，1代表已删除）'
)
    comment '友链' charset = utf8mb4;

