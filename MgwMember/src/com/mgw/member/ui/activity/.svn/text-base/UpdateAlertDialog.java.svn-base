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
package com.mgw.member.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts.Intents.UI;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mgw.member.R;
import com.mgw.member.manager.UpdateManager;
import com.mgw.member.ottoEvent.AppDownloadProgressEvent;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ottoEvent.MallFragmentNeedRefreshEvent;
import com.mgw.member.ui.widget.RoundProgressBar;
import com.mgw.member.uitls.AppManager;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.PreferenceHelper;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.Utils;
import com.mgw.member.updateApp.MyIntentService;
import com.squareup.otto.Subscribe;

public class UpdateAlertDialog extends BaseActivity implements OnClickListener {

	/**
	 * 更新内容
	 */
	private TextView tv_md_FVersion_Explain;
	// 下载状态
	private TextView tv_updata_vn;
	// 下载图标
	private ImageView iv_updata_download;

	private TextView tv_install;
	Button mDownload1;

	ProgressBar mProgress1;

	TextView mProgress1Txt;
	Context mContext;

	// 下载版本
	private String fVersion_Name;
	// 下载更新说明
	private String fv_exp;
	// 下载地址
	private String fVersion_FileURL;

	private RoundProgressBar RoundProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mdialog);
		mContext = this;
		initView();

	}

	private void initView() {
		tv_md_FVersion_Explain = (TextView) findViewById(R.id.tv_md_FVersion_Explain);
		tv_updata_vn = (TextView) findViewById(R.id.tv_updata_vn);
		tv_install = (TextView) findViewById(R.id.tv_install);
		iv_updata_download = (ImageView) findViewById(R.id.iv_updata_download);
		RoundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
		fVersion_Name = getIntent().getStringExtra("FVersion_Name");
		fv_exp = getIntent().getStringExtra("fv_exp");
		fVersion_FileURL = getIntent().getStringExtra("FVersion_FileURL");
		iv_updata_download.setOnClickListener(this);
		tv_install.setOnClickListener(this);
		tv_md_FVersion_Explain.setText(fv_exp);
		tv_updata_vn.setText("最新版本："+fVersion_Name);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BusProvider.getInstance().register(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		BusProvider.getInstance().unregister(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_updata_download:
			// mdialog.dismiss();
			// tv_updata_vn.setText("app下载中请稍后！");
			iv_updata_download.setVisibility(View.GONE);
			RoundProgressBar.setVisibility(View.VISIBLE);
			Intent intent = new Intent(mContext, MyIntentService.class);
			intent.putExtra("FVersion_FileURL", fVersion_FileURL);
			startService(intent);
			break;
		case R.id.tv_install:
			
		Utils.installApk(UpdateManager.saveFileName, mContext, null, 0);
			
			
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Subscribe
	public void AppDownloadProgressEvent(AppDownloadProgressEvent progress) {
		if (progress.getDownloadstatus() == 1) {
			LogUtils.i("otto", "正在下载   progress" + progress.getProgress());
			RoundProgressBar.setProgress(progress.getProgress());
		} else if (progress.getDownloadstatus() == 2) {

			UIUtils.showToastSafe("下载成功，准备安装");
			RoundProgressBar.setTextIsDisplayable(false);
			tv_install.setVisibility(View.VISIBLE);
			LogUtils.i("otto", "progress下载成功");
		} else if (progress.getDownloadstatus() == 3) {
			UIUtils.showToastSafe("下载失败!");
			LogUtils.i("otto", "progress下载失败");
		}
	}

	
	private long mExitTime;

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if ((System.currentTimeMillis() - mExitTime) > 2000) {
//				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//				mExitTime = System.currentTimeMillis();
//			} else {
//				// 防止退出后 再次点击 直接进入主页
//				PreferenceHelper.getInstance(mContext).setAppLogined(false);
//				AppManager.getAppManager().AppExit(mContext);
//			}
			 moveTaskToBack(true);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
