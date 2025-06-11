package com.jn.iyuba.novel.model;

import com.jn.iyuba.novel.model.bean.NovelBookBean;
import com.jn.iyuba.novel.view.ChooseLessonContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChooseLessonModel implements ChooseLessonContract.ChooseLessonModel {


    @Override
    public Disposable getStroryInfo(String types, int level, String from, ChooseLessonContract.BookCallback callback) {

        return NetWorkManager
                .getRequest()
                .getStroryInfo(types,level,from)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NovelBookBean>() {
                    @Override
                    public void accept(NovelBookBean novelBookBean) throws Exception {

                        callback.success(novelBookBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
