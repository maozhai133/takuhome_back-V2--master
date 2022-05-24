package com.takuhome.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title:ArticleCount
 * @Author:NekoTaku
 * @Date:2022/05/23 11:11
 * @Version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCount {

    private Integer countArticles;//博文数量
    private Integer countComments;//评论数量
}
