package com.example.money;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SetActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.set_left:
			finish();
			break;

		default:
			break;
		}
	}
}
