package com.bepo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bepo.R;
import com.bepo.contradiction.EventAccept;
import com.finddreams.graygridView.BaseViewHolder;

public class MyGridAdapter extends BaseAdapter {
	private Context mContext;

	public String[] img_text = { "事件管理", "基础信息", "信息共享", "巡查轨迹", "统计分析", "民情日志", "居家养老", "考核评估", "其他" };
	public int[] imgs = { R.drawable.home_event, R.drawable.home_jichuxinxi, R.drawable.home_xinxigongxiang,
			R.drawable.home_xunchaguiji, R.drawable.home_tongjifenxi, R.drawable.home_mingqingrizhi,
			R.drawable.home_jujiayanglao, R.drawable.home_dahonghua, R.drawable.home_qita };

	public MyGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return img_text.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);
		tv.setText(img_text[position]);

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switch (position) {
				case 0:
					Intent mainIntent = new Intent(mContext, EventAccept.class);
					mContext.startActivity(mainIntent);
					break;

				default:
					break;
				}

			}
		});
		return convertView;
	}
}
