package com.wxd.myutils.inter;

public interface RetryNetwork {
	/**
	 * 重试需要的网络操作
	 */
	void retry();

	/**
	 * 断网通知
	 */
	void netError();

}
