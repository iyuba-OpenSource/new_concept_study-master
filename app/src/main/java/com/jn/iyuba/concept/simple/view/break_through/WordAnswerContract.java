package com.jn.iyuba.concept.simple.view.break_through;


import com.jn.iyuba.concept.simple.entity.ExamRecordPost;
import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.ExamRecordBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateBTBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public interface WordAnswerContract {


    interface WordAnswerView extends LoadingView {

        void updateExamRecordComplete(ExamRecordBean examRecordBean);
    }

    interface WordAnswerPresenter extends IBasePresenter<WordAnswerView> {


        void updateExamRecord(ExamRecordPost bean);

    }


    interface WordAnswerModel extends BaseModel {

        Disposable updateExamRecord(ExamRecordPost bean, Callback callback);
    }

    interface Callback {

        void success(UpdateBTBean examRecordBean);

        void error(Exception e);
    }
}
