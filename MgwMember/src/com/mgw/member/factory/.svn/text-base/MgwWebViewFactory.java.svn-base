package com.mgw.member.factory;

import java.util.Stack;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mgw.member.bean.WebViewInfo;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.ui.fragment.BaseWebViewClient;
import com.mgw.member.ui.fragment.HomeFragment;
import com.mgw.member.uitls.LogUtils;

/**
 * Web view factory class for creating {@link BrowserWebView}'s.
 */
public class MgwWebViewFactory implements WebViewFactory {
	String TAG = MgwWebViewFactory.class.getSimpleName().toString();
	public static final int TAB_HOME = 1;
	public static final int TAB_NEWS = 2;
	public static final int TAB_FIND = 3;
	public static final int TAB_MYINFO = 4;
	/** 记录home页的view栈 */
	public volatile static Stack<View> mhome_webviews = null;
	/** 记录home页的view栈 */
	public volatile static Stack<WebViewInfo> mhome_webviewsinfos = null;
	/** 记录info页的view栈 */
	public volatile static Stack<View> mmyinfo_webviews = null;
	/** 记录mall页的view栈 */
	public volatile static Stack<View> mmall_webviews = null;
	/** 记录mall页的view栈 */
	public volatile static Stack<WebViewInfo> mmall_webviewinfos = null;
	/** 记录news页的view栈 */
	public volatile static Stack<View> mnews_webviews = null;
	/** 记录find页的view栈 */
	public volatile static Stack<View> mfind_wiews = null;

	/**
	 * 添加view到堆栈
	 */
	public synchronized void addToHome(View view) {

		LogUtils.i(TAG, "addToHome,viewcode=" + view.hashCode());
		if (mhome_webviews == null) {
			mhome_webviews = new Stack<View>();
			mhome_webviews.add(view);
		} else {
			if (!mhome_webviews.contains(view)) {
				mhome_webviews.add(view);
			}

		}

	}

	/**
	 * 添加view到堆栈
	 */
	public synchronized void addToHome2(WebViewInfo view) {

		LogUtils.i(TAG, "addToHome2,viewcode=" + view.hashCode());

		if (mhome_webviewsinfos == null) {
			mhome_webviewsinfos = new Stack<WebViewInfo>();
			mhome_webviewsinfos.add(view);
		} else {
			if (!mhome_webviewsinfos.contains(view)) {
				mhome_webviewsinfos.add(view);
				LogUtils.i(TAG, "addToHome2,contains viewcode=" + view.hashCode() + "mhome_webviewsinfos.size=" + mhome_webviewsinfos.size());
			}

		}

	}

	/**
	 * 添加view到堆栈
	 */
	public synchronized void addToMyInfo(View view) {
		if (mmyinfo_webviews == null) {
			mmyinfo_webviews = new Stack<View>();
			mmyinfo_webviews.add(view);
		} else {
			if (!mmyinfo_webviews.contains(view)) {
				mmyinfo_webviews.add(view);
			}

		}
		LogUtils.i(TAG, "addToMyInfo,viewcode=" + view.hashCode());
	}

	/**
	 * 添加view到堆栈
	 */
	public synchronized void addToMall(View view) {

		LogUtils.i(TAG, "addToMall,viewcode=" + view.hashCode());

		if (mmall_webviews == null) {
			mmall_webviews = new Stack<View>();
			mmall_webviews.add(view);

		} else {
			if (!mmall_webviews.contains(view)) {
				mmall_webviews.add(view);
				LogUtils.i(TAG, "addToMall,contains viewcode=" + view.hashCode() + "mmall_webviews.size=" + mmall_webviews.size());
			}

		}

	}

