package com.jn.iyuba.novel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jn.iyuba.novel.db.NovelBook;
import com.jn.iyuba.novel.model.NetWorkManager;

public class NovelApplication extends Application {

    private static Context context;


    public static Context getContext() {
        return context;
    }

    public static void init(Context context) {
        NovelApplication.context = context;


        SharedPreferences bookSP = context.getSharedPreferences(NovelConstant.SP_BOOK, MODE_PRIVATE);
        String bookStr = bookSP.getString(NovelConstant.SP_KEY_BOOK, "");

        //是否选择课本的数据，没有则使用默认的课本
        if (bookStr.equals("")) {

            String bookDefStr = "{\n" +
                    "            \"types\": \"newCamstory\",\n" +
                    "            \"orderNumber\": \"0\",\n" +
                    "            \"level\": \"0\",\n" +
                    "            \"bookname_en\": \"What a Lottery\",\n" +
                    "            \"author\": \"Colin Campbell\",\n" +
                    "            \"about_book\": \"里克是个穷困潦倒的歌手，他的妻子离开了他。他买了一张彩票，结果中了巨奖。他想要告诉妻子这个好消息，却在去找妻子的路上遇到了逃犯巴里。巴里和里克之间发生了怎样的争抢？里克能否顺利兑奖？之后又发生了什么离奇风波？彩票给里克带来的是麻烦还是好运？\",\n" +
                    "            \"bookname_cn\": \"彩票风波\",\n" +
                    "            \"about_interpreter\": \"河北保定市第十七中学英语一级教师。荣获过第六届全国中学英语教师教学技能大赛一等奖、第二届保定市中学英语教师技能大赛一等奖。著有多篇论文，其辅导的学生在国家级、省级英语竞赛中多次获奖。\",\n" +
                    "            \"wordcounts\": \"37千字\",\n" +
                    "            \"interpreter\": \"吴如妹\",\n" +
                    "            \"pic\": \"/newCamstory/images/0_0.jpg\",\n" +
                    "            \"about_author\": \"英语语言教育专家，拥有近三十年的一线英语教学经历，在欧洲各国担任英语教师、教师培训师和顾问。他在意大利和波兰创办语言学校，在意大利一家电视台主持英语学习节目，创作过多部英语学习小说。在“剑桥双语分级阅读•小说馆”系列中，他创作了《彩票风波》《平行世界》等读本。\"\n" +
                    "        }";
            NovelConstant.novelBook = new Gson().fromJson(bookDefStr, NovelBook.class);
        } else {

            NovelConstant.novelBook = new Gson().fromJson(bookStr, NovelBook.class);
        }


        NetWorkManager.getInstance().init();


    }

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
