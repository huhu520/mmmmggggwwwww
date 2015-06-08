package com.hx.hxchat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.uitls.UIUtils;

public class PublicGroupsListItemHolder extends BaseHolder<GroupInfo> {

	/**
	 * 群头像1
	 */
	ImageView iv_avatar1;
	/**
	 * 群头像2
	 */
	ImageView iv_avatar2;
	/**
	 * 群头像3
	 */
	ImageView iv_avatar3;
	/**
	 * 群头像4
	 */
	ImageView iv_avatar4;
	/**
	 * 群名字
	 */
	TextView tv_name;
	/**
	 * 群人数
	 */
	TextView tv_number;
	/**
	 * 群简介
	 */
	TextView tv_dec;

	@Override
	protected View initView() {
		// TODO Auto-generated method stub
		
		View view=UIUtils.inflate(R.layout.row_group);
		
		
		tv_name=(TextView) view.findViewById(R.id.tv_name);
		iv_avatar1=(ImageView) view.findViewById(R.id.iv_avatar1);
		iv_avatar2=(ImageView) view.findViewById(R.id.iv_avatar2);
		iv_avatar3=(ImageView) view.findViewById(R.id.iv_avatar3);
		iv_avatar4=(ImageView) view.findViewById(R.id.iv_avatar4);
		tv_number=(TextView) view.findViewById(R.id.tv_number);
		tv_dec=(TextView) view.findViewById(R.id.tv_dec);
		return view;
	}
	
	@Override
	public void refreshView() {
		GroupInfo data = getData();
		
		tv_name.setText(data!=null?data.getGroupName():"null");
		tv_dec.setText(data!=null?data.getDescription():"");
		tv_number.setText(data!=null?data.getMembers().size()+"":"loading");
		if (data != null &&data.getGroupIcon()!=null&& !"".equals(data.getGroupIcon())) {
			String[] split = data.getGroupIcon().split("#");
			UserUtils.setGroupsIcon(iv_avatar1, iv_avatar2, iv_avatar3, iv_avatar4, split);
		}
		
		
		
	}

}
