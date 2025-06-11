package com.jn.iyuba.concept.simple.activity.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.adapter.DubbingRankAdapter;
import com.jn.iyuba.succinct.databinding.FragmentVideoRankBinding;
import com.jn.iyuba.concept.simple.model.bean.DubbingRankBean;
import com.jn.iyuba.concept.simple.presenter.home.VideoRankPresenter;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.concept.simple.view.home.VideoRankContract;

import java.util.ArrayList;

/**
 * 配音排行榜
 */
public class VideoRankFragment extends BaseFragment<VideoRankContract.VideoRankView, VideoRankContract.VideoRankPresenter>
        implements VideoRankContract.VideoRankView {

    private FragmentVideoRankBinding binding;

    private int voaid;

    private int pageNumber;


    private DubbingRankAdapter dubbingRankAdapter;


    /**
     * 更新数据
     */
    public void updateData() {

        if (getArguments() != null) {

            voaid = getArguments().getInt("VOAID");
        }
        pageNumber = 1;
        presenter.getDubbingRank("android", "json", 60001, voaid + "",
                pageNumber, 10, 1, Constant.TYPE, 3);
    }

    public VideoRankFragment() {
        // Required empty public constructor
    }

    public static VideoRankFragment newInstance(int voaid) {
        VideoRankFragment fragment = new VideoRankFragment();
        Bundle args = new Bundle();
        args.putInt("VOAID", voaid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            voaid = getArguments().getInt("VOAID");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dubbingRankAdapter = new DubbingRankAdapter(R.layout.item_dubbing_rank, new ArrayList<>());
        binding.vrRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.vrRv.setAdapter(dubbingRankAdapter);
        if (binding.vrRv.getItemDecorationCount() == 0) {

            LineItemDecoration lineItemDecoration = new LineItemDecoration(MyApplication.getContext(), LinearLayoutManager.VERTICAL);
            lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));
            binding.vrRv.addItemDecoration(lineItemDecoration);
        }

        dubbingRankAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                pageNumber++;
                presenter.getDubbingRank("android", "json", 60001, voaid + "",
                        pageNumber, 10, 1, Constant.TYPE, 3);
            }
        }, binding.vrRv);

        pageNumber = 1;
        presenter.getDubbingRank("android", "json", 60001, voaid + "", pageNumber, 10, 1, Constant.TYPE, 3);
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentVideoRankBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected VideoRankContract.VideoRankPresenter initPresenter() {
        return new VideoRankPresenter();
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
    public void getDubbingRank(DubbingRankBean dubbingRankBean) {


        if(dubbingRankBean.getPageNumber() == 1){

            dubbingRankAdapter.setNewData(dubbingRankBean.getData());
        }else{

            dubbingRankAdapter.addData(dubbingRankBean.getData());
        }


        if (dubbingRankBean.getPageNumber() == dubbingRankBean.getTotalPage()) {


            dubbingRankAdapter.loadMoreEnd();
        } else {

            dubbingRankAdapter.loadMoreComplete();
        }

    }
}