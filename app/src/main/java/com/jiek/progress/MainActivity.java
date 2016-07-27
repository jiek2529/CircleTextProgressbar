package com.jiek.progress;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    /**
     * 默认类型。
     */
    private CircleProgressbar mTvDefault;
    /**
     * 五个字的。
     */
    private CircleProgressbar mTvFive;
    /**
     * 圆心点击变色。
     */
    private CircleProgressbar mTvCicleColor;

    /**
     * 顺数进度条。
     */
    private CircleProgressbar mTvCount;
    /**
     * 宽进度条。
     */
    private CircleProgressbar mTvWide;
    /**
     * 窄进度条。
     */
    private CircleProgressbar mTvNarrow;
    /**
     * 红色进度条。
     */
    private CircleProgressbar mTvRedPro;
    /**
     * 红色边框。
     */
    private CircleProgressbar mTvRedFrame;
    /**
     * 红色圆心。
     */
    private CircleProgressbar mTvRedCircle;
    /**
     * 跳过。
     */
    private CircleProgressbar mTvSkip;
    /**
     * 更新进度条文字。
     */
    private CircleProgressbar mTvProgressBar1, mTvProgressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvDefault = (CircleProgressbar) findViewById(R.id.tv_default);
        mTvFive = (CircleProgressbar) findViewById(R.id.tv_five_text);
        mTvCicleColor = (CircleProgressbar) findViewById(R.id.tv_red_circle_color);

        // 和系统进度条一样，由0-100。
        mTvCount = (CircleProgressbar) findViewById(R.id.tv_count);
        mTvCount.setProgressType(CircleProgressbar.ProgressDirection.COUNTERCLOCKWISE);

        // 宽进度条。
        mTvWide = (CircleProgressbar) findViewById(R.id.tv_five_wide);
        mTvWide.setProgressLineWidth(10);//写入宽度。
        mTvWide.setCounter_millisecond(10000);// 把倒计时时间改长一点。

        // 窄进度条。
        mTvNarrow = (CircleProgressbar) findViewById(R.id.tv_five_narrow);
        mTvNarrow.setProgressLineWidth(3);// 写入宽度。

        // 红色进度条。
        mTvRedPro = (CircleProgressbar) findViewById(R.id.tv_red_progress);
        mTvRedPro.setProgressColor(Color.RED);

        // 红色边框进度条。
        mTvRedFrame = (CircleProgressbar) findViewById(R.id.tv_red_frame);
        mTvRedFrame.setOutLineColor(Color.RED);

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
        mTvSkip.setOutLineColor(Color.TRANSPARENT);
        mTvSkip.setInCircleColor(Color.parseColor("#AAC6C6C6"));
        mTvSkip.setProgressColor(Color.DKGRAY);
        mTvSkip.setProgressLineWidth(3);
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
            mTvDefault.reStart();
            mTvFive.reStart();
            mTvCicleColor.reStart();
            mTvCount.reStart();
            mTvWide.reStart();
            mTvNarrow.reStart();
            mTvRedPro.reStart();
            mTvRedFrame.reStart();
            mTvRedCircle.reStart();
            mTvProgressBar1.reStart();
            mTvProgressBar2.reStart();
            mTvSkip.reStart();
        }
        return true;
    }
}
