package com.jn.iyuba.novel.model;

import com.jn.iyuba.novel.model.bean.ChapterBean;
import com.jn.iyuba.novel.view.ChapterContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChapterModel implements ChapterContract.ChapterModel {


    @Override
    public Disposable getStroryInfo(String types, int level, int orderNumber, String from, ChapterContract.Callback callback) {

        return NetWorkManager
                .getRequest()
                .getStroryInfo(types, level, orderNumber, from)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ChapterBean>() {
                    @Override
                    public void accept(ChapterBean chapterBean) throws Exception {

                        callback.success(chapterBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
