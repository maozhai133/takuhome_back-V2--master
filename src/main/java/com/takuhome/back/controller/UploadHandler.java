package com.takuhome.back.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 上传图片
 *
 * @author nekotaku
 * @create 2022-03-01 8:58
 */
@Controller
@RequestMapping
public class UploadHandler {

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
     * @param file
     * @param request
     * @return
     */
    @PostMapping(value = "/uploadimg")
    @ResponseBody
    public Map fileUpload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        System.out.println("调用上传封面图");
        Map res = new HashMap();
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
        String filePath = realBasePath + today + "/"; // 上传后的路径
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
        String filename = accessPath + today + "/" + fileName;
        res.put("code", "0");
        res.put("msg", "");
        res.put("data", filename);

        return res;
    }
}
