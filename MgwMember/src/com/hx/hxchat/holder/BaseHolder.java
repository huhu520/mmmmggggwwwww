package com.hx.hxchat.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.mgw.member.http.image.ImageLoader;


public abstract class BaseHolder<Data> {
	protected View mRootView;
	protected int mPosition;
	protected Data mData;
	protected Context context;
	public BaseHolder() {
		mRootView = initView();
		mRootView.setTag(this);
	}
	public BaseHolder(Context context) {
		this.context=context;
		mRootView = initView();
		mRootView.setTag(this);
	}


	public View getRootView() {
		return mRootView;
	}

	public void setData(Data data) {
		mData = data;
		refreshView();
	}

	public Data getData() {
		return mData;
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public int getPosition() {
		return mPosition;
	}

	public void recycleImageView(ImageView view) {
		Object tag = view.getTag();
		if (tag != null && tag instanceof String) {
			String key = (String) tag;
			ImageLoader.cancel(key);
			// view.setImageDrawable(null);
		}
	}

	/** 子类必须覆盖用于实现UI初始化 */
	protected abstract View initView();

	/** 子类必须覆盖用于实现UI刷新 */
	public abstract void refreshView();

	/** 用于回收 */
	public void recycle() {

	}
}
