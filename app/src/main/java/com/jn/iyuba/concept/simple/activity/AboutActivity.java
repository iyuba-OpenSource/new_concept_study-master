package com.jn.iyuba.concept.simple.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.util.PackageUtil;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.SplashActivity;
import com.jn.iyuba.concept.simple.activity.login.LoginActivity;
import com.jn.iyuba.succinct.databinding.ActivityAboutBinding;
import com.jn.iyuba.concept.simple.model.bean.LogoffBean;
import com.jn.iyuba.concept.simple.presenter.login.AboutPresenter;
import com.jn.iyuba.concept.simple.util.LoginUtil;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.view.login.AboutContract;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AboutActivity extends BaseActivity<AboutContract.AboutView, AboutContract.AboutPresenter>
        implements AboutContract.AboutView {


    private ActivityAboutBinding binding;

    private ProgressDialog progressDialog;

    String intro = "一款通过语音评测技术，提升您英语水平的实用APP。\n" +
            "\n" +
            "【核心功能】\n" +
            "1.口语测评\n" +
            "\n" +
            "该app采用先进口语评测技术, 通过对使用者英语发音情况分析（单词音标分析）,反馈口语发音成绩,起到纠正发音、提升英语口语水平的目的。\n" +
            "\n" +
            "2.经典的英语学习资源\n" +
            "\n" +
            "新概念经典的双语听力教材资源，涵盖英语单词、语法、听力、口语等英语学习核心功能集，初学者更容易入门。\n" +
            "\n" +
            "【适合人群】\n" +
            "\n" +
            "1.适合7-20岁年龄段用户。\n" +
            "\n" +
            "2.英语基础薄弱、英语听力差、口语差的用户。\n" +
            "\n" +
            "3.课堂外，需要巩固自身英语学习的用户。\n" +
            "\n" +
            "4.想要提高英语考试分数的用户。\n" +
            "\n" +
            "【服务宗旨】\n" +
            "\n" +
            "1.全方位提升用户的英语听力、口语、语法能力，经过系统学习后，能够考出高分。\n" +
            "\n" +
            "2.旨在引领不同水平的英语学习者，在使用app学习后，能够对英语不再恐惧，更加轻松应对英语日常听、说、读、写。\n" +
            "\n" +
            "3.我们致力于提供一个健康、便捷的学英语的线上平台，助力英语成绩薄弱者，让您轻松应对考试，和外国友人沟通可以对答如流，说一口流利的英语。\n" +
            "\n" +
            "4.如果您有任何疑问，请与我们联系，邮箱：iyuba@sina.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intro = "【" + PackageUtil.getAppName(this) + "】  " + intro;


        binding.aboutTvIntro.setText(intro);
        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        binding.toolbar.toolbarIvTitle.setText("关于我们");
        binding.toolbar.toolbarIvRight.setVisibility(View.INVISIBLE);

        binding.aboutCvLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(AboutActivity.this, LoginActivity.class));
                } else {

                    showWarning();
                }
            }
        });
    }

    @Override
    public View initLayout() {
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public AboutContract.AboutPresenter initPresenter() {
        return new AboutPresenter();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

        if (progressDialog != null) {

            progressDialog.dismiss();
        }
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 是否要注销的弹窗
     */
    private void showWarning() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
        builder.setTitle("提示");
        View diaView = getLayoutInflater().inflate(R.layout.dialog_logoff, null);
        builder.setView(diaView);
        builder.setPositiveButton("继续注销", (dialog, which) -> PressPwd());
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    /**
     * 注销  输入密码的弹窗
     */
    private void PressPwd() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
        builder.setTitle("输入密码注销账号");
        final View diaView2 = getLayoutInflater().inflate(R.layout.dialog_logoff2, null);
        final EditText et = diaView2.findViewById(R.id.logout_pwd);
        builder.setView(diaView2);
        builder.setPositiveButton("确认注销", (dialog, which) -> {

            String username = Constant.userinfo.getUsername();
            String pressPwd = et.getText().toString();
            String signStr = MD5Util.MD5("11005" + username + MD5Util.MD5(pressPwd) + "iyubaV2");
            progressDialog = ProgressDialog.show(AboutActivity.this, "注销中...", "", true);

            //请求注销
            try {
                presenter.logoff(11005, URLEncoder.encode(username, "utf-8"), MD5Util.MD5(pressPwd), "json", signStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

    @Override
    public void logoffComplete(LogoffBean logoffBean) {


        LoginUtil.logout();
    }
}