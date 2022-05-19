package com.takuhome.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 管理员操作日志实体层
 *
 * @Title:SysAdminUserLog
 * @Author:NekoTaku
 * @Date:2022/05/11 10:27
 * @Version:1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysAdminUserLog implements Serializable {

    private static final long serialVersionUID = -6801549838990111588L;
    private Long logId;//操作日志id
    private Integer admId;//操作管理员id
    private String admUser;//操作管理员用户名
    private String admMsg;//操作信息
    private String operationTime;//操作时间
}
