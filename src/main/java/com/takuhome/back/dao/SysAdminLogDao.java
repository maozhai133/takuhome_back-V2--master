package com.takuhome.back.dao;

import com.takuhome.back.entity.SysAdminUserLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 管理员操作日志dao层
 *
 * @Title:SysAdminLogDao
 * @Author:NekoTaku
 * @Date:2022/05/11 10:59
 * @Version:1.0
 */
@Mapper
@Repository
public interface SysAdminLogDao {

    //1.添加日志
    void insertAdmLog(SysAdminUserLog sysAdminUserLog);
}
