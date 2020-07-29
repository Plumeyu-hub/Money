package com.snxun.book.event;

/**
 * 添加账单页面-明细页面的EventBus
 * @author wangshy
 * @date 2020/07/23
 */
public class AddDetailsEvent {
    private String date;

    public AddDetailsEvent(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
