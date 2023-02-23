-- auto-generated definition
create table ms_article_tag
(
    article_id bigint auto_increment comment '文章id',
    tag_id     bigint default 0 not null comment '标签id',
    primary key (article_id, tag_id)
)
    comment '文章标签关联表' charset = utf8mb4;

