package com.snxun.book.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.snxun.book.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内容栏
 *
 * @author wangshy
 * @date 2020/08/11
 */
public class ContentLayout extends RelativeLayout {

    /**
     * 图片
     */
    @BindView(R.id.btn)
    TextView mBtn;
    /**
     * 文字
     */
    @BindView(R.id.text)
    TextView mText;

    public ContentLayout(Context context) {
        super(context);
        init(null);
    }

    public ContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        findViews();
        config(attrs);
    }

    private void findViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_content_layout, this);
        ButterKnife.bind(this);
    }

    private void config(AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ContentLayout);
        }

        Drawable iconDrawable = typedArray == null ? null : typedArray.getDrawable(R.styleable.ContentLayout_img);
        if (iconDrawable != null) {
            setDrawable(iconDrawable);
        }

        String text = typedArray == null ? "" : typedArray.getString(R.styleable.ContentLayout_text);
        if (!TextUtils.isEmpty(text)) {
            setTitleName(text);
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
        mText.setText(title);
    }

    /**
     * 设置img
     *
     * @param drawable drawable
     */
    public void setDrawable(Drawable drawable) {
        mBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        //mBtn.setImageDrawable(drawable);
    }

    /**
     * 更改img
     *
     * @param iconRes iconRes
     */
    public void setIcon(int iconRes) {
        mBtn.setBackgroundResource(iconRes);
    }

}
