-- auto-generated definition
create table ms_category
(
    id          bigint(200) auto_increment
        primary key,
    name        varchar(128)            null comment '分类名',
    pid         bigint(200) default -1  null comment '父分类id，如果没有父分类为-1',
    description varchar(512)            null comment '描述',
    status      char        default '0' null comment '状态0:正常,1禁用',
    create_by   bigint(200)             null,
    create_time datetime                null,
    update_by   bigint(200)             null,
    update_time datetime                null,
    del_flag    int         default 0   null comment '删除标志（0代表未删除，1代表已删除）'
)
    comment '分类表' charset = utf8mb4;

