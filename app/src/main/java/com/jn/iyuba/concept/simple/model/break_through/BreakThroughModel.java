package com.jn.iyuba.concept.simple.model.break_through;


import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.WordBean;
import com.jn.iyuba.concept.simple.view.break_through.BreakThroughContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BreakThroughModel implements BreakThroughContract.BreakThroughModel {


    @Override
    public Disposable getConceptWord(String url, int book, BreakThroughContract.WordCallback wordCallback) {

        return NetWorkManager
                .getRequest()
                .getConceptWord(url, book)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordBean>() {
                    @Override
                    public void accept(WordBean wordBean) throws Exception {

                        wordCallback.success(wordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getWordByUnit(String bookid, BreakThroughContract.WordCallback wordCallback) {

        return NetWorkManager
                .getRequest()
                .getWordByUnit(bookid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordBean>() {
                    @Override
                    public void accept(WordBean wordBean) throws Exception {

                        wordCallback.success(wordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordCallback.error((Exception) throwable);
                    }
                });
    }
}
