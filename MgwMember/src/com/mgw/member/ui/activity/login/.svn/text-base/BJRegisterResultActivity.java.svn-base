package com.mgw.member.ui.activity.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mgw.member.R;

public class BJRegisterResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bjregister_result);

		((TextView) findViewById(R.id.tv_title_cent)).setText("激活成功");

		((TextView) findViewById(R.id.info0)).setText(getIntent().getExtras().getString("no"));
		((TextView) findViewById(R.id.info1)).setText("卡号: " + getIntent().getExtras().getString("card") + ",激活成功");

		findViewById(R.id.login).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}