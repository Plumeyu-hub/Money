package com.example.money;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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