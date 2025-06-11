package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncEvalBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncListenBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncWordBean;
import com.jn.iyuba.concept.simple.view.me.MeContract;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MeModel implements MeContract.MeModel {


    @Override
    public Disposable getStudyRecordByTestMode(String format, int uid, int Pageth,
                                               int NumPerPage, int TestMode, String sign,
                                               String Lesson, MeContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getStudyRecordByTestMode(Constant.GET_STUDY_RECORD_BY_TEST_MODE, format, uid, Pageth, NumPerPage, TestMode, sign, Lesson)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SyncListenBean>() {
                    @Override
                    public void accept(SyncListenBean syncListenBean) throws Exception {

                        callback.success(syncListenBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getTestRecord(String userId, String newstype, MeContract.SyncEvalCallback callback) {

        return NetWorkManager
                .getRequest()
                .getTestRecord(Constant.GET_TEST_RECORD, userId, newstype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SyncEvalBean>() {
                    @Override
                    public void accept(SyncEvalBean syncEvalBean) throws Exception {

                        callback.success(syncEvalBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getExamDetail(int appId, String uid, String lesson, String TestMode, int mode,
                                    String sign, String format, MeContract.SyncWordCallback syncWordCallback) {

        return NetWorkManager
                .getRequest()
                .getExamDetail(Constant.GET_EXAM_DETAIL, appId, uid, lesson, TestMode, mode, sign, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SyncWordBean>() {
                    @Override
                    public void accept(SyncWordBean syncWordBean) throws Exception {

                        syncWordCallback.success(syncWordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        syncWordCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getTestRecordDetail(String appId, String uid, String TestMode, String sign,
                                          String format, String Pageth, String NumPerPage, MeContract.SyncExerciseCallback syncExerciseCallback) {

        return NetWorkManager
                .getRequest()
                .getTestRecordDetail(Constant.GET_TEST_RECORD_DETAIL, appId, uid, TestMode, sign, format, Pageth, NumPerPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SyncExerciseBean>() {
                    @Override
                    public void accept(SyncExerciseBean syncExerciseBean) throws Exception {

                        syncExerciseCallback.success(syncExerciseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        syncExerciseCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable islatest(String url, int currver, String packageStr, MeContract.LatestCallback latestCallback) {

        return NetWorkManager
                .getRequest()
                .islatest(url, currver, packageStr)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> latestCallback.success(responseBody), new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        latestCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign, MeContract.MoreCallback moreCallback) {

        return NetWorkManager
                .getRequest()
                .getMoreInfo(Constant.PROTOCOL, platform, protocol, id, myid, appid, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoreInfoBean>() {
                    @Override
                    public void accept(MoreInfoBean moreInfoBean) throws Exception {

                        moreCallback.success(moreInfoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        moreCallback.error((Exception) throwable);
                    }
                });
    }
}
