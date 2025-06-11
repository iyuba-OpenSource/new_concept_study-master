package com.jn.iyuba.novel.presenter;

import com.jn.iyuba.novel.model.ChapterModel;
import com.jn.iyuba.novel.model.bean.ChapterBean;
import com.jn.iyuba.novel.view.ChapterContract;

import io.reactivex.disposables.Disposable;

public class ChapterPresenter extends BasePresenter<ChapterContract.ChapterView, ChapterContract.ChapterModel>
        implements ChapterContract.ChapterPresenter {


    @Override
    protected ChapterContract.ChapterModel initModel() {
        return new ChapterModel();
    }

    @Override
    public void getStroryInfo(String types, int level, int orderNumber, String from) {

        Disposable disposable = model.getStroryInfo(types, level, orderNumber, from, new ChapterContract.Callback() {
            @Override
            public void success(ChapterBean chapterBean) {

                if (chapterBean.getResult() == 200) {

                    view.getStroryInfo(chapterBean, from);
                } else {

                    view.getStroryInfo(null, null);
                }
            }

            @Override
            public void error(Exception e) {

                view.getStroryInfo(null, null);
            }
        });
        addSubscribe(disposable);
    }
}
