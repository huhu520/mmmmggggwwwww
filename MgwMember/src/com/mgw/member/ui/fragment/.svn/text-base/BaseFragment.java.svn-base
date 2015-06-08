package com.mgw.member.ui.fragment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mgw.member.R;
import com.mgw.member.constant.imp_Define;
import com.mgw.member.factory.FragmentFactory;
import com.mgw.member.factory.MgwWebViewFactory;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.MGWBaseActivity;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.ui.activity.login.LoginActivity;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.DialogUtils;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.NetworkProber;
import com.mgw.member.uitls.UIUtils;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public abstract class BaseFragment extends Fragment implements imp_Define {
	private final String TAG = BaseFragment.class.getSimpleName();
	private static final String APP_CACAHE_DIRNAME = "/webcache";
	public DialogUtils mDialogUtils;
	public View view;
	public Context context;
	protected Dialog dialog;
	boolean mShowExit = false;
	protected MgwWebViewFactory mWebViewFactory;

	/** 标记第一次显示用 */
	private int count = 0;

	protected BackHandledInterface mBackHandledInterface;

	/**
	 * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
	 * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
	 * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
	 */
	public abstract boolean onBackPressed();

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);

		if (!hidden) {
			com.mgw.member.uitls.LogUtils.i(TAG + "onHiddenChanged+..." + this + context);
			// 将当前显示的fragment显示注册到BackHandledInterface中
			mBackHandledInterface.setSelectedFragment(this);

		}

	}

	@Override
	public void onStart() {
		// 第一次将HomeFragment注册到BackHandledInterface
		if (count == 0) {
			mBackHandledInterface.setSelectedFragment(FragmentFactory.createFragment(FragmentFactory.TAB_HOME));
			com.mgw.member.uitls.LogUtils.i(TAG + "onHiddenChanged+---onStart..." + this + "count=" + count);
			count++;
		}
		count++;
		super.onStart();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		mWebViewFactory = MgwWebViewFactory.getInstance(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		if (!(getActivity() instanceof BackHandledInterface)) {
			throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
		} else {
			this.mBackHandledInterface = (BackHandledInterface) getActivity();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 设计进度条
		mDialogUtils = DialogUtils.getDialogUtils(getActivity());
		// mDialogUtils.createLoadingDialog("美顾问为您卖力加载中...", getActivity());
		view = initView(inflater);
		return view;
	}

	public View getRootView() {
		return view;
	}

	/**
	 * 初始化view
	 * 
	 * @param inflater
	 * @return Created by huyan
	 */
	public abstract View initView(LayoutInflater inflater);

	/**
	 * 初始化数据 Created by huyan
	 */
	public abstract void initData();

	/**
	 * 在本fragment 打开一个webview展示
	 * 
	 * @param view
	 * @param url
	 *            Created by huyan
	 */
	public abstract void showTwoPage(WebView view, String url);

	/**
	 * 关闭本fragment中的webview 并清空webview栈
	 */
	public abstract void hideErrorPage();

	/**
	 * 打开连接到新开的activist中显示
	 * 
	 * @param url
	 * @param intent
	 *            Created by huyan
	 */
	public abstract void open2OhterActivitye(Intent intent);

	/**
	 * 开启activist
	 * 
	 * @param intent
	 *            意图
	 * @param forResut
	 *            true（forreslut） Created by HUYAN
	 */
	public void open2OhterActivitye(Intent intent, boolean forResut) {

	}

	/**
	 * 直接在本fragment的webview加载
	 */
	protected abstract void openWebview(WebView view, String url);

	/**
	 * webviewSettings设置
	 * 
	 * @param webview
	 * @param OnKeyListener
	 *            对回退键的监听处理 Created by huyan
	 */
	protected void webViewSetting( WebView webview, OnKeyListener listener) {
		WebSettings settings = webview.getSettings();
		// // 适应屏幕设置webview推荐使用的窗口，设置为true
		settings.setUseWideViewPort(true);
		settings.setSupportZoom(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setJavaScriptEnabled(true);
//		webview.removeJavascriptInterface("searchBoxJavaBridge_");
		settings.setLoadWithOverviewMode(true);
		FrameLayout.LayoutParams mWebViewLP = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		webview.setLayoutParams(mWebViewLP);
		webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webview.setOnKeyListener(listener);

		// settings.setCacheMode(WebSettings.LOAD_DEFAULT); //设置 缓存模式
		// // 开启 DOM storage API 功能
		// settings.setDomStorageEnabled(true);
		// //开启 database storage API 功能
		// settings.setDatabaseEnabled(true);
		// String cacheDirPath =
		// getActivity().getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
		// // String cacheDirPath =
		// getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
		// Log.i(TAG, "cacheDirPath="+cacheDirPath);
		// //设置数据库缓存路径
		// settings.setDatabasePath(cacheDirPath);
		// //设置 Application Caches 缓存目录
		// settings.setAppCachePath(cacheDirPath);
		// //开启 Application Caches 功能
		// settings.setAppCacheEnabled(true);

	}

	/**
	 * 跳转到登录页面
	 * 
	 * @param activity
	 *            Created by Administrator
	 */
	protected void go2LoginActivity(Context context) {
		UIUtils.startActivity(new Intent(context, LoginActivity.class));
		AppManager.getAppManager().finishActivity(MainActivity.class);

	}

	/**
	 * 返回html解析的结构
	 * 
	 * @param data
	 *            html内容
	 * @return map parentPage（含有此标签true ，找不到网页true） Created by huyan
	 */
	protected Map<String, Boolean> parseHtmlForIsRootPage(String data) {
		Map<String, String> mapss = parseHtml2GetParentValue(data);
		HashMap<String, Boolean> maps = new HashMap<>();
		maps.put("parentPage", !(mapss.get("parentPage") == ""));
		maps.put("errorPage", data.contains("找不到网页"));
		return maps;
	}

	protected Map<String, String> parseHtml2GetParentValue(String data) {
		HashMap<String, String> maps = new HashMap<>();
		long a = SystemClock.currentThreadTimeMillis();
		Document parse = Jsoup.parse(data);
		String val = "";
		Element elementById = parse.getElementById("parentPage");
		if (elementById != null) {
			val = elementById.val();
		}
		long b = SystemClock.currentThreadTimeMillis() - a;
		LogUtils.i(TAG + "解析html耗时time =" + b + "ms,value=" + val);
		parse = null;
		maps.put("parentPage", val);
		maps.put("errorPage", data.contains("找不到网页") ? "yes" : "no");
		return maps;
	}

	public synchronized void showLoadingDialog(Context con, Class clazz) {

		if ((MainActivity.mainActivity) == null)
			return;
		int click_m_index = ((MainActivity.mainActivity)).click_m_index;
		Class class1 = ((MainActivity.mainActivity)).fragmentClass[click_m_index - 1];

		if (class1 == clazz) {
			// 如果当前是哪个fragment 就显示哪个dialog
			if ((MainActivity.mainActivity) != null && !((MainActivity.mainActivity)).isFinishing()) {
				if (dialog == null) {
					dialog = new Dialog(con, R.style.MyDialogStyle);
					dialog.setContentView(R.layout.dialog);
					dialog.show();
					return;
				}
				if (dialog != null && !dialog.isShowing()) {
					dialog.show();
				}

			}

		} else {

		}

	}

	public synchronized void dismissLoadingDialog() {
		if ((MainActivity.mainActivity) != null && !((MainActivity.mainActivity)).isFinishing()) {

			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
		// if ((MainActivity) getActivity() != null && !((MainActivity)
		// getActivity()).isFinishing()) {
		//
		// if (dialog != null && dialog.isShowing()) {
		// dialog.dismiss();
		// dialog = null;
		// }
		// }
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

		dismissLoadingDialog();
	}

	/**
	 * 清除WebView缓存
	 */
	public void clearWebViewCache() {

		// 清理Webview缓存数据库
		try {
			getActivity().deleteDatabase("webview.db");
			getActivity().deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// WebView 缓存文件
		File appCacheDir = new File(getActivity().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
		Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

		File webviewCacheDir = new File(getActivity().getCacheDir().getAbsolutePath() + "/webviewCache");
		Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

		// 删除webview 缓存目录
		if (webviewCacheDir.exists()) {
			FileUtils.deleteFile(webviewCacheDir);
		}
		// 删除webview 缓存 缓存目录
		if (appCacheDir.exists()) {
			FileUtils.deleteFile(appCacheDir);
		}
	}

}
