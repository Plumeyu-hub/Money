package com.example.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.money.R;

public class SubmitRemarkActivity extends Activity {
	EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_remark);
		et = (EditText) findViewById(R.id.add_in_remark_edit);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.submit_remark_left_tv:
			finish();
			break;
		case R.id.submit_remark_yes_btn:// 把编辑框的内容返回上个界面
			String ai_str = et.getText().toString().trim();
			if (TextUtils.isEmpty(ai_str)) {
				Toast.makeText(this, "编辑框内容不能为空", Toast.LENGTH_SHORT).show();

			} else {
				// 第二步：用setResult()方法返回数据
				Intent i = new Intent();// Intent不要设置跳转的界面
				i.putExtra("submit_remark", ai_str);
				this.setResult(1, i);
				finish();
			}
		default:
			break;
		}
	}
}
