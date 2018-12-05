package com.rainbell.www.slidelayout.recycle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;

public class FullScreenManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
    private PagerSnapHelper mPagerSnapHelper;
    private SnapHelper mSnapHelper;
    private LinearSnapHelper mLinearSnapHelper;
    private int mDrift = 0;

    public FullScreenManager(Context context) {
        super(context);
    }

    public FullScreenManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPagerSnapHelper = new PagerSnapHelper();
        mLinearSnapHelper = new LinearSnapHelper();
        mSnapHelper = new SnapHelper() {
            /**
             这个方法会计算第二个参数对应的ItemView当前的坐标与需要对齐的坐标之间的距离。
             该方法返回一个大小为2的int数组，分别对应x轴和y轴方向上的距离。
             * @param layoutManager
             * @param targetView
             * @return
             */
            @Nullable
            @Override
            public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
                int[] out = new int[2];
                return out;

            }

            /**
             *  该方法会找到当前layoutManager上最接近对齐位置的那个view，该view称为SanpView，对应的position称为SnapPosition。
             *  如果返回null，就表示没有需要对齐的View，也就不会做滚动对齐调整。
             * @param layoutManager
             * @return
             */
            @Nullable
            @Override
            public View findSnapView(RecyclerView.LayoutManager layoutManager) {
                return null;
            }

            /**
             *该方法会根据触发Fling操作的速率（参数velocityX和参数velocityY）来找到RecyclerView需要滚动到哪个位置，
             * 该位置对应的ItemView就是那个需要进行对齐的列表项。我们把这个位置称为targetSnapPosition，
             * 对应的View称为targetSnapView。如果找不到targetSnapPosition，就返回RecyclerView.NO_POSITION。
             * @param layoutManager
             * @param velocityX
             * @param velocityY
             * @return
             */
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                return 0;
            }
        };
    }


    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        Log.e("AttachedToWindow","=============");
        int position = getPosition(view);
        if (0 == position) {
        }

    }

    @Override
    public void onScrollStateChanged(int state) {

        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View view = mPagerSnapHelper.findSnapView(this);
                int pos = getPosition(view);
                break;
        }
        super.onScrollStateChanged(state);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        Log.e("DetachedFromWindow","=============");
        if (mDrift >= 0) {
        } else {

        }

    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

}
