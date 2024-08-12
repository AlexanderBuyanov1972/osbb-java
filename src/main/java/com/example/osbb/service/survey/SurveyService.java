package com.example.osbb.service.survey;

import com.example.osbb.dao.*;
import com.example.osbb.dao.owner.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.SurveyDAO;
import com.example.osbb.dto.ResultSurvey;
import com.example.osbb.dto.ShareTotalAreaQuestionAnswer;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import com.example.osbb.entity.Survey;
import com.example.osbb.enums.TypeOfAnswer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SurveyService implements ISurveyService {

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
    public ResponseEntity<?> getResultSurveyByTitle(String title) {
        String message = "Результаты опроса по теме \"" + title + "\" обработаны";
        List<String> list = List.of("Подготовлена карта для голосования имеет размер НОЛЬ",
                "Подсчёт результатов голосования невозможен.",
                "Нужен хотя бы один проголосовавший.");
        ResultSurvey rs = getResultSurveyByTitleForPrint(title);
        if (rs == null) {
            list.forEach(log::info);
            return ResponseEntity.badRequest().body(new Response(list));
        }
        log.info(message);
        return ResponseEntity.ok(new Response(rs, List.of(message)));
    }


    public ResultSurvey getResultSurveyByTitleForPrint(String title) {
        List<Survey> baseList = surveyDAO.findByTitle(title)
                .stream().filter(x -> x.getDateReceiving() != null)
                .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                .toList();
        log.info("Подготовлен базовый лист в размере : {}", baseList.size());

        Map<String, Map<TypeOfAnswer, Long>> mapVotedOwner = createMapVotedOwner(baseList);
        int size = mapVotedOwner.size();
        log.info("Подготовлена карта голосования личным участием в размере : {}", size);
        if (size == 0) {
            return null;
        }

        Map<String, Map<TypeOfAnswer, Double>> mapVotedArea = createMapVotedArea(baseList);
        size = mapVotedArea.size();
        log.info("Подготовлена карта голосования квадратными метрами в размере : {}", size);
        if (size == 0) {
            return null;
        }

        String question = new ArrayList<>(mapVotedArea.keySet()).get(0);
        log.info("Вопрос String question = new ArrayList<>(mapVotedArea.keySet()).get(0); ---> {}", question);

        double areaVoted = resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.BEHIND) +
                resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.AGAINST) +
                resultAnswerDouble(mapVotedArea, question, TypeOfAnswer.ABSTAINED);
        log.info("Квадратные метры принявшие участие в голосовании : {}", areaVoted);

        double summaArea = ownershipDAO.findAll().stream()
                .mapToDouble(Ownership::getTotalArea)
                .sum();
        log.info("Общая площадь помещений в доме : {}", summaArea);

        Long ownerVoted = resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.BEHIND) +
                resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.AGAINST) +
                resultAnswerLong(mapVotedOwner, question, TypeOfAnswer.ABSTAINED);
        log.info("Личные голоса принявшие участие в голосовании : {}", ownerVoted);

        Long countOwner = ownerDAO.count();
        log.info("Всего собственников дома : {}", countOwner);

        List<TypeOfAnswer> list = List.of(TypeOfAnswer.BEHIND, TypeOfAnswer.AGAINST, TypeOfAnswer.ABSTAINED);
        fillMapVotedOwnerFromNull(list, mapVotedOwner);
        log.info("Заполнена карта голосования личным участием успешно");
        fillMapVotedAreaFromNull(list, mapVotedArea);
        log.info("Заполнена карта голосования квадратными метрами успешно");
        ResultSurvey rs = new ResultSurvey(
                formatDoubleValue(((double) ownerVoted * 100 / (double) countOwner)),
                ownerVoted,
                countOwner,
                mapVotedOwner,
                formatDoubleValue(areaVoted * 100 / summaArea),
                formatDoubleValue(areaVoted),
                formatDoubleValue(summaArea),
                mapVotedArea);
        log.info("Результаты опроса подготовлены успешно");
        return rs;

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

    private Map<String, Map<TypeOfAnswer, Long>> createMapVotedOwner(List<Survey> baseList) {
        return baseList.stream()
                .collect(
                        Collectors.groupingBy(Survey::getQuestion,
                                Collectors.groupingBy(Survey::getAnswer,
                                        Collectors.counting())));
    }

    private Map<String, Map<TypeOfAnswer, Double>> createMapVotedArea(List<Survey> baseList) {
        Map<String, Map<TypeOfAnswer, Double>> map = baseList
                .stream()
                .map(this::computeShare)
                .peek(e -> log.info(e.toString()))
                .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
                        Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getAnswer,
                                Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea))));
        log.info("Карта голосования квадратными метрами подготовлена успешно в размере : {}", map.size());
        return map;
    }

    private Double resultAnswerDouble(Map<String, Map<TypeOfAnswer, Double>> map, String key, TypeOfAnswer answer) {
        return map.get(key).get(answer) == null ? 0.00 : map.get(key).get(answer);
    }

    private Long resultAnswerLong(Map<String, Map<TypeOfAnswer, Long>> map, String key, TypeOfAnswer answer) {
        return map.get(key).get(answer) == null ? 0L : map.get(key).get(answer);
    }

    private ShareTotalAreaQuestionAnswer computeShare(Survey q) {
        log.info("Входящий опрос : {}", q);
        return recordDAO.findAll().stream()
                .filter(Objects::nonNull)
                .filter(s -> s.getOwnership().getAddress().getApartment().equals(q.getApartment()))
                .filter(s -> mapOwnerToFullName(s.getOwner()).equals(q.getFullName()))
                .map(s -> new ShareTotalAreaQuestionAnswer(
                        q.getQuestion(),
                        q.getAnswer(),
                        s.getShare() * s.getOwnership().getTotalArea())).findFirst().orElse(null);
    }

