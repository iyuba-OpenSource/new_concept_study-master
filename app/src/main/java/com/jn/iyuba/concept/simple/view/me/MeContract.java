package com.jn.iyuba.concept.simple.view.me;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncEvalBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncListenBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncWordBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MeContract {

    interface MeView extends LoadingView {

        void getStudyRecordByTestMode(SyncListenBean syncListenBean);

        void updateApp(ResponseBody responseBody);

        void moreInfoComplete(MoreInfoBean moreInfoBean);
    }

    interface MePresenter extends IBasePresenter<MeView> {

        void getStudyRecordByTestMode(String format, int uid, int Pageth, int NumPerPage,
                                      int TestMode, String sign, String Lesson);

        void getTestRecord(String userId, String newstype);

        void getExamDetail(int appId, String uid, String lesson, String TestMode, int mode,
                           String sign, String format);

        void getTestRecordDetail(String appId, String uid, String TestMode, String sign,
                                 String format, String Pageth, String NumPerPage);

        void islatest(String url, int currver, String packageStr);

        void getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign);
    }

    interface MeModel extends BaseModel {

        Disposable getStudyRecordByTestMode(String format, int uid, int Pageth, int NumPerPage,
                                            int TestMode, String sign, String Lesson,
                                            Callback callback);

        Disposable getTestRecord(String userId, String newstype, SyncEvalCallback callback);


        Disposable getExamDetail(int appId, String uid, String lesson, String TestMode, int mode,
                                 String sign, String format, SyncWordCallback syncWordCallback);

        Disposable getTestRecordDetail(String appId, String uid, String TestMode, String sign,
                                       String format, String Pageth, String NumPerPage, SyncExerciseCallback syncExerciseCallback);

        Disposable islatest(String url, int currver, String packageStr, LatestCallback latestCallback);

        Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign, MoreCallback moreCallback);
    }

    interface MoreCallback {

        void success(MoreInfoBean moreInfoBean);

        void error(Exception e);
    }

    interface LatestCallback {
        void success(ResponseBody responseBody);

        void error(Exception e);
    }

    interface SyncExerciseCallback {

        void success(SyncExerciseBean syncExerciseBean);

        void error(Exception e);
    }

    interface SyncWordCallback {

        void success(SyncWordBean syncWordBean);

        void error(Exception e);
    }

    interface SyncEvalCallback {

        void success(SyncEvalBean syncEvalBean);

        void error(Exception e);
    }

    interface Callback {

        void success(SyncListenBean syncListenBean);

        void error(Exception e);
    }
}
