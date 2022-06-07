package com.takuhome.back.controller;

import com.takuhome.back.entity.Article;
import com.takuhome.back.entity.Category;
import com.takuhome.back.result.Page;
import com.takuhome.back.result.ResponseCode;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IArticleService;
import com.takuhome.back.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 分类控制器
 *
 * @author nekotaku
 * @create 2021-11-07 21:21
 */
@Controller
@RequestMapping(value = "/Category")
public class CategoryController {

    @Autowired
    private ICategoryService ICategoryService;

    @Autowired
    private IArticleService IArticleService;

    /**
     * 添加分类
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/insertCategory")
    @ResponseBody
    public Results<Category> addCategory(HttpServletRequest request) {
        System.out.println("调用添加分类");

//    public Results<Category> addCategory(Category category) {
        String category_name = request.getParameter("category_name");//获取输入的类别名
        Integer parent_id = Integer.parseInt(request.getParameter("parent_id"));//获取父类ID
        String userName = request.getParameter("userName");//获取用户名


//        System.out.println("类别名为："+category_name+",父类ID为："+parent_id);

        //查重验证
//        List<Category> categories = categoryService.getCategoryByName(category.getCategoryName(),category.getUserName());
        List<Category> categories = ICategoryService.getCategoryByName(category_name, userName);
        if (categories != null && categories.size() > 0) {
            return Results.failure(ResponseCode.CATEGORY_NAME_REPEAT.getCode(),
                    ResponseCode.CATEGORY_NAME_REPEAT.getMessage());
        }

        //父类名
        String parentName = "";


        //查找父类名
        if (parent_id == 0) {
            parentName = "无";
        } else {
            parentName = ICategoryService.findParentById(parent_id, userName);
//            category.setParentName(categoryService.findParentById(category.getParentId(),category.getUserName()));
        }

        //添加分类
        return ICategoryService.insertCategory(parent_id, category_name, parentName, userName);
    }

    /**
     * 获取全部分类，并分页
     *
     * @param page
     * @return
     */
    @GetMapping(value = "/CategoryList")
    @ResponseBody
    public Results<Category> list(Page page, String userName) {
        System.out.println("调用获取全部分类，并分页");

        page.countOffset();
        return ICategoryService.getAllCategory(userName, page.getOffset(), page.getLimit());
    }

    /**
     * 获取分类，绑定下拉列表用
     *
     * @return
     */
    @PostMapping(value = "/getList")
    @ResponseBody
    public Results<Category> getList(String userName) {
        System.out.println("调用获取分类，绑定下拉列表用");

        return ICategoryService.getCategory(userName);
    }

    /**
     * 打开分类编辑
     *
     * @param model
     * @param category
     * @return
     */
    @GetMapping(value = "/edit")
    public String editCategory(Model model, Category category) {
        System.out.println("调用打开分类编辑");
        model.addAttribute(ICategoryService.getCategoryById(category.getCategoryId(), category.getUserName()));
        return "Category/editCategory";
    }

