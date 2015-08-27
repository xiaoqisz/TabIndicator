package org.itheima.tabindicator.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  @项目名：  TabIndicator 
 *  @包名：    org.itheima.tabindicator.library
 *  @文件名:   TabIndicator
 *  @创建者:   Administrator
 *  @描述：    TODO
 */
public class TabIndicator
        extends HorizontalScrollView
{
    public final static String TAG = "TabIndicator";

    // ######## Tab 的模式常量
    public final static int TAB_MODE_LINE     = 0;
    public final static int TAB_MODE_TRIANGLE = 1;
    public final static int TAB_MODE_RECT     = 2;

    // ######## line 模式下的样式常量
    public final static int LINE_STYLE_MATCH = 0;
    public final static int LINE_STYLE_WRAP  = 1;

    // ######## rect 模式下的样式常量
    public final static int RECT_STYLE_FILL   = 0;
    public final static int RECT_STYLE_STROKE = 1;

    // ######## triangle 模式下的样式常量
    public final static int TRIANGLE_STYLE_FILL   = 0;
    public final static int TRIANGLE_STYLE_STROKE = 1;

    private LinearLayout    mTabContainer    = null;
    private ViewPager       mViewPager       = null;//当前tab对应的ViewPager
    private TabPageListener mTabPageListener = null;//页面的监听器

    // ################### TAB 通用的属性 ################################
    private float   mTabPaddingLeft   = 15;
    private float   mTabPaddingRight  = 15;
    private float   mTabPaddingTop    = 12;
    private float   mTabPaddingBottom = 12;
    private int     mTabBackground    = R.drawable.tab_bg_selector;
    private int     mTabTextColor     = R.color.tab_textcolor_selector;
    private float   mTabTextSize      = 18;
    private boolean mTabTextBlod      = false;
    private float   mUnderLineHeight  = 2;
    private int     mUnderLineColor   = Color.parseColor("#33000000");
    private int     mTabMode          = TAB_MODE_TRIANGLE;

    // ################## TAB line模式下的属性 ###########################
    private float mLineHeight = 5;
    private int   mLineColor  = Color.BLUE;
    private int   mLineStyle  = LINE_STYLE_MATCH;

    // ################## TAB triangle模式下的属性 #######################
    private float mTriangleHeight      = 8;
    private float mTriangleWidth       = 20;
    private int   mTriangleColor       = Color.TRANSPARENT;
    private int   mTriangleStyle       = TRIANGLE_STYLE_FILL;
    private float mTriangleStrokeWidth = 2;

    // ################## TAB rect模式下的属性 ###########################
    private float mRectPaddingLeft   = 8;
    private float mRectPaddingTop    = 8;
    private float mRectPaddingRight  = 8;
    private float mRectPaddingBottom = 8;
    private int   mRectColor         = Color.TRANSPARENT;
    private float mRectRadius        = 0;
    private int   mRectStyle         = RECT_STYLE_FILL;
    private int   mRectStrokeColor   = Color.TRANSPARENT;
    private float mRectStrokeWidth   = 0;

    private Paint            mPaint        = new Paint();
    private Path             mTrianglePath = null;
    private GradientDrawable mRectDrawable = null;

    private float mPagerOffset     = 0f;
    private int   mCurrentPosition = 0;


    public TabIndicator(Context context)
    {
        this(context, null);
    }

    public TabIndicator(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        //滑动条不可见
        setFillViewport(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setHorizontalFadingEdgeEnabled(false);
        setBackgroundColor(Color.TRANSPARENT);

        //创建tab容器
        mTabContainer = new LinearLayout(context, attrs);
        mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
        //        mTabContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        addView(mTabContainer);

        //初始化自定义属性
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet set)
    {
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.TabIndicator);

        //通用的属性获取
        mTabPaddingLeft = ta.getDimension(R.styleable.TabIndicator_tabPaddingLeft, mTabPaddingLeft);
        mTabPaddingTop = ta.getDimension(R.styleable.TabIndicator_tabPaddingTop, mTabPaddingTop);
        mTabPaddingRight = ta.getDimension(R.styleable.TabIndicator_tabPaddingRight,
                                           mTabPaddingRight);
        mTabPaddingBottom = ta.getDimension(R.styleable.TabIndicator_tabPaddingBottom,
                                            mTabPaddingBottom);

        mTabBackground = ta.getResourceId(R.styleable.TabIndicator_tabBackground, mTabBackground);
        mTabTextColor = ta.getResourceId(R.styleable.TabIndicator_tabTextColor, mTabTextColor);
        mTabTextSize = ta.getDimension(R.styleable.TabIndicator_tabTextSize, mTabTextSize);
        mTabTextBlod = ta.getBoolean(R.styleable.TabIndicator_tabTextBlod, mTabTextBlod);

        mUnderLineHeight = ta.getDimension(R.styleable.TabIndicator_underLineHeight,
                                           mUnderLineHeight);
        mUnderLineColor = ta.getColor(R.styleable.TabIndicator_underLineColor, mUnderLineColor);

        mTabMode = ta.getInt(R.styleable.TabIndicator_tabMode, mTabMode);

        mLineHeight = ta.getDimension(R.styleable.TabIndicator_lineHeight, mLineHeight);
        mLineColor = ta.getColor(R.styleable.TabIndicator_lineColor, mLineColor);
        mLineStyle = ta.getInt(R.styleable.TabIndicator_lineStyle, mLineStyle);

        mTriangleHeight = ta.getDimension(R.styleable.TabIndicator_triangleHeight, mTriangleHeight);
        mTriangleWidth = ta.getDimension(R.styleable.TabIndicator_triangleHeight, mTriangleWidth);
        mTriangleColor = ta.getColor(R.styleable.TabIndicator_triangleColor, mTriangleColor);
        mTriangleStyle = ta.getInt(R.styleable.TabIndicator_triangleStyle, mTriangleStyle);
        mTriangleStrokeWidth = ta.getDimension(R.styleable.TabIndicator_triangleStrokeWidth,
                                               mTriangleStrokeWidth);

        mRectPaddingLeft = ta.getDimension(R.styleable.TabIndicator_rectPaddingLeft,
                                           mRectPaddingLeft);
        mRectPaddingTop = ta.getDimension(R.styleable.TabIndicator_rectPaddingLeft,
                                          mRectPaddingTop);
        mRectPaddingRight = ta.getDimension(R.styleable.TabIndicator_rectPaddingLeft,
                                            mRectPaddingRight);
        mRectPaddingBottom = ta.getDimension(R.styleable.TabIndicator_rectPaddingLeft,
                                             mRectPaddingBottom);
        mRectColor = ta.getColor(R.styleable.TabIndicator_rectColor, mRectColor);
        mRectRadius = ta.getDimension(R.styleable.TabIndicator_rectRadius, mRectRadius);
        mRectStyle = ta.getInt(R.styleable.TabIndicator_rectStyle, mRectStyle);
        mRectStrokeWidth = ta.getDimension(R.styleable.TabIndicator_rectStrokeWidth,
                                           mRectStrokeWidth);
        mRectStrokeColor = ta.getColor(R.styleable.TabIndicator_rectStrokeColor, mRectStrokeColor);

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        switch (mTabMode)
        {
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
     * @param canvas 画布
     */
    private void drawLine(Canvas canvas)
    {
        //重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mLineColor);

        //计算当前的left和right
        float[] clr   = getCurrentLeftAndRight();
        float   left  = clr[0];
        float   right = clr[1];

        if (mLineStyle == LINE_STYLE_WRAP)
        {
            left += mTabPaddingLeft;
            right -= mTabPaddingRight;
        }

        float top    = getMeasuredHeight() - mUnderLineHeight - mLineHeight;
        float bottom = getMeasuredHeight() - mUnderLineHeight;
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * draw triangle
     * @param canvas 画布
     */
    private void drawTriangle(Canvas canvas)
    {
        //重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTriangleColor);

        //计算当前的left和right
        float[] clr   = getCurrentLeftAndRight();
        float   left  = clr[0];
        float   right = clr[1];

        float x1 = (left + right) / 2f;
        float y1 = getMeasuredHeight() - mUnderLineHeight - mTriangleHeight;
        float x2 = x1 - mTriangleWidth / 2f;
        float y2 = getMeasuredHeight() - mUnderLineHeight;
        float x3 = x1 + mTriangleWidth / 2f;
        float y3 = getMeasuredHeight() - mUnderLineHeight;

        if (mTriangleStyle == TRIANGLE_STYLE_FILL)
        {
            drawFillTriangle(x1, y1, x2, y2, x3, y3, canvas);
        } else
        {
            drawStrokeTriangle(x1, y1, x2, y2, x3, y3, canvas);
        }
    }

    /**
     * draw stroke style triangle
     * @param canvas 画布
     */
    private void drawStrokeTriangle(float x1,
                                    float y1,
                                    float x2,
                                    float y2,
                                    float x3,
                                    float y3,
                                    Canvas canvas)
    {
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
     * @param canvas 画布
     */
    private void drawFillTriangle(float x1,
                                  float y1,
                                  float x2,
                                  float y2,
                                  float x3,
                                  float y3,
                                  Canvas canvas)
    {
        if (mTrianglePath == null)
        {
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
     * @param canvas 画布
     */
    private void drawRect(Canvas canvas)
    {
        //计算当前的left和right
        float[] clr   = getCurrentLeftAndRight();
        float   left  = clr[0] + mRectPaddingLeft;
        float   right = clr[1] - mRectPaddingRight;

        float top    = mRectPaddingTop;
        float bottom = getMeasuredHeight() - mUnderLineHeight - mRectPaddingBottom;

        if (mRectDrawable == null)
        {
            mRectDrawable = new GradientDrawable();
        }
        mRectDrawable.setShape(GradientDrawable.RECTANGLE);
        mRectDrawable.setColor(mRectColor);
        mRectDrawable.setCornerRadius(mRectRadius);
        mRectDrawable.setStroke((int) mRectStrokeWidth, mRectStrokeColor);
        mRectDrawable.setBounds((int) left, (int) top, (int) right, (int) bottom);

        mRectDrawable.draw(canvas);
    }

    /**
     * 计算当前滑动的left和right
     * @return 获得动态的left和right，结果为float[],0为left，1为right
     */
    private float[] getCurrentLeftAndRight()
    {
        //获得当前的tab的left和right
        View  currentTab = mTabContainer.getChildAt(mCurrentPosition);
        float left       = currentTab.getLeft();
        float right      = currentTab.getRight();

        if (mPagerOffset > 0f && mCurrentPosition < mViewPager.getAdapter()
                                                              .getCount() - 1)
        {
            //获得下一个的tab的left和right
            View nextTab = mTabContainer.getChildAt(mCurrentPosition + 1);
            float nextLeft = nextTab.getLeft();
            float nextRight = nextTab.getRight();

            //计算偏移后的left和right
            left = nextLeft * mPagerOffset + (1 - mPagerOffset) * left;
            right = nextRight * mPagerOffset + (1 - mPagerOffset) * right;
        }

        return new float[]{left,
                           right};
    }

    /**
     * draw underline
     * @param canvas 画布
     */
    private void drawUnderLine(Canvas canvas)
    {
        if (mTabMode == TAB_MODE_TRIANGLE && mTriangleStyle == TRIANGLE_STYLE_STROKE)
        {
            return;
        }

        //重置画笔
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mUnderLineColor);

        float left   = 0;
        float top    = getMeasuredHeight() - mUnderLineHeight;
        float right  = mTabContainer.getMeasuredWidth();
        float bottom = getMeasuredHeight();
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * 设置ViewPager
     *
     * @param pager viewpager
     */
    public void setViewPager(ViewPager pager)
    {
        if (pager.getAdapter() == null)
        {
            throw new IllegalStateException("ViewPager还没有调用setAdapter()来设置数据");
        }

        //保存实例
        this.mViewPager = pager;

        //设置监听
        if (mTabPageListener == null)
        {
            mTabPageListener = new TabPageListener();
        }
        this.mViewPager.setOnPageChangeListener(mTabPageListener);

        //更新Tab的显示
        updateTabs();
    }

    private void updateTabs()
    {
        //清空
        mTabContainer.removeAllViews();

        PagerAdapter adapter = mViewPager.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++)
        {
            CharSequence title = adapter.getPageTitle(i);
            addTab(title, i);
        }
    }

    /**
     * 添加tab
     * @param title tab显示的title
     * @param index tab的index
     */
    private void addTab(CharSequence title, final int index)
    {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setPadding((int) mTabPaddingLeft,
                       (int) mTabPaddingTop,
                       (int) mTabPaddingRight,
                       (int) mTabPaddingBottom);
        tab.setBackgroundResource(mTabBackground);
        tab.setTextColor(getResources().getColorStateList(mTabTextColor));
        tab.setTextSize(mTabTextSize);
        tab.getPaint()
           .setFakeBoldText(mTabTextBlod);
        tab.setSelected(index == 0);//设置默认
        tab.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (index == mViewPager.getCurrentItem())
                {
                    return;
                }
                mViewPager.setCurrentItem(index);
            }
        });

        mTabContainer.addView(tab, index, getTabLayoutParam());
    }

    private LinearLayout.LayoutParams getTabLayoutParam()
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                                                                         LayoutParams.WRAP_CONTENT);

        params.bottomMargin = (int) (mUnderLineHeight + 0.5f);
        return params;
    }

    private void scrollTabs()
    {
        View view = mTabContainer.getChildAt(mCurrentPosition);
        int  endX = (int) (view.getMeasuredWidth() * mPagerOffset + view.getLeft() + 0.5f);
        scrollTo(endX, 0);
    }

    /**
     * set tab padding
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setTabPadding(int left, int top, int right, int bottom)
    {
        this.mTabPaddingLeft = left;
        this.mTabPaddingTop = top;
        this.mTabPaddingRight = right;
        this.mTabPaddingBottom = bottom;
    }

    public void setTabBackground(int resId)
    {
        this.mTabBackground = resId;
    }

    public void setTabTextColor(int resId)
    {
        this.mTabTextColor = resId;
    }

    public void setTabTextSize(float size)
    {
        this.mTabTextSize = size;
    }

    public void setTabTextBlod(boolean blod)
    {
        this.mTabTextBlod = blod;
    }

    public void setUnderLineHeight(float underLineHeight)
    {
        this.mUnderLineHeight = underLineHeight;
    }

    public void setUnderLineColor(int underLineColor)
    {
        this.mUnderLineColor = underLineColor;
    }

    public void setTabMode(int tabMode)
    {
        if (tabMode != TAB_MODE_LINE && tabMode != TAB_MODE_RECT && tabMode != TAB_MODE_TRIANGLE)
        {
            tabMode = TAB_MODE_LINE;
        }
        this.mTabMode = tabMode;
    }

    public void setLineHeight(float lineHeight)
    {
        this.mLineHeight = lineHeight;
    }

    public void setLineColor(int lineColor)
    {
        this.mLineColor = lineColor;
    }

    public void setLineStyle(int lineStyle)
    {
        if (lineStyle == LINE_STYLE_MATCH)
        {
            this.mLineStyle = lineStyle;
        } else
        {
            this.mLineStyle = LINE_STYLE_WRAP;
        }
    }

    public void setTriangleHeight(float triangleHeight)
    {
        this.mTriangleHeight = triangleHeight;
    }

    public void setTriangleWidth(float triangleWidth)
    {
        this.mTriangleWidth = triangleWidth;
    }

    public void setTriangleColor(int triangleColor)
    {
        this.mTriangleColor = triangleColor;
    }

    public void setTriangleStyle(int triangleStyle)
    {
        if (triangleStyle == TRIANGLE_STYLE_FILL)
        {
            this.mTriangleStyle = triangleStyle;
        } else
        {
            this.mTriangleStyle = TRIANGLE_STYLE_STROKE;
        }
    }

    public void setRectPadding(float left, float top, float right, float bottom)
    {
        this.mRectPaddingLeft = left;
        this.mRectPaddingTop = top;
        this.mRectPaddingRight = right;
        this.mRectPaddingBottom = bottom;
    }

    public void setRectColor(int color)
    {
        this.mRectColor = color;
    }

    public void setRectRadius(float radius)
    {
        this.mRectRadius = radius;
    }

    public void setRectStyle(int style)
    {
        if (style == RECT_STYLE_FILL)
        {
            this.mRectStyle = style;
        } else
        {
            this.mRectStyle = RECT_STYLE_STROKE;
        }
    }

    public void setRectStrokeColor(int color)
    {
        this.mRectStrokeColor = color;
    }

    public void setRectStrokeWidth(float width)
    {
        this.mRectStrokeWidth = width;
    }


    public void setTriangleStrokeWidth(float triangleStrokeWidth)
    {
        this.mTriangleStrokeWidth = triangleStrokeWidth;
    }

    /**
     * Page 改变的监听器
     */
    private class TabPageListener
            implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            //存储position,offset
            mCurrentPosition = position;
            mPagerOffset = positionOffset;

            //滚动
            scrollTabs();

            //触发绘制
            invalidate();
        }

        @Override
        public void onPageSelected(int position)
        {
            int count = mTabContainer.getChildCount();
            for (int i = 0; i < count; i++)
            {
                View view = mTabContainer.getChildAt(i);
                view.setSelected(i == position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }
}
