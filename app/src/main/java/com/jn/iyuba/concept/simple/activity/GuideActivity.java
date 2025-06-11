package com.jn.iyuba.concept.simple.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MainActivity;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.GuideAdapter;
import com.jn.iyuba.succinct.databinding.ActivityGuideBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends AppCompatActivity {

    private GuideAdapter guideAdapter;

    private ActivityGuideBinding activityGuideBinding;

    private List<Integer> resList = new ArrayList<>();

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGuideBinding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(activityGuideBinding.getRoot());


        sp = getSharedPreferences(Constant.SP_PRIVACY, MODE_PRIVATE);

        activityGuideBinding.guideButEnter.setVisibility(View.GONE);
        activityGuideBinding.guideButEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp.edit().putInt(Constant.SP_KEY_GUIDE_STATE, 1).apply();
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

        resList.add(R.drawable.guide_1);
        resList.add(R.drawable.guide_2);
        resList.add(R.drawable.guide_3);
        resList.add(R.drawable.guide_4);
        resList.add(R.drawable.guide_5);


        guideAdapter = new GuideAdapter(resList);

        activityGuideBinding.guideVp2.setAdapter(guideAdapter);
        activityGuideBinding.guideVp2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        activityGuideBinding.guideVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == resList.size() - 1) {

                    activityGuideBinding.guideButEnter.setVisibility(View.VISIBLE);
                } else {

                    activityGuideBinding.guideButEnter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }
}