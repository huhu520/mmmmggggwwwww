package com.mgw.member.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.mgw.member.js.dao.JsCnative;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.MGWBaseActivity;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.ui.activity.OrderFinishActivity;
import com.mgw.member.ui.activity.PayTypeActivity;
import com.mgw.member.ui.activity.PayingActivity;
import com.mgw.member.ui.activity.SaomadengluActivity;
import com.mgw.member.ui.activity.SubWebviewActivity;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.NetworkProber;
import com.mgw.member.uitls.StringUtils;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.Utils;
import com.zxing.activity.CaptureActivity;

/**
 * 
 * @author huyan Create On 2015/2/10. description:所有的html展示页面由这个fragment显装载
 */
@SuppressLint({ "JavascriptInterface", "HandlerLeak", "SetJavaScriptEnabled" })
public class HomeFragment extends BaseFragment implements OnClickListener {
	private final String TAG = HomeFragment.class.getSimpleName().toString();
	private int currentLoadProgress = 0;
	private final String defaultUrl = Define_C.HOME_URL;
	private WebView webview = null;
	private MgwWebViewFactory mWebViewFactory;
	private RelativeLayout errorPage;
	private boolean isContainParentPageValue = false;
	public Boolean isErrorPage;
	private String currentUrl = "";
	
	private TextView top_title;
	private String pre_top_title;
	private String cur_top_title;
	private TextView mainpage;
	private TextView back;
	
	
	
	private HashMap<String, String>  loadurlmap;

