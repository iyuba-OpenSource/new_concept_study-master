package com.jn.iyuba.concept.simple.model.home;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.model.NetWorkManager;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.model.bean.PdfBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.view.home.ContentContract;
import com.jn.iyuba.novel.model.bean.NovelSentenceBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContentModel implements ContentContract.ContentModel {


    @Override
    public Disposable getConceptSentence(int voaid, ContentContract.SentenceCallback sentenceCallback) {

        return NetWorkManager
                .getRequest()
                .getConceptSentence(voaid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LessonDetailBean>() {
                    @Override
                    public void accept(LessonDetailBean lessonDetailBean) throws Exception {

                        sentenceCallback.success(lessonDetailBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        sentenceCallback.error((Exception) throwable);
                    }
                });
    }


    @Override
    public Disposable getConceptPdfFile(int voaid, int type, ContentContract.PdfCallback callback) {

        return NetWorkManager
                .getRequest()
                .getConceptPdfFile(voaid, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PdfBean>() {
                    @Override
                    public void accept(PdfBean pdfBean) throws Exception {

                        callback.success(pdfBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable textExamApi(int voaid, ContentContract.SentenceCallback sentenceCallback) {

        return NetWorkManager
                .getRequest()
                .textExamApi(Constant.TEXT_EXAM_API, voaid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LessonDetailBean>() {
                    @Override
                    public void accept(LessonDetailBean lessonDetailBean) throws Exception {

                        sentenceCallback.success(lessonDetailBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        sentenceCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getVoaPdfFile_new(String type, int voaid, int isenglish, ContentContract.PdfCallback callback) {

        return NetWorkManager
                .getRequest()
                .getVoaPdfFile_new(Constant.GET_VOA_PDF_FILE_NEW, type, voaid, isenglish)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PdfBean>() {
                    @Override
                    public void accept(PdfBean pdfBean) throws Exception {

                        callback.success(pdfBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getStroryInfo(String types, int level, String orderNumber, String chapterOrder, String from, ContentContract.NovelSentenceCallback novelSentenceCallback) {

        return NetWorkManager
                .getRequest()
                .getStroryInfo(Constant.GET_STRORY_INFO, types, level, orderNumber, chapterOrder, from)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NovelSentenceBean>() {
                    @Override
                    public void accept(NovelSentenceBean novelSentenceBean) throws Exception {

                        novelSentenceCallback.success(novelSentenceBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        novelSentenceCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getBookWormPdf(String voaid, String type, String language, ContentContract.PdfCallback pdfCallback) {

        return NetWorkManager
                .getRequest()
                .getBookWormPdf(Constant.GET_BOOK_WORM_PDF, voaid, type, language)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PdfBean>() {
                    @Override
                    public void accept(PdfBean pdfBean) throws Exception {

                        pdfCallback.success(pdfBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        pdfCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateScore(int srid, int mobile, String flag, String uid, int appid, String idindex, ContentContract.ScoreCallback scoreCallback) {

        return NetWorkManager
                .getRequest()
                .updateScore(Constant.UPDATE_SCORE, srid, mobile, flag, uid, appid, idindex)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ScoreBean>() {
                    @Override
                    public void accept(ScoreBean scoreBean) throws Exception {

                        scoreCallback.success(scoreBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        scoreCallback.error((Exception) throwable);
                    }
                });
    }
}
