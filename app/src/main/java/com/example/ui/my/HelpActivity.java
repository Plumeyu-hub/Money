package com.example.ui.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.money.R;

public class HelpActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.help_left:
			finish();
			break;

		default:
			break;
		}
	}
}