package com.jn.iyuba.concept.simple.activity.fragment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iyuba.imooclib.event.ImoocPlayEvent;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.adapter.PointReadingAdapter;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.model.bean.PointReadingBean;
import com.jn.iyuba.concept.simple.presenter.home.PointReadingPresenter;
import com.jn.iyuba.concept.simple.view.home.PointReadingContract;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.FragmentPointReadingBinding;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 青少版
 * 点读页面
 */
public class PointReadingFragment extends BaseFragment<PointReadingContract.PointReadingView, PointReadingContract.PointReadingPresenter>
        implements PointReadingContract.PointReadingView {

    private int voaid;
    private FragmentPointReadingBinding binding;

    private PointReadingAdapter pointReadingAdapter;

    private MediaPlayer mediaPlayer;

    private PointReadingBean.VoatextDTO startVoatextDTO;
    private PointReadingBean.VoatextDTO endVoatextDTO;


    /**
     * 检测handler
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (mediaPlayer != null && startVoatextDTO != null && endVoatextDTO != null) {

                if (mediaPlayer.isPlaying()) {

                    int curPos = mediaPlayer.getCurrentPosition();
                    if (curPos > (endVoatextDTO.getEndTiming() * 1000)) {

                        mediaPlayer.pause();
                    }

                    handler.sendEmptyMessageDelayed(1, 200);
                }
            }
            return false;
        }
    });


    public PointReadingFragment() {
        // Required empty public constructor
    }

    public static PointReadingFragment newInstance(int voaid) {
        PointReadingFragment fragment = new PointReadingFragment();
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

        initMediaPlayer();

        pointReadingAdapter = new PointReadingAdapter(R.layout.item_point_reading, new ArrayList<>());
        binding.prVp2.setAdapter(pointReadingAdapter);
        binding.prVp2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.prVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (pointReadingAdapter != null) {

                    binding.prTvPage.setText((position + 1) + "/" + pointReadingAdapter.getItemCount());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        pointReadingAdapter.setCallback(new PointReadingAdapter.Callback() {
            @Override
            public void play(PointReadingBean.VoatextDTO voatextDTO) {

                PointReadingFragment.this.startVoatextDTO = voatextDTO;
                PointReadingBean pointReadingBean = pointReadingAdapter.getPointReadingBean();
                List<PointReadingBean.VoatextDTO> voatextDTOList = pointReadingBean.getVoatext();
                int index = 0;
                for (int i = 0; i < voatextDTOList.size(); i++) {

                    PointReadingBean.VoatextDTO voa = voatextDTOList.get(i);
                    if (voa == voatextDTO) {
                        index = 0;
                        break;
                    }
                }
                index++;
                int endIndex = -1;
                for (; index < voatextDTOList.size(); index++) {

                    PointReadingBean.VoatextDTO voa = voatextDTOList.get(index);
                    if (voatextDTO.getImgWords().equals(voa.getImgWords())) {

                        endIndex = index;
                    }
                }
                //如果只有一条的话
                if (endIndex == -1) {
                    PointReadingFragment.this.endVoatextDTO = voatextDTO;
                } else {
                    PointReadingFragment.this.endVoatextDTO = voatextDTOList.get(endIndex);
                }


                mediaPlayer.seekTo((int) (voatextDTO.getTiming() * 1000));
                EventBus.getDefault().post(new ImoocPlayEvent());//停止后台播放
                mediaPlayer.start();
                handler.sendEmptyMessage(1);
            }
        });

        binding.prPbLoading.setVisibility(View.VISIBLE);
        binding.prLlError.setVisibility(View.GONE);
        binding.prLlContent.setVisibility(View.GONE);
        presenter.textExamApi("json", voaid + "");

        initOperation();
    }


    private void initMediaPlayer() {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
        }

        mediaPlayer.reset();
        List<Title> titleList = LitePal.where("voaid = ? and bookid = ?", voaid + "", Constant.book.getId()).find(Title.class);
        Title title = titleList.get(0);

        String url;
        //检测是否下载，优先使用本地文件
        File file = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC), title.getDownloadName());
        if (file.exists()) {//存在

            url = file.getAbsolutePath();
        } else {

            url = title.getSound().replace("voa", "voa/sentence").replace(title.getVoaId() + ".mp3", title.getVoaId() + "/" + title.getVoaId() + ".mp3");
        }
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void initOperation() {

        binding.prTvPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = binding.prVp2.getCurrentItem();
                index--;
                if (index < 0) {
                    index = 0;
                }
                binding.prVp2.setCurrentItem(index);
            }
        });
        binding.prTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = binding.prVp2.getCurrentItem();
                int count = pointReadingAdapter.getItemCount();
                index++;
                if (index > count) {

                    index = count - 1;
                }
                binding.prVp2.setCurrentItem(index);
            }
        });
        binding.prLlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                binding.prPbLoading.setVisibility(View.VISIBLE);
                binding.prLlError.setVisibility(View.GONE);
                binding.prLlContent.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        presenter.textExamApi("json", voaid + "");
                    }
                }, 1000);

            }
        });
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentPointReadingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected PointReadingContract.PointReadingPresenter initPresenter() {
        return new PointReadingPresenter();
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
    public void textExamApi(PointReadingBean pointReadingBean) {


        if (pointReadingBean == null) {

            binding.prPbLoading.setVisibility(View.GONE);
            binding.prLlError.setVisibility(View.VISIBLE);
            binding.prLlContent.setVisibility(View.GONE);
        } else {

            binding.prPbLoading.setVisibility(View.GONE);
            binding.prLlError.setVisibility(View.GONE);
            binding.prLlContent.setVisibility(View.VISIBLE);

            pointReadingAdapter.setPointReadingBean(pointReadingBean);

            String[] imgs = pointReadingBean.getImages().split(",");
            List<String> imgList = Arrays.asList(imgs);
            pointReadingAdapter.setNewData(imgList);

            binding.prTvPage.setText(1 + "/" + imgs.length);
        }
    }


    /**
     * 随机播放或者书序播放
     * 更新数据
     */
    public void updateData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            voaid = bundle.getInt("VOA_ID", 0);
        }
        binding.prPbLoading.setVisibility(View.VISIBLE);
        binding.prLlError.setVisibility(View.GONE);
        binding.prLlContent.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                presenter.textExamApi("json", voaid + "");
            }
        }, 1000);
    }
}