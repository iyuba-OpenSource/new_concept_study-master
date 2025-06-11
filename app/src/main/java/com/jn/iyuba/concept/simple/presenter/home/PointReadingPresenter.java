package com.jn.iyuba.concept.simple.presenter.home;

import com.jn.iyuba.concept.simple.model.bean.PointReadingBean;
import com.jn.iyuba.concept.simple.model.home.PointReadingModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.PointReadingContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class PointReadingPresenter extends BasePresenter<PointReadingContract.PointReadingView, PointReadingContract.PointReadingModel>
        implements PointReadingContract.PointReadingPresenter {


    @Override
    protected PointReadingContract.PointReadingModel initModel() {
        return new PointReadingModel();
    }

    @Override
    public void textExamApi(String format, String voaid) {

        Disposable disposable = model.textExamApi(format, voaid, new PointReadingContract.Callback() {
            @Override
            public void success(PointReadingBean pointReadingBean) {

                view.textExamApi(pointReadingBean);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.textExamApi(null);
                }
            }
        });
        addSubscribe(disposable);
    }
}
