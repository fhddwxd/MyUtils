package com.wxd.myutils.views;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.ContextThemeWrapper;

import com.wxd.myutils.R;


public class BaseAlertDialog {

	//创建普通对话框
	public static Dialog createNormaltDialog(Context context, String title,
                                             String message, String positiveBtnName, String negativeBtnName,
                                             OnClickListener positiveBtnListener, OnClickListener negativeBtnListener) {
		Dialog dialog=null;
		Builder builder=new Builder(new ContextThemeWrapper(context,
				R.style.dialog_translucent));
		builder.setTitle("提示");
		builder.setMessage(message);
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		dialog=builder.create();

		return dialog;
	}

	//创建单选按钮对话框
	public static Dialog createRadioDialog(Context context, String title,
                                           String[] itemsString, String positiveBtnName, String negativeBtnName,
                                           OnClickListener positiveBtnListener, OnClickListener negativeBtnListener,
                                           OnClickListener itemClickListener) {
		Dialog dialog=null;
		Builder builder=new Builder(new ContextThemeWrapper(context,
				android.R.style.Theme_Holo_Light));
		builder.setTitle(title);
		//默认第一个按钮被选中
		builder.setSingleChoiceItems(itemsString, 0, itemClickListener);
		//添加一个按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		dialog=builder.create();

		return dialog;
	}
	//创建一个单选按钮有默认选中功能的对话框
	public static Dialog createMyDialog(Context context, String title,
                                        String[] itemsString, int checkedItem, String positiveBtnName, String negativeBtnName,
                                        OnClickListener positiveBtnListener, OnClickListener negativeBtnListener,
                                        OnClickListener itemClickListener){
		Dialog dialog=null;
		Builder builder=new Builder(new ContextThemeWrapper(context,
				android.R.style.Theme_Holo_Light));
		builder.setTitle(title);
		//设置默认一个按钮被选中
		builder.setSingleChoiceItems(itemsString, checkedItem, itemClickListener);
		//添加一个按钮
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		dialog=builder.create();
		return dialog;
	}
}
