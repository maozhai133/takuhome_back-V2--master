package com.takuhome.back.controller;

import com.alibaba.fastjson.JSONObject;
import com.takuhome.back.entity.SysUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 上传头像控制器
 *
 * @Title:UploadHeadController
 * @Author:NekoTaku
 * @Date:2021/12/07 15:59
 * @Version:1.0
 */
@Controller
@RequestMapping
public class UploadHeadController {

    //虚拟路径
    @Value("${file.accessPath}")
    private String accessPath;
    //真实存储路径
    @Value("${file.uploadFolder}")
    private String realBasePath;
    //项目端口
    @Value("${server.port}")
    public String port;

    /**
     * 上传封面图
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping(value = "/uploadHeadImg")
    @ResponseBody
    public void fileUpload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        System.out.println("调用上传头像");

        //获取用户名
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        String username = user.getUserName();

        JSONObject jsonObject = new JSONObject();

//        Map res = new HashMap();
//        System.out.println("文件:"+file.toString());
        if (file.isEmpty()) {
            System.out.println("文件为空");
        }
        //给每天上传的图片创建单独文件夹分类
        Date todayDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(todayDate);

        String fileName = file.getOriginalFilename();  // 文件真实名
//        System.out.println(fileName);
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = realBasePath + "headImg/" + username + "/" + today + "/"; // 上传后的路径
//        String filePath = realBasePath + "/"; // 上传后的路径
        fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //虚拟路径,链接访问用
        String url = accessPath + "headImg/" + username + "/" + today + "/" + fileName;
        jsonObject.put("code", "0");
        jsonObject.put("msg", url);

//        System.out.println(url);
        response.setHeader("Content-Type", "text/html");
        response.getWriter().write(jsonObject.toString());
    }
}
