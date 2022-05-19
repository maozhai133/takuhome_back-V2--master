package com.takuhome.back.controller;

import com.takuhome.back.entity.SysUser;
import com.takuhome.back.result.ResponseCode;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IMailService;
import com.takuhome.back.service.ISysUserService;
import com.takuhome.back.util.emailCode.EmailVerify;
import com.takuhome.back.util.md5.Md5Cipher;
import com.takuhome.back.util.nickname.CreateNickname;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 注册控制器
 *
 * @Title:RegisterController
 * @Author:NekoTaku
 * @Date:2021/12/02 11:21
 * @Version:1.0
 */
@Controller
@RequestMapping(value = "/Register")
public class RegisterController {

    @Autowired
    private IMailService IMailService;

    private Map<String, Object> resultMap = new HashMap<>();

    @Autowired
    private ISysUserService ISysUserService;

    /**
     * 发送邮箱验证码(注册时)
     *
     * @param email
     * @return
     */
    @PostMapping(value = "/sendEmail")
    @ResponseBody
    public Results getEmailCode(String email) {
        System.out.println("调用发送邮箱验证码(注册时)");
        String code = EmailVerify.VerifyCode(6);//随机生成6位数的验证码
        String subject = "博客系统";//标题
        String message = "<h3>欢迎注册【博客系统】</h3><br/>你的验证码为:" +
                "<span style='color:red'>" + code + "</span>,有效时间为<span style='color:red'>5</span>分钟(若不是本人操作，请忽略该条邮件)";
        //发送邮件

        try {
            IMailService.sendMail(email, subject, message);
            resultMap = EmailVerify.saveCode(resultMap,code);
            return Results.success();
        } catch (Exception e) {
            return Results.failure();
        }
    }

    /**
     * 用户注册
     *
     * @param sysUser
     * @return
     */
    @PostMapping(value = "/registerUser")
    @ResponseBody
    public Results registerUser(SysUser sysUser, String identify) {
        System.out.println("调用用户注册");

        sysUser.setUserName(sysUser.getUserName().trim());
//        System.out.println(sysUser.toString());
//        System.out.println("验证码："+identify);


        //查重验证
        List<SysUser> result = ISysUserService.getUserByRepeat(sysUser.getUserName(),
                sysUser.getUserEmail());
        if (result != null && result.size() > 0) {
            for (SysUser user : result) {
                //判断用户名是否已存在
                if (user.getUserName().equals(sysUser.getUserName())) {
                    return Results.failure(ResponseCode.USERNAME_REPEAT.getCode(),
                            ResponseCode.USERNAME_REPEAT.getMessage());
                }
                //判断邮箱是否已存在
                if (user.getUserEmail().equals(sysUser.getUserEmail())) {
                    return Results.failure(ResponseCode.EMAIL_REPEAT.getCode(),
                            ResponseCode.EMAIL_REPEAT.getMessage());
                }
            }
        }
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

                //设置初始默认值
                sysUser.setUserStatus(1);//状态默认1：启用
                sysUser.setHeadImg("/asset/headImg/defaultImage/cat.jpg");//默认头像图片
                sysUser.setNickName(CreateNickname.getStringRandom(10));//随机生成用户昵称(英文)
                sysUser.setUserDesc("这个人很懒，还没有留下任何讯息");//默认简介
//                sysUser.setNickName(CreateNickname.getRandomJianHan(6));//随机生成用户昵称(中文)
                //对密码进行加密
                sysUser.setPassWord(Md5Cipher.encrypt(sysUser.getPassWord()));
                //添加到数据库
                return ISysUserService.insertUser(sysUser);
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
