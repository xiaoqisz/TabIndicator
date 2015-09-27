package org.itheima.tabindicator.sample;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

/*
 *  @项目名：  TabIndicator 
 *  @包名：    org.itheima.tabindicator.sample
 *  @文件名:   ColorDialog
 *  @创建者:   Administrator
 *  @创建时间:  2015/9/27 17:30
 *  @描述：    TODO
 */
public class ColorDialog
        extends Dialog
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener
{
    private View    mVColor;
    private SeekBar mSbAlpha;
    private SeekBar mSbRed;
    private SeekBar mSbGreen;
    private SeekBar mSbBlue;
    private Button  mBtnOk;

    private int                    mColor;
    private OnColorChangedListener mListener;

    public ColorDialog(Context context)
    {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color);

        //set title
        setTitle(R.string.dialog_color_title);

        // init view
        initView();

        //init event
        initEvent();

        // initData
        initData();
    }


    private void initView()
    {
        mVColor = findViewById(R.id.dialog_color_show);
        mSbAlpha = (SeekBar) findViewById(R.id.dialog_color_alpha);
        mSbRed = (SeekBar) findViewById(R.id.dialog_color_red);
        mSbGreen = (SeekBar) findViewById(R.id.dialog_color_green);
        mSbBlue = (SeekBar) findViewById(R.id.dialog_color_blue);
        mBtnOk = (Button) findViewById(R.id.dialog_color_btn_ok);

        mSbAlpha.setMax(255);
        mSbRed.setMax(255);
        mSbGreen.setMax(255);
        mSbBlue.setMax(255);
    }

    private void initEvent()
    {
        mSbAlpha.setOnSeekBarChangeListener(this);
        mSbRed.setOnSeekBarChangeListener(this);
        mSbGreen.setOnSeekBarChangeListener(this);
        mSbBlue.setOnSeekBarChangeListener(this);
        mBtnOk.setOnClickListener(this);
    }

    private void initData()
    {
        if (mVColor != null)
        {
            mVColor.setBackgroundColor(mColor);
        }

        if (mSbAlpha != null)
        {
            int alpha = Color.alpha(mColor);
            mSbAlpha.setProgress(alpha);
        }

        if (mSbRed != null)
        {
            int red = Color.red(mColor);
            mSbRed.setProgress(red);
        }

        if (mSbGreen != null)
        {
            int green = Color.green(mColor);
            mSbGreen.setProgress(green);
        }

        if (mSbBlue != null)
        {
            int blue = Color.blue(mColor);
            mSbBlue.setProgress(blue);
        }
    }

    public void setColor(int color)
    {
        this.mColor = color;

        initData();
    }

    public void setColor(String colorString)
    {
        this.mColor = Color.parseColor(colorString);

        initData();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        int alpha = Color.alpha(mColor);
        int red   = Color.red(mColor);
        int green = Color.green(mColor);
        int blue  = Color.blue(mColor);

        if (seekBar == mSbAlpha)
        {
            alpha = progress;
        } else if (seekBar == mSbRed)
        {
            red = progress;
        } else if (seekBar == mSbGreen)
        {
            green = progress;
        } else if (seekBar == mSbBlue)
        {
            blue = progress;
        }

        this.mColor = Color.argb(alpha, red, green, blue);
        mVColor.setBackgroundColor(mColor);

        if (mListener != null)
        {
            mListener.onColorChanged(mColor);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onClick(View v)
    {
        if (v == mBtnOk)
        {
            clickOk();
        }
    }

    private void clickOk()
    {
        dismiss();
    }

    public void setOnColorChangedListener(OnColorChangedListener listener)
    {
        this.mListener = listener;
    }

    public interface OnColorChangedListener
    {
        void onColorChanged(int color);
    }
}
