package com.snxun.book.ui.login;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.snxun.book.R;
import com.snxun.book.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RetrievePasswordActivity extends BaseActivity {

	/**
	 * 返回按钮
	 */
	@BindView(R.id.retrieve_password_return_btn)
	ImageView mRetrievePasswordReturnBtn;
	/**
	 * 用户名
	 */
	@BindView(R.id.retrieve_password_username_edit)
	EditText mRetrievePasswordUsernameEdit;
	/**
	 * 密保问题
	 */
	@BindView(R.id.retrieve_password_problem_edit)
	EditText mRetrievePasswordProblemEdit;
	/**
	 * 密保答案
	 */
	@BindView(R.id.retrieve_password_answer_edit)
	EditText mRetrievePasswordAnswerEdit;
	/**
	 * 确定按钮
	 */
	@BindView(R.id.retrieve_password_btn)
	TextView mRetrievePasswordBtn;
	/**
	 * 返回密码的tv
	 */
	@BindView(R.id.retrieve_password_tv)
	TextView mRetrievePasswordTv;

	private String mUsername, mProblem, mAnswer, mPasswordSql,
			mProblemSql, mAnswerAql;

	/**
	 * 数据库
	 */
	private SQLiteDatabase mDb;
	private Cursor mCursor;// 游标对象，用来报错查询返回的结果集

	@Override
	protected int getLayoutId() {
		return R.layout.activity_retrieve_password;
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
		mRetrievePasswordReturnBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		mRetrievePasswordBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mUsername = mRetrievePasswordUsernameEdit.getText().toString().trim();
				mProblem = mRetrievePasswordProblemEdit.getText().toString().trim();
				mAnswer = mRetrievePasswordAnswerEdit.getText().toString().trim();

				if (mUsername == null || mUsername.length() == 0
						|| mProblem == null || mProblem.length() == 0
						|| mAnswer == null || mAnswer.length() == 0) {
					Toast.makeText(RetrievePasswordActivity.this, "对不起，请填写完整的注册信息",
							Toast.LENGTH_LONG).show();
				} else {
					// 查找数据库是否存在相同的用户名
					mCursor = mDb.query("user", new String[] { "username",
									"password", "problem", "answer" }, "username=?",
							new String[] {mUsername}, null, null, null);
					if (mCursor != null) {
						while (mCursor.moveToNext()) {
							mPasswordSql = mCursor.getString(mCursor
									.getColumnIndex("password"));
							mProblemSql = mCursor.getString(mCursor
									.getColumnIndex("problem"));
							mAnswerAql = mCursor.getString(mCursor
									.getColumnIndex("answer"));
						}
						if (mProblem.equals(mProblemSql)
								&& mAnswer.equals(mAnswerAql)) {
							Toast.makeText(RetrievePasswordActivity.this, "已查询到您的密码",
									Toast.LENGTH_LONG).show();
							mRetrievePasswordTv.setText("您的密码为：" + mPasswordSql);
						} else {
							Toast.makeText(RetrievePasswordActivity.this,
									"您输入的密保信息有误", Toast.LENGTH_LONG).show();
							mRetrievePasswordTv.setText(" ");
						}
					} else {
						Toast.makeText(RetrievePasswordActivity.this, "未查询到该用户",
								Toast.LENGTH_LONG).show();
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
	}

}