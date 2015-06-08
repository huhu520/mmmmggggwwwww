package com.mgw.member.ui.activity.cityleague;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;

public class ShopPhotosActivity extends BaseActivity2 implements OnClickListener {
	private String TAG=ShopPhotosActivity.class.getSimpleName();
	private String sid;
	private JSONArray m_array_title = new JSONArray();
	private JSONArray m_array_photo = new JSONArray();
	private TabHost tb_shop_photo;
	private MyGridVIewAdapter gridVIewAdapter;
	private GridView gv_shop_pooto;

	ArrayList<String> mImage = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shop_photo);

		sid = getIntent().getExtras().getString("sid");
		Log.e("huoqusid", sid + "");
		initTitleButton();
		gridVIewAdapter = new MyGridVIewAdapter();
		getDataPhotoTitle(true);
		findViewById(R.id.tv_titlebar_right).setVisibility(View.INVISIBLE);
	}

	private void initTab() {
		tb_shop_photo = (TabHost) findViewById(R.id.tb_shop_photo);
		gv_shop_pooto = (GridView) findViewById(R.id.gv_shop_pooto);
		gv_shop_pooto.setAdapter(gridVIewAdapter);
		tb_shop_photo.setup();
		gv_shop_pooto.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(ShopPhotosActivity.this, SingleImageShowActivity.class);
				intent.putStringArrayListExtra("image", mImage);
				intent.putExtra("index", arg2);
				startActivity(intent);
			}
		});

		TabHost.TabSpec spec;
		if (m_array_title.length() > 0) {
			for (int i = 0; i < m_array_title.length(); i++) {

				spec = tb_shop_photo.newTabSpec(i + "");
				View niTab = LayoutInflater.from(this).inflate(R.layout.item_tab_shopphoto, null);
				TextView tv_item_tab_shopphoto = (TextView) niTab.findViewById(R.id.tv_item_tab_shopphoto);
				try {

					tv_item_tab_shopphoto.setText(m_array_title.getJSONObject(i).getString("aname"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				spec.setIndicator(niTab);
				spec.setContent(R.id.gv_shop_pooto);
				tb_shop_photo.addTab(spec);

			}

		}
		tb_shop_photo.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				m_array_photo = new JSONArray();
				Log.e("tabid", "tabid;;;;;" + tabId);
				getDataPhoto(true, Integer.parseInt(tabId));
			}
		});
		
		tb_shop_photo.setCurrentTab(1);
		
		tb_shop_photo.setCurrentTab(0);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		
		}
	}

	private void getDataPhotoTitle(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("phototitle", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_array_title = obj.getJSONArray("items");
						initTab();
						runOnUiThread(new Runnable() {
							public void run() {
								getDataPhoto(true, 0);
							}
						});

						((TextView) findViewById(R.id.tv_title_cent)).setText(getIntent().getExtras().getString("name") + "相册");
					} else {
						UIUtils.showToastWithAlertPic(obj.getString("msg"));

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
		params.put("type", "wzreposity.albumlist");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			Log.e("ggggggggggggggg", sid + "");
			params.put("sid", sid);
			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	private void getDataPhoto(boolean show, int type) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("photo", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_array_photo = obj.getJSONArray("items");

						mImage.removeAll(mImage);
						int count = m_array_photo.length();
						for (int i = 0; i < count; i++) {
							mImage.add(m_array_photo.getJSONObject(i).getString("value"));
						}
						runOnUiThread(new Runnable() {
							public void run() {
								gridVIewAdapter.notifyDataSetChanged();
							}
						});
				
					
					} else {
						if (obj.getString("msg").equals("商家属性编号为空")) {
							Toast.makeText(getApplicationContext(), "商家没有上传相册", Toast.LENGTH_SHORT).show();
						} else {
							UIUtils.showToastWithAlertPic(obj.getString("msg"));
						}

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
		params.put("type", "wzreposity.albumvalue");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			Log.e("ggggggggggggggg", sid + "");
			params.put("sid", sid);
			params.put("aid", m_array_title.getJSONObject(type).getString("aid"));
			Log.e("aaaaaaaaaaaaaaa", "aid::::" + m_array_title.getJSONObject(type).getString("aid"));
			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	class MyGridVIewAdapter extends BaseAdapter {
		
		
		
		

		@Override
		public int getCount() {
			Log.e(TAG,"getCount" +m_array_photo.length());
			
			return m_array_photo.length();
			
			
//			return mImage.size();
		}

	

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView image = new ImageView(ShopPhotosActivity.this);
			image.setScaleType(ScaleType.CENTER_CROP);
			try {
				Log.e(TAG, "value;;;;;;;;;" + m_array_photo.getJSONObject(position).getString("value")+",mImage.get(position)="+mImage.get(position));
				ImageLoaderHelper.displayImage(R.drawable.img_loading, image, m_array_photo.getJSONObject(position).getString("value"));
//				ImageLoaderHelper.displayImage(R.drawable.img_loading, image, mImage.get(position));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return image;
		}



		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
