package com.snxun.book.ui.money.update;

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
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.snxun.book.ui.money.bean.DataBean;
import com.snxun.book.ui.money.details.DetailsFragment;
import com.snxun.book.R;

import java.util.Calendar;

public class UpdateActivity extends Activity {

    private static final String EXTRA_ACCOUNT_BEAN = "EXTRA_ACCOUNT_BEAN";
    private static final String EXTRA_POSITION = "EXTRA_POSITION";

    public static void startForResult(Activity activity, DataBean dataBean, int position, int requestCode) {
        Intent starter = new Intent(activity, UpdateActivity.class);
        starter.putExtra(EXTRA_ACCOUNT_BEAN, dataBean);
        starter.putExtra(EXTRA_POSITION, position);
        activity.startActivityForResult(starter, requestCode);
    }
    
    /** 类别 */
    private TextView mAccountInformationCategoryTv;
    //金额的符号
    private TextView mAccountInformationSymbolTv;
    //金额，时间，备注
    private EditText mAccountInformationMoneyEdit, mAccountInformationRemarksEdit, maccountInformationDaytimeEdit;
    //账户
    private Spinner mAccountInformationAccountSp;

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

    // 操作用户名
    private int mUserId;

    // 数据库
    private SQLiteDatabase db;// 数据库对象
    private ContentValues cv;// 存储工具栏
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 修改按钮
        LinearLayout mAccountInformationBottomLin;
        // 操作用户名
        SharedPreferences mSharedPreferences;
        SharedPreferences.Editor mEditor;

        // 软键盘会覆盖在屏幕上面，而不会把你的布局顶上去
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_account_information);

        // 操作用户名
        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
        mUserId = mSharedPreferences.getInt("userid", 0);

        // 数据库
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);

        mAccountInformationCategoryTv = (TextView) findViewById(R.id.account_information_category_tv);
        mAccountInformationSymbolTv = (TextView) findViewById(R.id.account_information_symbol_tv);
        mAccountInformationMoneyEdit = (EditText) findViewById(R.id.account_information_money_edit);
        maccountInformationDaytimeEdit = (EditText) findViewById(R.id.account_information_daytime_edit);
        mAccountInformationAccountSp = (Spinner) findViewById(R.id.account_information_account_sp);
        mAccountInformationRemarksEdit = (EditText) findViewById(R.id.account_information_remarks_edit);
        mAccountInformationBottomLin = (LinearLayout) findViewById(R.id.account_information_bottom_lin);

        //maccountInformationDaytimeEdit可以获取焦点，但是不会显示软键盘
        maccountInformationDaytimeEdit.setInputType(EditorInfo.TYPE_NULL);

        getData();// 获取传递过来的数据并显示

        // 修改
        mAccountInformationBottomLin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // 获取修改后的信息
				String mUpdateMoney = mAccountInformationMoneyEdit.getText().toString();
                mDaytime = maccountInformationDaytimeEdit.getText().toString();
                mRemarks = mAccountInformationRemarksEdit.getText().toString();
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
                mMoney = mAccountInformationSymbolTv.getText().toString() + rightMoney;
                //把int操作用户名转换成string
                String userIdString = String.valueOf(mUserId);
                if ((mAccountInformationSymbolTv.getText().toString()).equals("-")) {
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
                    num = db.update("expenditure", cv, "aoid=? and aouserid=?",
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
                } else if ((mAccountInformationSymbolTv.getText().toString()).equals("+")) {
                    // 修改数据
                    // 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
                    cv = new ContentValues();
                    cv.put("aicategory", mCategory);
                    cv.put("aimoney", rightMoney);
                    cv.put("aiaccount", mAccount);
                    cv.put("airemarks", mRemarks);
                    cv.put("aitime", mDaytime);
                    // 修改数据，返回修改成功的行数，失败则返回0
                    num = db.update("income", cv, "aiid=? and aiuserid=?",
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
        mAccountInformationAccountSp.setOnItemSelectedListener(new OnItemSelectedListener() {

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
        maccountInformationDaytimeEdit.setOnClickListener(new OnClickListener() {
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
                                maccountInformationDaytimeEdit.setText(new StringBuilder()
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

    public void click(View v) {
        switch (v.getId()) {
            case R.id.account_information_left_tv:
                finish();
                break;

            default:
                break;
        }
    }

    public void getData() {
        Intent i = this.getIntent();
        if (i != null) {
            DataBean dataBean = (DataBean) i.getSerializableExtra(EXTRA_ACCOUNT_BEAN);
            mId = dataBean.getId();
            System.out.println(mId);
            mAccountInformationCategoryTv.setText(dataBean.getCategory());
            mCategory = dataBean.getCategory();
            mAccountInformationSymbolTv.setText(dataBean.getMoney().substring(0, 1));
            mAccountInformationMoneyEdit.setText(dataBean.getMoney().substring(1));
            mAccountInformationMoneyEdit.setSelection(mAccountInformationMoneyEdit.length());
            maccountInformationDaytimeEdit.setText(dataBean.getDaytime());
            String sd_account = dataBean.getAccount();
            for (int j = 0; j < spaccItems.length; j++) {
                if (sd_account.equals(spaccItems[j])) {
                    // 定义数组适配器，利用系统布局文件
                    ArrayAdapter<String> spacc_adapter1 = new ArrayAdapter<String>(
                            this, android.R.layout.simple_spinner_item,
                            spaccItems);
                    // 定义下拉框的样式
                    spacc_adapter1
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mAccountInformationAccountSp.setAdapter(spacc_adapter1);
                    // 设置sp的值等于传递过来的值
                    mAccountInformationAccountSp.setSelection(j, true);
                    mAccount = spaccItems[j];
                    break;
                }
            }
            mAccountInformationRemarksEdit.setText(dataBean.getRemarks());
            mAccountInformationRemarksEdit.setSelection(mAccountInformationRemarksEdit.length());// 将光标移至文字末尾
            position = i.getIntExtra(EXTRA_POSITION, 0);
        }

    }
}
