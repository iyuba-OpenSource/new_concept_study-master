package com.jn.iyuba.concept.simple.presenter.home;

import android.widget.Toast;

import com.google.gson.Gson;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.entity.RewardEventbus;
import com.jn.iyuba.concept.simple.model.bean.TestRecordBean;
import com.jn.iyuba.concept.simple.model.home.MediaModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.MediaContract;
import com.jn.iyuba.concept.simple.view.home.OriginalContract;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class MediaPresenter extends BasePresenter<MediaContract.MediaView, MediaContract.MediaModel>
        implements MediaContract.MediaPresenter {


    @Override
    protected MediaContract.MediaModel initModel() {
        return new MediaModel();
    }

    @Override
    public void updateStudyRecordNew(String format, String uid, String BeginTime, String EndTime,
                                     String Lesson, String TestMode, String TestWords, String platform,
                                     String appName, String DeviceId, String LessonId, String sign,
                                     int EndFlg, int TestNumber, int bookid, String language, int rewardVersion) {

        Disposable disposable = model.updateStudyRecordNew(format, uid, BeginTime, EndTime, Lesson,
                TestMode, TestWords, platform, appName,
                DeviceId, LessonId, sign, EndFlg, TestNumber, rewardVersion, new MediaContract.UpdateStudyCallback() {
                    @Override
                    public void success(ResponseBody responseBody) {

                        TestRecordBean testRecordBean = null;
                        try {
                            testRecordBean = new Gson().fromJson(responseBody.string().trim(), TestRecordBean.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!testRecordBean.getReward().equals("0")) {

                            EventBus.getDefault().post(new RewardEventbus(Integer.parseInt(testRecordBean.getReward())));
                        } else if (testRecordBean.getJiFen() != null) {

                            Toast.makeText(MyApplication.getContext(), "获得" + testRecordBean.getJiFen() + "积分", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void error(Exception e) {


                    }
                });
    }
}
