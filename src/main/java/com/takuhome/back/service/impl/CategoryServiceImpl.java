package com.takuhome.back.service.impl;

import com.takuhome.back.dao.CategoryDao;
import com.takuhome.back.entity.Category;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 分类管理服务实现层
 *
 * @author nekotaku
 * @create 2021-08-07 20:44
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryDao categoryDao;


    /**
     * 添加分类
     *
     * @param parentId
     * @param categoryName
     * @param parentName
     * @param userName
     * @return
     */
    @Override
    public Results<Category> insertCategory(Integer parentId, String categoryName,
                                            String parentName, String userName) {
        if (categoryName != null && categoryName != "") {
            categoryDao.insertCategory(parentId, categoryName, parentName, userName);
            return Results.success();
        } else {
            return Results.failure();
        }

    }

    /**
     * 验证分类名是否重复
     *
     * @param categoryName
     * @param userName
     * @return
     */
    @Override
    public List<Category> getCategoryByName(String categoryName, String userName) {
        return categoryDao.getCategoryByName(categoryName, userName);
    }

    /**
     * 查询所有分类并分页
     *
     * @param userName
     * @param pageNum
     * @param limit
     * @return
     */
    @Override
    public Results<Category> getAllCategory(String userName, Integer pageNum, Integer limit) {
        return Results.success(categoryDao.countAllCategory(userName).intValue(),
                categoryDao.getAllCategory(pageNum, limit, userName));
    }

    /**
     * 查询分类绑定下拉列表
     *
     * @param userName
     * @return
     */
    @Override
    public Results<Category> getCategory(String userName) {
//        List<Category> category = categoryDao.getCategory();
//        System.out.println("测试"+category);
//        JSONArray array= JSONArray.parseArray(JSON.toJSONString(categoryDao.getCategory()));
        return Results.success(categoryDao.getCategory(userName));
    }

    /**
     * 根据父类ID查询父类名
     *
     * @param categoryId
     * @param userName
     * @return
     */
    @Override
    public String findParentById(Integer categoryId, String userName) {
        return categoryDao.findParentById(categoryId, userName);
    }

    /**
     * 修改分类信息
     *
     * @param category
     * @return
     */
    @Override
    public Results<Category> updateCategory(Category category) {
        if (categoryDao.updateCategory(category) > 0) {
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 根据分类ID查询分类
     *
     * @param categoryId
     * @param userName
     * @return
     */
    @Override
    public Category getCategoryById(Integer categoryId, String userName) {
        return categoryDao.getCategoryById(categoryId, userName);
    }


    /**
     * 根据ID删除分类
     *
     * @param categoryId
     * @param userName
     * @return
     */
    @Override
    public Results<Category> delByCategoryId(Integer categoryId, String userName) {
        if (categoryId != null) {
            categoryDao.delByCategoryId(categoryId, userName);
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 模糊查询并分类
     *
     * @param categoryName
     * @param userName
     * @param pageNum
     * @param limit
     * @return
     */
    @Override
    public Results<Category> findAllCategoryNamePage(String categoryName, String userName,
                                                     Integer pageNum, Integer limit) {
        return Results.success(categoryDao.getCategoryByName2(categoryName, userName).intValue(),
                categoryDao.getAllCategoryByNamePage(categoryName, pageNum, limit, userName));
    }

    /**
     * 批量删除选中的分类
     *
     * @param ids
     * @param userName
     * @return
     */
    @Override
    public Results deleteCategoryByCheck(String ids, String userName) {
        //String通过","切割 转成集合
        List idList = Arrays.asList(ids.split(","));
        idList.forEach(id -> {
            //转成Integer
            Integer categoryId = Integer.parseInt((String) id);
            //调用删除功能
            categoryDao.delByCategoryId(categoryId, userName);
        });
        return Results.success();
    }

    /**
     * 根据父类ID查找子分类
     *
     * @param parentId
     * @param userName
     * @return
     */
    @Override
    public Results<Category> findCategoryByParentId(Integer parentId, String userName) {
        return Results.success(categoryDao.findCategoryByParentId(parentId, userName));
    }

    /**
     * 检查当前父类分类是否含有子集分类
     * @param parentId
     * @param userName
     * @return
     */
    @Override
    public List findCategoryByParentIdForCheck(Integer parentId, String userName) {
        return categoryDao.findCategoryByParentId(parentId,userName);
    }


}
