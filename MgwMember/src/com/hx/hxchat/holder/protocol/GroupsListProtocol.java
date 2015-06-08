package com.hx.hxchat.holder.protocol;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.hx.hxchat.activity.GroupsActivity;
import com.hx.hxchat.db.GroupsDao;
import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.manager.ThreadManager;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.StringUtils;
import com.mgw.member.uitls.UIUtils;

/**
 * 数据加载器
 * （只负责数据加载，逻辑和视图无关）
 * @author Administrator
 *
 */
public class GroupsListProtocol extends BaseProtocol<List<GroupInfo>> {
	private String mPackageName = "";

	public void setPackageName(String packageName) {
		mPackageName = packageName;
	}

	@Override
	protected String getKey() {
		return "detail";
	}

	@Override
	protected String getParames() {
		if (StringUtils.isEmpty(mPackageName)) {
			return super.getParames();
		} else {
			return "&packageName=" + mPackageName;
		}
	}

	@Override
	protected List<GroupInfo> parseFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GroupInfo> load(int index) {
		List<EMGroup> grouplist = EMGroupManager.getInstance().getAllGroups();
		final List<GroupInfo> GroupInfos = new ArrayList<GroupInfo>();
		GroupInfos.clear();
		
		GroupInfos.add(new GroupInfo("new"));
		for (EMGroup dd : grouplist) {
			GroupInfo groupInfo = new GroupInfo(dd.getGroupId());
			groupInfo.setAffiliationsCount(dd.getAffiliationsCount());
			groupInfo.setDescription(dd.getDescription());
			groupInfo.setEid(dd.getEid());
			// groupInfo.setEmGroup(dd.get));
			groupInfo.setGroupId(dd.getGroupId());
			groupInfo.setGroupName(dd.getGroupName());
			groupInfo.setIsPublic(dd.isPublic());
			groupInfo.setLastModifiedTime(dd.getLastModifiedTime());
			groupInfo.setMaxUsers(dd.getMaxUsers());
			groupInfo.setMembers(dd.getMembers());
			groupInfo.setMsgBlocked(dd.getMsgBlocked());
			groupInfo.setNick(dd.getNick());
			groupInfo.setOwner(dd.getOwner());
			groupInfo.setPublic(dd.isPublic());
			groupInfo.setUsername(dd.getUsername());
			String gropsIcon = BaseApplication.getApplication().GetGroupsDao().getGropsIcon(groupInfo.getGroupId());
			groupInfo.setGroupIcon(gropsIcon == null ? null : gropsIcon);
			GroupInfos.add(groupInfo);
		}
		
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (GroupInfo dd : GroupInfos) {
					LogUtils.i(GroupsActivity.TAG, "GroupInfo members="+dd.getMembers().toString()+",GroupInfoname="+dd.getGroupName()+",GroupInfoicon="+dd.getGroupIcon());
					if (dd.getGroupIcon() == null&&!dd.getGroupId().equals("new")) {
						EMGroup groupFromServer;
						try {
							groupFromServer = EMGroupManager.getInstance().getGroupFromServer(dd.getGroupId());
							EMGroupManager.getInstance().createOrUpdateLocalGroup(groupFromServer);

						} catch (EaseMobException e) {
							
							e.printStackTrace();
						}
						UserUtils.setGroupICon(dd);

					}

				}

				// notice refresh
			}

	
		};

		ThreadManager.getLongPool().execute(runnable);
		
		
		return GroupInfos;

	}

}
