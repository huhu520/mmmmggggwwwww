package com.mgw.member.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mgw.member.ui.activity.login.LoginActivity;
import com.mgw.member.ui.widget.ViewPagerCompat;
import com.mgw.member.ui.widget.ViewPagerCompat.OnPageChangeListener;
import com.mgw.member.ui.widget.transformer.RotateDownPageTransformer;
import com.mgw.member.uitls.FileUtils;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.R;
import com.squareup.picasso.Picasso;

/**
 * 引导界面
 * 
 * @author huyan Create on 2015-3-10下午8:42:18 description：引导页（webview填充）
 */
public class SplashActivity extends MGWBaseActivity {

	private ViewPagerCompat viewPager;// 自定义viewpager兼容
	private List<View> mImageViews; // 滑动的图片集合
	// private int[] imageResId; // 图片ID
	// 引导图片的SD卡路径
	private String[] imageResPath;
	private int currentItem = 0; // 当前图片的索引号
	private GestureDetector gestureDetector; // 用户滑动
	/** 记录当前分页ID */
	private int flaggingWidth;// 互动翻页所需滚动的长度是当前屏幕宽度的1/3

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		// gestureDetector = new GestureDetector(new GuideViewTouch());

		// 获取分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		flaggingWidth = dm.widthPixels / 4;
		imageResPath = new String[] { FileUtils.getGuideImageDir()+"guide_image1.png", FileUtils.getGuideImageDir()+"guide_image2.png", FileUtils.getGuideImageDir()+"guide_image3.png",FileUtils.getGuideImageDir()+"guide_image4.png" };
		mImageViews = new ArrayList<View>();
		initViewData();
	}

	@SuppressLint("NewApi")
	private void initViewData() {

		for (int i = 0; i < imageResPath.length; i++) {
			View mView = UIUtils.inflate(R.layout.item_guide);
			RelativeLayout mLinearLayout = (RelativeLayout) mView.findViewById(R.id.guide_item);
			ImageView imageView = (ImageView) mView.findViewById(R.id.img_guide);
			Picasso.with(mContext).load("file://"+imageResPath[i]).into(imageView);
			//imageView.setBackground(Drawable.createFromPath(imageResPath[i]));
			mView.findViewById(R.id.tv_guide_jump).setOnClickListener(new MyOnclickListener());
			
			// 最后一张显示按钮
			if (i == imageResPath.length - 1) {
				
				Button btn = (Button) mView.findViewById(R.id.start);
				btn.setVisibility(View.VISIBLE);
				btn.setOnClickListener(new MyOnclickListener());
				mImageViews.add(mLinearLayout);
			} else {
				
				
				mImageViews.add(mLinearLayout);
			}
			
			for(String imageResPathd:imageResPath){
				LogUtils.i(imageResPathd);
				
			}
		}
		
		viewPager = (ViewPagerCompat) findViewById(R.id.guide_view);
		viewPager.setPageTransformer(true, new RotateDownPageTransformer());
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	private class MyOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			GoToLoginActivity();
		}
		
	}
	
	/**
	 * 进入主界面
	 */
	void GoToLoginActivity() {
		Intent i = new Intent(SplashActivity.this, LoginActivity.class);
		startActivity(i);
		finish();
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageResPath.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPagerCompat) arg0).addView(mImageViews.get(arg1));
			return mImageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerCompat) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	 // TODO Auto-generated method stub
	 if (keyCode == KeyEvent.KEYCODE_BACK) {
	 //TODO 注释
	  GoToLoginActivity();
	 return false;
	 }
	 return super.onKeyDown(keyCode, event);
	 }
	//
	// @Override
	// public boolean dispatchTouchEvent(MotionEvent event) {
	// if (gestureDetector.onTouchEvent(event)) {
	// event.setAction(MotionEvent.ACTION_CANCEL);
	// }
	// return super.dispatchTouchEvent(event);
	// }
	//
	// private class GuideViewTouch extends SimpleOnGestureListener {
	// @Override
	// public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	// float velocityY) {
	// if (currentItem == 1) {
	// if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY()) &&
	// (e1.getX() - e2.getX() <= (-flaggingWidth) || e1.getX() - e2.getX() >=
	// flaggingWidth)) {
	// if (e1.getX() - e2.getX() >= flaggingWidth) {
	// //TODO 注释
	// //GoToLoginActivity();
	//
	// return true;
	// }
	// }
	// }
	// return false;
	// }
	// }

}
