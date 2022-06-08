package com.takuhome.back.controller;

import com.takuhome.back.entity.Article;
import com.takuhome.back.entity.Comment;
import com.takuhome.back.entity.SysUser;
import com.takuhome.back.result.Page;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IArticleService;
import com.takuhome.back.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title:CommentController
 * @Author:NekoTaku
 * @Date:2022/06/06 9:11
 * @Version:1.0
 */
@Controller
@RequestMapping(value = "/Comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IArticleService articleService;

    /**
     * 发表评论
     * @param request
     * @param comment
     * @return
     */
    @PostMapping(value = "/addComment")
    @ResponseBody
    public Results<Comment> addComment(HttpServletRequest request,Comment comment){
        //获取用户名
        String userName = ((SysUser) request.getSession().getAttribute("user")).getUserName();
        comment.setArticleUserName(userName);

        Results<Comment> commentResults = commentService.addComment(comment);
        //更改博文信息表的评论数量
        Long commentNumber = commentService.countCommentNumber(comment.getArticleId());
        System.out.println("评论数量"+commentNumber);
        Article articleById = articleService.getArticleById(comment.getArticleId(), userName);
        articleById.setCountComment(Integer.parseInt(String.valueOf(commentNumber)));
        articleById.setUserName(userName);
//        System.out.println(articleById);
        System.out.println("成功添加评论："+articleService.updateArticle(articleById));

        return commentResults;
    }

    /**
     * 获取当前用户所有评论，并分页
     * @param page
     * @param userName
     * @return
     */
    @GetMapping(value = "/CommentList")
    @ResponseBody
    public Results<Comment> list(Page page,String userName){
        System.out.println("获取当前用户全部评论，并分页");
        page.countOffset();
        return commentService.getAllComment(page.getOffset(), page.getLimit(), userName);

    }

    /**
     * 删除评论
     *
     * @param commentId
     * @param userName
     * @return
     */
    @DeleteMapping(value = "/delComment")
    @ResponseBody
    public Results<Comment> delComment(Integer commentId,String userName,Integer articleId){
//        System.out.println("调用删除评论");

        //更改博文评论数量
        Article articleById = articleService.getArticleById(articleId, userName);
        Integer countComment = articleById.getCountComment();
        countComment -=1;
        articleById.setCountComment(countComment);
        articleById.setUserName(userName);
        System.out.println("成功删除评论："+articleService.updateArticle(articleById));

        return commentService.delCommentById(userName,commentId);
    }



}
