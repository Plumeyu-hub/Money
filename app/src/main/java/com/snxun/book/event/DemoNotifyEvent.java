package com.snxun.book.event;

/**
 * 样例的通知事件
 * @author zhouL
 * @date 2020/7/20
 */
public class DemoNotifyEvent {

    private String text;

    public DemoNotifyEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
