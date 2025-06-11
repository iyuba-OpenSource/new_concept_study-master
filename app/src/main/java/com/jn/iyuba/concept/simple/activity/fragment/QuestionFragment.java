package com.jn.iyuba.concept.simple.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.iyuba.module.toolbox.MD5;
import com.jn.iyuba.concept.simple.Constant;
import com.jn.iyuba.concept.simple.MyApplication;
import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.activity.BaseFragment;
import com.jn.iyuba.concept.simple.activity.login.WxLoginActivity;
import com.jn.iyuba.concept.simple.adapter.ContentQuestionAdapter;
import com.jn.iyuba.succinct.databinding.FragmentContentQuestionBinding;
import com.jn.iyuba.concept.simple.db.Exercise;
import com.jn.iyuba.concept.simple.db.Title;
import com.jn.iyuba.concept.simple.entity.Exercises;
import com.jn.iyuba.concept.simple.entity.Refresh;
import com.jn.iyuba.concept.simple.model.bean.ConceptExerciseBean;
import com.jn.iyuba.concept.simple.model.bean.UpdateTestRecordBean;
import com.jn.iyuba.concept.simple.presenter.home.ContentQuestionPresenter;
import com.jn.iyuba.concept.simple.util.LineItemDecoration;
import com.jn.iyuba.concept.simple.view.home.ContentQuestionContract;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.listener.OnGetOaidListener;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评测主页面-问题fragmnt
 */
