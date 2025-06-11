package com.jn.iyuba.concept.simple.view.me;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordCollectListBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordPdfBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WordCollectContract {

    interface WordCollectView extends LoadingView {


        void wordListService(WordCollectListBean wordCollectListBean);

        void updateWord(WordCollectBean wordCollectBean);

        void getWordToPDF(WordPdfBean wordPdfBean);
    }

    interface WordCollectPresenter extends IBasePresenter<WordCollectView> {

        void wordListService(String u, int pageNumber, int pageCounts, String format);

        void updateWord(String groupName, String mod,
                        String word, String userId, String format);

        void getWordToPDF(int u, int pageNumber, int pageCountsm);
    }

    interface WordCollectModel extends BaseModel {

        Disposable wordListService(String u, int pageNumber, int pageCounts, String format, Callback callback);

        Disposable updateWord(String groupName, String mod,
                              String word, String userId, String format, WordDeleteCallback wordDeleteCallback);

        Disposable getWordToPDF(int u, int pageNumber, int pageCountsm, WordPdfCallback wordPdfCallback);
    }

    interface WordPdfCallback {

        void success(WordPdfBean wordPdfBean);

        void error(Exception e);
    }

    interface WordDeleteCallback {

        void success(WordCollectBean wordCollectBean);

        void error(Exception e);
    }

    interface Callback {

        void success(WordCollectListBean wordCollectListBean);

        void error(Exception e);
    }
}

