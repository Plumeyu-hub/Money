package com.example.ui.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.money.R;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.aboutleft_tv:
			finish();
			break;

		default:
			break;
		}
	}
}
