package com.jn.iyuba.concept.simple.view.home;


import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.ConceptExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateTestRecordBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface ContentQuestionContract {

    interface ContentQuestionView extends LoadingView {

        void updateTestRecordComplete(UpdateTestRecordBean updateTestRecordBean);

        void getConceptExercise(ConceptExerciseBean conceptExerciseBean);
    }

    interface ContentQuestionPresenter extends IBasePresenter<ContentQuestionView> {

        void updateTestRecordNew(String format, String appName, String sign,
                                 String uid, String appId, int TestMode,
                                 String DeviceId, String jsonStr);

        void getConceptExercise(int bookNum);
    }

    interface ContentQuestionModel extends BaseModel {


        Disposable updateTestRecordNew(String format, String appName, String sign,
                                       String uid, String appId, int TestMode,
                                       String DeviceId, String jsonStr, Callback callback);

        Disposable getConceptExercise(int bookNum, ExerciseCallback exerciseCallback);
    }

    interface ExerciseCallback {


        void success(ConceptExerciseBean conceptExerciseBean);

        void error(Exception e);

    }

    interface Callback {

        void success(UpdateTestRecordBean updateTestRecordBean);

        void error(Exception e);
    }

}
