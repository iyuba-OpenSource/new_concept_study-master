package com.jn.iyuba.concept.simple.view.login;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.UserBean;
import com.jn.iyuba.concept.simple.model.bean.VCodeBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface RegisterContract {

    interface RegisterView extends LoadingView {


        void getVCodeComplete(boolean b);

        void registerComplete(UserBean.UserinfoDTO userinfoDTO);
    }

    interface RegisterPresenter extends IBasePresenter<RegisterView> {


        void register(int protocol, String mobile, String username, String password, String platform,
                      int appid, String app, String format, String sign);
    }

    interface RegisterModel extends BaseModel {



        Disposable register(int protocol, String mobile, String username, String password, String platform,
                            int appid, String app, String format, String sign, UserCallback userCallback);
    }

    interface VCodeCallback {

        void success(VCodeBean vCodeBean);

        void error(Exception e);

    }

    interface UserCallback {

        void success(UserBean.UserinfoDTO userinfoDTO);

        void error(Exception e);
    }
}
