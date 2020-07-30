package com.snxun.book.bean.update;

import java.io.Serializable;

/**
 * @author wangshy
 * @date 2020/07/27
 */
public class UpdateBean implements Serializable {
    private Long money;// 金额
    private String date;// 日期
    private String mode;// (金额扣除或增加的)账户或方式
    private String remark;// 备注

    public UpdateBean(Long money, String date, String mode, String remark) {
        this.money = money;
        this.date = date;
        this.mode = mode;
        this.remark = remark;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
