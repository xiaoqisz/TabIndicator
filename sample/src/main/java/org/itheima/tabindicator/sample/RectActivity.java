package org.itheima.tabindicator.sample;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.itheima.tabindicator.library.TabIndicator;

public class RectActivity
        extends AppCompatActivity
{

    private static final String TAG = "RectActivity";

    private String[] mDatas = new String[]{"网游",
                                           "WIFI万能钥匙",
                                           "播放器",
                                           "捕鱼达人2",
                                           "机票",
                                           "游戏",
                                           "熊出没之熊大快跑"};
    private TabIndicator mIndicator;
    private ViewPager    mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect);

        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (TabIndicator) findViewById(R.id.indicator);

        mIndicator.addLeftTabIcon(R.mipmap.tab1_n, R.mipmap.tab1_p);
        mIndicator.addLeftTabIcon(R.mipmap.tab2_n, R.mipmap.tab2_p);
        mIndicator.addLeftTabIcon(R.mipmap.tab3_n, R.mipmap.tab3_p);


        //设置adapter
        mPager.setAdapter(new MainPagerAdapter());

        //设置viewpager
        mIndicator.setViewPager(mPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_rect, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem rectFillItem    = menu.findItem(R.id.rect_action_style_fill);
        MenuItem rectStrokeItem  = menu.findItem(R.id.rect_action_style_stroke);
        MenuItem strokeColorItem = menu.findItem(R.id.rect_action_stroke_color);
        int      rectStyle       = mIndicator.getRectStyle();
        switch (rectStyle)
        {
            case TabIndicator.RECT_STYLE_FILL:
                rectFillItem.setChecked(true);

                strokeColorItem.setVisible(false);
                break;
            case TabIndicator.RECT_STYLE_STROKE:
                rectStrokeItem.setChecked(true);

                strokeColorItem.setVisible(true);
                break;
            default:
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.rect_action_style_fill:
                item.setChecked(true);
                mIndicator.setRectStyle(TabIndicator.RECT_STYLE_FILL);
                break;
            case R.id.rect_action_style_stroke:
                item.setChecked(true);
                mIndicator.setRectStyle(TabIndicator.RECT_STYLE_STROKE);
                break;
            case R.id.rect_action_color:
                choiceColor();
                break;
            case R.id.rect_action_stroke_color:
                choiceStrokeColor();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void choiceColor()
    {
        ColorDialog dialog    = new ColorDialog(this);
        int         rectColor = mIndicator.getRectColor();
        dialog.setColor(rectColor);
        dialog.setOnColorChangedListener(new ColorDialog.OnColorChangedListener()
        {
            @Override
            public void onColorChanged(int color)
            {
                mIndicator.setRectColor(color);
            }
        });

        dialog.show();
    }

    private void choiceStrokeColor()
    {
        ColorDialog dialog    = new ColorDialog(this);
        int         rectColor = mIndicator.getRectStrokeColor();
        dialog.setColor(rectColor);
        dialog.setOnColorChangedListener(new ColorDialog.OnColorChangedListener()
        {
            @Override
            public void onColorChanged(int color)
            {
                mIndicator.setRectStrokeColor(color);
            }
        });

        dialog.show();
    }


    private class MainPagerAdapter
            extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return mDatas.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            TextView tv = new TextView(RectActivity.this);
            tv.setText(mDatas[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(24);
            container.addView(tv);

            return tv;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mDatas[position];
        }
    }


}
