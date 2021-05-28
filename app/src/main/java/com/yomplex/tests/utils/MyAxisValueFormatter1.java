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
        //value+"\n"+"%";
        if (value == 1.0) {
            appendix = "4 wks ago";
        } else if (value == 2.0) {
            appendix = "3 wks ago";
        } else if (value == 3.0) {
            appendix = "2 wks ago";
        } else if (value == 4.0) {

            appendix = "Last week";
        } else if (value == 5.0) {

            appendix = "This week";
        }
       // Log.e("my axis value formater1","getFormattedValue........appendix......"+value);
        return appendix;
    }
}

