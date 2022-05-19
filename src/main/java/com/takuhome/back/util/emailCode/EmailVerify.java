package com.takuhome.back.util.emailCode;

import com.takuhome.back.util.md5.Md5Cipher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

/**
 * 邮箱验证码
 *
 * @Title:EmailVerify
 * @Author:NekoTaku
 * @Date:2021/12/10 15:32
 * @Version:1.0
 */
public class EmailVerify {

    /**
     * 随机生成邮箱验证码
     *
     * @param num
     * @return
     */
    public static String VerifyCode(int num) {
        Random r = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int ran1 = r.nextInt(10);
            stringBuffer.append(String.valueOf(ran1));
        }
//        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }


    /**
     * 保存验证码
     *
     * @param resultMap
     * @param code
     * @return
     */
    public static Map<String, Object> saveCode(Map<String, Object> resultMap,String code) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();//获取时间点
        calendar.add(Calendar.MINUTE, 5);
        String currentTime = sf.format(calendar.getTime());//生成5分钟后时间，用户校验是否过期

        String hash = Md5Cipher.encrypt(code);//加密验证码
        resultMap.put("hash", hash);//验证码
        resultMap.put("tamp", currentTime);//时间
        return resultMap;
    }
}
