package com.example.ui.my;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.money.R;

public class ModifyPwActivity extends Activity {
	String pwone, pwtow;
	EditText et_modifypw_password, et_modifypw_passwordtwo;
	Button btn_modifypw_yes;

	// 操作用户名
	private SharedPreferences spuser;
	private SharedPreferences.Editor editoruser;
	private int userid;

	// 数据库
	private SQLiteDatabase db;// 数据库对象
	private ContentValues cv;// 存储工具栏
	private int num = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifypw);

		// 操作用户名
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			userid = spuser.getInt("userid", 0);
		}
		// 数据库
		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);

		et_modifypw_password = (EditText) findViewById(R.id.et_modifypw_password);
		et_modifypw_passwordtwo = (EditText) findViewById(R.id.et_modifypw_passwordtwo);
		btn_modifypw_yes = (Button) findViewById(R.id.btn_modifypw_yes);
		btn_modifypw_yes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View source) {

				pwone = et_modifypw_password.getText().toString().trim();
				pwtow = et_modifypw_passwordtwo.getText().toString().trim();
				showNormalDialog();

			}
		});

	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.modifypw_left:
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
		builder.setMessage("是否确认此修改？");
		// [2]设置确定和取消按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (pwone == null || pwone.length() == 0 || pwtow == null
						|| pwtow.length() == 0) {
					Toast.makeText(ModifyPwActivity.this,
							"对不起，您输入的密码或确认密码不能为空", Toast.LENGTH_LONG).show();
				} else if (pwone.equals(pwtow) == false) {
					Toast.makeText(ModifyPwActivity.this, "对不起，您输入的密码和确认密码不一致",
							Toast.LENGTH_LONG).show();
				} else {
					String uid = String.valueOf(userid);
					// 修改数据
					// 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
					cv = new ContentValues();
					cv.put("password", pwone);
					// 修改数据，返回修改成功的行数，失败则返回0
					num = db.update("user", cv, "userid=?", new String[] { uid
							+ "" });
					if (num > 0) {
						Toast.makeText(ModifyPwActivity.this, "修改成功",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ModifyPwActivity.this, "修改失败",
								Toast.LENGTH_SHORT).show();
					}

				}
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
