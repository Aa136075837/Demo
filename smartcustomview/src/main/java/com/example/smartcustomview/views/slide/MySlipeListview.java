package com.example.smartcustomview.views.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class MySlipeListview extends ListView {
    public MySlipeListview(Context context) {
        super(context);
    }

    public MySlipeListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlipeListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int touchx = 0;
    private int touchy = 0;

    public void setbDealInLocal(boolean bDealInLocal) {
        this.bDealInLocal = bDealInLocal;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchy = (int) ev.getY();
                touchx = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int mx = (int) ev.getX();
                int my = (int) ev.getY();
                int delax = mx - touchx;
                int delay = my - touchy;

                if (Math.abs(delax) < 10 && Math.abs(delay) < 10) {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //事件是否需要在本地处理
    private boolean bDealInLocal = true;

    int mmmx = 0;
    int mmmy = 0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mmmy = (int) ev.getY();
                mmmx = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int mx = (int) ev.getX();
                int my = (int) ev.getY();
                int delax = mx - mmmx;
                int delay = my - mmmy;

               // Log.i("lanjie", "XXXX"+delax +"   YYYY"+delay);

                //如果已经交给了孩子去处理 接下来孩子就直接去处理
                if(!bDealInLocal){
                    return false;
                }

                if (Math.abs(delay) >= 10 && Math.abs(delay) > Math.abs(delax)) {
                    Log.i("lanjie", "拦截111111111");
                    bDealInLocal = true;
                    return true;
                }else  if (Math.abs(delax) >= 10 && Math.abs(delax) > Math.abs(delay) ) {
                    Log.i("lanjie", "拦截333333333");
                    bDealInLocal = false;
                    return false;
                }else{
                    Log.i("lanjie", "拦截44444444");
                    bDealInLocal = true;
                    return true;
                }
            case MotionEvent.ACTION_UP:
                bDealInLocal = true;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}