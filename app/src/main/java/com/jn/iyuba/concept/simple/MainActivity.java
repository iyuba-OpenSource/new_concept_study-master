package com.jn.iyuba.concept.simple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivityNew;
import com.iyuba.headlinelibrary.ui.content.TextContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivityNew;
import com.iyuba.headlinelibrary.ui.video.VideoMiniContentActivity;
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;
import com.iyuba.module.favor.data.model.BasicFavorPart;
import com.jn.iyuba.concept.simple.activity.AboutActivity;
import com.jn.iyuba.concept.simple.activity.AlertActivity;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.activity.ChooseBookActivity;
import com.jn.iyuba.concept.simple.activity.ContentActivity;
import com.jn.iyuba.concept.simple.activity.MyWebActivity;
import com.jn.iyuba.concept.simple.activity.fragment.BreakThroughFragment;
import com.jn.iyuba.concept.simple.activity.fragment.HomeFragment;
import com.jn.iyuba.concept.simple.activity.fragment.MeFragment;
import com.jn.iyuba.concept.simple.activity.login.WxLoginActivity;
import com.jn.iyuba.concept.simple.activity.vip.VipActivity;
import com.jn.iyuba.concept.simple.adapter.MenuAdapter;
import com.jn.iyuba.concept.simple.entity.RewardEventbus;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.util.NumberUtil;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.ActivityMainBinding;
import com.jn.iyuba.concept.simple.db.Book;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.ShowPage;
import com.jn.iyuba.concept.simple.entity.SideNav;
import com.jn.iyuba.concept.simple.model.bean.ProcessBean;
import com.jn.iyuba.concept.simple.presenter.MainPresenter;
import com.jn.iyuba.concept.simple.util.DataUtil;
import com.jn.iyuba.concept.simple.util.LoginUtil;
import com.jn.iyuba.concept.simple.util.popup.LoadingPopup;
import com.jn.iyuba.concept.simple.view.MainContract;
import com.jn.iyuba.concept.simple.work.ExerciseDataWork;
import com.jn.iyuba.concept.simple.work.SentenceDataWork;
import com.jn.iyuba.concept.simple.work.TitleDataWork;
import com.jn.iyuba.concept.simple.work.WordDataWork;
import com.jn.iyuba.novel.ChapterFragment;
import com.jn.iyuba.novel.db.Chapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import personal.iyuba.personalhomelibrary.event.ArtDataSkipEvent;

