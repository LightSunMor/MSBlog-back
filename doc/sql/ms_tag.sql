-- auto-generated definition
create table ms_tag
(
    id          bigint auto_increment
        primary key,
    name        varchar(128)     null comment '标签名',
    create_by   bigint           null,
    create_time datetime         null,
    update_by   bigint           null,
    update_time datetime         null,
    del_flag    int(1) default 0 null comment '删除标志（0代表未删除，1代表已删除）',
    remark      varchar(500)     null comment '备注'
)
    comment '标签' charset = utf8mb4;

