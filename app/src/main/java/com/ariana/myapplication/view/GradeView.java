package com.ariana.myapplication.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ariana.myapplication.R;
import com.ariana.myapplication.Util;

/**
 * Created by Ariana Wong on 2016/8/17.
 */
public class GradeView extends View {


    private Path gradePath, currentGradePath;
    private Paint gradePaint, progressPaint;

    private int gradeHeight;
    private int gradeWidth;
    private int backgroundHeight;

    private int radius = 10;

    private PopupWindow popupWindow;

    private int grade, runPosition, backGradeWidth, backGradeHeight;
    private AnimatorSet animSet;

    Bitmap bitmap1, bitmap2;



    public GradeView(Context context) {
        this(context, null);
    }

    public GradeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        gradePath = new Path();
        currentGradePath = new Path();
        gradePaint = new Paint();
        gradePaint.setAntiAlias(true);
        gradePaint.setColor(Color.parseColor("#5DE3FF"));
        progressPaint = new Paint();
        progressPaint.setColor(Color.GRAY);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        animSet = new AnimatorSet();
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.run1);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.run2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        if (heightMode == MeasureSpec.EXACTLY) {
//
//            gradeHeight = height;
//        } else {
            //如果布局里面没有设置固定值,这里取布局的高度的3/4
            gradeHeight = Util.dip2px(getContext(), 200);
//        }
        backgroundHeight = (int) (gradeHeight * 0.9);
        gradeWidth = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(gradeWidth, gradeHeight);
        Log.e("WLH", "onMeasure--- gradeWidth:"+gradeWidth
                +" heightMode:"+heightMode+ " height:"+height+" gradeHeight:"+gradeHeight);
        startAnim();
    }

    private void startAnim() {
        ValueAnimator gradeAnimator = getAnim(0, gradeWidth* 7 / 10);
        gradeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                grade = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator runAnimator = getAnim(0, gradeWidth* 7 / 10);
        runAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                runPosition = (int) valueAnimator.getAnimatedValue();
                Log.e("WLH", "runPosition:"+runPosition);
                postInvalidate();
            }
        });

        ValueAnimator backgroundAnimHeight = ValueAnimator.ofInt(backgroundHeight,
                backgroundHeight * 49 / 50,backgroundHeight * 19 / 20,backgroundHeight * 4 / 5);
        backgroundAnimHeight.setInterpolator(new LinearInterpolator());
        backgroundAnimHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                backGradeHeight = (int) valueAnimator.getAnimatedValue();
                Log.e("WLH", "backGradeHeight:"+backGradeHeight);
                postInvalidate();

            }
        });

        ValueAnimator backgroundAnimWidth = ValueAnimator.ofInt(0,
                gradeWidth * 21 / 80,gradeWidth * 42 / 80,gradeWidth * 7 / 10);
        backgroundAnimWidth.setInterpolator(new LinearInterpolator());
        backgroundAnimWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                backGradeWidth = (int) valueAnimator.getAnimatedValue();
                Log.e("WLH", "backGradeWidth:"+backGradeWidth +  " backGradeHeight:"+backGradeHeight);
                postInvalidate();
            }
        });

        animSet.setDuration(5000);
        animSet.playTogether(gradeAnimator, runAnimator,backgroundAnimHeight, backgroundAnimWidth);
        animSet.playSequentially();
        animSet.start();
    }

    private ValueAnimator getAnim(int from, int to){
        ValueAnimator anim = ValueAnimator.ofInt(from,
                to);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        gradePath.moveTo(0, backgroundHeight);//学残
        gradePath.lineTo(gradeWidth * 21 / 80, backgroundHeight * 49 / 50);//学渣
        gradePath.lineTo(gradeWidth * 42 / 80, backgroundHeight * 19 / 20);//学弱
        gradePath.lineTo(gradeWidth * 7 / 10, backgroundHeight * 4 / 5);//学民
        gradePath.lineTo(gradeWidth * 17 / 20, backgroundHeight * 1 / 3);//学痞
        gradePath.lineTo(gradeWidth, 0);//学神
        gradePath.lineTo(gradeWidth, backgroundHeight);
        gradePath.lineTo(0, backgroundHeight);
        gradePaint.setColor(Color.parseColor("#AAAAAA"));

        canvas.drawPath(gradePath, gradePaint);

        //绘制背景分数
        gradePaint.setColor(Color.parseColor("#5DE3FF"));
        gradePaint.setDither(true);

        currentGradePath.moveTo(0, backgroundHeight);
        if (backGradeHeight >= backgroundHeight * 49 / 50  &&  backGradeHeight <=backgroundHeight){
            Log.e("WLH","学残---");
            currentGradePath.lineTo(backGradeWidth, backGradeHeight);
        } else if(backGradeHeight < backgroundHeight * 49 / 50 && backGradeHeight >= backgroundHeight * 19 / 20){
            Log.e("WLH","学渣---");
            currentGradePath.lineTo(gradeWidth * 21 / 80, backgroundHeight * 49 / 50);
            currentGradePath.lineTo(backGradeWidth, backGradeHeight);
        }else
        if(backGradeHeight < backgroundHeight * 19 / 20 && backGradeHeight >= backgroundHeight * 4 / 5){
            Log.e("WLH","学弱---");
            currentGradePath.lineTo(gradeWidth * 21 / 80, backgroundHeight * 49 / 50);
            currentGradePath.lineTo(gradeWidth * 42 / 80, backgroundHeight * 19 / 20);
            currentGradePath.lineTo(backGradeWidth, backGradeHeight);
        }
        currentGradePath.lineTo(backGradeWidth,backgroundHeight);
        currentGradePath.lineTo(0,backgroundHeight);
        canvas.drawPath(currentGradePath, gradePaint);

        //绘制小圆圈
        gradePaint.setColor(Color.parseColor("#FFE545"));
        canvas.drawCircle(0, backgroundHeight, radius, gradePaint);
        canvas.drawCircle(gradeWidth * 21 / 80, backgroundHeight * 49 / 50, radius, gradePaint);
        canvas.drawCircle(gradeWidth * 42 / 80, backgroundHeight * 19 / 20, radius, gradePaint);
        canvas.drawCircle(gradeWidth * 7 / 10, backgroundHeight * 4 / 5, radius, gradePaint);
        canvas.drawCircle(gradeWidth * 17 / 20, backgroundHeight * 1 / 3, radius, gradePaint);
        canvas.drawCircle(gradeWidth, 0, radius, gradePaint);

        //绘制跑步进度条
        progressPaint.setColor(Color.GRAY);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(gradeHeight - backgroundHeight - 30);
        canvas.drawLine(20, backgroundHeight + 30, gradeWidth - 20, backgroundHeight + 30, progressPaint);
        progressPaint.setColor(Color.GREEN);
        canvas.drawLine(20,  backgroundHeight + 30, grade - 20, backgroundHeight + 30, progressPaint);

        if (runPosition % 21 <= 10){
            canvas.drawBitmap(bitmap1, runPosition,  gradeHeight- bitmap1.getHeight(),null);
        }else {
            canvas.drawBitmap(bitmap2, runPosition,  gradeHeight- bitmap1.getHeight(),null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        if (action == MotionEvent.ACTION_DOWN){
            Log.e("WLH", "X:"+X + " Y:"+ Y);
            Log.e("WLH", "gradeWidth:"+gradeWidth + "  backgroundHeight:"+backgroundHeight);
            if (X <= 0+radius && Y >= backgroundHeight - radius){
                showPopView("学残", 0, backgroundHeight-30);
                return true;
            }
            if (X<= gradeWidth * 21 / 80 +radius && X >= gradeWidth * 21 / 80 - radius
                    && Y <= backgroundHeight * 49 / 50+radius && Y>= backgroundHeight * 49 / 50-radius){
                showPopView("学渣", gradeWidth * 21 / 80 - 10, backgroundHeight * 49 / 50 -10);
                return true;
            }
            if (X <= gradeWidth * 42 / 80 +radius && X >= gradeWidth * 42 / 80 -radius
                    && Y <= backgroundHeight * 19 / 20 +radius && Y >= backgroundHeight * 19 / 20 - radius){
                showPopView("学弱", gradeWidth * 42 / 80 - 10, backgroundHeight * 19 / 20 -10);
                return true;
            }
            if (X <= gradeWidth * 7 / 10 + radius && X >= gradeWidth * 7 / 10 - radius
                    && Y <= backgroundHeight * 4 / 5 + radius && Y >= backgroundHeight * 4 / 5 - radius){
                showPopView("学民", gradeWidth * 7 / 10 - 10, backgroundHeight * 4 / 5 -10);
                return true;
            }
            if (X <= gradeWidth* 17 / 20 + radius && X >= gradeWidth* 17 / 20 - radius
                    && Y <= backgroundHeight * 1 / 3 + radius && Y >= backgroundHeight * 1 / 3 - radius){
                showPopView("学痞", gradeWidth * 17 / 20 - 10, backgroundHeight * 1 / 3 -10);
                return true;
            }
            if (X >= gradeWidth - radius && Y <= 0 + radius){
                showPopView("学神", gradeWidth  - 50, 0);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }


    private void showPopView(final String name, int X, int Y){

        Log.e("WLH", "showPopView X:"+X + " Y:"+ Y);

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }

        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.pop_grade, null);
        TextView text = (TextView) contentView.findViewById(R.id.text);
        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
            }
        });
        text.setText(name);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, X, Y);

    }

}
