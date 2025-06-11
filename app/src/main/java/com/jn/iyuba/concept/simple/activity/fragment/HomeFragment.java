package com.jn.iyuba.concept.simple.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huawei.hms.ads.AdCloseBtnClickListener;
import com.huawei.hms.ads.AdFeedbackListener;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AdvertiserInfo;
import com.huawei.hms.ads.AppInfo;
import com.huawei.hms.ads.Image;
import com.huawei.hms.ads.Video;
import com.huawei.hms.ads.VideoConfiguration;
import com.huawei.hms.ads.VideoOperator;
import com.huawei.hms.ads.nativead.DislikeAdListener;
import com.huawei.hms.ads.nativead.DislikeAdReason;
import com.huawei.hms.ads.nativead.MediaContent;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.reward.RewardVerifyConfig;
import com.huawei.openalliance.ad.beans.metadata.PromoteInfo;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MainActivity;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.adapter.AdAdapter;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.activity.ChooseBookActivity;
import com.jn.iyuba.concept.simple.activity.ContentActivity;
import com.jn.iyuba.concept.simple.adapter.BannerAdapter;
import com.jn.iyuba.concept.simple.adapter.HomeAdapter;
import com.jn.iyuba.concept.simple.adapter.IndicatorAdapter;
import com.jn.iyuba.succinct.databinding.FragmentHomeBinding;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.ChapterEventbus;
import com.jn.iyuba.concept.simple.entity.DownloadRefresh;
import com.jn.iyuba.concept.simple.entity.Refresh;
import com.jn.iyuba.concept.simple.entity.ShowPage;
import com.jn.iyuba.concept.simple.model.bean.TitleBean;
import com.jn.iyuba.concept.simple.presenter.home.HomePresenter;
import com.jn.iyuba.concept.simple.service.MediaService;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.DataUtil;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.util.popup.BookPopup;
import com.jn.iyuba.concept.simple.view.home.HomeContract;
import com.jn.iyuba.novel.db.Chapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 课文列表
 */