	/**
	 * 添加view到堆栈
	 */
	public synchronized void addToMall2(WebViewInfo view) {

		// LogUtils.i(TAG, "addToMall2,viewcode=" + view.hashCode());

		if (mmall_webviewinfos == null) {
			mmall_webviewinfos = new Stack<WebViewInfo>();
			mmall_webviewinfos.add(view);

		} else {
			if (!mmall_webviewinfos.contains(view)) {
				mmall_webviewinfos.add(view);
				// LogUtils.i(TAG, "addToMall2,contains viewcode=" +
				// view.hashCode() + "mmall_webviews.size=" +
				// mmall_webviews.size());
			}

		}

	}

	/**
	 * 添加view到堆栈
	 */
	public synchronized void addToNews(View view) {
		if (mnews_webviews == null) {
			mnews_webviews = new Stack<View>();
			mnews_webviews.add(view);
		} else {
			if (!mnews_webviews.contains(view)) {
				mnews_webviews.add(view);
			}

		}
		LogUtils.i(TAG, "addToNews,viewcode=" + view.hashCode());
	}

	/**
	 * 添加view到堆栈
	 */
	public synchronized void addToFind(View view) {
		if (mfind_wiews == null) {
			mfind_wiews = new Stack<View>();
			mfind_wiews.add(view);
			LogUtils.i(TAG, "addToFind,contains viewcode=" + view.hashCode() + "mfind_wiews.size=" + mfind_wiews.size());
		} else {
			if (!mfind_wiews.contains(view)) {
				mfind_wiews.add(view);
				LogUtils.i(TAG, "addToFind,contains viewcode=" + view.hashCode() + "mfind_wiews.size=" + mfind_wiews.size());
			}

		}
		LogUtils.i(TAG, "addToFind,viewcode=" + view.hashCode());
	}

	// ===========================获取当前view
	// -----getCurrentWebView===========================================================

	/**
	 * 获取当前view（堆栈中最后一个压入的） home
	 */
	public View getCurrentWebView_home() {
		View View = mhome_webviews.lastElement();
		return View;
	}

	/**
	 * 获取当前view（堆栈中最后一个压入的） myInfo
	 */
	public View getCurrentWebView_info() {
		View View = mmyinfo_webviews.lastElement();
		return View;
	}

	/**
	 * 获取当前view（堆栈中最后一个压入的） mall
	 */
	public View getCurrentWebView_mall() {
		View View = mmall_webviews.lastElement();
		return View;
	}

	/**
	 * 获取当前view（堆栈中最后一个压入的） mall2
	 */
	public WebViewInfo getCurrentWebView_mall2() {
		WebViewInfo View = mmall_webviewinfos.lastElement();
		return View;
	}

	/**
	 * 获取当前view（堆栈中最后一个压入的）home2
	 */
	public WebViewInfo getCurrentWebView_home2() {
		WebViewInfo View = mhome_webviewsinfos.lastElement();
		return View;
	}

	/**
	 * 获取当前view（堆栈中最后一个压入的） news
	 */
	public View getCurrentWebView_news() {
		View View = mnews_webviews.lastElement();
		return View;
	}

	/**
	 * 获取当前view（堆栈中最后一个压入的） find
	 */
	public View getCurrentWebView_find() {
		View View = mfind_wiews.lastElement();
		return View;
	}

	// =========================
	// 获取最底层的view-----getBottomWebView=====================================================
	/**
	 * 获取最底层的view（堆栈中最开始压入的） home
	 */
	public View getBottomWebView_home() {
		View View = mhome_webviews.firstElement();
		return View;
	}

	/**
	 * 获取最底层的view（堆栈中最开始压入的） mall
	 */
	public WebViewInfo getBottomWebView_home2() {
		WebViewInfo View = mhome_webviewsinfos.firstElement();
		return View;
	}

	/**
	 * 获取最底层的view（堆栈中最开始压入的） info
	 */
	public View getBottomWebView_info() {
		View View = mmyinfo_webviews.firstElement();
		return View;
	}

	/**
	 * 获取最底层的view（堆栈中最开始压入的） find
	 */
	public View getBottomWebView_find() {
		View View = mfind_wiews.firstElement();
		return View;
	}

