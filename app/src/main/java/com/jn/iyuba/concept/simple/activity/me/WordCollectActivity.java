package com.jn.iyuba.concept.simple.activity.me;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.adapter.me.WordCollectAdapter;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordCollectListBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordPdfBean;
import com.jn.iyuba.concept.simple.presenter.me.WordCollectPresenter;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.concept.simple.view.me.WordCollectContract;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.ActivityWordCollectBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏单词的列表
 */
public class WordCollectActivity extends BaseActivity<WordCollectContract.WordCollectView, WordCollectContract.WordCollectPresenter>
        implements WordCollectContract.WordCollectView {

    private ActivityWordCollectBinding binding;

    private WordCollectAdapter wordCollectAdapter;

    private LineItemDecoration lineItemDecoration;

    private int pageNumber = 1;

    private int pageCounts = 30;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding.toolbar.toolbarTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getWordToPDF(Constant.userinfo.getUid(), 1, 2000);
            }
        });
        binding.wcRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        wordCollectAdapter = new WordCollectAdapter(R.layout.item_word_collect, new ArrayList<>());
        binding.wcRv.setAdapter(wordCollectAdapter);
        lineItemDecoration = new LineItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        binding.wcRv.addItemDecoration(lineItemDecoration);

        binding.toolbar.toolbarIvTitle.setText("我的单词");
        binding.toolbar.toolbarIvBack.setOnClickListener(v -> finish());

        wordCollectAdapter.setOnLoadMoreListener(() -> {

            pageNumber++;
            presenter.wordListService(Constant.userinfo.getUid() + "", pageNumber, pageCounts, "json");
        }, binding.wcRv);

        wordCollectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


                WordCollectListBean.DataDTO dataDTO = wordCollectAdapter.getItem(position);
                int id = view.getId();
                if (id == R.id.wc_iv_audio && dataDTO != null) {

                    initMediaPlayer(dataDTO.getAudio());
                } else if (id == R.id.wc_tv_delete) {

                    alertDelete(dataDTO.getWord());
                }
            }
        });


        presenter.wordListService(Constant.userinfo.getUid() + "", pageNumber, pageCounts, "json");
    }


    private void initMediaPlayer(String url) {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mediaPlayer.start();
                }
            });
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void alertDelete(String word) {

        new AlertDialog.Builder(WordCollectActivity.this)
                .setTitle("提示")
                .setMessage("是否要删除")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        presenter.updateWord("Iyuba", "delete", word, Constant.userinfo.getUid() + "", "json");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public View initLayout() {

        binding = ActivityWordCollectBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public WordCollectContract.WordCollectPresenter initPresenter() {
        return new WordCollectPresenter();
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
    public void wordListService(WordCollectListBean wordCollectListBean) {


        if (wordCollectListBean == null) {

            wordCollectAdapter.loadMoreFail();
        } else {

            if (pageNumber == 1) {

                wordCollectAdapter.setNewData(wordCollectListBean.getData());
                if (wordCollectListBean.getCounts() < pageCounts) {

                    wordCollectAdapter.loadMoreEnd();
                } else {
                    wordCollectAdapter.loadMoreComplete();
                }
            } else {

                wordCollectAdapter.addData(wordCollectListBean.getData());
                if (wordCollectListBean.getCounts() < pageCounts) {

                    wordCollectAdapter.loadMoreEnd();
                } else {
                    wordCollectAdapter.loadMoreComplete();
                }

            }
        }
    }

    @Override
    public void updateWord(WordCollectBean wordCollectBean) {//删除

        if (wordCollectAdapter == null) {

            return;
        }

        int index = -1;
        List<WordCollectListBean.DataDTO> dataDTOList = wordCollectAdapter.getData();
        for (int i = 0; i < dataDTOList.size(); i++) {

            WordCollectListBean.DataDTO dataDTO = dataDTOList.get(i);
            if (wordCollectBean.getStrWord().equals(dataDTO.getWord())) {

                index = i;
                break;
            }
        }
        if (index != -1) {

            wordCollectAdapter.remove(index);
        }
    }

    @Override
    public void getWordToPDF(WordPdfBean wordPdfBean) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(wordPdfBean.getFilePath())));
    }
}