    /**
     * 修改分类并保存
     *
     * @param request
     * @return
     */
    @PostMapping(value = "edit")
    @ResponseBody
    public Results edit(HttpServletRequest request) {
        System.out.println("调用修改分类并保存");

        //获取更改的值
        Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));//分类ID
        String categoryName = request.getParameter("categoryName");//分类名
        Integer parentId = Integer.parseInt(request.getParameter("parentId"));//父类ID
        String userName = request.getParameter("userName");//用户名
        String parentName = "";//父类名

        List categoryByParentIdForCheck = ICategoryService.findCategoryByParentIdForCheck(categoryId, userName);
        if (categoryByParentIdForCheck.size() > 0 && categoryByParentIdForCheck != null) {
            return Results.failure(ResponseCode.CATEGORY_ARTICLE_TOP.getCode(),
                    ResponseCode.CATEGORY_ARTICLE_TOP.getMessage());
        }

        if (parentId == 0) {
            parentName = "无";
        } else {
            parentName = ICategoryService.findParentById(parentId, userName);
        }
        Category category = new Category(categoryId, parentId, categoryName, parentName, userName);

        //查重验证
        List<Category> categories = ICategoryService.getCategoryByName(categoryName, userName);
        if (categories != null && categories.size() > 0) {
            for (Category category1 : categories) {
                if (category1.getCategoryId() == categoryId) {
                    continue;
                }
                if (category1.getCategoryName().toLowerCase().equals(categoryName.toLowerCase())) {
                    return Results.failure(ResponseCode.CATEGORY_NAME_REPEAT.getCode(),
                            ResponseCode.CATEGORY_NAME_REPEAT.getMessage());
                }
            }
        }
        return ICategoryService.updateCategory(category);
    }

    /**
     * 删除分类
     *
     * @param request
     * @return
     */
    @DeleteMapping(value = "/delCategory")
    @ResponseBody
    public Results delCategory(HttpServletRequest request) {
        System.out.println("调用删除分类");

        Integer categoryId = Integer.parseInt(request.getParameter("categoryId"));//获取分类ID
        String userName = request.getParameter("userName");//用户名
        //查询是否为父级分类(父级分类需清除或改变子级分类才可以删除)
        List categoryByParentIdForCheck = ICategoryService.findCategoryByParentIdForCheck(categoryId, userName);
        if (categoryByParentIdForCheck.size() > 0 && categoryByParentIdForCheck != null) {
            return Results.failure(ResponseCode.CATEGORY_PARENT_ZERO.getCode(),
                    ResponseCode.CATEGORY_PARENT_ZERO.getMessage());
        }
        //查询是否在博文中含有该分类(如果有就需要先清除博文)
        List<Article> articles = IArticleService.checkCategoryById(categoryId, userName);
        if (articles.size() > 0 && articles != null) {
            return Results.failure(ResponseCode.CATEGORY_ARTICLE_CONTAIN.getCode(),
                    ResponseCode.CATEGORY_ARTICLE_CONTAIN.getMessage());
        }

        return ICategoryService.delByCategoryId(categoryId, userName);
    }

    /**
     * 分类模糊插叙并分页
     *
     * @param page
     * @param categoryName
     * @return
     */
    @GetMapping(value = "/findAllByCategoryNamePage")
    @ResponseBody
    public Results<Category> findAllByCategoryNamePage(Page page, String categoryName, String userName) {
        System.out.println("调用分类模糊插叙并分页");
        page.countOffset();
        return ICategoryService.findAllCategoryNamePage(categoryName, userName, page.getOffset(), page.getLimit());
    }

    /**
     * 批量删除选中的分类
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/deleteCategoryByCheck")
    @ResponseBody
    public Results deleteCategoryByIds(@RequestParam String ids, String userName) {
        System.out.println("调用批量删除选中的分类");

        //查询是否为父级分类(父级分类需清除或改变子级分类才可以删除)
        //分割获取分类ID存为集合
        boolean isflagByParents = true;
        boolean isflagByArticle = true;
        List idList = Arrays.asList(ids.split(","));
        for (Object str : idList) {
            Integer categoryId = Integer.parseInt((String) str);
            List categoryByParentIdForCheck = ICategoryService.findCategoryByParentIdForCheck(categoryId, userName);
            if (categoryByParentIdForCheck.size() > 0 && categoryByParentIdForCheck != null) {
                isflagByParents = false;
                break;
            }

            //查询是否在博文中含有该分类(如果有就需要先清除博文)
            List<Article> articles = IArticleService.checkCategoryById(categoryId, userName);
            if (articles.size() > 0 && articles != null) {
                isflagByArticle = false;
                break;
            }
        }
        if (!isflagByParents) {
            return Results.failure(ResponseCode.CATEGORY_PARENT_ZERO.getCode(),
                    ResponseCode.CATEGORY_PARENT_ZERO.getMessage());
        }
        if (!isflagByArticle) {
            return Results.failure(ResponseCode.CATEGORY_ARTICLE_CONTAIN.getCode(),
                    ResponseCode.CATEGORY_ARTICLE_CONTAIN.getMessage());
        }
        return ICategoryService.deleteCategoryByCheck(ids, userName);
    }

    /**
     * 根据父分类ID查找子分类(实现二级联动绑定)
     *
     * @param parentId
     * @return
     */
    @PostMapping(value = "/getCategoryByParentId")
    @ResponseBody
    public Results<Category> getCategoryByParentId(Integer parentId, String userName) {
        System.out.println("调用根据父分类ID查找子分类(实现二级联动绑定)");
        return ICategoryService.findCategoryByParentId(parentId, userName);
    }


}
