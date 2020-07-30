package com.snxun.book.ui.money.update;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.snxun.book.bean.update.UpdateBean;
import com.snxun.book.config.Constants;
import com.snxun.book.db.DbFactory;
import com.snxun.book.event.DetailsUpdateEvent;
import com.snxun.book.event.RefreshEvent;
import com.snxun.book.event.SearchUpdateEvent;
import com.snxun.book.greendaolib.table.BillTable;
import com.snxun.book.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private BillTable mBillTable;
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
    @BindView(R.id.update_mode_sp)
    Spinner mUpdateModeSp;
    /**
     * sp,定义一个字符串数组来存储下拉框每个item要显示的文本
     */
    private String[] mSpItem = {"支付宝", "微信", "银行卡", "信用卡", "其他"};
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
     * 时间
     * 定义显示时间控件
     */
    private int mYear;
    private int mMonth;
    private int mDay;
    /**
     * 修改后保存的数据
     */
    private String mDate, mMode, mRemarks;
    private Long mId, mMoney;

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
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 获取修改后的信息
                mDate = mUpdateDateEdit.getText().toString();
                mRemarks = mUpdateRemarksEdit.getText().toString();
                String MoneyEdit = mUpdateMoneyEdit.getText().toString();
                // 规范过得出的金额
                String moneyStr;
                if (TextUtils.isEmpty(MoneyEdit)) {
                    Toast.makeText(UpdateActivity.this, "请输入金额",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (MoneyEdit.indexOf('.') == 0) {
                    Toast.makeText(UpdateActivity.this, "请输入正确的金额",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (MoneyEdit.lastIndexOf('.') == (MoneyEdit.length() - 1)) {
                    Toast.makeText(UpdateActivity.this, "请输入正确的金额",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //计算小数点出现的个数
                    int dotNum = 0;
                    // 将金额字符串转为数组
                    char[] moneyArray = MoneyEdit.toCharArray();
                    for (char c : moneyArray) {
                        if (c == '.') {
                            dotNum++;
                        }
                    }
                    // 记录小数点后两位
                    if (dotNum == 1) {
                        //返回某个指定的字符串值在字符串中首次出现的位置
                        int dotFirst = MoneyEdit.indexOf('.');
                        //字符串的长度
                        int length = MoneyEdit.length();
                        if (length - dotFirst == 2) {
                            moneyStr = MoneyEdit.substring(0,
                                    MoneyEdit.indexOf('.'))
                                    + MoneyEdit.substring(MoneyEdit.indexOf('.'),
                                    MoneyEdit.indexOf('.') + 2);
                        } else {
                            moneyStr = MoneyEdit.substring(0,
                                    MoneyEdit.indexOf('.'))
                                    + MoneyEdit.substring(MoneyEdit.indexOf('.'),
                                    MoneyEdit.indexOf('.') + 3);
                        }
                    } else {
                        moneyStr = MoneyEdit;
                    }
                }
                mMoney = Long.valueOf(moneyStr);
                UpdateBean updateBean = new UpdateBean(mMoney, mDate, mMode, mRemarks);
                boolean isSaveSuccess = DbFactory.create().updateBillInfo(mId, updateBean);
                if (!isSaveSuccess) {
                    ToastUtils.showShort(getContext(), "数据保存失败");
                    return;
                }
                ToastUtils.showShort(getContext(), "数据保存成功");
                boolean refresh = true;
                EventBus.getDefault().post(new RefreshEvent(refresh));
                finish();
            }
        });


        // sp账户
        // 设置下拉列表的条目被选择监听器
        mUpdateModeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // 设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
                mMode = mSpItem[arg2];
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
        setSp();
        showData();
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
        String nowDate = mUpdateDateEdit.getText().toString().trim();
        mYear = Integer.parseInt(nowDate.substring(0, 4));
        mMonth = Integer.parseInt(nowDate.substring(4, 6));
        mDay = Integer.parseInt(nowDate.substring(6, 8));
        // 创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                AlertDialog.THEME_HOLO_LIGHT, DateListener, mYear, mMonth - 1, mDay);
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
        //sp的简单适配器
        ArrayAdapter<String> mSpAdapter;
        // 定义数组适配器，利用系统布局文件
        mSpAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mSpItem);
        // 定义下拉框的样式
        mSpAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUpdateModeSp.setAdapter(mSpAdapter);
    }

    /**
     * 获取传递过来的数据并显示
     */
    public void showData() {
        mId = mBillTable.getId();
        mUpdateCategoryTv.setText(mBillTable.getCategory());
        int symbol = mBillTable.getSymbol();
        mUpdateSymbolMoneyTv.setText(String.valueOf((symbol == Constants.EXPENDITURE ? "-" : "+")));
        mUpdateMoneyEdit.setText(String.valueOf(mBillTable.getMoney()));
        mUpdateDateEdit.setText(mBillTable.getDate());
        mUpdateRemarksEdit.setText(mBillTable.getRemark());
        String modeStr = mBillTable.getMode();
        for (int i = 0; i < mSpItem.length; i++) {
            if (modeStr.equals(mSpItem[i])) {
                // 设置sp的值等于传递过来的值
                mUpdateModeSp.setSelection(i, true);
                mMode = mSpItem[i];
                break;
            }
        }
        mUpdateMoneyEdit.setSelection(mUpdateMoneyEdit.length());// 将光标移至文字末尾
        mUpdateRemarksEdit.setSelection(mUpdateRemarksEdit.length());// 将光标移至文字末尾
    }

    //EventBus的黏性sticky = true
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSearchUpdateEvent(SearchUpdateEvent event) {
        mBillTable = event.getBillTable();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDetailsUpdateEvent(DetailsUpdateEvent event) {
        mBillTable = event.getBillTable();
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
