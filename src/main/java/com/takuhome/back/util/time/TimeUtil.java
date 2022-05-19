package com.takuhome.back.util.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 基本的时间工具类
 *
 * @Title:TimeUtil
 * @Author:NekoTaku
 * @Date:2021/12/08 9:37
 * @Version:1.0
 */
public class TimeUtil {

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static Long getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = format.format(date);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();

        return ts;
    }

    /**
     * 将时间戳转换为时间格式
     */
    public static String getTimeFormat(String timeString) {
        long time = Long.parseLong(timeString);
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
//        System.out.println("时间："+format);
        return format;
    }

    /**
     * 计算用户注册到今日的天数
     *
     * @param time
     * @return
     */
    public static int DaysBetween(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date createTime = format.parse(time);
            Calendar calendar = Calendar.getInstance();//获取指定时间点
            calendar.setTime(createTime);
            Calendar today = Calendar.getInstance();
            long days = (today.getTimeInMillis() - calendar.getTimeInMillis()) / (1000 * 60 * 60 * 24);
            return Integer.parseInt(String.valueOf(days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
