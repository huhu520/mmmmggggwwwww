package com.hx.hxchat.holder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hx.hxchat.activity.GroupsActivity;
import com.hx.hxchat.adapter.GroupAdaptercopy;
import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.utils.StringUtils;
import com.mgw.member.R;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.UIUtils;

public class GroupListItemHeaderHolder extends BaseHolder<String> implements OnClickListener {

	private TextView textView;
	private Context context;
	private BaseListView groupListView;
	private EditText query;
	private ImageButton search_clear;
	private ImageButton imgb_search;

	public GroupListItemHeaderHolder(Context context, BaseListView groupListView) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.groupListView = groupListView;
	}

	@Override
	protected View initView() {
		View v = UIUtils.inflate(R.layout.search_bar_with_padding);
		query = (EditText) v.findViewById(R.id.query);
		search_clear = (ImageButton) v.findViewById(R.id.search_clear);
		imgb_search = (ImageButton) v.findViewById(R.id.imgb_search);
		search_clear.setOnClickListener(this);
		imgb_search.setOnClickListener(this);
		query.addTextChangedListener(new watcher());
		return v;
	}

	@Override
	public void refreshView() {
		
		
			
			search_clear.setVisibility(StringUtils.isEmpty(query.getText().toString())?View.GONE:View.VISIBLE);
		
		

	}
	private long sencodeTextChangedTime;
	private long firstTextChangedTime;
	private String beforeText;
	private String crrentText;
	private GroupAdaptercopy groupAdapter;
	
	private Handler handler=new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			
			switch (msg.what) {
			case 001:
			
				
				
				
				
				break;

			default:
				break;
			}
			
			
			
		};
		
	};
	class watcher implements TextWatcher {

		

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			firstTextChangedTime = SystemClock.currentThreadTimeMillis();
			refreshView();
			
			
			if("".equals(s.toString())){
				refreshinfiet("");
			}
			LogUtils.i(GroupsActivity.TAG, "TextWatcher,afterTextChanged s=" + s + ",Editable=" + s);
		}

	

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
			LogUtils.i(GroupsActivity.TAG, "TextWatcher,beforeTextChanged s=" + s + ",count=" + count);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
			LogUtils.i(GroupsActivity.TAG, "TextWatcher,onTextChanged s=" + s + ",count=" + count);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_clear:
			query.setText("");
			refreshinfiet("");
			break;
		case R.id.imgb_search:
			if(query.getText().toString()==null){
			return;
			}
			refreshinfiet(query.getText().toString());
			break;

		default:
			break;
		}
		
	}
	private void refreshinfiet(String aa) {
		
		
		Pattern p = Pattern.compile(aa);
		List<GroupInfo> mDatas = ((GroupsActivity) context).mDatas;
		List<GroupInfo> mDatasMatcher = new ArrayList<GroupInfo>();
		mDatasMatcher.clear();
		for (GroupInfo dd : mDatas) {
			if(dd==null||dd.getGroupName()==null||dd.getGroupId()==null)
				continue;
			Matcher matcher = p.matcher(dd.getGroupName()+dd.getGroupId());
			if (matcher.find()) {
				mDatasMatcher.add(dd);
			}
			groupAdapter = new GroupAdaptercopy(context, 1, mDatasMatcher, groupListView);
			groupListView.setAdapter(groupAdapter);
//			groupAdapter.notifyDataSetChanged();
			query.requestFocus();
		}
		
		
	}
}
