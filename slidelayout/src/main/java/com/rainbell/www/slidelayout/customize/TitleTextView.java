package com.rainbell.www.slidelayout.customize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.text.DynamicLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

public class TitleTextView extends AppCompatTextView {
    private Context context;
    private TextPaint paint;
    private String textString;
    private int bg_W, bg_Left, bg_Y, width, height, viewWith, direction;
    private int padLeft;
    @ColorInt
    private int textSelectorColor,
            textUnSelectorColor,
            textSelectorColorStart,
            textSelectorColorEnd,
            textUnSelectorColorStart,
            textUnSelectorColorEnd;
    private float[] radii = new float[2];
    private LinearGradient linearGradient;
    private float start = 0, end;

    public TitleTextView(Context context) {
        this(context, null);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * 设置选中字体颜色，未选中字体颜色
     */
    public void setTextUnSelectorColor(
            int textUnSelectorColor,
            int textSelectorColor,
            int textSelectorColorStart,
            int textSelectorColorEnd,
            int textUnSelectorColorStart,
            int textUnSelectorColorEnd,
            int direction) {
        this.textUnSelectorColor = textUnSelectorColor;
        this.textSelectorColor = textSelectorColor;
        this.textSelectorColorStart = textSelectorColorStart;
        this.textSelectorColorEnd = textSelectorColorEnd;
        this.textUnSelectorColorStart = textUnSelectorColorStart;
        this.textUnSelectorColorEnd = textUnSelectorColorEnd;
        this.direction = direction;
    }


    public void setTextSizeDimension(float size) {
        if (paint != null) paint.setTextSize(size);
    }

    private void init() {
        if (paint == null) {
            paint = this.getPaint();
            paint.setDither(true);
            paint.setAntiAlias(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!TextUtils.isEmpty(textString)) viewWith = (int) paint.measureText(textString);
        if (viewWith != 0)
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewWith + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
//        Log.e("fontMetrics.bottom=", fontMetrics.bottom + "    fontMetrics.top= " + fontMetrics.top+ "    fontMetrics.ascent= " + fontMetrics.ascent+ "    fontMetrics.descent= " + fontMetrics.descent+ "    fontMetrics.leading= " + fontMetrics.leading);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(fontMetrics.bottom - fontMetrics.top + getPaddingBottom() + getPaddingTop(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //绘制渐变
//        Log.e("onDraw=", height + "   width==" + width );

        if (direction != 0 && textUnSelectorColorStart != 0 && textUnSelectorColorEnd != 0) {
            setDirection(false);
            paint.setShader(linearGradient);
        } else if (textUnSelectorColor != 0) paint.setColor(textUnSelectorColor);
        else paint.setColor(Color.GREEN);

        Path path = new Path();//背景path
        RectF rectF = new RectF(start, 0.0f, start + getWidth(), height);
        if (radii.length < 8)
            radii = new float[]{height / 2, height / 2, height / 2, height / 2, height / 2, height / 2, height / 2, height / 2};
        path.addRoundRect(rectF, radii, Path.Direction.CW);
//        canvas.drawPath(path, paint);//画背景
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = height - getPaddingBottom() - fontMetrics.bottom;
        canvas.drawText(textString, getPaddingLeft(), baseline, paint); //先画一遍内容
        canvas.save();
        drawText(rectF, canvas, path, baseline);//再画交汇的内容
        canvas.restore();
    }

    private void drawText(RectF rectF, Canvas canvas, Path path, float baseline) {
        if (direction != 0 && textSelectorColorStart != 0 && textSelectorColorEnd != 0) {
            setDirection(true);
            paint.setShader(linearGradient);
        } else if (textSelectorColor != 0) paint.setColor(textSelectorColor);
        else paint.setColor(Color.WHITE);

        paint.setShader(null);
//        Rect rect = new Rect();
//        rectF.roundOut(rect);
        canvas.clipPath(path);
//        paint.getTextBounds(textString, 0, textString.length(), rect);
//        float dx = getWidth() / 2 - rect.width() / 2;
//        float dx = getWidth() / 2 - rectF.width() / 2;
        canvas.drawText(textString, getPaddingLeft(), baseline, paint);
    }

    public void setStart(float start) {
        this.start = start;
        invalidate();
    }

    public void setRadiusValues(float[] radii) {
        if (radii.length >= 8)
            this.radii = radii;
    }


    public void setDirection(boolean selector) {
        if (direction == CusRelativeLayout.horizontal) {
            linearGradient = new LinearGradient(0, 0, width, 0,
                    new int[]{selector ? textSelectorColorStart : textUnSelectorColorStart, selector ? textSelectorColorEnd : textUnSelectorColorEnd},
                    null,
                    LinearGradient.TileMode.REPEAT);
        } else if (direction == CusRelativeLayout.vertical) {
            linearGradient = new LinearGradient(0, 0, 0, height,
                    new int[]{selector ? textSelectorColorStart : textUnSelectorColorStart, selector ? textSelectorColorEnd : textUnSelectorColorEnd},
                    null,
                    LinearGradient.TileMode.REPEAT);
        } else if (direction == CusRelativeLayout.leftTopDown) {
            linearGradient = new LinearGradient(0, 0, width, height,
                    new int[]{selector ? textSelectorColorStart : textUnSelectorColorStart, selector ? textSelectorColorEnd : textUnSelectorColorEnd},
                    null,
                    LinearGradient.TileMode.REPEAT);
        } else if (direction == CusRelativeLayout.leftBottomUp) {
            linearGradient = new LinearGradient(0, height, width, 0,
                    new int[]{selector ? textSelectorColorStart : textUnSelectorColorStart, selector ? textSelectorColorEnd : textUnSelectorColorEnd},
                    null,
                    LinearGradient.TileMode.REPEAT);
        } else {
            linearGradient = new LinearGradient(0, 0, 0, height,
                    new int[]{selector ? textSelectorColorStart : textUnSelectorColorStart, selector ? textSelectorColorEnd : textUnSelectorColorEnd},
                    null,
                    LinearGradient.TileMode.REPEAT);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        bg_W = width;
        textString = getText().toString();
//        Log.e("height=", height + "   width==" + width + "  ==" + getTextSize() + "    " + paint.getTextSize());
        if (start == 0) start = -getMeasuredWidth();

    }

    private DynamicLayout mDynamicLayout;

    public int getTextHeight() {
        //用来计算内容的大小
//        if (mDynamicLayout == null)
//            mDynamicLayout = new DynamicLayout(textString, paint, viewWith, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.0f, true);
//        return mDynamicLayout.getHeight();

        return 0;
    }

    public int getTextWith() {
//        if (mDynamicLayout == null)
//            mDynamicLayout = new DynamicLayout(textString, paint, viewHeight,
//                    Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.0f,
//                    true);
//        return mDynamicLayout.getWidth();
        return 0;
    }


    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


}
