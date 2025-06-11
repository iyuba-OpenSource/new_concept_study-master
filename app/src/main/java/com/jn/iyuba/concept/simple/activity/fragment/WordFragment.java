package com.jn.iyuba.concept.simple.activity.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.activity.home.WordExerciseActivity;
import com.jn.iyuba.concept.simple.activity.home.WordListenActivity;
import com.jn.iyuba.concept.simple.activity.home.WordSpellExerciseActivity;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.break_through.CPWordAdapter;
import com.jn.iyuba.succinct.databinding.FragmentWordBinding;
import com.jn.iyuba.concept.simple.db.ConceptWord;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

/**
 * 课文主页面-单词页面
 * WordExerciseActivity
 */
public class WordFragment extends Fragment {

    private FragmentWordBinding binding;

    private int voaid;

    private CPWordAdapter cpWordAdapter;

    private LineItemDecoration lineItemDecoration;


    /**
     * 1单词音频
     * 2 句子音频
     */
    private int flag = 1;

    private MediaPlayer mediaPlayer;

    public WordFragment() {
    }

    public void updateData() {

        if (getArguments() != null) {

            voaid = getArguments().getInt("VOAID");
        }
        List<ConceptWord> conceptWordList = LitePal.where("voaid = ?", voaid + "").find(ConceptWord.class);
        cpWordAdapter.setNewData(conceptWordList);
    }

    public static WordFragment newInstance(int voaid) {
        WordFragment fragment = new WordFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<ConceptWord> conceptWordList = LitePal.where("voaid = ?", voaid + "").find(ConceptWord.class);

        lineItemDecoration = new LineItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_line_word));
        binding.wordRv.addItemDecoration(lineItemDecoration);

        binding.wordRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        cpWordAdapter = new CPWordAdapter(R.layout.item_cp_word, conceptWordList);
        binding.wordRv.setAdapter(cpWordAdapter);
        cpWordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                ConceptWord conceptWord = cpWordAdapter.getItem(position);

                if (view.getId() == R.id.word_iv_sentence) {

                    flag = 2;
                    initMediaPlayer(conceptWord.getSentenceAudio(), conceptWord);
                } else {

                    flag = 1;
                    initMediaPlayer(conceptWord.getAudio(), conceptWord);
                }

            }
        });

        binding.wordSrlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                List<ConceptWord> conceptWordList = LitePal.where("voaid = ?", voaid + "").find(ConceptWord.class);
                cpWordAdapter.setNewData(conceptWordList);
                new Handler().postDelayed(() -> binding.wordSrlRefresh.setRefreshing(false), 800);
            }
        });
        //英汉训练
        binding.wordLlEn2cn.setOnClickListener(v -> {

            WordExerciseActivity.startActivity(requireActivity(), cpWordAdapter.getData(), 1);
        });
        //汉英训练
        binding.wordLlCn2en.setOnClickListener(v -> {

            WordExerciseActivity.startActivity(requireActivity(), cpWordAdapter.getData(), 0);
        });
        //单词拼写
        binding.wordLlSpell.setOnClickListener(v -> {

            WordSpellExerciseActivity.startActivity(requireActivity(), cpWordAdapter.getData(), 0);
        });
        //单词听写
        binding.wordLlListen.setOnClickListener(v -> {

            WordListenActivity.startActivity(requireActivity(), cpWordAdapter.getData(), 0);
        });
    }


    private void initMediaPlayer(String url, ConceptWord conceptWord) {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                if (getFlag() == 1) {
                    mediaPlayer.start();
                } else {

                    mediaPlayer.seekTo((int) (Double.parseDouble(conceptWord.getTiming()) * 1000));
                    mediaPlayer.start();
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = conceptWord;
                    handler.sendMessage(message);
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
}