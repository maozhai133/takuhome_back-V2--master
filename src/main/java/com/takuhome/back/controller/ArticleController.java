package com.takuhome.back.controller;

import com.takuhome.back.entity.Article;
import com.takuhome.back.result.Page;
import com.takuhome.back.result.ResponseCode;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IArticleService;
import com.takuhome.back.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


/**
 * 添加博文控制器
 *
 * @author nekotaku
 * @create 2021-09-23 15:35
 */
@Controller
@RequestMapping(value = "/Article")
public class ArticleController {

    @Autowired
    private IArticleService IArticleService;

    @Autowired
    private ICategoryService ICategoryService;

    /**
     * 添加博文
     *
     * @param articleContent
     * @param articleImage
     * @param articleTitle
     * @param articleIsTop
     * @param categoryId
     * @param articleDesc
     * @return
     */
    @PostMapping(value = "/addArticle")
    @ResponseBody
    public Results<Article> addArticle(String articleContent, String articleImage, String articleTitle
            , Integer articleIsTop, Integer categoryId, String articleDesc, Integer articleTag1
            , Integer articleTag2, Integer articleTag3,String userName) {
        System.out.println("调用添加博文");
        articleTitle = articleTitle.trim();

        //博文标题查重
        List<Article> article = IArticleService.getArticleByTitle(articleTitle,userName);
        if (article != null && article.size() > 0) {
            return Results.failure(ResponseCode.ARTICLE_TITLE_REPEAT.getCode()
                    , ResponseCode.ARTICLE_TITLE_REPEAT.getMessage());
        }
        // 博文内容转码
        articleContent = HtmlUtils.htmlEscape(articleContent);
        return IArticleService.addArticle(articleContent, articleImage, articleTitle, articleIsTop
                , categoryId, articleDesc, articleTag1, articleTag2, articleTag3,userName);
    }

    /**
     * 获取全部博文并分页
     *
     * @param page
     * @return
     */
    @GetMapping(value = "/articleList")
    @ResponseBody
    public Results<Article> articleList(Page page,String userName) {
        System.out.println("调用获取全部博文并分页");
        page.countOffset();
        return IArticleService.getAllArticle(page.getOffset(), page.getLimit(),userName);
    }


    /**
     * 打开博文编辑
     *
     * @param model
     * @param article
     * @return
     */
    @GetMapping(value = "/edit")
    public String editArticle(Model model, Article article,String userName) {

        System.out.println("调用打开博文编辑");
        Article articleById = IArticleService.getArticleById(article.getArticleId(), userName);
        // 博文内容解码
        articleById.setArticleContent(HtmlUtils.htmlUnescape(articleById.getArticleContent()));
        model.addAttribute(articleById);

        //获取当前分类id
        Integer categoryId = IArticleService.getArticleById(article.getArticleId(),userName).getCategoryId();
        //根据id查找当前的父类分类id
        Integer parentId = ICategoryService.getCategoryById(categoryId,userName).getParentId();
        //根据父类id查找父类分类
        String parentName = ICategoryService.getCategoryById(parentId,userName).getCategoryName();

        model.addAttribute("parentId", parentId);
        model.addAttribute("parentName", parentName);

        return "Article/editArticle";
    }

    /**
     * 保存博文内容修改
     *
     * @param article
     * @return
     */
    @PostMapping(value = "edit")
    @ResponseBody
    public Results<Article> updateArticle(Article article,String userName) {
        System.out.println("调用保存博文内容修改");
        //标题查重
        List<Article> articleByTitle = IArticleService.getArticleByTitle(article.getArticleTitle(),userName);
        if(articleByTitle !=null && articleByTitle.size()>0){
            for(Article article1:articleByTitle){
                //如果相等,跳出当前判断
                if(article.getArticleId()==article1.getArticleId()){
                    continue;
                }
                if(article1.getArticleTitle().toLowerCase().equals(
                        article.getArticleTitle().toLowerCase()
                )){
                    return Results.failure(ResponseCode.ARTICLE_TITLE_REPEAT.getCode(),
                            ResponseCode.ARTICLE_TITLE_REPEAT.getMessage());
                }
            }
        }
        //获取分类名
        article.setCategoryName(ICategoryService.getCategoryById(article.getCategoryId(),userName).getCategoryName());
        // 博文内容转码
        article.setArticleContent(HtmlUtils.htmlEscape(article.getArticleContent()));
        return IArticleService.updateArticle(article);
    }

    /**
     * 博文模糊查询并分页
     *
     * @param page
     * @param articleTitle
     * @return
     */
    @GetMapping(value = "/findAllArticleByTitle")
    @ResponseBody
    public Results<Article> findAllArticleByTitle(Page page, String articleTitle,String userName) {
        System.out.println("调用博文模糊查询并分页");
        page.countOffset();
        return IArticleService.findAllArticleByTitle(articleTitle, page.getOffset()
                , page.getLimit(),userName);
    }

    /**
     * 删除博文
     *
     * @param articleId
     * @return
     */
    @DeleteMapping(value = "/delArticleById")
    @ResponseBody
    public Results<Article> delArticleById(Integer articleId,String userName) {
        System.out.println("调用删除博文");
        return IArticleService.deleteArticleById(articleId,userName);
    }

    /**
     * 批量删除博文
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/delArticleByCheck")
    @ResponseBody
    public Results<Article> deleteArticleByCheck(@RequestParam String ids,String userName) {
        System.out.println("批量删除博文");
        if (ids != null && ids != "") {
            return IArticleService.deleteArticleByCheck(ids,userName);
        }
        return Results.failure();
    }


}
