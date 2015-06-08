package com.hx.hxchat.holder.protocol;

import java.util.ArrayList;
import java.util.List;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.hx.hxchat.activity.GroupsActivity;
import com.hx.hxchat.activity.PublicGroupsActivity;
import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.manager.ThreadManager;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.StringUtils;

/**
 * 数据加载器 （只负责数据加载，逻辑和视图无关）
 * 
 * @author Administrator
 * 
 */
public class PublicGroupsListProtocol extends BaseProtocol<List<GroupInfo>> {
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
		List<EMGroup> grouplist = new ArrayList<EMGroup>();
		grouplist.clear();
		try {
			// 从服务器获取所用公开的群聊
			final List<EMGroupInfo> publicgroupsList = EMGroupManager.getInstance().getAllPublicGroupsFromServer();

			for (EMGroupInfo dd : publicgroupsList) {

				LogUtils.i(PublicGroupsActivity.TAG, "groupsList:id" + dd.getGroupId());
				EMGroup groupFromServer;

				groupFromServer = EMGroupManager.getInstance().getGroupFromServer(dd.getGroupId());

				grouplist.add(groupFromServer);

			}
		} catch (EaseMobException e) {
			e.printStackTrace();
			LogUtils.e(PublicGroupsActivity.TAG, e);
			return null;
		}

		final List<GroupInfo> GroupInfos = new ArrayList<GroupInfo>();
		GroupInfos.clear();

		assignObject(grouplist, GroupInfos);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (GroupInfo dd : GroupInfos) {
					LogUtils.i(GroupsActivity.TAG, "GroupInfo members=" + dd.getMembers().toString() + ",GroupInfoname=" + dd.getGroupName() + ",GroupInfoicon=" + dd.getGroupIcon());
					if (dd.getGroupIcon() == null && !dd.getGroupId().equals("new")) {
						EMGroup groupFromServer;
						try {
							groupFromServer = EMGroupManager.getInstance().getGroupFromServer(dd.getGroupId());
							UserUtils.CommonSetGroupIcon(dd, groupFromServer);
							
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

	private void assignObject(List<EMGroup> grouplist, List<GroupInfo> groupInfos) {

		for (EMGroup dd : grouplist) {
			GroupInfo groupInfo = new GroupInfo(dd.getGroupId());
			groupInfo.setAffiliationsCount(dd.getAffiliationsCount());
			groupInfo.setDescription(dd.getDescription());
			groupInfo.setEid(dd.getEid());
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
			groupInfos.add(groupInfo);
		}

	}

}
