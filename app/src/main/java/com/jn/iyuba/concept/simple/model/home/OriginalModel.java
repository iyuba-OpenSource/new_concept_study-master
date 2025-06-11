package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.AdEntryBean;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.view.home.OriginalContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class OriginalModel implements OriginalContract.OriginalModel {
    @Override
    public Disposable getConceptSentence(int voaid, OriginalContract.SentenceCallback sentenceCallback) {


        return NetWorkManager
                .getRequest()
                .getConceptSentence(voaid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LessonDetailBean>() {
                    @Override
                    public void accept(LessonDetailBean lessonDetailBean) throws Exception {

                        sentenceCallback.success(lessonDetailBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        sentenceCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getAdEntryAll(String appId, int flag, String uid, OriginalContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getAdEntryAll(Constant.GET_AD_ENTRY_ALL, Constant.APPID + "", flag, uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AdEntryBean>>() {
                    @Override
                    public void accept(List<AdEntryBean> adEntryBeans) throws Exception {

                        callback.success(adEntryBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateStudyRecordNew(String format, String uid, String BeginTime, String EndTime,
                                           String Lesson, String TestMode, String TestWords, String platform,
                                           String appName, String DeviceId, String LessonId, String sign,
                                           int EndFlg, int TestNumber, int rewardVersion, OriginalContract.UpdateStudyCallback callback) {

        return NetWorkManager
                .getRequest()
                .updateStudyRecordNew(Constant.UPDATE_STUDY_RECORD_NEW, format, uid, BeginTime, EndTime,
                        Lesson, TestMode, TestWords, platform, appName, DeviceId, LessonId,
                        sign, EndFlg, TestNumber, rewardVersion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        callback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable apiWord(String q, OriginalContract.WordCallback wordCallback) {

        return NetWorkManager
                .getRequest()
                .apiWord(Constant.API_WORD, q)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        wordCallback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateWord(String groupName, String mod, String word, String userId, String format
            , OriginalContract.WordCollectCallback wordCollectCallback) {

        return NetWorkManager
                .getRequest()
                .updateWord(Constant.UPDATE_WORD, groupName, mod, word, userId, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordCollectBean>() {
                    @Override
                    public void accept(WordCollectBean wordCollectBean) throws Exception {

                        wordCollectCallback.success(wordCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordCollectCallback.error((Exception) throwable);
                    }
                });
    }
}
