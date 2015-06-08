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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.hxchat.activity.ChatActivity;
import com.hx.hxchat.widget.ExpandGridView;
import com.hx.hxchat.widget.ExpandListvview;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.uitls.ImageLoaderHelper;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;

public class ShopDetailAndIntroduceActivity extends BaseActivity2 implements OnClickListener {
	private String sid;
	JSONObject m_jiben_list = new JSONObject();
	JSONArray m_seckill_list = new JSONArray();
	JSONArray m_tui_list = new JSONArray();
	JSONArray m_other_list = new JSONArray();
	JSONArray m_data_list = new JSONArray();
	JSONArray m_more_list = new JSONArray();
	private int returnpay = -1;
	private MyMoreGoodsAdapter moreGoodsAdapter;
	private MySecAdapter secAdapter;
	private MyTuiAdapter tuiAdapter;

	JSONObject mInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shop_detaill_and_introduce);

		sid = getIntent().getExtras().getString("sid");
		Log.e("huoqusid", sid + "");
		initView();

		findViewById(R.id.tv_titlebar_right).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.tv_title_cent)).setText("加载中 ...");

		initTitleButton();
		// initShowShare();

		getData(true);
		findViewById(R.id.tv_location).setOnClickListener(this);
		findViewById(R.id.compile).setOnClickListener(this);
	}

	private void initView() {
		findViewById(R.id.bt_shop_detailintroduce_takecare).setOnClickListener(this);
		findViewById(R.id.tv_findall_product).setOnClickListener(this);
		findViewById(R.id.iv_credicable_mg).setVisibility(View.GONE);

		findViewById(R.id.bt_chat).setOnClickListener(this);
		findViewById(R.id.iv_shop_detailintroduce_icon).setOnClickListener(this);

		moreGoodsAdapter = new MyMoreGoodsAdapter();
		secAdapter = new MySecAdapter();
		tuiAdapter = new MyTuiAdapter();
		((ExpandListvview) findViewById(R.id.lv_shop_detail_more)).setAdapter(moreGoodsAdapter);
		((ExpandListvview) findViewById(R.id.lv_shop_detail_sec)).setAdapter(secAdapter);
		((ExpandGridView) findViewById(R.id.gv_shop_detail_introduce)).setAdapter(tuiAdapter);
		((ExpandListvview) findViewById(R.id.lv_shop_detail_more)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String pname = m_more_list.getJSONObject(arg2).getString("pname");
					String pid = m_more_list.getJSONObject(arg2).getString("pid");
					Intent intent = new Intent(ShopDetailAndIntroduceActivity.this, GoodDetailActivity.class);
					intent.putExtra("sid", sid);
					intent.putExtra("pid", pid);
					intent.putExtra("pname", pname);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		((ExpandListvview) findViewById(R.id.lv_shop_detail_sec)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String pname = m_seckill_list.getJSONObject(arg2).getString("pname");
					String pid = m_seckill_list.getJSONObject(arg2).getString("pid");
					Intent intent = new Intent(ShopDetailAndIntroduceActivity.this, GoodDetailActivity.class);
					intent.putExtra("sid", sid);
					intent.putExtra("pid", pid);
					intent.putExtra("pname", pname);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		((ExpandGridView) findViewById(R.id.gv_shop_detail_introduce)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					String pname = m_tui_list.getJSONObject(arg2).getString("pname");
					String pid = m_tui_list.getJSONObject(arg2).getString("pid");
					Intent intent = new Intent(ShopDetailAndIntroduceActivity.this, GoodDetailActivity.class);
					intent.putExtra("sid", sid);
					intent.putExtra("pid", pid);
					intent.putExtra("pname", pname);
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_shop_detailintroduce_takecare:
			if ("关注".equals(((TextView) v).getText().toString())) {
				getDataoTakeCare(true, 1);
			} else {
				getDataoTakeCare(true, 0);
			}
			break;

		case R.id.tv_location:
			try {
				if (mInfo != null) {
					Intent intent = new Intent(this, BussinessMapActivity.class);
					intent.putExtra("lat", mInfo.getDouble("posx"));
					intent.putExtra("lng", mInfo.getDouble("posy"));
					intent.putExtra("address", mInfo.getString("saddr"));
					startActivity(intent);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		case R.id.tv_findall_product: {
			if(mInfo==null){
				break;
			}
			Intent intent = new Intent(this, ShopDetailActivity.class);
			intent.putExtra("sid", sid);
			try {
				intent.putExtra("name", mInfo.getString("sname"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			startActivity(intent);
		}
			break;

		case R.id.iv_shop_detailintroduce_photo: {
			Intent intent2 = new Intent(this, ShopPhotosActivity.class);
			intent2.putExtra("sid", sid);
			startActivity(intent2);
		}
			break;

		case R.id.bt_chat: {
			try {
				if(mInfo==null){
					break;
				}
				String userid = mInfo.getString("cashier");
				if (userid.length() == 0) {
					Toast.makeText(this, "该商户暂未开通在线咨询服务", Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(ShopDetailAndIntroduceActivity.this, ChatActivity.class);
					intent.putExtra("userId", userid);
					intent.putExtra("userName", mInfo.getString("sname"));
					startActivity(intent);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		}

		case R.id.iv_shop_detailintroduce_icon:
			try {
				Intent intent2 = new Intent(this, ShopPhotosActivity.class);
				if(mInfo!=null){
					intent2.putExtra("sid", mInfo.getString("sid"));
					intent2.putExtra("name", mInfo.getString("sname"));
					startActivity(intent2);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		case R.id.compile: {
			if(mInfo!=null){
				break;
			}
			
			Intent intent = new Intent(this, CompileActivity.class);
			try {
				intent.putExtra("sid", mInfo.getString("sid"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			startActivity(intent);
		}
			break;
		}
	}

	private class MyMoreGoodsAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_more_list.length();
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
			View view = LayoutInflater.from(ShopDetailAndIntroduceActivity.this).inflate(R.layout.item_shop_detail_more, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
			TextView tv_oldprice = (TextView) view.findViewById(R.id.tv_oldprice);
			TextView tv_newprice = (TextView) view.findViewById(R.id.tv_newprice);
			TextView tv_sellcount = (TextView) view.findViewById(R.id.tv_sellcount);

			try {
				tv_name.setText(m_more_list.getJSONObject(position).getString("pname"));
				tv_oldprice.setText("￥" + m_more_list.getJSONObject(position).getString("mprice"));
				tv_newprice.setText(m_more_list.getJSONObject(position).getString("sprice"));
				tv_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				tv_sellcount.setText("已售" + m_more_list.getJSONObject(position).getString("scount"));
				tv_oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return view;
		}
	}

	class MySecAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_seckill_list.length();
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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.item_seckill_list, null);

				holder.name = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_name);

				holder.option = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_option);
				holder.distance = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_distance);
				holder.seckillprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_secprice);
				holder.nowprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_nowprice);
				holder.oldprice = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_oldprice);
				holder.sellcount = (TextView) convertView.findViewById(R.id.tv_item_seckill_sellcount);
				holder.secname = (TextView) convertView.findViewById(R.id.tv_item_seckill_list_secname);
				holder.img = (ImageView) convertView.findViewById(R.id.iv_item_seckill_list);

				holder.name.setVisibility(View.GONE);
				holder.option.setVisibility(View.GONE);
				holder.distance.setVisibility(View.GONE);
				holder.secname.setVisibility(View.VISIBLE);

				holder.pos = position;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.pos = position;
			}
			try {

				JSONObject item = m_seckill_list.getJSONObject(position);

				String pname = item.getString("pname");
				String kprice = item.getString("kprice");
				String mprice = item.getString("mprice");
				String scount = item.getString("scount");
				String image = item.getString("image");
				int ftype = item.getInt("ftype");
				holder.secname.setText(pname);
				if (ftype == 0) {
					holder.seckillprice.setText("秒杀价");
					holder.seckillprice.setBackgroundResource(R.drawable.bg_orange);
				} else {
					holder.seckillprice.setText("团购价");
					holder.seckillprice.setBackgroundResource(R.drawable.bg_slowblue2);
				}
				holder.nowprice.setText(kprice);
				holder.oldprice.setText("￥" + mprice);
				holder.oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				holder.sellcount.setText("已售" + scount);
				// Picasso.with(getApplicationContext()).load(image).into(holder.img);
				ImageLoaderHelper.displayImage(R.drawable.img_loading, holder.img, image);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

	public final class ViewHolder {
		public TextView name;
		public TextView seckillprice;
		public TextView nowprice;
		public TextView oldprice;
		public TextView distance;
		public ImageView img;
		public TextView option;
		public TextView sellcount;
		public TextView secname;
		public int pos;
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
				holder.sellcount = (TextView) convertView.findViewById(R.id.tv_item_goods_gvlist_sellcount);
				holder.img = (ImageView) convertView.findViewById(R.id.iv_item_goods_gvlist);

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
				String scount = item.getString("scount");
				String image = item.getString("image");
				holder.name.setText(pname);
				holder.nowprice.setText(sprice);
				holder.oldprice.setText("￥" + mprice);
				holder.oldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				holder.sellcount.setText("已售" + scount);
				// Picasso.with(getApplicationContext()).load(image).into(holder.img);
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
		public TextView sellcount;
		public int pos;
	}

	private void getDataoTakeCare(boolean show, int pay) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("TakeCare", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONArray items = obj.getJSONArray("items");
						returnpay = items.getJSONObject(0).getInt("ispay");
						if (returnpay == 0) {
							((Button) findViewById(R.id.bt_shop_detailintroduce_takecare)).setText("关注");
						} else {
							((Button) findViewById(R.id.bt_shop_detailintroduce_takecare)).setText("取消关注");
						}
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
		params.put("type", "wzreposity.payattention");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("pay", pay + "");
			params.put("sid", sid);
			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}

	private void getData(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("data", obj.toString());
					if (obj.getInt("flag") == 0) {
						mInfo = obj.getJSONObject("item").getJSONObject("info");
						dealCredicableView(findViewById(R.id.ll_credicable), 5);
						// Picasso.with(getApplicationContext()).load(mInfo.getString("image")).into((ImageView)
						// findViewById(R.id.iv_shop_detailintroduce_photo));
						// Picasso.with(getApplicationContext()).load(mInfo.getString("photo")).into((ImageView)
						// findViewById(R.id.iv_shop_detailintroduce_icon));
						ImageLoaderHelper.displayImage(R.drawable.img_loading, (ImageView) findViewById(R.id.iv_shop_detailintroduce_photo), mInfo.getString("image"));
						ImageLoaderHelper.displayImage(R.drawable.img_loading, (ImageView) findViewById(R.id.iv_shop_detailintroduce_icon), mInfo.getString("photo"));
						((TextView) findViewById(R.id.tv_shop_detailintroduce_name)).setText(mInfo.getString("sname"));
						((TextView) findViewById(R.id.tv_title_cent)).setText(mInfo.getString("sname"));
						double ssafe = mInfo.getDouble("ssafe");
						((TextView) findViewById(R.id.tv_rebate)).setText((int) (ssafe * 100) + "%");
						((TextView) findViewById(R.id.tv_discount)).setText(mInfo.getString("sdic"));
						((TextView) findViewById(R.id.tv_location)).setText(mInfo.getString("saddr"));
						((TextView) findViewById(R.id.tv_distance)).setText(String.format("< %.2fkm", mInfo.getDouble("distance")));
						if (mInfo.getInt("ispay") == 0) {
							((Button) findViewById(R.id.bt_shop_detailintroduce_takecare)).setText("关注");
						} else {
							((Button) findViewById(R.id.bt_shop_detailintroduce_takecare)).setText("取消关注");
						}

						m_more_list = obj.getJSONObject("item").getJSONArray("more");
						m_seckill_list = obj.getJSONObject("item").getJSONArray("tuan");
						m_tui_list = obj.getJSONObject("item").getJSONArray("tui");
						Log.e("tui", "tui::" + m_tui_list.toString());
						moreGoodsAdapter.notifyDataSetChanged();
						secAdapter.notifyDataSetChanged();
						tuiAdapter.notifyDataSetChanged();
						findViewById(R.id.iv_shop_detailintroduce_photo).setFocusable(true);
						findViewById(R.id.iv_shop_detailintroduce_photo).setFocusableInTouchMode(true);
						findViewById(R.id.iv_shop_detailintroduce_photo).requestFocus();

						if (m_tui_list.length() == 0) {
							findViewById(R.id.layout0).setVisibility(View.GONE);
							((TextView) findViewById(R.id.title_more)).setText("店家推荐");
						}

						if (m_more_list.length() == 0) {
							findViewById(R.id.layout1).setVisibility(View.GONE);
						}
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
		params.put("type", "wzreposity.supplierdetails");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("sid", sid);
			params.put("posx", getSharedPreferences("mgw_data", 0).getString("lng", ""));
			params.put("posy", getSharedPreferences("mgw_data", 0).getString("lat", ""));
			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);
	}
}
