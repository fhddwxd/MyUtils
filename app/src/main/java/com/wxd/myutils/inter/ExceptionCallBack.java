package com.wxd.myutils.inter;

import com.lidroid.xutils.exception.HttpException;

public interface ExceptionCallBack {
	
	public void throwException(HttpException ex, String str);

}
