package com.mgw.member.ui.activity.cityleague;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.mgw.member.uitls.SystemUtils;

public class ShopActivity extends BaseActivity2 implements OnClickListener {
	private int page = 1;
	JSONArray m_array_list = new JSONArray();
	JSONArray m_dialef_list = new JSONArray();
	JSONArray m_shopkind_list = new JSONArray();
	JSONArray m_shopkind_list2 = new JSONArray();
	JSONArray m_cricle_list = new JSONArray();
	private MyAdapter m_adapter;
	private KillAdapter mKillAdapter;
	private ListView listview;
	private PullToRefreshListView ptrlvHeadlineNews;
	private SharedPreferences sp;
	private int sort = 0;
	private Dialog dialog;
	private String stype = "";
	private String circle = "";
	private String skey;
	private PopupWindow popupWindow;
	private MyShopkindAdapter myShopkindAdapter;
	private MyShopkindAdapter2 myShopkindAdapter2;
	private int selectListleftpont = -2;
	private MyCircleGridVIewAdapter circleGridVIewAdapter;

	private MyAdapterSec m_adapter_sec;
	private int pageSec = 1;
	JSONArray m_array_list_sec = new JSONArray();
	JSONArray m_shopkind_list_sec = new JSONArray();
	JSONArray m_shopkind_list2_sec = new JSONArray();
	private MyShopkindAdapterSec myShopkindAdapterSec;
	private MyShopkindAdapterSec2 myShopkindAdapterSec2;
	private int selectListleftpontSec = -2;
	private String stypeSec = "";

