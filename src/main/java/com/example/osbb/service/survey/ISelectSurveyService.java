package com.example.osbb.service.survey;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface ISelectSurveyService {
    // ------------- selects ------------
    ResponseEntity<?> selectAllSurveyByTitle(String title);
    ResponseEntity<?> selectAllTitles();

    ResponseEntity<?> selectAllSurveyByQuestion(String question);

    ResponseEntity<?> selectAllSurveyByFullName(String fullName);

    ResponseEntity<?> selectAllSurveyByApartment(String apartment);

    ResponseEntity<?> selectAllSurveyByTitleAndQuestion(String title, String question);

    ResponseEntity<?> selectAllSurveyByTitleAndId(String title, Long id);

    ResponseEntity<?> selectAllSurveyByTitleAndFullName(String title, String fullName);

    ResponseEntity<?> selectAllSurveyByTitleAndFullNameAndApartment(String title, String fullName, String apartment);

    ResponseEntity<?> selectAllSurveyByFullNameAndApartment(String fullName, String apartment);

    ResponseEntity<?> selectAllSurveyByDateDispatch(LocalDate dateDispatch);

    ResponseEntity<?> selectAllSurveyByDateReceiving(LocalDate dateReceiving);

    ResponseEntity<?> selectAllSurveyByTitleAndDateDispatch(String title, LocalDate dateDispatch);

    ResponseEntity<?> selectAllSurveyByTitleAndDateReceiving(String title, LocalDate dateReceiving);



}
