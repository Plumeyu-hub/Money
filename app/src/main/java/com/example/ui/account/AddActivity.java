package com.example.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.bean.AddBean;
import com.example.money.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddActivity extends Activity {
	private ViewPager viewPager;
	private ArrayList<View> pageview;
	private TextView appoutLayout;
	private TextView appinLayout;
	// 滚动条图片
	private ImageView scrollbar;
	// 滚动条初始偏移量
	private int offset = 0;
	// 滚动条宽度
	private int bmpW;
	// 一倍滚动量
	private int one;

	// 支出
	private GridView ao_gridView;
	private AddOutGridviewAdapter addoutGridviewAdapter;
	private String[] iconName_out = { "餐厅", "食材", "外卖", "水果", "零食", "烟酒茶饮料",
			"住房", "水电煤", "交通", "汽车", "购物", "快递", "通讯", "鞋饰服", "日用品", "美容",
			"还款", "投资", "工作", "数码", "学习", "运动", "娱乐", "医疗药品", "维修", "旅行", "社交",
			"公益捐赠", "宠物", "孩子", "长辈", "其他" };

	// sp账户选择框
	private Spinner ao_spinner;
	// 备注
	EditText aoet_remarks;
	// 时间
	private EditText aoet_daytime;
	// 定义显示时间控件
	private Calendar ao_calendar; // 通过Calendar获取系统时间
	private int ao_mYear;
	private int ao_mMonth;
	int ao_mDay;

	// cal金额
	Button aobt_ainyes;// 确认按钮
	EditText aoet_money;// 金额
	String aostr_money = "";

	// 数据
	String ao_category = "";// 类别
	String ao_money;// 金额
	String ao_account = "支付宝";// 账户
	String ao_remarks;// 备注
	String ao_daytime;// 时间

	// 收入
	// GV控件
	private GridView ai_gridView;
	private List<Map<String, Object>> ai_datalist;
	private SimpleAdapter ai_sim_adapter;
	private String[] iconName_in = { "工资", "奖金", "兼职收入", "意外所得", "收债", "借入",
			"投资回收", "投资收益", "礼金", "其他" };
	// 数据库类别名字
	private String[] sqliconName_in = {};
	private int[] icon_inno = { R.drawable.ic_wages_nor, R.drawable.ic_bonus_nor,
			R.drawable.ic_part_nor, R.drawable.ic_mis_nor, R.drawable.ic_collect_nor,
			R.drawable.ic_borrow_nor, R.drawable.ic_sell_nor, R.drawable.ic_financial_nor,
			R.drawable.ic_gifts_nor, R.drawable.ic_aiother_nor};
	private int[] icon_inyes = { R.drawable.ic_wages_sel, R.drawable.ic_bonus_sel,
			R.drawable.ic_part_sel, R.drawable.ic_mis_sel, R.drawable.ic_collect_sel,
			R.drawable.ic_borrow_sel, R.drawable.ic_sell_sel,
			R.drawable.ic_financial_sel, R.drawable.ic_gifts_sel,
			R.drawable.ic_aiother_sel};
	// GV的上次变量变化
	int ai_last = 0;

	// sp账户选择框
	private Spinner ai_spinner;
	// 备注
	EditText aiet_remarks;
	// 时间
	private EditText aiet_daytime;
	// 定义显示时间控件
	private Calendar ai_calendar; // 通过Calendar获取系统时间
	private int ai_mYear;
	private int ai_mMonth;
	private int ai_mDay;

	// cal金额
	Button aibt_ainyes;// 确认按钮
	EditText aiet_money;// 金额
	String aistr_money = "";

	// 数据
	String ai_category = "";// 类别
	String ai_money, ai_symoney;// 金额
	String ai_account = "支付宝";// 账户
	String ai_remarks;// 备注
	String ai_daytime;// 时间

	// 数据库
	private SQLiteDatabase db;// 数据库对象
	private ContentValues cv;// 存储工具栏
	private int num = 0;

	// // 收入类别数据库
	// private ContentValues incomecv;// 存储工具类
	// private boolean a = false;
	// private UserDao dao;
	// private List<Object> list = null;

	// 操作用户名
	private SharedPreferences spuser;
	private SharedPreferences.Editor editoruser;
	private int userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

		// 操作用户名
		spuser = getSharedPreferences("user", MODE_PRIVATE);
		editoruser = spuser.edit();
		if (spuser != null) {// 判断文件是否存在
			userid = spuser.getInt("userid", 0);
		}

		// // 收入类别数据库
		// dao = new UserDao(this, "incategory");
		// cv = new ContentValues();
		// for (int i = 0; i < iconName_in.length; i++) {
		// cv.put("incname", iconName_in[i]);
		// }
		//
		// a = dao.insert(incomecv);// 执行数据库对象的插入数据方法返回
		// if (a) {
		// Toast.makeText(this, "插入数据成功", Toast.LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(this, "插入数据失败", Toast.LENGTH_SHORT).show();
		// }

		// 数据库
		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists expenditure (aoid integer primary key,aocategory text,aomoney text,aotime text,aoaccount text,aoremarks text,aouserid integer)");
		db.execSQL("create table if not exists income (aiid integer primary key,aicategory text,aimoney text,aitime text,aiaccount text,airemarks text,aiuserid integer)");

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		// 查找布局文件用LayoutInflater.inflate
		LayoutInflater inflater = getLayoutInflater();
		View view1 = inflater.inflate(R.layout.addout, null);
		View view2 = inflater.inflate(R.layout.addin, null);
		appoutLayout = (TextView) findViewById(R.id.tv_addout);
		appinLayout = (TextView) findViewById(R.id.tv_addin);
		scrollbar = (ImageView) findViewById(R.id.scrollbar);
		findViewById(R.id.tv_addout).setOnClickListener(onClickListener);
		findViewById(R.id.tv_addin).setOnClickListener(onClickListener);

		pageview = new ArrayList<View>();
		// 添加想要切换的界面
		pageview.add(view1);
		pageview.add(view2);
		// 数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			// 获取当前窗体界面数
			public int getCount() {
				// TODO Auto-generated method stub
				return pageview.size();
			}

			@Override
			// 判断是否由对象生成界面
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			// 使从ViewGroup中移出当前View
			public void destroyItem(View arg0, int arg1, Object arg2) {
				((ViewPager) arg0).removeView(pageview.get(arg1));
			}

			// 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
			public Object instantiateItem(View arg0, int arg1) {
				((ViewPager) arg0).addView(pageview.get(arg1));
				return pageview.get(arg1);
			}
		};
		// 绑定适配器
		viewPager.setAdapter(mPagerAdapter);
		// 设置viewPager的初始界面为第一个界面
		viewPager.setCurrentItem(0);
		// 添加切换界面的监听器
		viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// 获取滚动条的宽度
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_underline).getWidth();
		// 为了获取屏幕宽度，新建一个DisplayMetrics对象
		DisplayMetrics displayMetrics = new DisplayMetrics();
		// 将当前窗口的一些信息放在DisplayMetrics类中
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		// 得到屏幕的宽度
		int screenW = displayMetrics.widthPixels;
		// 计算出滚动条初始的偏移量
		offset = (screenW / 2 - bmpW) / 2;
		// 计算出切换一个界面时，滚动条的位移量
		one = offset * 2 + bmpW;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		// 将滚动条的初始位置设置成与左边界间隔一个offset
		scrollbar.setImageMatrix(matrix);

		// 支出
		// GV控件
		ao_gridView = (GridView) view1.findViewById(R.id.gv_addout);
		addoutGridviewAdapter = new AddOutGridviewAdapter(this);
		ao_gridView.setAdapter(addoutGridviewAdapter);// 通过设置适配器实现网格内布局
		ao_gridView// 为每个单元格（item）添加单击事件
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						addoutGridviewAdapter.setSeclection(position);
						// 保存当前类别的名字
						ao_category = iconName_out[position];
						addoutGridviewAdapter.notifyDataSetChanged();
					}
				});

		// sp账户
		ao_spinner = (Spinner) view1.findViewById(R.id.sp_addoutaccount);
		// 定义一个字符串数组来存储下拉框每个item要显示的文本
		final String[] ao_items = { "支付宝", "微信", "银行卡", "信用卡", "其他" };
		// 定义数组适配器，利用系统布局文件
		ArrayAdapter<String> ao_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ao_items);
		// 定义下拉框的样式
		ao_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 设置下拉列表的条目被选择监听器
		ao_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
				ao_account = ao_items[arg2];
				// Toast.makeText(AddActivity.this, items[arg2], 0).show();
				// 注意： 这句话的作用是当下拉列表刚显示出来的时候，数组中第0个文本不会显示Toast
				// 如果没有这句话，当下拉列表刚显示出来的时候，数组中第0个文本会显示Toast
				// arg0.setVisibility(View.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		ao_spinner.setAdapter(ao_adapter);

		// 备注
		aoet_remarks = (EditText) view1.findViewById(R.id.et_addoutcaltext);
		// cal
		aoet_money = (EditText) view1.findViewById(R.id.et_aoutmoney);

		// 时间
		// 获取对象
		aoet_daytime = (EditText) view1.findViewById(R.id.et_addoutshowdate);
		ao_calendar = Calendar.getInstance();
		// 点击"日期"按钮布局 设置日期
		aoet_daytime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(AddActivity.this,
						AlertDialog.THEME_HOLO_LIGHT,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int day) {
								// TODO Auto-generated method stub
								ao_mYear = year;
								ao_mMonth = month;
								ao_mDay = day;
								// 更新EditText控件日期 小于10加0
								aoet_daytime.setText(new StringBuilder()
										.append(ao_mYear)
										.append("-")
										.append((ao_mMonth + 1) < 10 ? "0"
												+ (ao_mMonth + 1)
												: (ao_mMonth + 1))
										.append("-")
										.append((ao_mDay < 10) ? "0" + ao_mDay
												: ao_mDay));
							}
						}, ao_calendar.get(Calendar.YEAR), ao_calendar
								.get(Calendar.MONTH), ao_calendar
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		// 设置初始时间与当前系统时间一致
		ao_mYear = ao_calendar.get(Calendar.YEAR);
		ao_mMonth = ao_calendar.get(Calendar.MONTH);
		ao_mDay = ao_calendar.get(Calendar.DAY_OF_MONTH);
		// 更新EditText控件日期 小于10加0
		aoet_daytime.setText(new StringBuilder()
				.append(ao_mYear)
				.append("-")
				.append((ao_mMonth + 1) < 10 ? "0" + (ao_mMonth + 1)
						: (ao_mMonth + 1)).append("-")
				.append((ao_mDay < 10) ? "0" + ao_mDay : ao_mDay));

		// 收入
		// GV控件
		ai_gridView = (GridView) view2.findViewById(R.id.gv_addin);
		// 1.准备数据源
		// 2.新建适配器(SimpleAdapter)
		// 3.GridView加载适配器
		// 4.GridView配置事件监听器(OnItemClickListener)
		ai_datalist = new ArrayList<Map<String, Object>>();
		ai_sim_adapter = new SimpleAdapter(this, ai_getdata(),
				R.layout.additem, new String[] { "image", "text" }, new int[] {
						R.id.iv_additem, R.id.tv_additem });
		ai_gridView.setAdapter(ai_sim_adapter);
		// Android中取消GridView默认的点击背景色
		ai_gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		ai_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// 重置上次颜色为Color.BLACK
				setAiLastColorBlack();

				LinearLayout inLayout = (LinearLayout) view;

				ImageView inImage = (ImageView) inLayout.getChildAt(0);
				TextView inText = (TextView) inLayout.getChildAt(1);

				// 设置背景改变
				for (int j = 0; j < icon_inyes.length; j++) {
					inImage.setImageResource(icon_inyes[position]);

				}
				// 设置text变颜色
				inText.setTextColor(getResources().getColor(R.color.addinyes));

				// 保存最新的上次ID
				ai_last = position;

				// 土司
				// Toast.makeText(this, gridView.getItemAtPosition(i) + "hei",
				// Toast.LENGTH_LONG).show();
				// 保存当前类别的名字
				ai_category = iconName_in[position];
			}
		});
		// sp账户
		ai_spinner = (Spinner) view2.findViewById(R.id.sp_addinaccount);
		// 定义一个字符串数组来存储下拉框每个item要显示的文本
		final String[] ai_items = { "支付宝", "微信", "银行卡", "信用卡", "其他" };
		// 定义数组适配器，利用系统布局文件
		ArrayAdapter<String> ai_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ai_items);
		// 定义下拉框的样式
		ai_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 下面的可以直接用适配器添加item(需要把数组适配器最后一个参数去掉)
		// adapter.ic_add("java");
		// adapter.ic_add("android");
		// adapter.ic_add("dotnet");
		// adapter.ic_add("php");

		// 设置下拉列表的条目被选择监听器
		ai_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
				ai_account = ai_items[arg2];
				// Toast.makeText(AddActivity.this, items[arg2], 0).show();
				// 注意： 这句话的作用是当下拉列表刚显示出来的时候，数组中第0个文本不会显示Toast
				// 如果没有这句话，当下拉列表刚显示出来的时候，数组中第0个文本会显示Toast
				// arg0.setVisibility(View.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		ai_spinner.setAdapter(ai_adapter);

		// 备注
		aiet_remarks = (EditText) view2.findViewById(R.id.et_addincaltext);
		// cal
		aiet_money = (EditText) view2.findViewById(R.id.et_ainmoney);

		// 时间
		// 获取对象
		aiet_daytime = (EditText) view2.findViewById(R.id.et_addinshowdate);
		ai_calendar = Calendar.getInstance();
		// 点击"日期"按钮布局 设置日期
		aiet_daytime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(AddActivity.this,
						AlertDialog.THEME_HOLO_LIGHT,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int day) {
								// TODO Auto-generated method stub
								ai_mYear = year;
								ai_mMonth = month;
								ai_mDay = day;
								// 更新EditText控件日期 小于10加0
								aiet_daytime.setText(new StringBuilder()
										.append(ai_mYear)
										.append("-")
										.append((ai_mMonth + 1) < 10 ? "0"
												+ (ai_mMonth + 1)
												: (ai_mMonth + 1))
										.append("-")
										.append((ai_mDay < 10) ? "0" + ai_mDay
												: ai_mDay));
							}
						}, ai_calendar.get(Calendar.YEAR), ai_calendar
								.get(Calendar.MONTH), ai_calendar
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		// 设置初始时间与当前系统时间一致
		ai_mYear = ai_calendar.get(Calendar.YEAR);
		ai_mMonth = ai_calendar.get(Calendar.MONTH);
		ai_mDay = ai_calendar.get(Calendar.DAY_OF_MONTH);
		// 更新EditText控件日期 小于10加0
		aiet_daytime.setText(new StringBuilder()
				.append(ai_mYear)
				.append("-")
				.append((ai_mMonth + 1) < 10 ? "0" + (ai_mMonth + 1)
						: (ai_mMonth + 1)).append("-")
				.append((ai_mDay < 10) ? "0" + ai_mDay : ai_mDay));
		// 下面这句话的月份小于10没有加0
		// aiet_daytime.setText(ai_mYear + "-" + (ai_mMonth+1) + "-" +
		// ai_mDay);
	}

	// 函数外
	public class MyOnPageChangeListener implements
			ViewPager.OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {// 支出
			case 0:
				/**
				 * TranslateAnimation的四个属性分别为 float fromXDelta 动画开始的点离当前View
				 * X坐标上的差值 float toXDelta 动画结束的点离当前View X坐标上的差值 float fromYDelta
				 * 动画开始的点离当前View Y坐标上的差值 float toYDelta 动画开始的点离当前View Y坐标上的差值
				 **/
				animation = new TranslateAnimation(one, 0, 0, 0);
				break;
			case 1:// 收入
				animation = new TranslateAnimation(offset, one, 0, 0);

				// // 设置上次的颜色归零
				// setAiLastColorBlack();
				// // 重置GV的上次变量变化
				// ai_last = 0;
				// // 设置类别数据为第一个
				// // ai_category = iconName_in[0];
				break;
			}
			// 将此属性设置为true可以使得图片停在动画结束时的位置
			animation.setFillAfter(true);
			// 动画持续时间，单位为毫秒
			animation.setDuration(200);
			// 滚动条开始动画
			scrollbar.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_addout:
				// 点击时切换到第一页
				viewPager.setCurrentItem(0);
				break;
			case R.id.tv_addin:
				// 点击时切换的第二页
				viewPager.setCurrentItem(1);
				break;
			}
		}
	};

	public void click(View v) {
		Intent iGBadd = new Intent();
		switch (v.getId()) {
		case R.id.tv_addexit:
			finish();
			break;
		// 支出
		case R.id.et_addoutcaltext:// 跳转到第二个界面并返回数据
			// 第一步:通过startActivityForResult跳转到第二个界面
			Intent iouttext = new Intent(this, AddOutTextActivity.class);
			startActivityForResult(iouttext, 0);
			break;
		case R.id.bt_aoutyes:
			String ao_symoney, // 带符号的字符串
			ao_dmoney = ""; // 保留两位小数点的字符串
			int ao_dotnum = 0;// 小数点出现的个数
			// 获取到金额
			ao_money = aoet_money.getText().toString();
			// 将金额字符串转为数组
			char[] ao_m = ao_money.toCharArray();
			for (int k = 0; k < ao_m.length; k++) {
				if (ao_m[k] == '.') {
					ao_dotnum++;
				}
			}
			if (TextUtils.isEmpty(ao_money) && TextUtils.isEmpty(ao_category)) {
				Toast.makeText(AddActivity.this, "请完善账单信息", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (TextUtils.isEmpty(ao_money)) {
				Toast.makeText(AddActivity.this, "请输入账单金额", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (TextUtils.isEmpty(ao_category)) {
				Toast.makeText(AddActivity.this, "请选择账单类别", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (ao_dotnum > 1) {
				Toast.makeText(AddActivity.this, "请输入正确的金额", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (ao_money.indexOf('.') == 0) {
				Toast.makeText(AddActivity.this, "请输入正确的金额", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (ao_money.lastIndexOf('.') == (ao_money.length() - 1)) {
				Toast.makeText(AddActivity.this, "请输入正确的金额", Toast.LENGTH_LONG)
						.show();
				return;
			} else {
				// 记录小数点后两位ao_dmoney
				if (ao_dotnum == 1) {
					int ao_n = ao_money.indexOf('.');
					int ao_l = ao_money.length();
					if (ao_l - ao_n == 2) {
						ao_dmoney = ao_money
								.substring(0, ao_money.indexOf('.'))
								+ ao_money.substring(ao_money.indexOf('.'),
										ao_money.indexOf('.') + 2);
					} else {
						ao_dmoney = ao_money
								.substring(0, ao_money.indexOf('.'))
								+ ao_money.substring(ao_money.indexOf('.'),
										ao_money.indexOf('.') + 3);
					}
				} else {
					ao_dmoney = ao_money;
				}
			}
			// 备注
			ao_remarks = aoet_remarks.getText().toString();
			// 时间
			String aostrtime = aoet_daytime.getText().toString();
			ao_daytime = aostrtime.replace("-", "");
			// 为收入的金额增加-号
			ao_symoney = "-" + ao_dmoney;

			// 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
			cv = new ContentValues();
			cv.put("aocategory", ao_category);
			cv.put("aomoney", ao_dmoney);
			cv.put("aoaccount", ao_account);
			cv.put("aoremarks", ao_remarks);
			cv.put("aotime", ao_daytime);
			cv.put("aouserid", userid);
			// 插入数据，成功返回当前行号，失败返回0
			num = (int) db.insert("expenditure", null, cv);
			if (num > 0) {
				Toast.makeText(this, "数据保存成功" + num, Toast.LENGTH_SHORT).show();
				AddBean addoutdata = new AddBean(ao_category, ao_symoney,
						ao_account, ao_remarks, ao_daytime, num, userid);
				System.out.println(userid);
				// 设置广播的名字（设置Action）
				iGBadd.setAction("GBadd");
				// 携带数据
				iGBadd.putExtra("adddata", addoutdata);// 必须要序列化
				// i3.putExtra("hhh","啦啦啦");
				// 发送广播（无序广播）
				sendBroadcast(iGBadd);
				finish();
			} else {
				Toast.makeText(this, "数据保存失败" + num, Toast.LENGTH_SHORT).show();
			}

			break;

		// 收入
		case R.id.et_addincaltext:// 跳转到第二个界面并返回数据
			// 第一步:通过startActivityForResult跳转到第二个界面
			Intent iintext = new Intent(this, AddInTextActivity.class);
			startActivityForResult(iintext, 0);
			break;
		case R.id.bt_ainyes:
			// 类别
			// System.out.println("类别" + ai_category);
			// 金额
			// ai_money = aiet_money.getText().toString();
			// System.out.println("金额" + ai_money);
			// 账户
			// System.out.println("账户" + ai_account);
			// 备注
			// ai_remarks = aiet_remarks.getText().toString();
			// System.out.println("备注" + ai_remarks);
			// 时间
			// ai_daytime = aiet_daytime.getText().toString();
			// System.out.println("时间" + ai_daytime);

			String ai_symoney, // 带符号的字符串
			ai_dmoney = ""; // 保留两位小数点的字符串
			int ai_dotnum = 0;// 小数点出现的个数
			// 获取到金额
			ai_money = aiet_money.getText().toString();
			// 将金额字符串转为数组
			char[] ai_m = ai_money.toCharArray();
			for (int k = 0; k < ai_m.length; k++) {
				if (ai_m[k] == '.') {
					ai_dotnum++;
				}
			}

			if (TextUtils.isEmpty(ai_money) && TextUtils.isEmpty(ai_category)) {
				Toast.makeText(AddActivity.this, "请完善账单信息", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (TextUtils.isEmpty(ai_money)) {
				Toast.makeText(AddActivity.this, "请输入账单金额", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (TextUtils.isEmpty(ai_category)) {
				Toast.makeText(AddActivity.this, "请选择账单类别", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (ai_dotnum > 1) {
				Toast.makeText(AddActivity.this, "请输入正确的金额", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (ai_money.indexOf('.') == 0) {
				Toast.makeText(AddActivity.this, "请输入正确的金额", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (ai_money.lastIndexOf('.') == (ai_money.length() - 1)) {
				Toast.makeText(AddActivity.this, "请输入正确的金额", Toast.LENGTH_LONG)
						.show();
				return;
			} else {
				// 记录小数点后两位ao_dmoney
				if (ai_dotnum == 1) {
					int ai_n = ai_money.indexOf('.');
					int ai_l = ai_money.length();
					if (ai_l - ai_n == 2) {
						ai_dmoney = ai_money
								.substring(0, ai_money.indexOf('.'))
								+ ai_money.substring(ai_money.indexOf('.'),
										ai_money.indexOf('.') + 2);
					} else {
						ai_dmoney = ai_money
								.substring(0, ai_money.indexOf('.'))
								+ ai_money.substring(ai_money.indexOf('.'),
										ai_money.indexOf('.') + 3);
					}
				} else {
					ai_dmoney = ai_money;
				}
			}
			// 备注
			ai_remarks = aiet_remarks.getText().toString();
			// 时间
			String aistrtime = aiet_daytime.getText().toString();
			ai_daytime = aistrtime.replace("-", "");
			// 为收入的金额增加+号
			ai_symoney = "+" + ai_dmoney;

			// 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
			cv = new ContentValues();
			cv.put("aicategory", ai_category);
			cv.put("aimoney", ai_dmoney);
			cv.put("aiaccount", ai_account);
			cv.put("airemarks", ai_remarks);
			cv.put("aitime", ai_daytime);
			cv.put("aiuserid", userid);
			// 插入数据，成功返回当前行号，失败返回0
			num = (int) db.insert("income", null, cv);
			if (num > 0) {
				Toast.makeText(this, "数据保存成功" + num, Toast.LENGTH_SHORT).show();

				AddBean addindata = new AddBean(ai_category, ai_symoney,
						ai_account, ai_remarks, ai_daytime, num, userid);
				// 设置广播的名字（设置Action）
				iGBadd.setAction("GBadd");
				// 携带数据
				iGBadd.putExtra("adddata", addindata);// 必须要序列化
				// i3.putExtra("hhh","啦啦啦");
				// 发送广播（无序广播）
				sendBroadcast(iGBadd);
				finish();
			} else {
				Toast.makeText(this, "数据保存失败" + num, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	// 数字键盘
	public void clickoutnum(View V) {
		aostr_money = aoet_money.getText().toString().trim();
		switch (V.getId()) {
		case R.id.bt_aout1:
			aostr_money = aostr_money + "1";
			break;
		case R.id.bt_aout2:
			aostr_money = aostr_money + "2";
			break;
		case R.id.bt_aout3:
			aostr_money = aostr_money + "3";
			break;
		case R.id.bt_aout4:
			aostr_money = aostr_money + "4";
			break;
		case R.id.bt_aout5:
			aostr_money = aostr_money + "5";
			break;
		case R.id.bt_aout6:
			aostr_money = aostr_money + "6";
			break;
		case R.id.bt_aout7:
			aostr_money = aostr_money + "7";
			break;
		case R.id.bt_aout8:
			aostr_money = aostr_money + "8";
			break;
		case R.id.bt_aout9:
			aostr_money = aostr_money + "9";
			break;
		case R.id.bt_aout0:
			aostr_money = aostr_money + "0";
			break;
		case R.id.bt_aoutd:
			aostr_money = aostr_money + ".";
			break;
		case R.id.bt_aouts:
			if (aostr_money.length() > 0) {
				aostr_money = aostr_money
						.substring(0, aostr_money.length() - 1);
			}
			break;
		default:
			break;
		}
		if (aostr_money.length() > 8) {
			aostr_money = aostr_money.substring(0, 8);
		}
		aoet_money.setText(aostr_money);
	}

	// 收入
	// GV控件
	private List<Map<String, Object>> ai_getdata() {
		for (int i = 0; i < icon_inno.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon_inno[i]);

			// list = dao.query(a, "incategory", null, null, null, null, null,
			// ai_account);
			// if (null == list) {
			// Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
			// } else {
			// for (int j = 0; j < list.size(); j++) {
			// UserDao user = (UserDao) list.get(j);
			// for (int k; k < list.size(); k++) {
			// sqliconName_in[j] = user.getId();
			// }
			// }
			//
			// }

			map.put("text", iconName_in[i]);
			ai_datalist.add(map);
		}
		return ai_datalist;
	}

	// // 设置新打开页面的颜色
	// public void setAoNewColorBlack() {
	// LinearLayout lastLayout = (LinearLayout) ao_gridView.getChildAt(0);
	//
	// TextView lastText = (TextView) lastLayout.getChildAt(1);
	// ImageView lastImage = (ImageView) lastLayout.getChildAt(0);
	//
	// lastText.setTextColor(this.getResources().getColor(R.color.addoutyes));
	// // 设置背景改变
	// lastImage.setImageResource(icon_outyes[0]);
	// }

	// 设置GV变化
	// 设置上次的颜色归零
	public void setAiLastColorBlack() {
		LinearLayout lastinLayout = (LinearLayout) ai_gridView
				.getChildAt(ai_last);

		TextView lastinText = (TextView) lastinLayout.getChildAt(1);
		ImageView lastinImage = (ImageView) lastinLayout.getChildAt(0);

		lastinText.setTextColor(this.getResources().getColor(R.color.addno));
		// 设置背景改变
		for (int k = 0; k < icon_inno.length; k++) {
			lastinImage.setImageResource(icon_inno[ai_last]);
		}
	}

	// 数字键盘
	public void clickinnum(View V) {
		aistr_money = aiet_money.getText().toString().trim();
		switch (V.getId()) {
		case R.id.bt_ain1:
			aistr_money = aistr_money + "1";
			break;
		case R.id.bt_ain2:
			aistr_money = aistr_money + "2";
			break;
		case R.id.bt_ain3:
			aistr_money = aistr_money + "3";
			break;
		case R.id.bt_ain4:
			aistr_money = aistr_money + "4";
			break;
		case R.id.bt_ain5:
			aistr_money = aistr_money + "5";
			break;
		case R.id.bt_ain6:
			aistr_money = aistr_money + "6";
			break;
		case R.id.bt_ain7:
			aistr_money = aistr_money + "7";
			break;
		case R.id.bt_ain8:
			aistr_money = aistr_money + "8";
			break;
		case R.id.bt_ain9:
			aistr_money = aistr_money + "9";
			break;
		case R.id.bt_ain0:
			aistr_money = aistr_money + "0";
			break;
		case R.id.bt_aind:
			aistr_money = aistr_money + ".";
			break;
		case R.id.bt_ains:
			if (aistr_money.length() > 0) {
				aistr_money = aistr_money
						.substring(0, aistr_money.length() - 1);
			}
			break;
		default:
			break;
		}
		if (aistr_money.length() > 8) {
			aistr_money = aistr_money.substring(0, 8);
		}
		aiet_money.setText(aistr_money);
	}

	// 当用startActivityForResult跳转到第二个界面时，一旦第二个界面关闭并回到当前界面，则自动回调本方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		// 第三步：接收第二个界面返回的数据并展示
		if (intent != null) {
			if (requestCode == 0 && resultCode == 1) {
				// 知道当前是从第二个界面返回过来
				String ai_str = intent.getStringExtra("ai_str");
				aiet_remarks.setText(ai_str);
				String ao_str = intent.getStringExtra("ao_str");
				aoet_remarks.setText(ao_str);
			}
		}
	}

}
