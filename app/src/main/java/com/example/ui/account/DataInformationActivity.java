package com.example.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.AddBean;
import com.example.money.R;

import java.util.Calendar;

public class DataInformationActivity extends Activity {
	TextView tv_id, tv_category, tv_symoney;
	EditText et_money, et_remarks, et_daytime;
	Spinner sp_account;
	LinearLayout lin_update;
	// 传递
	int position = 0;

	// 获取当前的money
	String money;
	// 判断当前money的符号
	String jumoney;// 无符号的
	String jum;// 符号

	// sp,定义一个字符串数组来存储下拉框每个item要显示的文本
	String[] spacc_items = { "支付宝", "微信", "银行卡", "信用卡", "其他" };

	// 时间
	// 定义显示时间控件
	Calendar up_calendar; // 通过Calendar获取系统时间
	int up_mYear;
	int up_mMonth;
	int up_mDay;

	// 修改后保存的
	String up_category, up_money, up_daytime, up_account, up_remarks;
	int up_id;
	// 数据库
	private SQLiteDatabase db;// 数据库对象
	private ContentValues cv;// 存储工具栏
	private int num = 0;

	// 操作用户名
	private SharedPreferences spuser;
	private SharedPreferences.Editor editoruser;
	private int userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 软键盘会覆盖在屏幕上面，而不会把你的布局顶上去
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_datainformation);

		// 操作用户名
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			userid = spuser.getInt("userid", 0);
		}

		// 数据库
		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);

		tv_category = (TextView) findViewById(R.id.datainformationcategory_tv);
		tv_symoney = (TextView) findViewById(R.id.datainformationsymbol_tv);
		et_money = (EditText) findViewById(R.id.datainformationmoney_et);
		et_daytime = (EditText) findViewById(R.id.datainformationdaytime_et);
		sp_account = (Spinner) findViewById(R.id.datainformationaccount_sp);
		et_remarks = (EditText) findViewById(R.id.datainformationremarks_et);
		lin_update = (LinearLayout) findViewById(R.id.datainformationbottomupdate_lin);

		getData();// 获取传递过来的数据并显示

		// 修改
		lin_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String updmoney = et_money.getText().toString();
				String rightmoney;
				up_daytime = et_daytime.getText().toString();
				up_remarks = et_remarks.getText().toString();
				if (TextUtils.isEmpty(updmoney)) {
					Toast.makeText(DataInformationActivity.this, "请输入金额",
							Toast.LENGTH_LONG).show();
					return;
				} else if (updmoney.indexOf('.') == 0) {
					Toast.makeText(DataInformationActivity.this, "请输入正确的金额",
							Toast.LENGTH_LONG).show();
					return;
				} else if (updmoney.lastIndexOf('.') == (updmoney.length() - 1)) {
					Toast.makeText(DataInformationActivity.this, "请输入正确的金额",
							Toast.LENGTH_LONG).show();
					return;
				} else {
					int up_dotnum = 0;// 小数点出现的个数
					// 将金额字符串转为数组
					char[] up_m = updmoney.toCharArray();
					for (int k = 0; k < up_m.length; k++) {
						if (up_m[k] == '.') {
							up_dotnum++;
						}
					}
					// 记录小数点后两位ao_dmoney
					if (up_dotnum == 1) {
						int up_n = updmoney.indexOf('.');
						int up_l = updmoney.length();
						if (up_l - up_n == 2) {
							rightmoney = updmoney.substring(0,
									updmoney.indexOf('.'))
									+ updmoney.substring(updmoney.indexOf('.'),
											updmoney.indexOf('.') + 2);
						} else {
							rightmoney = updmoney.substring(0,
									updmoney.indexOf('.'))
									+ updmoney.substring(updmoney.indexOf('.'),
											updmoney.indexOf('.') + 3);
						}
					} else {
						rightmoney = updmoney;
					}
				}
				up_money = tv_symoney.getText().toString() + rightmoney;
				String uid = String.valueOf(userid);
				if ((tv_symoney.getText().toString()).equals("-")) {
					// 修改数据
					// 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
					cv = new ContentValues();
					// cv.put("aoid", up_id);
					cv.put("aocategory", up_category);
					cv.put("aomoney", rightmoney);
					cv.put("aoaccount", up_account);
					cv.put("aoremarks", up_remarks);
					cv.put("aotime", up_daytime);
					// 修改数据，返回修改成功的行数，失败则返回0
					num = db.update("expenditure", cv, "aoid=? and aouserid=?",
							new String[] { up_id + "", uid + "" });
					if (num > 0) {
						Toast.makeText(DataInformationActivity.this,
								"修改成功" + num, Toast.LENGTH_SHORT).show();
						AddBean updata = new AddBean(up_category, up_money,
								up_account, up_remarks, up_daytime, up_id,
								userid);
						Intent i = new Intent();
						i.putExtra("updata", updata);
						i.putExtra("position", position);
						setResult(102, i);
						finish();
					} else {
						Toast.makeText(DataInformationActivity.this,
								"修改失败" + num, Toast.LENGTH_SHORT).show();
					}
				} else if ((tv_symoney.getText().toString()).equals("+")) {
					// 修改数据
					// 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
					cv = new ContentValues();
					cv.put("aicategory", up_category);
					cv.put("aimoney", rightmoney);
					cv.put("aiaccount", up_account);
					cv.put("airemarks", up_remarks);
					cv.put("aitime", up_daytime);
					// 修改数据，返回修改成功的行数，失败则返回0
					num = db.update("income", cv, "aiid=? and aiuserid=?",
							new String[] { up_id + "", uid + "" });
					if (num > 0) {
						Toast.makeText(DataInformationActivity.this,
								"修改成功" + num, Toast.LENGTH_SHORT).show();
						AddBean updata = new AddBean(up_category, up_money,
								up_account, up_remarks, up_daytime, up_id,
								userid);
						Intent i = new Intent();
						i.putExtra("updata", updata);
						i.putExtra("position", position);
						setResult(102, i);
						finish();
					} else {
						Toast.makeText(DataInformationActivity.this,
								"修改失败" + num, Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(DataInformationActivity.this, "修改失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// sp账户
		// 设置下拉列表的条目被选择监听器
		sp_account.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
				up_account = spacc_items[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// 时间
		up_calendar = Calendar.getInstance();
		// 点击"日期"按钮布局 设置日期
		et_daytime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(DataInformationActivity.this,
						AlertDialog.THEME_HOLO_LIGHT,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int day) {
								// TODO Auto-generated method stub
								up_mYear = year;
								up_mMonth = month;
								up_mDay = day;
								// 更新EditText控件日期 小于10加0
								et_daytime.setText(new StringBuilder()
										.append(up_mYear)
										// .append("-")
										.append((up_mMonth + 1) < 10 ? "0"
												+ (up_mMonth + 1)
												: (up_mMonth + 1))
										// .append("-")
										.append((up_mDay < 10) ? "0" + up_mDay
												: up_mDay));
							}
						}, up_calendar.get(Calendar.YEAR), up_calendar
								.get(Calendar.MONTH), up_calendar
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.datainformationleft_tv:
			finish();
			break;

		default:
			break;
		}
	}

	public void getData() {
		Intent i = this.getIntent();
		if (i != null) {
			AddBean aidata = (AddBean) i.getSerializableExtra("showdata");
			up_id = aidata.getId();
			System.out.println(up_id);
			tv_category.setText(aidata.getCategory());
			up_category = aidata.getCategory();
			tv_symoney.setText(aidata.getMoney().substring(0, 1));
			et_money.setText(aidata.getMoney().substring(1));
			et_money.setSelection(et_money.length());
			et_daytime.setText(aidata.getDaytime());
			String sd_account = aidata.getAccount();
			for (int j = 0; j < spacc_items.length; j++) {
				if (sd_account.equals(spacc_items[j])) {
					// 定义数组适配器，利用系统布局文件
					ArrayAdapter<String> spacc_adapter1 = new ArrayAdapter<String>(
							this, android.R.layout.simple_spinner_item,
							spacc_items);
					// 定义下拉框的样式
					spacc_adapter1
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					sp_account.setAdapter(spacc_adapter1);
					// 设置sp的值等于传递过来的值
					sp_account.setSelection(j, true);
					up_account = spacc_items[j];
					break;
				}
			}
			et_remarks.setText(aidata.getRemarks());
			et_remarks.setSelection(et_remarks.length());// 将光标移至文字末尾
			position = i.getIntExtra("position", 0);
		}

	}
}
