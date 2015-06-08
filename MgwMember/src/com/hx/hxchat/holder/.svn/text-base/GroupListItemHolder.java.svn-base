package com.hx.hxchat.holder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx.hxchat.activity.GroupsActivity;
import com.hx.hxchat.domain.GroupInfo;
import com.hx.hxchat.utils.LoadUserAvatar;
import com.mgw.member.R;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.UIUtils;
import com.squareup.picasso.Picasso;

/**
 * 视图展示（不负责数据加载）
 * 
 * @author huyan
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class GroupListItemHolder extends BaseHolder<GroupInfo> {

	public GroupListItemHolder() {
		super();
//		avatarLoader = new LoadUserAvatar(UIUtils.getContext(), FileUtils.getCacheDir());
	}

	@Override
	public void recycle() {

	}

	private TextView tv_name;
	private ImageView iv_avatar1;
	private ImageView iv_avatar2;
	private ImageView iv_avatar3;
	private ImageView iv_avatar4;
	private RelativeLayout re_avatar;
	private LoadUserAvatar avatarLoader;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.item_chatroom_4);
		re_avatar = (RelativeLayout) view.findViewById(R.id.re_avatar);

		iv_avatar1 = (ImageView) view.findViewById(R.id.iv_avatar1);
		iv_avatar2 = (ImageView) view.findViewById(R.id.iv_avatar2);
		iv_avatar3 = (ImageView) view.findViewById(R.id.iv_avatar3);
		iv_avatar4 = (ImageView) view.findViewById(R.id.iv_avatar4);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		return view;
	}

	@Override
	public void setData(GroupInfo data) {
		super.setData(data);
	}

	@Override
	public void refreshView() {
		GroupInfo info = getData();
		tv_name.setText(info.getGroupName());

		if (info != null && info.getGroupIcon() != null) {
			String[] split = info.getGroupIcon().split("#");
			setIcon(iv_avatar1, iv_avatar2, iv_avatar3, iv_avatar4, split);
		}

		LogUtils.i(GroupsActivity.TAG, "groupname:" + info.getGroupName() + ",icon=" + info.getGroupIcon());

	}

	private void setIcon(ImageView iv_avatar1, ImageView iv_avatar2, ImageView iv_avatar3, ImageView iv_avatar4, String[] split) {
		String urr = "wadfasfa";
		switch (split.length) {

		case 1:

			Picasso.with(UIUtils.getContext()).load(split[0]).into(iv_avatar1);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar2);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar3);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar4);

			break;
		case 2:

			Picasso.with(UIUtils.getContext()).load(split[0]).into(iv_avatar1);
			Picasso.with(UIUtils.getContext()).load(split[1]).into(iv_avatar2);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar3);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar4);

			break;
		case 3:
			//
			Picasso.with(UIUtils.getContext()).load(split[0]).into(iv_avatar1);
			Picasso.with(UIUtils.getContext()).load(split[1]).into(iv_avatar2);
			Picasso.with(UIUtils.getContext()).load(split[2]).into(iv_avatar3);
			Picasso.with(UIUtils.getContext()).load(urr).placeholder(R.drawable.default_useravatar).into(iv_avatar4);
			break;
		case 4:
			Picasso.with(UIUtils.getContext()).load(split[0]).into(iv_avatar1);
			Picasso.with(UIUtils.getContext()).load(split[1]).into(iv_avatar2);
			Picasso.with(UIUtils.getContext()).load(split[2]).into(iv_avatar3);
			Picasso.with(UIUtils.getContext()).load(split[3]).into(iv_avatar4);

			break;

		default:
			break;
		}

	}

}
