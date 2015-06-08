package com.mgw.member.ui.activity.cityleague;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mgw.member.R;
import com.mgw.member.ui.widget.TouchImageView;
import com.mgw.member.uitls.ImageLoaderHelper;

public class SingleImageShowActivity extends Activity implements
		OnClickListener {
	// ------------闪图-----------------
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private final List<ImageView> imageViews = new ArrayList<ImageView>(); // 滑动的图片集合
	private FrameLayout frameLayout_image_show;
	private List<String> mImage = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_show);
		frameLayout_image_show = (FrameLayout) findViewById(R.id.image_show_framlayout);
		mImage = getIntent().getStringArrayListExtra("image");
		frameLayout_image_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		init();
	}

	private void init() {
		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setOnPageChangeListener(new MyPageChangeListener());

		int count = mImage.size();
		for (int i = 0; i < count; i++) {
			TouchImageView imageView = new TouchImageView(this);
			ImageLoaderHelper.displayImage(R.drawable.img_loading, imageView,
					mImage.get(i));
			imageView.setOnClickListener(this);
			imageView.setMaxZoom(4f);
			imageViews.add(imageView);
		}

		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		viewPager.setCurrentItem(getIntent().getExtras().getInt("index"));
	}

	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	private class MyAdapter extends PagerAdapter {
		public MyAdapter() {
		}

		@Override
		public int getCount() {
			// return imageResId.length;
			return imageViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
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
	public void onClick(View arg0) {

		this.finish();
	}
}
