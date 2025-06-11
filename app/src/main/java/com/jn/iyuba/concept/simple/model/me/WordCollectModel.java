package com.jn.iyuba.concept.simple.model.me;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.home.WordCollectBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordCollectListBean;
import com.jn.iyuba.concept.simple.model.bean.me.WordPdfBean;
import com.jn.iyuba.concept.simple.view.me.WordCollectContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WordCollectModel implements WordCollectContract.WordCollectModel {


    @Override
    public Disposable wordListService(String u, int pageNumber, int pageCounts, String format, WordCollectContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .wordListService(Constant.WORD_LIST_SERVICE, u, pageNumber, pageCounts, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordCollectListBean>() {
                    @Override
                    public void accept(WordCollectListBean wordCollectListBean) throws Exception {

                        callback.success(wordCollectListBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateWord(String groupName, String mod, String word, String userId, String format, WordCollectContract.WordDeleteCallback wordDeleteCallback) {

        return NetWorkManager
                .getRequest()
                .updateWord(Constant.UPDATE_WORD, groupName, mod, word, userId, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordCollectBean>() {
                    @Override
                    public void accept(WordCollectBean wordCollectBean) throws Exception {

                        wordDeleteCallback.success(wordCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordDeleteCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getWordToPDF(int u, int pageNumber, int pageCounts, WordCollectContract.WordPdfCallback wordPdfCallback) {

        return NetWorkManager
                .getRequest()
                .getWordToPDF(Constant.GET_WORD_TO_PDF, u, pageNumber, pageCounts)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordPdfBean>() {
                    @Override
                    public void accept(WordPdfBean wordPdfBean) throws Exception {

                        wordPdfCallback.success(wordPdfBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordPdfCallback.error((Exception) throwable);
                    }
                });
    }
}
