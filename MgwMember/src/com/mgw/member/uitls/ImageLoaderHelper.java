package com.mgw.member.uitls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageLoaderHelper {
	private static String TAG = "ImageLoaderHelper";
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	public static void displayImage(int defaultResourceId, ImageView view, String url) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(defaultResourceId)
				.showImageForEmptyUri(defaultResourceId)
				.showImageOnFail(defaultResourceId)
				.cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage(url, view, options, animateFirstListener);
	}
	
	public static void loadImage(int defaultResourceId, String url, SimpleImageLoadingListener listener) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
//		DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.loadImage(url, options, listener);
	}
	
	public static void clearDataCache() {
		imageLoader.clearDiscCache();
	}
	
	public static interface IGetSavedFile {
		public void whileGotFile(String filePath);
	}
	
	public static void getFileFromUrl(String url, Context ctx, IGetSavedFile gotFile)
	{
		File f = imageLoader.getDiscCache().get(url);
		if (f != null && f.exists()) 
		{
			gotFile.whileGotFile(f.getAbsolutePath());
		} 
		else 
		{
			new BitmapWorkerTask(ctx, gotFile).execute(url);
		}
	}
	
	private static class BitmapWorkerTask extends AsyncTask<String, String, String> {
		private Context ctx;
		private IGetSavedFile gotFile;
		
		public BitmapWorkerTask(Context context, IGetSavedFile gotFile) {
			ctx = context;
			this.gotFile = gotFile;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String urlString = params[0];
				URL url = new URL(params[0]);
                HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect(); 
                InputStream inputStream = conn.getInputStream();
                File f = getFile(urlString, ctx);
                saveFile(inputStream, f);
                return f.getAbsolutePath();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return "";
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}

		@Override
		protected void onPostExecute(String fileName) {
			gotFile.whileGotFile(fileName);
		}
	}
	
	private static void saveFile(InputStream is, File f) 
	{
		if (f.exists()) 
		{
			f.delete();
		}
		
		try
		{
			f.createNewFile();
			FileOutputStream fOut = null;
			byte[] buffer = new byte[1024];
			fOut = new FileOutputStream(f);
			while(is.read(buffer) != -1) 
			{
				fOut.write(buffer);
			}
			fOut.close();
			is.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static File getFile(String url, Context ctx) {
		File dir;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			dir = new File(android.os.Environment.getExternalStorageDirectory(), "imgo/imageCache");
		} else {
			dir = ctx.getCacheDir();
		}
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String filename = url.substring(url.lastIndexOf("/"));
		File f = new File(dir, filename);
		return f;
	}
	
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
}
