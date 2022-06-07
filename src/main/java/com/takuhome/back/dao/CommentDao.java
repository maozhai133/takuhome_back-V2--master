package com.takuhome.back.dao;

import com.takuhome.back.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Title:CommentDao
 * @Author:NekoTaku
 * @Date:2022/06/06 8:56
 * @Version:1.0
 */
@Mapper
@Repository
public interface CommentDao {

    //1.发表评论
    Integer addComment(Comment comment);

    //2.统计评论数量
    Long countCommentNumber(@Param("articleId")Integer articleId);

    //3.根据博文id查询相关评论
    List<Comment> selectCommentById(@Param("articleId")Integer articleId);

    
}
