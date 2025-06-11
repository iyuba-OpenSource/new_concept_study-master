package com.jn.iyuba.concept.simple.view.login;



import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.WxLoginBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface WxLoginContract {

    interface WxLoginView extends LoadingView {


        void getWxAppletToken(WxLoginBean wxLoginBean);

        void getUidByToken(WxLoginBean wxLoginBean);

        void getUserInfo(MoreInfoBean userInfoResponse, String uid);
    }


    interface WxLoginPresenter extends IBasePresenter<WxLoginView> {

        void getWxAppletToken(String platform, String format, String protocol, String appid, String sign);

        void getUidByToken(String platform, String format, String protocol, String token, String sign, String appid);

        void getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign);
    }

    interface WxLoginModel extends BaseModel {

        Disposable getWxAppletToken(String platform, String format, String protocol, String appid, String sign, Callback callback);

        Disposable getUidByToken(String platform, String format, String protocol, String token, String sign, String appid, Callback callback);


        Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign, UserCallback callback);
    }

    interface UserCallback {

        void success(MoreInfoBean userInfoResponse);

        void error(Exception e);
    }

    interface Callback {

        void success(WxLoginBean wxLoginBean);

        void error(Exception e);
    }
}
