package com.mgw.member.ui.fragment;

import java.io.File;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.hx.hxchat.activity.AlertDialog;
import com.hx.hxchat.utils.CommonUtils;
import com.mgw.member.R;
import com.mgw.member.bean.UserInfoBean;
import com.mgw.member.constant.Define_C;
import com.mgw.member.factory.MgwWebViewFactory;
import com.mgw.member.http.Http;
import com.mgw.member.interfaces.initZxingListener;
import com.mgw.member.js.dao.JsCnative;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.manager.UpdateManager;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ottoEvent.MallFragmentNeedRefreshEvent;
import com.mgw.member.ottoEvent.ReGetLoginInfoEvent;
import com.mgw.member.ui.activity.ChangeSkinActivity;
import com.mgw.member.ui.activity.MGWBaseActivity;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.ui.activity.OrderFinishActivity;
import com.mgw.member.ui.activity.PayTypeActivity;
import com.mgw.member.ui.activity.PayingActivity;
import com.mgw.member.ui.activity.SaomadengluActivity;
import com.mgw.member.ui.activity.SubWebviewActivity;
import com.mgw.member.ui.activity.UpdateAlertDialog;
import com.mgw.member.ui.activity.cityleague.CityleagueActivity;
import com.mgw.member.ui.fragment.HomeFragment.Handle;
import com.mgw.member.ui.fragment.HomeFragment.MyWebChromeClient_Home;
import com.mgw.member.ui.fragment.HomeFragment.MyWebViewClient_Home;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.NetworkProber;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.Utils;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.zxing.activity.CaptureActivity;

/**
 * 
 * @author huyan Create On 2015/2/10. description:所有的html展示页面由这个fragment显装载
 */
public class FindFragment extends BaseFragment {

	// private String defaultUrl = "file:///android_asset/wenquan/index.html";
	/** 在这里改了html的读取路径 */
	private String defaultUrl = Define_C.FIND_URL;
	private final String TAG = FindFragment.class.getSimpleName().toString();

	private MgwWebViewFactory mWebViewFactory;
	/**
	 * webview容器
	 */
	private FrameLayout fl_webviews;
	private RelativeLayout errorPage;
	private String currentUrl = "";
	private final String parentPageValue = "";

	private FrameLayout fl_root;
	private final boolean isContainParentPageValue = false;
	public Boolean isErrorPage;
	private int currentLoadProgress;

	private WebView webview = null;

