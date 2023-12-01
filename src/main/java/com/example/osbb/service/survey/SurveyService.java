package com.example.osbb.service.survey;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.*;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.SurveyDAO;
import com.example.osbb.dto.ResultSurvey;
import com.example.osbb.dto.ShareTotalAreaQuestionAnswer;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Survey;
import com.example.osbb.enums.TypeOfAnswer;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveyService implements ISurveyService {
    private static final Logger log = Logger.getLogger(SurveyService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;
    @Autowired
    SurveyDAO surveyDAO;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    OwnerDAO ownerDAO;
    @Autowired
    OwnershipDAO ownershipDAO;

    //  result --------------------------------
    @Override
    public Object getResultSurveyByTitle(String title) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageSuccessfully = "Результаты опроса по теме \"" + title + "\" обработаны";
        log.info(messageEnter(methodName));
        try {
            ResultSurvey rq = getResultSurveyByTitleForPrint(title);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new Response(rq, List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }


    public ResultSurvey getResultSurveyByTitleForPrint(String title) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageSuccessfully = "Результаты опроса подготовлены успешно";

        try {
            log.info(messageEnter(methodName));
            List<Survey> baseList = surveyDAO.findByTitle(title)
                    .stream()
                    .filter(x -> x.getDateReceiving() != null)
                    .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                    .toList();
            log.info("Подготовлен базовый лист в размере : " + baseList.size());

            Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner = createMapVotedOwner(baseList);
            log.info("Подготовлен карта голосования личным участием в размере : " + mapVotedOwner.size());

            Map<String, Map<TypeOfAnswer, Double>> mapVotedArea = createMapVotedArea(baseList);
            log.info("Подготовлен карта голосования квадратными метрами в размере : " + mapVotedArea.size());

            String question = new ArrayList<>(mapVotedArea.keySet()).get(0);
            log.info("Вопрос String question = new ArrayList<>(mapVotedArea.keySet()).get(0); ---> " + question);

            double areaVoted = resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.BEHIND) +
                    resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.AGAINST) +
                    resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.ABSTAINED);
            log.info("Квадратные метры принявшие участие в голосовании : " + areaVoted);

            double summaArea = ownershipDAO.findAll().stream()
                    .mapToDouble(Ownership::getTotalArea)
                    .sum();
            log.info("Общая площадь помещений в доме : " + summaArea);

            Long ownerVoted = resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.BEHIND) +
                    resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.AGAINST) +
                    resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.ABSTAINED);
            log.info("Личные голоса принявшие участие в голосовании : " + ownerVoted);

            Long countOwner = ownerDAO.count();
            log.info("Всего собственников дома : " + countOwner);

            List<TypeOfAnswer> list = List.of(TypeOfAnswer.BEHIND, TypeOfAnswer.AGAINST, TypeOfAnswer.ABSTAINED);
            fillMapVotedOwnerFromNull(list, mapVotedOwner);
            log.info("Заполнена карта голосования личным участием успешно");
            fillMapVotedAreaFromNull(list, mapVotedArea);
            log.info("Заполнена карта голосования квадратными метрами успешно");
            ResultSurvey rq = ResultSurvey
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

    private Map<String, Map<TypeOfAnswer, Long>> createMapVotedOwner(List<Survey> baseList) {
        try {
            return baseList.stream()
                    .collect(
                            Collectors.groupingBy(Survey::getQuestion,
                                    Collectors.groupingBy(Survey::getAnswer,
                                            Collectors.counting())));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    private Map<String, Map<TypeOfAnswer, Double>> createMapVotedArea(List<Survey> baseList) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));

        try {
            Map<String, Map<TypeOfAnswer, Double>> map = baseList
                    .stream()
                    .map(this::computeShare)
                    .peek(log::info)
                    .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
                            Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getAnswer,
                                    Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea))));
            log.info("Карта голосования квадратными метрами подготовлена успешно в размере : " + map.size());
            log.info(messageExit(methodName));
            return map;
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


    private ShareTotalAreaQuestionAnswer computeShare(Survey q) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        log.info("Входящий опрос : " + q);
        try {
            return recordDAO.findAll().stream()
                    .filter(Objects::nonNull)
                    .filter(s -> s.getOwnership().getAddress().getApartment().equals(q.getApartment()))
                    .filter(s -> mapOwnerToFullName(s.getOwner()).equals(q.getFullName()))
                    .map(s -> new ShareTotalAreaQuestionAnswer(
                            q.getQuestion(),
                            q.getAnswer(),
                            s.getShare() * s.getOwnership().getTotalArea())).findFirst().orElse(null);
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

    // all -------------------------------

    @Override
    @Transactional
    public Object createAllSurvey(List<Survey> surveys) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageSuccessfully = "";

        log.info(messageEnter(methodName));
        long count = surveyDAO.findByTitle(surveys.get(0).getTitle()).size();
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
                    .messages(messages)
                    .build();
        }
        try {
            recordDAO.findAll()
                    .forEach(el -> {
                        surveys.forEach(s -> {
                            surveyDAO.save(
                                    new Survey(
                                            s,
                                            mapOwnerToFullName(el.getOwner()),
                                            el.getOwnership().getAddress().getApartment()));
                        });
                    });
            int size = surveys.size();
            messageSuccessfully = "Создано " + size + " вопросов по теме \""
                    + surveys.get(0).getTitle() + "\"";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new Response(size, List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllSurvey(List<Survey> surveys) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageSuccessfully = "Операция выполнена успешно";

        log.info(messageEnter(methodName));
        try {
            for (Survey one : surveys) {
                if (surveyDAO.existsById(one.getId())) {
                    // устанавливаем дату прохождения опроса, опрос пройден
                    one.setDateReceiving(LocalDate.now());
                    surveyDAO.save(one);
                }
            }
            //------------ start ---- использовать ТОЛЬКО при заполненой базе --------------
//            for (Survey two : surveys) {
//                List<Survey> list = surveyDAO
//                        .findByTitleAndFullNameAndDateReceiving(two.getTitle(), two.getFullName(), null);
//                if (!list.isEmpty()) {
//                    list.forEach(el -> {
//                        if (el.getQuestion().equals(two.getQuestion())) {
//                            el.setAnswer(two.getAnswer());
//                            el.setDateReceiving(LocalDate.now());
//                            surveyDAO.save(el);
//                        }
//                    });
//                }
//            }
            //------------ finish -----------------
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new Response(List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object getAllSurvey() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findAll().stream().filter(el -> el.getDateReceiving() == null)
                    .sorted(comparatorSurveyByApartment()).toList();
            String messageSuccessfully = "Получено " + list.size() + " записей";
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new Response(list, List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteAllSurveyByTitle(String title) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageSuccessfully = "Все вопросы по теме \"" + title + "\" удалены успешно ";
        log.info(messageEnter(methodName));
        try {
            surveyDAO.deleteByTitle(title);
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new Response(List.of(messageSuccessfully));
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object deleteAllSurveyByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String messageSuccessfully = "Оперция завершена успешно";
        String messageOwnerNotExists = "Собственник по ID : " + ownerId + " не найден";
        String messageOwnershipNotExists = "Объект собственности по ID : " + ownershipId + " не найден";

        log.info(messageEnter(methodName));
        try {
            Owner owner = ownerDAO.findById(ownerId).orElse(null);
            if (owner == null) {
                log.info(messageOwnerNotExists);
                return new Response(List.of(messageOwnerNotExists));
            }
            String fullName = mapOwnerToFullName(owner);
            Ownership ownership = ownershipDAO.findById(ownershipId).orElse(null);
            if (ownership == null) {
                log.info(messageOwnershipNotExists);
                return new Response(List.of(messageOwnershipNotExists));
            }
            String apartment = ownership.getAddress().getApartment();

            surveyDAO.deleteByFullNameAndApartment(fullName, apartment);

            List<String> list = surveyDAO.findAll()
                    .stream()
                    .map(Survey::getTitle)
                    .distinct()
                    .toList();
            log.info(messageSuccessfully);
            log.info(messageExit(methodName));
            return new Response(list, List.of(messageSuccessfully));
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

    private Comparator<Survey> comparatorSurveyByApartment() {
        return (a, b) -> Integer.parseInt(a.getApartment())
                - Integer.parseInt(b.getApartment());
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
