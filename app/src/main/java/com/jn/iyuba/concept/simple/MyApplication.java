package com.jn.iyuba.concept.simple;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.data.local.HeadlineInfoHelper;
import com.iyuba.imooclib.IMooc;
import com.iyuba.imooclib.data.local.IMoocDBManager;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.favor.data.local.BasicFavorInfoHelper;
import com.iyuba.module.privacy.IPrivacy;
import com.iyuba.module.privacy.PrivacyInfoHelper;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.iyuba.share.ShareExecutor;
import com.iyuba.share.mob.MobShareExecutor;
import com.iyuba.widget.unipicker.IUniversityPicker;
import com.jn.iyuba.concept.simple.db.Book;
import com.jn.iyuba.concept.simple.entity.WordBook;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.UserBean;
import com.jn.iyuba.novel.NovelApplication;
import com.jn.iyuba.succinct.BuildConfig;
import com.jn.iyuba.succinct.R;
import com.mob.MobSDK;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.umeng.commonsdk.UMConfigure;
import com.yd.saas.ydsdk.manager.YdConfig;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.common.YoudaoSDK;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import personal.iyuba.personalhomelibrary.PersonalHome;
import personal.iyuba.personalhomelibrary.data.local.HLDBManager;
import personal.iyuba.personalhomelibrary.ui.widget.dialog.ShareBottomDialog;

public class MyApplication extends Application {


    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private String channel;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        NovelApplication.init(this);

        channel = ChannelReaderUtil.getChannel(this);
        UMConfigure.preInit(this, Constant.UMENG_KEY, channel);
        NetWorkManager.getInstance().init();

        LitePal.initialize(this);


        Constant.URL_PROTOCOLUSE = "http://iuserspeech.iyuba.cn:9001/api/protocoluse666.jsp?company=" + BuildConfig.COMPANY + "&apptype=" + getString(R.string.app_name);

        Constant.URL_PROTOCOLPRI = "http://iuserspeech.iyuba.cn:9001/api/protocolpri.jsp?company=" + BuildConfig.COMPANY + "&apptype=" + getString(R.string.app_name);


        IHeadline.resetMseUrl();
        IHeadline.setExtraMseUrl(Constant.IUSERSPEECH_URL + "/jtest/");
        IHeadline.setExtraMergeAudioUrl(Constant.IUSERSPEECH_URL + "/jtest/merge/");


        //获取用户数据
        SharedPreferences privateSp = getSharedPreferences(Constant.SP_PRIVACY, MODE_PRIVATE);
        int state = privateSp.getInt(Constant.SP_KEY_PRIVACY_STATE, 0);
        if (state == 1) {


            //共通模块
            init();
        } else {

            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(Constant.ACTION_INIT));
        }


    }


    private void init() {

        //分享
        List<String> list = new ArrayList<>();
        list.add("QQ");
        list.add("QQ空间");
        list.add("微信好友");
        list.add("微信收藏");
        list.add("微信朋友圈");

        UMConfigure.init(this, Constant.UMENG_KEY, channel, UMConfigure.DEVICE_TYPE_PHONE, "");
        MobSDK.submitPolicyGrantResult(true);

        System.loadLibrary("msaoaidsec");


        YdConfig.getInstance().init(this, Constant.APPID + "");

        ShareBottomDialog.setSharedPlatform(list);
        MobShareExecutor executor = new MobShareExecutor();
        ShareExecutor.getInstance().setRealExecutor(executor);

        //华为平台上的
//        HwAds.init(this);

        //初始化有道
        YouDaoAd.getYouDaoOptions().setCanObtainAndroidId(false);
        YouDaoAd.getYouDaoOptions().setAppListEnabled(false);
        YouDaoAd.getYouDaoOptions().setPositionEnabled(false);
        YouDaoAd.getYouDaoOptions().setSdkDownloadApkEnabled(true);
        YouDaoAd.getYouDaoOptions().setDeviceParamsEnabled(false);
        YouDaoAd.getYouDaoOptions().setWifiEnabled(false);
        YouDaoAd.getNativeDownloadOptions().setConfirmDialogEnabled(true);
        YoudaoSDK.init(this);

        //个人中心 学习选择
        IUniversityPicker.init(MyApplication.this);


        //微课的隐私协议
        PrivacyInfoHelper.init(this);
        PrivacyInfoHelper.getInstance().putApproved(true);

        PersonalHome.init(this, Constant.APPID + "", getString(R.string.app_name));
        PersonalHome.setCategoryType(Constant.TYPE);

        //appname 传type值
        IMooc.init(this, Constant.OWERID + "", Constant.TYPE);

        IPrivacy.init(this);

        IMoocDBManager.init(this);
        HLDBManager.init(this);
        com.iyuba.headlinelibrary.data.local.db.HLDBManager.init(this);
        IMooc.setEnableShare(false);

        HeadlineInfoHelper.init(getApplicationContext());
        IHeadline.init(getApplicationContext(), Constant.APPID + "", getString(R.string.app_name));
        IHeadline.setEnableShare(true);
        IHeadline.setEnableGoStore(false);

        BasicFavorInfoHelper.init(this);
        BasicFavorDBManager.init(this);

        DLManager.init(this, 8);

        SharedPreferences userSp = getSharedPreferences(Constant.SP_USER, MODE_PRIVATE);
        String userStr = userSp.getString(Constant.SP_KEY_USER_INFO, "");
        if (!userStr.equals("")) {

            Constant.userinfo = new Gson().fromJson(userStr, UserBean.UserinfoDTO.class);

            PersonalHome.setSaveUserinfo(Constant.userinfo.getUid(), Constant.userinfo.getUsername(), Constant.userinfo.getVipStatus());
            //登录共通模块
            User user = new User();
            user.vipStatus = Constant.userinfo.getVipStatus();
            if (Constant.userinfo.getExpireTime() != 0) {
                user.vipExpireTime = Long.parseLong(Constant.userinfo.getExpireTime() + "");
            }
            user.uid = Constant.userinfo.getUid();
            user.credit = Constant.userinfo.getCredits();
            user.name = Constant.userinfo.getUsername();
            user.imgUrl = Constant.userinfo.getImgSrc();
            user.email = Constant.userinfo.getEmail();
            user.mobile = Constant.userinfo.getMobile();
            if (!TextUtils.isEmpty(Constant.userinfo.getAmount()) && !"null".equals(Constant.userinfo.getAmount())) {
                user.iyubiAmount = Integer.parseInt(Constant.userinfo.getAmount());
            }
            IyuUserManager.getInstance().setCurrentUser(user);
        }

        //课本
        String bookStr = userSp.getString(Constant.SP_KEY_BOOk, "");
        if (bookStr.equals("")) {

            Book book = new Book();
            book.setId("1");
            book.setDescCn("第一册");
            Constant.book = book;
        } else {

            Constant.book = new Gson().fromJson(bookStr, Book.class);
        }
        //语言
        String languageStr = userSp.getString(Constant.SP_KEY_LANGUAGE, "");
        if (languageStr.equals("")) {

            Constant.LANGUAGE = "US";
        } else {

            Constant.LANGUAGE = languageStr;
        }
        //单词闯关课本
        String wordBookStr = userSp.getString(Constant.SP_KEY_WORD_BOOK, "");
        if (!wordBookStr.equals("")) {

            Constant.wordBook = new Gson().fromJson(wordBookStr, WordBook.class);
        }
    }

    public static Context getContext() {
        return context;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        context = null;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            init();
        }
    };
}
