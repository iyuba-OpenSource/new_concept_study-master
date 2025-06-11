package com.jn.iyuba.concept.simple.activity.fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION_CODES.BASE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.model.bean.ApiWordBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.util.popup.ApiWordPopup;
import com.jn.iyuba.concept.simple.util.widget.SelectWordTextView;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.activity.login.LoginActivity;
import com.jn.iyuba.concept.simple.activity.login.WxLoginActivity;
import com.jn.iyuba.concept.simple.activity.vip.VipActivity;
import com.jn.iyuba.concept.simple.adapter.EvaluatingAdapter;
import com.jn.iyuba.succinct.databinding.FragmentEvaluatingBinding;
import com.jn.iyuba.concept.simple.db.EvalWord;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.MediaPauseEventbus;
import com.jn.iyuba.concept.simple.entity.Refresh;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;
import com.jn.iyuba.concept.simple.model.bean.MergeBean;
import com.jn.iyuba.concept.simple.model.bean.PublishEvalBean;
import com.jn.iyuba.concept.simple.presenter.home.EvaluatingPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.EvalWordUtil;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.concept.simple.util.SentenceUtil;
import com.jn.iyuba.concept.simple.view.home.EvaluatingContract;
import com.jn.iyuba.novel.NovelConstant;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.Chapter;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.db.NovelEvalWord;
import com.jn.iyuba.novel.db.NovelSentence;
import com.jn.iyuba.novel.db.dao.ChapterDao;
import com.jn.iyuba.novel.db.dao.NovelEvalWordDao;
import com.jn.iyuba.novel.db.dao.NovelSentenceDao;
import com.mob.MobSDK;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 评测页面
 */
