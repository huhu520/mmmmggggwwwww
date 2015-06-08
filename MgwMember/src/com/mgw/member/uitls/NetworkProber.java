package com.mgw.member.uitls;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.mgw.member.constant.Define_C;
import com.mgw.member.manager.BaseApplication;

public class NetworkProber {

	/**
	 * 网络是否可用
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gps是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = locationManager.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	
	
	/**
	 * wifi是否打开
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	
	
	/**
	 * 判断当前网络是否是wifi网络
	 * if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) { //判断3G�? *
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前网络是否�?G网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

//	public static boolean isNetworkAvailable_00(Context context) {
//		ConnectivityManager cm = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
//		if (cm != null) {
//			NetworkInfo info = cm.getActiveNetworkInfo();
//			if (info != null && info.isConnectedOrConnecting()) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public static boolean isNetworkAvailable_01(Context context) {
//		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo network = cm.getActiveNetworkInfo();
//		if (network != null) {
//			return network.isAvailable();
//		}
//		return false;
//	}
//
//	public static boolean checkNet(Context context) {
//
//		try {
//			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//			if (connectivity != null) {
//
//				NetworkInfo info = connectivity.getActiveNetworkInfo();
//				if (info != null && info.isConnected()) {
//
//					if (info.getState() == NetworkInfo.State.CONNECTED) {
//						return true;
//					}
//				}
//			}
//		} catch (Exception e) {
//			return false;
//		}
//		return false;
//	}


	/*
	 * 打开设置网络界面(设置Result)
	 */
	public static void setNetworkMethod(final Activity context) {
		// 提示对话框
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("网络设置提示").setMessage("网络连接不可用,请设置网络！").setNegativeButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				BaseApplication.getMainThreadHandler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO AAuto-generated method stub
						AppManager.getAppManager().AppExit(context);
					}
				}, 100);
			}
		}).show();

	}
	
//	.setPositiveButton("设置", new DialogInterface.OnClickListener() {
//
//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			// TODO Auto-generated method stub
//			Intent intent = null;
//			// 判断手机系统的版本 即API大于10 就是3.0或以上版本
//			if (android.os.Build.VERSION.SDK_INT > 10) {
//				intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
//			} else {
//				intent = new Intent();
//				ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
//				intent.setComponent(component);
//				intent.setAction("android.intent.action.VIEW");
//			}
//			context.startActivityForResult(intent, 0);
//			dialog.dismiss();
//		}
//	})
}
