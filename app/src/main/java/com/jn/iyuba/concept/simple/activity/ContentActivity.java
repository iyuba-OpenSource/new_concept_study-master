package com.jn.iyuba.concept.simple.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.fragment.PointReadingFragment;
import com.jn.iyuba.concept.simple.entity.NotificationEventbus;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.fragment.EvaluatingFragment;
import com.jn.iyuba.concept.simple.activity.fragment.OriginalFragment;
import com.jn.iyuba.concept.simple.activity.fragment.ParaFragment;
import com.jn.iyuba.concept.simple.activity.fragment.QuestionFragment;
import com.jn.iyuba.concept.simple.activity.fragment.RankFragment;
import com.jn.iyuba.concept.simple.activity.fragment.WordFragment;
import com.jn.iyuba.concept.simple.activity.fragment.YouthVideoFragment;
import com.jn.iyuba.concept.simple.activity.login.LoginActivity;
import com.jn.iyuba.concept.simple.activity.vip.VipActivity;
import com.jn.iyuba.concept.simple.adapter.MyFragmentAdapter;
import com.jn.iyuba.succinct.databinding.ActivityContentBinding;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.model.bean.PdfBean;
import com.jn.iyuba.concept.simple.presenter.home.ContentPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.popup.MorePopup;
import com.jn.iyuba.concept.simple.view.home.ContentContract;
import com.jn.iyuba.novel.NovelConstant;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.Chapter;
import com.jn.iyuba.novel.db.NovelBook;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.db.NovelSentence;
import com.jn.iyuba.novel.db.dao.BookDao;
import com.jn.iyuba.novel.db.dao.ChapterDao;
import com.jn.iyuba.novel.db.dao.NovelSentenceDao;
import com.jn.iyuba.novel.model.bean.NovelSentenceBean;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 课文 主界面
 */
