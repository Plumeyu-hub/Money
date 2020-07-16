package com.snxun.book.ui.my.budget.bean;

import java.io.Serializable;

public class BudgetBean implements Serializable {
    /**
     * 金额
     */
    private String money;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 时间
     */
    private String date;
    /**
     * 预算编号
     */
    private int id;
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 颜色（是否超出预算）
     */
    private String color;

    public BudgetBean(String money, String remarks, String date, int num,
                      int useridnum, String color) {
        super();
        this.money = money;
        this.remarks = remarks;
        this.date = date;
        this.id = num;
        this.userId = useridnum;
        this.setColor(color);
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
