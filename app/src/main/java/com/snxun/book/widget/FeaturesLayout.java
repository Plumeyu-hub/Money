package com.snxun.book.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.snxun.book.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangshy
 * @date 2020/08/10
 */
public class FeaturesLayout extends RelativeLayout {

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
     * next按钮
     */
    @BindView(R.id.next_img)
    TextView mNextImg;
    /**
     * 下划线
     */
    @BindView(R.id.line_view)
    View mLineView;


    public FeaturesLayout(Context context) {
        super(context);
        init(null);
    }

    public FeaturesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FeaturesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FeaturesLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        findViews();
        config(attrs);
    }

    private void findViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_features_layout, this);
        ButterKnife.bind(this);
    }

    private void config(AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FeaturesLayout);
        }

        Drawable iconDrawable = typedArray == null ? null : typedArray.getDrawable(R.styleable.FeaturesLayout_iconImg);
        if (iconDrawable != null) {
            setTitleName(iconDrawable);
        }

        String text = typedArray == null ? "" : typedArray.getString(R.styleable.FeaturesLayout_textTv);
        if (!TextUtils.isEmpty(text)) {
            setTitleName(text);
        }

        Drawable nextDrawable = typedArray == null ? null : typedArray.getDrawable(R.styleable.FeaturesLayout_nextImg);
        if (nextDrawable != null) {
            mNextImg.setCompoundDrawablesWithIntrinsicBounds(nextDrawable, null, null, null);
            //mNextImg.setImageDrawable(nextDrawable);
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
    public void setTitleName(Drawable drawable) {
        mIconImg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    /**
     * 请重写实现返回按钮监听
     */
    public void setOnBackBtnClickListener(OnClickListener listener) {
        mBtnRe.setOnClickListener(listener);
    }

}
