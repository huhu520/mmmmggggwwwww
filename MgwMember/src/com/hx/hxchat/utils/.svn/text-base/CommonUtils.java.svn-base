/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hx.hxchat.utils;

import java.math.BigDecimal;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.hx.hxchat.Constant;
import com.hx.hxchat.utils.LoadUserAvatar.ImageDownloadedCallBack;
import com.mgw.member.R;
import com.mgw.member.uitls.UIUtils;

public class CommonUtils {

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	public static String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getString(context, R.string.location_recv);
				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getString(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			digest = getString(context, R.string.picture);
			break;
		case VOICE:// 语音消息
			digest = getString(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getString(context, R.string.video);
			break;
		case TXT: // 文本消息
			if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getString(context, R.string.voice_call) + txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getString(context, R.string.file);
			break;
		default:
			System.err.println("error, unknow type");
			return "";
		}

		return digest;
	}

	static String getString(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	public static double round(Double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = null == v ? new BigDecimal("0.0") : new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// 处理数字位数
	public static String switchprice(String str) {
		if (str.contains(".") && str.indexOf(".") < str.length() - 3) {
			str = str.substring(0, str.indexOf(".") + 2);
			return str;
		} else {
			return str;
		}
	}

	/**
	 * 返回一个加载progressDialog
	 * 
	 * @param context
	 * @param setMessage
	 * @param setCancelable
	 * @return
	 */
	public static ProgressDialog getProgrossDialog(Context context, String setMessage, boolean setCancelable) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(setMessage); // 正在加载
		progressDialog.setCancelable(setCancelable);
		return progressDialog;
	}

	/**
	 * 返回一个加载progressDialog
	 * 
	 * @param context
	 * @param setMessage
	 * @param setCanceledOnTouchOutside
	 *            默认false
	 * @param setCancelable
	 *            默认true
	 * @return
	 */
	public static Dialog getUserDefinedDialog(Context context, String setMessage, boolean setCanceledOnTouchOutside, boolean setCancelable) {
		Dialog progressDialog = new Dialog(context, R.style.MyDialogStyle);
		progressDialog.setCanceledOnTouchOutside(setCanceledOnTouchOutside);
		progressDialog.setCancelable(setCancelable);
		View view = UIUtils.inflate(R.layout.dialog);
		progressDialog.setContentView(R.layout.dialog);
		((TextView) view.findViewById(R.id.tv_dialog_loadingtext)).setText(setMessage);
		return progressDialog;
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param activity
	 *            Created by Administrator
	 */
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

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
	
	public static  void showUserAvatar(ImageView iamgeView, String avatar,LoadUserAvatar avatarLoader) {
		final String url_avatar = avatar;
		iamgeView.setTag(url_avatar);
		if (url_avatar != null && !url_avatar.equals("")) {
			Bitmap bitmap = avatarLoader.loadImage(iamgeView, url_avatar, new ImageDownloadedCallBack() {

				public void onImageDownloaded(ImageView imageView, Bitmap bitmap) {
					if (imageView.getTag() == url_avatar) {
						imageView.setImageBitmap(bitmap);

					}
				}

			});
			if (bitmap != null)
				iamgeView.setImageBitmap(bitmap);

		}
	}
	
	
}
