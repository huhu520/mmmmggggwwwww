package com.mgw.member.ui.activity.cityleague;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.constant.PulicData;
import com.mgw.member.http.WZHttp;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.uitls.DBLoad;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;

public class CityleagueActivity extends BaseActivity2 implements OnClickListener {
	public static double m_lng = 0.0;
	public static double m_lat = 0.0;
	public static String m_city = "";

	private List<View> pageViews;
	private ViewPager viewPager;
	ArrayList<JSONObject> m_array_list = new ArrayList<JSONObject>();
	JSONArray m_cricle_list = new JSONArray();
	ArrayList<JSONObject> m_firstshoptype_list = new ArrayList<JSONObject>();
	ArrayList<JSONObject> m_hotshoptype_list = new ArrayList<JSONObject>();
	/** 将小圆点的图片用数组表示 */
	private ImageView[] imageViews;
	// 包裹小圆点的LinearLayout
	private ViewGroup viewPoints;

	private ImageView imageView;
	private SharedPreferences sp;
	private MyAdapter m_adapter;
	private PopupWindow popupWindow;
	private String cid = "4301";
	private String circle = "";
	private TextView tv_city;
	Map<String, String> allcityMap;
	private static int Lastpage = 3;
	boolean isGoOtherAvtivity = false;

	// 声音
	private AudioManager audioManager;
	int page = 1;
	/*
	 * 百度地图定位
	 */
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = null;
	View mHeadView = null;
	private MyCircleGridVIewAdapter circleGridVIewAdapter;

	/*
	 * 百度定位
	 */
	private void GetLoaction() {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		myListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setScanSpan(30000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		mLocationClient.requestLocation();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cityleague);
		/*
		 * 1、定位 2、初始化界面 3、加载上面数据。 4、加载下面的数据。 5、加载列表数据
		 */
		GetLoaction();
	}

	/**
	 * 初始化界面
	 */
	private void Instial() {
		mInstial = true;
		LayoutInflater inflater2 = LayoutInflater.from(this);

		mHeadView = inflater2.inflate(R.layout.home_list_headview, null);

		// initShowShare();
		tv_city = (TextView) findViewById(R.id.tv_cityserach_select);
		findViewById(R.id.et_search).setOnClickListener(this);
		findViewById(R.id.ll_cityserach_select).setOnClickListener(this);

		final PullToRefreshListView ptrlvHeadlineNews = (PullToRefreshListView) findViewById(R.id.home_listview);
		sp = getSharedPreferences("mgw_data", 0);

		ptrlvHeadlineNews.setMode(Mode.DISABLED);
		ptrlvHeadlineNews.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					Log.e("position", "p:::::::::" + arg2 + "");

					String sid = m_array_list.get(arg2 - 2).getString("sid");
					Log.e("positionsiddddd", "sid::::::::" + sid + "");
					Intent intent = new Intent(CityleagueActivity.this, ShopDetailAndIntroduceActivity.class);
					intent.putExtra("sid", sid);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		mHeadView.findViewById(R.id.bt_home_seckill).setOnClickListener(this);
		mHeadView.findViewById(R.id.bt_home_coupon).setOnClickListener(this);
		ptrlvHeadlineNews.getRefreshableView().addHeaderView(mHeadView);
		m_adapter = new MyAdapter();
		ptrlvHeadlineNews.getRefreshableView().setAdapter(m_adapter);

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

		getDatafirstshoptype();
		getDatahostshoptype();
		/*
		 * 设置当前城市
		 */
		SetCityName();
		// getData(true);
	}

