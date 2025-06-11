package com.jn.iyuba.concept.simple.presenter.me;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.iyuba.module.toolbox.MD5;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.db.ConceptWord;
import com.jn.iyuba.concept.simple.db.Exercise;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.model.bean.MoreInfoBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncEvalBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncListenBean;
import com.jn.iyuba.concept.simple.model.bean.sync.SyncWordBean;
import com.jn.iyuba.concept.simple.model.home.MeModel;
import com.jn.iyuba.concept.simple.presenter.BasePresenter;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.view.me.MeContract;

import org.litepal.LitePal;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class MePresenter extends BasePresenter<MeContract.MeView, MeContract.MeModel>
        implements MeContract.MePresenter {


    private int page = 1;


    @Override
    protected MeContract.MeModel initModel() {
        return new MeModel();
    }

    @Override
    public void getStudyRecordByTestMode(String format, int uid, int Pageth, int NumPerPage, int TestMode, String sign, String Lesson) {

        view.showLoading("正在同步数据...");
        Disposable disposable = model.getStudyRecordByTestMode(format, uid, Pageth, NumPerPage, TestMode, sign, Lesson, new MeContract.Callback() {
            @Override
            public void success(SyncListenBean syncListenBean) {

                if (syncListenBean.getResult().equals("1")) {

                    List<SyncListenBean.DataDTO> dataDTOList = syncListenBean.getData();
                    for (int i = 0; i < dataDTOList.size(); i++) {

                        SyncListenBean.DataDTO dataDTO = dataDTOList.get(i);
                        if (!dataDTO.getLesson().equalsIgnoreCase(Constant.TYPE)) {

                            continue;
                        }
                        List<Title> titleList = LitePal.where(" voaid = ?", dataDTO.getLessonId()).find(Title.class);
                        if (titleList.size() > 0) {//有数据

                            Title title = titleList.get(0);
                            if (title.getTestNumber() == 0) {//进度为0则更新

                                title.setEndFlg(Integer.parseInt(dataDTO.getEndFlg()));
                                title.setTestNumber(Integer.parseInt(dataDTO.getTestNumber()));
                                title.updateAll(" voaid = ? and bookid = ? and language = ?", dataDTO.getLessonId(), title.getBookid() + "", title.getLanguage());
                            } else {//有进度

                                if (title.getSyncUpdateTime() == null) {//进度记录时间为null

                                    title.setEndFlg(Integer.parseInt(dataDTO.getEndFlg()));
                                    title.setTestNumber(Integer.parseInt(dataDTO.getTestNumber()));
                                    title.updateAll("  voaid = ?  and bookid = ? and language = ?", dataDTO.getLessonId(), title.getBookid() + "", title.getLanguage());
                                } else {//有进度时间

                                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                                    simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
                                    try {
                                        long saveTime = simpleDateFormat.parse(title.getSyncUpdateTime()).getTime();
                                        long dataTime = simpleDateFormat.parse(dataDTO.getEndTime()).getTime();
                                        if (saveTime < dataTime) {//存储的时间小于数据的时间

                                            title.setEndFlg(Integer.parseInt(dataDTO.getEndFlg()));
                                            title.setTestNumber(Integer.parseInt(dataDTO.getTestNumber()));
                                            title.setSyncUpdateTime(dataDTO.getEndTime());
                                            title.updateAll(" voaid = ?  and bookid = ? and language = ?", dataDTO.getLessonId(), title.getBookid() + "", title.getLanguage());
                                        }
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        } //
                    }

                    if (syncListenBean.getData().size() == NumPerPage) {

                        page++;
                        handler.sendEmptyMessageDelayed(1, 400);
                    } else {//同步完成

                        getTestRecord(Constant.userinfo.getUid() + "", Constant.TYPE);
                    }
                }
            }

            @Override
            public void error(Exception e) {

                Log.d("同步", "听力同步");
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getTestRecord(String userId, String newstype) {

        Disposable disposable = model.getTestRecord(userId, newstype, new MeContract.SyncEvalCallback() {
            @Override
            public void success(SyncEvalBean syncEvalBean) {


                if (syncEvalBean.getResult().equals("1")) {

//                    DecimalFormat decimalFormat = new DecimalFormat("#000");
//                    DecimalFormat dF00 = new DecimalFormat("#00");

                    List<SyncEvalBean.DataDTO> dataDTOList = syncEvalBean.getData();
                    for (int i = 0; i < dataDTOList.size(); i++) {

                        SyncEvalBean.DataDTO dataDTO = dataDTOList.get(i);
                        List<Sentence> sentenceList = LitePal
                                .where("voaid = ? and paraid = ? and idindex = ? and sentence = ?",
                                        dataDTO.getNewsid() + "", dataDTO.getParaid() + "", dataDTO.getIdindex() + "", dataDTO.getSentence())
                                .find(Sentence.class);
                        if (sentenceList.size() > 0) {//有数据更新

                            Sentence sentence = sentenceList.get(0);
                            sentence.setScore(dataDTO.getScore());
                            sentence.setRecordSoundUrl(dataDTO.getUrl());
                            sentence.updateAll("voaid = ? and paraid = ? and idindex = ? and sentence = ?",
                                    dataDTO.getNewsid() + "", dataDTO.getNewstype(), dataDTO.getIdindex() + "", dataDTO.getSentence());
                        } /*else {//没数据则添加

                            Sentence sentence = new Sentence();
                            sentence.setSourceid(decimalFormat.format(dataDTO.getNewsid()));
                            sentence.setSource(dataDTO.getNewstype());
                            sentence.setSentence(dataDTO.getSentence());
                            sentence.setScore(dataDTO.getScore());
                            sentence.setIdindex(dF00.format(dataDTO.getIdindex()));
                            sentence.setRecordSoundUrl(dataDTO.getUrl());
                            sentence.save();
                        }*/
                    }
                }


                String lesson = Constant.book.getId();
                String signStr = Constant.userinfo.getUid() + lesson + 2 + "W" + Constant.APPID + DateUtil.getCurDate();
                String sign = MD5Util.MD5(signStr);
                getExamDetail(Constant.APPID, Constant.userinfo.getUid() + "", lesson, "W",
                        2, sign, "json");

            }

            @Override
            public void error(Exception e) {

                Log.d("同步", "句子评测同步");
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getExamDetail(int appId, String uid, String lesson, String TestMode, int mode, String sign, String format) {

        Disposable disposable = model.getExamDetail(appId, uid, lesson, TestMode, mode, sign, format, new MeContract.SyncWordCallback() {
            @Override
            public void success(SyncWordBean syncWordBean) {

                if (syncWordBean.getResult() == 1) {

                    List<SyncWordBean.WordDataDTO> wrongDataDTOList = syncWordBean.getDataWrong();
                    for (int i = 0; wrongDataDTOList != null && i < wrongDataDTOList.size(); i++) {

                        SyncWordBean.WordDataDTO wordDataDTO = wrongDataDTOList.get(i);
                        List<ConceptWord> wordList = LitePal.where("bookid = ? and voaid = ? and position = ?"
                                , syncWordBean.getLesson(), wordDataDTO.getLessonId() + "", wordDataDTO.getTestId()).find(ConceptWord.class);
                        if (wordList.size() > 0) {//更新

                            ConceptWord word = wordList.get(0);
                            word.setAnswer_status(2);
                            word.updateAll("bookid = ? and voaid = ? and position = ?", syncWordBean.getLesson(), wordDataDTO.getLessonId(), wordDataDTO.getTestId());
                        }/* else {//添加数据

                            ConceptWord jpWord = new ConceptWord();
                            jpWord.setAnswer_status(2);
                            jpWord.setId(wordDataDTO.getId());
                            jpWord.save();
                        }*/
                    }
                    //正确
                    List<SyncWordBean.WordDataDTO> rightDataDTOList = syncWordBean.getDataRight();
                    for (int i = 0; rightDataDTOList != null && i < rightDataDTOList.size(); i++) {

                        SyncWordBean.WordDataDTO wordDataDTO = rightDataDTOList.get(i);
                        List<ConceptWord> wordList = LitePal
                                .where("bookid = ? and voaid = ? and position = ?", syncWordBean.getLesson(), wordDataDTO.getLessonId(), wordDataDTO.getTestId())
                                .find(ConceptWord.class);
                        if (wordList.size() > 0) {//更新

                            ConceptWord word = wordList.get(0);
                            word.setAnswer_status(1);
                            word.updateAll("bookid = ? and voaid = ? and position = ?", syncWordBean.getLesson(), wordDataDTO.getLessonId(), wordDataDTO.getTestId());
                        }/* else {//添加数据

                            JpWord jpWord = new JpWord();
                            jpWord.setAnswer_status(1);
                            jpWord.setId(wordDataDTO.getId());
                            jpWord.save();
                        }*/
                    }
                }

              /*  view.hideLoading();
                view.toast("同步数据完成");*/
                String sign = MD5Util.MD5(Constant.userinfo.getUid() + DateUtil.getCurDate());
                getTestRecordDetail(Constant.APPID + "", Constant.userinfo.getUid() + "", "10"
                        , sign, "json", 1 + "", "10");
            }

            @Override
            public void error(Exception e) {

                Log.d("同步", "单词闯关");
            }
        });
        addSubscribe(disposable);
    }


    /**
     * 判断
     *
     * @param answer
     */
    private boolean isChoose(String answer) {

        if (answer.equals("A")) {

            return true;
        } else if (answer.equals("B")) {

            return true;
        } else if (answer.equals("C")) {

            return true;
        } else if (answer.equals("D")) {

            return true;
        }
        return false;
    }

    @Override
    public void getTestRecordDetail(String appId, String uid, String TestMode, String sign, String format, String Pageth, String NumPerPage) {

        Disposable disposable = model.getTestRecordDetail(appId, uid, TestMode, sign, format, Pageth, NumPerPage, new MeContract.SyncExerciseCallback() {
            @Override
            public void success(SyncExerciseBean syncExerciseBean) {

                if (syncExerciseBean.getResult().equals("1")) {

                    List<SyncExerciseBean.DataDTO> dataDTOList = syncExerciseBean.getData();
                    for (int i = 0; i < dataDTOList.size(); i++) {

                        SyncExerciseBean.DataDTO dataDTO = dataDTOList.get(i);

                        if (dataDTO.getAppId().equals(Constant.APPID + "") && dataDTO.getAppName().equalsIgnoreCase("concept")) {//如果是222

                            if (isChoose(dataDTO.getUserAnswer())) {//是否是选择题

                                List<Exercise> exerciseList = LitePal
                                        .where("voaid = ? and indexid = ?", dataDTO.getLessonId(), dataDTO.getTestNumber()).find(Exercise.class);

                                if (exerciseList.size() > 0) {//如果存在

                                    Exercise exercise = exerciseList.get(0);
                                    if (exercise.getCheckAnswer() == null || exercise.getCheckAnswer().equals("")) {

                                        if (dataDTO.getUserAnswer().equalsIgnoreCase("A")) {

                                            exercise.setCheckAnswer(exercise.getChoiceA());
                                            exercise.setCheckPosition(0);
                                        } else if (dataDTO.getUserAnswer().equalsIgnoreCase("B")) {

                                            exercise.setCheckAnswer(exercise.getChoiceB());
                                            exercise.setCheckPosition(1);
                                        } else if (dataDTO.getUserAnswer().equalsIgnoreCase("C")) {

                                            exercise.setCheckAnswer(exercise.getChoiceC());
                                            exercise.setCheckPosition(2);
                                        } else {

                                            exercise.setCheckAnswer(exercise.getChoiceD());
                                            exercise.setCheckPosition(3);
                                        }
                                        exercise.updateAll("voaid = ? and indexid = ?", dataDTO.getLessonId(), dataDTO.getTestNumber());
                                    }
                                }
                            } else {//填空題

                                List<Exercise> exerciseList = LitePal
                                        .where("voaid = ? and number = ?", dataDTO.getLessonId(), dataDTO.getTestNumber()).find(Exercise.class);
                                if (exerciseList.size() > 0) {

                                    Exercise exercise = exerciseList.get(0);

                                    if (exercise.getCheckAnswer() == null || exercise.getCheckAnswer().equals("")) {

                                        exercise.setCheckAnswer(dataDTO.getUserAnswer());
                                        exercise.updateAll("voaid = ? and number = ?", dataDTO.getLessonId(), dataDTO.getTestNumber());
                                    }
                                }
                            }
                        }
                    }


                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (Integer.parseInt(NumPerPage) == syncExerciseBean.getData().size()) {

                                int page = Integer.parseInt(Pageth) + 1;
                                String sign = MD5Util.MD5(Constant.userinfo.getUid() + DateUtil.getCurDate());
                                getTestRecordDetail(Constant.APPID + "", Constant.userinfo.getUid() + "", "10"
                                        , sign, "json", page + "", "10");
                            } else {
                                view.hideLoading();
                                view.toast("同步完成");
                            }
                        }
                    }, 300);
                    //更新數據
                }
            }

            @Override
            public void error(Exception e) {

                Log.d("同步", "练习");
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void islatest(String url, int currver, String packageStr) {

        Disposable disposable = model.islatest(url, currver, packageStr, new MeContract.LatestCallback() {
            @Override
            public void success(ResponseBody responseBody) {

                view.updateApp(responseBody);
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
    public void getMoreInfo(String platform, int protocol, int id, int myid, int appid, String sign) {

        Disposable disposable = model.getMoreInfo(platform, protocol, id, myid, appid, sign, new MeContract.MoreCallback() {
            @Override
            public void success(MoreInfoBean moreInfoBean) {

                if (moreInfoBean.getResult() == 201) {//成功获取

                    view.moreInfoComplete(moreInfoBean);
                } else {
                    view.toast(moreInfoBean.getMessage());
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            String sign = MD5.getMD5ofStr(Constant.userinfo.getUid() + DateUtil.getCurDate());
            getStudyRecordByTestMode("json", Constant.userinfo.getUid(), page, 10, 1, sign, Constant.TYPE);
            return false;
        }
    });
}
