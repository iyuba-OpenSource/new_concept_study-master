package com.jn.iyuba.concept.simple.activity.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.module.toolbox.MD5;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MainActivity;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.concept.simple.activity.BaseActivity;
import com.jn.iyuba.concept.simple.adapter.break_through.AnswerAdapter;
import com.jn.iyuba.concept.simple.db.ConceptWord;
import com.jn.iyuba.concept.simple.entity.ExamRecordPost;
import com.jn.iyuba.concept.simple.entity.Refresh;
import com.jn.iyuba.concept.simple.entity.Score;
import com.jn.iyuba.concept.simple.entity.TestRecord;
import com.jn.iyuba.concept.simple.entity.WordQuestion;
import com.jn.iyuba.concept.simple.model.bean.ExamRecordBean;
import com.jn.iyuba.concept.simple.presenter.break_through.WordAnswerPresenter;
import com.jn.iyuba.concept.simple.util.BookUtil;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.concept.simple.util.popup.AnalysisPopup;
import com.jn.iyuba.concept.simple.util.popup.BTFailPopup;
import com.jn.iyuba.concept.simple.util.popup.BTSuccessPopup;
import com.jn.iyuba.concept.simple.util.popup.WordTipPopup;
import com.jn.iyuba.concept.simple.view.break_through.WordAnswerContract;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.ActivityWordAnswerBinding;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 单词训练页面
 */
