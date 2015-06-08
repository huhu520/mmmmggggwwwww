package com.mgw.member.updateApp;

import java.io.File;

import com.mgw.member.ottoEvent.AppDownloadProgressEvent;
import com.mgw.member.ottoEvent.BusProvider;
import com.mgw.member.ottoEvent.ReGetLoginInfoEvent;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.Utils;
import com.squareup.otto.Produce;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;
import com.thin.downloadmanager.DownloadRequest.Priority;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class MyIntentService extends IntentService {

	private ThinDownloadManager downloadManager;
	private String urlPath;
	// 下载的线程数
	private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;

	MyDownloadListner myDownloadStatusListener = new MyDownloadListner();

	int downloadId1 = 0;
	private DownloadRequest request1;

	EppNotificationControl notificationControl;

	public MyIntentService() {
		super("MyIntentService");
	}

	Context context;
	private String fVersion_FileURL;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		context = this;
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		BusProvider.getInstance().register(this);
		fVersion_FileURL = intent.getStringExtra("FVersion_FileURL");

		Log.i("jone", "download FVersion_FileURL" + fVersion_FileURL);

	}

	@Override
	protected void onHandleIntent(Intent intent) {

		urlPath = Utils.createSDCardDir("upapk.apk");
		notificationControl = new EppNotificationControl(urlPath, context);
		downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
		initDownload();

		if (downloadManager.query(downloadId1) == DownloadManager.STATUS_NOT_FOUND) {
			downloadId1 = downloadManager.add(request1);
		}
		notificationControl.showProgressNotify();

	}

	private void initDownload() {
		Uri downloadUri = Uri.parse(fVersion_FileURL);
		Uri destinationUri = Uri.parse(urlPath);
		request1 = new DownloadRequest(downloadUri).setDestinationURI(destinationUri).setPriority(Priority.HIGH).setDownloadListener(myDownloadStatusListener);

	}

	int oldprogress = 0;

	class MyDownloadListner implements DownloadStatusListener {

		@Override
		public void onDownloadComplete(int id) {
			if (id == downloadId1) {
				Log.i("jone", "download completed");

				AppDownloadProgressEvent appDownloadProgressEvent = getAppDownloadProgressEvent();
				appDownloadProgressEvent.setDownloadstatus(2);
				BusProvider.getInstance().post(appDownloadProgressEvent);

			}

		}

		@Override
		public void onDownloadFailed(int id, int errorCode, String errorMessage) {

			if (id == downloadId1) {
				AppDownloadProgressEvent appDownloadProgressEvent = getAppDownloadProgressEvent();
				appDownloadProgressEvent.setDownloadstatus(3);
				BusProvider.getInstance().post(appDownloadProgressEvent);
			}

		}

		@Override
		public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {

			if (id == downloadId1) {
				Log.i("jone", progress + "");

				if (progress > oldprogress) {
					notificationControl.updateNotification(progress);
					AppDownloadProgressEvent appDownloadProgressEvent = getAppDownloadProgressEvent();
					appDownloadProgressEvent.setProgress(progress);
					appDownloadProgressEvent.setDownloadstatus(1);
					BusProvider.getInstance().post(appDownloadProgressEvent);

				}
				oldprogress = progress;

			}

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("jone", "onDestroy");
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
	}

	@Produce
	public AppDownloadProgressEvent getAppDownloadProgressEvent() {
		LogUtils.i("otto" + "ReGetLoginInfoEvent=newProgres=");
		AppDownloadProgressEvent appDownloadProgressEvent = new AppDownloadProgressEvent();
		return appDownloadProgressEvent;
	}
}
