package com.snxun.book.ui.money.bean;

import java.io.Serializable;

public class DataBean implements Serializable {
	private String mCategory;// 类别
	private String mMoney;// 金额
	private String mAccount;// 账户
	private String mRemarks;// 备注
	private String mDate;// 时间
	private int mId;
	private String mUserId;

	public DataBean(String mCategory, String mMoney, String mAccount,
					String mRemarks, String mDate, int mId, String mUserId) {
		super();
		this.mCategory = mCategory;
		this.mMoney = mMoney;
		this.mAccount = mAccount;
		this.mRemarks = mRemarks;
		this.mDate = mDate;
		this.mId = mId;
		this.mUserId = mUserId;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public String getmCategory() {
		return mCategory;
	}

	public void setmCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public String getmMoney() {
		return mMoney;
	}

	public void setmMoney(String mMoney) {
		this.mMoney = mMoney;
	}

	public String getmAccount() {
		return mAccount;
	}

	public void setmAccount(String mAccount) {
		this.mAccount = mAccount;
	}

	public String getmRemarks() {
		return mRemarks;
	}

	public void setmRemarks(String mRemarks) {
		this.mRemarks = mRemarks;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

	public String getmUserId() {
		return mUserId;
	}

	public void setmUserId(String mUserId) {
		this.mUserId = mUserId;
	}
}
