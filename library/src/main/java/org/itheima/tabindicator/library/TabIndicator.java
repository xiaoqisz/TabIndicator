package org.itheima.tabindicator.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TabIndicator
        extends HorizontalScrollView {

    // ######## Tab 的模式常量 ############
    public final static int TAB_MODE_LINE = 0;
    public final static int TAB_MODE_TRIANGLE = 1;
    public final static int TAB_MODE_RECT = 2;

    // ######## line 模式下的样式常量 ############
    public final static int LINE_STYLE_MATCH = 0;
    public final static int LINE_STYLE_WRAP = 1;

    // ######## rect 模式下的样式常量 ############
    public final static int RECT_STYLE_FILL = 0;
    public final static int RECT_STYLE_STROKE = 1;

    // ######## triangle 模式下的样式常量
    public final static int TRIANGLE_STYLE_FILL = 0;
    public final static int TRIANGLE_STYLE_STROKE = 1;

    // ################### TAB 通用的属性 ################################
    private float mTabPaddingLeft = 8;
    private float mTabPaddingRight = 8;
    private float mTabPaddingTop = 8;
    private float mTabPaddingBottom = 8;
    private int mTabBackground = 0;
    private ColorStateList mTabTextColor;
    private float mTabTextSize = 13;
    private boolean mTabTextBlod = false;
    private float mUnderLineHeight = 1;
    private int mUnderLineColor = 0xFF39ccd3;
    private int mTabMode = LINE_STYLE_WRAP;

    // ################## TAB line模式下的属性 ###########################
    private float mLineHeight = 3;
    private int mLineColor = 0xFF39ccd3;
    private int mLineStyle = LINE_STYLE_MATCH;

    // ################## TAB triangle模式下的属性 #######################
    private float mTriangleHeight = 8;
    private float mTriangleWidth = 20;
    private int mTriangleColor = Color.TRANSPARENT;
    private int mTriangleStyle = TRIANGLE_STYLE_FILL;
    private float mTriangleStrokeWidth = 2;

    // ################## TAB rect模式下的属性 ###########################
    private int mRectColor = Color.TRANSPARENT;
    private float mRectRadius = 2;
    private int mRectStyle = RECT_STYLE_FILL;
    private int mRectStrokeColor = Color.TRANSPARENT;
    private float mRectStrokeWidth = 1;

    //####################Tab drawable集合########################################

    List<StateListDrawable> drawableLeft, drawableTop, drawableRight, drawableBottom;
    private int drawablePadding = 0;

    //#########################################################################

    private LinearLayoutCompat mTabContainer;
    private ViewPager mViewPager;//当前tab对应的ViewPager
    private TabPageListener mTabPageListener;//页面的监听器
    private Paint mPaint;
    private Path mTrianglePath;
    private GradientDrawable mRectDrawable;

    private float mPagerOffset = 0f;
    private int mCurrentPosition = 0;
    private int mLastPosition = -1;

    private int mVisibleTabCount = -1;
    private int mTabCount;
    private boolean isWeight = false;
    private int mTabWidith = -2;

    /**
     * tab 标题
     */
    private List<CharSequence> mtitles;

    /**
     * tab 被点击监听器集合
     */
    private List<OnTabClickListener> mClickListeners = new LinkedList<>();

    /**
     * tab 重复点击的监听器集合
     */
    private List<OnTabRepeatClickListener> mRepeatClickListeners = new LinkedList<>();


    public TabIndicator(Context context) {
        this(context, null);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.indicator_style);

    }

    public TabIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        handleAttrs(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
        handleAttrs(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        //滑动条不可见
        setFillViewport(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setHorizontalFadingEdgeEnabled(false);
        setBackgroundColor(Color.TRANSPARENT);
        mPaint = new Paint();

        //创建tab容器
        mTabContainer = new LinearLayoutCompat(context, attrs);
        mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabContainer.setGravity(Gravity.CENTER);
        addView(mTabContainer);
    }

    private void handleAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabIndicator, defStyleAttr, 0);
        //通用的属性获取
        mTabPaddingLeft = ta.getDimension(R.styleable.TabIndicator_tiTabPaddingLeft, dpToPx(mTabPaddingLeft));
        mTabPaddingTop = ta.getDimension(R.styleable.TabIndicator_tiTabPaddingTop, dpToPx(mTabPaddingTop));
        mTabPaddingRight = ta.getDimension(R.styleable.TabIndicator_tiTabPaddingRight, dpToPx(mTabPaddingRight));
        mTabPaddingBottom = ta.getDimension(R.styleable.TabIndicator_tiTabPaddingBottom, dpToPx(mTabPaddingBottom));

        mTabBackground = ta.getResourceId(R.styleable.TabIndicator_tiTabBackground, mTabBackground);
        mTabTextColor = ta.getColorStateList(R.styleable.TabIndicator_tiTabTextColor);
        mTabTextSize = ta.getDimension(R.styleable.TabIndicator_tiTabTextSize, dpToPx(mTabTextSize));
        mTabTextBlod = ta.getBoolean(R.styleable.TabIndicator_tiTabTextBlod, mTabTextBlod);

        mUnderLineHeight = ta.getDimension(R.styleable.TabIndicator_tiUnderLineHeight, dpToPx(mUnderLineHeight));
        mUnderLineColor = ta.getColor(R.styleable.TabIndicator_tiUnderLineColor, mUnderLineColor);

        mTabMode = ta.getInt(R.styleable.TabIndicator_tiTabMode, mTabMode);
        mVisibleTabCount = ta.getInt(R.styleable.TabIndicator_tiVisibletabCount, mVisibleTabCount);
        if (mVisibleTabCount != -1) {
            isWeight = true;
        }

        mLineHeight = ta.getDimension(R.styleable.TabIndicator_tiLineHeight, dpToPx(mLineHeight));
        mLineColor = ta.getColor(R.styleable.TabIndicator_tiLineColor, mLineColor);
        mLineStyle = ta.getInt(R.styleable.TabIndicator_tiLineStyle, mLineStyle);

        mTriangleHeight = ta.getDimension(R.styleable.TabIndicator_tiTriangleHeight, dpToPx(mTriangleHeight));
        mTriangleWidth = ta.getDimension(R.styleable.TabIndicator_tiTriangleWidth, dpToPx(mTriangleWidth));
        mTriangleColor = ta.getColor(R.styleable.TabIndicator_tiTriangleColor, mTriangleColor);
        mTriangleStyle = ta.getInt(R.styleable.TabIndicator_tiTriangleStyle, mTriangleStyle);
        mTriangleStrokeWidth = ta.getDimension(R.styleable.TabIndicator_tiTriangleStrokeWidth, dpToPx(mTriangleStrokeWidth));

        mRectColor = ta.getColor(R.styleable.TabIndicator_tiRectColor, mRectColor);
        mRectRadius = ta.getDimension(R.styleable.TabIndicator_tiRectRadius, dpToPx(mRectRadius));
        mRectStyle = ta.getInt(R.styleable.TabIndicator_tiRectStyle, mRectStyle);
        mRectStrokeWidth = ta.getDimension(R.styleable.TabIndicator_tiRectStrokeWidth, dpToPx(mRectStrokeWidth));
        mRectStrokeColor = ta.getColor(R.styleable.TabIndicator_tiRectStrokeColor, mRectStrokeColor);

        drawablePadding = ta.getInt(R.styleable.TabIndicator_tiDrawablePaddding, drawablePadding);

        ta.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mTabMode) {
            case TAB_MODE_LINE:
                // 画line
                drawLine(canvas);
                break;
            case TAB_MODE_TRIANGLE:
                //画triangle
                drawTriangle(canvas);
                break;
            case TAB_MODE_RECT:
                //画rect
                drawRect(canvas);
                break;
            default:
                break;
        }

        //画underLine
        drawUnderLine(canvas);
    }


    /**
     * draw line
     *
     * @param canvas 画布
     */
    private void drawLine(Canvas canvas) {

        //计算当前的left和right
        float[] clr = getCurrentBounds();
        if (null == clr) return;
        float left = clr[0];
        float right = clr[2];
        if (left == right) return;

        //重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mLineColor);

        if (mLineStyle == LINE_STYLE_WRAP && !isWeight) {
            left += mTabPaddingLeft;
            right -= mTabPaddingRight;
        }

        float top = getMeasuredHeight() - mUnderLineHeight - mLineHeight;
        float bottom = getMeasuredHeight() - mUnderLineHeight;

        Log.e("indicator", "width :" + (right - left));

        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * draw triangle
     *
     * @param canvas 画布
     */
    private void drawTriangle(Canvas canvas) {

        //计算当前的left和right
        float[] clr = getCurrentBounds();
        if (null == clr) return;
        float left = clr[0];
        float right = clr[2];
        if (left == right) return;

        //重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTriangleColor);

        float x1 = (left + right) / 2f;
        float y1 = getMeasuredHeight() - mUnderLineHeight - mTriangleHeight;
        float x2 = x1 - mTriangleWidth / 2f;
        float y2 = getMeasuredHeight() - mUnderLineHeight;
        float x3 = x1 + mTriangleWidth / 2f;
        float y3 = getMeasuredHeight() - mUnderLineHeight;

        if (mTriangleStyle == TRIANGLE_STYLE_FILL) {
            drawFillTriangle(x1, y1, x2, y2, x3, y3, canvas);
        } else {
            drawStrokeTriangle(x1, y1, x2, y2, x3, y3, canvas);
        }
    }

    /**
     * draw stroke style triangle
     *
     * @param canvas 画布
     */
    private void drawStrokeTriangle(float x1,
                                    float y1,
                                    float x2,
                                    float y2,
                                    float x3,
                                    float y3,
                                    Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mTriangleStrokeWidth);

        float x0 = 0;
        float y0 = getMeasuredHeight() - mUnderLineHeight;
        float x4 = mTabContainer.getMeasuredWidth();
        float y4 = getMeasuredHeight() - mUnderLineHeight;

        canvas.drawLine(x0, y0, x2, y2, mPaint);
        canvas.drawLine(x2, y2, x1, y1, mPaint);
        canvas.drawLine(x1, y1, x3, y3, mPaint);
        canvas.drawLine(x3, y3, x4, y4, mPaint);
    }

    /**
     * draw fill style triangle
     *
     * @param canvas 画布
     */
    private void drawFillTriangle(float x1,
                                  float y1,
                                  float x2,
                                  float y2,
                                  float x3,
                                  float y3,
                                  Canvas canvas) {
        if (mTrianglePath == null) {
            mTrianglePath = new Path();
        }

        mTrianglePath.reset();
        mTrianglePath.moveTo(x1, y1);
        mTrianglePath.lineTo(x2, y2);
        mTrianglePath.lineTo(x3, y3);
        mTrianglePath.lineTo(x1, y1);
        mTrianglePath.close();

        canvas.drawPath(mTrianglePath, mPaint);
    }

    /**
     * draw rect
     *
     * @param canvas 画布
     */
    private void drawRect(Canvas canvas) {
        //计算当前的left和right
        float[] clr = getCurrentBounds();
        if (null == clr) return;
        float left = clr[0];
        float top = clr[1];
        float right = clr[2];
        float bottom = clr[3];

        bottom -= mUnderLineHeight;

        if (!isWeight) {
            top += mTabPaddingTop;
            bottom -= mTabPaddingBottom;
            left += mTabPaddingLeft;
            right -= mTabPaddingRight;
        }

        top -= mRectRadius / 2;
        left -= mRectRadius / 2;
        right += mRectRadius / 2;
        bottom += mRectRadius / 2;

        if (mRectDrawable == null) {
            mRectDrawable = new GradientDrawable();
        }

        mRectDrawable.setShape(GradientDrawable.RECTANGLE);
        mRectDrawable.setColor(mRectColor);
        mRectDrawable.setCornerRadius(mRectRadius);

        if (mRectStyle == RECT_STYLE_STROKE) {
            mRectDrawable.setStroke((int) mRectStrokeWidth, mRectStrokeColor);
        } else {
            mRectDrawable.setStroke((int) mRectStrokeWidth, Color.TRANSPARENT);
        }
        mRectDrawable.setBounds((int) left, (int) top, (int) right, (int) bottom);

        mRectDrawable.draw(canvas);
    }

    /**
     * 计算当前滑动的left和right
     *
     * @return 获得动态的left和right，结果为float[],0为left，1为right
     */
    private float[] getCurrentBounds() {
        //获得当前的tab的left和right
        TextView currentTab = (TextView) mTabContainer.getChildAt(mCurrentPosition);
        if (currentTab == null) return null;

        float left = currentTab.getLeft();
        float top = currentTab.getTop();
        float right = currentTab.getRight();
        float bottom = currentTab.getBottom();

        if (isWeight) {
            int tabWidth = currentTab.getMeasuredWidth();

            int textWidth = getTextWH(currentTab, currentTab.getText().toString())[0];
            int textWidthMargin = (tabWidth - textWidth) / 2;

            left += textWidthMargin;
            right -= textWidthMargin;
        }

        if (mPagerOffset > 0f && mCurrentPosition < mTabCount - 1) {
            //获得下一个的tab的left和right
            TextView nextTab = (TextView) mTabContainer.getChildAt(mCurrentPosition + 1);

            float nextLeft = nextTab.getLeft();
            float nextRight = nextTab.getRight();

            if (isWeight) {
                int tabWidth = nextTab.getMeasuredWidth();
                int textWidth = getTextWH(nextTab, nextTab.getText().toString())[0];
                int textWidthMargin = (tabWidth - textWidth) / 2;

                nextLeft += textWidthMargin;
                nextRight -= textWidthMargin;
            }

            //计算偏移后的left和right
            left = nextLeft * mPagerOffset + (1 - mPagerOffset) * left;
            right = nextRight * mPagerOffset + (1 - mPagerOffset) * right;

        }

        return new float[]{left, top, right, bottom};
    }

    /**
     * draw underline
     *
     * @param canvas 画布
     */
    private void drawUnderLine(Canvas canvas) {
        if (mTabMode == TAB_MODE_TRIANGLE && mTriangleStyle == TRIANGLE_STYLE_STROKE) {
            return;
        }

        //重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mUnderLineColor);

        float left = 0;
        float top = getMeasuredHeight() - mUnderLineHeight;
        float right = mTabContainer.getMeasuredWidth();
        float bottom = getMeasuredHeight();
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * 设置ViewPager
     *
     * @param pager viewpager
     */
    public void setViewPager(ViewPager pager) {
        //保存实例
        this.mViewPager = pager;

        //设置监听
        if (mTabPageListener == null) {
            mTabPageListener = new TabPageListener();
        }
        this.mViewPager.addOnPageChangeListener(mTabPageListener);

        PagerAdapter mAdapter = mViewPager.getAdapter();
        if (null == mAdapter) {
            throw new IllegalStateException("null adapter");
        }
        mTabCount = mAdapter.getCount();

        List<CharSequence> titles = new ArrayList<>(mTabCount);
        for (int i = 0; i < mTabCount; i++) {
            CharSequence title = mAdapter.getPageTitle(i);
            if (!TextUtils.isEmpty(title)) {
                titles.add(title);
            }
        }
        if (titles.size() > 0) setTitles(titles);
        else updateTabs();
    }

    public void setTitles(List<CharSequence> titles) {
        if (null == mtitles) {
            mtitles = new ArrayList<>();
        } else {
            mtitles.clear();
        }
        mtitles.addAll(titles);
        mTabCount = mtitles.size();
        updateTabs();
    }


    /**
     * add listener
     *
     * @param listener
     */
    public void addOnTabClickListener(OnTabClickListener listener) {
        if (!mClickListeners.contains(listener)) {
            mClickListeners.add(listener);
        }
    }

    /**
     * add listener
     *
     * @param listener
     */
    public void addOnTabRepeatClickListener(OnTabRepeatClickListener listener) {
        if (!mRepeatClickListeners.contains(listener)) {
            mRepeatClickListeners.add(listener);
        }
    }

    /**
     * remove listener
     *
     * @param listener
     */
    public void removeTabClickListener(OnTabClickListener listener) {
        mClickListeners.remove(listener);
    }

    /**
     * remove listener
     *
     * @param listener
     */
    public void removeTabRepeatClickListener(OnTabRepeatClickListener listener) {
        mRepeatClickListeners.remove(listener);
    }

    /**
     * update tabs
     */
    private void updateTabs() {
        //清空
        mTabContainer.removeAllViews();

        for (int i = 0; i < mTabCount; i++) {
            CharSequence title = null;
            if (null != mtitles && mtitles.size() > 0) {
                title = mtitles.get(i);
            }

            addTab(title, i);
        }

    }

    /**
     * 添加tab
     *
     * @param title tab显示的title
     * @param index tab的index
     */
    private void addTab(CharSequence title, final int index) {
        TextView tab = new TextView(getContext());
        tab.setGravity(Gravity.CENTER);

        if (null != title) {
            tab.setText(title);
            tab.setTextColor(mTabTextColor != null ? mTabTextColor : ColorStateList.valueOf(0xFF000000));
            tab.setTextSize(px2dip(mTabTextSize));
            tab.getPaint()
                    .setFakeBoldText(mTabTextBlod);
        }

        if (!isWeight) {
            tab.setPadding((int) mTabPaddingLeft,
                    (int) mTabPaddingTop,
                    (int) mTabPaddingRight,
                    (int) mTabPaddingBottom);
        } else {
            tab.setPadding(0,
                    (int) mTabPaddingTop,
                    0,
                    (int) mTabPaddingBottom);
        }


        if (drawableLeft != null && drawableLeft.size() > index) {
            StateListDrawable drawable = drawableLeft.get(index);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tab.setCompoundDrawables(drawable, null, null, null);
            tab.setCompoundDrawablePadding(drawablePadding);
        }

        if (drawableTop != null && drawableTop.size() > index) {
            StateListDrawable drawable = drawableTop.get(index);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tab.setCompoundDrawables(null, drawable, null, null);
            tab.setCompoundDrawablePadding(drawablePadding);
        }

        if (drawableRight != null && drawableRight.size() > index) {
            StateListDrawable drawable = drawableRight.get(index);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tab.setCompoundDrawables(null, null, drawable, null);
            tab.setCompoundDrawablePadding(drawablePadding);
        }

        if (drawableBottom != null && drawableBottom.size() > index) {
            StateListDrawable drawable = drawableBottom.get(index);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tab.setCompoundDrawables(null, null, null, drawable);
            tab.setCompoundDrawablePadding(drawablePadding);
        }


        tab.setBackgroundResource(mTabBackground);
        tab.setSelected(index == 0);//设置默认
        tab.setOnClickListener(new TabClickListener(index));

        mTabContainer.addView(tab, index, getTabLayoutParam());
    }

    private LinearLayoutCompat.LayoutParams getTabLayoutParam() {
        if (isWeight) {
            mTabWidith = getScreenWidth(getContext()) / mVisibleTabCount;
        } else {
            mTabWidith = -2;
        }

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(mTabWidith, -2);
        params.bottomMargin = (int) (mUnderLineHeight + 0.5f);
        return params;
    }

    private void scrollTabs() {
        View view = mTabContainer.getChildAt(mCurrentPosition);
        int endX = (int) (view.getMeasuredWidth() * mPagerOffset + view.getLeft() + 0.5f);
        scrollTo(endX, 0);
        invalidate();
    }

    /**
     * @return Tab padding left
     */
    public float getTabPaddingLeft() {
        return mTabPaddingLeft;
    }

    /**
     * @return Tab padding top
     */
    public float getTabPaddingTop() {
        return mTabPaddingTop;
    }


    /**
     * @return Tab padding right
     */
    public float getTabPaddingRight() {
        return mTabPaddingRight;
    }


    /**
     * @return Tab padding Bottom
     */
    public float getTabPaddingBottom() {
        return mTabPaddingBottom;
    }


    /**
     * set tab padding
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setTabPadding(int left, int top, int right, int bottom) {
        this.mTabPaddingLeft = left;
        this.mTabPaddingTop = top;
        this.mTabPaddingRight = right;
        this.mTabPaddingBottom = bottom;
        invalidate();
    }

    /**
     * set tab background selector
     *
     * @param resId
     */
    public void setTabBackground(int resId) {
        this.mTabBackground = resId;
        invalidate();
    }

    /**
     * set tab text color
     *
     * @param color
     */
    public void setTabTextColor(@ColorInt int color) {
        this.mTabTextColor = ColorStateList.valueOf(color);
        invalidate();
    }

    /**
     * set tab text color
     *
     * @param colors
     */
    public void setTabTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }
        this.mTabTextColor = colors;
        invalidate();
    }

    /**
     * @return tab text size
     */
    public float getTabTextSize() {
        return mTabTextSize;
    }

    /**
     * set tab text size
     *
     * @param size
     */
    public void setTabTextSize(float size) {
        this.mTabTextSize = size;
        invalidate();
    }

    /**
     * @return is tab text blod
     */
    public boolean isTabTextBlod() {
        return mTabTextBlod;
    }

    /**
     * set tab text blod
     *
     * @param blod
     */
    public void setTabTextBlod(boolean blod) {
        this.mTabTextBlod = blod;
        invalidate();
    }

    /**
     * @return get under line height
     */
    public float getUnderLineHeight() {
        return mUnderLineHeight;
    }

    /**
     * set under line height
     *
     * @param underLineHeight
     */
    public void setUnderLineHeight(float underLineHeight) {
        this.mUnderLineHeight = underLineHeight;
        invalidate();
    }

    /**
     * @return get under line color
     */
    public int getUnderLineColor() {
        return mUnderLineColor;
    }

    /**
     * set under line color
     *
     * @param underLineColor
     */
    public void setUnderLineColor(int underLineColor) {
        this.mUnderLineColor = underLineColor;
        invalidate();
    }

    /**
     * @return get tab mode
     */
    public int getTabMode() {
        return mTabMode;
    }

    /**
     * set tab mode
     *
     * @param tabMode
     */
    public void setTabMode(int tabMode) {
        if (tabMode != TAB_MODE_LINE && tabMode != TAB_MODE_RECT && tabMode != TAB_MODE_TRIANGLE) {
            tabMode = TAB_MODE_LINE;
        }
        this.mTabMode = tabMode;
        invalidate();
    }

    /**
     * @return get line height
     */
    public float getLineHeight() {
        return mLineHeight;
    }

    /**
     * set line height
     *
     * @param lineHeight
     */
    public void setLineHeight(float lineHeight) {
        this.mLineHeight = lineHeight;
        invalidate();
    }

    /**
     * @return line color
     */
    public int getLineColor() {
        return mLineColor;
    }

    /**
     * set line color
     *
     * @param lineColor
     */
    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
        invalidate();
    }

    /**
     * @return line style
     * @see {@link TabIndicator#LINE_STYLE_MATCH}
     * @see {@link TabIndicator#LINE_STYLE_WRAP}
     */
    public int getLineStyle() {
        return mLineStyle;
    }

    /**
     * set line style
     *
     * @param lineStyle
     * @see {@link TabIndicator#LINE_STYLE_MATCH}
     * @see {@link TabIndicator#LINE_STYLE_WRAP}
     */
    public void setLineStyle(int lineStyle) {
        if (lineStyle == LINE_STYLE_MATCH) {
            this.mLineStyle = lineStyle;
        } else {
            this.mLineStyle = LINE_STYLE_WRAP;
        }
        invalidate();
    }

    /**
     * @return get triangle height
     */
    public float getTriangleHeight() {
        return mTriangleHeight;
    }

    /**
     * set triangle height
     *
     * @param triangleHeight
     */
    public void setTriangleHeight(float triangleHeight) {
        this.mTriangleHeight = triangleHeight;
        invalidate();
    }

    /**
     * @return get triangle width
     */
    public float getTriangleWidth() {
        return mTriangleWidth;
    }

    /**
     * set triangle width
     *
     * @param triangleWidth
     */
    public void setTriangleWidth(float triangleWidth) {
        this.mTriangleWidth = triangleWidth;
        invalidate();
    }

    /**
     * @return get triangle color
     */
    public int getTriangleColor() {
        return mTriangleColor;
    }

    /**
     * set triangle color
     *
     * @param triangleColor
     */
    public void setTriangleColor(int triangleColor) {
        this.mTriangleColor = triangleColor;
        invalidate();
    }

    /**
     * @return
     * @see {@link TabIndicator#TRIANGLE_STYLE_FILL}
     * @see {@link TabIndicator#TRIANGLE_STYLE_STROKE}
     */
    public int getTriangleStyle() {
        return mTriangleStyle;
    }

    /**
     * set triangle style
     *
     * @param triangleStyle
     */
    public void setTriangleStyle(int triangleStyle) {
        if (triangleStyle == TRIANGLE_STYLE_FILL) {
            this.mTriangleStyle = triangleStyle;
        } else {
            this.mTriangleStyle = TRIANGLE_STYLE_STROKE;
        }
        invalidate();
    }

    public int getDrawablePadding() {
        return drawablePadding;
    }

    public void setDrawablePadding(int drawablePadding) {
        this.drawablePadding = drawablePadding;
        invalidate();
    }

    public int getmVisibleTabCount() {
        return mVisibleTabCount;
    }

    public void setmVisibleTabCount(int mVisibleTabCount) {
        this.mVisibleTabCount = mVisibleTabCount;
        invalidate();
    }

    /**
     * @return rect color
     */
    public int getRectColor() {
        return mRectColor;
    }

    /**
     * set rect color
     *
     * @param color
     */
    public void setRectColor(int color) {
        this.mRectColor = color;
        invalidate();
    }

    /**
     * @return rect radius
     */
    public float getRectRadius() {
        return mRectRadius;
    }

    /**
     * set rect radius
     *
     * @param radius
     */
    public void setRectRadius(float radius) {
        this.mRectRadius = radius;
        invalidate();
    }

    /**
     * @return rect style
     */
    public int getRectStyle() {
        return this.mRectStyle;
    }

    /**
     * set rect style
     *
     * @param style
     */
    public void setRectStyle(int style) {
        if (style == RECT_STYLE_FILL) {
            this.mRectStyle = style;
        } else {
            this.mRectStyle = RECT_STYLE_STROKE;
        }
        invalidate();
    }

    /**
     * @return rect stroke color
     */
    public int getRectStrokeColor() {
        return mRectStrokeColor;
    }

    /**
     * set rect stroke color
     *
     * @param color
     */
    public void setRectStrokeColor(int color) {
        this.mRectStrokeColor = color;
        invalidate();
    }

    /**
     * @return rect stroke width
     */
    public float getRectStrokeWidth() {
        return mRectStrokeWidth;
    }

    /**
     * set rect stroke width
     *
     * @param width
     */
    public void setRectStrokeWidth(float width) {
        this.mRectStrokeWidth = width;
        invalidate();
    }

    /**
     * @return triangle stroke width
     */
    public float getTriangleStrokeWidth() {
        return mTriangleStrokeWidth;
    }

    /**
     * set triangle stroke width
     *
     * @param triangleStrokeWidth
     */
    public void setTriangleStrokeWidth(float triangleStrokeWidth) {
        this.mTriangleStrokeWidth = triangleStrokeWidth;
    }


    public void addLeftTabIcon(int idNormal, int idSelected) {
        StateListDrawable sld = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : getContext().getResources().getDrawable(idNormal);
        Drawable select = idSelected == -1 ? null : getContext().getResources().getDrawable(idSelected);
        sld.addState(new int[]{android.R.attr.state_selected}, select);
        sld.addState(new int[]{}, normal);
        if (drawableLeft == null) {
            drawableLeft = new ArrayList<>();
        }
        drawableLeft.add(sld);
    }

    public void addTopTabIcon(int idNormal, int idSelected) {
        StateListDrawable sld = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : getContext().getResources().getDrawable(idNormal);
        Drawable select = idSelected == -1 ? null : getContext().getResources().getDrawable(idSelected);
        sld.addState(new int[]{android.R.attr.state_selected}, select);
        sld.addState(new int[]{}, normal);
        if (drawableTop == null) {
            drawableTop = new ArrayList<>();
        }
        drawableTop.add(sld);
    }

    public void addRightTabIcon(int idNormal, int idSelected) {
        StateListDrawable sld = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : getContext().getResources().getDrawable(idNormal);
        Drawable select = idSelected == -1 ? null : getContext().getResources().getDrawable(idSelected);
        sld.addState(new int[]{android.R.attr.state_selected}, select);
        sld.addState(new int[]{}, normal);
        if (drawableRight == null) {
            drawableRight = new ArrayList<>();
        }
        drawableRight.add(sld);
    }

    public void addBottomTabIcon(int idNormal, int idSelected) {
        StateListDrawable sld = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : getContext().getResources().getDrawable(idNormal);
        Drawable select = idSelected == -1 ? null : getContext().getResources().getDrawable(idSelected);
        sld.addState(new int[]{android.R.attr.state_selected}, select);
        sld.addState(new int[]{}, normal);
        if (drawableBottom == null) {
            drawableBottom = new ArrayList<>();
        }
        drawableBottom.add(sld);
    }

    /**
     * Page 改变的监听器
     */
    private class TabPageListener
            extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //存储position,offset
            mCurrentPosition = position;
            mPagerOffset = positionOffset;

            //滚动
            scrollTabs();

        }

        @Override
        public void onPageSelected(int position) {
            switchPager(position);
        }
    }


    public interface OnTabClickListener {
        void onTabClick(View view, int index);
    }

    public interface OnTabRepeatClickListener {
        void onTabClick(View view, int index);
    }

    private class TabClickListener implements OnClickListener {

        private final int index;

        public TabClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {

            if (index == mLastPosition) {
                notifyOnRepeatTabClick(v, index);

            } else {
                notifyOnTabClick(v, index);
            }
        }
    }

    private void notifyOnTabClick(View v, int index) {

        mCurrentPosition = index;

        for (OnTabClickListener next : mClickListeners) {
            if (next != null) {
                next.onTabClick(v, index);
            }
        }

        if (null != mViewPager) {
            mViewPager.setCurrentItem(index);
        } else {
            switchPager(index);
            scrollTabs();
        }

    }

    private void switchPager(int index) {
        int count = mTabContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mTabContainer.getChildAt(i);
            view.setSelected(i == index);
        }

        mLastPosition = index;
    }

    private void notifyOnRepeatTabClick(View v, int index) {

        for (OnTabRepeatClickListener next : mRepeatClickListeners) {
            if (next != null) {
                next.onTabClick(v, index);
            }
        }
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private float dpToPx(float dps) {
        return Math.round(getContext().getResources().getDisplayMetrics().density * dps);
    }

    public float px2dip(float pxValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }

    public int[] getTextWH(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return new int[]{bounds.width(), bounds.height()};
    }
}

