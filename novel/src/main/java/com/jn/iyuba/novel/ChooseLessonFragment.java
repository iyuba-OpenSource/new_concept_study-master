package com.jn.iyuba.novel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.jn.iyuba.novel.activity.BaseFragment;
import com.jn.iyuba.novel.adapter.BookAdapter;
import com.jn.iyuba.novel.adapter.LevelAdapter;
import com.jn.iyuba.novel.databinding.NovelFragmentChooseLessonBinding;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.NovelBook;
import com.jn.iyuba.novel.db.dao.BookDao;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.entity.BookType;
import com.jn.iyuba.novel.model.bean.NovelBookBean;
import com.jn.iyuba.novel.presenter.ChooseLessonPresenter;
import com.jn.iyuba.novel.util.popup.BookTypePopup;
import com.jn.iyuba.novel.view.ChooseLessonContract;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择课程
 */
public class ChooseLessonFragment extends BaseFragment<ChooseLessonContract.ChooseLessonView, ChooseLessonContract.ChooseLessonPresenter>
        implements ChooseLessonContract.ChooseLessonView {

    private NovelFragmentChooseLessonBinding binding;

    private LevelAdapter levelAdapter;

    private BookAdapter bookAdapter;

    private BookTypePopup bookTypePopup;

    /**
     * 书类型集合
     */
    private List<BookType> bookTypeList;

    public ChooseLessonFragment() {
        // Required empty public constructor
    }

    public static ChooseLessonFragment newInstance() {
        ChooseLessonFragment fragment = new ChooseLessonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initOperation();
        presenter.getStroryInfo("home", Integer.parseInt(NovelConstant.novelBook.getLevel()), NovelConstant.novelBook.getTypes());
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        binding = NovelFragmentChooseLessonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected ChooseLessonContract.ChooseLessonPresenter initPresenter() {
        return new ChooseLessonPresenter();
    }

    private void initOperation() {

        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().finish();
            }
        });
        binding.toolbar.toolbarIvTitle.setText("课程选择");
        binding.toolbar.toolbarIvRight.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarIvRight.setImageResource(R.mipmap.ic_book);
        binding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initBookTypePopup(bookTypeList);
            }
        });

        initLevel();

        bookAdapter = new BookAdapter(R.layout.novel_item_book, new ArrayList<>());
        binding.clRvBooks.setLayoutManager(new GridLayoutManager(NovelApplication.getContext(), 3));
        binding.clRvBooks.setAdapter(bookAdapter);

        bookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                bookAdapter.setPosition(position);
                NovelBook novelBook = bookAdapter.getItem(position);

                NovelConstant.novelBook = novelBook;

                //存储选择的书
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(NovelConstant.SP_BOOK, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(NovelConstant.SP_KEY_BOOK, new Gson().toJson(novelBook)).apply();

                bookAdapter.notifyDataSetChanged();
                //触发刷新
                EventBus.getDefault().post(novelBook);
                requireActivity().finish();
            }
        });
    }

    private void initLevel() {

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(requireContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW); //主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP); //按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START); //交叉轴的起点对齐。
        binding.clRvLevel.setLayoutManager(flexboxLayoutManager);


        bookTypeList = new ArrayList<>();

        List<BookType.BookLevel> newCamstoryColorList = new ArrayList<>();
        newCamstoryColorList.add(new BookType.BookLevel("入门级", 0, "newCamstoryColor"));
        newCamstoryColorList.add(new BookType.BookLevel("一级", 1, "newCamstoryColor"));
        newCamstoryColorList.add(new BookType.BookLevel("二级", 2, "newCamstoryColor"));
        newCamstoryColorList.add(new BookType.BookLevel("三级", 3, "newCamstoryColor"));
        newCamstoryColorList.add(new BookType.BookLevel("四级", 4, "newCamstoryColor"));
        newCamstoryColorList.add(new BookType.BookLevel("五级", 5, "newCamstoryColor"));
        bookTypeList.add(new BookType("剑桥英语小说馆彩绘", "newCamstoryColor", newCamstoryColorList));

        List<BookType.BookLevel> newCamstoryList = new ArrayList<>();
        newCamstoryList.add(new BookType.BookLevel("入门级", 0, "newCamstory"));
        newCamstoryList.add(new BookType.BookLevel("一级", 1, "newCamstory"));
        newCamstoryList.add(new BookType.BookLevel("二级", 2, "newCamstory"));
        newCamstoryList.add(new BookType.BookLevel("三级", 3, "newCamstory"));
//        newCamstoryList.add(new BookType.BookLevel("四级", 4));
//        newCamstoryList.add(new BookType.BookLevel("五级", 5));
//        newCamstoryList.add(new BookType.BookLevel("六级", 6));
        bookTypeList.add(new BookType("剑桥英语小说馆", "newCamstory", newCamstoryList));

        List<BookType.BookLevel> bookwormList = new ArrayList<>();
        bookwormList.add(new BookType.BookLevel("入门级", 0, "bookworm"));
        bookwormList.add(new BookType.BookLevel("一级", 1, "bookworm"));
        bookwormList.add(new BookType.BookLevel("二级", 2, "bookworm"));
        bookwormList.add(new BookType.BookLevel("三级", 3, "bookworm"));
        bookwormList.add(new BookType.BookLevel("四级", 4, "bookworm"));
        bookwormList.add(new BookType.BookLevel("五级", 5, "bookworm"));
        bookwormList.add(new BookType.BookLevel("六级", 6, "bookworm"));
        bookTypeList.add(new BookType("书虫", "bookworm", bookwormList));


        BookType type;
        if (NovelConstant.novelBook.getTypes().equals("bookworm")) {

            binding.clTvName.setText("书虫");
            type = bookTypeList.get(2);
        } else if (NovelConstant.novelBook.getTypes().equals("newCamstoryColor")) {

            binding.clTvName.setText("剑桥英语小说馆彩绘");
            type = bookTypeList.get(0);
        } else {

            binding.clTvName.setText("剑桥英语小说馆");
            type = bookTypeList.get(1);
        }

        levelAdapter = new LevelAdapter(R.layout.novel_item_level, type.getBookLevelList());
        binding.clRvLevel.setAdapter(levelAdapter);
        levelAdapter.setPosition(Integer.parseInt(NovelConstant.novelBook.getLevel()));
        levelAdapter.notifyDataSetChanged();

        //等级点击事件
        levelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                levelAdapter.setPosition(position);
                levelAdapter.notifyDataSetChanged();

                BookType.BookLevel bookLevel = levelAdapter.getItem(position);
                presenter.getStroryInfo("home", bookLevel.getLevel(), bookLevel.getFrom());
            }
        });


    }


    /**
     * 选择课本类型
     */
    private void initBookTypePopup(List<BookType> bookTypeList) {

        if (bookTypePopup == null) {

            bookTypePopup = new BookTypePopup(requireContext());
            bookTypePopup.setCallback(new BookTypePopup.Callback() {
                @Override
                public void getType(BookType type) {

                    //更新等级及等级数据
                    levelAdapter.setPosition(0);
                    levelAdapter.setNewData(type.getBookLevelList());

                    binding.clTvName.setText(type.getName());

                    presenter.getStroryInfo("home", type.getBookLevelList().get(0).getLevel(), type.getFrom());
                    bookTypePopup.dismiss();
                }
            });
        }
        bookTypePopup.setData(bookTypeList);
        bookTypePopup.showPopupWindow();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void getBook(NovelBookBean novelBookBean, String from) {

        if (novelBookBean == null) {

        } else {

            bookAdapter.setNewData(novelBookBean.getData());

            new Thread(new Runnable() {
                @Override
                public void run() {

                    AppDatabase appDatabase = NovelDB.getInstance().getDB();
                    BookDao bookDao = appDatabase.bookDao();

                    List<NovelBook> novelBookList = novelBookBean.getData();
                    for (int i = 0; i < novelBookList.size(); i++) {

                        NovelBook novelBook = novelBookList.get(i);
                        if (novelBook.getTypes() == null || novelBook.getTypes().equals("")) {//处理空指针的问题

                            novelBook.setTypes("bookworm");
                        }
                        int count = bookDao.getCountById(novelBook.getTypes(), novelBook.getOrder_number(), novelBook.getLevel());
                        if (count == 0) {

                            bookDao.save(novelBook);
                        }
                    }

                }
            }).start();
        }
    }
}