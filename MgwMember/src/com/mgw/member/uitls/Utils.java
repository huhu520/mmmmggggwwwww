package com.mgw.member.uitls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hx.hxchat.activity.ChatActivity;
import com.hx.hxchat.domain.UserFriendBean;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.ui.activity.SubWebviewActivity;
import com.mgw.member.ui.activity.cityleague.AdinfoActivity;
import com.mgw.member.ui.activity.cityleague.CityleagueActivity;
import com.mgw.member.ui.activity.cityleague.GoodDetailActivity;
import com.mgw.member.ui.activity.cityleague.ShopDetailAndIntroduceActivity;

public class Utils {
	public static final String PACKAGE_NAME = "com.dong8";

	@SuppressWarnings("unused")
	public static String getClientVersionHead(Context context) {

		String pName = PACKAGE_NAME;
		String versionName = getVersionName(context);
		return "Android|" + versionName;
	}

	public static String getVersionName(Context context) {
		String versionName = "1.0";
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS);
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {

		}

		return versionName;
	}

	public static int getVersionCode(Context context) {

		int versionCode = 8;
		try {
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_CONFIGURATIONS);
			versionCode = pinfo.versionCode;
		} catch (NameNotFoundException e) {

		}

		return versionCode;
	}

	// public static RespUser.User getUserInfo(Context context) {
	// String sss = PreferencesUtils.getString(context, "user", "");
	// RespUser.User user = null;
	// TLog.i("hansen", "=========user===:"+sss);
	// try {
	// user = (RespUser.User) JSON.parseObject(sss, RespUser.User.class);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return user;
	// }

	// /**
	// * 获取城市列表信息
	// * @param context
	// * @return
	// */
	// public static CityList getCityList(Context context) {
	// String sss = PreferencesUtils.getString(context, "CITY_LIST", "");
	// CityList cityList=null;
	// try {
	// cityList = (CityList) JSON.parseObject(sss, CityList.class);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// return cityList;
	// }
	//

	// /**
	// * 获取当前选择的城�? * @param context
	// * @return
	// */
	// public static String getCityString(Context context) {
	// return PreferencesUtils.getString(context, "city", "");
	//
	// }

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static boolean isMobileNO(String mobiles) {
		String telRegex = "[1][358]\\d{9}";
		if (TextUtils.isEmpty(mobiles))
			return false;
		return mobiles.matches(telRegex);
	}

	/**
	 * 拨打电话
	 * 
	 * @param telNumber
	 *            电话号码
	 * @param isDr
	 *            true直接拨打，需要权限，false调用系统拨打界面拨打电话，不需要权限 Created by huyan
	 */
	public static void callMobliePhone(String telNumber, boolean isDr) {

		if (isDr) {
			// 传入服务， parse（）解析号码
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
			// 通知activtity处理传入的call服务
			UIUtils.getContext().startActivity(intent);

		} else {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telNumber));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			UIUtils.getContext().startActivity(intent);
		}

	}

	/**
	 * 拨打电话
	 * 
	 * @param telNumber
	 *            电话号码
	 * @param isDr
	 *            true直接拨打，需要权限，false调用系统拨打界面拨打电话，不需要权限 Created by huyan
	 */
	public static void callMobliePhone(Uri telNumber, boolean isDr) {

		if (isDr) {
			// 传入服务， parse（）解析号码
			Intent intent = new Intent(Intent.ACTION_CALL, telNumber);
			// 通知activtity处理传入的call服务
			UIUtils.getContext().startActivity(intent);

		} else {
			Intent intent = new Intent(Intent.ACTION_DIAL, telNumber);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			UIUtils.getContext().startActivity(intent);
		}

	}

	public static void toConsulterHX(final Context context, final String userid) {

		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(context, ChatActivity.class);
				UserFriendBean friendInfo2Bean;
				intent.putExtra("userId", userid);
				try {
					friendInfo2Bean = UserUtils.getFriendInfo2Bean(userid);
					if (friendInfo2Bean != null && friendInfo2Bean.getItems().size() > 0) {
						intent.putExtra("userName", friendInfo2Bean.getItems().get(0).getNickName());

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				context.startActivity(intent);
			}
		});

	}

	/**
	 * 去城市商家页
	 * 
	 * @param context
	 * @param sid
	 *            Created by Administrator
	 */
	public static void toSupplier(final Activity context, final String sid) {

		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(context, ShopDetailAndIntroduceActivity.class);
				intent.putExtra("sid", sid);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
			}
		});

	}

	public static void buyInSupplier(final Activity context, final String sid, final String pid) {
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(context, GoodDetailActivity.class);
				intent.putExtra("sid", sid);
				intent.putExtra("pid", pid);
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
			}
		});

	}

	public static void transforTo(final Activity context, final String url, final String parm, final int tAG) {

		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(context, SubWebviewActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("type", tAG);
				intent.putExtra("parm", parm == null ? "" : parm);
				intent.putExtra("title", false);
				context.startActivityForResult(intent, 1);
				context.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
			}
		});

	}

	public static void toAds(final Context context, final String dd) {
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(context, AdinfoActivity.class);

				Bundle bundle = new Bundle();
				try {
					bundle.putString("adId", dd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});

	}

	/**
	 * 去城市联盟
	 * 
	 * @param activity
	 */
	public static void toSupplier(final Context activity) {

		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(activity, CityleagueActivity.class);
				activity.startActivity(intent);
			}

		});

	}

	/**
	 * 异步获得更新信息
	 * 
	 * @param LocalHtmlcod
	 *            (versionName)
	 * @param htmlcode 
	 * @return Created by Administrator
	 */
	public static String getAppUpdateInfo(String apkVersionname) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", "app.get"));
		params.add(new BasicNameValuePair("telephone", "13548636482"));
		params.add(new BasicNameValuePair("pType", "5"));
		params.add(new BasicNameValuePair("pFlag", "0"));
		params.add(new BasicNameValuePair("pVersionName", apkVersionname));
