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
	private RelativeLayout rlEventType;// �¼�����ѡ��
	private RelativeLayout rlLocation;// ��ͼλ��ѡ��
	private LinearLayout linDetail;// ���������飬��ʼ��ʱ����
	private LinearLayout linImage;//
	private ImageView ivPhoto;
	private SelectPicPop mSelectPicPop;
	private static SelectEventTypePop mSelectEventTypePop;
	private TextView tvImageDescribe, tvLocation;
	private static TextView tvEventType;

	public static String eventTypeStr = "����ѡ��";
	public static String address;
//	public static String  ;
	Bitmap Opic;
	File tempFile;

	// ��λ���
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation;// �����ж϶�λ��ʱ
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
		handler.postDelayed(this, 12000);// ���ó���12�뻹û�ж�λ����ֹͣ��λ

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
			// ������Ƭ�洢·�����еĻ��ͷ��ص�intent ��Ϊnull,��ʱ���ܴ�����ȡ�����ˣ�
			// ��Ҫ���Լ������·��ȡ����
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
		stopLocation();// ֹͣ��λ
	}

	/**
	 * ���ٶ�λ
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	/**
	 * ��϶�λ�ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			this.aMapLocation = location;// �жϳ�ʱ����
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("��λ�ɹ�:(" + geoLng + "," + geoLat + ")" + "\n��    ��    :" + location.getAccuracy() + "��"
					+ "\n��λ��ʽ:" + location.getProvider() + "\n��λʱ��:" + AMapUtil.convertToTime(location.getTime())
					+ "\n���б���:" + cityCode + "\nλ������:" + desc + "\nʡ:" + location.getProvince() + "\n��:"
					+ location.getCity() + "\n��(��):" + location.getDistrict() + "\n�������:" + location.getAdCode());

			tvLocation.setText(desc);
		}
	}

	@Override
	public void run() {
		if (aMapLocation == null) {
			tvLocation.setText("12���ڻ�û�ж�λ�ɹ���ֹͣ��λ");
			stopLocation();// ���ٵ���λ
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
