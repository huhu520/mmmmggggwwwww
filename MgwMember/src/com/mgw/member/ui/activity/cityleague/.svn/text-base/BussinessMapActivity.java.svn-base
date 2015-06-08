package com.mgw.member.ui.activity.cityleague;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.mgw.member.R;

@SuppressLint("HandlerLeak")
public class BussinessMapActivity extends Activity implements OnClickListener, OnGetRoutePlanResultListener {
	MapView mMapView = null;
	BaiduMap mBaiduMap;

	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	int nodeIndex = -2;// 节点索引,供浏览节点时使用
	@SuppressWarnings("rawtypes")
	RouteLine route = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;

	private LocationClient mLocationClient = null;
	public BDLocation mLocation = null;

	public GeoCoder mGeoCoder;
	public String mCity = "";

	double mLat, mLng;

	Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map_view);

		findViewById(R.id.ibBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		findViewById(R.id.left).setOnClickListener(this);
		findViewById(R.id.mid).setOnClickListener(this);
		findViewById(R.id.right).setOnClickListener(this);

		mLat = getIntent().getExtras().getDouble("lng");
		mLng = getIntent().getExtras().getDouble("lat");
		((TextView) findViewById(R.id.add)).setText(getIntent().getExtras().getString("address"));

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.setMyLocationEnabled(true);
		final Handler handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				LatLng ll = new LatLng(mLat, mLng);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				mGeoCoder = GeoCoder.newInstance();
				mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
					@Override
					public void onGetGeoCodeResult(GeoCodeResult result) {
						if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
							// 没有检索到结果
						}
					}

					@Override
					public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
						if (result != null && result.error == SearchResult.ERRORNO.NO_ERROR) {
							mCity = result.getAddressDetail().city;
							if (mCity == null)
								mCity = "";
						}
						// 获取反向地理编码结果
					}
				});
				mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
			}
		};

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				handle.sendEmptyMessage(0);
			}
		}, 200);

		final BDLocationListener listener = new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
					return;

				mLocation = location;
				mLocationClient.unRegisterLocationListener(this);
				mLocationClient.stop();

				MyLocationData locData = new MyLocationData.Builder().accuracy(mLocation.getRadius()).direction(100).latitude(mLocation.getLatitude()).longitude(mLocation.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);

				onClick(findViewById(R.id.left));
			}

			@Override
			public void onReceivePoi(BDLocation poiLocation) {

			}
		};

		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(listener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(600000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();

		dialog = new Dialog(this, R.style.MyDialogStyle);
		dialog.setContentView(R.layout.dialog);
		dialog.show();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mSearch != null)
			mSearch.destroy();
		if (mGeoCoder != null)
			mGeoCoder.destroy();
		mMapView.onDestroy();
		super.onDestroy();
	}

	/*
	 * public class MyLocationListener implements BDLocationListener {
	 * 
	 * @Override public void onReceiveLocation(BDLocation location) { if
	 * (location == null) return ;
	 * 
	 * MyLocationData locData = new
	 * MyLocationData.Builder().accuracy(location.getRadius())
	 * .direction(100).latitude(location.getLatitude())
	 * .longitude(location.getLongitude()).build();
	 * mBaiduMap.setMyLocationData(locData); LatLng ll = new
	 * LatLng(location.getLatitude(),location.getLongitude()); MapStatusUpdate u
	 * = MapStatusUpdateFactory.newLatLng(ll); mBaiduMap.animateMapStatus(u);
	 * 
	 * //创建InfoWindow展示的view TextView button = new
	 * TextView(getApplicationContext());
	 * button.setBackgroundResource(R.drawable.corner);
	 * button.setText("贺龙体育馆\n地址: 劳动路 \n电话: 13888888888");
	 * button.setTextColor(Color.rgb(80, 80, 80)); button.setPadding(40, 20, 40,
	 * 20); //定义用于显示该InfoWindow的坐标点 LatLng pt = new
	 * LatLng(location.getLatitude(), location.getLongitude());
	 * //创建InfoWindow的点击事件监听者 OnInfoWindowClickListener listener = new
	 * OnInfoWindowClickListener() { public void onInfoWindowClick() {
	 * //添加点击后的事件响应代码 } }; //创建InfoWindow InfoWindow mInfoWindow = new
	 * InfoWindow(button, pt, listener); //显示InfoWindow
	 * mBaiduMap.showInfoWindow(mInfoWindow); }
	 * 
	 * public void onReceivePoi(BDLocation poiLocation) { if (poiLocation ==
	 * null) { return ; } StringBuffer sb = new StringBuffer(256);
	 * sb.append("Poi time : "); sb.append(poiLocation.getTime());
	 * sb.append("\nerror code : "); sb.append(poiLocation.getLocType());
	 * sb.append("\nlatitude : "); sb.append(poiLocation.getLatitude());
	 * sb.append("\nlontitude : "); sb.append(poiLocation.getLongitude());
	 * sb.append("\nradius : "); sb.append(poiLocation.getRadius()); if
	 * (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
	 * sb.append("\naddr : "); sb.append(poiLocation.getAddrStr()); }
	 * if(poiLocation.hasPoi()){ sb.append("\nPoi:");
	 * sb.append(poiLocation.getPoi()); }else{ sb.append("noPoi information"); }
	 * } }
	 */

	public void showDialog() {
		/*
		 * final Dialog dialog = new Dialog(this,R.style.dialog);
		 * dialog.setContentView(R.layout.dialog); dialog.setCancelable(true);
		 * 
		 * ((Button)dialog.findViewById(R.id.confirm)).setText("我知道了");
		 * dialog.findViewById(R.id.confirm).setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { dialog.dismiss(); } });
		 * dialog.findViewById(R.id.cancel).setVisibility(View.GONE);
		 * ((TextView)dialog.findViewById(R.id.msg)).setText("还没有获取到您当前位置");
		 * dialog.show();
		 */
	}

	@Override
	public void onClick(View arg0) {
		/*
		 * if(R.id.call == arg0.getId()) { Intent intent = new
		 * Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mGym.phone));
		 * startActivity(intent); return; }
		 */
		if (mLocation == null) {
			showDialog();
			return;
		}

		if (mSearch == null) {
			mSearch = RoutePlanSearch.newInstance();
			mSearch.setOnGetRoutePlanResultListener(this);
		}

		mBaiduMap.clear();

		((Button) findViewById(R.id.left)).setSelected(false);
		((Button) findViewById(R.id.mid)).setSelected(false);
		((Button) findViewById(R.id.right)).setSelected(false);

		((Button) arg0).setSelected(true);

		LatLng sLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
		LatLng eLatLng = new LatLng(mLat, mLng);

		PlanNode stNode = PlanNode.withLocation(sLatLng);
		PlanNode enNode = PlanNode.withLocation(eLatLng);

		switch (arg0.getId()) {
		case R.id.left:
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
			break;

		case R.id.mid:
			mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(mCity).to(enNode));
			break;

		case R.id.right:
			mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
			break;
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			int distance = result.getRouteLines().get(0).getDistance();
			String disStr = "";
			if (distance < 1000)
				disStr = distance + "米";
			else {
				int km = distance / 1000;
				distance = distance - km * 1000;
				disStr = km + "." + distance + "千米";
			}

			int sec = result.getRouteLines().get(0).getDuration();
			int hour = sec / 3600;
			int min = (sec - hour * 3600) / 60;
			((TextView) findViewById(R.id.distance)).setText(hour + "小时" + min + "分钟    " + disStr);

			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			int distance = result.getRouteLines().get(0).getDistance();
			String disStr = "";
			if (distance < 1000)
				disStr = distance + "米";
			else {
				int km = distance / 1000;
				distance = distance - km * 1000;
				disStr = km + "." + distance + "千米";
			}

			int sec = result.getRouteLines().get(0).getDuration();
			int hour = sec / 3600;
			int min = (sec - hour * 3600) / 60;
			((TextView) findViewById(R.id.distance)).setText(hour + "小时" + min + "分钟    " + disStr);

			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {

		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		dialog = null;

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			int distance = result.getRouteLines().get(0).getDistance();
			String disStr = "";
			if (distance < 1000)
				disStr = distance + "米";
			else {
				int km = distance / 1000;
				distance = distance - km * 1000;
				disStr = km + "." + distance + "千米";
			}

			int sec = result.getRouteLines().get(0).getDuration();

			int hour = sec / 3600;
			int min = (sec - hour * 3600) / 60;

			float price = 10;
			if (result.getRouteLines().get(0).getDistance() > 3000) {
				float offset = (result.getRouteLines().get(0).getDistance() - 3000) / (float) 1000;
				price += 1.6 * offset;
			}

			((TextView) findViewById(R.id.distance)).setText(hour + "小时" + min + "分钟    " + disStr);
			((TextView) findViewById(R.id.price)).setText(String.valueOf((int) price));

			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}
}