public class HomeFragment extends BaseFragment<HomeContract.HomeView, HomeContract.HomePresenter>
        implements HomeContract.HomeView {


    private FragmentHomeBinding binding;

    private HomeAdapter homeAdapter;

    private boolean showBack = true;

    private LineItemDecoration lineItemDecoration;

    private BookPopup bookPopup;

    /**
     * 书的id
     */
    private int book = 1;

    private BannerAdapter bannerAdapter;

    private IndicatorAdapter indicatorAdapter;

    private BroadcastReceiver broadcastReceiver;

    private ServiceConnection serviceConnection;

    private MediaService.MusicController musicController;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(boolean showBack) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("SHOW_BACK", showBack);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle bundle = getArguments();
        if (bundle != null) {

            showBack = bundle.getBoolean("SHOW_BACK");
        }
        initBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (serviceConnection != null) {

            MyApplication.getContext().unbindService(serviceConnection);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initOperation(view.getContext());

        if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {//青少

            List<Title> titleList = LitePal.where("bookid = ?", Constant.book.getId() + "").find(Title.class);
            if (titleList.size() == 0) {

                int uid = Constant.userinfo == null ? 0 : Constant.userinfo.getUid();
                String sign = MD5Util.MD5("iyuba" + DateUtil.getDays() + "series");
                presenter.getTitleBySeriesid("title", Constant.APPID, uid, sign, Integer.parseInt(Constant.book.getId()));
            } else {

                showData(titleList);
            }
        } else {//新概念英语

            List<Title> titleList = LitePal
                    .where("bookid = ? and language = ?", Constant.book.getId() + "", Constant.LANGUAGE)
                    .find(Title.class);
            if (titleList.size() == 0) {

                presenter.getConceptTitle(Constant.LANGUAGE, Integer.parseInt(Constant.book.getId()), 1);
            } else {

                showData(titleList);
            }

        }
    }

    private void bindServer() {

        Intent intent = new Intent(MyApplication.getContext(), MediaService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                musicController = (MediaService.MusicController) service;
                Title title = musicController.getCurData();
                if (title == null) {

                    return;
                }
                //新概念
                String titleStr = null;
                if (BookUtil.isYouthBook(title.getBookid())) {

                    titleStr = title.getTitle();
                } else {
                    String lesson = title.getVoaId().substring(title.getVoaId().length() - 3);
                    titleStr = "LESSON " + Integer.parseInt(lesson) + " " + title.getTitle();
                }

                binding.homeTvTitle.setText(titleStr);

                binding.homeLlSound.setVisibility(View.VISIBLE);

                if (musicController.isPlaying()) {//判断是否正在播放来设置按钮的图标

                    binding.homeIvSound.setImageResource(R.mipmap.icon_ori_pause);
                } else {

                    binding.homeIvSound.setImageResource(R.mipmap.icon_ori_play);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        MyApplication.getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initBroadcast() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals(MediaService.FLAG_MUSIC_SWITCH_DATA)) {//切换数据

                    if (musicController != null) {//防止空指针

//                        HomeListItem homeListItem = musicController.getCurData();
//                        home_tv_title.setText(homeListItem.getTitleCn());
                    }

                } else if (action.equals(MediaService.FLAG_MUSIC_PLAY)) {//通知点击时间触发

                    if (musicController == null) {
                        bindServer();
                    } else {//切换课文音频时触发，用来随机播放、顺序播放切换后的更新ui

                        Title title = musicController.getCurData();
                        if (title == null) {
                            return;
                        }
                        String titleStr = null;
                        if (BookUtil.isYouthBook(title.getBookid())) {

                            titleStr = title.getTitle();
                        } else {
                            String lesson = title.getVoaId().substring(title.getVoaId().length() - 3);
                            titleStr = "LESSON " + Integer.parseInt(lesson) + " " + title.getTitle();
                        }
                        binding.homeTvTitle.setText(titleStr);
                        if (musicController.isPlaying()) {//判断是否正在播放来设置按钮的图标

                            binding.homeIvSound.setImageResource(R.mipmap.icon_ori_pause);
                        } else {

                            binding.homeIvSound.setImageResource(R.mipmap.icon_ori_play);
                        }

                        int mode = musicController.getMode();
                        if (mode == 1) {

                            binding.homeIvPlaytype.setImageResource(R.mipmap.icon_home_random_g);
                        } else if (mode == 2) {

                            binding.homeIvPlaytype.setImageResource(R.mipmap.icon_home_list_g);
                        } else {

                            binding.homeIvPlaytype.setImageResource(R.mipmap.icon_home_single_g);
                        }
                    }
                }
            }
        };
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(MyApplication.getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_MUSIC_SWITCH_DATA);
        intentFilter.addAction(MediaService.FLAG_MUSIC_PLAY);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 剑桥书虫
     * 获取Chapter来更新播放器
     *
     * @param chapterEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChapterEventbus chapterEventbus) {

        Chapter chapter = chapterEventbus.getChapter();
        int flag = chapterEventbus.getFlag();

        if (flag == 0) {//更新

            binding.homeTvTitle.setText(chapter.getCname_cn());

            binding.homeLlSound.setVisibility(View.VISIBLE);

            if (musicController != null && musicController.isPlaying()) {//判断是否正在播放来设置按钮的图标

                binding.homeIvSound.setImageResource(R.mipmap.icon_ori_pause);
            } else {

                binding.homeIvSound.setImageResource(R.mipmap.icon_ori_play);
            }
        } else {

            if (musicController == null) {

                return;
            }
            ContentActivity.startActivity(requireActivity(), musicController.getVoaid(), chapter.getCname_cn()
                    , 0, musicController.getChapterOrder(), musicController.isBookWorm());
        }
    }


    /**
     * 下载进度
     *
     * @param downloadRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadRefresh downloadRefresh) {

        if (homeAdapter == null) {

            return;
        }

        int id = downloadRefresh.getVoaid();
        int bookid = downloadRefresh.getBookid();
        List<Title> titleList = homeAdapter.getData();
        int index = -1;
        for (int i = 0; i < titleList.size(); i++) {

            Title title = titleList.get(i);
            if (title.getVoaId().equals(id + "") && title.getBookid() == bookid) {

                index = i;
                break;
            }
        }
        homeAdapter.notifyItemChanged(index);
    }

    /**
     * 刷新数据
     *
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Refresh refresh) {


        if (homeAdapter == null) {

            return;
        }
        int id = refresh.getVoaid();
        List<Title> titleList = homeAdapter.getData();
        int index = -1;
        for (int i = 0; i < titleList.size(); i++) {

            Title title = titleList.get(i);
            if (title.getVoaId().equals(id + "")) {

                index = i;
                break;
            }
        }
        if (index != -1) {

            Title iTitle = titleList.get(index);
            List<Title> list;
            if (BookUtil.isYouthBook(iTitle.getBookid())) {

                list = LitePal.where("voaid = ?", iTitle.getVoaId()).find(Title.class);
            } else {

                list = LitePal.where("voaid = ? and language = ?", iTitle.getVoaId(), iTitle.getLanguage()).find(Title.class);
            }
            if (list.size() > 0) {

                titleList.set(index, list.get(0));
                homeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

/*        if (homeAdapter != null) {

            homeAdapter.getData();

            homeAdapter.notifyDataSetChanged();
        }*/
    }

    private void requestAd() {


        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(MyApplication.getContext(), "i9izala7w9");
        builder.setNativeAdLoadedListener(new NativeAd.NativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                // 广告加载成功后调用

                new Handler(Looper.getMainLooper()).post(() -> {

                    List<NativeAd> nativeAdList = new ArrayList<>();
                    nativeAdList.add(nativeAd);
                    AdAdapter adAdapter = new AdAdapter(homeAdapter, nativeAdList);
                    binding.homeRv.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
                    binding.homeRv.setAdapter(adAdapter);
                });

                Log.d("HiAdSDK——————", nativeAd.getTitle() + "");
            }
        }).setAdListener(new AdListener() {
            @Override
            public void onAdFailed(int errorCode) {
                // 广告加载失败时调用
                Log.d("HiAdSDK——————", errorCode + "");
            }
        });
        NativeAdLoader nativeAdLoader = builder.build();
        nativeAdLoader.loadAd(new AdParam.Builder().build());
    }

    /**
     * 显示数据
     *
     * @param titleList
     */
    private void showData(List<Title> titleList) {

        homeAdapter.setData(titleList);
        homeAdapter.notifyDataSetChanged();

       /* if (Constant.userinfo == null || !Constant.userinfo.isVip()) {

            requestAd();
        }*/


        //banner
        Random random = new Random();
        List<Title> bannerList = new ArrayList<>();

        if (titleList.size() <= 3) {

            bannerList.addAll(titleList);
        } else {

            for (int i = 0; i < 3; i++) {

                int index = random.nextInt(titleList.size());
                bannerList.add(titleList.get(index));
            }
        }
        bannerAdapter.setTitleList(bannerList);
        bannerAdapter.notifyDataSetChanged();

        indicatorAdapter.setNewData(bannerList);
    }


    private void initView() {

        binding.toolbar.toolbarIvTitle.setText(Constant.book.getDescCn());
        binding.toolbar.toolbarIvRight.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarIvBack.setVisibility(View.INVISIBLE);
        binding.toolbar.toolbarIvRight.setImageResource(R.mipmap.ic_book);
        if (showBack) {

            binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    requireActivity().finish();
                }
            });
        } else {

            binding.toolbar.toolbarIvBack.setVisibility(View.INVISIBLE);
        }
    }

    private void initOperation(Context context) {

        indicatorAdapter = new IndicatorAdapter(R.layout.item_indicator, new ArrayList<>());
        binding.homeRcIndicator.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false));
        binding.homeRcIndicator.setAdapter(indicatorAdapter);
        //banner
        bannerAdapter = new BannerAdapter(new ArrayList<>());
        binding.homeVp2.setAdapter(bannerAdapter);
        binding.homeVp2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        bannerAdapter.setCallback(new BannerAdapter.Callback() {
            @Override
            public void getTitle(int position, Title title) {

                ContentActivity.startActivity(requireActivity(), Integer.parseInt(title.getVoaId()), title.getTitle(), 0, position);
            }
        });
        binding.homeVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (indicatorAdapter != null) {

                    indicatorAdapter.setPosition(position);
                    indicatorAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        binding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(requireActivity(), ChooseBookActivity.class));
            }
        });
     /*   binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.openDrawer();
            }
        });*/

        homeAdapter = new HomeAdapter(R.layout.item_home, new ArrayList<>());
        binding.homeRv.setLayoutManager(new LinearLayoutManager(context));
        binding.homeRv.setAdapter(homeAdapter);
        binding.homeRv.setItemAnimator(null);

        homeAdapter.setCallback(new HomeAdapter.Callback() {
            @Override
            public void itemChildClick(View view, Title title, int position) {

                if (view.getId() == R.id.home_ll_listen) {

                    ContentActivity.startActivity(requireActivity(), Integer.parseInt(title.getVoaId()), title.getTitle(), 0, position);
                } else if (view.getId() == R.id.home_ll_eval) {

                    ContentActivity.startActivity(requireActivity(), Integer.parseInt(title.getVoaId()), title.getTitle(), 1, position);
                } else if (view.getId() == R.id.home_ll_word) {

                    EventBus.getDefault().post(new ShowPage("闯关"));
                } else if (view.getId() == R.id.home_ll_exercise) {

                    ContentActivity.startActivity(requireActivity(), Integer.parseInt(title.getVoaId()), title.getTitle(), 2, position);
                } else if (view.getId() == R.id.home_ll_imooc) {

                    Intent intent = com.iyuba.imooclib.ui.content.ContentActivity.buildIntent(requireActivity(), title.getCategoryid(), "class.jichu");
                    startActivity(intent);
                } else if (view.getId() == R.id.home_iv_download) {

                    title.download();
                }
            }

            @Override
            public void itemClick(Title title, int position) {

                int id;
                if (title.getVoaId() == null) {

                    id = title.getId();
                } else {
                    id = Integer.parseInt(title.getVoaId());
                }
                ContentActivity.startActivity(requireActivity(), id, title.getTitle(), 0, position);
            }
        });

      /*  homeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                Title title = homeAdapter.getItem(position);
                if (view.getId() == R.id.home_ll_listen) {

                    ContentActivity.startActivity(requireActivity(), Integer.parseInt(title.getVoaId()), title.getTitle(), 0, position);
                } else if (view.getId() == R.id.home_ll_eval) {

                    ContentActivity.startActivity(requireActivity(), Integer.parseInt(title.getVoaId()), title.getTitle(), 1, position);
                } else if (view.getId() == R.id.home_ll_word) {

                    EventBus.getDefault().post(new ShowPage("闯关"));
                } else if (view.getId() == R.id.home_ll_exercise) {

                    ContentActivity.startActivity(requireActivity(), Integer.parseInt(title.getVoaId()), title.getTitle(), 2, position);
                } else if (view.getId() == R.id.home_ll_imooc) {

                    Intent intent = com.iyuba.imooclib.ui.content.ContentActivity.buildIntent(requireActivity(), title.getCategoryid(), "class.jichu");
                    startActivity(intent);
                } else if (view.getId() == R.id.home_iv_download) {

                    title.download();
                }
            }
        });*/

