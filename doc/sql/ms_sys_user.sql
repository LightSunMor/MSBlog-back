-- auto-generated definition
create table ms_sys_user
(
    id           bigint auto_increment comment '主键'
        primary key,
    user_name    varchar(64) default 'NULL' not null comment '用户名',
    nick_name    varchar(64) default 'NULL' not null comment '昵称',
    password     varchar(64) default 'NULL' not null comment '密码',
    type         char        default '0'    null comment '用户类型：0代表普通用户，1代表管理员',
    status       char        default '0'    null comment '账号状态（0正常 1停用）',
    email        varchar(64)                null comment '邮箱',
    phone_number varchar(32)                null comment '手机号',
    sex          char                       null comment '用户性别（0男，1女，2未知）',
    avatar       varchar(255)               null comment '头像',
    create_by    bigint                     null comment '创建人的用户id',
    create_time  datetime                   null comment '创建时间',
    update_by    bigint                     null comment '更新人',
    update_time  datetime                   null comment '更新时间',
    del_flag     int         default 0      null comment '删除标志（0代表未删除，1代表已删除）',
    constraint ms_sys_user_user_name_uindex
        unique (user_name)
)
    comment '用户表' charset = utf8mb4;

