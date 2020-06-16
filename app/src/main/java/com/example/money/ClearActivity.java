package com.example.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ClearActivity extends Activity {
	private Button btn_clear;
	private SQLiteDatabase DB;
	// private Cursor cs;// 游标对象，用来报错查询返回的结果集
	// 操作用户名
	private SharedPreferences spuser;
	private SharedPreferences.Editor editoruser;
	private int userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clear);

		// 操作用户名
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			userid = spuser.getInt("userid", 0);
		}

		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		DB = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);

		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View source) {
				showNormalDialog();
			}
		});
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.clear_left:
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
		builder.setMessage("是否清除该账号所有的数据库记录？");
		// [2]设置确定和取消按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 删除指定数据
				int aonum, ainum;
				String uid = String.valueOf(userid);
				// 删除数据，成功返回删除的数据的行数，失败返回0
				aonum = DB.delete("expenditure", "aouserid=?",
						new String[] { uid + "" });
				ainum = DB.delete("income", "aiuserid=?", new String[] { uid
						+ "" });
				if ((aonum > 0) && (ainum > 0)) {
					Toast.makeText(ClearActivity.this, "删除成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ClearActivity.this, "删除失败",
							Toast.LENGTH_SHORT).show();
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
				// System.out.println("2222");
			}
		});
		// [3]展示对话框 和toast一样 一定要记得show出来
		builder.show();

	}
}