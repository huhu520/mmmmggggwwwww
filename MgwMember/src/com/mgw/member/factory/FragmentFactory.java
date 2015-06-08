package com.mgw.member.factory;



import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

import com.hx.hxchat.fragment.NewsFragmentCopy;
import com.mgw.member.ui.fragment.BaseFragment;
import com.mgw.member.ui.fragment.FindFragment;
import com.mgw.member.ui.fragment.HomeFragment;
import com.mgw.member.ui.fragment.MallFragment;
import com.mgw.member.ui.fragment.MyInfoFragment;
import com.mgw.member.ui.fragment.NewsFragment;


@SuppressLint("UseSparseArrays")
public class FragmentFactory {
	public static final int TAB_HOME = 1;
	public static final int TAB_NEWS = 2;
	public static final int TAB_FIND = 3;
	public static final int TAB_MALL = 4;
	public static final int TAB_MYINFO = 5;
	public static final int TAB_ = 6;
	public static final int TAB_HOME_FRIENDS  = 7;
	
	
	/** 记录所有的fragment，防止重复创建 */
	private static Map<Integer, BaseFragment> mFragmentMap = new HashMap<Integer, BaseFragment>();

	/** 采用工厂类进行创建Fragment，便于扩展，已经创建的Fragment不再创建 */
	public static BaseFragment createFragment(int index) {
		BaseFragment fragment = mFragmentMap.get(index);
		if (fragment == null) {
			switch (index) {
				case TAB_HOME:
					fragment = new HomeFragment();
					break;
				case TAB_NEWS:
					fragment = new NewsFragmentCopy();
					break;
				case TAB_FIND:
					fragment = new FindFragment();
					break;
				case TAB_MALL:
					fragment = new MallFragment();
					break;
				case TAB_MYINFO:
					fragment = new MyInfoFragment();
					break;
//				case TAB_HOME_MSG:
//					fragment = new MyInfoFragment();
//					break;
//				case TAB_HOME_FRIENDS:
//					fragment = new MyInfoFragment();
//					break;
//					
			}
			mFragmentMap.put(index, fragment);
		}
		return fragment;
	}
	
	
	public static void clearFragments(){
		
		mFragmentMap.clear();
	}
}