	boolean mIsKill = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shop);
		sp = getSharedPreferences("mgw_data", 0);
		if (getIntent().getExtras().containsKey("skey")) {
			skey = getIntent().getExtras().getString("skey");
		}

		findViewById(R.id.ll_shop_shopkind).setOnClickListener(this);
		findViewById(R.id.ll_shop_location).setOnClickListener(this);
		findViewById(R.id.ll_shop_distance).setOnClickListener(this);
		findViewById(R.id.bt_shop_search).setOnClickListener(this);
		findViewById(R.id.tv_shop_seckill).setOnClickListener(this);
		findViewById(R.id.bt_shop_location).setOnClickListener(this);
		findViewById(R.id.tv_shop_allshop).setOnClickListener(this);
		initTitleButton();

		ptrlvHeadlineNews = (PullToRefreshListView) findViewById(R.id.shop_listview);
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
		listview = ptrlvHeadlineNews.getRefreshableView();
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String sid = m_array_list.getJSONObject(arg2 - 1).getString("sid");

					if (mIsKill) {
						String pname = m_array_list.getJSONObject(arg2 - 1).getString("pname");
						String pid = m_array_list.getJSONObject(arg2 - 1).getString("pid");
						Intent intent = new Intent(ShopActivity.this, GoodDetailActivity.class);
						intent.putExtra("sid", sid);
						intent.putExtra("pid", pid);
						intent.putExtra("pname", pname);
						startActivity(intent);
					} else {
						Intent intent = new Intent(ShopActivity.this, ShopDetailAndIntroduceActivity.class);
						intent.putExtra("sid", sid);
						startActivity(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		int type = getIntent().getExtras().getInt("type");
		if (type == 0) {
			((TextView) findViewById(R.id.tv_shop_shopkind)).setText(getIntent().getExtras().getString("name"));
			stypeSec = getIntent().getExtras().getString("stype");
			stype = getIntent().getExtras().getString("stype");
		} else if (type == 1) {
			((TextView) findViewById(R.id.tv_shop_location)).setText(getIntent().getExtras().getString("circlename"));
			circle = getIntent().getExtras().getString("circleid");
		} else if (type == 2) {
			((TextView) findViewById(R.id.tv_shop_shopkind)).setText(getIntent().getExtras().getString("name"));
			skey = getIntent().getExtras().getString("name");
		} else if (type == 3) {
			findViewById(R.id.v_shop_line).setVisibility(View.GONE);
			findViewById(R.id.v_shop_line0).setVisibility(View.VISIBLE);
			mIsKill = true;
			mKillAdapter = null;
			m_adapter = null;
		}

		m_adapter = new MyAdapter();
		myShopkindAdapter = new MyShopkindAdapter();
		myShopkindAdapter2 = new MyShopkindAdapter2();

		listview.setAdapter(m_adapter);
		getData(true);

		findViewById(R.id.ll_seckil_location).setOnClickListener(this);
		findViewById(R.id.ll_seckil_youhui).setOnClickListener(this);
		findViewById(R.id.ll_seckil_shopkind).setOnClickListener(this);

		final PullToRefreshListView ptrlvHeadlineNewsSec = (PullToRefreshListView) findViewById(R.id.listview);
		ptrlvHeadlineNewsSec.setMode(Mode.DISABLED);
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

		ListView listviewSec = ptrlvHeadlineNewsSec.getRefreshableView();
		listviewSec.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					Log.e("position", "p:::::::::" + (arg2 - 1) + "");

					String pid = m_array_list_sec.getJSONObject(arg2 - 1).getString("pid");
					Log.e("positionsiddddd", "pid::::::::" + pid + "");
					String sid = m_array_list_sec.getJSONObject(arg2 - 1).getString("sid");
					String pname = m_array_list_sec.getJSONObject(arg2 - 1).getString("pname");
					Log.e("positionsisid", "sid::::::::" + sid + "");
					Intent intent = new Intent(ShopActivity.this, GoodDetailActivity.class);

					intent.putExtra("pid", pid);
					intent.putExtra("sid", sid);
					intent.putExtra("pname", pname);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		m_adapter_sec = new MyAdapterSec();
		myShopkindAdapterSec = new MyShopkindAdapterSec();
		myShopkindAdapterSec2 = new MyShopkindAdapterSec2();
		listviewSec.setAdapter(m_adapter_sec);
	}

	class MyAdapter extends BaseAdapter {
		final int VIEW_TYPE = 3;
		final int TYPE_1 = 0;
		final int TYPE_2 = 1;
		final int TYPE_3 = 2;

		@Override
		public int getItemViewType(int position) {
			try {
				JSONObject item1 = (JSONObject) m_array_list.get(position);
				if (item1.getJSONArray("special").length() == 0) {
					return 0;
				}
				if (item1.getJSONArray("special").length() == 1) {
					return 1;
				}
				if (item1.getJSONArray("special").length() == 2) {
					return 2;
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public int getViewTypeCount() {

			return 3;
		}

		@Override
		public int getCount() {
			if (m_array_list == null)
				return 0;
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
			ViewHolder1 holder1 = null;
			ViewHolder2 holder2 = null;
			int type = getItemViewType(position);
			if (convertView == null) {
				switch (type) {
				case TYPE_1:
					LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = mInflater.inflate(R.layout.item_all_shop_list0, parent, false);
					holder = new ViewHolder();
					holder.sname = (TextView) convertView.findViewById(R.id.tv_item_allshop_name);
					holder.sdesc = (TextView) convertView.findViewById(R.id.tv_item_allshop_option);
					holder.sdistance = (TextView) convertView.findViewById(R.id.tv_item_allshop_distance);
					holder.sdisc = (TextView) convertView.findViewById(R.id.youhui_card_02);
					holder.ssafe = (TextView) convertView.findViewById(R.id.youhui_card_01);
					holder.sconsume = (TextView) convertView.findViewById(R.id.tv_item_allshop_count);
					holder.image = (ImageView) convertView.findViewById(R.id.iv_item_allshop_list);
					convertView.setTag(holder);
					break;

				case TYPE_2:
					LayoutInflater mInflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = mInflater1.inflate(R.layout.item_all_shop_list1, parent, false);
					holder1 = new ViewHolder1();
					holder1.sname = (TextView) convertView.findViewById(R.id.tv_item_allshop_name);
					holder1.sdesc = (TextView) convertView.findViewById(R.id.tv_item_allshop_option);
					holder1.sdistance = (TextView) convertView.findViewById(R.id.tv_item_allshop_distance);
					holder1.sdisc = (TextView) convertView.findViewById(R.id.youhui_card_02);
					holder1.ssafe = (TextView) convertView.findViewById(R.id.youhui_card_01);
					holder1.sconsume = (TextView) convertView.findViewById(R.id.tv_item_allshop_count);
					holder1.image = (ImageView) convertView.findViewById(R.id.iv_item_allshop_list);
					holder1.iv_type0 = (ImageView) convertView.findViewById(R.id.iv_type0);

					holder1.tv_name0 = (TextView) convertView.findViewById(R.id.tv_name0);
					holder1.tv_newprice0 = (TextView) convertView.findViewById(R.id.tv_newprice0);
					holder1.tv_oldprice0 = (TextView) convertView.findViewById(R.id.tv_oldprice0);
					holder1.tv_sellcount0 = (TextView) convertView.findViewById(R.id.tv_sellcount0);

					holder1.iv_type0.setVisibility(View.VISIBLE);
					convertView.setTag(holder1);
					break;
				case TYPE_3:
					LayoutInflater mInflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = mInflater2.inflate(R.layout.item_all_shop_list, parent, false);
					holder2 = new ViewHolder2();
					holder2.sname = (TextView) convertView.findViewById(R.id.tv_item_allshop_name);
					holder2.sdesc = (TextView) convertView.findViewById(R.id.tv_item_allshop_option);
					holder2.sdistance = (TextView) convertView.findViewById(R.id.tv_item_allshop_distance);
					holder2.sdisc = (TextView) convertView.findViewById(R.id.youhui_card_02);
					holder2.ssafe = (TextView) convertView.findViewById(R.id.youhui_card_01);
					holder2.sconsume = (TextView) convertView.findViewById(R.id.tv_item_allshop_count);
					holder2.image = (ImageView) convertView.findViewById(R.id.iv_item_allshop_list);
					holder2.iv_type0 = (ImageView) convertView.findViewById(R.id.iv_type0);

					holder2.tv_name0 = (TextView) convertView.findViewById(R.id.tv_name0);
					holder2.tv_newprice0 = (TextView) convertView.findViewById(R.id.tv_newprice0);
					holder2.tv_oldprice0 = (TextView) convertView.findViewById(R.id.tv_oldprice0);
					holder2.tv_sellcount0 = (TextView) convertView.findViewById(R.id.tv_sellcount0);
					holder2.iv_type1 = (ImageView) convertView.findViewById(R.id.iv_type1);

					holder2.tv_name1 = (TextView) convertView.findViewById(R.id.tv_name1);
					holder2.tv_newprice1 = (TextView) convertView.findViewById(R.id.tv_newprice1);
					holder2.tv_oldprice1 = (TextView) convertView.findViewById(R.id.tv_oldprice1);
					holder2.tv_sellcount1 = (TextView) convertView.findViewById(R.id.tv_sellcount1);
					holder2.iv_type0.setVisibility(View.VISIBLE);
					holder2.iv_type1.setVisibility(View.VISIBLE);
					convertView.setTag(holder2);
					break;

				}
			} else {
				switch (type) {
				case TYPE_1:
					holder = (ViewHolder) convertView.getTag();
					break;

				case TYPE_2:
					holder1 = (ViewHolder1) convertView.getTag();
					break;
				case TYPE_3:
					holder2 = (ViewHolder2) convertView.getTag();
					break;
				}
			}
			switch (type) {
			case TYPE_1:
				JSONObject item;
				try {
					item = m_array_list.getJSONObject(position);
					String sname = item.getString("sname");
					String sdesc = item.getString("sdesc");
					double sdistance = item.getDouble("sdistance");
					String sdisc = item.getString("sdisc");
					double ssafe = item.getDouble("ssafe");
					String sconsume = item.getString("sconsume");
					String image = item.getString("image");

					holder.sname.setText(sname);
					holder.sdesc.setText(sdesc);
					Log.e("distance", sdistance + "");
					// holder.sdistance.setText((sdistance.split("."))[0]+"m");
					holder.sdistance.setText(String.format("< %.2fkm", sdistance));
					holder.sdisc.setText(sdisc + "折起");
					holder.ssafe.setText("返利 " + (int) (ssafe * 100) + "%");
					holder.sconsume.setText("已售 " + sconsume);
					// Picasso.with(ShopActivity.this).load(image).placeholder(R.drawable.img_loading).into(holder.image);

					ImageLoaderHelper.displayImage(R.drawable.img_loading, holder.image, image);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;

			case TYPE_2:
				JSONObject item2;
				try {
					item2 = m_array_list.getJSONObject(position);
					String sname = item2.getString("sname");
					String sdesc = item2.getString("sdesc");
					double sdistance = item2.getDouble("sdistance");
					String sdisc = item2.getString("sdisc");
					double ssafe = item2.getDouble("ssafe");
					String sconsume = item2.getString("sconsume");
					String image = item2.getString("image");
					JSONObject type2item = (JSONObject) item2.getJSONArray("special").get(0);
					holder1.tv_name0.setText(type2item.getString("pname"));
					holder1.tv_newprice0.setText("现价：" + type2item.getString("kprice"));
					holder1.tv_oldprice0.setText("原价：" + type2item.getString("mprice"));
					holder1.tv_oldprice0.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					holder1.tv_sellcount0.setText("已售：" + type2item.getString("scount"));
					if (type2item.getInt("ptype") == 0) {
						holder1.iv_type0.setImageResource(R.drawable.ic_miao);

					} else {
						holder1.iv_type0.setImageResource(R.drawable.ic_tuan);
					}

					holder1.sname.setText(sname);
					holder1.sdesc.setText(sdesc);
					Log.e("distance", sdistance + "");
					// holder.sdistance.setText((sdistance.split("."))[0]+"m");
					holder1.sdistance.setText(String.format("< %.2fkm", sdistance));
					holder1.sdisc.setText(sdisc + "折起");
					holder1.ssafe.setText("返利 " + (int) (ssafe * 100) + "%");
					holder1.sconsume.setText("已售 " + sconsume);
					// Picasso.with(ShopActivity.this).load(image).placeholder(R.drawable.img_loading).into(holder1.image);
					ImageLoaderHelper.displayImage(R.drawable.img_loading, holder1.image, image);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case TYPE_3:
				JSONObject item3;
				try {
					item3 = m_array_list.getJSONObject(position);
					String sname = item3.getString("sname");
					String sdesc = item3.getString("sdesc");
					double sdistance = item3.getDouble("sdistance");
					String sdisc = item3.getString("sdisc");
					double ssafe = item3.getDouble("ssafe");
					String sconsume = item3.getString("sconsume");
					String image = item3.getString("image");

					JSONObject type3item = (JSONObject) item3.getJSONArray("special").get(0);
					holder2.tv_name0.setText(type3item.getString("pname"));
					holder2.tv_newprice0.setText("现价：" + type3item.getString("kprice"));
					holder2.tv_oldprice0.setText("原价：" + type3item.getString("mprice"));
					holder2.tv_oldprice0.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					holder2.tv_sellcount0.setText("已售：" + type3item.getString("scount"));
					if (type3item.getInt("ptype") == 0) {
						holder2.iv_type0.setImageResource(R.drawable.ic_miao);

					} else {
						holder2.iv_type0.setImageResource(R.drawable.ic_tuan);
					}
					JSONObject type3item2 = (JSONObject) item3.getJSONArray("special").get(1);

					holder2.tv_name1.setText(type3item2.getString("pname"));
					holder2.tv_newprice1.setText("现价：" + type3item2.getString("kprice"));
					holder2.tv_oldprice1.setText("原价：" + type3item2.getString("mprice"));
					holder2.tv_oldprice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					holder2.tv_sellcount1.setText("已售：" + type3item2.getString("scount"));
					if (type3item2.getInt("ptype") == 0) {
						holder2.iv_type1.setImageResource(R.drawable.ic_miao);

					} else {
						holder2.iv_type1.setImageResource(R.drawable.ic_tuan);
					}
					holder2.sname.setText(sname);
					holder2.sdesc.setText(sdesc);
					Log.e("distance", sdistance + "");
					// holder.sdistance.setText((sdistance.split("."))[0]+"m");
					holder2.sdistance.setText(String.format("< %.2fkm", sdistance));
					holder2.sdisc.setText(sdisc + "折起");
					holder2.ssafe.setText("返利 " + (int) (ssafe * 100) + "%");
					holder2.sconsume.setText("已售 " + sconsume);
					// Picasso.with(ShopActivity.this).load(image).placeholder(R.drawable.img_loading).into(holder2.image);
					ImageLoaderHelper.displayImage(R.drawable.img_loading, holder2.image, image);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}
			return convertView;

		}
	}

	public final class ViewHolder {
		public TextView sname;
		public TextView sdesc;
		public TextView sdistance;
		public TextView sdisc;
		public TextView ssafe;
		public TextView sconsume;
		public ImageView image;
		public LinearLayout item0;
		public LinearLayout item1;
		public int pos;

	}

	public final class ViewHolder1 {
		public TextView sname;
		public TextView sdesc;
		public TextView sdistance;
		public TextView sdisc;
		public TextView ssafe;
		public TextView sconsume;
		public ImageView image;
		public LinearLayout item0;
		public LinearLayout item1;
		public int pos;
		public ImageView iv_type0;
		public TextView tv_name0;
		public TextView tv_newprice0;
		public TextView tv_oldprice0;
		public TextView tv_sellcount0;

	}

	public final class ViewHolder2 {
		public TextView sname;
		public TextView sdesc;
		public TextView sdistance;
		public TextView sdisc;
		public TextView ssafe;
		public TextView sconsume;
		public ImageView image;
		public LinearLayout item0;
		public LinearLayout item1;
		public int pos;
		public ImageView iv_type1;
		public TextView tv_name1;
		public TextView tv_newprice1;
		public TextView tv_oldprice1;
		public TextView tv_sellcount1;
		public ImageView iv_type0;
		public TextView tv_name0;
		public TextView tv_newprice0;
		public TextView tv_oldprice0;
		public TextView tv_sellcount0;
	}

	class MyDialefAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_dialef_list.length();
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
			ImageView iv_item_dialef = (ImageView) view.findViewById(R.id.iv_item_dialef);
			try {
				String name = m_dialef_list.getJSONObject(position).getString("fstname");
				String image = m_dialef_list.getJSONObject(position).getString("icon");
				tv_item_dialft.setText(name);
				// Picasso.with(ShopActivity.this).load(image).placeholder(R.drawable.img_loading).into(iv_item_dialef);
				ImageLoaderHelper.displayImage(R.drawable.img_loading, iv_item_dialef, image);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return view;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_shop_shopkind:
			showShopKindPopuwindow(v);
			break;
		case R.id.ll_shop_location:
			showCirclePopuWindow(v);
			break;
		case R.id.ll_shop_distance:
			showDistancePopuwindow(v);
			break;

		case R.id.tv_dialog_distance_small:
			refreshList(1);
			((TextView) findViewById(R.id.tv_shop_distance)).setText("距离最近");
			dissmissPopuwindow();
			break;
		case R.id.tv_dialog_distance_big:
			refreshList(3);
			((TextView) findViewById(R.id.tv_shop_distance)).setText("最大返利");
			dissmissPopuwindow();
			break;
		case R.id.tv_dialog_distance_nomal:
			refreshList(4);
			((TextView) findViewById(R.id.tv_shop_distance)).setText("最大优惠");
			dissmissPopuwindow();
			break;
		case R.id.tv_dialog_3:
			refreshList(5);
			((TextView) findViewById(R.id.tv_shop_distance)).setText("评价最高");
			dissmissPopuwindow();
			break;
		case R.id.tv_dialog_4:
			refreshList(6);
			((TextView) findViewById(R.id.tv_shop_distance)).setText("人气最高");
			dissmissPopuwindow();
			break;
		case R.id.tv_dialog_5:
			refreshList(7);
			((TextView) findViewById(R.id.tv_shop_distance)).setText("最新发布");
			dissmissPopuwindow();
			break;
		case R.id.tv_dialog_0:
			refreshList(0);
			((TextView) findViewById(R.id.tv_shop_distance)).setText("智能排序");
			dissmissPopuwindow();
			break;
		case R.id.bt_shop_search:
			// startActivity(new Intent(this, SearchActivity.class));
			finish();

			break;
		case R.id.tv_shop_seckill:
			findViewById(R.id.v_shop_line).setVisibility(View.GONE);
			findViewById(R.id.v_shop_line0).setVisibility(View.VISIBLE);
			mIsKill = true;
			mKillAdapter = null;
			m_adapter = null;
			getData(true);
			break;

		case R.id.tv_shop_allshop:
			findViewById(R.id.ll_shop).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_sec).setVisibility(View.GONE);
			findViewById(R.id.v_shop_line).setVisibility(View.VISIBLE);
			findViewById(R.id.v_shop_line0).setVisibility(View.GONE);
			mIsKill = false;
			mKillAdapter = null;
			m_adapter = null;
			getData(false);
			break;

		case R.id.bt_shop_location:
			// startActivity(new Intent(this, MyCityListActivity.class));
			break;

		case R.id.ll_seckil_shopkind:

			showShopKindPopuwindowSec(v);

			break;
		case R.id.ll_seckil_youhui:
			refreshListSec(3);
			break;
		case R.id.ll_seckil_location:
			refreshListSec(1);

			break;
		}
	}

	class KillAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (m_array_list == null)
				return 0;
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
			KillHolder holder = null;
			if (convertView == null) {
				holder = new KillHolder();

				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.item_seckill_list, null);

				holder.name = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_name);
				holder.option = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_option);
				holder.distance = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_distance);
				holder.seckillprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_secprice);
				holder.nowprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_nowprice);
				holder.oldprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_oldprice);
				holder.sellcount = (TextView) convertView.findViewById(R.id.tv_item_seckill_sellcount);
				holder.img = (ImageView) convertView.findViewById(R.id.iv_item_seckill_list);

				convertView.setTag(holder);
			} else {
				holder = (KillHolder) convertView.getTag();
			}
			try {

				JSONObject item = m_array_list.getJSONObject(position);

				String pname = item.getString("pname");
				String pdesc = item.getString("pdesc");
				String kprice = item.getString("kprice");
				String mprice = item.getString("mprice");
				String scount = item.getString("scount");
				double distance = item.getDouble("distance");
				String image = item.getString("image");
				int ptype = item.getInt("ptype");
				holder.name.setText(pname);
				holder.option.setText(pdesc);
				if (ptype == 0) {
					holder.seckillprice.setText("秒杀价");
					holder.seckillprice.setBackgroundResource(R.drawable.bg_orange);
				} else {
					holder.seckillprice.setText("团购价");
					holder.seckillprice.setBackgroundResource(R.drawable.bg_slowblue2);
				}
				holder.nowprice.setText(kprice);
				holder.oldprice.setText("￥" + mprice);
				holder.oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				holder.sellcount.setText("已售：" + scount);
				holder.distance.setText(String.format("< %.2fkm", distance));
				// Picasso.with(ShopActivity.this).load(image).placeholder(R.drawable.img_loading).into(holder.img);
				ImageLoaderHelper.displayImage(R.drawable.img_loading, holder.img, image);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	public final class KillHolder {
		public TextView name;
		public TextView seckillprice;
		public TextView nowprice;
		public TextView oldprice;
		public TextView distance;
		public ImageView img;
		public TextView option;
		public TextView sellcount;
	}

	private void getKillData(boolean show) {
		if (mKillAdapter == null) {
			page = 1;
			m_array_list = new JSONArray();
			mKillAdapter = new KillAdapter();
			listview.setAdapter(mKillAdapter);
		}

		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				ptrlvHeadlineNews.onRefreshComplete();

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONArray array = obj.getJSONArray("items");
						int len = array.length();
						int count = m_array_list.length();
						for (int i = 0; i < len; i++) {
							m_array_list.put(i + count, array.getJSONObject(i));
						}
						mKillAdapter.notifyDataSetChanged();
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
				ptrlvHeadlineNews.onRefreshComplete();
			}
		};

		RequestParams params = new RequestParams();
		params.put("type", "wzreposity.speciallist");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			String cid = sp.getString("cid", "4301");

			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("city", cid);
			params.put("posx", sp.getString("lng", ""));
			params.put("posy", sp.getString("lat", ""));
			params.put("pindex", page + "");
			params.put("order", sort);
			params.put("telephone", obj.getString("Telephone"));
			if (stype != null && !"".equals(stype)) {
				params.put("stype", stype);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}

	private void refreshList(int sort) {
		this.sort = sort;
		m_adapter = null;
		mKillAdapter = null;
		m_array_list = new JSONArray();

		getData(true);

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

	}

	@SuppressWarnings("deprecation")
	private void showCirclePopuWindow(View ll) {
		dissmissPopuwindow();
		getCirclelistData(true);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.circlelist, null);
		RelativeLayout rl_cricle = (RelativeLayout) view.findViewById(R.id.tv_criclelist_change);
		rl_cricle.setVisibility(View.GONE);
		GridView gv_circle = (GridView) view.findViewById(R.id.gv_circle);
		circleGridVIewAdapter = new MyCircleGridVIewAdapter();
		gv_circle.setAdapter(circleGridVIewAdapter);
		gv_circle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					((TextView) findViewById(R.id.tv_shop_location)).setText(m_cricle_list.getJSONObject(arg2).getString("circlename"));
					circle = m_cricle_list.getJSONObject(arg2).getString("circleid");
					page = 1;
					m_array_list = new JSONArray();
					getData(true);
					m_adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				dissmissPopuwindow();

			}
		});
		view.findViewById(R.id.layout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dissmissPopuwindow();
			}
		});
		view.findViewById(R.id.tv_criclelist_change).setOnClickListener(this);

		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOutsideTouchable(false);
		popupWindow.setAnimationStyle(R.style.AnimTop2);
		popupWindow.showAsDropDown(ll, 0, 10);

	}

	class MyCircleGridVIewAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_cricle_list.length();
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
			View view = mInflater.inflate(R.layout.item_city_bt, null);
			final Button bt_city = (Button) view.findViewById(R.id.bt_city);

			try {
				bt_city.setTag(m_cricle_list.getJSONObject(position).getString("circleid"));
				bt_city.setText(m_cricle_list.getJSONObject(position).getString("circlename"));
			} catch (Exception e) {
				e.printStackTrace();
			}

			bt_city.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((TextView) findViewById(R.id.tv_shop_location)).setText(bt_city.getText().toString());
					sp.edit().putString("circleid", bt_city.getTag() + "").commit();
					circle = v.getTag() + "";

					page = 1;
					m_array_list = new JSONArray();
					getData(true);
					dissmissPopuwindow();
				}
			});

			return view;
		}
	}

	@SuppressWarnings("deprecation")
	private void showShopKindPopuwindow(View ll) {
		if (m_array_list != null && m_array_list.length() > 0)
			getDataShopKind(false);
		else
			getDataShopKind(true);

		dissmissPopuwindow();
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.dialog_shopkind_select, null);
		ListView lisiview = (ListView) view.findViewById(R.id.lv_dialog_shopkind_select_left);
		ListView lv_dialog_shopkind_select_right = (ListView) view.findViewById(R.id.lv_dialog_shopkind_select_right);
		lisiview.setAdapter(myShopkindAdapter);
		lv_dialog_shopkind_select_right.setAdapter(myShopkindAdapter2);
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.AnimTop2);
		popupWindow.showAsDropDown(ll, 0, 10);

	}

	private void showDistancePopuwindow(View ll) {
		dissmissPopuwindow();
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.dialog_distance_select, null);
		TextView tv1 = (TextView) view.findViewById(R.id.tv_dialog_distance_small);
		TextView tv2 = (TextView) view.findViewById(R.id.tv_dialog_distance_big);
		TextView tv3 = (TextView) view.findViewById(R.id.tv_dialog_distance_nomal);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		view.findViewById(R.id.tv_dialog_3).setOnClickListener(this);
		view.findViewById(R.id.tv_dialog_4).setOnClickListener(this);
		view.findViewById(R.id.tv_dialog_5).setOnClickListener(this);
		view.findViewById(R.id.tv_dialog_0).setOnClickListener(this);

		popupWindow = new PopupWindow(view, SystemUtils.getScreenWidth(this) / 3, LayoutParams.WRAP_CONTENT);
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
					// Picasso.with(ShopActivity.this).load(image).placeholder(R.drawable.img_loading).into(iv_item_dialef);
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
							((TextView) findViewById(R.id.tv_shop_shopkind)).setText("全部分类");
							stype = " ";
							page = 1;
							m_array_list = new JSONArray();
							getData(true);
							return;
						}
						Log.e("我点了第一个list", v.getTag() + "....");
						selectListleftpont = (Integer) v.getTag();

						m_shopkind_list2 = m_shopkind_list.getJSONObject((Integer) v.getTag() - 1).getJSONArray("data");

						if (m_shopkind_list2.length() <= 0) {
							dissmissPopuwindow();
							((TextView) findViewById(R.id.tv_shop_shopkind)).setText(m_shopkind_list.getJSONObject(p - 1).getString("name"));
							stype = m_shopkind_list.getJSONObject(p - 1).getString("id");
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
						stype = m_shopkind_list2.getJSONObject(p).getString("id");
						((TextView) findViewById(R.id.tv_shop_shopkind)).setText(m_shopkind_list2.getJSONObject(p).getString("name"));
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
		if (mIsKill) {
			getKillData(show);
			return;
		}

		if (m_adapter == null) {
			page = 1;
			m_array_list = new JSONArray();
			m_adapter = new MyAdapter();
			listview.setAdapter(m_adapter);
		}

		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				ptrlvHeadlineNews.onRefreshComplete();

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONArray array = obj.getJSONArray("items");
						int len = array.length();
						int count = m_array_list.length();
						for (int i = 0; i < len; i++) {
							m_array_list.put(i + count, array.getJSONObject(i));
						}
						m_adapter.notifyDataSetChanged();
						page++;
					} else {
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));
						m_adapter.notifyDataSetChanged();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable ble) {
				ptrlvHeadlineNews.onRefreshComplete();

			}
		};

		RequestParams params = new RequestParams();
		params.put("type", "wzreposity.shopsearch");
		try {
			String cid = getSharedPreferences("mgw_data", 0).getString("cid", "4301");

			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("city", cid);
			params.put("special", "1");
			params.put("pindex", page + "");
			params.put("posx", sp.getString("lng", ""));
			params.put("posy", sp.getString("lat", ""));
			params.put("telephone", obj.getString("Telephone"));
			params.put("sort", sort + "");
			if (skey != null && !"".equals(skey)) {
				params.put("skey", skey);
				Log.e("添加了关键字收索", skey);
			}
			if (stype != null && !"".equals(stype)) {
				params.put("stype", stype.trim());
				params.put("skey", "");
			}
			if (circle != null && !"".equals(circle)) {
				params.put("circle", circle);
			}
			// params.put("stype", stype);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	private void getDataShopKind(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("shopkind", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_shopkind_list = obj.getJSONArray("items");

						myShopkindAdapter.notifyDataSetChanged();
					}

					else {
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

	private void getCirclelistData(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("circlelist", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_cricle_list = obj.getJSONArray("items");
						circleGridVIewAdapter.notifyDataSetChanged();
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
		params.put("type", "wzreposity.circlelist");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			String cid = sp.getString("cid", "4301");
			if (cid == null || "".equals(cid)) {
				cid = "4301";

			}
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("city", cid);
			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	private void refreshListSec(int order) {
		pageSec = 1;
		m_array_list_sec = new JSONArray();

		getData(true);

		dissmissPopuwindow();
	}

	class MyAdapterSec extends BaseAdapter {
		@Override
		public int getCount() {
			return m_array_list_sec.length();
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
			ViewHolderSec holder = null;
			if (convertView == null) {
				holder = new ViewHolderSec();

				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.item_seckill_list, null);

				holder.name = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_name);
				holder.option = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_option);
				holder.distance = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_distance);
				holder.seckillprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_secprice);
				holder.nowprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_nowprice);
				holder.oldprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_oldprice);
				holder.sellcount = (TextView) convertView.findViewById(R.id.tv_item_seckill_sellcount);
				holder.img = (ImageView) convertView.findViewById(R.id.iv_item_seckill_list);

				holder.pos = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderSec) convertView.getTag();
				holder.pos = position;
			}
			try {

				JSONObject item = m_array_list_sec.getJSONObject(position);

				String pname = item.getString("pname");
				String pdesc = item.getString("pdesc");
				String kprice = item.getString("kprice");
				String sprice = item.getString("sprice");
				String mprice = item.getString("mprice");
				String scount = item.getString("scount");
				String image = item.getString("image");
				double distance = item.getDouble("distance");
				int ptype = item.getInt("ptype");
				Log.e("团购秒杀type", "type::::" + ptype);
				holder.name.setText(pname);
				holder.option.setText(pdesc);
				if (ptype == 0) {
					holder.seckillprice.setText("秒杀价" + kprice + "元");
					holder.seckillprice.setBackgroundResource(R.drawable.bg_orange);
				} else {
					holder.seckillprice.setText("团购价" + kprice + "元");
					holder.seckillprice.setBackgroundResource(R.drawable.bg_slowblue2);
				}
				holder.nowprice.setText(sprice);
				holder.oldprice.setText(mprice);
				holder.oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				holder.sellcount.setText("已售：" + scount);
				holder.distance.setText((int) distance + "m");
				// Picasso.with(ShopActivity.this).load(image).placeholder(R.drawable.img_loading).into(holder.img);
				ImageLoaderHelper.displayImage(R.drawable.img_loading, holder.img, image);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	public final class ViewHolderSec {
		public TextView name;
		public TextView seckillprice;
		public TextView nowprice;
		public TextView oldprice;
		public TextView distance;
		public ImageView img;
		public TextView option;
		public TextView sellcount;
		public int pos;
	}

	class MyShopkindAdapterSec extends BaseAdapter {
		@Override
		public int getCount() {
			return m_shopkind_list_sec.length() + 1;
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

					String name = m_shopkind_list_sec.getJSONObject(position - 1).getString("name");
					String image = m_shopkind_list_sec.getJSONObject(position - 1).getString("icon");
					tv_item_dialft.setText(name);
					// Picasso.with(ShopActivity.this).load(image).placeholder(R.drawable.img_loading).into(iv_item_dialef);
					ImageLoaderHelper.displayImage(R.drawable.img_loading, iv_item_dialef, image);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (position == selectListleftpontSec) {
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
							((TextView) findViewById(R.id.tv_seckill_shopkind)).setText("全部分类");
							stypeSec = "";
							pageSec = 1;
							m_array_list_sec = new JSONArray();
							getData(true);
							m_adapter_sec.notifyDataSetChanged();
							return;
						}
						Log.e("我点了第一个list", v.getTag() + "....");
						selectListleftpontSec = (Integer) v.getTag();

						m_shopkind_list2_sec = m_shopkind_list_sec.getJSONObject((Integer) v.getTag() - 1).getJSONArray("data");

						if (m_shopkind_list2_sec.length() <= 0) {
							dissmissPopuwindow();
							((TextView) findViewById(R.id.tv_seckill_shopkind)).setText(m_shopkind_list_sec.getJSONObject(p - 1).getString("name"));
							stype = m_shopkind_list_sec.getJSONObject(p - 1).getString("id");
							pageSec = 1;
							m_array_list_sec = new JSONArray();
							getData(true);
							m_adapter_sec.notifyDataSetChanged();
							return;

						}

						myShopkindAdapterSec2.notifyDataSetChanged();
						myShopkindAdapterSec.notifyDataSetChanged();

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			});
			return view;
		}
	}

	class MyShopkindAdapterSec2 extends BaseAdapter {
		@Override
		public int getCount() {
			return m_shopkind_list2_sec.length();
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
				String name = m_shopkind_list2_sec.getJSONObject(position).getString("name");
				String image = m_shopkind_list2_sec.getJSONObject(position).getString("icon");
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
						stypeSec = m_shopkind_list2_sec.getJSONObject(p).getString("id");
						((TextView) findViewById(R.id.tv_seckill_shopkind)).setText(m_shopkind_list2_sec.getJSONObject(p).getString("name"));
						dissmissPopuwindow();
						pageSec = 1;
						m_array_list_sec = new JSONArray();
						getData(true);
						m_adapter_sec.notifyDataSetChanged();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
			return view;
		}
	}

	private void getDataShopKindSec(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("shopkind", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_shopkind_list_sec = obj.getJSONArray("items");

						myShopkindAdapterSec.notifyDataSetChanged();
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

	@SuppressWarnings("deprecation")
	private void showShopKindPopuwindowSec(View ll) {
		if (m_array_list_sec.length() > 0)
			getDataShopKindSec(false);
		else
			getDataShopKindSec(true);

		dissmissPopuwindow();
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.dialog_shopkind_select, null);
		ListView lisiview = (ListView) view.findViewById(R.id.lv_dialog_shopkind_select_left);
		ListView lv_dialog_shopkind_select_right = (ListView) view.findViewById(R.id.lv_dialog_shopkind_select_right);
		lisiview.setAdapter(myShopkindAdapterSec);
		lv_dialog_shopkind_select_right.setAdapter(myShopkindAdapterSec2);
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, 400);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.AnimTop2);
		popupWindow.showAsDropDown(ll, 0, 10);
	}

	private void getDataSec(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				PullToRefreshListView listviewSec = (PullToRefreshListView) findViewById(R.id.listview);
				listviewSec.onRefreshComplete();

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {

						JSONArray array = obj.getJSONArray("items");
						for (int i = 0; i < array.length(); i++) {

							m_array_list_sec.put(array.getJSONObject(i));
							Log.e("sid", array.getJSONObject(i).getString("sid") + ",,,,mmm");
						}

						m_adapter_sec.notifyDataSetChanged();
						pageSec++;
						Log.e("pagesec", pageSec + "...");

						// int count = m_array_list.length();
						// for (int i = 0; i < count; i++) {
						// JSONObject item = m_array_list.getJSONObject(i);
						//
						// String pname = item.getString("pname");
						// String kprice = item.getString("kprice");
						// String sprice = item.getString("sprice");
						// String mprice = item.getString("mprice");
						// String scount = item.getString("scount");
						// String image = item.getString("image");
						// Log.e("result", pname + ",," + kprice + ",,"
						// + sprice + ",," + mprice + ",," + scount
						// + ",," + image);
						//
						// }

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
				PullToRefreshListView listviewSec = (PullToRefreshListView) findViewById(R.id.listview);
				listviewSec.onRefreshComplete();

			}
		};

		RequestParams params = new RequestParams();
		params.put("type", "wzreposity.speciallist");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			String cid = sp.getString("cid", "4301");

			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("city", cid);
			params.put("posx", sp.getString("lng", ""));
			params.put("posy", sp.getString("lat", ""));
			params.put("pindex", page + "");
			// params.put("order", order);
			params.put("telephone", obj.getString("Telephone"));
			if (stypeSec != null && !"".equals(stypeSec)) {
				params.put("stype", stypeSec);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}

	// sec///////////////////////////////////////////////

}
