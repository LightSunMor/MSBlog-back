-- auto-generated definition
create table ms_sys_menu
(
    id          bigint auto_increment comment '菜单ID'
        primary key,
    menu_name   varchar(50)              not null comment '菜单名称',
    parent_id   bigint       default 0   null comment '父菜单ID,没有父菜单为0',
    order_num   int(4)       default 0   null comment '显示顺序',
    path        varchar(200) default ''  null comment '路由地址',
    component   varchar(255)             null comment '组件路径',
    is_frame    int(1)       default 1   null comment '是否为外链（0是 1否）',
    menu_type   char         default ''  null comment '菜单类型（M目录 C菜单 F按钮）',
    visible     char         default '0' null comment '菜单状态（0显示 1隐藏）',
    status      char         default '0' null comment '菜单状态（0正常 1停用）',
    perms       varchar(100)             null comment '权限标识',
    icon        varchar(100) default '#' null comment '菜单图标',
    create_by   bigint                   null comment '创建者',
    create_time datetime                 null comment '创建时间',
    update_by   bigint                   null comment '更新者',
    update_time datetime                 null comment '更新时间',
    remark      varchar(500) default ''  null comment '备注',
    del_flag    char         default '0' null
)
    comment '菜单权限表';

