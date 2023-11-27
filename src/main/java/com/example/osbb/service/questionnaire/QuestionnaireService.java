package com.example.osbb.service.questionnaire;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.*;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
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
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionnaireService implements IQuestionnaireService {
    private static final Logger log = Logger.getLogger(QuestionnaireService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    QuestionnaireDAO questionnaireDAO;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OwnerDAO ownerDAO;
    @Autowired
    OwnershipDAO ownershipDAO;

    //  result --------------------------------
    @Override
    public Object getResultOfQuestionnaireByTitle(String title) {
        String methodName = "getResultOfQuestionnaireByTitle";
        String messageSuccessfully = "Результаты опроса " + title + " обработаны";

        log.info(messageEnter(methodName));
        try {
            ResultQuestionnaire rq = getResultPoolByTitle(title);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response.builder()
                    .data(rq)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public ResultQuestionnaire getResultPoolByTitle(String title) {
        String methodName = "getResultPoolByTitle";
        String messageSuccessfully = "Результаты опроса подготовлены успешно";

        try {
            log.info(messageEnter(methodName));

            List<Questionnaire> baseList = questionnaireDAO.findByTitle(title)
                    .stream()
                    .filter(x -> x.getDateReceiving() != null)
                    .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                    .toList();
            Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner = createMapVotedOwner(baseList);
            Map<String, Map<TypeOfAnswer, Double>> mapVotedArea = createMapVotedArea(baseList);
            String question = new ArrayList<>(mapVotedArea.keySet()).get(0);
            double areaVoted = resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.BEHIND) +
                    resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.AGAINST) +
                    resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.ABSTAINED);
            Long ownerVoted = resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.BEHIND) +
                    resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.AGAINST) +
                    resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.ABSTAINED);
            Long countOwner = ownerDAO.count();
            double summaArea = ownershipDAO.findAll().stream()
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            List<TypeOfAnswer> list = List.of(TypeOfAnswer.BEHIND, TypeOfAnswer.AGAINST, TypeOfAnswer.ABSTAINED);
            fillMapVotedOwnerFromNull(list, mapVotedOwner);
            fillMapVotedAreaFromNull(list, mapVotedArea);
            ResultQuestionnaire rq = ResultQuestionnaire
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
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return rq;
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    private void fillMapVotedOwnerFromNull(List<TypeOfAnswer> list, Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner) {
        try {
            for (String key : mapVotedOwner.keySet()) {
                Map<TypeOfAnswer, Long> map = mapVotedOwner.get(key);
                for (TypeOfAnswer one : list) {
                    if (map.get(one) == null) {
                        map.put(one, 0L);
                        mapVotedOwner.put(key, map);
                    }
                }
            }
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }

    }

    private void fillMapVotedAreaFromNull(List<TypeOfAnswer> list, Map<String, Map<TypeOfAnswer, Double>> mapVotedArea) {
        try {
            for (String key : mapVotedArea.keySet()) {
                Map<TypeOfAnswer, Double> map = mapVotedArea.get(key);
                for (TypeOfAnswer one : list) {
                    if (map.get(one) == null) {
                        map.put(one, 0.00);
                        mapVotedArea.put(key, map);
                    }
                }
            }
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private Map<String, Map<TypeOfAnswer, Long>> createMapVotedOwner(List<Questionnaire> baseList) {
        try {
            return baseList.stream()
                    .collect(
                            Collectors.groupingBy(Questionnaire::getQuestion,
                                    Collectors.groupingBy(Questionnaire::getAnswer,
                                            Collectors.counting())));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private Map<String, Map<TypeOfAnswer, Double>> createMapVotedArea(List<Questionnaire> baseList) {
        try {
            return baseList
                    .stream()
                    .map(this::computeShare)
                    .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
                            Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getAnswer,
                                    Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea))));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private Double resultAnswerDouble(Map<String, Map<TypeOfAnswer, Double>> map, String key, TypeOfAnswer answer) {
        try {
            return map.get(key).get(answer) == null ? 0.00 : map.get(key).get(answer);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private Long resultAnswerLong(Map<String, Map<TypeOfAnswer, Long>> map, String key, TypeOfAnswer answer) {
        try {
            return map.get(key).get(answer) == null ? 0L : map.get(key).get(answer);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }


    private ShareTotalAreaQuestionAnswer computeShare(Questionnaire q) {
        try {
            return recordDAO.findAll().stream()
                    .filter(Objects::nonNull)
                    .filter(s -> s.getOwnership().getAddress().getApartment().equals(q.getApartment()))
                    .filter(s -> mapOwnerToFullName(s.getOwner()).equals(q.getFullName()))
                    .map(s -> new ShareTotalAreaQuestionAnswer(
                            q.getQuestion(),
                            q.getAnswer(),
                            s.getShare() * s.getOwnership().getTotalArea())).findFirst().orElse(
                            new ShareTotalAreaQuestionAnswer());
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // all -------------------------------

    @Override
    @Transactional
    public Object createAllQuestionnaire(List<Questionnaire> questionnaires) {
        String methodName = "createAllQuestionnaire";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        long count = questionnaireDAO.findByTitle(questionnaires.get(0).getTitle()).size();
        List<String> messages = List.of(
                "Создать опрос с данной темой не представляется возможным",
                "Такой опрос был создан ранее",
                "Измените наименование темы "
        );
        if (count != 0) {
            messages.forEach(log::info);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(count)
                    .messages(messages)
                    .build();
        }
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
            messageSuccessfully = "Создано " + size + " вопросов по теме \""
                    + questionnaires.get(0).getTitle() + "\"";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(size)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllQuestionnaire(List<Questionnaire> questionnaires) {
        String methodName = "updateAllQuestionnaire";
        String messageSuccessfully = "Операция выполнена успешно";

        log.info(messageEnter(methodName));
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
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response.builder()
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllQuestionnaire() {
        String methodName = "getAllQuestionnaire";
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        try {
            List<Questionnaire> list = questionnaireDAO.findAll()
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                    .toList();
            messageSuccessfully = "Получено " + list.size() + " записей";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllQuestionnaireByTitle(String title) {
        String methodName = "deleteAllQuestionnaireByTitle";
        String messageSuccessfully = "Все вопросы по теме \"" + title + "\" удалены успешно ";

        log.info(messageEnter(methodName));
        try {
            questionnaireDAO.deleteByTitle(title);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new ResponseMessages(List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object deleteAllQuestionnaireByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        String methodName = "deleteAllQuestionnaireByOwnerIdAndOwnershipId";
        String messageSuccessfully = "Оперция завершена успешно";
        String messageOwnerNotExists = "Собственник по ID : " + ownerId + " не найден";
        String messageOwnershipNotExists = "Объект собственности по ID : " + ownershipId + " не найден";

        log.info(messageEnter(methodName));
        try {
            Owner owner = ownerDAO.findById(ownerId).orElse(null);
            if (owner == null) {
                log.info(messageOwnerNotExists);
                return new ResponseMessages(List.of(messageOwnerNotExists));
            }
            String fullName = mapOwnerToFullName(owner);
            Ownership ownership = ownershipDAO.findById(ownershipId).orElse(null);
            if (ownership == null) {
                log.info(messageOwnershipNotExists);
                return new ResponseMessages(List.of(messageOwnershipNotExists));
            }
            String apartment = ownership.getAddress().getApartment();

            questionnaireDAO.deleteByFullNameAndApartment(fullName, apartment);

            List<String> list = questionnaireDAO.findAll()
                    .stream()
                    .map(Questionnaire::getTitle)
                    .distinct()
                    .toList();
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageSuccessfully))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
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

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }
}
