package com.mgw.member.ui.activity.cityleague;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.hxchat.widget.ExpandGridView;
import com.mgw.member.R;
import com.mgw.member.constant.PulicData;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.cityleague.model.CityList;
import com.mgw.member.ui.activity.cityleague.model.CityModel;
import com.mgw.member.uitls.DBLoad;
import com.mgw.member.uitls.UIUtils;
import com.zxing.view.MyLetterListView;
import com.zxing.view.MyLetterListView.OnTouchingLetterChangedListener;

public class MyCityListActivity extends Activity {
	private BaseAdapter adapter;
	private ListView mCityLit;
	private TextView overlay;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;//
	private Handler handler;
	private OverlayThread overlayThread;
	// 热门城市
	private final JSONArray m_array_list = new JSONArray();
	private final List<String> actvList = new ArrayList<String>();
	private final List<String> codeList = new ArrayList<String>();
	private MyAdapter m_adapter;

	List<CityModel> mCityArray = new ArrayList<CityModel>();

	CityList cList;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_list);
		sp = getSharedPreferences("mgw_data", 0);

		m_adapter = new MyAdapter();

		mCityLit = (ListView) findViewById(R.id.city_list);
		mCityLit.setOnItemClickListener(new CityListOnItemClick());
		((MyLetterListView) findViewById(R.id.cityLetterListView)).setOnTouchingLetterChangedListener(new LetterListViewListener());
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();

		addHead();
		initData();
		GetHostCity();

		ArrayAdapter<String> av = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, actvList);

		((AutoCompleteTextView) findViewById(R.id.actv_citylist)).setAdapter(av);
		((AutoCompleteTextView) findViewById(R.id.actv_citylist)).setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String city = arg0.getItemAtPosition(arg2).toString();
				int pos = actvList.indexOf(city);
				String code = codeList.get(pos);
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				if (sp.getString("currentcityID", "").equals("")) {
					bundle.putString("result", "first");// 返回界面需要界面重新初始化
					sp.edit().putString("currentcityname", city).commit();
					sp.edit().putString("currentcityID", code).commit();
				} else {
					bundle.putString("result", "Second");// 返回界面需要界面不需要重新初始化
				}
				sp.edit().putString("city", city).commit();
				sp.edit().putString("cid", code).commit();
				resultIntent.putExtras(bundle);
				MyCityListActivity.this.setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
		/*
		 * ((AutoCompleteTextView)
		 * findViewById(R.id.actv_citylist)).setOnKeyListener(new
		 * OnKeyListener() {
		 * 
		 * @Override public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		 * if(arg1==KeyEvent.KEYCODE_ENTER) { ((InputMethodManager)
		 * getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
		 * getCurrentFocus
		 * ().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		 * getDataAllCity(true,((AutoCompleteTextView)
		 * findViewById(R.id.actv_citylist)).getText().toString()); return true;
		 * } return false; } });
		 */
		((TextView) findViewById(R.id.tv_title_cent)).setText("城市选择");

		findViewById(R.id.bt_titlebar_left).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if ("".equals(sp.getString("cid", "")) || sp.getString("city", "").equals("")) {
					m_handler.sendEmptyMessage(-1);
				} else {
					finish();
				}
			}
		});
		if ("".equals(sp.getString("cid", "")) || sp.getString("city", "").equals("")) {
			findViewById(R.id.bt_titlebar_left).setVisibility(View.GONE);
		}
	}

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ("".equals(sp.getString("cid", "")) || sp.getString("city", "").equals("")) {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
					return true;
				} else {
					finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(0);
					return true;
				}
			}
			return super.onKeyDown(keyCode, event);
		}

		return super.onKeyDown(keyCode, event);
	}

	Handler m_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				break;
			case PulicData.LOCAL_HOSTCITY:
				ShowHostCity();
				break;
			case -1:
				UIUtils.showToastLong(MyCityListActivity.this, "请选择城市");
				// Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
				break;
			case PulicData.GETCITYNAME_ERR:

				break;
			case PulicData.NET_ERR:// 网络错误

				break;
			case PulicData.SERVER_ERR:
				UIUtils.showToastWithAlertPic("服务器维护中");
				// m_adapter.notifyDataSetChanged();
				break;
			case PulicData.LOCAL_CITY:// 从本地获取城市信息
				GetLocalCity();
				break;
			}
		}
	};

	private void ShowHostCity() {
		String Sql = "select * from hostcity";
		BaseApplication.getApplication().GetDbhandler().GetDataForJSONArray(m_array_list, Sql);
		m_adapter.notifyDataSetChanged();
	}

	/*
	 * 显示所有城市
	 */
	private void GetLocalCity() {
		String Sql = "select * from city order by pinyin";
		try {
			ArrayList<JSONObject> m_Citylist = new ArrayList<JSONObject>();
			try {
				BaseApplication.getApplication().GetDbhandler().GetDataFromLocalDB(m_Citylist, Sql);

				for (int i = 0; i < m_Citylist.size(); i++) {
					CityModel model = new CityModel();

					model.cityCode = m_Citylist.get(i).getString("cid");

					model.cityName = m_Citylist.get(i).getString("cname");
					model.nameSort = m_Citylist.get(i).getString("pinyin").substring(0, 1);
					mCityArray.add(model);

					actvList.add(model.cityName);
					codeList.add(model.cityCode);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		adapter = new ListAdapter(this, mCityArray);
		mCityLit.setAdapter(adapter);

	}

	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj;
				try {
					if (DBLoad.IsLoadCity) {
						m_handler.sendEmptyMessage(PulicData.LOCAL_CITY);
					} else {
						obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
						DBLoad.LoadAllCity(obj.getString("UserID"), obj.getString("serial"));
						m_handler.sendEmptyMessage(PulicData.LOCAL_CITY);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					m_handler.sendEmptyMessage(PulicData.LOCAL_CITY);
				}
			}
		}).start();

	}

	private void addHead() {
		View view = LayoutInflater.from(this).inflate(R.layout.city_head, null);
		ExpandGridView gv_cityhead = (ExpandGridView) view.findViewById(R.id.gv_cityhead);

		Button bt_now_loccity = (Button) view.findViewById(R.id.bt_now_loccity);
		String city = sp.getString("currentcityname", "");
		if (!sp.getString("currentcityID", "").equals("") && !city.equals("")) {
			bt_now_loccity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String city = ((Button) v).getText().toString();

					sp.edit().putString("city", sp.getString("currentcityname", "")).commit();
					sp.edit().putString("cid", sp.getString("currentcityID", "")).commit();
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("result", "Second");// 返回界面需要界面不需要重新初始化

					resultIntent.putExtras(bundle);
					MyCityListActivity.this.setResult(RESULT_OK, resultIntent);
					finish();
				}
			});
		} else {
			city = "请设置当前城市";
		}

		bt_now_loccity.setText(city);

		gv_cityhead.setAdapter(m_adapter);
		mCityLit.addHeaderView(view);
	}

	class CityListOnItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
			CityModel city = mCityArray.get(pos - 1);
			sp.edit().putString("city", city.cityName).commit();
			sp.edit().putString("cid", city.cityCode).commit();
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();

			if (sp.getString("currentcityID", "").equals("")) {
				bundle.putString("result", "first");// 返回界面需要界面重新初始化
				sp.edit().putString("currentcityname", city.cityName).commit();
				sp.edit().putString("currentcityID", city.cityCode).commit();
			} else {
				bundle.putString("result", "Second");// 返回界面需要界面不需要重新初始化
			}
			resultIntent.putExtras(bundle);
			MyCityListActivity.this.setResult(RESULT_OK, resultIntent);
			finish();
		}

	}

	/**
	 * ListViewAdapter
	 * 
	 * @author sy
	 * 
	 */
	private class ListAdapter extends BaseAdapter {
		private final LayoutInflater inflater;
		private final List<CityModel> list;

		public ListAdapter(Context context, List<CityModel> list) {

			this.inflater = LayoutInflater.from(context);
			this.list = list;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {

				String currentStr = list.get(i).nameSort;
				String previewStr = (i - 1) >= 0 ? list.get(i - 1).nameSort : " ";
				if (!previewStr.equals(currentStr)) {
					String name = list.get(i).nameSort;
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_city, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(list.get(position).cityName);
			String currentStr = list.get(position).nameSort;
			String previewStr = (position - 1) >= 0 ? list.get(position - 1).nameSort : " ";
			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha;
			TextView name;
		}

	}

	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mCityLit.setSelection(position);
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				handler.postDelayed(overlayThread, 1500);
			}
		}

	}

	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	private void GetHostCity() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject obj;
				try {
					if (DBLoad.IsLoadHostCity) {
						m_handler.sendEmptyMessage(PulicData.LOCAL_HOSTCITY);
					} else {
						obj = new JSONObject(getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));
						DBLoad.LoadHostCity(obj.getString("UserID"), obj.getString("serial"));
						m_handler.sendEmptyMessage(PulicData.LOCAL_HOSTCITY);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					m_handler.sendEmptyMessage(PulicData.LOCAL_HOSTCITY);
				}
			}
		}).start();
	}

	/*
	 * private void getData(boolean show) { MgqDataHandler loginHandler = new
	 * MgqDataHandler(this, show, false) {
	 * 
	 * @Override public void onSuccess(String response) {
	 * super.onSuccess(response);
	 * 
	 * try { JSONObject obj = new JSONObject(response); Log.e("json",
	 * obj.toString()); if (obj.getInt("flag") == 0) { m_array_list =
	 * obj.getJSONArray("items"); m_adapter.notifyDataSetChanged(); }
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); } }
	 * 
	 * public void onFailure(Throwable ble) { PullToRefreshListView listview =
	 * (PullToRefreshListView) findViewById(R.id.listview);
	 * listview.onRefreshComplete();
	 * 
	 * } };
	 * 
	 * RequestParams params = new RequestParams(); params.put("type",
	 * "wzreposity.hotcity"); try { JSONObject obj = new
	 * JSONObject(getSharedPreferences("mgw_data",
	 * Context.MODE_PRIVATE).getString("mgw_data", "")); params.put("userid",
	 * obj.getString("UserID")); params.put("serial", obj.getString("serial"));
	 * params.put("telephone", obj.getString("Telephone")); } catch
	 * (JSONException e1) { e1.printStackTrace(); }
	 * 
	 * MgqRestClient.get("http://Android2.mgw.cc/index.aspx", params,
	 * loginHandler);
	 * 
	 * }
	 */

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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = mInflater.inflate(R.layout.item_city_bt, null);
			Button bt_city = (Button) view.findViewById(R.id.bt_city);
			try {
				String name = m_array_list.getJSONObject(position).getString("cname");

				bt_city.setText(name);
				bt_city.setTag(String.valueOf(position));
				bt_city.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int index = Integer.valueOf((String) v.getTag());
						try {
							JSONObject obj = m_array_list.getJSONObject(index);
							Intent resultIntent = new Intent();
							Bundle bundle = new Bundle();

							if (sp.getString("currentcityID", "").equals("")) {
								bundle.putString("result", "first");// 返回界面需要界面重新初始化
								sp.edit().putString("currentcityname", obj.getString("cname")).commit();
								sp.edit().putString("currentcityID", obj.getString("cid")).commit();
							} else {
								bundle.putString("result", "Second");// 返回界面需要界面不需要重新初始化
							}

							sp.edit().putString("city", obj.getString("cname")).commit();
							sp.edit().putString("cid", obj.getString("cid")).commit();
							resultIntent.putExtras(bundle);
							MyCityListActivity.this.setResult(RESULT_OK, resultIntent);
							finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return view;
		}
	}

	/*
	 * private void getDataAllCity(boolean show,final String city) {
	 * MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
	 * 
	 * @Override public void onSuccess(String response) {
	 * super.onSuccess(response);
	 * 
	 * try { if(city.length() == 0) { sp.edit().putString("allcity",
	 * response).commit();
	 * findViewById(R.id.cityLetterListView).setVisibility(View.VISIBLE); } else
	 * { findViewById(R.id.cityLetterListView).setVisibility(View.INVISIBLE); }
	 * JSONObject obj = new JSONObject(response);
	 * 
	 * Log.e("allcity", obj.toString()); if (obj.getInt("flag") == 0) {
	 * actvList.clear(); codeList.clear(); mCityArray.clear(); JSONArray items =
	 * obj.getJSONArray("items"); for (int i = 0; i < items.length(); i++) {
	 * CityModel model = new CityModel(); model.cityCode =
	 * items.getJSONObject(i).getString("cid"); model.cityName =
	 * items.getJSONObject(i).getString("cname"); model.nameSort =
	 * items.getJSONObject(i).getString("pinyin").substring(0, 1);
	 * mCityArray.add(model); actvList.add(model.cityName);
	 * codeList.add(model.cityCode); }
	 * 
	 * adapter = new ListAdapter(MyCityListActivity.this, mCityArray);
	 * mCityLit.setAdapter(adapter); }
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); } }
	 * 
	 * public void onFailure(Throwable ble) {
	 * 
	 * } };
	 * 
	 * RequestParams params = new RequestParams(); params.put("type",
	 * "wzreposity.citysearch"); try { JSONObject obj = new
	 * JSONObject(getSharedPreferences
	 * ("mgw_data",Context.MODE_PRIVATE).getString("mgw_data", ""));
	 * params.put("userid", obj.getString("UserID")); params.put("serial",
	 * obj.getString("serial")); params.put("ckey", city); } catch
	 * (JSONException e1) { e1.printStackTrace(); }
	 * 
	 * MgqRestClient.get("http://Android2.mgw.cc/index.aspx", params,
	 * loginHandler);
	 * 
	 * }
	 */
	@SuppressWarnings("unused")
	private void closeKeyboard(final View view) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

			}

		}, 100);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}