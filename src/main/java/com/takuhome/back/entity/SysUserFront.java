package com.takuhome.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *  前台用户信息映射实体层
 *
 * @Title:SysUserFront
 * @Author:NekoTaku
 * @Date:2022/05/23 10:18
 * @Version:1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserFront {

    private String admId; //用户id(唯一id 自增)
    private String userName; //用户名(登录用)
    private String nickName; //用户昵称
    private String headImg; //用户头像Url
    private String userEmail; //用户邮箱
    private String userDesc;//个人简介

    private ArticleCount articleCount;//博文评论和文章数量
    private Long countTags;//标签数量
    private List<Tag> tags;//标签列表
}
