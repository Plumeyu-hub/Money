package com.snxun.book.ui.money.graph;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.snxun.book.R;
import com.snxun.book.base.BaseFragment;
import com.snxun.book.utils.sp.SpManager;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 14043
 * @date 2020/7/12
 */
public class GraphFragment extends BaseFragment {

    private static final String EXTRA_TEXT = "extra_text";
    public static GraphFragment newInstance(String from) {
        GraphFragment graphFragment = new GraphFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TEXT, from);
        graphFragment.setArguments(bundle);
        return graphFragment;
    }

    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    private Cursor mCs;// 游标对象，用来报错查询返回的结果集
    /**
     * 当前登录的用户ID
     */
    private int mUserId;

    /**
     * 选择日期按钮
     */
    @BindView(R.id.graph_month_btn)
    EditText mGraphMonthBtn;
    /**
     * 结余
     */
    @BindView(R.id.balance_num_tv)
    TextView mBalanceNumTv;
    /**
     * 结余隐藏***
     */
    @BindView(R.id.balance_gone_num_tv)
    TextView mBalanceGoneNumTv;
    /**
     * 结余文字
     */
    @BindView(R.id.balance_text_tv)
    TextView mBalanceTextTv;
    /**
     * 支出
     */
    @BindView(R.id.out_num_tv)
    TextView mOutNumTv;
    /**
     * 支出隐藏***
     */
    @BindView(R.id.out_gone_num_tv)
    TextView mOutGoneNumTv;
    /**
     * 支出文字
     */
    @BindView(R.id.out_text_tv)
    TextView mOutTextTv;
    /**
     * 收入
     */
    @BindView(R.id.in_num_tv)
    TextView mInNumTv;
    /**
     * 收入隐藏***
     */
    @BindView(R.id.in_gone_num_tv)
    TextView mInGoneNumTv;
    /**
     * 收入文字
     */
    @BindView(R.id.in_text_tv)
    TextView mInTextTv;
    /**
     * 隐藏金额按钮
     */
    @BindView(R.id.hide_money_btn)
    TextView mHideMoneyBtn;
    /**
     * 判断当前隐藏金额键的状态
     */
    private boolean state = true;
    /**
     * 图表
     */
    @BindView(R.id.piechart)
    PieChart mPiechart;

    /**
     * 计算支出、收入、结余的值
     */
    private float mOutSum = 0;
    private float mInSum = 0;
    private float mCountSum = 0;
    /**
     * 展示的每月支出和收入（无符号）
     */
    String mShowInNum, mShowOutNum;// 每月支出和收入

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_graph;
    }

    @Override
    protected void findViews(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void setListeners(View view) {
        super.setListeners(view);
        // 点击"日期"按钮布局 设置日期
        mGraphMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        //隐藏
        mHideMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state) {// 可见
                    mHideMoneyBtn.setBackgroundResource(R.drawable.ic_eye_nor);
                    state =false;

                    mBalanceNumTv.setVisibility(View.GONE);
                    mBalanceGoneNumTv.setVisibility(View.VISIBLE);

                    mOutNumTv.setVisibility(View.GONE);
                    mOutGoneNumTv.setVisibility(View.VISIBLE);

                    mInNumTv.setVisibility(View.GONE);
                    mInGoneNumTv.setVisibility(View.VISIBLE);

                } else {// 隐藏
                    mHideMoneyBtn.setBackgroundResource(R.drawable.ic_eye_sel);
                    state = true;

                    mBalanceNumTv.setVisibility(View.VISIBLE);
                    mBalanceGoneNumTv.setVisibility(View.GONE);

                    mOutNumTv.setVisibility(View.VISIBLE);
                    mOutGoneNumTv.setVisibility(View.GONE);

                    mInNumTv.setVisibility(View.VISIBLE);
                    mInGoneNumTv.setVisibility(View.GONE);

                }
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
    }

    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = getActivity().openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
    }


    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        mUserId = SpManager.get().getUserId();
    }

    // 获取当前日期
    Calendar mCalendar = Calendar.getInstance();
    int mYear = mCalendar.get(Calendar.YEAR);
    int mMonth = mCalendar.get(Calendar.MONTH);
    int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
    private void showDatePicker() {

        // 创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT, DateListener, mYear, mMonth, mDay);
        dialog.show();

        // 只显示年月，隐藏掉日
        DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow()
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

    private DatePickerDialog.OnDateSetListener DateListener = new DatePickerDialog.OnDateSetListener() {
        /**
         * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
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

        // 当DatePickerDialog关闭时，更新日期显示
        private void updateDate() {
            // 在TextView上显示日期
            mGraphMonthBtn.setText(new StringBuilder().append(mYear).append("年")
                    .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                    .append("月"));
            //monthtime.setTextColor(getResources().getColor(R.color.white));
            mGraphMonthBtn.setTextColor(Color.WHITE);

            // 条件
            String years = String.valueOf(mYear);
            int monthn = mMonth + 1;
            String months;
            if ((mMonth + 1) < 10) {
                months = String.valueOf("0" + monthn);
            } else {
                months = String.valueOf(monthn);
            }
            String condition = String.valueOf(years + months);
            // System.out.println(condition);
            String userId = String.valueOf(mUserId);
            mCs = mDb.rawQuery(
                    "select sum(aomoney) from expenditure where aotime like ? and aouserid=?",
                    new String[] { condition + "%", userId + "" });

            if (mCs != null) {
                if (mCs.moveToFirst()) {
                    do {
                        mOutSum = mCs.getFloat(0);

                    } while (mCs.moveToNext());
                }
                // System.out.println(outsum);
                mShowOutNum = String.format("%.2f", mOutSum);
                mOutNumTv.setText("-" + mShowOutNum);
                mOutTextTv.setText(monthn + "月支出");
            } else {
                mOutNumTv.setText("0.00");
                mOutTextTv.setText(monthn + "月支出");
            }

            mCs = mDb.rawQuery(
                    "select sum(aimoney) from income where aitime like ? and aiuserid=?",
                    new String[] { condition + "%", userId + "" });

            if (mCs != null) {
                if (mCs.moveToFirst()) {
                    do {
                        mInSum = mCs.getFloat(0);

                    } while (mCs.moveToNext());
                }
                mShowInNum = String.format("%.2f", mInSum);
                mInNumTv.setText("+" + mShowInNum);
                mInTextTv.setText(monthn + "月收入");
            } else {
                mInNumTv.setText("0.00");
                mInTextTv.setText(monthn + "月收入");
            }
            mCountSum = mInSum - mOutSum;
            PieData mPieData = getPieData(4, 100);
            showChart(mPiechart, mPieData);
            String resultcount = String.format("%.2f", mCountSum);
            mBalanceNumTv.setText(resultcount);
            mBalanceTextTv.setText(monthn + "月结余");

        }

    };

    // 绘图
    private void showChart(PieChart pieChart, PieData pieData) {
        //pieChart.setHoleColorTransparent(true);//旧版，设置中间空心圆孔的颜色
        pieChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        pieChart.setHoleRadius(60f); // 半径

        // pieChart.setDescription("109笔收入记录");//饼图右下角的提示
        //pieChart.setDescriptionColor(getResources().getColor(color.white));
        mPiechart.setDrawSliceText(false);// 设置隐藏饼图上文字，只显示百分比
        pieChart.setDrawCenterText(true); // 饼状图中间可以添加文字
        pieChart.setCenterTextColor(getResources().getColor(android.R.color.white));
        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setUsePercentValues(true); // 显示成百分比

        // pieChart.setCenterText("10000012.12"); // 饼状图中间的文字
        pieChart.setCenterTextSize(18);

        // 设置数据
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend(); // 设置比例图
        //mLegend.setPosition(LegendPosition.ABOVE_CHART_LEFT); // 最右边显示
        mLegend.setXEntrySpace(2f);
        mLegend.setYEntrySpace(2f);
        mLegend.setTextColor(getResources().getColor(android.R.color.white));

        pieChart.animateXY(1000, 1000); // 设置动画
    }

    /**
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        //ArrayList<String> xValues = new ArrayList<String>(); // xVals用来表示每个饼块上的内容

        //xValues.add("收入");
        //xValues.add("支出");
        // xValues.add("水费");
        // xValues.add("管理费");
        ArrayList<PieEntry> yValues = new ArrayList<>();
        //ArrayList<Entry> yValues = new ArrayList<Entry>(); // yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分，四部分的数值比例为14:14:34:38 所以 14代表的百分比就是14%
         */

        float innum = Float.parseFloat(mShowInNum);
        float outnum = Float.parseFloat(mShowOutNum);

        // float quarterly1 = 80.3f;
        // float quarterly2 = 5f;
        // float quarterly3 = 11.6f;
        // float quarterly4 = 3.1f;
        float quarterly1 = innum;
        float quarterly2 = outnum;
        //设置左下角的图示
        yValues.add(new PieEntry(innum, "收入"));
        yValues.add(new PieEntry(outnum, "支出"));
        // yValues.add(new Entry(quarterly3, 2));
        // yValues.add(new Entry(quarterly4, 3));

        // y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(74, 144, 226));
        colors.add(Color.rgb(9, 175, 169));
        // colors.add(Color.rgb(244, 185, 19));
        // colors.add(Color.rgb(126, 211, 33));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(getResources().getColor(android.R.color.white));

        return pieData;
    }


}
