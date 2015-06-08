package com.mgw.member.interfaces;

import android.content.Intent;

/**
 * 二维码初始化
 * 
 * @author huyan
 * 
 */
public interface initZxingListener {
	
	/**
	 * 跳转到页面
	 * 
	 */
	public void toZxingActivity();
	/**
	 * setResult  返回值
	 * 
	 */
	public void getForResult(Intent intent);
	
	
	
}