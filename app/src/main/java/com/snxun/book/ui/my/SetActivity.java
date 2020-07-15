package com.snxun.book.ui.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.snxun.book.R;

public class SetActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.setleft_tv:
			finish();
			break;

		default:
			break;
		}
	}
}
