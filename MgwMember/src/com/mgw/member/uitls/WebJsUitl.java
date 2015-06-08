package com.mgw.member.uitls;

import java.io.File;

import android.util.Log;
import android.webkit.WebView;

import com.mgw.member.js.dao.JsCnative;
/**
 * 
 * @author huyan
 *
 */
public  class WebJsUitl {

	/**
	 * 
	 * @param mWebView 
	 * @param mJsCnative
	 * @param objName
	 */
	public static void setAddJavascriptInterface(WebView mWebView,JsCnative mJsCnative,String objName){
		if(mWebView!=null){
			mWebView.addJavascriptInterface(mJsCnative, objName);
		}
	}
	
	

}
