package com.mgw.member.ui.activity.cityleague;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.mgw.member.R;

public abstract class BaseActivity2 extends Activity implements OnClickListener {
	private PopupWindow popupWindow;
	String m_page_url = "";
	String m_page_title = "";
	byte[] m_byteArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void initTitleButton() {
		findViewById(R.id.bt_titlebar_left).setOnClickListener(this);
	}

	// public void initShowShare() {
	// WebChromeClient wvcc = new WebChromeClient() {
	// @Override
	// public void onReceivedTitle(WebView view, String title) {
	// super.onReceivedTitle(view, title);
	// ((TextView) findViewById(R.id.title)).setText(title);
	// m_page_title = title;
	// }
	// };
	// }

	// public void showPpuwindowShare(View ll) {
	// dissmissPopuwindow();
	// LayoutInflater inflater = LayoutInflater.from(this);
	// View view = inflater.inflate(R.layout.popu_share, null);
	// TextView share_friend = (TextView) view.findViewById(R.id.share_friend);
	// TextView share_friend_all = (TextView) view
	// .findViewById(R.id.share_friend_all);
	// share_friend_all.setOnClickListener(this);
	// share_friend.setOnClickListener(this);
	//
	// popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
	// LayoutParams.WRAP_CONTENT);
	// popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	// popupWindow.setOutsideTouchable(true);
	// popupWindow.showAsDropDown(ll, 0, 20);
	// }

	// private void dissmissPopuwindow() {
	// if (popupWindow != null && popupWindow.isShowing()) {
	// popupWindow.dismiss();
	// }
	// }

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void initPayKind() {
		((ImageView) findViewById(R.id.iv_order_pay_zfb)).setImageResource(R.drawable.ic_checkbox_photo_unselected);
		((ImageView) findViewById(R.id.iv_order_pay_yhk)).setImageResource(R.drawable.ic_checkbox_photo_unselected);
		((ImageView) findViewById(R.id.iv_order_pay_wx)).setImageResource(R.drawable.ic_checkbox_photo_selected);
		findViewById(R.id.ll_order_pay_zfb).setOnClickListener(this);
		findViewById(R.id.ll_order_pay_yhk).setOnClickListener(this);
		findViewById(R.id.ll_order_pay_wx).setOnClickListener(this);
		findViewById(R.id.ll_order_pay_more).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_order_pay_zfb:
			((ImageView) findViewById(R.id.iv_order_pay_zfb)).setImageResource(R.drawable.ic_checkbox_photo_selected);
			((ImageView) findViewById(R.id.iv_order_pay_yhk)).setImageResource(R.drawable.ic_checkbox_photo_unselected);
			((ImageView) findViewById(R.id.iv_order_pay_wx)).setImageResource(R.drawable.ic_checkbox_photo_unselected);
			break;

		case R.id.ll_order_pay_yhk:
			((ImageView) findViewById(R.id.iv_order_pay_yhk)).setImageResource(R.drawable.ic_checkbox_photo_selected);
			((ImageView) findViewById(R.id.iv_order_pay_zfb)).setImageResource(R.drawable.ic_checkbox_photo_unselected);
			((ImageView) findViewById(R.id.iv_order_pay_wx)).setImageResource(R.drawable.ic_checkbox_photo_unselected);
			break;
		case R.id.ll_order_pay_wx:
			((ImageView) findViewById(R.id.iv_order_pay_wx)).setImageResource(R.drawable.ic_checkbox_photo_selected);
			((ImageView) findViewById(R.id.iv_order_pay_yhk)).setImageResource(R.drawable.ic_checkbox_photo_unselected);
			((ImageView) findViewById(R.id.iv_order_pay_zfb)).setImageResource(R.drawable.ic_checkbox_photo_unselected);
			break;
		case R.id.ll_order_pay_more:
			findViewById(R.id.ll_order_pay_more).setVisibility(View.GONE);
			findViewById(R.id.ll_order_pay_yhk).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_order_pay_wx).setVisibility(View.VISIBLE);
			break;
		case R.id.bt_titlebar_left:
			finish();
			overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
			break;
		// case R.id.tv_titlebar_right:
		// showPpuwindowShare(v);
		//
		// break;
		// case R.id.share_friend:
		// shareFriend();
		// break;
		//
		// case R.id.share_friend_all:
		// break;
		}
	}

	// private void shareFriend() {
	// WXWebpageObject webpage = new WXWebpageObject();
	// webpage.webpageUrl = m_page_url;
	// WXMediaMessage msg = new WXMediaMessage(webpage);
	// if (m_page_title != null && m_page_title.length() > 0)
	// msg.title = m_page_title;
	// else
	// msg.title = "我分享啦";
	// msg.description = "好东西哦";
	// if (m_byteArray != null)
	// msg.thumbData = m_byteArray;
	// else {
	// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
	// R.drawable.icon_wx);
	// msg.thumbData = Util.bmpToByteArray(thumb, true);
	// }
	//
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = buildTransaction("webpage");
	// req.message = msg;
	// req.scene = SendMessageToWX.Req.WXSceneSession;
	// //((GlobelElements) getApplicationContext()).api.sendReq(req);
	// }

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	public void dealCredicableView(View ll, int score) {
		ImageView v1 = (ImageView) ll.findViewById(R.id.iv_credicable_mg1);
		ImageView v2 = (ImageView) ll.findViewById(R.id.iv_credicable_mg2);
		ImageView v3 = (ImageView) ll.findViewById(R.id.iv_credicable_mg3);
		ImageView v4 = (ImageView) ll.findViewById(R.id.iv_credicable_mg4);
		ImageView v5 = (ImageView) ll.findViewById(R.id.iv_credicable_mg5);

		switch (score) {
		case 0:
			v1.setImageResource(R.drawable.ic_star);
			v2.setImageResource(R.drawable.ic_star);
			v3.setImageResource(R.drawable.ic_star);
			v4.setImageResource(R.drawable.ic_star);
			v5.setImageResource(R.drawable.ic_star);

			break;
		case 1:
			v2.setImageResource(R.drawable.ic_star);
			v3.setImageResource(R.drawable.ic_star);
			v4.setImageResource(R.drawable.ic_star);
			v5.setImageResource(R.drawable.ic_star);

			break;
		case 2:
			v3.setImageResource(R.drawable.ic_star);
			v4.setImageResource(R.drawable.ic_star);
			v5.setImageResource(R.drawable.ic_star);

			break;
		case 3:
			v4.setImageResource(R.drawable.ic_star);
			v5.setImageResource(R.drawable.ic_star);

			break;
		case 4:
			v5.setImageResource(R.drawable.ic_star);

			break;
		case 5:

			break;
		}
	}
}