public class WordExerciseActivity extends BaseActivity<WordAnswerContract.WordAnswerView, WordAnswerContract.WordAnswerPresenter>
        implements WordAnswerContract.WordAnswerView {

    private ActivityWordAnswerBinding activityWordAnswerBinding;

    private List<ConceptWord> jpConceptWordList;

    private List<WordQuestion> wordQuestions;

    private Random random;
    private int position;

    private AnswerAdapter answerAdapter;

    private AnalysisPopup analysisPopup;

    private DecimalFormat decimalFormat;

    private LineItemDecoration lineItemDecoration;

    /**
     * type
     * 0：中文
     * 1：英文
     */
    private int questionType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = 0;
        random = new Random();
        decimalFormat = new DecimalFormat("##.0");
        getBundle();


        activityWordAnswerBinding.toolbar.toolbarIvRight.setVisibility(View.GONE);
        initData();
        initOperation();
        showData();
    }

    @Override
    public View initLayout() {

        activityWordAnswerBinding = ActivityWordAnswerBinding.inflate(getLayoutInflater());
        return activityWordAnswerBinding.getRoot();
    }

    @Override
    public WordAnswerContract.WordAnswerPresenter initPresenter() {
        return new WordAnswerPresenter();
    }

    private void initOperation() {

        activityWordAnswerBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        activityWordAnswerBinding.toolbar.toolbarIvTitle.setText((position + 1) + "/" + jpConceptWordList.size());

        lineItemDecoration = new LineItemDecoration(WordExerciseActivity.this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(getResources().getDrawable(R.drawable.space_10dp));

        if (activityWordAnswerBinding.waRvAnswers.getItemDecorationCount() == 0) {

            activityWordAnswerBinding.waRvAnswers.addItemDecoration(lineItemDecoration);
        }

        activityWordAnswerBinding.waRvAnswers.setLayoutManager(new LinearLayoutManager(this));
        answerAdapter = new AnswerAdapter(R.layout.item_answer, new ArrayList<>());
        activityWordAnswerBinding.waRvAnswers.setAdapter(answerAdapter);
        answerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int listPosition) {

                //选择了就不能再选了
                if (answerAdapter.getChoosePosition() != -1 && answerAdapter.gettPosition() != -1) {

                    return;
                }

                WordQuestion wordQuestion = wordQuestions.get(position);
                wordQuestion.setChoosePosition(listPosition);//记录选择的位置
                wordQuestion.setTestTime(getCurrentTime());//记录作答的时间
                answerAdapter.setChoosePosition(listPosition);//适配器记录选择的位置
                answerAdapter.settPosition(wordQuestion.gettPosition());//适配器记录正确答案的位置
                answerAdapter.notifyDataSetChanged();

                //显示下一个按钮
                activityWordAnswerBinding.waLlButton.setVisibility(View.VISIBLE);
                ConceptWord conceptWord = wordQuestion.getWord();
                if (listPosition == wordQuestion.gettPosition()) {//选中的是正确的

                    conceptWord.setAnswer_status(1);
                } else {

                    conceptWord.setAnswer_status(2);
                }
            }
        });
        //下一个
        activityWordAnswerBinding.waButNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == (wordQuestions.size() - 1)) {

                    showResultAlert();
                    return;
                }

                position++;
                //设置答题开始时间
                wordQuestions.get(position).setBeginTime(getCurrentTime());

                activityWordAnswerBinding.toolbar.toolbarIvTitle.setText((position + 1) + "/" + jpConceptWordList.size());
                answerAdapter.settPosition(-1);
                answerAdapter.setChoosePosition(-1);
                showData();
            }
        });
        activityWordAnswerBinding.waButAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initAnalysisPopup(wordQuestions.get(position).getWord());
            }
        });
    }

    /**
     * 检测闯关是否失败
     */
    private void showResultAlert() {

        int totalDoQuestion = 0;//共做多少道题
        int tNUM = 0;//已做题正确数量
        int fNum = 0;//已做题错误数量

        for (int i = 0; i < wordQuestions.size(); i++) {

            WordQuestion wq = wordQuestions.get(i);
            if (wq.getChoosePosition() == -1) {
                break;
            } else {
                totalDoQuestion++;
                if (wq.gettPosition() == wq.getChoosePosition()) {
                    tNUM++;
                } else {
                    fNum++;
                }
            }
        }
//        double fPercentage = 100.0 * fNum / wordQuestions.size();//总的错误率
//        double questionPercentage = 100.0 * totalDoQuestion / wordQuestions.size();//总做题进度

        double tPercentage = 100.0 * tNUM / totalDoQuestion;

        AlertDialog alertDialog = new AlertDialog.Builder(WordExerciseActivity.this)
                .setTitle("训练结果")
                .setMessage("共做：" + totalDoQuestion + "题，\n做对：" + tNUM + "题，\n正确比例" + decimalFormat.format(tPercentage) + "%")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }


    /**
     * 解释、解析弹窗
     *
     * @param conceptWord
     */
    private void initAnalysisPopup(ConceptWord conceptWord) {

        if (analysisPopup == null) {

            analysisPopup = new AnalysisPopup(this);
        }

        analysisPopup.setJpWord(conceptWord);
        analysisPopup.showPopupWindow();
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String strCurrTime = formatter.format(curDate);
        return strCurrTime;
    }

    private void initData() {

        //找出此level单词最小id与最大id
        List<ConceptWord> jps = LitePal.where("bookid =?", Constant.wordBook.getBookid() + "").order("id").limit(1).find(ConceptWord.class);
        int minId = jps.get(0).getId();
        List<ConceptWord> jpsMax = LitePal.where("bookid =?", Constant.wordBook.getBookid() + "").order("id  desc").limit(1).find(ConceptWord.class);
        int maxId = jpsMax.get(0).getId();


        wordQuestions = new ArrayList<>();
        for (int i = 0; i < jpConceptWordList.size(); i++) {

            ConceptWord conceptWord = jpConceptWordList.get(i);
            WordQuestion wordQuestion = new WordQuestion();
            wordQuestion.setWord(conceptWord);
            wordQuestion.setAnswerList(getAnswerData(minId, maxId, Integer.parseInt(conceptWord.getPosition())));
            int tPosition = random.nextInt(4);
            wordQuestion.getAnswerList().set(tPosition, conceptWord);
            wordQuestion.settPosition(tPosition);
            wordQuestion.setType(questionType);
            wordQuestions.add(wordQuestion);
        }
        //设置开始时间
        wordQuestions.get(0).setBeginTime(getCurrentTime());
    }


    private void showData() {

        //隐藏按钮
        activityWordAnswerBinding.waLlButton.setVisibility(View.INVISIBLE);

        WordQuestion wordQuestion = wordQuestions.get(position);
        if (wordQuestion.getType() == 0) {

            activityWordAnswerBinding.waTvQuestion.setText(wordQuestion.getWord().getDef());
        } else {

            activityWordAnswerBinding.waTvQuestion.setText(wordQuestion.getWord().getWord());
        }
        answerAdapter.setType(wordQuestion.getType());
        answerAdapter.setNewData(wordQuestion.getAnswerList());
    }

    /**
     * 创建答题项
     *
     * @param min
     * @param max
     * @param position 单词的position
     * @return
     */
    private List<ConceptWord> getAnswerData(int min, int max, int position) {

        List<ConceptWord> answerList = new ArrayList<>();
        do {

            int id = random.nextInt((max + 1) - min) + min;
            while (id == position) {

                id = random.nextInt((max + 1) - min) + min;
            }

            List<ConceptWord> jpConceptWords = LitePal.where("id = ? and  bookid = ?", id + "", Constant.wordBook.getBookid() + "").find(ConceptWord.class);
            if (jpConceptWords.size() != 0) {//有数据

                ConceptWord addConceptWord = jpConceptWords.get(0);
                boolean isAdd = true;//添加不重复的选项
                for (int i = 0; i < answerList.size(); i++) {

                    ConceptWord jp = answerList.get(i);
                    if (jp.getPosition().equals(addConceptWord.getPosition())) {

                        isAdd = false;
                    }
                }
                if (isAdd) {

                    answerList.add(addConceptWord);
                }
            }
        } while (answerList.size() != 4);
        return answerList;
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            jpConceptWordList = (List<ConceptWord>) bundle.getSerializable("DATAS");
            questionType = bundle.getInt("QUESTION_TYPE");
        }
    }

    public static void startActivity(Activity activity, List<ConceptWord> jpConceptWordList, int questionType) {

        Intent intent = new Intent(activity, WordExerciseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATAS", (Serializable) jpConceptWordList);
        bundle.putInt("QUESTION_TYPE", questionType);
        intent.putExtras(bundle);
        activity.startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();

        if (jpConceptWordList != null && jpConceptWordList.size() > 0) {
            //更新单词进度
            ConceptWord conceptWord = jpConceptWordList.get(0);
            EventBus.getDefault().post(new Refresh(conceptWord.getVoaId()));
        }
    }


    @Override
    public void updateExamRecordComplete(ExamRecordBean examRecordBean) {

    }
}