package com.jn.iyuba.concept.simple.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.RankingDetailsAdapter;
import com.jn.iyuba.succinct.databinding.ActivityRankingDetailsBinding;
import com.jn.iyuba.concept.simple.model.bean.RankingDetailsBean;
import com.jn.iyuba.concept.simple.presenter.home.RankingDetailsPresenter;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.view.home.RankingDetailsContract;
import com.jn.iyuba.novel.NovelConstant;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜-用户音频信息
 */
public class RankingDetailsActivity extends BaseActivity<RankingDetailsContract.RankingDetailsView, RankingDetailsContract.RankingDetailsPresenter>
        implements RankingDetailsContract.RankingDetailsView {

    private ActivityRankingDetailsBinding binding;

    private RankingDetailsAdapter rankingDetailsAdapter;

    private int lessonid;

    private int uid = 0;

    private String portrait;

    private MediaPlayer mediaPlayer;

    private String name;

    //是否是剑桥书虫
    private boolean isBookWorm = false;


    public static void startActivity(Activity activity, int lessonid, int uid, String portrait, String name, boolean isBookWorm) {

        Intent intent = new Intent(activity, RankingDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("LESSONID", lessonid);
        bundle.putInt("UID", uid);
        bundle.putString("PORTRAIT", portrait);
        bundle.putString("NAME", name);
        bundle.putBoolean("IS_BOOK_WORM", isBookWorm);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            lessonid = bundle.getInt("LESSONID");
            name = bundle.getString("NAME");
            portrait = bundle.getString("PORTRAIT");
            uid = bundle.getInt("UID", uid);
            isBookWorm = bundle.getBoolean("IS_BOOK_WORM", false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle();

        binding.toolbar.toolbarIvTitle.setText(name + "的评测");
        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        rankingDetailsAdapter = new RankingDetailsAdapter(R.layout.item_ranking_details, new ArrayList<>());
        binding.rdRv.setLayoutManager(new LinearLayoutManager(this));
        binding.rdRv.setAdapter(rankingDetailsAdapter);

        rankingDetailsAdapter.setName(name);
        rankingDetailsAdapter.setPortrait(portrait);

        rankingDetailsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                RankingDetailsBean.DataDTO dataDTO = rankingDetailsAdapter.getItem(position);
                initMediaPlayer(dataDTO.getShuoShuo());
            }
        });

        String sign = uid + "getWorksByUserId" + DateUtil.getCurDate();
        String type;
        if (isBookWorm) {

            type = NovelConstant.novelBook.getTypes();
        } else {
            type = Constant.TYPE;
        }
        presenter.getWorksByUserId(uid, type, lessonid, "2,4", MD5Util.MD5(sign));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {

            mediaPlayer.release();
        }
    }

    @Override
    public View initLayout() {

        binding = ActivityRankingDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public RankingDetailsContract.RankingDetailsPresenter initPresenter() {
        return new RankingDetailsPresenter();
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
    public void getMergeData(RankingDetailsBean rankingDetailsBean) {

        rankingDetailsAdapter.setNewData(rankingDetailsBean.getData());
    }


    /**
     * 播放句子
     */
    private void initMediaPlayer(String path) {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mediaPlayer.start();

                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        }

        mediaPlayer.reset();
        try {
            //http://static2.iyuba.cn/Japan/jp3/talk/001/001_01.mp3

            String url = Constant.IUSERSPEECH_URL + "/voa/" + path;
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}