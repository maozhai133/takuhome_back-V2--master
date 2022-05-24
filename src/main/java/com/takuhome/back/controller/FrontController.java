package com.takuhome.back.controller;

import com.takuhome.back.entity.ArticleCount;
import com.takuhome.back.entity.SysUser;
import com.takuhome.back.entity.SysUserFront;
import com.takuhome.back.service.IArticleService;
import com.takuhome.back.service.ISysUserService;
import com.takuhome.back.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
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

        model.addAttribute("userFront", userInfoForFront);

        return "/Front/frontIndex";
    }

    /**
     * 跳回后台页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/comeback")
    public String goBack() {
        System.out.println("跳回后台管理");
        return "redirect:/index";
    }
}
