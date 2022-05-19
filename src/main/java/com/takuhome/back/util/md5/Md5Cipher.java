package com.takuhome.back.util.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author nekotaku
 * @create 2021-07-07 17:14
 */

//加密工具类
public class Md5Cipher {

    static String salt = "Jd8785d1246asdf511234eetw2112das3365dferer1da87er523";

    public Md5Cipher() {

    }

    public static String encrypt(String string){
        String encryptedString = "";

        try {
            byte[] strByte = string.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(strByte);
            byte[] md5Byte = algorithm.digest();

            StringBuffer sb = new StringBuffer();
            String hex = "";
            for (int i = 0; i < md5Byte.length; i++) {
                hex = Integer.toHexString(0Xff & md5Byte[i]);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            encryptedString = sb.toString();
        }catch (NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }

        return encryptedString;
    }

    public static String encryptWithSalt(String string){
        return Md5Cipher.encrypt(string + salt);
    }

    public static void main(String[] argv) {
        String str = "123456";
        System.out.println(Md5Cipher.encryptWithSalt(str));
    }
}
