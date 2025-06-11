package com.jn.iyuba.concept.simple.presenter.home;

import com.jn.iyuba.concept.simple.model.bean.home.ReadSubmitBean;
import com.jn.iyuba.concept.simple.model.home.ParaModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.ParaContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class ParaPresenter extends BasePresenter<ParaContract.ParaView, ParaContract.ParaModel>
        implements ParaContract.ParaPresenter {


    @Override
    protected ParaContract.ParaModel initModel() {
        return new ParaModel();
    }


    @Override
    public void updateNewsStudyRecord(String format, int uid, String BeginTime, String EndTime,
                                      String appName, String Lesson, int LessonId, int appId,
                                      String Device, String DeviceId, int EndFlg, int wordcount,
                                      int categoryid, String platform, int rewardVersion) {

        Disposable disposable = model.updateNewsStudyRecord(format, uid, BeginTime, EndTime,
                appName, Lesson, LessonId, appId,
                Device, DeviceId, EndFlg, wordcount, categoryid, platform, rewardVersion,
        new ParaContract.Callback() {
            @Override
            public void success(ReadSubmitBean readSubmitBean) {

                if (readSubmitBean.getResult().equals("1")) {

                    view.toast("提交成功");
                    view.submitRead(readSubmitBean);
                } else if (readSubmitBean.getResult().equals("0")) {

                    view.toast("重复提交");
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
