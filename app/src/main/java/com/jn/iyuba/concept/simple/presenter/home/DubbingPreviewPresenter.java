package com.jn.iyuba.concept.simple.presenter.home;


import android.util.Log;

import com.google.gson.Gson;
import com.jn.iyuba.concept.simple.entity.DubbingPublish;
import com.jn.iyuba.concept.simple.model.bean.DubbingPublishBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.model.home.DubbingPreviewModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.DubbingPreviewContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class DubbingPreviewPresenter extends BasePresenter<DubbingPreviewContract.DubbingPreviewView, DubbingPreviewContract.DubbingPreviewModel>
        implements DubbingPreviewContract.DubbingPreviewPresenter {

    @Override
    protected DubbingPreviewContract.DubbingPreviewModel initModel() {
        return new DubbingPreviewModel();
    }

    @Override
    public void publishDubbing(int protocol, int content, String userid, DubbingPublish dubbingPublish) {

        String jsonStr = new Gson().toJson(dubbingPublish);
        Log.d("jsonStr", jsonStr);

        Disposable disposable = model.publishDubbing(protocol, content, userid, dubbingPublish, new DubbingPreviewContract.Callback() {
            @Override
            public void success(DubbingPublishBean dubbingPublishBean) {

                if (dubbingPublishBean.getResultCode() == 200) {

                    view.toast("发布成功");
                    view.publishDubbingCompelte(dubbingPublishBean);
                } else {

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
    public void updateScore(String flag, String uid, String appid, String idindex, int srid, int mobile) {

        Disposable disposable = model.updateScore(flag, uid, appid, idindex, srid, mobile, new DubbingPreviewContract.UpdateScoreCallback() {
            @Override
            public void success(ScoreBean updateScoreBean) {

                if (updateScoreBean.getResult().equals("200")) {

                    view.updateScore(updateScoreBean);
                    view.toast("扣除积分成功");
                } else {

                    view.toast(updateScoreBean.getMessage());
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
