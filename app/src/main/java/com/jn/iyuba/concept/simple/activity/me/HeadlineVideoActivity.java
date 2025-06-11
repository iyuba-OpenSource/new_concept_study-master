package com.jn.iyuba.concept.simple.activity.me;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.ActivityHeadlineVideoBinding;

/**
 * 视频模块
 */
public class HeadlineVideoActivity extends AppCompatActivity {

    private ActivityHeadlineVideoBinding activityHeadlineVideoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHeadlineVideoBinding = ActivityHeadlineVideoBinding.inflate(getLayoutInflater());
        setContentView(activityHeadlineVideoBinding.getRoot());

        String[] types = {HeadlineType.SMALLVIDEO};
        Bundle bundle = DropdownTitleFragmentNew.buildArguments(10, types, true);
        DropdownTitleFragmentNew fragment = DropdownTitleFragmentNew.newInstance(bundle);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.hv_fl, fragment);
        fragmentTransaction.commit();
    }
}