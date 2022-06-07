package com.takuhome.back.controller;

import com.takuhome.back.entity.Article;
import com.takuhome.back.entity.Tag;
import com.takuhome.back.result.Page;
import com.takuhome.back.result.ResponseCode;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IArticleService;
import com.takuhome.back.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 标签控制层
 *
 * @author nekotaku
 * @create 2021-09-11 16:45
 */

@Controller
@RequestMapping(value = "/Tag")
public class TagController {

    @Autowired
    private ITagService ITagService;

    @Autowired
    private IArticleService IArticleService;

    /**
     * 添加标签
     *
     * @param tagName
     * @param userName
     * @return
     */
    @PostMapping(value = "/insertTag")
    @ResponseBody
    public Results<Tag> insertTag(String tagName, String userName) {
        System.out.println("调用添加标签");
        //查重验证
        List<Tag> tagByName = ITagService.getTagByName(tagName, userName);
        if (tagByName != null && tagByName.size() > 0) {
            return Results.failure(ResponseCode.TAG_NAME_REPEAT.getCode(),
                    ResponseCode.TAG_NAME_REPEAT.getMessage());
        }

        return ITagService.insertTag(tagName, userName);
    }

    /**
     * 获取全部标签，并分页
     *
     * @param page
     * @param userName
     * @return
     */
    @GetMapping(value = "/tagList")
    @ResponseBody
    public Results<Tag> list(Page page, String userName) {
        System.out.println("调用获取全部标签，并分页");
        page.countOffset();
        return ITagService.getAllTag(page.getOffset(), page.getLimit(), userName);
    }

    /**
     * 打开编辑标签
     *
     * @param model
     * @param tag
     * @return
     */
    @GetMapping(value = "/edit")
    public String editTag(Model model, Tag tag) {
        System.out.println("调用打开编辑标签");
        model.addAttribute(ITagService.getTagById(tag.getTagId(), tag.getUserName()));
        return "Tag/editTag";
    }

    /**
     * 保存修改标签
     *
     * @param tag
     * @return
     */
    @PostMapping(value = "edit")
    @ResponseBody
    public Results<Tag> edit(Tag tag) {
        System.out.println("调用保存修改标签");
        //查重验证
        List<Tag> tagByName = ITagService.getTagByName(tag.getTagName(), tag.getUserName());
        if (tagByName != null && tagByName.size() > 0) {
            for (Tag tag1 : tagByName) {
                //如果相等，跳出当前判断
                if (tag1.getTagId() == tag.getTagId()) {
                    continue;
                }
                //判断标签名是否已存在
                if (tag1.getTagName().toLowerCase().equals(tag.getTagName().toLowerCase())) {
                    return Results.failure(ResponseCode.TAG_NAME_REPEAT.getCode(),
                            ResponseCode.TAG_NAME_REPEAT.getMessage());
                }
            }
        }
        return ITagService.updateTag(tag);
    }

    /**
     * 删除标签
     *
     * @param tagId
     * @return
     */
    @DeleteMapping(value = "/delTag")
    @ResponseBody
    public Results<Tag> delTag(Integer tagId, String userName) {
        System.out.println("调用删除标签");

        List<Article> articles = IArticleService.checkTagById(tagId);
        if (articles.size() > 0 && articles != null) {
            return Results.failure(ResponseCode.TAG_ARTICLE_CONTAIN.getCode(),
                    ResponseCode.TAG_ARTICLE_CONTAIN.getMessage());
        }
        return ITagService.delByTagId(tagId, userName);
    }

    /**
     * 批量删除标签
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/delTagByCheck")
    @ResponseBody
    public Results delTagByCheck(String ids, String userName) {
        System.out.println("调用批量删除标签");

        //检测当前标签是否在博文中存在
        List idList = Arrays.asList(ids.split(","));
        for (Object str : idList) {
            int tagId = Integer.parseInt((String) str);
            List<Article> articles = IArticleService.checkTagById(tagId);
            if (articles.size() > 0 && articles != null) {
                return Results.failure(ResponseCode.TAG_ARTICLE_CONTAIN.getCode(),
                        ResponseCode.TAG_ARTICLE_CONTAIN.getMessage());
            }
        }

        if (ids != null && ids != "") {
            return ITagService.delTagByCheck(ids, userName);
        }
        return Results.failure();
    }

    /**
     * 标签模糊插叙并分页
     *
     * @param page
     * @param tagName
     * @return
     */
    @GetMapping(value = "/findAllByTagNamePage")
    @ResponseBody
    public Results<Tag> findAllByTagNamePage(Page page, String tagName, String userName) {
        System.out.println("调用标签模糊插叙并分页");
        page.countOffset();
        return ITagService.findAllTagByNamePage(tagName, page.getOffset(), page.getLimit(), userName);
    }

    /**
     * 获取所有标签(绑定下拉列表)
     *
     * @return
     */
    @PostMapping(value = "/getTagList")
    @ResponseBody
    public Results<Tag> getTagList(String userName) {
        System.out.println("调用获取所有标签(绑定下拉列表)");
        return ITagService.getTags(userName);
    }
}
