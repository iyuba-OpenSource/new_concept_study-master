package com.jn.iyuba.concept.simple.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.adapter.DubbingAdapter;
import com.jn.iyuba.succinct.databinding.ActivityDubbingBinding;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.entity.MediaPauseEventbus;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;
import com.jn.iyuba.concept.simple.presenter.home.DubbingPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.concept.simple.util.popup.DownloadPopup;
import com.jn.iyuba.concept.simple.util.popup.PermissionPopup;
import com.jn.iyuba.concept.simple.view.home.DubbingContract;
import com.jn.iyuba.novel.NovelConstant;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 配音界面
 */
public class DubbingActivity extends BaseActivity<DubbingContract.DubbingView, DubbingContract.DubbingPresenter>
        implements DubbingContract.DubbingView {

    private ActivityDubbingBinding activityDubbingBinding;

    private DubbingAdapter dubbingAdapter;

    private LineItemDecoration lineItemDecoration;

    private ExoPlayer player;

    private String id;
    private String category;
    private String bg_sound;
    private String pic;
    private String title;

    private String video;

    private static String ID = "ID";
    private static String CATEGORY = "CATEGORY";
    //背景音频
    private static String BG_SOUND = "BG_SOUND";
    private static String PIC = "PIC";
    private static String TITLE = "TITLE";

    private static String VIDEO = "VIDEO";


    private MediaRecorder mediaRecorder;

    private Handler handler;

    private HandlerThread handlerThread;

    private MediaPlayer userMediaPlayer;

    private MediaPlayer bgMediaPlayer;
    /**
     * 检测播放，用来暂停
     */
    private Handler playHandler;

    private DownloadPopup downloadPopup;

    //视频
    private String url = null;

    /**
     * 权限请求
     */
    private RxPermissions rxPermissions;

    private PermissionPopup permissionPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle();
        initOperation();

        testPlay();
        testRecord();


        String filePath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/" + id + ".mp4";
        if (new File(filePath).exists()) {


            url = filePath;
        } else {

            if (Constant.userinfo == null) {

                url = "http://staticvip.iyuba.cn/" + video;
            } else {

                if (Constant.userinfo.isVip()) {

                    url = "http://staticvip.iyuba.cn/" + video;
                } else {

                    url = "http://static0.iyuba.cn/" + video;
                }
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                downloadVideo(url, id);
            }
        }).start();


        MediaItem mediaItem = MediaItem.fromUri(url);

        player = new ExoPlayer.Builder(this).build();
        activityDubbingBinding.dubbingSpv.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();

        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onIsPlayingChanged(EventTime eventTime, boolean isPlaying) {
//                AnalyticsListener.super.onIsPlayingChanged(eventTime, isPlaying);

                if (!isPlaying) {

                    player.setVolume(0.8f);
                    if (dubbingAdapter.getPostion() == -1) {

                        return;
                    }
                    Sentence sentence = dubbingAdapter.getItem(dubbingAdapter.getPostion());
                    player.seekTo((long) (Double.parseDouble(sentence.getTiming()) * 1000));
                } else {

                    EventBus.getDefault().post(new MediaPauseEventbus());
                    //启动检测
                    playHandler.sendEmptyMessage(1);
                }
            }
        });


