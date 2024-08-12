package com.example.osbb.controller;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.entity.Survey;
import com.example.osbb.service.survey.ISurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ApiPaths.SURVEYS)
public class SurveyController {

    @Autowired
    private ISurveyService service;

    // all surveys --------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createAllSurvey(@RequestBody List<Survey> list) {
        return service.createAllSurvey(list);
    }

    @PutMapping
    public ResponseEntity<?> updateSurvey(@RequestBody List<Survey> list) {
        return service.updateAllSurvey(list);
    }

    @GetMapping
    public ResponseEntity<?> getAllSurvey() {
        return service.getAllSurvey();
    }

    @DeleteMapping(value = ApiPaths.PARAM_TITLE)
    public ResponseEntity<?> deleteAllSurvey(@PathVariable String title) {
        return service.deleteAllSurveyByTitle(title);
    }

    // delete all by owner id and ownership id ----------------------------------
    @DeleteMapping(ApiPaths.PARAM_OWNER_ID + ApiPaths.PARAM_OWNERSHIP_ID)
    public ResponseEntity<?> deleteAllSurveyByOwnerIdAndOwnershipId(@PathVariable Long ownerId,
                                                                    @PathVariable Long ownershipId) {
        return service.deleteAllSurveyByOwnerIdAndOwnershipId(ownerId, ownershipId);
    }

    // results -------------------------------------------------------------------
    @GetMapping(ApiPaths.RESULT + ApiPaths.PARAM_TITLE)
    public ResponseEntity<?> getResultSurveyByTitle(@PathVariable String title) {
        return service.getResultSurveyByTitle(title);
    }


}
