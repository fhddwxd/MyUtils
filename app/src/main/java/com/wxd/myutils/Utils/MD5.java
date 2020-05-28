package com.wxd.myutils.Utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5加密工具
 *
 * @author Fansd(fansd@hadlinks.com)
 */

public class MD5 {

    public static void main(String[] str){
        String s = "11231231";
        String a = MD5.encodeMD5(s);
        System.out.println(a);
    }

    public static String encodeMD5(String s) {
        if (s.equals("")&&s==null) {
            return null;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Log.e("StringUtil", "Can't encode to md5 string", ex);
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        md.update(s.getBytes());
        byte[] data = md.digest();
        int len = data.length;
        char str[] = new char[len * 2];
        int k = 0;
        for (byte byte0 : data) {
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

}
