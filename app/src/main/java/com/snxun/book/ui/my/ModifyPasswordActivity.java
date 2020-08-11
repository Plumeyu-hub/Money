package com.snxun.book.ui.my;

import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.utils.sp.SpManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifyPasswordActivity extends BaseActivity {

    /**
     * 确定修改密码按钮
     */
    @BindView(R.id.modify_pw_yes_btn)
    TextView mModifyPwYesBtn;
    /**
     * 密码1
     */
    @BindView(R.id.modify_pw_password_edit)
    EditText mModifyPwPasswordEdit;
    /**
     * 密码2
     */
    @BindView(R.id.modify_pw_password_two_edit)
    EditText mModifyPwPasswordTwoEdit;
    /**
     * 获取用户输入的密码
     */
    private String pwOne, pwTow;

    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    private ContentValues cv;// 存储工具栏
    private int num = 0;
    /**
     * 当前登录的用户账号
     */
    private String mAccount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_password;
    }


    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        initTitleLayout();
    }

    private void initTitleLayout() {
        showTitleBar();
        getTitleLayout().setTitleName(R.string.persoal_info_modify_pw);
    }

    @Override
    protected void clickBackBtn() {
        super.clickBackBtn();
        finish();
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListeners() {
        super.setListeners();

        //确定修改密码
        mModifyPwYesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pwOne = mModifyPwPasswordEdit.getText().toString().trim();
                pwTow = mModifyPwPasswordTwoEdit.getText().toString().trim();
                showNormalDialog();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        showUserInfo();
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
    }

    /**
     * 获取当前登录用户的Id
     */
    private void showUserInfo() {
        //获取SharedPreferences对象
        mAccount = SpManager.get().getUserAccount();
    }

    // 弹出一个普通对话框
    private void showNormalDialog() {
        // [1]构造对话框的实例
        Builder builder = new Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否确认此修改？");
        // [2]设置确定和取消按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (pwOne == null || pwOne.length() == 0 || pwTow == null
                        || pwTow.length() == 0) {
                    Toast.makeText(ModifyPasswordActivity.this,
                            "对不起，您输入的密码或确认密码不能为空", Toast.LENGTH_LONG).show();
                } else if (pwOne.equals(pwTow) == false) {
                    Toast.makeText(ModifyPasswordActivity.this, "对不起，您输入的密码和确认密码不一致",
                            Toast.LENGTH_LONG).show();
                } else {
                    String userId = String.valueOf(mAccount);
                    // 修改数据
                    // 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
                    cv = new ContentValues();
                    cv.put("password", pwOne);
                    // 修改数据，返回修改成功的行数，失败则返回0
                    num = mDb.update("user", cv, "userid=?", new String[]{userId
                            + ""});
                    if (num > 0) {
                        Toast.makeText(ModifyPasswordActivity.this, "修改成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ModifyPasswordActivity.this, "修改失败",
                                Toast.LENGTH_SHORT).show();
                    }

                }
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
