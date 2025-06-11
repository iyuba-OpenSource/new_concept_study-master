package com.jn.iyuba.concept.simple.view.vip;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean2;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface VipContract {


    interface VipView extends LoadingView{


        void showQQDialog(JpQQBean jpQQBean, JpQQBean2 jpQQBean2);
    }


    interface  VipPresenter extends IBasePresenter<VipView>{

        void getQQGroup(String type);

        void getJpQQ(int appid, JpQQBean2 jpQQBean2);
    }

    interface  VipModel extends BaseModel{

        Disposable getQQGroup(String type, JpQQ2Callback callback);

        Disposable getJpQQ(int appid, JpQQCallback callback);
    }


    interface JpQQCallback {

        void success(JpQQBean jpQQBean);

        void error(Exception e);
    }

    interface JpQQ2Callback {

        void success(JpQQBean2 jpQQBean2);

        void error(Exception e);
    }
}
