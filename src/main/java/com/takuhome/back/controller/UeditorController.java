package com.takuhome.back.controller;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author nekotaku
 * @create 2021-10-11 11:07
 */

@Controller
@RequestMapping("/ueditor")
public class UeditorController {

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
     * 配置文件接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/config")
    @ResponseBody
    public String config(HttpServletRequest request, HttpServletResponse response) {
        String json = "";
        response.setContentType("application/json");
        // 获取项目在磁盘的绝对路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        try {
            // 将josn文件读到Stirng
            json = IOUtils.toString(new FileInputStream(new File(path + "/static/umedit/config.json")), Charsets.UTF_8.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 上传博文内容的图片
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/uploadImageData")
    @ResponseBody
    public Map<String, String> uploadImage(@RequestParam("upfile") MultipartFile file, HttpServletRequest request) throws IOException {

        System.out.println("调用上传博文内容的图片");
        //给每天上传的图片创建单独文件夹分类
        Date todayDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(todayDate);

        String fileName = file.getOriginalFilename();  // 文件真实名
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
        //复制
//            FileCopyUtils.copy(upfile.getBytes(), newFile);

        //虚拟访问路径
        String url = accessPath + today + "/" + fileName;

        //返回结果信息(UEditor官方要求这个json格式)
        Map<String, String> map = new HashMap<String, String>();
        //是否上传成功
        map.put("state", "SUCCESS");
        //如今文件名称
        map.put("title", fileName);
        //文件原名称
        map.put("original", fileName);
        //文件类型 .+后缀名
        map.put("type", fileName.substring(file.getOriginalFilename().lastIndexOf(".")));
        //文件路径
        // map.put("url", uploadPath);    // 浏览器不能直接访问项目外目录的图片等文件，须要作虚拟路径映射
        map.put("url", url);  // 这个路径的 /PathImage/ 是在配置类里指定的映射到本地的绝对路径
        //文件大小（字节数）
        map.put("size", file.getSize() + "");
        return map;
    }
}

