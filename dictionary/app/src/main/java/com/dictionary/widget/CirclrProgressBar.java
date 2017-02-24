package com.dictionary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CirclrProgressBar extends View {
    private Paint mpPaint;// 画笔
    private int mProgress = 0;// 进度
    RectF oval;
    private Paint textPain;// 画笔
    float bili;
    int result;

    public CirclrProgressBar(Context context) {
        this(context, null);
    }

    public CirclrProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclrProgressBar(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mpPaint = new Paint();
        mpPaint.setStrokeWidth(30);
        mpPaint.setStyle(Paint.Style.STROKE);
        mpPaint.setAntiAlias(true);

        textPain = new Paint();
        textPain.setColor(0xff656565);
        textPain.setTextSize(40);

        mpPaint.setStrokeJoin(Paint.Join.ROUND);
        //画笔的笔触为圆角
        mpPaint.setStrokeCap(Paint.Cap.ROUND);

    }


    String text;

    @Override
    protected void onDraw(Canvas canvas) {

        int center = getWidth() / 2;// 获取圆心坐标
        int radius = center - center / 3;// 半径
//        // 定义圆弧的形状和大小
        mpPaint.setColor(0xffD5D5D5);// 设置圆环的背景颜色
        oval = new RectF(center - radius, center - radius, center + radius,
                center + radius);
        canvas.drawArc(oval, -90, 360, false, mpPaint);// 根据进度画圆弧

        mpPaint.setColor(0xff209E85);// 设置圆环的颜色
        oval = new RectF(center - radius, center - radius, center + radius,
                center + radius);
        canvas.drawArc(oval, -90, mProgress, false, mpPaint);// 根据进度画圆弧

        text = (int) ((mProgress / 360.0f) * 100) + "%";
        int width = getStringWidth("" + text);
        int height = getStringHeight("" + text);
        canvas.drawText(text, center - width / 2, center + height / 2, textPain);
    }

    private int getStringWidth(String str) {
        return (int) textPain.measureText(str);
    }

    private int getStringHeight(String str) {
        Paint.FontMetrics fr = textPain.getFontMetrics();
        return (int) Math.ceil(fr.descent - fr.top) + 2;//ceil() 函数向上舍入为最接近的整数。
    }


    public void setBili(float bili) {
        this.bili = bili;
        result = (int) (bili * 360);//设置360的占用比例
    }

    public void start() {
        ValueAnimator animator = ValueAnimator.ofInt(0, result);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setDuration(1000);
        animator.start();
    }
}
