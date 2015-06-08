package com.mgw.member.ui.activity;

/**create by hyb
 * modify by hy
 * */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.hx.hxchat.db.LinkerDao;
import com.mgw.member.R;
import com.mgw.member.http.HttpHelper;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.manager.ThreadManager;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.service.Myservice;
import com.mgw.member.ui.activity.login.LoginActivity;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.NetworkProber;
import com.mgw.member.uitls.PreferenceHelper;
import com.mgw.member.uitls.UIUtils;

public class FirstActivity extends MGWBaseActivity {
	private static final String TAG = FirstActivity.class.getSimpleName();
	/** 解压路径 */
	private static final String htmlsavePath = Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/unziphtml";
	/** guide解压路径 */
	private static final String imagesavePath = Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/guideimage";

	/**
	 * 网络连接错误
	 */
	public final static int MESSAGE_TYPE_NETERROR = 208;
	/**
	 * 动画播放完毕
	 */
	public final static int MESSAGE_TYPE_ANIMOVER = 209;
	public final static int MESSAGE_TYPE_PLAYANIM = 210;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private boolean isUpdateApping = false;

	@Override
	protected void initView() {
		super.initView();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);
		AppManager.getAppManager().addActivity(this);
		Intent intent = new Intent(this, Myservice.class);
		startService(intent);
		CheckNet();
		// initData();
		ThreadManager.getSinglePool().execute(new InitDbTask());

		//

		SharedPreferences.Editor sharedata = getSharedPreferences("mgw_data", 0).edit();

		sharedata.putString("mgw_pwd", "");
		sharedata.putString("mgw_account", "");

