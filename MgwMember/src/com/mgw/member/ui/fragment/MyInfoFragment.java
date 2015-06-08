package com.mgw.member.ui.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.alipay.android.msp.Keys;
import com.alipay.android.msp.Rsa;
import com.hx.hxchat.activity.ChatActivity;
import com.hx.hxchat.domain.UserFriendBean;
import com.hx.hxchat.utils.UserUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.http.pay.WXPayRequest1;
import com.mgw.member.js.dao.JsCnative;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.AddadActivity;
import com.mgw.member.ui.activity.MGWBaseActivity;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.ui.activity.MymaterialActivity;
import com.mgw.member.ui.activity.SubWebviewActivity;
import com.mgw.member.ui.activity.cityleague.GoodDetailActivity;
import com.mgw.member.ui.activity.cityleague.ShopDetailAndIntroduceActivity;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.NetworkProber;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.Utils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 
 * @author huyan Create On 2015/2/10. description:所有的html展示页面由这个fragment显装载
 */
public class MyInfoFragment extends BaseFragment implements OnClickListener {
	public static boolean shoudeflush = false;
	private final String TAG = MyInfoFragment.class.getSimpleName().toString();
	private WebView webview = null;
	// private MgwWebViewFactory mWebViewFactory;
	private RelativeLayout errorPage;
	private String currentUrl = "";
	private boolean isContainParentPageValue = false;
	private String parentPageValue = "";
	public Boolean isErrorPage;

	private TextView mainpage;
	private LinearLayout layout;
	private TextView back;
	private TextView top_title;

	@Override
	public View initView(LayoutInflater inflater) {
		View view = UIUtils.inflate(R.layout.fragment_myinfo);
		// mWebViewFactory = MgwWebViewFactory.getInstance(context);

		mainpage = (TextView) view.findViewById(R.id.mainpage);
		layout = (LinearLayout) view.findViewById(R.id.layout);
		back = (TextView) view.findViewById(R.id.back);
		top_title = (TextView) view.findViewById(R.id.title);

		back.setOnClickListener(this);
		mainpage.setOnClickListener(this);

		mainpage.setVisibility(View.GONE);
		layout.setVisibility(View.VISIBLE);
		back.setVisibility(View.GONE);
		top_title.setVisibility(View.VISIBLE);

		webview = (WebView) view.findViewById(R.id.webView);
		errorPage = (RelativeLayout) view.findViewById(R.id.rl_error);
		// webview.setOnKeyListener(new OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// // TODO Auto-generated method stub
		// return false;
		// }
		// });
		view.findViewById(R.id.refresh).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if ((MGWBaseActivity) getActivity() != null) {
					((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
				}
				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_REFRESH);
			}
		});
		webview.addJavascriptInterface(new Handle(), JsCnative.mgwjs);// 处理android调用js方法
		webViewSetting(webview, new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack() && !isContainParentPageValue) { //
						webview.clearHistory();
						// webview.goBack(); // 后退
						return false; // 已处理
					} else if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack() && isContainParentPageValue) {
						// webview.clearHistory();
						webview.loadUrl(parentPageValue);
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

		webview.setWebViewClient(new MyWebViewClient_MyInfo());
		webview.setWebChromeClient(new MyWebChromeClient_MyInfo());
		webview.loadUrl(getActivity().getSharedPreferences("mgw_data", 0).getString("mgw_owner", "http://www.baidu.com"));
		// mWebViewFactory.addToMyInfo(webview);

		return view;

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (shoudeflush) {
			webview.reload();
		}
		LogUtils.i(TAG + "onResume=currentLoadProgress=");
	}

	@Override
	public boolean onBackPressed() {
		LogUtils.i(TAG + "onBackPressed");
		// if (mWebViewFactory.mmyinfo_webviews.size() > 1) {
		// hideErrorPage();
		// return true;
		// }
		return false;
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
			Map<String, String> map = parseHtml2GetParentValue(data);
			parentPageValue = map.get("parentPage");

			isContainParentPageValue = !(parentPageValue == "");
			isErrorPage = map.get("errorPage") == "yes";

			showAndHideBack(parentPageValue == "");

			// if (isErrorPage) {
			// handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_ERROR);
			// } else {
			// handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_OK);
			// }
			if (NetworkProber.isNetworkAvailable(context) && !isErrorPage) {
				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_OK);
			} else {
				handler.sendEmptyMessage(MESSAGE_TYPE_WEBPAGER_ERROR);
			}
		}

		@Override
		@JavascriptInterface
		public void getUserInfo() {
			return;
		}

		@Override
		@JavascriptInterface
		public void transferTo(String url, String parm) {

		}

		@Override
		@JavascriptInterface
		public void setClientInfo(String groupid, String uid, String sid) {

		}

		@Override
		@JavascriptInterface
		public void toConsulter(String userid) {

		}

		@Override
		@JavascriptInterface
		public void toSupplier(String sid) {

		}

		@Override
		@JavascriptInterface
		public void buyInSupplier(String sid, String pid) {

		}
	}

	/**
	 * js消息处理handler
	 */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MESSAGE_TYPE_WEBPAGER_ERROR) {
				LogUtils.i(TAG, "MESSAGE_TYPE_WEBPAGER_ERROR");

				// Toast.makeText(context, "errorpage", 0).show();
				errorPage.setVisibility(View.VISIBLE);
			} else if (msg.what == MESSAGE_TYPE_WEBPAGER_REFRESH) {
				// TODO
				// ((WebView)
				// mWebViewFactory.getCurrentWebView_info()).loadUrl(currentUrl);
				webview.loadUrl(currentUrl);
				LogUtils.i(TAG, "MESSAGE_TYPE_WEBPAGER_REFRESH,currentUrl=" + currentUrl);
			} else if (msg.what == MESSAGE_TYPE_WEBPAGER_OK) {
				LogUtils.i(TAG, "MESSAGE_TYPE_WEBPAGER_OK");
				errorPage.setVisibility(View.GONE);
			}
		}

	};

	@Override
	public void showTwoPage(WebView view, String url) {
		// mWebViewFactory.showUrl2OhterWebView(view, url, new
		// MyWebViewClient_MyInfo(), TAG);
	}

	@Override
	public void hideErrorPage() {
		// mWebViewFactory.goBackfromOhterWebview(TAG, null);
	}

	@Override
	protected void openWebview(WebView view, String url) {
		view.loadUrl(url);
		// mDialogUtils.show();
	}

	public class MyWebViewClient_MyInfo extends BaseWebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			http://androidweb.mgw.cc/Member/MyWZOrderDetail.aspx?OrderID=C150423153400000013&Entrance=0&OrderStatus=
