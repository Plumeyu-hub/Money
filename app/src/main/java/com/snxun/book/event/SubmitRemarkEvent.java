package com.snxun.book.event;

/**
 * 备注的通知事件
 * @author wangshy
 * @date 2020/07/23
 */
public class SubmitRemarkEvent {

    private String text;

    public SubmitRemarkEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