public class ContentActivity extends BaseActivity<ContentContract.ContentView, ContentContract.ContentPresenter>
        implements ContentContract.ContentView {

    //显示第几页
    private int page = 0;
    private int voaid;

    private String title;

    /**
     * 书虫和小说馆用的到
     * * 章节序号
     */
    private int position;

    /**
     * 是否是书虫
     */
    private boolean isBookWorm = false;


    private ActivityContentBinding binding;

    private MyFragmentAdapter myFragmentAdapter;

    private String[] nav;

    private MorePopup morePopup;

    private MorePopup pdfpopup;


    /**
     * 接收书虫剑桥
     */
    private BroadcastReceiver broadcastReceiver;



   /*

   遍历数据存储到数据库，导出json文件
   private List<Title> dataList;
    private int position = 0;

    private int page = 1;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (dataList.size() < 100) {

                Log.d("qqqqqqq", "快完成了");
            }

            if (position < dataList.size()) {

                Title te = dataList.get(position);

                if (BookUtil.isYouthBook(te.getBookid())) {//新概念英语青少版

                    binding.contentPbLoading.setVisibility(View.VISIBLE);
                    presenter.textExamApi(Integer.parseInt(te.getVoaId()));
                } else {//新概念英语

                    if (te.getLanguage().equalsIgnoreCase("US")) {//美音 = voaid * 10

                        presenter.getConceptSentence(Integer.parseInt(te.getVoaId()));
                    } else {

                        presenter.getConceptSentence(Integer.parseInt(te.getVoaId()) * 10);
                    }
                }
            } else {

                page++;
                position = 0;
                dataList = LitePal.limit(100).offset((page - 1) * 100).find(Title.class);
                if (dataList.size() != 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    Log.d("qqqqqqq", "结束了");
                }
                Log.d("qqqqqqq", page + "");
            }
            return false;
        }
    });*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chapterBroadcast();
        getBundle();
        initView();

     /*   page = 1;
        position = 0;
        dataList = LitePal.limit(100).offset(0).find(Title.class);
        handler.sendEmptyMessage(1);*/

        getData();

        //显示哪个页面
        if (page != 0) {

            binding.contentVp.setCurrentItem(page);
        }
    }

    /**
     * 随机播放 顺序播放
     * 数据切换
     */
    private void chapterBroadcast() {

        IntentFilter intentFilter = new IntentFilter(Constant.BROADCAST_CHAPTER);
        intentFilter.addAction(Constant.BROADCAST_TITLE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals(Constant.BROADCAST_CHAPTER)) {

                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {

                        Chapter chapter = (Chapter) bundle.getSerializable("DATA");
                        updateData(Integer.parseInt(chapter.getVoaid()), Integer.parseInt(chapter.getChapter_order()));
                    }
                } else if (action.equals(Constant.BROADCAST_TITLE)) {//新概念

                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {

                        int voaid = bundle.getInt("DATA");
                        updateData(voaid, -1);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(MyApplication.getContext())
                .registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (broadcastReceiver != null) {

            LocalBroadcastManager.getInstance(MyApplication.getContext()).unregisterReceiver(broadcastReceiver);
        }
    }

    private void getData() {

        if (isBookWorm) {//是否是书虫

            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
                    int count = novelSentenceDao.getCountForChapter(NovelConstant.novelBook.getTypes(), voaid + "", position + "",
                            NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel());
                    if (count == 0) {

                        presenter.getStroryInfo("detail", Integer.parseInt(NovelConstant.novelBook.getLevel()),
                                NovelConstant.novelBook.getOrder_number(), position + "", NovelConstant.novelBook.getTypes());
                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                initOperation();
                            }
                        });
                    }
                }
            }).start();
        } else {//新概念

            int count;
            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {//新概念英语青少版

                count = LitePal.where("voaid = ?", voaid + "").count(Sentence.class);
                if (count > 0) {

                    initOperation();
                } else {

                    binding.contentPbLoading.setVisibility(View.VISIBLE);
                    presenter.textExamApi(voaid);
                }
            } else {//新概念英语

                if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                    count = LitePal.where("voaid = ? ", voaid + "").count(Sentence.class);
                } else {//uk = us * 10  与title中数据乘10的关系

                    count = LitePal.where("voaid = ? ", (voaid * 10) + "").count(Sentence.class);
                }

                if (count > 0) {

                    initOperation();
                } else {

                    binding.contentPbLoading.setVisibility(View.VISIBLE);

                    if (Constant.LANGUAGE.equals("US")) {//美音 = voaid * 10

                        presenter.getConceptSentence(voaid);
                    } else {

                        presenter.getConceptSentence(voaid * 10);
                    }

                }
            }
        }
    }

    @Override
    public View initLayout() {
        binding = ActivityContentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public ContentContract.ContentPresenter initPresenter() {
        return new ContentPresenter();
    }


    private void initOperation() {


        if (isBookWorm) {

            nav = new String[]{"听力", "阅读", "AI评测", "排行榜"};
        } else {

            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                nav = new String[]{"听力", "阅读", "AI评测", "单词", "排行榜", "视频", "点读"};
            } else {
                nav = new String[]{"听力", "阅读", "AI评测", "单词", "练习", "排行榜"};
            }
        }

        myFragmentAdapter = new MyFragmentAdapter(this, voaid, nav, isBookWorm, position);
        binding.contentVp.setAdapter(myFragmentAdapter);
        binding.contentVp.setOffscreenPageLimit(6);

        binding.contentVp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                TabLayout.Tab tab = binding.contentTlTitle.getTabAt(position);
                if (tab != null) {

                    tab.select();
                }
            }
        });
        //tab
        for (String s : nav) {

            TabLayout.Tab tab = binding.contentTlTitle.newTab();
            tab.setText(s);
            tab.setTag(s);
            binding.contentTlTitle.addTab(tab);
        }
        binding.contentTlTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                binding.contentVp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //设置显示哪个页面
        SharedPreferences lessonSp = getSharedPreferences(Constant.SP_LESSON, MODE_PRIVATE);
        String type = lessonSp.getString(Constant.SP_KEY_LESSON, "原文");

        int tabCount = binding.contentTlTitle.getTabCount();
        for (int i = 0; i < tabCount; i++) {

            TabLayout.Tab tab = binding.contentTlTitle.getTabAt(i);
            if (tab != null && tab.getTag() != null && tab.getTag().equals(type)) {

                tab.select();
                break;
            }
        }
    }


    /**
     * 播放
     * 切换数据
     */
    public void updateVoaid() {

        if (myFragmentAdapter != null) {

            myFragmentAdapter.setVoaid(voaid);

            FragmentManager fragmentManager = getSupportFragmentManager();
            OriginalFragment originalFragment = null;
            EvaluatingFragment evaluatingFragment = null;
            RankFragment rankFragment = null;
            YouthVideoFragment youthVideoFragment = null;
            QuestionFragment questionFragment = null;
            ParaFragment paraFragment = null;
            WordFragment wordFragment = null;//单词
            PointReadingFragment pointReadingFragment = null;


            for (Fragment fragment : fragmentManager.getFragments()) {

                if (fragment instanceof ParaFragment) {

                    paraFragment = (ParaFragment) fragment;
                } else if (fragment instanceof OriginalFragment) {

                    originalFragment = (OriginalFragment) fragment;
                } else if (fragment instanceof EvaluatingFragment) {

                    evaluatingFragment = (EvaluatingFragment) fragment;
                } else if (fragment instanceof RankFragment) {

                    rankFragment = (RankFragment) fragment;
                } else if (fragment instanceof WordFragment) {

                    wordFragment = (WordFragment) fragment;
                } else if (fragment instanceof YouthVideoFragment) {

                    youthVideoFragment = (YouthVideoFragment) fragment;
                } else if (fragment instanceof QuestionFragment) {

                    questionFragment = (QuestionFragment) fragment;
                } else if (fragment instanceof PointReadingFragment) {

                    pointReadingFragment = (PointReadingFragment) fragment;
                }
            }

            if (originalFragment != null) {

                Bundle bundle = originalFragment.getArguments();
                if (bundle != null) {

                    bundle.putInt("VOA_ID", voaid);
                    bundle.putInt("CHAPTER_ORDER", position);
                }
                originalFragment.updateData();
            }

            if (evaluatingFragment != null) {

                Bundle bundle = evaluatingFragment.getArguments();
                if (bundle != null) {

                    bundle.putInt("VOA_ID", voaid);
                    bundle.putInt("CHAPTER_ORDER", position);
                }
                evaluatingFragment.updateData();
            }

            if (rankFragment != null) {

                Bundle bundle = rankFragment.getArguments();
                if (bundle != null) {

                    bundle.putInt("VOA_ID", voaid);
                }
                rankFragment.updateData();
            }
            if (youthVideoFragment != null) {

                Bundle bundle = youthVideoFragment.getArguments();
                if (bundle != null) {

                    bundle.putInt("VOA_ID", voaid);
                }
                youthVideoFragment.updateData();
            }
            if (questionFragment != null) {

                Bundle bundle = questionFragment.getArguments();
                if (bundle != null) {

                    bundle.putInt("VOA_ID", voaid);
                }
                questionFragment.updateData();
            }
            if (paraFragment != null) {

                Bundle bundle = paraFragment.getArguments();
                if (bundle != null) {

                    bundle.putInt("VOAID", voaid);
                    bundle.putInt("CHAPTER_ORDER", position);
                }
                paraFragment.updateData();
            }
            if (wordFragment != null) {

                Bundle bundle = wordFragment.getArguments();
                if (bundle != null) {

                    bundle.putInt("VOAID", voaid);
                }
                wordFragment.updateData();
            }
            if (pointReadingFragment != null) {

                Bundle bundle = pointReadingFragment.getArguments();
                if (bundle != null) {

                    bundle.putInt("VOAID", voaid);
                }
                pointReadingFragment.updateData();
            }
        }

    }

    private void initView() {

        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        if (isBookWorm) {

            binding.toolbar.toolbarIvTitle.setText(title);
        } else {

            List<Title> titleList = LitePal.where("voaid = ?", voaid + "").find(Title.class);
            if (titleList.size() > 0) {

                Title t = titleList.get(0);
                if (BookUtil.isYouthBook(t.getBookid())) {

                    binding.toolbar.toolbarIvTitle.setText(title);
                } else {

                    String voaStr = voaid + "";
                    String lessonPosition = voaStr.substring(voaStr.length() - 3);
                    binding.toolbar.toolbarIvTitle.setText("LESSON " + Integer.parseInt(lessonPosition) + " " + title);
                }
            }
        }


        binding.toolbar.toolbarIvRight.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarIvRight.setImageResource(R.mipmap.icon_more);
        binding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initMorePopup();
            }
        });

        binding.contentLlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.contentLlError.setVisibility(View.GONE);
                binding.contentPbLoading.setVisibility(View.VISIBLE);

                getData();
            }
        });
    }


    /**
     * pdf弹窗
     */
    private void initPdfMore() {

        if (pdfpopup == null) {

            List<String> strings = new ArrayList<>();
            strings.add("双语pdf");
            strings.add("英文pdf");
            pdfpopup = new MorePopup(ContentActivity.this);
            pdfpopup.setDatas(strings);
            pdfpopup.setPopupGravity(Gravity.CENTER);
            pdfpopup.setCallback(new MorePopup.Callback() {
                @Override
                public void getItem(String s) {

                    if (s.equals("双语pdf")) {

                        if (isBookWorm) {

                            presenter.getBookWormPdf(voaid + "", NovelConstant.novelBook.getTypes(), "cn");
                        } else if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                            presenter.getVoaPdfFile_new("voa", voaid, 0);
                        } else {

                            if (Constant.LANGUAGE.equals("US")) {

                                presenter.getConceptPdfFile(voaid, 0);
                            } else {
                                presenter.getConceptPdfFile(voaid * 10, 0);
                            }
                        }
                    } else {

                        if (isBookWorm) {

                            presenter.getBookWormPdf(voaid + "", NovelConstant.novelBook.getTypes(), "en");
                        } else if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                            presenter.getVoaPdfFile_new("voa", voaid, 1);
                        } else {

                            if (Constant.LANGUAGE.equals("US")) {

                                presenter.getConceptPdfFile(voaid, 2);
                            } else {
                                presenter.getConceptPdfFile(voaid * 10, 2);
                            }
                        }
                    }
                }
            });
        }
        pdfpopup.showPopupWindow();
    }

    private void initMorePopup() {

        if (morePopup == null) {


            List<String> strings = new ArrayList<>();

            if (isBookWorm) {
                strings.add("PDF导出");
                strings.add("中英/英文");
            } else {
                strings.add("PDF导出");
                strings.add("中英/英文");
                strings.add("分享");
            }


            morePopup = new MorePopup(ContentActivity.this);
            morePopup.setDatas(strings);
            morePopup.setCallback(new MorePopup.Callback() {
                @Override
                public void getItem(String s) {

                    if (s.equals("PDF导出")) {

                        if (Constant.userinfo == null) {

                            startActivity(new Intent(ContentActivity.this, LoginActivity.class));
                            return;
                        }
                        if (Constant.userinfo.isVip()) {

                            initPdfMore();
                        } else {

                            //todo  扣除积分的弹窗
                            alertVip();
                        }

                        morePopup.dismiss();

                    } else if (s.equals("分享")) {

                        dealShare();
                    } else if (s.equals("中英/英文")) {

                        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
                        OriginalFragment originalFragment = null;
                        for (Fragment fragment : fragmentList) {

                            if (fragment instanceof OriginalFragment) {
                                originalFragment = (OriginalFragment) fragment;
                            }
                        }
                        if (originalFragment != null) {

                            originalFragment.setShowCnEn();
                            morePopup.dismiss();
                        }
                    }
                }
            });
        }
        morePopup.showPopupWindow(binding.toolbar.toolbarIvRight);
    }

    private void alertVip() {

        new AlertDialog.Builder(ContentActivity.this)
                .setTitle("提示")
                .setMessage("会员用户下载pdf文件免费,非会员用户每次下载需要扣除20积分")
                .setNegativeButton("购买会员", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        VipActivity.startActivity(ContentActivity.this);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                        String flag = null;
                        try {
                            flag = Base64.encodeToString(
                                    URLEncoder.encode(df.format(new Date(System.currentTimeMillis())), "UTF-8").getBytes(), Base64.DEFAULT);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        presenter.updateScore(40, 1, flag, Constant.userinfo.getUid() + "", Constant.APPID, voaid + "");
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 存储logo到本地
     */
    public void saveLogoToLocal() {

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/logo.jpg");
        if (!file.exists()) {

            try {

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 处理分享所需要的数据
     */
    private void dealShare() {

        String url;
        String sTitle;
        String content = "新概念英语学习英语的好帮手，快来下载吧！";
        String imgUrl;

        if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

            List<Title> titleList = LitePal.where("voaid = ?", voaid + "").find(Title.class);

            if (titleList.size() == 0) {

                return;
            }
            Title tTitle = titleList.get(0);

            url = Constant.URL_M_QOMOLAMA + "/voaS/playPY.jsp?apptype=newConceptTalk&id=" + voaid;
            sTitle = "新概念英语青少版--" + tTitle.getTitle();
            imgUrl = tTitle.getPic();
        } else {

            List<Title> titleList;
            if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                titleList = LitePal.where("voaid = ?", voaid + "").find(Title.class);
            } else {

                titleList = LitePal.where("voaid = ? and language = ?", voaid + "", "UK").find(Title.class);
            }

            if (titleList.size() == 0) {

                return;
            }
            Title tTitle = titleList.get(0);

            url = Constant.URL_AI + "/newconcept/course?language=" + Constant.LANGUAGE + "&unit=" + Constant.book.getId() + "&lesson=" + voaid;
            sTitle = "新概念英语--" + tTitle.getTitle();
            imgUrl = tTitle.getPic();
        }

        if (imgUrl == null || imgUrl.equals("")) {

            saveLogoToLocal();
            imgUrl = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/logo.jpg";
        }
        share(sTitle, content, url, imgUrl);
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            voaid = bundle.getInt("VOA_ID");
            title = bundle.getString("TITLE");
            page = bundle.getInt("PAGE", 0);
            position = bundle.getInt("POSITION", -1);
            isBookWorm = bundle.getBoolean("IS_BOOK_WORM", false);
        }
    }

    /**
     * 跳转
     *
     * @param activity
     * @param voaId    voaid
     * @param title    标题
     * @param page     显示哪一页
     */
    public static void startActivity(Activity activity, int voaId, String title, int page, int position) {

        Intent intent = new Intent(activity, ContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("VOA_ID", voaId);
        bundle.putString("TITLE", title);
        bundle.putInt("PAGE", page);
        bundle.putInt("POSITION", position);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    /**
     * 跳转
     *
     * @param activity
     * @param voaId
     * @param title
     * @param page
     * @param position
     * @param isBookWorm 是否是书虫（剑桥小说馆）
     */
    public static void startActivity(Activity activity, int voaId, String title, int page, int position, boolean isBookWorm) {

        Intent intent = new Intent(activity, ContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("VOA_ID", voaId);
        bundle.putString("TITLE", title);
        bundle.putInt("PAGE", page);
        bundle.putInt("POSITION", position);
        bundle.putBoolean("IS_BOOK_WORM", isBookWorm);
        intent.putExtras(bundle);
        activity.startActivity(intent);
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
    public void getSentenceComplete(LessonDetailBean lessonDetailBean) {


        binding.contentPbLoading.setVisibility(View.GONE);
        if (lessonDetailBean == null) {

            binding.contentLlError.setVisibility(View.VISIBLE);
        } else {


            List<Sentence> sentenceList = lessonDetailBean.getData();
            if (sentenceList == null) {

                sentenceList = lessonDetailBean.getVoatext();
            }
            //存储数据
            for (Sentence sentence : sentenceList) {

                if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {//青少版

                    sentence.setVoaid(voaid + "");
                }

                List<Sentence> sentences = LitePal
                        .where("voaid = ? and Paraid = ? and IdIndex = ? "
                                , sentence.getVoaid()
                                , sentence.getParaid()
                                , sentence.getIdIndex())
                        .find(Sentence.class);

                if (sentences.size() == 0) {

                    sentence.save();
                } else {

                    sentence.updateAll("voaid = ? and Paraid = ? and IdIndex = ?"
                            , sentence.getVoaid()
                            , sentence.getParaid()
                            , sentence.getIdIndex());
                }
            }
            if (myFragmentAdapter == null) {

                initOperation();
            } else {//随机播放或者
                updateVoaid();
            }
        }
/*     数据遍历存储
        position++;
        handler.sendEmptyMessage(1);*/
    }

    @Override
    public void getConceptPdfFile(PdfBean pdfBean) {

        if (pdfpopup != null) {

            pdfpopup.dismiss();
        }
        if (isBookWorm) {

            String url = "http://apps.iyuba.cn/book" + pdfBean.getPath();
            showPdfUrl(url);
        } else {

            showPdfUrl(Constant.URL_APPS + "/iyuba" + pdfBean.getPath());
        }

    }

    @Override
    public void getStroryInfo(NovelSentenceBean novelSentenceBean) {

        if (binding.contentPbLoading.getVisibility() == View.VISIBLE) {

            binding.contentPbLoading.setVisibility(View.GONE);
        }

        if (novelSentenceBean == null) {

            binding.contentLlError.setVisibility(View.VISIBLE);
        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
                    List<NovelSentence> novelSentenceList = novelSentenceBean.getTexts();
                    for (int i = 0; i < novelSentenceList.size(); i++) {

                        NovelSentence novelSentence = novelSentenceList.get(i);
                        if (novelSentence.getTypes() == null) {

                            novelSentence.setTypes("bookworm");
                        }

                        int count = novelSentenceDao.getCountByPrimaryKeys(novelSentence.getTypes(), novelSentence.getVoaid(), novelSentence.getChapterOrder()
                                , novelSentence.getOrderNumber(), novelSentence.getLevel(), novelSentence.getParaid(), novelSentence.getIndex());
                        if (count == 0) {//不存在则存储

                            novelSentenceDao.addSentence(novelSentence);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (myFragmentAdapter == null) {

                                initOperation();
                            } else {//随机播放 或者 顺序播放

                                updateVoaid();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void deductScore(ScoreBean scoreBean) {

        initPdfMore();
    }


    private void showPdfUrl(String pdfUrl) {

        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", pdfUrl);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);

        AlertDialog.Builder builder = new AlertDialog.Builder(ContentActivity.this);
        builder.setTitle("PDF链接生成成功!");
        builder.setMessage(pdfUrl + "(链接已复制)");
        // builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);  //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String sTitle;
                String content = "新概念英语学习英语的好帮手，快来下载吧！";
                String imgUrl;

                if (isBookWorm) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            AppDatabase appDatabase = NovelDB.getInstance().getDB();
                            ChapterDao chapterDao = appDatabase.chapterDao();
                            //章节
                            Chapter chapter = chapterDao.getSingleByPrimaryKeys(voaid + "", NovelConstant.novelBook.getTypes(), NovelConstant.novelBook.getLevel(),
                                    NovelConstant.novelBook.getOrder_number(), position + "");
                            //获取课本图片

                            String cTitle = chapter.getCname_cn();
                            String cContent = chapter.getCname_en();
                            String cImgUrl = null;
                            if (Constant.userinfo != null && Constant.userinfo.isVip()) {

                                cImgUrl = "http://staticvip2.iyuba.cn" + NovelConstant.novelBook.getPic();
                            } else {
                                cImgUrl = "http://static2.iyuba.cn" + NovelConstant.novelBook.getPic();
                            }
                            share(cTitle + "PDF", cContent, pdfUrl, cImgUrl);
                        }
                    }).start();
                } else if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                    List<Title> titleList = LitePal.where("voaid = ?", voaid + "").find(Title.class);

                    if (titleList.size() == 0) {

                        return;
                    }
                    Title tTitle = titleList.get(0);

                    sTitle = "新概念英语青少版--" + tTitle.getTitle();
                    imgUrl = tTitle.getPic();

                    if (imgUrl == null || imgUrl.equals("")) {

                        saveLogoToLocal();
                        imgUrl = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/logo.jpg";
                    }
                    share(sTitle + "PDF", content, pdfUrl, imgUrl);
                } else {

                    List<Title> titleList;
                    if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                        titleList = LitePal.where("voaid = ?", voaid + "").find(Title.class);
                    } else {

                        titleList = LitePal.where("voaid = ? and language = ?", voaid + "", "UK").find(Title.class);
                    }

                    if (titleList.size() == 0) {

                        return;
                    }
                    Title tTitle = titleList.get(0);

                    sTitle = "新概念英语--" + tTitle.getTitle();
                    imgUrl = tTitle.getPic();

                    if (imgUrl == null || imgUrl.equals("")) {

                        saveLogoToLocal();
                        imgUrl = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/logo.jpg";
                    }
                    share(sTitle + "PDF", content, pdfUrl, imgUrl);
                }
            }
        });

        //设置反面按钮
        builder.setNegativeButton("打开", (dialog, which) -> {
            Uri uri = Uri.parse(pdfUrl);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            startActivity(intent);
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        dialog.show();                              //显示对话框
    }


    private void share(String title, String content, String url, String imageUrl) {

        OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
       /* if (platform != null) {
            oks.setPlatform(platform);
        }*/
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        //分享回调
        if (imageUrl.startsWith("http")) {

            oks.setImageUrl(imageUrl);
        } else {

            oks.setImagePath(imageUrl);
        }
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                // 分享成功回调
                Toast.makeText(MyApplication.getContext(), "分享成功", Toast.LENGTH_SHORT).show();

                if (Constant.userinfo == null) {

                    return;
                }

                int srid = 0;
                if (platform.getName().equals(QQ.NAME) || platform.getName().equals(Wechat.NAME)) {

                    srid = 49;
                } else {
                    srid = 19;
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                String flag = null;
                try {
                    flag = Base64.encodeToString(
                            URLEncoder.encode(df.format(new Date(System.currentTimeMillis())), "UTF-8").getBytes(), Base64.DEFAULT);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                presenter.updateScore(srid, 1, flag, Constant.userinfo.getUid() + "", Constant.APPID, voaid + "");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                // 分享失败回调   platform:平台对象，i:表示当前的动作(9表示分享)，throwable:异常信息

            }

            @Override
            public void onCancel(Platform platform, int i) {
                // 分享取消回调
            }
        });
        // 启动分享
        oks.show(MobSDK.getContext());
    }


    /**
     * 设置新的标题
     *
     * @param voaid
     */
    private void getTitle(int voaid) {


        if (isBookWorm) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    ChapterDao chapterDao = appDatabase.chapterDao();
                    Chapter chapter = chapterDao.getSingleByPrimaryKeys(voaid + "", NovelConstant.novelBook.getTypes(), NovelConstant.novelBook.getLevel(),
                            NovelConstant.novelBook.getOrder_number(), position + "");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            binding.toolbar.toolbarIvTitle.setText(chapter.getCname_en());
                        }
                    });
                }
            }).start();

        } else {

            List<Title> titleList;
            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                titleList = LitePal.where("voaid = ?", voaid + "").find(Title.class);
            } else {

                titleList = LitePal.where("voaid = ? and language = ?", voaid + "", Constant.LANGUAGE).find(Title.class);
            }

            if (titleList.size() > 0) {

                String voaidStr = titleList.get(0).getVoaId();
                String lesson = voaidStr.substring(voaidStr.length() - 3);
                binding.toolbar.toolbarIvTitle.setText("LESSON " + Integer.parseInt(lesson) + " " + titleList.get(0).getTitle());
            }
        }
    }


    /**
     * 获取句子数据
     */
    private void getSentence() {


        //获取句子数据
        int count;
        if (isBookWorm) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
                    int nsCount = novelSentenceDao.getCountForChapter(NovelConstant.novelBook.getTypes(), voaid + "",
                            position + "", NovelConstant.novelBook.getOrder_number(), NovelConstant.novelBook.getLevel());
                    if (nsCount > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                updateVoaid();
                            }
                        });
                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                binding.contentPbLoading.setVisibility(View.VISIBLE);
                                presenter.getStroryInfo("detail", Integer.parseInt(NovelConstant.novelBook.getLevel()),
                                        NovelConstant.novelBook.getOrder_number(),
                                        position + "", NovelConstant.novelBook.getTypes());
                            }
                        });
                    }
                }
            }).start();
        } else {//新概念

            if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {

                count = LitePal.where("voaid = ?", voaid + "").count(Sentence.class);
                if (count > 0) {

                    updateVoaid();
                } else {

                    binding.contentPbLoading.setVisibility(View.VISIBLE);
                    presenter.textExamApi(voaid);
                }
            } else {

                if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                    count = LitePal.where("voaid = ? ", voaid + "").count(Sentence.class);
                } else {

                    count = LitePal.where("voaid = ? ", (voaid * 10) + "").count(Sentence.class);
                }

                if (count > 0) {

                    updateVoaid();
                } else {

                    binding.contentPbLoading.setVisibility(View.VISIBLE);

                    if (Constant.LANGUAGE.equals("US")) {//美音 = voaid * 10

                        presenter.getConceptSentence(voaid);
                    } else {

                        presenter.getConceptSentence(voaid * 10);
                    }

                }
            }
        }
    }


    /**
     * 更新数据
     */
/*    public void updateData(int voaid) {

        this.voaid = voaid;

        getTitle(voaid);

        getSentence();
    }*/


    /**
     * 书虫剑桥需要
     *
     * @param voaid
     * @param chapterOrder
     */
    public void updateData(int voaid, int chapterOrder) {

        this.voaid = voaid;
        this.position = chapterOrder;

        getTitle(voaid);

        getSentence();
    }
}