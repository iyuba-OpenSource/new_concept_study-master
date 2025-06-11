package com.jn.iyuba.concept.simple.activity.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.module.toolbox.MD5;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MainActivity;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.me.HeadlineVideoActivity;
import com.jn.iyuba.concept.simple.activity.me.MyWalletActivity;
import com.jn.iyuba.concept.simple.activity.me.SettingActivity;
import com.jn.iyuba.concept.simple.activity.me.WordCollectActivity;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.util.DownloadApk;
import com.jn.iyuba.concept.simple.util.broadcast.DownloadBroadcast;
import com.jn.iyuba.succinct.BuildConfig;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.AboutActivity;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.activity.ChooseBookActivity;
import com.jn.iyuba.concept.simple.activity.MyWebActivity;
import com.jn.iyuba.concept.simple.activity.home.MyDubbingActivity;
import com.jn.iyuba.concept.simple.activity.login.WxLoginActivity;
import com.jn.iyuba.concept.simple.activity.me.MyDownloadActivity;
import com.jn.iyuba.concept.simple.activity.me.SignActivity;
import com.jn.iyuba.concept.simple.activity.vip.VipActivity;
import com.jn.iyuba.concept.simple.adapter.MenuAdapter;
import com.jn.iyuba.succinct.databinding.FragmentMeBinding;
import com.jn.iyuba.concept.simple.entity.SideNav;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncListenBean;
import com.jn.iyuba.concept.simple.presenter.me.MePresenter;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.LoginUtil;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.util.popup.LoadingPopup;
import com.jn.iyuba.concept.simple.view.me.MeContract;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;
import personal.iyuba.personalhomelibrary.ui.my.MySpeechActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryType;

/**
 * 我的
 */
public class MeFragment extends BaseFragment<MeContract.MeView, MeContract.MePresenter>
        implements MeContract.MeView {

    private FragmentMeBinding binding;
    private MenuAdapter menuAdapter;

    private LoadingPopup loadingPopup;

    public MeFragment() {
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();
        presenter.islatest(Constant.IS_LATEST, BuildConfig.VERSION_CODE, "succinct");
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        binding = FragmentMeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected MeContract.MePresenter initPresenter() {
        return new MePresenter();
    }


    @Override
    public void onResume() {
        super.onResume();

        updateInfo();

        if (Constant.userinfo != null) {
            String sign = MD5.getMD5ofStr("20001" + Constant.userinfo.getUid() + "iyubaV2");
            presenter.getMoreInfo("android", 20001, Constant.userinfo.getUid(), Constant.userinfo.getUid(), Constant.APPID, sign);
        } else {

            if (menuAdapter != null) {

                List<SideNav> sideNavList = menuAdapter.getData();
                for (int i = 0; i < sideNavList.size(); i++) {

                    SideNav sideNav = sideNavList.get(i);
                    if (sideNav.getId() == 22) {

                        sideNav.setName("我的钱包");
                        break;
                    }
                }
                menuAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 更新界面
     */
    private void updateInfo() {

        if (Constant.userinfo != null) {

            //菜单
           /* boolean isEx = false;
            List<SideNav> sideNavList = menuAdapter.getData();
            for (int i = 0; i < sideNavList.size(); i++) {

                SideNav sideNav = sideNavList.get(i);
                if (sideNav.getId() == 10) {

                    isEx = true;
                    break;
                }
            }
            if (!isEx) {

                sideNavList.add(new SideNav(10, "退出登录"));
                menuAdapter.notifyDataSetChanged();
            }*/

            if (Constant.userinfo.getImgSrc().startsWith("http://")) {

                Glide.with(requireContext()).load(Constant.userinfo.getImgSrc()).into(binding.mainCivAv);
            } else {

                Glide.with(requireContext()).load(Constant.STATIC1_URL + "/uc_server/" + Constant.userinfo.getImgSrc()).into(binding.mainCivAv);
            }

            binding.mainTvLogin.setText(Constant.userinfo.getUsername());
        } else {

            Glide.with(requireContext()).load(R.mipmap.logo).into(binding.mainCivAv);
            binding.mainTvLogin.setText("登录/注册");
        }
    }

    private void initOperation() {

        //个人中心
        binding.mainCivAv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                } else {

                    startActivity(PersonalHomeActivity.buildIntent(getContext(), Constant.userinfo.getUid(), Constant.userinfo.getUsername(), 0));
                }
            }
        });

        //打卡签到
        binding.meTvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                } else {

                    startActivity(new Intent(requireActivity(), SignActivity.class));
                }
            }
        });

        //登录
        binding.mainLlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                }
            }
        });
        //菜单
        List<SideNav> sideNavList = new ArrayList<>();
        sideNavList.add(new SideNav(9, "会员中心"));
