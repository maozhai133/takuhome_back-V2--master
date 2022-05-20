package com.takuhome.back.controller;

import com.takuhome.back.entity.SysUser;
import com.takuhome.back.result.ResponseCode;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IMailService;
import com.takuhome.back.service.ISysUserService;
import com.takuhome.back.util.emailCode.EmailVerify;
import com.takuhome.back.util.md5.Md5Cipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 找回密码控制器
 *
 * @Title:RePasswordController
 * @Author:NekoTaku
 * @Date:2022/05/12 8:47
 * @Version:1.0
 */
@RequestMapping(value = "/resetPassword")
@Controller
public class RePasswordController {

    @Autowired
    private ISysUserService ISysUserService;

    @Autowired
    private IMailService IMailService;

    private Map<String, Object> resultMap = new HashMap<>();

    /**
     * 跳转忘记密码页面
     *
     * @return
     */
    @GetMapping("/openRePwd")
    public String openRePassword() {
        System.out.println("调用打开忘记密码页面");
        return "loginForgot";
    }

    /**
     * 找回密码保存
     *
     * @param sysUser
     * @param identify
     * @param newPwd
     * @return
     */
    @PostMapping("/retrievePwd")
    @ResponseBody
    public Results<SysUser> RetrievePwd(SysUser sysUser, String identify, String newPwd) {

        System.out.println("调用保存修改密码");
        SysUser userByEmail = ISysUserService.getUserByEmail(sysUser.getUserEmail());

        //邮箱存在
        if (userByEmail != null) {
            //验证原密码和新密码是否相同
            if (userByEmail.getPassWord() == newPwd) {
                return Results.failure(ResponseCode.PASSWORD_REPEAT.getCode(),
                        ResponseCode.PASSWORD_REPEAT.getMessage());
            }
            //判断邮箱验证码是否正确
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
                    //对密码进行加密
                    userByEmail.setPassWord(Md5Cipher.encrypt(newPwd));
                    //保存修改
                    if (ISysUserService.updateUserInfo(userByEmail)) {
                        //修改成功
                        return Results.success();

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
        return Results.failure(ResponseCode.EMAIL_ERROR.getCode(),
                ResponseCode.EMAIL_ERROR.getMessage());
    }

    /**
     * 发送邮箱验证码(找回密码密码时)
     *
     * @param email
     * @return
     */
    @PostMapping(value = "/sendEmail")
    @ResponseBody
    public Results getEmailCode(String email) {
        System.out.println("调用发送邮箱验证码(修改密码时)");

        //验证邮箱是否存在
        SysUser user = ISysUserService.getUserByEmail(email);
        if (!email.equals(user.getUserEmail())) {
            return Results.failure(ResponseCode.EMAIL_ERROR.getCode(),
                    ResponseCode.EMAIL_ERROR.getMessage());
        }

        String code = EmailVerify.VerifyCode(6);//随机生成6位数的验证码
        String subject = "博客系统";//标题
        String message = "<h3>【博客系统】您正在使用忘记密码进行修改密码操作，</h3><br/>您的验证码为:" +
                "<span style='color:red'>" + code + "</span>,有效时间为<span style='color:red'>5</span>分钟(若不是本人操作，请及时修改密码)";
        //发送邮件

        try {
            IMailService.sendMail(email, subject, message);
            resultMap = EmailVerify.saveCode(resultMap, code);
            return Results.success();
        } catch (Exception e) {
            return Results.failure();
        }
    }

}
