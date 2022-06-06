package com.takuhome.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Title:Comment
 * @Author:NekoTaku
 * @Date:2022/06/06 8:44
 * @Version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = -2856891861217722777L;

    private Integer commentId;//评论id
    private String commentNickName;//评论用户昵称
    private String commentContent;//评论内容
    private String commentCreatedTime;//评论发表时间
    private String commentUserEmail;//评论用户邮箱

    private Integer articleId;//相关博文id
    private String articleUserName;//相关博文用户名
    private String articleTitle;//相关博文标题


}
