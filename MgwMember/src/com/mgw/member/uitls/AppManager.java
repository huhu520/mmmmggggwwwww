package com.mgw.member.uitls;

import java.util.List;
import java.util.Stack;

import com.mgw.member.factory.FragmentFactory;
import com.mgw.member.factory.MgwWebViewFactory;
import com.mgw.member.factory.WebViewFactory;
import com.mgw.member.service.Myservice;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	
	/**
	 * 结束所有Activity除了 clazz
	 */
	public void finishAllActivity(Class<?> cls) {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i) && !activityStack.get(i).getClass().equals(cls)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序(清空webview集合和fragment集合)
	 */
	public void AppExit(Context context) {
		try {
			MgwWebViewFactory.getInstance(context).clearWebVector();
			FragmentFactory.clearFragments();
			stopService(context, "com.mgw.member.service.Myservice", Myservice.class);
			finishAllActivity();

			System.exit(0);
		} catch (Exception e) {
		}
	}
	
	
	private static final int MAX_LIST_SRV = 50;

	/**
	 * 判断服务是否运行
	 * 
	 * @param mContext
	 * @param className(含包名)
	 * @return Created by Administrator
	 */
	public boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = activityManager.getRunningServices(MAX_LIST_SRV);
		LogUtils.i(" service isServiceRunning=" + serviceList.toString());
		if ((serviceList.size() < 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				LogUtils.i("service serviceList=" + serviceList.get(i).service.getClassName());
				isRunning = true;
				break;
			}
//			LogUtils.i("service serviceList=" + serviceList.get(i).service.getClassName());
		}
		return isRunning;
	}

	
	/**
	 * 
	 * @param context
	 * @param srvName
	 * @param srvClass
	 * Created by huyan
	 */
	public void stopService(Context context, String srvName, Class srvClass) {
		LogUtils.i("service stopService=,isServiceRunning(context, srvName)" + isServiceRunning(context, srvName));
		if (isServiceRunning(context, srvName)) {
			context.stopService(new Intent(context, srvClass));
		}
	}
}
