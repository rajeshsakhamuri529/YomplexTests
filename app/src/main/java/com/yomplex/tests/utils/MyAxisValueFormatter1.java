package com.yomplex.tests.utils;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter1 implements IAxisValueFormatter
{

    private final DecimalFormat mFormat;

    public MyAxisValueFormatter1() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        //Log.e("my axis value formater1","getFormattedValue........value......"+value);
        String appendix = "";

        if (value == 1) {
            appendix = "4 weeks ago";
        } else if (value == 2) {
            appendix = "3 weeks ago";
        } else if (value == 3) {
            appendix = "2 weeks ago";
        } else if (value == 4) {

            appendix = "Last week";
        } else if (value == 5) {

            appendix = "This week";
        }
        return appendix;
    }
}

