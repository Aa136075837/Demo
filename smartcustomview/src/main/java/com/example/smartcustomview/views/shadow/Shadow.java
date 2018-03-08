package com.example.smartcustomview.views.shadow;

import android.graphics.Canvas;


public interface Shadow {
    public void setParameter (ZDepthParam parameter, int left, int top, int right, int bottom);
    public void onDraw (Canvas canvas);
}
