package com.snxun.book.ui.my.budget.bean;

import java.io.Serializable;

public class BudgetBean implements Serializable {
    /**
     * 金额
     */
    private String mMoney;
    /**
     * 备注
     */
    private String mRemarks;
    /**
     * 时间
     */
    private String mDate;
    /**
     * 预算编号
     */
    private int mId;
    /**
     * 用户ID
     */
    private int mUserId;
    /**
     * 颜色（是否超出预算）
     */
    private boolean isSelected;

    public BudgetBean(String mMoney, String mRemarks, String mDate, int mId,
                      int mUserId, boolean isSelected) {
        super();
        this.mMoney = mMoney;
        this.mRemarks = mRemarks;
        this.mDate = mDate;
        this.mId = mId;
        this.mUserId = mUserId;
        this.isSelected = isSelected;
    }

    public String getmMoney() {
        return mMoney;
    }

    public void setmMoney(String mMoney) {
        this.mMoney = mMoney;
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

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }
}
