package com.example.ui.my;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.money.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportActivity extends Activity {
	Button btn_ex;

	// 操作用户名
	private SharedPreferences spuser;
	private SharedPreferences.Editor editoruser;
	private int userid;

	private SQLiteDatabase db;// 数据库对象
	private Cursor csout, csin;// 游标对象，用来报错查询返回的结果集

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export);

		// 操作用户名
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			userid = spuser.getInt("userid", 0);
		}

		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);

		btn_ex = (Button) findViewById(R.id.export_btn);
		btn_ex.setOnClickListener(new View.OnClickListener() {
			public void onClick(View source) {
				showNormalDialog();

			}
		});
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.exportleft_tv:
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
		builder.setMessage("是否导出该账号所有的数据库记录？");
		// [2]设置确定和取消按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String uid = String.valueOf(userid);
				csout = db.rawQuery(
						"select * from expenditure where aouserid=?",
						new String[] { uid + "" });
				csin = db.rawQuery("select * from income where aiuserid=?",
						new String[] { uid + "" });
				ExportToCSV(csout, "expenditure.csv");
				ExportToCSV(csin, "income.csv");
				// Toast.makeText(ExportActivity.this, "导出完毕！",
				// Toast.LENGTH_SHORT)
				// .show();
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

	public void ExportToCSV(Cursor c, String fileName) {

		int rowCount = 0;
		int colCount = 0;
		FileWriter fw;
		BufferedWriter bfw;
		File sdCardDir = Environment.getExternalStorageDirectory();
		File saveFile = new File(sdCardDir, fileName);
		try {

			rowCount = c.getCount();
			colCount = c.getColumnCount();
			fw = new FileWriter(saveFile);
			bfw = new BufferedWriter(fw);
			if (rowCount > 0) {
				c.moveToFirst();
				// 写入表头
				for (int i = 0; i < colCount; i++) {
					if (i != colCount - 1)
						bfw.write(c.getColumnName(i) + ',');
					else
						bfw.write(c.getColumnName(i));
				}
				// 写好表头后换行
				bfw.newLine();
				// 写入数据
				for (int i = 0; i < rowCount; i++) {
					c.moveToPosition(i);
					Toast.makeText(this, "正在导出第" + (i + 1) + "条",
							Toast.LENGTH_SHORT).show();
					Log.v("导出数据", "正在导出第" + (i + 1) + "条");
					for (int j = 0; j < colCount; j++) {
						if (j != colCount - 1)
							bfw.write(c.getString(j) + ',');
						else
							bfw.write(c.getString(j));
					}
					// 写好每条记录后换行
					bfw.newLine();
				}
			}
			// 将缓存数据写入文件
			bfw.flush();
			// 释放缓存
			bfw.close();
			Toast.makeText(this, "导出完毕！", Toast.LENGTH_SHORT).show();
			Log.v("导出数据", "导出完毕！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			c.close();
		}
	}
}
