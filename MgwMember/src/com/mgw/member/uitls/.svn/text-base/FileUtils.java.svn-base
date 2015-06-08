package com.mgw.member.uitls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 文件工具类
 */
public class FileUtils {
	/** 引导页图片的解压路径 */
	public static final String GuidimagePath = Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/unziphtml/guideimage/";
	/**
	 * gen目录
	 */
	public static final String ROOT_DIR = "mgw";
	/**
	 * 下载目录
	 */
	public static final String DOWNLOAD_DIR = "download";
	/**
	 * 缓存目录
	 */
	public static final String CACHE_DIR = "cache";
	/**
	 * icon目录
	 */
	public static final String ICON_DIR = "icon";
	/**
	 * image目录
	 */
	public static final String IMAGE_DIR = "guideimage";


	/** 判断SD卡是否挂载 */
	public static boolean isSDCardAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	/** 获取下载目录 */
	public static String getDownloadDir() {
		return getDir(DOWNLOAD_DIR);
	}

	/** 获取缓存目录 */
	public static String getCacheDir() {
		return getDir(CACHE_DIR);
	}

	/** 获取icon目录 */
	public static String getIconDir() {
		return getDir(ICON_DIR);
	}

	/** 获取guideimage目录 */
	public static String getGuideImageDir() {
		return GuidimagePath;
	}
	
