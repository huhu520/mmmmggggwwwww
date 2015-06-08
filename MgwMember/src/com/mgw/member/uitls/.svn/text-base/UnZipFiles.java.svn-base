package com.mgw.member.uitls;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.util.Log;

public class UnZipFiles {
	/**
	 * 解压缩功能. 将zipFile文件解压到folderPath目录下.
	 * 
	 * @throws Exception
	 */
	public int upZipFile(File zipFile, String folderPath) throws ZipException,
			IOException {
		// public static void upZipFile() throws Exception{
		ZipFile zfile = new ZipFile(zipFile);
		@SuppressWarnings("rawtypes")
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				Log.d("upZipFile", "ze.getName() = " + ze.getName());
				String dirstr = folderPath + ze.getName();
				// dirstr.trim();
				dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
				Log.d("upZipFile", "str = " + dirstr);
				File f = new File(dirstr);
				f.mkdir();
				continue;
			}
			Log.d("upZipFile", "ze.getName() = " + ze.getName());

			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(folderPath, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
		Log.d("upZipFile", "finishssssssssssssssssssss");
		return 0;
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = new File(baseDir);
		String substr = null;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				substr = dirs[i];
				try {
					// substr.trim();
					substr = new String(substr.getBytes("8859_1"), "GB2312");

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = new File(ret, substr);

			}
			Log.d("upZipFile", "1ret = " + ret);
			if (!ret.exists())
				ret.mkdirs();
			substr = dirs[dirs.length - 1];
			try {
				// substr.trim();
				substr = new String(substr.getBytes("8859_1"), "GB2312");
				Log.d("upZipFile", "substr = " + substr);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ret = new File(ret, substr);
			Log.d("upZipFile", "2ret = " + ret);
			return ret;
		} else {
			ret = new File(ret, absFileName);
		}
		return ret;
	}

	/** 解压已输入流形式存在的zip文件 */
	public void upZipinputstreamFile(InputStream zipinputStream,
			String folderPath) throws ZipException, IOException {
		File dest = new File(folderPath);
		if (!dest.isDirectory())
			throw new IOException("Invalid Unzip destination " + dest);

		if (null == zipinputStream) {
			throw new IOException("InputStream is null");
		}

		ZipInputStream zip = new ZipInputStream(zipinputStream);

		ZipEntry ze;

		while ((ze = zip.getNextEntry()) != null) {
			final String path = dest.getAbsolutePath() + File.separator
					+ ze.getName();

			// Create any entry folders
			String zeName = ze.getName();
			char cTail = zeName.charAt(zeName.length() - 1);
			if (cTail == File.separatorChar) {
				File file = new File(path);
				if (!file.exists()) {
					if (!file.mkdirs()) {
						throw new IOException("Unable to create folder " + file);
					}
				}
				continue;
			}

			FileOutputStream fout = new FileOutputStream(path);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = zip.read(bytes)) != -1) {
				fout.write(bytes, 0, c);
			}

			zip.closeEntry();
			fout.close();
		}

	}

	public void deepFile(Context ctxDealFile, String path) {
		try {
			String str[] = ctxDealFile.getAssets().list(path);
			if (str.length > 0) {// 如果是目录
				File file = new File("/data/" + path);
				file.mkdirs();
				for (String string : str) {
					path = path + "/" + string;
					System.out.println("zhoulc:\t" + path);
					// textView.setText(textView.getText()+"\t"+path+"\t");
					deepFile(ctxDealFile, path);
					path = path.substring(0, path.lastIndexOf('/'));
				}
			} else {// 如果是文件
				InputStream is = ctxDealFile.getAssets().open(path);
				FileOutputStream fos = new FileOutputStream(new File("/data/"
						+ path));
				byte[] buffer = new byte[1024];
				int count = 0;
				while (true) {
					count++;
					int len = is.read(buffer);
					if (len == -1) {
						break;
					}
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
