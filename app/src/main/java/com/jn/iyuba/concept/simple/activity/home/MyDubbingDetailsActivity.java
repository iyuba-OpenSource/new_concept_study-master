package com.jn.iyuba.concept.simple.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.succinct.databinding.ActivityMyDubbingDetailsBinding;
import com.jn.iyuba.concept.simple.entity.MediaPauseEventbus;
import com.jn.iyuba.concept.simple.view.home.MyDubbingDetailsContract;

import org.greenrobot.eventbus.EventBus;

/**
 * 我的-我的配音-我的配音详情
 */
public class MyDubbingDetailsActivity extends BaseActivity<MyDubbingDetailsContract.MyDubbingDetailsView, MyDubbingDetailsContract.MyDubbingDetailsPresenter>
        implements MyDubbingDetailsContract.MyDubbingDetailsView {

    private ActivityMyDubbingDetailsBinding activityBinding;

    private ExoPlayer player;

    private String videoUrl;

    private String title;

    private String titleCh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle();

        initOperation();


        String url = null;
        if (Constant.userinfo == null) {

            url = Constant.URL_STATIC0 + "/" + videoUrl;
        } else {

            if (Constant.userinfo.isVip()) {

                url = Constant.URL_STATICVIP + "/" + videoUrl;
            } else {
                url = Constant.URL_STATIC0 + "/" + videoUrl;
            }
        }
        MediaItem mediaItem = MediaItem.fromUri(url);
        player = new ExoPlayer.Builder(this).build();
        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onIsPlayingChanged(EventTime eventTime, boolean isPlaying) {

                if (isPlaying) {

                    EventBus.getDefault().post(new MediaPauseEventbus());
                }
            }
        });
        activityBinding.mddSpv.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (player != null) {

            player.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null) {

            player.release();
        }
    }


    private void initOperation() {

        activityBinding.mddTvTitle.setText(title);
        activityBinding.mddTvTitleCh.setText(titleCh);

        activityBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityBinding.toolbar.toolbarIvTitle.setText("详情");
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            videoUrl = bundle.getString("VIDEO_URL");
            title = Bundle.EMPTY.getString("TITLE");
            titleCh = Bundle.EMPTY.getString("TITLE_CH");
        }
    }

    public static void startActivity(Activity activity, String videoUrl, String title, String titleCh) {

        Intent intent = new Intent(activity, MyDubbingDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("VIDEO_URL", videoUrl);
        bundle.putString("TITLE", title);
        bundle.putString("TITLE_CH", titleCh);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    public View initLayout() {

        activityBinding = ActivityMyDubbingDetailsBinding.inflate(getLayoutInflater());
        return activityBinding.getRoot();
    }

    @Override
    public MyDubbingDetailsContract.MyDubbingDetailsPresenter initPresenter() {
        return null;
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}