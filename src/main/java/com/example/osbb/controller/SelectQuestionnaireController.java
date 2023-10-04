package com.example.osbb.controller;

import com.example.osbb.service.questionnaire.ISelectQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = ApiConstants.SELECT)
public class SelectQuestionnaireController {

    @Autowired
    private ISelectQuestionnaireService service;

    @Autowired
    private HelpMethodsForController response;

    // ---------------------- one ---------------
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.PARAM_TITLE)
    public ResponseEntity<?> selectAllQuestionnaireByTitle(
            @PathVariable String title) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitle(title));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.ALL)
    public ResponseEntity<?> selectAllTitles() {
        return response.returnResponse(service.selectAllTitles());
    }

    @GetMapping(value = ApiConstants.QUESTION + ApiConstants.PARAM_QUESTION)
    public ResponseEntity<?> selectAllQuestionnaireByQuestion(
            @PathVariable String question) {
        return response.returnResponse(service
                .selectAllQuestionnaireByQuestion(question));
    }

    @GetMapping(value = ApiConstants.FULLNAME + ApiConstants.PARAM_FULL_NAME)
    public ResponseEntity<?> selectAllQuestionnaireByFullName(
            @PathVariable String fullName) {
        return response.returnResponse(service.
                selectAllQuestionnaireByFullName(fullName));
    }

    @GetMapping(value = ApiConstants.APARTMENT + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllQuestionnaireByApartment(
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllQuestionnaireByApartment(apartment));
    }

    @GetMapping(value = ApiConstants.DATE_DISPATCH + ApiConstants.PARAM_DATE_DISPATCH)
    public ResponseEntity<?> selectAllQuestionnaireByDateDispatch(
            @PathVariable LocalDate dateDispatch) {
        return response.returnResponse(service
                .selectAllQuestionnaireByDateDispatch(dateDispatch));
    }

    @GetMapping(value = ApiConstants.DATE_RECEIVING + ApiConstants.PARAM_DATE_RECEIVING)
    public ResponseEntity<?> selectAllQuestionnaireByDateReceiving(
            @PathVariable LocalDate dateReceiving) {
        return response.returnResponse(service
                .selectAllQuestionnaireByDateReceiving(dateReceiving));
    }

    // -------------- two -------------------
    @GetMapping(value = ApiConstants.FULLNAME + ApiConstants.APARTMENT + ApiConstants.PARAM_FULL_NAME + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllQuestionnaireByFullNameAndApartment(
            @PathVariable String fullName,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllQuestionnaireByFullNameAndApartment(fullName, apartment));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.APARTMENT + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndApartment(
            @PathVariable String title,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndApartment(title, apartment));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.FULLNAME + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_FULL_NAME)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndFullname(
            @PathVariable String title,
            @PathVariable String fullName) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndFullName(title, fullName));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.DATE_DISPATCH + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_DATE_DISPATCH)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndDateDispatch(
            @PathVariable String title,
            @PathVariable LocalDate dateDispatch) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndDateDispatch(title, dateDispatch));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.DATE_RECEIVING + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_DATE_RECEIVING)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndDateReceiving(
            @PathVariable String title,
            @PathVariable LocalDate dateReceiving) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndDateReceiving(title, dateReceiving));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.QUESTION + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_QUESTION)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndQuestion(
            @PathVariable String title,
            @PathVariable String question) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndQuestion(title, question));
    }


    // -------------- three -------------------

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.FULLNAME +
            ApiConstants.APARTMENT + ApiConstants.PARAM_TITLE + ApiConstants.PARAM_FULL_NAME + ApiConstants.PARAM_APARTMENT)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndFullNameAndApartment(
            @PathVariable String title,
            @PathVariable String fullName,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndFullNameAndApartment(title, fullName, apartment));
    }


    // ------------------------------------------------------------------------------



}