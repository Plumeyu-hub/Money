package com.example.ui.login;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money.R;

public class RetrievePwActivity extends Activity {
	private EditText et_retrievepw_username, et_retrievepw_problem,
			et_retrievepw_answer;
	private TextView tv_repw;
	private Button btn_retrievepw_yes;
	private String username, problem, answer, usernamesql, passwordsql,
			problemsql, answersql;

	// 数据库
	private SQLiteDatabase db;// 数据库对象
	private Cursor cs;// 游标对象，用来报错查询返回的结果集

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrievepw);
		et_retrievepw_username = (EditText) this
				.findViewById(R.id.et_retrievepw_username);
		et_retrievepw_problem = (EditText) this
				.findViewById(R.id.et_retrievepw_problem);
		et_retrievepw_answer = (EditText) this
				.findViewById(R.id.et_retrievepw_answer);
		tv_repw = (TextView) this.findViewById(R.id.tv_repw);
		btn_retrievepw_yes = (Button) this
				.findViewById(R.id.btn_retrievepw_yes);

		// 数据库
		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);

		btn_retrievepw_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = et_retrievepw_username.getText().toString().trim();
				problem = et_retrievepw_problem.getText().toString().trim();
				answer = et_retrievepw_answer.getText().toString().trim();

				if (username == null || username.length() == 0
						|| problem == null || problem.length() == 0
						|| answer == null || answer.length() == 0) {
					Toast.makeText(RetrievePwActivity.this, "对不起，请填写完整的注册信息",
							Toast.LENGTH_LONG).show();
				} else {
					// 查找数据库是否存在相同的用户名
					cs = db.query("user", new String[] { "ic_username",
							"ic_password", "problem", "answer" }, "ic_username=?",
							new String[] { username }, null, null, null);
					if (cs != null) {
						while (cs.moveToNext()) {
							passwordsql = cs.getString(cs
									.getColumnIndex("ic_password"));
							problemsql = cs.getString(cs
									.getColumnIndex("problem"));
							answersql = cs.getString(cs
									.getColumnIndex("answer"));
						}
						if (problem.equals(problemsql)
								&& answer.equals(answersql)) {
							Toast.makeText(RetrievePwActivity.this, "已查询到您的密码",
									Toast.LENGTH_LONG).show();
							tv_repw.setText("您的密码为：" + passwordsql);
						} else {
							Toast.makeText(RetrievePwActivity.this,
									"您输入的密保信息有误", Toast.LENGTH_LONG).show();
							tv_repw.setText(" ");
						}
					} else {
						Toast.makeText(RetrievePwActivity.this, "未查询到该用户",
								Toast.LENGTH_LONG).show();
					}

				}
			}
		});

	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.retrievepw_left:
			finish();
			break;

		default:
			break;
		}
	}
}