/*        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Title title = homeAdapter.getItem(position);

                int id;
                if (title.getVoaId() == null) {

                    id = title.getId();
                } else {
                    id = Integer.parseInt(title.getVoaId());
                }
                ContentActivity.startActivity(requireActivity(), id, title.getTitle(), 0, position);
            }
        });*/

        lineItemDecoration = new LineItemDecoration(context, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.rctg_home_space_10dp, requireActivity().getTheme()));

        binding.homeRv.addItemDecoration(lineItemDecoration);

        //音频
        binding.homeLlSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Title title = musicController.getCurData();
                if (title != null) {

                    ContentActivity.startActivity(requireActivity(), musicController.getVoaid(), title.getTitle()
                            , 0, musicController.getChapterOrder(), musicController.isBookWorm());

                }
            }
        });
        binding.homeIvSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }
                if (musicController.isPlaying()) {

                    binding.homeIvSound.setImageResource(R.mipmap.icon_ori_play);
                    musicController.pause();
                } else {


                    binding.homeIvSound.setImageResource(R.mipmap.icon_ori_pause);
                    musicController.start();
                }
            }
        });

        binding.homeIvPlaytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }
                int mode = musicController.getMode();
                if (mode >= 3) {

                    mode = 1;
                } else {
                    mode++;
                }
                musicController.setPlayMode(mode);
                if (mode == 1) {

                    binding.homeIvPlaytype.setImageResource(R.mipmap.icon_home_random_g);
                    toast("列表随机播放");
                } else if (mode == 2) {

                    binding.homeIvPlaytype.setImageResource(R.mipmap.icon_home_list_g);
                    toast("列表循环播放");
                } else {

                    binding.homeIvPlaytype.setImageResource(R.mipmap.icon_home_single_g);
                    toast("单曲循环播放");
                }
            }
        });
    }


    /**
     * 请求
     *
     * @param language
     * @param book
     * @param flg
     * @param bookName
     */
    public void update(String language, int book, int flg, String bookName) {

        if (presenter == null) {

            return;
        }

        //隐藏列表，请求到数据再显示
        binding.homeLlContent.setVisibility(View.GONE);


        if (BookUtil.isYouthBook(book)) {

            int uid = Constant.userinfo == null ? 0 : Constant.userinfo.getUid();
            String sign = MD5Util.MD5("iyuba" + DateUtil.getDays() + "series");
            presenter.getTitleBySeriesid("title", Constant.APPID, uid, sign, Integer.parseInt(Constant.book.getId()));
        } else {
            presenter.getConceptTitle(language, book, 1);
        }

        binding.toolbar.toolbarIvTitle.setText(bookName);
    }


