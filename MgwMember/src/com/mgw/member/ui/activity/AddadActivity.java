package com.mgw.member.ui.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.mgw.member.R;
import com.mgw.member.constant.Define_C;
import com.mgw.member.ui.fragment.MyInfoFragment;
import com.mgw.member.uitls.MgqDataHandler;
import com.mgw.member.uitls.MgqRestClient;
import com.squareup.picasso.Picasso;

/**
 * 广告添加页
 * 
 * @author Administrator
 * 
 */
public class AddadActivity extends Activity {
	private EditText et_title_ad, et_abstract_ad, et_detail_ad;
	private ImageView imageView_adcover, imageView_firstadimg, imageView_nextadimg, imageView_thirdadimg;
	private Button button_savead;
	private ImageButton button_deletcover, button_deletefirstimg, button_deletenextimg, button_deletethirdimg;
	private TextView imagebutton_addad_back;
	private LinearLayout liLayout_addad;
	private InputMethodManager inputMethodManager;
	// public boolean coveimg = false;
	// public boolean firstimg = false;
	// public boolean nexttimg = false;
	// public boolean thirdtimg = false;
	// public String coveimgid = "";
	// public String firstimgid = "";
	// public String nexttimgid = "";
	// public String thirdtimgid = "";
	private String images = "";
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addad);
		et_title_ad = (EditText) findViewById(R.id.et_title_ad);
		et_abstract_ad = (EditText) findViewById(R.id.et_abstract_ad);
		et_detail_ad = (EditText) findViewById(R.id.et_detail_ad);
		imageView_adcover = (ImageView) findViewById(R.id.imageView_adcover);
		imageView_firstadimg = (ImageView) findViewById(R.id.imageView_firstadimg);
		imageView_nextadimg = (ImageView) findViewById(R.id.imageView_nextadimg);
		imageView_thirdadimg = (ImageView) findViewById(R.id.imageView_thirdadimg);
		button_savead = (Button) findViewById(R.id.button_savead);
		imagebutton_addad_back = (TextView) findViewById(R.id.imagebutton_addad_back);
		button_deletcover = (ImageButton) findViewById(R.id.button_deletecover);
		button_deletefirstimg = (ImageButton) findViewById(R.id.button_deletefirstimg);
		button_deletenextimg = (ImageButton) findViewById(R.id.button_deletenextimg);
		button_deletethirdimg = (ImageButton) findViewById(R.id.button_deletethirdimg);
		liLayout_addad = (LinearLayout) findViewById(R.id.liLayout_addad);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		Intent intent = getIntent();
		if (intent.getIntExtra("mod", 1) == 2) {
			String adid = intent.getStringExtra("adid");
			Log.i("===adid===", adid);
			getadinfoData(true, adid);
		}
		Log.i("oncreat方法执行了", "oncreat方法执行了。。。");
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences.Editor sharedPreferences = getSharedPreferences("mgw_data", 0).edit();
		sharedPreferences.putInt("QQsharecod", 200);
		sharedPreferences.commit();
		init();
	}

	// 給各个控件设置监听
	public void init() {
		imagebutton_addad_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		imageView_adcover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Define_C.coveimg = true;
				showImgDialog();
			}
		});
		imageView_firstadimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Define_C.firstimg = true;
				showImgDialog();
			}
		});
		imageView_nextadimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Define_C.nexttimg = true;
				showImgDialog();
			}
		});
		imageView_thirdadimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Define_C.thirdtimg = true;
				showImgDialog();

			}
		});
		button_deletcover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView_adcover.setImageResource(R.drawable.moren0);
				button_deletcover.setVisibility(View.GONE);
				Define_C.coveimgid = "";

			}
		});
		button_deletefirstimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView_firstadimg.setImageResource(R.drawable.moren0);
				button_deletefirstimg.setVisibility(View.GONE);
				Define_C.firstimgid = "";
			}
		});
		button_deletenextimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView_nextadimg.setImageResource(R.drawable.moren0);
				button_deletenextimg.setVisibility(View.GONE);
				Define_C.nexttimgid = "";

			}
		});
		button_deletethirdimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView_thirdadimg.setImageResource(R.drawable.moren0);
				button_deletethirdimg.setVisibility(View.GONE);
				Define_C.thirdtimgid = "";

			}
		});
		button_savead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (et_title_ad.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), "广告标题不能为空或者空格哦！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (et_abstract_ad.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), "广告摘要不能为空或者空格哦！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (et_detail_ad.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), "广告内容不能为空或者空格哦！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (Define_C.coveimgid.equals("")) {
					Toast.makeText(getApplicationContext(), "封面图片必须要有哦！", Toast.LENGTH_SHORT).show();
					return;
				}
				confirmData();
			}
		});
		liLayout_addad.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});

	}

	AlertDialog mDailog = null;

	// 展示选择图片对话框
	void showImgDialog() {
		mDailog = new AlertDialog.Builder(this).setCancelable(false).setNegativeButton("取消选择", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Define_C.coveimg = false;
				Define_C.firstimg = false;
				Define_C.nexttimg = false;
				Define_C.thirdtimg = false;
				mDailog.dismiss();
			}
		}).setTitle("图片选择与处理").setItems(new String[] { "拍照", "从相册中选择" }, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				displayBriefMemory();
				final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
				activityManager.getMemoryInfo(info);
				// if ((info.availMem >> 10) < 160000) {
				// Toast.makeText(getApplicationContext(),
				// "您的设备可用内存不够，无法执行以下操作，敬请谅解！",
				// Toast.LENGTH_LONG).show();
				// return;
				// }
				if (which == 0) {

					showImgPick(0);

				} else if (which == 1) {

					showImgPick(1);

				}
				mDailog.dismiss();
				mDailog = null;
			}
		}).show();
	}

	public static final int CROPPHOT_CODE = 5;
	public static final int IMAGE_CODE = 3;
	public static final int TAKEPHOT_CODE = 4;
	private final String IMAGE_TYPE = "image/*";
	// private static final String IMAGE_FILE_LOCATION =
	// "file:///sdcard/temp.jpg";
	// Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
	Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/" + IMAGE_FILE_NAME));

	// 处理拍的照片
	private void cropImageUri(Uri uri, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 7);
		intent.putExtra("aspectY", 4);
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 182);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);

	}

	// 处理相册的图片
	private void getImage() {
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		getAlbum.putExtra("crop", "true");
		getAlbum.putExtra("aspectX", 7);
		getAlbum.putExtra("aspectY", 4);
		getAlbum.putExtra("outputX", 320);
		getAlbum.putExtra("outputY", 182);
		getAlbum.putExtra("scale", true);
		getAlbum.putExtra("return-data", false);
		getAlbum.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		getAlbum.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		getAlbum.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(getAlbum, IMAGE_CODE);
	}

	void showImgPick(int type) {
		if (type == 0) {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKEPHOT_CODE);
			}
		} else {
			getImage();
		}
	}

	Bitmap photo = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			Define_C.coveimg = false;
			Define_C.firstimg = false;
			Define_C.nexttimg = false;
			Define_C.thirdtimg = false;
			return;
		}

		switch (requestCode) {
		case TAKEPHOT_CODE:
			cropImageUri(imageUri, CROPPHOT_CODE);
			return;
		case CROPPHOT_CODE:
			photo = decodeUriAsBitmap(imageUri);
			break;
		case IMAGE_CODE:
			photo = decodeUriAsBitmap(imageUri);
			break;
		}

		InputStream is = Bitmap2IS(photo);

		getData(is, photo);
	}

	boolean mUpImage = false;

	// 图片上传方法代码
	private void getData(final InputStream is, final Bitmap photo) {
		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						if (Define_C.coveimg) {
							Log.i("封面图地址", obj.getJSONObject("item").getString("url"));
							Define_C.coveimgid = obj.getJSONObject("item").getString("url");
							imageView_adcover.setImageBitmap(photo);
							button_deletcover.setVisibility(View.VISIBLE);
							Define_C.coveimg = false;
						}
						if (Define_C.firstimg) {
							Log.i("第一张图地址", obj.getJSONObject("item").getString("url"));
							Define_C.firstimgid = obj.getJSONObject("item").getString("url");
							imageView_firstadimg.setImageBitmap(photo);
							button_deletefirstimg.setVisibility(View.VISIBLE);
							Define_C.firstimg = false;
						}
						if (Define_C.nexttimg) {
							Log.i("第二张图地址", obj.getJSONObject("item").getString("url"));
							Define_C.nexttimgid = obj.getJSONObject("item").getString("url");
							imageView_nextadimg.setImageBitmap(photo);
							button_deletenextimg.setVisibility(View.VISIBLE);
							Define_C.nexttimg = false;
						}
						if (Define_C.thirdtimg) {
							Log.i("第三张图地址", obj.getJSONObject("item").getString("url"));
							Define_C.thirdtimgid = obj.getJSONObject("item").getString("url");
							imageView_thirdadimg.setImageBitmap(photo);
							button_deletethirdimg.setVisibility(View.VISIBLE);
							Define_C.thirdtimg = false;
						}
						Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_SHORT).show();
					} else {
						Define_C.coveimg = false;
						Define_C.firstimg = false;
						Define_C.nexttimg = false;
						Define_C.thirdtimg = false;
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mUpImage = false;
			}

			@Override
			public void onFailure(Throwable ble) {

				Toast.makeText(getApplicationContext(), "上传失败！", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFinish() {
				super.onFinish();
				try {
					is.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Define_C.coveimg = false;
				Define_C.firstimg = false;
				Define_C.nexttimg = false;
				Define_C.thirdtimg = false;
			}
		};

		RequestParams params = new RequestParams();

		params.put("picture", is);
		// MgqRestClient.post(
		// "http://Android4.mgw.cc/MemImage/UploadPicture.aspx?"
		// + "&userid=" + obj.getString("UserID")
		// + "&format=.jpg", params, loginHandler);
		MgqRestClient.post(Define_C.mgw_url2 + "/MemImage/UploadPicture.aspx?" + "&userid=" + getSharedPreferences("mgw_data", 0).getString("mgw_userID", null) + "&format=.jpg", params, loginHandler);

	}

	// 确认保存的方法
	private void confirmData() {
		MgqDataHandler loginHandler = new MgqDataHandler(this, true, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json", obj.toString());
					if (obj.getInt("flag") == 0) {
						MyInfoFragment.shoudeflush = true;
						Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();
						finish();

					} else {
						// ToastUtil.showToastWithAlertPic(obj.getString("msg"));
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mUpImage = false;
			}

			@Override
			public void onFailure(Throwable ble) {
				Toast.makeText(getApplicationContext(), "保存失败！", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				Define_C.coveimgid = "";
				Define_C.firstimgid = "";
				Define_C.nexttimgid = "";
				Define_C.thirdtimgid = "";
			}
		};

		if (!Define_C.firstimgid.equals("")) {
			images = Define_C.firstimgid + ",";
		}
		if (!Define_C.nexttimgid.equals("")) {
			images = images + Define_C.nexttimgid + ",";
		}
		if (!Define_C.thirdtimgid.equals("")) {
			images = images + Define_C.thirdtimgid + ",";
		}
		if (images.equals("")) {
			images = ",";
		}
		RequestParams params = new RequestParams();
		// 广告保存的接口
		String type = "platinum.uploadad";
		Intent intent = getIntent();
		if (intent.getIntExtra("mod", 1) == 2) {
			String adid = intent.getStringExtra("adid");
			Log.i("===adid===", adid);
			params.put("adid", adid);
			// 广告修改后后保存的接口
			type = "platinum.modifyad";
		}

		params.add("type", type);
		params.add("userid", getSharedPreferences("mgw_data", 0).getString("mgw_userID", null));
		params.add("serial", getSharedPreferences("mgw_data", 0).getString("mgw_serial", null));
		params.add("title", et_title_ad.getText().toString().trim());
		params.add("face", Define_C.coveimgid);
		params.add("images", images.toString().substring(0, images.toString().length() - 1));
		params.add("desc", et_abstract_ad.getText().toString().trim());
		params.add("content", et_detail_ad.getText().toString().trim());
		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

	// 通过Uri得到bitmap对象
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {

			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

		} catch (FileNotFoundException e) {

			e.printStackTrace();

			return null;

		}

		return bitmap;

	}

	// 将bitmap对象转换为输入流
	private InputStream Bitmap2IS(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		return sbs;
	}

	// 获取内存状况
	private void displayBriefMemory() {
		final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		Log.i("系统剩余内存----->", "系统剩余内存:" + (info.availMem >> 10) + "k");
		Log.i("系统是否处于低内存运行----->", "系统是否处于低内存运行：" + info.lowMemory);
		Log.i("当系统剩余内存低于----->", "当系统剩余内存低于" + info.threshold + "时就看成低内存运行");
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		Log.i("onDestroy方法执行了", "广告activity以消亡。。。");
	}

	// 编辑广告前下载数据
	private void getadinfoData(boolean show, String adid) {

		MgqDataHandler loginHandler = new MgqDataHandler(this, show, false) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);

				try {
					JSONObject obj = new JSONObject(response);
					Log.e("json==", obj.toString());
					if (obj.getInt("flag") == 0) {
						JSONObject jsonObject = obj.getJSONObject("item");
						et_title_ad.setText(jsonObject.getString("title"));
						et_abstract_ad.setText(jsonObject.getString("abstract"));
						et_detail_ad.setText(jsonObject.getString("content"));
						String face = jsonObject.getString("face");
						Picasso.with(getApplicationContext()).load(face).into(imageView_adcover);
						// ImageLoaderHelper.displayImage(R.drawable.img_loading,
						// imageView_adcover, face);
						button_deletcover.setVisibility(View.VISIBLE);
						Define_C.coveimgid = face;
						String[] cimgs = jsonObject.getString("cimg").split(",");
						Log.i("==imgsize", cimgs.length + "");
						switch (cimgs.length) {
						case 1:
							if (cimgs[0].equals("")) {
								break;
							}
							Picasso.with(getApplicationContext()).load(cimgs[0]).into(imageView_firstadimg);
							// ImageLoaderHelper.displayImage(
							// R.drawable.img_loading,
							// imageView_firstadimg, cimgs[0]);
							Define_C.firstimgid = cimgs[0];
							button_deletefirstimg.setVisibility(View.VISIBLE);
							Log.i("==图一==", cimgs[0]);
							break;

						case 2:

							Log.i("==图一==", cimgs[0]);
							Picasso.with(getApplicationContext()).load(cimgs[0]).into(imageView_firstadimg);
							// ImageLoaderHelper.displayImage(
							// R.drawable.img_loading,
							// imageView_firstadimg, cimgs[0]);
							button_deletefirstimg.setVisibility(View.VISIBLE);
							Define_C.firstimgid = cimgs[0];
							Picasso.with(getApplicationContext()).load(cimgs[1]).into(imageView_nextadimg);
							// ImageLoaderHelper.displayImage(
							// R.drawable.img_loading,
							// imageView_nextadimg, cimgs[1]);
							button_deletenextimg.setVisibility(View.VISIBLE);
							Define_C.nexttimgid = cimgs[1];
							break;
						case 3:
							Log.i("==图一==", cimgs[0]);
							Log.i("==图二==", cimgs[1]);
							Log.i("==图三==", cimgs[2]);
							Picasso.with(getApplicationContext()).load(cimgs[0]).into(imageView_firstadimg);
							// ImageLoaderHelper.displayImage(
							// R.drawable.img_loading,
							// imageView_firstadimg, cimgs[0]);
							button_deletefirstimg.setVisibility(View.VISIBLE);
							Define_C.firstimgid = cimgs[0];
							Picasso.with(getApplicationContext()).load(cimgs[1]).into(imageView_nextadimg);
							// ImageLoaderHelper.displayImage(
							// R.drawable.img_loading,
							// imageView_nextadimg, cimgs[1]);
							button_deletenextimg.setVisibility(View.VISIBLE);
							Define_C.nexttimgid = cimgs[1];
							Picasso.with(getApplicationContext()).load(cimgs[2]).into(imageView_thirdadimg);
							// ImageLoaderHelper.displayImage(
							// R.drawable.img_loading,
							// imageView_thirdadimg, cimgs[2]);
							button_deletethirdimg.setVisibility(View.VISIBLE);
							Define_C.thirdtimgid = cimgs[2];
							break;
						}

					} else {
						Toast.makeText(getApplicationContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable ble) {

			}
		};
		RequestParams params = new RequestParams();
		params.put("type", "member.getadinfo");

		params.add("userid", getSharedPreferences("mgw_data", 0).getString("mgw_userID", null));
		params.add("serial", getSharedPreferences("mgw_data", 0).getString("mgw_serial", null));
		params.put("adid", adid);

		MgqRestClient.get(Define_C.mgw_url, params, loginHandler);

	}

}