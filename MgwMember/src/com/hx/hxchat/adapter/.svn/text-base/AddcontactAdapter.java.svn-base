package com.hx.hxchat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hx.hxchat.activity.AddContactActivity;
import com.hx.hxchat.activity.AlertDialog;
import com.hx.hxchat.domain.UserFriendBean.Items;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.uitls.UIUtils;


public class AddcontactAdapter extends BaseAdapter {
	/** 上下文 */
	private final Context context;
	/** item 列表 */
	private final List<Items> data;
	public Dialog progressDialog;
	private AddContactActivity addContactActivity;
	public LayoutInflater inflater;

	public AddcontactAdapter(Context context, List<Items> data) {
		this.context = context;
		addContactActivity = (AddContactActivity) context;
		this.data = new ArrayList<Items>(data);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return 0;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHold hold = null;
		if (convertView == null){
			convertView = inflater.inflate(R.layout.item_addcontact,parent,false);
		}
		if (hold == null) {
			hold = new ViewHold();
			hold.name = (TextView) convertView.findViewById(R.id.name);
			hold.avater = (ImageView) convertView.findViewById(R.id.avatar);
			hold.btn_add = (Button) convertView.findViewById(R.id.indicator);
			convertView.setTag(hold);
		} else {
			hold = ((ViewHold) convertView.getTag());
		}

		final Items user = data.get(position);
		if (user != null) {
			hold.name.setText(user.getNickName());
			UserUtils.setUserAvatar(context, Uri.parse(user.getMemberPic()), hold.avater);

			hold.btn_add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					if (BaseApplication.getApplication().getUserName().equals(user.getFUser_ID())) {
						context.startActivity(new Intent(context, AlertDialog.class).putExtra("msg", "不能添加自己"));
						return;
					}

					if (BaseApplication.getApplication().getContactList().containsKey(user.getFUser_ID())) {
						context.startActivity(new Intent(context, AlertDialog.class).putExtra("msg", "此用户已是你的好友"));
						return;
					}

					progressDialog = CommonUtils.getUserDefinedDialog(context, "正在发生请求", false, true);
					progressDialog.show();

					addContactActivity.addFriend(user);

				}
			});
		}

		return convertView;
	}

	public class ViewHold {
		public TextView name;
		public ImageView avater;
		public Button btn_add;
	}

}