	/**
	 * 获取最底层的view（堆栈中最开始压入的） mall
	 */
	public View getBottomWebView_mall() {
		View View = mmall_webviews.firstElement();
		return View;
	}

	/**
	 * 获取最底层的view（堆栈中最开始压入的） mall
	 */
	public WebViewInfo getBottomWebView_mall2() {
		WebViewInfo View = mmall_webviewinfos.firstElement();
		return View;
	}

	// ================================删除当前view（堆栈中最后一个压入的）
	// ----finishWebView_home================================================================
	/**
	 * 删除当前view（堆栈中最后一个压入的） home
	 */
	public synchronized void finishWebView_home() {
		View View = mhome_webviews.lastElement();
		finishWebView(View, mhome_webviews);
	}

	/**
	 * 删除当前view（堆栈中最后一个压入的） info
	 */
	public synchronized void finishWebView_info() {
		View View = mmyinfo_webviews.lastElement();
		finishWebView(View, mmyinfo_webviews);
	}

	/**
	 * 删除当前view（堆栈中最后一个压入的） mall
	 */
	public synchronized void finishWebView_mall() {
		View View = mmall_webviews.lastElement();
		finishWebView(View, mmall_webviews);
	}

	/**
	 * 删除当前view（堆栈中最后一个压入的） mall
	 */
	public synchronized void finishWebView_mall2() {
		WebViewInfo View = mmall_webviewinfos.lastElement();
		finishWebView(View, mmall_webviewinfos);
	}

	/**
	 * 删除当前view（堆栈中最后一个压入的） mall
	 */
	public synchronized void finishWebView_home2() {
		WebViewInfo View = mhome_webviewsinfos.lastElement();
		finishWebView(View, mhome_webviewsinfos);
	}

	/**
	 * 删除当前view（堆栈中最后一个压入的） info
	 */
	public synchronized void finishWebView_news() {
		View View = mnews_webviews.lastElement();
		finishWebView(View, mnews_webviews);
	}

	/**
	 * 删除当前view（堆栈中最后一个压入的） info
	 */
	public synchronized void finishWebView_find() {
		View View = mfind_wiews.lastElement();
		finishWebView(View, mfind_wiews);
	}

	// ================================删将自身删除,重新刷新view树
	// ----finishWebView_home================================================================
	/**
	 * 将自身删除,重新刷新view树
	 */

	public void finishWebView(View view, Stack<View> mhome_webviews2) {
		if (view != null) {
			mhome_webviews2.remove(view);
		}
	}

	public void finishWebView(WebViewInfo view, Stack<WebViewInfo> mhome_webviews2) {
		if (view != null) {
			mhome_webviews2.remove(view);
		}
	}

	public synchronized void finishWebView_home(View view) {
		if (view != null) {
			finishWebView(view, mhome_webviews);
		}
	}

	public synchronized void finishWebView_home2(WebViewInfo view) {

		if (view != null) {
			finishWebView(view, mhome_webviewsinfos);
		}
	}

	public synchronized void finishWebView_info(View view) {
		if (view != null) {
			finishWebView(view, mmyinfo_webviews);
		}
	}

	public synchronized void finishWebView_mall(View view) {

		if (view != null) {
			finishWebView(view, mmall_webviews);
			LogUtils.i(TAG, "finishWebView_mall,viewcode=" + view.hashCode() + ",mmall_webviews.size=" + mmall_webviews.size());
		}
	}

	public synchronized void finishWebView_mall2(WebViewInfo view) {

		if (view != null) {
			finishWebView(view, mmall_webviewinfos);
			// LogUtils.i(TAG, "finishWebView_mall2,viewcode=" + view.hashCode()
			// + ",mmall_webviews.size=" + mmall_webviews.size());
		}
	}

	public synchronized void finishWebView_find(View view) {
		if (view != null) {
			finishWebView(view, mfind_wiews);
			LogUtils.i(TAG, "finishWebView_find,viewcode=" + view.hashCode() + ",mfind_wiews.size=" + mfind_wiews.size());
		}
	}

