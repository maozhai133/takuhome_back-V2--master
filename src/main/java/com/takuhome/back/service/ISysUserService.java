package com.takuhome.back.service;

import com.takuhome.back.entity.SysUser;
import com.takuhome.back.entity.SysUserFront;
import com.takuhome.back.result.Results;

import java.util.List;

/**
 * 用户管理服务层
 *
 * @Title:SysUserService
 * @Author:NekoTaku
 * @Date:2021/11/17 14:37
 * @Version:1.0
 */
public interface ISysUserService {

    //1.登录验证
    SysUser getUserByUserName(String userName);

    //2.查重
    List<SysUser> getUserByRepeat(String userName,String userEmail);

    //3.注册新用户
    Results<SysUser> insertUser(SysUser sysUser);

    //4.修改用户基本信息
    Boolean updateUserInfo(SysUser sysUser);

    //5.用户昵称查重
    List<SysUser> getUserByNickName(String nickName);

    //6.修改密码时验证信息
    SysUser updateUserByCheck(String userName);

    //7.查询所有用户并分页(管理员用)
    Results<SysUser> getAllUsers(Integer pageNum, Integer limit);

    //8.查询用户权限值
    Integer getUserStatus(String userName);

    //9.根据用户id查询用户
    SysUser getUserById(Integer admId);

    //10.根据用户邮箱查询用户
    SysUser getUserByEmail(String userEmail);

    //11.根据用户名查询用户(前台信息用)
    SysUserFront getUserInfoForFront(String userName);

}