//		 );

		String res = PackageUtils.sendPost(params);
		return res;

	}
	/**
	 * 异步获得更新信息
	 * 
	 * @param LocalHtmlcod
	 *            (versionName)
	 * @param htmlcode 
	 * @return Created by Administrator
	 */
	public static String getAppUpdateInfo(String apkVersionname, String htmlcode) {
		LogUtils.w("apkVersionname="+apkVersionname+",htmlcode"+htmlcode);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", "app.get"));
		params.add(new BasicNameValuePair("telephone", "13548636482"));
		params.add(new BasicNameValuePair("pType", "5"));
		params.add(new BasicNameValuePair("pFlag", "1"));
		params.add(new BasicNameValuePair("pVersionName", htmlcode.compareTo(apkVersionname) > 0 ? htmlcode : apkVersionname));
//		 );
		
		String res = PackageUtils.sendPost(params);
		
		return res;
		
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 〈功能详细描述〉 sd卡中创建一个目标文件
	 * 
	 * @param name
	 * @return Author: 14052012 zyn Date: 2014年11月7日 下午3:10:35
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static String createSDCardDir(String name) {

		/* 下载的apk文件的保存位置 */
		String savePath = Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/apk";

		// File sdcardDir = Environment.getExternalStorageDirectory();
		// String path = sdcardDir.getPath() + "/MUDOWN";
		File file = null;
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

				File dir = new File(savePath);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				file = new File(dir + File.separator + name);
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
			}
		} catch (Exception e) {

		}

		return file.getPath();
	}

	// /storage/emulated/0/mgw/apk/storage/emulated/0/mgw/apk/upapk.apk
	public static void installApk(String urlPath, Context context, NotificationManager mNotificationManager, int NOTIFYCATIONID) {
		Intent apkIntent = new Intent();
		apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		apkIntent.setAction(android.content.Intent.ACTION_VIEW);
		File apkFile = new File(urlPath);
		if (!apkFile.exists()) {
			Toast.makeText(context, "apk文件不存在！", 0).show();
			return;
		}
		Log.i("jone", "apk length " + apkFile.length() + "");
		Uri uri = Uri.fromFile(apkFile);
		apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(apkIntent);
		if (mNotificationManager != null) {
			mNotificationManager.cancel(NOTIFYCATIONID);// 删除一个特定的通知ID对应的通知
		}
	};

	/**
	 *
	 * @param serverPath
	 * @param savedPath 文件夹
	 * @param savedPathName 文件名
	 * @param dialog
	 * @return
	 */
	public static File downLoad(String serverPath, String savedPath, String savedPathName,ProgressDialog dialog) {
		try {

			URL url = new URL(serverPath);
			HttpURLConnection _conn = (HttpURLConnection) url.openConnection();
			_conn.setRequestMethod("GET");
			_conn.setConnectTimeout(5000);
			int code = _conn.getResponseCode();
			if (code == 200) {
				// 设置进度条的长度
				if (dialog != null) {

					dialog.setMax(_conn.getContentLength());
				}
				InputStream is = _conn.getInputStream();
				File _file = new File(savedPath);
				
				if (!_file.exists()) {
					_file.mkdirs();
				}
				File _file1 = new File(savedPath+savedPathName);
				if (!_file1.exists()) {
					
					_file1.createNewFile();
				}else{
					_file1.delete();
					_file1.createNewFile();
				}
				
				
				FileOutputStream _FileOutputStream = new FileOutputStream(_file1);
				int len = 0;
				byte[] buffer = new byte[1024];
				int _total = 0;
				while ((len = is.read(buffer)) != -1) {
					_FileOutputStream.write(buffer, 0, len);
					_total += len;
					if (dialog != null) {
						dialog.setProgress(_total);

					}

					Thread.sleep(20);
				}
				_FileOutputStream.flush();
				_FileOutputStream.close();
				is.close();
				return _file;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
