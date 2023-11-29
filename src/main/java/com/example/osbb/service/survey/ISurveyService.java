package com.example.osbb.service.survey;

import com.example.osbb.dto.ResultSurvey;
import com.example.osbb.entity.Survey;

import java.util.List;

public interface ISurveyService {

    // all --------------------------------

    public Object createAllSurvey(List<Survey> surveys);

    public Object updateAllSurvey(List<Survey> surveys);

    public Object getAllSurvey();

    // results ---------------------------
    public Object getResultSurveyByTitle(String title);

    // этого метода нет в контроллере --------------
    public ResultSurvey getResultSurveyByTitleForPrint(String title);

    // удаление всех опросов по ID собственника и ID недвижимости
    public Object deleteAllSurveyByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId);

    // удаление всех опросов по теме опроса
    public Object deleteAllSurveyByTitle(String title);


}
