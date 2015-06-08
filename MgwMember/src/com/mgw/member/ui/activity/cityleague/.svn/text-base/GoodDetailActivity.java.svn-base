package com.mgw.member.ui.activity.cityleague;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.widget.MyScrollView;
import com.mgw.member.ui.widget.MyScrollView.OnScrollListener;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;

public class GoodDetailActivity extends BaseActivity2 implements OnClickListener, OnScrollListener {
	private JSONArray m_review_list = new JSONArray();
	private JSONArray m_tui_list = new JSONArray();
	private MyReviewAdapter myReviewAdapter;
	private MyTuisAdapter myTuiAdapter;
	private int ptype = 0;
	private String price;
	/** 判断是否从网上取回了数据 */
	private boolean novalue = false;
	private JSONObject mInfo;
	TextView tv_title_cent;
	ArrayList<String> mImage = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.good_detail);
		initTitleBar();
		// initShowShare();

		findViewById(R.id.tv_titlebar_right).setVisibility(View.INVISIBLE);
		findViewById(R.id.iv_good_detail).setOnClickListener(this);
		((MyScrollView) findViewById(R.id.scrollview)).setOnScrollListener(this);
		findViewById(R.id.layout).getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				onScroll(((MyScrollView) findViewById(R.id.scrollview)).getScrollY());
			}
		});

		findViewById(R.id.bt_good_detail_buy0).setOnClickListener(this);
		findViewById(R.id.bt_good_detail_buy).setOnClickListener(this);
		findViewById(R.id.bt_good_detail_call).setOnClickListener(this);
		findViewById(R.id.show_detail).setOnClickListener(this);

		myReviewAdapter = new MyReviewAdapter();
		myTuiAdapter = new MyTuisAdapter();
		((ListView) findViewById(R.id.lv_good_detail_review)).setAdapter(myReviewAdapter);
		((ListView) findViewById(R.id.lv_good_detail_tui)).setAdapter(myTuiAdapter);

		getDataJiben(true);

		((ListView) findViewById(R.id.lv_good_detail_tui)).setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String pid = m_tui_list.getJSONObject(arg2).getString("pid");
					Intent intent = new Intent(GoodDetailActivity.this, GoodDetailActivity.class);
					intent.putExtra("sid", getIntent().getExtras().getString("sid"));
					intent.putExtra("pid", pid);
					intent.putExtra("pname", getIntent().getExtras().getString("pname"));
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		findViewById(R.id.addr_map).setOnClickListener(this);
	}

	private void initTitleBar() {
		initTitleButton();
		tv_title_cent = (TextView) findViewById(R.id.tv_title_cent);
		tv_title_cent.setText(getIntent().getExtras().getString("pname"));
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ((GlobelElements) getApplicationContext()).mActivityArray.add(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ((GlobelElements)
		// getApplicationContext()).mActivityArray.remove(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.bt_good_detail_buy:
		case R.id.bt_good_detail_buy0: {
			if (!novalue) {
				Intent intent = new Intent(this, OrderSubmitActivity.class);
				intent.putExtra("price", Double.valueOf(price));
				try {
					intent.putExtra("pname", mInfo.getString("pname"));
					intent.putExtra("ssafe", Double.valueOf(mInfo.getString("ssafe")));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				intent.putExtra("sid", getIntent().getExtras().getString("sid"));
				intent.putExtra("pid", getIntent().getExtras().getString("pid"));
				intent.putExtra("ptype", ptype);
				startActivity(intent);
			}

		}
			break;

		case R.id.addr_map:
			if (!novalue&&mInfo!=null) {
				try {
					Intent intent = new Intent(this, BussinessMapActivity.class);
					intent.putExtra("lat", mInfo.getDouble("sposx"));
					intent.putExtra("lng", mInfo.getDouble("sposy"));
					intent.putExtra("address", mInfo.getString("saddr"));
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			break;

		case R.id.bt_good_detail_call:
			if (!novalue) {
				try {
					Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mInfo.getString("mphone")));
					startActivity(intentCall);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			break;

		case R.id.iv_good_detail: {
			if (!novalue) {
				Intent intent = new Intent(this, SingleImageShowActivity.class);
				intent.putStringArrayListExtra("image", mImage);
				intent.putExtra("index", 0);
				startActivity(intent);
			}

		}
			break;

		case R.id.show_detail: {
			if (!novalue) {
				Intent intent = new Intent(this, GoodWebViewActivity.class);
				intent.putExtra("info", mInfo.toString());
				intent.putExtra("price", price);
				startActivity(intent);
			}

		}
			break;
		}
	}

	private class MyTuisAdapter extends BaseAdapter {

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
			View view = LayoutInflater.from(GoodDetailActivity.this).inflate(R.layout.item_shop_detail_more, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_oldprice = (TextView) view.findViewById(R.id.tv_oldprice);
			TextView tv_newprice = (TextView) view.findViewById(R.id.tv_newprice);
			TextView tv_sellcount = (TextView) view.findViewById(R.id.tv_sellcount);
			tv_sellcount.setVisibility(View.GONE);

			try {

				tv_name.setText(m_tui_list.getJSONObject(position).getString("pname"));
				tv_name.setTextColor(Color.BLACK);
				tv_oldprice.setText("￥" + m_tui_list.getJSONObject(position).getString("mprice"));
				tv_newprice.setText(m_tui_list.getJSONObject(position).getString("sprice"));

				tv_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return view;
		}
	}

	private class MyReviewAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_review_list.length();
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
			View view = LayoutInflater.from(GoodDetailActivity.this).inflate(R.layout.item_good_detail_review, null);
			TextView tv_item_good_review_name = (TextView) view.findViewById(R.id.tv_item_good_review_name);
			TextView tv_item_good_review_time = (TextView) view.findViewById(R.id.tv_item_good_review_time);
			TextView tv_item_good_review_rtext = (TextView) view.findViewById(R.id.tv_item_good_review_rtext);
			TextView tv_good_detail_item_review_stext = (TextView) view.findViewById(R.id.tv_good_detail_item_review_stext);
			LinearLayout ll_good_detail_item_review_stext = (LinearLayout) view.findViewById(R.id.ll_good_detail_item_review_stext);
			LinearLayout ll_credicable = (LinearLayout) view.findViewById(R.id.ll_credicable);

			try {
				tv_item_good_review_name.setText(m_review_list.getJSONObject(position).getString("username"));
				tv_item_good_review_time.setText(m_review_list.getJSONObject(position).getString("rdate"));
				tv_item_good_review_rtext.setText(m_review_list.getJSONObject(position).getString("rtext"));
				dealCredicableView(ll_credicable, m_review_list.getJSONObject(position).getInt("score"));
				if ("".equals(m_review_list.getJSONObject(position).getString("stext"))) {
					ll_good_detail_item_review_stext.setVisibility(View.GONE);
				} else {
					tv_good_detail_item_review_stext.setText(m_review_list.getJSONObject(position).getString("stext"));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return view;
		}

	}

	private void getDataJiben(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("jiben", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONObject item = obj.getJSONObject("item").getJSONObject("info");
						mInfo = item;

						ptype = item.getInt("ptype");

						String sname = item.getString("sname");
						String pname = item.getString("pname");
						String mprice = item.getString("mprice");
						String sprice = item.getString("sprice");
						String scount = item.getString("scount");
						String image1 = item.getString("image1");
						String sdesc = item.getString("sdesc");
						String kprice = item.getString("kprice");
						String saddr = item.getString("saddr");
						String mphone = item.getString("mphone");
						if (mphone.length() == 0) {
							findViewById(R.id.bt_good_detail_call).setVisibility(View.INVISIBLE);
							findViewById(R.id.line).setVisibility(View.INVISIBLE);
						}
						int review = item.getInt("review");
						double ssafe = item.getDouble("ssafe");
						double distance = item.getDouble("distance");
						m_review_list = obj.getJSONObject("item").getJSONArray("review");
						if (m_review_list.length() == 0) {
							findViewById(R.id.layout0).setVisibility(View.GONE);
						}

						m_tui_list = obj.getJSONObject("item").getJSONArray("tui");
						if (m_tui_list.length() > 0) {
							myTuiAdapter.notifyDataSetChanged();
						}
						if (m_review_list.length() > 0) {
							myReviewAdapter.notifyDataSetChanged();
							((TextView) findViewById(R.id.tv_good_detail_reviewcount)).setText("查看半年内" + m_review_list.length() + "条评价");
						} else {
							((TextView) findViewById(R.id.tv_good_detail_reviewcount)).setText("暂时还没有任何评价信息");
						}

						if (item.getInt("stype") != 2)
							price = kprice;
						else
							price = sprice;

						ptype = item.getInt("ptype");
						((TextView) findViewById(R.id.tv_good_detail_sname)).setText(sname);
						((TextView) findViewById(R.id.tv_good_detail_nowprice)).setText(price);
						((TextView) findViewById(R.id.tv_good_detail_oldprice)).setText("￥" + mprice);
						((TextView) findViewById(R.id.tv_good_detail_oldprice)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
						((TextView) findViewById(R.id.tv_good_detail_oldprice0)).setText("￥" + mprice);
						((TextView) findViewById(R.id.tv_good_detail_nowprice0)).setText(price + "");
						((TextView) findViewById(R.id.tv_good_detail_oldprice0)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

						((TextView) findViewById(R.id.youhui_card_02)).setText(String.format("%.1f折", 10 * (Double.valueOf(price) / Double.valueOf(mprice))));
						((TextView) findViewById(R.id.youhui_card_01)).setText("返利 " + (int) (ssafe * 100) + "%");
						((TextView) findViewById(R.id.tv_good_detail_sellcount)).setText("已售：" + scount);
						((TextView) findViewById(R.id.tv_good_detail_pname)).setText(pname);
						tv_title_cent.setText(pname);
						((TextView) findViewById(R.id.tv_good_detail_saddr)).setText(saddr);
						((TextView) findViewById(R.id.tv_good_detail_distance)).setText(String.format("< %.2fkm", distance));
						((TextView) findViewById(R.id.tv_good_detail_judge)).setText(review + "人评论");
						if (sdesc.length() == 0)
							findViewById(R.id.layout1).setVisibility(View.GONE);
						((TextView) findViewById(R.id.bt_good_detail_sdesc)).setText(sdesc);
						// tv_good_detail_desc.setText(desc);
						// webview.loadData(fmtString(desc), "text/html",
						// "utf-8");
						// Picasso.with(GoodDetailActivity.this).load(image1).into((ImageView)
						// findViewById(R.id.iv_good_detail));
						ImageLoaderHelper.displayImage(R.drawable.img_loading, (ImageView) findViewById(R.id.iv_good_detail), image1);
						findViewById(R.id.iv_good_detail).setFocusable(true);
						findViewById(R.id.iv_good_detail).setFocusableInTouchMode(true);
						findViewById(R.id.iv_good_detail).requestFocus();

						String url = item.getString("image1");
						if (url.length() > 0)
							mImage.add(url);
						url = item.getString("image2");
						if (url.length() > 0)
							mImage.add(url);
						url = item.getString("image3");
						if (url.length() > 0)
							mImage.add(url);
						url = item.getString("image4");
						if (url.length() > 0)
							mImage.add(url);
						url = item.getString("image5");
						if (url.length() > 0)
							mImage.add(url);
					} else {

						if (obj.getInt("flag") == 99) {
							novalue = true;
							Toast.makeText(getApplicationContext(), "商家没有设置具体信息！", Toast.LENGTH_SHORT).show();
						} else {
							novalue = true;
							Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
							// ToastUtil.showToastWithAlertPic(obj.getString("msg"));
						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable ble) {
				novalue = true;
			}
		};

		RequestParams params = new RequestParams();
		params.put("type", "wzreposity.productdetails");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));

			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("sid", getIntent().getExtras().getString("sid"));
			params.put("pid", getIntent().getExtras().getString("pid"));
			params.put("posx", getSharedPreferences("mgw_data", 0).getString("lng", ""));
			params.put("posy", getSharedPreferences("mgw_data", 0).getString("lat", ""));
			// params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	float imgHeight = 0;

	@Override
	public void onScroll(int scrollY) {
		if (imgHeight == 0)
			imgHeight = findViewById(R.id.iv_good_detail).getHeight();
		if (imgHeight <= scrollY) {
			findViewById(R.id.toplayout0).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.toplayout0).setVisibility(View.INVISIBLE);
		}

		((MyScrollView) findViewById(R.id.scrollview)).setOnScrollListener(this);
	}
}