	// ==============================获得指定webview 的上一个webview=================
	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public View getPreviousWebView_home(View webview) {
		return getPreviousWebView(webview, mhome_webviews);
	}

	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public WebViewInfo getPreviousWebView_home2(WebViewInfo webview) {
		return getPreviousWebView(webview, mhome_webviewsinfos);
	}

	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public View getPreviousWebView_info(View webview) {
		return getPreviousWebView(webview, mmyinfo_webviews);
	}

	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public View getPreviousWebView_mall(View webview) {
		return getPreviousWebView(webview, mmall_webviews);
	}

	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public WebViewInfo getPreviousWebView_mall2(WebViewInfo webview) {
		return getPreviousWebView(webview, mmall_webviewinfos);
	}

	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public View getPreviousWebView_news(View webview) {
		return getPreviousWebView(webview, mnews_webviews);
	}

	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public View getPreviousWebView_find(View webview) {
		return getPreviousWebView(webview, mfind_wiews);
	}

	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public View getPreviousWebView(View webview, Stack<View> ss) {
		View view = null;
		int indexOf = ss.indexOf(webview);
		if (indexOf > 0) {
			view = ss.get(indexOf - 1);
		}
		return view;
	}

	/**
	 * 获得指定webview 的上一个webview
	 * 
	 * @param WebView
	 * @return Created by huyan
	 */
	public WebViewInfo getPreviousWebView(WebViewInfo webview, Stack<WebViewInfo> ss) {
		WebViewInfo view = null;
		int indexOf = ss.indexOf(webview);
		if (indexOf > 0) {
			view = ss.get(indexOf - 1);
		}
		return view;
	}

	// ============================================================

	public synchronized void clearWebVector() {
		if (mhome_webviews != null) {
			mhome_webviews.clear();
			mhome_webviews = null;
		}
		if (mhome_webviewsinfos != null) {
			mhome_webviewsinfos.clear();
			mhome_webviewsinfos = null;
		}

		if (mnews_webviews != null) {
			mnews_webviews.clear();
			mnews_webviews = null;
		}

		if (mfind_wiews != null) {
			mfind_wiews.clear();
			mfind_wiews = null;
		}

		if (mmall_webviews != null) {
			mmall_webviews.clear();
			mmall_webviews = null;
		}
		if (mmall_webviewinfos != null) {
			mmall_webviewinfos.clear();
			mmall_webviewinfos = null;
		}

		if (mmyinfo_webviews != null) {

			mmyinfo_webviews.clear();
			mmyinfo_webviews = null;

		}

	}

	// ============================================================

	public Stack<View> getHomeWebSize() {
		return mhome_webviews;

	}

	public Stack<View> getMallWebSize() {
		return mmall_webviews;

	}

	public Stack<WebViewInfo> getHomeWebinfoSize() {
		return mhome_webviewsinfos;

	}

	public Stack<WebViewInfo> getMallWebinfoSize() {
		return mmall_webviewinfos;

	}

	// =========================================================
	private final Context mContext;
	public static MgwWebViewFactory mInstance;

	public MgwWebViewFactory(Context context) {
		mContext = context;
	}

	public static MgwWebViewFactory getInstance(Context mContext) {
		if (mInstance == null) {
			mInstance = new MgwWebViewFactory(mContext);
		}
		return mInstance;

	}

	protected WebView instantiateWebView(Context mContext) {
		return new WebView(mContext);
	}

	@Override
	public WebView createSubWebView(boolean privateBrowsing) {
		return createWebView(privateBrowsing);
	}

	@Override
	public WebView createSubWebView(boolean privateBrowsing, Context mContext) {
		WebView w = instantiateWebView(mContext);
		initWebViewSettings(w);
		return w;
	}

	public WebViewInfo createSubWebViewInfo(WebView view) {
		WebViewInfo info = new WebViewInfo(view);
		return info;
	}

