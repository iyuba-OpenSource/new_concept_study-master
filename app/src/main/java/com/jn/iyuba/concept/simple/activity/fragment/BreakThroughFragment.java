package com.jn.iyuba.concept.simple.activity.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.entity.WordBook;
import com.jn.iyuba.concept.simple.util.popup.MorePopup;
import com.jn.iyuba.concept.simple.util.popup.WordBookPopup;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.activity.break_through.BTWordsActivity;
import com.jn.iyuba.concept.simple.activity.vip.VipActivity;
import com.jn.iyuba.concept.simple.adapter.break_through.BreakThroughAdapter;
import com.jn.iyuba.succinct.databinding.FragmentBreakThroughBinding;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.db.ConceptWord;
import com.jn.iyuba.concept.simple.entity.CheckPoint;
import com.jn.iyuba.concept.simple.model.bean.WordBean;
import com.jn.iyuba.concept.simple.presenter.break_through.BreakThroughPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.GridSpacingItemDecoration;
import com.jn.iyuba.concept.simple.util.popup.WordLoadingPopup;
import com.jn.iyuba.concept.simple.view.break_through.BreakThroughContract;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 单词闯关
 */
public class BreakThroughFragment extends BaseFragment<BreakThroughContract.BreakThroughView, BreakThroughContract.BreakThroughPresenter>
        implements BreakThroughContract.BreakThroughView {


    private FragmentBreakThroughBinding fragmentBreakThroughBinding;

//    private BTMorePopup btMorePopup;

    private WordLoadingPopup loadingPopup;

    private int progress = 0;

    private BreakThroughAdapter breakThroughAdapter;

    private GridSpacingItemDecoration gridSpacingItemDecoration;

    private WordBookPopup wordBookPopup;


    /**
     * 单词页面的课本弹窗
     */
    private void initWordBookPopup() {


        if (wordBookPopup == null) {

            List<WordBook> bookList = new ArrayList<>();
            bookList.add(new WordBook("新概念第一册", 1, 1062));
            bookList.add(new WordBook("新概念第二册", 2, 858));
            bookList.add(new WordBook("新概念第三册", 3, 1038));
            bookList.add(new WordBook("新概念第四册", 4, 788));

            bookList.add(new WordBook("新概念英语青少版StarterA", 278, 94));
            bookList.add(new WordBook("新概念英语青少版StarterB", 279, 130));
            bookList.add(new WordBook("新概念英语青少版1A", 280, 368));
            bookList.add(new WordBook("新概念英语青少版1B", 281, 346));
            bookList.add(new WordBook("新概念英语青少版2A", 282, 308));
            bookList.add(new WordBook("新概念英语青少版2B", 283, 276));
            bookList.add(new WordBook("新概念英语青少版3A", 284, 320));
            bookList.add(new WordBook("新概念英语青少版3B", 285, 197));
            bookList.add(new WordBook("新概念英语青少版4A", 286, 493));
            bookList.add(new WordBook("新概念英语青少版4B", 287, 433));
            bookList.add(new WordBook("新概念英语青少版5A", 288, 378));
            bookList.add(new WordBook("新概念英语青少版5B", 289, 314));

            wordBookPopup = new WordBookPopup(requireContext());
            wordBookPopup.setDatas(bookList);
            wordBookPopup.setCallback(new WordBookPopup.Callback() {
                @Override
                public void getBook(WordBook wordBook) {


                    //当前选择课本
                    Constant.wordBook = wordBook;
                    fragmentBreakThroughBinding.btTvWordNum.setText(wordBook.getName() + wordBook.getNum() + "个单词");
                    //保存单词闯关的课本
                    SharedPreferences userSp = requireContext().getSharedPreferences(Constant.SP_USER, Context.MODE_PRIVATE);
                    userSp.edit().putString(Constant.SP_KEY_WORD_BOOK, new Gson().toJson(wordBook)).apply();

                    //检测是否有数据，没有数据则请求
                    int count = LitePal.where("bookid = ?", Constant.wordBook.getBookid() + "").count(ConceptWord.class);
                    if (count == 0) {

                        progress = 0;
                        showLoading("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");
                        if (BookUtil.isYouthBook(Constant.wordBook.getBookid())) {

                            presenter.getWordByUnit(Constant.wordBook.getBookid() + "");
                        } else {

                            presenter.getConceptWord(Constant.GET_CONCEPT_WORD, Constant.wordBook.getBookid());
                        }
                    } else {

                        initData();
                    }
                    wordBookPopup.dismiss();
                }
            });
        }
        wordBookPopup.showPopupWindow();
    }

    public BreakThroughFragment() {

    }

    public static BreakThroughFragment newInstance() {
        BreakThroughFragment fragment = new BreakThroughFragment();
        return fragment;
    }

    /**
     * 更新bundle
     */
    public void updateBundle() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        breakThroughAdapter = new BreakThroughAdapter(R.layout.item_break_through, new ArrayList<>());
        breakThroughAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (Constant.userinfo != null) {

                    if (!Constant.userinfo.isVip() && position != 0) {

                        showBuyVipDialog();
                        return;
                    }
                } else {

                    if (position != 0) {

                        showBuyVipDialog();
                        return;
                    }
                }
                CheckPoint checkPoint = breakThroughAdapter.getItem(position);
                if (checkPoint != null && checkPoint.isPass()) {

                    BTWordsActivity.startActivity(requireActivity(), checkPoint.getVoaid(), checkPoint.getBookid(), position, checkPoint.getUnit_id());
                } else {

                    toast("此关未解锁");
                }

            }
        });

        gridSpacingItemDecoration = new GridSpacingItemDecoration(3, 40, true);
    }


    /**
     * 是否购买会员
     */
    private void showBuyVipDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setMessage("非VIP用户单词闯关体验闯1关，VIP会员无限单词闯关,是否购买会员？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        VipActivity.startActivity(requireActivity());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();
        if (Constant.wordBook == null) {

            Constant.wordBook = new WordBook("新概念第一册", 1, 1062);
        }
        fragmentBreakThroughBinding.btTvWordNum.setText(Constant.wordBook.getName() + Constant.wordBook.getNum() + "个单词");
//        initData();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            int count = LitePal.where("bookid = ?", Constant.wordBook.getBookid() + "").count(ConceptWord.class);
            if (count == 0) {

                progress = 0;
                showLoading("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");


                if (BookUtil.isYouthBook(Integer.parseInt(Constant.wordBook.getBookid() + ""))) {

                    presenter.getWordByUnit(Constant.wordBook.getBookid() + "");
                } else {

                    presenter.getConceptWord(Constant.GET_CONCEPT_WORD, Constant.wordBook.getBookid());
                }
            } else {

                initData();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        int count = LitePal.where("bookid = ?", Constant.wordBook.getBookid() + "").count(ConceptWord.class);
        if (count != 0) {
            initData();
        }
    }

    private void initOperation() {

        fragmentBreakThroughBinding.toolbar.toolbarIvBack.setVisibility(View.GONE);

        fragmentBreakThroughBinding.toolbar.toolbarIvTitle.setText("单词闯关");
        fragmentBreakThroughBinding.toolbar.toolbarIvRight.setImageResource(R.mipmap.ic_book);
        fragmentBreakThroughBinding.toolbar.toolbarIbRight2.setVisibility(View.GONE);
        fragmentBreakThroughBinding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initWordBookPopup();
            }
        });

        fragmentBreakThroughBinding.btIvSyncdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = 0;
                showLoading("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");
                if (BookUtil.isYouthBook(Constant.wordBook.getBookid())) {

                    presenter.getWordByUnit(Constant.wordBook.getBookid() + "");
                } else {

                    presenter.getConceptWord(Constant.GET_CONCEPT_WORD, Constant.wordBook.getBookid());
                }
            }
        });
        fragmentBreakThroughBinding.btRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        fragmentBreakThroughBinding.btRv.setAdapter(breakThroughAdapter);
        if (fragmentBreakThroughBinding.btRv.getItemDecorationCount() == 0) {

            fragmentBreakThroughBinding.btRv.addItemDecoration(gridSpacingItemDecoration);
        }
    }


    @SuppressLint("Range")
    private void initData() {

        //处理单词库变更，title也要变
        fragmentBreakThroughBinding.toolbar.toolbarIvTitle.setText("单词闯关");

       /* //此level的数量
        int checkpointNuM = LitePal.where("bookid = ?", Constant.bookid + "").count(Title.class);*/

        //已通过的关卡
        if (BookUtil.isYouthBook(Constant.wordBook.getBookid())) {

            //处理单词分组
            Cursor cursor = LitePal.findBySQL("select unit_id from conceptword where bookid = ? group by unit_id", Constant.wordBook.getBookid() + "");
            List<CheckPoint> checkPoints = new ArrayList<>();
            while (cursor.moveToNext()) {

                int unit_id = cursor.getInt(cursor.getColumnIndex("unit_id"));
                CheckPoint checkPoint = new CheckPoint();
                checkPoint.setUnit_id(unit_id);
                checkPoint.setBookid(Integer.parseInt(Constant.wordBook.getBookid() + ""));
                checkPoints.add(checkPoint);
            }
            cursor.close();

            for (int i = 0; i < checkPoints.size(); i++) {

                CheckPoint checkPoint = checkPoints.get(i);
                int tCount = LitePal
                        .where("bookid = ? and unit_id = ? and answer_status = 1"
                                , Constant.wordBook.getBookid() + "", checkPoint.getUnit_id() + "").count(ConceptWord.class);

                int total = LitePal
                        .where("bookid = ? and unit_id = ?"
                                , Constant.wordBook.getBookid() + "", checkPoint.getUnit_id() + "").count(ConceptWord.class);

                checkPoint.settCount(tCount);
                checkPoint.setTotal(total);
                boolean isPass = dealPass(tCount, total);
                checkPoint.setPass(isPass);
            }

            int position = 0;
            for (int i = (checkPoints.size() - 1); i >= 0; i--) {

                CheckPoint checkPoint = checkPoints.get(i);
                if (position != 0) {

                    checkPoint.setPass(true);
                }
                if (checkPoint.isPass()) {

                    position++;
                }
            }
            if (position < checkPoints.size()) {

                checkPoints.get(position).setPass(true);
            }
            breakThroughAdapter.setNewData(checkPoints);
        } else {

            List<CheckPoint> checkPoints = new ArrayList<>();
            List<Title> titleList = LitePal.where("bookid = ? and language = ?", Constant.wordBook.getBookid() + "", "US").find(Title.class);

            for (int i = 0; i < titleList.size(); i++) {

                Title title = titleList.get(i);
                int total = LitePal.where("bookid = ? and voaId = ?", Constant.wordBook.getBookid() + "", title.getVoaId())
                        .count(ConceptWord.class);
                int tCount = LitePal.where("bookid = ? and voaId = ? and answer_status = 1", Constant.wordBook.getBookid() + "", title.getVoaId())
                        .count(ConceptWord.class);
                boolean isPass = dealPass(tCount, total);

                CheckPoint checkPoint = new CheckPoint();
                checkPoint.setBookid(title.getBookid());
                checkPoint.settCount(tCount);
                checkPoint.setTotal(total);
                checkPoint.setPass(isPass);
                checkPoint.setVoaid(Integer.parseInt(title.getVoaId()));
                checkPoints.add(checkPoint);
            }

            //设置可进行的最高关卡,将已通过的关卡设置成通过状态(已通过的关卡可能再次闯关会变成未通过状态)
            int position = 0;
            for (int i = (checkPoints.size() - 1); i >= 0; i--) {

                CheckPoint checkPoint = checkPoints.get(i);
                if (position != 0) {

                    checkPoint.setPass(true);
                }
                if (checkPoint.isPass()) {

                    position++;
                }
            }
            if (position < checkPoints.size()) {

                checkPoints.get(position).setPass(true);
            }
            breakThroughAdapter.setNewData(checkPoints);
        }
    }


    /**
     * 计算这一关的正确率，大于等于80%算通过
     *
     * @return
     */
    private boolean dealPass(int tnum, int total) {

        int accuracy = (int) (100.0 * tnum / total);
        if (accuracy >= 80) {

            return true;
        } else {

            return false;
        }
    }


    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        fragmentBreakThroughBinding = FragmentBreakThroughBinding.inflate(inflater, container, false);
        return fragmentBreakThroughBinding.getRoot();
    }

    @Override
    protected BreakThroughContract.BreakThroughPresenter initPresenter() {
        return new BreakThroughPresenter();
    }

    @Override
    public void showLoading(String msg) {

        if (loadingPopup == null) {

            loadingPopup = new WordLoadingPopup(getContext());
        }
        loadingPopup.setContent(msg);
        loadingPopup.showPopupWindow();
    }

    @Override
    public void hideLoading() {

        if (loadingPopup != null) {

            loadingPopup.dismiss();
        }
    }


    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == 1) {

                if (progress == 100) {
                    initData();
                    hideLoading();
                } else {

                    loadingPopup.setContent("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");
//                        showLoading("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");
                }
            } else if (msg.what == 2) {

                ConceptWord conceptWord = (ConceptWord) msg.obj;
                conceptWord.save();
            } else {

                ConceptWord conceptWord = (ConceptWord) msg.obj;
                int id = msg.arg1;
                conceptWord.updateAll("bookid = ? and position = ?  and voaid = ?", conceptWord.getBookid() + "", conceptWord.getPosition(), conceptWord.getVoaId() + "");
            }
            return false;
        }
    });


    @Override
    public void getConceptWord(WordBean wordBean, int bookid) {


        new Thread(new Runnable() {
            @Override
            public synchronized void run() {

                List<ConceptWord> jpConceptWordList = wordBean.getData();
                for (int i = 0; i < jpConceptWordList.size(); i++) {

                    ConceptWord conceptWord = jpConceptWordList.get(i);
                    conceptWord.setBookid(bookid);
                    List<ConceptWord> conceptWords = LitePal
                            .where("bookid = ? and position = ?  and voaid = ?", conceptWord.getBookid() + "", conceptWord.getPosition(), conceptWord.getVoaId() + "")
                            .find(ConceptWord.class);
                    if (conceptWords.size() == 0) {

                        Message message = Message.obtain();
                        message.obj = conceptWord;
                        message.what = 2;
                        handler.sendMessage(message);
                    } else {

                        Message message = Message.obtain();
                        message.obj = conceptWord;
                        message.what = 3;
                        message.arg1 = conceptWord.getBookid();
                        message.arg2 = Integer.parseInt(conceptWord.getPosition());
                        handler.sendMessage(message);
//                        word.updateAll("wordid = ?", word.getId() + "");
                    }
                    progress = (int) (100.0 * (i + 1) / jpConceptWordList.size());

                    if (loadingPopup != null) {//过滤重复的
                        if (loadingPopup.getProgress() != progress) {

                            loadingPopup.setProgress(progress);
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            }
        }).start();
    }
}