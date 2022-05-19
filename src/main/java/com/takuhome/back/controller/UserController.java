package com.takuhome.back.controller;

import com.takuhome.back.entity.SysUser;
import com.takuhome.back.result.Page;
import com.takuhome.back.result.ResponseCode;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IMailService;
import com.takuhome.back.service.ISysUserService;
import com.takuhome.back.util.emailCode.EmailVerify;
import com.takuhome.back.util.md5.Md5Cipher;
import com.takuhome.back.util.time.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 *
 * @Title:UserController
 * @Author:NekoTaku
 * @Date:2021/12/07 10:52
 * @Version:1.0
 */

@Controller
@RequestMapping(value = "/User")
public class UserController {

    @Autowired
    private ISysUserService ISysUserService;

    @Autowired
    private IMailService IMailService;

    private Map<String, Object> resultMap = new HashMap<>();

    /**
     * 打开用户编辑界面
     *
     * @return
     */
    @GetMapping(value = "/editUser")
    public String editUser() {
        System.out.println("调用打开编辑用户信息界面");
        return "UserInfo/editUserInfo";
    }

    /**
     * 编辑用户基本信息并保存
     *
     * @param sysUser
     * @return
     */
    @PostMapping(value = "/savaInfoUser")
    @ResponseBody
    public Results<SysUser> edit(SysUser sysUser, HttpServletRequest request) {
        System.out.println("调用编辑用户基本信息并保存");

        //昵称查重验证
        List<SysUser> userByNickName = ISysUserService.getUserByNickName(sysUser.getNickName());
        if (userByNickName.size() > 0 && userByNickName != null) {
            for (SysUser sysUser1 : userByNickName) {
                if (sysUser1.getUserName().equals(sysUser.getUserName())) {
                    continue;
                }
                //判断昵称是否重复
                if (sysUser1.getNickName().equals(sysUser.getNickName())) {
                    System.out.println("昵称重复");
                    return Results.failure(ResponseCode.NICKNAME_REPEAT.getCode(),
                            ResponseCode.NICKNAME_REPEAT.getMessage());
                }
            }
        }
        //更新时间
        sysUser.setUpdateTime(Long.toString(TimeUtil.getCurrentTime()));

        //通过验证,保存修改
        if (ISysUserService.updateUserInfo(sysUser)) {
            SysUser userByUserName = ISysUserService.getUserByUserName(sysUser.getUserName());
            request.getSession().removeAttribute("user");
            request.getSession().setAttribute("user", userByUserName);
            return Results.success();
        }
        return Results.failure();
    }

    /**
     * 发送邮箱验证码(修改密码时)
     *
     * @param email
     * @return
     */
    @PostMapping(value = "/sendEmail")
    @ResponseBody
    public Results getEmailCode(String email, String userName) {
        System.out.println("调用发送邮箱验证码(修改密码时)");

        //验证是否为当前登录用户的绑定邮箱
        SysUser user = ISysUserService.updateUserByCheck(userName);
        if (!email.equals(user.getUserEmail())) {
            return Results.failure(ResponseCode.EMAIL_ILLEGALITY.getCode(),
                    ResponseCode.EMAIL_ILLEGALITY.getMessage());
        }

        String code = EmailVerify.VerifyCode(6);//随机生成6位数的验证码
        String subject = "博客系统";//标题
        String message = "<h3>【博客系统】您正在进行修改密码操作，</h3><br/>您的验证码为:" +
                "<span style='color:red'>" + code + "</span>,有效时间为<span style='color:red'>5</span>分钟(若不是本人操作，请及时修改密码)";
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
     * 修改用户密码
     *
     * @param sysUser
     * @return
     */
    @PostMapping(value = "/updatePwd")
    @ResponseBody
    public Results<SysUser> updatePassword(SysUser sysUser, String identify,
                                           String newPwd, HttpServletRequest request) {
        //获取当前需要修改的用户名
        String updateUserName = sysUser.getUserName();

        //根据用户名查找当前用户的基本信息
        SysUser userOld = ISysUserService.updateUserByCheck(updateUserName);

        //校验基本信息
        //1.原密码对比
        String originalPwd = Md5Cipher.encrypt(sysUser.getPassWord());
        if (!originalPwd.equals(userOld.getPassWord())) {
            return Results.failure(ResponseCode.ORIGINAL_PASSWORD.getCode(),
                    ResponseCode.ORIGINAL_PASSWORD.getMessage());
        }
        //2.绑定邮箱对比
        String userEmail = sysUser.getUserEmail();
        if (!userEmail.equals(userOld.getUserEmail())) {
            return Results.failure(ResponseCode.EMAIL_ILLEGALITY.getCode(),
                    ResponseCode.EMAIL_ILLEGALITY.getMessage());
        }
        //3.验证原密码和新密码是否相同
        if (sysUser.getPassWord() == newPwd) {
            return Results.failure(ResponseCode.PASSWORD_REPEAT.getCode(),
                    ResponseCode.PASSWORD_REPEAT.getMessage());
        }
        //4.验证邮箱验证码
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

                //对密码进行加密
                SysUser newUser = new SysUser();
                newUser.setPassWord(Md5Cipher.encrypt(newPwd));
                newUser.setUserName(updateUserName);
                //添加到数据库
                if (ISysUserService.updateUserInfo(newUser)) {
                    request.getSession().removeAttribute("user");
                    return Results.success();//修改成功
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

    /**
     * 获取全部用户列表
     *
     * @param page
     * @return
     */
    @GetMapping(value = "/allUserList")
    @ResponseBody
    public Results<SysUser> allUserList(Page page,String username) {

        System.out.println("获取全部用户列表");
        //权限验证
        Integer userStatus = ISysUserService.getUserStatus(username);
        if(userStatus!=2){
            return Results.failure(ResponseCode.USER_PERMISSION_DENIED.getCode(),
                    ResponseCode.USER_PERMISSION_DENIED.getMessage());
        }

        page.countOffset();
        return ISysUserService.getAllUsers(page.getOffset(), page.getLimit());
    }



}
