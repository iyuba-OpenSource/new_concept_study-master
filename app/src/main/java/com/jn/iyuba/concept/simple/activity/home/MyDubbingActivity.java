package com.jn.iyuba.concept.simple.activity.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.adapter.MyDubbingAdapter;
import com.jn.iyuba.succinct.databinding.ActivityMyDubbingBinding;
import com.jn.iyuba.concept.simple.model.bean.MyDubbingBean;
import com.jn.iyuba.concept.simple.presenter.home.MyDubbingPresenter;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.concept.simple.view.home.MyDubbingContract;

import java.util.ArrayList;

/**
 * 我的 - 我的配音
 */
public class MyDubbingActivity extends BaseActivity<MyDubbingContract.MyDubbingView, MyDubbingContract.MyDubbingPresenter>
        implements MyDubbingContract.MyDubbingView {

    private ActivityMyDubbingBinding activityMyDubbingBinding;

    private MyDubbingAdapter myDubbingAdapter;

    private LineItemDecoration lineItemDecoration;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDubbingAdapter = new MyDubbingAdapter(R.layout.item_my_dubbing, new ArrayList<>());
        activityMyDubbingBinding.mydubbingRv.setLayoutManager(new LinearLayoutManager(this));
        activityMyDubbingBinding.mydubbingRv.setAdapter(myDubbingAdapter);
        myDubbingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MyDubbingBean.DataDTO dataDTO = myDubbingAdapter.getItem(position);
                MyDubbingDetailsActivity.startActivity(MyDubbingActivity.this, dataDTO.getVideoUrl(), dataDTO.getTitle(),
                        dataDTO.getTitleCn());
            }
        });
        //删除
        myDubbingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                MyDubbingBean.DataDTO dataDTO = myDubbingAdapter.getItem(position);
                delDialog(dataDTO.getId());
            }
        });

        lineItemDecoration = new LineItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));
        activityMyDubbingBinding.mydubbingRv.addItemDecoration(lineItemDecoration);

        initOperation();

        //请求
        presenter.getTalkShowOtherWorks(Constant.APPID, Constant.userinfo.getUid() + "", Constant.TYPE);
    }

    private void initOperation() {

        activityMyDubbingBinding.toolbar.toolbarIvTitle.setText("我的配音");
        activityMyDubbingBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    public View initLayout() {
        activityMyDubbingBinding = ActivityMyDubbingBinding.inflate(getLayoutInflater());
        return activityMyDubbingBinding.getRoot();
    }

    @Override
    public MyDubbingContract.MyDubbingPresenter initPresenter() {
        return new MyDubbingPresenter();
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
    public void getMyDubbingComplete(MyDubbingBean myDubbingBean) {

        myDubbingAdapter.setNewData(myDubbingBean.getData());
    }

    private void delDialog(int id) {

        alertDialog = new AlertDialog.Builder(MyDubbingActivity.this)
                .setTitle("删除")
                .setMessage("是否要删除？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        presenter.delEvalAndDubbing(61003, id);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void delEvalComplete(int id) {


        if (alertDialog != null) {

            alertDialog.dismiss();
        }

        for (int i = 0; i < myDubbingAdapter.getItemCount(); i++) {

            MyDubbingBean.DataDTO dataDTO = myDubbingAdapter.getItem(i);
            if (dataDTO.getId() == id) {

                myDubbingAdapter.getData().remove(dataDTO);
                myDubbingAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}