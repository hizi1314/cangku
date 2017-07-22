package com.guantang.cangkuonline.mpchart.custom;

import java.text.DecimalFormat;

import com.github.mikephil.charting.utils.ValueFormatter;

public class MyValueFormatter_WANYUAN implements ValueFormatter {

    private DecimalFormat mFormat;
    
    public MyValueFormatter_WANYUAN() {
        mFormat = new DecimalFormat("###########0.0");
    }
    
    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value/10000) + "万元";
    }

}
