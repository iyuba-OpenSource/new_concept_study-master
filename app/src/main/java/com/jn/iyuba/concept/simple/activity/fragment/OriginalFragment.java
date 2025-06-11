package com.jn.iyuba.concept.simple.activity.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;
import com.iyuba.module.toolbox.DensityUtil;
import com.iyuba.module.toolbox.MD5;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.login.WxLoginActivity;
import com.jn.iyuba.concept.simple.entity.NotificationEventbus;
import com.jn.iyuba.concept.simple.model.bean.ApiWordBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.util.popup.ApiWordPopup;
import com.jn.iyuba.concept.simple.util.widget.SelectWordTextView;
import com.jn.iyuba.succinct.BuildConfig;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.activity.ContentActivity;
import com.jn.iyuba.concept.simple.activity.MyWebActivity;
import com.jn.iyuba.concept.simple.activity.login.LoginActivity;
import com.jn.iyuba.concept.simple.adapter.OriginalAdapter;
import com.jn.iyuba.succinct.databinding.FragmentOriginalBinding;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.MediaPauseEventbus;
import com.jn.iyuba.concept.simple.entity.Refresh;
import com.jn.iyuba.concept.simple.model.bean.AdEntryBean;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.presenter.home.OriginalPresenter;
import com.jn.iyuba.concept.simple.service.MediaService;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.SentenceUtil;
import com.jn.iyuba.concept.simple.util.popup.SpeedPopup;
import com.jn.iyuba.concept.simple.view.home.OriginalContract;
import com.jn.iyuba.novel.NovelConstant;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.Chapter;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.db.NovelSentence;
import com.jn.iyuba.novel.db.dao.ChapterDao;
import com.jn.iyuba.novel.db.dao.NovelSentenceDao;
import com.jn.iyuba.novel.entity.NovelRefresh;
import com.yd.saas.base.interfaces.AdViewBannerListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 原文fragment
 */
