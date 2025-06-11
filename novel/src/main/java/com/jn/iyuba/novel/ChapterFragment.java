package com.jn.iyuba.novel;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.novel.activity.BaseFragment;
import com.jn.iyuba.novel.activity.ChooseLessonActivity;
import com.jn.iyuba.novel.adapter.ChapterAdapter;
import com.jn.iyuba.novel.databinding.NovelFragmentChapterBinding;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.NovelBook;
import com.jn.iyuba.novel.db.Chapter;
import com.jn.iyuba.novel.db.dao.ChapterDao;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.entity.NovelRefresh;
import com.jn.iyuba.novel.model.bean.ChapterBean;
import com.jn.iyuba.novel.presenter.ChapterPresenter;
import com.jn.iyuba.novel.util.LineItemDecoration;
import com.jn.iyuba.novel.view.ChapterContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 章节列表
 */
public class ChapterFragment extends BaseFragment<ChapterContract.ChapterView, ChapterContract.ChapterPresenter>
        implements ChapterContract.ChapterView {


    private ChapterAdapter chapterAdapter;

    private NovelFragmentChapterBinding binding;

    private LineItemDecoration lineItemDecoration;

    public ChapterFragment() {
    }

    public static ChapterFragment newInstance() {
        ChapterFragment fragment = new ChapterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 选择课本后触发更新数据
     *
     * @param novelBook
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NovelBook novelBook) {

        presenter.getStroryInfo("book", Integer.parseInt(NovelConstant.novelBook.getLevel()),
                Integer.parseInt(NovelConstant.novelBook.getOrder_number()), NovelConstant.novelBook.getTypes());
    }


    /**
     * 更新adapter中的某一条数据的进度
     *
     * @param novelRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NovelRefresh novelRefresh) {

        if (chapterAdapter == null) {

            return;
        }
        List<Chapter> chapterList = chapterAdapter.getData();
        for (int i = 0; i < chapterList.size(); i++) {

            Chapter chapter = chapterList.get(i);
            if (novelRefresh.getVoaid() == Integer.parseInt(chapter.getVoaid())) {

                chapterAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation(view);
        binding.chapterPb.setVisibility(View.VISIBLE);
        presenter.getStroryInfo("book", Integer.parseInt(NovelConstant.novelBook.getLevel()),
                Integer.parseInt(NovelConstant.novelBook.getOrder_number()), NovelConstant.novelBook.getTypes());
    }

    private void initOperation(View view) {

        binding.toolbar.toolbarIvBack.setVisibility(View.INVISIBLE);
        binding.toolbar.toolbarIvTitle.setText("阅读");
        binding.toolbar.toolbarIvRight.setImageResource(R.mipmap.ic_book);
        binding.toolbar.toolbarIvRight.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(requireActivity(), ChooseLessonActivity.class));
            }
        });

        lineItemDecoration = new LineItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(view.getResources(), R.drawable.shape_rctg_space_10dp, requireActivity().getTheme()));
        binding.chapterRvList.addItemDecoration(lineItemDecoration);


        chapterAdapter = new ChapterAdapter(R.layout.novel_item_chapter, new ArrayList<>());
        binding.chapterRvList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.chapterRvList.setAdapter(chapterAdapter);

        binding.chapterRvList.setItemAnimator(null);
        //列表的点击事件
        chapterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Chapter chapter = chapterAdapter.getItem(position);
                EventBus.getDefault().post(chapter);
            }
        });
        binding.chapterFlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.chapterPb.setVisibility(View.VISIBLE);
                binding.chapterFlError.setVisibility(View.GONE);
                presenter.getStroryInfo("book", Integer.parseInt(NovelConstant.novelBook.getLevel()),
                        Integer.parseInt(NovelConstant.novelBook.getOrder_number()), NovelConstant.novelBook.getTypes());
            }
        });

    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        binding = NovelFragmentChapterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected ChapterContract.ChapterPresenter initPresenter() {
        return new ChapterPresenter();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(NovelApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void getStroryInfo(ChapterBean chapterBean, String from) {


        if (chapterBean == null) {

            binding.chapterRvList.setVisibility(View.GONE);
            binding.chapterPb.setVisibility(View.GONE);
            binding.chapterFlError.setVisibility(View.VISIBLE);
        } else {

            binding.chapterRvList.setVisibility(View.VISIBLE);
            binding.chapterPb.setVisibility(View.GONE);
            binding.chapterFlError.setVisibility(View.GONE);

            chapterAdapter.setNewData(chapterBean.getChapterInfo());

            //存储Chapter数据
            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    ChapterDao chapterDao = appDatabase.chapterDao();
                    List<Chapter> chapterList = chapterBean.getChapterInfo();
                    for (int i = 0; i < chapterList.size(); i++) {

                        Chapter chapter = chapterList.get(i);
                        //处理获取书虫数据没有types的问题
                        if (chapter.getTypes() == null) {

                            chapter.setTypes("bookworm");
                        }

                        int count = chapterDao.getCountByID(chapter.getVoaid(), chapter.getTypes(), chapter.getLevel(),
                                chapter.getOrder_number(), chapter.getChapter_order());
                        if (count == 0) {

                            chapterDao.insertSingle(chapter);
                        }
                    }

                }
            }).start();
        }
    }
}