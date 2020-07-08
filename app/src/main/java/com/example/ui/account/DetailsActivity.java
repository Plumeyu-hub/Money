package com.example.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bean.AddBean;
import com.example.money.R;
import com.example.ui.budget.BudgetActivity;
import com.example.ui.my.AboutActivity;
import com.example.ui.my.ClearActivity;
import com.example.ui.my.ExportActivity;
import com.example.ui.my.HelpActivity;
import com.example.ui.my.MessageActivity;
import com.example.ui.my.MineActivity;
import com.example.ui.my.RemindActivity;
import com.example.ui.my.SetActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetailsActivity extends Activity {
	// 广播
	MyBroadcastReceiver receiver;

	// list
	private ListView lv;
	private List<AddBean> list;
	private AddListAdapter adapter;

	// 侧边栏
	ImageView iv_my;
	DrawerLayout drawerLayout;/* 重点，声明DrawerLayout */

	// 时间
	private EditText monthtime;

	// 搜索
	TextView tv_search;

	// 数据库
	private SQLiteDatabase DB;
	private Cursor cs;// 游标对象，用来报错查询返回的结果集

	// 用户名
	private SharedPreferences spuser;
	private SharedPreferences.Editor editoruser;
	private String username;
	private int userid;
	private TextView tv_username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		// 用户名存储
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			// 使用getString方法获得value，注意第2个参数是value的默认值
			username = spuser.getString("ic_username", "");
			userid = spuser.getInt("userid", 0);
			// System.out.println(ic_username);
			tv_username = (TextView) findViewById(R.id.tv_username);
			tv_username.setText(username);
			// System.out.println(userid);
		}

		// lv
		// 第一步:listview控件实例化
		lv = (ListView) findViewById(R.id.lv_details);
		// 第二步:初始化数据
		list = new ArrayList<AddBean>();
		// 实例化适配器
		adapter = new AddListAdapter(this, list);
		// listview设置适配器
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				AddBean showdata = list.get(position);
				Intent i = new Intent(DetailsActivity.this,
						DataInformationActivity.class);
				i.putExtra("showdata", showdata);
				i.putExtra("position", position);
				startActivityForResult(i, 0);
			}
		});

		// 广播
		registerReceiver();// 注册广播

		// 侧边栏
		/* 重点，获取点击弹出侧边栏的组件 */
		iv_my = (ImageView) findViewById(R.id.iv_details_my);
		/* 重点，获取主界面的布局 */
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_details);
		iv_my.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* 重点，LEFT是xml布局文件中侧边栏布局所设置的方向 */
				drawerLayout.openDrawer(Gravity.LEFT);
			}
		});

		// 时间
		// 获取对象
		monthtime = (EditText) findViewById(R.id.et_details_month);
		// 点击"日期"按钮布局 设置日期
		monthtime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePicker();
			}
		});

		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		DB = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);
		// 长按删除
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// 获取所点击项的_id
				TextView tv_id = (TextView) arg1
						.findViewById(R.id.tv_dataitem_id);
				final String id = tv_id.getText().toString();

				TextView tv_sy = (TextView) arg1
						.findViewById(R.id.tv_dataitem_money);
				final String sy = tv_sy.getText().toString().substring(0, 1);

				// 通过Dialog提示是否删除
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DetailsActivity.this);
				builder.setMessage("确定要删除吗？");
				// 确定按钮点击事件
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String uid = String.valueOf(userid);
								// 删除指定数据
								int num;
								if (sy.equals("-")) {
									// 删除数据，成功返回删除的数据的行数，失败返回0
									num = DB.delete("expenditure",
											"aoid=? and aouserid=?",
											new String[] { id + "", uid + "" });
									if (num > 0) {
										Toast.makeText(DetailsActivity.this,
												"删除成功" + num,
												Toast.LENGTH_SHORT).show();
										// 删掉长按的item
										list.remove(arg2);
										// 动态更新listview
										adapter.notifyDataSetChanged();
									} else {
										Toast.makeText(DetailsActivity.this,
												"删除失败" + num,
												Toast.LENGTH_SHORT).show();
									}

								} else if (sy.equals("+")) {
									// 删除数据，成功返回删除的数据的行数，失败返回0
									num = DB.delete("income",
											"aiid=? and aiuserid=?",
											new String[] { id + "", uid + "" });
									if (num > 0) {
										Toast.makeText(DetailsActivity.this,
												"删除成功" + num,
												Toast.LENGTH_SHORT).show();
										// 删掉长按的item
										list.remove(arg2);
										// 动态更新listview
										adapter.notifyDataSetChanged();
									} else {
										Toast.makeText(DetailsActivity.this,
												"删除失败" + num,
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(DetailsActivity.this,
											"删除失败", Toast.LENGTH_SHORT).show();
								}

							}
						});
				// 取消按钮点击事件
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();

				return true;
			}
		});
	}

	// 外
	// 注册广播接收者
	public void registerReceiver() {
		receiver = new MyBroadcastReceiver();// 实例化广播接收者
		IntentFilter filter1 = new IntentFilter("GBadd");
		registerReceiver(receiver, filter1);
	}

	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if ("GBadd".equals(intent.getAction())) {
				// Toast.makeText(DetailsActivity.this,
				// "接收到广播一" + intent.getStringExtra("hhh"),
				// Toast.LENGTH_SHORT).show();
				// 接收到广播，取出里面携带的数据
				AddBean adddata = (AddBean) intent
						.getSerializableExtra("adddata");
				if (adddata != null) {
					// tv.setText(aidata.getMoney());
					String monthtimes = monthtime.getText().toString();
					if (monthtimes.equals("请点击选择账单年月")) {
						list.add(adddata);
						adapter.notifyDataSetChanged();
					} else {
						monthtime.setText("请点击选择账单年月");
						monthtime.setTextColor(getResources().getColor(
								R.color.blackthree));
						list.clear();
						list.add(adddata);
						adapter.notifyDataSetChanged();
					}
				}
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);// 注销广播接收者
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (intent != null) {
			if (resultCode == 102) {
				AddBean updata = (AddBean) intent
						.getSerializableExtra("updata");
				int position = intent.getIntExtra("position", 0);
				list.set(position, updata);
				adapter.notifyDataSetChanged();
			}

		}
	}

	public void click(View v) {
		switch (v.getId()) {
		case R.id.login_no:
			Intent i1 = new Intent(DetailsActivity.this, MineActivity.class);
			startActivity(i1);
			break;
		case R.id.mes:
			Intent i2 = new Intent(DetailsActivity.this, MessageActivity.class);
			startActivity(i2);
			break;
		case R.id.remind:
			Intent i3 = new Intent(DetailsActivity.this, RemindActivity.class);
			startActivity(i3);
			break;
		case R.id.set:
			Intent i4 = new Intent(DetailsActivity.this, SetActivity.class);
			startActivity(i4);
			break;
		case R.id.help:
			Intent i5 = new Intent(DetailsActivity.this, HelpActivity.class);
			startActivity(i5);
			break;
		case R.id.about:
			Intent i6 = new Intent(DetailsActivity.this, AboutActivity.class);
			startActivity(i6);
			break;
		case R.id.exit:
			createPopWindow();
			break;
		case R.id.tv_details_search:
			Intent i7 = new Intent(DetailsActivity.this, SearchActivity.class);
			startActivity(i7);
			break;
		case R.id.export:
			Intent i8 = new Intent(DetailsActivity.this, ExportActivity.class);
			startActivity(i8);
			break;
		case R.id.clear:
			Intent i10 = new Intent(DetailsActivity.this, ClearActivity.class);
			startActivity(i10);
			break;
		case R.id.budget:
			Intent i11 = new Intent(DetailsActivity.this, BudgetActivity.class);
			startActivity(i11);
			break;
		default:
			break;
		}
	}

	// 创建弹窗并显示
	PopupWindow pop;

	public void createPopWindow() {
		if (pop == null) {
			View view = LayoutInflater.from(this)
					.inflate(R.layout.dialog, null);
			// 获取圆角对话框布局View，背景设为圆角
			view.setBackgroundResource(R.drawable.bg_eeeeee_rl_corner_20);
			// 实例化弹窗
			pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			// 创建弹出对话框，设置弹出对话框的背景为圆角
			pop.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_eeeeee_rl_corner_20));
			pop.setFocusable(true);// 获取焦点（把焦点集中在pop上面）
			pop.setBackgroundDrawable(new BitmapDrawable());// 设置点击pop外面的窗口时是否弹窗
			// 这两句话是防止软键盘遮挡弹窗
			// pop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
			// pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

			TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
			tv_yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
					// System.ic_exit(0);
				}
			});
			TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
			tv_no.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pop.dismiss();
				}
			});
		}
		pop.showAtLocation(
				LayoutInflater.from(this).inflate(R.layout.activity_main, null),
				Gravity.CENTER, 0, 0);// 在父控件LayoutInflater.from(this).inflate(R.layout.activity_main,
										// null)的中间显示出来
	}

	// 获取当前日期
	Calendar calendar = Calendar.getInstance();
	int year = calendar.get(Calendar.YEAR);
	int month = calendar.get(Calendar.MONTH);
	int day = calendar.get(Calendar.DAY_OF_MONTH);

	private void showDatePicker() {

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
			monthtime.setText(new StringBuilder().append(year).append("年")
					.append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
					.append("月"));
			monthtime.setTextColor(getResources().getColor(R.color.blackone));

			// 条件
			String years = String.valueOf(year);
			int monthn = month + 1;
			String months;
			if ((month + 1) < 10) {
				months = String.valueOf("0" + monthn);
			} else {
				months = String.valueOf(monthn);
			}
			String condition = String.valueOf(years + months);
			// System.out.println(condition);

			list.clear();
			String uid = String.valueOf(userid);
			AddBean addBean;
			cs = DB.query("expenditure",
					new String[] { "aoid", "aocategory", "aomoney", "aotime",
							"aoaccount", "aoremarks", "aouserid" },
					"aotime like ? and aouserid=?", new String[] {
							condition + "%", uid + "" }, null, null,
					"aotime ASC");
			if (cs != null) {

				while (cs.moveToNext()) {
					int aoid = cs.getInt(cs.getColumnIndex("aoid"));// 得到列名id对于的值
					int aouserid = cs.getInt(cs.getColumnIndex("aouserid"));
					String aocategory = cs.getString(cs
							.getColumnIndex("aocategory"));
					String aomoney = "-"
							+ cs.getString(cs.getColumnIndex("aomoney"));
					String aotime = cs.getString(cs.getColumnIndex("aotime"));
					String aoaccount = cs.getString(cs
							.getColumnIndex("aoaccount"));
					String aoremarks = cs.getString(cs
							.getColumnIndex("aoremarks"));
					addBean = new AddBean(aocategory, aomoney, aoaccount,
							aoremarks, aotime, aoid, aouserid);
					list.add(addBean);
					// adapter.notifyDataSetChanged();
				}
			}

			cs = DB.query("income",
					new String[] { "aiid", "aicategory", "aimoney", "aitime",
							"aiaccount", "airemarks", "aiuserid" },
					"aitime like ? and aiuserid=?", new String[] {
							condition + "%", uid + "" }, null, null,
					"aitime ASC");
			if (cs != null) {
				// AddBean addBean;
				while (cs.moveToNext()) {
					int aiid = cs.getInt(cs.getColumnIndex("aiid"));// 得到列名id对于的值
					int aiuserid = cs.getInt(cs.getColumnIndex("aiuserid"));
					String aicategory = cs.getString(cs
							.getColumnIndex("aicategory"));
					String aimoney = "+"
							+ cs.getString(cs.getColumnIndex("aimoney"));
					String aitime = cs.getString(cs.getColumnIndex("aitime"));
					String aiaccount = cs.getString(cs
							.getColumnIndex("aiaccount"));
					String airemarks = cs.getString(cs
							.getColumnIndex("airemarks"));
					addBean = new AddBean(aicategory, aimoney, aiaccount,
							airemarks, aitime, aiid, aiuserid);
					list.add(addBean);
				}
			}
			Collections.sort(list, new Comparator<AddBean>() {

				@Override
				public int compare(AddBean lhs, AddBean rhs) {
					// TODO Auto-generated method stub
					int a = Integer.parseInt(lhs.getDaytime());
					int b = Integer.parseInt(rhs.getDaytime());
					int diff = a - b;
					if (diff > 0) {
						return 1;
					} else if (diff < 0) {
						return -1;
					}
					return 0;// 相等为0
				}
			});
			adapter.notifyDataSetChanged();
			// 设置空列表的时候，显示为一张图片
			lv.setEmptyView(findViewById(R.id.lin_details_empty));
		}
	};
}