//        presenter.textExamApi("json", id);
        List<Sentence> sentences = LitePal.where("voaid = ?", id).find(Sentence.class);
        dubbingAdapter.setNewData(sentences);
    }


    private void initDownloadPopup(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (downloadPopup == null) {

                    downloadPopup = new DownloadPopup(DubbingActivity.this);
                }
                downloadPopup.setContent(msg);
                downloadPopup.showPopupWindow();
            }
        });

    }

    private void hideDownloadPopup() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (downloadPopup != null) {

                    downloadPopup.dismiss();
                }
            }
        });

    }

    /**
     * 检测播放的进度
     */
    private void testPlay() {

        playHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                if (dubbingAdapter.getPostion() == -1) {

                    return false;
                }
                long endTime = 0;
                int next = dubbingAdapter.getPostion() + 1;//计算一句的开始时间
                if (next < dubbingAdapter.getItemCount()) {

                    Sentence sentence = dubbingAdapter.getItem(next);
                    endTime = (long) (Double.parseDouble(sentence.getTiming()) * 1000);
                } else {//最后一条数据处理

                    Sentence sentence = dubbingAdapter.getItem(dubbingAdapter.getPostion());
                    endTime = (long) (Double.parseDouble(sentence.getEndTiming()) * 1000) + 1000;
                }

                if (player.isPlaying()) {

                    if (player.getCurrentPosition() >= endTime) {

                        stopPlay();
                        dubbingAdapter.getItem(dubbingAdapter.getPostion()).setPlaying(false);
                        //更新播放按钮
                        dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                    } else {

                        playHandler.sendEmptyMessageDelayed(1, 100);
                    }
                }
                return false;
            }
        });
    }


    /**
     * 检测录音相关
     */
    private void testRecord() {

        handlerThread = new HandlerThread("检测录音时长");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                Sentence sentence = dubbingAdapter.getItem(dubbingAdapter.getPostion());
                if (sentence.isRecord()) {

                    long recordTime = System.currentTimeMillis() - sentence.getStartRecordTime();
                    sentence.setRecordPosition(recordTime);
                    double maxDuration = 0;
                    int next = dubbingAdapter.getPostion() + 1;
                    if (next < dubbingAdapter.getItemCount()) {//下一个位置是否有数据

                        maxDuration = Double.parseDouble(dubbingAdapter.getItem(next).getTiming()) - Double.parseDouble(sentence.getTiming());
                    } else {//没有下一条数据，则直接计算本条数据的时间
                        maxDuration = Double.parseDouble(sentence.getEndTiming()) - Double.parseDouble(sentence.getTiming()) + 1;
                    }

                    if (recordTime < maxDuration * 1000) {//录音的时间小于最大录音时间

                        handler.sendEmptyMessageDelayed(1, 100);
                    } else {

                        //停止录音
                        sentence.setRecord(false);
                        sentence.setRecordPosition((long) (maxDuration * 1000));
                        stopRecord(sentence);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                        }
                    });
                }
                return false;
            }
        });
    }

    /**
     * 播放自己录制音频
     */
    private void playSound() {

        if (userMediaPlayer == null) {

            userMediaPlayer = new MediaPlayer();
            userMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    playBgSound();
                }
            });
        }
        userMediaPlayer.reset();

        int p = dubbingAdapter.getPostion();
        if (p == -1) {

            return;
        }
        Sentence sentence = dubbingAdapter.getItem(p);

        try {
            userMediaPlayer.setDataSource(Constant.IUSERSPEECH_URL + "/voa/" + sentence.getRecordSoundUrl());
            userMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 播放背景音乐
     */
    private void playBgSound() {

        if (bgMediaPlayer == null) {

            bgMediaPlayer = new MediaPlayer();

        }
        bgMediaPlayer.reset();
        bgMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                int p = dubbingAdapter.getPostion();

                if (p == -1) {

                    return;
                }

                Sentence sentence = dubbingAdapter.getItem(p);

                int curPosition = (int) (Double.parseDouble(sentence.getTiming()) * 1000);

                userMediaPlayer.start();
                //背景音乐
                bgMediaPlayer.seekTo(curPosition);
                bgMediaPlayer.start();

                player.setVolume(0);//静音，凸显背景音乐和自己录制的音频
                player.seekTo(curPosition);
                player.play();
                //更新播放按钮
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                //触发player的播放和暂停回调
            }
        });
        try {
            //非新闻类的
            bgMediaPlayer.setVolume(0.02f, 0.02f);
            if (!bg_sound.startsWith("http")) {//处理新闻的

                bgMediaPlayer.setVolume(1.0f, 1.0f);
                bg_sound = "http://staticvip.iyuba.cn/sounds/voa" + bg_sound;
            }
            bgMediaPlayer.setDataSource(bg_sound);
            bgMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initOperation() {

        dubbingAdapter = new DubbingAdapter(R.layout.item_dubbing, new ArrayList<>());
        activityDubbingBinding.dubbingRv.setLayoutManager(new LinearLayoutManager(this));
        activityDubbingBinding.dubbingRv.setAdapter(dubbingAdapter);

        activityDubbingBinding.dubbingRv.setItemAnimator(null);

        dubbingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(position);

            }
        });
        dubbingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                Sentence sentence = dubbingAdapter.getItem(position);
                //是否有冲突，播放和录音有冲突
                if (dubbingAdapter.getPostion() == -1 || dubbingAdapter.getPostion() == position) {

                    if (dubbingAdapter.getPostion() == -1) {//位置在默认位置才需要进行处理

                        dubbingAdapter.setPostion(position);
                    }
                    playAndRecord(view, sentence, position);
                } else {//选择操作的这项，与之前不一致，可能会有冲突，需处理

                    Sentence oldSentence = dubbingAdapter.getItem(dubbingAdapter.getPostion());
                    if (oldSentence.isPlaying() || oldSentence.isRecord()) {

                        if (oldSentence.isPlaying()) {
                            //停止播放
                            stopPlay();
                            oldSentence.setPlaying(false);
                            dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                        }
                        if (oldSentence.isRecord()) {//如果正在录音则关闭录音

                            mediaRecorder.stop();
                            oldSentence.setRecordPosition(0);
                            oldSentence.setRecord(false);
                        }
                        dubbingAdapter.setPostion(position);
                        playAndRecord(view, sentence, position);
                    } else {//操作的这一项没有录音也没有播放
                        dubbingAdapter.setPostion(position);
                        playAndRecord(view, sentence, position);
                    }
                }
            }
        });

        lineItemDecoration = new LineItemDecoration(this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));
        activityDubbingBinding.dubbingRv.addItemDecoration(lineItemDecoration);

        activityDubbingBinding.dubbingIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //预览
        activityDubbingBinding.dubbingTvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dubbingAdapter.getItemCount() != 0) {

                    DubbingPreviewActivity.startActivity(DubbingActivity.this, dubbingAdapter.getData(),
                            id, category, bg_sound, pic, title);
                }
            }
        });
    }

    /**
     * 停止播放录音、视频、背景音乐
     */
    private void stopPlay() {

        if (userMediaPlayer != null && userMediaPlayer.isPlaying()) {
            userMediaPlayer.pause();
        }
        if (bgMediaPlayer.isPlaying()) {
            bgMediaPlayer.pause();
        }
        if (player.isPlaying()) {
            player.pause();
        }
    }

    /**
     * 录音和播放视频
     *
     * @param view
     * @param sentence
     * @param position
     */
    private void playAndRecord(View view, Sentence sentence, int position) {

        if (view.getId() == R.id.dubbing_iv_record) {//录音

            if (sentence.isPlaying()) {

                sentence.setPlaying(false);//录音和播放互斥
                //更新播放按钮
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                stopPlay();
            }
            //检测是否授权录音权限
            requestRecord(sentence, position);
        } else {//播放自己的录音
            //录音和播放互斥
            if (sentence.isRecord() && mediaRecorder != null) {//中断录音
                mediaRecorder.reset();
                sentence.setRecord(false);
                sentence.setRecordPosition(0);
                //更新录音进度
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
            }
            if (sentence.isPlaying()) {

                sentence.setPlaying(false);
                stopPlay();
                //更新播放按钮
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
            } else {

                sentence.setPlaying(true);
                //更新播放按钮
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                playSound();
            }
        }
    }

    /**
     * 开始录音和停止录音
     *
     * @param sentence
     * @param position
     */
    private void startStopRecord(Sentence sentence, int position) {

        if (sentence.isRecord()) {

            toast("结束录音");
            sentence.setRecord(false);
            stopRecord(sentence);
        } else {

            toast("开始录音");
            sentence.setRecord(true);
            initRecorder(sentence, position);
        }
    }

    /**
     * 权限提醒
     *
     * @param content
     */
    private void initPermissionPopup(String content, Sentence sentence, int position) {

        if (permissionPopup == null) {

            permissionPopup = new PermissionPopup(DubbingActivity.this);
            permissionPopup.setCallback(new PermissionPopup.Callback() {
                @SuppressLint("CheckResult")
                @Override
                public void clickOk() {

                    if (rxPermissions == null) {

                        rxPermissions = new RxPermissions(DubbingActivity.this);
                    }
                    rxPermissions.request(Manifest.permission.RECORD_AUDIO)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {

                                    if (aBoolean) {

                                        startStopRecord(sentence, position);
                                    } else {
                                        SharedPreferences sp = getSharedPreferences(Constant.SP_PERMISSIONS, MODE_PRIVATE);
                                        sp.edit().putInt(Constant.SP_KEY_RECORD, 1).apply();
                                        toast("你禁止了录音权限");
                                    }
                                }
                            });
                    permissionPopup.dismiss();
                }
            });
        }
        permissionPopup.setContent(content);
        permissionPopup.showPopupWindow();
    }


    /**
     * 请求录音权限
     */
    @SuppressLint("CheckResult")
    private void requestRecord(Sentence sentence, int position) {

        if (rxPermissions == null) {

            rxPermissions = new RxPermissions(DubbingActivity.this);
        }
        if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)) {

            startStopRecord(sentence, position);
        } else {

            SharedPreferences sp = getSharedPreferences(Constant.SP_PERMISSIONS, MODE_PRIVATE);
            int flag = sp.getInt(Constant.SP_KEY_RECORD, 0);
            if (flag == 0) {

                initPermissionPopup("配音将要申请录音权限！", sentence, position);
            } else {

                toast("你禁止了录音权限，请在权限管理中打开");
            }
        }
    }

    /**
     * 启动录音
     *
     * @param sentence
     */
    private void initRecorder(Sentence sentence, int position) {

        if (mediaRecorder == null) {
            //初始化
            mediaRecorder = new MediaRecorder();
        }
        if (!getExternalFilesDir(Environment.DIRECTORY_MUSIC).exists()) {
            getExternalFilesDir(Environment.DIRECTORY_MUSIC).mkdirs();
        }

        mediaRecorder.reset();
        String name = sentence.getVoaid() + sentence.getParaid() + sentence.getIdIndex();
        mediaRecorder.setOutputFile(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + name + ".mp3");
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //计算最大的录音时间
        int newPos = position + 1;
        double maxDuration = 0;
        if (newPos < dubbingAdapter.getItemCount()) {

            maxDuration = Double.parseDouble(dubbingAdapter.getItem(newPos).getTiming()) - Double.parseDouble(sentence.getTiming());
        } else {//最后一条加1秒

            maxDuration = Double.parseDouble(sentence.getEndTiming()) - Double.parseDouble(sentence.getTiming()) + 1.0;
        }
        mediaRecorder.setMaxDuration((int) (maxDuration * 1000));
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            //记录开始录音时间
            sentence.setStartRecordTime(System.currentTimeMillis());
            //启动检测
            handler.sendEmptyMessage(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 停止录音
     */
    private void stopRecord(Sentence sentence) {

        if (mediaRecorder == null) {

            return;
        }
        mediaRecorder.stop();
        sentence.setEndRecordTime(System.currentTimeMillis());


        String uid = "0";
        if (Constant.userinfo != null) {

            uid = Constant.userinfo.getUid() + "";
        }

        MediaType type = MediaType.parse("application/octet-stream");
        String name = sentence.getVoaid() + sentence.getParaid() + sentence.getIdIndex();
        File file = new File(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + name + ".mp3");
        RequestBody fileBody = RequestBody.create(type, file);

        String typeParameter = Constant.TYPE;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", typeParameter)
                .addFormDataPart("userId", uid)
                .addFormDataPart("newsId", sentence.getVoaid())
                .addFormDataPart("paraId", sentence.getParaid())
                .addFormDataPart("IdIndex", sentence.getIdIndex())
                .addFormDataPart("sentence", sentence.getSentence())
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("wordId", "0")
                .addFormDataPart("flg", "0")
                .addFormDataPart("appId", Constant.APPID + "")
                .build();
        presenter.test(requestBody, sentence.getIdIndex(), sentence.getParaid());
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            id = bundle.getString(ID);
            category = bundle.getString(CATEGORY);
            bg_sound = bundle.getString(BG_SOUND);
            pic = bundle.getString(PIC);
            title = bundle.getString(TITLE);
            video = bundle.getString(VIDEO);
        }
    }

    /**
     * @param activity
     * @param id       新闻id
     */
    public static void startActivity(Activity activity, String id, String category, String bg_sound, String pic, String title, String video) {

        Intent intent = new Intent(activity, DubbingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ID, id);
        bundle.putString(CATEGORY, category);
        bundle.putString(BG_SOUND, bg_sound);
        bundle.putString(PIC, pic);
        bundle.putString(TITLE, title);
        bundle.putString(VIDEO, video);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    public View initLayout() {
        activityDubbingBinding = ActivityDubbingBinding.inflate(getLayoutInflater());
        return activityDubbingBinding.getRoot();
    }

    @Override
    public DubbingContract.DubbingPresenter initPresenter() {
        return new DubbingPresenter();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void testComplete(EvalBean evalBean, String idindex, int paraId) {

        List<Sentence> sentenceList = dubbingAdapter.getData();
        int index = 0;
        for (int i = 0; i < sentenceList.size(); i++) {//找到评测的这句

            Sentence sentence = sentenceList.get(i);
            if (sentence.getParaid().equals(paraId + "")) {

                index = i;
                break;
            }
        }
        String url = evalBean.getData().getUrl();
        String socre = evalBean.getData().getTotalScore() + "";
        sentenceList.get(index).setRecordSoundUrl(url);
        sentenceList.get(index).setScore(socre);
        dubbingAdapter.notifyItemChanged(index);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (player != null) {

            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null) {

            player.release();
        }
        if (userMediaPlayer != null) {

            userMediaPlayer.release();
        }
        if (bgMediaPlayer != null) {

            bgMediaPlayer.release();
        }
    }


    /**
     * 下载视频
     *
     * @param url
     * @param id
     */
    private void downloadVideo(String url, String id) {

        String filePath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/" + id + ".mp4";
        if (new File(filePath).exists()) {

            return;
        }

        initDownloadPopup("正在下载视频");
        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new RetryIntercepter(3))//重试3次
//                .addInterceptor(new GzipRequestInterceptor())//gzip压缩
                .connectTimeout(3 * 60 * 1000, TimeUnit.MILLISECONDS) //连接超时
                .readTimeout(3 * 60 * 1000, TimeUnit.MILLISECONDS) //读取超时
                .writeTimeout(60 * 1000, TimeUnit.MILLISECONDS) //写超时
                .callTimeout(3 * 60 * 1000, TimeUnit.MILLISECONDS)
                // okhttp默认使用的RealConnectionPool初始化线程数==2147483647，在服务端会导致大量线程TIMED_WAITING
                //ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                InputStream inputStream = response.body().byteStream();
                long contentLength = response.body().contentLength();
                FileOutputStream fileOutputStream = new FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/" + id + ".mp4");

                byte[] b = new byte[1024];
                int curDataSize = 0;
                int len = inputStream.read(b);
                curDataSize = len;

                try {

                    while (len != -1) {

                        fileOutputStream.write(b, 0, len);
                        len = inputStream.read(b);
                        curDataSize = +len + curDataSize;
                        initDownloadPopup("正在下载视频" + (100 * curDataSize / contentLength) + "%");
                    }
                } catch (Exception e) {

                    hideDownloadPopup();
                    toast("下载异常");
                }
                hideDownloadPopup();
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            } else {

                Log.d("download", response.message());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}