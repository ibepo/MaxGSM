package com.bepo.contradiction;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.bepo.R;
import com.bepo.core.PathConfig;
import com.bepo.pickpic.ImgFileListActivity;
import com.bepo.utils.AMapUtil;
import com.bepo.utils.BitmapUtil;
import com.bepo.view.MapLocation;
import com.bepo.view.SelectEventTypePop;
import com.bepo.view.SelectPicPop;
import com.kyleduo.switchbutton.SwitchButton;

public class EventAccept extends Activity implements OnClickListener, AMapLocationListener, Runnable {

	private ScrollView scrollView;
	private SwitchButton mSwitchButton;
	private RelativeLayout rlEventType;// 事件类型选择
	private RelativeLayout rlLocation;// 地图位置选择
	private LinearLayout linDetail;// 诉讼人详情，初始化时隐藏
	private LinearLayout linImage;//
	private ImageView ivPhoto;
	private SelectPicPop mSelectPicPop;
	private static SelectEventTypePop mSelectEventTypePop;
	private TextView tvImageDescribe, tvLocation;
	private static TextView tvEventType;

	public static String eventTypeStr = "请点击选择";
	public static String address;
//	public static String  ;
	Bitmap Opic;
	File tempFile;

	// 定位相关
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contradiction_event_accept);
		initView();
		initLocation();
	}

	private void initLocation() {
		aMapLocManager = LocationManagerProxy.getInstance(this);
		aMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10, this);
		handler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位

	}

	private void initView() {
		tempFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

		scrollView = (ScrollView) this.findViewById(R.id.scrollView);
		ivPhoto = (ImageView) this.findViewById(R.id.ivPhoto);
		linDetail = (LinearLayout) this.findViewById(R.id.linDetail);
		tvImageDescribe = (TextView) this.findViewById(R.id.tvImageDescribe);
		tvEventType = (TextView) this.findViewById(R.id.tvEventType);

		tvLocation = (TextView) this.findViewById(R.id.tvLocation);

		rlEventType = (RelativeLayout) this.findViewById(R.id.rlEventType);
		rlEventType.setOnClickListener(this);

		rlLocation = (RelativeLayout) this.findViewById(R.id.rlLocation);
		rlLocation.setOnClickListener(this);

		linImage = (LinearLayout) this.findViewById(R.id.linImage);
		linImage.setOnClickListener(this);

		mSwitchButton = (SwitchButton) this.findViewById(R.id.switch_detail);
		mSwitchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				linDetail.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PathConfig.CAMERA && resultCode == RESULT_OK) {
			// 设置照片存储路径后，有的机型返回的intent 就为null,这时不能从里面取数据了，
			// 需要从自己定义的路径取数据
			// Bitmap thumbnail = data.getParcelableExtra("data");

			try {
				Opic = BitmapUtil.reSizePic(tempFile.getAbsolutePath(), 480, 640);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ivPhoto.setImageBitmap(Opic);
			tvImageDescribe.setVisibility(View.INVISIBLE);
			linImage.setClickable(false);
		} else if (requestCode == PathConfig.SELECT && resultCode == RESULT_OK) {

		} else if (requestCode == PathConfig.LOCATION) {
			address = data.getExtras().getString("address");
			tvLocation.setText(address);

		}
	}

	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			mSelectPicPop.dismiss();

			switch (v.getId()) {

			case R.id.btn_take_photo:
				Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));

				if (CameraIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(CameraIntent, PathConfig.CAMERA);
				}

				break;

			case R.id.btn_pick_photo:

				Intent intent = new Intent(EventAccept.this, ImgFileListActivity.class);
				startActivity(intent);

				break;

			default:
				break;
			}

		}

	};

	public static void changeEventType() {
		if (!mSelectEventTypePop.isShowing()) {
			tvEventType.setText(eventTypeStr);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linImage:

			mSelectPicPop = new SelectPicPop(EventAccept.this, itemsOnClick);
			mSelectPicPop.showAtLocation(scrollView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;

		case R.id.rlEventType:

			mSelectEventTypePop = new SelectEventTypePop(EventAccept.this, null);
			mSelectEventTypePop.showAsDropDown(rlEventType);

			break;

		case R.id.rlLocation:
			Intent intent = new Intent(EventAccept.this, MapLocation.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();// 停止定位
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	/**
	 * 混合定位回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			this.aMapLocation = location;// 判断超时机制
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("定位成功:(" + geoLng + "," + geoLat + ")" + "\n精    度    :" + location.getAccuracy() + "米"
					+ "\n定位方式:" + location.getProvider() + "\n定位时间:" + AMapUtil.convertToTime(location.getTime())
					+ "\n城市编码:" + cityCode + "\n位置描述:" + desc + "\n省:" + location.getProvince() + "\n市:"
					+ location.getCity() + "\n区(县):" + location.getDistrict() + "\n区域编码:" + location.getAdCode());

			tvLocation.setText(desc);
		}
	}

	@Override
	public void run() {
		if (aMapLocation == null) {
			tvLocation.setText("12秒内还没有定位成功，停止定位");
			stopLocation();// 销毁掉定位
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

}
