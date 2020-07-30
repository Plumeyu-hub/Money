package com.snxun.book.event;

/**
 * 添加账单页面-明细页面的EventBus
 * @author wangshy
 * @date 2020/07/23
 */
public class RefreshEvent {
    private boolean refresh;

    public RefreshEvent(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean getRefresh() {
        return refresh;
    }
}
