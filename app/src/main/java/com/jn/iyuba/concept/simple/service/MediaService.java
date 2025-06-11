package com.jn.iyuba.concept.simple.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iyuba.headlinelibrary.event.HeadlinePlayEvent;
import com.iyuba.imooclib.event.ImoocPlayEvent;
import com.iyuba.module.toolbox.MD5;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.entity.MediaPauseEventbus;
import com.jn.iyuba.concept.simple.entity.NotificationEventbus;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.ChapterEventbus;
import com.jn.iyuba.concept.simple.presenter.home.MediaPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.SentenceUtil;
import com.jn.iyuba.concept.simple.view.MediaContract;
import com.jn.iyuba.novel.NovelConstant;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.Chapter;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.db.NovelSentence;
import com.jn.iyuba.novel.db.dao.ChapterDao;
import com.jn.iyuba.novel.db.dao.NovelSentenceDao;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.listener.OnGetOaidListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 新闻音频播放器
 */
public class MediaService extends Service implements MediaContract.MediaView {


    private int voaid;

    private boolean isBookWorm = false;

    private int chapterOrder = 0;

    private static final String TAG = "MediaService";
    private MediaPlayer mPlayer;

    /**
     * 随机播放
     */
    public final static int MODE_RANDOM = 1;

    /**
     * 列表播放
     */
    public final static int MODE_LIST = 2;

    /**
     * 单曲循环
     */
    public final static int MODE_SINGLE = 3;

    /**
     * 默认播放模式为3
     */
    private int mode = 3;

    /**
     * homefragment与ContentOriFragment的暂停与播放
     */
    public final static String FLAG_MUSIC_PLAY = "com.jn.iyuba.succinct.play";

    /**
     * 通知的播放与暂停
     */
    public final static String FLAG_MUSIC_PLAY_NOTICE = "com.jn.iyuba.succinct.play.notice";


    /**
     * 通知的关闭按钮
     */
    public final static String FLAG_MUSIC_PLAY_CLOSE = "com.jn.iyuba.succinct.close.notice";

    /**
     * 切换音频源触发
     */
    public final static String FLAG_MUSIC_SWITCH_DATA = "com.jn.iyuba.succinct.switch_data";

    private NotificationManagerCompat nManagerCompat;

    /**
     * 开始时间
     */
    private String beginTime;


    /**
     * 网络请求
     */
    private MediaPresenter mediaPresenter;

    /**
     * 是不是原文界面  1为原文界面
     * 如果是原文界面则直接播放
     */
    private int curItem = -1;

    //binder
    private MusicController musicController = new MusicController();


    public class MusicController extends Binder {


        public void setData(int voaid, boolean isBookWorm, int chapterOrder) {

            MediaService.this.voaid = voaid;
            MediaService.this.isBookWorm = isBookWorm;
            MediaService.this.chapterOrder = chapterOrder;
        }


        public int getVoaid() {

            return voaid;
        }

        public boolean isBookWorm() {

            return isBookWorm;
        }

        public int getChapterOrder() {

            return chapterOrder;
        }

        public void setCurItem(int curItem) {

            MediaService.this.curItem = curItem;
        }