// all -------------------------------

    @Override
    @Transactional
    public ResponseEntity<?> createAllSurvey(List<Survey> surveys) {
        long count = surveyDAO.findByTitle(surveys.get(0).getTitle()).size();
        List<String> messages = List.of(
                "Создать опрос с данной темой не представляется возможным",
                "Такой опрос был создан ранее",
                "Измените наименование темы "
        );
        if (count != 0) {
            messages.forEach(log::info);
            return ResponseEntity.badRequest().body(new Response(messages));
        }
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
        String message = "Создано " + size + " вопросов по теме \""
                + surveys.get(0).getTitle() + "\"";
        log.info(message);
        return ResponseEntity.ok(new Response(size, List.of(message)));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateAllSurvey(List<Survey> surveys) {
        String message = "Операция выполнена успешно";
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
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> getAllSurvey() {
        List<Survey> list = surveyDAO.findAll().stream().filter(el -> el.getDateReceiving() == null)
                .sorted(comparatorSurveyByApartment()).toList();
        String message = "Получено по всем опросам " + list.size() + " записей";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));

    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllSurveyByTitle(String title) {
        String message = "Все вопросы по теме \"" + title + "\" удалены успешно ";
        surveyDAO.deleteByTitle(title);
        log.info(message);
        return ResponseEntity.ok(new Response(List.of(message)));
    }

    @Override
    public ResponseEntity<?> deleteAllSurveyByOwnerIdAndOwnershipId(Long ownerId, Long ownershipId) {
        String message = "Собственник по ID : " + ownerId + " не найден";
        Owner owner = ownerDAO.findById(ownerId).orElse(null);
        if (owner == null) {
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        String fullName = mapOwnerToFullName(owner);
        Ownership ownership = ownershipDAO.findById(ownershipId).orElse(null);
        if (ownership == null) {
            message = "Объект собственности по ID : " + ownershipId + " не найден";
            log.info(message);
            return ResponseEntity.badRequest().body(new Response(List.of(message)));
        }
        String apartment = ownership.getAddress().getApartment();
        surveyDAO.deleteByFullNameAndApartment(fullName, apartment);
        List<String> list = surveyDAO.findAll().stream().map(Survey::getTitle).distinct().toList();
        message = "Оперция завершена успешно";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
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
}
