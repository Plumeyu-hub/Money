package com.snxun.book.ui.money.bean;

import java.io.Serializable;

/**
 * @author wangshy
 * @date 2020/07/20
 */
public class RvGrBean implements Serializable {
    private String name;
    private int imgResId;
    private boolean isSelected;

    public RvGrBean(String name, int imgResId, boolean isSelected) {
        this.name = name;
        this.imgResId = imgResId;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
