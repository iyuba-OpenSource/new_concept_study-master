package com.jn.iyuba.concept.simple.presenter.login;

import com.jn.iyuba.concept.simple.model.bean.BookBean;
import com.jn.iyuba.concept.simple.model.home.ChooseBookModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.ChooseBookContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class ChooseBookPresenter extends BasePresenter<ChooseBookContract.ChooseBookView, ChooseBookContract.ChooseBookModel>
        implements ChooseBookContract.ChooseBookPresenter {


    @Override
    protected ChooseBookContract.ChooseBookModel initModel() {
        return new ChooseBookModel();
    }

    @Override
    public void getTitleBySeries(String type, int category, int appid, int uid, String sign) {

        Disposable disposable = model.getTitleBySeries(type, category, appid, uid, sign, new ChooseBookContract.Callback() {
            @Override
            public void success(BookBean bookBean) {

                view.getTitleBySeries(bookBean);
            }

            @Override
            public void error(Exception e) {

                view.getTitleBySeries(null);
            }
        });
        addSubscribe(disposable);
    }
}
