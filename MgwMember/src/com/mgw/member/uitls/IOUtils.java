package com.mgw.member.uitls;

import java.io.Closeable;
import java.io.IOException;
/**
 * IO流操作类
 * @author huyan
 */
public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
