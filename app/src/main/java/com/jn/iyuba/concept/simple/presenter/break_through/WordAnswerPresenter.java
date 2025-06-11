package com.jn.iyuba.concept.simple.presenter.break_through;

import android.util.Log;

import com.google.gson.Gson;
import com.jn.iyuba.concept.simple.entity.ExamRecordPost;
import com.jn.iyuba.concept.simple.model.bean.UpdateBTBean;
import com.jn.iyuba.concept.simple.model.break_through.WordAnswerModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.break_through.WordAnswerContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class WordAnswerPresenter extends BasePresenter<WordAnswerContract.WordAnswerView, WordAnswerContract.WordAnswerModel>
        implements WordAnswerContract.WordAnswerPresenter {

    @Override
    protected WordAnswerContract.WordAnswerModel initModel() {
        return new WordAnswerModel();
    }

    @Override
    public void updateExamRecord(ExamRecordPost bean) {

        Disposable disposable = model.updateExamRecord(bean, new WordAnswerContract.Callback() {
            @Override
            public void success(UpdateBTBean examRecordBean) {

                if (examRecordBean.getResult().equals("1")) {

                    if (examRecordBean.getJiFen().equals("0")) {

                        view.toast("测评数据成功同步到云端。");
                    } else {

//                            view.toast("测评数据成功同步到云端 " + jsonObject.getInt("jiFen") + "积分");
                        view.toast("测评数据成功同步到云端。");
                    }
                } else {

                    view.toast("测评数据同步到云端失败！");
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }
}
