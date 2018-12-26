package com.rainbell.simple.tools;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

import com.rainbell.simple.config.Config;

import io.netty.channel.ChannelInboundHandlerAdapter;

public class Utils {
    public static void verticalCome(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 400);
        animator.setTarget(view);
        animator.setDuration(800).start();
//        AnimatorSet set = new AnimatorSet();
//        set.play(animator);
        animator.addUpdateListener(animation -> view.setTranslationY((Float) animation.getAnimatedValue()));
    }

    public static void alphaCome(View view) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, Config.ALPHA, 0f, 1f);
        animator.setDuration(500);
        animator.start();
//        AnimatorSet set = new AnimatorSet();
//        set.play(animator);
    }

}
