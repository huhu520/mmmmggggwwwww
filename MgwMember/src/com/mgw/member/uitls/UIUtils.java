package com.mgw.member.uitls;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.MGWBaseActivity;
import com.mgw.member.R;

public class UIUtils {

	public static Context getContext() {
		return BaseApplication.getApplication();
	}

	public static Thread getMainThread() {
		return BaseApplication.getMainThread();
	}

	public static long getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return BaseApplication.getMainThreadHandler();
	}

	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	public static View inflate(int resId) {
		return LayoutInflater.from(getContext()).inflate(resId, null);
	}

	/** 获取资源 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择器 */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}

	// 判断当前的线程是不是在主线程
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}

	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	/**
	 * 打开一个activity
	 * 
	 * @param intent
	 */
	public static void startActivity(Intent intent) {
		MGWBaseActivity activity = MGWBaseActivity.getForegroundActivity();
		if (activity != null) {
			activity.startActivity(intent);
		} else {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(intent);
		}
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final int resId) {
		showToastSafe(getString(resId));
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final String str) {
		if (isRunInMainThread()) {
			showToast(str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast(str);
				}
			});
		}
	}

	private static void showToast(String str) {
		MGWBaseActivity frontActivity = MGWBaseActivity.getForegroundActivity();
		if (frontActivity != null) {
			Toast.makeText(frontActivity, str, Toast.LENGTH_LONG).show();
		}
	}

	private static Toast toast;
	private static String showingText;

	/**
	 * 显示toast，时长为Toast.LENGTH_SHORT
	 * 
	 * @param context
	 *            The context to use. Usually your Application or Activity
	 *            object.
	 * @param text
	 *            The text to show. Can be formatted text.
	 */
	public static void showToastShort(Context context, String text) {
		showToast(context, text, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示toast，时长为Toast.LENGTH_SHORT
	 * 
	 * @param context
	 *            context The context to use. Usually your Application or
	 *            Activity object.
	 * @param resId
	 *            The resource id of the string resource to use. Can be
	 *            formatted text.
	 */
	public static void showToastShort(Context context, int resId) {
		showToast(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
	}

	/**
	 * 显示toast，时长为Toast.LENGTH_LONG
	 * 
	 * @param context
	 *            The context to use. Usually your Application or Activity
	 *            object.
	 * @param text
	 *            The text to show. Can be formatted text.
	 */
	public static void showToastLong(Context context, String text) {
		showToast(context, text, Toast.LENGTH_LONG);
	}

	/**
	 * 显示toast，时长为Toast.LENGTH_LONG
	 * 
	 * @param context
	 *            context The context to use. Usually your Application or
	 *            Activity object.
	 * @param resId
	 *            The resource id of the string resource to use. Can be
	 *            formatted text.
	 */
	public static void showToastLong(Context context, int resId) {
		showToast(context, context.getResources().getString(resId), Toast.LENGTH_LONG);
	}

	/**
	 * 显示一个toast，在这个toast没有完全消失之前，不会再显示同样的toast
	 * 
	 * @param context
	 *            context The context to use. Usually your Application or
	 *            Activity object.
	 * @param text
	 *            text The text to show. Can be formatted text.
	 * @param duration
	 *            {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
	 */
	private static void showToast(Context context, String text, int duration) {
		if (text != null && !text.equals(showingText)) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
			showingText = text;
			// 启动计时器，当toast消失后，将showingText置为null
			if (duration == Toast.LENGTH_SHORT) {
				newCountDownTimer(2000);
			} else if (duration == Toast.LENGTH_LONG) {
				newCountDownTimer(3500);
			}
		}
	}

	private static void newCountDownTimer(int time) {
		new CountDownTimer(time, time) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				showingText = null;
			}
		}.start();
	}

	/**
	 * cancel the toast </br> you can use in Activity's onDestory method
	 * 
	 */
	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}

	/**
	 * A Toast with an alert picture.
	 * 
	 * @param msg
	 *            a string to show in Toast.
	 */
	public static int dimen = dip2px(160);

	public static void showToastWithAlertPic(String msg) {

		Toast.makeText(UIUtils.getContext(), msg, Toast.LENGTH_SHORT).show();
//		if (msg.contains("重新登录")) {
//			/*
//			 * SharedPreferences.Editor sharedata =
//			 * GlobelElements.getContext().getSharedPreferences("mgw_data",
//			 * 0).edit(); sharedata.putString("mgw_data", "");
//			 * sharedata.putString("mgw_pwd", "");
//			 * sharedata.putString("mgw_account", ""); sharedata.commit();
//			 * 
//			 * GlobelElements.getInstance().logout();
//			 * 
//			 * ((GlobelElements) GlobelElements.getContext()).m_user_id = "";
//			 * GlobelElements.getContext().startActivity(new
//			 * Intent(GlobelElements.getContext(), LoginActivity.class));
//			 */
//			// TODO
//			 if(MainActivity.s_Instance!=null)
//			 MainActivity.s_Instance.showConflictDialog();
//
//		}
	}

	/**
	 * A Toast with an alert picture.
	 * 
	 * @param resId
	 *            the resource id of a string to show.
	 */
	public static void showToastWithAlertPic(int resId) {
		showToastWithAlertPic(UIUtils.getContext().getResources().getString(resId));
	}

	/**
	 * A Toast with an OK picture.
	 * 
	 * @param msg
	 *            the string to show in Toast.
	 */
	public static void showToastWithOkPic(String msg) {
		if (!TextUtils.isEmpty(msg) && !msg.equals(showingText)) {
			toast = Toast.makeText(UIUtils.getContext(), msg, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			View toastView = View.inflate(UIUtils.getContext(), R.layout.toast_custom_view, null);
			LinearLayout llToastView = (LinearLayout) toastView.findViewById(R.id.llToastView);
			if (msg.length() <= 6) {
				RelativeLayout.LayoutParams params = (LayoutParams) llToastView.getLayoutParams();
				params.width = dimen;
				params.height = dimen;
				llToastView.setLayoutParams(params);
			} else {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, dimen);
				llToastView.setLayoutParams(params);
			}
			((TextView) toastView.findViewById(R.id.tvToastText)).setText(msg);
			((ImageView) toastView.findViewById(R.id.ivToastIcon)).setImageResource(R.drawable.ic_tip_ok);
			toast.setView(toastView);
			toast.show();
			showingText = msg;
			newCountDownTimer(2000);
		}
	}

	/**
	 * A Toast with an OK picture.
	 * 
	 * @param resId
	 *            the resource id of a string to show.
	 */
	public static void showToastWithOkPic(int resId) {
		showToastWithOkPic(UIUtils.getContext().getResources().getString(resId));
	}
}
