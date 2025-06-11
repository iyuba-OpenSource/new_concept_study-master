package com.jn.iyuba.concept.simple.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.adapter.ChooseBookAdapter;
import com.jn.iyuba.succinct.databinding.ActivityChooseBookBinding;
import com.jn.iyuba.concept.simple.db.Book;
import com.jn.iyuba.concept.simple.model.bean.BookBean;
import com.jn.iyuba.concept.simple.presenter.login.ChooseBookPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.DateUtil;
import com.jn.iyuba.concept.simple.util.MD5Util;
import com.jn.iyuba.concept.simple.view.home.ChooseBookContract;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择课本
 */
public class ChooseBookActivity extends BaseActivity<ChooseBookContract.ChooseBookView, ChooseBookContract.ChooseBookPresenter>
        implements ChooseBookContract.ChooseBookView {

    private ActivityChooseBookBinding binding;

    private ChooseBookAdapter bookAdapter;

    private ChooseBookAdapter youthAdapter;

    /**
     * 青少版课本
     */
    private BookBean bookBean;

    /**
     * 新概念英语1-4
     */
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initOperation();

        int uid = Constant.userinfo == null ? 0 : Constant.userinfo.getUid();
        String sign = MD5Util.MD5("iyuba" + DateUtil.getDays() + "series");
        presenter.getTitleBySeries("category", 321, Constant.APPID, uid, sign);
    }

    private void initOperation() {

        binding.cbLlContent.setVisibility(View.GONE);
        binding.cbPbLoading.setVisibility(View.VISIBLE);
        binding.cbLlError.setVisibility(View.GONE);

        binding.cbLlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int uid = Constant.userinfo == null ? 0 : Constant.userinfo.getUid();
                String sign = MD5Util.MD5("iyuba" + DateUtil.getDays() + "series");
                presenter.getTitleBySeries("category", 321, Constant.APPID, uid, sign);
            }
        });

        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        binding.toolbar.toolbarIvTitle.setText("选择课本");
        binding.toolbar.toolbarIvRight.setVisibility(View.INVISIBLE);


        bookList = new ArrayList<>();
        Book book = new Book();
        book.setId("1");
        book.setDescCn("第一册");
        bookList.add(book);

        Book book2 = new Book();
        book2.setId("2");
        book2.setDescCn("第二册");
        bookList.add(book2);

        Book book3 = new Book();
        book3.setId("3");
        book3.setDescCn("第三册");
        bookList.add(book3);

        Book book4 = new Book();
        book4.setId("4");
        book4.setDescCn("第四册");
        bookList.add(book4);

        bookAdapter = new ChooseBookAdapter(R.layout.item_new_book, bookList);
        binding.cbRvNewbook.setLayoutManager(new GridLayoutManager(ChooseBookActivity.this, 4));
        binding.cbRvNewbook.setAdapter(bookAdapter);

        bookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                bookAdapter.setPosition(position);
                bookAdapter.notifyDataSetChanged();
                youthAdapter.setPosition(-1);
                youthAdapter.notifyDataSetChanged();

                binding.cbRbUs.setClickable(true);
                binding.cbRbUk.setClickable(true);


                int id = binding.cbRgLanguage.getCheckedRadioButtonId();
                if (id == -1) {//当没有选中发音时，选中发音；已选中发音则不做处理

                    if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                        binding.cbRgLanguage.check(R.id.cb_rb_us);
                    } else {

                        binding.cbRgLanguage.check(R.id.cb_rb_uk);
                    }
                }
            }
        });
        //确定
        binding.cbButConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Book mbook;
                if (bookAdapter.getPosition() != -1) {

                    mbook = bookAdapter.getItem(bookAdapter.getPosition());
                } else {
                    mbook = youthAdapter.getItem(youthAdapter.getPosition());
                }
                Constant.book = mbook;

                //存储选中的书
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SP_USER, MODE_PRIVATE);
                sharedPreferences.edit().putString(Constant.SP_KEY_BOOk, new Gson().toJson(mbook)).apply();

                int p = bookAdapter.getPosition();
                if (p != -1) {//新概念有两种发音

                    int id = binding.cbRgLanguage.getCheckedRadioButtonId();
                    if (id == R.id.cb_rb_us) {

                        Constant.LANGUAGE = "US";
                    } else {

                        Constant.LANGUAGE = "UK";
                    }

                    sharedPreferences.edit().putString(Constant.SP_KEY_LANGUAGE, Constant.LANGUAGE).apply();
                }
                EventBus.getDefault().post(mbook);
                finish();
            }
        });
    }

    @Override
    public View initLayout() {
        binding = ActivityChooseBookBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public ChooseBookContract.ChooseBookPresenter initPresenter() {
        return new ChooseBookPresenter();
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
    public void getTitleBySeries(BookBean bookBean) {


        if (bookBean == null) {

            binding.cbLlContent.setVisibility(View.GONE);
            binding.cbPbLoading.setVisibility(View.GONE);
            binding.cbLlError.setVisibility(View.VISIBLE);
        } else {

            this.bookBean = bookBean;
            binding.cbLlContent.setVisibility(View.VISIBLE);
            binding.cbPbLoading.setVisibility(View.GONE);
            binding.cbLlError.setVisibility(View.GONE);
        }

        //音频选择
        if (BookUtil.isYouthBook(Integer.parseInt(Constant.book.getId()))) {
            //青少版不需要选择
            binding.cbRgLanguage.clearCheck();
        } else {

            //显示选中的状态
            if (Constant.LANGUAGE.equalsIgnoreCase("us")) {

                binding.cbRgLanguage.check(R.id.cb_rb_us);
            } else {

                binding.cbRgLanguage.check(R.id.cb_rb_uk);
            }
        }

        //青少
        List<Book> books = bookAdapter.getData();
        int index = -1;
        for (int i = 0; i < books.size(); i++) {

            Book book = books.get(i);
            if (book.getId().equals(Constant.book.getId())) {

                index = i;
                break;
            }
        }
        bookAdapter.setPosition(index);
        bookAdapter.notifyDataSetChanged();

        if (bookBean == null) {

            return;
        }

        //青少版
        youthAdapter = new ChooseBookAdapter(R.layout.item_choose_book, bookBean.getData());

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW); //主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP); //按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START); //交叉轴的起点对齐。
        binding.cbRvYouthbook.setLayoutManager(flexboxLayoutManager);
        binding.cbRvYouthbook.setAdapter(youthAdapter);
        youthAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                youthAdapter.setPosition(position);
                youthAdapter.notifyDataSetChanged();
                bookAdapter.setPosition(-1);
                bookAdapter.notifyDataSetChanged();

                binding.cbRgLanguage.clearCheck();
                binding.cbRbUs.setClickable(false);
                binding.cbRbUk.setClickable(false);
            }
        });

        List<Book> youthBooks = youthAdapter.getData();
        int yIndex = -1;
        for (int i = 0; i < youthBooks.size(); i++) {

            Book book = youthBooks.get(i);
            if (book.getId().equals(Constant.book.getId())) {

                yIndex = i;
                break;
            }
        }
        youthAdapter.setPosition(yIndex);
        youthAdapter.notifyDataSetChanged();

    }
}