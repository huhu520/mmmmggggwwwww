package com.hx.hxchat.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.fz.core.net.RequestHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hx.hxchat.Constant;
import com.hx.hxchat.db.LinkerDao;
import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.domain.UserFriendBean;
import com.hx.hxchat.domain.UserFriendBean.Items;
import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.http.Http;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.ui.activity.MainActivity;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.PreferenceHelper;
import com.mgw.member.uitls.UIUtils;
import com.squareup.picasso.Picasso;

public class UserUtils {

	public static String TAG = UserUtils.class.getSimpleName();

	/**
	 * 根据username获取相应user， r
	 * 
	 * @param username
	 * @return
	 */
	public static User getUserInfo(String username) {
		User user = BaseApplication.getApplication().getContactList().get(username);

		if (user == null) {
			User user2 = BaseApplication.getApplication().GetLinkerDao().getContact(username);

			if (user2 == null) {
				User useFromNet = getUseFromNetAsync(username);
				if (useFromNet != null) {

					// 如果没有存到linkdao中
					LinkerDao getLinkerDao = BaseApplication.getApplication().GetLinkerDao();
					useFromNet.setLinkcache("1");
					getLinkerDao.saveContact(useFromNet);

					return useFromNet;
				} else {
					user = new User(username);

				}
			} else {

				return user2;

			}

		}

		// if (user != null) {
		//
		//
		// // User useFromNet = getUseFromNetAsync(username);
		// user.setAvatar(useFromNet.getAvatar());
		// }
		return user;
	}

	/**
	 * 设置用户头像
	 * 
	 * @param username
	 */
	public static void setUserAvatar(Context context, String username, ImageView imageView) {
		if (TextUtils.isEmpty(username)) {
			return;
		}
		if (TextUtils.isDigitsOnly(username)) {

			User user = getUserInfo(username);

			if (user != null && user.getAvatar() != null && !"".equals(user.getAvatar())) {
				Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
			} else {
				Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
			}
		} else {

			Picasso.with(context).load(Uri.parse(username)).placeholder(R.drawable.default_avatar).into(imageView);
		}
	}

	/**
	 * 设置用户头像
	 * 
	 * @param context
	 * @param path
	 *            图像路径
	 * @param imageView
	 *            imageview
	 */
	public static void setUserAvatar(Context context, Uri path, ImageView imageView) {
		if (path instanceof Uri) {
			Picasso.with(context).load(path).placeholder(R.drawable.default_avatar).into(imageView);
		}
	}

	/**
	 * 通知自己服务器删除好友
	 * 
	 * @throws JSONException
	 */
	public static JSONObject delfriend(Context context, String frienduserid) throws JSONException {
		SharedPreferences sp = context.getSharedPreferences("mgw_data", 0);
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "member.delfriend");
		params.put("userid", sp.getString("mgw_userID", "0"));
		params.put("serial", sp.getString("mgw_serial", "0"));// serial登录的随机码（确定没有被挤下去）
		params.put("friend", frienduserid);
		String resultString = RequestHelper.PostBySingleBitmap(Define_C.mgw_url, params, null);
		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postGetOrderState", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 通知自己公司服务器同意添加某个好友
	 * 
	 * @throws JSONException
	 * 
	 */
	public static JSONObject addfriend(Context context, String userid) throws JSONException {

		SharedPreferences sp = context.getSharedPreferences("mgw_data", 0);
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "member.addfriend");
		params.put("userid", sp.getString("mgw_userID", "0"));
		params.put("serial", sp.getString("mgw_serial", "0"));
		params.put("friend", userid);

