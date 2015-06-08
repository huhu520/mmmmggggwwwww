package com.mgw.member.ui.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.util.EasyUtils;
import com.hx.hxchat.utils.CommonUtils;
import com.mgw.member.manager.BeepManager;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ottoEvent.MallFragmentNeedRefreshEvent;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.R;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

/**
 * 所有Activity的基类，继承于ActionBarActivity
 * @author huyan
 *
 */
/**
 * 所有Activity的基类，继承于ActionBarActivity
 * 
 * @author huyan
 * 
 */
public class MGWBaseActivity extends BaseActivity {
	/** 记录处于前台的Activity */
	private static MGWBaseActivity mForegroundActivity = null;
	/** 记录所有活动的Activity */
	private static final List<MGWBaseActivity> mActivities = new LinkedList<MGWBaseActivity>();
	protected Context mContext;
	public int useCount = 0;
	public BeepManager beepManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = this;
		init();
		initView();
		beepManager = new BeepManager(this);
		AppManager.getAppManager().addActivity(this);
		initActionBar();
	}

	@Override
	protected void onResume() {
		mForegroundActivity = this;
	super.onResume();
	
	}

	@Override
	protected void onPause() {
		mForegroundActivity = null;
		super.onPause();
	
	}


	/**
	 * 初始化数据
	 */
	protected void init() {
	}

	/**
	 * 初始化view
	 */
	protected void initView() {
	}

	/**
	 * 初始化ActionBar
	 */
	protected void initActionBar() {

	}

	/** 关闭所有Activity */
	public static void finishAll() {
		List<MGWBaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<MGWBaseActivity>(mActivities);
		}
		for (MGWBaseActivity activity : copy) {
			activity.finish();
		}
	}

	/** 关闭所有Activity，除了参数传递的Activity */
	public static void finishAll(MGWBaseActivity except) {
		List<MGWBaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<MGWBaseActivity>(mActivities);
		}
		for (MGWBaseActivity activity : copy) {
			if (activity != except)
				activity.finish();
		}
	}

	/** 是否有启动的Activity */
	public static boolean hasActivity() {
		return mActivities.size() > 0;
	}

	/** 获取当前处于前台的activity */
	public static MGWBaseActivity getForegroundActivity() {
		return mForegroundActivity;
	}

	/** 获取当前处于栈顶的activity，无论其是否处于前台 */
	public static MGWBaseActivity getCurrentActivity() {
		List<MGWBaseActivity> copy;
		synchronized (mActivities) {
			copy = new ArrayList<MGWBaseActivity>(mActivities);
		}
		if (copy.size() > 0) {
			return copy.get(copy.size() - 1);
		}
		return null;
	}

	/** 推出应用 */
	public void exitApp() {
		finishAll();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
		
	}
	
	private boolean isConflictDialogShow;
	
	
	public void back(View v){
		
		
		finish();
	}

}
