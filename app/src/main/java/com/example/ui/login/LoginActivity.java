package com.example.ui.login;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.money.MainActivity;
import com.example.money.R;

public class LoginActivity extends Activity {
	private EditText et_username, et_password;
	private TextView tv_login, tv_login_forgetpw;
	private String usernamesql, passwordsql;
	private int useridsql;
	// 数据库
	private SQLiteDatabase db;// 数据库对象
	// private ContentValues cv;// 存储工具栏
	// private int num = 0;
	private Cursor cs;// 游标对象，用来报错查询返回的结果集
	// 判断登录状态
	private SharedPreferences sp_login, sp_user;
	private Boolean bool_login = false;
	private SharedPreferences.Editor editor_login, editor_user;

	// 定时器
	AlarmManager manager;// 定时服务
	PendingIntent pIntent;// 延时意图

	// 登录进度条
	private ProgressDialog Pdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		et_username = (EditText) this.findViewById(R.id.et_login_username);
		et_password = (EditText) this.findViewById(R.id.et_login_password);
		tv_login = (TextView) this.findViewById(R.id.tv_login);
		tv_login_forgetpw = (TextView) this
				.findViewById(R.id.tv_login_forgetpw);

		// sharedPerference sp=this.getSharedPerference();
		// boolean bool=sp.getBoolean("isLoad",false);
		// if(bool==false){
		//
		// }else{
		// 定时器
		// }
		// 判断登录状态
		sp_login = this.getSharedPreferences("userLogin", MODE_PRIVATE);
		editor_login = sp_login.edit();
		bool_login = sp_login.getBoolean("isLoad", false);
		// 存储用户名
		sp_user = this.getSharedPreferences("user", MODE_PRIVATE);
		editor_user = sp_user.edit();
		if (bool_login == false) {// 未登录

// 数据库
			// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
			db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);
			db.execSQL("create table if not exists user (userid integer primary key,username text,password text,problem text,answer text)");

			tv_login.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String username = et_username.getText().toString().trim();
					String password = et_password.getText().toString().trim();
					// 判断字符串为空
					// if(zhanghao==null || zhanghao.equals("")){
					if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
						Toast.makeText(LoginActivity.this, "账号或者密码为空",
								Toast.LENGTH_LONG).show();
					} else {
						// 查找数据库是否存在相同的用户名
						cs = db.query("user", new String[] { "userid", "username",
										"password" }, "username=?",
								new String[] { username }, null, null, null);

						if (cs != null) {
							while (cs.moveToNext()) {
								usernamesql = cs.getString(cs
										.getColumnIndex("username"));
								passwordsql = cs.getString(cs
										.getColumnIndex("password"));
								useridsql = cs.getInt(cs.getColumnIndex("userid"));
							}

							if (username.equals(usernamesql)
									&& password.equals(passwordsql)) {
								Toast.makeText(LoginActivity.this, "登录成功",
										Toast.LENGTH_LONG).show();
								bool_login = true;
								editor_login.putBoolean("isLoad", bool_login);
								editor_login.commit();

								editor_user.clear();
								editor_user.putString("username", usernamesql);
								// System.out.println(usernamesql);
								editor_user.putInt("userid", useridsql);
								// System.out.println(useridsql);
								editor_user.commit();

								Intent i = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivity(i);
								finish();
							} else {
								Toast.makeText(LoginActivity.this, "您输入的密码有误",
										Toast.LENGTH_LONG).show();
							}

						} else {
							Toast.makeText(LoginActivity.this, "当前账号不存在",
									Toast.LENGTH_LONG).show();
						}

					}
				}
			});

		} else {// 登录
				// 进度条
			if (Pdialog == null) {
				Pdialog = new ProgressDialog(this);// 创建了圆形进度条对话框
				Pdialog.setMessage("登录中。。。");
				Pdialog.setCancelable(false);// 设置点击返回按钮无效
			}
			Pdialog.show();
			manager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
			Intent i = new Intent(this, MainActivity.class);
			pIntent = PendingIntent.getActivity(this, 0, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			// 设置一次闹钟
			manager.set(AlarmManager.RTC_WAKEUP, 1 * 1000, pIntent);
			Pdialog.dismiss();
			//finish();

		}


	}

	public void click(View v) {
		switch (v.getId()) {

		case R.id.tv_login_registered:
			Intent i1 = new Intent(this, RegisteredActivity.class);
			startActivity(i1);
			break;
		case R.id.tv_login_forgetpw:
			Intent i2 = new Intent(this, RetrievePwActivity.class);
			startActivity(i2);
			break;

		default:
			break;
		}
	}
	// /**
	// * 此方法必须重写，以决绝退出activity时 dialog未dismiss而报错的bug
	// */
	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// try{
	// myDialog.dismiss();
	// }catch (Exception e) {
	// System.out.println("myDialog取消，失败！");
	// // TODO: handle exception
	// }
	// super.onDestroy();
	// }

}