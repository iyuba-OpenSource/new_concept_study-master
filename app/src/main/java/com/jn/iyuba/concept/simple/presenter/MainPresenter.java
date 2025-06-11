package com.jn.iyuba.concept.simple.presenter;

import com.jn.iyuba.concept.simple.model.MainModel;
import com.jn.iyuba.concept.simple.model.bean.ProcessBean;
import com.jn.iyuba.concept.simple.view.MainContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<MainContract.MainView, MainContract.MainModel>
        implements MainContract.MainPresenter {


    @Override
    protected MainContract.MainModel initModel() {
        return new MainModel();
    }

    @Override
    public void getRegisterAll(String appId, String appVersion) {

        Disposable disposable = model.getRegisterAll(appId, appVersion, new MainContract.Callback() {
            @Override
            public void success(ProcessBean processBean) {

                view.getRegisterAll(processBean);
            }

            @Override
            public void error(Exception e) {

                ProcessBean processBean = new ProcessBean();
                processBean.setResult("1");
                view.getRegisterAll(processBean);
            }
        });

        addSubscribe(disposable);
    }
}
