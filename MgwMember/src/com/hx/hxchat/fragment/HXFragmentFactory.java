package com.hx.hxchat.fragment;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

import com.hx.hxchat.activity.ChatHistoryFragmentCopy;
import com.hx.hxchat.activity.ContactlistFragment;

@SuppressLint("UseSparseArrays")
public class HXFragmentFactory {

	public static final int TAB_NEWS_CHATHISTORY = 0;
	public static final int TAB_NEWS_FRIENDS = 1;

	/** 记录所有的fragment，防止重复创建 */
	private static Map<Integer, HXBaseFragment> mFragmentMap = new HashMap<Integer, HXBaseFragment>();

	/** 采用工厂类进行创建Fragment，便于扩展，已经创建的Fragment不再创建 */
	public static HXBaseFragment createFragment(int index) {
		HXBaseFragment fragment = mFragmentMap.get(index);
		if (fragment == null) {
			
			switch (index) {
			
			case TAB_NEWS_CHATHISTORY:
				fragment = new ChatHistoryFragmentCopy();
				break;
			case TAB_NEWS_FRIENDS:
				fragment = new ContactlistFragment();
				break;
			}
			mFragmentMap.put(index, fragment);
		}
		return fragment;
	}

	public static void clearFragments() {
		mFragmentMap.clear();
	}
}
