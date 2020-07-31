package com.snxun.book.bean.budget;

import com.snxun.book.bean.base.BaseRequestBean;

/**
 * @author zhouL
 * @date 2020/7/30
 */
public class BudgetRequestBean extends BaseRequestBean {

    /** 日期 */
    public String date;
    /** 数量 */
    public int num;

    public BudgetRequestBean() {
    }

    public BudgetRequestBean(String date, int num) {
        this.date = date;
        this.num = num;
    }
}