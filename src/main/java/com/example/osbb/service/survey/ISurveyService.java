package com.example.osbb.service.survey;

import com.example.osbb.dto.ResultSurvey;
import com.example.osbb.entity.Survey;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISurveyService {

    // all --------------------------------

    ResponseEntity<?> createAllSurvey(List<Survey> surveys);

    ResponseEntity<?> updateAllSurvey(List<Survey> surveys);

    ResponseEntity<?> getAllSurvey();

    // results ---------------------------
    ResponseEntity<?> getResultSurveyByTitle(String title);

    // этого метода нет в контроллере --------------
    public ResultSurvey getResultSurveyByTitleForPrint(String title);

    // удаление всех опросов по ID собственника и ID недвижимости
    ResponseEntity<?> deleteAllSurveyByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId);

    // удаление всех опросов по теме опроса
    ResponseEntity<?> deleteAllSurveyByTitle(String title);


}
