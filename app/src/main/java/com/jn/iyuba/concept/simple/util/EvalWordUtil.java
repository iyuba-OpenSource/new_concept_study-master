package com.jn.iyuba.concept.simple.util;

import com.jn.iyuba.concept.simple.db.EvalWord;
import com.jn.iyuba.novel.db.NovelEvalWord;

import java.util.ArrayList;
import java.util.List;

/**
 * 评测单词转换工具
 */
public class EvalWordUtil {


    public static NovelEvalWord evalWordToNovelEvalWord(EvalWord evalWord, String chapter_order, String order_number, String types, String level) {

        NovelEvalWord novelEvalWord = new NovelEvalWord();
        novelEvalWord.setIndex(evalWord.getIndex());
        novelEvalWord.setContent(evalWord.getContent());
        novelEvalWord.setPron(evalWord.getPron());
        novelEvalWord.setPron2(evalWord.getPron2());
        novelEvalWord.setScore(evalWord.getScore());
        novelEvalWord.setUserPron(evalWord.getUserPron());
        novelEvalWord.setUserPron2(evalWord.getUserPron2());

        novelEvalWord.setParaid(evalWord.getParaid());
        novelEvalWord.setSenIndex(evalWord.getSenIndex());
        novelEvalWord.setChapter_order(chapter_order);
        novelEvalWord.setOrder_number(order_number);
        novelEvalWord.setTypes(types);
        novelEvalWord.setLevel(level);
        novelEvalWord.setVoaid(evalWord.getVoaid());

        return novelEvalWord;
    }

    /**
     * 小说馆的评测单词 转换成 新概念的评测单词
     *
     * @param novelEvalWordList
     * @return
     */
    public static List<EvalWord> novelWordToEvalWord(List<NovelEvalWord> novelEvalWordList) {

        List<EvalWord> evalWordList = new ArrayList<>();
        for (int i = 0; i < novelEvalWordList.size(); i++) {

            NovelEvalWord novelEvalWord = novelEvalWordList.get(i);
            EvalWord evalWord = new EvalWord();
            evalWord.setIndex(novelEvalWord.getIndex());
            evalWord.setContent(novelEvalWord.getContent());
            evalWord.setScore(novelEvalWord.getScore());
            evalWordList.add(evalWord);
        }
        return evalWordList;
    }
}
