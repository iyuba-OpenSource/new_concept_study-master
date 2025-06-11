package com.jn.iyuba.concept.simple.view.home;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.AdEntryBean;
import com.jn.iyuba.concept.simple.model.bean.ApiWordBean;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;
import com.jn.iyuba.concept.simple.view.SplashContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface OriginalContract {

    interface OriginalView extends LoadingView {

        void getSentenceComplete(LessonDetailBean jpSentenceBean);

        void getAdEntryAllComplete(AdEntryBean adEntryBean);


        void getWord(ApiWordBean apiWordBean);

        void collectWord(WordCollectBean wordCollectBean);
    }


    interface OriginalPresenter extends IBasePresenter<OriginalView> {

        void getConceptSentence(int voaid);

        void getAdEntryAll(String appId, int flag, String uid);

        void updateStudyRecordNew(String format, String uid,
                                  String BeginTime, String EndTime,
                                  String Lesson, String TestMode,
                                  String TestWords, String platform,
                                  String appName, String DeviceId,
                                  String LessonId, String sign, int EndFlg, int TestNumber, int bookid,
                                  String language, int rewardVersion);

        void apiWord(String q);

        void updateWord(String groupName, String mod, String word, String userId, String format);
    }

    interface OriginalModel extends BaseModel {

        Disposable getConceptSentence(int voaid, SentenceCallback sentenceCallback);

        Disposable getAdEntryAll(String appId, int flag, String uid, Callback callback);

        Disposable updateStudyRecordNew(String format, String uid,
                                        String BeginTime, String EndTime,
                                        String Lesson, String TestMode,
                                        String TestWords, String platform,
                                        String appName, String DeviceId,
                                        String LessonId, String sign, int EndFlg, int TestNumber, int rewardVersion, UpdateStudyCallback callback);

        Disposable apiWord(String q, WordCallback wordCallback);


        Disposable updateWord(String groupName, String mod, String word, String userId, String format, WordCollectCallback wordCollectCallback);
    }

    interface WordCollectCallback {

        void success(WordCollectBean wordCollectBean);

        void error(Exception e);

    }

    interface WordCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);
    }

    interface UpdateStudyCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);

    }

    interface Callback {

        void success(List<AdEntryBean> adEntryBeans);

        void error(Exception e);
    }

    interface SentenceCallback {

        void success(LessonDetailBean lessonDetailBean);

        void error(Exception e);
    }
}

