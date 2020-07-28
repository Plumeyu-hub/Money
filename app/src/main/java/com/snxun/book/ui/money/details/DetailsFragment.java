package com.snxun.book.ui.money.details;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseFragment;
import com.snxun.book.db.DbFactory;
import com.snxun.book.greendaolib.table.BillTable;
import com.snxun.book.ui.money.adapter.RvListAdapter;
import com.snxun.book.ui.money.add.AddActivity;
import com.snxun.book.ui.money.search.SearchActivity;
import com.snxun.book.ui.my.AboutActivity;
import com.snxun.book.ui.my.ClearActivity;
import com.snxun.book.ui.my.ExportActivity;
import com.snxun.book.ui.my.HelpActivity;
import com.snxun.book.ui.my.MessageActivity;
import com.snxun.book.ui.my.PersonalInfoActivity;
import com.snxun.book.ui.my.RemindActivity;
import com.snxun.book.ui.my.SetActivity;
import com.snxun.book.ui.my.budget.BudgetActivity;
import com.snxun.book.ui.my.demo.home.DemoHomeActivity;
import com.snxun.book.utils.sp.SpManager;

import org.greenrobot.eventbus.EventBus;

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
 * @author Wangshy
 * @date 2020/7/12
 */

public class DetailsFragment extends BaseFragment {

    private static final String EXTRA_TEXT = "extra_text";

    public static DetailsFragment newInstance(String from) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TEXT, from);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    /**
     * RV列表
     */
    @BindView(R.id.details_rv)
    RecyclerView mRecyclerView;
    /**
     * RvList适配器
     */
    private RvListAdapter mRvListAdapter;
    /**
     * 定义了一个数组List，里面只能存放BillBean
     */
    private List<BillTable> mDetailsList;

    /**
     * 侧边栏按钮
     */
    @BindView(R.id.drawer_btn)
    ImageView mDrawerBtn;
    /**
     * 侧边栏控件
     */
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    /**
     * 日期选择按钮
     */
    @BindView(R.id.details_month_btn)
    EditText mMonthSelectorBtn;

    /**
     * 当前登录的用户账号
     */
    private String mAccount;
    /**
     * 当前登录的用户名展示
     */
    @BindView(R.id.username_tv)
    TextView mUserNameTv;
    /**
     * 时间
     * 定义显示时间控件
     */
    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDay;

    /**
     * 退出应用的弹窗
     */
    private PopupWindow mPopupWindow;
    /**
     * 当前点击的list的id
     */
    private int mPosition;
    /**
     * 数据源
     */
    private BillTable mBillTable;

//    @Override
//    protected void startCreate() {
//        super.startCreate();
//        // 在你需要订阅的类里去注册EventBus
//        EventBus.getDefault().register(this);
//    }

    /**
     * 页面初始化，设置内容视图
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_details;
    }

    /**
     * 控件初始化
     */
    @Override
    protected void findViews(View view) {
        ButterKnife.bind(this, view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        // 初始化数据列表
        mDetailsList = new ArrayList<>();
        mRvListAdapter = new RvListAdapter(getContext(), mDetailsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRvListAdapter);
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListeners(View view) {
        super.setListeners(view);

//        //点击RVlist跳转UpdateActivity
//        mRvListAdapter.setOnItemClickListener(new RvListAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                mBillBean = mDetailsList.get(position);
//                //给UpdateActivity页面传递信息
//                EventBus.getDefault().postSticky(new DetailsUpdateEvent(mBillBean));
//                UpdateActivity.start(getContext());
//                mPosition = position;
//            }
//        });

//        // 长按RVlist按删除
//        mRvListAdapter.setOnItemLongClickListener(new RvListAdapter.OnItemLongClickListener() {
//            @Override
//            public void onClick(int position) {
//                deleteListItem(position);
//            }
//        });
//
//        //侧边栏按钮
//        mDrawerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /* 重点，START是xml布局文件中侧边栏布局所设置的方向 */
//                mDrawerLayout.openDrawer(GravityCompat.START);
//            }
//        });

        //月份选择按钮
        mMonthSelectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        //账目搜索按钮
        view.findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从fragment跳转到activity中
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        //添加记账信息
        view.findViewById(R.id.add_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddActivity.class));
            }
        });

        // 个人信息
        view.findViewById(R.id.personal_info_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
            }
        });

        // 消息
        view.findViewById(R.id.mes_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MessageActivity.class));
            }
        });

        // 定时记账
        view.findViewById(R.id.remind_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RemindActivity.class));
            }
        });

        // 预算
        view.findViewById(R.id.budget_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BudgetActivity.class));
            }
        });

        // 导出账目
        view.findViewById(R.id.export_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ExportActivity.class));
            }
        });

        // 数据库数据清除
        view.findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ClearActivity.class));
            }
        });

        // 设置
        view.findViewById(R.id.set_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SetActivity.class));
            }
        });

        // 帮助
        view.findViewById(R.id.help_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });

        // 关于
        view.findViewById(R.id.about_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        // Demo主页
        view.findViewById(R.id.demo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DemoHomeActivity.start(getActivity());
            }
        });

        // 退出应用
        view.findViewById(R.id.exit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopWindow();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        showUserInfo();
        setDate();
    }

    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        mAccount = SpManager.get().getUserAccount();
        mUserNameTv.setText(String.valueOf(mAccount));
    }

