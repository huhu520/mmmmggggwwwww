package com.mgw.member.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipException;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.mgw.member.R;
import com.mgw.member.ui.activity.UpdateAlertDialog;
import com.mgw.member.uitls.UnZipFiles;

public class UpdateManager {
	/* 更新类型 */
	private static String uptype = "";
	/* 下载地址 */
	private String dowloadUrl = "";
	/* apk的版本名称 */
	private String FVersion_Name = "";
	/* apk的更新内容描述 */
	private String fv_exp = "";
	/* apk更新dialog */
	private static AlertDialog mdialog;

	private AlertDialog downloadDialog;
	private ProgressBar mProgress;
	Context mContext;
	/* 下载的apk文件的保存位置 */
	public static final String savePath = Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/apk";
	/* 下载的html文件的保存位置 */
	public static final String htmlsavePath = Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/html/";

	public static final String saveFileName = savePath + "/upapk.apk";
	private String htmlsaveFileName = "";
	/** 解压路径 */
	public static final String unziphtmlsavePath = Environment.getExternalStorageDirectory().getPath() + "/mgw" + "/unziphtml";
	private int progress, downloadcount = 0;

	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private final boolean interceptFlag = false;
	private Thread downLoadThread;
	/** 检测到的需要更新的html版本的数量 */
	private int htmlversionnumber = 0;
	private int htmlversionnumber2 = 0;
	private String[] urls;
	/** 通知栏显示下载进度 */
	private NotificationManager mNotificationManager = null;
	private Notification mNotification;
	Intent intent;

	public UpdateManager() {
	}

	/** apk更新调用的构造方法 */
	public UpdateManager(Context context, String url, String fv_exp, String FVersion_Name) {
		uptype = "apk";
		this.dowloadUrl = url;
		this.FVersion_Name = "最新版本：" + FVersion_Name + "版";
		this.mContext = context;
		this.fv_exp = fv_exp;
	}

