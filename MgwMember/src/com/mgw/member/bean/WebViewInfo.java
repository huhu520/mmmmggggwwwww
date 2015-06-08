package com.mgw.member.bean;

import com.mgw.member.ui.fragment.BaseFragment;

import android.webkit.WebView;

/**
 * webview封装对象
 * 
 * @author huyan
 * 
 */
public class WebViewInfo {

	/**
	 * webview对象
	 */
	private WebView mWebView;
	/**
	 * webviewtitle
	 */
	private String topTitle;

	public String getTopTitle() {
		return topTitle;
	}

	public void setTopTitle(String topTitle) {
		this.topTitle = topTitle;
	}

	/**
	 * 对象的哈希值
	 */
	private int hashcode;

	/**
	 * 加载url
	 */
	private String url = "";
	/**
	 * 对应的fragment
	 */
	private BaseFragment mBaseFragment;

	/**
	 * 是否是根页
	 */
	public boolean isRootPage;

	/**
	 * 上一个webview
	 */
	public WebView preWebview;

	public WebViewInfo(WebView mWebView) {
		super();
		this.mWebView = mWebView;

	}

	public BaseFragment getmBaseFragment() {
		return mBaseFragment;
	}

	public void setmBaseFragment(BaseFragment mBaseFragment) {
		this.mBaseFragment = mBaseFragment;
	}

	public WebView getmWebView() {
		return mWebView;
	}

	public void setmWebView(WebView mWebView) {
		this.mWebView = mWebView;
	}

	public int getHashcode() {
		return hashcode;
	}

	public void setHashcode(int hashcode) {
		this.hashcode = hashcode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
