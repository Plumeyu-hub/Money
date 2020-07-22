package com.snxun.book.ui.money.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseFragment;
import com.snxun.book.event.DemoNotifyEvent;
import com.snxun.book.ui.my.demo.gr.GrAdapter;
import com.snxun.book.ui.my.demo.gr.GrDataBean;
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

    String mCategory = "";// 类别
    /**
     * 金额输入
     */
    @BindView(R.id.add_in_money_edit)
    EditText mAddInMoneyEdit;
    private String mMoney;
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
    private String mAccount = "支付宝";
    /**
     * 备注框
     */
    @BindView(R.id.add_in_submit_remark_btn)
    EditText mAddInSubmitRemarkBtn;
    private String mRemark;
    /**
     * 日期选择
     */
    @BindView(R.id.add_in_date_edit)
    EditText mAddInDateEdit;
    private String mDate;
    // 定义显示时间控件
    private Calendar mCalendar;
    // 通过Calendar获取系统时间
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
    private GrAdapter mGrAdapter;//自定义适配器，继承RecyclerView.Adapter
    private List<GrDataBean> mGrDataList;

    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    private ContentValues mCv;// 存储工具栏
    private int num = 0;
    /**
     * 当前登录的用户ID
     */
    private int mUserId;

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
        mAddInMoneyEdit.setInputType(EditorInfo.TYPE_NULL);
        mAddInSubmitRemarkBtn.setInputType(EditorInfo.TYPE_NULL);
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 初始化数据列表
        mGrDataList = new ArrayList<>();

        for (int i = 0; i < iconName.length; i++) {
            mGrDataList.add(new GrDataBean(iconName[i], iconRes[i], false));
        }

        mGrAdapter = new GrAdapter(getContext(), mGrDataList);
        //第二参数是控制显示多少列,第三个参数是控制滚动方向和LinearLayout一样,第四个参数是控制是否反向排列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mAddInGv.setLayoutManager(layoutManager);
        mAddInGv.setHasFixedSize(true);
        mAddInGv.setAdapter(mGrAdapter);
    }

    @Override
    protected void setListeners(View view) {
        super.setListeners(view);

        int[] iconResSel = {R.drawable.ic_wages_sel, R.drawable.ic_bonus_sel,
                R.drawable.ic_part_sel, R.drawable.ic_mis_sel, R.drawable.ic_collect_sel,
                R.drawable.ic_borrow_sel, R.drawable.ic_sell_sel,
                R.drawable.ic_financial_sel, R.drawable.ic_gifts_sel,
                R.drawable.ic_other_in_sel};

        mGrAdapter.setOnItemClickListener(new GrAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                mGrDataList.get(mLast).setImgResId(iconRes[mLast]);
                mGrDataList.get(mLast).setIsSelected(false);
                Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
                mGrDataList.get(position).setImgResId(iconResSel[position]);
                mGrDataList.get(position).setIsSelected(true);
                mLast = position;
                mCategory = iconName[position];
                mGrAdapter.notifyDataSetChanged();
            }
        });


        mAddInOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "1";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "2";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInThreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "3";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInFourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "4";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInFiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "5";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInSixBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "6";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInSevenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "7";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInEightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "8";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInNineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "9";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInZeroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + "0";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInDotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                mMoney = mMoney + ".";
                if (mMoney.length() > 6) {
                    mMoney = mMoney.substring(0, 6);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInMoneyDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                if (mMoney.length() > 0) {
                    mMoney = mMoney
                            .substring(0, mMoney.length() - 1);
                }
                mAddInMoneyEdit.setText(mMoney);
            }
        });
        mAddInYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mMemoryMoney = ""; // 保留两位小数点的字符串
                int dot = 0;// 小数点出现的个数
                // 获取到金额
                mMoney = mAddInMoneyEdit.getText().toString().trim();
                // 将金额字符串转为数组
                char[] charMoney = mMoney.toCharArray();
                for (int k = 0; k < charMoney.length; k++) {
                    if (charMoney[k] == '.') {
                        dot++;
                    }
                }
                if (TextUtils.isEmpty(mMoney) && TextUtils.isEmpty(mCategory)) {
                    Toast.makeText(getContext(), "请完善账单信息", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if (TextUtils.isEmpty(mMoney)) {
                    Toast.makeText(getContext(), "请输入账单金额", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if (TextUtils.isEmpty(mCategory)) {
                    Toast.makeText(getContext(), "请选择账单类别", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if (dot > 1) {
                    Toast.makeText(getContext(), "请输入正确的金额", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if (mMoney.indexOf('.') == 0) {//小数点首次出现的位置是第一位
                    Toast.makeText(getContext(), "请输入正确的金额", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if (mMoney.lastIndexOf('.') == (mMoney.length() - 1)) {//小数点最后一次出现的位置是最后一位
                    Toast.makeText(getContext(), "请输入正确的金额", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else {
                    // 记录小数点后两位ao_dmoney
                    if (dot == 1) {
                        int length = mMoney.length();
                        if (length - dot == 2) {
                            mMemoryMoney = mMoney
                                    .substring(0, mMoney.indexOf('.'))
                                    + mMoney.substring(mMoney.indexOf('.'),
                                    mMoney.indexOf('.') + 2);
                        } else {
                            mMemoryMoney = mMoney
                                    .substring(0, mMoney.indexOf('.'))
                                    + mMoney.substring(mMoney.indexOf('.'),
                                    mMoney.indexOf('.') + 3);
                        }
                    } else {
                        mMemoryMoney = mMoney;
                    }
                }
                // 备注
                mRemark = mAddInSubmitRemarkBtn.getText().toString();
                // 时间
                String mNoSymbolDate = mAddInDateEdit.getText().toString();
                mDate = mNoSymbolDate.replace("-", "");

                // 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
                mCv = new ContentValues();
                mCv.put("aicategory", mCategory);
                mCv.put("aimoney", mMemoryMoney);
                mCv.put("aiaccount", mAccount);
                mCv.put("airemarks", mRemark);
                mCv.put("aitime", mDate);
                mCv.put("aiuserid", mUserId);
                // 插入数据，成功返回当前行号，失败返回0
                num = (int) mDb.insert("income", null, mCv);
                if (num > 0) {
                    Toast.makeText(getContext(), "数据保存成功" + num, Toast.LENGTH_SHORT).show();
                    //该方法用于监听用户点击返回键的事件，也可以调用它来关闭view。
                    Objects.requireNonNull(getActivity()).onBackPressed();
                } else {

                    Toast.makeText(getContext(), "数据保存失败" + num, Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).onBackPressed();
                }
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
        initDb();
        showUserInfo();

        setSp();
        setDate();
    }

    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        mUserId = SpManager.get().getUserId();
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = requireActivity().openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
        mDb.execSQL("create table if not exists income (aiid integer primary key,aicategory text,aimoney text,aitime text,aiaccount text,airemarks text,aiuserid integer)");
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
                mAccount = mAccountSpItem[arg2];
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
        mCalendar = Calendar.getInstance();
        // 设置初始时间与当前系统时间一致
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
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
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDemoNotifyEvent(DemoNotifyEvent event) {
        if (!TextUtils.isEmpty(event.getText())) {
            mAddInSubmitRemarkBtn.setText(event.getText());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