	@Override
	public WebView createWebView(boolean privateBrowsing) {
		WebView w = instantiateWebView(mContext);
		initWebViewSettings(w);
		return w;
	}

	@Override
	public WebView createWebView(boolean privateBrowsing, String url) {
		WebView w = instantiateWebView(mContext);
		w.loadUrl(url);
		return null;
	}

	/**
	 * 初始化settings
	 * 
	 * @param w
	 *            Created by huyan
	 */
	protected void initWebViewSettings(WebView w) {
		// w.setScrollbarFadingEnabled(true);
		// w.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		// w.setMapTrackballToArrowKeys(false); // use trackball directly
		// // Enable the built-in zoom
		// w.getSettings().setBuiltInZoomControls(true);
		// final PackageManager pm = mContext.getPackageManager();
		// boolean supportsMultiTouch =
		// pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)
		// ||
		// pm.hasSystemFeature(PackageManager.FEATURE_FAKETOUCH_MULTITOUCH_DISTINCT);
		// w.getSettings().setDisplayZoomControls(!supportsMultiTouch);
		//
		// // Add this WebView to the settings observer list and update the
		// // settings
		// final BrowserSettings s = BrowserSettings.getInstance();
		// s.startManagingSettings(w.getSettings());

	}

	/**
	 * webview的父view下添加一个webview 并且将url显示
	 * 
	 * @param view
	 *            点击url的webview
	 * @param url
	 *            路径
	 * @param MyWebViewClient_Mall
	 *            自定义的webviewClient Created by huyan
	 */
	public void showUrl2OhterWebView(WebView view, String url, BaseWebViewClient MyWebViewClient, String TAG) {
		LogUtils.i(TAG + "showTwoPage-url=" + url + ",webview.hashcode=" + view.hashCode());
		WebView webview2 = createWebView(true);
		webViewSetting(webview2);
		webview2.setWebViewClient(MyWebViewClient);
		ViewGroup group = (ViewGroup) view.getParent();
		if (group != null && group instanceof ViewGroup) {
			group.addView(webview2);
		}
		// ViewUtils.requestLayoutParent(view, true);

		switch (TAG) {
		case "HomeFragment":
			addToHome(webview2);
			break;
		case "NewsFragment":

			break;
		case "FindFragment":
			addToFind(webview2);
			break;
		case "MallFragment":
			addToMall(webview2);
			break;
		case "MyInfoFragment":
			addToMyInfo(webview2);
			break;
		default:
			break;
		}
		webview2.loadUrl(url);
	}

	/**
	 * goback;
	 * 
	 * @param TAG
	 *            Created by huyan
	 */
	public void goBackfromOhterWebview(String TAG, View fatherView) {
		// int mmall_webviews = this.mmall_webviews.size();
		// LogUtils.i(TAG + "hideErrorPage1" + mmall_webviews + "");
		// 保留最底部的webview
		switch (TAG) {
		case "HomeFragment":
			if (this.mhome_webviewsinfos.size() > 1) {
				WebViewInfo webView2 = getCurrentWebView_home2();
				WebViewInfo webView1 = getPreviousWebView_home2(webView2);
				((FrameLayout) fatherView).addView(webView1.getmWebView());
				((FrameLayout) fatherView).removeView(webView2.getmWebView());
				finishWebView_home2(webView2);
			}

			LogUtils.i(TAG, "fgoBackfromOhterWebview mmall_webviewinfos.size=" + mhome_webviewsinfos.size());

			break;
		case "NewsFragment":

			break;
		case "FindFragment":

			break;
		case "MallFragment":

			if (this.mmall_webviewinfos.size() > 1) {
				WebViewInfo webView2 = getCurrentWebView_mall2();
				WebViewInfo webView1 = getPreviousWebView_mall2(webView2);
				((FrameLayout) fatherView).addView(webView1.getmWebView());
				((FrameLayout) fatherView).removeView(webView2.getmWebView());
				finishWebView_mall2(webView2);
			}

			LogUtils.i(TAG, "fgoBackfromOhterWebview mmall_webviewinfos.size=" + mmall_webviewinfos.size());

			break;
		case "MyInfoFragment":
			
			
			
			break;
		default:
			break;
		}

		// if (this.mmall_webviews.size() > 1) {
		// View webView2 = getCurrentWebView_mall();
		// View webView1 = getPreviousWebView_mall(webView2);
		// ViewUtils.removeSelfFromParent(webView2);
		// finishWebView_mall(webView2);
		// }

	}

