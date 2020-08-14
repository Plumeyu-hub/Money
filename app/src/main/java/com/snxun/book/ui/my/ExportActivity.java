package com.snxun.book.ui.my;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lodz.android.core.utils.AppUtils;
import com.lodz.android.core.utils.ToastUtils;
import com.lodz.android.core.utils.UiHandler;
import com.snxun.book.App;
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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 在需要动态申请权限的类上添加 @RuntimePermissions 的注解
 */
@RuntimePermissions
public class ExportActivity extends BaseActivity {

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
        initTitleLayout();
    }

    private void initTitleLayout() {
        showTitleBar();
        getTitleLayout().setTitleName(R.string.drawer_export);
    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }

    @Override
    protected void setListeners() {
        super.setListeners();

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
        UiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
                    //申请权限
                    ExportActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(ExportActivity.this);
                } else {
                    init();
                }
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ExportActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);//将回调交给代理类处理
    }

    /**
     * 权限申请成功 请求许可
     */
    //单一权限
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    //多权限
    //    @NeedsPermission({
    //            Manifest.permission.READ_PHONE_STATE,// 手机状态
    //            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 存储卡读写
    //            Manifest.permission.READ_EXTERNAL_STORAGE,// 存储卡读写
    //    })
    protected void requestPermission() {
        //        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.READ_PHONE_STATE)){
        //            return;
        //        }
        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return;
        }
        //        if (!AppUtils.isPermissionGranted(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)){
        //            return;
        //        }
        init();
    }

    /**
     * 被拒绝
     */
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    protected void onDenied() {
        ExportActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(this);//申请权限
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    protected void showRationaleBeforeRequest(PermissionRequest request) {
        request.proceed();//请求权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    protected void onNeverAskAgain() {
        ToastUtils.showShort(getContext(), "请在权限页面打开应用所有权限");
        showPermissionCheckDialog();
        AppUtils.jumpAppDetailSetting(this);
    }

    /** 显示权限核对弹框 */
    private void showPermissionCheckDialog() {
        CheckDialog dialog = new CheckDialog(getContext());
        dialog.setContentMsg("请确认已打开该应用使用的所有权限");
        dialog.setPositiveText("已确认", new CheckDialog.Listener() {
            @Override
            public void onClick(Dialog dialog) {
                ExportActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(ExportActivity.this);//申请权限
                dialog.dismiss();
            }
        });
        dialog.setNegativeText("再次确认", new CheckDialog.Listener() {
            @Override
            public void onClick(Dialog dialog) {
                AppUtils.jumpAppDetailSetting(getContext());
            }
        });
        //设置点击Dialog以外的区域时Dialog不消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ToastUtils.showShort(getContext(),"请在设置中打开权限后重启应用");
                App.get().exit();
            }
        });
        dialog.show();
    }


    private void init() {
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