//			supplierid:150125144400000000 去商店联盟
			
			if (url.equals("upimage:")) {
				view.stopLoading();
				showImgDialog();

				return true;
			}
			if (url.contains("mycoupon:")) {
				view.stopLoading();
				Intent intent = new Intent(getActivity(), MymaterialActivity.class);
				startActivity(intent);
				return true;
			}
			if (url.contains("addad:")) {
				view.stopLoading();
				// view.reload();
				Intent intent = new Intent(getActivity(), AddadActivity.class);
				intent.putExtra("mod", 1);
				startActivity(intent);
				return true;
			}
			if (url.contains("editad:")) {
				view.stopLoading();
				// view.reload();
				Intent intent = new Intent(getActivity(), AddadActivity.class);
				intent.putExtra("adid", url.substring(7, url.length()));
				intent.putExtra("mod", 2);
				startActivity(intent);
				return true;
			}
			if (url.contains("tel:")) {
				view.stopLoading();

				// Intent phoneIntent = new Intent("android.intent.action.CALL",
				// Uri.parse(url));
				// startActivity(phoneIntent);

				String substring = url.substring(4);
				if ("".equals(substring)) {
					UIUtils.showToastSafe("该店家没有设置电话号码");
					return true;
				}

				Utils.callMobliePhone(Uri.parse(url), false);
				return true;
			}
			if (url.contains("MemberCardManage")) {
				view.stopLoading();
				Intent intent = new Intent(getActivity(), SubWebviewActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("type", 1);
				intent.putExtra("title", true);
				startActivity(intent);
				return true;
			}
			if (url.contains("chat:{")) {
				view.stopLoading();
				try {
					JSONObject obj = new JSONObject(url.substring(5));
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					intent.putExtra("userId", obj.getString("UserID"));
					// intent.putExtra("Name",
					// URLDecoder.decode(obj.getString("Name"), "utf-8"));
					UserFriendBean friendInfo2Bean = UserUtils.getFriendInfo2Bean(obj.getString("UserID"));
					if (friendInfo2Bean != null) {
						intent.putExtra("userName", friendInfo2Bean.getItems().get(0).getNickName());

					}
					startActivity(intent);
					return true;
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			if (url.toLowerCase().contains("supplierid:")) {
				view.stopLoading();
				String sid = url.substring("supplierid:".length()).trim();
				Intent intent = new Intent(getActivity(), ShopDetailAndIntroduceActivity.class);
				intent.putExtra("sid", sid);
				startActivity(intent);
				
				return true;
			}
			
			
			//去商品详细页
			if (url.toLowerCase().contains("sid")&&url.toLowerCase().contains("pid")&&url.toLowerCase().contains("pname")) {
	
//				supplierid:150125144400000000
//				sid=150125144400000000&pid=5648787786&pname=5475
				//String url="sid=12545858555&pid=5895651891565&pname=哇哈哈矿泉水";
				
				int sidindex=url.indexOf("sid=");
				int pidindex=url.indexOf("&pid=");
				int pameindex=url.indexOf("&pname");
				
				String sid=url.substring(sidindex+4, pidindex);
				String pid=url.substring(pidindex+5, pameindex);
				String pname=url.substring(pameindex+5);
				
				
				view.stopLoading();
				Intent intent = new Intent(context, GoodDetailActivity.class);
				intent.putExtra("sid", sid);//店铺id
				intent.putExtra("pid", pid);//商品id
				intent.putExtra("pname", pname);//商品名
				startActivity(intent);
				return true;
				
			}
			
			if (url.contains("buy:{")) {
				view.stopLoading();
				if (finishPay) {
					finishPay = false;

					try {
						mOrderInfo = new JSONObject(url.substring(4));

						new Builder(getActivity()).setTitle("选择支付方式").setItems(new String[] { "微信支付", "支付宝支付" }, new DialogInterface.OnClickListener() {
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
			if (url.contains("goto:login")) {
				if (mShowExit)
					return true;
				mShowExit = true;
				view.stopLoading();
				{
					// SharedPreferences.Editor sharedata =
					// getActivity().getSharedPreferences("mgw_data", 0).edit();
					// sharedata.putString("mgw_data", "");
					// sharedata.putString("mgw_pwd", "");
					// sharedata.putString("mgw_account", "");
					// sharedata.putBoolean("logined", false);
					// sharedata.commit();

					BaseApplication.getApplication().logout(context);

					// ((BaseApplication)
					// getActivity().getApplicationContext()).m_user_id = "";
					// startActivity(new Intent(context, LoginActivity.class));
					mShowExit = false;
					// getActivity().finish();
					// AppManager.getAppManager().finishActivity(MainActivity.class);

				}
				return true;
			}

			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
			// 通过内部类定义的方法获取html页面加载的内容，这个需要添加在webview加载完成后的回调中
			view.loadUrl("javascript:window.mgwjs.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			dismissLoadingDialog();
			LogUtils.i("onPageFinished=" + "url=" + url);
			if (context == null)
				return;
			
			if(MainActivity.mainActivity!=null)
			(MainActivity.mainActivity).findViewById(R.id.rl_error).setVisibility(View.INVISIBLE);
		}

		boolean mShowExit = false;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if ((MGWBaseActivity) getActivity() != null) {
				((MGWBaseActivity) getActivity()).beepManager.playBeepSoundAndVibrate();
			}

			// currentUrl = url;

			currentUrl = url;
			//
			// if (url.contains("goto:login")) {
			// if (mShowExit)
			// return;
			// mShowExit = true;
			// view.stopLoading();
			// {
			// SharedPreferences.Editor sharedata =
			// getActivity().getSharedPreferences("mgw_data", 0).edit();
			// sharedata.putString("mgw_data", "");
			// sharedata.putString("mgw_pwd", "");
			// sharedata.putString("mgw_account", "");
			// sharedata.putBoolean("logined", false);
			// sharedata.commit();
			//
			// BaseApplication.getApplication().logout();
			//
			// // ((BaseApplication)
			// // getActivity().getApplicationContext()).m_user_id = "";
			// startActivity(new Intent(context, LoginActivity.class));
			// mShowExit = false;
			// getActivity().finish();
			// // AppManager.getAppManager().finishActivity(MainActivity.class);
			//
			// }
			// return;
			// }
			// 含有键值对，不加载，直接跳转
			// if (url.contains("?id=") || url.contains("?key=")) {
			if ((url.contains("?id=") && !url.contains("&style"))) {
				view.stopLoading();
				Intent intent = new Intent(context, SubWebviewActivity.class);
				intent.putExtra("url", url);
				intent.putExtra("type", 1);
				open2OhterActivitye(intent, false);
				return;
			} else {
				if (((MainActivity) getActivity() != null) && !((MainActivity) getActivity()).isFinishing()) {
					showLoadingDialog(getActivity(), MyInfoFragment.class);
				}
				return;
			}

		}
	}

	public class MyWebChromeClient_MyInfo extends BaseWebChromeClient {

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			top_title.setText(title);
			LogUtils.i(TAG, "onReceivedTitle=" + "title=" + title);
		}

	}

	@Override
	public void open2OhterActivitye(Intent intent) {

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

	/** 这是弹出选择图片来源的dailog */
	AlertDialog mDailog = null;

	void showImgDialog() {
		mDailog = new AlertDialog.Builder(getActivity()).setTitle("选择图片").setItems(new String[] { "拍照", "从相册中选择" }, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					showImgPick(0);
				} else if (which == 1) {
					showImgPick(1);
				}

				mDailog.dismiss();
				mDailog = null;
			}
		}).show();
	}

	public static final int IMAGE_CODE = 3;
	public static final int REQUEST_CODE = 4;
	private final String IMAGE_TYPE = "image/*";
	Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/" + "faceImage.jpg"));

	/** 该方法传入参数0则调用相机拍照，传入参数1则从相册选取 */
	void showImgPick(int type) {
		if (type == 0) {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

				startActivityForResult(intent, REQUEST_CODE);
			}
		} else {
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);

			startActivityForResult(getAlbum, IMAGE_CODE);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;

		Bitmap photo = null;
		if (requestCode == REQUEST_CODE) {
			// upimg(new File(imageUri.toString()));
			Uri uri = data.getData();
			if (uri != null) {
				photo = BitmapFactory.decodeFile(uri.getPath());
			}

			if (photo == null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					photo = (Bitmap) bundle.get("data");
				}
			}
		} else if (requestCode == IMAGE_CODE) {
			// upimg(new File(imageUri.toString()));
			try {
				photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} // 显得到bitmap图片
		}

		if (photo == null)
			return;
		photo = ThumbnailUtils.extractThumbnail(photo, 200, 200, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

		InputStream is = Bitmap2IS(photo);
		getData(is);
		// saveBitmap2file(photo, "face.jpg");
		// upimg(new File("/sdcard/" + "face.jpg"));

		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 将bitmap对象转换成inputstream */
	private InputStream Bitmap2IS(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		return sbs;
	}

	/** 原来的图片上传的方法 */
	private void getData(InputStream is) {
		MgqDataHandler loginHandler = new MgqDataHandler(getActivity(), true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
						webview.loadUrl(getActivity().getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_owner", ""));
					} else {
						Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Throwable ble) {

			}
		};

		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		SharedPreferences sp = context.getSharedPreferences("mgw_data", 0);
		params.put("picture", is);
		MgqRestClient.post("http://android4.mgw.cc" + "/MemImage/UpImage.aspx?serial=" + sp.getString("mgw_serial", null) + "&userid=" + sp.getString("mgw_userID", null) + "&format=.jpg", params,
				loginHandler);
	}

	/** 将bitmap对象存到本地 */
	static boolean saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream("/sdcard/" + filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}

	/** 使用xutils的图片上传的方法 */
	private void upimg(File file) {
		SharedPreferences sp = context.getSharedPreferences("mgw_data", 0);
		RequestParams params = new RequestParams();

		params.addBodyParameter("picture", file);

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, "http://android4.mgw.cc" + "/MemImage/UpImage.aspx?serial=" + sp.getString("mgw_serial", null) + "&userid=" + sp.getString("mgw_userID", null) + "&format=.jpg",
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
						if (((MainActivity) getActivity() != null) && !((MainActivity) getActivity()).isFinishing()) {
							showLoadingDialog(getActivity(), MyInfoFragment.class);
						}
					}

					@Override
					public void onLoading(long total, long current, boolean isUploading) {
						if (isUploading) {

						} else {

						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						dismissLoadingDialog();
						Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
						webview.reload();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						dismissLoadingDialog();
						if (msg.contains("out")) {

							Toast.makeText(getActivity(), "当前网络环境较差，请更换网络后再上传！", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(), "上传失败！", Toast.LENGTH_SHORT).show();
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:

			if (webview.canGoBack() && isContainParentPageValue) {
				// webview.clearHistory();
				webview.loadUrl(parentPageValue);
				return; // 已处理
			}

			break;

		case R.id.mainpage:
			webview.clearHistory();
			webview.loadUrl(getActivity().getSharedPreferences("mgw_data", 0).getString("mgw_owner", "http://www.baidu.com"));

		default:
			break;
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
					mainpage.setVisibility(View.GONE);
					back.setVisibility(View.GONE);
				} else {
					back.setVisibility(View.VISIBLE);
					mainpage.setVisibility(View.VISIBLE);
				}

			}
		});

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

	void PaydWX() {
		try {
			IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), Define_C.APP_ID);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			SharedPreferences sp = getActivity().getSharedPreferences("mgw_data", 0);
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
			new WXPayRequest1(getActivity(), item).WXPay();

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
					AliPay alipay = new AliPay(getActivity(), mHandler);
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

		SharedPreferences sp = getActivity().getSharedPreferences("mgw_data", 0);
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

}
