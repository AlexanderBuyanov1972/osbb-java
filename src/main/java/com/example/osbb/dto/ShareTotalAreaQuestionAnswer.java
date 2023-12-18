package com.example.osbb.dto;

import com.example.osbb.enums.TypeOfAnswer;

public class ShareTotalAreaQuestionAnswer {
    private String question;
    private TypeOfAnswer answer;
    // доля в квартире умноженная на общую площадь той же квартиры
    private Double shareTotalArea;

    public ShareTotalAreaQuestionAnswer() {
    }

    public ShareTotalAreaQuestionAnswer(String question, TypeOfAnswer answer, Double shareTotalArea) {
        this.question = question;
        this.answer = answer;
        this.shareTotalArea = shareTotalArea;
    }

    public String getQuestion() {
        return question;
    }

    public TypeOfAnswer getAnswer() {
        return answer;
    }

    public Double getShareTotalArea() {
        return shareTotalArea;
    }

    public ShareTotalAreaQuestionAnswer setQuestion(String question) {
        this.question = question;
        return this;
    }

    public ShareTotalAreaQuestionAnswer setAnswer(TypeOfAnswer answer) {
        this.answer = answer;
        return this;
    }

    public ShareTotalAreaQuestionAnswer setShareTotalArea(Double shareTotalArea) {
        this.shareTotalArea = shareTotalArea;
        return this;
    }

    @Override
    public String toString() {
        return "ShareTotalAreaQuestionAnswer = { question = " + question + ", answer = " + answer +
                ", shareTotalArea=" + shareTotalArea + "}";
    }
}
