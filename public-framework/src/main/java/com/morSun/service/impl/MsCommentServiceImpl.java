package com.morSun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morSun.common.BaseResponse;
import com.morSun.common.ErrorCode;
import com.morSun.common.ResultUtil;
import com.morSun.constants.SystemConstants;
import com.morSun.dto.CommentAddDto;
import com.morSun.exception.SystemException;
import com.morSun.mapper.UserMapper;
import com.morSun.pojo.MsComment;
import com.morSun.pojo.User;
import com.morSun.service.MsCommentService;
import com.morSun.mapper.MsCommentMapper;
import com.morSun.utils.BeanCopyUtils;
import com.morSun.vo.PageVo;
import com.morSun.vo.commentVo.CommentMsgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author 86176
* @description 针对表【ms_comment(评论表)】的数据库操作Service实现
* @createDate 2022-12-22 15:51:36
*/
@Service
@Slf4j
public class MsCommentServiceImpl extends ServiceImpl<MsCommentMapper, MsComment>
    implements MsCommentService{

    @Autowired
    private UserMapper userMapper;
    /***
     *  查询对应的评论
     *
     * @param commentType
     * @param articleId
     * @param pageNum
     * @param pageSize 10  前端规定一次查询十条
     * @return
     */
    @Override
    public BaseResponse commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        // 分页查询对应文章的 根评论
        LambdaQueryWrapper<MsComment> queryWrapper = new LambdaQueryWrapper<>();
        //因为友链和文章的评论都是用这个方法，所以要判断评论类型是文章，才加上文章限制类型
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),MsComment::getArticleId,articleId);
        queryWrapper.eq(MsComment::getRootId, SystemConstants.NO_ROOT_COMMENT);
        queryWrapper.eq(MsComment::getType,commentType);
        queryWrapper.orderByAsc(MsComment::getCreateTime);
        Page<MsComment> page = new Page<>(pageNum, pageSize);
        Page<MsComment> commentPage = this.page(page, queryWrapper);

        //封装为Vo
        List<MsComment> commentList = commentPage.getRecords();
              // 如果没有拷贝上内容的属性，直接不显示到json中了
        //！！ 使用封装方法转换Vo，因为这里用了，子评论也会用，不封装为方法，就会写两堆一样的代码，就违背的开发原则
        List<CommentMsgVo> commentMsgVoList=changeToCommentMsgVo(commentList);

            //查询所有根评论对应的 子评论
        List<CommentMsgVo> commentMsgVoListAll = commentMsgVoList.stream().map(rootVo -> {
            Long rootVoId = rootVo.getId();
            //定义方法拿到子评论的正确返回值
            List<CommentMsgVo> childrenList = this.getChildrenList(rootVoId);
            rootVo.setChildren(childrenList);
            return rootVo;
        }).toList();

        // 封装为pageVo
        PageVo pageVo = new PageVo();
        pageVo.setRows(commentMsgVoListAll);
        pageVo.setTotal(commentPage.getTotal());
        return ResultUtil.success(pageVo);
    }

    /**
     *  添加评论
     * @param commentAddDto
     * @return
     */
    @Override
    public BaseResponse<String> addComment(CommentAddDto commentAddDto) {
        if (!StringUtils.hasText(commentAddDto.getContent()))
            throw new SystemException(ErrorCode.NO_CONTENT);
        MsComment comment = BeanCopyUtils.copyBean(commentAddDto, MsComment.class);
        log.info("要插入的评|论信息如下\n:{}",comment.toString());
        //如果里面报错了，会直接抛出异常
        this.save(comment);
        return ResultUtil.success("添加评论成功");
    }


    /***
     *  根据根id查询子评论的响应返回值,直接查询所有的子评论
     *  todo 待改进为先显示三条子评论，如果前端点击更多，再分页显示多条子评论
     * @param rootVoId
     * @return
     */
    private List<CommentMsgVo> getChildrenList(Long rootVoId) {
        LambdaQueryWrapper<MsComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MsComment::getRootId,rootVoId);
        List<MsComment> commentList = this.list(wrapper); // 查出所有的子评论
        List<CommentMsgVo> childrenList = changeToCommentMsgVo(commentList);
        return childrenList;
    }

    /**
     *  转为Vo返回值
     * @param commentList
     * @return
     */
    private List<CommentMsgVo> changeToCommentMsgVo(List<MsComment> commentList)
    {
        List<CommentMsgVo> commentMsgVoList = BeanCopyUtils.copyBeanList(commentList, CommentMsgVo.class);
        for (CommentMsgVo vo : commentMsgVoList) {
            User user = userMapper.selectById(vo.getCreateBy());
            vo.setUsername(user.getNickName()); // 为评论消息绑定评论人的昵称（每一条评论都会）
            vo.setAvatarUrl(user.getAvatar()); //为评论信息绑定评论人的昵称 （每一条评论都会经过）
            // 如果有回复目标评论
            if (vo.getToCommentUserId()!=-1)
            {
                User toCommentUser = userMapper.selectById(vo.getToCommentUserId());
                vo.setToCommentUserName(toCommentUser.getNickName());
                //注意这里设置的是当前评论回复目标评论的。不要将回复目标的头像，放入setAvatarUrl方法中（因为一条评论只有一个头像）
            }
        }
        return commentMsgVoList;
    }

}




