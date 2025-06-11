package com.jn.iyuba.concept.simple.activity.me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.activity.ContentActivity;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.me.MyDownloadAdapter;
import com.jn.iyuba.succinct.databinding.ActivityMyDownloadBinding;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的下载
 */
public class MyDownloadActivity extends AppCompatActivity {


    private MyDownloadAdapter myDownloadAdapter;

    private ActivityMyDownloadBinding binding;

    private LineItemDecoration lineItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        binding.toolbar.toolbarIvTitle.setText("我的下载");

        binding.mdTvPath.setText("下载路径：" + getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath());

        List<Title> titleList = LitePal.where("upload = 1 and language = ?", Constant.LANGUAGE).find(Title.class);

        lineItemDecoration = new LineItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.rctg_home_space_10dp, getTheme()));

        binding.mdRv.addItemDecoration(lineItemDecoration);

        myDownloadAdapter = new MyDownloadAdapter(R.layout.item_my_download, titleList);
        binding.mdRv.setLayoutManager(new LinearLayoutManager(this));
        binding.mdRv.setAdapter(myDownloadAdapter);

        myDownloadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Title title = myDownloadAdapter.getItem(position);

                int id;
                if (title.getVoaId() == null) {

                    id = title.getId();
                } else {
                    id = Integer.parseInt(title.getVoaId());
                }
                ContentActivity.startActivity(MyDownloadActivity.this, id, title.getTitle(), 0, position);
            }
        });
    }
}