package com.example.money;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.about_left:
			finish();
			break;

		default:
			break;
		}
	}
}
