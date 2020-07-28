package com.snxun.book.greendaolib.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

/**
 * 账单信息表
 * @author wangshy
 * @date 2020/07/27
 */
@Entity
public class BillTable {
    /** 主键，编号 */
    @Id(autoincrement = true)
    private Long id;
    /** 类别 */
    @NotNull
    private String category;
    /** 金额 */
    @NotNull
    private Long money;
    /** 日期 */
    @NotNull
    private Date date;
    /** (金额扣除或增加的)账户或方式 */
    @NotNull
    private String mode;
    /** 备注 */
    private String remark;
    /** 符号（0为支出，1为收入） */
    @NotNull
    private int symbol;
    /** 用户账号 */
    @NotNull
    private String account;
    @Generated(hash = 1881477837)
    public BillTable(Long id, @NotNull String category, @NotNull Long money,
            @NotNull Date date, @NotNull String mode, String remark, int symbol,
            @NotNull String account) {
        this.id = id;
        this.category = category;
        this.money = money;
        this.date = date;
        this.mode = mode;
        this.remark = remark;
        this.symbol = symbol;
        this.account = account;
    }
    @Generated(hash = 414826536)
    public BillTable() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCategory() {
        return this.category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Long getMoney() {
        return this.money;
    }
    public void setMoney(Long money) {
        this.money = money;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getMode() {
        return this.mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public int getSymbol() {
        return this.symbol;
    }
    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    
}
