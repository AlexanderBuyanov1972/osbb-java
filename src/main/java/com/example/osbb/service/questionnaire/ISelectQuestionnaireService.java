package com.example.osbb.service.questionnaire;

import java.time.LocalDate;

public interface ISelectQuestionnaireService {
    // ------------- selects ------------
    public Object selectAllQuestionnaireByTitle(String title);
    public Object selectAllTitles();

    public Object selectAllQuestionnaireByQuestion(String question);

    public Object selectAllQuestionnaireByFullname(String fullname);

    public Object selectAllQuestionnaireByApartment(String apartment);

    public Object selectAllQuestionnaireByTitleAndQuestion(String title, String question);

    public Object selectAllQuestionnaireByTitleAndApartment(String title, String apartment);

    public Object selectAllQuestionnaireByTitleAndFullname(String title, String fullname);

    public Object selectAllQuestionnaireByTitleAndFullnameAndApartment(String title, String fullname, String apartment);

    public Object selectAllQuestionnaireByFullnameAndApartment(String fullname, String apartment);

    public Object selectAllQuestionnaireByDateDispatch(LocalDate dateDispatch);

    public Object selectAllQuestionnaireByDateReceiving(LocalDate dateReceiving);

    public Object selectAllQuestionnaireByTitleAndDateDispatch(String title, LocalDate dateDispatch);

    public Object selectAllQuestionnaireByTitleAndDateReceiving(String title, LocalDate dateReceiving);


}