	private void addloadurlentry(String url,String title){
		if(loadurlmap==null){
			loadurlmap=new HashMap<>();
		}
		
		if(!loadurlmap.containsKey(url)){
			loadurlmap.put(url, title);
		}
	}
	private String getloadurlentry(String url){
		
		if(loadurlmap!=null&&loadurlmap.containsKey(url)){
			return loadurlmap.get(url);
		}
		return null;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		LogUtils.i(TAG + "initView");
		View view = inflater.inflate(R.layout.fragment_home, null);
		mWebViewFactory = MgwWebViewFactory.getInstance(context);
		// alertDialog = new AlertDialog.Builder(getActivity()).create();
		webview = (WebView) view.findViewById(R.id.webView);

		top_title = (TextView) view.findViewById(R.id.title);
		top_title.setVisibility(View.VISIBLE);
		back = (TextView) view.findViewById(R.id.back);
		back.setVisibility(View.GONE);
		back.setOnClickListener(this);
		
		mainpage = (TextView) view.findViewById(R.id.mainpage);
		mainpage.setBackgroundResource(R.drawable.sao);
		mainpage.setVisibility(View.VISIBLE);
		mainpage.setOnClickListener(this);

		errorPage = (RelativeLayout) view.findViewById(R.id.rl_error);
		view.findViewById(R.id.refresh).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_REFRESH);
			}
		});
		webview.addJavascriptInterface(new Handle(), JsCnative.mgwjs);// 处理android调用js方法
		webview.requestFocus();
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		webViewSetting(webview, new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) { //
						webview.goBack(); // 后退
						LogUtils.i(TAG, "goBack=" + "title=" + webview.getTitle());
						return true; // 已处理
					}
				}
				return false;
			}
		});
		webview.setWebViewClient(new MyWebViewClient_Home());
		webview.setWebChromeClient(new MyWebChromeClient_Home());
		webview.loadUrl(defaultUrl);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i(TAG + "onResume=currentLoadProgress=" + currentLoadProgress);
	}

	@Override
	public void initData() {

	}

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

			isContainParentPageValue = map.get("parentPage");
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

		@Override
		@JavascriptInterface
		public void transferTo(String url, String parm) {
			if(Utils.isFastDoubleClick()){
				LogUtils.i(TAG, "不能快速点击");
				return;
			}
			LogUtils.i(TAG, "transforTo,url=" + url + ",parm=" + parm);
			// final String url1 =
			// "file:///storage/emulated/0/mgw/unziphtml/html/" + url;
			final String url1 = "file://" + FileUtils.getHomeHtmlDir() + url;

			// Utils.transforTo(getActivity(),url,parm,1);
			UIUtils.runInMainThread(new Runnable() {

				@Override
				public void run() {
					if (!NetworkProber.isNetworkAvailable(context)) {
						UIUtils.showToastSafe("网络不可用");
						return;
					}
					Intent intent = new Intent(context, SubWebviewActivity.class);
					intent.putExtra("url", url1);
					intent.putExtra("type", 1);
					intent.putExtra("title", true);
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
			if(Utils.isFastDoubleClick()){
				LogUtils.i(TAG, "不能快速点击");
				return;
			}
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
			if(Utils.isFastDoubleClick()){
				LogUtils.i(TAG, "不能快速点击");
				return;
			}
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
			if(Utils.isFastDoubleClick()){
				LogUtils.i(TAG, "不能快速点击");
				return;
			}
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

	/**
	 * js消息处理handler
	 */
	private final Handler handler = new Handler() {
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

	@Override
	public void onHiddenChanged(boolean hidden) {
		String aa = getActivity().getSharedPreferences("mgw_data", 0).getString("mgw_data", "");

	};

	public class MyWebViewClient_Home extends BaseWebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			// view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
			// 通过内部类定义的方法获取html页面加载的内容，这个需要添加在webview加载完成后的回调中
			view.loadUrl("javascript:window.mgwjs.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			super.onPageFinished(view, url);
			// dismissLoadingDialog();
			dismissLoadingDialog();
			LogUtils.i("onPageFinished=" + "url=" + url+ "title=" + view.getTitle());
			addloadurlentry(url, view.getTitle());
			MainActivity.mainActivity.findViewById(R.id.rl_error).setVisibility(View.INVISIBLE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			if (!NetworkProber.isNetworkAvailable(context)) {
				UIUtils.showToastSafe("网络不可用");
				view.stopLoading();
				return;
			}
			String getloadurlentry = getloadurlentry(url);
			
			LogUtils.i(TAG, "onPageStarted=" + "title=" + view.getTitle()+"getloadurlentry="+getloadurlentry);
			if(getloadurlentry!=null){
				top_title.setText(getloadurlentry);
			}
			
			showAndHideBack(url!=null&&url.contains("index.ht"));
			
			
			
			
			
			if ((MGWBaseActivity) getActivity() != null) {
				((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
			}

			
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
			currentUrl = url;
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

	public class MyWebChromeClient_Home extends BaseWebChromeClient {

		// @Override
		// // 弹出警告框操作
		// public boolean onJsAlert(WebView view, String url, String message,
		// final JsResult result) {
		// Dialog dialog = new
		// AlertDialog.Builder(context).setMessage(message).setPositiveButton(android.R.string.ok,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// result.cancel();
		// // 这里我们通过Webview.loadUrl()方法去调用js中的函数
		// webview.loadUrl("javacript:java_call()");
		// }
		// }).create();
		// dialog.setCancelable(false);// 避免点Back取消，那样js接收不到任何返回信息
		// dialog.show();
		// return true;
		// }
		@Override
		public void onReceivedTitle(WebView view, String title) {
			top_title.setText(title);
			LogUtils.i(TAG, "onReceivedTitle=" + "title=" + title);
			
			super.onReceivedTitle(view, title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			currentLoadProgress = newProgress;
			if (newProgress > 49) {

			}
			LogUtils.i(TAG + "home_onProgressChanged=newProgres=" + newProgress);
		}

	}

	/**
	 * 在自己的窗口 新建一个webview，打开uri 覆盖之前的窗口
	 * 
	 * @param oldwebview
	 *            之前的webview
	 * @param url
	 *            路径 Created by huyan
	 */
	@Override
	public void showTwoPage(WebView oldwebview, String url) {
		mWebViewFactory.showUrl2OhterWebView(oldwebview, url, new MyWebViewClient_Home(), TAG);
	}

	/**
	 * 隐藏当前的webview 显示下一层的webview Created by huyan
	 */
	@Override
	public void hideErrorPage() {
		mWebViewFactory.goBackfromOhterWebview(TAG, null);
	}

	@Override
	public boolean onBackPressed() {
		LogUtils.i(TAG + "onBackPressed");

		return false;
	}

	@Override
	public void open2OhterActivitye(Intent intent) {
		startActivity(intent);
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

	@Override
	public void openWebview(WebView view, String url) {

	}

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
		
		if (resultCode == 1 && data != null) {
			boolean str = data.getBooleanExtra("bind", false);
			LogUtils.i(TAG + "data.getBooleanExtra(bind)="+str );
			if (str) {
				webview.reload();
				UIUtils.showToastSafe("绑定店铺成功！");
			}

		}
	}

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
//				http://m.mgw.cc?addfriend=10653862&app=2352
//				final String userid = str.substring(26, str.length());
				final String userid;
				
				if(!str.contains("&app=")){
					 userid = str.substring(26, str.length());
				}else{
					 userid = StringUtils.substringBetween(str, "addfriend=", "&app=");
					 String app = str.substring(str.indexOf("&app=")+"&app=".length(), str.length());
				}
				
				
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
					mainpage.setBackgroundResource(R.drawable.sao);
					mainpage.setVisibility(View.VISIBLE);
					
					back.setVisibility(View.GONE);
				} else {
					mainpage.setBackgroundResource(0);
					mainpage.setVisibility(View.GONE);
					back.setVisibility(View.VISIBLE);
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:

			if (webview.canGoBack()) {
				// webview.clearHistory();
				webview.goBack();
				return; // 已处理
			}

			break;

		case R.id.mainpage:
			UIUtils.runInMainThread(new Runnable() {

				@Override
				public void run() {
					if ((MGWBaseActivity) getActivity() != null) {
						((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
					}
					startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 1);
				}
			});
			break;

		}
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		loadurlmap.clear();
		loadurlmap=null;
	}

}