public class EvaluatingFragment extends BaseFragment<EvaluatingContract.EvaluatingView, EvaluatingContract.EvaluatingPresenter>
        implements EvaluatingContract.EvaluatingView {


    /**
     * 是否是书虫
     */
    private boolean isBookWorm = false;
    /**
     * 课文id
     */
    private int voa_id;

    /**
     * 章节序号
     */
    private int chapterOrder = 0;


    private FragmentEvaluatingBinding binding;

    private EvaluatingAdapter evaluatingAdapter;

    private MediaPlayer mediaPlayer;

    private MediaRecorder mediaRecorder;

    /**
     * 权限请求
     */
    private RxPermissions rxPermissions;

    /**
     * 权限请求的状态
     */
    private SharedPreferences pSP;


    private MergeBean mergeBean;

    private DecimalFormat decimalFormat;

    /**
     * 记录评测次数
     */
    private SharedPreferences evalSP;

    /**
     * 存储评测次数的文件
     */
    private String SP_NAME = "EVAL";

    private ApiWordPopup apiWordPopup;


    /**
     * 随机播放或者书序播放
     * 更新数据
     */
    public void updateData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            voa_id = bundle.getInt("VOA_ID", 0);
            chapterOrder = bundle.getInt("CHAPTER_ORDER", 0);
        }

        if (isBookWorm) {

            updateForBookWorm();
        } else {

            updateForConcept();
        }
    }

    /**
     * 随机播放 顺序播放  书虫
     */
    private void updateForBookWorm() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase appDatabase = NovelDB.getInstance().getDB();
                NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
                List<NovelSentence> novelSentenceList = novelSentenceDao.getDataByPrimaryKeys(NovelConstant.novelBook.getTypes(), voa_id + "",
                        chapterOrder + "", NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel());
                List<Sentence> sentenceList = SentenceUtil.convert(novelSentenceList);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        if (evaluatingAdapter != null) {

                            evaluatingAdapter.setNewData(sentenceList);
                        }
                    }
                });
            }
        }).start();
    }


    /**
     * 随机播放 顺序播放 新概念相关
     */
    private void updateForConcept() {

        List<Sentence> sentenceList;
        if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

            sentenceList = LitePal.where("voaid = ? ", voa_id + "").find(Sentence.class);
        } else {

            if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                sentenceList = LitePal.where("voaid = ? ", voa_id + "").find(Sentence.class);
            } else {

                sentenceList = LitePal.where("voaid = ? ", (voa_id * 10) + "").find(Sentence.class);
            }
        }

        for (int i = 0; i < sentenceList.size(); i++) {//添加评测的单词

            Sentence sentence = sentenceList.get(i);
            List<EvalWord> evalWordList = LitePal.where("voaid = ? and paraid = ? and senIndex = ?", sentence.getVoaid(), sentence.getParaid(), sentence.getIdIndex())
                    .find(EvalWord.class);
            if (evalWordList.size() != 0) {

                sentence.setWordsDTOS(evalWordList);
            }
        }
        if (evaluatingAdapter != null) {

            evaluatingAdapter.setNewData(sentenceList);
        }
    }

    public EvaluatingFragment() {
    }

    public static EvaluatingFragment newInstance(int voa_id) {
        EvaluatingFragment fragment = new EvaluatingFragment();
        Bundle args = new Bundle();
        args.putInt("VOA_ID", voa_id);
        fragment.setArguments(args);
        return fragment;
    }

    public static EvaluatingFragment newInstance(int voa_id, boolean isBookWorm, int chapterOrder) {
        EvaluatingFragment fragment = new EvaluatingFragment();
        Bundle args = new Bundle();
        args.putInt("VOA_ID", voa_id);
        args.putBoolean("IS_BOOK_WORM", isBookWorm);
        args.putInt("CHAPTER_ORDER", chapterOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voa_id = getArguments().getInt("VOA_ID");
            chapterOrder = getArguments().getInt("CHAPTER_ORDER", 0);
            isBookWorm = getArguments().getBoolean("IS_BOOK_WORM", false);
        }

        evalSP = MyApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (mediaPlayer != null) {

            mediaPlayer.release();
        }

        if (mediaRecorder != null) {

            mediaRecorder.release();
        }

        if (recordHandler != null) {
            recordHandler.removeMessages(1);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();

        if (decimalFormat == null) {

            decimalFormat = new DecimalFormat("#00");
        }

        pSP = view.getContext().getSharedPreferences(Constant.SP_PERMISSIONS, MODE_PRIVATE);


        LineItemDecoration lineItemDecoration = new LineItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.shape_eval_line, requireActivity().getTheme()));
        if (binding.evaluatingTvList.getItemDecorationCount() == 0) {

            binding.evaluatingTvList.addItemDecoration(lineItemDecoration);
        }

        getData();
    }

    private void showSentenceData(List<Sentence> sentenceList) {

        binding.evaluatingTvList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        evaluatingAdapter = new EvaluatingAdapter(R.layout.item_evaluating, sentenceList);
        binding.evaluatingTvList.setHasFixedSize(true);
        binding.evaluatingTvList.setItemAnimator(null);
        binding.evaluatingTvList.setAdapter(evaluatingAdapter);

        evaluatingAdapter.setOnClickWordListener(new SelectWordTextView.OnClickWordListener() {
            @Override
            public void onClickWord(String word) {

                presenter.apiWord(word);

            }
        });
        evaluatingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                int id = view.getId();
                dealClick(id, position);
            }
        });
    }


    /**
     * 获取数据
     */
    private void getData() {

        if (isBookWorm) {//书虫

            getDataForBookworm();
        } else {//新概念

            List<Sentence> sentenceList = getDataForConcept();
            showSentenceData(sentenceList);
        }
    }

    /**
     * 获取书虫和剑桥的句子数据
     */
    private void getDataForBookworm() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                AppDatabase appDatabase = NovelDB.getInstance().getDB();
                NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
                List<NovelSentence> novelSentenceList = novelSentenceDao.getDataByPrimaryKeys(NovelConstant.novelBook.getTypes(), voa_id + "",
                        chapterOrder + "", NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel());
                List<Sentence> sentenceList = SentenceUtil.convert(novelSentenceList);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        showSentenceData(sentenceList);
                    }
                });
            }
        }).start();
    }

    /**
     * 获取新概念句子的数据
     *
     * @return
     */
    private List<Sentence> getDataForConcept() {

        List<Sentence> sentenceList;
        if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

            sentenceList = LitePal.where("voaid = ? ", voa_id + "").find(Sentence.class);
        } else {

            if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                sentenceList = LitePal.where("voaid = ? ", voa_id + "").find(Sentence.class);
            } else {

                sentenceList = LitePal.where("voaid = ? ", (voa_id * 10) + "").find(Sentence.class);
            }
        }

        for (int i = 0; i < sentenceList.size(); i++) {//添加评测的单词

            Sentence sentence = sentenceList.get(i);
            List<EvalWord> evalWordList = LitePal.where("voaid = ? and paraid = ? and senIndex = ?", sentence.getVoaid(), sentence.getParaid(), sentence.getIdIndex())
                    .find(EvalWord.class);
            if (evalWordList.size() != 0) {

                sentence.setWordsDTOS(evalWordList);
            }
        }
        return sentenceList;
    }


    private void initOperation() {

        //合成
        binding.evaluatingTvMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mergeStr = binding.evaluatingTvMerge.getText().toString();
                if (mergeStr.equals("合成")) {

                    StringBuilder stringBuilder = new StringBuilder();
                    int recordCount = 0;
                    List<Sentence> sentenceList = evaluatingAdapter.getData();
                    for (int i = 0; i < sentenceList.size(); i++) {

                        Sentence sentence = sentenceList.get(i);
                        if (sentence.getRecordSoundUrl() != null) {

                            recordCount++;
                            stringBuilder.append(sentence.getRecordSoundUrl() + ",");
                        }
                    }

                    stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());

                    if (recordCount < 2) {

                        toast("至少两句才能合成");
                        return;
                    }

                    String type;
                    if (isBookWorm) {

                        type = sentenceList.get(0).getTypes();
                    } else {

                        type = Constant.TYPE;
                    }

                    presenter.merge(stringBuilder.toString(), type);
                } else if (mergeStr.equals("听")) {

                    evaluatingAdapter.setFlag(3);
                    initMediaPlayer(mergeBean, null, -1);
                } else if (mergeStr.equals("停")) {//播放过程中点击

                    if (mediaPlayer != null) {

                        mediaPlayer.pause();
                    }
                    binding.evaluatingTvMerge.setText("听");
                }
            }
        });
        //发布合成音频
        binding.evaluatingTvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mergeBean == null) {

                    toast("请先合成音频");
                    return;
                }
                if (Constant.userinfo == null) {

                    toast("请登录");
                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                    return;
                }

                String uid = Constant.userinfo.getUid() + "";
                String name = Constant.userinfo.getUsername();

                int totalScore = 0;
                int count = 0;
                List<Sentence> sentenceList = evaluatingAdapter.getData();
                for (int i = 0; i < sentenceList.size(); i++) {

                    Sentence sentence = sentenceList.get(i);
                    if (sentence.getScore() != null) {

                        totalScore = (int) (totalScore + Double.parseDouble(sentence.getScore()) * 20);
                        count++;
                    }
                }

                String mVoaid;

                if (!isBookWorm) {
                    if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                        mVoaid = voa_id + "";
                    } else {

                        if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                            mVoaid = voa_id + "";
                        } else {
                            mVoaid = voa_id * 10 + "";
                        }
                    }
                } else {
                    mVoaid = voa_id + "";
                }
                String type;
                if (isBookWorm) {

                    type = sentenceList.get(0).getTypes();
                } else {

                    type = Constant.TYPE;
                }

                presenter.publishEval(type, mergeBean.getUrl(), "json", 0, 0, "android",
                        60002, totalScore / count, 4, uid, name, mVoaid, 1, Constant.APPID);
            }
        });
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        binding = FragmentEvaluatingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected EvaluatingContract.EvaluatingPresenter initPresenter() {
        return new EvaluatingPresenter();
    }


    /**
     * 处理点击事件
     *
     * @param id
     * @param position
     */
    private void dealClick(int id, int position) {

        if (id == R.id.evaluating_iv_play) {//播放音频

            //停止后台播放，如果后台播放正在播放的话
            EventBus.getDefault().post(new MediaPauseEventbus());
            evaluatingAdapter.setFlag(1);
            playOriginal(position);
        } else if (id == R.id.evaluating_rpb_record) {

            EventBus.getDefault().post(new MediaPauseEventbus());
            requestRecord(position);
        } else if (id == R.id.evaluating_iv_play_oneself) {

            EventBus.getDefault().post(new MediaPauseEventbus());
            evaluatingAdapter.setFlag(2);
            playOriginal(position);
        } else if (id == R.id.evaluating_iv_submit) {

            if (Constant.userinfo == null) {

                toast("请登录");
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                return;
            }
            publish(position);
        } else if (id == R.id.evaluating_iv_share) {

            share(position);
        }
    }

    /**
     * 分享
     */
    private void share(int position) {

        Sentence dataDTO = evaluatingAdapter.getItem(position);

        String username = "";
        if (Constant.userinfo != null) {

            username = Constant.userinfo.getUsername();
        }
        String title = username + "在" + getString(R.string.app_name) + "语音评测中获得了" + (int) (Double.parseDouble(dataDTO.getScore()) * 20) + "分";
        String url = Constant.URL_VOA + "/voa/play.jsp?id=" + dataDTO.getShuoshuoId() + "&appid=" + Constant.APPID + "&apptype=" + Constant.TYPE;


        showShare(title, dataDTO.getSentenceCn(), url, shareImg());
    }


    /**
     * 获取分享的图片
     */
    private String shareImg() {

        List<Title> titleBean;
        if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

            titleBean = LitePal.where("voaid = ?", voa_id + "").find(Title.class);
        } else {

            titleBean = LitePal.where("voaid = ? and language = ?", voa_id + "", Constant.LANGUAGE).find(Title.class);
        }
        Title mTitle = null;
        if (titleBean.size() > 0) {

            mTitle = titleBean.get(0);
        }
        String imgUrl = null;
        if (mTitle != null) {

            if (mTitle.getPic() != null && !mTitle.getPic().equals("")) {

                imgUrl = mTitle.getPic();
            }
        }

        if (imgUrl == null) {
            saveLogoToLocal();
            imgUrl = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/logo.img";
        }
        return imgUrl;
    }

    /**
     * 存储logo到本地
     */
    public void saveLogoToLocal() {

        File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/logo.jpg");
        if (!file.exists()) {

            try {

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 分享
     *
     * @param title
     * @param text
     * @param url
     * @param imageUrl
     */
    private void showShare(String title, String text, String url, String imageUrl) {

        OnekeyShare oks = new OnekeyShare();
//        oks.addHiddenPlatform(SinaWeibo.NAME);

//        oks.setImagePath(imagePath);

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博

        if (imageUrl.startsWith("http")) {

            oks.setImageUrl(imageUrl);
        } else {

            oks.setImagePath(imageUrl);
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        oks.show(MobSDK.getContext());
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }

    private void publish(int position) {

        String uid = "0";
        String name = "";
        if (Constant.userinfo != null) {

            uid = Constant.userinfo.getUid() + "";
            name = Constant.userinfo.getUsername();
        }
        Sentence dataDTO = evaluatingAdapter.getItem(position);

        String type;
        if (isBookWorm) {

            type = dataDTO.getTypes();
        } else {

            type = Constant.TYPE;
        }

        presenter.publishEval(type, dataDTO.getRecordSoundUrl(), "json",
                Integer.parseInt(dataDTO.getIdIndex()),
                Integer.parseInt(dataDTO.getParaid()),
                "android",
                60002,
                (int) (Double.parseDouble(dataDTO.getScore()) * 20),
                2, uid, name, dataDTO.getVoaid(), 0, Constant.APPID);
    }


    /**
     * 播放原音频
     */
    private void playOriginal(int position) {

        if (!evaluatingAdapter.isPlay() && !evaluatingAdapter.isRecord()) {//没有录音则可执行

            Sentence dataDTO = evaluatingAdapter.getItem(position);
            initMediaPlayer(null, dataDTO, position);
        } else if (evaluatingAdapter.isPlay()) {

            if (position == evaluatingAdapter.getPosition()) {//操作的数据正在播放

                evaluatingAdapter.setPlay(false);
                mediaPlayer.stop();
                evaluatingAdapter.notifyItemChanged(position);
            } else {//操作的数据没有播放，其他数据在播放
                Sentence dataDTO = evaluatingAdapter.getItem(position);
                int oPosition = evaluatingAdapter.getPosition();
                evaluatingAdapter.setPlay(false);
                mediaPlayer.stop();
                evaluatingAdapter.notifyItemChanged(oPosition);
                initMediaPlayer(null, dataDTO, position);
            }
        } else if (evaluatingAdapter.isRecord()) {

            evaluatingAdapter.setRecord(false);
            mediaRecorder.stop();
            evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
            Sentence dataDTO = evaluatingAdapter.getItem(position);
            initMediaPlayer(null, dataDTO, position);
        }
    }


    /**
     * 播放句子
     */
    private void initMediaPlayer(MergeBean mergeBean, Sentence titleDataDTO, int p) {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mediaPlayer.start();
                    if (evaluatingAdapter.getFlag() != 3) {
                        evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
                        if (evaluatingAdapter.getFlag() == 1) {//播放原音

                            Sentence sentence = evaluatingAdapter.getItem(evaluatingAdapter.getPosition());
                            if (sentence != null) {

                                mediaPlayer.seekTo((int) (Double.parseDouble(sentence.getTiming()) * 1000));

                                Message message = Message.obtain();
                                message.what = 1;
                                message.obj = sentence;
                                playHandler.sendMessage(message);
                            }
                        }
                    } else {//合成音频

                        binding.evaluatingTvMerge.setText("停");
                        binding.evaluatingSbProgress.setMax(mp.getDuration());
                        binding.evaluatingSbProgress.setProgress(0);
                        handler.sendEmptyMessage(1);
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (evaluatingAdapter.getFlag() == 3) {


                        binding.evaluatingTvMerge.setText("听");
                        binding.evaluatingSbProgress.setProgress(0);
                        binding.evaluatingTvCtime.setText("00:00");
                    } else {

                        //完成则更改播放状态
                        Sentence dataDTO = evaluatingAdapter.getItem(evaluatingAdapter.getPosition());
                        if (dataDTO != null) {

                            evaluatingAdapter.setPlay(false);
                            evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
                        }
                    }
                }
            });
        }

        int flag = evaluatingAdapter.getFlag();
        if (flag == 3) {

        } else {//item的原音和录音触发

            //记录播放的数据的位置
            evaluatingAdapter.setPosition(p);//记录操作单词的数据位置
            evaluatingAdapter.setPlay(true);
        }


        mediaPlayer.reset();
        try {
            //http://static2.iyuba.cn/Japan/jp3/talk/001/001_01.mp3

            String url = null;
            if (flag == 3) {//合成音频

                url = Constant.IUSERSPEECH_URL + "/voa/" + mergeBean.getUrl();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
            } else {

                if (evaluatingAdapter.getFlag() == 1) {

                    if (isBookWorm) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                AppDatabase appDatabase = NovelDB.getInstance().getDB();
                                ChapterDao chapterDao = appDatabase.chapterDao();
                                Chapter chapter = chapterDao.getSingleByPrimaryKeys(titleDataDTO.getVoaid(), titleDataDTO.getTypes(), titleDataDTO.getLevel(),
                                        titleDataDTO.getOrderNumber(), titleDataDTO.getChapter_order());
                                String bookwormUrl;
                                if (Constant.userinfo != null && Constant.userinfo.isVip()) {

                                    bookwormUrl = "http://staticvip2.iyuba.cn" + chapter.getSound();
                                } else {

                                    bookwormUrl = "http://static2.iyuba.cn/" + chapter.getSound();
                                }

                                if (mediaPlayer != null) {
                                    try {
                                        mediaPlayer.setDataSource(bookwormUrl);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    mediaPlayer.prepareAsync();
                                }
                            }
                        }).start();
                    } else if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                        List<Title> titleList = LitePal.where("voaid = ?", voa_id + "").find(Title.class);
                        if (titleList.size() > 0) {

                            Title title = titleList.get(0);
                            url = title.getSound().replace("voa", "voa/sentence").replace(title.getVoaId() + ".mp3", title.getVoaId() + "/" + title.getVoaId() + ".mp3");
                        }
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepareAsync();
                    } else {

                        int a = Integer.parseInt(titleDataDTO.getVoaid()) / 1000;
                        int b = Integer.parseInt(titleDataDTO.getVoaid()) - Integer.parseInt(titleDataDTO.getVoaid()) / 1000 * 1000;

                        if (Constant.LANGUAGE.equalsIgnoreCase("US")) {

                            url = "http://static2.iyuba.cn/newconcept/" + a + "_" + b + ".mp3";
                        } else {

                            a = a / 10;
                            b = b / 10;
                            url = "http://static2.iyuba.cn/newconcept/british/" + a + "/" + a + "_" + b + ".mp3";
                        }
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepareAsync();
                    }

                } else {//播放录音

                    url = Constant.IUSERSPEECH_URL + "/voa/" + titleDataDTO.getRecordSoundUrl();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();
                }
            }
