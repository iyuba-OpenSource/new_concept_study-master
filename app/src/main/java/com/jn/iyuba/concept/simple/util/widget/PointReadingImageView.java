package com.jn.iyuba.concept.simple.util.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jn.iyuba.concept.simple.model.bean.PointReadingBean;
import com.jn.iyuba.succinct.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 点读的imageview
 */
public class PointReadingImageView extends androidx.appcompat.widget.AppCompatImageView {

    private List<PointReadingBean.VoatextDTO> voatextDTOList;

    private Paint paint;

    private List<Rect> rects;

    private MyClickListen myClickListen;


    public PointReadingImageView(@NonNull Context context) {
        super(context);

        init();
    }

    public PointReadingImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public PointReadingImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    private void init() {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#D2AAFF"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6.0f);
    }

    public void initRect() {

        if (voatextDTOList == null) {

            return;
        }

        rects = new ArrayList<>();
        for (int i = 0; i < voatextDTOList.size(); i++) {

            PointReadingBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
            //老坐标
            int _left = Integer.parseInt(voatextDTO.getStartX());
            int _top = Integer.parseInt(voatextDTO.getStartY());
            int _right = Integer.parseInt(voatextDTO.getEndX());
            int _bottom = Integer.parseInt(voatextDTO.getEndY());


            int _w = 750;//图片原始高度
            int w = getWidth();
            float wScale = (float) (1.0 * w / _w);//缩放比例
            int _h = 1060;//原始高度
            int h = (int) (1.0 * _h * wScale);
            //计算空白区域
            int blank = (getHeight() - h) / 2;


            int left = (int) (_left * wScale);
            int top = (int) (_top * wScale);
            int right = (int) (_right * wScale);
            int bottom = (int) (_bottom * wScale);

            Rect rect = new Rect(left, blank + top, right, blank + bottom);
            voatextDTO.setRect(rect);
            rects.add(rect);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (voatextDTOList == null) {

            return;
        }

        initRect();

        for (int i = 0; i < rects.size(); i++) {

            Rect rect = rects.get(i);
            canvas.drawRect(rect, paint);
        }
    }


    public List<PointReadingBean.VoatextDTO> getVoatextDTOList() {
        return voatextDTOList;
    }

    public void setVoatextDTOList(List<PointReadingBean.VoatextDTO> voatextDTOList) {
        this.voatextDTOList = voatextDTOList;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {

            for (int i = 0; i < voatextDTOList.size(); i++) {

                PointReadingBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
                Rect rect = voatextDTO.getRect();
                if (rect.contains(x, y)) {

                    if (myClickListen != null) {

                        myClickListen.get(voatextDTO);
                    }
                    return true;
                }
            }
        } else {

            return true;
        }
        return false;
    }

    public MyClickListen getMyClickListen() {
        return myClickListen;
    }

    public void setMyClickListen(MyClickListen myClickListen) {
        this.myClickListen = myClickListen;
    }

    public interface MyClickListen {

        void get(PointReadingBean.VoatextDTO voatextDTO);
    }
}
