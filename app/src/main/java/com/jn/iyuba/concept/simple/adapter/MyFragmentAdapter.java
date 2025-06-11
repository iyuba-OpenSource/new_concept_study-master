package com.jn.iyuba.concept.simple.adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.fragment.EvaluatingFragment;
import com.jn.iyuba.concept.simple.activity.fragment.OriginalFragment;
import com.jn.iyuba.concept.simple.activity.fragment.ParaFragment;
import com.jn.iyuba.concept.simple.activity.fragment.PointReadingFragment;
import com.jn.iyuba.concept.simple.activity.fragment.QuestionFragment;
import com.jn.iyuba.concept.simple.activity.fragment.RankFragment;
import com.jn.iyuba.concept.simple.activity.fragment.WordFragment;
import com.jn.iyuba.concept.simple.activity.fragment.YouthVideoFragment;


public class MyFragmentAdapter extends FragmentStateAdapter {


    private String[] nav;
    private int voaid;

    private boolean isBookWorm = false;

    private int chapterOrder = 0;

    private String lessonHome;

    public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity, int voaid, String[] nav, boolean isBookWorm, int chapterOrder) {
        super(fragmentActivity);
        this.voaid = voaid;
        this.nav = nav;
        this.isBookWorm = isBookWorm;
        this.chapterOrder = chapterOrder;
        SharedPreferences lessonHomeSp = MyApplication.getContext().getSharedPreferences(Constant.SP_LESSON, Context.MODE_PRIVATE);
        lessonHome = lessonHomeSp.getString(Constant.SP_KEY_LESSON, "原文");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        String navStr = nav[position];
        if (navStr.equals("听力")) {

            boolean isAutoPlay = true;
            if (lessonHome.equals("原文")) {
            } else {
                isAutoPlay = false;
            }
            return OriginalFragment.newInstance(voaid, isBookWorm, chapterOrder, isAutoPlay);
        } else if (navStr.equals("AI评测")) {

            return EvaluatingFragment.newInstance(voaid, isBookWorm, chapterOrder);
        } else if (navStr.equals("视频")) {

            return YouthVideoFragment.newInstance(voaid);
        } else if (navStr.equals("练习")) {

            return QuestionFragment.newInstance(voaid);
        } else if (navStr.equals("阅读")) {

            return ParaFragment.newInstance(voaid, chapterOrder, isBookWorm);
        } else if (navStr.equals("单词")) {

            return WordFragment.newInstance(voaid);
        } else if (navStr.equals("点读")) {

            return PointReadingFragment.newInstance(voaid);
        } else {

            return RankFragment.newInstance(voaid, isBookWorm);
        }
    }

    @Override
    public int getItemCount() {
        return nav.length;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setVoaid(int voaid) {
        this.voaid = voaid;
    }

    public int getVoaid() {
        return voaid;
    }
}
