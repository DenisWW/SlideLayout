package com.rainbell.www.slidelayout.customize;

import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.v4.view.ViewPager;

public class CusRelativeManager {

    private ViewPager viewPager;
    private CusRelativeLayout cusRelativeLayout;
    private ClickTitleViewListener listener;

    private CusRelativeManager(Builder builder) {
        this.viewPager = builder.viewPager;
        this.cusRelativeLayout = builder.cusRelativeLayout;
        this.listener = builder.listener;
        addScrollListener();
    }

    private void addScrollListener() {
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    if (cusRelativeLayout != null) cusRelativeLayout.setViewOffset(i, v,i1,cusRelativeLayout.getId());

                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
        if (cusRelativeLayout != null) cusRelativeLayout.setListener(listener);
    }


    public static class Builder {
        private CusRelativeLayout cusRelativeLayout;
        private ClickTitleViewListener listener;
        private ViewPager viewPager;

        public Builder(CusRelativeLayout cusRelativeLayout, ViewPager viewPager) {
            this.cusRelativeLayout = cusRelativeLayout;
            this.viewPager = viewPager;
        }

        public Builder addCusRelativeLayout(CusRelativeLayout cusRelativeLayout) {
            this.cusRelativeLayout = cusRelativeLayout;
            return this;
        }

        public Builder addClicTitleViewListener(ClickTitleViewListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder addViewPager(ViewPager viewPager) {
            this.viewPager = viewPager;
            return this;
        }

        public CusRelativeManager build() {
            return new CusRelativeManager(this);
        }
    }

    public static class CusRelativeLayoutBuilder {
        @Dimension
        public float
                backgroundHeight,
                backgroundWidth,
                backgroundMargin,
                backgroundMarginLeft,
                backgroundMarginTop,
                backgroundMarginRight,
                backgroundMarginBottom;
        @ColorInt
        public int startColor,
                endColor,
                textSelectorColor,
                textUnSelectorColor,
                textSelectorColorStart,
                textSelectorColorEnd,
                textUnSelectorColorStart,
                textUnSelectorColorEnd;
        @Dimension
        public float childTitlePaddingBottom,
                childTitlePaddingLeft,
                childTitlePaddingRight,
                childTitlePaddingTop,
                childTitlePadding;
        @Dimension
        public float childTitleMarginBottom,
                childTitleMarginLeft,
                childTitleMarginRight,
                childTitleMarginTop,
                childTitleMargin;
        @Dimension
        public float radiusBackground,
                radiusBackgroundLeftTop,
                radiusBackgroundRightTop,
                radiusBackgroundLeftBottom,
                radiusBackgroundRightBottom;
        public String text, regex;
        public int direction, defaultSelectorPosition;
        public float textSize;
        public boolean radiusBackgroundHalf, backgroundBottom;

        public CusRelativeLayoutBuilder() {

        }

        public CusRelativeLayoutBuilder setBackgroundHeight(float backgroundHeight) {
            this.backgroundHeight = backgroundHeight;
            return this;
        }

        public CusRelativeLayoutBuilder setBackgroundWidth(float backgroundWidth) {
            this.backgroundWidth = backgroundWidth;
            return this;
        }

        public CusRelativeLayoutBuilder setBackgroundMargin(float backgroundMargin) {
            this.backgroundMargin = backgroundMargin;
            return this;
        }

        public CusRelativeLayoutBuilder setBackgroundMarginLeft(float backgroundMarginLeft) {
            this.backgroundMarginLeft = backgroundMarginLeft;
            return this;
        }

        public CusRelativeLayoutBuilder setBackgroundMarginTop(float backgroundMarginTop) {
            this.backgroundMarginTop = backgroundMarginTop;
            return this;
        }

        public CusRelativeLayoutBuilder setBackgroundMarginRight(float backgroundMarginRight) {
            this.backgroundMarginRight = backgroundMarginRight;
            return this;
        }

        public CusRelativeLayoutBuilder setBackgroundMarginBottom(float backgroundMarginBottom) {
            this.backgroundMarginBottom = backgroundMarginBottom;
            return this;
        }

        public CusRelativeLayoutBuilder setDirection(int direction) {
            this.direction = direction;
            return this;
        }

        public CusRelativeLayoutBuilder setRadiusBackgroundHalf(boolean radiusBackgroundHalf) {
            this.radiusBackgroundHalf = radiusBackgroundHalf;
            return this;
        }

        public CusRelativeLayoutBuilder setBackgroundBottom(boolean backgroundBottom) {
            this.backgroundBottom = backgroundBottom;
            return this;
        }

