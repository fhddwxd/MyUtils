package com.wxd.myutils.Utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CY on 2017/3/6.
 */

public class UserInfoUtils implements Serializable {
    //密码验证
    public static final boolean isRightPwd(String pwd) {
        Pattern p = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\D+$)[0-9a-zA-Z]{8,16}$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    //手机号码验证
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[6-8]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /**
     * 判断ip格式是否合法
     *
     * @param str id字符串
     * @return 合法返回true 不合法返回 false
     */
    public static boolean isIPAdress(String str) {
        Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}"
                + "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }

    /**
     * 禁止EditText输入空格
     */
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" ") || source.equals("\n"))
                    return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
