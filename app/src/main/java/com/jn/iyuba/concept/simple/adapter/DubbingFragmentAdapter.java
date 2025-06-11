package com.jn.iyuba.concept.simple.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jn.iyuba.concept.simple.activity.fragment.VideoOriFragment;
import com.jn.iyuba.concept.simple.activity.fragment.VideoRankFragment;

import java.util.List;

public class DubbingFragmentAdapter extends FragmentStateAdapter {


    private String[] nav;

    private int voaid;

    public DubbingFragmentAdapter(@NonNull Fragment fragment, String[] nav, int voaid) {
        super(fragment);
        this.nav = nav;
        this.voaid = voaid;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        String navName = nav[position];
        if (navName.equals("原文")) {

            return VideoOriFragment.newInstance(voaid);
        } else if (navName.equals("排行")) {

            return VideoRankFragment.newInstance(voaid);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return nav.length;
    }
}
