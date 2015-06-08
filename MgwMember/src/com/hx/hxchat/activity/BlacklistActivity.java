package com.hx.hxchat.activity;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.hx.hxchat.domain.User;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.LoadUserAvatar;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.uitls.FileUtils;

/**
 * 黑名单列表页面
 * 
 */
public class BlacklistActivity extends Activity {
	private ListView listView;
	private BlacklistAdapater adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_list);

		listView = (ListView) findViewById(R.id.list);

		// 从本地获取黑名单
		 List<String> blacklist = EMContactManager.getInstance().getBlackListUsernames();

		 
		 
		 
		 
		// 显示黑名单列表
		if (blacklist != null) {
			Collections.sort(blacklist);
			adapter = new BlacklistAdapater(this, 1, blacklist);
			listView.setAdapter(adapter);
		}

		// 注册上下文菜单
		registerForContextMenu(listView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.remove_from_blacklist, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.remove) {
			final String tobeRemoveUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 把目标user移出黑名单
			removeOutBlacklist(tobeRemoveUser);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 移出黑民单
	 * 
	 * @param tobeRemoveUser
	 */
	void removeOutBlacklist(final String tobeRemoveUser) {
		try {
			// 移出黑民单
			EMContactManager.getInstance().deleteUserFromBlackList(tobeRemoveUser);
			adapter.remove(tobeRemoveUser);
		} catch (EaseMobException e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				public void run() {
					String str2 = getResources().getString(R.string.Removed_from_the_failure);
					Toast.makeText(getApplicationContext(), str2, 0).show();
				}
			});
		}
	}

	/**
	 * adapter
	 * 
	 */
	private class BlacklistAdapater extends ArrayAdapter<String> {

		private LoadUserAvatar loadUserAvatar;
		public BlacklistAdapater(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
			loadUserAvatar=new LoadUserAvatar(context, FileUtils.getCacheDir());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.row_contact, null);
			}

			TextView name = (TextView) convertView.findViewById(R.id.name);
			ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
			
			User userInfo = UserUtils.getUserInfo(getItem(position));
			name.setText(userInfo!=null?userInfo.getNick():getItem(position));

			CommonUtils.showUserAvatar(avatar, userInfo!=null?userInfo.getAvatar():"ade", loadUserAvatar);
			return convertView;
		}

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}