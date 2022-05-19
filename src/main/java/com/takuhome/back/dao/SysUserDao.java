package com.takuhome.back.dao;

import com.takuhome.back.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户dao层
 *
 * @author nekotaku
 * @create 2022-03-19 20:28
 */
@Mapper
@Repository
public interface SysUserDao {

    //1.登录验证
    SysUser getUserByUserName(@Param("userName")String userName);

    //2.新增用户
    Integer insertUser(SysUser sysUser);

    //3.用户信息查重(邮箱和用户名)
    List<SysUser> getUserByRepeat(@Param("userName")String userName,
                                  @Param("userEmail")String userEmail);

    //4.修改用户基本信息
    int updateUserInfo(SysUser sysUser);

    //5.用户昵称查重
    List<SysUser> getUserByNickName(@Param("nickName")String nickName);

    //6.修改密码时验证
    SysUser updateUserByCheck(@Param("userName")String userName);

    //7.获取所有用户列表
    List<SysUser> getAllUserByManagement(@Param("pageNum") Integer pageNum, @Param("limit") Integer limit);
    Long countAllUser();

    //8.查询用户权限值
    Integer getUserStatus(@Param("userName")String userName);

    //9.根据用户ID查询用户
    SysUser getUserById(@Param("admId")Integer admId);

    //10.根据用户邮箱查询用户
    SysUser getUserByEmail(@Param("userEmail")String userEmail);

}
