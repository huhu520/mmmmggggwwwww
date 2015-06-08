package com.mgw.member.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.mgw.member.uitls.IOUtils;
import com.mgw.member.uitls.LogUtils;
import com.mgw.member.uitls.StringUtils;

/**
 * Created by huyan .
 */
public class HttpHelper {
	/**
	 * The invalid APN
	 */
	public static final String INVALID_ACCESS_POINT = "None";
	public static final String Network_2G = "2G";
	public static final String Network_3G = "3G";
	public static final String Network_WIFI = "WIFI";

	public static final int NETWORK_TYPE_LTE = 13;
	public static final int NETWORK_TYPE_IDEN = 11;
	public static final int NETWORK_TYPE_HSUPA = 9;
	public static final int NETWORK_TYPE_HSPAP = 15;
	public static final int NETWORK_TYPE_HSPA = 10;
	public static final int NETWORK_TYPE_HSDPA = 8;
	public static final int NETWORK_TYPE_EVDO_B = 12;
	public static final int NETWORK_TYPE_EHRPD = 14;

	public static final String URL = "http://127.0.0.1:8090/";

	// public static final String URL = "http://192.168.16.55:8080/WebInfos/";

	/** get请求，获取返回字符串内容 */
	public static HttpResult get(String url) {
		HttpGet httpGet = new HttpGet(url);
		return execute(url, httpGet);
	}

	/** post请求，获取返回字符串内容 */
	public static HttpResult post(String url, byte[] bytes) {
		HttpPost httpPost = new HttpPost(url);
		ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
		httpPost.setEntity(byteArrayEntity);
		return execute(url, httpPost);
	}

	/** 下载 */
	public static HttpResult download(String url) {
		HttpGet httpGet = new HttpGet(url);
		return execute(url, httpGet);
	}

	/** 执行网络访问 */
	private static HttpResult execute(String url, HttpRequestBase requestBase) {
		boolean isHttps = url.startsWith("https://");// 判断是否需要采用https
		AbstractHttpClient httpClient = HttpClientFactory.create(isHttps);
		HttpContext httpContext = new SyncBasicHttpContext(
				new BasicHttpContext());
		HttpRequestRetryHandler retryHandler = httpClient
				.getHttpRequestRetryHandler();// 获取重试机制
		int retryCount = 0;
		boolean retry = true;
		while (retry) {
			try {
				HttpResponse response = httpClient.execute(requestBase,
						httpContext);// 访问网络
				if (response != null) {
					return new HttpResult(response, httpClient, requestBase);
				}
			} catch (Exception e) {
				IOException ioException = new IOException(e.getMessage());
				retry = retryHandler.retryRequest(ioException, ++retryCount,
						httpContext);// 把错误异常交给重试机制，以判断是否需要采取从事
				LogUtils.e(e);
			}
		}
		return null;
	}

	/** http的返回结果的封装，可以直接从中获取返回的字符串或者流 */
	public static class HttpResult {
		private final HttpResponse mResponse;
		private InputStream mIn;
		private String mStr;
		private final HttpClient mHttpClient;
		private final HttpRequestBase mRequestBase;

		public HttpResult(HttpResponse response, HttpClient httpClient,
				HttpRequestBase requestBase) {
			mResponse = response;
			mHttpClient = httpClient;
			mRequestBase = requestBase;
		}

		public int getCode() {
			StatusLine status = mResponse.getStatusLine();
			return status.getStatusCode();
		}

		/** 从结果中获取字符串，一旦获取，会自动关流，并且把字符串保存，方便下次获取 */
		public String getString() {
			if (!StringUtils.isEmpty(mStr)) {
				return mStr;
			}
			InputStream inputStream = getInputStream();
			ByteArrayOutputStream out = null;
			if (inputStream != null) {
				try {
					out = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024 * 4];
					int len = -1;
					while ((len = inputStream.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					byte[] data = out.toByteArray();
					mStr = new String(data, "utf-8");
				} catch (Exception e) {
					LogUtils.e(e);
				} finally {
					IOUtils.close(out);
					close();
				}
			}
			return mStr;
		}

		/** 获取流，需要使用完毕后调用close方法关闭网络连接 */
		public InputStream getInputStream() {
			if (mIn == null && getCode() < 300) {
				HttpEntity entity = mResponse.getEntity();
				try {
					mIn = entity.getContent();
				} catch (Exception e) {
					LogUtils.e(e);
				}
			}
			return mIn;
		}

		/** 关闭网络连接 */
		public void close() {
			if (mRequestBase != null) {
				mRequestBase.abort();
			}
			IOUtils.close(mIn);
			if (mHttpClient != null) {
				mHttpClient.getConnectionManager().closeExpiredConnections();
			}
		}
	}

	/**
	 * 获取网络接入�? * @return {@link #INVALID_ACCESS_POINT} or {@link #Network_WIFI}
	 * or "cmnet","cmwap"诸如此类
	 * 
	 * @author yinb
	 */
	public static String getAccessPoint(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if (info == null || info.getState() != NetworkInfo.State.CONNECTED) {
			return INVALID_ACCESS_POINT;
		}
		String networkType = info.getTypeName();
		if (networkType != null && !networkType.equalsIgnoreCase("WIFI")) {
			String mobiName = info.getExtraInfo();
			if (mobiName != null && !mobiName.equals("")) {
				networkType = mobiName;
			}
		}
		return networkType;
	}

	/**
	 * 判断移动网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileNetworkActive(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = mConnectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return activeNetInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 判断wifi是否可用
	 * 
	 * @param context
	 * @return
	 * @author yinb
	 */
	public boolean isWifiActive(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWiFiNetworkInfo != null) {
			return mWiFiNetworkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * Gps是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = locationManager.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	/**
	 * 判断当前网络是不3G网络
	 * 
	 * @param context
	 * @return
	 * @author yinb
	 */
	public static boolean is3G(Context context) {
		if (Network_3G.equals(getNetworkType(context))) {
			return true;
		}
		return false;
	}

	/**
	 * 网络是否可用
	 * 
	 * @param activity
	 * @return 如果有可用连接则返回true
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivityManager == null) {
		} else {
			NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return {@link #INVALID_ACCESS_POINT} or {@link #Network_2G} or
	 *         {@link #Network_3G} or {@link #Network_WIFI}
	 * @author yinb
	 */
	public static String getNetworkType(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if (info == null || info.getState() != NetworkInfo.State.CONNECTED) {
			return INVALID_ACCESS_POINT;
		}

		int type = info.getType();
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int subtype = mTelephonyManager.getNetworkType();

		return getMobileNetworkType(type, subtype);
	}

	private static String getMobileNetworkType(int type, int subtype) {
		if (type == ConnectivityManager.TYPE_WIFI) {
			return Network_WIFI;
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subtype) {

			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case NETWORK_TYPE_LTE:
			case NETWORK_TYPE_HSUPA:
			case NETWORK_TYPE_HSPAP:
			case NETWORK_TYPE_HSPA:
			case NETWORK_TYPE_HSDPA:
			case NETWORK_TYPE_EVDO_B:
			case NETWORK_TYPE_EHRPD:
				return Network_3G;

			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			case NETWORK_TYPE_IDEN:
			default:
				return Network_2G;
			}
		}
		return Network_2G;
	}
}
