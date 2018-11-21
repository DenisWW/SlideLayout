package com.rainbell.www.slidelayout.customize;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainbell.www.slidelayout.R;


public class CusRelativeLayout extends RelativeLayout {
    public static final int horizontal = 1;  //横向
    public static final int vertical = horizontal + 1;   // 纵向
    public static final int leftTopDown = vertical + 1;
    public static final int leftBottomUp = leftTopDown + 1;

    private String text, regex;
    @Dimension
    private float childTitlePaddingBottom,
            childTitlePaddingLeft,
            childTitlePaddingRight,
            childTitlePaddingTop,
            childTitlePadding;
    @Dimension
    private float childTitleMarginBottom,
            childTitleMarginLeft,
            childTitleMarginRight,
            childTitleMarginTop,
            childTitleMargin;
    @Dimension
    private float radiusBackground,
            radiusBackgroundLeftTop,
            radiusBackgroundRightTop,
            radiusBackgroundLeftBottom,
            radiusBackgroundRightBottom;

    @Dimension
    private float
            backgroundHeight,
            backgroundWidth,
            backgroundMargin,
            backgroundMarginLeft,
            backgroundMarginTop,
            backgroundMarginRight,
            backgroundMarginBottom;

    private boolean radiusBackgroundHalf, backgroundBottom;
    @ColorInt
    private int startColor,
            endColor,
            textSelectorColor,
            textUnSelectorColor,
            textSelectorColorStart,
            textSelectorColorEnd,
            textUnSelectorColorStart,
            textUnSelectorColorEnd;

    private Paint paint;
    private BackgroupTextView backgroundView;
    private Context context;
    private LayoutParams layoutParams, currentLayoutParams, nextLayoutParams;

    private float textSize, start0ffset, differenceOffset;
    private String[] mStrings = new String[]{};
    private TextView[] titleTvs;
    @IdRes
    int[] mInts;
    private int measure;
    private TitleTextView currentView, nextView;
    private ClickTitleViewListener listener;
    private int direction, defaultSelectorPosition;

    public CusRelativeLayout(Context context) {
        this(context, null);
    }

