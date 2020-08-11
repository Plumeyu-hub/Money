package com.snxun.book.ui.my;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.login.LoginActivity;
import com.snxun.book.utils.sp.SpManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalInfoActivity extends BaseActivity {

    /**
     * 修改密码
     */
    @BindView(R.id.modify_pw_btn)
    ImageView mModifyPwBtn;
    /**
     * 退出账户
     */
    @BindView(R.id.exit_user_btn)
    ImageView mExitUserBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }


    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initTitleLayout();
    }

    private void initTitleLayout() {
        showTitleBar();
        getTitleLayout().setTitleName(R.string.persoal_info_title);
    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        mModifyPwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalInfoActivity.this, ModifyPasswordActivity.class));
            }
        });
        mExitUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    // 弹出一个普通对话框
    private void showNormalDialog() {
        // [1]构造对话框的实例
        Builder builder = new Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否退出此账号？");
        // [2]设置确定和取消按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除sp存储
                SpManager.get().deleteSp();
                startActivity(new Intent(PersonalInfoActivity.this, LoginActivity.class));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // [3]展示对话框 和toast一样 一定要记得show出来
        builder.show();

    }
}