package com.jiek.progress;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.widget.TextView;

public class CircleProgressbar extends TextView {

    /**
     * max of progress
     * progress最大值
     */
    private int mMax = 100;

    /**
     * current progress
     * 当前进度值
     */
    private int progress = 0;

    /**
     * 0:x-right  -90:y-top  90:y-bottom  180:x-left
     */
    private float startAngle = -90;

    /**
     * progress arc width px
     * 进度圈的厚度
     */
    private int progressArcWidth = 4;

    /**
     * progress arc color
     */
    private int progressArcColor = Color.BLUE;

    /**
     * progress type
     */
    private ProgressDirection mProgressDirection = ProgressDirection.COUNTERCLOCKWISE;

    /**
     * down time count (millisecond)
     */
    private long counter_millisecond = 5000;


    /**
     * shape stroke color
     */
    private int shapeStrokeColor = Color.GRAY;

    /**
     * shape stroke width
     */
    private int shapeStrokeWidth = 10;

    /**
     * solid circle color
     */
    private ColorStateList solidCircleColors = ColorStateList.valueOf(Color.BLUE);
    /**
     * solid circle
     */
    private int circleColor;

    /**
     * paint
     */
    private Paint mPaint = new Paint();

    /**
     *
     */
    private RectF mArcRect = new RectF();

    /**
     * View rect
     */
    final Rect bounds = new Rect();
    /**
     * progress Update Listener
     */
    private ProgressUpdateListener mCountdownProgressListener;
    /**
     * Listener what。
     */
    private int listenerWhat = 0x0f0f;

    public void setStartAngle(int _startAngle) {
        startAngle = _startAngle;
    }

    /**
     * left:0   top:1  right:2  bottom:3
     */
    public void setStartOrientant(int orientant) {
        switch (orientant) {
            case 0:
                startAngle = 180;
                break;
            case 2:
                startAngle = 0;
                break;
            case 3:
                startAngle = 90;
                break;
            default:
                startAngle = -90;
                break;
        }
    }

    public CircleProgressbar(Context context) {
        this(context, null);
    }

