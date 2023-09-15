package com.example.osbb.service.questionnaire;


import com.example.osbb.entity.Questionnaire;

import java.util.List;

public interface IQuestionnaireService {

    public Object createQuestionnaire(Questionnaire Questionnaire);

    public Object updateQuestionnaire(Questionnaire Questionnaire);

    public Object getQuestionnaire(Long id);

    public Object deleteQuestionnaire(Long id);

    // ------------------ all ----------------

    public Object createAllQuestionnaire(List<Questionnaire> questionnaires);

    public Object updateAllQuestionnaire(List<Questionnaire> questionnaires);

    public Object getAllQuestionnaire();

    public Object deleteAllQuestionnaire();

    // ------------- results --------------
    public Object getResultOfQuestionnaireByTitle(String title);
}
