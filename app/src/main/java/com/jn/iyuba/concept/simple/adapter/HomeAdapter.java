package com.jn.iyuba.concept.simple.adapter;

import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iyuba.imooclib.data.local.IMoocDBManager;
import com.iyuba.imooclib.data.model.StudyProgress;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.db.Exercise;
import com.jn.iyuba.concept.simple.db.Sentence;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.db.ConceptWord;
import com.jn.iyuba.concept.simple.util.BookUtil;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 微课进度展示逻辑：
 * 1.获取的接口中ClassId这个参数，使用逗号切分成数组
 * 2.如果数据为1，则操作如下：
 * 通过 IMoocDBManager.getInstance().findStudyProgress 这个方法获取数据。然后以 yyyy-MM-dd HH:mm:ss 这种样式，格式化 studyProgress.endTime 和 studyProgress.startTime ，并将endTime-startTime获取时间数据。如果 voa.totalTime() > 0 ，则进度显示为 （endTime-startTime） / 10 / voa.totalTime()
 * 3.如果数据大于1，则操作如下：
 * 获取上边classId数组中最大值和最小值，通过 IMoocDBManager.getInstance().findStudyProgressByRange 方法查询集合数据。然后将集合中的所有数据，以 yyyy-MM-dd HH:mm:ss 这种样式，格式化 studyProgress.endTime 和 studyProgress.startTime，并将endTime-startTime获取时间数据。将时间数据/1000，然后加起来。如果 voa.totalTime() > 0 ，则进度显示为 （所有的时间数据） / 100 / voa.totalTime()
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.TitleViewHolder> {

    private SimpleDateFormat simpleDateFormat;
    private List<Title> data;

    private Callback callback;


    public List<Title> getData() {
        return data;
    }

    public void setData(List<Title> data) {
        this.data = data;
    }

    public HomeAdapter(int layoutResId, @Nullable List<Title> data) {

        this.data = data;
        simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 计算微课进度
     *
     * @param str
     * @return
     */
    private int dealImooc(String[] str) {

        int percentage = 0;
        for (int i = 0; i < str.length; i++) {

            StudyProgress progress1 = IMoocDBManager.getInstance().findStudyProgress(Constant.userinfo.getUid(), Integer.parseInt(str[i]));
            if (progress1 != null) {

                percentage = percentage + progress1.percentage;
            }
        }
        return (int) (percentage / str.length);
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {

        Title item = data.get(position);

        holder.setData(item, position);

        if (BookUtil.isYouthBook(item.getBookid())) {

            holder.home_tv_num.setText("UNIT " + (position + 1));
        } else {

            holder.home_tv_num.setText("LESSON " + (position + 1));
        }

        holder.home_tv_title.setText(item.getTitle());
        holder.home_tv_title.setText(item.getTitleCn());

        ImageView home_iv_img = holder.home_iv_img;
        if (item.getPic() != null && !item.getPic().equals("")) {

            holder.home_tv_index.setVisibility(View.VISIBLE);
            Glide.with(home_iv_img.getContext()).load(item.getPic()).into(home_iv_img);
        } else {

            holder.home_tv_index.setVisibility(View.GONE);
            holder.home_tv_index.setText("第" + (holder.getBindingAdapterPosition() + 1) + "课");
            Glide.with(home_iv_img.getContext()).load(R.mipmap.icon_lesson_default).into(home_iv_img);
        }

        //进度
        //听力
        int p;
        int sentenceCount;
        if (BookUtil.isYouthBook(item.getBookid())) {

            sentenceCount = Integer.parseInt(item.getTexts());
            if (item.getEndFlg() == 1) {//阅读完成

                p = 100;
            } else {
                p = (int) (item.getTestNumber() * 100.0 / sentenceCount);
            }
        } else {

            sentenceCount = Integer.parseInt(item.getTextNum());
            if (item.getEndFlg() == 1) {

                p = 100;
            } else {

                p = (int) (item.getTestNumber() * 100.0 / sentenceCount);
            }
        }

        if (p == 0) {
            holder.home_tv_listen.setTextColor(Color.parseColor("#C2C2C2"));
            holder.home_prb_listen.setImageResource(R.mipmap.icon_listen_gray);
        } else {

            holder.home_tv_listen.setTextColor(MyApplication.getContext().getColor(R.color.mColorPrimary));
            holder.home_prb_listen.setImageResource(R.mipmap.icon_listen_green);
        }
        holder.home_tv_listen.setText(p + "%");

        //评测进度
        int evalCount = LitePal.where("voaid = ? and score is not null", item.getVoaId()).count(Sentence.class);
        if (evalCount == 0) {

            holder.home_tv_eval.setTextColor(Color.parseColor("#C2C2C2"));
            holder.home_prb_eval.setImageResource(R.mipmap.icon_eval_gray);
        } else {

            holder.home_tv_eval.setTextColor(MyApplication.getContext().getColor(R.color.mColorPrimary));
            holder.home_prb_eval.setImageResource(R.mipmap.icon_eval_green);
        }
        holder.home_tv_eval.setText(evalCount + "/" + sentenceCount);
        //练习进度
        if (BookUtil.isYouthBook(item.getBookid())) {

            holder.home_ll_exercise.setVisibility(View.VISIBLE);
        } else {

            holder.home_ll_exercise.setVisibility(View.GONE);

            int teCount = 0;//正确的数量
            List<Exercise> exerciseList = LitePal.where("voaid = ?", item.getVoaId()).find(Exercise.class);
            int eCount = exerciseList.size();
            for (Exercise exercise : exerciseList) {

                if (exercise.getNote() == null) {

                    if (exercise.getCheckPosition() == Integer.parseInt(exercise.getAnswer())) {//正确的

                        teCount++;
                    }
                } else {

                    if (exercise.getCheckAnswer() != null && exercise.getAnswer() != null && exercise.getCheckAnswer().equals(exercise.getAnswer())) {
                        teCount++;
                    }
                }
            }

            if (teCount == 0) {

                holder.home_tv_exercise.setTextColor(Color.parseColor("#C2C2C2"));
                holder.home_prb_exercise.setImageResource(R.mipmap.icon_exercise_gray);

            } else {

                holder.home_tv_exercise.setTextColor(MyApplication.getContext().getColor(R.color.mColorPrimary));
                holder.home_prb_exercise.setImageResource(R.mipmap.icon_exercise_green);
            }
            holder.home_tv_exercise.setText(teCount + "/" + eCount);
        }
        //微课显示

        if (BookUtil.isYouthBook(item.getBookid())) {//青少

            holder.home_ll_imooc.setVisibility(View.VISIBLE);
        } else {

            holder.home_ll_imooc.setVisibility(View.GONE);
            if (Constant.userinfo != null) {

                String[] str = item.getTitleid().split(",");

                int imoocP = dealImooc(str);
                if (imoocP == 0) {

                    holder.home_tv_imooc.setTextColor(Color.parseColor("#C2C2C2"));
                    holder.home_prb_imooc.setImageResource(R.mipmap.icon_imooc_gray);
                } else {

                    holder.home_tv_imooc.setTextColor(MyApplication.getContext().getColor(R.color.mColorPrimary));
                    holder.home_prb_imooc.setImageResource(R.mipmap.icon_imooc_green);
                }

                holder.home_tv_imooc.setText(imoocP + "%");
            } else {

                holder.home_prb_imooc.setImageResource(R.mipmap.icon_imooc_gray);
                holder.home_tv_imooc.setTextColor(Color.parseColor("#C2C2C2"));
                holder.home_tv_imooc.setText(0 + "%");
            }
        }

        //单词进度
        int total = LitePal.where("voaId = ?", item.getVoaId())
                .count(ConceptWord.class);
        int tCount = LitePal.where("voaId = ? and answer_status = 1", item.getVoaId())
                .count(ConceptWord.class);
        if (tCount == 0) {

            holder.home_tv_word.setTextColor(Color.parseColor("#C2C2C2"));
            holder.home_tv_word.setText("0/" + total);
            holder.home_iv_word.setImageResource(R.mipmap.icon_word_gray);

        } else {

            holder.home_tv_word.setTextColor(MyApplication.getContext().getColor(R.color.mColorPrimary));
            holder.home_tv_word.setText(tCount + "/" + total);
            holder.home_iv_word.setImageResource(R.mipmap.icon_word_green);
        }

        //文件下载
        File file = new File(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC), item.getDownloadName());
        if (file.exists()) {

            if (item.getDownloadProgress() == -1 || item.getDownloadProgress() == 100) {//下载完成

                holder.home_tv_download.setVisibility(View.VISIBLE);
                holder.home_iv_download.setVisibility(View.GONE);
                holder.home_iv_download.setImageResource(R.mipmap.icon_download_complete);

            } else {//显示下载进度

                holder.home_tv_download.setVisibility(View.GONE);
                holder.home_iv_download.setVisibility(View.VISIBLE);
                holder.home_tv_download.setText(item.getDownloadProgress() + "%");
                holder.home_tv_download.setTextColor(Color.parseColor("#92CB7E"));
            }
        } else {
            //显示下载按钮

            holder.home_tv_download.setVisibility(View.VISIBLE);
            holder.home_iv_download.setVisibility(View.GONE);
            holder.home_iv_download.setImageResource(R.mipmap.icon_download);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {

        LinearLayout home_ll_listen;
        LinearLayout home_ll_eval;
        LinearLayout home_ll_word;
        LinearLayout home_ll_exercise;
        LinearLayout home_ll_imooc;
        ImageView home_iv_download;

        TextView home_tv_num;

        TextView home_tv_title;
        TextView home_tv_title_cn;

        ImageView home_iv_img;

        TextView home_tv_index;

        TextView home_tv_listen;

        ImageView home_prb_listen;

        TextView home_tv_eval;
        ImageView home_prb_eval;

        TextView home_tv_exercise;
        ImageView home_prb_exercise;

        TextView home_tv_imooc;
        ImageView home_prb_imooc;

        TextView home_tv_word;
        ImageView home_iv_word;
        TextView home_tv_download;

        Title title;

        int position;

        View.OnClickListener onClickListener = view -> {

            if (callback != null) {

                callback.itemChildClick(view, title, position);
            }
        };

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);

            home_ll_listen = itemView.findViewById(R.id.home_ll_listen);
            home_ll_eval = itemView.findViewById(R.id.home_ll_eval);
            home_ll_word = itemView.findViewById(R.id.home_ll_word);
            home_ll_exercise = itemView.findViewById(R.id.home_ll_exercise);
            home_ll_imooc = itemView.findViewById(R.id.home_ll_imooc);
            home_iv_download = itemView.findViewById(R.id.home_iv_download);

            home_tv_num = itemView.findViewById(R.id.home_tv_num);
            home_tv_title = itemView.findViewById(R.id.home_tv_title);
            home_tv_title_cn = itemView.findViewById(R.id.home_tv_title_cn);

            home_iv_img = itemView.findViewById(R.id.home_iv_img);
            home_tv_index = itemView.findViewById(R.id.home_tv_index);

            home_tv_listen = itemView.findViewById(R.id.home_tv_listen);

            home_prb_listen = itemView.findViewById(R.id.home_prb_listen);

            home_tv_eval = itemView.findViewById(R.id.home_tv_eval);
            home_prb_eval = itemView.findViewById(R.id.home_prb_eval);

            home_tv_exercise = itemView.findViewById(R.id.home_tv_exercise);
            home_prb_exercise = itemView.findViewById(R.id.home_prb_exercise);

            home_tv_imooc = itemView.findViewById(R.id.home_tv_imooc);
            home_prb_imooc = itemView.findViewById(R.id.home_prb_imooc);

            home_tv_word = itemView.findViewById(R.id.home_tv_word);
            home_iv_word = itemView.findViewById(R.id.home_iv_word);

            home_tv_download = itemView.findViewById(R.id.home_tv_download);

            home_ll_listen.setOnClickListener(onClickListener);
            home_ll_eval.setOnClickListener(onClickListener);
            home_ll_word.setOnClickListener(onClickListener);
            home_ll_exercise.setOnClickListener(onClickListener);
            home_ll_imooc.setOnClickListener(onClickListener);
            home_iv_download.setOnClickListener(onClickListener);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (callback != null) {

                        callback.itemClick(title, position);
                    }
                }
            });
        }

        private void setData(Title title, int position) {

            this.position = position;
            this.title = title;
        }
    }


    public interface Callback {

        void itemChildClick(View view, Title title, int position);

        void itemClick(Title title, int position);
    }
}
