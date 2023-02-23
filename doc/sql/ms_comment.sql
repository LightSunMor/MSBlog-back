-- auto-generated definition
create table ms_comment
(
    id                 bigint auto_increment
        primary key,
    type               char   default '0' null comment '评论类型（0代表文章评论，1代表友链评论）',
    article_id         bigint             null comment '文章id',
    root_id            bigint default -1  null comment '根评论id',
    content            varchar(512)       null comment '评论内容',
    to_comment_user_id bigint default -1  null comment '所回复的目标评论的userid',
    to_comment_id      bigint default -1  null comment '回复目标评论id',
    create_by          bigint             null,
    create_time        datetime           null,
    update_by          bigint             null,
    update_time        datetime           null,
    del_flag           int(1) default 0   null comment '删除标志（0代表未删除，1代表已删除）'
)
    comment '评论表' charset = utf8mb4;

