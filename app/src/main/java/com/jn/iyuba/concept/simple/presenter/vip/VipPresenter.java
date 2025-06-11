package com.jn.iyuba.concept.simple.presenter.vip;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean;
import com.jn.iyuba.concept.simple.model.bean.JpQQBean2;
import com.jn.iyuba.concept.simple.model.vip.VipModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.vip.VipContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class VipPresenter extends BasePresenter<VipContract.VipView, VipContract.VipModel>
        implements VipContract.VipPresenter {


    @Override
    protected VipContract.VipModel initModel() {
        return new VipModel();
    }

    @Override
    public void getQQGroup(String type) {

        Disposable disposable = model.getQQGroup(type, new VipContract.JpQQ2Callback() {
            @Override
            public void success(JpQQBean2 jpQQBean2) {

                if (jpQQBean2.getMessage().equals("true")) {

                    //获取客服qq
                    getJpQQ(Constant.APPID, jpQQBean2);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getJpQQ(int appid, JpQQBean2 jpQQBean2) {

        Disposable disposable = model.getJpQQ(appid, new VipContract.JpQQCallback() {
            @Override
            public void success(JpQQBean jpQQBean) {

                if (jpQQBean.getResult() == 200) {

                    view.showQQDialog(jpQQBean, jpQQBean2);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
