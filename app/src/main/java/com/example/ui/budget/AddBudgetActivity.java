package com.example.ui.budget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.money.R;

import java.util.Calendar;

public class AddBudgetActivity extends Activity {
	private EditText et_addbudget_money, et_addbudget_daytime,
			et_addbudget_remarks;
	private String addb_money, addb_daytime, addb_remarks;

	// 获取当前日期
	private Calendar calendar = Calendar.getInstance();
	private int year = calendar.get(Calendar.YEAR);
	private int month = calendar.get(Calendar.MONTH);
	private int day = calendar.get(Calendar.DAY_OF_MONTH);

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
		setContentView(R.layout.addbudget);

		// 操作用户名
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			userid = spuser.getInt("userid", 0);
		}
		et_addbudget_money = (EditText) findViewById(R.id.et_addbudget_money);
		et_addbudget_daytime = (EditText) findViewById(R.id.et_addbudget_daytime);
		et_addbudget_remarks = (EditText) findViewById(R.id.et_addbudget_remarks);

		// 数据库
		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists budget (id integer primary key,money text,time text,remarks text,userid integer)");

		// 更新EditText控件日期 小于10加0
		et_addbudget_daytime.setText(new StringBuilder().append(year)
				.append((month + 1) < 10 ? "0" + (month + 1) : (month + 1)));

		et_addbudget_daytime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MDate();
			}
		});

	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.addbudget_left:
			finish();
			break;
		case R.id.btn_addbudget_yes:
			addbudget();
			break;
		default:
			break;
		}
	}

	public void addbudget() {
		addb_money = et_addbudget_money.getText().toString();
		String rightmoney;
		addb_daytime = et_addbudget_daytime.getText().toString();
		addb_remarks = et_addbudget_remarks.getText().toString();

		if (TextUtils.isEmpty(addb_money)) {
			Toast.makeText(AddBudgetActivity.this, "请输入金额", Toast.LENGTH_LONG)
					.show();
			return;
		} else if (addb_money.indexOf('.') == 0) {
			Toast.makeText(AddBudgetActivity.this, "请输入正确的金额",
					Toast.LENGTH_LONG).show();
			return;
		} else if (addb_money.lastIndexOf('.') == (addb_money.length() - 1)) {
			Toast.makeText(AddBudgetActivity.this, "请输入正确的金额",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			int up_dotnum = 0;// 小数点出现的个数
			// 将金额字符串转为数组
			char[] up_m = addb_money.toCharArray();
			for (int k = 0; k < up_m.length; k++) {
				if (up_m[k] == '.') {
					up_dotnum++;
				}
			}
			// 记录小数点后两位ao_dmoney
			if (up_dotnum == 1) {
				int up_n = addb_money.indexOf('.');
				int up_l = addb_money.length();
				if (up_l - up_n == 2) {
					rightmoney = addb_money.substring(0,
							addb_money.indexOf('.'))
							+ addb_money.substring(addb_money.indexOf('.'),
									addb_money.indexOf('.') + 2);
				} else {
					rightmoney = addb_money.substring(0,
							addb_money.indexOf('.'))
							+ addb_money.substring(addb_money.indexOf('.'),
									addb_money.indexOf('.') + 3);
				}
			} else {
				rightmoney = addb_money;
			}
		}
		String uid = String.valueOf(userid);
		addb_daytime = et_addbudget_daytime.getText().toString();
		addb_remarks = et_addbudget_remarks.getText().toString();
		// 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
		cv = new ContentValues();
		cv.put("money", rightmoney);
		cv.put("time", addb_daytime);
		cv.put("remarks", addb_remarks);
		cv.put("userid", uid);
		// 插入数据，成功返回当前行号，失败返回0
		num = (int) db.insert("ic_budget", null, cv);
		if (num > 0) {
			Toast.makeText(this, "预算保存成功" + num, Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, "预算保存失败" + num, Toast.LENGTH_SHORT).show();
		}
	}

	private void MDate() {

		// 创建并显示DatePickerDialog
		DatePickerDialog dialog = new DatePickerDialog(this,
				AlertDialog.THEME_HOLO_LIGHT, Datelistener, year, month, day);
		dialog.show();

		// 只显示年月，隐藏掉日
		DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow()
				.getDecorView());
		if (dp != null) {
			((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
					.getChildAt(2).setVisibility(View.GONE);
			// 如果想隐藏掉年，将getChildAt(2)改为getChildAt(0)
		}
	}

	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
		/**
		 * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
		 * params：dayOfMonth：当前选择的日
		 */
		@Override
		public void onDateSet(DatePicker view, int myyear, int monthOfYear,
				int dayOfMonth) {

			// 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
			year = myyear;
			month = monthOfYear;
			day = dayOfMonth;
			// 更新日期
			updateDate();

		}

		// 当DatePickerDialog关闭时，更新日期显示
		private void updateDate() {
			// 在TextView上显示日期
			et_addbudget_daytime
					.setText(new StringBuilder()
							.append(year)
							.append((month + 1) < 10 ? "0" + (month + 1)
									: (month + 1)));
			et_addbudget_daytime.setTextColor(getResources().getColor(
					R.color.blackone));
		}

	};

}