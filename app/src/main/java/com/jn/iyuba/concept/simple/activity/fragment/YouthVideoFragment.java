package com.jn.iyuba.concept.simple.activity.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.material.tabs.TabLayout;
import com.iyuba.imooclib.event.ImoocPlayEvent;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.home.DubbingActivity;
import com.jn.iyuba.concept.simple.adapter.DubbingFragmentAdapter;
import com.jn.iyuba.concept.simple.adapter.OriginalAdapter;
import com.jn.iyuba.succinct.databinding.FragmentYouthVideoBinding;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.MediaPauseEventbus;
import com.jn.iyuba.concept.simple.util.BookUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 青少版
 * 视频
 */
public class YouthVideoFragment extends Fragment {


    private int voaid;

    private FragmentYouthVideoBinding binding;

    private ExoPlayer player;

    private String[] nav = {"原文", "排行"};

    private DubbingFragmentAdapter dubbingFragmentAdapter;

    private VideoOriFragment videoOriFragment;

    private VideoRankFragment videoRankFragment;

    /**
     * 更新数据
     */
    public void updateData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            voaid = bundle.getInt("VOA_ID", 0);
        }


        if (player != null) {

            List<Title> titleList = LitePal.where("voaid = ?", voaid + "").find(Title.class);
            if (titleList.size() == 0) {

                return;
            }
            String url;
            if (Constant.userinfo != null && Constant.userinfo.isVip()) {

                url = "http://staticvip.iyuba.cn/" + titleList.get(0).getVideo();
            } else {
                url = "http://static0.iyuba.cn/" + titleList.get(0).getVideo();
            }

            MediaItem mediaItem = MediaItem.fromUri(url);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }


        if (videoOriFragment == null || videoRankFragment == null) {

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            List<Fragment> fragmentList = fragmentManager.getFragments();
            for (Fragment fragment : fragmentList) {

                if (fragment instanceof VideoOriFragment) {

                    videoOriFragment = (VideoOriFragment) fragment;
                } else if (fragment instanceof VideoRankFragment) {

                    videoRankFragment = (VideoRankFragment) fragment;
                }
            }
        }
        if (videoOriFragment != null) {

            Bundle bundle1 = videoOriFragment.getArguments();
            if (bundle1 != null) {

                bundle1.putInt("VOAID", voaid);
            }
            videoOriFragment.updateData();
        }
        if (videoRankFragment != null) {

            Bundle bundle1 = videoRankFragment.getArguments();
            if (bundle1 != null) {
                bundle1.putInt("VOAID", voaid);
            }
            videoRankFragment.updateData();
        }
    }

    public YouthVideoFragment() {
        // Required empty public constructor
    }

    public static YouthVideoFragment newInstance(int voaid) {
        YouthVideoFragment fragment = new YouthVideoFragment();
        Bundle args = new Bundle();
        args.putInt("VOA_ID", voaid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voaid = getArguments().getInt("VOA_ID", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentYouthVideoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Title> titleList = LitePal.where("voaid = ?", voaid + "").find(Title.class);

        if (titleList.size() == 0) {

            return;
        }

        //视频
        String url;
        if (Constant.userinfo != null && Constant.userinfo.isVip()) {

            url = "http://staticvip.iyuba.cn/" + titleList.get(0).getVideo();
        } else {
            url = "http://static0.iyuba.cn/" + titleList.get(0).getVideo();
        }

        MediaItem mediaItem = MediaItem.fromUri(url);

        player = new ExoPlayer.Builder(requireContext()).build();
        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onIsPlayingChanged(EventTime eventTime, boolean isPlaying) {

                if (isPlaying) {

                    EventBus.getDefault().post(new ImoocPlayEvent());
                    handler.sendEmptyMessage(1);
                }
            }
        });
        binding.yvSpcvExoplay.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();
//        player.play();

        for (int i = 0; i < nav.length; i++) {

            TabLayout.Tab tab = binding.yvTlTab.newTab();
            tab.setText(nav[i]);
            binding.yvTlTab.addTab(tab);
        }
        binding.yvTlTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                binding.yvVp2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dubbingFragmentAdapter = new DubbingFragmentAdapter(this, nav, voaid);
        binding.yvVp2.setAdapter(dubbingFragmentAdapter);

        binding.yvVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.yvTlTab.selectTab(binding.yvTlTab.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        //开始配音
        binding.yvTvDubbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Title> titles = LitePal.where("voaid = ?", voaid + "").find(Title.class);
                if (titles.size() > 0) {

                    Title title = titles.get(0);
                    DubbingActivity.startActivity(requireActivity(), title.getVoaId() +"",
                            title.getCategory(), title.getSound(), title.getPic(), title.getTitle(),
                            title.getVideo());
                }
            }
        });
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (player != null && player.isPlaying()) {

                if (videoOriFragment == null) {

                    FragmentManager fragmentManager = getChildFragmentManager();
                    List<Fragment> fragmentList = fragmentManager.getFragments();
                    for (Fragment fragment : fragmentList) {

                        if (fragment instanceof VideoOriFragment) {

                            videoOriFragment = (VideoOriFragment) fragment;
                        }
                    }
                }

                if (videoOriFragment != null) {

                    long curPostion = player.getCurrentPosition();
                    videoOriFragment.playProcess(curPostion);
                }
                handler.sendEmptyMessageDelayed(1, 200);
            }
            return false;
        }
    });


    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }


    @Override
    public void onStop() {
        super.onStop();

        if (player != null) {

            player.stop();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (player != null) {

            player.release();
        }
    }
}