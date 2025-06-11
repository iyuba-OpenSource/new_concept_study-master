package com.jn.iyuba.concept.simple.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.activity.MyWebActivity;
import com.jn.iyuba.succinct.databinding.ActivityLoginBinding;
import com.jn.iyuba.concept.simple.model.bean.UserBean;
import com.jn.iyuba.concept.simple.presenter.login.LoginPresenter;
import com.jn.iyuba.concept.simple.util.LoginUtil;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.util.popup.LoadingPopup;
import com.jn.iyuba.concept.simple.view.login.LoginContract;


public class LoginActivity extends BaseActivity<LoginContract.LoginView, LoginContract.LoginPresenter>
        implements LoginContract.LoginView {

    private ActivityLoginBinding activityLoginBinding;
    private SharedPreferences sp;

    private LoadingPopup loadingPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sp = getSharedPreferences(Constant.SP_USER, MODE_PRIVATE);
        loadingPopup = new LoadingPopup(this);

        initOperation();
    }

    private void initOperation() {

        activityLoginBinding.toolbar.toolbarIvRight.setVisibility(View.GONE);
        activityLoginBinding.toolbar.toolbarIvTitle.setText("登录");
        activityLoginBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        activityLoginBinding.loginButLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String account = activityLoginBinding.loginEtAccount.getText().toString();
                String password = activityLoginBinding.loginEtPassword.getText().toString();

                if (account.trim().equals("")) {

                    toast("请输入账号");
                    return;
                }
                if (password.trim().equals("")) {

                    toast("请输入密码");
                    return;
                }

                if (!activityLoginBinding.loginCb.isChecked()) {

                    toast("请勾选协议");
                    return;
                }
                String md5_pwd = MD5Util.MD5(password);
                String sign = MD5Util.MD5(11001 + account + md5_pwd + "iyubaV2");
                presenter.login(11001, Constant.APPID, account, md5_pwd, 0, 0, sign);
            }
        });
        activityLoginBinding.loginButRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        String priStr = "我已阅读并同意使用条款和隐私政策";
        SpannableString spannableString = new SpannableString(priStr);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                MyWebActivity.startActivity(LoginActivity.this, Constant.URL_PROTOCOLUSE, "用户协议");
            }
        }, priStr.indexOf("使用条款"), priStr.indexOf("使用条款") + "使用条款".length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                MyWebActivity.startActivity(LoginActivity.this, Constant.URL_PROTOCOLPRI, "隐私政策");
            }
        }, priStr.indexOf("隐私政策"), priStr.indexOf("隐私政策") + "隐私政策".length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        //协议
        activityLoginBinding.loginTvPri.setText(spannableString);
        activityLoginBinding.loginTvPri.setMovementMethod(LinkMovementMethod.getInstance());
        activityLoginBinding.loginTvPri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activityLoginBinding.loginCb.isChecked()) {

                    activityLoginBinding.loginCb.setChecked(false);
                } else {

                    activityLoginBinding.loginCb.setChecked(true);
                }
            }
        });
        activityLoginBinding.loginTvPri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activityLoginBinding.loginCb.isChecked()) {

                    activityLoginBinding.loginCb.setChecked(false);
                } else {

                    activityLoginBinding.loginCb.setChecked(true);
                }
            }
        });

        activityLoginBinding.loginTvForget.setText(Html.fromHtml(
                "<a href=\"http://m." + Constant.DOMAIN2 + "/m_login/inputPhonefp.jsp?\">忘记密码?</a>",Html.FROM_HTML_MODE_LEGACY));
        activityLoginBinding.loginTvForget.setMovementMethod(new LinkMovementMethod());
    }

    @Override
    public View initLayout() {
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        return activityLoginBinding.getRoot();
    }

    @Override
    public LoginContract.LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showLoading(String msg) {

        loadingPopup.setContent(msg);
        loadingPopup.showPopupWindow();
    }

    @Override
    public void hideLoading() {

        loadingPopup.dismiss();
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginComplete(UserBean.UserinfoDTO userBean) {

        LoginUtil.login(userBean, sp);
        finish();
    }
}