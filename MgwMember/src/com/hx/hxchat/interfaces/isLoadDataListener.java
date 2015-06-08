package com.hx.hxchat.interfaces;

/**
 * 异步加载监听接口
 * @author huyan
 *
 */
public interface isLoadDataListener {
	/**
	 * 执行完成
	 * 
	 */
		public void loadComplete();
		/**
		 * 开始执行
		 * 
		 */
		public void loadStart();
		
		/**
		 *  执行进度
		 * @param progress
		 */
		public void progress(int progress);
	}