	/**
	 * 百度定位
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || location.getLongitude() == 0.0f) {
				CityleagueActivity.m_lng = 0;
				CityleagueActivity.m_lat = 0;
				mLocationClient.stop();
				mLocationClient.unRegisterLocationListener(myListener);
				DealGetCityError();
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
				return;
			}

			CityleagueActivity.m_lng = location.getLongitude();
			CityleagueActivity.m_lat = location.getLatitude();
			CityleagueActivity.m_city = location.getCity();
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(myListener);

			SharedPreferences.Editor sharedata = getSharedPreferences("mgw_data", 0).edit();
			sharedata.putString("lng", String.valueOf(CityleagueActivity.m_lng));
			sharedata.putString("lat", String.valueOf(CityleagueActivity.m_lat));
			if (CityleagueActivity.m_city != null) {
				sharedata.putString("city", CityleagueActivity.m_city);
				sharedata.putString("currentcityname", CityleagueActivity.m_city);
				sharedata.putString("currentcityID", "");
			}
			/*
			 * 1、设置本地城市编号 2、如果本地城市编号获取到了那么所在城市编号也获取到了。
			 */
			sharedata.commit();
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			getDataAllCity(true);
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(myListener);

		}
	}

	/**
	 * 设置本地城市编号
	 */
	private void SetCurrentCityCode() {
		SharedPreferences oCurrentsp = getSharedPreferences("mgw_data", 0);
		String sql = "select * from city where cname='" + oCurrentsp.getString("currentcityname", "") + "'";
		JSONObject obj = BaseApplication.getApplication().GetDbhandler().GetDataFromLocalDB2Dictionary(sql);
		if (obj != null) {
			try {
				oCurrentsp.edit().putString("currentcityID", obj.getString("cid")).commit();
				oCurrentsp.edit().putString("cid", obj.getString("cid")).commit();
				cid = obj.getString("cid");
				Instial();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// 未找到城市编号
				DealGetCityError();

			}
		} else {
			// 未找到城市编号
			DealGetCityError();

		}
	}

	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}
	}

	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.dot_s);
				// 不是当前选中的page，其小圆点设置为未选中的状态
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.dot);
				}
			}
		}
	}

	/** 点击圆圈图标打开activity的方法 */
	void startShop(String fstid, String name) {
		Intent intent = new Intent(this, ShopActivity.class);
		intent.putExtra("stype", fstid);
		intent.putExtra("name", name);
		intent.putExtra("type", 0);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}

	@Override
	public void onClick(View v) {

		int viewId = v.getId();

		// super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_home_seckill: {
			Intent intent = new Intent(this, ShopActivity.class);
			intent.putExtra("type", 3);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		}

			break;
		case R.id.bt_home_coupon: {

			Intent intent = new Intent(this, VipmaterialActivity.class);

			startActivity(intent);
			break;
		}
		case R.id.et_search:
			startActivity(new Intent(this, SearchActivity.class));

			break;

		case R.id.ll_cityserach_select:
			showPopuWindowInputNum(v);
			break;
		case R.id.iv_item_home_more:
			startActivity(new Intent(this, AllTypeActivity.class));
			break;
		case R.id.iv_item_home_fastfood:
			try {
				startShop(m_firstshoptype_list.get(1).getString("fstid"), m_firstshoptype_list.get(1).getString("fstname"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case R.id.iv_item_home_fun:
			try {
				startShop(m_firstshoptype_list.get(2).getString("fstid"), m_firstshoptype_list.get(2).getString("fstname"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		case R.id.iv_item_home_food:

			try {
				startShop(m_firstshoptype_list.get(0).getString("fstid"), m_firstshoptype_list.get(0).getString("fstname"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case R.id.tv_criclelist_change:
			dissmissPopuwindow();
			Intent intent = new Intent(this, MyCityListActivity.class);
			startActivityForResult(intent, 0);

			break;
		}

	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_array_list.size();
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
				convertView = mInflater.inflate(R.layout.item_home_list, null);

				holder.sname = (TextView) convertView.findViewById(R.id.tv_item_home_list_shopname);
				holder.sdesc = (TextView) convertView.findViewById(R.id.tv_item_home_list_option);
				holder.sdistance = (TextView) convertView.findViewById(R.id.tv_item_home_distance);
				holder.sdisc = (TextView) convertView.findViewById(R.id.youhui_card_02);
				holder.ssafe = (TextView) convertView.findViewById(R.id.youhui_card_01);
				holder.sconsume = (TextView) convertView.findViewById(R.id.tv_item_home_list_count);
				holder.image = (ImageView) convertView.findViewById(R.id.iv_item_home_list);

				holder.pos = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.pos = position;
			}
			try {

				JSONObject item = m_array_list.get(position);

				String sid = item.getString("sid");
				Log.e("获取的sid", sid);

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

				ImageLoaderHelper.displayImage(R.drawable.img_loading, holder.image, image);
				// Picasso.with(CityleagueActivity.this).load(image).into(holder.image);
			} catch (Exception e) {
				e.printStackTrace();
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
		public int pos;
	}

	private ProgressDialog progressDialog = null;

	/** 获取商家数据（listview的数据） */
	private void getData(boolean show) {
		if (!mgetFinished)
			return;
		mgetFinished = false;
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}

		if (page == 1)
			m_array_list.clear();

		/*
		 * MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
		 * 
		 * @Override public void onSuccess(String response) {
		 * super.onSuccess(response); PullToRefreshListView listview =
		 * (PullToRefreshListView) findViewById(R.id.home_listview);
		 * listview.onRefreshComplete(); try { JSONObject obj = new
		 * JSONObject(response); Log.e("json", obj.toString()); if
		 * (obj.getInt("flag") == 0) { JSONArray array =
		 * obj.getJSONArray("items"); for (int i = 0; i < array.length(); i++) {
		 * 
		 * m_array_list.add(array.getJSONObject(i));
		 * 
		 * } page ++; m_adapter.notifyDataSetChanged();
		 * 
		 * } else { ToastUtil.showToastWithAlertPic(obj.getString("msg"));
		 * m_adapter.notifyDataSetChanged(); } } catch (JSONException e) {
		 * e.printStackTrace(); } }
		 * 
		 * public void onFailure(Throwable ble) { PullToRefreshListView listview
		 * = (PullToRefreshListView) findViewById(R.id.listview);
		 * listview.onRefreshComplete(); } };
		 * 
		 * RequestParams params = new RequestParams(); params.put("type",
		 * "wzreposity.shopsearch"); try { sp = getSharedPreferences("mgw_data",
		 * 0); JSONObject obj = new JSONObject(getSharedPreferences("mgw_data",
		 * Context.MODE_PRIVATE).getString("mgw_data", "")); String cid =
		 * sp.getString("cid", "4301"); params.put("userid",
		 * obj.getString("UserID")); params.put("serial",
		 * obj.getString("serial")); params.put("special", "0");
		 * params.put("city", cid); params.put("pindex", page + "");
		 * params.put("posx", sp.getString("lng", "")); params.put("posy",
		 * sp.getString("lat", "")); params.put("telephone",
		 * obj.getString("Telephone"));
		 * 
		 * if(circle!=null&&!"".equals(circle)){ params.put("circle", circle); }
		 * } catch (JSONException e1) { e1.printStackTrace(); }
		 * 
		 * MgqRestClient.get("http://Android2.mgw.cc/index.aspx", params,
		 * loginHandler);
		 */

		if (page == 1)
			m_array_list.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj;
				try {
					try {
						obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));

						obj = WZHttp.Getshopsearch(obj.getString("UserID"), obj.getString("serial"), cid, page + "", "0", sp.getString("lng", ""), sp.getString("lat", ""), "", "", "1", "");
						if (obj != null && obj.getInt("flag") == 0) {
							JSONArray jarr = obj.getJSONArray("items");
							if (jarr != null && jarr.length() > 0) {
								for (int i = 0; i < jarr.length(); i++) {
									obj = jarr.getJSONObject(i);
									m_array_list.add(obj);
									/*
									 * sql =
									 * "insert into shopFirst (sid,rtype,sdesc,sposx,sposy,sdistance,sdisc,ssafe,"
									 * +
									 * "sconsume,image,score,cityid) values ('"
									 * +obj
									 * .getString("sid")+"','"+obj.getString(
									 * "rtype")+"'," +
									 * "'"+obj.getString("sdesc")
									 * +"','"+obj.getString("sposx")+"'" +
									 * ",'"+obj
									 * .getString("sposy")+"','"+obj.getString
									 * ("sdistance")+"'"+
									 * ",'"+obj.getString("sdisc"
									 * )+"','"+obj.getString("ssafe")+"'"+
									 * ",'"+obj
									 * .getString("sconsume")+"','"+obj.
									 * getString ("image")+"'"+
									 * ",'"+obj.getString("score")+"',"+cid+")";
									 * GlobelElements
									 * .getInstance().GetDbhandler().
									 * LocalExecuteUpdate(sql);
									 */
								}
								page++;

								mgetFinished = true;
								m_handler.sendEmptyMessage(2);
							} else {
								mgetFinished = true;
								Message msg = new Message();
								msg.obj = obj.getString("msg");
								msg.what = Lastpage;
								m_handler.sendMessage(msg);
							}
						} else {

							if (obj != null && obj.getInt("flag") == 10) {
								// 如果返回的结果是，没有找到符合条件的商家让切换城市的时候需要再次刷新（不然刷新不了）
								// if (obj.getInt("flag") == 10) {
								// shouldflush = true;
								// }
								Message msg = new Message();
								msg.obj = obj.getString("msg");
								msg.what = Lastpage;
								m_handler.sendMessage(msg);
							} else if (obj != null) {
								Message msg = new Message();
								msg.obj = obj.getString("msg");
								msg.what = Lastpage;
								m_handler.sendMessage(msg);
							} else {
								m_handler.sendEmptyMessage(PulicData.NET_ERR);
							}

						}
					} catch (JSONException e) {
						m_handler.sendEmptyMessage(2);
						m_handler.sendEmptyMessage(PulicData.NET_ERR);
					}

				} catch (Exception ex) {
				}
				mgetFinished = true;
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
			}
		}).start();
	}

	void setFirstContent() {

		if (m_firstshoptype_list.size() < 3)
			return;

		mHeadView.findViewById(R.id.top).setVisibility(View.VISIBLE);

		try {
			String image01 = m_firstshoptype_list.get(0).getString("icon");
			String tv01 = m_firstshoptype_list.get(0).getString("fstname");
			((TextView) mHeadView.findViewById(R.id.tv_item_home_01)).setText(tv01);
			ImageLoaderHelper.displayImage(R.drawable.img_loading, ((ImageView) mHeadView.findViewById(R.id.iv_item_home_food)), image01);
			// Picasso.with(CityleagueActivity.this).load(image01).into((ImageView)
			// mHeadView.findViewById(R.id.iv_item_home_food));
			String image02 = m_firstshoptype_list.get(1).getString("icon");
			String tv02 = m_firstshoptype_list.get(1).getString("fstname");
			((TextView) mHeadView.findViewById(R.id.tv_item_home_02)).setText(tv02);
			ImageLoaderHelper.displayImage(R.drawable.img_loading, ((ImageView) mHeadView.findViewById(R.id.iv_item_home_fastfood)), image02);
			// Picasso.with(CityleagueActivity.this).load(image02).into((ImageView)
			// mHeadView.findViewById(R.id.iv_item_home_fastfood));
			String tv03 = m_firstshoptype_list.get(2).getString("fstname");
			String image03 = m_firstshoptype_list.get(2).getString("icon");
			((TextView) mHeadView.findViewById(R.id.tv_item_home_03)).setText(tv03);
			ImageLoaderHelper.displayImage(R.drawable.img_loading, ((ImageView) mHeadView.findViewById(R.id.iv_item_home_fun)), image03);
			// Picasso.with(CityleagueActivity.this).load(image03).into((ImageView)
			// mHeadView.findViewById(R.id.iv_item_home_fun));
			mHeadView.findViewById(R.id.iv_item_home_food).setOnClickListener(CityleagueActivity.this);
			mHeadView.findViewById(R.id.iv_item_home_fastfood).setOnClickListener(CityleagueActivity.this);
			mHeadView.findViewById(R.id.iv_item_home_fun).setOnClickListener(CityleagueActivity.this);
			mHeadView.findViewById(R.id.iv_item_home_more).setOnClickListener(CityleagueActivity.this);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	Handler m_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:
				ReturnSecond();
				break;
			case -2:
				ReturnFirst();
				break;
			case 0:
				setFirstContent();
				break;
			case 1:
				setHostContent();
				break;

			case 2:
				m_adapter.notifyDataSetChanged();
				break;
			case 3:
				// ToastUtil.showToastWithAlertPic(msg.obj.toString());
				Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();

				/*
				 * Toast.makeText(BussinessActivity2.this, msg.obj.toString(),
				 * Toast.LENGTH_LONG).show();
				 * //ToastUtil.showToastWithAlertPic(msg.obj.toString());
				 * if(msg.obj.toString().contains("重新登")) {
				 * SharedPreferences.Editor sharedata =
				 * getSharedPreferences("mgw_data", 0).edit();
				 * sharedata.putString("mgw_data","");
				 * sharedata.putString("mgw_pwd","");
				 * //sharedata.putString("mgw_account",""); sharedata.commit();
				 * 
				 * GlobelElements.getInstance().logout();
				 * 
				 * GlobelElements.getInstance().m_user_id = "";
				 * if(MainActivity.s_Instance != null)
				 * MainActivity.s_Instance.finish(); startActivity(new
				 * Intent(BussinessActivity2.this,LoginActivity.class));
				 * finish(); }
				 */
				break;
			case PulicData.NET_ERR:
				Toast.makeText(CityleagueActivity.this, "网络连接不可用。", Toast.LENGTH_LONG).show();
				// ToastUtil.showToastWithAlertPic("网络连接不可用");
				m_adapter.notifyDataSetChanged();
				break;
			case PulicData.SERVER_ERR:
				Toast.makeText(CityleagueActivity.this, "服务器维护中。", Toast.LENGTH_LONG).show();
				// ToastUtil.showToastWithAlertPic("服务器维护中");
				m_adapter.notifyDataSetChanged();
				break;
			case PulicData.LOCAL_CITY:
				SetCurrentCityCode();
				break;
			}
		}
	};

	private void DealGetCityError() {
		Toast.makeText(getApplicationContext(), "对不起，无法确定您当前所在的城市！", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(CityleagueActivity.this, MyCityListActivity.class);
		startActivityForResult(intent, 0);
		startActivity(new Intent(this, MyCityListActivity.class));
	}

	@Override
	public void onActivityResult(int rc, int qc, Intent data) {
		super.onActivityResult(rc, qc, data);
		if (data != null) {
			if (data.hasExtra("result")) {
				// String str = data.getStringExtra("result");
				if (!mInstial) {
					// shouldflush = false;
					m_handler.sendEmptyMessage(-2);
					// ReturnFirst();
				} else {
					isGoOtherAvtivity = true;
					m_handler.sendEmptyMessage(-1);
					// ReturnSecond();
				}
			}
		}
	}

	Boolean mInstial = false;

	// Boolean shouldflush = false;

	private void ReturnFirst() {
		sp = getSharedPreferences("mgw_data", 0);
		if (sp != null) {
			Instial();
		}
	}

	private void ReturnSecond() {

		sp = getSharedPreferences("mgw_data", 0);
		if (sp != null) {
			tv_city.setText(sp.getString("city", "长沙市"));
			cid = sp.getString("cid", "4301");
			Log.e("onresume的获取城市", cid);

			if (isGoOtherAvtivity) {
				page = 1;
				circle = "";
				m_array_list.clear();
				m_adapter.notifyDataSetChanged();
				// m_handler.sendEmptyMessage(2);
				getData(true);
			}
			isGoOtherAvtivity = false;
		} else {
			Toast.makeText(CityleagueActivity.this, "网络连接不可用", Toast.LENGTH_LONG).show();
			// ToastUtil.showToastWithAlertPic("网络连接不可用");
			DealGetCityError();
		}
	}

	private void getDatafirstshoptype() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj;
				try {
					obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
					obj = WZHttp.Getfirstshoptype(obj.getString("UserID"), obj.getString("serial"));
					if (obj != null && obj.getInt("flag") == 0) {
						JSONArray jarr = obj.getJSONArray("items");
						if (jarr != null && jarr.length() > 0) {
							String sql = "delete from shopfirstType";
							BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(sql);
							m_firstshoptype_list.clear();
							for (int i = 0; i < jarr.length(); i++) {
								obj = jarr.getJSONObject(i);
								m_firstshoptype_list.add(obj);
								sql = "insert into shopfirstType (fstid,fstname,icon) values ('" + obj.getString("fstid") + "'," + "'" + obj.getString("fstname") + "','" + obj.getString("icon")
										+ "')";
								BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(sql);
							}
							m_handler.sendEmptyMessage(0);
						} else {
							getLocalFist();
							m_handler.sendEmptyMessage(0);
						}
					} else {
						getLocalFist();
						m_handler.sendEmptyMessage(0);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					getLocalFist();
					m_handler.sendEmptyMessage(0);
				}

			}
		}).start();
	}

	void getLocalFist() {
		String sql = "select * from shopfirstType";
		BaseApplication.getApplication().GetDbhandler().GetDataFromLocalDB(m_firstshoptype_list, sql);
	}

	void setHostContent() {
		if (pageViews == null) {
			pageViews = new ArrayList<View>();
			viewPager = (ViewPager) mHeadView.findViewById(R.id.vp_home_options);
		}
		pageViews.removeAll(pageViews);
		// 从指定的XML文件加载视图

		// 实例化小圆点的linearLayout和viewpager
		viewPoints = (ViewGroup) findViewById(R.id.viewGroup);
		if (m_hotshoptype_list.size() == 0)
			return;
		try {
			viewPager.setVisibility(View.VISIBLE);
			mHeadView.findViewById(R.id.ll_home_points).setVisibility(View.VISIBLE);
			LayoutInflater inflater = LayoutInflater.from(CityleagueActivity.this);
			int count = m_hotshoptype_list.size();
			View view;
			LinearLayout lvItem = null;
			// 创建imageviews数组，大小是要显示的图片的数量

			for (int i = 0; i < count; i++) {
				final JSONObject item = m_hotshoptype_list.get(i);
				if (i % 4 == 0) {
					view = inflater.inflate(R.layout.item_home_viewpaper01, null);

					pageViews.add(view);
					lvItem = (LinearLayout) view.findViewById(R.id.ll_viewpaper_item01);
				}
				LinearLayout omy = (LinearLayout) inflater.inflate(R.layout.secondshoptype, null);

				ImageView imageview = (ImageView) omy.findViewById(R.id.img_0);
				TextView tv = (TextView) omy.findViewById(R.id.tv_0);
				ImageLoaderHelper.displayImage(R.drawable.img_loading, imageview, item.getString("icon"));
				// Picasso.with(CityleagueActivity.this).load(item.getString("icon")).into(imageview);
				tv.setText(item.getString("fstname"));
				omy.setTag(item);
				omy.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {

						int eventaction = event.getAction();
						switch (eventaction) {
						case MotionEvent.ACTION_DOWN: // touch down so check if
														// the
							return true;
						case MotionEvent.ACTION_UP:
							// audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
							try {
								startShop(item.getString("fstid"), item.getString("fstname"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return true;
						case MotionEvent.ACTION_CANCEL:
							break;
						}
						return false;
					}
				});
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.weight = 1;
				lvItem.addView(omy, layoutParams);
			}
			imageViews = new ImageView[pageViews.size()];

			// 添加小圆点的图片
			for (int i = 0; i < pageViews.size(); i++) {
				imageView = new ImageView(CityleagueActivity.this);
				// 设置小圆点imageview的参数
				imageView.setLayoutParams(new LayoutParams(10, 10));// 创建一个宽高均为20
																	// 的布局

				// imageView.setPadding(30, 0, 30, 0);
				// 将小圆点layout添加到数组中
				imageViews[i] = imageView;

				// 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
				if (i == 0) {
					imageViews[i].setBackgroundResource(R.drawable.dot_s);
				} else {
					imageViews[i].setBackgroundResource(R.drawable.dot);
				}
				LinearLayout.LayoutParams layoutParamsa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParamsa.leftMargin = 20;
				layoutParamsa.rightMargin = 20;
				// 将imageviews添加到小圆点视图组
				viewPoints.addView(imageViews[i], layoutParamsa);
			}
			// 显示滑动图片的视图

			viewPager.setAdapter(new GuidePageAdapter());
			viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getDatahostshoptype() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj;
				try {
					obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
					obj = WZHttp.GetHostshoptype(obj.getString("UserID"), obj.getString("serial"));
					if (obj != null && obj.getInt("flag") == 0) {
						JSONArray jarr = obj.getJSONArray("items");
						if (jarr != null && jarr.length() > 0) {
							String sql = "delete from shopsecondType";
							BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(sql);
							m_hotshoptype_list.clear();
							for (int i = 0; i < jarr.length(); i++) {
								obj = jarr.getJSONObject(i);
								m_hotshoptype_list.add(obj);
								sql = "insert into shopsecondType (fstid,fstname,icon) values ('" + obj.getString("fstid") + "'," + "'" + obj.getString("fstname") + "','" + obj.getString("icon")
										+ "')";
								BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(sql);
							}
							m_handler.sendEmptyMessage(1);
						} else {
							GetLocalHost();
							m_handler.sendEmptyMessage(1);
						}
					} else {
						GetLocalHost();
						m_handler.sendEmptyMessage(1);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					GetLocalHost();
					m_handler.sendEmptyMessage(1);
				}

			}
		}).start();
	}

	private void GetLocalHost() {
		String sql = "select * from shopsecondType";
		BaseApplication.getApplication().GetDbhandler().GetDataFromLocalDB(m_hotshoptype_list, sql);
	}

	/** 获取popupwindow所需的城市数据 */
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

	private void SetCityName() {
		// sp = getSharedPreferences("mgw_data", 0);
		tv_city.setText(sp.getString("city", "长沙市"));
		cid = sp.getString("cid", "4301");
		getData(true);
	}

	/*
	 * 获取所有城市数据
	 */
	private void getDataAllCity(boolean show) {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "", "正在加载中...", true, false);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj;
				try {
					if (DBLoad.IsLoadCity) {
						if (progressDialog != null) {
							progressDialog.dismiss();
							progressDialog = null;
						}
						m_handler.sendEmptyMessage(PulicData.LOCAL_CITY);

					} else {
						obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
						DBLoad.LoadAllCity(obj.getString("UserID"), obj.getString("serial"));
						if (progressDialog != null) {
							progressDialog.dismiss();
							progressDialog = null;
						}
						m_handler.sendEmptyMessage(PulicData.LOCAL_CITY);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					m_handler.sendEmptyMessage(PulicData.LOCAL_CITY);
					if (progressDialog != null) {
						progressDialog.dismiss();
						progressDialog = null;
					}
				}

			}
		}).start();
	}

	@SuppressWarnings("deprecation")
	private void showPopuWindowInputNum(View ll) {
		if (popupWindow != null) {
			dissmissPopuwindow();
			return;
		}

		m_cricle_list = new JSONArray();
		getCirclelistData(true);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.circlelist, null);
		GridView gv_circle = (GridView) view.findViewById(R.id.gv_circle);
		circleGridVIewAdapter = new MyCircleGridVIewAdapter();
		gv_circle.setAdapter(circleGridVIewAdapter);
		gv_circle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				try {
					Intent intent = new Intent(CityleagueActivity.this, ShopActivity.class);
					intent.putExtra("circleid", m_cricle_list.getJSONObject(arg2).getString("circleid"));
					intent.putExtra("circlename", m_cricle_list.getJSONObject(arg2).getString("circlename"));
					intent.putExtra("type", 1);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
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
				Log.e("点啦up", "点啦up");
			}
		});
		TextView txtC = (TextView) view.findViewById(R.id.tv_criclelist);
		txtC.setText("当前城市：" + sp.getString("currentcityname", "长沙市"));
		view.findViewById(R.id.tv_criclelist_change).setOnClickListener(this);

		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOutsideTouchable(false);
		popupWindow.setAnimationStyle(R.style.AnimTop2);
		popupWindow.showAsDropDown(ll, 0, 30);
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
					Intent intent = new Intent(CityleagueActivity.this, ShopActivity.class);
					intent.putExtra("circleid", v.getTag() + "");
					intent.putExtra("circlename", bt_city.getText().toString());
					intent.putExtra("type", 1);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

					dissmissPopuwindow();
				}
			});

			return view;
		}
	}

	private void dissmissPopuwindow() {
		if (popupWindow != null && popupWindow.isShowing()) {

			popupWindow.dismiss();
			popupWindow = null;
		}

	}

	Boolean mgetFinished = true;

	private long mExitTime;

}
