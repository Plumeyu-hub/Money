package com.snxun.book.ui.money.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseFragment;
import com.snxun.book.config.Constants;
import com.snxun.book.db.DbFactory;
import com.snxun.book.event.AddDetailsEvent;
import com.snxun.book.event.SubmitRemarkEvent;
import com.snxun.book.ui.money.adapter.RvGrAdapter;
import com.snxun.book.ui.money.bean.RvGrBean;
import com.snxun.book.utils.ToastUtils;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/07/22
 */
public class AddInFragment extends BaseFragment {
    /**
     * 类别
     */
    String mCategory = "";
    /**
     * 金额输入
     */
    @BindView(R.id.add_in_money_edit)
    EditText mAddInMoneyEdit;
    /**
     * 输入框输入过程中的金额
     */
    private String mMoneyEdit;
    /**
     * 金额删除
     */
    @BindView(R.id.add_in_money_delect_btn)
    ImageView mAddInMoneyDeleteBtn;
    /**
     * sp账户选择框
     */
    @BindView(R.id.add_in_sp)
    Spinner mAddInSp;
    private String mMode = "支付宝";
    /**
     * 备注框
     */
    @BindView(R.id.add_in_submit_remark_btn)
    EditText mAddInSubmitRemarkBtn;
    /**
     * 日期选择
     */
    @BindView(R.id.add_in_date_edit)
    EditText mAddInDateEdit;
    /**
     * 时间
     */
    private int mYear;
    private int mMonth;
    private int mDay;
    /**
     * 确定按钮
     */
    @BindView(R.id.add_in_yes_btn)
    TextView mAddInYesBtn;

    /**
     * 1
     */
    @BindView(R.id.add_in_one_btn)
    TextView mAddInOneBtn;
    /**
     * 2
     */
    @BindView(R.id.add_in_two_btn)
    TextView mAddInTwoBtn;
    /**
     * 3
     */
    @BindView(R.id.add_in_three_btn)
    TextView mAddInThreeBtn;
    /**
     * 4
     */
    @BindView(R.id.add_in_four_btn)
    TextView mAddInFourBtn;
    /**
     * 5
     */
    @BindView(R.id.add_in_five_btn)
    TextView mAddInFiveBtn;
    /**
     * 6
     */
    @BindView(R.id.add_in_six_btn)
    TextView mAddInSixBtn;
    /**
     * 7
     */
    @BindView(R.id.add_in_seven_btn)
    TextView mAddInSevenBtn;
    /**
     * 8
     */
    @BindView(R.id.add_in_eight_btn)
    TextView mAddInEightBtn;
    /**
     * 9
     */
    @BindView(R.id.add_in_nine_btn)
    TextView mAddInNineBtn;
    /**
     * 0
     */
    @BindView(R.id.add_in_zero_btn)
    TextView mAddInZeroBtn;
    /**
     * 小数点
     */
    @BindView(R.id.add_in_dot_btn)
    TextView mAddInDotBtn;

    /**
     * Gr
     */
    @BindView(R.id.add_in_gv)
    RecyclerView mAddInGv;
    private RvGrAdapter mRvGrAdapter;//自定义适配器，继承RecyclerView.Adapter
    private List<RvGrBean> mGrDataList;

    /**
     * 当前登录的用户
     */
    private String mAccount;

    /**
     * 记录上次选择的图片Id
     */
    private int mLast;
    String[] iconName = {"工资", "奖金", "兼职收入", "意外所得", "收债", "借入",
            "投资回收", "投资收益", "礼金", "其他"};
    private int[] iconRes = {R.drawable.ic_wages_nor, R.drawable.ic_bonus_nor,
            R.drawable.ic_part_nor, R.drawable.ic_mis_nor, R.drawable.ic_collect_nor,
            R.drawable.ic_borrow_nor, R.drawable.ic_sell_nor, R.drawable.ic_financial_nor,
            R.drawable.ic_gifts_nor, R.drawable.ic_other_nor};

    private static final String EXTRA_TEXT = "extra_text";

