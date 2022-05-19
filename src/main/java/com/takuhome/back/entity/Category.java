package com.takuhome.back.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 分类实体类
 * @author nekotaku
 * @create 2021-08-07 20:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable{


    private static final long serialVersionUID = -3337076773874843209L;
    private Integer categoryId; //分类ID
    private Integer parentId; //父类分类ID
    private String categoryName; //分类名
    private String parentName; //父类分类名
    private String userName;//用户名


//    @Override
//    public String toString() {
//        return "Category{" +
//                "category_id:" + categoryId +
//                ", parent_id:" + parentId +
//                ", category_name:'" + categoryName + '\'' +
//                '}';
//    }
}
