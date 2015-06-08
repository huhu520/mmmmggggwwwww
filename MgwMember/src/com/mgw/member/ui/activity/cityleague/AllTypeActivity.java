package com.mgw.member.ui.activity.cityleague;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hx.hxchat.widget.ExpandGridView;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;

public class AllTypeActivity extends BaseActivity2 implements OnClickListener {
	private JSONArray m_array_list = new JSONArray();
	private final JSONArray m_hot_list = new JSONArray();
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.all_type);
		initTitleBar();
		adapter = new MyAdapter();
		((ListView) findViewById(R.id.lv_all_type)).setAdapter(adapter);
		getData(true);
	}

	private void initTitleBar() {
		LinearLayout ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
		ll_title_right.setVisibility(View.GONE);
		((TextView) findViewById(R.id.tv_title_cent)).setText("全部分类");
		initTitleButton();

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		}
	}

	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_array_list.length();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(AllTypeActivity.this).inflate(R.layout.item_alltype_list, null);
			ImageView iv_alltype_item_icon = (ImageView) view.findViewById(R.id.iv_alltype_item_icon);
			TextView tv_alltype_item_firstname = (TextView) view.findViewById(R.id.tv_alltype_item_firstname);
			ExpandGridView gv_alltype_item = (ExpandGridView) view.findViewById(R.id.gv_alltype_item);
			if (position == 0) {
				tv_alltype_item_firstname.setText("热门");
				iv_alltype_item_icon.setImageResource(R.drawable.ic_bussiness_info);
				gv_alltype_item.setAdapter(new MyGridVIewAdapter(m_hot_list));
				return view;
			}

			try {
				String image = m_array_list.getJSONObject(position - 1).getString("icon");
				String fname = m_array_list.getJSONObject(position - 1).getString("name");

				ImageLoaderHelper.displayImage(R.drawable.img_loading, iv_alltype_item_icon, image);

				// Picasso.with(AllTypeActivity.this).load(image).placeholder(R.drawable.img_loading).into(iv_alltype_item_icon);

				tv_alltype_item_firstname.setText(fname);
				gv_alltype_item.setAdapter(new MyGridVIewAdapter(m_array_list.getJSONObject(position - 1).getJSONArray("data")));

				final int pos = position;
				gv_alltype_item.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						try {
							Intent intent = new Intent(AllTypeActivity.this, ShopActivity.class);
							intent.putExtra("stype", m_array_list.getJSONObject(pos - 1).getString("id"));
							intent.putExtra("name", m_array_list.getJSONObject(pos - 1).getString("name"));
							intent.putExtra("type", 0);
							startActivity(intent);
							AllTypeActivity.this.finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return view;
		}
	}

	class MyGridVIewAdapter extends BaseAdapter {
		JSONArray items;

		public MyGridVIewAdapter(JSONArray items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.length();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(AllTypeActivity.this).inflate(R.layout.item_textview, null);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			try {
				if (items.getJSONObject(position).isNull("name")) {
					tv_item.setText(items.getJSONObject(position).getString("fstname"));
				} else {
					tv_item.setText(items.getJSONObject(position).getString("name"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return view;
		}
	}

	private void getData(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_array_list = obj.getJSONArray("items");
						adapter.notifyDataSetChanged();
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
		params.put("type", "wzreposity.allshoptype");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}
}