    public static AddInFragment newInstance(String from) {
        AddInFragment addInFragment = new AddInFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TEXT, from);
        addInFragment.setArguments(bundle);
        return addInFragment;
    }

    @Override
    protected void startCreate() {
        super.startCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_in;
    }

    @Override
    protected void findViews(View view) {
        ButterKnife.bind(this, view);
        //可以获取焦点，但是不会显示软键盘;
        mAddInMoneyEdit.setInputType(EditorInfo.TYPE_NULL);
        mAddInSubmitRemarkBtn.setInputType(EditorInfo.TYPE_NULL);
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 初始化数据列表
        mGrDataList = new ArrayList<>();

        for (int i = 0; i < iconName.length; i++) {
            mGrDataList.add(new RvGrBean(iconName[i], iconRes[i], false));
        }

        mRvGrAdapter = new RvGrAdapter(getContext(), mGrDataList);
        //第二参数是控制显示多少列,第三个参数是控制滚动方向和LinearLayout一样,第四个参数是控制是否反向排列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mAddInGv.setLayoutManager(layoutManager);
        mAddInGv.setHasFixedSize(true);
        mAddInGv.setAdapter(mRvGrAdapter);
    }

    @Override
    protected void setListeners(View view) {
        super.setListeners(view);

        int[] iconResSel = {R.drawable.ic_wages_sel, R.drawable.ic_bonus_sel,
                R.drawable.ic_part_sel, R.drawable.ic_mis_sel, R.drawable.ic_collect_sel,
                R.drawable.ic_borrow_sel, R.drawable.ic_sell_sel,
                R.drawable.ic_financial_sel, R.drawable.ic_gifts_sel,
                R.drawable.ic_other_in_sel};

        mRvGrAdapter.setOnItemClickListener(new RvGrAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                mGrDataList.get(mLast).setImgResId(iconRes[mLast]);
                mGrDataList.get(mLast).setIsSelected(false);
                mGrDataList.get(position).setImgResId(iconResSel[position]);
                mGrDataList.get(position).setIsSelected(true);
                mLast = position;
                mCategory = iconName[position];
                mRvGrAdapter.notifyDataSetChanged();
            }
        });


        mAddInOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "1";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "2";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInThreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "3";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInFourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "4";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInFiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "5";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInSixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "6";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInSevenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "7";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInEightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "8";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInNineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "9";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInZeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "0";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInDotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + ".";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInMoneyDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                if (mMoneyEdit.length() > 0) {
                    mMoneyEdit = mMoneyEdit
                            .substring(0, mMoneyEdit.length() - 1);
                }
                mAddInMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddInYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //符号 收入1
                int mSymbol = Constants.INCOME;
                // 时间
                String mDateEdit = mAddInDateEdit.getText().toString();
                String mDate;
                mDate = mDateEdit.replace("-", "");
                // 备注
                String mRemark = mAddInSubmitRemarkBtn.getText().toString();
                // 获取到金额
                mMoneyEdit = mAddInMoneyEdit.getText().toString().trim();
                String mMoney = ""; // 保留两位小数点的字符串(最后存入数据库保存的)
                int dot = 0;// 小数点出现的个数
                // 将金额字符串转为数组
                char[] charMoney = mMoneyEdit.toCharArray();
                for (int k = 0; k < charMoney.length; k++) {
                    if (charMoney[k] == '.') {
                        dot++;
                    }
                }
                if (TextUtils.isEmpty(mCategory)) {
                    ToastUtils.showShort(getContext(), "请选择账单的类别");
                    return;
                }
                if (TextUtils.isEmpty(mMoneyEdit)) {
                    ToastUtils.showShort(getContext(), "请输入账单的金额");
                    return;
                }
                if (dot > 1) {
                    ToastUtils.showShort(getContext(), "请输入正确的金额");
                    return;
                }
                if (mMoneyEdit.indexOf('.') == 0) {//小数点首次出现的位置是第一位
                    ToastUtils.showShort(getContext(), "请输入正确的金额");
                    return;
                }
                if (mMoneyEdit.lastIndexOf('.') == (mMoneyEdit.length() - 1)) {//小数点最后一次出现的位置是最后一位
                    ToastUtils.showShort(getContext(), "请输入正确的金额");
                    return;
                }
                // 记录小数点后两位ao_dmoney
                if (dot == 1) {
                    int length = mMoneyEdit.length();
                    if (length - dot == 2) {
                        mMoney = mMoneyEdit
                                .substring(0, mMoneyEdit.indexOf('.'))
                                + mMoneyEdit.substring(mMoneyEdit.indexOf('.'),
                                mMoneyEdit.indexOf('.') + 2);
                        return;
                    }
                    mMoney = mMoneyEdit
                            .substring(0, mMoneyEdit.indexOf('.'))
                            + mMoneyEdit.substring(mMoneyEdit.indexOf('.'),
                            mMoneyEdit.indexOf('.') + 3);
                } else {
                    mMoney = mMoneyEdit;
                }
                Long mLongMoney = Long.parseLong(mMoney);

                add(mCategory, mLongMoney, mDate, mMode, mRemark, mSymbol, mAccount);
            }
        });

        mAddInSubmitRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), SubmitRemarkActivity.class));
                SubmitRemarkActivity.start(getContext());
            }
        });

        // 点击"日期"按钮布局 设置日期
        mAddInDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        showUserInfo();
        setSp();
        setDate();
    }

    /**
     * 获取当前登录用户账户
     */
    private void showUserInfo() {
        mAccount = SpManager.get().getUserAccount();
    }

    public void setSp() {
        // 定义一个字符串数组来存储下拉框每个item要显示的文本
        final String[] mAccountSpItem = {"支付宝", "微信", "银行卡", "信用卡", "其他"};
        // 定义数组适配器，利用系统布局文件
        ArrayAdapter<String> mSpAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_spinner_item, mAccountSpItem);
        // 定义下拉框的样式
        mSpAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 设置下拉列表的条目被选择监听器
        mAddInSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // 设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
                mMode = mAccountSpItem[arg2];
                // Toast.makeText(AddActivity.this, items[arg2], 0).show();
                // 注意： 这句话的作用是当下拉列表刚显示出来的时候，数组中第0个文本不会显示Toast
                // 如果没有这句话，当下拉列表刚显示出来的时候，数组中第0个文本会显示Toast
                // arg0.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mAddInSp.setAdapter(mSpAdapter);
    }

    public void setDate() {
        // 时间
        Calendar calendar = Calendar.getInstance();
        // 设置初始时间与当前系统时间一致
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDate();
    }

    public void updateDate() {
        // 更新EditText控件日期 小于10加0
        mAddInDateEdit.setText(new StringBuilder()
                .append(mYear)
                .append("-")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1)
                        : (mMonth + 1)).append("-")
                .append((mDay < 10) ? "0" + mDay : mDay));
    }

    private void showDatePicker() {
        // 创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                AlertDialog.THEME_HOLO_LIGHT, DateListener, mYear, mMonth, mDay);
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
            // 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            mYear = year;
            mMonth = month;
            mDay = day;
            // 更新日期
            updateDate();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubmitRemarkEvent(SubmitRemarkEvent event) {
        if (!TextUtils.isEmpty(event.getText())) {
            mAddInSubmitRemarkBtn.setText(event.getText());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 添加账单
     *
     * @param category 类别
     * @param money    金额
     * @param date     日期
     * @param mode     (金额扣除或增加的)账户或方式
     * @param remark   备注
     * @param symbol   符号（0为支出，1为收入）
     * @param account  用户账号
     */
    public void add(String category, Long money, String date, String mode, String remark, int symbol, String account) {
        long isSaveSuccess = DbFactory.create().saveBillInfo(category, money, date, mode, remark, symbol, account);
        if (isSaveSuccess < 0) {
            ToastUtils.showShort(getContext(), "数据保存失败");
            return;
        }
        ToastUtils.showShort(getContext(), "数据保存成功");
        EventBus.getDefault().post(new AddDetailsEvent(date));
        //该方法用于监听用户点击返回键的事件，也可以调用它来关闭view。
        Objects.requireNonNull(getActivity()).onBackPressed();
    }
}
