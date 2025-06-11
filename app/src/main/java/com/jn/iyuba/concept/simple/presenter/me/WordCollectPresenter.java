package com.jn.iyuba.concept.simple.presenter.me;

import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordCollectListBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordPdfBean;
import com.jn.iyuba.concept.simple.model.me.WordCollectModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.me.WordCollectContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class WordCollectPresenter extends BasePresenter<WordCollectContract.WordCollectView, WordCollectContract.WordCollectModel>
        implements WordCollectContract.WordCollectPresenter {


    @Override
    protected WordCollectContract.WordCollectModel initModel() {
        return new WordCollectModel();
    }

    @Override
    public void wordListService(String u, int pageNumber, int pageCounts, String format) {

        Disposable disposable = model.wordListService(u, pageNumber, pageCounts, format, new WordCollectContract.Callback() {
            @Override
            public void success(WordCollectListBean wordCollectListBean) {

                view.wordListService(wordCollectListBean);
            }

            @Override
            public void error(Exception e) {

                view.wordListService(null);
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void updateWord(String groupName, String mod, String word, String userId, String format) {

        Disposable disposable = model.updateWord(groupName, mod, word, userId, format, new WordCollectContract.WordDeleteCallback() {
            @Override
            public void success(WordCollectBean wordCollectBean) {

                if (wordCollectBean.getResult() == 1) {

                    view.updateWord(wordCollectBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("删除失败，请重试");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getWordToPDF(int u, int pageNumber, int pageCountsm) {

        Disposable disposable = model.getWordToPDF(u, pageNumber, pageCountsm, new WordCollectContract.WordPdfCallback() {
            @Override
            public void success(WordPdfBean wordPdfBean) {

                view.getWordToPDF(wordPdfBean);
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
