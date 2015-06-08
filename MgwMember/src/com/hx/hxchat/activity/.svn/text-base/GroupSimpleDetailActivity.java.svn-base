/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hx.hxchat.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.LoadUserAvatar;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.R.color;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;

public class GroupSimpleDetailActivity extends HXBaseActivity {
	private static final String TAG = null;
	private Button btn_add_group;
	private TextView tv_admin;
	private TextView tv_name;
	private TextView tv_introduction;
	private ImageView avatar;
	private ImageView iv_avatar1;
	private ImageView iv_avatar2;
	private ImageView iv_avatar3;
	private ImageView iv_avatar4;
	private RelativeLayout rl_group_avs;
	private EMGroup group;
	private String groupid;
	private ProgressBar progressBar;
	private LoadUserAvatar loadUserAvatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_simle_details);
		loadUserAvatar = new LoadUserAvatar(GroupSimpleDetailActivity.this, FileUtils.getCacheDir());
		tv_name = (TextView) findViewById(R.id.name);
		tv_admin = (TextView) findViewById(R.id.tv_admin);
		rl_group_avs = (RelativeLayout) findViewById(R.id.rl_group_avs);
		avatar = (ImageView) findViewById(R.id.avatar);
		iv_avatar1 = (ImageView) findViewById(R.id.iv_avatar1);
		iv_avatar2 = (ImageView) findViewById(R.id.iv_avatar2);
		iv_avatar3 = (ImageView) findViewById(R.id.iv_avatar3);
		iv_avatar4 = (ImageView) findViewById(R.id.iv_avatar4);
		btn_add_group = (Button) findViewById(R.id.btn_add_to_group);
		tv_introduction = (TextView) findViewById(R.id.tv_introduction);
		progressBar = (ProgressBar) findViewById(R.id.loading);

		EMGroupInfo groupInfo = (EMGroupInfo) getIntent().getSerializableExtra("groupinfo");
		String groupname = groupInfo.getGroupName();
		groupid = groupInfo.getGroupId();
		
		tv_name.setText(groupname);
		
		new Thread(new Runnable() {

			public void run() {
				//从服务器获取详情
				try {
					group = EMGroupManager.getInstance().getGroupFromServer(groupid);
					runOnUiThread(new Runnable() {
						public void run() {
							
							List<String> members = group.getMembers();
							
							setGroupMembersAvs(members, iv_avatar1,iv_avatar2,iv_avatar3,iv_avatar4);
							progressBar.setVisibility(View.INVISIBLE);
							//获取详情成功，并且自己不在群中，才让加入群聊按钮可点击
							if(!group.getMembers().contains(EMChatManager.getInstance().getCurrentUser())){
								btn_add_group.setEnabled(true);
								tv_name.setText(group.getGroupName());
								tv_admin.setText(group.getOwner());
								tv_introduction.setText(group.getDescription());
							}else{
								//
								btn_add_group.setEnabled(false);
								tv_name.setText(group.getGroupName());
								
								User userInfo = UserUtils.getUserInfo(group.getOwner());
								tv_admin.setText(userInfo==null?group.getOwner():userInfo.getNick());
								tv_introduction.setText(group.getDescription());
								
								btn_add_group.setBackgroundColor(color.gray_normal);
								
								if(!group.getOwner().equals(BaseApplication.getApplication().m_user_id)){
									btn_add_group.setText("已在群中");
									
								}else{
									btn_add_group.setText("群主");
								}
									
									
									
							}
							
							
							
						}
					});
				} catch (final EaseMobException e) {
					e.printStackTrace();
					final String st1 = getResources().getString(R.string.Failed_to_get_group_chat_information);
					runOnUiThread(new Runnable() {
						public void run() {
							progressBar.setVisibility(View.INVISIBLE);
							Toast.makeText(GroupSimpleDetailActivity.this, st1+e.getMessage(), 1).show();
						}
					});
				}
				
			}
		}).start();
		
	}
	private void setGroupMembersAvs(List<String> members, ImageView iv_avatar1, ImageView iv_avatar2, ImageView iv_avatar3, ImageView iv_avatar4) {
		if(members==null||members.size()==0){
			return;
		}
		
		String[] avatars= new String[members.size()];
		avatars = getGroupAvs(members, avatars);
		
		
		switch (members.size()) {
		case 1:
			showUserAvatar(iv_avatar1, avatars[0]);
			break;
		case 2:
			showUserAvatar(iv_avatar1, avatars[0]);
			
			showUserAvatar(iv_avatar2, avatars[1]);
			break;
		case 3:
			
			showUserAvatar(iv_avatar1, avatars[0]);
			showUserAvatar(iv_avatar2, avatars[1]);
			showUserAvatar(iv_avatar3, avatars[2]);
			break;
		case 4:
			
			showUserAvatar(iv_avatar1, avatars[0]);
			showUserAvatar(iv_avatar2, avatars[1]);
			showUserAvatar(iv_avatar3, avatars[2]);
			showUserAvatar(iv_avatar4, avatars[3]);
			
			
			break;

		default:
			showUserAvatar(iv_avatar1, avatars[0]);
			showUserAvatar(iv_avatar2, avatars[1]);
			showUserAvatar(iv_avatar3, avatars[2]);
			showUserAvatar(iv_avatar4, avatars[3]);
			
			
			
			break;
		}
		
		
	}
	
	private void showUserAvatar(ImageView iv_avatar12, String string) {
		
		CommonUtils.showUserAvatar(iv_avatar12, string,loadUserAvatar );
		
	}
	private String[] getGroupAvs(final List<String> members, String[] avatars) {
		LogUtils.i(TAG, "members" + members.toString() + ",groupName_temp=");
		//
		for (int i = 0; i < members.size(); i++) {

			String uid = members.get(i);
			if (i < 5) {
				
				User useFromNetAsync = UserUtils.getUserInfo(uid);
//				User useFromNetAsync=null;
				if (useFromNetAsync != null) {
					
					avatars[i] = useFromNetAsync.getAvatar()!=null? useFromNetAsync.getAvatar():"";
				}
				LogUtils.i(TAG, "avatars=" + avatars[i] + ",uid=" + uid);
			}
		}


		return avatars;

	}
	//加入群聊
	public void addToGroup(View view){
		String st1 = getResources().getString(R.string.Is_sending_a_request);
		final String st2 = getResources().getString(R.string.Request_to_join);
		final String st3 = getResources().getString(R.string.send_the_request_is);
		final String st4 = getResources().getString(R.string.Join_the_group_chat);
		final String st5 = getResources().getString(R.string.Failed_to_join_the_group_chat);
		final ProgressDialog pd = new ProgressDialog(this);
//		getResources().getString(R.string)
		pd.setMessage(st1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					//如果是membersOnly的群，需要申请加入，不能直接join
					if(group.isMembersOnly()){
						EMGroupManager.getInstance().applyJoinToGroup(groupid, st2);
					}else{
						EMGroupManager.getInstance().joinGroup(groupid);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							if(group.isMembersOnly())
								Toast.makeText(GroupSimpleDetailActivity.this, st3, 0).show();
							else
								Toast.makeText(GroupSimpleDetailActivity.this, st4, 0).show();
							btn_add_group.setEnabled(false);
						}
					});
				} catch (final EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(GroupSimpleDetailActivity.this, st5+e.getMessage(), 0).show();
						}
					});
				}
			}
		}).start();
	}
	
	public void back(View view){
		finish();
	}
}