        public CusRelativeLayoutBuilder setStartColor(int startColor) {
            this.startColor = startColor;
            return this;
        }

        public CusRelativeLayoutBuilder setEndColor(int endColor) {
            this.endColor = endColor;
            return this;
        }

        public CusRelativeLayoutBuilder setTextSelectorColor(int textSelectorColor) {
            this.textSelectorColor = textSelectorColor;
            return this;
        }

        public CusRelativeLayoutBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public CusRelativeLayoutBuilder setRegex(String regex) {
            this.regex = regex;
            return this;
        }

        public CusRelativeLayoutBuilder setTextUnSelectorColor(int textUnSelectorColor) {
            this.textUnSelectorColor = textUnSelectorColor;
            return this;
        }

        public CusRelativeLayoutBuilder setTextSelectorColorStart(int textSelectorColorStart) {
            this.textSelectorColorStart = textSelectorColorStart;
            return this;
        }

        public CusRelativeLayoutBuilder setTextSelectorColorEnd(int textSelectorColorEnd) {
            this.textSelectorColorEnd = textSelectorColorEnd;
            return this;
        }

        public CusRelativeLayoutBuilder setTextUnSelectorColorStart(int textUnSelectorColorStart) {
            this.textUnSelectorColorStart = textUnSelectorColorStart;
            return this;
        }

        public CusRelativeLayoutBuilder setTextUnSelectorColorEnd(int textUnSelectorColorEnd) {
            this.textUnSelectorColorEnd = textUnSelectorColorEnd;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitlePaddingBottom(float childTitlePaddingBottom) {
            this.childTitlePaddingBottom = childTitlePaddingBottom;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitlePaddingLeft(float childTitlePaddingLeft) {
            this.childTitlePaddingLeft = childTitlePaddingLeft;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitlePaddingRight(float childTitlePaddingRight) {
            this.childTitlePaddingRight = childTitlePaddingRight;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitlePaddingTop(float childTitlePaddingTop) {
            this.childTitlePaddingTop = childTitlePaddingTop;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitlePadding(float childTitlePadding) {
            this.childTitlePadding = childTitlePadding;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitleMarginBottom(float childTitleMarginBottom) {
            this.childTitleMarginBottom = childTitleMarginBottom;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitleMarginLeft(float childTitleMarginLeft) {
            this.childTitleMarginLeft = childTitleMarginLeft;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitleMarginRight(float childTitleMarginRight) {
            this.childTitleMarginRight = childTitleMarginRight;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitleMarginTop(float childTitleMarginTop) {
            this.childTitleMarginTop = childTitleMarginTop;
            return this;
        }

        public CusRelativeLayoutBuilder setChildTitleMargin(float childTitleMargin) {
            this.childTitleMargin = childTitleMargin;
            return this;
        }

        public CusRelativeLayoutBuilder setRadiusBackground(float radiusBackground) {
            this.radiusBackground = radiusBackground;
            return this;
        }

        public CusRelativeLayoutBuilder setRadiusBackgroundLeftTop(float radiusBackgroundLeftTop) {
            this.radiusBackgroundLeftTop = radiusBackgroundLeftTop;
            return this;
        }

        public CusRelativeLayoutBuilder setRadiusBackgroundRightTop(float radiusBackgroundRightTop) {
            this.radiusBackgroundRightTop = radiusBackgroundRightTop;
            return this;
        }

        public CusRelativeLayoutBuilder setRadiusBackgroundLeftBottom(float radiusBackgroundLeftBottom) {
            this.radiusBackgroundLeftBottom = radiusBackgroundLeftBottom;
            return this;
        }

        public CusRelativeLayoutBuilder setRadiusBackgroundRightBottom(float radiusBackgroundRightBottom) {
            this.radiusBackgroundRightBottom = radiusBackgroundRightBottom;
            return this;
        }

        public CusRelativeLayoutBuilder setDirection(CusRelativeLayout.Direction direction) {
            if (direction == CusRelativeLayout.Direction.horizontal)
                this.direction = CusRelativeLayout.horizontal;
            if (direction == CusRelativeLayout.Direction.vertical)
                this.direction = CusRelativeLayout.vertical;
            if (direction == CusRelativeLayout.Direction.leftTopDown)
                this.direction = CusRelativeLayout.leftTopDown;
            if (direction == CusRelativeLayout.Direction.leftBottomUp)
                this.direction = CusRelativeLayout.leftBottomUp;
            return this;
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
        }

        public void setDefaultSelectorPosition(int defaultSelectorPosition) {
            this.defaultSelectorPosition = defaultSelectorPosition;
        }

        public void build(CusRelativeLayout cusLayout) {
            cusLayout.build(this);
        }
    }

}
