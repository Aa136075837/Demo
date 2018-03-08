package com.example.smartcustomview.views.slide;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

public class MySlipeItem extends ViewGroup {
    private Scroller mScroller;
    private ScrollerCompat scrollCompat;

    public MySlipeItem(Context context) {
        super(context);
    }

    public MySlipeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollCompat = ScrollerCompat.create(context);
        mScroller = new Scroller(context, new BounceInterpolator ());
    }

    public MySlipeItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int getPx(int idp) {
        DisplayMetrics ccc = new DisplayMetrics();
        DisplayMetrics displayMetrics = getResources ().getDisplayMetrics ();
        //((Activity) getContext())
        //        .getWindowManager().getDefaultDisplay().getMetrics(ccc);
        return (int) (displayMetrics.density * (float)idp + 0.5f);
    }

    private ViewGroup child1;
    private ViewGroup child2;

    int leftLimitScrollX = 0;
    int rightLimitscrollX = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        child1 = (ViewGroup) getChildAt(0);
        child2 = (ViewGroup) getChildAt(1);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int child1W = w - getPx(80);


        child2.measure(widthMeasureSpec, heightMeasureSpec);
        child1.measure(MeasureSpec.makeMeasureSpec(child1W, MeasureSpec.EXACTLY), heightMeasureSpec);

        leftLimitScrollX = -child1.getMeasuredWidth();
        rightLimitscrollX = 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        child2.layout(l, t, r, b);
        child1.layout(-child1.getMeasuredWidth(), t, 0, b);
        if (bOpen){
            scrollTo(leftLimitScrollX,0);
        }else{
            scrollTo(0,0);
        }
    }

    private int getDisdance(){
        return Math.abs(leftLimitScrollX - rightLimitscrollX);
    }

    int iRecordX = 0;

    public boolean bOpen = false;
    public void setOpen(boolean bO){
        bOpen = bO;
        if (bOpen){
            scrollTo(leftLimitScrollX,0);
        }else{
            scrollTo(0,0);
        }
        invalidate();
    }


    int touchx = 0;
    int touchy = 0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
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

                if (Math.abs(delax) > 10) {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                iRecordX = (int) event.getX();
                Log.i("lanjie", "down~~~~~~~~~~");
                break;

            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int deltaX = iRecordX - moveX;

                Log.i("lanjie", "TTTTTTTt" + deltaX);
                scrollBy(deltaX, 0);
                iRecordX = moveX;

                int currentScrollX = getScrollX();
                if(currentScrollX <= leftLimitScrollX){
                    scrollTo(leftLimitScrollX,0);
                }else if(currentScrollX >= 0){
                    scrollTo(0,0);
                }
                break;
            case MotionEvent.ACTION_UP:
                currentScrollX = getScrollX();
                if(currentScrollX < -getDisdance()*19/20){
                    doAnim(currentScrollX,leftLimitScrollX);
                    bOpen = true;
                    if(ll!= null){
                        ll.OnOpen(bOpen);
                    }
                }
                if (currentScrollX > -getDisdance()/20){
                    doAnim(currentScrollX, 0);
                    bOpen = false;
                    if(ll!= null){
                        ll.OnOpen(bOpen);
                    }
                }

                if(currentScrollX > -getDisdance()*19/20 && currentScrollX < -getDisdance()/20){
                    if(!bOpen){
                        doAnim(currentScrollX, leftLimitScrollX);
                        bOpen = true;
                        if(ll!= null){
                            ll.OnOpen(bOpen);
                        }
                    }else if(bOpen){
                        doAnim(currentScrollX,0);
                        bOpen = false;
                        if(ll!= null){
                            ll.OnOpen(bOpen);
                        }
                    }
                }
                break;
        }
        invalidate();
        super.onTouchEvent(event);
        return true;
    }


    public interface OpenListner {
        void OnOpen (boolean b);
    }
    private OpenListner ll;
    public void setOpenListner(OpenListner ll){
        this.ll = ll;
    }

    private void doAnim(int currentScrollX, int leftLimitScrollX){
        ValueAnimator viewAnimator = ValueAnimator.ofFloat(currentScrollX,leftLimitScrollX);
        viewAnimator.setDuration(200);
        viewAnimator.setInterpolator(new DecelerateInterpolator ());
        viewAnimator.setRepeatCount(0);
        viewAnimator.setTarget(this);
        viewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float toV = (float) animation.getAnimatedValue();
                scrollTo((int) toV, 0);
                invalidate();
            }
        });
        viewAnimator.start();
    }
}