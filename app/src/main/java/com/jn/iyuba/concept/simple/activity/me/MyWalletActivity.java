package com.jn.iyuba.concept.simple.activity.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.adapter.me.MyWalletAdapter;
import com.jn.iyuba.concept.simple.model.bean.me.RewardBean;
import com.jn.iyuba.concept.simple.presenter.me.MyWalletPresenter;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.view.me.MyWalletContract;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.ActivityMyWalletBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的钱包
 */
public class MyWalletActivity extends BaseActivity<MyWalletContract.MyWalletView, MyWalletContract.MyWalletPresenter>
        implements MyWalletContract.MyWalletView {

    private ActivityMyWalletBinding binding;

    private MyWalletAdapter myWalletAdapter;
    private int page = 1;

    private int pageSize = 10;

    private SimpleDateFormat simpleDateFormat;

    /**
     * 正在加载数据
     */
    private boolean isLoading = false;

    private SharedPreferences userSharedPreferences;

    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding.toolbar.toolbarIvTitle.setText("我的钱包");
        binding.toolbar.toolbarIvBack.setOnClickListener(v -> finish());

        userSharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        uid = userSharedPreferences.getString("uid", "0");
        binding.mywalletSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (myWalletAdapter == null) {

                    return;
                }
                if (isLoading) {

                    binding.mywalletSrl.setRefreshing(false);
                }

                binding.mywalletSrl.setRefreshing(true);
                page = 1;
                String sign = uid + "iyuba" + simpleDateFormat.format(new Date());
                presenter.getUserActionRecord(Integer.parseInt(uid), page, pageSize, MD5Util.MD5(sign));
            }
        });

        binding.mywalletRv.setLayoutManager(new LinearLayoutManager(this));

        myWalletAdapter = new MyWalletAdapter(R.layout.item_my_wallet, new ArrayList<>());
        binding.mywalletRv.setAdapter(myWalletAdapter);

        myWalletAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {


                isLoading = true;
                String sign = uid + "iyuba" + simpleDateFormat.format(new Date());
                presenter.getUserActionRecord(Integer.parseInt(uid), ++page, pageSize, MD5Util.MD5(sign));
            }
        }, binding.mywalletRv);

        simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("yyyy-MM-dd");

        String sign = uid + "iyuba" + simpleDateFormat.format(new Date());
        presenter.getUserActionRecord(Integer.parseInt(uid), page, pageSize, MD5Util.MD5(sign));
    }

    @Override
    public View initLayout() {
        binding = ActivityMyWalletBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public MyWalletContract.MyWalletPresenter initPresenter() {
        return new MyWalletPresenter();
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
    public void wallet(int pages, List<RewardBean.DataDTO> dataDTOS) {


        isLoading = false;
        if (pages == 1) {


            if (binding.mywalletSrl.isRefreshing()) {

                binding.mywalletSrl.setRefreshing(false);
            }

            if (dataDTOS == null) {

                toast("请求超时，请下拉刷新");
                return;
            }

            myWalletAdapter.setNewData(dataDTOS);
            if (dataDTOS.size() < pageSize) {

                myWalletAdapter.loadMoreEnd();
            } else {

                myWalletAdapter.loadMoreComplete();
            }


        } else {

            if (dataDTOS == null) {

                myWalletAdapter.loadMoreFail();
                return;
            }
            if (dataDTOS.size() < pageSize) {

                myWalletAdapter.addData(dataDTOS);
                myWalletAdapter.loadMoreEnd();
            } else {

                myWalletAdapter.addData(dataDTOS);
                myWalletAdapter.loadMoreComplete();
            }

        }
    }
}