//        sideNavList.add(new SideNav(11, "打卡签到"));
        sideNavList.add(new SideNav(22, "我的钱包"));
        sideNavList.add(new SideNav(17, "小视频"));
        sideNavList.add(new SideNav(13, "学习报告"));
        sideNavList.add(new SideNav(12, "口语圈"));
        sideNavList.add(new SideNav(14, "我的配音"));
        sideNavList.add(new SideNav(19, "我的单词"));
        sideNavList.add(new SideNav(16, "我的下载"));
        sideNavList.add(new SideNav(18, "积分商城"));
        sideNavList.add(new SideNav(21, "设置"));
        sideNavList.add(new SideNav(7, "用户协议"));
        sideNavList.add(new SideNav(8, "隐私协议"));

        menuAdapter = new MenuAdapter(R.layout.item_book, sideNavList);
        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                SideNav sideNav = menuAdapter.getItem(position);
                if (sideNav != null) {

                    dealClick(sideNav);
                }
            }
        });
        binding.mainRvBook.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.mainRvBook.setAdapter(menuAdapter);
    }


    public void dealClick(SideNav sideNav) {

        int id = sideNav.getId();
        if (id == 5) {

            startActivity(new Intent(requireActivity(), AboutActivity.class));
        } else if (id == 6) {

            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=572828703";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                Toast.makeText(MyApplication.getContext(), "请先安装QQ！", Toast.LENGTH_SHORT).show();
            }
        } else if (id == 7) {

            MyWebActivity.startActivity(requireActivity(), Constant.URL_PROTOCOLUSE, "用户协议");
        } else if (id == 8) {

            MyWebActivity.startActivity(requireActivity(), Constant.URL_PROTOCOLPRI, "隐私政策");
        } else if (id == 9) {//会员中心

            startActivity(new Intent(requireActivity(), VipActivity.class));
        } else if (id == 10) {

            LoginUtil.logout();
            updateInfo();
        } else if (id == 11) {

            if (Constant.userinfo == null) {

                startActivity(new Intent(requireActivity(), WxLoginActivity.class));
            } else {

                startActivity(new Intent(requireActivity(), SignActivity.class));
            }

        } else if (id == 12) {

            startActivity(MySpeechActivity.buildIntent(getContext()));
        } else if (id == 13) {

            if (Constant.userinfo == null) {

                startActivity(new Intent(requireActivity(), WxLoginActivity.class));
            } else {

                String[] types = new String[]{
                        SummaryType.LISTEN,
                        SummaryType.EVALUATE,
                        //SummaryType.WORD,
//                        SummaryType.TEST,
                        //SummaryType.MOOC,
//                    SummaryType.READ
                };
                startActivity(SummaryActivity.getIntent(requireActivity(), types, 0));
//                startActivity(SummaryActivity.getIntent(getContext(), Constant.TYPE, types, 0));//10 PersonalType.NEWS
            }
        } else if (id == 14) {//我的配音

            if (Constant.userinfo == null) {

                startActivity(new Intent(requireActivity(), WxLoginActivity.class));
            } else {

                startActivity(new Intent(requireActivity(), MyDubbingActivity.class));
            }
        } else if (id == 15) {//同步数据


            if (Constant.userinfo == null) {

                startActivity(new Intent(requireActivity(), WxLoginActivity.class));
            } else {

                String sign = MD5.getMD5ofStr(Constant.userinfo.getUid() + DateUtil.getCurDate());
                presenter.getStudyRecordByTestMode("json", Constant.userinfo.getUid(), 1, 10, 1, sign, Constant.TYPE);

            /*    String sign = MD5Util.MD5(Constant.userinfo.getUid() + DateUtil.getCurDate());
                presenter.getTestRecordDetail(Constant.APPID + "", Constant.userinfo.getUid() + "", "10"
                        , sign, "json", 1 + "", "10");*/
            }
        } else if (id == 16) {

            startActivity(new Intent(requireActivity(), MyDownloadActivity.class));
        } else if (id == 17) {

            startActivity(new Intent(requireActivity(), HeadlineVideoActivity.class));
        } else if (id == 18) {

            if (Constant.userinfo == null) {

                startActivity(new Intent(requireActivity(), WxLoginActivity.class));
            } else {

                String username = Constant.userinfo.getUsername();
                String url = Constant.URL_M + "/mall/index.jsp?"
                        + "&uid=" + Constant.userinfo.getUid()
                        + "&sign=" + MD5.getMD5ofStr("iyuba" + Constant.userinfo.getUid() + "camstory")
                        + "&username=" + username
                        + "&platform=android&appid="
                        + Constant.APPID;
//                IntegralShopActivity.startActivity(getActivity(), url, "积分商城");
                MyWebActivity.startActivity(requireActivity(), url, "积分商城");
            }

        } else if (id == 19) {

            if (Constant.userinfo == null) {

                startActivity(new Intent(requireActivity(), WxLoginActivity.class));
            } else {

                startActivity(new Intent(requireActivity(), WordCollectActivity.class));
            }
        } else if (id == 20) {//更新服务

            presenter.islatest(Constant.IS_LATEST, BuildConfig.VERSION_CODE, "succinct");
        } else if (id == 21) {//设置

            startActivity(new Intent(requireActivity(), SettingActivity.class));
        } else if (id == 22) {

            if (Constant.userinfo == null) {

                startActivity(new Intent(requireActivity(), WxLoginActivity.class));
            } else {

                startActivity(new Intent(requireActivity(), MyWalletActivity.class));
            }
        } else {

            startActivity(new Intent(requireActivity(), ChooseBookActivity.class));
        }
    }

    @Override
    public void showLoading(String msg) {

        if (loadingPopup == null) {

            loadingPopup = new LoadingPopup(requireActivity());
        }
        loadingPopup.setContent(msg);
        loadingPopup.showPopupWindow();
    }

    @Override
    public void hideLoading() {

        if (loadingPopup != null) {

            loadingPopup.dismiss();
        }
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getStudyRecordByTestMode(SyncListenBean syncListenBean) {

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

    @Override
    public void moreInfoComplete(MoreInfoBean moreInfoBean) {

        if (menuAdapter != null) {

            List<SideNav> sideNavList = menuAdapter.getData();
            for (int i = 0; i < sideNavList.size(); i++) {

                SideNav sideNav = sideNavList.get(i);
                if (sideNav.getId() == 22) {

                    DecimalFormat decimalFormat = new DecimalFormat("###.##");
                    sideNav.setName("我的钱包:" + decimalFormat.format(moreInfoBean.getMoney() / 100.0f) + "元");
                    break;
                }
            }
            menuAdapter.notifyDataSetChanged();
        }
    }


    private void showAlert(String url, String name, String version) {

        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
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