package com.rainbell.www.slidelayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * /**
 * * MeasureSpec类的具体使用
 * 1. 获取测量模式（Mode）
 * int specMode=MeasureSpec.getMode(measureSpec)
 * <p>
 * 2. 获取测量大小（Size）
 * int specSize=MeasureSpec.getSize(measureSpec)
 * <p>
 * 3. 通过Mode 和 Size 生成新的SpecMode
 * int measureSpec=MeasureSpec.makeMeasureSpec(size,mode);
 */
public class ScrollerLayout extends ViewGroup {

    /**
     * Scroller纯做滑动计算用， 滑动还是需要刷新界面
     */
    private Scroller mScroller;
    //最大拖拽距离越大响应越慢 越迟钝 越小 响应越快 越灵敏
    int mTotalDragDistance;
    //最大位移
    int mSpinnerOffsetEnd;
    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;
    private float mYDown = 0;
    private float mYMove = 0;
    private int height;
    private int min, max;
    /**
     * 上次触发ACTION_MOVE事件时的屏幕坐标
     */
    private float mYLastMove;
    int count;
    private boolean isRefreshing = false;
    int mCurrentTargetOffsetTop;

    public ScrollerLayout(Context context) {
        this(context, null);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        min = viewConfiguration.getScaledMinimumFlingVelocity();
        max = viewConfiguration.getScaledMaximumFlingVelocity();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mTotalDragDistance = (int) (100 * metrics.density);
//        mSpinnerOffsetEnd = mTotalDragDistance;
        mSpinnerOffsetEnd = (int) (64 * metrics.density);
        Log.e("max==", max + "  min=" + min);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            height += childView.getMeasuredHeight();
        }
        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
//        if (b) {
        int time = 0;
        for (int j = 0; j < getChildCount(); j++) {
            View view = getChildAt(j);
//            Log.e("view", "=" + view.toString() + "    =" + view.getMeasuredWidth());
            view.layout(0, time, view.getMeasuredWidth(), time += view.getMeasuredHeight());
        }
//        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDown = ev.getRawY();
                mYLastMove = mYDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mYMove = ev.getRawY();
                float diff = Math.abs(mYMove - mYDown);
                mYLastMove = mYMove;
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }


        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
//        super.computeScroll();
    }

    /**
     * scrollTo 的坐标是绝对位置
     * scrollBy 的坐标是相对位置的距离
     *
     * @param event
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mYMove = event.getRawY();
                float scrollY = (mYLastMove - mYMove) * 0.5f;
                float overscrollTop = (mYMove - mYDown) * 0.5F;
                if (scrollY < 0) {
//                    scrollBy(0, (int) scrollY);
                }
                if (isRefreshing) {
                    isRefreshing = false;
                    mCurrentTargetOffsetTop = mTotalDragDistance;
                }
                if ((mCurrentTargetOffsetTop > 0 ? overscrollTop += mCurrentTargetOffsetTop : overscrollTop) <= 0.0F) {
                    return false;
                }
//                if (mCurrentTargetOffsetTop > 0) overscrollTop += mCurrentTargetOffsetTop;
                moveSpinner(overscrollTop);
//                scrollTo(0, (int) (mYDown - mYMove));
                mYLastMove = mYMove;
                // ViewCompat.offsetTopAndBottom(this, -(int) scrollY);
                break;
            case MotionEvent.ACTION_UP:
//                mScroller.startScroll(0, getScrollY(), 0, mSpinnerOffsetEnd);
                float y = event.getY();
                overscrollTop = (y - mYDown-getScrollY()) * 0.5F;
//                finishSpinner(overscrollTop);
                Log.e(", getScrollY()", "=" + getScrollY()+"  overscrollTop="+overscrollTop);
                if (overscrollTop > mTotalDragDistance) {
                    setRefreshing(true);
                } else {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                    isRefreshing = false;
                    mCurrentTargetOffsetTop = 0;
                }
                invalidate();
//                awakenScrollBars(mScroller.getDuration());
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setRefreshing(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + mSpinnerOffsetEnd));

    }

    private void finishSpinner(float overscrollTop) {

    }


    private void moveSpinner(float overscrollTop) {

        //原始百分比
        float originalDragPercent = overscrollTop / this.mTotalDragDistance;
        //拖拽百分比
        float dragPercent = Math.min(1.0F, Math.abs(originalDragPercent));

        //位移距离减去总的移动距离
        float extraOS = Math.abs(overscrollTop) - this.mTotalDragDistance;

        //弹力抵消 =totalDragDistance
        float slingshotDist = mSpinnerOffsetEnd;

        //张力弹弓百分比
        // 当 extraOS值为小于等于0  说明移动距离小于等于总的拖拽距离（totalDragDistance）  此数为0
        // 当extraOs 为正值  说明移动距离大于总的拖拽距离（totalDragDistance） 此数为正数 也就是大于0 最大值为2  当extraOS 大于slingshotDist的2倍时，取最大2
        float tensionSlingshotPercent = Math.max(0.0F, Math.min(extraOS, slingshotDist * 2.0F) / slingshotDist);

        //紧张百分比
        // 当 tensionSlingshotPercent 为0 的时候 也就是说明移动距离小于等于总的拖拽距离（totalDragDistance） 此值为0
        // 当tensionSlingshotPercent大于0 说明移动距离大于总的拖拽距离（totalDragDistance）
        // 当tensionSlingshotPercent为最大值2的时候  tensionPercent最大值为1/2
        float tensionPercent = (float) ((double) (tensionSlingshotPercent / 4.0F) - Math.pow((double) (tensionSlingshotPercent / 4.0F), 2.0D)) * 2.0F;

        //额外移动距离 最小值为 0 最大值为 slingshotDist=mTotalDragDistance  当滑动距离不超过mTotalDragDistance 时 当前值为0
        // 当滑动距离超过 mTotalDragDistance 最大为 slingshotDist
        float extraMove = slingshotDist * tensionPercent * 2.0F;

        //得出最终 偏移量
        // 当移动距离小于 mTotalDragDistance 不做弹力抵消   只算 移动距离占总距离的百分比（最大为1） 乘以  dragPercent  extraMove值为0
        // 当移动距离大于mTotalDragDistance    移动距离 + 拖拽的最大距离extraMove（最大为slingshotDist）  extraMove值为0
        int targetY = (int) (slingshotDist * dragPercent + extraMove);
        Log.e("dragPercent=", dragPercent + "");
        setTargetOffsetTopAndBottom(targetY);
    }

    void setTargetOffsetTopAndBottom(int offset) {
//        把当前View提到画面图层的最上面来显示
//        this.bringToFront();
        scrollTo(0, -offset);
    }

}
