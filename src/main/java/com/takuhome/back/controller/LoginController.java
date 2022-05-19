package com.takuhome.back.controller;

import com.takuhome.back.entity.SysUser;
import com.takuhome.back.result.ResponseCode;
import com.takuhome.back.result.Results;
import com.takuhome.back.service.IArticleService;
import com.takuhome.back.service.ISysUserService;
import com.takuhome.back.util.cpacha.CpachaUtil;
import com.takuhome.back.util.md5.Md5Cipher;
import com.takuhome.back.util.time.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录控制器
 *
 * @Title:LoginController
 * @Author:NekoTaku
 * @Date:2021/11/17 10:59
 * @Version:1.0
 */
@Controller
public class LoginController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IArticleService IArticleService;

    /**
     * 登录成功后的主页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView indexView(ModelAndView model) {
        System.out.println("调用登录成功后的主页");
        model.setViewName("index");
        return model;
    }

    /**
     * 打开登录页面
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/login")
    public ModelAndView loginView(ModelAndView model) {
        System.out.println("调用打开登录页面");
        model.setViewName("login");
        return model;
    }

    /**
     * 验证码生成
     *
     * @param vcodeLen
     * @param width
     * @param height
     * @param cpachaType : 区别验证码的类型，传入字符串
     * @param request
     * @param response
     */
    @GetMapping(value = "/get_cpacha")
    public void generateCpacha(
            @RequestParam(name = "vl", required = false, defaultValue = "4") Integer vcodeLen,
            @RequestParam(name = "w", required = false, defaultValue = "100") Integer width,
            @RequestParam(name = "h", required = false, defaultValue = "30") Integer height,
            @RequestParam(name = "type", required = true, defaultValue = "loginCpacha") String cpachaType,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        System.out.println("调用验证码生成");
        //调用CpachaUtil中的构造方法指定验证码的长度，宽度，高度
        CpachaUtil cpachaUtil = new CpachaUtil(vcodeLen, width, height);
        //生成验证码
        String generatorVCode = cpachaUtil.generatorVCode();
        request.getSession().setAttribute(cpachaType, generatorVCode);
        //把随机取到的验证码传入图片 true--需要干扰线
        BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
        try {
            ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录表单提交处理验证
     *
     * @param sysUser
     * @param cpacha
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public Results login(SysUser sysUser, String cpacha, HttpServletRequest request) {
        System.out.println("调用登录表单提交处理验证");
        //判断当前会话是否超时
        Object loginCpacha = request.getSession().getAttribute("loginCpacha");
        if (loginCpacha == null) {
            return Results.failure(ResponseCode.LOGIN_ERROR.getCode(),
                    ResponseCode.LOGIN_ERROR.getMessage());
        }

        //判断验证码是否正确
        if (!cpacha.toUpperCase().equals(loginCpacha.toString().toUpperCase())) {
            return Results.failure(ResponseCode.LOGIN_CPACHA.getCode(),
                    ResponseCode.LOGIN_CPACHA.getMessage());
        }

        //根据表单提交的用户名查询是否存在该用户
        SysUser findUser = userService.getUserByUserName(sysUser.getUserName());
        // System.out.println(findUser);

        //判断用户是否存在
        if (findUser == null) {
            return Results.failure(ResponseCode.PASSWORD_ERROR.getCode(),
                    ResponseCode.PASSWORD_ERROR.getMessage());
        }

        //判断用户账号是否启用;状态值>0即为启用
        if (findUser.getUserStatus() <= 0) {
            return Results.failure(ResponseCode.USER_PERMISSION_DENIED.getCode(),
                    ResponseCode.USER_PERMISSION_DENIED.getMessage());
        }


        //判断密码是否正确(数据库密码采用MD5加密，获得明文后加密再与数据库进行对比)
        sysUser.setPassWord(Md5Cipher.encrypt(sysUser.getPassWord()));//明文加密
//        System.out.println("密码"+sysUser.getPassWord());
        if (!sysUser.getPassWord().equals(findUser.getPassWord())) {
            return Results.failure(ResponseCode.PASSWORD_ERROR.getCode(),
                    ResponseCode.PASSWORD_ERROR.getMessage());
        }
        //获取注册到今天的天数
        int days = TimeUtil.DaysBetween(findUser.getCreateTime());
//        System.out.println("天数"+days);
        //获取文章总数
        Long countArticle = IArticleService.countAllArticle(findUser.getUserName());
        //验证通过后,将用户信息放到Session中
        request.getSession().setAttribute("user", findUser);
        request.getSession().setAttribute("days", days);
        request.getSession().setAttribute("count", countArticle);

        return Results.success();
    }

    /**
     * 用户退出登录
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/exit")
    public String exit(HttpServletRequest request) {
        System.out.println("调用用户退出登录");
        request.getSession().removeAttribute("user");
        return "redirect:/login";
    }


}
