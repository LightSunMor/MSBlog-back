-- auto-generated definition
create table ms_sys_role
(
    id          bigint auto_increment comment '角色ID'
        primary key,
    role_name   varchar(30)      not null comment '角色名称',
    role_key    varchar(100)     not null comment '角色权限字符串',
    role_sort   int(4)           not null comment '显示顺序',
    status      char             not null comment '角色状态（0正常 1停用）',
    del_flag    char default '0' null comment '删除标志（0代表存在 1代表删除）',
    create_by   bigint           null comment '创建者',
    create_time datetime         null comment '创建时间',
    update_by   bigint           null comment '更新者',
    update_time datetime         null comment '更新时间',
    remark      varchar(500)     null comment '备注'
)
    comment '角色信息表';

