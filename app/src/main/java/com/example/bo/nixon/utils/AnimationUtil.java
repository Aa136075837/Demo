package com.example.bo.nixon.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * Created by Administrator on 2016/6/7.
 */
public class AnimationUtil {

    public static ObjectAnimator nopeAnimation(View view) {
        int delta = 20;
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500);
    }

    public static ObjectAnimator nopeAnimation(View view, long time) {
        int delta = 20;
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(time);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(Integer.MAX_VALUE);
        return animator;
    }

    public static ObjectAnimator alphaToShow(View view, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animator.setDuration(time);
        return animator;
    }

    public static ObjectAnimator showToAlpha(View view, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        animator.setDuration(time);
        return animator;
    }

    public static ObjectAnimator transByOff(View view, float off, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), -off);
        animator.setDuration(time);
        return animator;
    }

    public static ObjectAnimator transByOff(View view, float x, float off, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", x, -off);
        animator.setDuration(time);
        return animator;
    }

    public static ObjectAnimator scaleCenterX(View view, float scale, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", 1f, scale);
        animator.setDuration(time);
        return animator;
    }

    public static ObjectAnimator scaleCenterY(View view, float scale, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 1f, scale);
        animator.setDuration(time);
        return animator;
    }

    public static ObjectAnimator transByOffY(View view, int offY, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationX(), -offY);
        animator.setDuration(time);
        return animator;
    }

    public static ObjectAnimator rotateByCenter(View view, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 10, -10);
        animator.setDuration(time);
        animator.setRepeatCount(Integer.MAX_VALUE);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        return animator;
    }


    public static AnimationSet initAnimationSet(int i, int offset) {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, i * 25f, 1f, i * 25f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(offset * 2);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        AlphaAnimation aa = new AlphaAnimation(0.2f, 0.01f);
        aa.setDuration(offset * 2);
        aa.setRepeatCount(Animation.INFINITE);//设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

}
