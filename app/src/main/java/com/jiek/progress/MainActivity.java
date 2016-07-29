package com.jiek.progress;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MainActivity extends AppCompatActivity implements ValueAnimator.AnimatorUpdateListener {
    /**
     * 默认类型。
     */
    private CircleProgressbar mTvDefault,
    /**
     * 五个字的。
     */
    mTvFive,
    /**
     * 圆心点击变色。
     */
    mTvCicleColor,

    /**
     * 顺数进度条。
     */
    mTvCount,
    /**
     * 宽进度条。
     */
    mTvWide,
    /**
     * 窄进度条。
     */
    mTvNarrow,
    /**
     * 红色进度条。
     */
    mTvRedPro,
    /**
     * 红色边框。
     */
    mTvRedFrame,
    /**
     * 红色圆心。
     */
    mTvRedCircle,
    /**
     * 跳过。
     */
    mTvSkip,
    /**
     * 更新进度条文字。
     */
    mTvProgressBar1, mTvProgressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvDefault = (CircleProgressbar) findViewById(R.id.tv_default);
        mTvDefault.setShapeStrokeColor(Color.RED);
        mTvFive = (CircleProgressbar) findViewById(R.id.tv_five_text);
        mTvCicleColor = (CircleProgressbar) findViewById(R.id.tv_red_circle_color);

        // 和系统进度条一样，由0-100。
        mTvCount = (CircleProgressbar) findViewById(R.id.tv_count);
        mTvCount.setProgressType(CircleProgressbar.ProgressDirection.COUNTERCLOCKWISE);

        // 宽进度条。
        mTvWide = (CircleProgressbar) findViewById(R.id.tv_five_wide);
        mTvWide.setProgressArcWidth(10);//写入宽度。
        mTvWide.setShapeStrokeColor(Color.YELLOW);
        mTvWide.setCounter_millisecond(10000);// 把倒计时时间改长一点。

        // 窄进度条。
        mTvNarrow = (CircleProgressbar) findViewById(R.id.tv_five_narrow);
        mTvNarrow.setProgressArcWidth(3);// 写入宽度。

        // 红色进度条。
        mTvRedPro = (CircleProgressbar) findViewById(R.id.tv_red_progress);
        mTvRedPro.setProgressColor(Color.RED);

        // 红色边框进度条。
        mTvRedFrame = (CircleProgressbar) findViewById(R.id.tv_red_frame);
        mTvRedFrame.setShapeStrokeColor(Color.RED);

        // 红色圆心进度条。
        mTvRedCircle = (CircleProgressbar) findViewById(R.id.tv_red_circle);
        mTvRedCircle.setInCircleColor(Color.RED);

        mTvProgressBar1 = (CircleProgressbar) findViewById(R.id.tv_red_progress_text1);
        mTvProgressBar1.setCountdownProgressListener(1, progressListener);
        mTvProgressBar1.setProgressType(CircleProgressbar.ProgressDirection.COUNTERCLOCKWISE);

        mTvProgressBar2 = (CircleProgressbar) findViewById(R.id.tv_red_progress_text2);
        mTvProgressBar2.setCountdownProgressListener(2, progressListener);


        // 模拟网易新闻跳过。
        mTvSkip = (CircleProgressbar) findViewById(R.id.tv_red_skip);
        mTvSkip.setShapeStrokeColor(Color.YELLOW);
        mTvSkip.setInCircleColor(Color.parseColor("#AA00C6C6"));
        mTvSkip.setProgressColor(Color.DKGRAY);
        mTvSkip.setProgressArcWidth(3);
    }

    private CircleProgressbar.ProgressUpdateListener progressListener = new CircleProgressbar.ProgressUpdateListener() {
        @Override
        public void onProgress(int what, int progress) {
            if (what == 1) {
                mTvProgressBar1.setText(progress + "%");
            } else if (what == 2) {
                mTvProgressBar2.setText(progress + "%");
            }
            // 比如在首页，这里可以判断进度，进度到了100或者0的时候，你可以做跳过操作。
        }
    };

    public void onClick(View view) {
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_start) {
            progressAll(mTvDefault,
                    mTvFive,
                    mTvCicleColor,
                    mTvCount,
                    mTvWide,
                    mTvNarrow,
                    mTvRedPro,
                    mTvRedFrame,
                    mTvRedCircle,
                    mTvProgressBar1,
                    mTvProgressBar2,
                    mTvSkip
            );
        }
        return true;
    }

    private void progressAll(CircleProgressbar... views) {
        count = 0;
        for (CircleProgressbar v : views) {
            v.setMaxProgress(100);
            progress(v);
//            v.reStart();
        }
    }

    private void progress(CircleProgressbar view) {
        try {
            ValueAnimator va = ObjectAnimator.ofInt(view, "progress",
                    20, view.getMaxProgress() - 20
            );
            va.setDuration(3000);
////            va.setInterpolator(new BounceInterpolator());//后弹力
            va.setInterpolator(new LinearInterpolator());//均匀
//            va.setInterpolator(new AccelerateDecelerateInterpolator());//先加速后减速
//            va.setInterpolator(new AccelerateInterpolator());//加速
//            va.setInterpolator(new DecelerateInterpolator());//减速
//            va.setInterpolator(new OvershootInterpolator());//最后超过再回来
//            va.setInterpolator(new AnticipateOvershootInterpolator());//先退后超过再回来
//            va.setInterpolator(new AnticipateInterpolator());//先退后
//            va.setInterpolator(new CycleInterpolator(1));//弹簧,最后回到第一个值位置,期间执行次数
            va.addUpdateListener(this);
            va.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int count = 0;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        count++;
        mTvSkip.setText("v:" + value);
    }
}
