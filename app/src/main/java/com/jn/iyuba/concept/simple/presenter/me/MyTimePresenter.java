package com.jn.iyuba.concept.simple.presenter.me;


import com.jn.iyuba.concept.simple.model.bean.MyTimeBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.model.me.MyTimeModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.me.MyTimeContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class MyTimePresenter extends BasePresenter<MyTimeContract.MyTimeView, MyTimeContract.MyTimModel>
        implements MyTimeContract.MyTimePresenter {

    @Override
    protected MyTimeContract.MyTimModel initModel() {
        return new MyTimeModel();
    }

    @Override
    public void getMyTime(String uid, int day, int flg) {

        Disposable disposable = model.getMyTime(uid, day, flg, new MyTimeContract.MyTimeCallback() {
            @Override
            public void success(MyTimeBean myTimeBean) {

                view.hideLoading();
                if (myTimeBean.getResult().equals("1")) {

                    view.getMyTimeComplete(myTimeBean);
                }
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void updateScore(int srid, int mobile, String flag, String uid, int appid) {

        Disposable disposable = model.updateScore(srid, mobile, flag, uid, appid, new MyTimeContract.ScoreCallback() {
            @Override
            public void success(ScoreBean scoreBean) {

                if (scoreBean.getResult().equals("200")) {

                    view.updateScore(scoreBean);
                } else if (scoreBean.getResult().equals("202")) {
                    view.toast("您今日已打卡");
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
