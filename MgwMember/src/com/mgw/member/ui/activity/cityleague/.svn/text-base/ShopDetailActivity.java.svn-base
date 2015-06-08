package com.mgw.member.ui.activity.cityleague;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;

public class ShopDetailActivity extends BaseActivity2 implements OnClickListener {
	JSONArray m_array_list = new JSONArray();
	JSONArray m_shopkind_list = new JSONArray();
	JSONArray m_shopkind_list2 = new JSONArray();
	JSONArray m_goodskind_list = new JSONArray();
	private MyAdapter m_adapter;
	private MyShopkindAdapter myShopkindAdapter;
	private MyShopkindAdapter2 myShopkindAdapter2;
	private MyGoodskindAdapter myGoodskindAdapter;
	private int page = 1;
	private String ptype = "";
	private int order = 1;
	private PopupWindow popupWindow;
	private int selectListleftpont = -2;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shop_detaill);

		findViewById(R.id.ll_shop_detail_order).setOnClickListener(this);
		findViewById(R.id.ll_shop_detail_youhui).setOnClickListener(this);
		findViewById(R.id.ll_shopdetail_shopkind).setOnClickListener(this);
		initTitleButton();

		final PullToRefreshListView ptrlvHeadlineNews = (PullToRefreshListView) findViewById(R.id.lv_product_list);
		ptrlvHeadlineNews.setMode(Mode.DISABLED);
		ptrlvHeadlineNews.getRefreshableView().setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						getData(true);
					}
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			}
		});

		ptrlvHeadlineNews.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String pid = m_array_list.getJSONObject(arg2 - 1).getString("pid");
					String pname = m_array_list.getJSONObject(arg2 - 1).getString("pname");
					Intent intent = new Intent(ShopDetailActivity.this, GoodDetailActivity.class);
					intent.putExtra("sid", getIntent().getExtras().getString("sid"));
					intent.putExtra("pid", pid);
					intent.putExtra("pname", pname);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		if (m_array_list.length() > 0)
			getData(false);
		else
			getData(true);

		m_adapter = new MyAdapter();
		myShopkindAdapter = new MyShopkindAdapter();
		myShopkindAdapter2 = new MyShopkindAdapter2();
		myGoodskindAdapter = new MyGoodskindAdapter();
		ptrlvHeadlineNews.getRefreshableView().setAdapter(m_adapter);

		((TextView) findViewById(R.id.tv_shop_detail_order)).setTextColor(Color.rgb(50, 185, 170));
		((TextView) findViewById(R.id.tv_shop_detail_youhui)).setTextColor(Color.rgb(160, 160, 160));

		TextView tv_title_cent = (TextView) findViewById(R.id.tv_title_cent);
		tv_title_cent.setText(getIntent().getExtras().getString("name"));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_shopdetail_shopkind:
			showGoodsKindPopuwindow(v);
			break;

		case R.id.ll_shop_detail_youhui:
			order = 4;
			((LinearLayout) findViewById(R.id.ll_shop_detail_order)).setBackgroundColor(getResources().getColor(R.color.white));
			page = 1;
			m_array_list = new JSONArray();
			getData(true);
			m_adapter.notifyDataSetChanged();

			((TextView) findViewById(R.id.tv_shop_detail_youhui)).setTextColor(Color.rgb(50, 185, 170));
			((TextView) findViewById(R.id.tv_shop_detail_order)).setTextColor(Color.rgb(160, 160, 160));

			break;
		case R.id.ll_shop_detail_order:
			order = 1;
			((LinearLayout) findViewById(R.id.ll_shop_detail_youhui)).setBackgroundColor(getResources().getColor(R.color.white));
			page = 1;
			m_array_list = new JSONArray();
			getData(true);
			m_adapter.notifyDataSetChanged();

			((TextView) findViewById(R.id.tv_shop_detail_order)).setTextColor(Color.rgb(50, 185, 170));
			((TextView) findViewById(R.id.tv_shop_detail_youhui)).setTextColor(Color.rgb(160, 160, 160));
			break;
		}
	}

	class MyAdapter extends BaseAdapter {
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
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.item_shop_detail_list, null);

				holder.pname = (TextView) convertView.findViewById(R.id.tv_allshop_list_option);
				holder.mprice = (TextView) convertView.findViewById(R.id.tv_item_shopdetail_list_oldprice);
				holder.sprice = (TextView) convertView.findViewById(R.id.tv_item_shopdetail_list_newprice);
				holder.scount = (TextView) convertView.findViewById(R.id.tv_item_shopdetail_list_count);
				holder.image = (ImageView) convertView.findViewById(R.id.iv_item_shopdetail_list);

				holder.pos = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.pos = position;
			}
			try {

				JSONObject item = m_array_list.getJSONObject(position);

				String pname = item.getString("pname");
				String mprice = item.getString("mprice");
				String sprice = item.getString("sprice");
				String scount = item.getString("scount");
				String image = item.getString("image");

				holder.pname.setText(pname);
				holder.mprice.setText("￥" + mprice);
				holder.sprice.setText(sprice);
				holder.scount.setText("已售 :" + scount);
				holder.mprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				// Picasso.with(getApplicationContext()).load(image).into(holder.image);
				ImageLoaderHelper.displayImage(R.drawable.img_loading, holder.image, image);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	public final class ViewHolder {
		public TextView pname;
		public TextView mprice;
		public TextView sprice;
		public TextView scount;
		public ImageView image;
		public int pos;
	}

	@SuppressWarnings("deprecation")
	private void showGoodsKindPopuwindow(View ll) {
		if (m_array_list.length() > 0)
			getDataGoodsKind(false);
		else
			getDataGoodsKind(true);

		dissmissPopuwindow();
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.dialog_shopkind_select, null);
		ListView lisiview = (ListView) view.findViewById(R.id.lv_dialog_shopkind_select_left);
		ListView lv_dialog_shopkind_select_right = (ListView) view.findViewById(R.id.lv_dialog_shopkind_select_right);
		lisiview.setAdapter(myGoodskindAdapter);
		lv_dialog_shopkind_select_right.setVisibility(View.GONE);
		// lv_dialog_shopkind_select_right.setAdapter(myShopkindAdapter2);
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, 400);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.AnimTop2);
		popupWindow.showAsDropDown(ll, 0, 10);

	}

	private void dissmissPopuwindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	class MyGoodskindAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_goodskind_list.length() + 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(R.layout.item_dialeflist, null);
			TextView tv_item_dialft = (TextView) view.findViewById(R.id.tv_item_dialft);
			view.setTag(position);

			try {

				if (position == 0) {
					tv_item_dialft.setText("全部分类");
				} else {

					String name = m_goodskind_list.getJSONObject(position - 1).getString("tname");
					tv_item_dialft.setText(name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						dissmissPopuwindow();
						ptype = m_goodskind_list.getJSONObject((Integer) v.getTag() - 1).getString("tid");
						page = 1;
						m_array_list = new JSONArray();
						getData(true);
						m_adapter.notifyDataSetChanged();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			return view;
		}
	}

	class MyShopkindAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_shopkind_list.length() + 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(R.layout.item_dialeflist, null);
			TextView tv_item_dialft = (TextView) view.findViewById(R.id.tv_item_dialft);
			ImageView iv_item_dialef = (ImageView) view.findViewById(R.id.iv_item_dialef);

			view.setTag(position);
			try {

				if (position == 0) {
					tv_item_dialft.setText("全部分类");
				} else {

					String name = m_shopkind_list.getJSONObject(position - 1).getString("name");
					String image = m_shopkind_list.getJSONObject(position - 1).getString("icon");
					tv_item_dialft.setText(name);
					// Picasso.with(ShopDetailActivity.this).load(image).into(iv_item_dialef);
					ImageLoaderHelper.displayImage(R.drawable.img_loading, iv_item_dialef, image);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (position == selectListleftpont) {
				view.setBackgroundColor(getResources().getColor(R.color.bg2));
			} else {
				view.setBackgroundColor(getResources().getColor(R.color.white));
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						int p = (Integer) v.getTag();
						if (p == 0) {
							dissmissPopuwindow();
							((TextView) findViewById(R.id.tv_shop_detail_shopkind)).setText("全部分类");
							page = 1;
							m_array_list = new JSONArray();
							getData(true);
							m_adapter.notifyDataSetChanged();
							return;
						}
						Log.e("我点了第一个list", v.getTag() + "....");
						selectListleftpont = (Integer) v.getTag();

						m_shopkind_list2 = m_shopkind_list.getJSONObject((Integer) v.getTag() - 1).getJSONArray("data");

						if (m_shopkind_list2.length() <= 0) {
							dissmissPopuwindow();
							((TextView) findViewById(R.id.tv_shop_detail_shopkind)).setText(m_shopkind_list.getJSONObject(p - 1).getString("name"));
							page = 1;
							m_array_list = new JSONArray();
							getData(true);
							m_adapter.notifyDataSetChanged();
							return;

						}

						myShopkindAdapter2.notifyDataSetChanged();
						myShopkindAdapter.notifyDataSetChanged();

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			return view;
		}
	}

	class MyShopkindAdapter2 extends BaseAdapter {
		@Override
		public int getCount() {
			return m_shopkind_list2.length();
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
			LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(R.layout.item_dialeflist, null);
			TextView tv_item_dialft = (TextView) view.findViewById(R.id.tv_item_dialft);
			try {
				String name = m_shopkind_list2.getJSONObject(position).getString("name");
				String image = m_shopkind_list2.getJSONObject(position).getString("icon");
				tv_item_dialft.setText(name);
				if (!"".equals(image)) {
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			view.setTag(position);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						dissmissPopuwindow();
						int p = (Integer) v.getTag();
						Log.e("我点了第er个list", p + "....");
						((TextView) findViewById(R.id.tv_shop_detail_shopkind)).setText(m_shopkind_list2.getJSONObject(p).getString("name"));
						dissmissPopuwindow();
						page = 1;
						m_array_list = new JSONArray();
						getData(true);
						m_adapter.notifyDataSetChanged();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
			return view;
		}
	}

	private void getData(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				PullToRefreshListView listview = (PullToRefreshListView) findViewById(R.id.lv_product_list);
				listview.onRefreshComplete();

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("allproduct", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONArray array = obj.getJSONArray("items");
						for (int i = 0; i < array.length(); i++) {

							m_array_list.put(array.getJSONObject(i));

						}

						m_adapter.notifyDataSetChanged();
						page++;
					} else {
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable ble) {
				PullToRefreshListView listview = (PullToRefreshListView) findViewById(R.id.lv_product_list);
				listview.onRefreshComplete();

			}
		};

		RequestParams params = new RequestParams();
		params.put("type", "wzreposity.productlist");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("sid", getIntent().getExtras().getString("sid"));
			params.put("pindex", page + "");
			params.put("order", 1 + "");

			if (ptype != null && !"".equals(ptype)) {
				params.put("ptype", ptype);
			}
			if (order != 0) {
				params.put("order", order + "");
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}

	private void getDataGoodsKind(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("shopkind", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_goodskind_list = obj.getJSONArray("items");

						myGoodskindAdapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));

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
		params.put("type", "wzreposity.supplierprotype");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("sid", getIntent().getExtras().getString("sid"));
			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}
}
