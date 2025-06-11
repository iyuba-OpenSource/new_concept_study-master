package com.jn.iyuba.concept.simple.model.bean;

import com.google.gson.annotations.SerializedName;
import com.jn.iyuba.concept.simple.db.Exercise;

import java.util.List;

public class ConceptExerciseBean {

    @SerializedName("MultipleChoice")
    private List<Exercise> multipleChoice;
    @SerializedName("VoaStructureExercise")
    private List<Exercise> voaStructureExercise;
    @SerializedName("SizeVoaDiffcultyExercise")
    private int sizeVoaDiffcultyExercise;
    @SerializedName("SizeVoaStructureExercise")
    private int sizeVoaStructureExercise;
    @SerializedName("SizeMultipleChoice")
    private int sizeMultipleChoice;
    @SerializedName("VoaDiffcultyExercise")
    private List<?> voaDiffcultyExercise;

    public List<Exercise> getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(List<Exercise> multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public List<Exercise> getVoaStructureExercise() {
        return voaStructureExercise;
    }

    public void setVoaStructureExercise(List<Exercise> voaStructureExercise) {
        this.voaStructureExercise = voaStructureExercise;
    }

    public int getSizeVoaDiffcultyExercise() {
        return sizeVoaDiffcultyExercise;
    }

    public void setSizeVoaDiffcultyExercise(int sizeVoaDiffcultyExercise) {
        this.sizeVoaDiffcultyExercise = sizeVoaDiffcultyExercise;
    }

    public int getSizeVoaStructureExercise() {
        return sizeVoaStructureExercise;
    }

    public void setSizeVoaStructureExercise(int sizeVoaStructureExercise) {
        this.sizeVoaStructureExercise = sizeVoaStructureExercise;
    }

    public int getSizeMultipleChoice() {
        return sizeMultipleChoice;
    }

    public void setSizeMultipleChoice(int sizeMultipleChoice) {
        this.sizeMultipleChoice = sizeMultipleChoice;
    }

    public List<?> getVoaDiffcultyExercise() {
        return voaDiffcultyExercise;
    }

    public void setVoaDiffcultyExercise(List<?> voaDiffcultyExercise) {
        this.voaDiffcultyExercise = voaDiffcultyExercise;
    }


}
