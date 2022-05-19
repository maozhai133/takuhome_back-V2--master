package com.takuhome.back.dao;

import com.takuhome.back.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标签dao层
 *
 * @author nekotaku
 * @create 2021-09-10 16:41
 */
@Mapper
@Repository
public interface TagDao {

    //1.添加标签
    Integer addTag(@Param("tagName") String tagName,@Param("userName")String userName);

    //2.标签查重
    List<Tag> getTagByName(@Param("tagName") String tagName,@Param("userName")String userName);

    //3.获取所有的标签并分页
    List<Tag> getAllTag(@Param("pageNum") Integer pageNum, @Param("limit") Integer limit,
                        @Param("userName")String userName);

    Long countAllTag(@Param("userName")String userName);

    //4.根据Id查询标签
    Tag getTagById(@Param("tagId") Integer tagId,@Param("userName")String userName);

    //5.修改标签
    int updateTag(Tag tag);

    //6.删除标签
    int delByTagId(@Param("tagId") Integer tagId,@Param("userName")String userName);

    //7.模糊查询并分页
    List<Tag> getAllTagByNamePage(@Param("tagName") String tagName,
                                  @Param("pageNum") Integer pageNum,
                                  @Param("limit") Integer limit,
                                  @Param("userName")String userName);

    Long getTagByNamePage(String tagName,@Param("userName")String userName);

    //8.获取所有标签(绑定下拉列表)
    List<Tag> getTag(@Param("userName")String userName);
}
