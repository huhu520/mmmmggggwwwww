package com.mgw.member.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.hx.hxchat.db.UserDao;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.otto.GetHxInfoNoticeEvent;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.bean.AppUpdateInfoBean;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.manager.ThreadManager;
import com.mgw.member.manager.UpdateManager;
import com.mgw.member.ottoEvent.AppShouldUpdateEvent;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ui.activity.UpdateAlertDialog;
import com.mgw.member.ui.activity.cityleague.CityleagueActivity;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.HomeWatcher;
import com.mgw.member.uitls.HomeWatcher.OnHomePressedListener;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.UnZipFiles;
import com.mgw.member.uitls.Utils;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

public class Myservice extends Service implements OnHomePressedListener {
	String TAG = "Myservice";
	/*
	 * 百度地图定位
	 */
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = null;
	private Context mContext;
	private HomeWatcher mHomeWatcher;
	/** 解压路径 */
	private static final String htmlsavePath = Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/unziphtml";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
		BusProvider.getInstance().register(this);
		LogUtils.i("service onCreate");
		mContext = this;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
		LogUtils.i("service onDestroy");
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		LogUtils.i("service onStart");
		
		mHomeWatcher = new HomeWatcher(this);
		mHomeWatcher.setOnHomePressedListener(this);
		mHomeWatcher.startWatch();
		initData();
		GetLoaction();
	}

	/**
	 * 初始化数据 将apk自带的自拍文件夹解压到本地的htmlsavePath文件夹下，然后检查是否需要更新
	 * */
	private void initData() {
		ThreadManager.getSinglePool().execute(new Runnable() {

			@Override
			public void run() {

				int int1 = getSharedPreferences("mgw_data", 0).getInt("starttime", 1);
				if (int1 <= 1) {

					UnZipFiles unZipFiles = new UnZipFiles();
					try {
						File file = new File(htmlsavePath);

						if (!file.exists()) {
							file.mkdirs();

						}
						Log.i("unzip文件夹", htmlsavePath);
						unZipFiles.upZipinputstreamFile(getAssets().open("html.zip"), htmlsavePath);

						SharedPreferences.Editor spEditor = getSharedPreferences("mgw_data", 0).edit();
						spEditor.putInt("starttime", ++int1);
						resetshowSplash();
						spEditor.commit();
						// TODO
					} catch (ZipException e) {

						e.printStackTrace();
						LogUtils.e(TAG, e.toString());
					} catch (IOException e) {
						LogUtils.e(TAG, e.toString());
						e.printStackTrace();
					}
					// getcod1();
					updateApk();
				} else {
					// getcod1();
					updateApk();
				}
			}
		});

	}

	/**
	 * 
	 * 如果检测到有apk更新直接下载apk，如果没有apk更新但是有html包更新， 获取每个html版本的地址传给updatamanager进行下载
	 */
	private void updateApk() {
		LogUtils.i(TAG, "updateApk");

		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
			}

			@Override
			protected String doInBackground(Void... params) {

				String res = Utils.getAppUpdateInfo(getVersionName(), getHtmlVersionCode());
				LogUtils.e("RES:" + res + ",getVersionName()=" + getVersionName() + ",getHtmlVersionCode()=" + getHtmlVersionCode());
				Gson gson = new Gson();
				if (res != null && res.contains("</body>")) {
					// 可能是连接了没有网的wifi
					return "ok";
				}
				AppUpdateInfoBean updateInfoBean = gson.fromJson(res, AppUpdateInfoBean.class);

				if (updateInfoBean != null && "0".equals(updateInfoBean.flag) && updateInfoBean.items.size() > 0) {
					if (updateInfoBean.items.get(0).IsReplace.equals("1")) {
						// 第一个是apk更新
						// 进行apk更新,跳转更新apk
						BaseApplication.getApplication().isHtmlupdate = false;
						BaseApplication.getApplication().isApKupdate = true;
						BaseApplication.getApplication().fv_exp = updateInfoBean.items.get(0).FVersion_Explain;

						BaseApplication.getApplication().FVersion_Name = updateInfoBean.items.get(0).FVersion_Name;

						BaseApplication.getApplication().FVersion_FileURL = updateInfoBean.items.get(0).FVersion_FileURL;
						LogUtils.w(TAG, "update  apk更新");
						return "";
					} else if (updateInfoBean.items.get(0).IsReplace.equals("0")) {
						BaseApplication.getApplication().isApKupdate = false;
						BaseApplication.getApplication().isHtmlupdate = true;
						// 第一个是不是apk更新 apk更新 依次下载更新下载更新直到完成更新进入登录页面
						int size = updateInfoBean.items.size();
						LogUtils.w(TAG, "update  html0更新开始");

						for (int i = 0; i < size; i++) {

							LogUtils.w(TAG, "update  当前htnl版本" + getHtmlVersionCode() + "下载版本：" + updateInfoBean.items.get(i).FVersion_Name);
							if (updateInfoBean.items.get(i).FVersion_Name.compareTo(getHtmlVersionCode()) > 0) {
								// 首先下载，
								File downLoad = Utils.downLoad(updateInfoBean.items.get(i).FVersion_FileURL, UpdateManager.htmlsavePath, "html.zip", null);
								if (downLoad != null) {

									// 然后解压，
									UnZipFiles unZipFiles = new UnZipFiles();
									File file = new File(UpdateManager.unziphtmlsavePath);
									if (!file.exists()) {
										file.mkdirs();
									}
									try {
										unZipFiles.upZipFile(new File(UpdateManager.htmlsavePath + "html.zip"), UpdateManager.unziphtmlsavePath);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										LogUtils.e(e);
									}

								}
								LogUtils.w(TAG, "update  html0更新结束");
								resetshowSplash();
								BaseApplication.getApplication().isHtmlupdate = false;
							} else {
								LogUtils.w(TAG, "update  html0g过小");
							}
							//
						}
						return "ok1";
					}

				}
				LogUtils.w(TAG, "update  无更新");
				BaseApplication.getApplication().isHtmlupdate = false;
				return "ok";

			}

			protected void onPostExecute(Void result) {
			};

			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			}

		}.execute();

		// if (res != null) {
		// try {
		// if (new JSONObject(res).optJSONArray("items") == null) {
		// return;
		// }
		// JSONArray array = new JSONObject(res).getJSONArray("items");
		// /*
		// * 如果检测到有apk更新直接弹出下载apk对话框，如果没有apk更新但是有html包更新，
		// * 获取每个html版本的地址传给updatamanager进行下载
		// */
		// for (int i = array.length() - 1; i >= 0; i--) {
		// JSONObject obj = array.getJSONObject(i);
		// if (obj.getInt("IsReplace") == 1 &&
		// obj.getString("FVersion_Name").compareTo(getVersionName()) > 0) {
		//
		// flag = 0;
		// tips[0] = obj.getString("FVersion_FileURL");
		// tips[1] = obj.getString("FVersion_Explain");
		// tips[2] = obj.getString("FVersion_Name");
		// tips[3] = obj.getString("IsReplace");
		// // SharedPreferences.Editor spEditor =
		// // getSharedPreferences("mgw_data", 0).edit();
		// // spEditor.putString("htmlcode",
		// // obj.getString("FVersion_Name"));
		// // spEditor.commit();
		// handler.sendMessage(handler.obtainMessage(flag, tips));
		// return;
		// // 这里有时可能需要处理一下，判断条件有可能会出问题
		// } else if (obj.getInt("IsReplace") == 0 &&
		// obj.getString("FVersion_Name").compareTo(getSharedPreferences("mgw_data",
		// 0).getString("htmlcode", getVersionName())) > 0) {
		//
		// SharedPreferences.Editor spEditor = getSharedPreferences("mgw_data",
		// 0).edit();
		// spEditor.putString("htmlcode", obj.getString("FVersion_Name"));
		// spEditor.commit();
		// flag = 0;
		// if (tips[0] != null) {
		// tips[0] = tips[0] + ";" + obj.getString("FVersion_FileURL");
		// } else {
		// tips[0] = obj.getString("FVersion_FileURL");
		// }
		//
		// tips[1] = obj.getString("FVersion_Explain");
		// tips[2] = obj.getString("FVersion_Name");
		// tips[3] = obj.getString("IsReplace");
		// }
		//
		// }
		// tips[4] = array.length() + "";
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// handler.sendMessage(handler.obtainMessage(flag, tips));
	}

	/** 本地的html版本号 */
	private String LocalHtmlcod = "";

	/**
	 * 如果检测到有apk更新直接下载apk，如果没有apk更新但是有html包更新， 获取每个html版本的地址传给updatamanager进行下载
	 */
	private void getcod1() {
		String[] tips = new String[5];
		int flag = 1;
		LocalHtmlcod = getSharedPreferences("mgw_data", 0).getString("htmlcode", getVersionName());

		LogUtils.i(TAG, "getcod1");
		String res = Utils.getAppUpdateInfo(LocalHtmlcod);
		if (res != null) {
			try {
				if (new JSONObject(res).optJSONArray("items") == null) {
					return;
				}
				JSONArray array = new JSONObject(res).getJSONArray("items");
				/*
				 * 如果检测到有apk更新直接弹出下载apk对话框，如果没有apk更新但是有html包更新，
				 * 获取每个html版本的地址传给updatamanager进行下载
				 */
				for (int i = array.length() - 1; i >= 0; i--) {
					JSONObject obj = array.getJSONObject(i);
					if (obj.getInt("IsReplace") == 1 && obj.getString("FVersion_Name").compareTo(getVersionName()) > 0) {

						flag = 0;
						tips[0] = obj.getString("FVersion_FileURL");
						tips[1] = obj.getString("FVersion_Explain");
						tips[2] = obj.getString("FVersion_Name");
						tips[3] = obj.getString("IsReplace");
						// SharedPreferences.Editor spEditor =
						// getSharedPreferences("mgw_data", 0).edit();
						// spEditor.putString("htmlcode",
						// obj.getString("FVersion_Name"));
						// spEditor.commit();
						handler.sendMessage(handler.obtainMessage(flag, tips));
						return;
						// 这里有时可能需要处理一下，判断条件有可能会出问题
					} else if (obj.getInt("IsReplace") == 0 && obj.getString("FVersion_Name").compareTo(getSharedPreferences("mgw_data", 0).getString("htmlcode", getVersionName())) > 0) {

						SharedPreferences.Editor spEditor = getSharedPreferences("mgw_data", 0).edit();
						spEditor.putString("htmlcode", obj.getString("FVersion_Name"));
						spEditor.commit();
						flag = 0;
						if (tips[0] != null) {
							tips[0] = tips[0] + ";" + obj.getString("FVersion_FileURL");
						} else {
							tips[0] = obj.getString("FVersion_FileURL");
						}

						tips[1] = obj.getString("FVersion_Explain");
						tips[2] = obj.getString("FVersion_Name");
						tips[3] = obj.getString("IsReplace");
					}

				}
				tips[4] = array.length() + "";
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		handler.sendMessage(handler.obtainMessage(flag, tips));
	}

	/**
	 * 这个handler用来接收gecod1()方法传回来的结果，如果有则调用updatemanager下载最新apk/html
	 */

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {

				String[] tips = (String[]) msg.obj;
				if (tips[3].equals("1")) {
					// SharedPreferences.Editor sharedata =
					// getSharedPreferences("mgw_data", 0).edit();
					// sharedata.putString("mgw_data", "");
					// sharedata.putString("mgw_pwd", "");
					// sharedata.commit();
					// apk
					UpdateManager mUpdateManager = new UpdateManager(getApplicationContext(), tips[0], tips[1], tips[2]);
					// mUpdateManager.checkUpdateInfo();
					// startUpdateAlert(tips[0],tips[1],tips[2]);
					AppShouldUpdateEvent appShouldUpdateEvent = getAppShouldUpdateEvent();
					appShouldUpdateEvent.setCanUpdate(true);
					appShouldUpdateEvent.setFVersion_FileURL(tips[0]);
					appShouldUpdateEvent.setFv_exp(tips[1]);
					appShouldUpdateEvent.setFVersion_Name(tips[2]);
					BusProvider.getInstance().post(appShouldUpdateEvent);

				} else {
					// html
					UpdateManager manager = new UpdateManager(getApplicationContext(), tips[0], tips[4]);
					manager.checkUpdateInfo();
				}

			} else {

			}

			super.handleMessage(msg);
		}
	};

	/**
	 * 跳转到升级activity
	 * 
	 * @param FVersion_Name
	 * @param fv_exp
	 * @param url
	 * @param updateManager
	 */
	private void startUpdateAlert(String fv_exp, String FVersion_Name, String url) {
		Intent intent = new Intent(mContext, UpdateAlertDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("fv_exp", fv_exp);
		intent.putExtra("FVersion_Name", FVersion_Name);
		intent.putExtra("FVersion_FileURL", url);
		mContext.startActivity(intent);
	}

	/** 获取当前apk版本 */
	public String getVersionName() {
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
			return pInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "1.0";
	}

	private String getHtmlVersionCode() {
		String readProperties = FileUtils.readProperties(UpdateManager.unziphtmlsavePath + "/version.txt", "version", "1.0.0");
		//
		LogUtils.i(TAG, "getHtmlVersionCode:" + readProperties);

		if (readProperties == null) {
			return "1.0.0";

		}
		return readProperties;
	}

	private void resetshowSplash() {
		String readProperties = FileUtils.readProperties(UpdateManager.unziphtmlsavePath + "/version.txt", "showsplash", "false");

		if (readProperties == null) {
			return;
		}

		if ("false".equals(readProperties)) {
			return;

		}
		if ("true".equals(readProperties)) {
			SharedPreferences sharedPreferences = getSharedPreferences("mgw_data", Context.MODE_PRIVATE);
			Editor edit = sharedPreferences.edit();
			edit.putInt("useCount", 0);
			edit.commit();
		}
		LogUtils.i(TAG, "resetshowSplash:" + readProperties);

	}

	/**
	 * 百度定位
	 */
	private void GetLoaction() {

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

	/**
	 * 百度定位监听器
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || location.getLongitude() == 0.0f) {
				CityleagueActivity.m_lng = 0;
				CityleagueActivity.m_lat = 0;
				mLocationClient.stop();
				mLocationClient.unRegisterLocationListener(myListener);

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

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(myListener);

		}
	}

	@Override
	public void onHomePressed() {
		UpdateManager.DismissDialog();

	}

	@Override
	public void onHomeLongPressed() {
		// UpdateManager.DismissDialog();

	}

	@Produce
	public AppShouldUpdateEvent getAppShouldUpdateEvent() {
		LogUtils.i("otto" + "appShouldUpdateEvent ");
		AppShouldUpdateEvent appShouldUpdateEvent = new AppShouldUpdateEvent();
		return appShouldUpdateEvent;

	}

	@Subscribe
	public void GetHxInfoNoticeEvent(GetHxInfoNoticeEvent GetHxInfoNoticeEvent) {
		if (GetHxInfoNoticeEvent != null && GetHxInfoNoticeEvent.isNeedRefresh()) {

			// processContactsAndGroups(GetHxInfoNoticeEvent.getGroupId());

			inithxAsyn(GetHxInfoNoticeEvent.getGroupId());

		}

	}

	private void inithxAsyn(String groupId) {
		// TODO Auto-generated method stub
		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				LogUtils.i("otto", "inithxAsyn");
				EMGroupManager.getInstance().loadAllGroups();
				EMChatManager.getInstance().loadAllConversations();

				boolean login = false;
				Map<String, User> userlist = new HashMap<String, User>();
				try {
					JSONObject jo = UserUtils.getfriendinfo();

					int flag = jo.getInt("flag");

					if (flag == 0) {
						if (!jo.isNull("items")) {
							JSONArray array = jo.getJSONArray("items");
							userlist.clear();
							for (int i = 0; i < array.length(); i++) {
								jo = array.getJSONObject(i);
								User user = new User();
								user.setNick(jo.getString("NickName"));
								user.setAvatar(jo.getString("MemberPic"));
								user.setUsername(jo.getString("UserId"));
								user.setreferee(jo.getString("referee"));
								userlist.put(user.getUsername(), user);
							}
						}
					}

					// }
					// 添加user"申请与通知"
					User newFriends = new User();
					newFriends.setUsername(com.hx.hxchat.Constant.NEW_FRIENDS_USERNAME);
					newFriends.setNick("申请与通知");
					newFriends.setHeader("");
					userlist.put(com.hx.hxchat.Constant.NEW_FRIENDS_USERNAME, newFriends);

					// 添加"群聊"
					User groupUser = new User();
					groupUser.setUsername(com.hx.hxchat.Constant.GROUP_USERNAME);
					groupUser.setNick("群聊");
					groupUser.setHeader("");
					userlist.put(com.hx.hxchat.Constant.GROUP_USERNAME, groupUser);

					// TODO 存入内存
					BaseApplication.getApplication().setContactList(userlist);

					// TODO 存入db
					UserDao dao = new UserDao(mContext);
					List<User> users = new ArrayList<User>(userlist.values());
					dao.saveContactList(users);

					// TODO 获取黑名单列表
					List<String> blackList = EMContactManager.getInstance().getBlackListUsernamesFromServer();
					EMContactManager.getInstance().saveBlackList(blackList);
					// TODO
					// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中(),群聊里只有groupid和groupname等简单信息，不包含members
					List<EMGroup> groupsFromServer = EMGroupManager.getInstance().getGroupsFromServer();

//					for (EMGroup dd : groupsFromServer) {
//						EMGroup groupFromServer = EMGroupManager.getInstance().getGroupFromServer(dd.getGroupId());
//						List<String> members = groupFromServer.getMembers();
//						EMGroupManager.getInstance().createOrUpdateLocalGroup(groupFromServer);
//						// EMGroup group =
//						//
//						EMGroupManager.getInstance().getGroup(dd.getGroupId());
//						LogUtils.i(TAG, "members:" + members.toString() + "local members:");
//					}

					login = true;

				} catch (Exception ex) {
					ex.printStackTrace();
					login = false;
					UIUtils.showToastSafe("get list failed");
					LogUtils.e(TAG, ex.toString());
				}

			}
		};

		ThreadManager.getSinglePool().execute(runnable);

	}

}
