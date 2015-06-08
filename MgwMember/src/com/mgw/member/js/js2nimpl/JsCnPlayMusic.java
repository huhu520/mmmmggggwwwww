package com.mgw.member.js.js2nimpl;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.mgw.member.js.dao.JsCnative;
import com.mgw.member.uitls.UIUtils;


/**
 * js操作音乐播放例子
 * @author huyan
 *
 */
public class JsCnPlayMusic implements JsCnative {
	
	private Context mContext;
	private WebView mWebView;
	
	public JsCnPlayMusic() {
		super();
	}

	public JsCnPlayMusic(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public JsCnPlayMusic(Context mContext,WebView mWebView) {
		super();
		this.mContext = mContext;
		this.mWebView=mWebView;
	}
	
	private void show(String fileName)  {
		UIUtils.showToastSafe("我是js控制弹出的！");
	}

	@Override
	@JavascriptInterface
	public void getUserInfo() {
		// TODO Auto-generated method stub
		return ;
	}

	@Override
	@JavascriptInterface
	public void transferTo(String url, String parm) {
		// TODO Auto-generated method stub
		
	}



	@Override
	@JavascriptInterface
	public void setClientInfo(String groupid, String uid, String sid) {
		// TODO Auto-generated method stub
		
	}




	@Override
	@JavascriptInterface
	public void toConsulter(String userid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@JavascriptInterface
	public void toSupplier(String sid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@JavascriptInterface
	public void buyInSupplier(String sid, String pid) {
		// TODO Auto-generated method stub
		
	}
	
		
	
	
}
