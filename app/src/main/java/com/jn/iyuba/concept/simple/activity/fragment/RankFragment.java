package com.jn.iyuba.concept.simple.activity.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.activity.RankingDetailsActivity;
import com.jn.iyuba.concept.simple.adapter.RankingAdapter;
import com.jn.iyuba.succinct.databinding.FragmentRankBinding;
import com.jn.iyuba.concept.simple.model.bean.AudioRankingBean;
import com.jn.iyuba.concept.simple.presenter.home.RankPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.view.home.RankingContract;
import com.jn.iyuba.novel.NovelConstant;

import java.util.ArrayList;

/**
 * 排行榜
 */
public class RankFragment extends BaseFragment<RankingContract.RankingView, RankingContract.RankingPresenter>
        implements RankingContract.RankingView {

    private FragmentRankBinding binding;

    private RankingAdapter rankingAdapter;

    private final int pageNum = 10;

    private int page = 1;

    private int voaId;

    /**
     * 是否是书虫
     */
    private boolean isBookWorm = false;

    private AudioRankingBean audioRankingBean;

    /**
     * 随机播放或者书序播放
     * 更新数据
     */
    public void updateData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            voaId = bundle.getInt("VOA_ID", 0);
        }
        firstData();
    }

    public RankFragment() {

    }


    public static RankFragment newInstance(int voaId, boolean isBookWorm) {

        RankFragment fragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("VOA_ID", voaId);
        bundle.putBoolean("IS_BOOK_WORM", isBookWorm);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {

            voaId = bundle.getInt("VOA_ID", 0);
            isBookWorm = bundle.getBoolean("IS_BOOK_WORM", false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();

    }

    private void initOperation() {

        rankingAdapter = new RankingAdapter(R.layout.item_ranking, new ArrayList<>());
        binding.rankingRvList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rankingRvList.setAdapter(rankingAdapter);
        rankingAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                page++;
                int uid = Constant.userinfo == null ? 0 : Constant.userinfo.getUid();

                if (isBookWorm) {

                    String sign = MD5Util.MD5(uid + NovelConstant.novelBook.getTypes() + (page - 1) * pageNum + "" + pageNum + DateUtil.getCurDate());

                    presenter.getTopicRanking(NovelConstant.novelBook.getTypes(), voaId, uid, "D", (page - 1) * pageNum, pageNum, sign);

                } else {

                    String sign = MD5Util.MD5(uid + Constant.TYPE + (page - 1) * pageNum + "" + pageNum + DateUtil.getCurDate());
                    if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {//新概念英语

                        presenter.getTopicRanking(Constant.TYPE, voaId, uid, "D", (page - 1) * pageNum, pageNum, sign);
                    } else {

                        if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                            presenter.getTopicRanking(Constant.TYPE, voaId, uid, "D", (page - 1) * pageNum, pageNum, sign);
                        } else {

                            presenter.getTopicRanking(Constant.TYPE, voaId * 10, uid, "D", (page - 1) * pageNum, pageNum, sign);
                        }
                    }
                }

            }
        }, binding.rankingRvList);

        rankingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                AudioRankingBean.DataDTO dataDTO = rankingAdapter.getItem(position);
                RankingDetailsActivity.startActivity(requireActivity(), voaId, dataDTO.getUid(), dataDTO.getImgSrc(), dataDTO.getName(), isBookWorm);
            }
        });
        //自己的跳转
        binding.rankingCvMyself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (audioRankingBean == null) {

                    return;
                }
                RankingDetailsActivity.startActivity(requireActivity(), voaId, audioRankingBean.getMyid(), audioRankingBean.getMyimgSrc(), audioRankingBean.getMyname(), isBookWorm);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        firstData();
    }


    public void firstData() {

        page = 1;
        int uid = Constant.userinfo == null ? 0 : Constant.userinfo.getUid();
        String types;
        if (isBookWorm) {

            types = NovelConstant.novelBook.getTypes();
        } else {
            types = Constant.TYPE;
        }
        String sign = MD5Util.MD5(uid + types + (page - 1) + "" + pageNum + DateUtil.getCurDate());


        if (isBookWorm) {

            presenter.getTopicRanking(types, voaId, uid, "D", 0, 10, sign);
        } else {

            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {//新概念英语

                presenter.getTopicRanking(Constant.TYPE, voaId, uid, "D", 0, 10, sign);
            } else {

                if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                    presenter.getTopicRanking(Constant.TYPE, voaId, uid, "D", 0, 10, sign);
                } else {

                    presenter.getTopicRanking(Constant.TYPE, voaId * 10, uid, "D", 0, 10, sign);
                }
            }
        }
    }


    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        binding = FragmentRankBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected RankingContract.RankingPresenter initPresenter() {
        return new RankPresenter();
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
    public void getTopicRanking(AudioRankingBean audioRankingBean, int start) {


        if (start == 0) {

            rankingAdapter.setNewData(audioRankingBean.getData());
        } else {

            rankingAdapter.addData(audioRankingBean.getData());
            int size = audioRankingBean.getResult();
            if (size == pageNum) {

                rankingAdapter.loadMoreComplete();
            } else {

                rankingAdapter.loadMoreEnd();
            }
        }

        this.audioRankingBean = audioRankingBean;
        if (Constant.userinfo == null) {

            binding.rankingCvMyself.setVisibility(View.GONE);
        } else {

            binding.rankingCvMyself.setVisibility(View.VISIBLE);
            binding.rankingTvIndex.setText(audioRankingBean.getMyranking() + "");

            Glide.with(MyApplication.getContext()).load(audioRankingBean.getMyimgSrc()).into(binding.rankingIvPortrait);
            binding.rankingTvName.setText(audioRankingBean.getMyname());
            binding.rankingTvSentence.setText("句子数：" + audioRankingBean.getMycount());
            int avg = audioRankingBean.getMycount() == 0 ? 0 : audioRankingBean.getMyscores() / audioRankingBean.getMycount();
            binding.rankingTvAvg.setText("平均分" + avg);
            binding.rankingTvScore.setText(audioRankingBean.getMyscores() + "分");
        }
    }
}