    public CircleProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleProgressbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs);
    }

    /**
     * initialize
     *
     * @param context
     * @param attributeSet
     */
    private void initialize(Context context, AttributeSet attributeSet) {
        mPaint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CircleProgressbar);
        if (typedArray.hasValue(R.styleable.CircleProgressbar_in_circle_color))
            solidCircleColors = typedArray.getColorStateList(R.styleable.CircleProgressbar_in_circle_color);
        else
            solidCircleColors = ColorStateList.valueOf(Color.LTGRAY);
        circleColor = solidCircleColors.getColorForState(getDrawableState(), Color.DKGRAY);
        typedArray.recycle();
    }

    /**
     * shape stroke color
     *
     * @param shapeStrokeColor @ColorInt
     */
    public void setShapeStrokeColor(@ColorInt int shapeStrokeColor) {
        this.shapeStrokeColor = shapeStrokeColor;
        invalidate();
    }

    /**
     * shape stroke width
     *
     * @param shapeStrokeWidth @DimenRes
     */
    public void setShapeStrokeWidth(@DimenRes int shapeStrokeWidth) {
        this.shapeStrokeWidth = dp2pxInt(shapeStrokeWidth);
        invalidate();
    }

    /**
     * set
     *
     * @param inCircleColor @ColorInt
     */
    public void setInCircleColor(@ColorInt int inCircleColor) {
        this.solidCircleColors = ColorStateList.valueOf(inCircleColor);
        invalidate();
    }

    /**
     * validate circle color
     */
    private void validateCircleColor() {
        int circleColorTemp = solidCircleColors.getColorForState(getDrawableState(), Color.TRANSPARENT);
        if (circleColor != circleColorTemp) {
            circleColor = circleColorTemp;
            invalidate();
        }
    }

    /**
     * set progress color
     *
     * @param _progressArcColor
     */
    public void setProgressColor(@ColorInt int _progressArcColor) {
        this.progressArcColor = _progressArcColor;
        invalidate();
    }

    /**
     * set progress line width (px)
     *
     * @param progressArcWidth
     */
    public void setProgressArcWidth(int progressArcWidth) {
        progressArcWidth = dp2pxInt(progressArcWidth);
        this.progressArcWidth = progressArcWidth;
        invalidate();
    }

    /**
     * set progress value
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = validateProgress(progress);
        invalidate();
    }

    /**
     * validate progress value
     *
     * @param progress
     * @return avaliable progress value.
     */
    private int validateProgress(int progress) {
        if (progress > mMax)
            progress = mMax;
        else if (progress < 0)
            progress = 0;
        return progress;
    }

    /**
     * fetch current progress value
     *
     * @return progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * set initial count timemillis
     *
     * @param counter_millisecond
     */
    public void setCounter_millisecond(long counter_millisecond) {
        this.counter_millisecond = counter_millisecond;
        invalidate();
    }

    /**
     * 拿到进度条计时时间。
     *
     * @return 毫秒。
     */
    public long getCounter_millisecond() {
        return this.counter_millisecond;
    }

    /**
     * 设置进度条类型。
     *
     * @param progressType {@link ProgressDirection}.
     */
    public void setProgressType(ProgressDirection progressType) {
        this.mProgressDirection = progressType;
        resetProgress();
        invalidate();
    }

    /**
     * reset Progress
     */
    private void resetProgress() {
        switch (mProgressDirection) {
            case COUNTERCLOCKWISE:
                progress = 0;
                break;
            case ANTICLOCKWISE:
                progress = mMax;
                break;
        }
    }

    /**
     * fetch current progressType
     *
     * @return
     */
    public ProgressDirection getProgressType() {
        return mProgressDirection;
    }

    /**
     * set ProgressUpdateLisener
     *
     * @param mCountdownProgressListener
     */
    public void setCountdownProgressListener(int what, ProgressUpdateListener mCountdownProgressListener) {
        this.listenerWhat = what;
        this.mCountdownProgressListener = mCountdownProgressListener;
    }

    /**
     * start progress task
     */
    public void start() {
        stop();
        post(progressChangeTask);
    }

    /**
     * restart progress task
     */
    public void restart() {
        resetProgress();
        start();
    }

    /**
     * stop update task
     */
    public void stop() {
        removeCallbacks(progressChangeTask);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //fetch view Drawing rect
        getDrawingRect(bounds);

        int size = bounds.height() > bounds.width() ? bounds.width() : bounds.height();
        float outerRadius = size / 2;

        //paint background color
        int circleColor = solidCircleColors.getColorForState(getDrawableState(), 0);
        /*mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(circleColor);
        canvas.drawCircle(
                bounds.centerX(),
                bounds.centerY(),
                outerRadius - shapeStrokeWidth / 2,
                mPaint);*/

        //paint stroke line
        if (shapeStrokeWidth > 0) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(shapeStrokeWidth);
            mPaint.setColor(shapeStrokeColor);
            canvas.drawCircle(
                    bounds.centerX(),
                    bounds.centerY(),
                    outerRadius - shapeStrokeWidth / 2,
                    mPaint);
        }

        //paint progress arc
        //画弧线
        mPaint.setColor(progressArcColor);
        mPaint.setStyle(Paint.Style.STROKE);//空心:Paint.Style.STROKE 实心:Paint.Style.FILL
        mPaint.setStrokeWidth(progressArcWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        int deleteWidth = progressArcWidth;// + shapeStrokeWidth;//压不压外圈线的逻辑区
        mArcRect.set(bounds.left + deleteWidth / 2, bounds.top + deleteWidth / 2, bounds.right - deleteWidth / 2, bounds.bottom - deleteWidth / 2);
        canvas.drawArc(
                mArcRect,
                startAngle,
                360 * progress / mMax,
                false, //useCenter true:显示连心两边线  false:只画圆弧线
                mPaint);

        //paint text
        //画文本
        Paint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        float textY = bounds.centerY() - (paint.descent() + paint.ascent()) / 2;
        canvas.drawText(
                getText().toString(),//"k\n"+progress,
                bounds.centerX(), textY, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int lineWidth = 4 * (shapeStrokeWidth + progressArcWidth);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = (width > height ? width : height) + lineWidth;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        validateCircleColor();
    }

    /**
     * update progress task runnable
     */
    private Runnable progressChangeTask = new Runnable() {
        @Override
        public void run() {
            removeCallbacks(this);
            switch (mProgressDirection) {
                case COUNTERCLOCKWISE:
                    progress += 1;
                    break;
                case ANTICLOCKWISE:
                    progress -= 1;
                    break;
            }
            if (progress >= 0 && progress <= mMax) {
                if (mCountdownProgressListener != null)
                    mCountdownProgressListener.onProgress(listenerWhat, progress);
                invalidate();
                postDelayed(progressChangeTask, counter_millisecond / mMax);
            } else
                progress = validateProgress(progress);
        }
    };

    /**
     * maxProgress, between 0-100,000
     *
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress) {
        if (maxProgress < 0) {
            maxProgress = 0;
        } else if (maxProgress > 100000) {
            maxProgress = 100000;
        }
        mMax = maxProgress;
    }

    public int getMaxProgress() {
        return mMax;
    }

    /**
     * progress direction
     */
    public enum ProgressDirection {
        /**
         * counterclockwise，from 0 to 100.
         * 顺时针
         */
        COUNTERCLOCKWISE,

        /**
         * anti-clockwise，from 100 to 0.
         * 逆时针
         */
        ANTICLOCKWISE
    }

    public interface ProgressUpdateListener {
        void onProgress(int what, int progress);
    }

    private float dp2px(int dp) {
        return getResources().getDisplayMetrics().density * dp + 0.5f;
    }

    private int dp2pxInt(int dp) {
        return (int) dp2px(dp);
    }
}
