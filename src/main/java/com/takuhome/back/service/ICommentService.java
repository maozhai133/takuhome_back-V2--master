package com.takuhome.back.service;

import com.takuhome.back.entity.Comment;
import com.takuhome.back.result.Results;

import java.util.List;

/**
 * 评论服务层
 *
 * @Title:ICommentService
 * @Author:NekoTaku
 * @Date:2022/06/06 9:19
 * @Version:1.0
 */
public interface ICommentService {

    //1.发表评论
    Results<Comment> addComment(Comment comment);

    //2.统计评论数量
    Long countCommentNumber(Integer articleId);

    //3.根据博文id查询相关评论
    List<Comment> selectCommentById(Integer articleId);

}
