package com.mgw.member.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.hx.hxchat.fragment.NewsFragmentCopy;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.factory.FragmentFactory;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.manager.SkinChangeManager.ChangeSkinStatusListener;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ottoEvent.ReGetLoginInfoEvent;
import com.mgw.member.ottoEvent.UnreadInviteRefeshEvent;
import com.mgw.member.ui.fragment.BackHandledInterface;
import com.mgw.member.ui.fragment.BaseFragment;
import com.mgw.member.ui.fragment.FindFragment;
import com.mgw.member.ui.fragment.HomeFragment;
import com.mgw.member.ui.fragment.MallFragment;
import com.mgw.member.ui.fragment.MyInfoFragment;
import com.mgw.member.ui.fragment.NewsFragment;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.PreferenceHelper;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.ViewUtils;
import com.squareup.otto.Subscribe;

/**
 * 主界面
 * 
 * @author huyan
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MainActivity extends MGWBaseActivity implements BackHandledInterface {
	public static MainActivity mainActivity;
	private BaseFragment mBackHandedFragment;
	private boolean hadIntercept;
	public final static int num = 3;
	Fragment homeFragment;
	Fragment personFragment;
	Fragment sorttypeFragment;
	public Class[] fragmentClass = { HomeFragment.class, NewsFragmentCopy.class, FindFragment.class, MallFragment.class, MyInfoFragment.class };
	private RadioGroup radioGroup;
	private RadioButton radio_home;
	private RadioButton radio_news;
	private RadioButton radio_find;
	private RadioButton radio_mall;
	private RadioButton radio_myinfo;
	private BaseFragment fragment = null;
	/** 当前显示的角标 */
	private int m_currentTabIndex = 1;
	/** 当前点击的角标 */
	public int click_m_index = 1;

	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;

	private ChangeSkinStatusListener changeSkinStatusListener;

	public void setChangeSkinStatusListener(ChangeSkinStatusListener changeSkinStatusListener) {
		this.changeSkinStatusListener = changeSkinStatusListener;
	}

	public TextView unreadLabel;
	public TextView unreadInvite;
	private FrameLayout fl_radiogroup;

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = this;

	}

	/** 控件初始化，会在父View中被调用 */
	@Override
	protected void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("loginedsuccess");
		registerReceiver(myReceiver, intentFilter);

		view = UIUtils.inflate(R.layout.main);
		fl_radiogroup = (FrameLayout) findViewById(R.id.fl_radiogroup);
		radio_button1 = (RadioButton) findViewById(R.id.radio_button1);
		/** 未读的消息数 */
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number_main);
		/** 未读的邀请消息数 */
		unreadInvite = (TextView) findViewById(R.id.unread_address_number);
		//
		radioGroup = (RadioGroup) findViewById(R.id.main_radio);
		// initChangeSkin(view);

		((RadioButton) radioGroup.findViewById(R.id.radio_button0)).setChecked(true);
		fragment = FragmentFactory.createFragment(FragmentFactory.TAB_HOME);
		getSupportFragmentManager().beginTransaction().add(R.id.tabcontent, fragment).add(R.id.tabcontent, FragmentFactory.createFragment(FragmentFactory.TAB_NEWS))
				.add(R.id.tabcontent, FragmentFactory.createFragment(FragmentFactory.TAB_FIND)).add(R.id.tabcontent, FragmentFactory.createFragment(FragmentFactory.TAB_MALL))
				.add(R.id.tabcontent, FragmentFactory.createFragment(FragmentFactory.TAB_MYINFO)).hide(FragmentFactory.createFragment(FragmentFactory.TAB_NEWS))
				.hide(FragmentFactory.createFragment(FragmentFactory.TAB_FIND)).hide(FragmentFactory.createFragment(FragmentFactory.TAB_MALL))
				.hide(FragmentFactory.createFragment(FragmentFactory.TAB_MYINFO)).show(fragment).commit();
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_button0:
					click_m_index = FragmentFactory.TAB_HOME;

					break;
				case R.id.radio_button1:
					click_m_index = FragmentFactory.TAB_NEWS;
					break;
				case R.id.radio_button2:
					click_m_index = FragmentFactory.TAB_FIND;
					break;
				case R.id.radio_button4:
					click_m_index = FragmentFactory.TAB_MALL;
					break;
				case R.id.radio_button3:
					click_m_index = FragmentFactory.TAB_MYINFO;
					break;
				}
				m_currentTabIndex = SwicthFragment(click_m_index, m_currentTabIndex);
				if (changeSkinStatusListener != null) {
					changeSkinStatusListener.radioGroupCheck(checkedId);
				}
			}

		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initChangeSkin(view);
		BusProvider.getInstance().register(this);
		// updateUnreadLabel(1);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		BusProvider.getInstance().unregister(this);
	}

	@Subscribe
	public void GetLoginInfo(ReGetLoginInfoEvent event) {
		LogUtils.i("otto", "GetLoginInfo");
		if (event.isNeedGet()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					LogUtils.i("otto", "GetLoginInfo_Thread");
					JSONObject loginInfo = UserUtils.getLoginInfo();
					if (loginInfo != null) {
						LogUtils.i("otto", "loginInfo." + loginInfo.toString());

						try {
							if (loginInfo.getInt("flag") == 0) {
								LogUtils.i("otto", "loginInfo." + loginInfo.toString());
								PreferenceHelper.getInstance(UIUtils.getContext()).setLoginInfo(loginInfo.toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}).start();

		}

	}

	private void initChangeSkin(View view) {
		radio_home = ViewUtils.findViewById(view, R.id.radio_button0);
		radio_news = ViewUtils.findViewById(view, R.id.radio_button1);
		radio_find = ViewUtils.findViewById(view, R.id.radio_button2);
		radio_mall = ViewUtils.findViewById(view, R.id.radio_button4);
		radio_myinfo = ViewUtils.findViewById(view, R.id.radio_button3);
		// //为button和背景设置资源
		String skinBasePath = "/data/data/com.mgw.member/skin";
		String radiogroupres = skinBasePath + "/maintab_toolbar_bg.png";

		boolean boolean1 = getSharedPreferences("skin", 0).getBoolean("useSkin", false);

		if (FileUtils.dirIsExists(radiogroupres) && boolean1) {

			Bitmap bmp = BitmapFactory.decodeFile(radiogroupres);
			radioGroup.setBackgroundDrawable(new BitmapDrawable(bmp));
			setChangeSkinStatusListener(new ChangeSkinStatusListener() {

				@Override
				public void radioGroupCheck(int id) {

					switch (id) {
					case R.id.radio_button0:

						break;
					case R.id.radio_button1:

						break;
					case R.id.radio_button2:

						break;
					case R.id.radio_button4:

						break;
					case R.id.radio_button3:

						break;
					}

				}
			});

		} else {

			// 使用摸任的
			radioGroup.setBackgroundResource(R.drawable.maintab_toolbar_bg);

		}

	}

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
				if ((System.currentTimeMillis() - mExitTime) > 1500) {
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					// BaseApplication.getApplication().logout();
					AppManager.getAppManager().AppExit(mContext);
				}

			} else {

				return true;
			}

			return true;

		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 跳转底部fragment
	 * 
	 * @param m_index
	 *            点击的index
	 * @param m_currentTabIndex
	 *            当前的index
	 * @return 即将显示的index
	 */
	private int SwicthFragment(int m_index, int m_currentTabIndex) {
		if (m_currentTabIndex != m_index) {

			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();

			trx.hide(FragmentFactory.createFragment(m_currentTabIndex));
			if (!FragmentFactory.createFragment(m_index).isAdded()) {
				trx.add(R.id.tabcontent, FragmentFactory.createFragment(m_index));
			}
			trx.show(FragmentFactory.createFragment(m_index)).commit();
		}
		m_currentTabIndex = m_index;
		return m_currentTabIndex;
	}

	/** 菜单键点击的事件处理 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 在4.0以前，android通过菜单键来增加选项，在4.0后，提倡actionBar，所以菜单键增加的按钮可以显示到actionBar上，这里也能处理ActionBar上的菜单键事件
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setSelectedFragment(BaseFragment baseFragment) {
		// TODO Auto-generated method stub
		this.mBackHandedFragment = baseFragment;

	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {

	}

	public android.app.AlertDialog.Builder conflictBuilder;
	public boolean isConflictDialogShow;
	private View view;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	
		if (myReceiver != null)
			unregisterReceiver(myReceiver);
		if (conflictBuilder != null) {
			conflictBuilder = null;
		}
		mainActivity = null;
	}

	private BroadcastReceiver myReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			switch (action) {
			case "loginedsuccess":

				// startActivity(new Intent(context, LoginActivity.class));
				// finish();
				break;

			default:
				break;
			}

		}

	};
	private RadioButton radio_button1;

	// TODO huanxin
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// if (getIntent().getBooleanExtra("conflict", false)
		// && !MainActivity.s_Instance.isConflictDialogShow)
		// MainActivity.s_Instance.showConflictDialog();
	}

	/**
	 * 刷新未读消息数
	 * 
	 * @param count2
	 */
	public void updateUnreadLabel(int count2) {

		if (count2 > 0) {
			unreadLabel.setText(String.valueOf(count2));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}

	}
	/**
	 * 刷新邀请未读消息数
	 * 
	 * @param count2
	 */
	public void updateUnreadInvite(int count2) {
		
		if (count2 > 0) {
			unreadInvite.setText(String.valueOf(count2));
			unreadInvite.setVisibility(View.VISIBLE);
		} else {
			unreadInvite.setVisibility(View.INVISIBLE);
		}
		
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub

		super.onWindowFocusChanged(hasFocus);
		int left = (int) radio_button1.getX();
		int with = radio_button1.getWidth();
		int top = (int) radio_button1.getY();
		int right = left + unreadLabel.getWidth();
		int bottom = top + unreadLabel.getHeight();
		LogUtils.i("MainActivity", "left=" + left + ",top" + top + ",right" + right + ",bottom" + bottom);
		LogUtils.i("MainActivity", "left=" + left + ",top" + top + ",right" + right + ",bottom" + bottom);

		FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) unreadLabel.getLayoutParams();
		layoutParams.leftMargin = left + with *3/ 5;
		
		FrameLayout.LayoutParams layoutParams_invite = (android.widget.FrameLayout.LayoutParams) unreadInvite.getLayoutParams();
		layoutParams_invite.leftMargin = left + with*1 / 5;
//		
		
		unreadLabel.setLayoutParams(layoutParams);
		unreadInvite.setLayoutParams(layoutParams_invite);
		
		
		int count = 0;
		int count1 = 0;
		if (EMChatManager.getInstance().isConnected()) {
			count = getUnreadMsgCountTotal();
			count1 = BaseApplication.getApplication().getUnreadAddressCountTotal();
		}
		updateUnreadLabel(count);
		updateUnreadInvite(count1);

	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}
	
	
	
	/**
	 * 邀请消息的消费者
	 * @param UnreadInviteRefeshEvent
	 */
	@Subscribe
	public void UnreadInviteRefeshEvent(UnreadInviteRefeshEvent UnreadInviteRefeshEvent) {
		
		if(UnreadInviteRefeshEvent!=null&&UnreadInviteRefeshEvent.getCount()>0){
			updateUnreadInvite(UnreadInviteRefeshEvent.getCount());
		}
		
		
		
	}
	
}