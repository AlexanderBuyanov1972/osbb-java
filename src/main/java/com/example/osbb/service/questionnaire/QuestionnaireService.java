package com.example.osbb.service.questionnaire;

import com.example.osbb.dao.*;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.ownership.OwnershipDAO;
import com.example.osbb.dao.QuestionnaireDAO;
import com.example.osbb.dto.ResultQuestionnaire;
import com.example.osbb.dto.ShareTotalAreaQuestionAnswer;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Questionnaire;
import com.example.osbb.enums.TypeOfAnswer;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionnaireService implements IQuestionnaireService {
    @Autowired
    QuestionnaireDAO questionnaireDAO;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    ShareDAO shareDAO;
    @Autowired
    OwnerDAO ownerDAO;
    @Autowired
    OwnershipDAO ownershipDAO;

    //  result --------------------------------
    @Override
    public Object getResultOfQuestionnaireByTitle(String title) {
        try {
            return Response.builder()
                    .data(getResultPoolByTitle(title))
                    .messages(List.of(
                            "Результаты опроса " + title + " обработаны"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    @Override
    public ResultQuestionnaire getResultPoolByTitle(String title) {
        List<Questionnaire> baseList = questionnaireDAO.findByTitle(title)
                .stream()
                .filter(x -> x.getDateReceiving() != null)
                .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                .toList();
        Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner = createMapVotedOwner(baseList);
        Map<String, Map<TypeOfAnswer, Double>> mapVotedArea = createMapVotedArea(baseList);
        String question = new ArrayList<>(mapVotedArea.keySet()).get(0);
        Double areaVoted = resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.BEHIND) +
                resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.AGAINST) +
                resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.ABSTAINED);
        Long ownerVoted = resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.BEHIND) +
                resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.AGAINST) +
                resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.ABSTAINED);
        Long countOwner = ownerDAO.count();
        Double summaArea = ownershipDAO.findAll().stream()
                .mapToDouble(Ownership::getTotalArea)
                .sum();
        List<TypeOfAnswer> list = List.of(TypeOfAnswer.BEHIND, TypeOfAnswer.AGAINST, TypeOfAnswer.ABSTAINED);
        fillMapVotedOwnerFromNull(list, mapVotedOwner);
        fillMapVotedAreaFromNull(list, mapVotedArea);
        return ResultQuestionnaire
                .builder()
                .processingPercentageOwner(formatDoubleValue(((double) ownerVoted * 100 / (double) countOwner)))
                .processingPercentageArea(formatDoubleValue(areaVoted * 100 / summaArea))
                .ownerVoted(ownerVoted)
                .countOwner(countOwner)
                .areaVoted(formatDoubleValue(areaVoted))
                .summaArea(formatDoubleValue(summaArea))
                .mapVotedOwner(mapVotedOwner)
                .mapVotedArea(mapVotedArea)
                .build();
    }

    private void fillMapVotedOwnerFromNull(List<TypeOfAnswer> list, Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner) {
        for (String key : mapVotedOwner.keySet()) {
            Map<TypeOfAnswer, Long> map = mapVotedOwner.get(key);
            for (TypeOfAnswer one : list) {
                if (map.get(one) == null) {
                    map.put(one, 0L);
                    mapVotedOwner.put(key, map);
                }
            }
        }
    }

    private void fillMapVotedAreaFromNull(List<TypeOfAnswer> list, Map<String, Map<TypeOfAnswer, Double>> mapVotedArea) {
        for (String key : mapVotedArea.keySet()) {
            Map<TypeOfAnswer, Double> map = mapVotedArea.get(key);
            for (TypeOfAnswer one : list) {
                if (map.get(one) == null) {
                    map.put(one, 0.00);
                    mapVotedArea.put(key, map);
                }
            }
        }
    }

    private Map<String, Map<TypeOfAnswer, Long>> createMapVotedOwner(List<Questionnaire> baseList) {
        return baseList.stream()
                .collect(
                        Collectors.groupingBy(Questionnaire::getQuestion,
                                Collectors.groupingBy(Questionnaire::getAnswer,
                                        Collectors.counting())));
    }

    private Map<String, Map<TypeOfAnswer, Double>> createMapVotedArea(List<Questionnaire> baseList) {
        return baseList
                .stream()
                .map(this::computeShare)
                .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
                        Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getAnswer,
                                Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea))));
    }

    private Double resultAnswerDouble(Map<String, Map<TypeOfAnswer, Double>> map, String key, TypeOfAnswer answer) {
        return map.get(key).get(answer) == null ? 0.00 : map.get(key).get(answer);
    }

    private Long resultAnswerLong(Map<String, Map<TypeOfAnswer, Long>> map, String key, TypeOfAnswer answer) {
        return map.get(key).get(answer) == null ? 0L : map.get(key).get(answer);
    }


    private ShareTotalAreaQuestionAnswer computeShare(Questionnaire q) {
        return shareDAO.findAll().stream()
                .filter(Objects::nonNull)
                .filter(s -> s.getOwnership().getAddress().getApartment().equals(q.getApartment()))
                .filter(s -> mapOwnerToFullName(s.getOwner()).equals(q.getFullName()))
                .map(s -> new ShareTotalAreaQuestionAnswer(
                        q.getQuestion(),
                        q.getAnswer(),
                        s.getValue() * s.getOwnership().getTotalArea())).findFirst().orElse(
                        new ShareTotalAreaQuestionAnswer());
    }

    // all -------------------------------

    @Override
    @Transactional
    public Object createAllQuestionnaire(List<Questionnaire> questionnaires) {
        long count = questionnaireDAO.findByTitle(questionnaires.get(0).getTitle()).size();
        if (count != 0)
            return Response
                    .builder()
                    .data(count)
                    .messages(List.of(
                            "Создать опрос с данной темой не представляется возможным",
                            "Такой опрос был создан ранее",
                            "Измените наименование темы "
                    ))
                    .build();


        try {
            recordDAO.findAll()
                    .forEach(el -> {
                        questionnaires.forEach(s -> {
                            questionnaireDAO.save(
                                    new Questionnaire(
                                            s,
                                            mapOwnerToFullName(el.getOwner()),
                                            el.getOwnership().getAddress().getApartment()));
                        });
                    });
            int size = questionnaires.size();
            return Response
                    .builder()
                    .data(size)
                    .messages(List.of("Создано " + size + " вопросов.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllQuestionnaire(List<Questionnaire> questionnaires) {
        try {
            for (Questionnaire one : questionnaires) {
                if (questionnaireDAO.existsById(one.getId())) {
                    // устанавливаем дату прохождения опроса, опрос пройден
                    one.setDateReceiving(LocalDate.now());
                    questionnaireDAO.save(one);
                }
            }
            //------------ start ------------------
            for (Questionnaire two : questionnaires) {
                List<Questionnaire> list = questionnaireDAO
                        .findByTitleAndFullNameAndDateReceiving(two.getTitle(), two.getFullName(), null);
                if (!list.isEmpty()) {
                    list.forEach(el -> {
                        if (el.getQuestion().equals(two.getQuestion())) {
                            el.setAnswer(two.getAnswer());
                            el.setDateReceiving(LocalDate.now());
                            questionnaireDAO.save(el);
                        }
                    });
                }
            }
            //------------ finish -----------------

            return Response.builder()
                    .messages(List.of(ServiceMessages.OK))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllQuestionnaire() {
        try {
            List<Questionnaire> list = questionnaireDAO.findAll()
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(
                            ServiceMessages.OK,
                            "Получено " + list.size() + " записей"
                    ))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllQuestionnaireByTitle(String title) {
        try {
            questionnaireDAO.deleteByTitle(title);
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object deleteAllQuestionnaireByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        try {
            String fullName = mapOwnerToFullName(ownerDAO.findById(ownerId).get());
            String apartment = ownershipDAO.findById(ownershipId).get().getAddress().getApartment();
            questionnaireDAO.deleteByFullNameAndApartment(fullName, apartment);
            List<String> list = questionnaireDAO.findAll()
                    .stream()
                    .map(Questionnaire::getTitle)
                    .distinct()
                    .toList();


            return Response.builder().data(list).messages(List.of(ServiceMessages.OK)).build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // other ------------------------------

    private String mapOwnerToFullName(Owner o) {
        return o.getLastName() + " " + o.getFirstName() + " " + o.getSecondName();
    }

    //Округление дробной части до 2-х запятых
    private Double formatDoubleValue(Double var) {
        return Math.rint(100.0 * var) / 100.0;
    }
}
