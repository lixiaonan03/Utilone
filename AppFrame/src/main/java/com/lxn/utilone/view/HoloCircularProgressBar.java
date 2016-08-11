package com.lxn.utilone.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.lxn.utilone.R;


/**
 * 头部带一个圆头的进度条
 */
public class HoloCircularProgressBar extends View {

    private static final String INSTANCE_STATE_SAVEDSTATE = "saved_state";

    private static final String INSTANCE_STATE_PROGRESS = "progress";

    private static final String INSTANCE_STATE_MARKER_PROGRESS = "marker_progress";

    private static final String INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR = "progress_background_color";

    private static final String INSTANCE_STATE_PROGRESS_COLOR = "progress_color";

    private static final String INSTANCE_STATE_THUMB_VISIBLE = "thumb_visible";

    private static final String INSTANCE_STATE_MARKER_VISIBLE = "marker_visible";


    /*  进度条所在的矩阵区域  The rectangle enclosing the circle. */
    private final RectF mCircleBounds = new RectF();
    /* 进度条头部的进度矩阵区域    */
    private final RectF mSquareRect = new RectF();



    /*  背景圆的画笔  */
    private Paint mBackgroundColorPaint = new Paint();
    /*  标记画笔 The Marker color paint */
    private Paint mMarkerColorPaint;
    /* 已经进行的进度条画笔   */
    private Paint mProgressColorPaint;
    /* 进度条头部的那个头   */
    private Paint mThumbColorPaint = new Paint();

    /** 圆的半径 在设计大小的时候计算圆的半径*/
    private float mRadius;

    /** 进度条头部的那个头的圆心X坐标  Care. the position is not the position of the rotated thumb. The position is only calculate in {@link #onMeasure(int, int)}  */
    private float mThumbPosX;
    /** 进度条头部的那个头的圆心Y坐标   这个时候画布旋转过*/
    private float mThumbPosY;

    /** 刻度线的长度  The pointer width (in pixels).  */
    private int mThumbRadius = 20;

    /*** 画布平移的x坐标  */
    private float mTranslationOffsetX;

    /** 画布平移的Y坐标 * The Translation offset y which gives us the ability to use our own coordinates system. */
    private float mTranslationOffsetY;


    /** 圆环的背景颜色  */
    private int mProgressBackgroundColor;

    /** 正在进行的进度条颜色 */
    private int mProgressColor;

    /** 圆环的宽度  */
    private int mCircleStrokeWidth = 10;

    /* 圆上刻度的角度  * The Marker progress.  */
    private float mMarkerProgress = 0.0f;

    /* 当前进度是否超过1* the overdraw is true if the progress is over 1.0.   */
    private boolean mOverrdraw = false;

    /* 当前进度   The current progress.   */
    private float mProgress = 0.0f;



    /** 刻度线是否显示标记  **/
    private boolean mIsMarkerEnabled = false;

    /* 进度条头部块是否显示   */
    private boolean mIsThumbEnabled = true;







    /** The gravity of the view. Where should the Circle be drawn within the given bounds* <p/>  * {@link #computeInsets(int, int)}*/
    private int mGravity = Gravity.CENTER;

    /** The Horizontal inset calcualted in {@link #computeInsets(int, int)} depends on {@link* #mGravity}.* 水平和垂直方向 因为设置Gravity 需要考虑的值*/
    private int mHorizontalInset = 0;
    private int mVerticalInset = 0;

    /** true if not all properties are set. then the view isn't drawn and there are no errors in the* LayoutEditor   */
    private boolean mIsInitializing = true;



