package com.example.osbb.controller;

import com.example.osbb.entity.Questionnaire;
import com.example.osbb.service.questionnaire.IQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.QUESTIONNAIRE)
public class QuestionnaireController {

    @Autowired
    private IQuestionnaireService service;

    @Autowired
    private HelpMethodsForController response;
    // -------------- one ----------------

    @PostMapping
    public ResponseEntity<?> createQuestionnaire(@RequestBody Questionnaire questionnaire) {
        return response.returnResponse(service.createQuestionnaire(questionnaire));
    }

    @PutMapping
    public ResponseEntity<?> updateQuestionnaire(@RequestBody Questionnaire questionnaire) {
        return response.returnResponse(service.updateQuestionnaire(questionnaire));
    }

    @GetMapping(ApiConstants.ID)
    public ResponseEntity<?> getQuestionnaire(@PathVariable Long id) {
        return response.returnResponse(service.getQuestionnaire(id));
    }

    @DeleteMapping(ApiConstants.ID)
    public ResponseEntity<?> deleteQuestionnaire(@PathVariable Long id) {
        return response.returnResponse(service.deleteQuestionnaire(id));
    }

    // ------------------- all ---------------------------------

    @PostMapping(ApiConstants.ALL)
    public ResponseEntity<?> createAllQuestionnaire(@RequestBody List<Questionnaire> list) {
        return response.returnResponse(service.createAllQuestionnaire(list));
    }

    @PutMapping(ApiConstants.ALL)
    public ResponseEntity<?> updateQuestionnaire(@RequestBody List<Questionnaire> list) {
        return response.returnResponse(service.updateAllQuestionnaire(list));
    }

    @GetMapping(ApiConstants.ALL)
    public ResponseEntity<?> getAllQuestionnaire() {
        return response.returnResponse(service.getAllQuestionnaire());
    }

    @DeleteMapping(ApiConstants.ALL)
    public ResponseEntity<?> deleteAllQuestionnaire() {
        return response.returnResponse(service.deleteAllQuestionnaire());
    }

    // ------------------ results ----------------

    @GetMapping(ApiConstants.RESULT + ApiConstants.TITLE)
    public ResponseEntity<?> getResultOfQuestionnaireByTitle(@PathVariable String title) {
        return response.returnResponse(service.getResultOfQuestionnaireByTitle(title));
    }
}