		sharedata.commit();
		//

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	/**
	 * 跳转到其他UI Created by huyan
	 */
	private void jumpToOtherUI() {
		Animation animation = new AlphaAnimation(1.0f, 0.6f);
		animation.setDuration(3000);
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				m_handler.sendEmptyMessage(MESSAGE_TYPE_ANIMOVER);
			}
		});
		findViewById(R.id.root).startAnimation(animation);

	}

	@Override
	protected void onResume() {
		super.onResume();
		BusProvider.getInstance().register(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		BusProvider.getInstance().unregister(this);
	}

	/**
	 * 消息处理handler
	 */
	private final Handler m_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_TYPE_NETERROR:
				// Toast.makeText(getApplicationContext(), "网络异常,美顾问即将退出！",
				// Toast.LENGTH_SHORT).show();
				SharedPreferences sharedPreferences = getSharedPreferences("mgw", Context.MODE_PRIVATE);
				Editor edit = sharedPreferences.edit();
				edit.putBoolean("logined", false);
				edit.commit();
				NetworkProber.setNetworkMethod(FirstActivity.this);

				break;
			case MESSAGE_TYPE_ANIMOVER:
				UIUtils.postDelayed(new Runnable() {

					@Override
					public void run() {
						int i=0;
						//防止低端机型 运算过慢 导致anr异常
						while (BaseApplication.getApplication().isHtmlupdate&&i<30) {
							
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									SystemClock.sleep(100);
									
								}
							}).start();
							
							LogUtils.i(TAG, "SystemClock.sleep"+i);
							i++;
						}
						SharedPreferences sharedPreferences = getSharedPreferences("mgw_data", Context.MODE_PRIVATE);
						int useCount = sharedPreferences.getInt("useCount", 0);
						boolean logined = sharedPreferences.getBoolean("logined", false);

						if (!BaseApplication.getApplication().isApKupdate) {
							if (useCount < 1) {
								show2Splash();
							} else {
								Show2Login();

							}
							useCount++;
							Editor edit = sharedPreferences.edit();
							edit.putInt("useCount", useCount);
							edit.commit();

						} else {

							LogUtils.i(TAG, "正在下载新应用");
							Intent intent = new Intent(mContext, UpdateAlertDialog.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("fv_exp", BaseApplication.getApplication().fv_exp);
							intent.putExtra("FVersion_Name", BaseApplication.getApplication().FVersion_Name);
							intent.putExtra("FVersion_FileURL", BaseApplication.getApplication().FVersion_FileURL);
							mContext.startActivity(intent);

						}

					}
				}, 0);
				break;
			case MESSAGE_TYPE_PLAYANIM:
				jumpToOtherUI();
				break;
			default:
				break;
			}
		};
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		CheckNet();

	};

	/** 检查网络连接是否可用 */
	public void CheckNet() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (!HttpHelper.isMobileNetworkActive(FirstActivity.this) && !HttpHelper.isNetworkAvailable(FirstActivity.this)) {
					m_handler.sendEmptyMessage(MESSAGE_TYPE_NETERROR);
				} else {
					m_handler.sendEmptyMessage(MESSAGE_TYPE_PLAYANIM);

				}
			}
		}).start();
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

	/** 获取当前html版本 */
	public String gethtmlVersionName() {
		try {
			SharedPreferences sp = getSharedPreferences("mgw_data", 0);
			return sp.getString("htmlVersion", "1");
		} catch (Exception e) {

		}
		return "1";
	}

	/**
	 * 进入登录页面
	 */
	private void Show2Login() {
		// SharedPreferences.Editor sharedata = getSharedPreferences("mgw_data",
		// 0).edit();
		// sharedata.putString("mgw_data", "");
		// sharedata.putString("mgw_pwd", "");
		// sharedata.commit();
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

	/** 跳转到引导页面 */
	private void show2Splash() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}

	/**
	 * 初始化数据库操作
	 * 
	 * @author hy
	 */
	private class InitDbTask implements Runnable {

		@Override
		public void run() {
			
			if (PreferenceHelper.getInstance(mContext).getAppLogined()) {

//				try {
//					BaseApplication.getApplication().getFriendsInfo();
//				} catch (EaseMobException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					LogUtils.e(TAG, e.toString());
//				}
			}

			// CheckLinker();
			// if (count < 1) {
			// FileUtils.CopyAssets(mContext, "image", imagesavePath);
			// }
		}
	}

	// huanxing start
	@Override
	protected void onStart() {
		super.onStart();

		// new Thread(new Runnable() {
		// public void run() {
		// if (MGWHXSDKHelper.getInstance().isLogined()) {
		// // ** 免登陆情况 加载所有本地群和会话
		// //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
		// //加上的话保证进了主页面会话和群组都已经load完毕
		// long start = System.currentTimeMillis();
		// EMGroupManager.getInstance().loadAllGroups();
		// EMChatManager.getInstance().loadAllConversations();
		// long costTime = System.currentTimeMillis() - start;
		// //等待sleeptime时长
		// if (sleepTime - costTime > 0) {
		// try {
		// Thread.sleep(sleepTime - costTime);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// //进入主页面
		// startActivity(new Intent(SplashActivity.this, MainActivity.class));
		// finish();
		// }else {
		// try {
		// Thread.sleep(sleepTime);
		// } catch (InterruptedException e) {
		// }
		// startActivity(new Intent(SplashActivity.this, LoginActivity.class));
		// finish();
		// }
		// }
		// }).start();

	}

	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		String st = getResources().getString(R.string.Version_number_is_wrong);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String version = packinfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return st;
		}
	}

	// huanxing start
	public static final int REQUEST_CODE_SETNICK = 1;

	/*
	 * 检查临时联系人表
	 */
	private void CheckLinker() {

		String strCreateTableSql = "CREATE TABLE " + LinkerDao.TABLE_NAME + " (" + LinkerDao.COLUMN_NAME_NICK + " TEXT, " + LinkerDao.COLUMN_NAME_UserID + " TEXT, " + LinkerDao.COLUMN_NAME_PHOTO
				+ " TEXT, " + LinkerDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";
		if (BaseApplication.getApplication().GetDbhandler().tabIsExist(LinkerDao.TABLE_NAME)) {
			if (!BaseApplication.getApplication().GetDbhandler().isExistField(LinkerDao.TABLE_NAME, LinkerDao.COLUMN_NAME_NICK)
					|| !BaseApplication.getApplication().GetDbhandler().isExistField(LinkerDao.TABLE_NAME, LinkerDao.COLUMN_NAME_ID)
					|| !BaseApplication.getApplication().GetDbhandler().isExistField(LinkerDao.TABLE_NAME, LinkerDao.COLUMN_NAME_PHOTO)
					|| !BaseApplication.getApplication().GetDbhandler().isExistField(LinkerDao.TABLE_NAME, LinkerDao.COLUMN_NAME_UserID)
					|| !BaseApplication.getApplication().GetDbhandler().isExistField(LinkerDao.TABLE_NAME, LinkerDao.COLUMN_LINKER_USER)) {
				BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate("drop table " + LinkerDao.TABLE_NAME);
				BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(strCreateTableSql);
			}
		} else {
			BaseApplication.getApplication().GetDbhandler().LocalExecuteUpdate(strCreateTableSql);
		}

	}

	// @Subscribe
	// public void AppShouldUpdateEvent(AppShouldUpdateEvent event) {
	// if (event.isCanUpdate()) {
	// isUpdateApping=true;
	// Intent intent = new Intent(mContext, UpdateAlertDialog.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.putExtra("fv_exp", event.getFv_exp());
	// intent.putExtra("FVersion_Name", event.getFVersion_Name());
	// intent.putExtra("FVersion_FileURL", event.getFVersion_FileURL());
	// mContext.startActivity(intent);
	//
	// }
	//
	// }

}
