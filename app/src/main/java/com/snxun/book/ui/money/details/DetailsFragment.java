package com.snxun.book.ui.money.details;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snxun.book.R;
import com.snxun.book.base.BaseFragment;
import com.snxun.book.db.DbFactory;
import com.snxun.book.event.AddDetailsEvent;
import com.snxun.book.event.DetailsUpdateEvent;
import com.snxun.book.event.RefreshEvent;
import com.snxun.book.greendaolib.table.BillTable;
import com.snxun.book.ui.money.adapter.RvListAdapter;
import com.snxun.book.ui.money.add.AddActivity;
import com.snxun.book.ui.money.search.SearchActivity;
import com.snxun.book.ui.money.update.UpdateActivity;
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
 * 明细页
 *
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
     * 定义了一个数组List，里面只能存放BillTable
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
    private int mYear;
    private int mMonth;
    private int mDay;
    /**
     * 退出应用的弹窗
     */
    private PopupWindow mPopupWindow;

    @Override
    protected void startCreate() {
        super.startCreate();
        // 在你需要订阅的类里去注册EventBus
        EventBus.getDefault().register(this);
    }

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
        mRvListAdapter = new RvListAdapter(getContext());
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

        //点击RVlist跳转UpdateActivity
        mRvListAdapter.setOnItemClickListener(new RvListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                BillTable billTable = mDetailsList.get(position);
                //EventBus的黏性postSticky
                EventBus.getDefault().postSticky(new DetailsUpdateEvent(billTable));
                UpdateActivity.start(getContext());
            }
        });

        // 长按RVlist按删除
        mRvListAdapter.setOnItemLongClickListener(new RvListAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                deleteListItem(position);
            }
        });

        //侧边栏按钮
        mDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 重点，START是xml布局文件中侧边栏布局所设置的方向 */
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

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

    /**
     * 删除账单
     */
    public void deleteListItem(int position) {
        // 获取所点击项的id
        Long id = mDetailsList.get(position).getId();
        //创建dialog
        showDeleteTipsDialog(id);
    }

    /**
     * 显示删除提示弹框
     *
     * @param id 数据库编号
     */
    private void showDeleteTipsDialog(Long id) {
        // 通过Dialog提示是否删除
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_text);
        // 确定按钮点击事件
        builder.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除指定数据
                boolean isSaveSuccess = DbFactory.create().deleteBillInfo(id);
                if (!isSaveSuccess) {
                    ToastUtils.showShort(getContext(), R.string.delete_no);
                    return;
                }
                ToastUtils.showShort(getContext(), R.string.delete_yes);
                updateDate(mYear, mMonth);
            }
        });

        // 取消按钮点击事件
        builder.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


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
        Calendar calendar = Calendar.getInstance();
        // 设置初始时间与当前系统时间一致
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DATE);
        updateDate(mYear, mMonth);
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
            updateDate(year, month);
        }
    };

    // 当DatePickerDialog关闭时，更新日期显示
    private void updateDate(int year, int month) {
        // 在TextView上显示日期
        mMonthSelectorBtn.setText(new StringBuilder().append(year).append("年")
                .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
                .append("月"));
        mMonthSelectorBtn.setTextColor(getResources().getColor(R.color.color_333333));

        refreshList(String.valueOf(year), String.valueOf((month + 1) < 10 ? "0" + (month + 1) : (month + 1)));
    }


    // 订阅事件方法
    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onUpdateDetailsEvent(UpdateDetailsEvent event) {
    //        dataBean = event.getDataBean();
    //        mDetailsList.set(mPosition, dataBean);
    //        mRvListAdapter.notifyDataSetChanged();
    //    }
    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDetailsEvent(AddDetailsEvent event) {
        if (!TextUtils.isEmpty(event.getDate())) {
            String date = event.getDate();
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            updateDate(Integer.parseInt(year), Integer.parseInt(month) - 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDetailsEvent(RefreshEvent event) {
        if (event.getRefresh()) {
            String date = mMonthSelectorBtn.getText().toString().trim();
            String year = date.substring(0, 4);
            String month = date.substring(5, 7);
            refreshList(year, month);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void refreshList(String year, String month) {
        // 日期条件
        String date = year + month;
        //String endDate = year + String.valueOf((Integer.parseInt(month) + 1) < 10 ? "0" + (Integer.parseInt(month) + 1) : (Integer.parseInt(month) + 1));

        mDetailsList = DbFactory.create().getAllAccountBillInfo(mAccount, date);
        mRvListAdapter.setData(mDetailsList);
        mRvListAdapter.notifyDataSetChanged();
        // 设置空列表的时候，显示为一张图片
        //mDetailsList.setEmptyView(getActivity().findViewById(R.id.details_empty_lin));
    }

}


