package com.mgw.member.uitls;

import org.apache.http.Header;

import u.aly.r;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mgw.member.R;

/**
 * 数据网络请求类
 * 
 * @author0 Administrator
 * 
 */
public class MgqDataHandler extends AsyncHttpResponseHandler {
	public final static String TAG = "MgqDataHandler";
	private final Context context;
	private boolean showDialog;
	private boolean showError;
	@SuppressWarnings("unused")
	private boolean clearCookie;
	private ProgressDialog progressDialog;

	public boolean isShowDialog() {
		return showDialog;
	}

	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}

	public boolean isShowError() {
		return showError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	public MgqDataHandler(Context ctx, boolean showDialog, boolean clearCookie) {

		this.showDialog = true;
		this.showError = true;
		context = ctx;
		this.showDialog = showDialog;

	}

	public void onFailed(Throwable ee) {
		LogUtils.i(TAG, "===in  onFailed==");
	}

	@Override
	public void onStart() {
		LogUtils.i(TAG, "===in  onStart==");
		super.onStart();
		if (checkNetwork()) {

			if (showDialog) {
				progressDialog = ProgressDialog.show(context, "", "正在加载中...", true, false);
				progressDialog.setCancelable(true);
			}
		} else {
			LogUtils.i(TAG, "===in  onStart==" + " net work  is  error====");

			Toast.makeText(context, R.string.network_unavailable, Toast.LENGTH_LONG).show();
		}
	}

	public void onFailure(Throwable ble) {
		LogUtils.i(TAG, "===in  onFailure==" + ble.getMessage());
		Toast.makeText(context, R.string.net_exception_wating, Toast.LENGTH_LONG).show();
	}

	public void onSuccess(String response) {
		LogUtils.d(TAG,"json str"+ response);
		LogUtils.i(TAG, "===in  onSuccess==");
		try {

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (showDialog) {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
		}
	}


	private boolean checkNetwork() {
		String service = Context.CONNECTIVITY_SERVICE;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(service);
		NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
		if (null == activeNetwork) {
			return false;
		}
		return activeNetwork.isConnected();
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		LogUtils.d(TAG, "http request success");
		try {
			String response = new String(responseBody, "UTF-8");
			LogUtils.d("response json:", response);
			onSuccess(response);
			/*
			 * // TODO - 主线程解码，阻塞，心增加线程解码 beyself 2014-08-23 final Handler
			 * handle = new Handler() { public void handleMessage(Message msg) {
			 * onSuccess((String)msg.obj); } };
			 * 
			 * final String resp = response; new Thread() { public void run() {
			 * handle.sendMessage(handle.obtainMessage(0, XXTEA.cj(resp,
			 * "1234567890abcdef"))); } }.start();
			 */
			// TODO-如果response全是json对象则可以对需要做认证的网络进行判断
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		LogUtils.d(TAG, "http request failed");
		if (showDialog) {
			if (NetworkProber.isNetworkAvailable(context)) {
				Toast.makeText(context, R.string.net_exception_wating, Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, R.string.network_unavailable, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onCancel() {
		super.onCancel();
		LogUtils.d(TAG, "http request canceled");
		dismissLoading();
	}

	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		super.onProgress(bytesWritten, totalSize);
		LogUtils.d(TAG, "http request onProgress");
	}

	@Override
	public void onRetry(int retryNo) {
		super.onRetry(retryNo);
		LogUtils.d(TAG, "http request retry");
	}

	@Override
	public void onFinish() {
		super.onFinish();
		LogUtils.d(TAG, "http request finished");
		// Completed the request (either success or failure)
		dismissLoading();
	}

	private void dismissLoading() {
		if (showDialog) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	}

}
