package com.hx.hxchat.holder;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.hx.hxchat.adapter.GroupAdaptercopy;
import com.hx.hxchat.domain.GroupInfo;
import com.mgw.member.ui.widget.InterceptorFrame;
import com.mgw.member.uitls.UIUtils;

/**
 * 
 * @author huyan
 * 
 */
public class GroupListInfoHolder extends BaseHolder<List<GroupInfo>> {
	private GroupAdaptercopy groupAdapter;
	private BaseListView groupListView;
	private GroupListItemHeaderHolder mHolder;

	public GroupListInfoHolder(Context context) {
		super(context);
	
	}
	public GroupListInfoHolder() {
		super();
		
	}

	@Override
	protected View initView() {

		groupListView = new BaseListView(UIUtils.getContext());

		mHolder = new GroupListItemHeaderHolder(context,groupListView);
		mHolder.setData("tou");
		groupListView.addHeaderView(mHolder.getRootView());

		if (mHolder != null) {
			InterceptorFrame frame = new InterceptorFrame(UIUtils.getContext());
			frame.addInterceptorView(mHolder.getRootView(), InterceptorFrame.ORIENTATION_LEFT | InterceptorFrame.ORIENTATION_RIGHT);
			frame.addView(groupListView);
			return frame;
		} else {
			return groupListView;
		}

		// return view;
	}

	@Override
	public void refreshView() {
		List<GroupInfo> data = getData();
		groupAdapter = new GroupAdaptercopy(context, 1, data, groupListView);
		groupListView.setAdapter(groupAdapter);
		groupAdapter.notifyDataSetChanged();
	}

}