/*    private void initBookPopup() {

        if (bookPopup == null) {

            bookPopup = new BookPopup(requireActivity());
            bookPopup.setCallback(new BookPopup.Callback() {
                @Override
                public void getBook(Book book) {


                    int id = book.getId();
                    if (id == 5) {

                        startActivity(new Intent(requireActivity(), AboutActivity.class));
                    } else if (id == 6) {

                        try {
                            String url = "mqqwpa://im/chat?chat_type=wpa&uin=572828703";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        } catch (Exception e) {
                            Toast.makeText(MyApplication.getContext(), "请先安装QQ！", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Constant.bookid = id;
                        presenter.getConceptTitle("US", book.getId(), 1);
                        binding.toolbar.toolbarIvTitle.setText(book.getName());
                    }
                }
            });
        }
        bookPopup.showPopupWindow(binding.toolbar.toolbarIvRight);
    }*/

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected HomeContract.HomePresenter initPresenter() {
        return new HomePresenter();
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
    public void getConceptTitle(TitleBean titleBean, int bookid, String language) {

//        binding.homePbLoading.setVisibility(View.GONE);
        if (binding.homeLlContent.getVisibility() == View.GONE) {

            binding.homeLlContent.setVisibility(View.VISIBLE);
        }

        List<Title> titleList = titleBean.getData();
        //存储数据
        for (int i = 0; i < titleList.size(); i++) {

            Title title = titleList.get(i);
            title.setBookid(bookid);

            if (BookUtil.isYouthBook(bookid)) {

                title.setVoaId(title.getId() + "");//设置id为voaid
                int count = LitePal.where("voaId = ?", title.getId() + "")
                        .count(Title.class);
                if (count > 0) {

                    title.updateAll("voaId = ?", title.getId() + "");
                } else {

                    title.save();
                }
            } else {

                int count = LitePal.where("bookid = ? and voaId = ? and language = ?", bookid + "", title.getVoaId(), language)
                        .count(Title.class);

                title.setLanguage(language);//存储英音或者美音
                if (count > 0) {

                    title.updateAll("bookid = ? and voaId = ?  and language = ?", bookid + "", title.getVoaId(), language);
                } else {

                    title.save();
                }
            }
        }
        showData(titleList);
    }
}