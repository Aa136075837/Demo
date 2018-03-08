package com.example.smartcustomview.views.shadow;

import android.content.Context;
import com.example.smartcustomview.utils.DisplayUtil;

public enum ZDepth {

    Depth0( // TODO
            0,
            0,
            0,
            0,
            0,
            0
    ),

    Depth1(
            30, // alpha to black
            61, // alpha to black
            1.0f, // dp
            1.0f, // dp
            1.5f, // dp
            1.0f  // dp
    ),
    Depth2(
            40,
            58,
            3.0f,
            3.0f,
            3.0f,
            3.0f
    ),
    Depth3(
            48,
            58,
            10.0f,
            6.0f,
            10.0f,
            3.0f
    ),
    Depth4(
            64,
            56,
            14.0f,
            10.0f,
            14.0f,
            5.0f
    ),
    Depth5(
            35,
            35,
            19.0f,
            19.0f,
            40.0f,
            40.0f
    );

    public final int mAlphaTopShadow; // alpha to black
    public final int mAlphaBottomShadow; // alpha to black

    public final float mOffsetYTopShadow; // dp
    public final float mOffsetYBottomShadow; // dp

    public final float mBlurTopShadow; // dp
    public final float mBlurBottomShadow; // dp

    private ZDepth(int alphaTopShadow, int alphaBottomShadow, float offsetYTopShadow, float offsetYBottomShadow, float blurTopShadow, float blurBottomShadow) {
        mAlphaTopShadow = alphaTopShadow;
        mAlphaBottomShadow = alphaBottomShadow;
        mOffsetYTopShadow = offsetYTopShadow;
        mOffsetYBottomShadow = offsetYBottomShadow;
        mBlurTopShadow = blurTopShadow;
        mBlurBottomShadow = blurBottomShadow;
    }

    public int getAlphaTopShadow() {
        return mAlphaTopShadow;
    }

    public int getAlphaBottomShadow() {
        return mAlphaBottomShadow;
    }

    public float getOffsetYTopShadowPx(Context context) {
        return DisplayUtil.convertDpToPx(context, mOffsetYTopShadow);
    }

    public float getOffsetYBottomShadowPx(Context context) {
        return DisplayUtil.convertDpToPx(context, mOffsetYBottomShadow);
    }

    public float getBlurTopShadowPx(Context context) {
        return DisplayUtil.convertDpToPx(context, mBlurTopShadow);
    }

    public float getBlurBottomShadowPx(Context context) {
        return DisplayUtil.convertDpToPx(context, mBlurBottomShadow);
    }
}