public class MainActivity extends BaseActivity<MainContract.MainView, MainContract.MainPresenter>
        implements MainContract.MainView {

    private ActivityMainBinding binding;

    private HomeFragment homeFragment;

    private BreakThroughFragment breakThroughFragment;

    private MobClassFragment mobClassFragment;

    private MeFragment meFragment;

    /**
     * 小说馆
     */
    private ChapterFragment chapterFragment;

    private String[] nav;

    private final int[] navImg = {R.mipmap.nav_textbook, R.mipmap.nav_bt, R.mipmap.nav_imooc, R.mipmap.nav_bookworm_gray, R.mipmap.nav_me};

    private final int[] cNavImg = {R.mipmap.nav_textbook_c, R.mipmap.nav_bt_c, R.mipmap.nav_imooc_c, R.mipmap.nav_bookworm_green, R.mipmap.nav_me_c};


    private final int[] navImgF = {R.mipmap.nav_textbook, R.mipmap.nav_bt, R.mipmap.nav_imooc, R.mipmap.nav_me};

    private final int[] cNavImgF = {R.mipmap.nav_textbook_c, R.mipmap.nav_bt_c, R.mipmap.nav_imooc_c, R.mipmap.nav_me_c};
    private LoadingPopup loadingPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EventBus.getDefault().register(this);

        int count = LitePal.count(Title.class);
        if (count == 0) {//初始化数据

            OneTimeWorkRequest titleDataWorkRequest =
                    new OneTimeWorkRequest.Builder(TitleDataWork.class)
                            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                            .build();
            OneTimeWorkRequest SentenceDataWorkRequest =
                    new OneTimeWorkRequest.Builder(SentenceDataWork.class)
                            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                            .build();
            OneTimeWorkRequest exerciseDataRequest =
                    new OneTimeWorkRequest.Builder(ExerciseDataWork.class)
                            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                            .build();
            OneTimeWorkRequest wordDataWorkRequest =
                    new OneTimeWorkRequest.Builder(WordDataWork.class)
                            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                            .build();
            WorkManager
                    .getInstance(getApplicationContext())
                    .beginWith(titleDataWorkRequest)
                    .then(SentenceDataWorkRequest)
                    .then(exerciseDataRequest)
                    .then(wordDataWorkRequest)
                    .enqueue();
        }


        String vName = null;
        try {
            vName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        presenter.getRegisterAll(Constant.APPID + "", vName);
    }

    @Override
    public View initLayout() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public MainContract.MainPresenter initPresenter() {
        return new MainPresenter();
    }

    /**
     * 显示loading
     *
     * @param content
     */
    private void initLoadingPopup(String content) {

        if (loadingPopup == null) {

            loadingPopup = new LoadingPopup(MainActivity.this);
        }
        loadingPopup.setContent(content);
        loadingPopup.showPopupWindow();
    }


    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

        if (loadingPopup != null) {

            loadingPopup.dismiss();
        }
    }


    @Override
    public void toast(String msg) {

    }


    /**
     * 奖励弹窗
     *
     * @param rewardEventbus
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RewardEventbus rewardEventbus) {

        AlertActivity.startActivity(MainActivity.this, rewardEventbus.getReward());
    }

    /**
     * 签到弹窗
     *
     * @param scoreBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ScoreBean scoreBean) {

        String money = scoreBean.getMoney();//本次打卡获得的钱数 ，单位 分
        String totalCredit = scoreBean.getTotalcredit();//总积分
        String days = scoreBean.getDays();
        String addCredit = scoreBean.getAddcredit();


        String content;
        float moneyThisTime = Float.parseFloat(money);
        if (moneyThisTime > 0) {

            float moneyTotal = Float.parseFloat(totalCredit);
            content = "打卡成功,\n" + "您已连续打卡" + days + "天,  获得" + NumberUtil.getFormatDouble(moneyThisTime * 0.01) + "元,\n获得"
                    + addCredit + "积分，\n总计: " + NumberUtil.getFormatDouble(moneyTotal * 0.01) + "元,\n" + "满10元可在\"爱语吧\"公众号提现";
        } else {

            content = "打卡成功，连续打卡" + days + "天,获得" + addCredit + "积分，总积分: " + totalCredit;
        }

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("打卡提示")
                .setMessage(content)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 书虫和剑桥小说馆
     *
     * @param chapter
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Chapter chapter) {

        ContentActivity.startActivity(MainActivity.this, Integer.parseInt(chapter.getVoaid()), chapter.getCname_en()
                , 0, Integer.parseInt(chapter.getChapter_order()), true);
    }

    /**
     * 显示
     *
     * @param showPage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShowPage showPage) {

        if (showPage.getTag().equals("闯关")) {

            binding.mainTlNav.getTabAt(1).select();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Book book) {

        if (homeFragment != null) {

            homeFragment.update(Constant.LANGUAGE, Integer.parseInt(book.getId()), 1, book.getDescCn());
        }
    }

    /**
     * 微课点击购买会员后触发
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyVIPEvent event) {

        VipActivity.startActivity(MainActivity.this, 2);
    }


    /**
     * 从口语圈点击后进入的页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArtDataSkipEvent event) {
        if (event.voa != null) {
            BasicFavorPart part = new BasicFavorPart();
            part.setType(event.voa.categoryString);
            part.setCategoryName(event.voa.categoryString);
            part.setTitle(event.voa.title);
            part.setTitleCn(event.voa.title_cn);
            part.setPic(event.voa.pic);
            part.setId(event.voa.voaid + "");
            part.setSound(event.voa.sound + "");
            jumpToCorrectFavorActivityByCate(this, part);
        }
    }


    public void jumpToCorrectFavorActivityByCate(Context context, BasicFavorPart item) {
        switch (item.getType()) {
            case "news":
                Intent intent1 = TextContentActivity.buildIntent(context, item.getId(), item.getTitle(), item.getTitleCn(), item.getType(),
                        item.getCategoryName(), item.getCreateTime(), item.getPic(), item.getSource());
                startActivity(intent1);
                break;
            case "headnews":

                Intent intent2 = TextContentActivity.buildIntent(context, item.getId(), item.getTitle(), item.getTitleCn(), "news",
                        item.getCategoryName(), item.getCreateTime(), item.getPic(), item.getSource());
                startActivity(intent2);
                break;
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                Intent intent3 = AudioContentActivityNew.buildIntent(context, item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(), item.getPic(), item.getType(), item.getId(), item.getSound());
                startActivity(intent3);
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "japanvideos":
            case "bbcwordvideo":
            case "topvideos":

                Intent intent = VideoContentActivityNew.buildIntent(context, item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(), item.getPic(), item.getType(), item.getId(), item.getSound());
                startActivity(intent);
                break;
            case "series":
//                startActivity(new Intent(context, OneMvSerisesView.class)
//                        .putExtra("serisesid", item.getSeriseId())
//                        .putExtra("voaid", item.getId()));
                break;
            case "smallvideo":
                startActivity(VideoMiniContentActivity.buildIntentForOne(this, item.getId(), 0, 1, 1));
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }


    /**
     * 显示模块
     *
     * @param title
     */
    private void showFragment(String title) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragmentList = fragmentManager.getFragments();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {

            Fragment fragment = fragmentList.get(i);
            if (fragment.getTag().equals(title)) {

                fragmentTransaction.show(fragment);
            } else {

                fragmentTransaction.hide(fragment);
            }
        }
        fragmentTransaction.commit();
    }

    private void initOperation(int flag) {


        //导航栏点击事件
        binding.mainTlNav.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                ImageView nav_iv_pic = view.findViewById(R.id.nav_iv_pic);
                TextView nav_name = view.findViewById(R.id.nav_name);
                nav_name.setTextColor(getColor(R.color.mColorPrimary));

                if (flag == 1) {

                    nav_iv_pic.setImageResource(cNavImgF[tab.getPosition()]);
                } else {

                    nav_iv_pic.setImageResource(cNavImg[tab.getPosition()]);
                }


                String navTitle = nav[tab.getPosition()];
                showFragment(navTitle);

/*                if (tab.getPosition() == 0) {

                    getSupportFragmentManager().beginTransaction().
                            show(homeFragment)
                            .hide(breakThroughFragment)
                            .hide(chapterFragment)
                            .hide(meFragment)
                            .hide(mobClassFragment).commit();
                } else if (tab.getPosition() == 1) {

                    getSupportFragmentManager().beginTransaction()
                            .show(breakThroughFragment)
                            .hide(homeFragment)
                            .hide(chapterFragment)
                            .hide(meFragment)
                            .hide(mobClassFragment).commit();
                } else if (tab.getPosition() == 2) {

                    getSupportFragmentManager().beginTransaction()
                            .show(chapterFragment)
                            .hide(homeFragment)
                            .hide(breakThroughFragment)
                            .hide(meFragment)
                            .hide(mobClassFragment).commit();
                } else if (tab.getPosition() == 3) {

                    getSupportFragmentManager().beginTransaction()
                            .show(mobClassFragment)
                            .hide(meFragment)
                            .hide(breakThroughFragment)
                            .hide(chapterFragment)
                            .hide(homeFragment).commit();
                } else {

                    getSupportFragmentManager().beginTransaction()
                            .show(meFragment)
                            .hide(mobClassFragment)
                            .hide(chapterFragment)
                            .hide(breakThroughFragment)
                            .hide(homeFragment).commit();
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                TextView nav_name = view.findViewById(R.id.nav_name);
                ImageView nav_iv_pic = view.findViewById(R.id.nav_iv_pic);
                nav_name.setTextColor(Color.GRAY);
                if (flag == 1) {

                    nav_iv_pic.setImageResource(navImgF[tab.getPosition()]);
                } else {

                    nav_iv_pic.setImageResource(navImg[tab.getPosition()]);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    /**
     * flag =1  隐藏阅读模块
     *
     * @param flag
     */
    private void initTab(int flag) {


        if (flag == 1) {

            nav = new String[]{"首页", "闯关", "微课", "我的"};
        } else {

            nav = new String[]{"首页", "闯关", "微课", "阅读", "我的"};
        }


        for (int i = 0; i < nav.length; i++) {

            TabLayout.Tab tab = binding.mainTlNav.newTab();
            View navView = LayoutInflater.from(this).inflate(R.layout.nav, null);
            tab.setCustomView(navView);
            TextView nav_name = navView.findViewById(R.id.nav_name);
            ImageView nav_iv_pic = navView.findViewById(R.id.nav_iv_pic);
            nav_name.setText(nav[i]);
            if (flag == 1) {

                nav_iv_pic.setImageResource(navImgF[i]);
            } else {
                nav_iv_pic.setImageResource(navImg[i]);
            }

            binding.mainTlNav.addTab(tab);

            if (i == 0) {

                nav_name.setTextColor(getColor(R.color.mColorPrimary));
                if (flag == 1) {

                    nav_iv_pic.setImageResource(cNavImgF[0]);
                } else {
                    nav_iv_pic.setImageResource(cNavImg[0]);
                }
            } else {

                nav_name.setTextColor(Color.GRAY);
            }
        }
    }

    private void initFragment(int flag) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < nav.length; i++) {

            String navStr = nav[i];
            Fragment fragment = fragmentManager.findFragmentByTag(navStr);
            if (fragment == null) {

                if (navStr.equals("首页")) {

                    fragment = HomeFragment.newInstance(false);
                    homeFragment = (HomeFragment) fragment;
                } else if (navStr.equals("闯关")) {

                    fragment = BreakThroughFragment.newInstance();
                    breakThroughFragment = (BreakThroughFragment) fragment;
                } else if (navStr.equals("微课")) {

                    List<Integer> imoocList = new ArrayList<>();
                    imoocList.add(3);
                    imoocList.add(21);
                    Bundle bundle = MobClassFragment.buildArguments(Constant.OWERID, false, (ArrayList<Integer>) imoocList);
                    fragment = MobClassFragment.newInstance(bundle);
                    mobClassFragment = (MobClassFragment) fragment;
                } else if (navStr.equals("我的")) {

                    fragment = MeFragment.newInstance();
                    meFragment = (MeFragment) fragment;
                } else if (navStr.equals("阅读")) {

                    fragment = ChapterFragment.newInstance();
                    chapterFragment = (ChapterFragment) fragment;
                }
            }
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < nav.length; i++) {

            String navStr = nav[i];
            Fragment fragment = null;
            if (navStr.equals("首页")) {
                fragment = homeFragment;
                fragmentTransaction.add(R.id.main_fl, homeFragment, "首页");
            } else if (navStr.equals("闯关")) {

                fragment = breakThroughFragment;
                fragmentTransaction.add(R.id.main_fl, breakThroughFragment, "闯关");
            } else if (navStr.equals("微课")) {

                fragment = mobClassFragment;
                fragmentTransaction.add(R.id.main_fl, mobClassFragment, "微课");
            } else if (navStr.equals("我的")) {

                fragment = meFragment;
                fragmentTransaction.add(R.id.main_fl, meFragment, "我的");
            } else if (navStr.equals("阅读")) {

                fragment = chapterFragment;
                fragmentTransaction.add(R.id.main_fl, chapterFragment, "阅读");
            }
            if (fragment != null) {

                if (i == 0) {

                    fragmentTransaction.show(fragment);
                } else {

                    fragmentTransaction.hide(fragment);
                }
            }

        }
        fragmentTransaction.commit();
    }

    @Override
    public void getRegisterAll(ProcessBean processBean) {


        int flag = Integer.parseInt(processBean.getResult());
        initTab(flag);
        initFragment(flag);
        initOperation(flag);

        binding.mainTlNav.setVisibility(View.VISIBLE);
        binding.mainFl.setVisibility(View.VISIBLE);
    }
}