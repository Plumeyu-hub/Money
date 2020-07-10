package com.example.demo.tab;

/**
 * 栏目数据
 * @author zhouL
 * @date 2020/7/9
 */
public class TabBean {

    /** 明细id */
    public static final int TAB_DETAILS_ID = 0;
    /** 图标id */
    public static final int TAB_GRAPH_ID = 1;

    /** 编号 */
    private int id;
    /** 标题 */
    private String title;

    public TabBean(int id, String title) {
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