//            mediaPlayer.setDataSource(url);
//            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 录音
     */
    private void initRecord(Sentence titleDataDTO, int p) {

        if (mediaRecorder == null) {

            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.reset();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioSamplingRate(44100);
        String name = titleDataDTO.getVoaid() + titleDataDTO.getParaid() + titleDataDTO.getIdIndex();
        mediaRecorder.setOutputFile(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/"
                + name + ".mp3");
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (evaluatingAdapter != null) {

            evaluatingAdapter.setRecord(true);
            evaluatingAdapter.setPosition(p);
            evaluatingAdapter.notifyItemChanged(p);
        }
        toast("开始录音，再次点击录音结束");
        recordHandler.sendEmptyMessage(1);
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


    /**
     * 请求录音权限
     *
     * @param position
     */
    @SuppressLint("CheckResult")
    private void requestRecord(int position) {


        if (rxPermissions == null) {

            rxPermissions = new RxPermissions(requireActivity());
        }

        if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)) {

            record(position);
        } else {

            int record = pSP.getInt(Constant.SP_KEY_RECORD, 0);
            if (record == 0) {


                new AlertDialog.Builder(requireActivity())
                        .setTitle("权限说明")
                        .setMessage("录音权限：评测功能需要录音权限")
                        .setPositiveButton("确定", (dialog, which) -> rxPermissions
                                .request(Manifest.permission.RECORD_AUDIO)
                                .subscribe(aBoolean -> {

                                    if (aBoolean) {

                                        record(position);
                                    } else {

                                        pSP.edit().putInt(Constant.SP_KEY_RECORD, 1).apply();
                                        toast("您拒绝了录音权限，请在权限管理中打开权限！");
                                    }
                                }))
                        .show();
            } else {

                toast("请在应用权限管理中打开录音权限！");
            }
        }
    }


    /**
     * 登录提醒
     */
    private void loginAlert() {

        AlertDialog alertDialog = new AlertDialog
                .Builder(requireActivity())
                .setTitle("提示")
                .setMessage("您还没有登录，是否要登录")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(requireActivity(), LoginActivity.class));
                    }
                })
                .show();
    }

    private void vipAlert() {


        AlertDialog alertDialog = new AlertDialog
                .Builder(requireActivity())
                .setTitle("提示")
                .setMessage("非vip用户每课只能评测三句，vip用户免费评测，是否要购买vip？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        VipActivity.startActivity(requireActivity());
                    }
                })
                .show();
    }


    /**
     * 录音评测
     *
     * @param i
     */
    private void record(int i) {


        //检测评测次数
        if (Constant.userinfo == null) {

            int count = evalSP.getInt("eavl" + voa_id, 0);
            if (count >= 3) {

                loginAlert();
                return;
            }
        } else {

            if (!Constant.userinfo.isVip()) {

                int count = evalSP.getInt("eavl" + voa_id, 0);
                if (count >= 3) {

                    vipAlert();
                    return;
                }
            }
        }


        if (!evaluatingAdapter.isPlay() && !evaluatingAdapter.isRecord()) {//没有录音则可执行

            Sentence dataDTO = evaluatingAdapter.getItem(i);
            initRecord(dataDTO, i);
        } else if (evaluatingAdapter.isRecord()) {

            if (i == evaluatingAdapter.getPosition()) {//操作的数据正在播放

                evaluatingAdapter.setRecord(false);
                evaluatingAdapter.setPlay(false);
                mediaRecorder.stop();
                Sentence dataDTO = evaluatingAdapter.getItem(i);
                test(dataDTO);
            } else {//操作的数据没有播放，其他数据在播放

                Sentence dataDTO = evaluatingAdapter.getItem(i);
                int oPosition = evaluatingAdapter.getPosition();
                evaluatingAdapter.setRecord(false);
                evaluatingAdapter.setPlay(false);
                mediaRecorder.stop();
                evaluatingAdapter.notifyItemChanged(oPosition);
                initRecord(dataDTO, i);
            }
        } else if (evaluatingAdapter.isPlay()) {//在音频播放的情况下，录音

            evaluatingAdapter.setPlay(false);
            mediaPlayer.stop();
            evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
            Sentence dataDTO = evaluatingAdapter.getItem(i);
            initRecord(dataDTO, i);
        }
    }


    /**
     * 请求评测接口
     *
     * @param dataDTO
     */
    private void test(Sentence dataDTO) {

        String uid = "0";

        if (Constant.userinfo != null) {

            uid = Constant.userinfo.getUid() + "";
        }

        MediaType type = MediaType.parse("application/octet-stream");
        String name = dataDTO.getVoaid() + dataDTO.getParaid() + dataDTO.getIdIndex();
        File file = new File(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + name + ".mp3");
        RequestBody fileBody = RequestBody.create(type, file);

        String typeParameter;
        if (isBookWorm) {

            typeParameter = NovelConstant.novelBook.getTypes();
        } else {

            typeParameter = Constant.TYPE;
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", typeParameter)
                .addFormDataPart("userId", uid)
                .addFormDataPart("newsId", dataDTO.getVoaid())
                .addFormDataPart("paraId", dataDTO.getParaid())
                .addFormDataPart("IdIndex", dataDTO.getIdIndex())
                .addFormDataPart("sentence", dataDTO.getSentence())
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("wordId", "0")
                .addFormDataPart("flg", "0")
                .addFormDataPart("appId", Constant.APPID + "")
                .build();
        presenter.test(requestBody, dataDTO.getIdIndex(), dataDTO.getParaid());


/*        Map<String, RequestBody> params = new HashMap<>();
        params.put("userId", toRequestBody(uid));
        params.put("IdIndex", toRequestBody(dataDTO.getIdindex()));
        params.put("paraId", toRequestBody(dataDTO.getSourceid()));
        params.put("newsId", toRequestBody(dataDTO.getSourceid()));
        params.put("type", toRequestBody(dataDTO.getSource()));
        params.put("sentence", toRequestBody(dataDTO.getSentence()));
        params.put("file\";filename=\"" + file.getName(), fileBody);
        presenter.test(params, dataDTO.getIdIndex());*/
    }

    /**
     * 用来监听播放原音，到指定的位置停止播放
     */
    Handler playHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            Sentence sentence = (Sentence) msg.obj;

            if (evaluatingAdapter.isPlay() && mediaPlayer != null && mediaPlayer.isPlaying() && sentence != null) {

                long endTiming = (long) (Double.parseDouble(sentence.getEndTiming()) * 1000);
                int curPos = mediaPlayer.getCurrentPosition();
                if (curPos > endTiming) {

                    mediaPlayer.pause();
                    evaluatingAdapter.setPlay(false);
                    evaluatingAdapter.notifyDataSetChanged();
                } else {

                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = sentence;
                    playHandler.sendMessage(message);
                }
            }
            return false;
        }
    });

    Handler recordHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (evaluatingAdapter.isRecord() && mediaRecorder != null) {

                double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
                double db = 0;// 分贝
                if (ratio > 1) {

                    db = 20 * Math.log10(ratio);
                    evaluatingAdapter.setDb(db);
                    evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
                }
                recordHandler.sendEmptyMessageDelayed(1, 200);
            }

            return false;
        }
    });


    private RequestBody toRequestBody(String param) {
        return RequestBody.create(MediaType.parse("text/plain"), param);
    }


    @Override
    public void getPublishResult(PublishEvalBean publishEvalBean, String idindex, int shuoshuotype) {

        if (shuoshuotype == 2) {//单句

            List<Sentence> sentenceList = evaluatingAdapter.getData();
            for (int i = 0; i < sentenceList.size(); i++) {

                Sentence sentence = sentenceList.get(i);
                if (sentence.getIdIndex().equals(idindex)) {//存储ShuoshuoId

                    sentence.setShuoshuoId(publishEvalBean.getShuoshuoId() + "");
                    sentence.updateAll("voaid = ? and paraid = ? and idindex = ?"
                            , sentence.getVoaid()
                            , sentence.getParaid()
                            , sentence.getIdIndex());
                    break;
                }
            }
        } else {//发布合成音频后，调用分享

            if (isBookWorm) {
                //剑桥和书虫没有分享
                return;
            }

            //展示获取的奖励
            showReward(publishEvalBean);

            String username;
            if (Constant.userinfo != null) {

                username = Constant.userinfo.getUsername();
            } else {

                username = "";
            }

            String title = username + "在「" + getString(R.string.app_name) + "」中进行口语评测";
            String text = "";
            if (mergeBean != null) {

                String url = Constant.URL_VOA + "/voa/play.jsp?id=" + publishEvalBean.getShuoshuoId() + "&addr=" + mergeBean.getUrl() + "&apptype=" + Constant.TYPE;
                showShare(title, text, url, shareImg());
            }
        }
    }


    /**
     * 展示获取的奖励
     *
     * @param publishEvalBean
     */
    private void showReward(PublishEvalBean publishEvalBean) {

        if (!publishEvalBean.getReward().equals("0")) {

            int reward = Integer.parseInt(publishEvalBean.getReward());
            double rewardDouble = reward / 100.0f;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");


            new AlertDialog.Builder(requireContext())
                    .setTitle("恭喜您！")
                    .setMessage("本次学习获得" + decimalFormat.format(rewardDouble) + "元红包奖励,已自动存入您的钱包账户。\n红包可在【爱语吧】微信公众体现，继续学习领取更多红包奖励吧！")
                    .setPositiveButton("好的", (dialog, which) -> {

                        dialog.dismiss();
                    })
                    .show();
        }
    }

    @Override
    public void getMerge(MergeBean mergeBean) {

        this.mergeBean = mergeBean;

        binding.evaluatingTvMerge.setText("听");
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


    /**
     * 合成音频进度条
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (mediaPlayer.isPlaying()) {

                int cp = mediaPlayer.getCurrentPosition();

                String curStr = getTimeStr(cp);
                String durStr = getTimeStr(mediaPlayer.getDuration());

                binding.evaluatingTvCtime.setText(curStr);
                binding.evaluatingTvTtime.setText(durStr);
                binding.evaluatingSbProgress.setProgress(cp);
                handler.sendEmptyMessageDelayed(1, 200);
            }
            return false;
        }
    });


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
    public void testComplete(EvalBean evalBean, String idindex, String paraid) {

        if (isBookWorm) {

            bookWormEval(evalBean, idindex, paraid);
        } else {

            conceptEval(evalBean, idindex, paraid);
        }

    }


    /**
     * 书虫的评测结果处理
     */
    private void bookWormEval(EvalBean evalBean, String idindex, String paraid) {


        //存储评测的单词
        List<EvalWord> evalWordList = evalBean.getData().getWords();

        if (evalWordList.size() > 0) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    List<Sentence> sentenceList = evaluatingAdapter.getData();
                    Sentence eSentence = null;
                    for (int i = 0; i < sentenceList.size(); i++) {

                        eSentence = sentenceList.get(i);
                        if (eSentence.getParaid().equals(paraid) && eSentence.getIdIndex().equals(idindex)) {

                            break;
                        }
                    }

                    if (eSentence != null) {

                        AppDatabase appDatabase = NovelDB.getInstance().getDB();
                        NovelEvalWordDao novelEvalWordDao = appDatabase.novelEvalWordDao();
                        //删除旧的评测数据
                        novelEvalWordDao.deleteSentenceWord(NovelConstant.novelBook.getTypes(), eSentence.getVoaid(), eSentence.getParaid(), eSentence.getIdIndex(),
                                chapterOrder + "", NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel());
                        for (int i = 0; i < evalWordList.size(); i++) {

                            EvalWord evalWord = evalWordList.get(i);
                            evalWord.setVoaid(eSentence.getVoaid());
                            evalWord.setParaid(eSentence.getParaid());
                            evalWord.setSenIndex(eSentence.getIdIndex());
                            NovelEvalWord novelEvalWord = EvalWordUtil.evalWordToNovelEvalWord(evalWord, chapterOrder + "", NovelConstant.novelBook.getOrder_number(),
                                    NovelConstant.novelBook.getTypes(), NovelConstant.novelBook.getLevel());
                            novelEvalWordDao.insertSingle(novelEvalWord);
                        }
                        eSentence.setWordsDTOS(evalWordList);
                        eSentence.setScore(evalBean.getData().getTotalScore() + "");
                        eSentence.setRecordSoundUrl(evalBean.getData().getUrl());


                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                evaluatingAdapter.notifyDataSetChanged();
                                //将按钮听设置成合成
                                binding.evaluatingTvMerge.setText("合成");
                            }
                        });

                        //存储得分和录音的路径
                        NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
                        NovelSentence novelSentence = novelSentenceDao.getSentenceByPrimaryKeys(eSentence.getTypes(), voa_id + ""
                                , chapterOrder + "", NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel(), eSentence.getParaid(), eSentence.getIdIndex());

                        novelSentence.setScore(evalBean.getData().getTotalScore() + "");
                        novelSentence.setRecordSoundUrl(evalBean.getData().getUrl());
                        novelSentenceDao.updateSingle(novelSentence);


                    }
                }
            }).start();
        }

    }

    /**
     * 新概念评测结果
     *
     * @param evalBean
     * @param idindex
     * @param paraid
     */
    private void conceptEval(EvalBean evalBean, String idindex, String paraid) {


        //记录评测的次数
        int count = evalSP.getInt("eavl" + voa_id, 0);
        evalSP.edit().putInt("eavl" + voa_id, ++count).apply();

        //存储评测的单词
        List<EvalWord> evalWordList = evalBean.getData().getWords();
        for (int i = 0; i < evalWordList.size(); i++) {

            EvalWord evalWord = evalWordList.get(i);

            //设置voaid
            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                evalWord.setVoaid(voa_id + "");
            } else {

                if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                    evalWord.setVoaid(voa_id + "");
                } else {
                    evalWord.setVoaid(voa_id * 10 + "");
                }
            }

            evalWord.setParaid(paraid);
            evalWord.setSenIndex(idindex);

            int eCount;

            eCount = LitePal.where("voaid = ? and paraid = ? and senIndex = ? and index = ?",
                            evalWord.getVoaid(),
                            evalWord.getParaid(),
                            evalWord.getSenIndex(),
                            evalWord.getIndex())
                    .count(EvalWord.class);
            if (eCount == 0) {

                evalWord.save();
            } else {

                evalWord.updateAll("voaid = ? and paraid = ? and senIndex = ? and index = ?",
                        evalWord.getVoaid(),
                        evalWord.getParaid(),
                        evalWord.getSenIndex(),
                        evalWord.getIndex());
            }
        }

        List<Sentence> sentenceList = evaluatingAdapter.getData();
        for (int i = 0; i < sentenceList.size(); i++) {

            Sentence sentence = sentenceList.get(i);
            if (sentence.getIdIndex().equals(idindex) && sentence.getParaid().equals(paraid)) {

                sentence.setRecordSoundUrl(evalBean.getData().getUrl());
                sentence.setScore(evalBean.getData().getTotalScore() + "");
                sentence.setWordsDTOS(evalBean.getData().getWords());

                sentence.updateAll("voaid = ? and paraid = ? and idIndex = ?", sentence.getVoaid(), sentence.getParaid(), sentence.getIdIndex());
                evaluatingAdapter.notifyItemChanged(i);
                break;
            }
        }
        //将按钮听设置成合成
        binding.evaluatingTvMerge.setText("合成");

        //刷新首页评测进度
        EventBus.getDefault().post(new Refresh(voa_id));

    }
}