    public HoloCircularProgressBar(final Context context) {
        this(context, null);
    }
    public HoloCircularProgressBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.circularProgressBarStyle);
    }
    public HoloCircularProgressBar(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        // load the styled attributes and set their properties
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HoloCircularProgressBar, defStyle, 0);
        if (attributes != null) {
            try {
                setProgressColor(attributes.getColor(R.styleable.HoloCircularProgressBar_progress_color, Color.CYAN));
                setProgressBackgroundColor(attributes.getColor(R.styleable.HoloCircularProgressBar_progress_background_color, Color.GREEN));
                setProgress(attributes.getFloat(R.styleable.HoloCircularProgressBar_progress, 0.0f));
                setMarkerProgress(attributes.getFloat(R.styleable.HoloCircularProgressBar_marker_progress, 0.0f));
                setWheelSize((int) attributes.getDimension(R.styleable.HoloCircularProgressBar_stroke_width, 10));
                setThumbEnabled(attributes.getBoolean(R.styleable.HoloCircularProgressBar_thumb_visible, true));
                setMarkerEnabled(attributes.getBoolean(R.styleable.HoloCircularProgressBar_marker_visible, true));

                mGravity = attributes.getInt(R.styleable.HoloCircularProgressBar_android_gravity, Gravity.CENTER);
            } finally {
                // make sure recycle is always called.
                attributes.recycle();
            }
        }
        mThumbRadius = mCircleStrokeWidth * 2;
        updateBackgroundColor();
        updateMarkerColor();
        updateProgressColor();
        // the view has now all properties and can be drawn
        mIsInitializing = false;
    }

    /**
     * 确定大小的方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight(), widthMeasureSpec);

        final int diameter;//直径
        // MeasureSpec.UNSPECIFIED 是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，通过measure方法传入的模式。
        //MeasureSpec.EXACTLY 是精确尺寸，当我们将控件的layout_width或layout_height指定为具体数值时如andorid:layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
        //MeasureSpec.AT_MOST 是最大尺寸，当控件的layout_width或layout_height指定为WRAP_CONTENT时，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
        if (heightMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // ScrollView
            diameter = width;
            computeInsets(0, 0);
        } else if (widthMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // HorizontalScrollView
            diameter = height;
            computeInsets(0, 0);
        } else {
            // Default
            diameter = Math.min(width, height);
            computeInsets(width - diameter, height - diameter);
        }

        setMeasuredDimension(diameter, diameter);

        final float halfWidth = diameter * 0.5f;

        // width of the drawed circle (+ the drawedThumb)
        final float drawedWith;
        if (isThumbEnabled()) {
            drawedWith = mThumbRadius * (5f / 6f);
        } else if (isMarkerEnabled()) {
            drawedWith = mCircleStrokeWidth * 1.4f;
        } else {
            drawedWith = mCircleStrokeWidth / 2f;
        }
        //TODO 半径的计算 页面宽度的一般减去需要圆外面显示的多余的部分
        // -0.5f for pixel perfect fit inside the viewbounds
        mRadius = halfWidth - drawedWith - 0.5f;

        mCircleBounds.set(-mRadius, -mRadius, mRadius, mRadius);

        mThumbPosX = (float) (mRadius * Math.cos(0));
        mThumbPosY = (float) (mRadius * Math.sin(0));

        mTranslationOffsetX = halfWidth + mHorizontalInset;
        mTranslationOffsetY = halfWidth + mVerticalInset;

    }

    /**
     * 绘画方法
     * @param canvas
     */
    @Override
    protected void onDraw(final Canvas canvas) {

        // All of our positions are using our internal coordinate system.
        // Instead of translating
        // them we let Canvas do the work for us.
        //画布平移
        canvas.translate(mTranslationOffsetX, mTranslationOffsetY);

        final float progressRotation = getCurrentRotation();

        // draw the background
        if (!mOverrdraw) {
            //画圆环 参数 第一个区域  第二个 开始角度 第三个 结束角度 第四个是否画2个边 第五个 画笔
            canvas.drawArc(mCircleBounds, 270, -(360 - progressRotation), false, mBackgroundColorPaint);
        }

        // draw the progress or a full circle if overdraw is true
        canvas.drawArc(mCircleBounds, 270, mOverrdraw ? 360 : progressRotation, false, mProgressColorPaint);

        // draw the marker at the correct rotated position
        //是否需要画刻度线
        if (mIsMarkerEnabled) {
            final float markerRotation = getMarkerRotation();

            canvas.save();
            canvas.rotate(markerRotation - 90);
            //drawLine 参数  1、 开始X坐标  2、开始Y坐标  3、结束 X 坐标 4、结束Y坐标  5、画笔
            canvas.drawLine((float) (mThumbPosX + mThumbRadius / 2 * 1.4), mThumbPosY, (float) (mThumbPosX - mThumbRadius / 2 * 1.4), mThumbPosY, mMarkerColorPaint);
            canvas.restore();
        }
        //是否需要画进度条前面的块
        if (isThumbEnabled()) {
            // draw the thumb square at the correct rotated position
            canvas.save();
            canvas.rotate(progressRotation - 90);
            // rotate the square by 45 degrees
            // mThumbColorPaint.setColor(Color.RED);
           /* canvas.rotate(45, mThumbPosX, mThumbPosY);
            mSquareRect.left = mThumbPosX - mThumbRadius / 3;
            mSquareRect.right = mThumbPosX + mThumbRadius / 3;
            mSquareRect.top = mThumbPosY - mThumbRadius / 3;
            mSquareRect.bottom = mThumbPosY + mThumbRadius / 3;
            canvas.drawRoundRect(mSquareRect,mThumbPosX,mThumbPosX, mThumbColorPaint);*/


            canvas.drawCircle(mThumbPosX, mThumbPosY, 10, mThumbColorPaint);
            canvas.restore();
        }
    }



    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            setProgress(bundle.getFloat(INSTANCE_STATE_PROGRESS));
            setMarkerProgress(bundle.getFloat(INSTANCE_STATE_MARKER_PROGRESS));

            final int progressColor = bundle.getInt(INSTANCE_STATE_PROGRESS_COLOR);
            if (progressColor != mProgressColor) {
                mProgressColor = progressColor;
                updateProgressColor();
            }

            final int progressBackgroundColor = bundle.getInt(INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR);
            if (progressBackgroundColor != mProgressBackgroundColor) {
                mProgressBackgroundColor = progressBackgroundColor;
                updateBackgroundColor();
            }

            mIsThumbEnabled = bundle.getBoolean(INSTANCE_STATE_THUMB_VISIBLE);

            mIsMarkerEnabled = bundle.getBoolean(INSTANCE_STATE_MARKER_VISIBLE);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE_SAVEDSTATE));
            return;
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE_SAVEDSTATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STATE_PROGRESS, mProgress);
        bundle.putFloat(INSTANCE_STATE_MARKER_PROGRESS, mMarkerProgress);
        bundle.putInt(INSTANCE_STATE_PROGRESS_COLOR, mProgressColor);
        bundle.putInt(INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR, mProgressBackgroundColor);
        bundle.putBoolean(INSTANCE_STATE_THUMB_VISIBLE, mIsThumbEnabled);
        bundle.putBoolean(INSTANCE_STATE_MARKER_VISIBLE, mIsMarkerEnabled);
        return bundle;
    }

    /**
     * 获取当前环的宽度
     * @return
     */
    public int getCircleStrokeWidth() {
        return mCircleStrokeWidth;
    }

    /**
     * 当前刻度的角度值
     * @return
     */
    public float getMarkerProgress() {
        return mMarkerProgress;
    }

    /**
     * 获取当前的进度值
     * @return
     */
    public float getProgress() {
        return mProgress;
    }

    /**
     * 获取当前进度值颜色
     * @return
     */
    public int getProgressColor() {
        return mProgressColor;
    }

    /**
     * @return 刻度是否显示
     */
    public boolean isMarkerEnabled() {
        return mIsMarkerEnabled;
    }

    /**
     * @return 头部块是否显示
     */
    public boolean isThumbEnabled() {
        return mIsThumbEnabled;
    }

    /**
     * 设置圆上的刻度是否显示
     */
    public void setMarkerEnabled(final boolean enabled) {
        mIsMarkerEnabled = enabled;
    }

    /**
     * 设置圆上的刻度显示的角度
     */
    public void setMarkerProgress(final float progress) {
        mIsMarkerEnabled = true;
        mMarkerProgress = progress;
    }

    /**
     * 设置当前的进度条进度
     */
    public void setProgress(final float progress) {
        if (progress == mProgress) {
            return;
        }

        if (progress == 1) {
            mOverrdraw = false;
            mProgress = 1;
        } else {

            if (progress >= 1) {
                mOverrdraw = true;
            } else {
                mOverrdraw = false;
            }

            mProgress = progress % 1.0f;

        }
        //TODO  外部实现动画  参考activity中的实现
        if (!mIsInitializing) {
            invalidate();
            // setAnimation(last, mProgress, 10000);
        }
        //内部实现
        //setAnimation(last, mProgress, 10000);

        last= progress % 1.0f;
    }

    /**
     * 为进度设置动画
     * @param last
     * @param current
     */
    private ValueAnimator progressAnimator;
    private float last = 0.0f;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress= (Float)animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }


    /**
     * 设置进度条背景颜色
     */
    public void setProgressBackgroundColor(final int color) {
        mProgressBackgroundColor = color;

        updateMarkerColor();
        updateBackgroundColor();
    }

    /**
     * 设置进度条颜色
     */
    public void setProgressColor(final int color) {
        mProgressColor = color;

        updateProgressColor();
    }

    /**
     * 设置进度条头部块是否显示
     */
    public void setThumbEnabled(final boolean enabled) {
        mIsThumbEnabled = enabled;
    }

    /**
     * 设置圆环的宽度
     */
    public void setWheelSize(final int dimension) {
        mCircleStrokeWidth = dimension;

        // update the paints
        updateBackgroundColor();
        updateMarkerColor();
        updateProgressColor();
    }

    /**
     * Compute insets.
     * <p/>
     * <pre>
     *  ______________________
     * |_________dx/2_________|
     * |......| /'''''\|......|
     * |-dx/2-|| View ||-dx/2-|
     * |______| \_____/|______|
     * |________ dx/2_________|
     * </pre>
     *
     * @param dx the dx the horizontal unfilled space
     * @param dy the dy the horizontal unfilled space
     */
    @SuppressLint("NewApi")
    private void computeInsets(final int dx, final int dy) {
        int absoluteGravity = mGravity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            absoluteGravity = Gravity.getAbsoluteGravity(mGravity, getLayoutDirection());
        }

        switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                mHorizontalInset = 0;
                break;
            case Gravity.RIGHT:
                mHorizontalInset = dx;
                break;
            case Gravity.CENTER_HORIZONTAL:
            default:
                mHorizontalInset = dx / 2;
                break;
        }
        switch (absoluteGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                mVerticalInset = 0;
                break;
            case Gravity.BOTTOM:
                mVerticalInset = dy;
                break;
            case Gravity.CENTER_VERTICAL:
            default:
                mVerticalInset = dy / 2;
                break;
        }
    }

    /**
     * 当前进度的角度
     */
    private float getCurrentRotation() {
        return 360 * mProgress;
    }

    /**
     * 标记的角度
     */
    private float getMarkerRotation() {
        return 360 * mMarkerProgress;
    }

    /**
     * 设置进度条背景颜色
     */
    private void updateBackgroundColor() {
        mBackgroundColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundColorPaint.setColor(mProgressBackgroundColor);
        mBackgroundColorPaint.setStyle(Paint.Style.STROKE);
        mBackgroundColorPaint.setStrokeWidth(mCircleStrokeWidth);

        invalidate();
    }

    /**
     * 更新标记画笔颜色
     */
    private void updateMarkerColor() {
        mMarkerColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkerColorPaint.setColor(mProgressBackgroundColor);
        mMarkerColorPaint.setStyle(Paint.Style.STROKE);
        mMarkerColorPaint.setStrokeWidth(mCircleStrokeWidth / 2);

        invalidate();
    }

    /**
     * 更新进度条颜色和进度条头部颜色
     */
    private void updateProgressColor() {
        mProgressColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressColorPaint.setColor(mProgressColor);
        mProgressColorPaint.setStyle(Paint.Style.STROKE);
        mProgressColorPaint.setStrokeWidth(mCircleStrokeWidth);

        mThumbColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThumbColorPaint.setColor(mProgressColor);
        mThumbColorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mThumbColorPaint.setStrokeWidth(mCircleStrokeWidth);

        invalidate();
    }

}
