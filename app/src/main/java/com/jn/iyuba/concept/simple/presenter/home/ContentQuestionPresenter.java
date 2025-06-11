package com.jn.iyuba.concept.simple.presenter.home;


import com.jn.iyuba.concept.simple.model.bean.ConceptExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateTestRecordBean;
import com.jn.iyuba.concept.simple.model.home.ContentQuestionModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.ContentQuestionContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class ContentQuestionPresenter extends BasePresenter<ContentQuestionContract.ContentQuestionView
        , ContentQuestionContract.ContentQuestionModel> implements ContentQuestionContract.ContentQuestionPresenter {


    @Override
    protected ContentQuestionContract.ContentQuestionModel initModel() {
        return new ContentQuestionModel();
    }

    @Override
    public void updateTestRecordNew(String format, String appName, String sign, String uid,
                                    String appId, int TestMode, String DeviceId, String jsonStr) {

        Disposable disposable = model.updateTestRecordNew(format, appName, sign, uid, appId, TestMode, DeviceId, jsonStr, new ContentQuestionContract.Callback() {
            @Override
            public void success(UpdateTestRecordBean updateTestRecordBean) {

                if (updateTestRecordBean.getResult().equals("1")) {

                    view.updateTestRecordComplete(updateTestRecordBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
                if (e instanceof RuntimeException) {

                    view.toast("请求超时");
                }

            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getConceptExercise(int bookNum) {

        Disposable disposable = model.getConceptExercise(bookNum, new ContentQuestionContract.ExerciseCallback() {
            @Override
            public void success(ConceptExerciseBean conceptExerciseBean) {

                view.getConceptExercise(conceptExerciseBean);
            }

            @Override
            public void error(Exception e) {

                if(e instanceof  UnknownHostException){


                }
            }
        });
        addSubscribe(disposable);
    }
}
