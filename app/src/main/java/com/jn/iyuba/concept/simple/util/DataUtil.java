package com.jn.iyuba.concept.simple.util;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jn.iyuba.concept.simple.db.Exercise;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.db.ConceptWord;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 预存数据
 */
public class DataUtil {


    /**
     * 初始化课程数据表
     */
    public static void addTitleDta(AssetManager assetManager, String jsonName) {

        try {
            InputStream inputStream = assetManager.open(jsonName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Title>>() {
            }.getType();

            String content = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            int index = 0;

            while (content != null) {

                content = content.trim();
                if (content.equals("[") || content.equals("]")) {

                    content = bufferedReader.readLine();
                    continue;
                } else if (content.equals("},")) {

                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    index++;
                } else {

                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    continue;
                }


                if (index == 50) {

                    stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                    stringBuilder.append("]");

                    List<Title> titleList = gson.fromJson(stringBuilder.toString(), type);
                    for (Title title : titleList) {

                        if (BookUtil.isYouthBook(title.getBookid())) {

                            int count = LitePal.where("voaid = ?", title.getVoaId()).count(Title.class);
                            if (count == 0) {
                                title.save();
                            }
                        } else {

                            int count = LitePal.where("bookid = ? and voaid = ? and language = ?", title.getBookid() + "", title.getVoaId(), title.getLanguage()).count(Title.class);
                            if (count == 0) {
                                title.save();
                            }
                        }

                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    stringBuilder.append("[");
                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    index = 0;
                }
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            if (index != 0) {

                stringBuilder.append("]");
                List<Title> titleList = gson.fromJson(stringBuilder.toString(), type);
                for (Title title : titleList) {
                    title.save();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 初始化句子数据表
     */
    public static void addSentenceData(AssetManager assetManager, String jsonName) {

        try {
            InputStream inputStream = assetManager.open(jsonName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Sentence>>() {
            }.getType();

            String content = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            int index = 0;

            while (content != null) {

                content = content.trim();
                if (content.equals("[") || content.equals("]")) {

                    content = bufferedReader.readLine();
                    continue;
                } else if (content.equals("},")) {

                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    index++;
                } else {

                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    continue;
                }


                if (index == 100) {

                    stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                    stringBuilder.append("]");

                    List<Sentence> sentenceList = gson.fromJson(stringBuilder.toString(), type);
                    for (Sentence sentence : sentenceList) {

                        int count = LitePal.where("voaid = ? and paraid =? and idindex = ?", sentence.getVoaid(), sentence.getParaid(), sentence.getIdIndex())
                                .count(Sentence.class);
                        if (count == 0) {

                            sentence.save();
                        }
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    stringBuilder.append("[");
                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    index = 0;
                }
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            if (index != 0) {

                stringBuilder.append("]");
                List<Sentence> sentenceList = gson.fromJson(stringBuilder.toString(), type);
                for (Sentence sentence : sentenceList) {
                    sentence.save();
                }
            }

        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 初始化句子数据表
     */
    public static void addExerciseDta(AssetManager assetManager, String jsonName) {

        try {
            InputStream inputStream = assetManager.open(jsonName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Exercise>>() {
            }.getType();

            String content = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            int index = 0;

            while (content != null) {

                content = content.trim();
                if (content.equals("[") || content.equals("]")) {

                    content = bufferedReader.readLine();
                    continue;
                } else if (content.equals("},")) {

                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    index++;
                } else {

                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    continue;
                }


                if (index == 100) {

                    stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                    stringBuilder.append("]");

                    List<Exercise> exerciseList = gson.fromJson(stringBuilder.toString(), type);
                    for (Exercise exercise : exerciseList) {

                        if (exercise.getNote() == null) {//选择

                            int count = LitePal.where("voaid = ? and indexid = ?", exercise.getVoaId(), exercise.getIndexId()).count(Exercise.class);
                            if (count == 0) {

                                exercise.save();
                            }
                        } else {//填空

                            int count = LitePal.where("voaid = ? and number = ?", exercise.getVoaId(), exercise.getNumber()).count(Exercise.class);
                            if (count == 0) {

                                exercise.save();
                            }
                        }
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    stringBuilder.append("[");
                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    index = 0;
                }
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            if (index != 0) {

                stringBuilder.append("]");
                List<Exercise> exerciseList = gson.fromJson(stringBuilder.toString(), type);
                for (Exercise exercise : exerciseList) {
                    exercise.save();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 初始化句子数据表
     */
    public static void addWordData(AssetManager assetManager, String jsonName) {

        try {
            InputStream inputStream = assetManager.open(jsonName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Gson gson = new Gson();
            Type type = new TypeToken<List<ConceptWord>>() {
            }.getType();

            String content = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            int index = 0;

            while (content != null) {

                content = content.trim();
                if (content.equals("[") || content.equals("]")) {

                    content = bufferedReader.readLine();
                    continue;
                } else if (content.equals("},")) {

                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    index++;
                } else {

                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    continue;
                }


                if (index == 100) {

                    stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                    stringBuilder.append("]");

                    List<ConceptWord> conceptWordList = gson.fromJson(stringBuilder.toString(), type);
                    for (ConceptWord conceptWord : conceptWordList) {

                        int count = LitePal.where("bookid = ? and position = ? and voaid =?"
                                , conceptWord.getBookid() + "", conceptWord.getPosition(), conceptWord.getVoaId() + "").count(ConceptWord.class);
                        if (count == 0) {

                            conceptWord.save();
                        }
                    }
                    stringBuilder.delete(0, stringBuilder.length());
                    stringBuilder.append("[");
                    stringBuilder.append(content);
                    content = bufferedReader.readLine();
                    index = 0;
                }
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            if (index != 0) {

                stringBuilder.append("]");
                List<ConceptWord> conceptWordList = gson.fromJson(stringBuilder.toString(), type);
                for (ConceptWord conceptWord : conceptWordList) {
                    conceptWord.save();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
