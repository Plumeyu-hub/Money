package com.snxun.book.ui.my;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.snxun.book.R;
import com.snxun.book.ui.login.LoginActivity;

public class MyActivity extends Activity {
	Button btn_modifypw, btn_exituser;

	// sp
	private SharedPreferences sp_login, sp_user;
	private SharedPreferences.Editor editor_login, editor_user;
	private Boolean bool_login;
	private int userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);

		btn_modifypw = (Button) findViewById(R.id.modifypw_btn);
		btn_exituser = (Button) findViewById(R.id.exituser_btn);

		sp_login = this.getSharedPreferences("userLogin", MODE_PRIVATE);
		editor_login = sp_login.edit();

		sp_user = this.getSharedPreferences("user", MODE_PRIVATE);
		editor_user = sp_user.edit();

		btn_modifypw.setOnClickListener(new View.OnClickListener() {
			public void onClick(View source) {
				Intent i = new Intent(MyActivity.this, ModifyPasswordActivity.class);
				startActivity(i);
			}
		});
		btn_exituser.setOnClickListener(new View.OnClickListener() {
			public void onClick(View source) {
				showNormalDialog();
			}
		});
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.mineleft_tv:
			finish();
			break;

		default:
			break;
		}
	}

	// 弹出一个普通对话框
	private void showNormalDialog() {
		// [1]构造对话框的实例
		Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("是否退出此账号？");
		// [2]设置确定和取消按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				bool_login = false;
				editor_login.putBoolean("isLoad", bool_login);
				editor_login.commit();

				editor_user.clear();
				editor_user.commit();

				Intent i1 = new Intent(MyActivity.this, LoginActivity.class);
				startActivity(i1);
			}

			private int deleteDatabase(String string) {
				// TODO Auto-generated method stub
				return 0;
			}

			private int deleteDatabase() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		// [3]展示对话框 和toast一样 一定要记得show出来
		builder.show();

	}
}