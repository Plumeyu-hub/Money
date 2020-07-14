package com.example.money;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.base.BaseFragment;
import com.example.bean.AccountBean;
import com.example.demo.home.DemoHomeActivity;
import com.example.ui.account.AccountInformationActivity;
import com.example.ui.account.AddAccountActivity;
import com.example.ui.account.DetailsInfoListAdapter;
import com.example.ui.account.SearchActivity;
import com.example.ui.budget.BudgetActivity;
import com.example.ui.my.AboutActivity;
import com.example.ui.my.ClearActivity;
import com.example.ui.my.ExportActivity;
import com.example.ui.my.HelpActivity;
import com.example.ui.my.MessageActivity;
import com.example.ui.my.MyActivity;
import com.example.ui.my.RemindActivity;
import com.example.ui.my.SetActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 14043
 * @date 2020/7/12
 */

public class DetailsFragment extends BaseFragment {

    /** 详情修改的请求码 */
    public static final int DETAIL_UPDATE_REQUEST_CODE = 0;
    /** 详情修改的结果码 */
    public static final int DETAIL_UPDATE_RESULT_CODE = 102;

    private static final String EXTRA_TEXT = "extra_text";

    public static DetailsFragment newInstance(String from) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TEXT, from);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    // 广播
    MyBroadcastReceiver mReceiver;

    // list
    @BindView(R.id.details_lv)
    ListView mDetailsLv;
    private List<AccountBean> mList;
    private DetailsInfoListAdapter mAdapter;

    /** 侧边栏按钮 */
    @BindView(R.id.detailslogo_img)
    ImageView mDrawerBtn;
    /** 侧边栏控件 */
    @BindView(R.id.accountdetails_dl)
    DrawerLayout mDrawerLayout;/* 重点，声明DrawerLayout */

    /** 日期选择按钮 */
    @BindView(R.id.detailsmonth_edit)
    EditText mMonthSelectorBtn;

    /** 账目搜索按钮 */
    TextView tv_search;

    // 数据库
    private SQLiteDatabase mDb;

    // 用户名
    private SharedPreferences sp_user;
    private SharedPreferences.Editor editor_user;
    private String username;
    private int mUserId;
    @BindView(R.id.username_tv)
    TextView mUserNameTv;

    // 创建弹窗并显示
    private PopupWindow mPopupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_details;
    }

    @Override
    protected void findViews(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void setListeners(View view) {
        super.setListeners(view);
        mDetailsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                AccountBean showdata = mList.get(position);
                AccountInformationActivity.startForResult(requireActivity(), showdata, position, DETAIL_UPDATE_REQUEST_CODE);
            }
        });

        // 长按删除
        mDetailsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                // 获取所点击项的_id
                TextView idTv = arg1.findViewById(R.id.dataitemid_tv);
                final String id = idTv.getText().toString();
                TextView syTv = arg1.findViewById(R.id.dataitemmoney_tv);
                final String sy = syTv.getText().toString().substring(0, 1);
                showDeleteTipsDialog(id, sy, arg2);
                return true;
            }
        });

        mDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 重点，LEFT是xml布局文件中侧边栏布局所设置的方向 */
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mMonthSelectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        //搜索
        view.findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "跳转", Toast.LENGTH_SHORT).show();
                //从fragment跳转到activity中
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        //添加记账信息
        view.findViewById(R.id.add_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddAccountActivity.class));
            }
        });

        // 立即登录/注册
        view.findViewById(R.id.nologin_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyActivity.class));
            }
        });

        // 消息
        view.findViewById(R.id.mes_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MessageActivity.class));
            }
        });

        // 定时记账
        view.findViewById(R.id.remind_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RemindActivity.class));
            }
        });

        // 设置
        view.findViewById(R.id.set_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SetActivity.class));
            }
        });

        // 帮助
        view.findViewById(R.id.help_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });

        // 关于
        view.findViewById(R.id.about_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        // 退出应用
        view.findViewById(R.id.exit_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopWindow();
            }
        });

        // 导出账目
        view.findViewById(R.id.export_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ExportActivity.class));
            }
        });

        // 数据库数据清除
        view.findViewById(R.id.clear_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ClearActivity.class));
            }
        });

        // 预算
        view.findViewById(R.id.budget_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BudgetActivity.class));
            }
        });

        // Demo主页
        view.findViewById(R.id.demo_lin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DemoHomeActivity.start(getActivity());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
        initListView();
        registerReceiver();// 注册广播
    }

    /**
     * 显示删除提示弹框
     * @param id 编号
     * @param sy 金额符号
     * @param position 序号
     */
    private void showDeleteTipsDialog(final String id,final String sy, final int position) {
        // 通过Dialog提示是否删除
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定要删除吗？");
        // 确定按钮点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除指定数据
                if (sy.equals("-")) {
                    deleteRecordByDb(id, position, "expenditure", "aoid=? and aouserid=?");
                    return;
                }
                if (sy.equals("+")) {
                    deleteRecordByDb(id, position, "income", "aiid=? and aiuserid=?");
                    return;
                }
                Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
            }
        });

        // 取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /** 通过SQL语句来删除记账记录 */
    private void deleteRecordByDb(String id, int position, String tableName, String whereClause) {
        // 删除数据，成功返回删除的数据的行数，失败返回0
        int num = mDb.delete(tableName, whereClause, new String[]{id + "", mUserId + ""});
        if (num<= 0){
            Toast.makeText(getActivity(), "删除失败" + num, Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getActivity(), "删除成功" + num, Toast.LENGTH_SHORT).show();
        // 删掉长按的item
        mList.remove(position);
        // 动态更新listview
        mAdapter.notifyDataSetChanged();
    }


    /** 显示用户信息 */
    private void showUserInfo() {
        // 用户名存储
        sp_user = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor_user = sp_user.edit();
        if (sp_user != null) {// 判断文件是否存在
            // 使用getString方法获得value，注意第2个参数是value的默认值
            username = sp_user.getString("username", "");
            mUserId = sp_user.getInt("userid", 0);
            // System.out.println(username);
            mUserNameTv.setText(username);
            // System.out.println(userid);
        }
    }

    /** 初始化记账信息列表 */
    private void initListView() {
        // 第二步:初始化数据
        mList = new ArrayList<>();
        // 实例化适配器
        mAdapter = new DetailsInfoListAdapter(getActivity(), mList);
        // listview设置适配器
        mDetailsLv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    /** 初始化数据库 */
    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = requireActivity().openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
    }

    // 外
    // 注册广播接收者
    public void registerReceiver() {
        mReceiver = new MyBroadcastReceiver();// 实例化广播接收者
        getActivity().registerReceiver(mReceiver, new IntentFilter("GBadd"));
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("GBadd".equals(intent.getAction())) {
                // Toast.makeText(DetailsActivity.this,
                // "接收到广播一" + intent.getStringExtra("hhh"),
                // Toast.LENGTH_SHORT).show();
                // 接收到广播，取出里面携带的数据
                AccountBean adddata = (AccountBean) intent.getSerializableExtra("adddata");
                if (adddata != null) {
                    // tv.setText(aidata.getMoney());
                    String monthtimes = mMonthSelectorBtn.getText().toString();
                    if (monthtimes.equals("请点击选择账单年月")) {
                        mList.add(adddata);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mMonthSelectorBtn.setText("请点击选择账单年月");
                        mMonthSelectorBtn.setTextColor(getResources().getColor(R.color.color_999999));
                        mList.clear();
                        mList.add(adddata);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(mReceiver);// 注销广播接收者
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null) {
            if (requestCode == DETAIL_UPDATE_REQUEST_CODE && resultCode == DETAIL_UPDATE_RESULT_CODE) {
                AccountBean updata = (AccountBean) intent.getSerializableExtra("updata");
                int position = intent.getIntExtra("position", 0);
                mList.set(position, updata);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void createPopWindow() {
        if (mPopupWindow == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_exit, null);
            // 获取圆角对话框布局View，背景设为圆角
            view.setBackgroundResource(R.drawable.bg_eeeeee_corner_20);
            // 实例化弹窗
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // 创建弹出对话框，设置弹出对话框的背景为圆角
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_eeeeee_corner_20));
            mPopupWindow.setFocusable(true);// 获取焦点（把焦点集中在pop上面）
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());// 设置点击pop外面的窗口时是否弹窗
            // 这两句话是防止软键盘遮挡弹窗
            // pop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            // pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            TextView yesTv = (TextView) view.findViewById(R.id.dialogyes_tv);
            yesTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    getActivity().finish();
                    // System.exit(0);
                }
            });
            TextView noTv = (TextView) view.findViewById(R.id.dialogno_tv);
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

    // 获取当前日期
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    private void showDatePicker() {
        // 创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, Datelistener, year, month, day);
        dialog.show();
        // 只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
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
                    if (result != null){
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        /**
         * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
            // 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            // 更新日期
            updateDate();

        }

        // 当DatePickerDialog关闭时，更新日期显示

    };

    private void updateDate() {
        // 在TextView上显示日期
        mMonthSelectorBtn.setText(new StringBuilder().append(year).append("年")
                .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
                .append("月"));
        mMonthSelectorBtn.setTextColor(getResources().getColor(R.color.color_333333));

        // 条件
        String years = String.valueOf(year);
        int monthn = month + 1;
        String months;
        if ((month + 1) < 10) {
            months = String.valueOf("0" + monthn);
        } else {
            months = String.valueOf(monthn);
        }
        String condition = String.valueOf(years + months);
        // System.out.println(condition);

        mList.clear();
        String uid = String.valueOf(mUserId);
        AccountBean accountBean;
        Cursor cs = mDb.query("expenditure",
                new String[]{"aoid", "aocategory", "aomoney", "aotime",
                        "aoaccount", "aoremarks", "aouserid"},
                "aotime like ? and aouserid=?", new String[]{
                        condition + "%", uid + ""}, null, null,
                "aotime ASC");
        if (cs != null) {

            while (cs.moveToNext()) {
                int aoid = cs.getInt(cs.getColumnIndex("aoid"));// 得到列名id对于的值
                int aouserid = cs.getInt(cs.getColumnIndex("aouserid"));
                String aocategory = cs.getString(cs
                        .getColumnIndex("aocategory"));
                String aomoney = "-"
                        + cs.getString(cs.getColumnIndex("aomoney"));
                String aotime = cs.getString(cs.getColumnIndex("aotime"));
                String aoaccount = cs.getString(cs
                        .getColumnIndex("aoaccount"));
                String aoremarks = cs.getString(cs
                        .getColumnIndex("aoremarks"));
                accountBean = new AccountBean(aocategory, aomoney, aoaccount,
                        aoremarks, aotime, aoid, aouserid);
                mList.add(accountBean);
                // adapter.notifyDataSetChanged();
            }
        }

        cs = mDb.query("income",
                new String[]{"aiid", "aicategory", "aimoney", "aitime",
                        "aiaccount", "airemarks", "aiuserid"},
                "aitime like ? and aiuserid=?", new String[]{
                        condition + "%", uid + ""}, null, null,
                "aitime ASC");
        if (cs != null) {
            // AccountBean accountBean;
            while (cs.moveToNext()) {
                int aiid = cs.getInt(cs.getColumnIndex("aiid"));// 得到列名id对于的值
                int aiuserid = cs.getInt(cs.getColumnIndex("aiuserid"));
                String aicategory = cs.getString(cs
                        .getColumnIndex("aicategory"));
                String aimoney = "+"
                        + cs.getString(cs.getColumnIndex("aimoney"));
                String aitime = cs.getString(cs.getColumnIndex("aitime"));
                String aiaccount = cs.getString(cs
                        .getColumnIndex("aiaccount"));
                String airemarks = cs.getString(cs
                        .getColumnIndex("airemarks"));
                accountBean = new AccountBean(aicategory, aimoney, aiaccount,
                        airemarks, aitime, aiid, aiuserid);
                mList.add(accountBean);
            }
        }
        if (cs != null){
            cs.close();
        }
        Collections.sort(mList, new Comparator<AccountBean>() {

            @Override
            public int compare(AccountBean lhs, AccountBean rhs) {
                int a = Integer.parseInt(lhs.getDaytime());
                int b = Integer.parseInt(rhs.getDaytime());
                int diff = a - b;
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                }
                return 0;// 相等为0
            }
        });
        mAdapter.notifyDataSetChanged();
        // 设置空列表的时候，显示为一张图片
        mDetailsLv.setEmptyView(getActivity().findViewById(R.id.detailsempty_lin));
    }
}