        public Title getCurData() {

            if (isBookWorm) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        AppDatabase appDatabase = NovelDB.getInstance().getDB();
                        ChapterDao chapterDao = appDatabase.chapterDao();
                        Chapter chapter = chapterDao.getSingleByPrimaryKeys(voaid + "", NovelConstant.novelBook.getTypes()
                                , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number(), chapterOrder + "");
                        if (chapter != null) {

                            ChapterEventbus chapterEventbus = new ChapterEventbus(chapter);
                            EventBus.getDefault().post(chapterEventbus);
                        }
                    }
                }).start();

            } else {

                if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                    List<Title> titleList = LitePal.where("voaid = ? and bookid = ?", voaid + "", Constant.book.getId()).find(Title.class);
                    if (titleList.size() > 0) {

                        return titleList.get(0);
                    }
                } else {

                    List<Title> titleList = LitePal
                            .where("voaid = ? and bookid = ? and language = ?", voaid + "", Constant.book.getId(), Constant.LANGUAGE)
                            .find(Title.class);
                    if (titleList.size() > 0) {

                        return titleList.get(0);
                    }
                }
            }
            return null;
        }

        public void play() {


            if (isBookWorm) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String url = null;
                        AppDatabase appDatabase = NovelDB.getInstance().getDB();
                        ChapterDao chapterDao = appDatabase.chapterDao();
                        Chapter chapter = chapterDao.getSingleByPrimaryKeys(voaid + "", NovelConstant.novelBook.getTypes()
                                , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number(), chapterOrder + "");
                        if (Constant.userinfo != null && Constant.userinfo.isVip()) {

                            url = NovelConstant.STATICVIP2_URL + chapter.getSound();
                        } else {

                            url = NovelConstant.STATIC2_URL + chapter.getSound();
                        }
                        String finalUrl = url;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    if (mPlayer.isPlaying()) {
                                        mPlayer.pause();
                                        mPlayer.stop();
                                    }
                                    mPlayer.reset();
                                    mPlayer.setDataSource(getApplication(), Uri.parse(finalUrl));
                                    mPlayer.prepareAsync();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();

            } else {

                String url = null;
                if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                    List<Title> titleList = LitePal.where("voaid = ? and bookid = ?", voaid + "", Constant.book.getId()).find(Title.class);
                    Title title = titleList.get(0);

                    //检测是否下载，优先使用本地文件
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), title.getDownloadName());
                    if (file.exists()) {//存在

                        url = file.getAbsolutePath();
                    } else {

                        url = title.getSound().replace("voa", "voa/sentence").replace(title.getVoaId() + ".mp3", title.getVoaId() + "/" + title.getVoaId() + ".mp3");
                    }
                } else {

                    List<Title> titleList = LitePal
                            .where("voaid = ? and bookid = ? and language = ?", voaid + "", Constant.book.getId(), Constant.LANGUAGE)
                            .find(Title.class);
                    if (titleList.size() > 0) {

                        Title title = titleList.get(0);
                        File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), title.getDownloadName());
                        if (file.exists()) {

                            url = file.getAbsolutePath();
                        } else {

                            if (Constant.LANGUAGE.equalsIgnoreCase("US")) {

                                url = "http://static2.iyuba.cn/newconcept/";
                                int a = voaid / 1000;
                                int b = voaid - voaid / 1000 * 1000;
                                url = url + a + "_" + b + ".mp3";
                            } else {

                                int a = voaid / 1000;
                                int b = voaid - voaid / 1000 * 1000;
                                url = "http://static2.iyuba.cn/newconcept/british/" + a + "/" + a + "_" + b + ".mp3";
                            }
                        }
                    }
                }
                try {
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                        mPlayer.stop();
                    }
                    mPlayer.reset();
                    if (url != null) {//处理空指针的问题

                        if (url.startsWith("http")) {

                            mPlayer.setDataSource(url);

                        } else {

                            mPlayer.setDataSource(getApplication(), Uri.parse(url));
                        }
                        mPlayer.prepareAsync();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean isPlaying() {

            if (mPlayer == null) {

                return false;
            } else {
                return mPlayer.isPlaying();
            }
        }

        public void pause() {

            mPlayer.pause();  //暂停音乐
            //暂停时间

            //上传播放进度
            sendPlayCurrent(0);

            //触发更新播放状态
            LocalBroadcastManager
                    .getInstance(MyApplication.getContext())
                    .sendBroadcastSync(new Intent(MediaService.FLAG_MUSIC_PLAY));


            //通知
            notificationData();
        }

        public long getMusicDuration() {

            if (mPlayer == null) {

                return 0;
            } else {

                return mPlayer.getDuration();  //获取文件的总长度
            }
        }

        public long getCurrentPosition() {

            if (mPlayer == null) {

                return 0;
            } else {

                return mPlayer.getCurrentPosition();  //获取当前播放进度
            }
        }

        public void seek(int position) {

            if (mPlayer != null) {

                mPlayer.seekTo(position);  //重新设定播放进度
            }
        }

        public void start() {

            mPlayer.start();
            beginTime = DateUtil.getCurTime();

            //触发更新播放状态
            LocalBroadcastManager
                    .getInstance(MyApplication.getContext())
                    .sendBroadcastSync(new Intent(MediaService.FLAG_MUSIC_PLAY));
            notificationData();
        }

        public boolean setPlaySpeed(float speed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PlaybackParams params = mPlayer.getPlaybackParams();
                params.setSpeed(speed);
                mPlayer.setPlaybackParams(params);
                return true;
            }
            return false;
        }

        /**
         * 设置播放模式
         *
         * @param mode
         */
        public void setPlayMode(int mode) {

            MediaService.this.mode = mode;
            //触发更新播放状态
            LocalBroadcastManager
                    .getInstance(MyApplication.getContext())
                    .sendBroadcastSync(new Intent(MediaService.FLAG_MUSIC_PLAY));
        }

        /**
         * 获取模式
         *
         * @return
         */
        public int getMode() {

            return mode;
        }
    }

    /**
     * 当绑定服务的时候，自动回调这个方法
     * 返回的对象可以直接操作Service内部的内容
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {

        return musicController;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        musicController = null;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        mode = MODE_SINGLE;
        mediaPresenter = new MediaPresenter();
        mediaPresenter.attchView(this);

        initMediaPlayer();
        //广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_MUSIC_PLAY);
        intentFilter.addAction(MediaService.FLAG_MUSIC_PLAY_NOTICE);
        intentFilter.addAction(MediaService.FLAG_MUSIC_PLAY_CLOSE);
        registerReceiver(new MusicBroadcast(), intentFilter);
    }

    /**
     * 将播放进度发送到服务器
     */
    private void sendPlayCurrent(int endflag) {

        String uid = Constant.userinfo == null ? "0" : Constant.userinfo.getUid() + "";
        if (!uid.equals("0")) {

            int currentPosition;
            if (endflag == 0) {//是否播放完成

                currentPosition = mPlayer.getCurrentPosition();
            } else {

                currentPosition = mPlayer.getDuration();
            }
            String sign = MD5.getMD5ofStr(uid + beginTime + DateUtil.getCurTime());

            if (isBookWorm) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //获取句子数据
                        AppDatabase appDatabase = NovelDB.getInstance().getDB();
                        NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
                        List<NovelSentence> novelSentenceList = novelSentenceDao.getDataByPrimaryKeys(NovelConstant.novelBook.getTypes(), voaid + "", chapterOrder + "",
                                NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel());
                        List<Sentence> sentenceList = SentenceUtil.convert(novelSentenceList);

                        //计算当前播放到第几句
                        int index = 0;
                        for (int i = 0; i < sentenceList.size(); i++) {

                            Sentence sentence = sentenceList.get(i);
                            int sTime = (int) (Double.parseDouble(sentence.getTiming()) * 1000);
                            int eTime = (int) (Double.parseDouble(sentence.getEndTiming()) * 1000);
                            if (currentPosition >= sTime && currentPosition <= eTime) {

                                index = i;
                                break;
                            }
                        }
                        int endFlag = 0;
                        if (index == (sentenceList.size() - 1)) {

                            endFlag = 1;
                        }

                        //获取oaid
                        int finalIndex = index;
                        int finalEndFlag = endFlag;
                        UMConfigure.getOaid(getApplicationContext(), new OnGetOaidListener() {
                            @Override
                            public void onGetOaid(String s) {

                                String uidStr = Constant.userinfo == null ? "0" : Constant.userinfo.getUid() + "";
                                mediaPresenter.updateStudyRecordNew("json", uidStr, beginTime, DateUtil.getCurTime(), NovelConstant.novelBook.getTypes(), "1", "0",
                                        "android", Constant.APPID + "", "", voaid + "", sign, finalEndFlag, finalIndex,
                                        Integer.parseInt(Constant.book.getId()), Constant.LANGUAGE, 1);
                            }
                        });
                    }
                }).start();

            } else {

                List<Sentence> sentences;
                if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                    sentences = LitePal.where("voaid = ?", voaid + "").find(Sentence.class);
                } else {

                    if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                        sentences = LitePal.where("voaid = ?", voaid + "").find(Sentence.class);
                    } else {

                        sentences = LitePal.where("voaid = ?", (voaid * 10) + "").find(Sentence.class);
                    }
                }
                //计算当前播放到第几句
                int index = 0;
                for (int i = 0; i < sentences.size(); i++) {

                    Sentence sentence = sentences.get(i);
                    int sTime = (int) (Double.parseDouble(sentence.getTiming()) * 1000);
                    int eTime = (int) (Double.parseDouble(sentence.getEndTiming()) * 1000);
                    if (currentPosition >= sTime && currentPosition <= eTime) {

                        index = i;
                        break;
                    }
                }

                int endFlag = 0;
                if (index == (sentences.size() - 1)) {

                    endFlag = 1;
                }
                //获取oaid
                int finalIndex = index;
                int finalEndFlag = endFlag;
                UMConfigure.getOaid(getApplicationContext(), new OnGetOaidListener() {
                    @Override
                    public void onGetOaid(String s) {

                        String uidStr = Constant.userinfo == null ? "0" : Constant.userinfo.getUid() + "";
                        mediaPresenter.updateStudyRecordNew("json", uidStr, beginTime, DateUtil.getCurTime(), Constant.TYPE, "1", "0",
                                "android", Constant.APPID + "", "", voaid + "", sign, finalEndFlag, finalIndex,
                                Integer.parseInt(Constant.book.getId()), Constant.LANGUAGE, 1);
                    }
                });
            }
        }

    }

    /**
     * 初始化播放器
     */
    private void initMediaPlayer() {

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(false);
        mPlayer.setOnPreparedListener(mp -> {

            beginTime = DateUtil.getCurTime();
            if (curItem == 1) {
                mPlayer.start();

                //弹通知
                notificationData();
            } else {//如果curItem不等于1则将curItem置为1，保证播放模式功能（顺序和随机）正常使用
                curItem = 1;
            }

            //更新播放状态
            LocalBroadcastManager
                    .getInstance(MyApplication.getContext())
                    .sendBroadcastSync(new Intent(MediaService.FLAG_MUSIC_PLAY));
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                //上传播放进度
                sendPlayCurrent(1);

                getNextData();
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }


    /**
     * 获取下一条数据
     */
    public void getNextData() {

        if (mode == 1) {//列表随机播放

            int mVoaid;

            if (isBookWorm) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Chapter chapter;
                        do {

                            AppDatabase appDatabase = NovelDB.getInstance().getDB();
                            ChapterDao chapterDao = appDatabase.chapterDao();
                            chapter = chapterDao.getChapterForRandom(NovelConstant.novelBook.getTypes()
                                    , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number());
                        } while (chapter.getVoaid().equals(voaid + ""));

                        //触发contentActivity中的广播接收器
                        Intent intent = new Intent(Constant.BROADCAST_CHAPTER);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DATA", chapter);
                        intent.putExtras(bundle);
                        LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(intent);
                    }
                }).start();

            } else {

                do {
                    if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                        List<Title> titleList = LitePal.where("bookid = ?", Constant.book.getId())
                                .order("RANDOM()")
                                .limit(1)
                                .find(Title.class);
                        mVoaid = Integer.parseInt(titleList.get(0).getVoaId());
                    } else {

                        List<Title> titleList = LitePal.where("bookid = ? and language = ?", Constant.book.getId(), Constant.LANGUAGE)
                                .order("RANDOM()")
                                .limit(1)
                                .find(Title.class);
                        mVoaid = Integer.parseInt(titleList.get(0).getVoaId());
                    }
                } while (voaid == mVoaid);

                Intent intent = new Intent(Constant.BROADCAST_TITLE);
                Bundle bundle = new Bundle();
                bundle.putInt("DATA", mVoaid);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(intent);
            }
        } else if (mode == 2) {//列表顺序播放


            if (isBookWorm) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        AppDatabase appDatabase = NovelDB.getInstance().getDB();
                        ChapterDao chapterDao = appDatabase.chapterDao();
                        Chapter chapter = chapterDao.getChapterForOrder(NovelConstant.novelBook.getTypes()
                                , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number(), voaid + "");

                        if (chapter == null) {


                            chapter = chapterDao.getFirstChapterForBook(NovelConstant.novelBook.getTypes()
                                    , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number());
                        }

                        //触发contentActivity中的广播接收器
                        Intent intent = new Intent(Constant.BROADCAST_CHAPTER);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DATA", chapter);
                        intent.putExtras(bundle);
                        LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(intent);
                    }
                }).start();
            } else {

                List<Title> titleList = null;
                if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                    titleList = LitePal.where("voaid > ? and bookid = ?", voaid + "", Constant.book.getId()).order("voaid").limit(1).find(Title.class);
                    if (titleList.size() == 0) {

                        titleList = LitePal.where("bookid = ?", Constant.book.getId()).order("voaid").limit(1).find(Title.class);
                    }
                } else {

                    titleList = LitePal.where("voaid > ? and bookid = ? and language = ?", voaid + "", Constant.book.getId(), Constant.LANGUAGE)
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
                Intent intent = new Intent(Constant.BROADCAST_TITLE);
                Bundle bundle = new Bundle();
                bundle.putInt("DATA", mVoaid);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(MyApplication.getContext()).sendBroadcast(intent);
            }
        } else {

            if (mPlayer != null) {

                mPlayer.start();
//                fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.icon_ori_pause);
                beginTime = DateUtil.getCurTime();
            }
        }
    }

    /**
     * 检测通知开关
     */
    private void checkNotificationSwitch() {

        if (nManagerCompat == null) {

            nManagerCompat = NotificationManagerCompat.from(this);
        }
        boolean isOpen = nManagerCompat.areNotificationsEnabled();

        if (isOpen) {//如果打开了就获取数据

            notificationData();
        }
    }

    /**
     * 初始化通知
     */
    private void initNotification(String title, int playImgRes, String bgUrl) {


        if (nManagerCompat == null) {

            nManagerCompat = NotificationManagerCompat.from(this);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {//android8.0及以上的通道

            NotificationChannel notificationChannel = new NotificationChannel("MediaServiceChannel", "MediaServiceChannel", NotificationManager.IMPORTANCE_DEFAULT);
            nManagerCompat.createNotificationChannel(notificationChannel);
        }


        Intent intent = new Intent(MediaService.FLAG_MUSIC_PLAY_NOTICE);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            pendingIntent
                    = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {

            pendingIntent
                    = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Intent closeIntent = new Intent(MediaService.FLAG_MUSIC_PLAY_CLOSE);
        PendingIntent closePendingIntent
                = PendingIntent.getBroadcast(this, 200, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_music);

        Notification notification;

        if (playImgRes == R.mipmap.icon_ori_pause) {//正在播放

            notification = new NotificationCompat.Builder(getApplicationContext(), "MediaServiceChannel")
                    .setCustomContentView(remoteViews)
                    .setSmallIcon(R.mipmap.logo)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setOngoing(true)
                    .build();
        } else {

            notification = new NotificationCompat.Builder(getApplicationContext(), "MediaServiceChannel")
                    .setCustomContentView(remoteViews)
                    .setSmallIcon(R.mipmap.logo)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setOngoing(false)
                    .build();
        }

        remoteViews.setOnClickPendingIntent(R.id.music_iv_but, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.music_iv_x, closePendingIntent);
        remoteViews.setImageViewResource(R.id.music_iv_but, playImgRes);
        if (title != null) {

            remoteViews.setTextViewText(R.id.music_tv_title, title);
        }
        nManagerCompat.notify(1, notification);

        if (bgUrl != null) {

            Glide.with(getBaseContext()).asBitmap().load(bgUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            //加载完图片再更新
                            if (mPlayer != null) {

                                if (mPlayer.isPlaying()) {

                                    Notification ntfc = bitmapNotification(title, R.mipmap.icon_ori_pause, resource);
                                    nManagerCompat.notify(1, ntfc);
                                } else {

                                    Notification ntfc = bitmapNotification(title, R.mipmap.icon_ori_play, resource);
                                    nManagerCompat.notify(1, ntfc);
                                }
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    private Notification bitmapNotification(String title, int playImgRes, Bitmap bitmap) {


        if (nManagerCompat == null) {

            nManagerCompat = NotificationManagerCompat.from(this);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {//android8.0及以上的通道

            NotificationChannel notificationChannel = new NotificationChannel("MediaServiceChannel", "MediaServiceChannel", NotificationManager.IMPORTANCE_DEFAULT);
            nManagerCompat.createNotificationChannel(notificationChannel);
        }


        Intent intent = new Intent(MediaService.FLAG_MUSIC_PLAY_NOTICE);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        Intent closeIntent = new Intent(MediaService.FLAG_MUSIC_PLAY_CLOSE);
        PendingIntent closePendingIntent
                = PendingIntent.getBroadcast(this, 200, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_music);

        Notification notification;
        if (playImgRes == R.mipmap.icon_ori_pause) {//正在播放

            notification = new NotificationCompat.Builder(getApplicationContext(), "MediaServiceChannel")
                    .setCustomContentView(remoteViews)
                    .setSmallIcon(R.mipmap.logo)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setOngoing(true)
                    .build();
        } else {

            notification = new NotificationCompat.Builder(getApplicationContext(), "MediaServiceChannel")
                    .setCustomContentView(remoteViews)
                    .setSmallIcon(R.mipmap.logo)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setOngoing(false)
                    .build();
        }

        remoteViews.setOnClickPendingIntent(R.id.music_iv_x, closePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.music_iv_but, pendingIntent);
        remoteViews.setImageViewResource(R.id.music_iv_but, playImgRes);
        if (title != null) {

            remoteViews.setTextViewText(R.id.music_tv_title, title);
        }
        if (bitmap != null) {

            remoteViews.setImageViewBitmap(R.id.music_iv_bg, bitmap);
        }
        return notification;
    }


    /**
     * 任意一次unbindService()方法，都会触发这个方法
     * 用于释放一些绑定时使用的资源
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 触发弹窗的 eventbus
     *
     * @param notificationEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationEventbus notificationEventbus) {

        checkNotificationSwitch();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocPlayEvent imoocPlayEvent) {

        pauseByAudio();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeadlinePlayEvent headlinePlayEvent) {

        pauseByAudio();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MediaPauseEventbus mediaPauseEventbus) {

        pauseByAudio();
    }

    /**
     * 暂停音频，关于
     */
    private void pauseByAudio() {

        if (mPlayer == null) {

            return;
        }
        if (mPlayer.isPlaying()) {

            mPlayer.pause();
            //上传播放进度
            sendPlayCurrent(0);
            //更新播放页面的状态
            LocalBroadcastManager
                    .getInstance(MyApplication.getContext())
                    .sendBroadcast(new Intent(MediaService.FLAG_MUSIC_PLAY));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = null;
        mediaPresenter.detachView();
        Log.d("onDestroy", 111111 + "");
    }


    public void notificationData() {

        if (isBookWorm) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    ChapterDao chapterDao = appDatabase.chapterDao();
                    Chapter chapter = chapterDao.getSingleByPrimaryKeys(voaid + "", NovelConstant.novelBook.getTypes()
                            , NovelConstant.novelBook.getLevel(), NovelConstant.novelBook.getOrder_number(), chapterOrder + "");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (mPlayer != null) {

                                if (mPlayer.isPlaying()) {

                                    initNotification(chapter.getCname_cn(), R.mipmap.icon_ori_pause, R.mipmap.logo + "");
                                } else {

                                    initNotification(chapter.getCname_cn(), R.mipmap.icon_ori_play, R.mipmap.logo + "");
                                }
                            }
                        }
                    });
                }
            }).start();
        } else {

            List<Title> titleList;
            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                titleList = LitePal.where("voaid = ? and bookid = ?", voaid + "", Constant.book.getId()).find(Title.class);

            } else {

                titleList = LitePal
                        .where("voaid = ? and bookid = ? and language = ?", voaid + "", Constant.book.getId(), Constant.LANGUAGE)
                        .find(Title.class);
            }
            Title title = titleList.get(0);
            if (mPlayer != null) {

                if (mPlayer.isPlaying()) {

                    initNotification(title.getTitle(), R.mipmap.icon_ori_pause, title.getPic());

                } else {

                    initNotification(title.getTitle(), R.mipmap.icon_ori_play, title.getPic());
                }
            }

        }

    }

    class MusicBroadcast extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(MediaService.FLAG_MUSIC_PLAY_NOTICE)) {

                if (mPlayer == null) {

                    return;
                }
                if (mPlayer.isPlaying()) {

                    mPlayer.pause();
                    //上传播放进度
                    sendPlayCurrent(0);
                    notificationData();
                } else {
                    mPlayer.start();
                    notificationData();
                }
                LocalBroadcastManager
                        .getInstance(MyApplication.getContext())
                        .sendBroadcast(new Intent(MediaService.FLAG_MUSIC_PLAY));
            } else if (action.equals(MediaService.FLAG_MUSIC_PLAY_CLOSE)) {

                //关闭服务
                if (musicController == null) {
                    return;
                }
                if (musicController.isPlaying()) {

                    musicController.pause();
                }
                nManagerCompat.cancelAll();
            }
        }
    }
}