package com.takuhome.back.service;

import com.takuhome.back.entity.Category;
import com.takuhome.back.result.Results;

import java.util.List;

/**
 * 分类管理服务层
 *
 * @author nekotaku
 * @create 2021-08-07 20:44
 */
public interface ICategoryService {

    //1.增加分类
    Results<Category> insertCategory(Integer parentId, String categoryName,
                                     String parentName, String userName);

    //2.查询分类是否重复命名
    List<Category> getCategoryByName(String categoryName, String userName);

    //3.查询所有分类并分页
    Results<Category> getAllCategory(String userName, Integer pageNum, Integer limit);

    //4.查询所有分类
    Results<Category> getCategory(String userName);

    //5.根据父类ID查询父类名
    String findParentById(Integer categoryId, String userName);

    //6.修改分类信息
    Results<Category> updateCategory(Category category);

    //7.根据id查询分类
    Category getCategoryById(Integer categoryId, String userName);

    //8.根据id删除分类
    Results<Category> delByCategoryId(Integer categoryId, String userName);

    //9.模糊查询并分页
    Results<Category> findAllCategoryNamePage(String categoryName, String userName,
                                              Integer pageNum, Integer limit);

    //10.批量删除分类
    Results deleteCategoryByCheck(String ids, String userName);

    //11.根据父类ID查找子分类
    Results<Category> findCategoryByParentId(Integer parentId, String userName);

    //11.根据id查找当前分类下的子分类
    List findCategoryByParentIdForCheck(Integer parentId, String userName);
}
