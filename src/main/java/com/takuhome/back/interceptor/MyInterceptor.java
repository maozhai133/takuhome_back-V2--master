package com.takuhome.back.interceptor;

import com.takuhome.back.dao.SysAdminLogDao;
import com.takuhome.back.entity.SysAdminUserLog;
import com.takuhome.back.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 *
 * @Title:MyInterceptor
 * @Author:NekoTaku
 * @Date:2021/11/17 15:02
 * @Version:1.0
 */
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private SysAdminLogDao sysAdminLogDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取登录时保存到session的用户信息
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        if (user == null) {
            //拦截未登录请求,跳转到登录页面
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 管理员操作日志
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SysUser user = (SysUser)request.getSession().getAttribute("user");
        Object msg = request.getSession().getAttribute("msg");
        if(user != null && msg != null){
            //写入日志表
            SysAdminUserLog sysAdminUserLog = new SysAdminUserLog();
            sysAdminUserLog.setAdmUser(user.getUserName());
            sysAdminUserLog.setAdmId(Integer.parseInt(user.getAdmId()));
            sysAdminUserLog.setAdmMsg(msg.toString());
            sysAdminLogDao.insertAdmLog(sysAdminUserLog);
            request.getSession().removeAttribute("msg");
        }
    }
}
