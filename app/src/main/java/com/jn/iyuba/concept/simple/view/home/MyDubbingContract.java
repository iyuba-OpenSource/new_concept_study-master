package com.jn.iyuba.concept.simple.view.home;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.DelEvalBean;
import com.jn.iyuba.concept.simple.model.bean.MyDubbingBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface MyDubbingContract {

    interface MyDubbingView extends LoadingView {

        void getMyDubbingComplete(MyDubbingBean myDubbingBean);

        void delEvalComplete(int id);

    }

    interface MyDubbingPresenter extends IBasePresenter<MyDubbingView> {

        void getTalkShowOtherWorks(int appid, String uid, String appname);

        void delEvalAndDubbing(int protocol, int id);
    }

    interface MyDubbingModel extends BaseModel {

        Disposable getTalkShowOtherWorks(int appid, String uid, String appname, Callback callback);

        Disposable delEvalAndDubbing(int protocol, int id, DelCallback delCallback);
    }

    interface Callback {

        void success(MyDubbingBean myDubbingBeans);

        void error(Exception e);
    }

    interface DelCallback {

        void success(DelEvalBean delEvalBean);

        void error(Exception e);
    }
}
