package com.jn.iyuba.novel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jn.iyuba.novel.ChooseLessonFragment;
import com.jn.iyuba.novel.R;
import com.jn.iyuba.novel.databinding.NovelActivityChooseLessonBinding;

/**
 * 选择课本
 */
public class ChooseLessonActivity extends AppCompatActivity {

    private NovelActivityChooseLessonBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NovelActivityChooseLessonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.cl_fl_fragment, ChooseLessonFragment.newInstance())
                .commit();
    }
}