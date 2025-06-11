package com.jn.iyuba.concept.simple.view;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.home.OriginalContract;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public interface MediaContract {


    interface MediaView extends BaseView {

    }

    interface MediaPresenter extends IBasePresenter<MediaView> {

        void updateStudyRecordNew(String format, String uid,
                                  String BeginTime, String EndTime,
                                  String Lesson, String TestMode,
                                  String TestWords, String platform,
                                  String appName, String DeviceId,
                                  String LessonId, String sign, int EndFlg, int TestNumber, int bookid, String language, int rewardVersion);
    }

    interface MediaModel extends BaseModel {

        Disposable updateStudyRecordNew(String format, String uid,
                                        String BeginTime, String EndTime,
                                        String Lesson, String TestMode,
                                        String TestWords, String platform,
                                        String appName, String DeviceId,
                                        String LessonId, String sign, int EndFlg, int TestNumber, int rewardVersion, UpdateStudyCallback callback);
    }

    interface UpdateStudyCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);

    }
}
