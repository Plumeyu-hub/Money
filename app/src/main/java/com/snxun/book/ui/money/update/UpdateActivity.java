package com.snxun.book.ui.money.update;

import android.app.Activity;
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
import com.snxun.book.ui.money.bean.DataBean;
import com.snxun.book.ui.money.details.DetailsFragment;
import com.snxun.book.utils.sp.SpManager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateActivity extends BaseActivity {

    private static final String EXTRA_ACCOUNT_BEAN = "EXTRA_ACCOUNT_BEAN";
    private static final String EXTRA_POSITION = "EXTRA_POSITION";

    public static void startForResult(Activity activity, DataBean dataBean, int position, int requestCode) {
        Intent starter = new Intent(activity, UpdateActivity.class);
        starter.putExtra(EXTRA_ACCOUNT_BEAN, dataBean);
        starter.putExtra(EXTRA_POSITION, position);
        activity.startActivityForResult(starter, requestCode);
    }


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
    /**
     * 当前登录的用户ID
     */
    private int mUserId;

    // 传递
    public int position = 0;

    // sp,定义一个字符串数组来存储下拉框每个item要显示的文本
    private String[] spaccItems = {"支付宝", "微信", "银行卡", "信用卡", "其他"};

    // 时间
    // 定义显示时间控件
    private Calendar mCalendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;

    // 修改后保存的
    private String mCategory, mMoney, mDaytime, mAccount, mRemarks;
    private int mId;

    private ContentValues cv;// 存储工具栏
    private int num = 0;

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
                // TODO Auto-generated method stub
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
                    cv = new ContentValues();
                    // cv.put("aoid", up_id);
                    cv.put("aocategory", mCategory);
                    cv.put("aomoney", rightMoney);
                    cv.put("aoaccount", mAccount);
                    cv.put("aoremarks", mRemarks);
                    cv.put("aotime", mDaytime);
                    // 修改数据，返回修改成功的行数，失败则返回0
                    num = mDb.update("expenditure", cv, "aoid=? and aouserid=?",
                            new String[]{mId + "", userIdString + ""});
                    if (num > 0) {
                        Toast.makeText(UpdateActivity.this,
                                "修改成功" + num, Toast.LENGTH_SHORT).show();
                        DataBean dataBean = new DataBean(mCategory, mMoney,
                                mAccount, mRemarks, mDaytime, mId,
                                mUserId);
                        Intent i = new Intent();
                        i.putExtra("updata", dataBean);
                        i.putExtra("position", position);
                        setResult(DetailsFragment.DETAIL_UPDATE_RESULT_CODE, i);
                        finish();
                    } else {
                        Toast.makeText(UpdateActivity.this,
                                "修改失败" + num, Toast.LENGTH_SHORT).show();
                    }
                } else if ((mUpdateSymbolMoneyTv.getText().toString()).equals("+")) {
                    // 修改数据
                    // 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
                    cv = new ContentValues();
                    cv.put("aicategory", mCategory);
                    cv.put("aimoney", rightMoney);
                    cv.put("aiaccount", mAccount);
                    cv.put("airemarks", mRemarks);
                    cv.put("aitime", mDaytime);
                    // 修改数据，返回修改成功的行数，失败则返回0
                    num = mDb.update("income", cv, "aiid=? and aiuserid=?",
                            new String[]{mId + "", userIdString + ""});
                    if (num > 0) {
                        Toast.makeText(UpdateActivity.this,
                                "修改成功" + num, Toast.LENGTH_SHORT).show();
                        DataBean dataBean = new DataBean(mCategory, mMoney,
                                mAccount, mRemarks, mDaytime, mId,
                                mUserId);
                        Intent i = new Intent();
                        i.putExtra("updata", dataBean);
                        i.putExtra("position", position);
                        setResult(DetailsFragment.DETAIL_UPDATE_RESULT_CODE, i);
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
                mAccount = spaccItems[arg2];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // 时间
        mCalendar = Calendar.getInstance();
        // 点击"日期"按钮布局 设置日期
        mUpdateDateEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateActivity.this,
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
                                mUpdateDateEdit.setText(new StringBuilder()
                                        .append(mYear)
                                        // .append("-")
                                        .append((mMonth + 1) < 10 ? "0"
                                                + (mMonth + 1)
                                                : (mMonth + 1))
                                        // .append("-")
                                        .append((mDay < 10) ? "0" + mDay
                                                : mDay));
                            }
                        }, mCalendar.get(Calendar.YEAR), mCalendar
                        .get(Calendar.MONTH), mCalendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
        showDate();
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

    //maccountInformationDaytimeEdit可以获取焦点，但是不会显示软键盘
    //maccountInformationDaytimeEdit.setInputType(EditorInfo.TYPE_NULL);
    private void showDate() {
        getData();// 获取传递过来的数据并显示
    }


    public void getData() {
        Intent i = this.getIntent();
        if (i != null) {
            DataBean dataBean = (DataBean) i.getSerializableExtra(EXTRA_ACCOUNT_BEAN);
            mId = dataBean.getmId();
            System.out.println(mId);
            mUpdateCategoryTv.setText(dataBean.getmCategory());
            mCategory = dataBean.getmCategory();
            mUpdateSymbolMoneyTv.setText(dataBean.getmMoney().substring(0, 1));
            mUpdateMoneyEdit.setText(dataBean.getmMoney().substring(1));
            mUpdateMoneyEdit.setSelection(mUpdateMoneyEdit.length());
            mUpdateDateEdit.setText(dataBean.getmDate());
            String sd_account = dataBean.getmAccount();
            for (int j = 0; j < spaccItems.length; j++) {
                if (sd_account.equals(spaccItems[j])) {
                    // 定义数组适配器，利用系统布局文件
                    ArrayAdapter<String> spacc_adapter1 = new ArrayAdapter<String>(
                            this, android.R.layout.simple_spinner_item,
                            spaccItems);
                    // 定义下拉框的样式
                    spacc_adapter1
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mUpdateAccountSp.setAdapter(spacc_adapter1);
                    // 设置sp的值等于传递过来的值
                    mUpdateAccountSp.setSelection(j, true);
                    mAccount = spaccItems[j];
                    break;
                }
            }
            mUpdateRemarksEdit.setText(dataBean.getmRemarks());
            mUpdateRemarksEdit.setSelection(mUpdateRemarksEdit.length());// 将光标移至文字末尾
            position = i.getIntExtra(EXTRA_POSITION, 0);
        }

    }
}
