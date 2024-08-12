package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.service.survey.ISelectSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = ApiPaths.SELECT)
public class SelectSurveyController {

    @Autowired
    private ISelectSurveyService service;

    // ---------------------- one ---------------
    @GetMapping(value = ApiPaths.TITLE + ApiPaths.PARAM_TITLE)
    public ResponseEntity<?> selectAllSurveyByTitle(
            @PathVariable String title) {
        return service.selectAllSurveyByTitle(title);
    }

    @GetMapping(value = ApiPaths.TITLE + ApiPaths.ALL)
    public ResponseEntity<?> selectAllTitles() {
        return service.selectAllTitles();
    }

    @GetMapping(value = ApiPaths.QUESTION + ApiPaths.PARAM_QUESTION)
    public ResponseEntity<?> selectAllSurveyByQuestion(
            @PathVariable String question) {
        return service.selectAllSurveyByQuestion(question);
    }

    @GetMapping(value = ApiPaths.FULL_NAME + ApiPaths.PARAM_FULL_NAME)
    public ResponseEntity<?> selectAllSurveyByFullName(
            @PathVariable String fullName) {
        return service.selectAllSurveyByFullName(fullName);
    }

    @GetMapping(value = ApiPaths.APARTMENT + ApiPaths.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllSurveyByApartment(
            @PathVariable String apartment) {
        return service.selectAllSurveyByApartment(apartment);
    }

    @GetMapping(value = ApiPaths.DATE_DISPATCH + ApiPaths.PARAM_DATE_DISPATCH)
    public ResponseEntity<?> selectAllSurveyByDateDispatch(
            @PathVariable LocalDate dateDispatch) {
        return service.selectAllSurveyByDateDispatch(dateDispatch);
    }

    @GetMapping(value = ApiPaths.DATE_RECEIVING + ApiPaths.PARAM_DATE_RECEIVING)
    public ResponseEntity<?> selectAllSurveyByDateReceiving(
            @PathVariable LocalDate dateReceiving) {
        return service.selectAllSurveyByDateReceiving(dateReceiving);
    }

    // -------------- two -------------------
    @GetMapping(value = ApiPaths.FULL_NAME + ApiPaths.APARTMENT + ApiPaths.PARAM_FULL_NAME + ApiPaths.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllSurveyByFullNameAndApartment(
            @PathVariable String fullName,
            @PathVariable String apartment) {
        return service.selectAllSurveyByFullNameAndApartment(fullName, apartment);
    }

    // выбрать все опросы по теме и ID помещения
    @GetMapping(value = ApiPaths.TITLE + ApiPaths.ID + ApiPaths.PARAM_TITLE + ApiPaths.PARAM_ID)
    public ResponseEntity<?> getAllSurveyByTitleAndById(@PathVariable String title, @PathVariable Long id) {
        return service.selectAllSurveyByTitleAndId(title, id);
    }

    @GetMapping(value = ApiPaths.TITLE + ApiPaths.FULL_NAME + ApiPaths.PARAM_TITLE + ApiPaths.PARAM_FULL_NAME)
    public ResponseEntity<?> selectAllSurveyByTitleAndFullName(
            @PathVariable String title,
            @PathVariable String fullName) {
        return service.selectAllSurveyByTitleAndFullName(title, fullName);
    }

    @GetMapping(value = ApiPaths.TITLE + ApiPaths.DATE_DISPATCH + ApiPaths.PARAM_TITLE + ApiPaths.PARAM_DATE_DISPATCH)
    public ResponseEntity<?> selectAllSurveyByTitleAndDateDispatch(
            @PathVariable String title,
            @PathVariable LocalDate dateDispatch) {
        return service.selectAllSurveyByTitleAndDateDispatch(title, dateDispatch);
    }

    @GetMapping(value = ApiPaths.TITLE + ApiPaths.DATE_RECEIVING + ApiPaths.PARAM_TITLE + ApiPaths.PARAM_DATE_RECEIVING)
    public ResponseEntity<?> selectAllSurveyByTitleAndDateReceiving(
            @PathVariable String title,
            @PathVariable LocalDate dateReceiving) {
        return service.selectAllSurveyByTitleAndDateReceiving(title, dateReceiving);
    }

    @GetMapping(value = ApiPaths.TITLE + ApiPaths.QUESTION + ApiPaths.PARAM_TITLE + ApiPaths.PARAM_QUESTION)
    public ResponseEntity<?> selectAllSurveyByTitleAndQuestion(
            @PathVariable String title,
            @PathVariable String question) {
        return service.selectAllSurveyByTitleAndQuestion(title, question);
    }


    // -------------- three -------------------

    @GetMapping(value = ApiPaths.TITLE + ApiPaths.FULL_NAME +
            ApiPaths.APARTMENT + ApiPaths.PARAM_TITLE + ApiPaths.PARAM_FULL_NAME + ApiPaths.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllSurveyByTitleAndFullNameAndApartment(
            @PathVariable String title,
            @PathVariable String fullName,
            @PathVariable String apartment) {
        return service.selectAllSurveyByTitleAndFullNameAndApartment(title, fullName, apartment);
    }


    // ------------------------------------------------------------------------------


}