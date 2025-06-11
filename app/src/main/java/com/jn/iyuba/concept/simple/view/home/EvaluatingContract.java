package com.jn.iyuba.concept.simple.view.home;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.ApiWordBean;
import com.jn.iyuba.concept.simple.model.bean.EvalBean;
import com.jn.iyuba.concept.simple.model.bean.EvaluteRecordBean;
import com.jn.iyuba.concept.simple.model.bean.MergeBean;
import com.jn.iyuba.concept.simple.model.bean.PublishEvalBean;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;

public interface EvaluatingContract {


    interface EvaluatingView extends LoadingView {

        void testComplete(EvalBean evalBean, String idindex, String paraid);

        /**
         * @param publishEvalBean
         * @param idindex
         * @param shuoshuotype    用来区分是合成发布还是单据发布
         */
        void getPublishResult(PublishEvalBean publishEvalBean, String idindex, int shuoshuotype);


        void getMerge(MergeBean mergeBean);

        void getWord(ApiWordBean apiWordBean);

        void collectWord(WordCollectBean wordCollectBean);
    }

    interface EvaluatingPresenter extends IBasePresenter<EvaluatingView> {

        void test(RequestBody body, String idindex, String paraid);


        void publishEval(String topic, String content, String format, int idIndex, int paraid, String platform,
                         int protocol, int score, int shuoshuotype, String userid, String name,
                         String voaid, int rewardVersion, int appid);

        void merge(String audios, String type);


        void apiWord(String q);

        void updateWord(String groupName, String mod, String word, String userId, String format);
    }

    interface EvaluatingModel extends BaseModel {

        Disposable test(RequestBody body, EvalCallback callback);

        Disposable publishEval(String topic, String content, String format, int idIndex, int paraid, String platform,
                               int protocol, int score, int shuoshuotype, String userid, String name,
                               String voaid, int rewardVersion, int appid, PublishCallback callback);

        Disposable merge(String audios, String type, MergeCallback mergeCallback);

        Disposable apiWord(String q, WordCallback wordCallback);


        Disposable updateWord(String groupName, String mod, String word, String userId, String format, WordCollectCallback wordCollectCallback);
    }


    interface WordCollectCallback {

        void success(WordCollectBean wordCollectBean);

        void error(Exception e);

    }

    interface WordCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);
    }

    interface MergeCallback {

        void success(MergeBean mergeBean);

        void error(Exception e);
    }

    interface PublishCallback {

        void success(PublishEvalBean publishEvalBean);

        void error(Exception e);
    }

    interface EvalCallback {

        void success(EvalBean evalBean);

        void error(Exception e);
    }
}
