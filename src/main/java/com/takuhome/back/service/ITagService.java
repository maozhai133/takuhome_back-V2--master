package com.takuhome.back.service;

import com.takuhome.back.entity.Tag;
import com.takuhome.back.result.Results;

import java.util.List;

/**
 * 标签管理服务层
 *
 * @author nekotaku
 * @create 2021-09-11 16:41
 */
public interface ITagService {

    //1.添加标签
    Results<Tag> insertTag(String tagName,String userName);

    //2.标签查重
    List<Tag> getTagByName(String tagName,String userName);

    //3.查询所有标签并分页
    Results<Tag> getAllTag(Integer pageNum, Integer limit,String userName);

    //4.根据ID查询标签
    Tag getTagById(Integer tagId,String userName);

    //5.修改标签
    Results<Tag> updateTag(Tag tag);

    //6.根据Id删除标签
    Results<Tag> delByTagId(Integer tagId,String userName);

    //7.批量删除标签
    Results delTagByCheck(String ids,String userName);

    //8.模糊查询并分页
    Results<Tag> findAllTagByNamePage(String tagName,Integer pageNum, Integer limit,
                                      String userName);

    //9.获取所有标签(绑定用)
    Results<Tag> getTags(String userName);

    //10.统计每个用户的标签数量
    Long countTagByUser(String userName);

    //11.查询用户所有标签
    List<Tag> getTagsByUser(String userName);
}
