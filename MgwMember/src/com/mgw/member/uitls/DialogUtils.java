package com.mgw.member.uitls;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgw.member.R;

public class DialogUtils {
	private Animation hyperspaceJumpAnimation;
	private ImageView spaceshipImage;
	private Dialog loadingDialog;
	private Dialog dialog;
	private Context context;
	private static DialogUtils instance;

	public DialogUtils(Context context) {
		super();
		this.context = context;
		// hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context,
		// R.anim.loading_animation);
	}

	/**
	 * 单一实例
	 */
	public static DialogUtils getDialogUtils(Context context) {
		if (instance == null) {
			instance = new DialogUtils(context);
		}
		return instance;
	}

	// /**
	// * 得到自定义的progressDialog
	// *
	// * @param context
	// * @param msg
	// * @return
	// */
	// public Dialog createLoadingDialog(String msg,Context context) {
	//
	// View v = UIUtils.inflate(R.layout.loading_dialog);// 得到加载view
	// LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);//
	// 加载布局
	// // main.xml中的ImageView
	// spaceshipImage = (ImageView) v.findViewById(R.id.img);
	// TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);//
	// 提示文字
	// tipTextView.setText(msg);// 设置加载信息
	// loadingDialog = new Dialog(context, R.style.loading_dialog);//
	// 创建自定义样式dialog
	// // loadingDialog.setCancelable(false);// 不可以用“返回键”取消
	// loadingDialog.setCanceledOnTouchOutside(false);
	// loadingDialog.setContentView(layout, new
	// LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
	// LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
	// return loadingDialog;
	//
	// }

	// public void show() {
	// if (loadingDialog != null && spaceshipImage != null &&
	// (!loadingDialog.isShowing())) {
	// loadingDialog.show();
	// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	// }
	// }
	//
	// public void dismiss() {
	// if (loadingDialog != null && spaceshipImage != null) {
	// loadingDialog.dismiss();
	// spaceshipImage.clearAnimation();
	// context = null;
	// }
	//
	// }
	// public void showLoadingDialog() {
	// if(dialog==null){
	// dialog = new Dialog(context,R.style.MyDialogStyle);
	// dialog.setContentView(R.layout.dialog);
	// dialog.show();
	// return;
	// }
	// if(dialog!=null&&!dialog.isShowing()){
	// dialog.show();
	// }
	// }
	//
	// public void dismissLoadingDialog() {
	// if(dialog!=null&&dialog.isShowing()){
	// dialog.dismiss();
	// }
	// }
}
