package com.mgw.member.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.easemob.EMCallBack;
import com.hx.hxchat.MGWHXSDKHelper;
import com.hx.hxchat.db.GroupsDao;
import com.hx.hxchat.db.InviteMessgeDao;
import com.hx.hxchat.db.LinkerDao;
import com.hx.hxchat.db.UserDao;
import com.hx.hxchat.domain.InviteMessage;
import com.hx.hxchat.domain.InviteMessage.InviteMesageStatus;
import com.hx.hxchat.domain.TopUser;
import com.hx.hxchat.domain.User;
import com.mgw.member.DaoMaster;
import com.mgw.member.DaoMaster.OpenHelper;
import com.mgw.member.DaoSession;
import com.mgw.member.bean.UserInfoBean;
import com.mgw.member.constant.Define_C;
import com.mgw.member.factory.FragmentFactory;
import com.mgw.member.factory.MgwWebViewFactory;
import com.mgw.member.ui.activity.login.LoginActivity;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.DBControl;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.PreferenceHelper;
import com.mgw.member.uitls.UIUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by huyan.
 */
public class BaseApplication extends Application {
	private final static String TAG = BaseApplication.class.getSimpleName();
	private final String APP_ID = "wxbf54c682cb7fded7";
	private UserInfoBean bean;

	/** 全局Context */
	private static BaseApplication mInstance;
	/** 主线程ID */
	private static int mMainThreadId = -1;
	/** 主线程ID */
	private static Thread mMainThread;
	/** 主线程Handler */
	private static Handler mMainThreadHandler;
	/** 主线程Looper */
	private static Looper mMainLooper;

	public static int EXIT_APP = 11111;

	// ====
	public String m_user_id = null;
	public String m_playerName = null;
	public String m_GroupID = null;
	// ====
	/**
	 * 登录用户名
	 */
	private String userName = null;
	/**
	 * 登录密码
	 */
	private String password = null;
	/**
	 * 是否登录
	 */
	public Boolean mLogin = false;
	/**
	 * 是否登录hx
	 */
	public Boolean isHxLogined = false;

	// huanxi start

	public static Context applicationContext;
	// login user name
	private static final String PREF_PWD = "pwd";
	/** 是否使用自定义皮肤 */
	public static final boolean useSkin = false;

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static MGWHXSDKHelper hxSDKHelper = new MGWHXSDKHelper();

