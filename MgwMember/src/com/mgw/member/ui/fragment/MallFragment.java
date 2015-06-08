package com.mgw.member.ui.fragment;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgw.member.R;
import com.mgw.member.bean.WebViewInfo;
import com.mgw.member.factory.MgwWebViewFactory;
import com.mgw.member.js.dao.JsCnative;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ottoEvent.MallFragmentNeedRefreshEvent;
import com.mgw.member.ui.activity.MGWBaseActivity;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.ui.activity.SubWebviewActivity;
import com.mgw.member.ui.activity.WebviewActivity;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.NetworkProber;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.ViewUtils;
import com.squareup.otto.Subscribe;

/**
 * 
 * 商城
 * 
 * 
 * 
 * @author huyan
 */

public class MallFragment extends BaseFragment implements OnClickListener {

	private final String TAG = MallFragment.class.getSimpleName().toString();

	// private WebView webview = null;

	private String defaultUrl = "";

	/**
	 * webview容器
	 */
	private FrameLayout fl_webviews;

	private RelativeLayout errorPage;
	private TextView mainpage;
	private LinearLayout layout;
	private TextView back;
	private TextView top_title;

	private String proTitle = "";
	private String currentUrl = "";

	private String parentPageValue = "";

	private boolean isContainParentPageValue = false;
	private int currentProgr = 0;
	public Boolean isErrorPage;

	// <input name="parentPage" />