public class QuestionFragment extends BaseFragment<ContentQuestionContract.ContentQuestionView, ContentQuestionContract.ContentQuestionPresenter>
        implements ContentQuestionContract.ContentQuestionView {

    /**
     * 新闻id
     */
    private int voaid;

    private FragmentContentQuestionBinding fragmentContentQuestionBinding;

    //选项适配器
    private ContentQuestionAdapter contentQuestionAdapter;

    private List<Exercise> exerciseList;

    //当前数据的位置
    private int position = 0;
    private SimpleDateFormat simpleDateFormat;

    private LineItemDecoration lineItemDecoration;

    /**
     * 句子填空题所使用的
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String input = s.toString();
            Exercise exercise = exerciseList.get(position);
            exercise.setCheckAnswer(input);
            exercise.setTestTime(getTime(1));
        }
    };


   /* //遍历数据存储到数据库，导出json文件
    private List<Title> dataList;
    private int dataPosition = 0;

    private int page = 1;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (dataList.size() < 100) {

                Log.d("qqqqqqq", "快完成了");
            }

            if (dataPosition < dataList.size()) {

                Title te = dataList.get(dataPosition);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.getConceptExercise(Integer.parseInt(te.getVoaId()));
                    }
                },500);

            } else {

                page++;
                dataPosition = 0;
                dataList = LitePal.where("bookid <=4").limit(100).offset((page - 1) * 100).find(Title.class);
                if (dataList.size() != 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    Log.d("qqqqqqq", "结束了");
                }
                Log.d("qqqqqqq", page + "");
            }
            return false;
        }
    });*/


    public QuestionFragment() {

        simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
    }

    /**
     * @param flag 1日期+时间 0时间
     * @return
     */

    private String getTime(int flag) {

        if (flag == 1) {

            simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        } else {

            simpleDateFormat.applyPattern("yyyy-MM-dd");
        }
        return simpleDateFormat.format(new Date());
    }

    public static QuestionFragment newInstance(int bbcid) {

        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt("VOA_ID", bbcid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voaid = getArguments().getInt("VOA_ID");
        }

        lineItemDecoration = new LineItemDecoration(MyApplication.getContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contentQuestionAdapter = new ContentQuestionAdapter(R.layout.item_content_question, new ArrayList<>());
        fragmentContentQuestionBinding.cqRvQl.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        int decCount = fragmentContentQuestionBinding.cqRvQl.getItemDecorationCount();
        if (decCount == 0) {

            fragmentContentQuestionBinding.cqRvQl.addItemDecoration(lineItemDecoration);
        }

        fragmentContentQuestionBinding.cqRvQl.setAdapter(contentQuestionAdapter);
        contentQuestionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {


                if (!contentQuestionAdapter.isCheck()) {//不能选择

                    return;
                }

                contentQuestionAdapter.setPostion(i);
                contentQuestionAdapter.notifyDataSetChanged();
                //记录用户选择的答案和位置
                Exercise exercise = exerciseList.get(position);
                exercise.setCheckPosition(i);
                exercise.setCheckAnswer(contentQuestionAdapter.getItem(i));
                exercise.setTestTime(getTime(1));
            }
        });

        initOperation();


        List<Exercise> list = LitePal.where("voaid = ?", voaid + "").find(Exercise.class);
        if (list.size() == 0) {

            presenter.getConceptExercise(voaid);
        } else {

            exerciseList = list;
            showData();
        }

    /*    //数据获取
        page = 1;
        dataPosition = 0;
        dataList = LitePal.where("bookid <= 4").limit(100).offset(0).find(Title.class);
        handler.sendEmptyMessage(1);*/

    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        fragmentContentQuestionBinding = FragmentContentQuestionBinding.inflate(inflater, container, false);
        return fragmentContentQuestionBinding.getRoot();
    }

    @Override
    protected ContentQuestionContract.ContentQuestionPresenter initPresenter() {
        return new ContentQuestionPresenter();
    }


    /**
     * 获取数据显示数据
     */
    private void showData() {

        if (exerciseList.size() > 0) {

            Exercise exercise = exerciseList.get(0);
            if (exercise.getUpload() == 1) {//如果上传过了，则直接进入到对照答案状态

                contentQuestionAdapter.setCheck(false);
                position = 0;
                showQuestion();
            } else {

                exercise.setBeginTime(getTime(1));
                position = 0;
                showQuestion();
            }
        }
    }

    /**
     * 切换数据
     */
    public void switchData() {

        if (getArguments() != null) {
            voaid = getArguments().getInt("VOA_ID");
        }
        showData();
    }

    private void initOperation() {

        fragmentContentQuestionBinding.cqTvPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (position <= 0) {
                    position = 0;
                    if (!contentQuestionAdapter.isCheck()) {//重做

                        redoQuestion();
                    }
                    return;
                }

                position--;
                showQuestion();
            }
        });
        fragmentContentQuestionBinding.cqTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position >= (exerciseList.size() - 1)) {


                    if (!contentQuestionAdapter.isCheck()) {//不能选择选线则是在对比答案状态

                        toast("已经是最后一页了");
                        return;
                    }

                    if (Constant.userinfo == null) {

                        startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                        toast("请登录");
                        return;
                    }
                    boolean isComplete = true;
                    for (int i = 0; i < exerciseList.size(); i++) {

                        Exercise exercise = exerciseList.get(i);
                        if (exercise.getCheckPosition() == -1 && exercise.getCheckAnswer() == null) {

                            isComplete = false;
                            break;
                        }
                    }

                    if (!isComplete) {//没有完成

                        Toast.makeText(MyApplication.getContext(), "还有题没有作答", Toast.LENGTH_SHORT).show();
                    } else {//完成

                        submitQuestion();
                    }
                } else {

                    position++;
                    exerciseList.get(position).setBeginTime(getTime(1));
                    showQuestion();
                }
            }
        });
    }


    /**
     * 重做练习题
     */
    private void redoQuestion() {


        for (int i = 0; i < exerciseList.size(); i++) {

            Exercise exercise = exerciseList.get(i);
            exercise.setCheckPosition(-1);
            exercise.setCheckAnswer(null);
            if (i == 0) {
                exercise.setBeginTime(getTime(1));
            }
        }

        contentQuestionAdapter.setCheck(true);
        showQuestion();
    }

    /**
     * 完成问题上传
     */
    private void submitQuestion() {

        List<Exercises> exercisesList = new ArrayList<>();
        for (int i = 0; i < exerciseList.size(); i++) {

            Exercise exercise = exerciseList.get(i);
            int AnswerResut = 0;//判断答题是否正确
            String userAnswer;//要上传ABCD 转换一下

            if (exercise.getNote() == null) {//选择

                if (exercise.getCheckPosition() == (Integer.parseInt(exercise.getAnswer()) - 1)) {

                    AnswerResut = 1;
                }
                if (exercise.getCheckPosition() == 0) {

                    userAnswer = "A";
                } else if (exercise.getCheckPosition() == 1) {

                    userAnswer = "B";
                } else if (exercise.getCheckPosition() == 2) {

                    userAnswer = "C";
                } else {

                    userAnswer = "D";
                }
            } else {

                if (exercise.getCheckAnswer().equals(exercise.getAnswer())) {
                    AnswerResut = 1;
                }
                userAnswer = exercise.getCheckAnswer();
            }


            int testNumber;
            if (exercise.getNote() == null) {

                testNumber = Integer.parseInt(exercise.getIndexId());
            } else {
                testNumber = Integer.parseInt(exercise.getNumber());
            }


            Exercises exercises = new Exercises(AnswerResut, exercise.getBeginTime(), voaid, exercise.getAnswer(),
                    testNumber, exercise.getTestTime(), userAnswer, Constant.userinfo == null ? 0 : Constant.userinfo.getUid());
            exercisesList.add(exercises);
        }

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("datalist", new Gson().toJson(exercisesList));


        simpleDateFormat.applyPattern("yyyy-MM-dd");
        UMConfigure.getOaid(MyApplication.getContext(), new OnGetOaidListener() {
            @Override
            public void onGetOaid(String s) {

                String jsonStr = null;
                try {
                    jsonStr = URLEncoder.encode(new Gson().toJson(stringStringMap), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (s == null) {

                    return;
                }

                String signStr = MD5.getMD5ofStr(Constant.userinfo.getUid() + "iyubaTest" + simpleDateFormat.format(new Date()));
                presenter.updateTestRecordNew("json", Constant.TYPE, signStr, Constant.userinfo.getUid() + "",
                        Constant.APPID + "", 0, s, jsonStr);
            }
        });
    }

    /**
     * 显示数据
     */
    private void showQuestion() {


        Exercise exercise = exerciseList.get(position);

        //根据可选状态显示或者隐藏答案,及按钮的显示文字
        if (contentQuestionAdapter.isCheck()) {//可选时，不显示

            fragmentContentQuestionBinding.cqTvAnalysis.setVisibility(View.GONE);
            if ((position + 1) == exerciseList.size()) {//最后一页按钮显示提交

                fragmentContentQuestionBinding.cqTvNext.setText("提交");
            } else {
                fragmentContentQuestionBinding.cqTvNext.setText("下一页");
            }
            fragmentContentQuestionBinding.cqTvPre.setText("上一页");
        } else {

            String analysis;
            if (exercise.getNote() == null) {

                analysis = exercise.getAnswer().replace("1", "A")
                        .replace("2", "B")
                        .replace("3", "C")
                        .replace("4", "D");
            } else {

                analysis = exercise.getAnswer();
            }
            fragmentContentQuestionBinding.cqTvAnalysis.setText("答案：" + analysis);
            fragmentContentQuestionBinding.cqTvAnalysis.setVisibility(View.VISIBLE);
            fragmentContentQuestionBinding.cqTvNext.setText("下一页");
            if (position == 0) {

                fragmentContentQuestionBinding.cqTvPre.setText("重做");
            } else {

                fragmentContentQuestionBinding.cqTvPre.setText("上一页");
            }
        }

        //判断题型
        if (exercise.getNote() == null) {//单选题

            //隐藏note
            fragmentContentQuestionBinding.cqTvNote.setVisibility(View.GONE);
            fragmentContentQuestionBinding.cqEtAnswer.setVisibility(View.GONE);
            fragmentContentQuestionBinding.cqRvQl.setVisibility(View.VISIBLE);

        } else {//多选题

            fragmentContentQuestionBinding.cqTvNote.setVisibility(View.VISIBLE);
            fragmentContentQuestionBinding.cqEtAnswer.setVisibility(View.VISIBLE);
            fragmentContentQuestionBinding.cqRvQl.setVisibility(View.GONE);
            fragmentContentQuestionBinding.cqEtAnswer.removeTextChangedListener(textWatcher);
            fragmentContentQuestionBinding.cqEtAnswer.setText("");
        }

        if (exercise.getNote() == null) {//单选

            //题目
            fragmentContentQuestionBinding.cqTvQuestion.setText((position + 1) + "." + exercise.getQuestion());
            //选项
            List<String> optionList = new ArrayList<>();
            optionList.add(exercise.getChoiceA());
            optionList.add(exercise.getChoiceB());
            optionList.add(exercise.getChoiceC());
            if (exercise.getChoiceD() != null && !exercise.getChoiceD().equals("")) {

                optionList.add(exercise.getChoiceD());
            }
            //用户选择的位置
            contentQuestionAdapter.setPostion(exercise.getCheckPosition());
            contentQuestionAdapter.setNewData(optionList);
        } else {//填空

            //题目描述
            fragmentContentQuestionBinding.cqTvQuestion.setText((position + 1) + "." + exercise.getDescCh());
            fragmentContentQuestionBinding.cqTvNote.setText(exercise.getNote());

            if (exercise.getCheckAnswer() != null && !exercise.getCheckAnswer().equals("")) {

                fragmentContentQuestionBinding.cqEtAnswer.setText(exercise.getCheckAnswer());
                fragmentContentQuestionBinding.cqEtAnswer.setSelection(exercise.getCheckAnswer().length());
            }
            fragmentContentQuestionBinding.cqEtAnswer.addTextChangedListener(textWatcher);
        }
        //题号
        fragmentContentQuestionBinding.cqTvNum.setText((position + 1) + "/" + exerciseList.size());
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
    public void updateTestRecordComplete(UpdateTestRecordBean updateTestRecordBean) {

        //更新数据库upload
        for (int i = 0; i < exerciseList.size(); i++) {

            Exercise exercise = exerciseList.get(i);
            exercise.setUpload(1);
            if (exercise.getNote() == null) {

                exercise.updateAll("voaid = ? and indexid = ?", exercise.getVoaId(), exercise.getIndexId());
            } else {

                exercise.updateAll("voaid = ? and number = ?", exercise.getVoaId(), exercise.getNumber());
            }
        }
        contentQuestionAdapter.setCheck(false);
        position = 0;

        //触发更新首页练习数据
        EventBus.getDefault().post(new Refresh(voaid));
        showQuestion();
    }

    @Override
    public void getConceptExercise(ConceptExerciseBean conceptExerciseBean) {

        //存储选择题
        List<Exercise> exercisesList = conceptExerciseBean.getMultipleChoice();
        for (int i = 0; i < exercisesList.size(); i++) {

            Exercise exercise = exercisesList.get(i);
            List<Exercise> list = LitePal.where("voaid = ? and indexid = ?", exercise.getVoaId(), exercise.getIndexId()).find(Exercise.class);
            if (list.size() == 0) {

                exercise.save();
            } else {

                exercise.updateAll("voaid = ? and indexid = ?", exercise.getVoaId(), exercise.getIndexId());
            }
        }
        //存储填空
        List<Exercise> fillExercisesList = conceptExerciseBean.getVoaStructureExercise();
        for (int i = 0; i < fillExercisesList.size(); i++) {

            Exercise fillExercise = fillExercisesList.get(i);
            List<Exercise> list = LitePal.where("voaid = ? and number = ?", fillExercise.getVoaId(), fillExercise.getNumber()).find(Exercise.class);
            if (list.size() == 0) {

                fillExercise.save();
            } else {

                fillExercise.updateAll("voaid = ? and number = ?", fillExercise.getVoaId(), fillExercise.getNumber());
            }
        }
        exerciseList = new ArrayList<>();
        exerciseList.addAll(exercisesList);
        exerciseList.addAll(fillExercisesList);

        showData();

//        //存储数据，导出json文件
//        dataPosition++;
//        handler.sendEmptyMessage(1);
    }

    /**
     * 更新数据
     */
    public void updateData() {

        if (getArguments() != null) {

            voaid = getArguments().getInt("VOA_ID");
        }
        List<Exercise> list = LitePal.where("voaid = ?", voaid + "").find(Exercise.class);
        if (list.size() == 0) {

            presenter.getConceptExercise(voaid);
        } else {

            exerciseList = list;
            showData();
        }
    }
}