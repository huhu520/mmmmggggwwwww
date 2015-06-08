package com.mgw.member.ui.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.activity.cityleague.CityleagueActivity;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.squareup.picasso.Picasso;

public class MymaterialActivity extends Activity {
	JSONArray my_Array_list = new JSONArray();
	private Myadapter myadapter = null;
	private int index = 1;
	private ListView listView_mymaterial;
	private ProgressDialog progressDialog;
	private Button ibBack_mymaterial;
	private TextView tv_mymaterial_use;
	private boolean isBottom = false;
	private View moreView;
	public static boolean shouldrefresh;
	// private RequestQueue requestQueue;
	BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymaterial);
		// requestQueue = new Volley().newRequestQueue(this);
		bitmapUtils = new BitmapUtils(this);
		moreView = getLayoutInflater().inflate(R.layout.footerview, null);
		moreView.setVisibility(View.GONE);
		listView_mymaterial = (ListView) findViewById(R.id.listView_mymaterial);
		ibBack_mymaterial = (Button) findViewById(R.id.ibBack_mymaterial);
		tv_mymaterial_use = (TextView) findViewById(R.id.tv_mymaterial_use);
		listView_mymaterial.addFooterView(moreView);
		myadapter = new Myadapter();
		listView_mymaterial.setAdapter(myadapter);
		init();
		getdata();

	}

	public void init() {

		ibBack_mymaterial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		listView_mymaterial.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MymaterialActivity.this, MaterialinfoActivity.class);
				try {
					JSONObject item = my_Array_list.getJSONObject(position);
					intent.putExtra("type", "mycoupon");
					intent.putExtra("cid", item.getString("cid"));
					intent.putExtra("sid", item.getString("sid"));
					startActivity(intent);
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		});
		listView_mymaterial.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (isBottom && scrollState == 0) {
					getdata();
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				isBottom = (firstVisibleItem + visibleItemCount == totalItemCount);

			}
		});
		tv_mymaterial_use.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MymaterialActivity.this, CouponUseActivity.class);
				startActivity(intent);

			}
		});
	}

	// listview适配器
	class Myadapter extends BaseAdapter {
		final int TYPE_1 = 1;
		final int TYPE_2 = 2;

		@Override
		public int getItemViewType(int position) {
			try {

				JSONObject item1 = (JSONObject) my_Array_list.get(position);
				if (item1.get("peroid").equals("0")) {
					return TYPE_2;
				} else {
					return TYPE_1;
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
			return TYPE_1;
		}

		@Override
		public int getViewTypeCount() {

			return 3;
		}

		@Override
		public int getCount() {
			if (my_Array_list == null) {
				return 0;
			}
			return my_Array_list.length();
		}

		@Override
		public Object getItem(int position) {

			return null;
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder mHolder = null;
			ViewHolder2 mHolder2 = null;
			int Types = getItemViewType(position);
			if (convertView == null) {
				switch (Types) {
				case TYPE_1:
					LayoutInflater mInflater = LayoutInflater.from(MymaterialActivity.this);
					convertView = mInflater.inflate(R.layout.item_mymateriallistview, parent, false);
					mHolder = new ViewHolder();
					mHolder.imageView_mymaterialitem = (ImageView) convertView.findViewById(R.id.imageView_mymaterialitem);
					mHolder.tv_mymaterial_shopname = (TextView) convertView.findViewById(R.id.tv_mymaterial_shopname);
					mHolder.tv_mymaterial_distance = (TextView) convertView.findViewById(R.id.tv_mymaterial_distance);
					mHolder.tv_mymaterialitem_couponname = (TextView) convertView.findViewById(R.id.tv_mymaterialitem_couponname);
					mHolder.tv_mymaterialitem_code = (TextView) convertView.findViewById(R.id.tv_mymaterialitem_code);
					mHolder.tv_mymaterialitem_validstart = (TextView) convertView.findViewById(R.id.tv_mymaterialitem_validstart);
					mHolder.tv_mymaterialitem_validend = (TextView) convertView.findViewById(R.id.tv_mymaterialitem_validend);
					mHolder.button_mymaterialitem_present = (Button) convertView.findViewById(R.id.button_mymaterialitem_present);
					convertView.setTag(mHolder);
					break;

				case TYPE_2:
					LayoutInflater mInflater2 = LayoutInflater.from(MymaterialActivity.this);
					convertView = mInflater2.inflate(R.layout.item_mymateriallistview2, parent, false);
					mHolder2 = new ViewHolder2();
					mHolder2.imageView_mymaterialitem2 = (ImageView) convertView.findViewById(R.id.imageView_mymaterialitem2);
					mHolder2.tv_mymaterial2_shopname = (TextView) convertView.findViewById(R.id.tv_mymaterial2_shopname);
					mHolder2.tv_mymaterial2_distance = (TextView) convertView.findViewById(R.id.tv_mymaterial2_distance);
					mHolder2.tv_mymaterialitem2_couponname = (TextView) convertView.findViewById(R.id.tv_mymaterialitem2_couponname);
					mHolder2.tv_mymaterialitem2_code = (TextView) convertView.findViewById(R.id.tv_mymaterialitem2_code);

					convertView.setTag(mHolder2);
					break;
				}
			} else {
				switch (Types) {
				case TYPE_1:
					mHolder = (ViewHolder) convertView.getTag();
					break;

				case TYPE_2:
					mHolder2 = (ViewHolder2) convertView.getTag();
					break;

				}

			}
			switch (Types) {
			case TYPE_1:
				try {
					final JSONObject item = my_Array_list.getJSONObject(position);

					// ImageLoaderHelper.displayImage(R.drawable.img_loading,
					// mHolder.imageView_mymaterialitem,
					// item.getString("cover"));
					Picasso.with(getApplicationContext()).load(item.getString("cover")).into(mHolder.imageView_mymaterialitem);

					// bitmapUtils.display(mHolder.imageView_mymaterialitem,
					// item.getString("cover"));
					mHolder.tv_mymaterial_shopname.setText(item.get("sname").toString());
					mHolder.tv_mymaterial_distance.setText(String.format("< %.2fkm", Double.valueOf(item.get("distance").toString())));
					mHolder.tv_mymaterialitem_couponname.setText(item.get("name").toString());
					mHolder.tv_mymaterialitem_code.setText(item.get("coupon").toString());

					mHolder.tv_mymaterialitem_validstart.setText("有效期：" + item.get("end").toString());
					mHolder.tv_mymaterialitem_validend.setText("距今日：" + item.get("peroid").toString() + "天");
					mHolder.button_mymaterialitem_present.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MymaterialActivity.this, GivecouponActivity.class);
							try {
								intent.putExtra("coupon", item.getString("coupon").toString());
								intent.putExtra("cover", item.getString("cover"));
								intent.putExtra("name", item.getString("name"));
								// intent.putExtra("sid",
								// item.getString("sid"));
								startActivity(intent);
							} catch (JSONException e) {

								e.printStackTrace();
							}

						}
					});

				} catch (JSONException e) {

					e.printStackTrace();
				}
				break;

			case TYPE_2:
				try {
					JSONObject item2 = my_Array_list.getJSONObject(position);
					Picasso.with(getApplicationContext()).load(item2.getString("cover")).into(mHolder2.imageView_mymaterialitem2);
					// ImageLoaderHelper.displayImage(R.drawable.img_loading,
					// mHolder2.imageView_mymaterialitem2,
					// item2.getString("cover"));
					mHolder2.tv_mymaterial2_shopname.setText(item2.get("sname").toString());
					mHolder2.tv_mymaterial2_distance.setText(String.format("< %.2fkm", Double.valueOf(item2.get("distance").toString())));
					mHolder2.tv_mymaterialitem2_couponname.setText(item2.get("name").toString());
					mHolder2.tv_mymaterialitem2_code.setText(item2.get("coupon").toString());
				} catch (JSONException e) {

					e.printStackTrace();
				}
				break;
			}
			return convertView;

		}

	}

	class ViewHolder {
		public ImageView imageView_mymaterialitem;
		public TextView tv_mymaterial_shopname;
		public TextView tv_mymaterial_distance;
		public TextView tv_mymaterialitem_couponname;
		public TextView tv_mymaterialitem_code;
		public TextView tv_mymaterialitem_validstart;
		public Button button_mymaterialitem_present;
		public TextView tv_mymaterialitem_validend;
	}

	class ViewHolder2 {
		public ImageView imageView_mymaterialitem2;
		public TextView tv_mymaterial2_shopname;
		public TextView tv_mymaterial2_distance;
		public TextView tv_mymaterialitem2_couponname;
		public TextView tv_mymaterialitem2_code;

	}

	/**
	 * 获取我的消费券数据
	 */
	public void getdata() {
		if (myadapter == null) {
			index = 1;
			my_Array_list = new JSONArray();
			myadapter = new Myadapter();
			listView_mymaterial.setAdapter(myadapter);
		}
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(true);
		}

		MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(), false, true) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						if (progressDialog != null) {
							progressDialog.dismiss();
							progressDialog = null;
						}
						JSONArray array = obj.getJSONArray("items");
						int len = array.length();
						if (len == 0) {
							Toast.makeText(MymaterialActivity.this, "您的消费券没有更多的库存啦！", Toast.LENGTH_SHORT).show();
						}

						int count = my_Array_list.length();
						for (int i = 0; i < len; i++) {
							my_Array_list.put(i + count, array.getJSONObject(i));
						}
						index++;
						myadapter.notifyDataSetChanged();
					} else {
						if (progressDialog != null) {
							progressDialog.dismiss();
							progressDialog = null;
						}

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
					progressDialog = null;
				}
			}
		};

		RequestParams params = new RequestParams();
		String type = "coupon.mycoupon";

		params.add("type", type);
		params.add("userid", getSharedPreferences("mgw_data", 0).getString("mgw_userID", null));
		params.add("serial", getSharedPreferences("mgw_data", 0).getString("mgw_serial", null));
		params.add("posx", CityleagueActivity.m_lng + "");
		params.add("posy", CityleagueActivity.m_lat + "");
		params.add("index", index + "");
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (shouldrefresh) {
			shouldrefresh = false;
			my_Array_list = null;
			index = 1;
			my_Array_list = new JSONArray();
			getdata();
		}

	}

}