public class OriginalFragment extends BaseFragment<OriginalContract.OriginalView, OriginalContract.OriginalPresenter>
        implements OriginalContract.OriginalView, AdViewBannerListener {

    private int voaId;

    /**
     * 是否是书虫
     */
    private boolean isBookWorm = false;

    /**
     * 章节序号
     */
    private int chapterOrder;

    /**
     * 是否自动播放
     */
    private boolean isAutoPlay = true;


    private FragmentOriginalBinding fragmentOriginalBinding;

    private OriginalAdapter originalAdapter;

    private View emptyView;

    private TextView empty_tv_content;

    /**
     * 设置播放时间、总时间等
     */
    private HandlerThread handlerThread;
    private Handler handler;

    private DecimalFormat decimalFormat;


    private SpeedPopup speedPopup;

    private AdEntryBean.DataDTO dataDTO;

    //开始时间
    private String beginTime;

    private ServiceConnection serviceConnection;

    private MediaService.MusicController musicController;

    /**
     * AB段播放
     */
    private long positionA = -1;

    private long positionB = -1;

    /**
     * 关于后台播放的广播接收器
     */
    private BroadcastReceiver mbgBR;

    /**
     * 单词
     */
    private ApiWordPopup apiWordPopup;

    /**
     * ab段播放，完成
     */
    Handler abHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (musicController != null) {

                if (musicController.getCurrentPosition() > positionB) {

                    musicController.seek((int) positionA);
                }
            }
            abHandler.sendEmptyMessageDelayed(0, 200);
            return false;
        }
    });

    //通知权限请求
    private ActivityResultLauncher<String> activityResultLauncher;


    public OriginalFragment() {
    }


    public static OriginalFragment newInstance(int voaId) {

        OriginalFragment fragment = new OriginalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("VOA_ID", voaId);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static OriginalFragment newInstance(int voaId, boolean isBookWorm, int chapterOrder, boolean isAutoPlay) {

        OriginalFragment fragment = new OriginalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("VOA_ID", voaId);
        bundle.putBoolean("IS_BOOK_WORM", isBookWorm);
        bundle.putInt("CHAPTER_ORDER", chapterOrder);
        bundle.putBoolean("IS_AUTO_PLAY", isAutoPlay);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onActivityResult(Boolean result) {

                if (result) {

                    EventBus.getDefault().post(new NotificationEventbus());
                } else {
                    SharedPreferences permissionsSP = requireContext().getSharedPreferences(Constant.SP_PERMISSIONS, Context.MODE_PRIVATE);
                    //赋值1
                    permissionsSP.edit().putInt(Constant.SP_KEY_NOTIFICATIONS, 1).apply();
                    Log.d("通知权限", "未授权");
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {

            voaId = bundle.getInt("VOA_ID", 0);
            isBookWorm = bundle.getBoolean("IS_BOOK_WORM", false);
            chapterOrder = bundle.getInt("CHAPTER_ORDER", 0);
            isAutoPlay = bundle.getBoolean("IS_AUTO_PLAY", true);
        }

//        EventBus.getDefault().register(this);

        decimalFormat = new DecimalFormat("#00");
        startHandler();


        initBroadcast();
        bindServer();
    }


    /**
     * 后台播放的事件
     */
    private void initBroadcast() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_MUSIC_SWITCH_DATA);
        intentFilter.addAction(MediaService.FLAG_MUSIC_PLAY);

        mbgBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals(MediaService.FLAG_MUSIC_SWITCH_DATA)) {


                } else if (action.equals(MediaService.FLAG_MUSIC_PLAY)) {//开始播放

                    //播放进度
                    handler.sendEmptyMessage(1);

                    if (musicController != null && musicController.isPlaying()) {

                        fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.icon_ori_pause);
                    } else {

                        fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.icon_ori_play);
                    }

                    //适配android 13 的通知不弹出问题
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        SharedPreferences permissionsSP = requireContext().getSharedPreferences(Constant.SP_PERMISSIONS, Context.MODE_PRIVATE);
                        int notifications = permissionsSP.getInt(Constant.SP_KEY_NOTIFICATIONS, 0);
                        if (notifications == 1) {//拒绝了权限

                        } else {

                            NotificationManagerCompat nManagerCompat = NotificationManagerCompat.from(requireContext());
                            if (!nManagerCompat.areNotificationsEnabled()) {
                                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                            }
                        }
                    }
                    //音频时长
//                    durTime.setText(TimeUtils.milliSecToMinute((int) musicController.getMusicDuration()));
                }
            }
        };
        LocalBroadcastManager
                .getInstance(MyApplication.getContext())
                .registerReceiver(mbgBR, intentFilter);
    }

    /**
     * 绑定服务
     */
    private void bindServer() {

        Intent intent = new Intent(MyApplication.getContext(), MediaService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                musicController = (MediaService.MusicController) service;
                if (musicController != null && musicController.isPlaying()) {//正在播放

                    if (voaId == musicController.getVoaid()) {

                        handler.sendEmptyMessage(1);
                        fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.icon_ori_pause);
                    } else {

                        musicController.setCurItem(1);
                        musicController.setData(voaId, isBookWorm, chapterOrder);
                        musicController.play();
                    }
                } else if (musicController != null) {

                    if (isAutoPlay) {
                        musicController.setCurItem(1);
                    } else {

                        musicController.setCurItem(-1);
                    }
                    musicController.setData(voaId, isBookWorm, chapterOrder);
                    musicController.play();
                }

                if (musicController == null) {

                    return;
                }
                int mode = musicController.getMode();
                if (mode == MediaService.MODE_RANDOM) {

                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_random_g);
                } else if (mode == MediaService.MODE_LIST) {

                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_list_g);
                } else {

                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_single_g);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                musicController = null;
            }
        };
        MyApplication.getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

