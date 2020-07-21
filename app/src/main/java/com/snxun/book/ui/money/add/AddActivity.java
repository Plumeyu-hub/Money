package com.snxun.book.ui.money.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.money.adapter.GridAdapter;
import com.snxun.book.ui.money.bean.DataBean;
import com.snxun.book.ui.my.demo.gr.GrAdapter;
import com.snxun.book.ui.my.demo.gr.GrDataBean;
import com.snxun.book.utils.sp.SpManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddActivity extends BaseActivity {
    /**
     * 添加账目广播请求码
     */
    public static final String ADD_BROADCAST_REQUEST_CODE = "com.snxun.book.GBaddGBadd";
    /**
     * 添加账目Intent带数据跳转请求码
     */
    public static final String ADD_INTENT_REQUEST_CODE = "adddata";


    /**
     * 返回按钮
     */
    @BindView(R.id.add_back_btn)
    ImageView mAddBackBtn;
    /**
     * 选择支出按钮
     */
    @BindView(R.id.add_out_btn)
	TextView mAddOutBtn;
    /**
     * 选择收入按钮
     */
    @BindView(R.id.add_in_btn)
	TextView mAddInBtn;

	private ViewPager mAddAccountVp;
	private ArrayList<View> mPageView;
	// 滚动条图片
	private ImageView mScrollbarIv;
	// 滚动条初始偏移量
	private int mOffset = 0;
	// 滚动条宽度
	private int mScrollWide;
	// 一倍滚动量
	private int mRoll;

	// 支出
	private GridView mGridViewOut;
	private GridAdapter mAddAccountOutGridviewAdapter;
	private String[] mIconNameOut = {"餐厅", "食材", "外卖", "水果", "零食", "烟酒茶饮料",
			"住房", "水电煤", "交通", "汽车", "购物", "快递", "通讯", "鞋饰服", "日用品", "美容",
			"还款", "投资", "工作", "数码", "学习", "运动", "娱乐", "医疗药品", "维修", "旅行", "社交",
			"公益捐赠", "宠物", "孩子", "长辈", "其他"};

	// sp账户选择框
	private Spinner mAddOutAccountSp;
	// 备注
	EditText mAddOutRemarkEdit;
	// 时间
	private EditText mAddOutDaytimeEdit;
	// 定义显示时间控件
	private Calendar mCalendar = Calendar.getInstance();
	; // 通过Calendar获取系统时间
	private int mYear;
	private int mMonth;
	private int mDay;

	// cal金额
	Button aobt_ainyes;// 确认按钮
	EditText mAddOutMoneyEdit;// 金额
	String aostr_money = "";

	// 数据
	String ao_category = "";// 类别
	String ao_money;// 金额
	String mAddOutAccount = "支付宝";// 账户
	String ao_remarks;// 备注
	String ao_daytime;// 时间

	// 收入
	/**
	 *GV
	 */
	@BindView(R.id.add_in_gv)
	RecyclerView mAddInGv;
	private GrAdapter mGrAdapter;//自定义适配器，继承RecyclerView.Adapter
	private List<GrDataBean> mGrDataList;

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

	/**
	 * 数据库
	 */
	private SQLiteDatabase mDb;
	// 数据库
	private ContentValues cv;// 存储工具栏
	private int num = 0;

	/**
	 * 当前登录的用户ID
	 */
	private int mUserId;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_add;
    }


    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListeners() {
        super.setListeners();

        //返回
        mAddBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

		mAddOutBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// 点击时切换到第一页
				mAddAccountVp.setCurrentItem(0);
			}
		});

		mAddInBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// 点击时切换到第二页
				mAddAccountVp.setCurrentItem(1);
			}
		});
    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();


		mAddAccountVp = (ViewPager) findViewById(R.id.add_vp);
		// 查找布局文件用LayoutInflater.inflate
		LayoutInflater mLayoutInflater = getLayoutInflater();
		View mViewAddAccountOut = mLayoutInflater.inflate(R.layout.view_add_out, null);
		View mViewAddAccountIn = mLayoutInflater.inflate(R.layout.view_add_in, null);
		mScrollbarIv = (ImageView) findViewById(R.id.add_scrollbar);

		mPageView = new ArrayList<>();
		// 添加想要切换的界面
		mPageView.add(mViewAddAccountOut);
		mPageView.add(mViewAddAccountIn);
		// 数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			// 获取当前窗体界面数
			public int getCount() {
				// TODO Auto-generated method stub
				return mPageView.size();
			}

			@Override
			// 判断是否由对象生成界面
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			// 使从ViewGroup中移出当前View
			public void destroyItem(View arg0, int arg1, Object arg2) {
				((ViewPager) arg0).removeView(mPageView.get(arg1));
			}

			// 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
			public Object instantiateItem(View arg0, int arg1) {
				((ViewPager) arg0).addView(mPageView.get(arg1));
				return mPageView.get(arg1);
			}
		};
		// 绑定适配器
		mAddAccountVp.setAdapter(mPagerAdapter);
		// 设置viewPager的初始界面为第一个界面
		mAddAccountVp.setCurrentItem(0);
		// 添加切换界面的监听器
		mAddAccountVp.addOnPageChangeListener(new MyOnPageChangeListener());
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// 获取滚动条的宽度
		mScrollWide = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_underline).getWidth();
		// 为了获取屏幕宽度，新建一个DisplayMetrics对象
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		// 将当前窗口的一些信息放在DisplayMetrics类中
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		// 得到屏幕的宽度
		int mScreenWide = mDisplayMetrics.widthPixels;
		// 计算出滚动条初始的偏移量
		mOffset = (mScreenWide / 2 - mScrollWide) / 2;
		// 计算出切换一个界面时，滚动条的位移量
		mRoll = mOffset * 2 + mScrollWide;
		Matrix mMatrix = new Matrix();
		mMatrix.postTranslate(mOffset, 0);
		// 将滚动条的初始位置设置成与左边界间隔一个offset
		mScrollbarIv.setImageMatrix(mMatrix);

		// 支出
		// GV控件
		mGridViewOut = (GridView) mViewAddAccountOut.findViewById(R.id.add_out_gv);
		this.mAddAccountOutGridviewAdapter = new GridAdapter(this);
		mGridViewOut.setAdapter(this.mAddAccountOutGridviewAdapter);// 通过设置适配器实现网格内布局
		mGridViewOut// 为每个单元格（item）添加单击事件
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						AddActivity.this.mAddAccountOutGridviewAdapter.setSeclection(position);
						// 保存当前类别的名字
						ao_category = mIconNameOut[position];
						AddActivity.this.mAddAccountOutGridviewAdapter.notifyDataSetChanged();
					}
				});

		// sp账户
		mAddOutAccountSp = (Spinner) mViewAddAccountOut.findViewById(R.id.add_out_sp);
		// 定义一个字符串数组来存储下拉框每个item要显示的文本
		final String[] mAccountSpItemOut = {"支付宝", "微信", "银行卡", "信用卡", "其他"};
		// 定义数组适配器，利用系统布局文件
		ArrayAdapter<String> mAddAccountOutGridviewAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mAccountSpItemOut);
		// 定义下拉框的样式
		mAddAccountOutGridviewAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 设置下拉列表的条目被选择监听器
		mAddOutAccountSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
				mAddOutAccount = mAccountSpItemOut[arg2];
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
		mAddOutAccountSp.setAdapter(mAddAccountOutGridviewAdapter);

		// 备注
		mAddOutRemarkEdit = (EditText) mViewAddAccountOut.findViewById(R.id.add_out_submit_remark_edit);
		// cal
		mAddOutMoneyEdit = (EditText) mViewAddAccountOut.findViewById(R.id.add_out_money_edit);

		// 时间
		// 获取对象
		mAddOutDaytimeEdit = (EditText) mViewAddAccountOut.findViewById(R.id.add_out_date_edit);
		// 点击"日期"按钮布局 设置日期
		mAddOutDaytimeEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(AddActivity.this,
						AlertDialog.THEME_HOLO_LIGHT,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
												  int month, int day) {
								// TODO Auto-generated method stub
								mYear = year;
								mMonth = month;
								mDay = day;
								// 更新EditText控件日期 小于10加0
								mAddOutDaytimeEdit.setText(new StringBuilder()
										.append(mYear)
										.append("-")
										.append((mMonth + 1) < 10 ? "0"
												+ (mMonth + 1)
												: (mMonth + 1))
										.append("-")
										.append((mDay < 10) ? "0" + mDay
												: mDay));
							}
						}, mCalendar.get(Calendar.YEAR), mCalendar
						.get(Calendar.MONTH), mCalendar
						.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		// 设置初始时间与当前系统时间一致
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		// 更新EditText控件日期 小于10加0
		mAddOutDaytimeEdit.setText(new StringBuilder()
				.append(mYear)
				.append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
						: (mMonth + 1)).append("-")
				.append((mDay < 10) ? "0" + mDay : mDay));

		// 收入
		// 初始化数据列表
		mGrDataList = new ArrayList<>();
		String[] iconName_in = {"工资", "奖金", "兼职收入", "意外所得", "收债", "借入",
				"投资回收", "投资收益", "礼金", "其他"};
		int[] icon_inno = {R.drawable.ic_wages_nor, R.drawable.ic_bonus_nor,
				R.drawable.ic_part_nor, R.drawable.ic_mis_nor, R.drawable.ic_collect_nor,
				R.drawable.ic_borrow_nor, R.drawable.ic_sell_nor, R.drawable.ic_financial_nor,
				R.drawable.ic_gifts_nor, R.drawable.ic_other_nor};
		int[] icon_inyes = {R.drawable.ic_wages_sel, R.drawable.ic_bonus_sel,
				R.drawable.ic_part_sel, R.drawable.ic_mis_sel, R.drawable.ic_collect_sel,
				R.drawable.ic_borrow_sel, R.drawable.ic_sell_sel,
				R.drawable.ic_financial_sel, R.drawable.ic_gifts_sel,
				R.drawable.ic_other_in_sel};
		for(int i = 0; i<iconName_in.length; i++){
			mGrDataList.add(new GrDataBean(iconName_in[i], icon_inno[i], false));
		}

		mGrAdapter = new GrAdapter(getContext(), mGrDataList);
		//第二参数是控制显示多少列,第三个参数是控制滚动方向和LinearLayout一样,第四个参数是控制是否反向排列
		GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
		layoutManager.setOrientation(RecyclerView.VERTICAL);
		mAddInGv.setLayoutManager(layoutManager);
		mAddInGv.setHasFixedSize(true);
		mAddInGv.setAdapter(mGrAdapter);

		mGrAdapter.setOnItemClickListener(new GrAdapter.OnItemClickListener() {
			@Override
			public void onClick(int position) {
//				mGrAdapter.setSeclection(position);
				Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();

				//ai_category = iconName_in[position];
			}
		});

		mGrAdapter.setOnItemLongClickListener(new GrAdapter.OnItemLongClickListener() {
			@Override
			public void onClick(int position) {
				Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
			}
		});


		// sp账户
		ai_spinner = (Spinner) mViewAddAccountIn.findViewById(R.id.add_in_account_sp);
		// 定义一个字符串数组来存储下拉框每个item要显示的文本
		final String[] ai_items = {"支付宝", "微信", "银行卡", "信用卡", "其他"};
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
		aiet_remarks = (EditText) mViewAddAccountIn.findViewById(R.id.add_in_submit_remark_edit);
		// cal
		aiet_money = (EditText) mViewAddAccountIn.findViewById(R.id.add_in_money_edit);

		// 时间
		// 获取对象
		aiet_daytime = (EditText) mViewAddAccountIn.findViewById(R.id.add_in_date_edit);
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

	/**
	 * 初始化数据库
	 */
	private void initDb() {
		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		mDb = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
		mDb.execSQL("create table if not exists expenditure (aoid integer primary key,aocategory text,aomoney text,aotime text,aoaccount text,aoremarks text,aouserid integer)");
		mDb.execSQL("create table if not exists income (aiid integer primary key,aicategory text,aimoney text,aitime text,aiaccount text,airemarks text,aiuserid integer)");

	}

	/**
	 * 获取当前登录用户的Id
	 */
	private void showUserInfo() {
		//获取SharedPreferences对象
		mUserId = SpManager.get().getUserId();
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
                    animation = new TranslateAnimation(mRoll, 0, 0, 0);
                    break;
                case 1:// 收入
                    animation = new TranslateAnimation(mOffset, mRoll, 0, 0);

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
            mScrollbarIv.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }


    public void click(View v) {
        Intent iGBadd = new Intent();
        switch (v.getId()) {
            // 支出
            case R.id.add_out_submit_remark_edit:// 跳转到第二个界面并返回数据
                // 第一步:通过startActivityForResult跳转到第二个界面
                Intent iouttext = new Intent(this, SubmitRemarkActivity.class);
                startActivityForResult(iouttext, 0);
                break;
            case R.id.add_out_yes_btn:
                String ao_symoney, // 带符号的字符串
                        ao_dmoney = ""; // 保留两位小数点的字符串
                int ao_dotnum = 0;// 小数点出现的个数
                // 获取到金额
                ao_money = mAddOutMoneyEdit.getText().toString();
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
                ao_remarks = mAddOutRemarkEdit.getText().toString();
                // 时间
                String aostrtime = mAddOutDaytimeEdit.getText().toString();
                ao_daytime = aostrtime.replace("-", "");
                // 为收入的金额增加-号
                ao_symoney = "-" + ao_dmoney;

                // 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
                cv = new ContentValues();
                cv.put("aocategory", ao_category);
                cv.put("aomoney", ao_dmoney);
                cv.put("aoaccount", mAddOutAccount);
                cv.put("aoremarks", ao_remarks);
                cv.put("aotime", ao_daytime);
                cv.put("aouserid", mUserId);
                // 插入数据，成功返回当前行号，失败返回0
                num = (int) mDb.insert("expenditure", null, cv);
                if (num > 0) {
                    Toast.makeText(this, "数据保存成功" + num, Toast.LENGTH_SHORT).show();
                    DataBean dataBean = new DataBean(ao_category, ao_symoney,
                            mAddOutAccount, ao_remarks, ao_daytime, num, mUserId);
                    System.out.println(mUserId);
                    // 设置广播的名字（设置Action）
                    iGBadd.setAction(ADD_BROADCAST_REQUEST_CODE);
                    // 携带数据
                    iGBadd.putExtra(ADD_INTENT_REQUEST_CODE, dataBean);// 必须要序列化
                    // i3.putExtra("hhh","啦啦啦");
                    // 发送广播（无序广播）
                    sendBroadcast(iGBadd);
                    finish();
                } else {
                    Toast.makeText(this, "数据保存失败" + num, Toast.LENGTH_SHORT).show();
                }

                break;

            // 收入
            case R.id.add_in_submit_remark_edit:// 跳转到第二个界面并返回数据
                // 第一步:通过startActivityForResult跳转到第二个界面
                Intent iintext = new Intent(this, SubmitRemarkActivity.class);
                startActivityForResult(iintext, 0);
                break;
            case R.id.add_in_yes_btn:
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
                cv.put("aiuserid", mUserId);
                // 插入数据，成功返回当前行号，失败返回0
                num = (int) mDb.insert("income", null, cv);
                if (num > 0) {
                    Toast.makeText(this, "数据保存成功" + num, Toast.LENGTH_SHORT).show();

                    DataBean dataBean = new DataBean(ai_category, ai_symoney,
                            ai_account, ai_remarks, ai_daytime, num, mUserId);
                    // 设置广播的名字（设置Action）
                    iGBadd.setAction(ADD_BROADCAST_REQUEST_CODE);
                    // 携带数据
                    iGBadd.putExtra(ADD_INTENT_REQUEST_CODE, dataBean);// 必须要序列化
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
        aostr_money = mAddOutMoneyEdit.getText().toString().trim();
        switch (V.getId()) {
            case R.id.add_out_one_btn:
                aostr_money = aostr_money + "1";
                break;
            case R.id.add_out_two_btn:
                aostr_money = aostr_money + "2";
                break;
            case R.id.add_out_three_btn:
                aostr_money = aostr_money + "3";
                break;
            case R.id.add_out_four_btn:
                aostr_money = aostr_money + "4";
                break;
            case R.id.add_out_five_btn:
                aostr_money = aostr_money + "5";
                break;
            case R.id.add_out_six_btn:
                aostr_money = aostr_money + "6";
                break;
            case R.id.add_out_seven_btn:
                aostr_money = aostr_money + "7";
                break;
            case R.id.add_out_eight_btn:
                aostr_money = aostr_money + "8";
                break;
            case R.id.add_out_nine_btn:
                aostr_money = aostr_money + "9";
                break;
            case R.id.add_out_zero_btn:
                aostr_money = aostr_money + "0";
                break;
            case R.id.add_out_dot_btn:
                aostr_money = aostr_money + ".";
                break;
            case R.id.add_out_money_btn:
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
        mAddOutMoneyEdit.setText(aostr_money);
    }

    // 收入
    // 数字键盘
    public void clickinnum(View V) {
        aistr_money = aiet_money.getText().toString().trim();
        switch (V.getId()) {
            case R.id.add_in_one_btn:
                aistr_money = aistr_money + "1";
                break;
            case R.id.add_in_two_btn:
                aistr_money = aistr_money + "2";
                break;
            case R.id.add_in_three_btn:
                aistr_money = aistr_money + "3";
                break;
            case R.id.add_in_four_btn:
                aistr_money = aistr_money + "4";
                break;
            case R.id.add_in_five_btn:
                aistr_money = aistr_money + "5";
                break;
            case R.id.add_in_six_btn:
                aistr_money = aistr_money + "6";
                break;
            case R.id.add_in_seven_btn:
                aistr_money = aistr_money + "7";
                break;
            case R.id.add_in_eight_btn:
                aistr_money = aistr_money + "8";
                break;
            case R.id.add_in_nine_btn:
                aistr_money = aistr_money + "9";
                break;
            case R.id.add_in_zero_btn:
                aistr_money = aistr_money + "0";
                break;
            case R.id.add_in_dot_btn:
                aistr_money = aistr_money + ".";
                break;
            case R.id.add_in_money_btn:
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
                String ai_str = intent.getStringExtra("submit_remark");
                aiet_remarks.setText(ai_str);
                mAddOutRemarkEdit.setText(ai_str);
            }
        }
    }

}
