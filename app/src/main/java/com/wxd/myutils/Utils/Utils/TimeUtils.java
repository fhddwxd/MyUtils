package com.wxd.myutils.Utils.Utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类或接口的描述信息
 *
 * @author zhaojy
 * @date 2017/9/8 13:13
 */

public class TimeUtils {

    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getNowTime(String format) {
        if (TextUtils.isEmpty(format)) {
            format = DEFAULT_TIME_FORMAT;
        }
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(now);
    }
    /**
     * 判断时间比当前时间
     *
     * @param timeStr
     * @return 比当前早返回-1 等于当前返回0 比当前晚返回1
     */
    public static int timeThanNow(String timeStr) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String currentTime = sdf.format(date);
        if (currentTime.compareTo(timeStr) < 0) {
            return 1;
        } else if (currentTime.compareTo(timeStr) == 0) {
            return 0;
        } else {
            return -1;
        }
    }

}
