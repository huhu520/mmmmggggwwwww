package com.mgw.member.ui.activity.cityleague;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mgw.member.R;
import com.mgw.member.constant.Define_C;

@SuppressLint("SetJavaScriptEnabled")
public class GoodWebViewActivity extends Activity implements OnClickListener {
	private JSONObject mInfo;
	String price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_web_view);

		try {
			mInfo = new JSONObject(getIntent().getExtras().getString("info"));

			// String url = "http://Android4.mgw.cc/wz/productdetail.aspx?sid="
			// + mInfo.getString("sid") + "&pid=" + mInfo.getString("pid");
			String url = Define_C.mgw_url2 + "/wz/productdetail.aspx?sid=" + mInfo.getString("sid") + "&pid=" + mInfo.getString("pid");
			final WebView mWebView = (WebView) findViewById(R.id.webview);
			WebSettings webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			mWebView.setWebViewClient(new MyWebChromeClient());
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
			mWebView.loadUrl(url);

			String mprice = mInfo.getString("mprice");
			String sprice = mInfo.getString("sprice");
			String kprice = mInfo.getString("kprice");
			if (mInfo.getInt("stype") != 2)
				price = kprice;
			else
				price = sprice;

			((TextView) findViewById(R.id.tv_good_detail_nowprice)).setText(price);
			((TextView) findViewById(R.id.tv_good_detail_oldprice)).setText("￥" + mprice);
			((TextView) findViewById(R.id.tv_good_detail_oldprice)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		((TextView) findViewById(R.id.tv_title_cent)).setText("商品详情");
		findViewById(R.id.bt_good_detail_buy).setOnClickListener(this);
		findViewById(R.id.bt_titlebar_left).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == R.id.bt_good_detail_buy) {
			try {
				Intent intent = new Intent(this, OrderSubmitActivity.class);
				intent.putExtra("price", Double.parseDouble(price));
				intent.putExtra("pname", mInfo.getString("pname"));
				intent.putExtra("sid", mInfo.getString("sid"));
				intent.putExtra("pid", mInfo.getString("pid"));
				intent.putExtra("ptype", Integer.parseInt(mInfo.getString("ptype")));
				intent.putExtra("ssafe", Float.valueOf(mInfo.getString("ssafe")));
				startActivity(intent);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		finish();
	}

	class MyWebChromeClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			findViewById(R.id.dialog).setVisibility(View.GONE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			findViewById(R.id.dialog).setVisibility(View.GONE);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			findViewById(R.id.dialog).setVisibility(View.VISIBLE);
		}
	}
}
