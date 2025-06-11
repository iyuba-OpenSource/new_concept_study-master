package com.jn.iyuba.concept.simple.model.break_through;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.entity.ExamRecordPost;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.UpdateBTBean;
import com.jn.iyuba.concept.simple.view.break_through.WordAnswerContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class WordAnswerModel implements WordAnswerContract.WordAnswerModel {


    @Override
    public Disposable updateExamRecord(ExamRecordPost bean, WordAnswerContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .updateExamRecord(Constant.UPDATE_EXAM_RECORD, bean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateBTBean>() {
                    @Override
                    public void accept(UpdateBTBean responseBody) throws Exception {

                        callback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
