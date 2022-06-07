package com.takuhome.back.dao;

import com.takuhome.back.entity.Article;
import com.takuhome.back.entity.ArticleCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 博文dao层
 *
 * @author nekotaku
 * @create 2022-01-23 15:35
 */
@Mapper
@Repository
public interface ArticleDao {

    //1.添加博文
    Integer addArticle(@Param("articleContent") String articleContent, @Param("articleLikeNum") Integer articleLikeNum
            , @Param("articleViews") Integer articleViews, @Param("articleImage") String articleImage
            , @Param("articleTitle") String articleTitle, @Param("articleCreateTime") String articleCreateTime
            , @Param("articleIsTop") Integer articleIsTop, @Param("categoryId") Integer categoryId
            , @Param("articleDesc") String articleDesc, @Param("countComment") Integer countComment
            , @Param("articleTag1") Integer articleTag1, @Param("articleTag2") Integer articleTag2
            , @Param("articleTag3") Integer articleTag3, @Param("categoryName") String categoryName
            , @Param("userName") String userName);

    //2.博文标题查重
    List<Article> getArticleByTitle(@Param("articleTitle") String articleTitle,
                                    @Param("userName") String userName);

    //3.获取所有博文并分页
    List<Article> getAllArticle(@Param("pageNum") Integer pageNum, @Param("limit") Integer limit,
                                @Param("userName") String userName);

    Long countAllArticle(@Param("userName") String userName);

    //4.根据id获取博文
    Article getArticleById(@Param("articleId") Integer articleId,
                           @Param("userName") String userName);

    //5.模糊查询并分页
    List<Article> getAllArticleByTitle(@Param("articleTitle") String articleTitle,
                                       @Param("pageNum") Integer pageNum,
                                       @Param("limit") Integer limit,
                                       @Param("userName") String userName);

    Long countAllArticleByTitle(String articleTitle,@Param("userName") String userName);

    //6.删除博文
    int deleteArticleById(@Param("articleId") Integer articleId,
                          @Param("userName") String userName);

    //7.修改博文
//    int updateArticle(@Param("articleContent") String articleContent, @Param("articleLikeNum") Integer articleLikeNum
//            , @Param("articleViews") Integer articleViews, @Param("articleImage") String articleImage
//            , @Param("articleTitle") String articleTitle, @Param("articleCreateTime") String articleCreateTime
//            , @Param("articleIsTop") Integer articleIsTop, @Param("categoryId") Integer categoryId
//            , @Param("articleDesc") String articleDesc, @Param("countComment") Integer countComment
//            , @Param("articleTag1") Integer articleTag1, @Param("articleTag2") Integer articleTag2
//            , @Param("articleTag3") Integer articleTag3, @Param("categoryName") String categoryName);
    int updateArticle(Article article);

    //8.根据分类id查询博文(删除分类时的检测)
    List<Article> checkCategoryById(@Param("categoryId")Integer categoryId,@Param("userName")String userName);

    //9.根据标签id查询博文(删除标签时的检测)
    List<Article> checkTagById(@Param("tagId")Integer tagId);

    //10.根据用户名查询博文(前台用)
    List<Article> selectByUserName(@Param("userName")String userName);

    //11.查询博文相关数量信息(博文总数和评论总数)
    ArticleCount countArticleInfoByUserName(@Param("userName")String userName);
}

