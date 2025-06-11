package com.jn.iyuba.concept.simple.model.home;


import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.ConceptExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateTestRecordBean;
import com.jn.iyuba.concept.simple.view.home.ContentQuestionContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContentQuestionModel implements ContentQuestionContract.ContentQuestionModel {


    @Override
    public Disposable updateTestRecordNew(String format, String appName, String sign, String uid,
                                          String appId, int TestMode, String DeviceId, String jsonStr, ContentQuestionContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .updateTestRecordNew(Constant.UPDATE_TEST_RECORD_NEW, format, appName, sign, uid, appId, TestMode, DeviceId, jsonStr)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateTestRecordBean>() {
                    @Override
                    public void accept(UpdateTestRecordBean updateTestRecordBean) throws Exception {

                        callback.success(updateTestRecordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getConceptExercise(int bookNum, ContentQuestionContract.ExerciseCallback exerciseCallback) {

        return NetWorkManager
                .getRequest()
                .getConceptExercise(bookNum)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ConceptExerciseBean>() {
                    @Override
                    public void accept(ConceptExerciseBean conceptExerciseBean) throws Exception {

                        exerciseCallback.success(conceptExerciseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        exerciseCallback.error((Exception) throwable);
                    }
                });
    }
}
