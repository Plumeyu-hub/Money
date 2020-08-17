package com.snxun.book.event;

/**
 * @author wangshy
 * @date 2020/08/17
 */
public class RemindEvent {
    private long time;

    public RemindEvent(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}