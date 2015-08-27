package org.itheima.tabindicator.sample;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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
