package com.jn.iyuba.concept.simple.presenter.break_through;


import com.jn.iyuba.concept.simple.model.bean.WordBean;
import com.jn.iyuba.concept.simple.model.break_through.BreakThroughModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.break_through.BreakThroughContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class BreakThroughPresenter extends BasePresenter<BreakThroughContract.BreakThroughView, BreakThroughContract.BreakThroughModel>
        implements BreakThroughContract.BreakThroughPresenter {

    @Override
    protected BreakThroughContract.BreakThroughModel initModel() {
        return new BreakThroughModel();
    }


    @Override
    public void getConceptWord(String url, int book) {

        Disposable disposable = model.getConceptWord(url, book, new BreakThroughContract.WordCallback() {
            @Override
            public void success(WordBean wordBean) {

                view.getConceptWord(wordBean, book);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求失败");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getWordByUnit(String bookid) {

        Disposable disposable = model.getWordByUnit(bookid, new BreakThroughContract.WordCallback() {
            @Override
            public void success(WordBean wordBean) {

                if (wordBean.getResult() == 200) {

                    view.getConceptWord(wordBean, Integer.parseInt(bookid));
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求失败");
                }
            }
        });
        addSubscribe(disposable);
    }
}