	/** 获取guideimage目录 */
	public static String getHomeHtmlDir() {
		return getExternalStoragePath()+"unziphtml/html/";
	}

	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public static String getDir(String name) {
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath());
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}

	/** 获取SD下的应用目录 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的cache目录 */
	public static String getCachePath() {
		File f = UIUtils.getContext().getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 创建文件夹 */
	public static boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}

	/** 复制文件，可以选择是否删除源文件 */
	public static boolean copyFile(String srcPath, String destPath, boolean deleteSrc) {
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		return copyFile(srcFile, destFile, deleteSrc);
	}

	/** 复制文件，可以选择是否删除源文件 */
	public static boolean copyFile(File srcFile, File destFile, boolean deleteSrc) {
		if (!srcFile.exists() || !srcFile.isFile()) {
			return false;
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = in.read(buffer)) > 0) {
				out.write(buffer, 0, i);
				out.flush();
			}
			if (deleteSrc) {
				srcFile.delete();
			}
		} catch (Exception e) {
			LogUtils.e(e);
			return false;
		} finally {
			IOUtils.close(out);
			IOUtils.close(in);
		}
		return true;
	}

	/** 判断文件是否可写 */
	public static boolean isWriteable(String path) {
		try {
			if (StringUtils.isEmpty(path)) {
				return false;
			}
			File f = new File(path);
			return f.exists() && f.canWrite();
		} catch (Exception e) {
			LogUtils.e(e);
			return false;
		}
	}

	/** 修改文件的权限,例如"777"等 */
	public static void chmod(String path, String mode) {
		try {
			String command = "chmod " + mode + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (Exception e) {
			LogUtils.e(e);
		}
	}

	/**
	 * 把数据写入文件
	 * 
	 * @param is
	 *            数据流
	 * @param path
	 *            文件路径
	 * @param recreate
	 *            如果文件存在，是否需要删除重建
	 * @return 是否写入成功
	 */
	public static boolean writeFile(InputStream is, String path, boolean recreate) {
		boolean res = false;
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			if (recreate && f.exists()) {
				f.delete();
			}
			if (!f.exists() && null != is) {
				File parentFile = new File(f.getParent());
				parentFile.mkdirs();
				int count = -1;
				byte[] buffer = new byte[1024];
				fos = new FileOutputStream(f);
				while ((count = is.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
				}
				res = true;
			}
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(fos);
			IOUtils.close(is);
		}
		return res;
	}

	/**
	 * 把字符串数据写入文件
	 * 
	 * @param content
	 *            需要写入的字符串
	 * @param path
	 *            文件路径名称
	 * @param append
	 *            是否以添加的模式写入
	 * @return 是否写入成功
	 */
	public static boolean writeFile(byte[] content, String path, boolean append) {
		boolean res = false;
		File f = new File(path);
		RandomAccessFile raf = null;
		try {
			if (f.exists()) {
				if (!append) {
					f.delete();
					f.createNewFile();
				}
			} else {
				f.createNewFile();
			}
			if (f.canWrite()) {
				raf = new RandomAccessFile(f, "rw");
				raf.seek(raf.length());
				raf.write(content);
				res = true;
			}
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(raf);
		}
		return res;
	}

	/**
	 * 把字符串数据写入文件
	 * 
	 * @param content
	 *            需要写入的字符串
	 * @param path
	 *            文件路径名称
	 * @param append
	 *            是否以添加的模式写入
	 * @return 是否写入成功
	 */
	public static boolean writeFile(String content, String path, boolean append) {
		return writeFile(content.getBytes(), path, append);
	}

	/**
	 * 把键值对写入文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param comment
	 *            该键值对的注释
	 */
	public static void writeProperties(String filePath, String key, String value, String comment) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(filePath)) {
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);// 先读取文件，再把键值对追加到后面
			p.setProperty(key, value);
			fos = new FileOutputStream(f);
			p.store(fos, comment);
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(fis);
			IOUtils.close(fos);
		}
	}

	/** 根据值读取键值对 */
	public static String readProperties(String filePath, String key, String defaultValue) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(filePath)) {
			return null;
		}
		String value = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			value = p.getProperty(key, defaultValue);
		} catch (IOException e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(fis);
		}
		return value;
	}
	/** 根据值读取键值对 
	 * @throws JSONException */
	public static String readText(String filePath, String key, String defaultValue) throws JSONException {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(filePath)) {
			return null;
		}
		 String value = "";   
		InputStreamReader fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new InputStreamReader(new FileInputStream(f), "utf-8");  
		
			 BufferedReader br = new BufferedReader(fis);    
	           
	          String mimeTypeLine = null ;  
	          while ((mimeTypeLine = br.readLine()) != null) {  
	        	  value = value+mimeTypeLine;  
	        }   
			
		} catch (IOException e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(fis);
		}
		if(!value.equals("")){
			JSONObject jsonObject = new JSONObject(value);
		
			JSONObject jsonObject2 = jsonObject.getJSONObject(key);
			return jsonObject2.toString();
		}else{
			return defaultValue;
			
		}
		
		
	}
	public static String JSONTokener(String in) {  
        // consume an optional byte order mark (BOM) if it exists  
        if (in != null && in.startsWith("\ufeff")) {  
        in = in.substring(1);  
        }  
        return in;  
   }
	/** 把字符串键值对的map写入文件 */
	public static void writeMap(String filePath, Map<String, String> map, boolean append, String comment) {
		if (map == null || map.size() == 0 || StringUtils.isEmpty(filePath)) {
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			Properties p = new Properties();
			if (append) {
				fis = new FileInputStream(f);
				p.load(fis);// 先读取文件，再把键值对追加到后面
			}
			p.putAll(map);
			fos = new FileOutputStream(f);
			p.store(fos, comment);
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(fis);
			IOUtils.close(fos);
		}
	}

	/** 把字符串键值对的文件读入map */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, String> readMap(String filePath, String defaultValue) {
		if (StringUtils.isEmpty(filePath)) {
			return null;
		}
		Map<String, String> map = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			map = new HashMap<String, String>((Map) p);// 因为properties继承了map，所以直接通过p来构造一个map
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(fis);
		}
		return map;
	}

	/** 改名 */
	public static boolean copy(String src, String des, boolean delete) {
		File file = new File(src);
		if (!file.exists()) {
			return false;
		}
		File desFile = new File(des);
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new FileOutputStream(desFile);
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
				out.flush();
			}
		} catch (Exception e) {
			LogUtils.e(e);
			return false;
		} finally {
			IOUtils.close(in);
			IOUtils.close(out);
		}
		if (delete) {
			file.delete();
		}
		return true;
	}

	/**
	 * 将Assets中的文件复制到Sd卡中
	 * 
	 * @param c
	 * @param assetDir
	 * @param dir
	 *            Created by Administrator
	 */
	public static void CopyAssets(Context c, String assetDir, String dir) {

		String[] files;
		try {
			files = c.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		if (!isSDCardAvailable())
			return;

		File mWorkingPath = new File(dir);
		// if this directory does not exists, make one.
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {
				Log.e("--CopyAssets--", "cannot create directory.");
			}
		}
		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				// we make sure file name not contains '.' to be a folder.
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						CopyAssets(c, fileName, dir + fileName + "/");
					} else {
						CopyAssets(c, assetDir + "/" + fileName, dir + fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = c.getAssets().open(assetDir + "/" + fileName);
				else
					in = c.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 * @return Created by huyan
	 */
	public static boolean dirIsExists(String path) {

		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}
	/**
     * 递归删除 文件/文件夹
     * 
     * @param file
     */ 
    public static void deleteFile(File file) { 
   
        LogUtils.i("delete file path=" + file.getAbsolutePath()); 
           
        if (file.exists()) { 
            if (file.isFile()) { 
                file.delete(); 
            } else if (file.isDirectory()) { 
                File files[] = file.listFiles(); 
                for (int i = 0; i < files.length; i++) { 
                    deleteFile(files[i]); 
                } 
            } 
            file.delete(); 
        } else { 
        	LogUtils.e("delete file no exists " + file.getAbsolutePath()); 
        } 
    } 
    
    
}
