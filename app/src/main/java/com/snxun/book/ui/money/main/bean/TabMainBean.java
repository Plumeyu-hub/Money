package com.snxun.book.ui.money.main.bean;

/**
 * 栏目数据
 * @author wangshy
 * @date 2020/07/10
 */
public class TabMainBean {

    /** 明细id */
    public static final int TAB_MAIN_DETAILS_ID = 0;
    /** 图标id */
    public static final int TAB_MAIN_GRAPH_ID = 1;

    /** 编号 */
    private int id;
    /** 标题 */
    private String title;

    public TabMainBean(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
