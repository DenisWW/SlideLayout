package com.rainbell.simple.tools;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

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
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
