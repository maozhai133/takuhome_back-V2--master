package com.takuhome.back.service.impl;

import com.takuhome.back.dao.SysUserDao;
import com.takuhome.back.entity.SysUser;
import com.takuhome.back.entity.SysUserFront;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.ISysUserService;
import com.takuhome.back.util.dozer.DozerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户管理服务实现层
 *
 * @Title:SysUserServiceImpl
 * @Author:NekoTaku
 * @Date:2021/11/17 14:41
 * @Version:1.0
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    SysUserDao sysUserDao;

    /**
     * 登录
     *
     * @param userName
     * @return
     */
    @Override
    public SysUser getUserByUserName(String userName) {
        return sysUserDao.getUserByUserName(userName);
    }

    /**
     * 查重
     *
     * @param userName
     * @param userEmail
     * @return
     */
    @Override
    public List<SysUser> getUserByRepeat(String userName, String userEmail) {
        return sysUserDao.getUserByRepeat(userName, userEmail);
    }

    /**
     * 添加新用户
     *
     * @param sysUser
     * @return
     */
    @Override
    public Results<SysUser> insertUser(SysUser sysUser) {
        sysUserDao.insertUser(sysUser);
        return Results.success();
    }

    /**
     * 修改用户基本信息
     *
     * @param sysUser
     * @return
     */
    @Override
    public Boolean updateUserInfo(SysUser sysUser) {
        int edit = sysUserDao.updateUserInfo(sysUser);
//        System.out.println("是否修改:"+edit);
        if (edit > 0) {
            return true;
        }
        return false;
    }

    /**
     * 用户昵称查重
     *
     * @param nickName
     * @return
     */
    @Override
    public List<SysUser> getUserByNickName(String nickName) {
        return sysUserDao.getUserByNickName(nickName);
    }

    /**
     * 修改时查询用户基本信息(检测用)
     *
     * @param userName
     * @return
     */
    @Override
    public SysUser updateUserByCheck(String userName) {
        return sysUserDao.updateUserByCheck(userName);
    }

    /**
     * 查询所有用户并分页(管理员)
     *
     * @param pageNum
     * @param limit
     * @return
     */
    @Override
    public Results<SysUser> getAllUsers(Integer pageNum, Integer limit) {
        return Results.success(sysUserDao.countAllUser().intValue(),
                sysUserDao.getAllUserByManagement(pageNum, limit));
    }

    /**
     * 查询用户权限值
     *
     * @param userName
     * @return
     */
    @Override
    public Integer getUserStatus(String userName) {
        return sysUserDao.getUserStatus(userName);
    }

    /**
     * 根据用户id查询用户
     *
     * @param admId
     * @return
     */
    @Override
    public SysUser getUserById(Integer admId) {
        return sysUserDao.getUserById(admId);
    }

    /**
     * 根据用户邮箱查询用户
     *
     * @param userEmail
     * @return
     */
    @Override
    public SysUser getUserByEmail(String userEmail) {
        return sysUserDao.getUserByEmail(userEmail);
    }

    @Override
    public SysUserFront getUserInfoForFront(String userName) {
        //查询用户信息
        SysUser userByUserName = sysUserDao.getUserByUserName(userName);
        //通过dozer映射新的实体
        SysUserFront userFrontInfo = DozerUtil.transfor(userByUserName, SysUserFront.class);
        return userFrontInfo;
    }
}
