package com.jn.iyuba.concept.simple.view.home;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public interface DubbingContract {

    interface DubbingView extends LoadingView {


        void testComplete(EvalBean evalBean, String idindex, int paraId);
    }

    interface DubbingPresenter extends IBasePresenter<DubbingView> {

        void test(RequestBody body, String idindex, String paraid);
    }

    interface DubbingModel extends BaseModel {

        Disposable test(RequestBody body, EvaluatingContract.EvalCallback callback);
    }


    interface EvalCallback {

        void success(EvalBean evalBean);

        void error(Exception e);
    }
}
