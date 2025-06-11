package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.home.ReadSubmitBean;
import com.jn.iyuba.concept.simple.view.home.ParaContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ParaModel implements ParaContract.ParaModel {

    @Override
    public Disposable updateNewsStudyRecord(String format, int uid, String BeginTime, String EndTime,
                                            String appName, String Lesson, int LessonId, int appId,
                                            String Device, String DeviceId, int EndFlg, int wordcount,
                                            int categoryid, String platform, int rewardVersion, ParaContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .updateNewsStudyRecord(Constant.URL_UPDATE_NEWS_STUDY_RECORD, format, uid,
                        BeginTime, EndTime, appName, Lesson, LessonId, appId, Device, DeviceId, EndFlg,
                        wordcount, categoryid, platform, rewardVersion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReadSubmitBean>() {
                    @Override
                    public void accept(ReadSubmitBean readSubmitBean) throws Exception {

                        callback.success(readSubmitBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
