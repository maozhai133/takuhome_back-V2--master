package com.takuhome.back.service.impl;

import com.takuhome.back.dao.TagDao;
import com.takuhome.back.entity.Tag;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 标签管理服务实现层
 *
 * @author nekotaku
 * @create 2021-09-11 16:42
 */
@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private TagDao tagDao;

    /**
     * 添加标签
     *
     * @param tagName
     * @param userName
     * @return
     */
    @Override
    public Results<Tag> insertTag(String tagName, String userName) {
        if (tagName != null && tagName != "") {
            tagDao.addTag(tagName, userName);
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 标签查重
     *
     * @param tagName
     * @param userName
     * @return
     */
    @Override
    public List<Tag> getTagByName(String tagName, String userName) {
        return tagDao.getTagByName(tagName, userName);
    }

    /**
     * 查看所有标签并分页
     *
     * @param pageNum
     * @param limit
     * @param userName
     * @return
     */
    @Override
    public Results<Tag> getAllTag(Integer pageNum, Integer limit, String userName) {
        return Results.success(tagDao.countAllTag(userName).intValue(),
                tagDao.getAllTag(pageNum, limit, userName));
    }

    /**
     * 根据Id查询标签
     *
     * @param tagId
     * @param userName
     * @return
     */
    @Override
    public Tag getTagById(Integer tagId, String userName) {
        return tagDao.getTagById(tagId, userName);
    }

    /**
     * 修改标签
     *
     * @param tag
     * @return
     */
    @Override
    public Results<Tag> updateTag(Tag tag) {
        if (tagDao.updateTag(tag) > 0) {
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 根据Id删除标签
     *
     * @param tagId
     * @param userName
     * @return
     */
    @Override
    public Results<Tag> delByTagId(Integer tagId, String userName) {
        if (tagId != null) {
            tagDao.delByTagId(tagId, userName);
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 批量删除选中的标签
     *
     * @param ids
     * @param userName
     * @return
     */
    @Override
    public Results delTagByCheck(String ids, String userName) {
        //String通过","切割 转成集合
        List<String> idList = Arrays.asList(ids.split(","));
        idList.forEach(id -> {
            //转成Integer
            int tagId = Integer.parseInt((String) id);
            delByTagId(tagId, userName);
        });
        return Results.success();
    }

    /**
     * 模糊查询并分页
     *
     * @param tagName
     * @param pageNum
     * @param limit
     * @param userName
     * @return
     */
    @Override
    public Results<Tag> findAllTagByNamePage(String tagName, Integer pageNum,
                                             Integer limit, String userName) {
        return Results.success(tagDao.getTagByNamePage(tagName, userName).intValue(),
                tagDao.getAllTagByNamePage(tagName, pageNum, limit, userName));
    }

    /**
     * 获取所有标签(绑定下拉列表)
     *
     * @param userName
     * @return
     */
    @Override
    public Results<Tag> getTag(String userName) {
        return Results.success(tagDao.getTag(userName));
    }
}
