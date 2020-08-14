package com.snxun.book.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.snxun.book.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 侧拉栏
 *
 * @author wangshy
 * @date 2020/08/10
 */
public class DrawerItemLayout extends LinearLayout {

    /**
     * 整个布局，btn
     */
    @BindView(R.id.btn_re)
    RelativeLayout mBtnRe;
    /**
     * 图片
     */
    @BindView(R.id.icon_img)
    TextView mIconImg;
    /**
     * 文字
     */
    @BindView(R.id.text_tv)
    TextView mTextTv;
    /**
     * next图片
     */
    @BindView(R.id.next_img)
    TextView mNextImg;

    public DrawerItemLayout(Context context) {
        super(context);
        init(null);
    }

    public DrawerItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DrawerItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawerItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        findViews();
        config(attrs);
    }

    private void findViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_drawer_item_layout, this);
        ButterKnife.bind(this);
    }

    private void config(AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DrawerItemLayout);
        }

        Drawable iconDrawable = typedArray == null ? null : typedArray.getDrawable(R.styleable.DrawerItemLayout_iconImg);
        if (iconDrawable != null) {
            setDrawable(iconDrawable, mIconImg);
        }

        String text = typedArray == null ? "" : typedArray.getString(R.styleable.DrawerItemLayout_textTv);
        if (!TextUtils.isEmpty(text)) {
            setTitleName(text);
        }

        Drawable nextDrawable = typedArray == null ? null : typedArray.getDrawable(R.styleable.DrawerItemLayout_nextImg);
        if (nextDrawable != null) {
            setDrawable(nextDrawable, mNextImg);
        }

        if (typedArray != null) {
            typedArray.recycle();
        }
    }

    /**
     * 设置text
     *
     * @param title text名
     */
    public void setTitleName(String title) {
        mTextTv.setText(title);
    }

    /**
     * 设置img
     *
     * @param drawable drawable
     */
    public void setDrawable(Drawable drawable, TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    /**
     * 请重写实现返回按钮监听
     */
    public void setOnBackBtnClickListener(OnClickListener listener) {
        mBtnRe.setOnClickListener(listener);
    }

}