//    /**
//     * 删除账单
//     */
//    public void deleteListItem(int position) {
//        // 获取所点击项的id
//        Long intId = mDetailsList.get(position).getmId();
//        String stringId = String.valueOf(intId);
//        // 获取所点击项的金额符号
//        String stringMoney = mDetailsList.get(position).getmMoney();
//        String symbolMoney = stringMoney.substring(0, 1);
//        //创建dialog
//        showDeleteTipsDialog(intId, symbolMoney, position);
//    }

    /**
     * 显示删除提示弹框
     *
     * @param id       编号
     * @param symbol   金额符号
     * @param position 序号
     */
//    private void showDeleteTipsDialog(final int id, final String symbol, final int position) {
//        // 通过Dialog提示是否删除
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage(R.string.delete_text);
//        // 确定按钮点击事件
//        builder.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 删除指定数据
//                if (symbol.equals("-")) {
//                    deleteRecordByDb(id, position, "expenditure", "aoid=? and aouserid=?");
//                    return;
//                }
//                if (symbol.equals("+")) {
//                    deleteRecordByDb(id, position, "income", "aiid=? and aiuserid=?");
//                    return;
//                }
//                Toast.makeText(getActivity(), R.string.app_abnormal_data, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // 取消按钮点击事件
//        builder.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }

    /**
     * 通过SQL语句来删除记账记录
     *
     * @param id          编号
     * @param position    移除某个位置的Item
     * @param tableName   表名
     * @param whereClause 条件
     */

