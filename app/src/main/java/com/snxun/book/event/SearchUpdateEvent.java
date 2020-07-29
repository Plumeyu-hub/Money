package com.snxun.book.event;

import com.snxun.book.greendaolib.table.BillTable;

/**
 * @author wangshy
 * @date 2020/07/23
 */
public class SearchUpdateEvent {
    BillTable billTable;

    public SearchUpdateEvent(BillTable billTable) {
        this.billTable=billTable;
    }

    public BillTable getBillTable() {
        return billTable;
    }
}
