package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.service.survey.ISelectSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = ApiConstants.SELECT)
public class SelectSurveyController {

    @Autowired
    private ISelectSurveyService service;

    @Autowired

    private HelpMethodsForController response;

    // ---------------------- one ---------------
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.PARAM_TITLE)
    public ResponseEntity<?> selectAllSurveyByTitle(
            @PathVariable String title) {
        return response.returnResponse(service
                .selectAllSurveyByTitle(title));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.ALL)
    public ResponseEntity<?> selectAllTitles() {
        return response.returnResponse(service.selectAllTitles());
    }

    @GetMapping(value = ApiConstants.QUESTION + ApiConstants.PARAM_QUESTION)
    public ResponseEntity<?> selectAllSurveyByQuestion(
            @PathVariable String question) {
        return response.returnResponse(service
                .selectAllSurveyByQuestion(question));
    }

    @GetMapping(value = ApiConstants.FULL_NAME + ApiConstants.PARAM_FULL_NAME)
    public ResponseEntity<?> selectAllSurveyByFullName(
            @PathVariable String fullName) {
        return response.returnResponse(service.
                selectAllSurveyByFullName(fullName));
    }

    @GetMapping(value = ApiConstants.APARTMENT + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllSurveyByApartment(
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllSurveyByApartment(apartment));
    }

    @GetMapping(value = ApiConstants.DATE_DISPATCH + ApiConstants.PARAM_DATE_DISPATCH)
    public ResponseEntity<?> selectAllSurveyByDateDispatch(
            @PathVariable LocalDate dateDispatch) {
        return response.returnResponse(service
                .selectAllSurveyByDateDispatch(dateDispatch));
    }

    @GetMapping(value = ApiConstants.DATE_RECEIVING + ApiConstants.PARAM_DATE_RECEIVING)
    public ResponseEntity<?> selectAllSurveyByDateReceiving(
            @PathVariable LocalDate dateReceiving) {
        return response.returnResponse(service
                .selectAllSurveyByDateReceiving(dateReceiving));
    }

    // -------------- two -------------------
    @GetMapping(value = ApiConstants.FULL_NAME + ApiConstants.APARTMENT + ApiConstants.PARAM_FULL_NAME + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllSurveyByFullNameAndApartment(
            @PathVariable String fullName,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllSurveyByFullNameAndApartment(fullName, apartment));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.APARTMENT + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllSurveyByTitleAndApartment(
            @PathVariable String title,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllSurveyByTitleAndApartment(title, apartment));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.FULL_NAME + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_FULL_NAME)
    public ResponseEntity<?> selectAllSurveyByTitleAndFullName(
            @PathVariable String title,
            @PathVariable String fullName) {
        return response.returnResponse(service
                .selectAllSurveyByTitleAndFullName(title, fullName));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.DATE_DISPATCH + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_DATE_DISPATCH)
    public ResponseEntity<?> selectAllSurveyByTitleAndDateDispatch(
            @PathVariable String title,
            @PathVariable LocalDate dateDispatch) {
        return response.returnResponse(service
                .selectAllSurveyByTitleAndDateDispatch(title, dateDispatch));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.DATE_RECEIVING + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_DATE_RECEIVING)
    public ResponseEntity<?> selectAllSurveyByTitleAndDateReceiving(
            @PathVariable String title,
            @PathVariable LocalDate dateReceiving) {
        return response.returnResponse(service
                .selectAllSurveyByTitleAndDateReceiving(title, dateReceiving));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.QUESTION + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_QUESTION)
    public ResponseEntity<?> selectAllSurveyByTitleAndQuestion(
            @PathVariable String title,
            @PathVariable String question) {
        return response.returnResponse(service
                .selectAllSurveyByTitleAndQuestion(title, question));
    }


    // -------------- three -------------------

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.FULL_NAME +
            ApiConstants.APARTMENT + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_FULL_NAME + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllSurveyByTitleAndFullNameAndApartment(
            @PathVariable String title,
            @PathVariable String fullName,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllSurveyByTitleAndFullNameAndApartment(title, fullName, apartment));
    }


    // ------------------------------------------------------------------------------



}