	// huanxi end
	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this.getApplicationContext();
		mInstance = this;
		mMainThreadId = android.os.Process.myTid();
		mMainThread = Thread.currentThread();
		mMainThreadHandler = new Handler();
		mMainLooper = getMainLooper();
		getLoginData();
		hxSDKHelper.onInit(mInstance);
		CrashHandler.getInstance().init(getApplicationContext());
		initImageLoader(applicationContext);
	}

	/**
	 * 获得登录data
	 */
	private void getLoginData() {
		SharedPreferences sharedata = getSharedPreferences("mgw_data", 0);
		String data = sharedata.getString("mgw_data", null);
		if (data != null && data.length() > 0) {
			try {
				m_user_id = new JSONObject(data).getString("UserID");
				m_playerName = sharedata.getString("mgw_name", null);
				Define_C.s_RelationID = sharedata.getString("RelationID", null);
				Define_C.s_isDelete = 1;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	public static BaseApplication getApplication() {
		return mInstance;
	}

	/** 获取主线程ID */
	public static int getMainThreadId() {
		return mMainThreadId;
	}

	/** 获取主线程 */
	public static Thread getMainThread() {
		return mMainThread;
	}

	/** 获取主线程的handler */
	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	/** 获取主线程的looper */
	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}

	public void setUserName(String username) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mInstance);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(Define_C.PREF_USERNAME, username).commit()) {
			userName = username;
		}
	}

	public void setPassword(String pwd) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mInstance);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(Define_C.PREF_PWD, pwd).commit()) {
			password = pwd;
		}
	}

	public static void initImageLoader(Context context) {
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

		MemoryCacheAware<String, Bitmap> memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).memoryCache(memoryCache).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO).build();
		// .tasksProcessingOrder(QueueProcessingType.LIFO).build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		// com.nostra13.universalimageloader.utils.L.setLogEnable(false);
	}

	/**
	 * 初始化环信
	 */
	public void InitHX() {

	}

	LinkerDao mLinkerDao;

	public LinkerDao GetLinkerDao() {
		if (mLinkerDao != null) {
			return mLinkerDao;
		} else {
			mLinkerDao = new LinkerDao(applicationContext);
			return mLinkerDao;
		}
	}

	UserDao mUserDao;

	public UserDao GetUserDao() {
		if (mUserDao != null) {
			return mUserDao;
		} else {
			mUserDao = new UserDao(applicationContext);
			return mUserDao;
		}
	}
	
	
	InviteMessgeDao inviteMessgeDao;
	public InviteMessgeDao GetInviteMessgeDao() {
		if (inviteMessgeDao != null) {
			return inviteMessgeDao;
		} else {
			inviteMessgeDao = new InviteMessgeDao(applicationContext);
			return inviteMessgeDao;
		}
	}
	
	GroupsDao groupsDao;
	public GroupsDao GetGroupsDao() {
		if (groupsDao != null) {
			return groupsDao;
		} else {
			groupsDao = new GroupsDao(applicationContext);
			return groupsDao;
		}
	}

	/**
	 * 获取内存中置顶好友user list
	 * 
	 * @return
	 */

	public Map<String, TopUser> getTopUserList() {
		return hxSDKHelper.getTopUserList();
	}

	/**
	 * 设置置顶好友到内存中
	 * 
	 * @param contactList
	 */
	public void setTopUserList(Map<String, TopUser> contactList) {
		hxSDKHelper.setTopUserList(contactList);
	}

	public DBControl mdb_handler = null;

	public DBControl GetDbhandler() {
		try {
			if (mdb_handler == null) {
				mdb_handler = new DBControl(getApplicationContext());
			}
		} catch (Exception ex) {
		}
		return mdb_handler;
	}

	private Map<String, User> contactList;

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		if (getUserName() != null && contactList == null) {
			UserDao dao = new UserDao(applicationContext);
			contactList = dao.getContactList();
		}
		Define_C.s_shouldflushcontactlist = true;
		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		if (userName == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			userName = preferences.getString(Define_C.PREF_USERNAME, null);
		}

		return userName;
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		if (password == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			password = preferences.getString(PREF_PWD, null);
		}
		return password;
	}

	/**
	 * 退出用户跳转到登录页面
	 * 
	 */
	public void logout(final Context context) {

		hxSDKHelper.logout(new EMCallBack() {
			@Override
			public void onSuccess() {
				// 重置
				SharedPreferences.Editor sharedata = getSharedPreferences("mgw_data", 0).edit();
				sharedata.putString("mgw_data", "");
				sharedata.putString("mgw_pwd", "");
				sharedata.putString("mgw_account", "");
				sharedata.putString("hxLogOut", "2");
				// sharedata.putBoolean("logined", false);
				sharedata.commit();
				bean = null;
				Define_C.bHxLogined = false;
				setPassword(null);
				setContactList(null);
				UIUtils.runInMainThread(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setClass(context, LoginActivity.class);
						context.startActivity(intent);
						LogUtils.i(TAG, "退出成功0");
						PreferenceHelper.getInstance(applicationContext).setAppLogined(false);
						LogUtils.i(TAG, "退出成功1");
						MgwWebViewFactory.getInstance(context).clearWebVector();
						LogUtils.i(TAG, "退出成功2");
						FragmentFactory.clearFragments();
						LogUtils.i(TAG, "退出成功3");
						AppManager.getAppManager().finishAllActivity(LoginActivity.class);
						LogUtils.i(TAG, "退出成功4");
					}
				});

			}

			@Override
			public void onProgress(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}


	public boolean isApKupdate = false;
	public boolean isHtmlupdate = true;
	public String fv_exp;
	public String FVersion_Name;
	public String FVersion_FileURL;



	public UserInfoBean getBean() {
		return bean;
	}

	public void setBean(UserInfoBean bean) {
		this.bean = bean;
	}
	
	
	/**
	 * 获取环信未读申请与通知消息(被邀请,*对方申请)
	 * 
	 * @return
	 */
	public synchronized int getUnreadAddressCountTotal() {
		List<InviteMessage> messagesList = BaseApplication.getApplication().GetInviteMessgeDao().getMessagesList();
		List<InviteMessage> messagesList1=new ArrayList<InviteMessage>();
		if (messagesList.size()>0) {
			for(InviteMessage ii:messagesList){
				if(ii.getStatus()==	InviteMesageStatus.BEINVITEED||ii.getStatus()==InviteMesageStatus.BEAPPLYED){
					messagesList1.add(ii);
				}
			}
		}
		return messagesList1.size();
	}
	
	
	// -----start
	/**
	 * 获得greenDao
	 */
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	/**
	 * 取得DaoMaster
	 * 
	 * @param context
	 * @return
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			OpenHelper helper = new DaoMaster.DevOpenHelper(context, "public_data", null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	/**
	 * 取得DaoSession
	 * @param context
	 * @return
	 */
	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}

	// -----end

}
