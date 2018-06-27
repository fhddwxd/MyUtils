package com.wxd.myutils.Utils;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wxd.myutils.inter.ExceptionCallBack;
import com.wxd.myutils.inter.IOAuthCallBackRequest;

public class XUtilsPost {
	protected ExceptionCallBack exceptionCallBack;
	protected IOAuthCallBackRequest ioAuthCallBack;
	private Context mContext;
	private String url;
	private static XUtilsPost xUtilsPost;
	
	public static XUtilsPost getInstance(Context mContext){
		if(xUtilsPost == null){
			xUtilsPost = new XUtilsPost(mContext);
		}
		
		return xUtilsPost;
	}
	
	public XUtilsPost(Context mContext){
		this.mContext = mContext;
	}
	
	public void setOnIOAuthCallBackRequest(IOAuthCallBackRequest ioAuthCallBack){
		this.ioAuthCallBack = ioAuthCallBack;
	}
	
	public void setOnExceptionCallBack(ExceptionCallBack exceptionCallBack){
		this.exceptionCallBack = exceptionCallBack;
	}
	
	public void doPostR(String name, final String action, RequestParams params) {

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.configSoTimeout(12000);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException ex, String str) {
				exceptionCallBack.throwException(ex, str);
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				ioAuthCallBack.getIOAuthCallBackRequest(info.result,action);
			}
		});
	}	
	public void doPostRequest(String name, String action, String STEXTValue) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("action", action);
		params.addBodyParameter("STEXT", STEXTValue);
		doPostR(name,action,params);
	}
	public void setUrl(String url){
		this.url = url;
	}
}

