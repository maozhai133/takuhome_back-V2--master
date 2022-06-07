package com.takuhome.back.service.impl;

import com.takuhome.back.dao.ArticleDao;
import com.takuhome.back.dao.CategoryDao;
import com.takuhome.back.entity.Article;
import com.takuhome.back.entity.ArticleCount;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IArticleService;
import com.takuhome.back.util.time.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 博文管理服务实现层
 *
 * @author nekotaku
 * @create 2021-10-14 10:21
 */

@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    CategoryDao categoryDao;

    /**
     * 添加博文
     *
     * @param articleContent
     * @param articleImage
     * @param articleTitle
     * @param articleIsTop
     * @param categoryId
     * @param articleDesc
     * @param articleTag1
     * @param articleTag2
     * @param articleTag3
     * @param userName
     * @return
     */
    @Override
    public Results<Article> addArticle(String articleContent, String articleImage,
                                       String articleTitle, Integer articleIsTop,
                                       Integer categoryId, String articleDesc,
                                       Integer articleTag1, Integer articleTag2,
                                       Integer articleTag3, String userName) {

        if ((articleContent != "" && articleContent.length() > 0) || (articleImage != "" && articleImage.length() > 0)
                || (articleTitle != "" && articleTitle.length() > 0) || (articleDesc != "" && articleDesc.length() > 0)) {
            //当前时间戳
//            String articleCreateTime = Long.toString(getCurrentTime());
            String articleCreateTime = Long.toString(TimeUtil.getCurrentTime());
            //点赞数(默认0)
            Integer articleLikeNum = 0;
            //浏览数(默认0)
            Integer articleViews = 0;
            //评论数(默认0)
            Integer countComment = 0;

            //根据分类id查询名字
            String categoryName = categoryDao.getCategoryById(categoryId, userName).getCategoryName();

            articleDao.addArticle(articleContent, articleLikeNum,
                    articleViews, articleImage,
                    articleTitle, articleCreateTime,
                    articleIsTop, categoryId,
                    articleDesc, countComment,
                    articleTag1, articleTag2,
                    articleTag3, categoryName,
                    userName);
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 博文标题查重
     *
     * @param articleTitle
     * @param userName
     * @return
     */
    @Override
    public List<Article> getArticleByTitle(String articleTitle, String userName) {
        return articleDao.getArticleByTitle(articleTitle, userName);
    }

    /**
     * 查询所有博文并分页
     *
     * @param pageNum
     * @param limit
     * @param userName
     * @return
     */
    @Override
    public Results<Article> getAllArticle(Integer pageNum, Integer limit, String userName) {

        List<Article> allArticle = articleDao.getAllArticle(pageNum, limit, userName);
        for (Article list : allArticle) {
//            list.setArticleCreateTime(getTimeFormat(list.getArticleCreateTime()));
            list.setArticleCreateTime(TimeUtil.getTimeFormat(list.getArticleCreateTime()));
        }

        return Results.success(articleDao.countAllArticle(userName).intValue(),
                allArticle);
    }

    /**
     * 根据id获取博文
     *
     * @param articleId
     * @param userName
     * @return
     */
    @Override
    public Article getArticleById(Integer articleId, String userName) {
        return articleDao.getArticleById(articleId, userName);
    }

    /**
     * 模糊查询并分页
     *
     * @param articleTitle
     * @param pageNum
     * @param limit
     * @param userName
     * @return
     */
    @Override
    public Results<Article> findAllArticleByTitle(String articleTitle, Integer pageNum, Integer limit,
                                                  String userName) {
        return Results.success(articleDao.countAllArticleByTitle(articleTitle, userName).intValue(),
                articleDao.getAllArticleByTitle(articleTitle, pageNum, limit, userName));
    }

    /**
     * 删除博文
     *
     * @param articleId
     * @param userName
     * @return
     */
    @Override
    public Results<Article> deleteArticleById(Integer articleId, String userName) {
        if (articleId != null) {
            articleDao.deleteArticleById(articleId, userName);
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @param userName
     * @return
     */
    @Override
    public Results<Article> deleteArticleByCheck(String ids, String userName) {
        //String通过","切割 转成集合
        List idList = Arrays.asList(ids.split(","));
        idList.forEach(id -> {
            //转成Integer
            Integer articleId = Integer.parseInt((String) id);
            //调用删除功能
            articleDao.deleteArticleById(articleId, userName);
        });
        return Results.success();
    }

    /**
     * 修改博文
     *
     * @param article
     * @return
     */
    @Override
    public Results<Article> updateArticle(Article article) {

//        article.setArticleCreateTime(Long.toString(TimeUtil.getCurrentTime()));

        if (articleDao.updateArticle(article) > 0) {
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 查询文章总数
     *
     * @param userName
     * @return
     */
    @Override
    public Long countAllArticle(String userName) {
        return articleDao.countAllArticle(userName);
    }

    /**
     * 根据分类id查询博文(删除分类时的检测)
     *
     * @param categoryId
     * @param userName
     * @return
     */
    @Override
    public List<Article> checkCategoryById(Integer categoryId, String userName) {
        return articleDao.checkCategoryById(categoryId, userName);
    }

    /**
     * 根据标签id查询博文(删除标签时的检测)
     *
     * @param tagId
     * @return
     */
    @Override
    public List<Article> checkTagById(Integer tagId) {
        return articleDao.checkTagById(tagId);
    }

    /**
     * 根据用户名查询博文(前台)
     *
     * @param userName
     * @return
     */
    @Override
    public List<Article> selectByUserName(String userName) {
        return articleDao.selectByUserName(userName);
    }

    /**
     * 查询博文相关数量信息(博文总数和评论总数)
     *
     * @param userName
     * @return
     */
    @Override
    public ArticleCount countArticleInfoByUserName(String userName) {
        return articleDao.countArticleInfoByUserName(userName);
    }

}
