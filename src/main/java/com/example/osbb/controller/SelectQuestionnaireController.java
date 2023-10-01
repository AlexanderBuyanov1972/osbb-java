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
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.PARAM_1)
    public ResponseEntity<?> selectAllQuestionnaireByTitle(
            @PathVariable String title) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitle(title));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.ALL)
    public ResponseEntity<?> selectAllTitles() {
        return response.returnResponse(service.selectAllTitles());
    }

    @GetMapping(value = ApiConstants.QUESTION + ApiConstants.PARAM_2)
    public ResponseEntity<?> selectAllQuestionnaireByQuestion(
            @PathVariable String question) {
        return response.returnResponse(service
                .selectAllQuestionnaireByQuestion(question));
    }

    @GetMapping(value = ApiConstants.FULLNAME + ApiConstants.PARAM_3)
    public ResponseEntity<?> selectAllQuestionnaireByFullName(
            @PathVariable String fullName) {
        return response.returnResponse(service.
                selectAllQuestionnaireByFullName(fullName));
    }

    @GetMapping(value = ApiConstants.APARTMENT + ApiConstants.PARAM_4)
    public ResponseEntity<?> selectAllQuestionnaireByApartment(
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllQuestionnaireByApartment(apartment));
    }

    @GetMapping(value = ApiConstants.DATE_DISPATCH + ApiConstants.PARAM_5)
    public ResponseEntity<?> selectAllQuestionnaireByDateDispatch(
            @PathVariable LocalDate dateDispatch) {
        return response.returnResponse(service
                .selectAllQuestionnaireByDateDispatch(dateDispatch));
    }

    @GetMapping(value = ApiConstants.DATE_RECEIVING + ApiConstants.PARAM_6)
    public ResponseEntity<?> selectAllQuestionnaireByDateReceiving(
            @PathVariable LocalDate dateReceiving) {
        return response.returnResponse(service
                .selectAllQuestionnaireByDateReceiving(dateReceiving));
    }

    // -------------- two -------------------
    @GetMapping(value = ApiConstants.FULLNAME + ApiConstants.APARTMENT + ApiConstants.PARAM_3 + ApiConstants.PARAM_4)
    public ResponseEntity<?> selectAllQuestionnaireByFullNameAndApartment(
            @PathVariable String fullName,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllQuestionnaireByFullNameAndApartment(fullName, apartment));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.APARTMENT + ApiConstants.PARAM_1 + ApiConstants.PARAM_4)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndApartment(
            @PathVariable String title,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndApartment(title, apartment));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.FULLNAME + ApiConstants.PARAM_1 + ApiConstants.PARAM_3)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndFullname(
            @PathVariable String title,
            @PathVariable String fullName) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndFullName(title, fullName));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.DATE_DISPATCH + ApiConstants.PARAM_1 + ApiConstants.PARAM_5)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndDateDispatch(
            @PathVariable String title,
            @PathVariable LocalDate dateDispatch) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndDateDispatch(title, dateDispatch));
    }

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.DATE_RECEIVING + ApiConstants.PARAM_1 + ApiConstants.PARAM_6)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndDateReceiving(
            @PathVariable String title,
            @PathVariable LocalDate dateReceiving) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndDateReceiving(title, dateReceiving));
    }
    @GetMapping(value = ApiConstants.TITLE + ApiConstants.QUESTION + ApiConstants.PARAM_1 + ApiConstants.PARAM_2)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndQuestion(
            @PathVariable String title,
            @PathVariable String question) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndQuestion(title, question));
    }


    // -------------- three -------------------

    @GetMapping(value = ApiConstants.TITLE + ApiConstants.FULLNAME +
            ApiConstants.APARTMENT + ApiConstants.PARAM_1 + ApiConstants.PARAM_3 + ApiConstants.PARAM_4)
    public ResponseEntity<?> selectAllQuestionnaireByTitleAndFullNameAndApartment(
            @PathVariable String title,
            @PathVariable String fullName,
            @PathVariable String apartment) {
        return response.returnResponse(service
                .selectAllQuestionnaireByTitleAndFullNameAndApartment(title, fullName, apartment));
    }


    // ------------------------------------------------------------------------------



}