	@SuppressLint({ "NewApi", "JavascriptInterface" })
	@Override
	public View initView(LayoutInflater inflater) {

		LogUtils.i(TAG + "initView");
		View view = UIUtils.inflate(R.layout.fragment_mall);
		fl_webviews = (FrameLayout) view.findViewById(R.id.fl_webviews);
		errorPage = (RelativeLayout) view.findViewById(R.id.error_layout);

		mainpage = (TextView) view.findViewById(R.id.mainpage);
		layout = (LinearLayout) view.findViewById(R.id.layout);
		back = (TextView) view.findViewById(R.id.back);
		top_title = (TextView) view.findViewById(R.id.title);

		back.setOnClickListener(this);
		mainpage.setOnClickListener(this);

		mainpage.setVisibility(View.GONE);
		layout.setVisibility(View.VISIBLE);
		back.setVisibility(View.GONE);
		top_title.setVisibility(View.VISIBLE);

		mWebViewFactory = MgwWebViewFactory.getInstance(context);
		view.findViewById(R.id.refresh).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_REFRESH);
			}
		});

		final WebView webView = mWebViewFactory.createSubWebView(true, context);

		webView.addJavascriptInterface(new Handle(), JsCnative.mgwjs);

		webViewSetting(webView, new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					// 能后退 没有包含根标签
					if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack() && !isContainParentPageValue) { //

						webView.clearHistory();

						return false; // 已处理

					} else if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack() && isContainParentPageValue) {

						// // webview.clearHistory();

						// return false; // 已处理

						webView.loadUrl(parentPageValue);

						return true; // 已处理

					}

				}

				return false;

			}

		});
		

		// id.webkit.WebView{42b2dd80 VFEDHVC. ......I. 0,0-0,0}
		// id.webkit.WebView{42ab0438 VFED..C. .F...... 0,0-1080,1653 #7f0a00fc
		// app:id/webView}
		webView.setWebViewClient(new MyWebViewClient_Mall());
		webView.setWebChromeClient(new MyWebChromeClient_Mall());

		defaultUrl = getActivity().getSharedPreferences("mgw_data", 0).getString("mgw_mall", "http://www.baidu.com");
		// http://Androidweb.mgw.cc/default.aspx?type=2&a=UxGmse1%2fhNgKbXvVQCmzaigGVcBKYBo5
		webView.loadUrl(defaultUrl);

		LogUtils.i(TAG + "initView,webview.hashcode=" + webView.hashCode());

		WebViewInfo viewInfo = mWebViewFactory.createSubWebViewInfo(webView);

		viewInfo.isRootPage = true;
		mWebViewFactory.addToMall2(viewInfo);

		if (mWebViewFactory.getBottomWebView_mall2().getmWebView() != null) {
			ViewUtils.removeSelfFromParent(mWebViewFactory.getBottomWebView_mall2().getmWebView());
		}
		fl_webviews.addView(mWebViewFactory.getBottomWebView_mall2().getmWebView());

		return view;

	}

	/**
	 * 
	 * js操作本地接口类
	 * 
	 * 
	 * 
	 * @author huyan
	 * 
	 * 
	 */

	public class Handle implements JsCnative {

		@JavascriptInterface
		public void showSource(final String data) {

			Map<String, String> map = parseHtml2GetParentValue(data);

			parentPageValue = map.get("parentPage");

			isContainParentPageValue = !(parentPageValue == "");

			isErrorPage = map.get("errorPage") == "yes";

			mWebViewFactory.getCurrentWebView_mall2().isRootPage = !isContainParentPageValue;

			showAndHideBack(mWebViewFactory.getCurrentWebView_mall2().isRootPage);

			if (isErrorPage) {

				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_ERROR);

			} else {

				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_OK);

			}
			LogUtils.i(TAG, "showSource=");
		}

		@Override
		@JavascriptInterface
		public void getUserInfo() {

			return;

		}

		@Override
		@JavascriptInterface
		public void transferTo(String url, String parm) {

		}

		@Override
		@JavascriptInterface
		public void setClientInfo(String groupid, String uid, String sid) {

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

	/**
	 * 
	 * js消息处理handler
	 */

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {

			if (msg.what == MESSAGE_TYPE_WEBPAGER_ERROR) {

				// Toast.makeText(context, "errorpage", 0).show();

				errorPage.setVisibility(View.VISIBLE);
				// top_title.setText("找不到网页");

			} else if (msg.what == MESSAGE_TYPE_WEBPAGER_REFRESH) {

				// TODO

				mWebViewFactory.getCurrentWebView_mall2().getmWebView().loadUrl(currentUrl);

			} else if (msg.what == MESSAGE_TYPE_WEBPAGER_OK) {

				errorPage.setVisibility(View.GONE);

			}

		}

	};

	@Override
	public void initData() {

	}

	@Override
	public void showTwoPage(WebView view, String url) {

		mWebViewFactory.showUrl2OhterWebView(view, url, new MyWebViewClient_Mall(), TAG);

	}

	@Override
	public void hideErrorPage() {

		mWebViewFactory.goBackfromOhterWebview(TAG, fl_webviews);

	}

	@Override
	public void open2OhterActivitye(Intent intent) {

		if (intent == null)

			return;

		// startActivity(intent);

		startActivityForResult(intent, 22);

	}

	@Override
	public void open2OhterActivitye(Intent intent, boolean forResut) {

		if (intent == null)

			return;

		if (forResut) {

			startActivityForResult(intent, 22);

		} else {

			startActivity(intent);

		}

	}

	/**
	 * 显示隐藏back键和home键
	 * 
	 * @param isRootPage
	 *            Created by Administrator
	 */
	private void showAndHideBack(final boolean isRootPage) {
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				if (isRootPage) {
					mainpage.setVisibility(View.GONE);
					back.setVisibility(View.GONE);
				} else {
					back.setVisibility(View.VISIBLE);
					mainpage.setVisibility(View.VISIBLE);
				}

			}
		});

	}

	@Override
	protected void openWebview(WebView view, String url) {

		view.loadUrl(url);

	}

	private boolean isRootURl;

	public class MyWebViewClient_Mall extends BaseWebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {

			// 通过内部类定义的方法获取html页面加载的内容，这个需要添加在webview加载完成后的回调中

			// view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");

			view.loadUrl("javascript:window.mgwjs.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			dismissLoadingDialog();
			LogUtils.i(TAG, "onPageFinished=" + "url=" + url);
			currentProgr = 0;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			LogUtils.i(TAG, "onPageStarted=" + "url=" + url);

			if (MainActivity.mainActivity == null) {
				return;
			}
			String aa = MainActivity.mainActivity.getSharedPreferences("mgw_data", 0).getString("mgw_mall", "http://www.baidu.com");

			// [104, 116, 116, 112, 58, 47, 47, 65, 110, 100, 114, 111, 105,

			// [104, 116, 116, 112, 58, 47, 47, 97, 110, 100, 114, 111, 105,

			if (url.length() > 60 && (url.substring(10).equals(aa.substring(10)))) {
				isRootURl = true;
				LogUtils.i(TAG, "onPageStarted=" + "isRootURlm(true),url=" + url);
			} else {
				currentUrl = url;
				isRootURl = false;
				LogUtils.i(TAG, "onPageStarted=" + "isRootURlm(false),url=" + url);

			}

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (!NetworkProber.isNetworkAvailable(context)) {
				view.stopLoading();
				UIUtils.showToastSafe(R.string.network_isnot_available);
				return true;
			}

			if ((MGWBaseActivity) getActivity() != null) {
				((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
			}
			// if (Utils.isFastDoubleClick()) {
			// return true;
			// }
			// 跳转到登录页面
			if (url.contains("goto:login")) {
				view.stopLoading();
				if (mShowExit)
					return true;
				mShowExit = true;
				{
					view.stopLoading();
					BaseApplication.getApplication().logout(context);
					mShowExit = false;
				}
				return true;

			}

			// 含有键值对，不加载，直接跳转

			// if (url.contains("?id=") || url.contains("?key=")) {

			// [104, 116, 116, 112, 58, 47, 47, 65, 110, 100, 114, 111, 105,
			// [104, 116, 116, 112, 58, 47, 47, 97, 110, 100, 114, 111, 105,
			// 防止重新点击

			if (mWebViewFactory.getCurrentWebView_mall2() != null && mWebViewFactory.getCurrentWebView_mall2().getUrl().equals(url)) {
				return true;
			}

			if (isRootURl) {
				LogUtils.i(TAG, "shouldOverrideUrlLoading=" + "isRootURlm(true),url=" + url);
				view.loadUrl(url);
				mWebViewFactory.getBottomWebView_mall2().setUrl(url);

				showLoadingDialog(getActivity(), MallFragment.class);
				return true;
			} else {

				LogUtils.i(TAG, "shouldOverrideUrlLoading=" + "mWebViewFactory.getBottomWebView_mall2().getUrl();" + mWebViewFactory.getBottomWebView_mall2().getUrl());
				LogUtils.i(TAG, "shouldOverrideUrlLoading=" + "isRootURlm(false),url=" + url);
				// http://androidweb.mgw.cc/default.aspx?type=2&a=UxGmse1%2fhNiyAg7Ha%2fT7w5%2fnfLMpK69X
				// http://androidweb.mgw.cc/default.aspx?type=2&a=UxGmse1%2fhNiyAg7Ha%2fT7w5%2fnfLMpK69X
				if (url.contains("?id=") && !url.contains("&style")) {
					// http://androidweb.mgw.cc/AppMalls/BrandsShow.aspx
					view.stopLoading();
					Intent intent = new Intent(context, SubWebviewActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("type", 1);
					open2OhterActivitye(intent, false);
					return true;

				} else {
					view.stopLoading();
					// http://androidweb.mgw.cc/AppMalls/BrandsShow.aspx
					// http://androidweb.mgw.cc/AppMalls/BrandsShow.aspx
					WebViewInfo fromUrl_mall2 = mWebViewFactory.getWebViewFromUrl_mall2(url);
					if (fromUrl_mall2 != null && fromUrl_mall2.getmWebView() != null) {
						fromUrl_mall2.setUrl(url);
						
						ViewUtils.removeSelfFromParent(fromUrl_mall2.getmWebView());
						fl_webviews.addView(fromUrl_mall2.getmWebView());
						
						fl_webviews.removeView(mWebViewFactory.getCurrentWebView_mall2().getmWebView());
						mWebViewFactory.add2Top(fromUrl_mall2, MallFragment.class);

					} else {

						WebView webview = new WebView(context);
						webview.setWebViewClient(new MyWebViewClient_Mall());
						webview.setWebChromeClient(new MyWebChromeClient_Mall());
						webViewSetting(webview, null);

						webview.addJavascriptInterface(new Handle(), JsCnative.mgwjs);

						WebViewInfo viewInfo = new WebViewInfo(webview);
						viewInfo.setUrl(url);

						mWebViewFactory.add2Top(viewInfo, MallFragment.class);

						webview.loadUrl(url);

						fl_webviews.addView(mWebViewFactory.getCurrentWebView_mall2().getmWebView());

						fl_webviews.removeView(mWebViewFactory.getPreviousWebView_mall2(mWebViewFactory.getCurrentWebView_mall2()).getmWebView());

						showLoadingDialog(getActivity(), MallFragment.class);
					}

					return true;

				}

			}
		}
	}

	public class MyWebChromeClient_Mall extends BaseWebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			currentProgr = newProgress;
			if (newProgress > 51) {

				dismissLoadingDialog();

			}

		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			top_title.setText(title);
			WebViewInfo fromUrl_mall2 = mWebViewFactory.getWebViewInfoFromWebView_mall2(view);
			if (fromUrl_mall2 != null) {
				fromUrl_mall2.setTopTitle(title);
				LogUtils.i(TAG, "onReceivedTitle=" + "title=" + title + "d" + fromUrl_mall2.isRootPage);
			}

		}
	}

	@Override
	public void onResume() {

		super.onResume();

		if (dialog != null && dialog.isShowing()) {

			dialog.dismiss();

			dialog = null;

		}
		BusProvider.getInstance().register(this);
	}



	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		BusProvider.getInstance().unregister(this);

	}

	@Override
	public boolean onBackPressed() {

		LogUtils.i(TAG + "onBackPressed");
		if (errorPage.getVisibility() == View.VISIBLE) {

			if (mWebViewFactory.getMallWebinfoSize().size() > 1) {

				hideErrorPage();

				errorPage.setVisibility(View.GONE);

				return true;

			}

		}

		if (mWebViewFactory.getCurrentWebView_mall2().isRootPage) {

			return false;

		}

		// 处理fragment中的back时间

		if (mWebViewFactory.getMallWebinfoSize().size() > 1) {

			hideErrorPage();
			showAndHideBack(mWebViewFactory.getCurrentWebView_mall2().isRootPage);
			top_title.setText(mWebViewFactory.getCurrentWebView_mall2().getTopTitle());
			if (errorPage.getVisibility() == View.VISIBLE) {

				errorPage.setVisibility(View.GONE);

			}

			return true;

		}

		return false;

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);

		LogUtils.i(TAG + "onActivityResult,requestCode=" + requestCode);

		if (resultCode == Activity.RESULT_OK) {

			Bundle bundle = data.getExtras();

			String scanResult = bundle.getString("result");

			Intent intent = new Intent(context, WebviewActivity.class);

			intent.putExtra("url", scanResult);

			startActivity(intent);

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;

		case R.id.mainpage:
			// 跳至主页
			while (!mWebViewFactory.getCurrentWebView_mall2().isRootPage) {
				onBackPressed();
			}
			mWebViewFactory.getCurrentWebView_mall2().getmWebView().reload();
			break;
		default:
			break;
		}

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			if (currentProgr != 0 && currentProgr < 50) {
				showLoadingDialog(getActivity(), MallFragment.class);
			}

		}

	}
}
