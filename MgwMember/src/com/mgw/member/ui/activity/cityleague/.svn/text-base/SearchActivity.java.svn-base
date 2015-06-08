package com.mgw.member.ui.activity.cityleague;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hx.hxchat.widget.ExpandGridView;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.mgw.member.uitls.UIUtils;

@SuppressLint("HandlerLeak")
public class SearchActivity extends BaseActivity2 implements OnClickListener {
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
		};
	};
	private List<View> pageViews;
	private ImageView[] imageViews;
	private ViewPager viewPager;
	JSONArray m_searchkey_list = new JSONArray();
	private MyGridVIewAdapter gridVIewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		findViewById(R.id.tv_title_bar_search).setOnClickListener(this);
		gridVIewAdapter = new MyGridVIewAdapter();

		initTitleButton();
		getDatahotsearch(true);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void initViewPaper() {
		LayoutInflater inflater = getLayoutInflater();
		View v1 = inflater.inflate(R.layout.search_viewpaper01, null);
		ExpandGridView gv_search_key = (ExpandGridView) v1
				.findViewById(R.id.gv_search_key);

		gv_search_key.setAdapter(gridVIewAdapter);
		gv_search_key.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					Intent intent = new Intent(SearchActivity.this,
							ShopActivity.class);
					intent.putExtra("name", m_searchkey_list
							.getJSONObject(arg2).getString("skey"));
					intent.putExtra("type", 2);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_from_left,
							R.anim.slide_out_to_right);

					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		pageViews = new ArrayList<View>();
		pageViews.add(v1);
		imageViews = new ImageView[pageViews.size()];

		viewPager = (ViewPager) findViewById(R.id.vp_search);

		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.tv_title_bar_search:
			String skey = ((EditText) findViewById(R.id.et_title_bar_search))
					.getText().toString();
			if (!"".equals(skey.trim())) {
				Intent intent = new Intent(SearchActivity.this,
						ShopActivity.class);
				intent.putExtra("name", skey);
				intent.putExtra("type", 2);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_from_left,
						R.anim.slide_out_to_right);

				finish();
			}
			break;
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
				switch (arg0) {
				case 0:
					((ImageView) findViewById(R.id.iv_search_point01))
							.setBackgroundResource(R.drawable.dot_s);
					((ImageView) findViewById(R.id.iv_search_point02))
							.setBackgroundResource(R.drawable.dot);

					break;
				case 1:
					((ImageView) findViewById(R.id.iv_search_point02))
							.setBackgroundResource(R.drawable.dot_s);
					((ImageView) findViewById(R.id.iv_search_point01))
							.setBackgroundResource(R.drawable.dot);

					break;
				}
			}
		}
	}

	private void getDatahotsearch(boolean show) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);

					Log.e("hotsearchkey", obj.toString());
					if (obj.getInt("flag") == 0) {
						m_searchkey_list = obj.getJSONArray("items");
						initViewPaper();
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
		params.put("type", "wzreposity.hotsearchkey");
		try {
			JSONObject obj = new JSONObject(getSharedPreferences("mgw_data",
					Context.MODE_PRIVATE).getString("mgw_data", ""));
			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("telephone", obj.getString("Telephone"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	class MyGridVIewAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return m_searchkey_list.length();
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
			View view = LayoutInflater.from(SearchActivity.this).inflate(
					R.layout.item_textview, null);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);

			Log.e("getview啦", "position" + position);
			try {
				tv_item.setText(m_searchkey_list.getJSONObject(position)
						.getString("skey"));

				Log.e("getview啦shuju ",
						"shuju;;;;;"
								+ m_searchkey_list.getJSONObject(position)
										.getString("skey"));

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return view;
		}
	}

	private void closeKeyboard(final View view) {

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

			}

		}, 10);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
