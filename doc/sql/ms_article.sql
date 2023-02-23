-- auto-generated definition
create table ms_article
(
    id            bigint(200) auto_increment
        primary key,
    title         varchar(255)     null comment '标题',
    content       longtext         null comment '文章内容',
    type          char             null comment '文章类型',
    summary       varchar(1024)    null comment '文章摘要（大概介绍）',
    category_id   bigint           null comment '所属分类id
',
    thumbnail     varchar(255)     null comment '缩略图',
    is_top        char default '0' null comment '是否置顶 0-否 1-是',
    status        char             null comment '状态：0-已经发布 1-草稿',
    comment_count int              not null comment '评论数',
    view_count    bigint(200)      not null comment '访问量',
    is_comment    char default '1' null comment '是否允许评论：1-是 0-否',
    create_by     bigint           null comment '创建人',
    create_time   datetime         null,
    update_by     bigint           null comment '更新人',
    update_time   datetime         null,
    del_flag      int  default 0   null comment '删除标志：0-未删除 1-已经删除'
)
    comment '博客的文章数据表';

