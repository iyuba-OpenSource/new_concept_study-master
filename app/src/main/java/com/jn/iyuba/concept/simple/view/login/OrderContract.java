package com.jn.iyuba.concept.simple.view.login;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.AlipayOrderBean;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.PayResultBean;
import com.jn.iyuba.concept.simple.model.bean.WXOrderBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface OrderContract {


    interface OrderView extends LoadingView {

        /**
         * 获取到了支付宝订单
         *
         * @param alipayOrderBean
         */
        void getAlipayOrder(AlipayOrderBean alipayOrderBean);

        /**
         * 更新订单成功
         */
        void notifyAliNewComplete();


        /**
         * 成功获取更多数据
         *
         * @param moreInfoBean
         */
        void moreInfoComplete(MoreInfoBean moreInfoBean);


        /**
         * 微信订单
         *
         * @param wxOrderBean
         */
        void getWXOrder(WXOrderBean wxOrderBean);

    }

    interface OrderPresenter extends IBasePresenter<OrderView> {

        void alipayOrder(int app_id, int userId, String code, String WIDtotal_fee, int amount,
                         int product_id, String WIDbody, String WIDsubject);

        void notifyAliNew(String data);

        void getMoreInfo(String platform, int protocol, int id, int myid,
                         int appid, String sign);

        void weixinPay(String wxkey, String appid, String weixinApp, String uid, String money,
                       String amount, String productid, String sign, String body, String format);
    }

    interface OrderModel extends BaseModel {

        Disposable alipayOrder(int app_id, int userId, String code, String WIDtotal_fee, int amount,
                               int product_id, String WIDbody, String WIDsubject, Callback callback);

        Disposable notifyAliNew(String data, PayResultCallback payResultCallback);

        Disposable getMoreInfo(String platform, int protocol, int id, int myid,
                               int appid, String sign, MoreInfoCallback moreInfoCallback);

        Disposable weixinPay(String wxkey, String appid, String weixinApp, String uid, String money,
                             String amount, String productid, String sign, String body, String format, WXCallback callback);
    }

    interface WXCallback {

        void success(WXOrderBean wxOrderBean);

        void error(Exception e);
    }

    interface Callback {

        void success(AlipayOrderBean alipayOrderBean);

        void error(Exception e);
    }

    interface PayResultCallback {

        void success(PayResultBean payResultBean);

        void error(Exception e);
    }

    interface MoreInfoCallback {

        void success(MoreInfoBean moreInfoBean);

        void error(Exception e);
    }
}
