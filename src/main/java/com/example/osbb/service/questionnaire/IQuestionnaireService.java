package com.example.osbb.service.questionnaire;

import com.example.osbb.entity.Questionnaire;

import java.time.LocalDate;
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

    // ------------- selects ------------
    public Object selectAllQuestionnaireByTitle(String title);

    public Object selectAllQuestionnaireByQuestion(String question);

    public Object selectAllQuestionnaireByFullname(String fullname);

    public Object selectAllQuestionnaireByApartment(String apartmebt);

    public Object selectAllQuestionnaireByTitleAndQuestion(String title, String question);

    public Object selectAllQuestionnaireByTitleAndApartment(String title, String apartment);

    public Object selectAllQuestionnaireByTitleAndFullname(String title, String fullname);

    public Object selectAllQuestionnaireByTitleAndFullnameAndApartment(String title, String fullname, String apartment);

    public Object selectAllQuestionnaireByFullnameAndApartment(String fullname, String apartment);

    public Object selectAllQuestionnaireByDateDispatch(LocalDate dateDispatch);

    public Object selectAllQuestionnaireByDateReceiving(LocalDate dateReceiving);

    public Object selectAllQuestionnaireByTitleAndDateDispatch(String title, LocalDate dateDispatch);

    public Object selectAllQuestionnaireByTitleAndDateReceiving(String title, LocalDate dateReceiving);


    // ------------- results --------------
    public Object getResultOfQuestionnaireByTitle(String title);

    // generate
    public Object generateListQuestionnaire(List<Questionnaire> questionnaires);
}
