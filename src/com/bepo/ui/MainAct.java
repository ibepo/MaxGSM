package com.bepo.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.bepo.R;
import com.bepo.adapter.MyGridAdapter;
import com.bepo.core.BaseAct;
import com.bepo.view.SplineChart01View;
import com.finddreams.graygridView.MyGridView;

public class MainAct extends BaseAct {
	private MyGridView gridview;
	private RelativeLayout rlChart;
	SplineChart01View chart = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_jiugongge);
		initView();
	}

	private void initView() {
		rlChart = (RelativeLayout) findViewById(R.id.rlChart);
		chart = (SplineChart01View) findViewById(R.id.SplineChart01View);
		gridview = (MyGridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyGridAdapter(this));

	}

}
