package com.jn.iyuba.concept.simple.presenter.home;


import com.jn.iyuba.concept.simple.model.bean.ApiWordBean;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;
import com.jn.iyuba.concept.simple.model.bean.MergeBean;
import com.jn.iyuba.concept.simple.model.bean.PublishEvalBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.model.home.EvaluatingModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.util.ApiWordXmlToBean;
import com.jn.iyuba.concept.simple.view.home.EvaluatingContract;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class EvaluatingPresenter extends BasePresenter<EvaluatingContract.EvaluatingView, EvaluatingContract.EvaluatingModel>
        implements EvaluatingContract.EvaluatingPresenter {


    @Override
    protected EvaluatingContract.EvaluatingModel initModel() {
        return new EvaluatingModel();
    }


    @Override
    public void test(RequestBody body, String idindex, String paraid) {

        Disposable disposable = model.test(body, new EvaluatingContract.EvalCallback() {
            @Override
            public void success(EvalBean evalBean) {

                if (evalBean.getResult().equals("1")) {

                    view.testComplete(evalBean, idindex, paraid);
                } else {

                    view.toast(evalBean.getMessage());
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

    @Override
    public void publishEval(String topic, String content, String format, int idIndex, int paraid,
                            String platform, int protocol, int score, int shuoshuotype, String userid,
                            String name, String voaid, int rewardVersion, int appid) {


        Disposable disposable = model.publishEval(topic, content, format, idIndex, paraid, platform,
                protocol, score, shuoshuotype, userid, name, voaid, rewardVersion, appid, new EvaluatingContract.PublishCallback() {
                    @Override
                    public void success(PublishEvalBean publishEvalBean) {

                        if (publishEvalBean.getResultCode().equals("501")) {

                            view.getPublishResult(publishEvalBean, idIndex + "", shuoshuotype);
//                            view.toast("获得积分" + publishEvalBean.getAddScore());
                            view.toast("上传成功");
                        } else {

                            view.toast(publishEvalBean.getMessage());
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


    @Override
    public void merge(String audios, String type) {

        Disposable disposable = model.merge(audios, type, new EvaluatingContract.MergeCallback() {

            @Override
            public void success(MergeBean mergeBean) {

                if (mergeBean.getResult().equals("1")) {

                    view.getMerge(mergeBean);
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

    @Override
    public void apiWord(String q) {

        Disposable disposable = model.apiWord(q, new EvaluatingContract.WordCallback() {
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

        Disposable disposable = model.updateWord(groupName, mod, word, userId, format, new EvaluatingContract.WordCollectCallback() {
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