/*    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MediaPauseEventbus mediaPauseEventbus) {

        if (musicController != null && musicController.isPlaying()) {

            musicController.pause();
            fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.icon_ori_play);
//            sendPlayProgress();
        }
    }*/

    /**
     * 随机播放或者书序播放
     * 更新数据
     */
    public void updateData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            voaId = bundle.getInt("VOA_ID", 0);
            chapterOrder = bundle.getInt("CHAPTER_ORDER", 0);
        }
        if (originalAdapter != null) {

            originalAdapter.setPosition(0);
        }
        getDataAndPlay();
    }

    /**
     * 中英文与英文的切换
     */
    public void setShowCnEn() {

        if (originalAdapter == null) {

            return;
        }

        if (originalAdapter.isShowEnCn()) {

            originalAdapter.setShowEnCn(false);
        } else {

            originalAdapter.setShowEnCn(true);
        }
        originalAdapter.notifyDataSetChanged();
    }


    /**
     * 更新ui
     */
    private void startHandler() {

        handlerThread = new HandlerThread("检测播放与recler的位置");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), msg -> {

            if (musicController != null && musicController.isPlaying()) {

                int cp = (int) musicController.getCurrentPosition();
                int dur = (int) musicController.getMusicDuration();
                setViewText(getTimeStr(cp), getTimeStr(dur), cp, dur);
            }
            handler.sendEmptyMessageDelayed(1, 50);
            return false;
        });
    }


    /**
     * 设置seekbar、播放时间、总时间、adapter,滚动到指定位置
     *
     * @param cp
     * @param dur
     * @param cpInt
     * @param durInt
     */
    private synchronized void setViewText(String cp, String dur, long cpInt, long durInt) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                fragmentOriginalBinding.originalTvPlayTime.setText(cp);
                fragmentOriginalBinding.originalTvDuration.setText(dur);

                fragmentOriginalBinding.originalSb.setProgress((int) cpInt);
                fragmentOriginalBinding.originalSb.setMax((int) durInt);

                int s = (int) (cpInt / 1000);

                for (int i = 0, size = originalAdapter.getData().size(); i < size; i++) {

                    Sentence dataDTO = originalAdapter.getItem(i);
                    if (s < Double.parseDouble(dataDTO.getEndTiming()) && s > Double.parseDouble(dataDTO.getTiming())) {

                        if (originalAdapter.getPosition() != i) {

                            originalAdapter.setPosition(i);
                            moveToPosition(fragmentOriginalBinding.originalRvSentences, i);
                            originalAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }


            }
        });
    }

    /**
     * 同步播放
     *
     * @param rv
     * @param position
     */
    private void moveToPosition(RecyclerView rv, int position) {

        int firstItem = rv.getChildLayoutPosition(rv.getChildAt(0));
        int lastItem = rv.getChildLayoutPosition(rv.getChildAt(rv.getChildCount() - 1));
        if (position < firstItem || position > lastItem) {
            rv.smoothScrollToPosition(position);
        } else {
            int movePosition = position - firstItem;
            int top = rv.getChildAt(movePosition).getTop();
            rv.smoothScrollBy(0, top);
        }
    }

    /**
     * 获取播放时间
     *
     * @param mill
     * @return
     */
    private String getTimeStr(long mill) {

        long s = mill / 1000;

        long ts = s % 60;
        long tm = s / 60;


        String cpStr = decimalFormat.format(ts);
        String durStr = decimalFormat.format(tm);

        return durStr + ":" + cpStr;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        if (handler != null) {

            handler.removeMessages(1);
        }
        if (handlerThread != null) {

            handlerThread.quit();
        }

        if (serviceConnection != null) {

            MyApplication.getContext().unbindService(serviceConnection);
        }

        if (musicController != null) {

            musicController = null;
        }

        if (mbgBR != null) {

            LocalBroadcastManager
                    .getInstance(MyApplication.getContext())
                    .unregisterReceiver(mbgBR);
        }

//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        fragmentOriginalBinding = FragmentOriginalBinding.inflate(inflater, container, false);
        return fragmentOriginalBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();
        getDataAndPlay();


        if (System.currentTimeMillis() > BuildConfig.AD_TIME) {

            if (Constant.userinfo == null) {

//                loadHWAd();
                presenter.getAdEntryAll(Constant.APPID + "", 4, "0");
            } else {

//                loadHWAd();
                if (!Constant.userinfo.isVip()) {

                    presenter.getAdEntryAll(Constant.APPID + "", 4, Constant.userinfo == null ? "0" : Constant.userinfo.getUid() + "");
                }
            }
        }
    }


    private void loadHWAd() {

        BannerView bannerView = new BannerView(fragmentOriginalBinding.originalFlAd.getContext());
        // 设置广告位ID和广告尺寸，"testw6vs28auh3"为测试专用的广告位ID
        bannerView.setAdId("s7hrzb7la0");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        // 设置轮播时间间隔为60秒
        bannerView.setBannerRefresh(60);
        // 创建广告请求，加载广告
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);

        fragmentOriginalBinding.originalFlAd.removeAllViews();
        fragmentOriginalBinding.originalFlAd.addView(bannerView);
        bannerView.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                fragmentOriginalBinding.originalFlAd.setVisibility(View.GONE);

                Log.d("bannerView", "onAdClosed");
            }

            @Override
            public void onAdFailed(int i) {
                super.onAdFailed(i);
                fragmentOriginalBinding.originalFlAd.setVisibility(View.GONE);
                Log.d("bannerView", "onAdFailed" + i);
            }

            @Override
            public void onAdLeave() {
                super.onAdLeave();
                Log.d("bannerView", "onAdLeave");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d("bannerView", "onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                fragmentOriginalBinding.originalFlAd.setVisibility(View.VISIBLE);
                Log.d("bannerView", "onAdLoaded");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.d("bannerView", "onAdClicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.d("bannerView", "onAdImpression");
            }
        });
    }


    /**
     * 获取句子和音频地址
     */
    private void getDataAndPlay() {


        if (isBookWorm) {//书虫

            getDataAndPlayForBookWorm();
        } else {

            getDataAndPlayForConcept();
        }
    }

    /**
     * 书虫获取数据、获取链接地址
     */
    private void getDataAndPlayForBookWorm() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                //获取句子数据
                AppDatabase appDatabase = NovelDB.getInstance().getDB();
                NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
                List<NovelSentence> novelSentenceList = novelSentenceDao.getDataByPrimaryKeys(NovelConstant.novelBook.getTypes(), voaId + "", chapterOrder + "",
                        NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel());
                List<Sentence> sentenceList = SentenceUtil.convert(novelSentenceList);
                //获取音频
                ChapterDao chapterDao = appDatabase.chapterDao();
                Chapter chapter = chapterDao.getSingleByPrimaryKeys(voaId + "", NovelConstant.novelBook.getTypes(), NovelConstant.novelBook.getLevel(),
                        NovelConstant.novelBook.getOrder_number(), chapterOrder + "");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        originalAdapter.setNewData(sentenceList);

                        if (musicController == null) {

                            bindServer();
                        } else {

                            musicController.setData(voaId, isBookWorm, chapterOrder);
                            musicController.play();
                        }
                        /*if (sentenceList.size() != 0 && chapter != null) {

                            String url;
                            if (Constant.userinfo != null && Constant.userinfo.isVip()) {

                                url = NovelConstant.STATICVIP2_URL + chapter.getSound();
                            } else {

                                url = NovelConstant.STATIC2_URL + chapter.getSound();
                            }
                            initMediaPlayer(url);
                        }*/
                    }
                });
            }
        }).start();
    }

    /**
     * 新概念获取数据
     */
    private void getDataAndPlayForConcept() {

        List<Sentence> sentences;
        if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

            sentences = LitePal.where("voaid = ?", voaId + "").find(Sentence.class);
        } else {

            if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                sentences = LitePal.where("voaid = ?", voaId + "").find(Sentence.class);
            } else {

                sentences = LitePal.where("voaid = ?", (voaId * 10) + "").find(Sentence.class);
            }
        }

        originalAdapter.setNewData(sentences);


        if (musicController == null) {

            bindServer();
        } else {

            musicController.setData(voaId, isBookWorm, chapterOrder);
            musicController.play();
        }
    }

    private void initOperation() {

        originalAdapter = new OriginalAdapter(R.layout.item_original, new ArrayList<>());
        fragmentOriginalBinding.originalRvSentences.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentOriginalBinding.originalRvSentences.setAdapter(originalAdapter);
        originalAdapter.setCallback(new OriginalAdapter.Callback() {
            @Override
            public void getWord(String word, int position) {

                List<Sentence> sentenceList = originalAdapter.getData();
                for (int i = 0; i < sentenceList.size(); i++) {

                    if (position != i) {

                        originalAdapter.notifyItemChanged(i);
                    }
                }
                presenter.apiWord(word);
            }
        });

        //没有数据或者报错显示的控件
        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_view, null);
        empty_tv_content = emptyView.findViewById(R.id.empty_tv_content);
        empty_tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getConceptSentence(voaId);
            }
        });

        //seekbar
        fragmentOriginalBinding.originalSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (musicController != null) {

                    musicController.seek(seekBar.getProgress());
                }
            }
        });
        //播放或者暂停
        fragmentOriginalBinding.originalIvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }

                if (musicController.isPlaying()) {

//                        handlerThread.quit();
                    musicController.pause();
                    fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.icon_ori_play);

