package com.jn.iyuba.concept.simple.activity.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.OriginalAdapter;
import com.jn.iyuba.succinct.databinding.FragmentViewOriBinding;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.util.BookUtil;

import org.litepal.LitePal;

import java.util.List;

/**
 * 视频原文fragment
 */
public class VideoOriFragment extends Fragment {

    private int voaid;

    private FragmentViewOriBinding binding;

    private OriginalAdapter originalAdapter;

    public VideoOriFragment() {
    }

    public static VideoOriFragment newInstance(int voaid) {
        VideoOriFragment fragment = new VideoOriFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentViewOriBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //句子
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

        originalAdapter = new OriginalAdapter(R.layout.item_original, sentences);
        binding.videooriRvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.videooriRvList.setAdapter(originalAdapter);


    }


    /**
     * 播放到那一条
     */
    public void playProcess(long currentPosition) {

        for (int i = 0, size = originalAdapter.getData().size(); i < size; i++) {

            Sentence dataDTO = originalAdapter.getItem(i);
            long timing = (long) (Double.parseDouble(dataDTO.getTiming()) * 1000);
            long endTiming = (long) (Double.parseDouble(dataDTO.getEndTiming()) * 1000);
            if (currentPosition < endTiming && currentPosition > timing) {

                if (originalAdapter.getPosition() != i) {

                    originalAdapter.setPosition(i);
                    moveToPosition(binding.videooriRvList, i);
                    originalAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }


    /**
     * 同步播放
     *
     * @param rv
     * @param position
     */
    private void moveToPosition(RecyclerView rv, int position) {

        int firstItem = rv.getChildLayoutPosition(rv.getChildAt(0));
        int lastItem = rv.getChildLayoutPosition(rv.getChildAt(rv.getChildCount() - 1));
        if (position < firstItem || position > lastItem) {
            rv.smoothScrollToPosition(position);
        } else {
            int movePosition = position - firstItem;
            int top = rv.getChildAt(movePosition).getTop();
            rv.smoothScrollBy(0, top);
        }
    }



    /**
     * 顺序播放或随机播放更新数据
     */
    public void updateData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            voaid = bundle.getInt("VOA_ID", 0);
        }

        List<Sentence> sentenceList;
        if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

            sentenceList = LitePal.where("voaid = ? ", voaid + "").find(Sentence.class);
        } else {

            if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                sentenceList = LitePal.where("voaid = ? ", voaid + "").find(Sentence.class);
            } else {

                sentenceList = LitePal.where("voaid = ? ", (voaid * 10) + "").find(Sentence.class);
            }
        }

        if (originalAdapter != null) {

            originalAdapter.setPosition(0);
            originalAdapter.setNewData(sentenceList);
        }
    }
}