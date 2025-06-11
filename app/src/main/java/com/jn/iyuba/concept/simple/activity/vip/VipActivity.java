package com.jn.iyuba.concept.simple.activity.vip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.activity.login.LoginActivity;
import com.jn.iyuba.concept.simple.adapter.VipAdapter;
import com.jn.iyuba.succinct.databinding.ActivityVipBinding;
import com.jn.iyuba.concept.simple.entity.Vip;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean2;
import com.jn.iyuba.concept.simple.presenter.vip.VipPresenter;
import com.jn.iyuba.concept.simple.util.QQUtil;
import com.jn.iyuba.concept.simple.view.vip.VipContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会员中心
 */
public class VipActivity extends BaseActivity<VipContract.VipView, VipContract.VipPresenter>
        implements VipContract.VipView {


    private ActivityVipBinding binding;

    private VipAdapter vipAdapter;

    /**
     * 0本应用会员
     * 1黄金会员
     */
    private int flag = 0;


    /**
     * 跳转
     *
     * @param activity
     */
    public static void startActivity(Activity activity) {

        Intent intent = new Intent(activity, VipActivity.class);
        activity.startActivity(intent);
    }


    /**
     * 跳转
     *
     * @param activity
     */
    public static void startActivity(Activity activity, int flag) {

        Intent intent = new Intent(activity, VipActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("FLAG", flag);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            flag = bundle.getInt("FLAG");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle();

        binding.vipIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        List<Vip> vipList = new ArrayList<>();

        Vip vip = new Vip();
        vip.setTitle("本应用会员");
        vip.setContent("本应用会员权限说明：\n" +
                "1.非会员单词只能闯一关，会员无限制\n" +
                "2.非会员口语只能评测3句，会员无限制\n" +
                "3.会员免费导出课文PDF\n" +
                "4.会员免费调节播放语速\n" +
                "5.本应用会员仅在本APP内android端使用");

        List<Vip.VipKind> vipKindList = new ArrayList<>();
        vipKindList.add(new Vip.VipKind("本应用会员一个月", 30 + "", 1));
        vipKindList.add(new Vip.VipKind("本应用会员六个月", 69 + "", 6));
        vipKindList.add(new Vip.VipKind("本应用会员一年", 99 + "", 12));
        vipKindList.add(new Vip.VipKind("本应用会员三年", 199 + "", 36));
        vip.setVipKindList(vipKindList);
        vipList.add(vip);


        Vip vip3 = new Vip();
        vip3.setTitle("全站会员");
        vip3.setContent("1.去除广告，专注学习\n" +
                "2.显示尊贵标识\n" +
                "3.开启语速调节功能\n" +
                "4.享受高速通道、无限下载\n" +
                "5.查看考试类所有试题答案解析\n" +
                "6.智慧化语音评测\n" +
                "7.支持PDF导出\n" +
                "8.适用于爱语吧旗下所有应用");
        List<Vip.VipKind> vipKindList3 = new ArrayList<>();
        vipKindList3.add(new Vip.VipKind("全站会员一个月", 50 + "", 1));
        vipKindList3.add(new Vip.VipKind("全站会员六个月", 198 + "", 6));
        vipKindList3.add(new Vip.VipKind("全站会员一年", 298 + "", 12));
        vipKindList3.add(new Vip.VipKind("全站会员三年", 588 + "", 36));
        vip3.setVipKindList(vipKindList3);
        vipList.add(vip3);


        Vip vip1 = new Vip();
        vip1.setTitle("黄金会员");
        List<Vip.VipKind> vipKindList1 = new ArrayList<>();
        vip1.setContent(getString(R.string.vip_intro3));
        vipKindList1.add(new Vip.VipKind("黄金会员一个月", 98 + "", 1));
        vipKindList1.add(new Vip.VipKind("黄金会员三个月", 288 + "", 3));
        vipKindList1.add(new Vip.VipKind("黄金会员六个月", 518 + "", 6));
        vipKindList1.add(new Vip.VipKind("黄金会员十二个月", 998 + "", 12));
        vip1.setVipKindList(vipKindList1);
        vipList.add(vip1);


        Vip iYuBiVip = new Vip();
        iYuBiVip.setTitle("爱语币");
        iYuBiVip.setContent("");
        List<Vip.VipKind> iYuBiKindList = new ArrayList<>();
        iYuBiKindList.add(new Vip.VipKind("200送10", "19.9", 210));
        iYuBiKindList.add(new Vip.VipKind("600送50", "59.9", 650));
        iYuBiKindList.add(new Vip.VipKind("1000送100", "99.9", 1100));
        iYuBiKindList.add(new Vip.VipKind("6000送600", "599", 6600));
        iYuBiKindList.add(new Vip.VipKind("10000送2000", "999", 12000));
        iYuBiVip.setVipKindList(iYuBiKindList);
        vipList.add(iYuBiVip);

        for (int i = 0; i < vipList.size(); i++) {

            Vip v = vipList.get(i);
            TabLayout.Tab newTab = binding.vipTabTitle.newTab();
            newTab.setText(v.getTitle());
            binding.vipTabTitle.addTab(newTab);
        }
        binding.vipTabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Vip vv = vipList.get(tab.getPosition());
                binding.vipTvContent.setText(vv.getContent());

                vipAdapter.setNewData(vv.getVipKindList());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.vipTvContent.setText(vip.getContent());
        vipAdapter = new VipAdapter(R.layout.item_vip, vip.getVipKindList());
        binding.vipRvKind.setLayoutManager(new LinearLayoutManager(this));
        binding.vipRvKind.setAdapter(vipAdapter);

        vipAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                vipAdapter.setPosition(position);
                vipAdapter.notifyDataSetChanged();
            }
        });

        binding.vipButPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Constant.userinfo == null) {


                    startActivity(new Intent(VipActivity.this, LoginActivity.class));
                } else {

                    Vip selectedVip = vipList.get(binding.vipTabTitle.getSelectedTabPosition());
                    Vip.VipKind vipKind = vipAdapter.getItem(vipAdapter.getPosition());

                    String desc;
                    if (selectedVip.getTitle().equals("爱语币")) {

                        desc = "爱语币 " + vipKind.getName();
                    } else {

                        desc = vipKind.getName();
                    }

                    Intent intent = new Intent(VipActivity.this, OrderActivity.class);
                    intent.putExtra("uname", Constant.userinfo.getUsername());
                    intent.putExtra("desc", desc);
                    intent.putExtra("month", vipKind.getMonth());
                    intent.putExtra("cate", selectedVip.getTitle());
                    intent.putExtra("price", vipKind.getPrice() + "");
                    startActivity(intent);
                }
            }
        });
        //客服
        binding.vipTvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getQQGroup(Constant.TYPE);
            }
        });
        //爱语币
        binding.vipTvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo != null) {

                    Intent intent = new Intent(VipActivity.this, BuyIyuBiActivity.class);
                    intent.putExtra("uname", Constant.userinfo.getNickname());
                    startActivity(intent);
                    VipActivity.this.finish();
                } else {

                    toast("请登录");
                    startActivity(new Intent(VipActivity.this, LoginActivity.class));
                }
            }
        });


        if (flag != 0 && flag <= 3) {


            binding.vipTabTitle.getTabAt(flag).select();
        }
    }

    @Override
    public View initLayout() {

        binding = ActivityVipBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public VipContract.VipPresenter initPresenter() {
        return new VipPresenter();
    }


    @Override
    protected void onResume() {
        super.onResume();

        updateInfo();
    }

    private void updateInfo() {

        if (Constant.userinfo != null) {

            if (Constant.userinfo.getImgSrc().startsWith("http://")) {

                Glide.with(this).load(Constant.userinfo.getImgSrc()).into(binding.vipCivAvatar);
            } else {

                Glide.with(this).load(Constant.STATIC1_URL + "/uc_server/" + Constant.userinfo.getImgSrc()).into(binding.vipCivAvatar);
            }

            binding.vipTvName.setText(Constant.userinfo.getUsername());


            if (Constant.userinfo.isVip()) {

                binding.vpiTvExpireTime.setVisibility(View.VISIBLE);

                SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                simpleDateFormat.applyPattern("yyyy-MM-dd");

                String expireTime = Constant.userinfo.getExpireTime() + "";
                if (expireTime.length() == 10) {

                    expireTime = expireTime + "000";
                }
                binding.vpiTvExpireTime.setText("会员到期：" + simpleDateFormat.format(new Date(Long.parseLong(expireTime))));
            } else {

                binding.vpiTvExpireTime.setVisibility(View.GONE);
            }
        }
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
    public void showQQDialog(JpQQBean jpQQBean, JpQQBean2 jpQQBean2) {


        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), binding.vipTvContact);
        popupMenu.getMenuInflater().inflate(R.menu.menu_home_qq, popupMenu.getMenu());

        //popupMenu.getMenu().getItem(0).setTitle(String.format("日语用户群: %s", BrandUtil.getQQGroupNumber(mContext)));
        popupMenu.getMenu().getItem(0).setTitle(String.format("用户群: %s", jpQQBean2.getQq()));
        popupMenu.getMenu().getItem(1).setTitle("内容QQ: " + jpQQBean.getData().get(0).getEditor());
        popupMenu.getMenu().getItem(2).setTitle("技术QQ: " + jpQQBean.getData().get(0).getTechnician());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.qq_group:
//                        String groupKey = BrandUtil.getQQGroupKey(mContext);
                        QQUtil.startQQGroup(VipActivity.this, jpQQBean2.getKey());
                        return true;
                    case R.id.qq_server:
                        QQUtil.startQQ(VipActivity.this, jpQQBean.getData().get(0).getEditor() + "");
                        return true;
                    case R.id.qq_tech:
                        QQUtil.startQQ(VipActivity.this, jpQQBean.getData().get(0).getTechnician() + "");
                        return true;
                    case R.id.qq_customer:

                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008881905")));
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
}