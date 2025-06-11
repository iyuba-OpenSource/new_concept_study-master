package com.jn.iyuba.concept.simple.util.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jn.iyuba.succinct.R;

import razerdp.basepopup.BasePopupWindow;

public class LoadingPopup extends BasePopupWindow {

    private TextView loading_tv_content;

    public LoadingPopup(Context context) {
        super(context);

        View view = createPopupById(R.layout.popup_loading);
        setContentView(view);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);

        loading_tv_content = contentView.findViewById(R.id.loading_tv_content);

    }

    public void setContent(String content) {

        loading_tv_content.setText(content);
    }


}
