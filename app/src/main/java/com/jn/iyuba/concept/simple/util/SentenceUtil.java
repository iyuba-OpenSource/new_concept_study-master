package com.jn.iyuba.concept.simple.util;

import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.db.NovelEvalWord;
import com.jn.iyuba.novel.db.NovelSentence;
import com.jn.iyuba.novel.db.dao.NovelEvalWordDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Sentence类转换的工具类
 */
public class SentenceUtil {


    /**
     * 单句转换
     *
     * @param novelSentence
     * @return
     */
    public static Sentence convert(NovelSentence novelSentence) {

        Sentence sentence = new Sentence();
        sentence.setVoaid(novelSentence.getVoaid());
        sentence.setSentenceCn(novelSentence.getTextCH());
        sentence.setSentence(novelSentence.getTextEN());
        sentence.setTypes(novelSentence.getTypes());
        sentence.setEndTiming(novelSentence.getEndTiming());
        sentence.setTiming(novelSentence.getBeginTiming());
        sentence.setIdIndex(novelSentence.getIndex());
        sentence.setParaid(novelSentence.getParaid());
        sentence.setScore(novelSentence.getScore());
        sentence.setRecordSoundUrl(novelSentence.getRecordSoundUrl());
        sentence.setShuoshuoId(novelSentence.getShuoshuoId());
        sentence.setSentence_audio(novelSentence.getSentenceAudio());
        sentence.setLevel(novelSentence.getLevel());
        sentence.setOrderNumber(novelSentence.getOrderNumber());
        sentence.setChapter_order(novelSentence.getChapterOrder());

        return sentence;
    }

    /**
     * 多句转换
     *
     * @param novelSentences
     * @return
     */
    public static List<Sentence> convert(List<NovelSentence> novelSentences) {


        AppDatabase appDatabase = NovelDB.getInstance().getDB();
        NovelEvalWordDao novelEvalWordDao = appDatabase.novelEvalWordDao();

        List<Sentence> sentenceList = new ArrayList<>();
        for (int i = 0; i < novelSentences.size(); i++) {

            NovelSentence novelSentence = novelSentences.get(i);
            Sentence sentence = convert(novelSentence);

            //获取评测的数据
            List<NovelEvalWord> novelEvalWords = novelEvalWordDao.getEvalWord(novelSentence.getTypes(), novelSentence.getVoaid(), novelSentence.getParaid(), novelSentence.getIndex(), novelSentence.getChapterOrder(),
                    novelSentence.getOrderNumber(), novelSentence.getLevel());
            if (novelEvalWords != null && novelEvalWords.size() != 0) {//设置评测的数据

                sentence.setWordsDTOS(EvalWordUtil.novelWordToEvalWord(novelEvalWords));
            }
            sentenceList.add(sentence);
        }
        return sentenceList;
    }


}