    public CusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CusRelativeLayout);
        text = typedArray.getString(R.styleable.CusRelativeLayout_textString);
        defaultSelectorPosition = typedArray.getInt(R.styleable.CusRelativeLayout_defaultSelectorPosition, 0);
        regex = typedArray.getString(R.styleable.CusRelativeLayout_textRegex);
        direction = typedArray.getInt(R.styleable.CusRelativeLayout_backgroundGradientColorDirection, 2);
        textSize = typedArray.getDimension(R.styleable.CusRelativeLayout_titleTextSize, 14.0f);


        getbackgroundAttrs(typedArray);
        getRadiusAttrs(typedArray);
        getChildPaddingAttrs(typedArray);
        getChildMarginAttrs(typedArray);
        getColorAttrs(typedArray);


        init();
        createdTitleViews();
        createdBackgroundView();
        typedArray.recycle();
    }

    private void getbackgroundAttrs(TypedArray typedArray) {
        backgroundBottom = typedArray.getBoolean(R.styleable.CusRelativeLayout_backgroundBottom, false);
        backgroundMargin = typedArray.getDimension(R.styleable.CusRelativeLayout_backgroundMargin, 0);
        backgroundHeight = typedArray.getDimension(R.styleable.CusRelativeLayout_backgroundHeight, 0);
        backgroundWidth = typedArray.getDimension(R.styleable.CusRelativeLayout_backgroundWidth, 0.0f);
        if (backgroundMargin == 0) {
            backgroundMarginLeft = typedArray.getDimension(R.styleable.CusRelativeLayout_backgroundMarginLeft, 0);
            backgroundMarginTop = typedArray.getDimension(R.styleable.CusRelativeLayout_backgroundMarginTop, 0);
            backgroundMarginRight = typedArray.getDimension(R.styleable.CusRelativeLayout_backgroundMarginRight, 0);
            backgroundMarginBottom = typedArray.getDimension(R.styleable.CusRelativeLayout_backgroundMarginBottom, 0);
        } else
            backgroundMarginLeft = backgroundMarginTop = backgroundMarginRight = backgroundMarginBottom = backgroundMargin;

    }

    private void getColorAttrs(TypedArray typedArray) {
        startColor = typedArray.getColor(R.styleable.CusRelativeLayout_backgroundGradientColorStart, ContextCompat.getColor(context, R.color.start));
        endColor = typedArray.getColor(R.styleable.CusRelativeLayout_backgroundGradientColorEnd, ContextCompat.getColor(context, R.color.end));
        textUnSelectorColor = typedArray.getColor(R.styleable.CusRelativeLayout_textUnSelectorColor, 0);
        textSelectorColor = typedArray.getColor(R.styleable.CusRelativeLayout_textSelectorColor, 0);
        if (textSelectorColor == 0) {
            textSelectorColorStart = typedArray.getColor(R.styleable.CusRelativeLayout_textSelectorColorStart, 0);
            textSelectorColorEnd = typedArray.getColor(R.styleable.CusRelativeLayout_textSelectorColorEnd, 0);
        }
        if (textUnSelectorColor == 0) {
            textUnSelectorColorStart = typedArray.getColor(R.styleable.CusRelativeLayout_textUnSelectorColorStart, 0);
            textUnSelectorColorEnd = typedArray.getColor(R.styleable.CusRelativeLayout_textUnSelectorColorEnd, 0);
        }

    }


    private void createdBackgroundView() {
        backgroundView = new BackgroupTextView(context);
        if (radiusBackgroundHalf || backgroundBottom) backgroundView.setRadiusValues(new float[]{});
        else backgroundView.setRadiusValues(
                new float[]{
                        radiusBackgroundLeftTop, radiusBackgroundLeftTop,
                        radiusBackgroundRightTop, radiusBackgroundRightTop,
                        radiusBackgroundRightBottom, radiusBackgroundRightBottom,
                        radiusBackgroundLeftBottom, radiusBackgroundLeftBottom
                });
        backgroundView.setDirection(direction);
        backgroundView.setStartAndEndColor(startColor, endColor);
        this.addView(backgroundView, 0);

        if (backgroundBottom) {
            LayoutParams lP = (LayoutParams) backgroundView.getLayoutParams();
            assert lP != null;
            if (getChildAt(1) != null) lP.addRule(BELOW, getChildAt(1).getId());
            lP.height = (backgroundHeight > 0 ? (int) backgroundHeight : 8);
            backgroundView.setLayoutParams(lP);
        }
    }

    private void createdTitleViews() {

        if (!TextUtils.isEmpty(text)) {
            TitleTextView titleTv;
            mStrings = text.split(regex);
            LayoutParams params;
            titleTvs = new TextView[mStrings.length];
            mInts = new int[mStrings.length];
            for (int i = 0; i < mStrings.length; i++) {
                titleTv = new TitleTextView(context);
                createdSetting(titleTv);
                titleTv.setText(mStrings[i]);
                titleTvs[i] = titleTv;
                mInts[i] = i + 10000;
                titleTv.setId(mInts[i]);
                if (paint == null) paint = titleTv.getPaint();
                this.addView(titleTv);
                params = (LayoutParams) titleTv.getLayoutParams();
                if (i > 0 && i < mStrings.length) {
                    params.addRule(RelativeLayout.RIGHT_OF, titleTvs[i - 1].getId());
                }
                createdMarginSetting(params);
                titleTv.setLayoutParams(params);
                setTitleOnClickListener(i, titleTv);
            }
        }
    }

    private void setTitleOnClickListener(int pos, TitleTextView myTextView) {
        myTextView.setOnClickListener((view) -> {
            if (listener != null)
                listener.onClickTitleListener(pos, myTextView.getText().toString());
        });
    }

    public void setDefaultSelectorPosition(int defaultSelectorPosition) {
        this.defaultSelectorPosition = defaultSelectorPosition;
//        postInvalidate();
//        invalidate();
    }

    public void setListener(ClickTitleViewListener listener) {
        this.listener = listener;
    }

    private void createdMarginSetting(LayoutParams params) {
        if (childTitleMargin > 0 ||
                childTitlePaddingLeft > 0 ||
                childTitlePaddingTop > 0 ||
                childTitlePaddingRight > 0 ||
                childTitlePaddingBottom > 0)
            params.setMargins((int) childTitleMarginLeft, (int) childTitleMarginTop, (int) childTitleMarginRight, (int) childTitleMarginBottom);

    }

    /**
     * 设置titleview属性
     *
     * @param myTextView
     */
    private void createdSetting(TitleTextView myTextView) {
        myTextView.setTextUnSelectorColor(
                textUnSelectorColor,
                textSelectorColor,
                textSelectorColorStart,
                textSelectorColorEnd,
                textUnSelectorColorStart,
                textUnSelectorColorEnd,
                direction);
        myTextView.setTextSizeDimension(textSize);
        if (childTitlePadding > 0 ||
                childTitlePaddingLeft > 0 ||
                childTitlePaddingTop > 0 ||
                childTitlePaddingRight > 0 ||
                childTitlePaddingBottom > 0)
            myTextView.setPadding((int) childTitlePaddingLeft, (int) childTitlePaddingTop, (int) childTitlePaddingRight, (int) childTitlePaddingBottom);


        if (backgroundBottom) {
            myTextView.setRadiusValues(new float[]{
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0
            });
        } else {
            if (radiusBackgroundHalf) myTextView.setRadiusValues(new float[]{});
            else myTextView.setRadiusValues(new float[]{
                    radiusBackgroundLeftTop, radiusBackgroundLeftTop,
                    radiusBackgroundRightTop, radiusBackgroundRightTop,
                    radiusBackgroundRightBottom, radiusBackgroundRightBottom,
                    radiusBackgroundLeftBottom, radiusBackgroundLeftBottom
            });
        }

    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void init() {
        if (TextUtils.isEmpty(regex)) regex = ",";
//        paint = new Paint();
//        setWillNotDraw(false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (measure == 0) {
//            for (int i = 0; i < getChildCount(); i++) {
//                View view = getChildAt(i);
//                if (view instanceof MyTextView)
//                    measure += paint.measureText(((MyTextView) view).getText().toString());
//            }
//        }
//        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(fontMetrics.bottom - fontMetrics.top + getPaddingBottom() + getPaddingTop(), MeasureSpec.EXACTLY);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(backgroundView.getMeasuredHeight(), MeasureSpec.UNSPECIFIED);
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(measure, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setViewOffset(int pos, float offset) {
        if (offset > 0) {
            if (pos >= 0 && pos < getChildCount() - 1) {
                currentView = (TitleTextView) getChildAt(pos + 1);
                nextView = (TitleTextView) getChildAt(pos + 2);
                if (nextView == null) return;
                currentLayoutParams = (LayoutParams) currentView.getLayoutParams();
                nextLayoutParams = (LayoutParams) nextView.getLayoutParams();
                start0ffset = (currentLayoutParams.leftMargin + currentView.getWidth() + currentLayoutParams.rightMargin) * offset;
                differenceOffset = (nextView.getMeasuredWidth() - currentView.getMeasuredWidth());
                if (differenceOffset != 0)
                    layoutParams.width = (int) (currentView.getMeasuredWidth()
                            - (backgroundBottom && backgroundMarginLeft > 0 ? backgroundMarginLeft : 0)
                            - (backgroundBottom && backgroundMarginRight > 0 ? backgroundMarginRight : 0)
                            + (differenceOffset * offset));

                layoutParams.leftMargin = (int) (start0ffset + currentView.getLeft() + (backgroundBottom && backgroundMarginLeft > 0 ? backgroundMarginLeft : 0));
                backgroundView.setLayoutParams(layoutParams);
                currentView.setStart(start0ffset);
                nextView.setStart(-(nextView.getMeasuredWidth() - (layoutParams.width - (currentView.getMeasuredWidth() - start0ffset) - (currentLayoutParams.rightMargin + nextLayoutParams.leftMargin))));
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            TitleTextView defaultView = null;
            for (int i = 0; i < getChildCount(); i++) {
                Log.e("CusRelativeLayout      " + this.getId(), i + getChildAt(i).toString() + "=" + getChildAt(i).getRight());
                if (getChildAt(i) instanceof TitleTextView && (defaultSelectorPosition + 1) == i) {
                    defaultView = (TitleTextView) getChildAt(i);
                    defaultView.setStart(0);
                    break;
                }
            }
            if (defaultView != null && layoutParams == null) {
                if (backgroundBottom && ((backgroundWidth != 0 || backgroundHeight != 0)))
                    layoutParams = new LayoutParams((int) ((backgroundWidth > 0 ? backgroundWidth : defaultView.getMeasuredWidth())
                            - (backgroundMarginLeft > 0 ? backgroundMarginLeft : 0)
                            - (backgroundMarginRight > 0 ? backgroundMarginRight : 0)),
                            (int) (backgroundHeight > 0 ? backgroundHeight : defaultView.getMeasuredHeight()));
                else
                    layoutParams = new LayoutParams(defaultView.getMeasuredWidth()
                            , defaultView.getMeasuredHeight()
                    );

//                layoutParams = new LayoutParams((int) (defaultView.getMeasuredWidth()
//                        - (backgroundMarginLeft > 0 ? backgroundMarginLeft : 0)
//                        - (backgroundMarginRight > 0 ? backgroundMarginRight : 0))
//                        , (int) (defaultView.getMeasuredHeight()
//                        - (backgroundMarginTop > 0 ? backgroundMarginTop : 0)
//                        - (backgroundMarginBottom > 0 ? backgroundMarginBottom : 0))
//                );
                if (backgroundBottom)
                    layoutParams.addRule(BELOW, defaultView.getId());
                if (backgroundBottom && (backgroundMarginLeft > 0
                        || backgroundMarginTop > 0
                        || backgroundMarginRight > 0
                        || backgroundMarginBottom > 0
                )) {
                    LayoutParams lp = (LayoutParams) defaultView.getLayoutParams();
                    int leftTotal;
                    if (defaultSelectorPosition > 0)
                        leftTotal = defaultView.getLeft() + (int) backgroundMarginLeft;
                    else leftTotal = lp.leftMargin + (int) backgroundMarginLeft;
                    layoutParams.setMargins(
                            leftTotal,
                            (int) backgroundMarginTop,
                            (int) backgroundMarginRight,
                            (int) backgroundMarginBottom);
//                    layoutParams.setMargins(
//                            leftTotal,
//                            lp.topMargin + (int) backgroundMarginTop,
//                            lp.rightMargin + (int) backgroundMarginRight,
//                            lp.bottomMargin + (int) backgroundMarginBottom);

                } else {
                    LayoutParams lp = (LayoutParams) defaultView.getLayoutParams();
                    layoutParams.setMargins(
                            lp.leftMargin,
                            lp.topMargin,
                            lp.rightMargin,
                            lp.bottomMargin);
                }

                backgroundView.setLayoutParams(layoutParams);
                layoutParams = (LayoutParams) backgroundView.getLayoutParams();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("onSizeChanged   ", this.getId() + "w=" + w + "  h=" + h);
    }

    private void getChildMarginAttrs(TypedArray typedArray) {
        childTitleMargin = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitleMargin, 0.0f);
        if (childTitleMargin == 0) {
            childTitleMarginBottom = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitleMarginBottom, 0.0f);
            childTitleMarginLeft = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitleMarginLeft, 0.0f);
            childTitleMarginTop = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitleMarginTop, 0.0f);
            childTitleMarginRight = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitleMarginRight, 0.0f);
        } else {
            childTitleMarginBottom = childTitleMarginLeft = childTitleMarginTop = childTitleMarginRight = childTitleMargin;
        }

    }

    private void getChildPaddingAttrs(TypedArray typedArray) {
        childTitlePadding = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitlePadding, 0.0f);
        if (childTitlePadding == 0) {
            childTitlePaddingBottom = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitlePaddingBottom, 0.0f);
            childTitlePaddingLeft = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitlePaddingLeft, 0.0f);
            childTitlePaddingTop = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitlePaddingTop, 0.0f);
            childTitlePaddingRight = typedArray.getDimension(R.styleable.CusRelativeLayout_childTitlePaddingRight, 0.0f);
        } else {
            childTitlePaddingBottom = childTitlePaddingLeft = childTitlePaddingTop = childTitlePaddingRight = childTitlePadding;
        }
    }

    private void getRadiusAttrs(TypedArray typedArray) {
        radiusBackgroundHalf = typedArray.getBoolean(R.styleable.CusRelativeLayout_radiusBackgroundHalf, false);
        if (!radiusBackgroundHalf) {
            radiusBackground = typedArray.getDimension(R.styleable.CusRelativeLayout_radiusBackground, 0.0f);
            if (radiusBackground == 0) {
                radiusBackgroundRightTop = typedArray.getDimension(R.styleable.CusRelativeLayout_radiusBackgroundRightTop, 0.0f);
                radiusBackgroundLeftTop = typedArray.getDimension(R.styleable.CusRelativeLayout_radiusBackgroundLeftTop, 0.0f);
                radiusBackgroundLeftBottom = typedArray.getDimension(R.styleable.CusRelativeLayout_radiusBackgroundLeftBottom, 0.0f);
                radiusBackgroundRightBottom = typedArray.getDimension(R.styleable.CusRelativeLayout_radiusBackgroundRightBottom, 0.0f);
            } else
                radiusBackgroundRightTop = radiusBackgroundLeftTop = radiusBackgroundLeftBottom = radiusBackgroundRightBottom = radiusBackground;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        Log.e("onDraw", "=======" + text);
//        paint.setColor(ContextCompat.getColor(getContext(), R.color.end));
//        paint.setTextSize(20.0f);
//        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
//        int baseline = getHeight() - getPaddingBottom() - fontMetrics.bottom;
//        canvas.drawText(text, 0, baseline, paint);
//        canvas.save();
//        invalidate();

    }

    public enum Direction {
        horizontal, vertical, leftTopDown, leftBottomUp,
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }


    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    public void build(CusRelativeManager.CusRelativeLayoutBuilder cusRelativeLayoutBuilder) {
        if (!TextUtils.isEmpty(cusRelativeLayoutBuilder.text))
            this.text = cusRelativeLayoutBuilder.text;
        if (!TextUtils.isEmpty(cusRelativeLayoutBuilder.regex))
            this.regex = cusRelativeLayoutBuilder.regex;
        if (cusRelativeLayoutBuilder.childTitleMargin > 0)
            this.childTitleMargin = cusRelativeLayoutBuilder.childTitleMargin;
        if (cusRelativeLayoutBuilder.startColor > 0)
            this.startColor = cusRelativeLayoutBuilder.startColor;
        if (cusRelativeLayoutBuilder.endColor > 0)
            this.endColor = cusRelativeLayoutBuilder.endColor;

        if (cusRelativeLayoutBuilder.textSelectorColor > 0)
            this.textSelectorColor = cusRelativeLayoutBuilder.textSelectorColor;
        if (cusRelativeLayoutBuilder.textUnSelectorColor > 0)
            this.textUnSelectorColor = cusRelativeLayoutBuilder.textUnSelectorColor;

        if (cusRelativeLayoutBuilder.textSelectorColorStart > 0)
            this.textSelectorColorStart = cusRelativeLayoutBuilder.textSelectorColorStart;
        if (cusRelativeLayoutBuilder.textSelectorColorEnd > 0)
            this.textSelectorColorEnd = cusRelativeLayoutBuilder.textSelectorColorEnd;

        if (cusRelativeLayoutBuilder.textUnSelectorColorStart > 0)
            this.textUnSelectorColorStart = cusRelativeLayoutBuilder.textUnSelectorColorStart;
        if (cusRelativeLayoutBuilder.textUnSelectorColorEnd > 0)
            this.textUnSelectorColorEnd = cusRelativeLayoutBuilder.textUnSelectorColorEnd;


        if (cusRelativeLayoutBuilder.childTitlePaddingBottom > 0)
            this.childTitlePaddingBottom = cusRelativeLayoutBuilder.childTitlePaddingBottom;
        if (cusRelativeLayoutBuilder.childTitlePaddingLeft > 0)
            this.childTitlePaddingLeft = cusRelativeLayoutBuilder.childTitlePaddingLeft;
        if (cusRelativeLayoutBuilder.childTitlePaddingRight > 0)
            this.childTitlePaddingRight = cusRelativeLayoutBuilder.childTitlePaddingRight;
        if (cusRelativeLayoutBuilder.childTitlePaddingTop > 0)
            this.childTitlePaddingTop = cusRelativeLayoutBuilder.childTitlePaddingTop;
        if (cusRelativeLayoutBuilder.childTitlePadding > 0)
            this.childTitlePadding = cusRelativeLayoutBuilder.childTitlePadding;

        if (cusRelativeLayoutBuilder.childTitleMarginBottom > 0)
            this.childTitleMarginBottom = cusRelativeLayoutBuilder.childTitleMarginBottom;
        if (cusRelativeLayoutBuilder.childTitleMarginLeft > 0)
            this.childTitleMarginLeft = cusRelativeLayoutBuilder.childTitleMarginLeft;
        if (cusRelativeLayoutBuilder.childTitleMarginRight > 0)
            this.childTitleMarginRight = cusRelativeLayoutBuilder.childTitleMarginRight;
        if (cusRelativeLayoutBuilder.childTitleMarginTop > 0)
            this.childTitleMarginTop = cusRelativeLayoutBuilder.childTitleMarginTop;
        if (cusRelativeLayoutBuilder.childTitleMargin > 0)
            this.childTitleMargin = cusRelativeLayoutBuilder.childTitleMargin;

        if (cusRelativeLayoutBuilder.radiusBackground > 0)
            this.radiusBackground = cusRelativeLayoutBuilder.radiusBackground;
        if (cusRelativeLayoutBuilder.radiusBackgroundLeftTop > 0)
            this.radiusBackgroundLeftTop = cusRelativeLayoutBuilder.radiusBackgroundLeftTop;
        if (cusRelativeLayoutBuilder.radiusBackgroundRightTop > 0)
            this.radiusBackgroundRightTop = cusRelativeLayoutBuilder.radiusBackgroundRightTop;
        if (cusRelativeLayoutBuilder.radiusBackgroundLeftBottom > 0)
            this.radiusBackgroundLeftBottom = cusRelativeLayoutBuilder.radiusBackgroundLeftBottom;
        if (cusRelativeLayoutBuilder.radiusBackgroundRightBottom > 0)
            this.radiusBackgroundRightBottom = cusRelativeLayoutBuilder.radiusBackgroundRightBottom;
        if (cusRelativeLayoutBuilder.direction > 0)
            this.direction = cusRelativeLayoutBuilder.direction;
        if (cusRelativeLayoutBuilder.textSize > 0)
            this.textSize = cusRelativeLayoutBuilder.textSize;
        if (cusRelativeLayoutBuilder.defaultSelectorPosition > 0)
            this.defaultSelectorPosition = cusRelativeLayoutBuilder.defaultSelectorPosition;

        if (cusRelativeLayoutBuilder.backgroundHeight > 0)
            this.backgroundHeight = cusRelativeLayoutBuilder.backgroundHeight;
        if (cusRelativeLayoutBuilder.backgroundWidth > 0)
            this.backgroundWidth = cusRelativeLayoutBuilder.backgroundWidth;
        if (cusRelativeLayoutBuilder.backgroundMargin > 0)
            this.backgroundMargin = cusRelativeLayoutBuilder.backgroundMargin;
        if (cusRelativeLayoutBuilder.backgroundMarginLeft > 0)
            this.backgroundMarginLeft = cusRelativeLayoutBuilder.backgroundMarginLeft;
        if (cusRelativeLayoutBuilder.backgroundMarginTop > 0)
            this.backgroundMarginTop = cusRelativeLayoutBuilder.backgroundMarginTop;
        if (cusRelativeLayoutBuilder.backgroundMarginRight > 0)
            this.backgroundMarginRight = cusRelativeLayoutBuilder.backgroundMarginRight;
        if (cusRelativeLayoutBuilder.backgroundMarginBottom > 0)
            this.backgroundMarginBottom = cusRelativeLayoutBuilder.backgroundMarginBottom;
        this.radiusBackgroundHalf = cusRelativeLayoutBuilder.radiusBackgroundHalf;
        this.backgroundBottom = cusRelativeLayoutBuilder.backgroundBottom;


        removeAllViews();
        createdTitleViews();
        createdBackgroundView();
//        this.postInvalidate();
    }

}
