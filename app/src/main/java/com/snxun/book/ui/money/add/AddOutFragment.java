package com.snxun.book.ui.money.add;

import android.annotation.SuppressLint;
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
import com.snxun.book.db.DbFactory;
import com.snxun.book.event.AddDetailsEvent;
import com.snxun.book.event.SubmitRemarkEvent;
import com.snxun.book.ui.money.adapter.RvGrAdapter;
import com.snxun.book.ui.money.bean.BillBean;
import com.snxun.book.ui.money.bean.RvGrBean;
import com.snxun.book.utils.ToastUtils;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/07/22
 */
public class AddOutFragment extends BaseFragment {
    /**
     * 类别
     */
    String mCategory = "";
    /**
     * 金额输入
     */
    @BindView(R.id.add_out_money_edit)
    EditText mAddOutMoneyEdit;
    /**
     * 输入框输入过程中的金额
     */
    private String mMoneyEdit;
    /**
     * 金额删除
     */
    @BindView(R.id.add_out_money_delect_btn)
    ImageView mAddOutMoneyDeleteBtn;
    /**
     * sp账户选择框
     */
    @BindView(R.id.add_out_sp)
    Spinner mAddOutSp;
    private String mMode = "支付宝";
    /**
     * 备注框
     */
    @BindView(R.id.add_out_submit_remark_btn)
    EditText mAddOutSubmitRemarkBtn;
    //private String mRemark;
    /**
     * 日期选择
     */
    @BindView(R.id.add_out_date_edit)
    EditText mAddOutDateEdit;
    //private String mDate;
    // 定义显示时间控件
    private Calendar mCalendar;
    // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    /**
     * 确定按钮
     */
    @BindView(R.id.add_out_yes_btn)
    TextView mAddOutYesBtn;

    /**
     * 1
     */
    @BindView(R.id.add_out_one_btn)
    TextView mAddOutOneBtn;
    /**
     * 2
     */
    @BindView(R.id.add_out_two_btn)
    TextView mAddOutTwoBtn;
    /**
     * 3
     */
    @BindView(R.id.add_out_three_btn)
    TextView mAddOutThreeBtn;
    /**
     * 4
     */
    @BindView(R.id.add_out_four_btn)
    TextView mAddOutFourBtn;
    /**
     * 5
     */
    @BindView(R.id.add_out_five_btn)
    TextView mAddOutFiveBtn;
    /**
     * 6
     */
    @BindView(R.id.add_out_six_btn)
    TextView mAddOutSixBtn;
    /**
     * 7
     */
    @BindView(R.id.add_out_seven_btn)
    TextView mAddOutSevenBtn;
    /**
     * 8
     */
    @BindView(R.id.add_out_eight_btn)
    TextView mAddOutEightBtn;
    /**
     * 9
     */
    @BindView(R.id.add_out_nine_btn)
    TextView mAddOutNineBtn;
    /**
     * 0
     */
    @BindView(R.id.add_out_zero_btn)
    TextView mAddOutZeroBtn;
    /**
     * 小数点
     */
    @BindView(R.id.add_out_dot_btn)
    TextView mAddOutDotBtn;

    /**
     * Gr
     */
    @BindView(R.id.add_out_gv)
    RecyclerView mAddOutGv;
    private RvGrAdapter mRvGrAdapter;//自定义适配器，继承RecyclerView.Adapter
    private List<RvGrBean> mGrDataList;

    /**
     * 当前登录的用户ID
     */
    private String mAccount;

    private String[] iconName = {"餐厅", "食材", "外卖", "水果", "零食", "烟酒茶水",
            "住房", "水电煤", "交通", "汽车", "购物", "快递", "通讯", "鞋服饰品", "日用品", "美容",
            "还款", "投资", "工作", "数码", "学习", "运动", "娱乐", "医疗药品", "维修", "旅行", "社交",
            "公益捐赠", "宠物", "孩子", "长辈", "其他"};

    private static final String EXTRA_TEXT = "extra_text";

    public static AddOutFragment newInstance(String from) {
        AddOutFragment addInFragment = new AddOutFragment();
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
        return R.layout.fragment_add_out;
    }

