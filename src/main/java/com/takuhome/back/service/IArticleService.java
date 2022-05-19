package com.takuhome.back.service;

import com.takuhome.back.entity.Article;
import com.takuhome.back.result.Results;

import java.util.List;

/**
 * 博文管理服务层
 *
 * @author nekotaku
 * @create 2021-10-14 10:21
 */
public interface IArticleService {

    //1.添加博文
    Results<Article> addArticle(String articleContent,String articleImage
            ,String articleTitle,Integer articleIsTop,Integer categoryId
            ,String articleDesc,Integer articleTag1,Integer articleTag2
            ,Integer articleTag3,String userName);

    //2.博文标题查重
    List<Article> getArticleByTitle(String articleTitle,String userName);

    //3.查询所有博文并分页
    Results<Article> getAllArticle(Integer pageNum, Integer limit,String userName);

    //4.根据id获取博文
    Article getArticleById(Integer articleId,String userName);

    //5.模糊查询并分页
    Results<Article> findAllArticleByTitle(String articleTitle,Integer pageNum, Integer limit,
                                           String userName);

    //6.删除博文
    Results<Article> deleteArticleById(Integer articleId,String userName);

    //7.批量删除博文
    Results<Article> deleteArticleByCheck(String ids,String userName);

    //8.修改博文
    Results<Article> updateArticle(Article article);

    //9.查询文章总数
    Long countAllArticle(String userName);

    //10.根据分类id查询博文(删除分类时检测)
    List<Article> checkCategoryById(Integer categoryId,String userName);

    //11.根据标签id查询博文(删除标签时检测)
    List<Article> checkTagById(Integer tagId);
}