//    private void deleteRecordByDb(int id, int position, String tableName, String whereClause) {
//
//
//
//
//
//
//        for (int i = 0; i < list.size(); i++) {
//            if(i==id){
//                mBillTableDao.delete(list.get(i));
//            }
//        }
//
//        // 删除数据，成功返回删除的数据的行数，失败返回0
//        int num = mDb.delete(tableName, whereClause, new String[]{id + "", mAccount + ""});
//        if (num <= 0) {
//            Toast.makeText(getActivity(), R.string.delete_yes + num, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Toast.makeText(getActivity(), R.string.delete_no + num, Toast.LENGTH_SHORT).show();
//        // 删掉长按的item
//        mDetailsList.remove(position);
//        // 动态更新listview
//        mRvListAdapter.notifyDataSetChanged();
//    }

    /**
     * 创建退出应用弹窗
     */
    public void createPopWindow() {
        if (mPopupWindow == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_exit, null);
            // 获取圆角对话框布局View，背景设为圆角
            view.setBackgroundResource(R.drawable.bg_eeeeee_corner_20);
            // 实例化弹窗
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // 创建弹出对话框，设置弹出对话框的背景为圆角
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_eeeeee_corner_20));
            mPopupWindow.setFocusable(true);// 获取焦点（把焦点集中在pop上面）// 点击空白处时，隐藏掉pop窗口
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));//设置透明的颜色背景
            //mPopupWindow.setBackgroundDrawable(new BitmapDrawable());被废弃了
            // 设置点击pop外面的窗口时是否弹窗

            // 这两句话是防止软键盘遮挡弹窗
            // pop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            // pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            TextView yesTv = (TextView) view.findViewById(R.id.pop_yes_tv);
            yesTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    getActivity().finish();
                    // System.exit(0);
                }
            });
            TextView noTv = (TextView) view.findViewById(R.id.pop_no_tv);
            noTv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    mPopupWindow.dismiss();
                }
            });
        }
        mPopupWindow.showAtLocation(
                LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null),
                Gravity.CENTER, 0, 0);// 在父控件LayoutInflater.from(this).inflate(R.layout.activity_main,
        // null)的中间显示出来
    }

    public void setDate() {
        // 时间
        mCalendar = Calendar.getInstance();
        // 设置初始时间与当前系统时间一致
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    private void showDatePicker() {
        // 创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), AlertDialog.THEME_HOLO_LIGHT, Datelistener, mYear, mMonth, mDay);
        dialog.show();
        // 只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup) Objects.requireNonNull(dialog.getWindow()).getDecorView());
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
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        /**
         * params：view：该事件关联的组件
         * params：year：当前选择的年
         * params：month：当前选择的月
         * params：day：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            mYear = year;
            mMonth = month;
            mDay = day;
            // 更新日期
            updateDate();
        }
    };

    // 当DatePickerDialog关闭时，更新日期显示
    private void updateDate() {
        // 在TextView上显示日期
        mMonthSelectorBtn.setText(new StringBuilder().append(mYear).append("年")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                .append("月"));
        mMonthSelectorBtn.setTextColor(getResources().getColor(R.color.color_333333));

        // 条件
        String yearString = String.valueOf(mYear);
        int monthInt = mMonth + 1;
        String monthString;
        if ((mMonth + 1) < 10) {
            monthString = String.valueOf("0" + monthInt);
        } else {
            monthString = String.valueOf(monthInt);
        }
        String condition = String.valueOf(yearString +"-" +monthString);

        // 时间
        Date mDate = null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//小写的mm表示的是分钟
        try {
            mDate = sdf.parse(condition);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String conditiona = String.valueOf(yearString +"-08");
        Date detea=null;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfa = new SimpleDateFormat("yyyy-MM");//小写的mm表示的是分钟
        try {
            detea = sdfa.parse(conditiona);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDetailsList.clear();
        List<BillTable> list = new ArrayList();
        list= DbFactory.create().getAllAccountBillInfo(mAccount,mDate,detea);

        for(int i=0;i<=list.size();i++){
            Long id=list.get(i).getId();
            String category=list.get(i).getCategory();
            Long money=list.get(i).getMoney();
            Date date=list.get(i).getDate();
            String mode=list.get(i).getMode();
            String remark=list.get(i).getRemark();
            int symbol=list.get(i).getSymbol();
            String account=list.get(i).getAccount();
            mBillTable = new BillTable(id,category, money, date,
                    mode, remark, symbol, account);
            mDetailsList.add(mBillTable);
        }
        mRvListAdapter.notifyDataSetChanged();
        // 设置空列表的时候，显示为一张图片
        //mDetailsList.setEmptyView(getActivity().findViewById(R.id.details_empty_lin));
    }

    // 订阅事件方法
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onUpdateDetailsEvent(UpdateDetailsEvent event) {
//        dataBean = event.getDataBean();
//        mDetailsList.set(mPosition, dataBean);
//        mRvListAdapter.notifyDataSetChanged();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onAddDetailsEvent(AddDetailsEvent event) {
//        BillBean billBean = event.getBillBean();
//        String monthSelectorBtn = mMonthSelectorBtn.getText().toString();
//        if (monthSelectorBtn.equals("请点击选择账单年月")) {
//            mDetailsList.add(dataBean);
//            mRvListAdapter.notifyDataSetChanged();
//        } else {
//            mMonthSelectorBtn.setText("请点击选择账单年月");
//            mMonthSelectorBtn.setTextColor(getResources().getColor(
//                    R.color.color_666666));
//            mDetailsList.clear();
//            mDetailsList.add(dataBean);
//            mRvListAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
