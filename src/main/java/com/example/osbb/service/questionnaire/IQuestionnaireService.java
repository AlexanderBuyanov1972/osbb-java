package com.example.osbb.service.questionnaire;

import com.example.osbb.dto.ResultQuestionnaire;
import com.example.osbb.entity.polls.Questionnaire;

import java.util.List;

public interface IQuestionnaireService {

    // results ---------------------------
    public Object getResultOfQuestionnaireByTitle(String title);
    public ResultQuestionnaire getResultPoolByTitle(String title);

    // all --------------------------------

    public Object createAllQuestionnaire(List<Questionnaire> questionnaires);

    public Object updateAllQuestionnaire(List<Questionnaire> questionnaires);

    public Object getAllQuestionnaire();

    public Object deleteAllQuestionnaireByTitle(String title);

    public Object deleteAllQuestionnaireByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId);


}
