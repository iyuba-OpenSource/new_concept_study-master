package com.jn.iyuba.concept.simple.activity.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.presenter.me.FeedbackPresenter;
import com.jn.iyuba.concept.simple.util.PackageUtil;
import com.jn.iyuba.concept.simple.view.me.FeedbackContract;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.ActivityFeedbackBinding;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity<FeedbackContract.FeedbackView, FeedbackContract.FeedbackPresenter>
        implements FeedbackContract.FeedbackView {

    private ActivityFeedbackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.toolbar.toolbarIvTitle.setText("意见反馈");
        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        initOperation();
    }

    private void initOperation() {


        binding.feedbackButSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit();
            }
        });
    }

    private void submit() {

        int uid = 0;
        if (Constant.userinfo != null) {

            uid = Constant.userinfo.getUid();
        }
        presenter.suggest(91001, uid, binding.feedbackEtContent.getText().toString(),
                binding.feedbackEtContact.getText().toString(), PackageUtil.getAppName(FeedbackActivity.this),
                "android");
    }

    @Override
    public View initLayout() {
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public FeedbackContract.FeedbackPresenter initPresenter() {
        return new FeedbackPresenter();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void suggestComplete() {


        toast("提交成功");
        finish();
    }
}