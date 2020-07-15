package com.snxun.book.ui.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.snxun.book.R;

public class HelpActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.helpleft_tv:
			finish();
			break;

		default:
			break;
		}
	}
}