    @Override
    protected void findViews(View view) {
        ButterKnife.bind(this, view);
        mAddOutMoneyEdit.setInputType(EditorInfo.TYPE_NULL);
        mAddOutSubmitRemarkBtn.setInputType(EditorInfo.TYPE_NULL);
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 初始化数据列表
        mGrDataList = new ArrayList<>();

        int[] iconRes = {R.drawable.selector_ic_tab_add_restaurant, R.drawable.selector_ic_tab_add_cook,
                R.drawable.selector_ic_tab_add_takeaway, R.drawable.selector_ic_tab_add_fruit, R.drawable.selector_ic_tab_add_snacks,
                R.drawable.selector_ic_tab_add_wine, R.drawable.selector_ic_tab_add_home, R.drawable.selector_ic_tab_add_house,
                R.drawable.selector_ic_tab_add_traffic, R.drawable.selector_ic_tab_add_car, R.drawable.selector_ic_tab_add_shopping,
                R.drawable.selector_ic_tab_add_post, R.drawable.selector_ic_tab_add_communication,
                R.drawable.selector_ic_tab_add_clothing, R.drawable.selector_ic_tab_add_daily, R.drawable.selector_ic_tab_add_beauty,
                R.drawable.selector_ic_tab_add_repayment, R.drawable.selector_ic_tab_add_investment,
                R.drawable.selector_ic_tab_add_office, R.drawable.selector_ic_tab_add_digital, R.drawable.selector_ic_tab_add_learn,
                R.drawable.selector_ic_tab_add_sport, R.drawable.selector_ic_tab_add_recreation,
                R.drawable.selector_ic_tab_add_medical, R.drawable.selector_ic_tab_add_maintenance,
                R.drawable.selector_ic_tab_add_travel, R.drawable.selector_ic_tab_add_social, R.drawable.selector_ic_tab_add_donate,
                R.drawable.selector_ic_tab_add_pet, R.drawable.selector_ic_tab_add_child, R.drawable.selector_ic_tab_add_elder,
                R.drawable.selector_ic_tab_add_other};
        for (int i = 0; i < iconName.length; i++) {
            mGrDataList.add(new RvGrBean(iconName[i], iconRes[i], false));
        }

        mRvGrAdapter = new RvGrAdapter(getContext(), mGrDataList);
        //第二参数是控制显示多少列,第三个参数是控制滚动方向和LinearLayout一样,第四个参数是控制是否反向排列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mAddOutGv.setLayoutManager(layoutManager);
        mAddOutGv.setHasFixedSize(true);
        mAddOutGv.setAdapter(mRvGrAdapter);
    }

    @Override
    protected void setListeners(View view) {
        super.setListeners(view);

        mRvGrAdapter.setOnItemClickListener(new RvGrAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                for (int i = 0; i < mGrDataList.size(); i++) {
                    mGrDataList.get(i).setIsSelected(i == position);
                    mCategory = iconName[position];
                }
                mRvGrAdapter.notifyDataSetChanged();
            }
        });

        mAddOutOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "1";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "2";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutThreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "3";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutFourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "4";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutFiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "5";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutSixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "6";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutSevenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "7";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutEightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "8";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutNineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "9";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutZeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + "0";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutDotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                mMoneyEdit = mMoneyEdit + ".";
                if (mMoneyEdit.length() > 6) {
                    mMoneyEdit = mMoneyEdit.substring(0, 6);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutMoneyDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
                if (mMoneyEdit.length() > 0) {
                    mMoneyEdit = mMoneyEdit
                            .substring(0, mMoneyEdit.length() - 1);
                }
                mAddOutMoneyEdit.setText(mMoneyEdit);
            }
        });
        mAddOutYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });

        mAddOutSubmitRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), SubmitRemarkActivity.class));
                SubmitRemarkActivity.start(getContext());
            }
        });

        // 点击"日期"按钮布局 设置日期
        mAddOutDateEdit.setOnClickListener(new View.OnClickListener() {
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
     * 获取当前登录用户的Id
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
        mAddOutSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        mAddOutSp.setAdapter(mSpAdapter);
    }

    public void setDate() {
        // 时间
        mCalendar = Calendar.getInstance();
        // 设置初始时间与当前系统时间一致
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        updateDate();
    }

    public void updateDate() {
        // 更新EditText控件日期 小于10加0
        mAddOutDateEdit.setText(new StringBuilder()
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
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
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

    public void addData() {
        //符号
        int mSymbol = 1;
        // 时间
        Date mDate = null;
        String mDateEdit = mAddOutDateEdit.getText().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
        try {
            mDate = sdf.parse(mDateEdit);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 备注
        String mRemark = mAddOutSubmitRemarkBtn.getText().toString();
        // 获取到金额
        mMoneyEdit = mAddOutMoneyEdit.getText().toString().trim();
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
            } else {
                mMoney = mMoneyEdit
                        .substring(0, mMoneyEdit.indexOf('.'))
                        + mMoneyEdit.substring(mMoneyEdit.indexOf('.'),
                        mMoneyEdit.indexOf('.') + 3);
            }
        } else {
            mMoney = mMoneyEdit;
        }
        Long mLongMoney = Long.parseLong(mMoney);
        add(mCategory, mLongMoney, mDate, mMode, mRemark, mSymbol, mAccount);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubmitRemarkEvent(SubmitRemarkEvent event) {
        if (!TextUtils.isEmpty(event.getText())) {
            mAddOutSubmitRemarkBtn.setText(event.getText());
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
     * @param category  类别
     * @param longMoney 金额
     * @param date      日期
     * @param mode      (金额扣除或增加的)账户或方式
     * @param remark    备注
     * @param symbol    符号（0为支出，1为收入）
     * @param account   用户账号
     */
    public void add(String category, Long longMoney, Date date, String mode, String remark, int symbol, String account) {
        boolean isSaveSuccess = DbFactory.create().saveBillInfo(category, longMoney, date, mode, remark, symbol, account);
        if (!isSaveSuccess) {
            ToastUtils.showShort(getContext(), "数据保存失败");
            return;
        }
        ToastUtils.showShort(getContext(), "数据保存成功");
        BillBean billBean = new BillBean(category, longMoney,
                date, mode, remark, symbol, account);
        EventBus.getDefault().post(new AddDetailsEvent(billBean));
        //该方法用于监听用户点击返回键的事件，也可以调用它来关闭view。
        Objects.requireNonNull(getActivity()).onBackPressed();
    }
}
