package com.example.bean;

import java.io.Serializable;

public class BudgetBean implements Serializable {
    private String money;// 金额
    private String remarks;// 备注
    private String daytime;// 时间
    private int id;
    private int userid;
    private String colorid;

    public BudgetBean(String money, String remarks, String daytime, int num,
                      int useridnum, String colorid) {
        super();
        this.money = money;
        this.remarks = remarks;
        this.daytime = daytime;
        this.id = num;
        this.userid = useridnum;
        this.setColorid(colorid);
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

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getColorid() {
        return colorid;
    }

    public void setColorid(String colorid) {
        this.colorid = colorid;
    }


}