	/** html更新调用的构造方法 */
	public UpdateManager(Context context, String url, String htmlversionnumber) {
		uptype = "html";
		this.htmlversionnumber = Integer.valueOf(htmlversionnumber) - 1;
		this.htmlversionnumber2 = Integer.valueOf(htmlversionnumber) - 1;
		/** html的下载链接地址数组，下标最小的数组元素为最老的地址，所以从下标0开始下载 */
		urls = url.split(";");
		mContext = context;
		htmlsaveFileName = htmlsavePath + mContext.getSharedPreferences("mgw_data", 0).getString("htmlcode", getVersionName()) + ".zip";
		File file = new File(htmlsavePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// htmlsaveFileName = htmlsavePath +
		// mContext.getSharedPreferences("mgw_data", 0).getString("htmlcode",
		// getVersionName()) + ".zip";
		this.dowloadUrl = urls[0];
	}

	/** 检查升级信息，如果是apk更新就展示更新的对话框，如果是html更新，不展示任何页面，直接下载 */
	public void checkUpdateInfo() {
		if (uptype.equals("apk")) {
//			showNoticeDialog();
		} else {
			downLoadThread = new Thread(mdownRunnable);
			downLoadThread.start();
		}
	}

	


	/** 显示更新通知对话框 */
	public void showNoticeDialog() {
		mdialog = new AlertDialog.Builder(mContext).create();
		mdialog.setCancelable(false);
		Window window = mdialog.getWindow();
		window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mdialog.show();
		window.setContentView(R.layout.mdialog);
		
		
		
		
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		TextView tv_md_FVersion_Explain = (TextView) window.findViewById(R.id.tv_md_FVersion_Explain);
		final TextView tv_updata_vn = (TextView) window.findViewById(R.id.tv_updata_vn);
		final ImageView iv_updata_download = (ImageView) window.findViewById(R.id.iv_updata_download);
		tv_updata_vn.setText(FVersion_Name);
		tv_md_FVersion_Explain.setText(fv_exp);
		iv_updata_download.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// mdialog.dismiss();
				tv_updata_vn.setText("app下载中请稍后！");
				iv_updata_download.setVisibility(View.GONE);
				notificationInit();
				showDownloadDialog();
			}

		});

	}

	public static void DismissDialog() {
		if (mdialog != null) {
			mdialog.dismiss();
		}
	}

	public void startDialog() {
		if (uptype.equals("apk")) {
			showNoticeDialog();
		}
	}

	/** 接收下载进度信息的handler */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				if (uptype.equals("apk")) {
					// mProgress.setProgress(progress);
					//
					mNotification.contentView.setProgressBar(R.id.progress, 100, progress, false);
					mNotificationManager.notify(0, mNotification);

				}

				break;
			case DOWN_OVER:
				if (uptype.equals("apk")) {
					Toast.makeText(mContext, "下载完成！", Toast.LENGTH_SHORT).show();
					// downloadDialog.dismiss();
					// mContext.stopService(intent);
					DismissDialog();
					installApk();

				} else {
					UnZipFiles unZipFiles = new UnZipFiles();
					try {
						File file = new File(unziphtmlsavePath);
						if (!file.exists()) {
							file.mkdirs();
						}
						unZipFiles.upZipFile(new File(htmlsaveFileName), unziphtmlsavePath);

						mContext.sendBroadcast(new Intent().putExtra("hadunzipfile", "to"));
						htmlversionnumber--;
						/* 循环下载html更新直到更新完 */
						if (htmlversionnumber >= 0) {
							dowloadUrl = urls[htmlversionnumber2 - htmlversionnumber];
							checkUpdateInfo();
						} else {
							// Toast.makeText(mContext, "html全部更新完成！",
							// Toast.LENGTH_SHORT).show();
						}

					} catch (ZipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}

				break;
			default:
				break;
			}
		};
	};

	/** 展示下载过程的界面 */
	private void showDownloadDialog() {
		// AlertDialog.Builder builder = new Builder(mContext);
		// builder.setTitle("软件版本更新");
		//
		// final LayoutInflater inflater = LayoutInflater.from(mContext);
		// View v = inflater.inflate(R.layout.progress, null);
		// mProgress = (ProgressBar) v.findViewById(R.id.progress);
		//
		// builder.setView(v);
		// builder.setCancelable(false);
		//
		// // builder.setNegativeButton("取消", new OnClickListener() {
		// // @Override
		// // public void onClick(DialogInterface dialog, int which) {
		// // dialog.dismiss();
		// // interceptFlag = true;
		// // }
		// // });
		// downloadDialog = builder.create();
		// downloadDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		//
		// downloadDialog.show();

		downloadApk();
	}

	/** 下载文件的线程 */
	private final Runnable mdownRunnable = new Runnable() {
		@Override
		public void run() {
			try {

				URL url = new URL(dowloadUrl);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				File file2 = new File(htmlsavePath);
				if (!file2.exists()) {
					file2.mkdirs();
				}
				String apkFile = saveFileName;

				File ApkFile;
				if (!uptype.equals("apk")) {
					ApkFile = new File(htmlsaveFileName);
				} else {
					ApkFile = new File(apkFile);
				}

				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					if (uptype.equals("apk")) {
						if (downloadcount == 0 || progress - downloadcount == 5 || progress == 100) {
							downloadcount = progress;
							mHandler.sendEmptyMessage(DOWN_UPDATE);
						}
					}
					if (numread <= 0) {
						// װ
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/** 安装apk */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		System.exit(0);
	}

	/** 获取当前apk版本 */
	public String getVersionName() {
		try {
			PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			return pInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "1.0";
	}

	public  synchronized void downloadApk() {
		downLoadThread = new Thread(mdownRunnable);
		downLoadThread.start();
	}

	/** 初始化下载进度条 **/
	private void notificationInit() {
		// 通知栏内显示下载进度条
		// intent = new Intent(mContext, null);
		// PendingIntent pIntent = PendingIntent.getActivity(mContext, 0,
		// intent, 0);
		mNotificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		mNotification = new Notification();
		mNotification.icon = R.drawable.icon;
		mNotification.tickerText = "开始下载";
		mNotification.contentView = new RemoteViews(mContext.getPackageName(), R.layout.progress);// 通知栏中进度布局
		// mNotification.contentIntent = pIntent;
		mNotification.contentView.setProgressBar(R.id.progress, 100, progress, false);
		// mNotificationManager.notify(0, mNotification);
	}
}
