package com.jn.iyuba.concept.simple.model.home;


import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;
import com.jn.iyuba.concept.simple.model.bean.MergeBean;
import com.jn.iyuba.concept.simple.model.bean.PublishEvalBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.view.home.EvaluatingContract;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class EvaluatingModel implements EvaluatingContract.EvaluatingModel {

    @Override
    public Disposable test(RequestBody body, EvaluatingContract.EvalCallback callback) {

        return NetWorkManager
                .getRequest()
                .eval(Constant.EVAL_URL, body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EvalBean>() {
                    @Override
                    public void accept(EvalBean evalBean) throws Exception {

                        callback.success(evalBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable publishEval(String topic, String content, String format, int idIndex, int paraid,
                                  String platform, int protocol, int score, int shuoshuotype, String userid,
                                  String name, String voaid, int rewardVersion, int appid, EvaluatingContract.PublishCallback callback) {

        return NetWorkManager
                .getRequest()
                .publishEval(Constant.PUBLISH, topic, content, format, idIndex, paraid, platform,
                        protocol, score, shuoshuotype, userid, name, voaid, rewardVersion, appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PublishEvalBean>() {
                    @Override
                    public void accept(PublishEvalBean publishEvalBean) throws Exception {

                        callback.success(publishEvalBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable merge(String audios, String type, EvaluatingContract.MergeCallback mergeCallback) {

        return NetWorkManager
                .getRequest()
                .merge(Constant.MERGE, audios, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MergeBean>() {
                    @Override
                    public void accept(MergeBean mergeBean) throws Exception {

                        mergeCallback.success(mergeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        mergeCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable apiWord(String q, EvaluatingContract.WordCallback wordCallback) {

        return NetWorkManager
                .getRequest()
                .apiWord(Constant.API_WORD, q)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        wordCallback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateWord(String groupName, String mod, String word, String userId, String format, EvaluatingContract.WordCollectCallback wordCollectCallback) {

        return NetWorkManager
                .getRequest()
                .updateWord(Constant.UPDATE_WORD, groupName, mod, word, userId, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordCollectBean>() {
                    @Override
                    public void accept(WordCollectBean wordCollectBean) throws Exception {

                        wordCollectCallback.success(wordCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordCollectCallback.error((Exception) throwable);
                    }
                });
    }
}