	public void webViewSetting(final WebView webview) {
		WebSettings settings = webview.getSettings();
		// 适应屏幕
		settings.setUseWideViewPort(true);
		settings.setSupportZoom(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setJavaScriptEnabled(true);
		settings.setLoadWithOverviewMode(true);
		FrameLayout.LayoutParams mWebViewLP = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		webview.setLayoutParams(mWebViewLP);
		webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webview.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) { //
						// 表示按返回键
						// if (mIsErrorPage) {
						// //hideErrorPage(webview);
						// }

						webview.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});
	}

	/**
	 * 通过url查找webviewinfo
	 * 
	 * @param url
	 * @return
	 */
	public synchronized WebViewInfo getWebViewFromUrl_mall2(String url) {

		return getWebViewInfoFrom(url, mmall_webviewinfos);

	}

	/**
	 * 通过url查找webviewinfo
	 * 
	 * @param WebView
	 * @return
	 */
	public synchronized WebViewInfo getWebViewInfoFromWebView_mall2(WebView webView) {
		return getWebViewInfoFrom(webView, mmall_webviewinfos);
	}

	/**
	 * 通过url查找webviewinfo
	 * 
	 * @param url
	 * @return
	 */
	public synchronized WebViewInfo getWebViewFromUrl_home2(String url) {

		return getWebViewInfoFrom(url, mhome_webviewsinfos);

	}

	/**
	 * 通过url查找webviewinfo
	 * 
	 * @param WebView
	 * @return
	 */
	public synchronized WebViewInfo getWebViewInfoFromWebView_home2(WebView webView) {
		return getWebViewInfoFrom(webView, mhome_webviewsinfos);
	}

	public WebViewInfo getWebViewInfoFrom(WebView webView, Stack<WebViewInfo> mmall_webviewinfos2) {

		for (WebViewInfo info : mmall_webviewinfos2) {
			if (webView.equals(info.getmWebView())) {
				return info;
			}
		}
		return null;

	}

	public WebViewInfo getWebViewInfoFrom(String url, Stack<WebViewInfo> mmall_webviewinfos2) {

		for (WebViewInfo info : mmall_webviewinfos2) {
			if (url.equals(info.getUrl())) {
				return info;
			}
		}
		return null;

	}

	public void add2Top(WebViewInfo fromUrl_mall2, Class clazz) {
		Stack<WebViewInfo> stack = null;

		switch (clazz.getSimpleName()) {
		case "HomeFragment":

			stack = mhome_webviewsinfos;
			break;
		case "NewsFragment":

			break;
		case "FindFragment":
			break;
		case "MallFragment":
			stack = mmall_webviewinfos;

			break;
		default:
			break;
		}

		add2StackTop(fromUrl_mall2, stack);
	}

	/**
	 * 添加到栈顶
	 * 
	 * @param fromUrl_mall2
	 *            元素
	 * @param mmall_webviewinfos2
	 *            栈
	 */
	private void add2StackTop(WebViewInfo fromUrl_mall2, Stack<WebViewInfo> mmall_webviewinfos2) {
		if (mmall_webviewinfos2.contains(fromUrl_mall2)) {
			mmall_webviewinfos2.remove(fromUrl_mall2);
		}
		mmall_webviewinfos2.push(fromUrl_mall2);

	}

}
