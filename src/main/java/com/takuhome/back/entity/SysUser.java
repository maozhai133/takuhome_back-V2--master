package com.takuhome.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后台管理用户实体类
 *
 * @Title:SysUser
 * @Author:NekoTaku
 * @Date:2021/11/17 11:00
 * @Version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser implements Serializable {

    private static final long serialVersionUID = 259516155028755852L;

    private String admId; //用户id(唯一id 自增)
    private String userName; //用户名(登录用)
    private String passWord; //用户密码
    private String nickName; //用户昵称
    private String headImg; //用户头像Url
    private String userEmail; //用户邮箱
    private String createTime; //账号创建时间
    private String updateTime; //账号信息更新时间
    private Integer userStatus; //账号状态(启用才可用)
    private String userDesc;//个人简介

}
