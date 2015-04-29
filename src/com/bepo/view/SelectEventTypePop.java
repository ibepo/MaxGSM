package com.bepo.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bepo.R;
import com.bepo.adapter.FirstEventTypeAdapter;
import com.bepo.adapter.SecondEventTypeAdapter;
import com.bepo.bean.EventTypeBean;
import com.bepo.contradiction.EventAccept;
import com.bepo.core.ApplicationController;

public class SelectEventTypePop extends PopupWindow {

	Context context;
	private View View;
	private ListView lvFirstType, lvSecondType;

	private FirstEventTypeAdapter<String> firstEventTypeAdapter;
	private SecondEventTypeAdapter<String> secondEventTypeAdapter;

	public static ArrayList<EventTypeBean> metaData = new ArrayList<EventTypeBean>();// Ԫ����
	public static ArrayList<EventTypeBean> firstDataList = new ArrayList<EventTypeBean>();// һ���˵�����
	public static ArrayList<EventTypeBean> secondDataList = new ArrayList<EventTypeBean>();// �����˵�����

	public static ArrayList<String> firstData = new ArrayList<String>();// һ���˵�����
	public static ArrayList<String> secondData = new ArrayList<String>();// �����˵�����

	@SuppressWarnings("deprecation")
	public SelectEventTypePop(Activity context, OnClickListener itemsOnClick) {
		super(context);
		initData();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View = inflater.inflate(R.layout.event_type_dialog, null);

		// һ���˵�
		lvFirstType = (ListView) View.findViewById(R.id.lvFirstType);
		firstEventTypeAdapter = new FirstEventTypeAdapter<String>(firstData, context);
		lvFirstType.setAdapter(firstEventTypeAdapter);
		lvFirstType.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, android.view.View arg1, int position, long arg3) {
				FirstEventTypeAdapter.setSelectedPosition(position);
				firstEventTypeAdapter.notifyDataSetChanged();
				secondDataMapper(firstDataList.get(position).getCODE());
				secondEventTypeAdapter.notifyDataSetChanged();
			}
		});

		// �����˵�
		lvSecondType = (ListView) View.findViewById(R.id.lvSecondType);
		secondEventTypeAdapter = new SecondEventTypeAdapter<String>(secondData, context);
		lvSecondType.setAdapter(secondEventTypeAdapter);

		lvSecondType.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, android.view.View arg1, int position, long arg3) {
				EventAccept.eventTypeStr = secondData.get(position);
				dismiss();
				EventAccept.changeEventType();
			}
		});

		// ����SelectPicPopupWindow��View
		this.setContentView(View);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.FILL_PARENT);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.SelectEventTypeStyle);
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		// ColorDrawable dw = new ColorDrawable(0xb0000000);
		ColorDrawable dw = new ColorDrawable();
		// ����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		// mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		View.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = View.findViewById(R.id.event_type_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	

	}

	// һ���˵����ݴ���
	private void firstDataMapper() {
		firstData.clear();
		firstDataList.clear();
		for (EventTypeBean temp : metaData) {
			if (temp.getPARENT_CODE().equals("0000")) {
				firstData.add(temp.getNAME_C());
				firstDataList.add(temp);

			}
		}

		if (secondData.isEmpty()) {
			for (EventTypeBean temp : metaData) {
				if (temp.getPARENT_CODE().equals(firstDataList.get(0).getCODE())) {
					secondData.add(temp.getNAME_C());
				}

			}

		}
		firstEventTypeAdapter.notifyDataSetChanged();
		secondEventTypeAdapter.notifyDataSetChanged();
	};

	// �����˵����ݴ���
	public static void secondDataMapper(String firstStr) {
		secondData.clear();
		for (EventTypeBean temp : metaData) {
			if (temp.getPARENT_CODE().equals(firstStr)) {
				secondData.add(temp.getNAME_C());
			}

		}

	}

	private void initData() {
		metaData.clear();
		String url = "http://192.168.123.26:8080/gsm/sys/sysdic/queryManageTree?name_dic=CODE_EVENT_TYPE";
		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				String jsondata = response.toString();
				metaData = (ArrayList<EventTypeBean>) JSON.parseArray(jsondata, EventTypeBean.class);
				firstDataMapper();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});
		ApplicationController.getInstance().addToRequestQueue(stringRequest);

	}
}
