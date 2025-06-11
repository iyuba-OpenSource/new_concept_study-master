package com.jn.iyuba.concept.simple.presenter.home;


import com.jn.iyuba.concept.simple.model.bean.EvalBean;
import com.jn.iyuba.concept.simple.model.home.DubbingModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.DubbingContract;
import com.jn.iyuba.concept.simple.view.home.EvaluatingContract;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public class DubbingPresenter extends BasePresenter<DubbingContract.DubbingView, DubbingContract.DubbingModel>
        implements DubbingContract.DubbingPresenter {


    @Override
    protected DubbingContract.DubbingModel initModel() {
        return new DubbingModel();
    }


    @Override
    public void test(RequestBody body, String idindex, String paraid) {

        Disposable disposable = model.test(body, new EvaluatingContract.EvalCallback() {
            @Override
            public void success(EvalBean evalBean) {

                if (evalBean.getResult().equals("1")) {

                    view.testComplete(evalBean, idindex, Integer.parseInt(paraid));
                } else {

                    view.toast(evalBean.getMessage());
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }
}