		String resultString = RequestHelper.PostBySingleBitmap(Define_C.mgw_url, params, null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postGetOrderState", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 获取好友信息
	 * 
	 * @throws JSONException
	 * 
	 */
	public static JSONObject getFriendInfo(Context context, String userid) throws JSONException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "user.getuserinfo");
		params.put("userid", userid);
		String resultString = RequestHelper.PostBySingleBitmap(Define_C.mgw_url, params, null);
		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postGetOrderState", resultString);
		JSONTokener jsonParser = new JSONTokener(resultString);

		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 获取好友信息（包含异步）
	 * 
	 * @throws JSONException
	 * 
	 */
	public static UserFriendBean getFriendInfo2Bean(final String userid) throws JSONException {
		UserFriendBean bean = null;

		AsyncTask<String, Void, UserFriendBean> execute = new AsyncTask<String, Void, UserFriendBean>() {

			@Override
			protected UserFriendBean doInBackground(String... params) {
				Gson gson = new Gson();
				Map<String, String> params1 = new HashMap<String, String>();
				params1.put("type", "user.getuserinfo");
				params1.put("userid", userid);
				String resultString = RequestHelper.PostBySingleBitmap(Define_C.mgw_url, params1, null);
				if (resultString == null || resultString.equals("")) {
					return null;
				}
				Log.i("postGetOrderState", resultString);
				JSONTokener jsonParser = new JSONTokener(resultString);

				if (resultString != null) {

					UserFriendBean fromJson = gson.fromJson(resultString, UserFriendBean.class);

					if (fromJson != null && "0".equals(fromJson.getFlag())) {
						return fromJson;
					}

				}

				return null;
			}

		};

		try {
			bean = execute.execute().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	/**
	 * 从网上获取bean（异步）
	 * 
	 * @param username
	 * @return
	 * @throws JSONException
	 */

	public static User getUseFromNetAsync(String username) {

		UserFriendBean friendInfo2Bean;
		try {
			friendInfo2Bean = getFriendInfo2Bean(username);
			if (friendInfo2Bean == null) {
				return null;
			} else {

				if (friendInfo2Bean != null && friendInfo2Bean.getItems() != null && friendInfo2Bean.getItems().size() > 0) {

					User user = new User();
					user.setNick(friendInfo2Bean.getItems().get(0).getNickName());
					user.setUsername(friendInfo2Bean.getItems().get(0).getUserId());
					user.setAvatar(friendInfo2Bean.getItems().get(0).getMemberPic());
					LogUtils.i("UserUtil", "getAvatar= " + user.getAvatar() + ",getUsername=" + user.getUsername() + ",getNick= " + user.getNick());
					return user;
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e(TAG, e.toString());
			return null;
		}

		return null;

	}

	/**
	 * 从网上获取bean（非异步）
	 * 
	 * @param username
	 * @return
	 */
	public static User getUseFromNet(String username) {

		JSONObject postUesrs = null;
		Gson gson = new Gson();
		if (username == null || "".equals(username)) {
			return null;
		}

		try {
			postUesrs = Http.postUesrs(username);

			if (postUesrs != null) {

				UserFriendBean fromJson = gson.fromJson(postUesrs.toString(), UserFriendBean.class);

				if (fromJson != null && fromJson.getItems() != null && fromJson.getItems().size() > 0) {

					User user = new User();

					user.setNick(fromJson.getItems().get(0).getNickName());
					user.setUsername(fromJson.getItems().get(0).getUserId());
					user.setAvatar(fromJson.getItems().get(0).getMemberPic());
					LogUtils.i("UserUtil", "getAvatar= " + user.getAvatar() + ",getUsername=" + user.getUsername() + ",getNick= " + user.getNick());
					return user;
				}

			} else {

				return null;

			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.i("UserUtil", "JSONException= " + e.toString());
			return null;
		}
		return null;
	}

	/**
	 * 通过自己的userid获取自己的好友信息
	 * 
	 * @param userid
	 *            自己的用户名
	 * */
	public static JSONObject getfriendinfo() throws JSONException {

		SharedPreferences sharedata = BaseApplication.getApplication().getSharedPreferences("mgw_data", 0);

		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "member.getfriend");
		params.put("userid", sharedata.getString("mgw_userID", "0"));
		params.put("serial", sharedata.getString("mgw_serial", "0"));

		String resultString = RequestHelper.PostBySingleBitmap(Define_C.mgw_url, params, null);

		if (resultString == null || resultString.equals("")) {
			return null;
		}
		Log.i("postUesrs", resultString);

		JSONTokener jsonParser = new JSONTokener(resultString);
		return (JSONObject) jsonParser.nextValue();
	}

	/**
	 * 通过自己的userid获取自己的好友信息
	 * 
	 * @param userid
	 *            自己的用户名
	 * */
	public static JSONObject getLoginInfo() {

		try {
			Map<String, String> params = new HashMap<String, String>();

			params.put("type", "user.apploading");
			params.put("telephone", "13888888888");
			params.put("pmID", PreferenceHelper.getInstance(UIUtils.getContext()).getLoginCountPassword()[0]);
			params.put("format", "json");
			params.put("pKey", PreferenceHelper.getInstance(UIUtils.getContext()).getLoginCountPassword()[1]);
			params.put("token", "");

			String resultString = RequestHelper.PostBySingleBitmap(Define_C.mgw_url, params, null);
			if (resultString == null || resultString.equals("")) {
				return null;
			}
			JSONTokener jsonParser = new JSONTokener(resultString);
			return (JSONObject) jsonParser.nextValue();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过自己的userid获取自己的好友信息
	 * 
	 * @param userid
	 *            自己的用户名
	 * */
	public static JSONObject getProductInfo(Context c, String pid, String sid) {

		try {
			Map<String, String> params = new HashMap<String, String>();

			params.put("type", "wzreposity.productdetails");

			JSONObject obj = new JSONObject(c.getSharedPreferences("mgw_data", Context.MODE_PRIVATE).getString("mgw_data", ""));

			params.put("userid", obj.getString("UserID"));
			params.put("serial", obj.getString("serial"));
			params.put("sid", sid);
			params.put("pid", pid);
			params.put("posx", c.getSharedPreferences("mgw_data", 0).getString("lng", ""));
			params.put("posy", c.getSharedPreferences("mgw_data", 0).getString("lat", ""));

			String resultString = RequestHelper.PostBySingleBitmap(Define_C.mgw_url, params, null);
			if (resultString == null || resultString.equals("")) {
				return null;
			}
			JSONTokener jsonParser = new JSONTokener(resultString);
			return (JSONObject) jsonParser.nextValue();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 设置comment
	 * 
	 * @param user传进的对象
	 * @param ispingjie是否要拼接
	 *            （true xxx[yyy],false xxx）
	 * @return
	 */
	public static String getCommentOrNick(User user, boolean ispingjie) {

		if (user == null) {
			return "null";

		}
		if (user != null && user.getComment() != null && !"".equals(user.getComment()) && user.getNick() != null) {

			if (ispingjie) {

				return user.getComment() + "[" + user.getNick() + "]";
			} else {
				return user.getComment();

			}
		} else {
			return user.getNick();
		}

	}

	/**
	 * 设置群头像
	 * 
	 * @param iv_avatar1
	 * @param iv_avatar2
	 * @param iv_avatar3
	 * @param iv_avatar4
	 * @param split
	 */
	public static void setGroupsIcon(ImageView iv_avatar1, ImageView iv_avatar2, ImageView iv_avatar3, ImageView iv_avatar4, String[] split) {
		String urr = "wadfasfa";
		switch (split.length) {

		case 1:
			Picasso.with(UIUtils.getContext()).load(split[0]).into(iv_avatar1);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar2);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar3);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar4);
			break;
		case 2:

			Picasso.with(UIUtils.getContext()).load(split[0]).into(iv_avatar1);
			Picasso.with(UIUtils.getContext()).load(split[1]).into(iv_avatar2);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar3);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar4);
			break;
		case 3:
			//
			Picasso.with(UIUtils.getContext()).load(split[0]).into(iv_avatar1);
			Picasso.with(UIUtils.getContext()).load(split[1]).into(iv_avatar2);
			Picasso.with(UIUtils.getContext()).load(split[2]).into(iv_avatar3);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar4);
			break;
		case 4:
			Picasso.with(UIUtils.getContext()).load(split[0]).into(iv_avatar1);
			Picasso.with(UIUtils.getContext()).load(split[1]).into(iv_avatar2);
			Picasso.with(UIUtils.getContext()).load(split[2]).into(iv_avatar3);
			Picasso.with(UIUtils.getContext()).load(split[3]).into(iv_avatar4);
			break;
		default:
			break;
		}

	}

	/**
	 * 设置个人图像
	 * 
	 * @param iv_avatar2
	 * @param avatar6
	 */
	public static void showUserAvatar(ImageView iv_avatar2, String avatar6) {
		Picasso.with(UIUtils.getContext()).load(avatar6).into(iv_avatar2);
	}
	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	public static  String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {

				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息

			digest = getStrng(context, R.string.picture);

			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			System.err.println("error, unknow type");
			return "";
		}

		return digest;
	}

	public static  String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}
	
	
	/**
	 * 转换并存储群头像（图像组合）
	 * @param dd GroupInfo
	 */
	public static void setGroupICon(GroupInfo dd) {
		EMGroup group = EMGroupManager.getInstance().getGroup(dd.getGroupId());
		if(StringUtils.isEmpty(dd.getGroupName()))
			dd.setGroupName(group.getGroupName());
		
		CommonSetGroupIcon(dd, group);
	}

