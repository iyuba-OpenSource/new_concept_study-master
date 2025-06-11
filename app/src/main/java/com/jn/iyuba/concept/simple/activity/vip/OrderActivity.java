package com.jn.iyuba.concept.simple.activity.vip;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.iyuba.module.toolbox.MD5;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MainActivity;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.entity.WxPayEvent;
import com.jn.iyuba.succinct.BuildConfig;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.succinct.databinding.ActivityOrderBinding;
import com.jn.iyuba.concept.simple.model.bean.AlipayOrderBean;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.WXOrderBean;
import com.jn.iyuba.concept.simple.presenter.vip.OrderPresenter;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.util.popup.LoadingPopup;
import com.jn.iyuba.concept.simple.view.login.OrderContract;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * 支付页，微信支付不可用
 */
public class OrderActivity extends BaseActivity<OrderContract.OrderView, OrderContract.OrderPresenter>
        implements View.OnClickListener, OrderContract.OrderView {

    public static final int PAY_FOR_WX = 1;
    public static final int PAY_FOR_ZFB = 2;

    public static final int NET_ERROR = -1;
    public static final int REQUEST_WX_INFO_FINISH = 3;

    /**
     * 会员类型
     */
    private String cate;
    private String price;
    private int amount;
    private String productId;


    private static final String TAG = "OrderActivity";

    private long clickTime = 0;  // 上一次点击支付按钮的时间

    private ActivityOrderBinding activityOrderBinding;

    private LoadingPopup loadingPopup;

    private final String wxKey = "wxf98d71b9618b19f3";
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        iwxapi = WXAPIFactory.createWXAPI(this, wxKey, true);
        iwxapi.registerApp(wxKey);
        bindView();
        initData();

        if (price == null || price.equals("")) {

            startActivity(new Intent(OrderActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        showAlert();
    }

    /**
     * 获取微信支付状态
     *
     * @param wxPayEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WxPayEvent wxPayEvent) {

        hideLoading();
        if (wxPayEvent.getErrcode() == 0) {

            showAlert();
        }
    }

    /**
     * 当从微信支付页面返回时触发
     */
    private void showAlert() {

        AlertDialog alertDialog = new AlertDialog.Builder(OrderActivity.this)
                .setTitle("提示")
                .setMessage("是否已支付")
                .setNegativeButton("未支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("已支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int uid = Constant.userinfo.getUid();
                        String signStr = "20001" + uid + "iyubaV2";
                        String sign = MD5Util.MD5(signStr);
//                        orderPresenter.getMoreInfo("android", 20001, Integer.parseInt(uid), Integer.parseInt(uid),
//                                Integer.parseInt(getString(R.string.appid)), sign);

                        presenter.getMoreInfo("android", 20001, uid, uid, Constant.APPID, sign);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public View initLayout() {

        activityOrderBinding = ActivityOrderBinding.inflate(getLayoutInflater());
        return activityOrderBinding.getRoot();
    }

    @Override
    public OrderContract.OrderPresenter initPresenter() {
        return new OrderPresenter();
    }

    private void initData() {
        Intent intent = getIntent();
        cate = intent.getStringExtra("cate");
        price = intent.getStringExtra("price");
        amount = intent.getIntExtra("month", 0);
        Log.d(TAG, "initData:  ======== 开通月数" + amount);
        if ("本应用会员".equals(cate)) {
            productId = "10";
        } else if ("全站会员".equals(cate)) {
            productId = "0";
        } else if ("黄金会员".equals(cate)) {
            productId = "21";
        } else if ("爱语币".equals(cate)) {
            productId = "1";
        }

        activityOrderBinding.orderUser.setText(intent.getStringExtra("uname"));
        String str1 = "购买" + intent.getStringExtra("desc");
        activityOrderBinding.orderDesc.setText(str1);
        String str2 = price + "元";
        activityOrderBinding.orderPrice.setText(str2);
    }

    private void bindView() {

        activityOrderBinding.toolbar.toolbarIvRight.setVisibility(View.GONE);
        activityOrderBinding.toolbar.toolbarIvTitle.setText("支付");
        activityOrderBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        activityOrderBinding.orderPayMethod1.setOnClickListener(this);
        activityOrderBinding.orderPayMethod2.setOnClickListener(this);
        activityOrderBinding.orderRb1.setOnClickListener(this);
        activityOrderBinding.orderRb2.setOnClickListener(this);
        activityOrderBinding.payBtn.setOnClickListener(this);
    }

    // 清空所有选项选中状态
    void setAllCheckFalse() {
        activityOrderBinding.orderRb1.setChecked(false);
        activityOrderBinding.orderRb2.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.order_pay_method1 || id == R.id.order_rb1) {
            setAllCheckFalse();
            activityOrderBinding.orderRb1.setChecked(true);
        } else if (id == R.id.order_pay_method2 || id == R.id.order_rb2) {
            setAllCheckFalse();
            activityOrderBinding.orderRb2.setChecked(true);
        } else if (id == R.id.pay_btn) {

            // 避免频繁发起订单，设置支付按钮点击，三秒生效一次
            if (System.currentTimeMillis() - clickTime > 3000) {
                if (activityOrderBinding.orderRb2.isChecked()) {//支付宝支付

                    payForZFB();
                } else {
//                     payForWX();
                    getWxOrderInfo();
//                    payForWXWeb();
                }
            }
        }
    }

    // 微信支付
    private void getWxOrderInfo() {

        if (iwxapi.isWXAppInstalled()) {

            showLoading("请稍后");
            try {

                if (BuildConfig.DEBUG) {
                    price = "0.01";
                }
                presenter.weixinPay(wxKey, Constant.APPID + "", wxKey, Constant.userinfo.getUid() + "", price,
                        amount + "", productId, generateSign(Constant.APPID + "", Constant.userinfo.getUid() + "",
                                price, String.valueOf(amount)), URLEncoder.encode("花费" + price + "元购买" + cate, StandardCharsets.UTF_8.toString()), "json");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {

            Toast.makeText(MyApplication.getContext(), "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
        }
    }

    // 微信支付(网页支付)
/*    private void payForWXWeb() {

        String url = "http://www.ibbc.net.cn/wcp/awcp.html?";
        int days = (int) (System.currentTimeMillis() / 1000 / (60 * 60 * 24));
        System.out.println(System.currentTimeMillis() + "===========days" + days);
        String sign = MD5Util.MD5("iyubaPay" + price + amount + productId + days);
        url = url + "uid=" + Constant.userinfo.getUid()
                + "&money=" + price
                + "&amount=" + amount
                + "&appid=" + Constant.APPID
                + "&productid=" + productId
                + "&weichatId=" + "wx64dc69e044316028"
                + "&sign=" + sign;
        System.out.println("===========url" + url);
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }*/

    // 支付宝支付
    private void payForZFB() {

        // 拼接code
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
        sdf.applyPattern("yyyy-MM-dd");
        String code = MD5Util.MD5(Constant.userinfo.getUid() + "iyuba" + sdf.format(System.currentTimeMillis()));


        // 拼接WID_body
        String WID_body = null;
        try {

            if (Constant.IS_PAY_DEBUG && !productId.equals("1")) {
                price = 0.01 + "";
                WID_body = URLEncoder.encode("花费" + price + "元购买" + cate, "utf-8");
            } else {

                WID_body = URLEncoder.encode("花费" + price + "元购买" + cate, "utf-8");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            showLoading("稍等片刻");
            presenter.alipayOrder(Constant.APPID, Constant.userinfo.getUid(), code, price, amount,
                    Integer.parseInt(productId), WID_body, URLEncoder.encode(cate, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // 微信支付拉取订单信息需要的sign
    private static String generateSign(String appid, String uid, String money, String amount) {

        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
        sdf.applyPattern("yyyyMMdd");
        String sb = appid + uid + money + amount + sdf.format(System.currentTimeMillis());
        return MD5Util.MD5(sb);
    }

    // 登录成功获取用户会员信息
    private void refreshUserInfo() {

        String signStr = "20001" + Constant.userinfo.getUid() + "iyubaV2";
        String sign = MD5Util.MD5(signStr);

        presenter.getMoreInfo("android", 20001, Constant.userinfo.getUid(), Constant.userinfo.getUid(), Constant.APPID, sign);
    }

    @Override
    public void showLoading(String msg) {

        if (loadingPopup == null) {

            loadingPopup = new LoadingPopup(OrderActivity.this);
            loadingPopup.setOutSideDismiss(false);
        }
        loadingPopup.setContent(msg);
        loadingPopup.showPopupWindow();
    }

    @Override
    public void hideLoading() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (loadingPopup != null) {

                    loadingPopup.dismiss();
                }
            }
        });
    }

    @Override
    public void toast(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getAlipayOrder(AlipayOrderBean alipayOrderBean) {

        //必须放在子线程中
        new Thread(new Runnable() {
            @Override
            public void run() {

                PayTask alipay = new PayTask(OrderActivity.this);
                Map<String, String> result = alipay.payV2(alipayOrderBean.getAlipayTradeStr(), true);

                if (result.get("resultStatus").equals("9000")) {
                    presenter.notifyAliNew(result.toString());
                } else {
                    toast(result.get("memo"));
                    hideLoading();
                }
            }
        }).start();
    }

    @Override
    public void notifyAliNewComplete() {

        hideLoading();
        runOnUiThread(() -> {

            if (!"1".equals(productId)) {//爱语币

                toast("开通成功！若未生效重新登陆即可。");
            } else {

                toast("购买成功！若未生效重新登陆即可。");
            }
        });
        refreshUserInfo();
    }

    @Override
    public void moreInfoComplete(MoreInfoBean moreInfoBean) {

        Constant.userinfo.setVipStatus(moreInfoBean.getVipStatus());
        Constant.userinfo.setExpireTime(moreInfoBean.getExpireTime());
        Constant.userinfo.setAmount(moreInfoBean.getAmount() + "");

        SharedPreferences sp = getSharedPreferences(Constant.SP_USER, MODE_PRIVATE);
        sp.edit().putString(Constant.SP_KEY_USER_INFO, new Gson().toJson(Constant.userinfo)).apply();

        User user = new User();
        user.name = Constant.userinfo.getUsername();
        user.uid = Constant.userinfo.getUid();
        user.vipStatus = Constant.userinfo.getVipStatus();
        if (String.valueOf(Constant.userinfo.getExpireTime()).length() == 10) {

            user.vipExpireTime = Constant.userinfo.getExpireTime() * 1000l;
        } else {

            user.vipExpireTime = Constant.userinfo.getExpireTime();
        }
        IyuUserManager.getInstance().setCurrentUser(user);

//        User user = IyuUserManager.getInstance().getCurrentUser();
//        user.vipStatus = moreInfoBean.getVipStatus();
//        user.vipExpireTime = moreInfoBean.getExpireTime();
//        user.iyubiAmount = moreInfoBean.getAmount();
        //personal
//        PersonalHome.setSaveUserinfo(Constant.userinfoDTO.getUid(), Constant.userinfoDTO.getUsername(), Constant.userinfoDTO.getVipStatus());

        OrderActivity.this.finish();
    }

    @Override
    public void getWXOrder(WXOrderBean wxOrderBean) {

        PayReq request = new PayReq();

        request.appId = wxOrderBean.getAppid();

        request.partnerId = wxOrderBean.getMchId();

        request.prepayId = wxOrderBean.getPrepayid();

        request.packageValue = "Sign=WXPay";

        request.nonceStr = wxOrderBean.getNoncestr();

        request.timeStamp = wxOrderBean.getTimestamp();

        request.sign = buildWeixinSign(request, wxOrderBean.getMchKey());
        iwxapi.sendReq(request);
    }


    private String buildWeixinSign(PayReq payReq, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildWeixinStringA(payReq));
        sb.append("&key=").append(key);
        Log.i(TAG, sb.toString());
        return MD5.getMD5ofStr(sb.toString()).toUpperCase();
    }

    /**
     * 老版微信支付的sign，与现在的拼接方式不同，接口给的sign没什么用
     *
     * @param payReq
     * @return
     */
    private String buildWeixinStringA(PayReq payReq) {
        StringBuilder sb = new StringBuilder();
        sb.append("appid=").append(payReq.appId);
        sb.append("&noncestr=").append(payReq.nonceStr);
        sb.append("&package=").append(payReq.packageValue);
        sb.append("&partnerid=").append(payReq.partnerId);
        sb.append("&prepayid=").append(payReq.prepayId);
        sb.append("&timestamp=").append(payReq.timeStamp);
        return sb.toString();
    }
}