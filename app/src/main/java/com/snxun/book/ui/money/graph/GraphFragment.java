package com.snxun.book.ui.money.graph;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.snxun.book.base.BaseFragment;
import com.snxun.book.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;

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

    // 时间
    private EditText monthtime;
    // db
    private SQLiteDatabase DB;
    private Cursor cs;// 游标对象，用来报错查询返回的结果集
    private float outsum = 0;
    private float insum = 0;
    private float countsum = 0;

    TextView tv_graph_countnum, tv_graph_counttext, tv_graph_outnum,
            tv_graph_outtext, tv_graph_innum, tv_graph_intext,
            tv_graph_countnumgone, tv_graph_outnumgone,
            tv_graph_innumgone;

    boolean state = true;

    String resultinnum, resultoutnum;// 每月支出和收入

    // 绘图
    private PieChart mChart;
    int O = 0;

    // 操作用户名
    private SharedPreferences spuser;
    private SharedPreferences.Editor editoruser;
    private int userid;

    //隐藏
    Button button;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_graph;
    }

    @Override
    protected void findViews(View view) {
        ButterKnife.bind(this, view);

        tv_graph_countnum = (TextView) view.findViewById(R.id.balancenum_tv);
        tv_graph_counttext = (TextView) view.findViewById(R.id.balancetext_tv);
        tv_graph_outnum = (TextView) view.findViewById(R.id.outnum_tv);
        tv_graph_outtext = (TextView) view.findViewById(R.id.outtext_tv);
        tv_graph_innum = (TextView) view.findViewById(R.id.innum_tv);
        tv_graph_intext = (TextView) view.findViewById(R.id.intext_tv);
        tv_graph_countnumgone = (TextView) view.findViewById(R.id.balancegone_tv);
        tv_graph_outnumgone = (TextView) view.findViewById(R.id.outgone_tv);
        tv_graph_innumgone = (TextView) view.findViewById(R.id.ingone_tv);
        // 时间
        // 获取对象
        monthtime = (EditText) view.findViewById(R.id.graphmonth_edit);

        // 绘图
        mChart = (PieChart) view.findViewById(R.id.piechart);

        //隐藏
        button= (Button) view.findViewById(R.id.eyenor_btn);


    }

    @Override
    protected void setListeners(View view) {
        super.setListeners(view);
        // 点击"日期"按钮布局 设置日期
        monthtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        //隐藏
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state) {// 可见
                    button.setBackgroundResource(R.drawable.ic_eye_nor);
                    state =false;

                    tv_graph_countnumgone.setVisibility(View.GONE);
                    tv_graph_countnum.setVisibility(View.VISIBLE);

                    tv_graph_outnumgone.setVisibility(View.GONE);
                    tv_graph_outnum.setVisibility(View.VISIBLE);

                    tv_graph_innumgone.setVisibility(View.GONE);
                    tv_graph_innum.setVisibility(View.VISIBLE);

                } else {// 隐藏
                    button.setBackgroundResource(R.drawable.ic_eye_sel);
                    state = true;

                    tv_graph_countnumgone.setVisibility(View.VISIBLE);
                    tv_graph_countnum.setVisibility(View.GONE);

                    tv_graph_outnumgone.setVisibility(View.VISIBLE);
                    tv_graph_outnum.setVisibility(View.GONE);

                    tv_graph_innumgone.setVisibility(View.VISIBLE);
                    tv_graph_innum.setVisibility(View.GONE);

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
        DB = getActivity().openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
    }

    private void showUserInfo() {
        // 操作用户名
        spuser = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        editoruser = spuser.edit();
        if (spuser != null) {// 判断文件是否存在
            userid = spuser.getInt("userid", 0);
        }
    }


    // 外
    // 获取当前日期
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    private void showDatePicker() {

        // 创建并显示DatePickerDialog
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT, Datelistener, year, month, day);
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

    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        /**
         * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear,
                              int dayOfMonth) {

            // 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            // 更新日期
            updateDate();

        }

        // 当DatePickerDialog关闭时，更新日期显示
        private void updateDate() {
            // 在TextView上显示日期
            monthtime.setText(new StringBuilder().append(year).append("年")
                    .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
                    .append("月"));
            //monthtime.setTextColor(getResources().getColor(R.color.white));
            monthtime.setTextColor(Color.WHITE);

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
            String uid = String.valueOf(userid);
            cs = DB.rawQuery(
                    "select sum(aomoney) from expenditure where aotime like ? and aouserid=?",
                    new String[] { condition + "%", uid + "" });

            if (cs != null) {
                if (cs.moveToFirst()) {
                    do {
                        outsum = cs.getFloat(0);

                    } while (cs.moveToNext());
                }
                // System.out.println(outsum);
                resultoutnum = String.format("%.2f", outsum);
                tv_graph_outnum.setText("-" + resultoutnum);
                tv_graph_outtext.setText(monthn + "月支出");
            } else {
                tv_graph_outnum.setText("0.00");
                tv_graph_outtext.setText(monthn + "月支出");
            }

            cs = DB.rawQuery(
                    "select sum(aimoney) from income where aitime like ? and aiuserid=?",
                    new String[] { condition + "%", uid + "" });

            if (cs != null) {
                if (cs.moveToFirst()) {
                    do {
                        insum = cs.getFloat(0);

                    } while (cs.moveToNext());
                }
                resultinnum = String.format("%.2f", insum);
                tv_graph_innum.setText("+" + resultinnum);
                tv_graph_intext.setText(monthn + "月收入");
            } else {
                tv_graph_innum.setText("0.00");
                tv_graph_intext.setText(monthn + "月收入");
            }
            countsum = insum - outsum;
            PieData mPieData = getPieData(4, 100);
            showChart(mChart, mPieData);
            String resultcount = String.format("%.2f", countsum);
            tv_graph_countnum.setText(resultcount);
            tv_graph_counttext.setText(monthn + "月结余");

        }

    };

    // 绘图
    private void showChart(PieChart pieChart, PieData pieData) {
        //pieChart.setHoleColorTransparent(true);//旧版，设置中间空心圆孔的颜色
        pieChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        pieChart.setHoleRadius(60f); // 半径

        // pieChart.setDescription("109笔收入记录");//饼图右下角的提示
        //pieChart.setDescriptionColor(getResources().getColor(color.white));
        mChart.setDrawSliceText(false);// 设置隐藏饼图上文字，只显示百分比
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

        float innum = Float.parseFloat(resultinnum);
        float outnum = Float.parseFloat(resultoutnum);

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