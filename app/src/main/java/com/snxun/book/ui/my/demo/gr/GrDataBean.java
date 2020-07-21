package com.snxun.book.ui.my.demo.gr;

import java.io.Serializable;

/**
 * @author wangshy
 * @date 2020/07/20
 */
public class GrDataBean implements Serializable {
    public String name;
    public int imgResId;
    public boolean isSelected;

    public GrDataBean(String name, int imgResId, boolean isSelected) {
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
