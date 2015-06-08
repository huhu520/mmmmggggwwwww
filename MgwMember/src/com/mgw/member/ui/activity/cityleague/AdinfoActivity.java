package com.mgw.member.ui.activity.cityleague;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;

public class AdinfoActivity extends Activity {
	private TextView textView_title_adinfo, textView_adinfo_content;
	private TextView imagebutton_adinfo_back;
	private ImageView imageView_adinfo_cimg1, imageView_adinfo_cimg2,
			imageView_adinfo_cimg3;
	private Button bt_goto_sorst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adinfo);
		textView_title_adinfo = (TextView) findViewById(R.id.TextView_title_dainfo);
		textView_adinfo_content = (TextView) findViewById(R.id.textView_adinfo_content);
		imagebutton_adinfo_back = (TextView) findViewById(R.id.imagebutton_adinfo_back);
		bt_goto_sorst = (Button) findViewById(R.id.bt_go_sorst);
		imageView_adinfo_cimg1 = (ImageView) findViewById(R.id.imageView_adinfo_cimg1);
		imageView_adinfo_cimg2 = (ImageView) findViewById(R.id.imageView_adinfo_cimg2);
		imageView_adinfo_cimg3 = (ImageView) findViewById(R.id.imageView_adinfo_cimg3);
		getadinfoData(true);
		init();
	}

	public void init() {
		imagebutton_adinfo_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	private void getadinfoData(boolean show) {

		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json==", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONObject jsonObject = obj.getJSONObject("item");
						// textView_adinfo_title.setText(jsonObject
						// .getString("title"));
						textView_title_adinfo.setText(jsonObject
								.getString("title"));

						textView_adinfo_content.setText(jsonObject
								.getString("content"));
						String[] cimgs = jsonObject.getString("cimg")
								.split(",");
						Log.i("==imgsize", cimgs.length + "");
						switch (cimgs.length) {
						case 1:
							if (cimgs[0].equals("")) {
								break;
							}
							imageView_adinfo_cimg1.setVisibility(View.VISIBLE);
							ImageLoaderHelper.displayImage(
									R.drawable.img_loading,
									imageView_adinfo_cimg1, cimgs[0]);
							Log.i("==图一==", cimgs[0]);
							break;

						case 2:
							imageView_adinfo_cimg1.setVisibility(View.VISIBLE);
							imageView_adinfo_cimg2.setVisibility(View.VISIBLE);
							Log.i("==图一==", cimgs[0]);
							ImageLoaderHelper.displayImage(
									R.drawable.img_loading,
									imageView_adinfo_cimg1, cimgs[0]);
							ImageLoaderHelper.displayImage(
									R.drawable.img_loading,
									imageView_adinfo_cimg2, cimgs[1]);
							break;
						case 3:
							imageView_adinfo_cimg1.setVisibility(View.VISIBLE);
							imageView_adinfo_cimg2.setVisibility(View.VISIBLE);
							imageView_adinfo_cimg3.setVisibility(View.VISIBLE);
							Log.i("==图一==", cimgs[0]);
							Log.i("==图二==", cimgs[1]);
							Log.i("==图三==", cimgs[2]);
							ImageLoaderHelper.displayImage(
									R.drawable.img_loading,
									imageView_adinfo_cimg1, cimgs[0]);
							ImageLoaderHelper.displayImage(
									R.drawable.img_loading,
									imageView_adinfo_cimg2, cimgs[1]);
							ImageLoaderHelper.displayImage(
									R.drawable.img_loading,
									imageView_adinfo_cimg3, cimgs[2]);
							break;
						}

						final String sid = jsonObject.getString("sid").trim();
						final String name = jsonObject.getString("title")
								.trim();
						final String stid = jsonObject.getString("stid").trim();

						if (sid.length() > 0) {
							bt_goto_sorst.setVisibility(View.VISIBLE);
							bt_goto_sorst.setText("进入商家店铺");
							bt_goto_sorst
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent intent = new Intent(
													AdinfoActivity.this,
													ShopDetailAndIntroduceActivity.class);
											Bundle bundle = new Bundle();
											bundle.putString("sid", sid);
											bundle.putString("name", name);
											intent.putExtras(bundle);
											startActivity(intent);
										}
									});
						} else if (stid.length() > 0) {
							bt_goto_sorst.setVisibility(View.VISIBLE);
							bt_goto_sorst.setText("进入商家列表");
							bt_goto_sorst
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent intent = new Intent(
													AdinfoActivity.this,
													ShopDetailActivity.class);
											Bundle bundle = new Bundle();
											bundle.putString("stid", stid);
											// bundle.putInt("type", 1);
											intent.putExtras(bundle);
											startActivity(intent);

										}
									});
						}

					} else {
						UIUtils.showToastWithAlertPic(obj.getString("msg"));
						textView_adinfo_content.setText("数据加载失败。。。");

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable ble) {

			}
		};
		RequestParams params = new RequestParams();
		params.put("type", "member.getadinfo");
		try {
			Bundle bundle = getIntent().getExtras();
			String adid = bundle.getString("adId");
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data",
					Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("adid", adid);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}
}