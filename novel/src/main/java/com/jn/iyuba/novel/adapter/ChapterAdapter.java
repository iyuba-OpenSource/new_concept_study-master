package com.jn.iyuba.novel.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jn.iyuba.novel.NovelApplication;
import com.jn.iyuba.novel.R;
import com.jn.iyuba.novel.db.AppDatabase;
import com.jn.iyuba.novel.db.Chapter;
import com.jn.iyuba.novel.db.NovelDB;
import com.jn.iyuba.novel.db.dao.ChapterDao;
import com.jn.iyuba.novel.db.dao.NovelSentenceDao;

import java.util.List;


/**
 * 章节列表
 */
public class ChapterAdapter extends BaseQuickAdapter<Chapter, BaseViewHolder> {


    public ChapterAdapter(int layoutResId, @Nullable List<Chapter> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Chapter item) {


        String[] cTitle = item.getCname_en().split("\\s+");
        helper.setText(R.id.chapter_tv_index, cTitle[0]);
        helper.setText(R.id.chapter_tv_cname_en, item.getCname_en());
        helper.setText(R.id.chapter_tv_cname_cn, item.getCname_cn());


        if (item.isUpdate()) {

            item.setUpdate(false);
            //听力进度
            if (item.getEnd_flg() == 1) {

                helper.setTextColor(R.id.chapter_tv_listen, NovelApplication.getContext().getColor(R.color.mColorPrimary));
                helper.setImageResource(R.id.chapter_prb_listen, R.mipmap.novel_icon_listen_green);
                helper.setText(R.id.chapter_tv_listen, "100%");
            } else {

                int p = (int) (item.getTest_number() * 100.0 / item.getSentenceTotal());
                if (p == 0) {

                    helper.setTextColor(R.id.chapter_tv_listen, Color.parseColor("#C2C2C2"));
                    helper.setImageResource(R.id.chapter_prb_listen, R.mipmap.novel_icon_listen_gray);
                } else {

                    helper.setTextColor(R.id.chapter_tv_listen, NovelApplication.getContext().getColor(R.color.mColorPrimary));
                    helper.setImageResource(R.id.chapter_prb_listen, R.mipmap.novel_icon_listen_green);
                }
                helper.setText(R.id.chapter_tv_listen, p + "%");
            }
            //评测进度
            int evalCount = item.getEvalCount();
            if (evalCount == 0) {

                helper.setTextColor(R.id.chapter_tv_eval, Color.parseColor("#C2C2C2"));
                helper.setImageResource(R.id.chapter_prb_eval, R.mipmap.novel_icon_eval_gray);
            } else {

                helper.setTextColor(R.id.chapter_tv_eval, NovelApplication.getContext().getColor(R.color.mColorPrimary));
                helper.setImageResource(R.id.chapter_prb_eval, R.mipmap.novel_icon_eval_green);
            }
            helper.setText(R.id.chapter_tv_eval, evalCount + "/" + item.getSentenceTotal());
        } else {

            new Thread(new ProgressRunnable(item)).start();
        }
    }


    /**
     * 进度获取
     */
    class ProgressRunnable implements Runnable {

        private Chapter item;

        public ProgressRunnable(Chapter item) {

            this.item = item;
        }

        @Override
        public void run() {

            AppDatabase appDatabase = NovelDB.getInstance().getDB();
            ChapterDao chapterDao = appDatabase.chapterDao();
            Chapter chapter = chapterDao.getSingleByPrimaryKeys(item.getVoaid(), item.getTypes(), item.getLevel()
                    , item.getOrder_number(), item.getChapter_order());

            if (chapter == null) {//处理进度崩溃的问题

                return;
            }

            NovelSentenceDao novelSentenceDao = appDatabase.novelSentenceDao();
            int sentenceTotal = novelSentenceDao.getCountForChapter(chapter.getTypes(), chapter.getVoaid(), chapter.getChapter_order(), chapter.getOrder_number(),
                    chapter.getLevel());

            int evalCount = novelSentenceDao.getCountForEval(chapter.getTypes(), chapter.getVoaid(), chapter.getChapter_order(), chapter.getOrder_number(),
                    chapter.getLevel());


            List<Chapter> chapterList = getData();
            for (int i = 0; i < chapterList.size(); i++) {

                Chapter c = chapterList.get(i);
                if (c.getChapter_order().equals(item.getChapter_order())) {

                    chapter.setUpdate(true);//数据已更新
                    chapter.setSentenceTotal(sentenceTotal);
                    chapter.setEvalCount(evalCount);
                    chapterList.set(i, chapter);
                    int finalI = i;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            notifyItemChanged(finalI);
                        }
                    });
                    break;
                }
            }
        }
    }
}
