package com.jn.iyuba.concept.simple.presenter.home;

import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.model.bean.PdfBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.model.home.ContentModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.view.home.ContentContract;
import com.jn.iyuba.concept.simple.view.home.OriginalContract;
import com.jn.iyuba.novel.model.bean.NovelSentenceBean;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class ContentPresenter extends BasePresenter<ContentContract.ContentView, ContentContract.ContentModel>
        implements ContentContract.ContentPresenter {


    @Override
    protected ContentContract.ContentModel initModel() {
        return new ContentModel();
    }

    @Override
    public void getConceptSentence(int voaid) {

        Disposable disposable = model.getConceptSentence(voaid, new ContentContract.SentenceCallback() {
            @Override
            public void success(LessonDetailBean lessonDetailBean) {

                view.getSentenceComplete(lessonDetailBean);
            }

            @Override
            public void error(Exception e) {

                view.getSentenceComplete(null);
            }
        });

        addSubscribe(disposable);
    }

    @Override
    public void getConceptPdfFile(int voaid, int type) {

        Disposable disposable = model.getConceptPdfFile(voaid, type, new ContentContract.PdfCallback() {
            @Override
            public void success(PdfBean pdfBean) {

                if (pdfBean.getExists().equals("true")) {

                    view.getConceptPdfFile(pdfBean);
                }
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

    @Override
    public void textExamApi(int voaid) {

        Disposable disposable = model.textExamApi(voaid, new ContentContract.SentenceCallback() {
            @Override
            public void success(LessonDetailBean lessonDetailBean) {

                view.getSentenceComplete(lessonDetailBean);
            }

            @Override
            public void error(Exception e) {

                view.getSentenceComplete(null);
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getVoaPdfFile_new(String type, int voaid, int isenglish) {

        Disposable disposable = model.getVoaPdfFile_new(type, voaid, isenglish, new ContentContract.PdfCallback() {
            @Override
            public void success(PdfBean pdfBean) {

                if (pdfBean.getExists().equals("true")) {

                    view.getConceptPdfFile(pdfBean);
                }
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

    @Override
    public void getStroryInfo(String types, int level, String orderNumber, String chapterOrder, String from) {

        Disposable disposable = model.getStroryInfo(types, level, orderNumber, chapterOrder, from, new ContentContract.NovelSentenceCallback() {
            @Override
            public void success(NovelSentenceBean novelSentenceBean) {

                if (novelSentenceBean.getResult() == 200) {

                    view.getStroryInfo(novelSentenceBean);
                }
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

    @Override
    public void getBookWormPdf(String voaid, String type, String language) {

        Disposable disposable = model.getBookWormPdf(voaid, type, language, new ContentContract.PdfCallback() {
            @Override
            public void success(PdfBean pdfBean) {

                if (pdfBean.getExists().equals("true")) {

                    view.getConceptPdfFile(pdfBean);
                }
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

    @Override
    public void updateScore(int srid, int mobile, String flag, String uid, int appid, String idindex) {

        Disposable disposable = model.updateScore(srid, mobile, flag, uid, appid, idindex, new ContentContract.ScoreCallback() {
            @Override
            public void success(ScoreBean scoreBean) {

                if (srid == 40) {

                    if (scoreBean.getResult().equals("200")) {

                        view.deductScore(scoreBean);
                    } else {

                        view.toast(scoreBean.getMessage());
                    }
                } else {

                    if (scoreBean.getResult().equals("200")) {

                        view.toast("增加" + scoreBean.getAddcredit() + "积分");
                    } else {

                    }
                }
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
