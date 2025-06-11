package com.jn.iyuba.concept.simple.presenter.home;

import com.google.android.exoplayer2.util.Log;
import com.google.gson.Gson;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.Refresh;
import com.jn.iyuba.concept.simple.model.bean.AdEntryBean;
import com.jn.iyuba.concept.simple.model.bean.ApiWordBean;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.model.bean.TestRecordBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.model.home.OriginalModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.util.ApiWordXmlToBean;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.view.home.OriginalContract;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class OriginalPresenter extends BasePresenter<OriginalContract.OriginalView, OriginalContract.OriginalModel>
        implements OriginalContract.OriginalPresenter {


    @Override
    protected OriginalContract.OriginalModel initModel() {
        return new OriginalModel();
    }

    @Override
    public void getConceptSentence(int voaid) {

        Disposable disposable = model.getConceptSentence(voaid, new OriginalContract.SentenceCallback() {
            @Override
            public void success(LessonDetailBean lessonDetailBean) {

                view.getSentenceComplete(lessonDetailBean);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getAdEntryAll(String appId, int flag, String uid) {

        Disposable disposable = model.getAdEntryAll(appId, flag, uid, new OriginalContract.Callback() {
            @Override
            public void success(List<AdEntryBean> adEntryBeans) {

                if (adEntryBeans.size() != 0) {

                    AdEntryBean adEntryBean = adEntryBeans.get(0);
                    if (adEntryBean.getResult().equals("1")) {

                        view.getAdEntryAllComplete(adEntryBean);
                    }
                }
            }

            @Override
            public void error(Exception e) {

            }
        });

        addSubscribe(disposable);
    }

    @Override
    public void updateStudyRecordNew(String format, String uid, String BeginTime, String EndTime,
                                     String Lesson, String TestMode, String TestWords, String platform,
                                     String appName, String DeviceId, String LessonId, String sign,
                                     int EndFlg, int TestNumber, int bookid, String language, int rewardVersion) {


        Disposable disposable = model.updateStudyRecordNew(format, uid, BeginTime, EndTime, Lesson,
                TestMode, TestWords, platform, appName,
                DeviceId, LessonId, sign, EndFlg, TestNumber, rewardVersion, new OriginalContract.UpdateStudyCallback() {
                    @Override
                    public void success(ResponseBody responseBody) {

                        TestRecordBean testRecordBean = null;
                        try {
                            testRecordBean = new Gson().fromJson(responseBody.string().trim(), TestRecordBean.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (testRecordBean != null) {


                        }
                    }

                    @Override
                    public void error(Exception e) {


                    }
                });
    }


    @Override
    public void apiWord(String q) {

        Disposable disposable = model.apiWord(q, new OriginalContract.WordCallback() {
            @Override
            public void success(ResponseBody responseBody) {

                try {
                    ApiWordBean apiWordBean = ApiWordXmlToBean.parseXMLWithPull(responseBody.string());
                    if (apiWordBean.getResult() == 1) {

                        view.getWord(apiWordBean);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void updateWord(String groupName, String mod, String word, String userId, String format) {

        Disposable disposable = model.updateWord(groupName, mod, word, userId, format, new OriginalContract.WordCollectCallback() {
            @Override
            public void success(WordCollectBean wordCollectBean) {

                if (wordCollectBean.getResult() == 1) {

                    view.collectWord(wordCollectBean);
                } else {

                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
