package com.example.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.money.R;

public class AddInTextActivity extends Activity {
	EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_addaccountincaltext);
		et = (EditText) findViewById(R.id.et_addincaltext);
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.addincaltext_left:
			finish();
			break;
		case R.id.bt_addincaltext:// 把编辑框的内容返回上个界面
			String ai_str = et.getText().toString().trim();
			if (TextUtils.isEmpty(ai_str)) {
				Toast.makeText(this, "编辑框内容不能为空", Toast.LENGTH_SHORT).show();

			} else {
				// 第二步：用setResult()方法返回数据
				Intent i = new Intent();// Intent不要设置跳转的界面
				i.putExtra("ai_str", ai_str);
				this.setResult(1, i);
				finish();
			}
		default:
			break;
		}
	}
}
