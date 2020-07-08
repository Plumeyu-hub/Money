package com.example.money;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ui.account.AddActivity;
import com.example.ui.account.DetailsActivity;
import com.example.ui.account.GraphActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityGroup {

	SharedPreferences sp;

	Intent i1, i2;
	LocalActivityManager manager;
	FrameLayout frame;
	View v1, v2;
	List<LinearLayout> list_lin;
	List<Integer> list_img;// 图片列表
	List<Integer> list_tv;// 文字列表

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sp = getSharedPreferences("data", MODE_PRIVATE);
		//MODE_APPEND
		list_lin = new ArrayList<LinearLayout>();
		list_lin.add((LinearLayout) findViewById(R.id.details_lin));
		list_lin.add((LinearLayout) findViewById(R.id.graph_lin));

		list_img = new ArrayList<Integer>();
		list_img.add(R.id.detailsimg_tv);
		list_img.add(R.id.graphimg_tv);

		list_tv = new ArrayList<Integer>();
		list_tv.add(R.id.detailstxt_tv);
		list_tv.add(R.id.graphtxt_tv);

		manager = this.getLocalActivityManager();
		frame = (FrameLayout) this.findViewById(R.id.main_frame);

		i1 = new Intent(this, DetailsActivity.class);
		i2 = new Intent(this, GraphActivity.class);

		v1 = manager.startActivity("第一页", i1).getDecorView();// 把Activity转换成视图
		v2 = manager.startActivity("第二页", i2).getDecorView();// 把Activity转换成视图

		frame.addView(v2);
		frame.addView(v1);

		setBackgroup(R.id.details_lin);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.details_lin:
			setBackgroup(v.getId());
			frame.bringChildToFront(v1);// 把v1调到前台
			frame.invalidate();// 刷新
			break;
		case R.id.graph_lin:
			setBackgroup(v.getId());
			frame.bringChildToFront(v2);// 把v2调到前台
			frame.invalidate();// 刷新
			break;
		case R.id.addaccount_lin:
			Intent i11 = new Intent(MainActivity.this, AddActivity.class);
			startActivity(i11);
			break;

		default:
			break;
		}

	}

	// int[] imgs_yes = { R.drawable.ic_details_sel, R.drawable.ic_graph_sel };
	// int[] imgs_no = { R.drawable.ic_details_nor, R.drawable.ic_graph_nor };

	int[] imgs = { R.drawable.ic_details_nor, R.drawable.ic_details_sel,
			R.drawable.ic_graph_nor, R.drawable.ic_graph_sel};

	public void setBackgroup(int id) {
		int n = 0;
		LinearLayout lin = null;
		for (int i = 0; i < list_lin.size(); i++) {
			lin = list_lin.get(i);
			if (id == lin.getId()) {
				n = i;
				break;
			}
		}

		for (int i = 0; i < list_img.size(); i++) {
			TextView tv_img = (TextView) findViewById(list_img.get(i));
			TextView tv = (TextView) findViewById(list_tv.get(i));
			if (i == n) {
				tv_img.setBackgroundResource(imgs[i * 2 + 1]);
				//tv.setTextColor(Color.parseColor("#FF6B6A"));
				tv.setTextColor(getResources().getColor(R.color.color_FF6B6A));
			} else {
				tv_img.setBackgroundResource(imgs[i * 2]);
				//tv.setTextColor(Color.parseColor("#707070"));
				tv.setTextColor(getResources().getColor(R.color.color_707070));
			}
		}

	}

}
