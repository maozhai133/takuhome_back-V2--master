package com.takuhome.back.controller;

import com.takuhome.back.entity.SysUser;
import com.takuhome.back.result.ResponseCode;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IMailService;
import com.takuhome.back.service.ISysUserService;
import com.takuhome.back.util.emailCode.EmailVerify;
import com.takuhome.back.util.md5.Md5Cipher;
import com.takuhome.back.util.nickname.CreateNickname;
import com.takuhome.back.util.password.RandomPwd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理用户控制器
 *
 * @Title:adminUserController
 * @Author:NekoTaku
 * @Date:2022/05/11 8:48
 * @Version:1.0
 */
@Controller
@RequestMapping("/adminUser")
public class AdminUserController {

    @Autowired
    private ISysUserService ISysUserService;

    @Autowired
    private IMailService IMailService;


    private Map<String, Object> resultMap = new HashMap<>();

    /**
     * 停用用户
     *
     * @param sysUser
     * @param request
     * @return
     */
    @PostMapping("/blockUser")
    @ResponseBody
    public Results<SysUser> blockUpUser(SysUser sysUser, HttpServletRequest request) {

        System.out.println("调用停用用户接口");

        //根据用户id查询当前用户
        Integer admId = Integer.parseInt(sysUser.getAdmId());
        SysUser userById = ISysUserService.getUserById(admId);

        //禁用当前用户
        userById.setUserStatus(0);
        //保存
        ISysUserService.updateUserInfo(userById);
        //存入日志
        request.getSession().setAttribute("msg", "停用了用户：id：" + admId + ",用户名：" + userById.getUserName());

        return Results.success();
    }

    /**
     * 启用用户
     *
     * @param sysUser
     * @param request
     * @return
     */
    @PostMapping("/restartUser")
    @ResponseBody
    public Results<SysUser> restartUser(SysUser sysUser, HttpServletRequest request) {

        System.out.println("调用启用用户接口");

        //根据用户id查询当前用户
        Integer admId = Integer.parseInt(sysUser.getAdmId());
        SysUser userById = ISysUserService.getUserById(admId);

        //禁用当前用户
        userById.setUserStatus(1);
        //保存
        ISysUserService.updateUserInfo(userById);
        request.getSession().setAttribute("msg", "启用了用户：id：" + admId + ",用户名：" + userById.getUserName());

        return Results.success();
    }


    /**
     * 打开管理员重置用户前的邮箱验证
     *
     * @return
     */
    @GetMapping("/adminEmailVerify")
    public String resetVerify(Model model, String userId) {
        System.out.println("管理员重置用户信息验证调用");
        model.addAttribute("userId", userId);
        return "UserInfo/adminUserEmailVerify";
    }

    /**
     * 发送管理员邮箱验证
     *
     * @param email
     * @param userName
     * @param userId
     * @return
     */
    @PostMapping("/sendVerifyCode")
    @ResponseBody
    public Results getVerifyEmailCode(String email, String userName, String userId) {
        SysUser user = ISysUserService.updateUserByCheck(userName);
        SysUser userById = ISysUserService.getUserById(Integer.parseInt(userId));
        if (!email.equals(user.getUserEmail())) {
            return Results.failure(ResponseCode.EMAIL_ILLEGALITY.getCode(),
                    ResponseCode.EMAIL_ILLEGALITY.getMessage());
        }

        String code = EmailVerify.VerifyCode(6);//随机生成6位数的验证码
        String subject = "博客系统";//标题
        String message = "<h3>【博客系统】管理员操作：您正在重置当前用户名为：" + userById.getUserName() + " 的基本信息，</h3><br/>您的验证码为:" +
                "<span style='color:red'>" + code + "</span>,有效时间为<span style='color:red'>5</span>分钟(若不是管理员本人操作，请及时修改密码)";
        //发送邮件

        try {
            IMailService.sendMail(email, subject, message);
            EmailVerify.saveCode(resultMap, code);
            return Results.success();
        } catch (Exception e) {
            return Results.failure();
        }
    }

    /**
     * 重置用户基本信息
     *
     * @param identify
     * @return
     */
    @PostMapping("/restUserInfo")
    @ResponseBody
    public Results resetUserInfo(String identify, String userId, HttpServletRequest request) {
        System.out.println("调用重置用户基本信息");

        //要重置的用户对象
        SysUser userById = ISysUserService.getUserById(Integer.parseInt(userId));
        //判断验证码是否正确
        String requestHash = "";
        try {
            requestHash = resultMap.get("hash").toString();
        } catch (Exception e) {
            //未发送验证码的情况
            return Results.failure(ResponseCode.CODE_VOIDVALUE.getCode(),
                    ResponseCode.CODE_VOIDVALUE.getMessage());
        }

        String tamp = resultMap.get("tamp").toString();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");//当前时间
        Calendar c = Calendar.getInstance();
        String currentTime = sf.format(c.getTime());
        if (tamp.compareTo(currentTime) > 0) {
            String hash = Md5Cipher.encrypt(identify);//生成MD5值
            if (hash.equalsIgnoreCase(requestHash)) {
                //验证码正确
                System.out.println("通过邮箱验证");

                //生成密码
                String resetPwd = RandomPwd.getPsw(10);
                //设置初始默认值
                userById.setUserStatus(1);//状态默认1：启用
                userById.setHeadImg("/asset/headImg/defaultImage/cat.jpg");//默认头像图片
                userById.setNickName(CreateNickname.getStringRandom(10));//随机生成用户昵称(英文)
                userById.setUserDesc("这个人很懒，还没有留下任何讯息");//默认简介
                //对密码进行加密
                userById.setPassWord(Md5Cipher.encrypt(resetPwd));
                //重置保存
                if (ISysUserService.updateUserInfo(userById)) {
                    //发送邮箱通知用户
                    String subject = "博客系统";//标题
                    String message = "<h3>【博客系统】：尊敬的用户：" + userById.getUserName() + " ，您的的基本信息(密码、头像、昵称和个人简介)已被管理员重置，</h3><br/>账号重置后的密码为:" +
                            "<span style='color:red'>" + resetPwd + "</span>，请登录账号及时修改密码！";
                    //发送邮件

                    try {
                        IMailService.sendMail(userById.getUserEmail(), subject, message);
                        request.getSession().setAttribute("msg", "重置了用户：id：" + userId + ",用户名：" + userById.getUserName() + " 的基本信息，当前重置后的密码为：" + Md5Cipher.encrypt(resetPwd));
                        return Results.success();
                    } catch (Exception e) {
                        return Results.failure();
                    }

                } else {
                    return Results.failure();//修改失败
                }
            } else {
                System.out.println("验证码错误");
                return Results.failure(ResponseCode.LOGIN_CPACHA.getCode(),
                        ResponseCode.LOGIN_CPACHA.getMessage());
            }
        } else {
            //验证码超时
            System.out.println("邮箱验证码失效");
            return Results.failure(ResponseCode.CODE_TIMEOUT.getCode(),
                    ResponseCode.CODE_TIMEOUT.getMessage());
        }
    }

}
