package com.snxun.book.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

	private long mExitTime;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_welcome;
	}


	@Override
	protected void findViews() {

	}

	@Override
	protected void initData() {
		super.initData();
		handler.sendEmptyMessageDelayed(0, 3000);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case 0:
					Intent i1 = new Intent(WelcomeActivity.this, LoginActivity.class);
					startActivity(i1);
					finish();
					break;

				default:
					break;
			}
		}
	};

	// 对返回键进行监听,如果是普通的activity则用onKeyDown方法;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			mExitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

}