package com.takuhome.back.util.password;

/**
 * 随机生成密码
 *
 * @Title:RandomPwd
 * @Author:NekoTaku
 * @Date:2022/05/11 17:00
 * @Version:1.0
 */

public class RandomPwd {

    public static String getPsw(int len) {
        // 1、定义基本字符串baseStr和出参password
        String password = null;
        String baseStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()_+{}|<>?";
        boolean flag = false;
        // 2、使用循环来判断是否是正确的密码
        while (!flag) {
            // 密码重置
            password = "";
            // 个数计数
            int a = 0, b = 0, c = 0, d = 0;
            for (int i = 0; i < len; i++) {
                int rand = (int) (Math.random() * baseStr.length());
                password += baseStr.charAt(rand);
                if (0 <= rand && rand < 10) {
                    a++;
                }
                if (10 <= rand && rand < 36) {
                    b++;
                }
                if (36 <= rand && rand < 62) {
                    c++;
                }
                if (62 <= rand && rand < baseStr.length()) {
                    d++;
                }
                if (a * b * c * d != 0) {
                    break;
                }
            }
            // 是否是正确的密码（4类中仅一类为0，其他不为0）
            flag = (a * b * c != 0 && d == 0) || (a * b * d != 0 && c == 0) || (a * c * d != 0 && b == 0)
                    || (b * c * d != 0 && a == 0);
        }
        return password;
    }

}
