package com.snxun.book.ui.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisteredActivity extends BaseActivity {


	/**
	 * 返回按钮
	 */
	@BindView(R.id.registered_back_btn)
	ImageView mRegisteredBackBtn;
	/**
	 * 用户名
	 */
	@BindView(R.id.registered_username_edit)
	EditText mRegisteredUsernameEdit;
	/**
	 *密码
	 */
	@BindView(R.id.registered_password_edit)
	EditText mRegisteredPasswordEdit;
	/**
	 *确认密码
	 */
	@BindView(R.id.registered_password_two_edit)
	EditText mRegisteredPasswordTwoEdit;
	/**
	 *密保问题
	 */
	@BindView(R.id.registered_problem_edit)
	EditText mRegisteredProblemEdit;
	/**
	 *密保答案
	 */
	@BindView(R.id.registered_answer_edit)
	EditText mRegisteredAnswerEdit;
	/**
	 *注册按钮
	 */
	@BindView(R.id.registered_btn)
	ImageView mRegisteredBtn;

	private String mUsername, mPassword, mPasswordTwo, mUsernameSql, mProblem,
			mAnswer;
	/**
	 * 数据库
	 */
	private SQLiteDatabase mDb;
	private ContentValues mCv;// 存储工具栏
	private int num = 0;
	private Cursor mCursor;// 游标对象，用来报错查询返回的结果集

	@Override
	protected int getLayoutId() {
		return R.layout.activity_registered;
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

		//返回
		mRegisteredBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		mRegisteredBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				mUsername = mRegisteredUsernameEdit.getText().toString().trim();
				mPassword = mRegisteredPasswordEdit.getText().toString().trim();
				mPasswordTwo = mRegisteredPasswordTwoEdit.getText().toString().trim();
				mProblem = mRegisteredProblemEdit.getText().toString().trim();
				mAnswer = mRegisteredAnswerEdit.getText().toString().trim();

				if (mUsername == null || mUsername.length() == 0
						|| mPassword == null || mPassword.length() == 0
						|| mPasswordTwo == null || mPasswordTwo.length() == 0
						|| mProblem == null || mProblem.length() == 0
						|| mAnswer == null || mAnswer.length() == 0) {
					Toast.makeText(RegisteredActivity.this, "对不起，请填写完整的注册信息",
							Toast.LENGTH_LONG).show();
				} else if (mPassword.equals(mPasswordTwo) == false) {
					Toast.makeText(RegisteredActivity.this,
							"对不起，您输入的密码和确认密码不一致", Toast.LENGTH_LONG).show();

				} else {

					// 查找数据库是否存在相同的用户名
					mCursor = mDb.query("user", new String[] { "username" },
							"username like ? ", new String[] {mUsername},
							null, null, null);

					if (mCursor != null) {
						while (mCursor.moveToNext()) {
							mUsernameSql = mCursor.getString(mCursor
									.getColumnIndex("username"));
						}
					}

					if (mUsername.equals(mUsernameSql)) {
						Toast.makeText(RegisteredActivity.this, "您输入的用户名已存在",
								Toast.LENGTH_LONG).show();
					} else {
						// 在存储工具类里面存储要操作的数据，以键值对的方式存储，键表示标的列名，值就是要操作的值
						mCv = new ContentValues();
						mCv.put("password", mPassword);
						mCv.put("problem", mProblem);
						mCv.put("answer", mAnswer);
						mCv.put("username", mUsername);
						// 插入数据，成功返回当前行号，失败返回0
						num = (int) mDb.insert("user", null, mCv);
						if (num > 0) {
							Toast.makeText(RegisteredActivity.this,
									"用户注册成功" + num, Toast.LENGTH_SHORT).show();
							finish();
						} else {
							Toast.makeText(RegisteredActivity.this,
									"用户注册失败" + num, Toast.LENGTH_SHORT).show();
						}
					}

				}
			}
		});
	}

	@Override
	protected void initData() {
		super.initData();
		initDb();
	}

	/**
	 * 初始化数据库
	 */
	private void initDb() {
		// 如果data.db数据库文件不存在，则创建并打开；如果存在，直接打开
		mDb = openOrCreateDatabase("data.db", Context.MODE_PRIVATE, null);
		mDb.execSQL("create table if not exists user (userid integer primary key,username text,password text,problem text,answer text)");

	}

}