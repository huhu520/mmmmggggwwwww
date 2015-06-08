package com.mgw.member.ui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.alipay.android.msp.Keys;
import com.alipay.android.msp.Rsa;
import com.google.gson.Gson;
import com.hx.hxchat.activity.ChatActivity;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.domain.UserFriendBean;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.bean.UserInfoBean;
import com.mgw.member.constant.Define_C;
import com.mgw.member.constant.imp_Define;
import com.mgw.member.http.pay.WXPayRequest1;
import com.mgw.member.js.dao.JsCnative;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ottoEvent.MallFragmentNeedRefreshEvent;
import com.mgw.member.ui.activity.cityleague.GoodDetailActivity;
import com.mgw.member.ui.activity.cityleague.ShopDetailAndIntroduceActivity;
import com.mgw.member.ui.activity.login.LoginActivity;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.DialogUtils;
import com.mgw.member.uitls.InputTools;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.PreferenceHelper;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.Utils;
import com.squareup.otto.Subscribe;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * desc：
 * 
 * @author huyan
 * 
 */
@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak", "JavascriptInterface" })
public class SubWebviewActivity extends MGWBaseActivity implements OnClickListener, imp_Define {
	private final String TAG = SubWebviewActivity.class.getSimpleName();

	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	public String m_back_url = "";
	private WebView mWebView;
	private RelativeLayout rl_title_root;

	/**
	 * 首页
	 */
	private final int TAB_HOME = 1;
	/**
	 * 消息
	 */
	private final int TAB_NEWS = 2;
	/**
	 * 发现
	 */
	private final int TAB_FIND = 3;
	/**
	 * 商城
	 */
	private final int TAB_MALL = 4;
	/**
	 * 我的地盘
	 */
	private final int TAB_MYINFO = 5;

	/*
	 * 1 首页 2 消息 3 发现
	 */
	private int mWebType = TAB_HOME;

	private String url = "";
	private DialogUtils mDialogUtils;

	private boolean title = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	protected void init() {

	}

