package com.snxun.book.ui.my.demo.gr;

import java.io.Serializable;

/**
 * @author wangshy
 * @date 2020/07/20
 */
public class GrDataBean implements Serializable {
    private String mText;
    private int mIcon;

    public GrDataBean(String mText, int mIcon) {
        this.mText = mText;
        this.mIcon = mIcon;
    }

    public String getmText() {
        return mText;
    }

    public int getmIcon() {
        return mIcon;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }
}
