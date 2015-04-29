package com.bepo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.bepo.R;

public class SplashAct extends Activity {

	private static final int delay_length = 2000;// 延时启动下一个activity的时间长短

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				// Intent mainIntent = new Intent(WelcomeAct.this, Main.class);
				// WelcomeAct.this.startActivity(mainIntent);
				// WelcomeAct.this.finish();

				Intent mainIntent = new Intent(SplashAct.this, LoginAct.class);
				SplashAct.this.startActivity(mainIntent);
				SplashAct.this.finish();

			}

		}, delay_length);

	}

	@Override
	public void finish() {
		super.finish();
	}

}
