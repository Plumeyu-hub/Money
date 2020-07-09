package com.example.ui.login;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.money.MainActivity;
import com.example.money.R;

public class LoginActivity extends Activity {
    private EditText et_username, et_password;
    private TextView tv_login, tv_login_forgetpw;
    private String usernamesql, passwordsql;
    private int useridsql;
    // 数据库
    private SQLiteDatabase db;// 数据库对象
    // private ContentValues cv;// 存储工具栏
    // private int num = 0;


    // 判断登录状态
    private Boolean bool_login = false;
    private SharedPreferences.Editor editor_login, editor_user;

    // 定时器
    AlarmManager manager;// 定时服务
    PendingIntent pIntent;// 延时意图

    // 登录进度条
    private ProgressDialog Pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

		// 判断登录状态
        SharedPreferences sp_login, sp_user;
        db = this.openOrCreateDatabase("data.db", MODE_PRIVATE, null);
        db.execSQL("create table if not exists user (userid integer primary key,username text,password text,problem text,answer text)");

        et_username = (EditText) this.findViewById(R.id.username_et);
        et_password = (EditText) this.findViewById(R.id.password_et);

        // sharedPerference sp=this.getSharedPerference();
        // boolean bool=sp.getBoolean("isLoad",false);
        // if(bool==false){
        //
        // }else{
        // 定时器
        // }
        // 判断登录状态
        sp_login = this.getSharedPreferences("userLogin", MODE_PRIVATE);
        editor_login = sp_login.edit();
        bool_login = sp_login.getBoolean("isLoad", false);
		editor_login.apply();
        // 存储用户名
        sp_user = this.getSharedPreferences("user", MODE_PRIVATE);
        editor_user = sp_user.edit();
		editor_user.apply();
        if (bool_login) {

            if (Pdialog == null) {
                Pdialog = new ProgressDialog(this);// 创建了圆形进度条对话框
                Pdialog.setMessage("登录中。。。");
                Pdialog.setCancelable(false);// 设置点击返回按钮无效
            }
            Pdialog.show();
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 0) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                Pdialog.dismiss();
                LoginActivity.this.finish();
            }
            return false;
        }
    });


    public void click(View v) {
        switch (v.getId()) {
            case R.id.registered_tv:
                Intent i1 = new Intent(this, RegisteredActivity.class);
                startActivity(i1);
                break;
            case R.id.forgetpw_tv:
                Intent i2 = new Intent(this, RetrievePasswordActivity.class);
                startActivity(i2);
                break;
            case R.id.login_tv:
				Cursor cs;// 游标对象，用来报错查询返回的结果集
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                // 判断字符串为空
                // if(zhanghao==null || zhanghao.equals("")){
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "账号或者密码为空",
                            Toast.LENGTH_LONG).show();
                } else {
                    // 查找数据库是否存在相同的用户名
                    cs = db.query("user", new String[]{"userid", "username",
                                    "password"}, "username=?",
                            new String[]{username}, null, null, null);

                    if (cs != null) {
                        while (cs.moveToNext()) {
                            usernamesql = cs.getString(cs
                                    .getColumnIndex("username"));
                            passwordsql = cs.getString(cs
                                    .getColumnIndex("password"));
                            useridsql = cs.getInt(cs.getColumnIndex("userid"));
                        }

                        if (username.equals(usernamesql)
                                && password.equals(passwordsql)) {
                            Toast.makeText(LoginActivity.this, "登录成功",
                                    Toast.LENGTH_LONG).show();
                            bool_login = true;
                            editor_login.putBoolean("isLoad", bool_login);
                            editor_login.commit();

                            editor_user.clear();
                            editor_user.putString("username", usernamesql);
                            // System.out.println(usernamesql);
                            editor_user.putInt("userid", useridsql);
                            // System.out.println(useridsql);
                            editor_user.commit();

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
                break;
            default:
                break;
        }
    }
    // /**
    // * 此方法必须重写，以决绝退出activity时 dialog未dismiss而报错的bug
    // */
    // @Override
    // protected void onDestroy() {
    // // TODO Auto-generated method stub
    // try{
    // myDialog.dismiss();
    // }catch (Exception e) {
    // System.out.println("myDialog取消，失败！");
    // // TODO: handle exception
    // }
    // super.onDestroy();
    // }

}