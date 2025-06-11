package com.jn.iyuba.concept.simple.activity.me;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.module.toolbox.MD5;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.AboutActivity;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.activity.ChooseBookActivity;
import com.jn.iyuba.concept.simple.activity.MyWebActivity;
import com.jn.iyuba.concept.simple.activity.home.MyDubbingActivity;
import com.jn.iyuba.concept.simple.activity.login.WxLoginActivity;
import com.jn.iyuba.concept.simple.activity.vip.VipActivity;
import com.jn.iyuba.concept.simple.adapter.MenuAdapter;
import com.jn.iyuba.concept.simple.entity.SideNav;
import com.jn.iyuba.concept.simple.presenter.me.SettingPresenter;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.DownloadApk;
import com.jn.iyuba.concept.simple.util.LoginUtil;
import com.jn.iyuba.concept.simple.util.broadcast.DownloadBroadcast;
import com.jn.iyuba.concept.simple.util.popup.MorePopup;
import com.jn.iyuba.concept.simple.view.me.SettingContract;
import com.jn.iyuba.succinct.BuildConfig;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.ActivitySettingBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import personal.iyuba.personalhomelibrary.ui.my.MySpeechActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryType;

/**
 * 我的-设置页面
 */
public class SettingActivity extends BaseActivity<SettingContract.SettingView, SettingContract.SettingPresenter>
        implements SettingContract.SettingView {

    private ActivitySettingBinding binding;

    private MenuAdapter menuAdapter;

    private MorePopup morePopup;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.toolbarIvTitle.setText("设置");
        binding.toolbar.toolbarIvBack.setOnClickListener(v -> finish());

        sp = getSharedPreferences(Constant.SP_LESSON, MODE_PRIVATE);

        //菜单
        List<SideNav> sideNavList = new ArrayList<>();
        sideNavList.add(new SideNav(22, "课文首页设置:" + sp.getString(Constant.SP_KEY_LESSON, "原文")));
        sideNavList.add(new SideNav(5, "关于我们"));
        sideNavList.add(new SideNav(15, "同步数据（听力、单词、口语、练习题）"));
        sideNavList.add(new SideNav(6, "举报"));
        sideNavList.add(new SideNav(23, "意见反馈"));
        sideNavList.add(new SideNav(20, "版本更新"));
        sideNavList.add(new SideNav(25, "客服电话：4008881905"));

        if (Constant.userinfo != null) {

            sideNavList.add(new SideNav(10, "退出登录"));
        }

        binding.setttingRv.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(R.layout.item_book, sideNavList);
        binding.setttingRv.setAdapter(menuAdapter);

        menuAdapter.setOnItemClickListener((adapter, view, position) -> {

            SideNav sideNav = menuAdapter.getItem(position);
            if (sideNav != null) {

                dealClick(sideNav);
            }
        });
    }

    @Override
    public View initLayout() {
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public SettingContract.SettingPresenter initPresenter() {
        return new SettingPresenter();
    }


    public void dealClick(SideNav sideNav) {

        int id = sideNav.getId();
        if (id == 5) {

            startActivity(new Intent(SettingActivity.this, AboutActivity.class));
        } else if (id == 6) {

            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=572828703";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                Toast.makeText(MyApplication.getContext(), "请先安装QQ！", Toast.LENGTH_SHORT).show();
            }
        } else if (id == 15) {//同步数据


            if (Constant.userinfo == null) {

                startActivity(new Intent(SettingActivity.this, WxLoginActivity.class));
            } else {

                String sign = MD5.getMD5ofStr(Constant.userinfo.getUid() + DateUtil.getCurDate());
                presenter.getStudyRecordByTestMode("json", Constant.userinfo.getUid(), 1, 10, 1, sign, Constant.TYPE);
            }
        } else if (id == 20) {//更新服务

            presenter.islatest(Constant.IS_LATEST, BuildConfig.VERSION_CODE, "succinct");
        } else if (id == 10) {

            LoginUtil.logout();
            updateDrawerList();
        } else if (id == 22) {

            List<String> stringList = new ArrayList<>();
            stringList.add("原文");
            stringList.add("阅读");
            morePopup = new MorePopup(SettingActivity.this);
            morePopup.setDatas(stringList);
            morePopup.setPopupGravity(Gravity.CENTER);
            morePopup.showPopupWindow();
            morePopup.setCallback(new MorePopup.Callback() {
                @Override
                public void getItem(String s) {


                    List<SideNav> sideNavList = menuAdapter.getData();
                    for (int i = 0; i < sideNavList.size(); i++) {

                        SideNav sideNav1 = sideNavList.get(i);
                        if (sideNav1.getId() == 22) {

                            sp.edit().putString(Constant.SP_KEY_LESSON, s).apply();
                            sideNav1.setName("课文首页设置:" + s);
                            break;
                        }
                    }
                    menuAdapter.notifyDataSetChanged();
                    morePopup.dismiss();
                }
            });
        } else if (id == 23) {//反馈功能

            startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
        } else if (id == 25) {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4008881905"));
            startActivity(intent);
        }
    }


    /**
     * 更新抽屉列表
     */
    private void updateDrawerList() {

        if (menuAdapter == null) {

            return;
        }
        List<SideNav> sideNavList = menuAdapter.getData();
        for (int i = 0; i < sideNavList.size(); i++) {

            SideNav sideNav = sideNavList.get(i);
            if (sideNav.getId() == 10) {

                sideNavList.remove(i);
                break;
            }
        }
        menuAdapter.notifyDataSetChanged();
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
    public void updateApp(ResponseBody responseBody) {


        try {
            String data = responseBody.string();
            if (data.trim().equals("")) {

                return;
            }
            if (data.startsWith("NO")) {//需要升级

                String[] urlArrays = data.split("\\|\\|");
                if (urlArrays.length >= 2) {

                    String url = urlArrays[1];
                    String[] version = urlArrays[0].split(",");

                    showAlert(url, getString(R.string.app_name), version[2]);
                }
            } else {
                toast("已经是最新版了!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void showAlert(String url, String name, String version) {

        AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this)
                .setTitle("有新版！")
                .setMessage("是否要下载新版本？")
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DownloadApk.download(MyApplication.getContext(), name + version, url);
                        MyApplication.getContext().registerReceiver(new DownloadBroadcast(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
}