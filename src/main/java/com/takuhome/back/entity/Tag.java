package com.takuhome.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 标签实体类
 * @author nekotaku
 * @create 2021-09-10 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Serializable {

    private static final long serialVersionUID = -3170236103932007534L;
    private Integer tagId; //标签id
    private String tagName; //标签名
    private String userName;//用户名

}
