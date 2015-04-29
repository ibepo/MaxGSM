package com.bepo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bepo.R;
import com.bepo.bean.ResultBean;
import com.bepo.core.ApplicationController;
import com.bepo.core.BaseAct;
import com.bepo.core.PathConfig;
import com.bepo.utils.MyTextUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class LoginAct extends BaseAct implements OnClickListener {
	Context mContext = this;
	TextView tvlogin;
	EditText etUser, etPsw;
	RelativeLayout rlLogin;
	LinearLayout linMobile, linPWD;
	ProgressBar progressBar;

	ResultBean mResultBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		initView();
	}

	private void initData(String user, String pwd) {
		String url = PathConfig.ADDRESS + "/gsm/sys/rusers/Userlogin?NAME=" + user + "&PWD=" + pwd;
		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				String jsondata = response.toString();
				mResultBean = JSON.parseObject(jsondata, ResultBean.class);

				if (mResultBean.getStatus().equals("true")) {
					goHomePage();
				} else {
					progressBar.setVisibility(View.GONE);
					tvlogin.setText(mResultBean.getInfo());
					YoYo.with(Techniques.BounceInUp).duration(700).playOn(findViewById(R.id.tvlogin));
					new Handler().postDelayed(new Runnable() {
						public void run() {
							initLogin();
						}
					}, 1000);
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressBar.setVisibility(View.GONE);
				tvlogin.setText("���ʷ�����ʧ�ܣ��Ժ�����");
				YoYo.with(Techniques.BounceInUp).duration(700).playOn(findViewById(R.id.tvlogin));
				new Handler().postDelayed(new Runnable() {
					public void run() {
						initLogin();
					}
				}, 1000);

			}
		});
		ApplicationController.getInstance().addToRequestQueue(stringRequest);

	}

	private void initView() {

		etPsw = (EditText) this.findViewById(R.id.etPsw);
		etUser = (EditText) this.findViewById(R.id.etUser);

		linMobile = (LinearLayout) this.findViewById(R.id.linMobile);
		linPWD = (LinearLayout) this.findViewById(R.id.linPWD);
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		rlLogin = (RelativeLayout) this.findViewById(R.id.rlLogin);
		tvlogin = (TextView) this.findViewById(R.id.tvlogin);
		rlLogin.setOnClickListener(this);

	}

	private void goHomePage() {
		progressBar.setVisibility(View.GONE);
		tvlogin.setText("��ӭ����   ;-)");
		YoYo.with(Techniques.BounceInDown).duration(500).playOn(findViewById(R.id.tvlogin));
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(LoginAct.this, MainAct.class);
				LoginAct.this.startActivity(mainIntent);
				LoginAct.this.finish();
			}

		}, 700);
	}

	void initLogin() {
		tvlogin.setText("��        ¼");
		YoYo.with(Techniques.BounceInDown).duration(700).playOn(findViewById(R.id.tvlogin));
	}

	@Override
	public void onClick(View V) {
		switch (V.getId()) {
		case R.id.rlLogin:

			String strUser = etUser.getText().toString().trim();
			String strPwd = etPsw.getText().toString().trim();

			if (!MyTextUtils.isEmpty(strPwd) && !MyTextUtils.isEmpty(strUser)) {
				tvlogin.setText("���ڵ�¼...");
				progressBar.setVisibility(View.VISIBLE);
				YoYo.with(Techniques.BounceInUp).duration(700).playOn(findViewById(R.id.tvlogin));
				initData(strUser, strPwd);
			} else {

				if (MyTextUtils.isEmpty(strUser)) {
					// YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.linMobile));
					// ToastUtils.showSuperToastAlert(mContext, "�ֻ��Ų���Ϊ��");
					tvlogin.setText("�˺Ų���Ϊ��");
					YoYo.with(Techniques.BounceInUp).duration(700).playOn(findViewById(R.id.tvlogin));
					new Handler().postDelayed(new Runnable() {
						public void run() {
							initLogin();
						}
					}, 1000);

				} else if (MyTextUtils.isEmpty(strPwd)) {
					// YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.linPWD));
					// ToastUtils.showSuperToastAlert(mContext, "���벻��Ϊ��");
					tvlogin.setText("���벻��Ϊ��");
					YoYo.with(Techniques.BounceInUp).duration(700).playOn(findViewById(R.id.tvlogin));
					new Handler().postDelayed(new Runnable() {
						public void run() {
							initLogin();
						}
					}, 1000);
				}
			}
		}
	}
}
