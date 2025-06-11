package com.jn.iyuba.concept.simple.view.home;

import com.jn.iyuba.concept.simple.model.BaseModel;
import com.jn.iyuba.concept.simple.model.bean.LessonDetailBean;
import com.jn.iyuba.concept.simple.model.bean.PdfBean;
import com.jn.iyuba.concept.simple.model.bean.ScoreBean;
import com.jn.iyuba.concept.simple.presenter.IBasePresenter;
import com.jn.iyuba.concept.simple.view.LoadingView;
import com.jn.iyuba.novel.model.bean.NovelBookBean;
import com.jn.iyuba.novel.model.bean.NovelSentenceBean;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ContentContract {

    interface ContentView extends LoadingView {

        void getSentenceComplete(LessonDetailBean jpSentenceBean);

        void getConceptPdfFile(PdfBean pdfBean);


        void getStroryInfo(NovelSentenceBean novelSentenceBean);


        /**
         * 扣除积分成功
         *
         * @param scoreBean
         */
        void deductScore(ScoreBean scoreBean);
    }

    interface ContentPresenter extends IBasePresenter<ContentView> {

        void getConceptSentence(int voaid);


        void getConceptPdfFile(int voaid, int type);

        void textExamApi(int voaid);

        void getVoaPdfFile_new(String type, int voaid, int isenglish);

        void getStroryInfo(String types, int level,
                           String orderNumber, String chapterOrder, String from);

        void getBookWormPdf(String voaid, String type, String language);

        void updateScore(int srid, int mobile, String flag, String uid, int appid, String idindex);
    }

    interface ContentModel extends BaseModel {

        Disposable getConceptSentence(int voaid, SentenceCallback sentenceCallback);


        Disposable getConceptPdfFile(int voaid, int type, PdfCallback callback);

        Disposable textExamApi(int voaid, SentenceCallback sentenceCallback);

        Disposable getVoaPdfFile_new(String type, int voaid, int isenglish, PdfCallback callback);


        Disposable getStroryInfo(String types, int level,
                                 String orderNumber, String chapterOrder, String from, NovelSentenceCallback novelSentenceCallback);

        Disposable getBookWormPdf(String voaid, String type, String language, PdfCallback pdfCallback);

        Disposable updateScore(int srid, int mobile, String flag, String uid, int appid, String idindex, ScoreCallback scoreCallback);
    }

    interface ScoreCallback {

        void success(ScoreBean scoreBean);

        void error(Exception e);
    }

    interface NovelSentenceCallback {

        void success(NovelSentenceBean novelSentenceBean);

        void error(Exception e);
    }

    interface PdfCallback {
        void success(PdfBean pdfBean);

        void error(Exception e);
    }

    interface SentenceCallback {

        void success(LessonDetailBean lessonDetailBean);

        void error(Exception e);
    }
}
