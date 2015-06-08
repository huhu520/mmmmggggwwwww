package com.hx.hxchat.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hx.hxchat.activity.ChatHistoryFragmentCopy;
import com.mgw.member.ui.fragment.BaseFragment;
import com.mgw.member.ui.widget.LoadingPage;
import com.mgw.member.ui.widget.LoadingPage.LoadResult;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.UIUtils;
import com.mgw.member.uitls.ViewUtils;

public abstract class HXBaseFragment extends Fragment {
	protected static final String TAG = HXBaseFragment.class.getSimpleName();
	protected LoadingPage mContentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//每次ViewPager要展示该页面时，均会调用该方法获取显示的View
		if (mContentView == null) {//为null时，创建一个
			mContentView = new LoadingPage(UIUtils.getContext()) {
				@Override
				public LoadResult load() {
					LogUtils.i(TAG, "load");
					return HXBaseFragment.this.load();
				}

				@Override
				public View createLoadedView() {
					LogUtils.i(TAG, "createLoadedView");
					return HXBaseFragment.this.createLoadedView();
				}
				@Override
				protected View createEmptyView() {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "createEmptyView");
					return super.createEmptyView();
				}
				@Override
				protected View createErrorView() {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "createErrorView");
					return super.createErrorView();
				}
				@Override
				protected View createLoadingView() {
					// TODO Auto-generated method stub
					LogUtils.i(TAG, "createLoadingView");
					return super.createLoadingView();
				}
			
			};
		} else {//不为null时，需要把自身从父布局中移除，因为ViewPager会再次添加
			ViewUtils.removeSelfFromParent(mContentView);
		}
		
		return mContentView;
	}


	/** 当显示的时候，加载该页面 */
	public void show() {
		if (mContentView != null) {
			mContentView.show();
			LogUtils.i(ChatHistoryFragmentCopy.TAG, "show");
		}
	}
	public void reset() {
		if (mContentView != null) {
			mContentView.reset();
		}
	}

	public LoadResult check(Object obj) {
		if (obj == null) {
			return LoadResult.ERROR;
		}
		if (obj instanceof List) {
			List list = (List) obj;
			if (list.size() == 0) {
				return LoadResult.SUCCEED;
			}
		}
		return LoadResult.SUCCEED;
	}

	/** 加载数据 */
	protected abstract LoadResult load();

	/** 加载完成的View */
	protected abstract View createLoadedView();
	
	
	
	
}
