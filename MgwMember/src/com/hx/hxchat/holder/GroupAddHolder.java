package com.hx.hxchat.holder;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.hx.hxchat.adapter.GroupAdaptercopy;
import com.hx.hxchat.domain.GroupInfo;
import com.mgw.member.R;
import com.mgw.member.ui.widget.InterceptorFrame;
import com.mgw.member.uitls.UIUtils;

/**
 * 
 * @author huyan
 * 
 */
public class GroupAddHolder extends BaseHolder<GroupInfo> {
	
	private Context context;

	public GroupAddHolder(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected View initView() {
		View view=UIUtils.inflate(R.layout.row_add_group);
		
		return view;
	}

	@Override
	public void refreshView() {
		
	}

}
