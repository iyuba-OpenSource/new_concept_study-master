package com.jn.iyuba.concept.simple.view.home;

import android.view.View;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.home.ReadSubmitBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ParaContract {


    interface ParaView extends LoadingView {


        /**
         * 上传阅读
         */
        void submitRead(ReadSubmitBean readSubmitBean);
    }

    interface ParaPresenter extends IBasePresenter<ParaView> {


        void updateNewsStudyRecord(String format, int uid, String BeginTime,
                                   String EndTime, String appName, String Lesson, int LessonId,
                                   int appId, String Device, String DeviceId, int EndFlg,
                                   int wordcount, int categoryid, String platform, int rewardVersion);
    }

    interface ParaModel extends BaseModel {


        Disposable updateNewsStudyRecord(String format, int uid, String BeginTime,
                                         String EndTime, String appName, String Lesson, int LessonId,
                                         int appId, String Device, String DeviceId, int EndFlg,
                                         int wordcount, int categoryid, String platform, int rewardVersion, Callback callback);
    }


    interface Callback {

        void success(ReadSubmitBean readSubmitBean);

        void error(Exception e);
    }
}