	protected void initView() {
		setContentView(R.layout.activity_sub_webview);

		mWebType = getIntent().getExtras().getInt("type");
		url = getIntent().getExtras().getString("url");
		title = getIntent().getExtras().getBoolean("title");

		mWebView = (WebView) findViewById(R.id.webview);
		rl_title_root = (RelativeLayout) findViewById(R.id.rl_title_root);

		if (title) {
			rl_title_root.setVisibility(View.VISIBLE);
		} else {
			rl_title_root.setVisibility(View.GONE);
		}

		if (6 == mWebType) {
			findViewById(R.id.mainpage).setVisibility(View.GONE);
			findViewById(R.id.layout).setVisibility(View.GONE);
			findViewById(R.id.back).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.mainpage).setVisibility(View.VISIBLE);
			findViewById(R.id.layout).setVisibility(View.VISIBLE);
			findViewById(R.id.back).setVisibility(View.VISIBLE);
		}

		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.refresh).setOnClickListener(this);
		findViewById(R.id.mainpage).setOnClickListener(this);

		dialog = new Dialog(mContext, R.style.MyDialogStyle);
		dialog.setContentView(R.layout.dialog);

		initWebView(mWebView);

	}

	/**
	 * 初始化webview
	 * 
	 * @param mWebView
	 *            Created by Administrator
	 */
	private void initWebView(WebView mWebView) {
		webViewSetting(mWebView);
		mWebView.setWebViewClient(new SubWebViewClient());
		mWebView.setWebChromeClient(new SubWebChromeClient());
		mWebView.addJavascriptInterface(new Handle(), JsCnative.mgwjs);
		mWebView.loadUrl(url);
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			String data = (String) msg.obj;
			if (data.contains("找不到网页")) {
				findViewById(R.id.error_layout).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.title)).setText("");
				return;
			}
			int pos = data.indexOf("<input name=\"parentPage\"");
			if (pos >= 0) {
				String back = data.substring(pos);
				pos = back.indexOf("value=\"");
				back = back.substring(pos + 7);
				pos = back.indexOf("\"");
				back = back.substring(0, pos);
				m_back_url = back;
			} else {
				if (mWebType != 1) {

				}
				// finish();
			}
		}
	};

	/**
	 * js控制类
	 * 
	 * @author huyan
	 */
	class Handle implements JsCnative {
		@JavascriptInterface
		public void showSource(String data) {
			Message msg = handler.obtainMessage(0, data);
			handler.sendMessage(msg);
		}

		@Override
		@JavascriptInterface
		public void getUserInfo() {
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					String aa = getSharedPreferences("mgw_data", 0).getString("mgw_data", "");

					mWebView.loadUrl("javascript:setUserInfo('" + aa + "')");
					LogUtils.i(TAG, "getUserInfo,aa=" + aa);
					// LogUtils.i(TAG, "getUserInfo,url=");
				}
			});
		}

		@Override
		@JavascriptInterface
		public void transferTo(final String url, String parm) {
			// TODO
			LogUtils.i(TAG, "transforTo,url=" + url + ",parm=" + parm);
			if (Utils.isFastDoubleClick()) {
				LogUtils.i(TAG, "不能快速点击");
				return;
			}

			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (url.contains("index.htm")) {

						InputTools.TimerHideKeyboard(mWebView);
						finish();
					}
				}
			});

		}

		@Override
		@JavascriptInterface
		public void setClientInfo(String groupid, String uid, String sid) {
			// TODO Auto-generated method stub
			LogUtils.i(TAG, "setClientInfo,json=" + groupid + "uid" + uid);

			SharedPreferences.Editor sharedata = getSharedPreferences("mgw_data", 0).edit();

			UserInfoBean bean = BaseApplication.getApplication().getBean();
			bean.item.groupid = groupid;
			bean.item.sid = sid;
			bean.item.uid = uid;
			String json = new Gson().toJson(bean);
			LogUtils.i(TAG, "setClientInfo,json=" + json.toString());

			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONObject jsonObject2 = jsonObject.getJSONObject("item");
				sharedata.putString("mgw_data", jsonObject2.toString());
				sharedata.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Utils.isFastDoubleClick()) {
				LogUtils.i(TAG, "不能快速点击");
				return;
			}
			UIUtils.runInMainThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					getLoginInfo();
					intent.putExtra("bind", true);
					setResult(1, intent);
					finish();
				}
			});

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

	public void getLoginInfo() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				LogUtils.i("otto", "GetLoginInfo_Thread");
				JSONObject loginInfo = UserUtils.getLoginInfo();
				if (loginInfo != null) {
					LogUtils.i("otto", "loginInfo." + loginInfo.toString());

					try {
						if (loginInfo.getInt("flag") == 0) {
							LogUtils.i("otto", "loginInfo." + loginInfo.toString());
							PreferenceHelper.getInstance(UIUtils.getContext()).setLoginInfo(loginInfo.toString());
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	class SubWebViewClient extends WebViewClient {
		// public boolean onJsAlert(WebView view, String url, String message,
		// JsResult result) {
		// return super.onJsAlert(view, url, message, result);
		// }
		// public boolean onJsConfirm(WebView view, String url,
		// String message, JsResult result) {
		// return super.onJsConfirm(view, url, message, result);
		// }
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			SubWebviewActivity.this.startActivityForResult(Intent.createChooser(intent, "File Chooser"), SubWebviewActivity.FILECHOOSER_RESULTCODE);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			openFileChooser(uploadMsg);
		}

		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			SubWebviewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), SubWebviewActivity.FILECHOOSER_RESULTCODE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.loadUrl("javascript:window.mgwjs.showSource(document.body.innerHTML);");
			// findViewById(R.id.error_layout).setVisibility(View.INVISIBLE);
			// findViewById(R.id.dialog).setVisibility(View.GONE);
			showOrDismissLoadingDialog(false);
			if (url.contains("buy:{")) {
				view.goBack();
				return;
			}

			if (url.contains("goto:login")) {
				view.goBack();
				return;
			}
			if (url.contains("addad:")) {
				view.goBack();
				return;
			}
			if (url.contains("editad:")) {
				view.goBack();
				return;
			}
			if (url.contains("chat:{")) {
				view.goBack();
				return;
			}
			if (url.contains("tel:")) {
				view.goBack();
				return;
			}
			if (url.toLowerCase().contains("supplierid:")) {
				view.goBack();
				return;
			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		@SuppressLint("NewApi")
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// // findViewById(R.id.dialog).setVisibility(View.GONE);
			// showOrDismissLoadingDialog(true);
			// if (url.contains("chat:{")) {
			// view.stopLoading();
			//
			// try {
			// JSONObject obj = new JSONObject(url.substring(5));
			//
			// Intent intent = new Intent(SubWebviewActivity.this,
			// ChatActivity.class);
			// intent.putExtra("userId", obj.getString("UserID"));
			// // intent.putExtra("Name",
			// // URLDecoder.decode(obj.getString("Name"), "utf-8"));
			// startActivity(intent);
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			//
			// return;
			// }
			//
			// if (url.contains("goto:login")) {
			// if (mShowExit)
			// return;
			// mShowExit = true;
			//
			// {
			// SharedPreferences.Editor sharedata =
			// getSharedPreferences("mgw_data", 0).edit();
			// sharedata.putString("mgw_data", "");
			// sharedata.putString("mgw_pwd", "");
			// sharedata.putString("mgw_account", "");
			// sharedata.commit();
			// // TODO
			// // // 百度推送取消绑定
			// // Utils.setBind(getApplicationContext(), false);
			// BaseApplication.getApplication().logout();
			//
			// startActivity(new Intent(SubWebviewActivity.this,
			// LoginActivity.class));
			// AppManager.getAppManager().finishActivity(MainActivity.class);
			// mShowExit = false;
			// }
			// return;
			// }
			//
			// if (url.toLowerCase().contains("supplierid:")) {
			// view.stopLoading();
			// // String sid = url.substring("supplierid:".length()).trim();
			// // Intent intent = new Intent(SubWebviewActivity.this,
			// // ShopDetailAndIntroduceActivity.class);
			// // intent.putExtra("sid", sid);
			// // startActivity(intent);
			// UIUtils.showToastSafe(TAG + "supplierid");
			// return;
			// }
			// if (url.contains("tel:")) {
			// view.stopLoading();
			// Utils.callMobliePhone(Uri.parse(url), false);
			// return;
			// }
			// // 广告添加页start
			// if (url.contains("addad:")) {
			// view.stopLoading();
			// view.reload();
			// Intent intent = new Intent(SubWebviewActivity.this,
			// AddadActivity.class);
			// intent.putExtra("mod", 1);
			// startActivity(intent);
			// return;
			// }
			// if (url.contains("editad:")) {
			// view.stopLoading();
			// view.reload();
			// Intent intent = new Intent(SubWebviewActivity.this,
			// AddadActivity.class);
			// intent.putExtra("adid", url.substring(7, url.length()));
			// intent.putExtra("mod", 2);
			// startActivity(intent);
			// return;
			// }
			// // 广告添加页end
			// if (url.contains("buy:{")) {
			// view.stopLoading();
			// if (finishPay) {
			// finishPay = false;
			//
			// try {
			// mOrderInfo = new JSONObject(url.substring(4));
			//
			// new
			// Builder(SubWebviewActivity.this).setTitle("选择支付方式").setItems(new
			// String[] { "微信支付", "支付宝支付" }, new
			// DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface arg0, int arg1) {
			// finishPay = true;
			// if (arg1 == 1) {
			// payOrder();
			// } else {
			//
			// mHandler.sendEmptyMessage(MESSAGE_TYPE_WXPAY);
			// }
			// }
			// }).setOnCancelListener(new OnCancelListener() {
			//
			// @Override
			// public void onCancel(DialogInterface dialog) {
			// finishPay = true;
			// }
			// }).show();
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// }
			// view.stopLoading();
			// return;
			// }
			//
			// // findViewById(R.id.dialog).setVisibility(View.VISIBLE);
			// showOrDismissLoadingDialog(true);

			// Intent intent = new Intent(ShopDetailAndIntroduceActivity.this,
			// GoodDetailActivity.class);
			// intent.putExtra("sid", sid);
			// intent.putExtra("pid", pid);
			// intent.putExtra("pname", pname);
			// startActivity(intent);

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			// showOrDismissLoadingDialog(true);
			
			if(mWebType==6){
				
				view.loadUrl(url);
				LogUtils.i(TAG, "mWebType"+mWebType);	
				return true;
			}
			
			if (url.contains("chat:{")) {
				view.stopLoading();

				try {
					JSONObject obj = new JSONObject(url.substring(5));

					Intent intent = new Intent(SubWebviewActivity.this, ChatActivity.class);
					intent.putExtra("userId", obj.getString("UserID"));
					UserFriendBean friendInfo2Bean = UserUtils.getFriendInfo2Bean(obj.getString("UserID"));

					if (friendInfo2Bean != null) {
						intent.putExtra("userName", friendInfo2Bean.getItems().get(0).getNickName());

					}
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return true;
			}

			if (url.contains("goto:login")) {
				view.stopLoading();
				if (mShowExit)
					return true;
				mShowExit = true;

				{
					// SharedPreferences.Editor sharedata =
					// getSharedPreferences("mgw_data", 0).edit();
					// sharedata.putString("mgw_data", "");
					// sharedata.putString("mgw_pwd", "");
					// sharedata.putString("mgw_account", "");
					// sharedata.commit();
					// TODO
					// // 百度推送取消绑定
					// Utils.setBind(getApplicationContext(), false);
					BaseApplication.getApplication().logout(mContext);
					//
					// startActivity(new Intent(SubWebviewActivity.this,
					// LoginActivity.class));
					// AppManager.getAppManager().finishActivity(MainActivity.class);
					mShowExit = false;
				}
				return true;
			}

			if (url.toLowerCase().contains("supplierid:")) {
				view.stopLoading();
				// String sid = url.substring("supplierid:".length()).trim();
				// Intent intent = new Intent(SubWebviewActivity.this,
				// ShopDetailAndIntroduceActivity.class);
				// intent.putExtra("sid", sid);
				// startActivity(intent);
				UIUtils.showToastSafe(TAG + "supplierid");
				return true;
			}
			if (url.contains("tel:")) {
				view.stopLoading();
				Utils.callMobliePhone(Uri.parse(url), false);
				return true;
			}
			// 广告添加页start
			if (url.contains("addad:")) {
				view.stopLoading();
				view.reload();
				Intent intent = new Intent(SubWebviewActivity.this, AddadActivity.class);
				intent.putExtra("mod", 1);
				startActivity(intent);
				return true;
			}
			if (url.contains("editad:")) {
				view.stopLoading();
				view.reload();
				Intent intent = new Intent(SubWebviewActivity.this, AddadActivity.class);
				intent.putExtra("adid", url.substring(7, url.length()));
				intent.putExtra("mod", 2);
				startActivity(intent);
				return true;
			}
			// 广告添加页end
			if (url.contains("buy:{")) {
				view.stopLoading();
				if (finishPay) {
					finishPay = false;

					try {
						mOrderInfo = new JSONObject(url.substring(4));

						new Builder(SubWebviewActivity.this).setTitle("选择支付方式").setItems(new String[] { "微信支付", "支付宝支付" }, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								finishPay = true;
								if (arg1 == 1) {
									payOrder();
								} else {
									mHandler.sendEmptyMessage(MESSAGE_TYPE_WXPAY);
								}
							}
						}).setOnCancelListener(new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								finishPay = true;
							}
						}).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				view.stopLoading();
				return true;
			}

			// findViewById(R.id.dialog).setVisibility(View.VISIBLE);
			view.loadUrl(url);
			showOrDismissLoadingDialog(true);
			return true;

		}
	}

	/**
	 * 调用加载or隐藏progrossDialog
	 */
	private void showOrDismissLoadingDialog(boolean ishow) {
		if (ishow) {

			if (dialog != null && !dialog.isShowing() && !isFinishing()) {
				dialog.show();
			}
		} else {

			if (dialog != null && dialog.isShowing() && !isFinishing()) {
				dialog.dismiss();
				dialog = null;
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		showOrDismissLoadingDialog(false);
		if(mWebView!=null){
			mWebView.removeAllViews();
			mWebView.destroy();
		}
	}

	class SubWebChromeClient extends WebChromeClient {
		// 关键代码，以下函数是没有API文档的，所以在Eclipse中会报错，如果添加了@Override关键字在这里的话。
		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {

			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			// i.setType("");
			SubWebviewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

		}

		// For Android 3.0+
		public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			SubWebviewActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
		}

		// For Android 4.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			// showImgDialog();
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			LogUtils.i(TAG + "ANDROID_LAB", "TITLE=" + title);
			((TextView) findViewById(R.id.title)).setText(title);
		}

	}

	boolean mShowExit = false;

	/**
	 * 退出登录
	 * 
	 */
	protected void showDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("是否退出该账号");
		builder.setTitle("美顾问");
		builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// SharedPreferences.Editor sharedata =
				// getSharedPreferences("mgw_data", 0).edit();
				// sharedata.putString("mgw_data", "");
				// sharedata.putString("mgw_pwd", "");
				// sharedata.putString("mgw_account", "");
				// sharedata.commit();
				// TODO
				BaseApplication.getApplication().logout(mContext);

				// ((GlobelElements) getApplicationContext()).m_user_id = "";
				// startActivity(new Intent(SubWebviewActivity.this,
				// LoginActivity.class));
				// finish();
				// AppManager.getAppManager().finishActivity(MainActivity.class);
				mShowExit = false;
			}
		});

		builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mShowExit = false;
			}
		});
		builder.create().show();
	}

	boolean finishPay = true;
	JSONObject mOrderInfo;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_TYPE_NOWX:

				UIUtils.showToastWithAlertPic("微信未安装或版本不支持微支付，请检查");
				break;
			case MESSAGE_TYPE_PARAMERR:
				UIUtils.showToastWithAlertPic("当前版本不支持，请检查");
				break;
			case MESSAGE_TYPE_WXPAY:
				PaydWX();
				break;
			default:
				finishPay = true;
				break;
			}

		}
	};

	private Dialog createLoadingDialog;

	private Dialog dialog;

	/**
	 * 微信支付
	 * 
	 */
	void PaydWX() {
		try {
			IWXAPI api = WXAPIFactory.createWXAPI(this, Define_C.APP_ID);
			if (!api.isWXAppInstalled() || !api.isWXAppSupportAPI()) {
				UIUtils.showToastWithAlertPic("微信未安装或版本不支持微支付，请检查");
				return;
			}
			JSONObject item = new JSONObject();
			item.put("trade_no", mOrderInfo.getString("out_trade_no"));
			item.put("amount", mOrderInfo.getString("total_fee"));
			String strpname = null;
			try {
				strpname = URLDecoder.decode(mOrderInfo.getString("subject"), "UTF-8").replaceAll(" ", "");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			SharedPreferences sp = getSharedPreferences("mgw_data", 0);
			if (mOrderInfo.getInt("Notetype") == 0) {
				item.put("notify_url", sp.getString("wz_tenpay", "http://pay.mgw.cc/WZ/tenpay_notify_url.aspx"));
				// item.put("pname", "美顾问城市联盟订单");

				item.put("pname", strpname);
			} else if (mOrderInfo.getInt("Notetype") == 1) {
				item.put("notify_url", sp.getString("xx_tenpay", "http://pay.mgw.cc/XX/tenpay_notify_url.aspx"));
				// item.put("pname", "美顾问城市联盟店内消费");

				item.put("pname", strpname);
			} else if (mOrderInfo.getInt("Notetype") == 2) {
				item.put("notify_url", sp.getString("mall_tenpay", "http://pay.mgw.cc/Mall/tenpay_notify_url.aspx"));
				// item.put("pname", "美顾问品牌商城订单");

				item.put("pname", strpname);
			} else {
				mHandler.sendEmptyMessage(MESSAGE_TYPE_PARAMERR);
				return;
			}
			new WXPayRequest1(SubWebviewActivity.this, item).WXPay();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 支付订单
	 * 
	 */
	void payOrder() {
		try {
			String info = getNewOrderInfo();
			if (info.equals("")) {
				return;
			}
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			final String orderInfo = info;
			new Thread() {
				@Override
				public void run() {
					AliPay alipay = new AliPay(SubWebviewActivity.this, mHandler);
					String result = alipay.pay(orderInfo);
					Message msg = new Message();
					msg.what = 0;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();
		} catch (Exception ex) {
		}
	}

	private String getSignType() {

		return "sign_type=\"RSA\"";

	}

	/**
	 * 获得新订单的信息
	 * 
	 * @return
	 * @throws JSONException
	 */
	private String getNewOrderInfo() throws JSONException {
		double fee = mOrderInfo.getDouble("total_fee");
		String strFee = String.format("%.2f", fee);

		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(mOrderInfo.getString("out_trade_no"));

		SharedPreferences sp = getSharedPreferences("mgw_data", 0);
		if (mOrderInfo.getInt("Notetype") == 0) {
			sb.append("\"&subject=\"");
			sb.append(mOrderInfo.getString("subject"));
			// sb.append("美顾问城市联盟订单");
			sb.append("\"&body=\"");
			sb.append(mOrderInfo.getString("subject"));
			// sb.append("美顾问城市联盟订单");
			sb.append("\"&total_fee=\"");
			sb.append(strFee);
			sb.append("\"&notify_url=\"");
			sb.append(URLEncoder.encode(sp.getString("wz_alipay", "http://pay.mgw.cc/WZ/alipay_notify_url.aspx")));
			sb.append("\"&service=\"mobile.securitypay.pay");
			sb.append("\"&_input_charset=\"UTF-8");
			sb.append("\"&return_url=\"");
			sb.append(URLEncoder.encode(sp.getString("wz_alipay_return_url", "http://pay.mgw.cc/WZ/alipay_return_url.aspx")));

		} else if (mOrderInfo.getInt("Notetype") == 1) {
			sb.append("\"&subject=\"");
			sb.append(mOrderInfo.getString("subject"));
			// sb.append("美顾问城市联盟店内消费");
			sb.append("\"&body=\"");
			sb.append(mOrderInfo.getString("subject"));
			// sb.append("美顾问城市联盟店内消费");
			sb.append("\"&total_fee=\"");
			sb.append(strFee);
			sb.append("\"&notify_url=\"");
			sb.append(URLEncoder.encode(sp.getString("xx_alipay", "http://pay.mgw.cc/XX/alipay_notify_url.aspx")));
			sb.append("\"&service=\"mobile.securitypay.pay");
			sb.append("\"&_input_charset=\"UTF-8");
			sb.append("\"&return_url=\"");
			sb.append(URLEncoder.encode(sp.getString("xx_alipay_return_url", "http://pay.mgw.cc/XX/alipay_return_url.aspx")));
		} else if (mOrderInfo.getInt("Notetype") == 2) {
			sb.append("\"&subject=\"");
			sb.append(mOrderInfo.getString("subject"));
			// sb.append("美顾问品牌商城订单");
			sb.append("\"&body=\"");
			sb.append(mOrderInfo.getString("subject"));
			// sb.append("美顾问品牌商城订单");
			sb.append("\"&total_fee=\"");
			sb.append(strFee);
			sb.append("\"&notify_url=\"");
			sb.append(URLEncoder.encode(sp.getString("mall_alipay", "http://pay.mgw.cc/Mall/alipay_notify_url.aspx")));
			sb.append("\"&service=\"mobile.securitypay.pay");
			sb.append("\"&_input_charset=\"UTF-8");
			sb.append("\"&return_url=\"");
			sb.append(URLEncoder.encode(sp.getString("mall_alipay_return_url", "http://pay.mgw.cc/Mall/alipay_return_url.aspx")));

		} else {
			mHandler.sendEmptyMessage(MESSAGE_TYPE_PARAMERR);
			return "";
		}
		// 网址需要做URL编码
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// findViewById(R.id.dialog).setVisibility(View.GONE);
		showOrDismissLoadingDialog(false);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// mWebView.reload();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// LinearLayout layout = (LinearLayout)
			// findViewById(R.id.share_layout);
			// layout.setVisibility(View.INVISIBLE);
			if (url.contains("bind")) {
				UIUtils.showToastSafe("需要绑定店铺");
				return true;
			}
			if (mWebType == 1 || mWebType == 6) {
				if (mWebView.canGoBack()) {
					// 返回键退回
					mWebView.goBack();
					return true;
				} else {
					finish();
					return true;
				}
			}
			if (m_back_url.length() > 0) {
				mWebView.loadUrl(m_back_url);
				return true;
			}

			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.refresh:
			mWebView.loadUrl(m_back_url);

			break;
		case R.id.back:

			if (mWebType == 1 || mWebType == 6) {
				InputTools.TimerHideKeyboard(mWebView);
				if (mWebView.canGoBack()) {
					// 返回键退回
					mWebView.goBack();
					return;
				} else {
					finish();
					return;
				}
			}
			if (m_back_url.length() > 0) {
				mWebView.loadUrl(m_back_url);
				return;
			}

			finish();
			break;
		case R.id.mainpage:
			InputTools.TimerHideKeyboard(mWebView);
			finish();
			break;
		}
	}

	/**
	 * webview设置
	 * 
	 * @param webview
	 *            Created by huyan
	 */
	protected void webViewSetting(final WebView webview) {
		WebSettings settings = webview.getSettings();
		// 适应屏幕
		// settings.setUseWideViewPort(true);
		// settings.setSupportZoom(true);
		// settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		settings.setJavaScriptEnabled(true);
		settings.setLoadWithOverviewMode(true);
		// TODO
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);

		// webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webview.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) { // 表示按返回键
						webview.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});
		webview.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

}