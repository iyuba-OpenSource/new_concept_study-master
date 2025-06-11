package com.jn.iyuba.concept.simple.util.popup;

import android.content.Context;
import android.view.View;


import com.jn.iyuba.succinct.R;
import com.jn.iyuba.succinct.databinding.PopupAnalysisBinding;
import com.jn.iyuba.concept.simple.db.ConceptWord;

import razerdp.basepopup.BasePopupWindow;

public class AnalysisPopup extends BasePopupWindow {

    private PopupAnalysisBinding popupAnalysisBinding;

    public AnalysisPopup(Context context) {
        super(context);
        View view = createPopupById(R.layout.popup_analysis);
        popupAnalysisBinding = PopupAnalysisBinding.bind(view);
        setContentView(view);

        initOperation();
    }

    private void initOperation() {

        popupAnalysisBinding.analysisIvX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }


    public void setJpWord(ConceptWord conceptWord) {

        popupAnalysisBinding.analysisTvWord.setText(conceptWord.getWord());
        popupAnalysisBinding.analysisTvPron.setText("[" + conceptWord.getPron() + "]");
        popupAnalysisBinding.analysisTvWordch.setText(conceptWord.getDef());
//        popupAnalysisBinding.analysisTvSentence.setText(word.getSentence());
//        popupAnalysisBinding.analysisTvSentencech.setText(word.getSentenceCh());
    }


}
