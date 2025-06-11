package com.jn.iyuba.concept.simple.view.me;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.MyTimeBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface MyTimeContract {


    interface MyTimeView extends LoadingView {


        void getMyTimeComplete(MyTimeBean myTimeBean);

        void updateScore(ScoreBean scoreBean);
    }

    interface MyTimePresenter extends IBasePresenter<MyTimeView> {

        void getMyTime(String uid, int day, int flg);

        void updateScore(int srid, int mobile, String flag, String uid, int appid);
    }

    interface MyTimModel extends BaseModel {

        Disposable getMyTime(String uid, int day, int flg, MyTimeCallback myTimeCallback);

        Disposable updateScore(int srid, int mobile, String flag, String uid, int appid, ScoreCallback callback);
    }

    interface ScoreCallback {

        void success(ScoreBean scoreBean);

        void error(Exception e);
    }

    interface MyTimeCallback {

        void success(MyTimeBean myTimeBean);

        void error(Exception e);
    }
}
