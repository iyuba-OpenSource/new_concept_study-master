package com.jn.iyuba.concept.simple.activity.break_through;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.imooclib.event.ImoocPlayEvent;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.break_through.CPWordAdapter;
import com.jn.iyuba.succinct.databinding.ActivityBtwordsBinding;
import com.jn.iyuba.concept.simple.db.ConceptWord;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 单词闯关单词列表
 */
public class BTWordsActivity extends AppCompatActivity {


    private ActivityBtwordsBinding activityBtwordsBinding;

    private int bookid;

    private int voaid;

    private int level;

    /**
     * 青少版unit_id
     */
    private int unit_id;

    private CPWordAdapter cpWordAdapter;

    private LineItemDecoration lineItemDecoration;

    private MediaPlayer mediaPlayer;

    /**
     * 1单词音频
     * 2 句子音频
     */
    private int flag = 1;

    private ConceptWord conceptWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBtwordsBinding = ActivityBtwordsBinding.inflate(getLayoutInflater());
        setContentView(activityBtwordsBinding.getRoot());
        getBundle();


        activityBtwordsBinding.toolbar.toolbarIvRight.setVisibility(View.GONE);
        initOperation();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();
    }

    private void initOperation() {

        activityBtwordsBinding.toolbar.toolbarIvTitle.setText("第" + (level + 1) + "关单词");
        activityBtwordsBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //开始学习
/*        activityBtwordsBinding.btwordsButStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WordDetailsActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData(), 0);
            }
        });*/
        //开始闯关
        activityBtwordsBinding.btwordsButBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WordAnswerActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData());
            }
        });


        lineItemDecoration = new LineItemDecoration(this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_line_word));
        activityBtwordsBinding.btwordsRvList.addItemDecoration(lineItemDecoration);

        activityBtwordsBinding.btwordsRvList.setLayoutManager(new LinearLayoutManager(this));
        cpWordAdapter = new CPWordAdapter(R.layout.item_cp_word, new ArrayList<>());
        activityBtwordsBinding.btwordsRvList.setAdapter(cpWordAdapter);

        cpWordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                ConceptWord conceptWord = cpWordAdapter.getItem(position);

                if (view.getId() == R.id.word_iv_sentence) {

                    flag = 2;

                    String url;
                    if (conceptWord.getSentence_single_audio() != null) {

                        url = conceptWord.getSentence_single_audio();
                    } else {
                        url = conceptWord.getSentenceAudio();
                    }
                    initMediaPlayer(url, conceptWord);
                } else {

                    flag = 1;
                    initMediaPlayer(conceptWord.getAudio(), conceptWord);
                }

            }
        });
/*        cpWordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


                WordDetailsActivity.startActivity(BTWordsActivity.this, cpWordAdapter.getData(), position);
            }
        });*/
    }

    public int getFlag() {
        return flag;
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (mediaPlayer == null) {

                return false;
            }

            if (mediaPlayer.isPlaying()) {

                ConceptWord conceptWord1 = (ConceptWord) msg.obj;
                int endTiming = (int) (Double.parseDouble(conceptWord1.getEndTiming()) * 1000);
                if (mediaPlayer.getCurrentPosition() >= endTiming) {

                    mediaPlayer.pause();
                } else {
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = conceptWord1;
                    handler.sendMessage(message);
                }
            }

            return false;
        }
    });

    private void initMediaPlayer(String url, ConceptWord conceptWord) {


        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();

        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //暂停音频播放
                EventBus.getDefault().post(new ImoocPlayEvent());
                if (getFlag() == 1) {
                    mediaPlayer.start();
                } else {

//                    mediaPlayer.seekTo((int) (Double.parseDouble(conceptWord.getTiming()) * 1000));
                    mediaPlayer.start();
//                    Message message = Message.obtain();
//                    message.what = 1;
//                    message.obj = conceptWord;
//                    handler.sendMessage(message);
                }
            }
        });
        try {
            if (flag == 1) {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
            } else {

                if (mediaPlayer.isPlaying()) {

                    mediaPlayer.pause();
                } else {

                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            voaid = bundle.getInt("VOA_ID", 0);
            bookid = bundle.getInt("BOOK_ID", 1);
            level = bundle.getInt("LEVEL", 0);
            unit_id = bundle.getInt("UNIT_ID", 0);
        }
    }

    private void initData() {

        List<ConceptWord> jpConceptWordList;
        if (unit_id == 0) {//新概念普通版

            jpConceptWordList = LitePal
                    .where("voaId = ? and bookid = ?", voaid + "", bookid + "")
                    .order("position")
                    .find(ConceptWord.class);

        } else {//新概念青少版

            jpConceptWordList = LitePal
                    .where("bookid = ? and unit_id = ?", bookid + "", unit_id + "")
                    .order("position")
                    .find(ConceptWord.class);
        }
        cpWordAdapter.setNewData(jpConceptWordList);
    }

    /**
     * @param activity
     * @param voa_id
     * @param bookid
     * @param level
     * @param unit_id  青少版unit_id
     */
    public static void startActivity(Activity activity, int voa_id, int bookid, int level, int unit_id) {

        Intent intent = new Intent(activity, BTWordsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("VOA_ID", voa_id);
        bundle.putInt("BOOK_ID", bookid);
        bundle.putInt("LEVEL", level);
        bundle.putInt("UNIT_ID", unit_id);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}