package com.snxun.book.ui.my.budget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.login.LoginActivity;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBudgetActivity extends BaseActivity {
    /**
     * 返回按钮
     */
    @BindView(R.id.add_budget_back_btn)
    ImageView mAddBudgetBackBtn;
    /**
     * 确认添加按钮
     */
    @BindView(R.id.add_budget_btn)
    TextView mAddBudgetBtn;
    /**
     * 金额
     */
    @BindView(R.id.add_budget_money_et)
    EditText mAddBudgetMoneyEt;
    /**
     * 日期
     */
    @BindView(R.id.add_budget_date_et)
    EditText mAddBudgetDateEt;
    /**
     * 备注
     */
    @BindView(R.id.add_budget_remarks_et)
    EditText mAddBudgetRemarksEt;

    /**
     * 获取et里的内容
     */
    private String mMoney, mDate, mRemarks;

    /**
     * 获取当前日期
     */
    private Calendar mCalendar = Calendar.getInstance();
    private int mYear = mCalendar.get(Calendar.YEAR);
    private int mMonth = mCalendar.get(Calendar.MONTH);
    private int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    /**
     * 存储工具栏
     */
    private ContentValues cv;

    private int num = 0;
    /**
     * 当前登录的用户ID
     */
    private int mUserId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_budget;
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
        mAddBudgetBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //确认添加
        mAddBudgetBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addBudget();
            }
        });

        //日期按钮
        mAddBudgetDateEt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
        setDate();
    }

    /**
     * 设置日期
     */
    private void setDate() {
        mAddBudgetDateEt
                .setText(new StringBuilder()
                        .append(mYear)
                        .append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
                                : (mMonth + 1)));
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
        mDb.execSQL("create table if not exists budget (id integer primary key,money text,time text,remarks text,userid integer)");
    }

    /**
     * 显示用户信息，获取当前登录的用户名
     */
    private void showUserInfo() {
        //获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SP_USER_CODE, MODE_PRIVATE);
        if (sharedPreferences != null) {// 判断文件是否存在
            mUserId = sharedPreferences.getInt(LoginActivity.SP_USERID_CODE, 0);
        }
    }


    public void addBudget() {
        mMoney = mAddBudgetMoneyEt.getText().toString();
        String rightMoney;
        mDate = mAddBudgetDateEt.getText().toString();
        mRemarks = mAddBudgetRemarksEt.getText().toString();

        if (TextUtils.isEmpty(mMoney)) {
            Toast.makeText(AddBudgetActivity.this, "请输入金额", Toast.LENGTH_LONG)
                    .show();
            return;
        } else if (mMoney.indexOf('.') == 0) {
            Toast.makeText(AddBudgetActivity.this, "请输入正确的金额",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (mMoney.lastIndexOf('.') == (mMoney.length() - 1)) {
            Toast.makeText(AddBudgetActivity.this, "请输入正确的金额",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            int dotNum = 0;// 小数点出现的个数
            // 将金额字符串转为数组
            char[] moneyChar = mMoney.toCharArray();
            for (int k = 0; k < moneyChar.length; k++) {
                if (moneyChar[k] == '.') {
                    dotNum++;
                }
            }
            // 记录小数点后两位
            if (dotNum == 1) {
                int m = mMoney.indexOf('.');
                int n = mMoney.length();
                if (n - m == 2) {
                    rightMoney = mMoney.substring(0,
                            mMoney.indexOf('.'))
                            + mMoney.substring(mMoney.indexOf('.'),
                            mMoney.indexOf('.') + 2);
                } else {
                    rightMoney = mMoney.substring(0,
                            mMoney.indexOf('.'))
                            + mMoney.substring(mMoney.indexOf('.'),
                            mMoney.indexOf('.') + 3);
                }
            } else {
                rightMoney = mMoney;
            }
        }
        String userId = String.valueOf(mUserId);
        // 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
        cv = new ContentValues();
        cv.put("money", rightMoney);
        cv.put("time", mDate);
        cv.put("remarks", mRemarks);
        cv.put("userid", userId);
        // 插入数据，成功返回当前行号，失败返回0
        num = (int) mDb.insert("budget", null, cv);
        if (num > 0) {
            Toast.makeText(this, "预算保存成功" + num, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "预算保存失败" + num, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDate() {
        // 创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(this,
                AlertDialog.THEME_HOLO_LIGHT, Datelistener, mYear, mMonth, mDay);
        dialog.show();

        // 只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup) Objects.requireNonNull(dialog.getWindow())
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
         * params：view 该事件关联的组件
         * params：year 当前选择的年
         * params：month 当前选择的月
         * params：day 当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int year, int month,
                              int day) {

            // 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            mYear = year;
            mMonth = month;
            mDay = day;
            // 更新日期
            updateDate();

        }

        // 当DatePickerDialog关闭时，更新日期显示
        private void updateDate() {
            // 在TextView上显示日期
            mAddBudgetDateEt
                    .setText(new StringBuilder()
                            .append(mYear)
                            .append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
                                    : (mMonth + 1)));
            mAddBudgetDateEt.setTextColor(getResources().getColor(
                    R.color.color_333333));
        }

    };

}