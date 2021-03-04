package com.yomplex.tests.utils;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter
{

    private final DecimalFormat mFormat;

    public MyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        //Log.e("my axis value formater","getFormattedValue........value......"+value);
        String appendix = "";

        if (value == 0.0f) {
            appendix = "0";
        } else if (value == 2.0f) {
            appendix = "2";
        } else if (value == 4.0f) {
            appendix = "4";
        } else if (value == 6.0f) {

            appendix = "6";
        } else if (value == 8.0f) {

            appendix = "8";
        }else if (value == 10.0f) {

            appendix = "10";
        }else if (value == 12.0f) {

            appendix = "12";
        }else if (value == 14.0f) {

            appendix = "14";
        }else if (value == 16.0f) {

            appendix = "16";
        }else if (value == 18.0f) {

            appendix = "18";
        }else if (value == 20.0f) {

            appendix = "20";
        }
       // Log.e("my axis value formater","getFormattedValue........appendix......"+appendix);
        return appendix;
    }
}

