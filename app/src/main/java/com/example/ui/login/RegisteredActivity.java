package com.example.ui.login;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.money.R;

public class RegisteredActivity extends Activity {
	private EditText et_re_username, et_re_password, et_re_passwordtwo,
			et_re_problem, et_re_answer;
	private Button btn_registered;
	// 数据库
	private SQLiteDatabase db;// 数据库对象
	private ContentValues cv;// 存储工具栏
	private int num = 0;
	private Cursor cs;// 游标对象，用来报错查询返回的结果集

	private String username, password, passwordtwo, usernamesql, problem,
			answer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registered);
		et_re_username = (EditText) findViewById(R.id.et_registered_username);
		et_re_password = (EditText) findViewById(R.id.et_registered_password);
		et_re_passwordtwo = (EditText) findViewById(R.id.et_registered_passwordtwo);
		et_re_problem = (EditText) findViewById(R.id.et_registered_problem);
		et_re_answer = (EditText) findViewById(R.id.et_registered_answer);
		btn_registered = (Button) this.findViewById(R.id.btn_re_registered);

		// 数据库
		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		//this.deleteDatabase("data.db");
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists user (userid integer primary key,username text,password text,problem text,answer text)");

		btn_registered.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				username = et_re_username.getText().toString().trim();
				password = et_re_password.getText().toString().trim();
				passwordtwo = et_re_passwordtwo.getText().toString().trim();
				problem = et_re_problem.getText().toString().trim();
				answer = et_re_answer.getText().toString().trim();

				if (username == null || username.length() == 0
						|| password == null || password.length() == 0
						|| passwordtwo == null || passwordtwo.length() == 0
						|| problem == null || problem.length() == 0
						|| answer == null || answer.length() == 0) {
					Toast.makeText(RegisteredActivity.this, "对不起，请填写完整的注册信息",
							Toast.LENGTH_LONG).show();
				} else if (password.equals(passwordtwo) == false) {
					Toast.makeText(RegisteredActivity.this,
							"对不起，您输入的密码和确认密码不一致", Toast.LENGTH_LONG).show();

				} else {

					// 查找数据库是否存在相同的用户名
					cs = db.query("user", new String[] { "ic_username" },
							"ic_username like ? ", new String[] { username },
							null, null, null);

					if (cs != null) {
						while (cs.moveToNext()) {
							usernamesql = cs.getString(cs
									.getColumnIndex("ic_username"));
						}
					}

					if (username.equals(usernamesql)) {
						Toast.makeText(RegisteredActivity.this, "您输入的用户名已存在",
								Toast.LENGTH_LONG).show();
					} else {
						// 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
						cv = new ContentValues();
						cv.put("ic_password", password);
						cv.put("problem", problem);
						cv.put("answer", answer);
						cv.put("ic_username", username);
						// 插入数据，成功返回当前行号，失败返回0
						num = (int) db.insert("user", null, cv);
						if (num > 0) {
							Toast.makeText(RegisteredActivity.this,
									"用户注册成功" + num, Toast.LENGTH_SHORT).show();

							finish();
						} else {
							Toast.makeText(RegisteredActivity.this,
									"用户注册失败" + num, Toast.LENGTH_SHORT).show();
						}
					}

				}
			}
		});

	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.registered_left:
			finish();
			break;

		default:
			break;
		}
	}
}