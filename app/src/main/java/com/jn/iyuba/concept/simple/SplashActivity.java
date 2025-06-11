package com.jn.iyuba.concept.simple;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.util.Log;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.splash.SplashView;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.activity.MyWebActivity;
import com.jn.iyuba.succinct.BuildConfig;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.ActivitySplashBinding;
import com.jn.iyuba.concept.simple.model.bean.AdEntryBean;
import com.jn.iyuba.concept.simple.presenter.SplashPresenter;
import com.jn.iyuba.concept.simple.util.popup.PrivacyPopup;
import com.jn.iyuba.concept.simple.view.SplashContract;
import com.yd.saas.base.interfaces.AdViewSpreadListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdSpread;

public class SplashActivity extends BaseActivity<SplashContract.SplashView, SplashContract.SplashPresenter>
        implements SplashContract.SplashView, AdViewSpreadListener {


    private SharedPreferences sp;

    private PrivacyPopup privacyPopup;

    private ActivitySplashBinding binding;

    private AdEntryBean.DataDTO dataDTO;

    private boolean isAdCLick = false;


    //华为
    private static final int AD_TIMEOUT = 10000;
    private static final int MSG_AD_TIMEOUT = 1001;

    private boolean hasPaused = false;

    Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (SplashActivity.this.hasWindowFocus()) {
                jump();
            }
            return false;
        }
    });


    private void jump() {
        if (!hasPaused) {
            hasPaused = true;
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStop() {
        // 移除消息队列中等待的超时消息
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        hasPaused = true;
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hasPaused = false;
        jump();
    }


    private void loadHwAd() {

/*        AdParam adParam = new AdParam.Builder().build();
        SplashView.SplashAdLoadListener splashAdLoadListener = new SplashView.SplashAdLoadListener() {
            @Override
            public void onAdLoaded() {
                // 广告加载成功时调用

                android.util.Log.d("SplashView", "SplashView");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // 广告加载失败时调用, 跳转至App主界面
                jump();
                android.util.Log.d("SplashView", errorCode + "");
            }

            @Override
            public void onAdDismissed() {
                // 广告展示完毕时调用, 跳转至App主界面
                jump();
                android.util.Log.d("SplashView", "onAdDismissed");
            }
        };
        String slotId = "d3yb6p362o";
        // 锁定设备当前屏幕方向，自适应横竖屏方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        // 根据当前屏幕方向设置默认Slogan和设置对应的广告位ID
        binding.splashAdView.setSloganResId(R.drawable.shape_slogan);
        // 获取SplashView
        SplashView splashView = findViewById(R.id.splash_ad_view);
        // 设置视频类开屏广告的音频焦点类型
        splashView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);
        // 加载广告
        splashView.load(slotId, orientation, adParam, splashAdLoadListener);
        // 发送延时消息，保证广告显示超时后，APP首页可以正常显示
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        timeoutHandler.sendEmptyMessageDelayed(MSG_AD_TIMEOUT, AD_TIMEOUT);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dealWindow();
        initView();

        sp = getSharedPreferences(Constant.SP_PRIVACY, MODE_PRIVATE);
        int pState = sp.getInt(Constant.SP_KEY_PRIVACY_STATE, 0);
        if (pState == 0) {

            initPrivacyPopup();
        } else {

            int uid;
            if (Constant.userinfo != null) {

                uid = Constant.userinfo.getUid();
            } else {
                uid = 0;
            }
            if (System.currentTimeMillis() > BuildConfig.AD_TIME) {


//                loadHwAd();
                presenter.getAdEntryAll(Constant.APPID + "", 1, uid + "");
            }
            countDownTimer.start();
        }
    }

    private void initView() {

        binding.splashIvAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTO == null) {
                    return;
                }
                if (dataDTO.getType().equals("web")) {

                    MyWebActivity.startActivity(SplashActivity.this, dataDTO.getStartuppicUrl(), "详情");
                }
            }
        });
    }

    @Override
    public View initLayout() {
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public SplashContract.SplashPresenter initPresenter() {
        return new SplashPresenter();
    }

    private void initPrivacyPopup() {

        if (privacyPopup == null) {

            privacyPopup = new PrivacyPopup(this);
            privacyPopup.setCallback(new PrivacyPopup.Callback() {
                @Override
                public void yes() {


                    LocalBroadcastManager.getInstance(SplashActivity.this).sendBroadcastSync(new Intent(Constant.ACTION_INIT));

                    sp.edit().putInt(Constant.SP_KEY_PRIVACY_STATE, 1).apply();
                    privacyPopup.dismiss();

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void no() {

                    privacyPopup.dismiss();
                    finish();
                }

                @Override
                public void user() {

                    MyWebActivity.startActivity(SplashActivity.this, Constant.URL_PROTOCOLUSE, "用户协议");
                }

                @Override
                public void privacy() {

                    MyWebActivity.startActivity(SplashActivity.this, Constant.URL_PROTOCOLPRI, "隐私政策");
                }
            });
        }
        privacyPopup.showPopupWindow();
    }


    /**
     * 处理状态栏和虚拟返回键
     */
    private void dealWindow() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {

            WindowInsetsController windowInsetsController = getWindow().getInsetsController();
            windowInsetsController.hide(WindowInsets.Type.systemBars());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isAdCLick) {//点击了就直接跳转mainactivity

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {

            countDownTimer.cancel();
        }
    }

    /**
     * 计时器
     */
    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {


            if (dataDTO == null) {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {

                if (dataDTO.getType().equals("web")) {

                    webCountDownTimer.start();
                }
            }
        }
    };


    CountDownTimer webCountDownTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

            int s = (int) (millisUntilFinished / 1000);
            binding.splashTvJump.setText("跳转(" + s + ")");
        }

        @Override
        public void onFinish() {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };

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

    @Override
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {

        dataDTO = adEntryBean.getData();
        String type = dataDTO.getType();
        if (type.equals("web")) {

            Glide.with(SplashActivity.this).load("http://static3.iyuba.cn/dev" + dataDTO.getStartuppic()).into(binding.splashIvAd);
        } else if (type.equals(Constant.AD_ADS1) || type.equals(Constant.AD_ADS2) || type.equals(Constant.AD_ADS3)
                || type.equals(Constant.AD_ADS4) || type.equals(Constant.AD_ADS5)) {

            YdSpread mSplashAd = new YdSpread.Builder(SplashActivity.this)
                    .setKey("0061")
                    .setContainer(binding.splashFl)
                    .setSpreadListener(this)
                    .setCountdownSeconds(4)
                    .setSkipViewVisibility(true)
                    .build();

            mSplashAd.requestSpread();
        }
    }

    @Override
    public void onAdDisplay() {
        Log.d("adadad", "onAdDisplay");
    }

    @Override
    public void onAdClose() {

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onAdClick(String s) {

        isAdCLick = true;
        Log.d("adadad", "onAdClick");
    }

    @Override
    public void onAdFailed(YdError ydError) {

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
        Log.d("adadad", "onAdFailed");
    }
}