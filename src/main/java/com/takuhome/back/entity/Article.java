package com.takuhome.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 博文实体类
 *
 * @author nekotaku
 * @create 2021-09-16 10:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {


    private static final long serialVersionUID = -2365569918662682545L;
    private Integer articleId;//博文ID
    private String articleContent;//博文内容
    private Integer articleLikeNum;//点赞数
    private Integer articleViews;//浏览数
    private String articleImage;//封面图片URL
    private String articleTitle;//博文标题

    private String articleCreateTime;//博文创建时间
    private Integer articleIsTop;//是否置顶博文 0:不置顶/1:置顶
    private Integer categoryId;//分类ID
    private String categoryName;//分类名称
    private String articleDesc;//博文描述
    private Integer countComment;//评论数量

    private Integer articleTag1;//标签1
    private Integer articleTag2;//标签2
    private Integer articleTag3;//标签3

    private String userName;//用户名
}
