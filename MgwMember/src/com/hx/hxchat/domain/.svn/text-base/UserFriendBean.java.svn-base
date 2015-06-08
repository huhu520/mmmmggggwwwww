package com.hx.hxchat.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 用于封装搜索时返回的 用户集
 * 
 * @author huyan
 * 
 */
public class UserFriendBean implements Serializable {

	public String flag;
	public String type;
	public String telephone;
	public String msg;
	public List<Items> items;

	public static class Items {

		public Items(String userId, String nickName, String memberPic) {
			super();
			UserId = userId;
			NickName = nickName;
			MemberPic = memberPic;
		}

		public Items() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Items(String userId, String nickName) {
			super();
			UserId = userId;
			NickName = nickName;

		}

		public String FUser_ID;
		public String UserId;
		public String NickName;
		public String MemberPic;

		public String getUserId() {
			return UserId;
		}

		public void setUserId(String userId) {
			UserId = userId;
		}

		public String getFUser_ID() {
			return FUser_ID;
		}

		public void setFUser_ID(String fUser_ID) {
			FUser_ID = fUser_ID;
		}

		public String getNickName() {
			return NickName;
		}

		public void setNickName(String nickName) {
			NickName = nickName;
		}

		public String getMemberPic() {
			return MemberPic;
		}

		public void setMemberPic(String memberPic) {
			MemberPic = memberPic;
		}

		@Override
		public String toString() {
			return "Items [FUser_ID=" + FUser_ID + ", NickName=" + NickName + ", MemberPic=" + MemberPic + "]";
		}

	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "UserFriendBean [flag=" + flag + ", type=" + type + ", telephone=" + telephone + ", msg=" + msg + ", items=" + items + "]";
	}

}