	// <input name="parentPage" />
	@SuppressLint({ "NewApi", "JavascriptInterface" })
	@Override
	public View initView(LayoutInflater inflater) {
		LogUtils.i(TAG + "initView");
		View view = UIUtils.inflate(R.layout.fragment_find);
		webview = (WebView) view.findViewById(R.id.webView);
		errorPage = (RelativeLayout) view.findViewById(R.id.rl_error);
		// view.findViewById(R.id.refresh).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_REFRESH);
		// }
		// });
		webview.addJavascriptInterface(new Handle(), JsCnative.mgwjs);//
		// 处理android调用js方法
		webViewSetting(webview, new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) { //
						webview.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});
		webview.setWebViewClient(new MyWebViewClient_Base());
		webview.setWebChromeClient(new MyWebChromeClient_Base());
		// String dd=defaultUrl.substring(8);
		// if(FileUtils.dirIsExists(dd)){
		//
		webview.loadUrl(defaultUrl);
		//
		// }else{
		//
		//
		// }
		initClick(view);
		return view;
	}

	private void initClick(View view) {
		view.findViewById(R.id.btn_scan).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CaptureActivity.class);
				startActivityForResult(intent, 0);

			}
		});
		view.findViewById(R.id.btn_call).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Utils.callMobliePhone("10010", false);

				Intent intent = new Intent(context, UpdateAlertDialog.class);
				intent.putExtra("fv_exp", "dddd");
				intent.putExtra("FVersion_Name", "123");
				intent.putExtra("FVersion_FileURL", "http://file.mgw.cc//Update/2015/4/24/MgwMembertest.apk");
				context.startActivity(intent);

			}
		});
		view.findViewById(R.id.btn_exit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// BaseApplication.getApplication().logout(context);

				// ReGetLoginInfoEvent loginInfohEvent = getLoginInfohEvent();
				// loginInfohEvent.setNeedGet(true);
				// BusProvider.getInstance().post(loginInfohEvent);
			}
		});
		view.findViewById(R.id.btn_changeskin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* 下载的apk文件的保存位置 */
				// String savePath =
				// Environment.getExternalStorageDirectory().getPath() + "/mgw"
				// + "/version.txt";
				// String savePath1 =
				// Environment.getExternalStorageDirectory().getPath() + "/mgw"
				// + "/version1.txt";
				// String savePath12=
				// Environment.getExternalStorageDirectory().getPath() + "/mgw"
				// + "/version3.txt";
				// // String readProperties;
				// // try {
				// // readProperties = FileUtils.readText(savePath, "version",
				// "0");
				// // } catch (JSONException e) {
				// // // TODO Auto-generated catch block
				// // e.printStackTrace();
				// // }
				// //
				//
				// FileUtils.writeProperties(savePath1, "version", "1111",
				// "banb");
				// String readProperties = FileUtils.readProperties(savePath12,
				// "liuxiang", "jiji0");
				//
				// LogUtils.i(TAG, "readProperties,=" + readProperties);
				// // {version: "1.x.x"}
				// new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// File downLoad =
				// Utils.downLoad("http://file.mgw.cc//Update/2015/4/28/html.zip",
				// UpdateManager.htmlsavePath, "html.zip",null);
				// }
				// }).start();
				String htmlcode = "1.0.2";
				String apkVersionname = "1.0.3";
				String hehe = (htmlcode.compareTo(apkVersionname) > 0 ? htmlcode : apkVersionname);
				LogUtils.i(TAG, "readProperties,=" + hehe);
			}
		});
		view.findViewById(R.id.btn_cityleague).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CityleagueActivity.class);
				startActivity(intent);

			}
		});
	}

	/**
	 * js消息处理handler
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			if (msg.what == MESSAGE_TYPE_WEBPAGER_ERROR) {

				// Toast.makeText(context, "errorpage", 0).show();
				errorPage.setVisibility(View.VISIBLE);
			} else if (msg.what == MESSAGE_TYPE_WEBPAGER_REFRESH) {
				// TODO
				webview.loadUrl(currentUrl);
			} else if (msg.what == MESSAGE_TYPE_WEBPAGER_OK) {
				errorPage.setVisibility(View.GONE);
			}
		}

	};

	/**
	 * js操作本地接口类
	 * 
	 * @author huyan
	 * 
	 */
	public class Handle implements JsCnative {
		@JavascriptInterface
		public void showSource(final String data) {
			Map<String, Boolean> map = parseHtmlForIsRootPage(data);
			// isContainParentPageValue = map.get("parentPage");
			isErrorPage = map.get("errorPage");
			LogUtils.i(TAG, "showSource,aa=" + data);
			if (NetworkProber.isNetworkAvailable(context) && !isErrorPage) {
				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_OK);
			} else {
				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_ERROR);
			}
			// Toast.makeText(context, "errorpage1", 0).show();
		}

		@Override
		@JavascriptInterface
		public void getUserInfo() {
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {

					String aa = getActivity().getSharedPreferences("mgw_data", 0).getString("mgw_data", "");

					webview.loadUrl("javascript:setUserInfo('" + aa + "')");
					LogUtils.i(TAG, "getUserInfo,aa=" + aa);

				}
			});
			return;
		}

		@JavascriptInterface
		public void transferTo(String url, String parm) {

			LogUtils.i(TAG, "transforTo,url=" + url + ",parm=" + parm);
			// final String url1 =
			// "file:///storage/emulated/0/mgw/unziphtml/html/" + url;
			final String url1 = "file://" + FileUtils.getHomeHtmlDir() + url;

			// Utils.transforTo(getActivity(),url,parm,1);
			UIUtils.runInMainThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					if (!NetworkProber.isNetworkAvailable(context)) {
						UIUtils.showToastSafe("网络不可用");
						return;
					}
					Intent intent = new Intent(context, SubWebviewActivity.class);
					intent.putExtra("url", url1);
					intent.putExtra("type", 1);
					intent.putExtra("title", false);
					// intent.putExtra("parm", parm == null ? "" : parm);
					startActivityForResult(intent, 1);

				}
			});

		}

		@Override
		@JavascriptInterface
		public void setClientInfo(String groupid, String uid, String sid) {
			if (!NetworkProber.isNetworkAvailable(context)) {
				UIUtils.showToastSafe("网络不可用");
				return;
			}
			SharedPreferences.Editor sharedata = getActivity().getSharedPreferences("mgw_data", 0).edit();

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

		}

		@Override
		@JavascriptInterface
		public void toConsulter(String userid) {

			if ((MGWBaseActivity) getActivity() != null) {
				((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
			}
			if (!NetworkProber.isNetworkAvailable(context)) {
				UIUtils.showToastSafe("网络不可用");
				return;
			}
			// TODO Auto-generated method stub
			LogUtils.i(TAG, "toConsulter,userid=" + userid);
			if ("".equals(userid)) {
				return;
			}
			Utils.toConsulterHX(getActivity(), userid);
		}

		@Override
		@JavascriptInterface
		public void toSupplier(String sid) {

			// TODO Auto-generated method stub
			// BigDecimal db = new BigDecimal(sid);
			// LogUtils.i(TAG, "toSupplier,sid="+sid +",BigDecimal"+
			// db.toPlainString());
			if (!NetworkProber.isNetworkAvailable(context)) {
				UIUtils.showToastSafe("网络不可用");
				return;
			}
			if ("".equals(sid)) {
				Utils.toSupplier(getActivity());
			} else {
				sid = sid.replace("\"", "");
				Utils.toSupplier(getActivity(), sid);
			}

		}

		@Override
		@JavascriptInterface
		public void buyInSupplier(String sid, String pid) {
			// TODO Auto-generated method stub
			if (!NetworkProber.isNetworkAvailable(context)) {
				UIUtils.showToastSafe("网络不可用");
				return;
			}
			if ((MGWBaseActivity) getActivity() != null) {
				((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
			}
			LogUtils.i(TAG, "buyInSupplier,sid=" + sid + ",pid=" + pid);
			Utils.buyInSupplier(getActivity(), sid, pid);
		}

		@JavascriptInterface
		public void toScan() {
			// TODO Auto-generated method stub
			LogUtils.i(TAG, "toScan");
			UIUtils.runInMainThread(new Runnable() {

				@Override
				public void run() {
					if ((MGWBaseActivity) getActivity() != null) {
						((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
					}
					startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 1);
				}
			});

		}

		@JavascriptInterface
		public void toAds(String dd) {
			if (!NetworkProber.isNetworkAvailable(context)) {
				UIUtils.showToastSafe("网络不可用");
				return;
			}
			Utils.toAds(getActivity(), dd);

		}

	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showTwoPage(WebView view, String url) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideErrorPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void open2OhterActivitye(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void openWebview(WebView view, String url) {
		// TODO Auto-generated method stub

	}

	private initZxingListener zxingListener;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			String str = data.getStringExtra(CaptureActivity.FORRESULT);
			if (str != null) {
				forResultZxing(str, context, mHandler);
			}

		}

	}

	/**
	 * 处理二维码扫返回
	 * 
	 * @param str
	 */
	private void forResultZxing(String str, Context context, final Handler mHandler) {
		if (str.equals("first")) {

		} else if (str.equals("Second")) {

		} else {
			if (str.indexOf("http://m.mgw.cc") == -1) {
				Toast.makeText(context, "二维码扫描失败,非美顾问专用二维码", Toast.LENGTH_LONG).show();
			} else if (str.contains("guid=")) {
				Intent intent = new Intent(context, SaomadengluActivity.class);
				intent.putExtra("str", str);
				context.startActivity(intent);
			} else if (str.contains("addfriend=")) {
				final String userid = str.substring(26, str.length());
				if (BaseApplication.getApplication().getUserName().equals(userid)) {
					context.startActivity(new Intent(context, AlertDialog.class).putExtra("msg", "不能添加自己"));
					return;
				}

				if (BaseApplication.getApplication().getContactList().containsKey(userid)) {
					context.startActivity(new Intent(context, AlertDialog.class).putExtra("msg", "此用户已是你的好友"));
					return;
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							EMContactManager.getInstance().addContact(userid, "加个好友吧");
							Message msg = new Message();
							msg.what = 2;
							mHandler.sendMessage(msg);

						} catch (EaseMobException e) {
							Message msg = new Message();
							msg.what = 3;
							mHandler.sendMessage(msg);
						}

					}
				}).start();

			} else {
				try {
					Define_C.s_shopingId = str.substring(20, str.indexOf("&oid"));
					Define_C.s_old = str.substring(str.indexOf("&oid") + 5);
					Message message = Message.obtain();
					message.what = MESSAGE_TYPE_SHOPING_ID;
					message.obj = null;
					mHandler.sendMessage(message);
				} catch (Exception ex) {
					Toast.makeText(context, "访问服务器端失败，请重新扫码", Toast.LENGTH_LONG).show();
				}
			}
		}

	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MESSAGE_TYPE_SHOPING_ID:

				// {
				// "item": {
				// "fmbid": "140819170100000003",
				// "suppliername": "17.5经典院线",
				// "supplierlogo":
				// "http://app.mgw.cc/Member/Images/avatar-100.png",
				// "discount": 0.3300,
				// "backsafe": 0.1000,
				// "oid": "140806170900000004"
				// },
				// "flag": 0,
				// "msg": "挂单成功",
				// "type": "user.getbill",
				// "telephone": null
				// }
				ShowNote();

				break;
			case 2:
				Toast.makeText(getActivity(), "好友添加请求发送成功！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getActivity(), "好友添加请求发送失败！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	void ShowNote() {
		new Thread() {
			@Override
			public void run() {
				try {
					JSONObject t_JsonObject;
					t_JsonObject = Http.postShoping(getActivity().getSharedPreferences("mgw_data", 0).getString("mgw_userID", null), Define_C.s_shopingId, Define_C.s_old);
					double t_num;
					double t_num2;
					if (t_JsonObject == null) {
						Toast.makeText(getActivity(), "网络异常，请重新扫码！", Toast.LENGTH_SHORT).show();
						return;

					}
					if (t_JsonObject.getInt("flag") == 0) {// 成功
						JSONArray t_JsonArray = t_JsonObject.getJSONArray("items");
						t_JsonObject = t_JsonArray.getJSONObject(0);
						// 140725114100000002,140818121000000000
						Intent intent;
						switch (t_JsonObject.getInt("fmbstatus")) {
						case 0:

							intent = new Intent(getActivity(), PayingActivity.class);

							intent.putExtra("suppliername", t_JsonObject.getString("suppliername"));

							t_num = Double.valueOf(t_JsonObject.getString("discount"));
							t_num2 = CommonUtils.round((t_num), 1);

							// 将折扣存在本地数据库中
							// DiscountDao dao=new
							// DiscountDao(CameraTestActivity.this);
							// dao.save(t_JsonObject.getString("123456789"),
							// String.valueOf(t_num2));
							Define_C.s_discount = String.valueOf(t_num2);
							Define_C.s_shopingName = t_JsonObject.getString("suppliername");

							intent.putExtra("discount", String.valueOf(t_num2) + "折");

							t_num = Double.valueOf(t_JsonObject.getString("backsafe"));

							intent.putExtra("backsafe", String.valueOf((int) (t_num * 100)) + "%");
							Define_C.s_return_money = String.valueOf((int) (t_num * 100)) + "%";
							intent.putExtra("ShopingId", Define_C.s_shopingId);
							Define_C.s_orderId = t_JsonObject.getString("fmbid");
							startActivity(intent);

							break;
						case 1:
							t_num = Double.valueOf(t_JsonObject.getString("discount"));
							t_num2 = CommonUtils.round((t_num), 1);

							// 将折扣存在本地数据库中
							// DiscountDao dao=new
							// DiscountDao(CameraTestActivity.this);
							// dao.save(t_JsonObject.getString("123456789"),
							// String.valueOf(t_num2));
							Define_C.s_discount = String.valueOf(t_num2);
							t_num = Double.valueOf(t_JsonObject.getString("backsafe"));
							Define_C.s_shopingName = t_JsonObject.getString("suppliername");
							Define_C.s_return_money = String.valueOf((int) (t_num * 100)) + "%";
							Define_C.s_orderId = t_JsonObject.getString("fmbid");
							if (t_JsonObject.getInt("fmbpayment") == 2) {
								intent = new Intent(getActivity(), OrderFinishActivity.class);
								Define_C.s_orderId = t_JsonObject.getString("fmbid");
								intent.putExtra("fmbid", Define_C.s_orderId);
								intent.putExtra("type", (byte) 1);

								startActivity(intent);

								return;
							}

							intent = new Intent(getActivity(), PayTypeActivity.class);
							intent.putExtra("OrderId", t_JsonObject.getString("fmbid"));
							startActivity(intent);

							break;
						// case 2:
						default:

							t_num = Double.valueOf(t_JsonObject.getString("discount"));
							t_num2 = CommonUtils.round((t_num), 1);

							// 将折扣存在本地数据库中
							// DiscountDao dao=new
							// DiscountDao(CameraTestActivity.this);
							// dao.save(t_JsonObject.getString("123456789"),
							// String.valueOf(t_num2));
							Define_C.s_discount = String.valueOf(t_num2);
							t_num = Double.valueOf(t_JsonObject.getString("backsafe"));
							Define_C.s_shopingName = t_JsonObject.getString("suppliername");
							Define_C.s_return_money = String.valueOf((int) (t_num * 100)) + "%";

							intent = new Intent(getActivity(), OrderFinishActivity.class);
							Define_C.s_orderId = t_JsonObject.getString("fmbid");
							switch (t_JsonObject.getInt("fmbpayment")) {
							case 1:
								intent.putExtra("type", (byte) 0);
								break;
							case 2:
								intent.putExtra("type", (byte) 1);
								break;
							}
							startActivity(intent);
							break;
						}
					} else {
						Message message = Message.obtain();
						message.what = 4;
						message.obj = t_JsonObject.getString("msg");
						mHandler.sendMessage(message);
						Toast.makeText(getActivity(), t_JsonObject.getString("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public class MyWebViewClient_Base extends BaseWebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			// view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
			// 通过内部类定义的方法获取html页面加载的内容，这个需要添加在webview加载完成后的回调中
			view.loadUrl("javascript:window.mgwjs.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			super.onPageFinished(view, url);
			// dismissLoadingDialog();
			dismissLoadingDialog();
			LogUtils.i("onPageFinished=" + "url=" + url);
			MainActivity.mainActivity.findViewById(R.id.rl_error).setVisibility(View.INVISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			if (!NetworkProber.isNetworkAvailable(context)) {
				UIUtils.showToastSafe("网络不可用");
				view.stopLoading();
				return;
			}
			if ((MGWBaseActivity) getActivity() != null) {
				((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
			}

			currentUrl = url;
			if (url.contains("goto:login")) {
				if (mShowExit)
					return;
				mShowExit = true;
				{

					BaseApplication.getApplication().logout(context);

					mShowExit = false;

				}
				return;
			}
			showLoadingDialog(getActivity(), HomeFragment.class);
			return;

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub

			if (!NetworkProber.isNetworkAvailable(context)) {
				UIUtils.showToastSafe("网络不可用");
				view.stopLoading();
				return true;
			} else {
				view.loadUrl(url);
			}

			return true;

		}

	}

	public class MyWebChromeClient_Base extends BaseWebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			currentLoadProgress = newProgress;
			if (newProgress > 49) {

			}
			LogUtils.i(TAG + "home_onProgressChanged=newProgres=" + newProgress);
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// BusProvider.getInstance().register(this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// BusProvider.getInstance().unregister(this);
	}

	// @Produce
	// public ReGetLoginInfoEvent getLoginInfohEvent() {
	// LogUtils.i("otto" + "ReGetLoginInfoEvent=newProgres=");
	// ReGetLoginInfoEvent mallFragmentNeedRefreshEvent = new
	// ReGetLoginInfoEvent();
	// return mallFragmentNeedRefreshEvent;
	// }

}
