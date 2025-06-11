package com.jn.iyuba.concept.simple.activity.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.InterstitialAd;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.activity.login.WxLoginActivity;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.model.bean.home.ReadSubmitBean;
import com.jn.iyuba.concept.simple.presenter.home.ParaPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.view.home.ParaContract;
import com.jn.iyuba.novel.db.Chapter;
import com.jn.iyuba.novel.db.dao.ChapterDao;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.ParaAdapter;
import com.jn.iyuba.succinct.databinding.FragmentParaBinding;
import com.jn.iyuba.concept.simple.entity.Para;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.novel.NovelConstant;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.db.NovelSentence;
import com.jn.iyuba.novel.db.dao.NovelSentenceDao;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * 主页面——阅读（只有剑桥书虫有）
 */
public class ParaFragment extends BaseFragment<ParaContract.ParaView, ParaContract.ParaPresenter>
        implements ParaContract.ParaView {


    private int voaid;

    /**
     * 章节序号
     */
    private int chapterOrder;

    private FragmentParaBinding binding;

    private ParaAdapter paraAdapter;

    private LineItemDecoration lineItemDecoration;


    /**
     * 是否是书虫
     */
    private boolean isBookWorm = true;


    /**
     * 考试阅读的开始时间
     */
    private long startTime = 0;

    /**
     * 考试阅读的结束时间
     */
    private long endTime = 0;

    /**
     * 单词数量
     */
    private int wordCount = 0;

    private InterstitialAd interstitialAd;

    public ParaFragment() {
    }


    public static ParaFragment newInstance(int voaid, int chapterOrder, boolean isBookWorm) {
        ParaFragment fragment = new ParaFragment();
        Bundle args = new Bundle();
        args.putInt("VOAID", voaid);
        args.putInt("CHAPTER_ORDER", chapterOrder);
        args.putBoolean("IS_BOOK_WORM", isBookWorm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            voaid = getArguments().getInt("VOAID");
            chapterOrder = getArguments().getInt("CHAPTER_ORDER");
            isBookWorm = getArguments().getBoolean("IS_BOOK_WORM");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        interstitialAd();

        initOperation();

        lineItemDecoration = new LineItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(view.getResources().getDrawable(R.drawable.space_10dp));
        if (binding.paraRv.getItemDecorationCount() == 0) {

            binding.paraRv.addItemDecoration(lineItemDecoration);
        }

        binding.paraRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        paraAdapter = new ParaAdapter(R.layout.item_para, new ArrayList<>());
        binding.paraRv.setAdapter(paraAdapter);

        if (isBookWorm) {

            getData();
        } else {

            getDataForConcept();
        }
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentParaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected ParaContract.ParaPresenter initPresenter() {
        return new ParaPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();

        startTime = System.currentTimeMillis();
    }

    /**
     * 点击事件
     */
    private void initOperation() {


        binding.paraButSubmit.setOnClickListener(v -> {


            if (Constant.userinfo == null) {//是否登录

                new AlertDialog.Builder(requireContext())
                        .setTitle("信息")
                        .setMessage("您还未登录，去登录吗？")
                        .setPositiveButton("确定", (dialog, which) -> {

                            startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                        })
                        .setNegativeButton("取消", (dialog, which) -> {

                            dialog.dismiss();
                        })
                        .show();
                return;
            }


            endTime = System.currentTimeMillis();
            double time = 1.0 * (endTime - startTime);
            int readspeed = (int) (wordCount / (time / (1000 * 60)));

            if (readspeed > Constant.NORMAL_WPM) {

                AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("阅读提醒")
                        .setMessage("你认真读完了这篇文章吗?请用正常速度阅读。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {


                int m_used = (int) (time / (1000 * 60));
                int s_used = (int) ((time / 1000) % 60);
                final String timestr = m_used + "分" + s_used + "秒";

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder
                        .append("当前文章单词数量：").append(wordCount + "\n")
                        .append("当前的阅读时间：").append(timestr + "\n")
                        .append("当前的阅读速度：").append(readspeed + "单词/分钟");


                AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("阅读提醒")
                        .setMessage(stringBuilder.toString())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                                simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");

                                String beginTime = simpleDateFormat.format(new Date(startTime));
                                String endTimeStr = simpleDateFormat.format(new Date(endTime));
                                String lesson;
                                if (isBookWorm) {

                                    lesson = NovelConstant.novelBook.getTypes();
                                } else {

                                    lesson = Constant.TYPE;
                                }


                                presenter.updateNewsStudyRecord("json", Constant.userinfo.getUid(), beginTime, endTimeStr, lesson,
                                        lesson, voaid, Constant.APPID, "",
                                        "", 1, wordCount, 0, "android", 1);
                            }
                        })
                        .show();
            }

        });
    }


    private void getDataForConcept() {

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

        //组装paraList
        List<Para> paraList = new ArrayList<>();
        for (int i = 0; i < sentences.size(); i++) {

            Sentence sentence = sentences.get(i);
            wordCount = wordCount + sentence.getSentence().split("\\s").length;

            Para para = new Para();
            para.setEn(sentence.getSentence());
            para.setCn(sentence.getSentenceCn());
            paraList.add(para);
        }

        paraAdapter.setNewData(paraList);
    }

    /**
     * 获取数据
     */
    private void getData() {

        new Thread(() -> {


            AppDatabase appDatabase = NovelDB.getInstance().getDB();
            NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();

            List<NovelSentence> novelSentenceList = novelSentenceDao.getDataByPrimaryKeys(NovelConstant.novelBook.getTypes(), voaid + "", chapterOrder + ""
                    , NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel());

            List<Para> paraList = new ArrayList<>();
            StringBuilder eStringBuilder = null;
            StringBuilder cStringBuilder = null;
            //组装paraList
            for (int i = 0; i < novelSentenceList.size(); i++) {

                NovelSentence novelSentence = novelSentenceList.get(i);

                wordCount = wordCount + novelSentence.getTextEN().split("\\s").length;
                if (i == 0) {//第一项

                    eStringBuilder = new StringBuilder(novelSentence.getTextEN());
                    cStringBuilder = new StringBuilder(novelSentence.getTextCH());
                } else {

                    NovelSentence pre = novelSentenceList.get(i - 1);
                    if (novelSentence.getParaid().equals(pre.getParaid())) {//是同一个

                        eStringBuilder.append(novelSentence.getTextEN());
                        cStringBuilder.append(novelSentence.getTextCH());
                    } else {

                        Para para = new Para();
                        para.setEn(eStringBuilder.toString());
                        para.setCn(cStringBuilder.toString());
                        paraList.add(para);
                        //清空数据
                        eStringBuilder.delete(0, eStringBuilder.length());
                        cStringBuilder.delete(0, cStringBuilder.length());
                        eStringBuilder.append(novelSentence.getTextEN());
                        cStringBuilder.append(novelSentence.getTextCH());
                    }

                    if (i == (novelSentenceList.size() - 1) && !eStringBuilder.toString().equals("")) {
                        Para para = new Para();
                        para.setEn(eStringBuilder.toString());
                        para.setCn(cStringBuilder.toString());
                        paraList.add(para);
                    }
                }
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    paraAdapter.setNewData(paraList);
                }
            });

        }).start();

    }

    /**
     * 通过顺序播放和随机播放来更新数据
     */
    public void updateData() {

        if (getArguments() != null) {

            voaid = getArguments().getInt("VOAID");
            chapterOrder = getArguments().getInt("CHAPTER_ORDER");
        }

        getData();
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
     * 加载华为的
     */
    private void interstitialAd() {

        interstitialAd = new InterstitialAd(MyApplication.getContext());
        interstitialAd.setAdId("s8dj06xyy9");
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);

        interstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("showInterstitialAd", "onAdClosed");
            }

            @Override
            public void onAdFailed(int i) {
                super.onAdFailed(i);
                Log.d("showInterstitialAd", "onAdFailed" + i);
            }

            @Override
            public void onAdLeave() {
                super.onAdLeave();
                Log.d("showInterstitialAd", "onAdLeave");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d("showInterstitialAd", "onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("showInterstitialAd", "onAdLoaded");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.d("showInterstitialAd", "onAdClicked");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.d("showInterstitialAd", "onAdImpression");
            }
        });
    }

    private void showInterstitialAd() {
        // 显示广告
        if (interstitialAd != null && interstitialAd.isLoaded()) {

            interstitialAd.show(requireActivity());
        } else {

//            Toast.makeText(requireActivity(), "Ad did not load", Toast.LENGTH_SHORT).show();
            Log.d("showInterstitialAd", "没有广告");
        }
    }

    @Override
    public void submitRead(ReadSubmitBean readSubmitBean) {

        showInterstitialAd();

        if (!readSubmitBean.getReward().equals("0") && !readSubmitBean.getJifen().equals("0")) {

            int reward = Integer.parseInt(readSubmitBean.getReward());
            double rewardDouble = reward / 100.0f;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");


            new AlertDialog.Builder(requireContext())
                    .setTitle("恭喜您！")
                    .setMessage("本次学习获得" + decimalFormat.format(rewardDouble) + "元红包奖励,已自动存入您的钱包账户。\n红包可在【爱语吧】微信公众体现，继续学习领取更多红包奖励吧！")
                    .setPositiveButton("好的", (dialog, which) -> {

                        dialog.dismiss();
                    })
                    .show();
        } else if (readSubmitBean.getReward().equals("0") && !readSubmitBean.getJifen().equals("0")) {

            int jifen = Integer.parseInt(readSubmitBean.getJifen());
            new AlertDialog.Builder(requireContext())
                    .setTitle("恭喜您！")
                    .setMessage("本次学习获得" + jifen + "积分奖励。")
                    .setPositiveButton("好的", (dialog, which) -> {

                        dialog.dismiss();
                    })
                    .show();

        } else if (!readSubmitBean.getReward().equals("0") && readSubmitBean.getJifen().equals("0")) {

            int reward = Integer.parseInt(readSubmitBean.getReward());
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
}