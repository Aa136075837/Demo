package com.example.bo.nixon.ui.activity.camera;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.example.bo.nixon.utils.AnimationUtil;

/**
 * @author ARZE
 * @version 创建时间：2017/6/14 10:17
 * @说明
 */
public class CameraAnimFactory {

    public static AnimatorSet createAnimatorSet(View view) {
        ObjectAnimator animator1 = AnimationUtil.scaleCenterX(view, 0.6f, 100);
        ObjectAnimator animator2 = AnimationUtil.scaleCenterY(view, 0.6f, 100);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(animator1).with(animator2);
        return mAnimatorSet;
    }

}
