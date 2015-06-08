package com.mgw.member.ui.activity.cityleague;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.activity.MaterialinfoActivity;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;

public class VipmaterialActivity extends Activity {
	JSONArray my_Array_list = new JSONArray();
	private ListView listView_vipmaterial;
	private Myadapter myadapter = null;
	private int index = 1;
	String cid = "";
	private Button ibBack_vipmaterial;
	private ProgressDialog progressDialog = null;
	private boolean isBottom = false;
	private View moreView;
	int itemid = -1;
	public static boolean shouldflush;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipmaterial);
		moreView = getLayoutInflater().inflate(R.layout.footerview, null);
		moreView.setVisibility(View.GONE);
		listView_vipmaterial = (ListView) findViewById(R.id.listView_vipmaterial);
		ibBack_vipmaterial = (Button) findViewById(R.id.ibBack_vipmaterial);
		listView_vipmaterial.addFooterView(moreView);
		myadapter = new Myadapter();
		listView_vipmaterial.setAdapter(myadapter);
		cid = getSharedPreferences("mgw_data", 0).getString("cid", "4301");
		init();
	}

	public void init() {
		ibBack_vipmaterial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		listView_vipmaterial.setOnScrollListener(new OnScrollListener() {

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
		getdata();
	}

	Handler m_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case -1:
				break;
			}
		};
	};

	// listview适配器
	class Myadapter extends BaseAdapter {
		final int TYPE_1 = 1;
		final int TYPE_2 = 2;

		@Override
		public int getItemViewType(int position) {
			try {

				JSONObject item1 = (JSONObject) my_Array_list.get(position);
				if (item1.get("useNum").equals("0")) {
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
			// TODO Auto-generated method stub
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder;

			if (convertView == null) {
				mHolder = new ViewHolder();
				LayoutInflater mInflater = LayoutInflater.from(VipmaterialActivity.this);
				convertView = mInflater.inflate(R.layout.item_vipmaterial, null);
				mHolder.imageView_vipmaterialitem = (ImageView) convertView.findViewById(R.id.imageView_vipmaterialitem);
				mHolder.tv_vipmaterialitem_shopname = (TextView) convertView.findViewById(R.id.tv_vipmaterialitem_shopname);
				mHolder.tv_vipmaterialitem_distance = (TextView) convertView.findViewById(R.id.tv_vipmaterialitem_distance);
				mHolder.tv_vipmaterialitem_couponname = (TextView) convertView.findViewById(R.id.tv_vipmaterialitem_couponname);
				mHolder.tv_vipmaterialitem_number = (TextView) convertView.findViewById(R.id.tv_vipmaterialitem_number);
				mHolder.tv_vipmaterialitem_validstart = (TextView) convertView.findViewById(R.id.tv_vipmaterialitem_validstart);
				mHolder.tv_vipmaterialitem_validend = (TextView) convertView.findViewById(R.id.tv_vipmaterialitem_validend);
				mHolder.button_vipmaterialitem_get = (Button) convertView.findViewById(R.id.button_vipmaterialitem_get);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			try {
				final JSONObject item = my_Array_list.getJSONObject(position);

				ImageLoaderHelper.displayImage(R.drawable.img_loading, mHolder.imageView_vipmaterialitem, item.getString("cover"));
				mHolder.tv_vipmaterialitem_shopname.setText(item.get("sname").toString());
				mHolder.tv_vipmaterialitem_distance.setText(String.format("< %.2fkm", Double.valueOf(item.get("distance").toString())));
				mHolder.tv_vipmaterialitem_couponname.setText(item.get("name").toString());
				mHolder.tv_vipmaterialitem_number.setText("已领取：" + item.get("geted").toString());
				mHolder.tv_vipmaterialitem_validstart.setText("有效期：" + item.get("begin").toString());
				mHolder.tv_vipmaterialitem_validend.setText("—" + item.get("end").toString());
				int i = Integer.parseInt(item.getString("canUse"));

				if (i == 1) {
					mHolder.button_vipmaterialitem_get.setBackgroundResource(R.drawable.bg_grey);
					mHolder.button_vipmaterialitem_get.setText("已领");
					mHolder.button_vipmaterialitem_get.setOnClickListener(null);
				}
				if (i == 2) {
					mHolder.button_vipmaterialitem_get.setBackgroundResource(R.drawable.bg_grey);
					mHolder.button_vipmaterialitem_get.setText("已领完");
					mHolder.button_vipmaterialitem_get.setOnClickListener(null);
				}
				if (i == 0) {
					mHolder.button_vipmaterialitem_get.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								itemid = position;
								getcoupon(item.get("coupon").toString(), item.get("begin").toString(), item.get("end").toString(), item.get("cover").toString());

							} catch (JSONException e) {

								e.printStackTrace();
							}
						}
					});
					mHolder.button_vipmaterialitem_get.setBackgroundResource(R.drawable.select_slowblue2_btn);
					mHolder.button_vipmaterialitem_get.setText("领取");
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}

			return convertView;
		}

	}

	class ViewHolder {
		public ImageView imageView_vipmaterialitem;
		public TextView tv_vipmaterialitem_shopname;
		public TextView tv_vipmaterialitem_distance;
		public TextView tv_vipmaterialitem_couponname;
		public TextView tv_vipmaterialitem_number;
		public TextView tv_vipmaterialitem_validstart;
		public Button button_vipmaterialitem_get;
		public TextView tv_vipmaterialitem_validend;
	}

	/**
	 * 获取消费券数据
	 */
	public void getdata() {
		if (myadapter == null) {
			index = 1;
			my_Array_list = new JSONArray();
			myadapter = new Myadapter();
			listView_vipmaterial.setAdapter(myadapter);
		}

		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}

		MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(), false, true) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {

						if (obj.getString("msg").equals("未获取到相关数据") && index == 1) {
							Toast.makeText(VipmaterialActivity.this, "一张消费券也没有！", Toast.LENGTH_SHORT).show();
							if (progressDialog != null) {
								progressDialog.dismiss();
								progressDialog = null;
							}
							return;
						}

						JSONArray array = obj.getJSONArray("items");
						int len = array.length();
						if (progressDialog != null) {
							progressDialog.dismiss();
							progressDialog = null;
						}
						int count = my_Array_list.length();
						for (int i = 0; i < len; i++) {
							my_Array_list.put(i + count, array.getJSONObject(i));
						}
						if (progressDialog != null) {
							progressDialog.dismiss();
							progressDialog = null;
						}
						index++;
						myadapter.notifyDataSetChanged();
						listView_vipmaterial.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								Intent intent = new Intent(VipmaterialActivity.this, MaterialinfoActivity.class);
								try {
									JSONObject item = my_Array_list.getJSONObject(position);
									intent.putExtra("type", "vipmaterial");
									intent.putExtra("coupon", item.getString("coupon"));
									intent.putExtra("sid", item.getString("sid"));
									intent.putExtra("canUse", Integer.parseInt(item.getString("canUse")));
									intent.putExtra("time", item.getString("begin") + "至" + item.getString("end"));

									startActivity(intent);
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						});
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
		String type = "coupon.searchcoupon";

		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.add("type", type);
			params.add("userid", obj.getString("UserID"));
			params.add("serial", obj.getString("serial"));
			params.add("city", cid);
			params.add("posy", CityleagueActivity.m_lng + "");
			params.add("posx", CityleagueActivity.m_lat + "");
			params.add("index", index + "");
			MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 领取消费券
	 * 
	 * 
	 * */
	public void getcoupon(String coupon, final String start, final String end, final String cover) {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
		}

		MgqDataHandler loginHandler = new MgqDataHandler(getApplicationContext(), false, false) {
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

						Intent intent = new Intent(VipmaterialActivity.this, GetedcouponActivity.class);
						intent.putExtra("name", obj.getJSONObject("item").getString("name"));
						intent.putExtra("desc", obj.getJSONObject("item").getString("desc"));
						String time = start + "至" + end;
						intent.putExtra("time", time);
						intent.putExtra("coupon", obj.getJSONObject("item").getString("citem"));
						intent.putExtra("cover", cover);
						intent.putExtra("sid", obj.getJSONObject("item").getString("sid"));
						startActivity(intent);
						// Toast.makeText(getApplicationContext(), "领取成功！",
						// Toast.LENGTH_SHORT).show();
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
		String type = "coupon.getcoupon";

		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.add("type", type);
			params.add("userid", obj.getString("UserID"));
			params.add("serial", obj.getString("serial"));
			params.add("coupon", coupon);
			MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	protected void onRestart() {

		super.onRestart();
		if (shouldflush) {
			shouldflush = false;
			my_Array_list = null;
			my_Array_list = new JSONArray();
			index = 1;
			getdata();
		}
	}

	@Override
	public void onActivityResult(int rc, int qc, Intent data) {
		super.onActivityResult(rc, qc, data);
		if (data != null) {
			if (data.hasExtra("result")) {
				String str = data.getStringExtra("result");

				m_handler.sendEmptyMessage(-2);
				// ReturnFirst();

				m_handler.sendEmptyMessage(-1);
				// ReturnSecond();

			}
		}
	}
}
