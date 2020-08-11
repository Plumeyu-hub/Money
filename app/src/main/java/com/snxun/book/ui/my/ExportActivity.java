package com.snxun.book.ui.my;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.db.DbFactory;
import com.snxun.book.utils.sp.SpManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExportActivity extends BaseActivity {

    /**
     * 返回按钮
     */
    @BindView(R.id.export_back_btn)
    ImageView mExportBackBtn;
    /**
     * 导出数据按钮
     */
    @BindView(R.id.export_btn)
    com.snxun.book.widget.ContentLayout mExportBtn;

    /**
     * 当前登录的用户ID
     */
    private String mAccount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_export;
    }


    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        //返回
        mExportBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //导出数据
        mExportBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showExportDialog();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        showUserInfo();
    }

    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        //获取SharedPreferences对象
        mAccount = SpManager.get().getUserAccount();
    }

    /**
     * 弹出一个普通对话框
     */
    private void showExportDialog() {
        // [1]构造对话框的实例
        Builder builder = new Builder(this);
        builder.setTitle(R.string.app_tip);
        builder.setMessage(R.string.export_text_dialog);
        // [2]设置确定和取消按钮
        builder.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                exportToCSV(DbFactory.create().allBillInfo(mAccount), "Bill.csv");
            }

        });
        builder.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // [3]展示对话框 和toast一样 一定要记得show出来
        builder.show();

    }

    /**
     * 导出数据
     *
     * @param cursor
     * @param fileName
     */
    public void exportToCSV(Cursor cursor, String fileName) {

        int rowCount = 0;
        int colCount = 0;
        FileWriter fw;
        BufferedWriter bfw;
        File sdCardDir = Environment.getExternalStorageDirectory();
        File saveFile = new File(sdCardDir, fileName);
        try {

            rowCount = cursor.getCount();
            colCount = cursor.getColumnCount();
            fw = new FileWriter(saveFile);
            bfw = new BufferedWriter(fw);
            if (rowCount > 0) {
                cursor.moveToFirst();
                // 写入表头
                for (int i = 0; i < colCount; i++) {
                    if (i != colCount - 1)
                        bfw.write(cursor.getColumnName(i) + ',');
                    else
                        bfw.write(cursor.getColumnName(i));
                }
                // 写好表头后换行
                bfw.newLine();
                // 写入数据
                for (int i = 0; i < rowCount; i++) {
                    cursor.moveToPosition(i);
                    Toast.makeText(this, "正在导出第" + (i + 1) + "条",
                            Toast.LENGTH_SHORT).show();
                    Log.v("导出数据", "正在导出第" + (i + 1) + "条");
                    for (int j = 0; j < colCount; j++) {
                        if (j != colCount - 1)
                            bfw.write(cursor.getString(j) + ',');
                        else
                            bfw.write(cursor.getString(j));
                    }
                    // 写好每条记录后换行
                    bfw.newLine();
                }
            }
            // 将缓存数据写入文件
            bfw.flush();
            // 释放缓存
            bfw.close();
            Toast.makeText(this, "导出完毕！", Toast.LENGTH_SHORT).show();
            Log.v("导出数据", "导出完毕！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }
}
