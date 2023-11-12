package com.example.osbb;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.controller.HelpMethodsForController;
import com.example.osbb.entity.Questionnaire;
import com.example.osbb.service.questionnaire.IQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.QUESTIONNAIRES)
public class QuestionnaireController {

    @Autowired
    private IQuestionnaireService service;

    @Autowired
    private HelpMethodsForController response;

    // ------------------- all ---------------------------------

    @PostMapping
    public ResponseEntity<?> createAllQuestionnaire(@RequestBody List<Questionnaire> list) {
        return response.returnResponse(service.createAllQuestionnaire(list));
    }

    @PutMapping
    public ResponseEntity<?> updateQuestionnaire(@RequestBody List<Questionnaire> list) {
        return response.returnResponse(service.updateAllQuestionnaire(list));
    }

    @GetMapping
    public ResponseEntity<?> getAllQuestionnaire() {
        return response.returnResponse(service.getAllQuestionnaire());
    }

    @DeleteMapping(value = ApiConstants.PARAM_TITLE)
    public ResponseEntity<?> deleteAllQuestionnaire(@PathVariable String title) {
        return response.returnResponse(service.deleteAllQuestionnaireByTitle(title));
    }

    // delete all by owner id and ownership id ----------------------------------
    @DeleteMapping(ApiConstants.PARAM_OWNER_ID + ApiConstants.PARAM_OWNERSHIP_ID)
    public ResponseEntity<?> deleteAllQuestionnaireByOwnerIdAndOwnershipId(
            @PathVariable Long ownerId,
            @PathVariable Long ownershipId
    ) {
        return response.returnResponse(service.deleteAllQuestionnaireByOwnerIdAndOwnershipId(ownerId, ownershipId));
    }

    // results -------------------------------------------------------------------

    @GetMapping(ApiConstants.RESULT + ApiConstants.PARAM_TITLE)
    public ResponseEntity<?> getResultOfQuestionnaireByTitle(@PathVariable String title) {
        return response.returnResponse(service.getResultOfQuestionnaireByTitle(title));
    }


}
