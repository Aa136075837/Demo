package com.example.smartcustomview.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * @author bo.
 * @Date 2017/8/31.
 * @desc  该控件宽度不能设置为wrap_content，
 */

public class AutoFitTextView extends TextView {

    //默认大小
    private static float DEFAULT_MIN_TEXT_SIZE = 5;
    private static float DEFAULT_MAX_TEXT_SIZE = 50;

    private TextPaint testPaint;
    private float minTextSize;
    private float maxTextSize;

    public AutoFitTextView (Context context) {
        this (context, null);
    }

    public AutoFitTextView (Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public AutoFitTextView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init (context, attrs, defStyleAttr);
    }

    private void init (Context context, AttributeSet attrs, int defStyleAttr) {
        testPaint = new TextPaint ();
        testPaint.set (this.getPaint ());

        maxTextSize = this.getTextSize ();
        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }
        minTextSize = DEFAULT_MIN_TEXT_SIZE;

        refitText (this.getText ().toString (), (int) (this.getWidth () * 0.8f));
    }

    /**
     * 根据控件宽度计算可显示的宽度和内容宽度计算字体大小
     * @param text
     * @param textViewWidth
     */
    private void refitText (String text, int textViewWidth) {
        if (textViewWidth > 0) {
            int availableWidth = textViewWidth - this.getPaddingLeft () - this.getPaddingRight ();
            float trySize = maxTextSize;
            testPaint.setTextSize (trySize);
            while ((trySize > minTextSize)) {
                int displayW = (int) testPaint.measureText (text);
                if (displayW < availableWidth) {
                    break;
                }
                trySize -= 8;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }

                testPaint.setTextSize (trySize);
            }
            this.setSingleLine (true);
            this.setMaxLines (1);
            if (text.contains ("m") || text.contains ("w")||text.contains ("M") || text.contains ("W")){
                trySize -= 12;
            }
            this.setTextSize (TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }

    @Override
    protected void onTextChanged (CharSequence text, int start, int before, int after) {
        super.onTextChanged (text, start, before, after);
        refitText (text.toString (), (int) (this.getWidth () * 0.8f));
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        Log.e ("TagSizeChange", "new(" + w + "," + h + ") old(" + oldw + "" + oldh + ")");
        if (w != oldw || h != oldh) {
            refitText (this.getText ().toString (), (int) (w * 0.8));
        }
    }
}
