package com.rainbell.www.slidelayout.customize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.DynamicLayout;
import android.util.AttributeSet;

import com.rainbell.www.slidelayout.R;

public class BackgroupTextView extends android.support.v7.widget.AppCompatTextView {
    private Context context;
    private Paint paint;
    private float[] radii = new float[2];
    private int bg_W, bg_Left, bg_Y, width, height, viewWith;
    private int direction;

    //绘制渐变
    private LinearGradient linearGradient;

    private int start, end;
    @ColorInt
    private int startColor, endColor;

    public BackgroupTextView(Context context) {
        this(context, null);
    }

    public BackgroupTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        if (paint == null) {
            paint = this.getPaint();
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        setDirection();
        paint.setShader(linearGradient);
        Path path = new Path();
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        if (radii.length < 8){
            radii = new float[]{height / 2, height / 2, height / 2, height / 2, height / 2, height / 2, height / 2, height / 2};
        }
         path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        canvas.save();
        canvas.restore();
    }

    public void setRadiusValues(float[] radii) {
        if (radii.length >= 8)
            this.radii = radii;
    }

    public void setDirection(int direction) {
        this.direction = direction;

    }

    public void setDirection() {
        if (direction == CusRelativeLayout.horizontal) {
            linearGradient = new LinearGradient(0, 0, width, 0,
                    new int[]{startColor, endColor},
                    null,
                    LinearGradient.TileMode.REPEAT);
        } else if (direction == CusRelativeLayout.vertical) {
            linearGradient = new LinearGradient(0, 0, 0, height,
                    new int[]{startColor, endColor},
                    null,
                    LinearGradient.TileMode.REPEAT);
        } else if (direction == CusRelativeLayout.leftTopDown) {
            linearGradient = new LinearGradient(0, 0, width, height,
                    new int[]{startColor, endColor},
                    null,
                    LinearGradient.TileMode.REPEAT);
        } else if (direction == CusRelativeLayout.leftBottomUp) {
            linearGradient = new LinearGradient(0, height, width, 0,
                    new int[]{startColor, endColor},
                    null,
                    LinearGradient.TileMode.REPEAT);
        } else {
            linearGradient = new LinearGradient(0, 0, 0, height,
                    new int[]{startColor, endColor},
                    null,
                    LinearGradient.TileMode.REPEAT);
        }
    }

    public void setStartAndEndColor(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public void setStart(int start) {
        this.start = start;
        invalidate();
    }


    @Override
    public CharSequence getText() {
        return super.getText();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        bg_W = width;
    }

    private DynamicLayout mDynamicLayout;


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
