package com.mgw.member.ui.activity.cityleague;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.TextView;

import com.hx.hxchat.widget.ExpandGridView;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;

public class PayCompletActivity extends BaseActivity2 implements OnClickListener {
	private String orderid;
	private JSONArray m_tui_list = new JSONArray();
	private MyTuiAdapter myTuiAdapter;

	JSONObject mInfo = new JSONObject();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pay_complet);

		orderid = getIntent().getExtras().getString("orderid");
		initTitleBar();
		findViewById(R.id.bt_pay_complemt_gotobuy).setOnClickListener(this);
		myTuiAdapter = new MyTuiAdapter();
		((ExpandGridView) findViewById(R.id.gv_pay_complet_tui)).setAdapter(myTuiAdapter);
		((ExpandGridView) findViewById(R.id.gv_pay_complet_tui)).setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					JSONObject item = m_tui_list.getJSONObject(arg2);

					Intent intent = new Intent(PayCompletActivity.this, GoodDetailActivity.class);
					intent.putExtra("sid", item.getString("sid"));
					intent.putExtra("pid", item.getString("pid"));
					intent.putExtra("pname", item.getString("pname"));
					startActivity(intent);
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			mInfo = new JSONObject(getIntent().getExtras().getString("info"));
			orderid = mInfo.getString("orderid");

			((TextView) findViewById(R.id.tv_pay_complemt_pname)).setText(mInfo.getString("pname"));
			((TextView) findViewById(R.id.detail)).setText("价格: " + mInfo.getString("sprice") + "    数量: " + mInfo.getString("quantity"));
			((TextView) findViewById(R.id.addr)).setText(mInfo.getString("addr"));

			if (mInfo.getInt("otype") == 1)
				findViewById(R.id.add_layout).setVisibility(View.GONE);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		getDataOtherView();
	}

	private void initTitleBar() {
		initTitleButton();

		((TextView) findViewById(R.id.tv_title_cent)).setText("支付完成");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_pay_complemt_gotobuy:
			finish();
			break;
		}
	}

	class MyTuiAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_tui_list.length();
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
			ViewHolderTui holder = null;
			if (convertView == null) {
				holder = new ViewHolderTui();

				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.item_goods_gvlist, null);

				holder.name = (TextView) convertView.findViewById(R.id.tv_item_goods_gvlist_name);

				holder.nowprice = (TextView) convertView.findViewById(R.id.tv_item_goods_gvlist_nowprive);
				holder.oldprice = (TextView) convertView.findViewById(R.id.tv_item_goods_gvlist_oldprice);
				holder.distance = (TextView) convertView.findViewById(R.id.tv_item_goods_gvlist_sellcount);
				holder.img = (ImageView) convertView.findViewById(R.id.iv_item_goods_gvlist);
				convertView.findViewById(R.id.tv_item_goods_gvlist_sellcount).setVisibility(View.INVISIBLE);
				holder.pos = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderTui) convertView.getTag();
				holder.pos = position;
			}
			try {

				JSONObject item = m_tui_list.getJSONObject(position);

				String pname = item.getString("pname");
				String sprice = item.getString("sprice");
				String mprice = item.getString("mprice");
				// String distance = item.getString("distance");
				String image = item.getString("image");
				holder.name.setText(pname);
				holder.nowprice.setText("￥" + sprice);
				holder.oldprice.setText("￥" + mprice);
				holder.oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				// holder.distance.setText(distance+"m");

				ImageLoaderHelper.displayImage(R.drawable.img_loading, holder.img, image);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	public final class ViewHolderTui {
		public TextView name;
		public TextView nowprice;
		public TextView oldprice;
		public ImageView img;
		public TextView distance;
		public int pos;
	}

	private void getDataOtherView() {
		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("createorder", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_tui_list = obj.getJSONArray("items");
						// initView();
						myTuiAdapter.notifyDataSetChanged();
						findViewById(R.id.tv_pay_complemtfanli).setFocusable(true);
						findViewById(R.id.tv_pay_complemtfanli).setFocusableInTouchMode(true);
						findViewById(R.id.tv_pay_complemtfanli).requestFocus();

						if (m_tui_list.length() == 0)
							findViewById(R.id.layout).setVisibility(View.GONE);
					} else {
						findViewById(R.id.layout).setVisibility(View.GONE);
						// Toast.makeText(getApplicationContext(),
						// obj.getString("msg"), Toast.LENGTH_SHORT).show();
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
		params.put("type", "wzreposity.otherview");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			String cid = getSharedPreferences("mgw_data", 0).getString("cid", "4301");
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("orderid", orderid);
			params.put("city", cid);

			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}
}
