package com.takuhome.back.controller;

import com.takuhome.back.entity.Comment;
import com.takuhome.back.entity.SysUser;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
//        System.out.println("获取评论信息"+comment);
        return commentService.addComment(comment);
    }


}