	public static void CommonSetGroupIcon(GroupInfo dd, EMGroup group) {
		if(group==null){
			return;
		}
		List<String> members = group.getMembers();
		switch ( members.size()) {
		
		

		case 1:

			String sd = UserUtils.getUserInfo(members.get(0)) == null ? "" : UserUtils.getUserInfo(members.get(0)).getAvatar();
			dd.setGroupIcon(sd+"#");
			BaseApplication.getApplication().GetGroupsDao().saveGroupInfo(dd);
			break;
		case 2:
			String sd0 = UserUtils.getUserInfo(members.get(0)) == null ? "" : UserUtils.getUserInfo(members.get(0)).getAvatar();
			String sd1 = UserUtils.getUserInfo(members.get(1)) == null ? "" : UserUtils.getUserInfo(members.get(1)).getAvatar();

			dd.setGroupIcon(sd0 + "#" + sd1);
			BaseApplication.getApplication().GetGroupsDao().saveGroupInfo(dd);
			break;
		case 3:
			String sd03 = UserUtils.getUserInfo(members.get(0)) == null ? "" : UserUtils.getUserInfo(members.get(0)).getAvatar();
			String sd13 = UserUtils.getUserInfo(members.get(1)) == null ? "" : UserUtils.getUserInfo(members.get(1)).getAvatar();
			String sd23 = UserUtils.getUserInfo(members.get(2)) == null ? "" : UserUtils.getUserInfo(members.get(2)).getAvatar();
			dd.setGroupIcon(sd03 + "#" + sd13 + "#" + sd23);
			BaseApplication.getApplication().GetGroupsDao().saveGroupInfo(dd);
			break;
		case 4:
			String sd034 = UserUtils.getUserInfo(members.get(0)) == null ? "" : UserUtils.getUserInfo(members.get(0)).getAvatar();
			String sd134 = UserUtils.getUserInfo(members.get(1)) == null ? "" : UserUtils.getUserInfo(members.get(1)).getAvatar();
			String sd234 = UserUtils.getUserInfo(members.get(2)) == null ? "" : UserUtils.getUserInfo(members.get(2)).getAvatar();
			String sd334 = UserUtils.getUserInfo(members.get(3)) == null ? "" : UserUtils.getUserInfo(members.get(3)).getAvatar();
			dd.setGroupIcon(sd034 + "#" + sd134 + "#" + sd234 + "#" + sd334);
			BaseApplication.getApplication().GetGroupsDao().saveGroupInfo(dd);

			break;

		default:
			if( members.size()>4){
				
				String sd0345 = UserUtils.getUserInfo(members.get(0)) == null ? "" : UserUtils.getUserInfo(members.get(0)).getAvatar();
				String sd1345 = UserUtils.getUserInfo(members.get(1)) == null ? "" : UserUtils.getUserInfo(members.get(1)).getAvatar();
				String sd2345 = UserUtils.getUserInfo(members.get(2)) == null ? "" : UserUtils.getUserInfo(members.get(2)).getAvatar();
				String sd3345 = UserUtils.getUserInfo(members.get(3)) == null ? "" : UserUtils.getUserInfo(members.get(3)).getAvatar();
				dd.setGroupIcon(sd0345 + "#" + sd1345 + "#" + sd2345 + "#" + sd3345);
				BaseApplication.getApplication().GetGroupsDao().saveGroupInfo(dd);

				
			}else if(members.size()==0){
				//显示群主的相片
				
			}
			
			
			break;
		}
	}
	
	
}
