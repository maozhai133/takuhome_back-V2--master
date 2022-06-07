package com.takuhome.back.controller;

import com.takuhome.back.entity.*;
import com.takuhome.back.service.*;
import com.takuhome.back.util.time.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 前台控制器
 *
 * @Title:FrontController
 * @Author:NekoTaku
 * @Date:2022/05/23 9:49
 * @Version:1.0
 */
@Controller
@RequestMapping(value = "/Front")
public class FrontController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ITagService tagService;


    /**
     * 跳转到前台页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/goFront")
    public String goIntoFront(HttpServletRequest request, Model model) {
        System.out.println("跳转前台页面");
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        //映射新的前台所需要的实体层
        SysUserFront userInfoForFront = sysUserService.getUserInfoForFront(user.getUserName());
        //统计用户博文数量和评论数量
        ArticleCount articleCount = articleService.countArticleInfoByUserName(user.getUserName());
        userInfoForFront.setArticleCount(articleCount);
        //统计用户标签数量
        userInfoForFront.setCountTags(tagService.countTagByUser(user.getUserName()));
        //查询用户所有标签
        userInfoForFront.setTags(tagService.getTagsByUser(user.getUserName()));


        //根据用户名查询博文
        List<Article> articles = articleService.selectByUserName(user.getUserName());
        for (Article article : articles) {
            article.setArticleCreateTime(TimeUtil.getTimeFormat(article.getArticleCreateTime()));
        }


        model.addAttribute("userFront", userInfoForFront);
        model.addAttribute("articles", articles);

        return "/Front/frontIndex";
    }

    /**
     * 跳回后台页面
     *
     * @param
     * @return
     */
    @RequestMapping("/comeback")
    public String goBack() {
        System.out.println("跳回后台管理");
        return "redirect:/index";
    }

    /**
     * 阅读详细博文
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/readDetail")
    public String readDetail(HttpServletRequest request, Model model) {

        // 获取用户名
        String userName = ((SysUser) request.getSession().getAttribute("user")).getUserName();
        // 获取博文id
        Integer articleId = Integer.parseInt(request.getParameter("articleId"));
        System.out.println("获取博文id：" + articleId);
        // 根据用户名和博文id查询博文
        Article articleById = articleService.getArticleById(articleId, userName);

        //根据博文id查询当前博文的评论数量
        Long commentNumber = commentService.countCommentNumber(articleId);

        //根据博文id查询当前博文得评论
        List<Comment> comments = commentService.selectCommentById(articleId);

        //设置时间格式
        articleById.setArticleCreateTime(TimeUtil.getTimeFormat(articleById.getArticleCreateTime()));

        // 博文内容解码
        articleById.setArticleContent(HtmlUtils.htmlUnescape(articleById.getArticleContent()));

        //映射新的前台所需要的实体层
        SysUserFront userInfoForFront = sysUserService.getUserInfoForFront(userName);
        //统计用户博文数量和评论数量
        ArticleCount articleCount = articleService.countArticleInfoByUserName(userName);
        userInfoForFront.setArticleCount(articleCount);
        //查询用户所有标签
        userInfoForFront.setCountTags(tagService.countTagByUser(userName));

        //设置当前博文评论总数
        userInfoForFront.setCountComments(commentNumber);

        //为博文绑定对应的标签
        List<String> tagsList = new ArrayList<>();
        Integer articleTag1 = articleById.getArticleTag1();
        Integer articleTag2 = articleById.getArticleTag2();
        Integer articleTag3 = articleById.getArticleTag3();
        int[] tags = new int[]{articleTag1, articleTag2, articleTag3};
        for (int i = 0; i < tags.length; i++) {
            if (tags[i] == 0) {
                continue;
            }
            tagsList.add(tagService.getTagById(tags[i], userName).getTagName());
        }

        model.addAttribute("article", articleById);
        model.addAttribute("userFront", userInfoForFront);
        model.addAttribute("comments", comments);
        model.addAttribute("tags", tagsList);

        return "/Front/frontContents";
    }

}
