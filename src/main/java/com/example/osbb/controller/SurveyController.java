package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiConstants;
import com.example.osbb.entity.Survey;
import com.example.osbb.service.survey.ISurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiConstants.SURVEYS)
public class SurveyController {
    @Autowired
    private ISurveyService service;
    @Autowired
    private HelpMethodsForController response;
    // all surveys --------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createAllSurvey(@RequestBody List<Survey> list) {
        return response.returnResponse(service.createAllSurvey(list));
    }

    @PutMapping
    public ResponseEntity<?> updateSurvey(@RequestBody List<Survey> list) {
        return response.returnResponse(service.updateAllSurvey(list));
    }

    @GetMapping
    public ResponseEntity<?> getAllSurvey() {
        return response.returnResponse(service.getAllSurvey());
    }

    @DeleteMapping(value = ApiConstants.PARAM_TITLE)
    public ResponseEntity<?> deleteAllSurvey(@PathVariable String title) {
        return response.returnResponse(service.deleteAllSurveyByTitle(title));
    }

    // delete all by owner id and ownership id ----------------------------------
    @DeleteMapping(ApiConstants.PARAM_OWNER_ID + ApiConstants.PARAM_OWNERSHIP_ID)
    public ResponseEntity<?> deleteAllSurveyByOwnerIdAndOwnershipId(@PathVariable Long ownerId,
                                                                    @PathVariable Long ownershipId) {
        return response.returnResponse(service.deleteAllSurveyByOwnerIdAndOwnershipId(ownerId, ownershipId));
    }

    // results -------------------------------------------------------------------
    @GetMapping(ApiConstants.RESULT + ApiConstants.PARAM_TITLE)
    public ResponseEntity<?> getResultSurveyByTitle(@PathVariable String title) {
        return response.returnResponse(service.getResultSurveyByTitle(title));
    }


}
