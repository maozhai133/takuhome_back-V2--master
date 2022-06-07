package com.takuhome.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面跳转共用控制器
 *
 * @author nekotaku
 * @create 2021-10-07 19:05
 */
@Controller
@RequestMapping("${api-url}")
public class ApiController {

    @RequestMapping("/getPage")
    @ResponseBody
    public ModelAndView getPage(ModelAndView modelAndView, String pageName) {
        modelAndView.setViewName(pageName);
        return modelAndView;
    }
}
