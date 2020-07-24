package com.snxun.book.ui.money.update;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.event.DetailsUpdateEvent;
import com.snxun.book.event.SearchUpdateEvent;
import com.snxun.book.event.UpdateDetailsEvent;
import com.snxun.book.event.UpdateSearchEvent;
import com.snxun.book.ui.money.bean.DataBean;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateActivity extends BaseActivity {
    /**
     * 跳转
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, UpdateActivity.class);
        context.startActivity(starter);
    }

    /**
     * 数据源
     */
    private DataBean dataBean;
    /**
     * 返回按钮
     */
    @BindView(R.id.update_back_btn)
    ImageView mUpdateBackBtn;
    /**
     * 类别
     */
    @BindView(R.id.update_category_tv)
    TextView mUpdateCategoryTv;
    /**
     * 金额符号
     */
    @BindView(R.id.update_symbol_money_tv)
    TextView mUpdateSymbolMoneyTv;
    /**
     * 金额
     */
    @BindView(R.id.update_money_edit)
    EditText mUpdateMoneyEdit;
    /**
     * 日期
     */
    @BindView(R.id.update_date_edit)
    EditText mUpdateDateEdit;
    /**
     * 账户
     */
    @BindView(R.id.update_account_sp)
    Spinner mUpdateAccountSp;
    /**
     * sp,定义一个字符串数组来存储下拉框每个item要显示的文本
     */
    private String[] mSpItem = {"支付宝", "微信", "银行卡", "信用卡", "其他"};
    /**
     * sp的简单适配器
     */
    private ArrayAdapter<String> mSpAdapter;
    /**
     * 备注
     */
    @BindView(R.id.update_remarks_edit)
    EditText mUpdateRemarksEdit;
    /**
     * 修改按钮
     */
    @BindView(R.id.update_btn)
    LinearLayout mUpdateBtn;

    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    private ContentValues mCv;// 存储工具栏
    private int num = 0;//查询返回值
    /**
     * 当前登录的用户ID
     */
    private int mUserId;

    /**
     * 时间
     * 定义显示时间控件
     */
    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    /**
     * 修改后保存的数据
     */
    private String mCategory, mMoney, mDaytime, mAccount, mRemarks;
    private int mId;

    /**
     * 注册EventBus
     */
    @Override
    protected void startCreate() {
        super.startCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        // 软键盘会覆盖在屏幕上面，而不会把你的布局顶上去
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_update;
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
        mUpdateBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 修改
        mUpdateBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 获取修改后的信息
                String mUpdateMoney = mUpdateMoneyEdit.getText().toString();
                mDaytime = mUpdateDateEdit.getText().toString();
                mRemarks = mUpdateRemarksEdit.getText().toString();
                // 规范过得出的金额
                String rightMoney;
                if (TextUtils.isEmpty(mUpdateMoney)) {
                    Toast.makeText(UpdateActivity.this, "请输入金额",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (mUpdateMoney.indexOf('.') == 0) {
                    Toast.makeText(UpdateActivity.this, "请输入正确的金额",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (mUpdateMoney.lastIndexOf('.') == (mUpdateMoney.length() - 1)) {
                    Toast.makeText(UpdateActivity.this, "请输入正确的金额",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //计算小数点出现的个数
                    int dotNum = 0;
                    // 将金额字符串转为数组
                    char[] moneyArray = mUpdateMoney.toCharArray();
                    for (int k = 0; k < moneyArray.length; k++) {
                        if (moneyArray[k] == '.') {
                            dotNum++;
                        }
                    }
                    // 记录小数点后两位
                    if (dotNum == 1) {
                        //返回某个指定的字符串值在字符串中首次出现的位置
                        int dotFirst = mUpdateMoney.indexOf('.');
                        //字符串的长度
                        int length = mUpdateMoney.length();
                        if (length - dotFirst == 2) {
                            rightMoney = mUpdateMoney.substring(0,
                                    mUpdateMoney.indexOf('.'))
                                    + mUpdateMoney.substring(mUpdateMoney.indexOf('.'),
                                    mUpdateMoney.indexOf('.') + 2);
                        } else {
                            rightMoney = mUpdateMoney.substring(0,
                                    mUpdateMoney.indexOf('.'))
                                    + mUpdateMoney.substring(mUpdateMoney.indexOf('.'),
                                    mUpdateMoney.indexOf('.') + 3);
                        }
                    } else {
                        rightMoney = mUpdateMoney;
                    }
                }
                //给金额加上正负号
                mMoney = mUpdateSymbolMoneyTv.getText().toString() + rightMoney;
                //把int操作用户名转换成string
                String userIdString = String.valueOf(mUserId);
                if ((mUpdateSymbolMoneyTv.getText().toString()).equals("-")) {
                    // 修改数据
                    // 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
                    mCv = new ContentValues();
                    // cv.put("aoid", up_id);
                    mCv.put("aocategory", mCategory);
                    mCv.put("aomoney", rightMoney);
                    mCv.put("aoaccount", mAccount);
                    mCv.put("aoremarks", mRemarks);
                    mCv.put("aotime", mDaytime);
                    // 修改数据，返回修改成功的行数，失败则返回0
                    num = mDb.update("expenditure", mCv, "aoid=? and aouserid=?",
                            new String[]{mId + "", userIdString + ""});
                    if (num > 0) {
                        Toast.makeText(UpdateActivity.this,
                                "修改成功" + num, Toast.LENGTH_SHORT).show();
                        DataBean dataBean = new DataBean(mCategory, mMoney,
                                mAccount, mRemarks, mDaytime, mId,
                                mUserId);
                        EventBus.getDefault().post(new UpdateSearchEvent(dataBean));
                        EventBus.getDefault().post(new UpdateDetailsEvent(dataBean));
                        finish();
                    } else {
                        Toast.makeText(UpdateActivity.this,
                                "修改失败" + num, Toast.LENGTH_SHORT).show();
                    }
                } else if ((mUpdateSymbolMoneyTv.getText().toString()).equals("+")) {
                    // 修改数据
                    // 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
                    mCv = new ContentValues();
                    mCv.put("aicategory", mCategory);
                    mCv.put("aimoney", rightMoney);
                    mCv.put("aiaccount", mAccount);
                    mCv.put("airemarks", mRemarks);
                    mCv.put("aitime", mDaytime);
                    // 修改数据，返回修改成功的行数，失败则返回0
                    num = mDb.update("income", mCv, "aiid=? and aiuserid=?",
                            new String[]{mId + "", userIdString + ""});
                    if (num > 0) {
                        Toast.makeText(UpdateActivity.this,
                                "修改成功" + num, Toast.LENGTH_SHORT).show();
                        DataBean dataBean = new DataBean(mCategory, mMoney,
                                mAccount, mRemarks, mDaytime, mId,
                                mUserId);
                        EventBus.getDefault().post(new UpdateSearchEvent(dataBean));
                        EventBus.getDefault().post(new UpdateDetailsEvent(dataBean));
                        finish();
                    } else {
                        Toast.makeText(UpdateActivity.this,
                                "修改失败" + num, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateActivity.this, "修改失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // sp账户
        // 设置下拉列表的条目被选择监听器
        mUpdateAccountSp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // 设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
                mAccount = mSpItem[arg2];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // 点击"日期"按钮布局 设置日期
        mUpdateDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
        setSp();
        showData();
    }


    /**
     * 初始化数据库
     */
    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
    }

    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        //获取SharedPreferences对象
        mUserId = SpManager.get().getUserId();
    }

    public void updateDate() {
        // 更新EditText控件日期 小于10加0
        mUpdateDateEdit.setText(new StringBuilder()
                .append(mYear)
                .append("")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
                        : (mMonth + 1)).append("")
                .append((mDay < 10) ? "0" + mDay : mDay));
    }

    private void showDatePicker() {
        // 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
        String nowDate=mUpdateDateEdit.getText().toString().trim();
        mYear=Integer.parseInt(nowDate.substring(0,4));
        mMonth=Integer.parseInt(nowDate.substring(4,6));
        mDay=Integer.parseInt(nowDate.substring(6,8));
        // 创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                AlertDialog.THEME_HOLO_LIGHT, DateListener,mYear,mMonth-1,mDay);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener DateListener = new DatePickerDialog.OnDateSetListener() {
        /**
         * params：view：该事件关联的组件
         * params：year：当前选择的年
         * params：month：当前选择的月
         * params：day：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int year, int month,
                              int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            // 更新日期
            updateDate();
        }
    };

    public void setSp() {
        // 定义数组适配器，利用系统布局文件
        mSpAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mSpItem);
        // 定义下拉框的样式
        mSpAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUpdateAccountSp.setAdapter(mSpAdapter);
    }

    /**
     * 获取传递过来的数据并显示
     */
    public void showData() {
        mId = dataBean.getmId();
        mUpdateCategoryTv.setText(dataBean.getmCategory());
        mCategory = dataBean.getmCategory();
        mUpdateSymbolMoneyTv.setText(dataBean.getmMoney().substring(0, 1));
        mUpdateMoneyEdit.setText(dataBean.getmMoney().substring(1));
        mUpdateMoneyEdit.setSelection(mUpdateMoneyEdit.length());
        mUpdateDateEdit.setText(dataBean.getmDate());
        String sd_account = dataBean.getmAccount();
        for (int j = 0; j < mSpItem.length; j++) {
            if (sd_account.equals(mSpItem[j])) {
                // 设置sp的值等于传递过来的值
                mUpdateAccountSp.setSelection(j, true);
                mAccount = mSpItem[j];
                break;
            }
        }
        mUpdateRemarksEdit.setText(dataBean.getmRemarks());
        mUpdateRemarksEdit.setSelection(mUpdateRemarksEdit.length());// 将光标移至文字末尾
    }

    //EventBus的黏性sticky = true
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDataEvent(SearchUpdateEvent event) {
        dataBean = event.getDataBean();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDetailsUpdateEvent(DetailsUpdateEvent event) {
        dataBean = event.getDataBean();
    }

    /**
     * 解注册
     */
    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().unregister(this);
    }

}
