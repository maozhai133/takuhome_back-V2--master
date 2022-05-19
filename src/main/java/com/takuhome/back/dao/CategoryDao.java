package com.takuhome.back.dao;

import com.takuhome.back.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类dao层
 *
 * @author nekotaku
 * @create 2021-08-07 20:28
 */

@Mapper
@Repository
public interface CategoryDao {

    //1.增加分类
    Integer insertCategory(@Param("parentId") Integer parentId, @Param("categoryName") String categoryName,
                           @Param("parentName") String parentName,@Param("userName") String userName);

    //2.查询分类是否重复命名
    List<Category> getCategoryByName(@Param("categoryName") String categoryName,
                                     @Param("userName") String userName);

    //3.获取所有的分类并分页
    List<Category> getAllCategory(@Param("pageNum") Integer pageNum, @Param("limit") Integer limit,
                                  @Param("userName") String userName);

    Long countAllCategory(@Param("userName") String userName);

    //4.查询所有分类
    List<Category> getCategory(@Param("userName") String userName);

    //5.根据父类ID查询父类名
    String findParentById(@Param("categoryId") Integer categoryId,
                          @Param("userName") String userName);

    //6.修改分类
    int updateCategory(Category category);

    //7.根据ID查询分类
    Category getCategoryById(@Param("categoryId") Integer categoryId,
                             @Param("userName") String userName);

    //8.删除分类
    int delByCategoryId(@Param("categoryId") Integer categoryId,
                        @Param("userName") String userName);

    //9.模糊查询并分页
    List<Category> getAllCategoryByNamePage(@Param("categoryName") String categoryName,
                                            @Param("pageNum") Integer pageNum,
                                            @Param("limit") Integer limit,
                                            @Param("userName") String userName);

    Long getCategoryByName2(String categoryName,
                            @Param("userName") String userName);

    //10.根据父类ID查找子分类
    List<Category> findCategoryByParentId(@Param("parentId") Integer parentId,
                                          @Param("userName") String userName);
}
