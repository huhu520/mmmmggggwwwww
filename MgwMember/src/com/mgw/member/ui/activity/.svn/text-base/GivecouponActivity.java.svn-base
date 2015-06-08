package com.mgw.member.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.Utils;
import com.squareup.picasso.Picasso;

public class GivecouponActivity extends Activity {
	private Button ibBack_givecoupon, button_givecoupon_give, btn_givecoupon_quxiao;
	private ImageView imageView_givecoupon, imageView_givecoupon_choosefrend;
	private TextView tv_givecoupon_name, tv_givecoupon_bywxqq;
	private EditText et_givecoupon;
	private String username, usernumber, text;
	private ProgressDialog progressDialog;
	// private LinearLayout givecoupon_fenxiang;
	private ScrollView scrollView;
	private InputMethodManager inputMethodManager;
	String coupon = "";
	String couponname = "";
	String cover = "";
	BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_givecoupon);
		Intent intent = getIntent();
		bitmapUtils = new BitmapUtils(this);
		coupon = "&coupon=" + intent.getStringExtra("coupon");
		couponname = intent.getStringExtra("name");
		cover = intent.getStringExtra("cover");
		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// givecoupon_fenxiang = (LinearLayout)
		// findViewById(R.id.givecoupon_fenxiang);
		ibBack_givecoupon = (Button) findViewById(R.id.ibBack_givecoupon);
		button_givecoupon_give = (Button) findViewById(R.id.button_givecoupon_give);
		// btn_givecoupon_quxiao = (Button)
		// findViewById(R.id.btn_givecoupon_quxiao);
		tv_givecoupon_name = (TextView) findViewById(R.id.tv_givecoupon_name);
		tv_givecoupon_bywxqq = (TextView) findViewById(R.id.tv_givecoupon_bywxqq);
		et_givecoupon = (EditText) findViewById(R.id.et_givecoupon);
		imageView_givecoupon = (ImageView) findViewById(R.id.imageView_givecoupon);
		imageView_givecoupon_choosefrend = (ImageView) findViewById(R.id.imageView_givecoupon_choosefrend);
		init();
	}

	public void init() {
		ibBack_givecoupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		Picasso.with(this).load(cover).into(imageView_givecoupon);
		tv_givecoupon_name.setText(couponname);
		imageView_givecoupon_choosefrend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);

			}
		});

		button_givecoupon_give.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					text = et_givecoupon.getText().toString().replace("-", "").trim();
					text = text.replace(" ", "");
					text = text.substring(0, 11);
					if (!Utils.isMobileNO(text)) {
						Toast.makeText(getApplicationContext(), "你输入的手机号码不正确！", Toast.LENGTH_SHORT).show();
					} else {
						givecoupon("1");
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
				}

			}
		});
		tv_givecoupon_bywxqq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// givecoupon_fenxiang.setVisibility(View.VISIBLE);
				showShare();
			}
		});
		// btn_givecoupon_quxiao.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// givecoupon_fenxiang.setVisibility(View.GONE);
		//
		// }
		// });
		scrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}

				return false;

			}

		});

		// tv_givecoupon_bywx.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = getIntent();
		// String coupon = "&coupon=" + intent.getStringExtra("coupon");
		// String couponname = intent.getStringExtra("name");
		// // givecoupon("2");
		// WXshare wXshare = new WXshare(getApplicationContext());
		// wXshare.sendmsgReq(1, coupon, couponname);
		// }
		// });
		// tv_givecoupon_bywxhy.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// // givecoupon("2");
		// WXshare wXshare = new WXshare(getApplicationContext());
		// wXshare.sendmsgReq(0, coupon, couponname);
		//
		// }
		// });
		// tv_givecoupon_byqq.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // Toast.makeText(getApplicationContext(), "qq分享功能尚未开放，敬请期待！",
		// // Toast.LENGTH_SHORT).show();
		// SharedPreferences sharedPreferences = getSharedPreferences(
		// "mgw_data", 0);
		// String shareurl = sharedPreferences.getString("register_page",
		// "http://m.mgw.cc/register.aspx") + coupon;
		// Intent intent = new Intent(Intent.ACTION_SEND);
		// intent.setType("text/plain");
		// intent.putExtra(Intent.EXTRA_SUBJECT, couponname);
		// intent.putExtra(Intent.EXTRA_TEXT, shareurl);
		// startActivity(Intent.createChooser(intent, coupon));
		// }
		// });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			ContentResolver reContentResolverol = getContentResolver();
			Uri contactData = data.getData();
			@SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
			while (phone.moveToNext()) {
				usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				et_givecoupon.setText(usernumber + " (" + username + ")");
			}
		}
	}

	// public void getdata() {
	//
	// MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(),
	// false, false) {
	// @Override
	// public void onSuccess(String response) {
	// super.onSuccess(response);
	// try {
	// JSONObject obj = new JSONObject(response);
	// JSONObject item = obj.getJSONArray("items").getJSONObject(0);
	// Log.e("json", obj.toString());
	// if (obj.getInt("flag") == 0) {
	// // ImageLoaderHelper.displayImage(R.drawable.img_loading,
	// // imageView_givecoupon, item.getString("cover"));
	// // bitmapUtils.display(imageView_givecoupon, cover);
	// tv_givecoupon_name.setText(couponname);
	// } else {
	// Toast.makeText(getApplicationContext(), obj.getString("msg"),
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onFailure(Throwable ble) {
	//
	// }
	//
	// @Override
	// public void onFinish() {
	//
	// }
	// };
	// RequestParams params = new RequestParams();
	// String type = "coupon.couponinfo";
	// Intent intent = getIntent();
	// String coupon = intent.getStringExtra("coupon");
	// try {
	// JSONObject obj = new JSONObject(getSharedPreferences("mgw_data",
	// Context.MODE_PRIVATE).getString("mgw_data", ""));
	// params.add("type", type);
	// params.add("userid", obj.getString("UserID"));
	// params.add("serial", obj.getString("serial"));
	// params.add("coupon", coupon);
	//
	// MgqRestClient.get(MainActivity.url, params, loginHandler);
	// } catch (JSONException e1) {
	// e1.printStackTrace();
	// }
	//
	// }
	/** 分享成功后调用此方法通知服务器 */
	public void givecoupon(final String givetype) {
		progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(), false, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						//TODO 11 要发短信（没有注册） 0不发
						if (progressDialog != null) {
							progressDialog.dismiss();
						}

						MymaterialActivity.shouldrefresh = true;
						if (givetype.equals("1")) {
							Toast.makeText(getApplicationContext(), "赠送成功！", Toast.LENGTH_SHORT).show();
							finish();
						}
						if (givetype.equals("2")) {
							Toast.makeText(getApplicationContext(), "赠送成功！", Toast.LENGTH_SHORT).show();
							finish();
						}

					} else {
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable ble) {

			}

			@Override
			public void onFinish() {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		};
		if (givetype.equals("2") || givetype.equals("3")) {
			SharedPreferences sharedPreferences = this.getSharedPreferences("mgw_data", 0);
			String shareurl = sharedPreferences.getString("register_page", "http://m.mgw.cc/register.aspx");
			Intent intent = getIntent();
			String coupon = "&coupon=" + intent.getStringExtra("coupon");
			text = shareurl + coupon;
		}
		RequestParams params = new RequestParams();
		String type = "coupon.givecoupon";
		Intent intent = getIntent();
		String coupon = intent.getStringExtra("coupon");
		// text=et_givecoupon.getText();

		params.add("type", type);
		params.add("userid", getSharedPreferences("mgw_data", 0).getString("mgw_userID", null));
		params.add("serial", getSharedPreferences("mgw_data", 0).getString("mgw_serial", null));
		params.add("coupon", coupon);
		params.add("giveType", givetype);
		params.add("text", text);
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	static public int WXsharecod = 100;
	static public int QQsharecod = 200;

	@Override
	protected void onResume() {
		super.onResume();

		if (WXsharecod != 100) {
			if (WXsharecod == 0) {
				givecoupon("2");

			} else if (WXsharecod == -2) {
				Toast.makeText(getApplicationContext(), "分享取消", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_SHORT).show();

			}
		}
		if (QQsharecod == 0) {
			givecoupon("2");
		}
		QQsharecod = 200;
		WXsharecod = 100;

	}

	private void showShare() {
		SharedPreferences sharedPreferences = getSharedPreferences("mgw_data", 0);
		String shareurl = sharedPreferences.getString("register_page", "http://m.mgw.cc/register.aspx") + coupon;
		ShareSDK.initSDK(getApplicationContext());
		OnekeyShare oks = new OnekeyShare();
		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.icon, getString(R.string.app_name));
		oks.setUrl(shareurl);
		oks.setImageUrl(cover);
		oks.setText("【抢卷了】" + sharedPreferences.getString("mgw_name", "美顾问") + "赠送1个消费卷：" + couponname + "大家快来抢吧！");
		oks.setTitleUrl(shareurl);
		oks.setTitle("【抢卷了】" + sharedPreferences.getString("mgw_name", "美顾问") + "赠送1个消费卷：" + "大家快来抢吧！");
		// 启动分享GUI
		oks.show(getApplicationContext());
	}
}
