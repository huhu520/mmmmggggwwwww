package com.mgw.member.ui.fragment;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebViewClient公共部分
 * @author huyan
 *
 */
public class BaseWebViewClient extends WebViewClient {

	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {
		// TODO Auto-generated method stub
		super.onScaleChanged(view, oldScale, newScale);
		view.requestFocus();
		view.requestFocusFromTouch();
		
	}



//	@Override
//	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//		String data = "Page NO FOUND！";
//		// // String data1=
//		// //
//		// "<img src='http://183.203.16.186:443/advertisementtouch/201402/96b9dd22-9580-4fda-bd8c-d7a7a41d1eea.jpg' height='100%' width='100%'>";
//		// String data1 =
//		// "<p align='center'><img src='../images/bomb.png' height='20%' width='20%'></p>";
//		// String path = "file://" + FileUtils.getGuideImageDir() +
//		// "guide_image1.png/>";
//		view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
//		super.onReceivedError(view, errorCode, description, failingUrl);
//	}
	
}
