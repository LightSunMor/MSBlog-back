package com.morSun.constants;


public class SystemConstants {
    /**
     *  文章正常状态，已发布
     *
     *  💦分类信息状态正常
     *
     *  💬友链申请已通过
     */
    public static final Character STATUS_NORMAL ='0';
    /**
     *  文章未发布
     *
     *  💦分类信息被禁用
     *
     *  💬友链申请未通过
     */
    public static final Character STATUS_NOT_NORMAL ='1';
    /**
     * 💬友链申请未审核状态
     */
    public static final Character STATUS_NOT_EXAMINE ='2';
    /**
     *热门文章限定取五个
     */
    public static final Integer CURRENT_PAGE=1;
    public static final Integer HOT_ARTICLE_NUMS=5;
    /***
     *  用户登录信息存入redis 的KEY前缀   前台
     */
    public static final String REDIS_LOGIN_USER_FRONT_PREFIX ="blogLogin:";
    /**
     *  用户登录信息存入redis key前缀   后台
     */
    public static final String REDIS_LOGIN_USER_BACK_PREFIX ="blogLoginBack:";

    /**
     *  评论没有根评论的rootId值
     */
    public static final Long NO_ROOT_COMMENT=-1L;
    /**
     *  文章评论类型 0
     */
    public static final String ARTICLE_COMMENT="0";
    /**
     * 友链评论类型 1
     */
    public static final String LINK_COMMENT="1";

    /**
     * 上传图片存储到map的key
     */
    public static final String PIC_AVATAR_PATH="file_path";


    /* -------*/
    /**
     *  viewcount的map集合存储key
     *  这种为key加上冒号的写法，key将同为article开头的数据保存到一个文件下，集中管理
     */
    public static final String REDIS_VIEW_COUNT_KEY = "article:vck";

    /**
     *  权限类型：M代表目录，C代表菜单，F代表按钮
     */
    public static final String MENU = "C";
    public static final String BUTTON = "F";
    public static final String DIRECTORY="M";

    /**
     *  权限菜单，没有父菜单的parent_id
     */
    public static final Long NO_PARENT_MENU=0L;

    /**
     *  后台用户关键字
     */
    public static final String IS_BACK_USER = "1";

    /**
     *  已删除标志
     */
    public static final Integer HAS_DELETE_FLAG=1;
};
