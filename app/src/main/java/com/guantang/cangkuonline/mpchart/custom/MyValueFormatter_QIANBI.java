package com.guantang.cangkuonline.mpchart.custom;

import java.text.DecimalFormat;

import com.github.mikephil.charting.utils.ValueFormatter;

public class MyValueFormatter_QIANBI implements ValueFormatter {

    private DecimalFormat mFormat;
    
    public MyValueFormatter_QIANBI() {
        mFormat = new DecimalFormat("###########0.0");
    }
    
    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value/1000) + "千笔";
    }

}