//                    sendPlayProgress();

                } else {

//                        startHandler();
//                        handler.sendEmptyMessage(1);
                    musicController.start();
                    fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.icon_ori_pause);
                    beginTime = DateUtil.getCurTime();
                }
            }
        });

        //速度
        fragmentOriginalBinding.originalFlSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {

                    if (Constant.userinfo.isVip()) {

                        initSpeedPopup();
                    } else {

                        toast("调速功能需要购买会员");
                    }
                }
            }
        });
        //模式
        fragmentOriginalBinding.originalIvMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }
                int mode = musicController.getMode();
                if (mode >= 3) {

                    mode = 1;
                } else {
                    mode++;
                }
                musicController.setPlayMode(mode);
                if (mode == 1) {

                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_random_g);
                    toast("列表随机播放");
                } else if (mode == 2) {

                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_list_g);
                    toast("列表循环播放");
                } else {

                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_single_g);
                    toast("单曲循环播放");
                }
            }
        });
        //广告
        fragmentOriginalBinding.originalIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentOriginalBinding.originalFlAd.setVisibility(View.GONE);
            }
        });
        fragmentOriginalBinding.originalIvAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTO == null) {

                    return;
                }
                MyWebActivity.startActivity(requireActivity(), dataDTO.getStartuppicUrl(), "详情");
            }
        });
        //ab
        fragmentOriginalBinding.originalIvAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (musicController == null) {

                    return;
                }

                if (positionA == -1 && positionB == -1) {

                    positionA = musicController.getCurrentPosition();
                    toast("已记录A点，再次点击开始区间播放");
                } else if (positionA != -1 && positionB == -1) {

                    positionB = musicController.getCurrentPosition();
                    abHandler.sendEmptyMessage(0);
                    toast("开始区间播放");
                } else {

                    abHandler.removeMessages(0);
                    toast("取消区间播放");
                    positionA = -1;
                    positionB = -1;
                }
            }
        });
        //下一个
        fragmentOriginalBinding.originalIvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                next();
            }
        });
        //上一个
        fragmentOriginalBinding.originalIvPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pre();
            }
        });
    }

    /**
     * 下一曲
     */
    private void next() {


        if (isBookWorm) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    ChapterDao chapterDao = appDatabase.chapterDao();
                    Chapter chapter = chapterDao.getChapterForOrder(NovelConstant.novelBook.getTypes()
                            , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number(), voaId + "");

                    if (chapter == null) {


                        chapter = chapterDao.getFirstChapterForBook(NovelConstant.novelBook.getTypes()
                                , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number());
                    }

                    Chapter finalChapter = chapter;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            ContentActivity contentActivity = (ContentActivity) requireActivity();
                            contentActivity.updateData(Integer.parseInt(finalChapter.getVoaid()), Integer.parseInt(finalChapter.getChapter_order()));
                        }
                    });
                }
            }).start();
        } else {

            List<Title> titleList = null;
            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                titleList = LitePal.where("voaid > ? and bookid = ?", voaId + "", Constant.book.getId()).order("voaid").limit(1).find(Title.class);
                if (titleList.size() == 0) {

                    titleList = LitePal.where("bookid = ?", Constant.book.getId()).order("voaid").limit(1).find(Title.class);
                }
            } else {

                titleList = LitePal.where("voaid > ? and bookid = ? and language = ?", voaId + "", Constant.book.getId(), Constant.LANGUAGE)
                        .order("voaid")
                        .limit(1)
                        .find(Title.class);
                if (titleList.size() == 0) {

                    titleList = LitePal.where("bookid = ? and language = ?", Constant.book.getId(), Constant.LANGUAGE)
                            .order("voaid")
                            .limit(1)
                            .find(Title.class);
                }
            }
            if (titleList.size() == 0) {

                return;
            }
            int mVoaid = Integer.parseInt(titleList.get(0).getVoaId());
            ContentActivity contentActivity = (ContentActivity) requireActivity();
            contentActivity.updateData(mVoaid, -1);
        }
    }


    /**
     * 上一曲
     */
    private void pre() {


        if (isBookWorm) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    ChapterDao chapterDao = appDatabase.chapterDao();
                    Chapter chapter = chapterDao.getChapterPre(NovelConstant.novelBook.getTypes()
                            , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number(), voaId + "");

                    if (chapter == null) {//没有数据则拿去最后一个

                        chapter = chapterDao.getLastChapterForBook(NovelConstant.novelBook.getTypes()
                                , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number());
                    }

                    Chapter finalChapter = chapter;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            ContentActivity contentActivity = (ContentActivity) requireActivity();
                            contentActivity.updateData(Integer.parseInt(finalChapter.getVoaid()), Integer.parseInt(finalChapter.getChapter_order()));
                        }
                    });
                }
            }).start();
        } else {

            List<Title> titleList = null;
            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                titleList = LitePal.where("voaid < ? and bookid = ?", voaId + "", Constant.book.getId()).order("voaid desc").limit(1).find(Title.class);
                if (titleList.size() == 0) {

                    titleList = LitePal.where("bookid = ?", Constant.book.getId()).order("voaid desc").limit(1).find(Title.class);
                }
            } else {

                titleList = LitePal.where("voaid < ? and bookid = ? and language = ?", voaId + "", Constant.book.getId(), Constant.LANGUAGE)
                        .order("voaid desc")
                        .limit(1)
                        .find(Title.class);
                if (titleList.size() == 0) {

                    titleList = LitePal.where("bookid = ? and language = ?", Constant.book.getId(), Constant.LANGUAGE)
                            .order("voaid desc")
                            .limit(1)
                            .find(Title.class);
                }
            }
            if (titleList.size() == 0) {

                return;
            }
            int mVoaid = Integer.parseInt(titleList.get(0).getVoaId());
            ContentActivity contentActivity = (ContentActivity) requireActivity();
            contentActivity.updateData(mVoaid, -1);
        }
    }


    /**
     * 上传播放进度
     */
   /* private void sendPlayProgress() {

        if (Constant.userinfo == null) {

            return;
        }

        int testNumber = 0;

        if (musicController == null) {

            return;
        }

        int cPosition = (int) musicController.getCurrentPosition();
        List<Sentence> sentenceList = originalAdapter.getData();
        for (int i = 0; i < sentenceList.size(); i++) {

            Sentence sentence = sentenceList.get(i);
            int sTime = (int) (Double.parseDouble(sentence.getTiming()) * 1000);
            int eTime;
            if (i == (sentenceList.size() - 1)) {

                eTime = (int) (Double.parseDouble(sentence.getEndTiming()) * 1000);
            } else {

                eTime = (int) (Double.parseDouble(sentenceList.get(i + 1).getTiming()) * 1000);
            }
            if (cPosition >= sTime && cPosition <= eTime) {

                testNumber = i;
                break;
            }
        }
        int endFlag = 0;
        if (testNumber == (sentenceList.size() - 1)) {

            endFlag = 1;
        }

        String type;
        if (isBookWorm) {//处理type,书虫

            type = NovelConstant.novelBook.getTypes();
        } else {

            type = Constant.TYPE;
        }

        String uidStr = Constant.userinfo.getUid() + "";
        String sign = MD5.getMD5ofStr(uidStr + beginTime + DateUtil.getCurTime());
        presenter.updateStudyRecordNew("json", uidStr, beginTime, DateUtil.getCurTime(), type, "1", "0",
                "android", Constant.APPID + "", "", voaId + "", sign, endFlag, testNumber,
                Integer.parseInt(Constant.book.getId()), Constant.LANGUAGE);

        if (isBookWorm) {

            int finalTestNumber = testNumber;
            int finalEndFlag = endFlag;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    ChapterDao chapterDao = appDatabase.chapterDao();
                    chapterDao.updateProgress(finalTestNumber + "", finalEndFlag + "", voaId + "", NovelConstant.novelBook.getTypes(),
                            NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number(), chapterOrder + "");
                    EventBus.getDefault().post(new NovelRefresh(voaId));
                }
            }).start();

        } else {

            //保存播放进度到数据库
            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {//青少版

                List<Title> titleList = LitePal.where("voaid = ?", voaId + "").find(Title.class);
                if (titleList.size() > 0) {

                    titleList.get(0).setTestNumber(testNumber);
                    titleList.get(0).setEndFlg(endFlag);
                    titleList.get(0).updateAll("voaid = ?", voaId + "");
                }
            } else {

                List<Title> titleList = LitePal.where("voaid = ? and  language = ?", voaId + "", Constant.LANGUAGE).find(Title.class);
                if (titleList.size() > 0) {

                    titleList.get(0).setTestNumber(testNumber);
                    titleList.get(0).setEndFlg(endFlag);
                    titleList.get(0).updateAll("voaid = ? and  language = ?", voaId + "", Constant.LANGUAGE);
                }
            }
            //触发更新首页
            EventBus.getDefault().post(new Refresh(voaId));
        }
    }*/
    private void initSpeedPopup() {

        if (speedPopup == null) {

            speedPopup = new SpeedPopup(getContext());
            speedPopup.setCallback(new SpeedPopup.Callback() {
                @Override
                public void getChoose(float speedFloat) {


                    if (musicController == null) {

                        return;
                    }

                    musicController.setPlaySpeed(speedFloat);
                    fragmentOriginalBinding.originalTvSpeed.setText(speedFloat + "x");
                    speedPopup.dismiss();
                }
            });
        }
        speedPopup.setChoosed(fragmentOriginalBinding.originalTvSpeed.getText().toString());
        speedPopup.showPopupWindow();
    }


    @Override
    protected OriginalContract.OriginalPresenter initPresenter() {
        return new OriginalPresenter();
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


    @Override
    public void getSentenceComplete(LessonDetailBean lessonDetailBean) {


        if (originalAdapter.getEmptyView() == null) {

            originalAdapter.setEmptyView(emptyView);
        }

        if (lessonDetailBean == null) {

            empty_tv_content.setText("连接超时点击重试");
        } else {


            if (lessonDetailBean.getSize() == 0) {

                empty_tv_content.setText("没有更多数据，点击重试");
            } else {


                originalAdapter.setNewData(lessonDetailBean.getData());
                /*if (lessonDetailBean.getData().size() != 0) {

                    String url = "http://static2.iyuba.cn/newconcept/";
                    int a = voaId / 1000;
                    int b = voaId - voaId / 1000 * 1000;
                    initMediaPlayer(url + a + "_" + b + ".mp3");
                }*/
            }
        }
    }

    @Override
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {

        AdEntryBean.DataDTO dataDTO = adEntryBean.getData();
        String type = dataDTO.getType();
        if (type.equals("web")) {

            this.dataDTO = dataDTO;
            fragmentOriginalBinding.originalFlAd.setVisibility(View.VISIBLE);
            Glide.with(requireActivity()).load("http://static3.iyuba.cn/dev" + dataDTO.getStartuppic()).into(fragmentOriginalBinding.originalIvAd);
        } else if (type.equals(Constant.AD_ADS1) || type.equals(Constant.AD_ADS2) || type.equals(Constant.AD_ADS3)
                || type.equals(Constant.AD_ADS4) || type.equals(Constant.AD_ADS5)) {

            DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = DensityUtil.dp2px(requireContext(), 60);

            YdBanner mBanner = new YdBanner.Builder(requireContext())
                    .setKey("0062")
                    .setWidth(width)
                    .setHeight(height)
                    .setMaxTimeoutSeconds(5)
                    .setBannerListener(this)
                    .build();

            mBanner.requestBanner();
        }
    }

    @Override
    public void getWord(ApiWordBean apiWordBean) {

        initApiWordPopup(apiWordBean);
    }

    @Override
    public void collectWord(WordCollectBean wordCollectBean) {

        toast("收藏成功");
    }


    /**
     * 单词详情弹窗
     *
     * @param apiWordBean
     */
    private void initApiWordPopup(ApiWordBean apiWordBean) {

        if (apiWordPopup == null && getContext() != null) {

            apiWordPopup = new ApiWordPopup(getContext());
            apiWordPopup.setCallback(new ApiWordPopup.Callback() {
                @Override
                public void add() {

                    if (Constant.userinfo == null) {

                        startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                    } else {

                        ApiWordBean apiWordBean1 = apiWordPopup.getWord();
                        presenter.updateWord("Iyuba", "insert", apiWordBean1.getKey(), Constant.userinfo.getUid() + "", "json");
                    }
                }

                @Override
                public void cancel() {

                    apiWordPopup.dismiss();
                }
            });
        }
        apiWordPopup.setWord(apiWordBean);
        apiWordPopup.showPopupWindow();
    }

    @Override
    public void onReceived(View view) {

        fragmentOriginalBinding.originalFlAd.removeAllViews();
        fragmentOriginalBinding.originalFlAd.addView(view);
        fragmentOriginalBinding.originalFlAd.setVisibility(View.VISIBLE);
        Log.d("banner111", "onReceived");
    }

    @Override
    public void onAdExposure() {


        Log.d("banner111", "onAdExposure");
    }

    @Override
    public void onAdClick(String s) {

        Log.d("banner111", "onAdClick");
    }

    @Override
    public void onClosed() {

        fragmentOriginalBinding.originalFlAd.setVisibility(View.GONE);
        Log.d("banner111", "onClosed");
    }

    @Override
    public void onAdFailed(YdError ydError) {

        Log.d("banner111", "onAdFailed");
    }
}