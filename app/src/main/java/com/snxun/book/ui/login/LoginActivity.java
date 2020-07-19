package com.snxun.book.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;
import com.snxun.book.ui.money.main.MainActivity;
import com.snxun.book.utils.sp.SpManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    /**
     * 用户名输入框
     */
    @BindView(R.id.login_username_edit)
    EditText mLoginUsernameEdit;
    /**
     * 密码输入框
     */
    @BindView(R.id.login_password_edit)
    EditText mLoginPasswordEdit;
    /**
     * 忘记密码按钮
     */
    @BindView(R.id.login_forget_password_btn)
    TextView mLoginForgetPasswordBtn;
    /**
     * 登录按钮
     */
    @BindView(R.id.login_btn)
    ImageView mLoginBtn;
    /**
     * 注册按钮
     */
    @BindView(R.id.login_registered_btn)
    ImageView mLoginRegisteredBtn;
    /**
     * 数据库
     */
    private SQLiteDatabase mDb;
    /**
     * 登录进度条
     */
    private ProgressDialog mLoginDialog;
    /**
     * 得到数据库的用户名
     */
    private String mUsername;
    /**
     * 得到数据库的密码
     */
    private String mPassword;
    /**
     * 得到数据库的Id
     */
    private int mUserId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    /**
     * 设置监听
     */
    @Override
    protected void setListeners() {
        super.setListeners();

        //登录按钮
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cs;// 游标对象，用来报错查询返回的结果集
                //获取输入框的内容
                String username = mLoginUsernameEdit.getText().toString().trim();
                String password = mLoginPasswordEdit.getText().toString().trim();
                // 判断字符串为空
                // if(zhanghao==null || zhanghao.equals("")) 另一种方法
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "账号或者密码为空",
                            Toast.LENGTH_LONG).show();
                } else {
                    // 查找数据库是否存在相同的用户名
                    cs = mDb.query("user", new String[]{"userid", "username",
                                    "password"}, "username=?",
                            new String[]{username}, null, null, null);

                    if (cs != null) {
                        while (cs.moveToNext()) {
                            mUsername = cs.getString(cs
                                    .getColumnIndex("username"));
                            mPassword = cs.getString(cs
                                    .getColumnIndex("password"));
                            mUserId = cs.getInt(cs.getColumnIndex("userid"));
                        }

                        if (username.equals(mUsername)
                                && password.equals(mPassword)) {
                            Toast.makeText(LoginActivity.this, "登录成功",
                                    Toast.LENGTH_LONG).show();

                            //存储用户的登录成功的状态
                            SpManager.get().setUserStatus(true);
                            //存储用户的登录Id
                            SpManager.get().setUserId(mUserId);

                            Intent i = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "您输入的密码有误",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "当前账号不存在",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        //注册按钮
        mLoginRegisteredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context(), RegisteredActivity.class));
            }
        });

        //忘记密码按钮
        mLoginForgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context(), RetrievePasswordActivity.class));
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        initDb();
        isLoginStatus();
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        // 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
        mDb = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
    }

    private void isLoginStatus() {
        // 判断登录状态
        if (SpManager.get().getUserStatus()) {
            if (mLoginDialog == null) {
                mLoginDialog = new ProgressDialog(this);// 创建了圆形进度条对话框
                mLoginDialog.setMessage("登录中。。。");
                mLoginDialog.setCancelable(false);// 设置点击返回按钮无效
            }
            mLoginDialog.show();
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 0) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                mLoginDialog.dismiss();
                LoginActivity.this.finish();
            }
            return false;
        }
    });

    // /**
    // * 此方法必须重写，以决绝退出activity时 dialog未dismiss而报错的bug
    // */
    // @Override
    // protected void onDestroy() {
    // try{
    // myDialog.dismiss();
    // }catch (Exception e) {
    // System.out.println("myDialog取消，失败！");
    // }
    // super.onDestroy();
    // }

}