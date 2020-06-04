package com.wxd.myutils.base

/**
 *  Create by @author wxd
 *  @time 2019/9/25  上午 10:40
 *  @describe
 */
data class BaseResponse<out T>(val code: Int, val msg: String, val data: T,val success:Boolean)