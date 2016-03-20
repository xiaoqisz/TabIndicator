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

public class LineActivity
        extends AppCompatActivity
{

    private static final String TAG = "LineActivity";

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
        setContentView(R.layout.activity_line);

        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (TabIndicator) findViewById(R.id.indicator);



        //设置adapter
        mPager.setAdapter(new MainPagerAdapter());

        //设置viewpager
        mIndicator.setViewPager(mPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_line, menu);

        MenuItem lineMatchItem = menu.findItem(R.id.line_action_style_match);
        MenuItem lineWrapItem  = menu.findItem(R.id.line_action_style_wrap);
        int      lineStyle     = mIndicator.getLineStyle();
        switch (lineStyle)
        {
            case TabIndicator.LINE_STYLE_MATCH:
                lineMatchItem.setChecked(true);
                break;
            case TabIndicator.LINE_STYLE_WRAP:
                lineWrapItem.setChecked(true);
                break;
            default:
                break;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.line_action_style_match:
                item.setChecked(true);
                mIndicator.setLineStyle(TabIndicator.LINE_STYLE_MATCH);
                break;
            case R.id.line_action_style_wrap:
                item.setChecked(true);
                mIndicator.setLineStyle(TabIndicator.LINE_STYLE_WRAP);
                break;
            case R.id.line_action_color:
                choiceColor();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void choiceColor()
    {
        ColorDialog dialog    = new ColorDialog(this);
        int         lineColor = mIndicator.getLineColor();
        dialog.setColor(lineColor);
        dialog.setOnColorChangedListener(new ColorDialog.OnColorChangedListener()
        {
            @Override
            public void onColorChanged(int color)
            {
                mIndicator.setLineColor(color);
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
            TextView tv = new TextView(LineActivity.this);
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
