package com.snxun.book.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.lodz.android.core.utils.DensityUtils;
import com.snxun.book.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 标题栏
 * @author zhouL
 * @date 2020/8/3
 */
public class TitleLayout extends LinearLayout {

    /** 默认返回按钮显隐 */
    private static final boolean DEFAULT_NEED_BACK_BUTTON = true;
    /** 默认分割线显隐 */
    private static final boolean DEFAULT_SHOW_DIVIDE_LINE = false;
    /** 默认阴影显隐 */
    private static final boolean DEFAULT_NEED_ELEVATION = true;
    /** 默认阴影值 */
    private static final int DEFAULT_ELEVATION_VALE = 12;


    /** 返回按钮布局 */
    @BindView(R.id.back_layout)
    ViewGroup mBackLayout;
    /** 返回按钮 */
    @BindView(R.id.back_btn)
    TextView mBackBtn;
    /** 标题文字 */
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    /** 扩展区域 */
    @BindView(R.id.expand_layout)
    ViewGroup mExpandLayout;
    /** 分割线 */
    @BindView(R.id.divide_line_view)
    View mDivideLineView;

    public TitleLayout(Context context) {
        super(context);
        init(null);
    }

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @SuppressLint("NewApi")
    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        findViews();
        config(attrs);
    }

    private void findViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_title_layout, this);
        ButterKnife.bind(this);
    }

    private void config(AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null){
            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleLayout);
        }

        // 默认显示返回按钮
        needBackButton(typedArray == null ? DEFAULT_NEED_BACK_BUTTON
                : typedArray.getBoolean(R.styleable.TitleLayout_isNeedBackBtn, DEFAULT_NEED_BACK_BUTTON));

        Drawable backDrawable = typedArray == null ? null : typedArray.getDrawable(R.styleable.TitleLayout_backDrawable);
        if (backDrawable != null){
            mBackBtn.setCompoundDrawablesWithIntrinsicBounds(backDrawable, null, null, null);
        }

        String backText = typedArray == null ? "" : typedArray.getString(R.styleable.TitleLayout_backText);
        if (!TextUtils.isEmpty(backText)) {
            setBackBtnName(backText);
        }

        ColorStateList backTextColor = typedArray == null ? null : typedArray.getColorStateList(R.styleable.TitleLayout_backTextColor);
        if (backTextColor != null) {
            setBackBtnTextColor(backTextColor);
        }

        int backTextSize = typedArray == null ? 0 : typedArray.getDimensionPixelSize(R.styleable.TitleLayout_backTextSize, 0);
        if (backTextSize != 0){
            setBackBtnTextSize(DensityUtils.px2sp(getContext(), backTextSize));
        }

        String titleText = typedArray == null ? "" : typedArray.getString(R.styleable.TitleLayout_titleText);
        if (!TextUtils.isEmpty(titleText)) {
            setTitleName(titleText);
        }

        ColorStateList titleTextColor = typedArray == null ? null : typedArray.getColorStateList(R.styleable.TitleLayout_titleTextColor);
        if (titleTextColor != null) {
            setTitleTextColor(titleTextColor);
        }

        int titleTextSize = typedArray == null ? 0 : typedArray.getDimensionPixelSize(R.styleable.TitleLayout_titleTextSize, 0);
        if (titleTextSize != 0){
            setTitleTextSize(DensityUtils.px2sp(getContext(), titleTextSize));
        }

        boolean isShowDivideLine = typedArray == null ? DEFAULT_SHOW_DIVIDE_LINE
                : typedArray.getBoolean(R.styleable.TitleLayout_isShowDivideLine, DEFAULT_SHOW_DIVIDE_LINE);
        mDivideLineView.setVisibility(isShowDivideLine ? View.VISIBLE : View.GONE);

        Drawable divideLineDrawable = typedArray == null ? null : typedArray.getDrawable(R.styleable.TitleLayout_divideLineColor);
        if (divideLineDrawable != null){
            setDivideLineColor(divideLineDrawable);
        }

        int divideLineHeight = typedArray == null ? 0 : typedArray.getDimensionPixelSize(R.styleable.TitleLayout_divideLineHeight, 0);
        if (divideLineHeight > 0){
            setDivideLineHeight(DensityUtils.px2dp(getContext(), divideLineHeight));
        }

        Drawable drawableBackground = typedArray == null ? null : typedArray.getDrawable(R.styleable.TitleLayout_titleBarBackground);
        if (drawableBackground != null){
            setBackground(drawableBackground);
        } else {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_FF6B6A));
        }

        boolean isNeedElevation = typedArray == null ? DEFAULT_NEED_ELEVATION
                : typedArray.getBoolean(R.styleable.TitleLayout_isNeedElevation, DEFAULT_NEED_ELEVATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isNeedElevation) {
            int elevationVale = typedArray == null ? 0 : typedArray.getDimensionPixelSize(R.styleable.TitleLayout_elevationVale, 0);
            setElevation(elevationVale != 0 ? elevationVale : DEFAULT_ELEVATION_VALE);
        }

        // 默认不需要右侧扩展区域
        needExpandView(typedArray != null && typedArray.getBoolean(R.styleable.TitleLayout_isNeedExpandView, false));
        int expandViewId = typedArray == null ? 0 : typedArray.getResourceId(R.styleable.TitleLayout_expandViewId, 0);
        if (expandViewId > 0){
            View view = LayoutInflater.from(getContext()).inflate(expandViewId, null);
            if (view != null){
                addExpandView(view);
            }
        }

        if (typedArray != null){
            typedArray.recycle();
        }
    }

    /**
     * 需要显示返回按钮
     * @param isNeed 是否需要
     */
    public void needBackButton(boolean isNeed){
        mBackLayout.setVisibility(isNeed ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置返回按钮的透明度
     * @param alpha 透明度
     */
    public void setBackButtonAlpha(@FloatRange(from=0.0, to=1.0) float alpha){
        mBackLayout.setAlpha(alpha);
    }

    /** 请重写实现返回按钮监听 */
    public void setOnBackBtnClickListener(OnClickListener listener) {
        mBackLayout.setOnClickListener(listener);
    }

    /**
     * 替换默认的返回按钮
     * @param view 返回按钮的View
     */
    public void replaceBackBtn(View view){
        mBackLayout.removeAllViews();
        mBackLayout.addView(view);
    }

    /**
     * 设置返回按钮文字
     * @param str 文字描述
     */
    public void setBackBtnName(String str){
        mBackBtn.setText(str);
    }

    /**
     * 设置返回按钮文字
     * @param strResId 文字资源id
     */
    public void setBackBtnName(@StringRes int strResId){
        mBackBtn.setText(getContext().getString(strResId));
    }

    /**
     * 设置返回按钮文字颜色
     * @param colorRes 颜色资源id
     */
    public void setBackBtnTextColor(@ColorRes int colorRes){
        mBackBtn.setTextColor(ContextCompat.getColor(getContext(), colorRes));
    }

    /**
     * 设置返回按钮文字颜色
     * @param color 颜色
     */
    public void setBackBtnTextColorInt(@ColorInt int color){
        mBackBtn.setTextColor(color);
    }

    /**
     * 设置返回按钮文字颜色
     * @param colorStateList 颜色
     */
    public void setBackBtnTextColor(ColorStateList colorStateList){
        if (colorStateList == null){
            return;
        }
        mBackBtn.setTextColor(colorStateList);
    }

    /**
     * 设置返回按钮文字大小
     * @param size 文字大小（单位sp）
     */
    public void setBackBtnTextSize(float size){
        mBackBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置标题名
     * @param title 标题名
     */
    public void setTitleName(String title){
        mTitleTv.setText(title);
    }

    /**
     * 设置标题名
     * @param strResId 文字资源id
     */
    public void setTitleName(@StringRes int strResId){
        mTitleTv.setText(getContext().getString(strResId));
    }

    /**
     * 设置标题文字颜色
     * @param colorRes 颜色资源id
     */
    public void setTitleTextColor(@ColorRes int colorRes){
        mTitleTv.setTextColor(ContextCompat.getColor(getContext(), colorRes));
    }

    /**
     * 设置标题文字颜色
     * @param color 颜色
     */
    public void setTitleTextColorInt(@ColorInt int color){
        mTitleTv.setTextColor(color);
    }

    /**
     * 设置文字颜色
     * @param colorStateList 颜色
     */
    public void setTitleTextColor(ColorStateList colorStateList){
        if (colorStateList == null){
            return;
        }
        mTitleTv.setTextColor(colorStateList);
    }

    /**
     * 设置标题文字大小
     * @param size 文字大小（单位sp）
     */
    public void setTitleTextSize(float size){
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 设置标题的透明度
     * @param alpha 透明度
     */
    public void setTitleAlpha(@FloatRange(from=0.0, to=1.0) float alpha){
        mTitleTv.setAlpha(alpha);
    }

    /**
     * 需要右侧扩展区
     * @param isNeed 是否需要
     */
    public void needExpandView(boolean isNeed){
        mExpandLayout.setVisibility(isNeed ? View.VISIBLE : View.GONE);
    }

    /**
     * 添加扩展区域的View
     * @param view 控件
     */
    public void addExpandView(View view){
        mExpandLayout.addView(view);
        needExpandView(true);
    }

    /** 获取扩展区域的View */
    public View getExpandView(){
        return mExpandLayout;
    }

    /** 隐藏分割线 */
    public void goneDivideLine(){
        mDivideLineView.setVisibility(View.GONE);
    }

    /** 设置分割线颜色 */
    public void setDivideLineColor(@ColorRes int colorRes){
        mDivideLineView.setBackgroundColor(ContextCompat.getColor(getContext(), colorRes));
    }

    /** 设置分割线颜色 */
    public void setDivideLineColorInt(@ColorInt int color){
        mDivideLineView.setBackgroundColor(color);
    }

    /** 设置分割线颜色 */
    public void setDivideLineColor(Drawable drawable){
        mDivideLineView.setBackground(drawable);
    }

    /**
     * 设置分割线高度
     * @param height 高度（单位dp）
     */
    public void setDivideLineHeight(float height){
        ViewGroup.LayoutParams layoutParams = mDivideLineView.getLayoutParams();
        layoutParams.height = DensityUtils.dp2px(getContext(), height);
        mDivideLineView.setLayoutParams(layoutParams);
    }

}
