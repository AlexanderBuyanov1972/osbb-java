package com.example.osbb.service.survey;

import java.time.LocalDate;

public interface ISelectSurveyService {
    // ------------- selects ------------
    public Object selectAllSurveyByTitle(String title);
    public Object selectAllTitles();

    public Object selectAllSurveyByQuestion(String question);

    public Object selectAllSurveyByFullName(String fullName);

    public Object selectAllSurveyByApartment(String apartment);

    public Object selectAllSurveyByTitleAndQuestion(String title, String question);

    public Object selectAllSurveyByTitleAndId(String title, Long id);

    public Object selectAllSurveyByTitleAndFullName(String title, String fullName);

    public Object selectAllSurveyByTitleAndFullNameAndApartment(String title, String fullName, String apartment);

    public Object selectAllSurveyByFullNameAndApartment(String fullName, String apartment);

    public Object selectAllSurveyByDateDispatch(LocalDate dateDispatch);

    public Object selectAllSurveyByDateReceiving(LocalDate dateReceiving);

    public Object selectAllSurveyByTitleAndDateDispatch(String title, LocalDate dateDispatch);

    public Object selectAllSurveyByTitleAndDateReceiving(String title, LocalDate dateReceiving);



}
