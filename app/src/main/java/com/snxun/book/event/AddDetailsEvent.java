package com.snxun.book.event;

import com.snxun.book.ui.money.bean.BillBean;

/**
 * @author wangshy
 * @date 2020/07/23
 */
public class AddDetailsEvent {
    private BillBean billBean;

    public AddDetailsEvent(BillBean billBean) {
        this.billBean=billBean;
    }

    public BillBean getBillBean() {
        return billBean;
    }
}
