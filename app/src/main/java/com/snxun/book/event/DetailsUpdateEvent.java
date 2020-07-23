package com.snxun.book.event;

import com.snxun.book.ui.money.bean.DataBean;

/**
 * @author wangshy
 * @date 2020/07/23
 */
public class DetailsUpdateEvent {
    DataBean mdataBean;

    public DetailsUpdateEvent(DataBean mdataBean) {
        this.mdataBean=mdataBean;
    }

    public DataBean getDataBean() {
        return mdataBean;
    }
}
