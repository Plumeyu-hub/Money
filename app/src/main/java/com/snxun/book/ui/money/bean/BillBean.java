package com.snxun.book.ui.money.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangshy
 * @date 2020/07/27
 */
public class BillBean implements Serializable {
    private Long id;
    private String category;// 类别
    private Long money;// 金额
    private Date date;// 日期
    private String mode;// (金额扣除或增加的)账户或方式
    private String remark;// 备注
    private int symbol;//符号（0为支出，1为收入）
    private String account;//用户账号

    public BillBean(String category, Long money, Date date, String mode, String remark, int symbol, String account) {
        super();
        this.category = category;
        this.money = money;
        this.date = date;
        this.mode = mode;
        this.remark = remark;
        this.symbol = symbol;
        this.account = account;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
