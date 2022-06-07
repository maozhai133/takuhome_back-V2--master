package com.takuhome.back.service.impl;

import com.takuhome.back.dao.CommentDao;
import com.takuhome.back.entity.Comment;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务实现层
 *
 * @Title:CommentServiceImpl
 * @Author:NekoTaku
 * @Date:2022/06/06 9:20
 * @Version:1.0
 */
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentDao commentDao;

    /**
     * 发表评论
     *
     * @param comment
     * @return
     */
    @Override
    public Results<Comment> addComment(Comment comment) {
        if (commentDao.addComment(comment) > 0) {
            //评论成功
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 统计评论数量
     *
     * @param articleId
     * @return
     */
    @Override
    public Long countCommentNumber(Integer articleId) {
        return commentDao.countCommentNumber(articleId);
    }

    /**
     * 根据博文id查询相关评论
     *
     * @param articleId
     * @return
     */
    @Override
    public List<Comment> selectCommentById(Integer articleId) {
        return commentDao.selectCommentById(articleId);
    }

    /**
     * 查询所有评论并分页
     *
     * @param pageNum
     * @param limit
     * @param articleUserName
     * @return
     */
    @Override
    public Results<Comment> getAllComment(Integer pageNum, Integer limit, String articleUserName) {
        return Results.success(commentDao.countAllComment(articleUserName).intValue(),
                commentDao.getAllComment(pageNum, limit, articleUserName));
    }

    /**
     * 根据id删除评论
     *
     * @param articleUserName
     * @param commentId
     * @return
     */
    @Override
    public Results<Comment> delCommentById(String articleUserName, Integer commentId) {
        if (commentId != null) {
            if (commentDao.delCommentById(articleUserName, commentId) > 0) {
                return Results.success();
            }
            return Results.failure();
        }
        return Results.failure();
    }

}
