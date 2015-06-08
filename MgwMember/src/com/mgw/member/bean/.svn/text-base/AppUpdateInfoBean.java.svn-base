package com.mgw.member.bean;

import java.io.Serializable;
import java.util.List;

import com.hx.hxchat.domain.UserFriendBean.Items;

/**
 * 更新信息bean
 * 
 * @author huyan
 * 
 */
// @TableName(DBHelper.TABLE_USER)
public class AppUpdateInfoBean implements Serializable {

	/**
	 * 0代表成功，1代表没有跟新 其他的代表失败
	 */
	public String flag;
	/** 类型 请求类型 更新：app.get */
	public String type;
	/** 随便填 */
	public String telephone;
	/** 类回执 */
	public String msg;
	/** 个人信息 */

	public List<Items> items;

	public static class Items {

		/** TSWWV-150424134600000000 无用 */
		public String FVersion_ID;
		/** 2.0.0 */
		public String FVersion_Name;
		/** 下载地址 */
		public String FVersion_FileURL;

		/** "全新体验，全新结构"回执 */
		public String FVersion_Explain;

		/** 2015-04-24 13:46:00 */
		public String FVersion_Create;

		/** 1 apk更新 */
		public String IsReplace;

	}
}
