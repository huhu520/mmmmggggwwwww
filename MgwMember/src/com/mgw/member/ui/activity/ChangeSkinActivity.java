package com.mgw.member.ui.activity;

/**create by hyb
 * modify by hy
 * */
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.hxchat.activity.AddContactActivity;
import com.hx.hxchat.activity.AlertDialog;
import com.hx.hxchat.adapter.AddcontactAdapter.ViewHold;
import com.hx.hxchat.domain.UserFriendBean.Items;
import com.hx.hxchat.utils.CommonUtils;
import com.hx.hxchat.utils.UserUtils;
import com.mgw.member.R;
import com.mgw.member.bean.SkinInfo;
import com.mgw.member.manager.BaseApplication;
import com.mgw.member.uitls.ZipUtil;

public class ChangeSkinActivity extends MGWBaseActivity {

	private ListView listview;
	private ChangeSkinAdapter changeSkinAdapter;

	private List<SkinInfo> mSkinInfos;
	String skinBasePath = "/data/data/com.mgw.member/skin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		setContentView(R.layout.activity_change_skin);
		mSkinInfos = new ArrayList<SkinInfo>();

		{

			SkinInfo info0 = new SkinInfo(0, "默认", "s", "/sdcard/mgw/skin.zip","1");
			SkinInfo info1 = new SkinInfo(1, "绿色海洋", "skin.zip", "/sdcard/mgw/unziphtml/skins/skin_green.zip", "1");
			SkinInfo info2 = new SkinInfo(2, "红红火火", "skin_red.zip", "/sdcard/mgw//unziphtml/skins/skin_red.zip", "1");
			SkinInfo info3 = new SkinInfo(3, "高贵冷紫", "skin_purple.zip", "/sdcard/mgw/skin.zip", "0");
			SkinInfo info4 = new SkinInfo(4, "白雪纷飞", "skin_snow.zip", "/sdcard/mgw/skin.zip", "0");
			mSkinInfos.add(info0);
			mSkinInfos.add(info1);
			mSkinInfos.add(info2);
			mSkinInfos.add(info3);
			mSkinInfos.add(info4);

		}

		listview = (ListView) findViewById(R.id.listview);
		changeSkinAdapter = new ChangeSkinAdapter(ChangeSkinActivity.this, mSkinInfos);
		listview.setAdapter(changeSkinAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});

	}

	private void setSkin(final SkinInfo sinfo) {

		if (sinfo != null) {

			new AsyncTask<Void, Void, String>() {

				@Override
				protected String doInBackground(Void... params) {
					SharedPreferences preferences = getSharedPreferences("skin", 0);
					Editor edit = preferences.edit();
					
					String result="";
					if ((!"默认".equals(sinfo.getSkinName()))) {
						
						if(!"1".equals(sinfo.getIsLocalExist())){
							
							result="需要联网下载！";
							
						}else{
							//TODO ant.jar暂时删除
//							edit.putBoolean("useSkin", true);
//							ZipUtil zipp = new ZipUtil(2049);
//							zipp.unZip(sinfo.getSkinDownloadUrl(), skinBasePath);
//							edit.commit();
//							result=sinfo.getSkinName() + ":导入成功";
						}
					

					} else {
						edit.putBoolean("useSkin", false);
						edit.commit();
						result=sinfo.getSkinName() + ":导入成功";

					}

					return result ;
				}

				protected void onPostExecute(String result) {

					Toast.makeText(ChangeSkinActivity.this, result, Toast.LENGTH_SHORT).show();
				};
			}.execute();

		}

	}

	public class ChangeSkinAdapter extends BaseAdapter {
		/** 上下文 */
		private final Context context;
		/** item 列表 */
		ChangeSkinActivity changeSkinActivity;
		private final List<SkinInfo> data;
		public Dialog progressDialog;
		public LayoutInflater inflater;

		public ChangeSkinAdapter(Context context, List<SkinInfo> data) {
			this.context = context;
			this.changeSkinActivity = (ChangeSkinActivity) context;
			this.data = data;
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
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_changeskin, parent, false);
			}
			if (hold == null) {
				hold = new ViewHold();
				hold.name = (TextView) convertView.findViewById(R.id.name);
				hold.avater = (ImageView) convertView.findViewById(R.id.avatar);
				hold.btn_add = (Button) convertView.findViewById(R.id.indicator);
				hold.tv_local = (TextView) convertView.findViewById(R.id.tv_local);
				convertView.setTag(hold);
			} else {
				hold = ((ViewHold) convertView.getTag());
			}

			final SkinInfo sinfo = data.get(position);
			if (sinfo != null) {
				hold.name.setText(sinfo.getSkinName());
				// UserUtils.setUserAvatar(context,
				// Uri.parse(sinfo.getMemberPic()), hold.avater);
				hold.tv_local.setText((sinfo.getIsLocalExist() != null && "1".equals(sinfo.getIsLocalExist())) ? "" : "需下载");
				hold.tv_local.setTextColor(Color.GREEN);

				hold.btn_add.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						// 使用

						setSkin(sinfo);

					}
				});
			}

			return convertView;
		}

		public class ViewHold {
			public TextView name;
			public TextView tv_local;
			public ImageView avater;
			public Button btn_add;
		}
	}
}
