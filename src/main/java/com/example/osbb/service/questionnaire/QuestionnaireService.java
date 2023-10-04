package com.example.osbb.service.questionnaire;

import com.example.osbb.dao.*;
import com.example.osbb.dto.polls.ShareTotalAreaQuestionAnswer;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.dto.response.ResponseMessages;
import com.example.osbb.entity.Owner;
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
    OwnershipDAO ownershipDAO;
    @Autowired
    OwnerDAO ownerDAO;
    @Autowired
    RecordDAO recordDAO;
    @Autowired
    ShareDAO shareDAO;

    //  result --------------------------------
    @Override
    public Object getResultOfQuestionnaireByTitle(String title) {
        List<String> messages = new ArrayList<>();
        try {
            // базовый лист с которым работаем на счётчик голосов
            List<Questionnaire> baseList = questionnaireDAO.findByTitle(title)
                    .stream()
                    .filter(x -> x.getDateReceiving() != null)
                    .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                    .toList();

            // мапа для подсчёта голосов собственниками по квартирам
            // Map<Вопрос, Map<Ответ, Один голос>>
            Map<String, Map<TypeOfAnswer, Map<String, Long>>> mapCountPeople = baseList.stream()
                    .collect(
                            Collectors.groupingBy(Questionnaire::getQuestion,
                                    Collectors.groupingBy(Questionnaire::getAnswer,
                                            Collectors.groupingBy(Questionnaire::getFullName,
                                                    Collectors.counting()))));


            // мапа для подсчёта голосов квадратными метрами по квартирам
            // Map<Вопрос, Map<Ответ, Квадратные метры>>
            Map<String, Map<TypeOfAnswer, Double>> mapCountArea =
                    generateShareTotalAreaQuestionAnswer(baseList)
                            .stream()
                            .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
                                    Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getAnswer,
                                            Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea))));
            // проверка если одна из мап пустая
            if (mapCountPeople.isEmpty() || mapCountArea.isEmpty()) {
                messages.addAll(List.of(
                        "Нет данных для обработки результатов голосования",
                        "Нужен хотя бы один проголосвавший"
                ));
            }
            return messages.isEmpty() ? Response
                    .builder()
                    .data(List.of(mapCountPeople, mapCountArea))
                    .messages(List.of(
                            "Результаты опроса " + title + " обработаны.",
                            "Удачного дня!"))
                    .build() : new ResponseMessages(messages);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<ShareTotalAreaQuestionAnswer> generateShareTotalAreaQuestionAnswer(List<Questionnaire> list) {
        return list.stream().map(this::computeShare).collect(Collectors.toList());
    }

    private ShareTotalAreaQuestionAnswer computeShare(Questionnaire q) {
        String[] fios = q.getFullName().split(" ");
        return shareDAO.findAll().stream()
                .filter(s -> s.getOwnership().getAddress().getApartment().equals(q.getApartment()))
                .filter(s -> s.getOwner().getLastName().equals(fios[0]))
                .filter(s -> s.getOwner().getFirstName().equals(fios[1]))
                .filter(s -> s.getOwner().getSecondName().equals(fios[2]))
                .map(s -> new ShareTotalAreaQuestionAnswer(
                        q.getQuestion(),
                        q.getAnswer(),
                        s.getValue() * s.getOwnership().getTotalArea())).findFirst().get();
    }

    // one -----------------------------
    @Override
    @Transactional
    public Object createQuestionnaire(Questionnaire questionnaire) {
        List<String> errors = new ArrayList<>();
        Questionnaire one = null;
        try {
            if (questionnaireDAO.existsById(questionnaire.getId())) {
                if (questionnaire.getAnswer() != null) {
                    // устанавливаем время создания опроса в базе данных, опрос не пройден ещё
                    questionnaire.setDateDispatch(LocalDate.now());
                    one = questionnaireDAO.save(questionnaire);
                } else {
                    errors.add("Опрос в системе уже был создан," +
                            " но он был уже пройден собственником," +
                            " поэтому создать его нельзя," +
                            " можно только обновить");
                }
            } else {
                errors.add("Опрос в системе не был сгенерирован, его создать нельзя");
            }
            return errors.isEmpty() ?
                    Response.builder()
                            .data(one)
                            .messages(List.of(ServiceMessages.OK))
                            .build()
                    : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateQuestionnaire(Questionnaire questionnaire) {
        List<String> errors = new ArrayList<>();
        try {
            if (!questionnaireDAO.existsById(questionnaire.getId()))
                errors.add("Опрос не был создан, его нет в базе данных");
            else
                // устанавливаем дату прохождения опроса, опрос пройден
                questionnaire.setDateReceiving(LocalDate.now());
            return errors.isEmpty() ?
                    Response.builder()
                            .data(questionnaireDAO.save(questionnaire))
                            .messages(List.of(ServiceMessages.OK))
                            .build()
                    : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getQuestionnaire(Long id) {
        List<String> errors = new ArrayList<>();
        try {
            if (!questionnaireDAO.existsById(id))
                errors.add(ServiceMessages.NOT_EXISTS);
            return errors.isEmpty() ?
                    Response.builder()
                            .data(questionnaireDAO.findById(id))
                            .messages(List.of(ServiceMessages.OK))
                            .build()
                    : new ResponseMessages(errors);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object deleteQuestionnaire(Long id) {
        List<String> list = new ArrayList<>();
        try {
            if (questionnaireDAO.existsById(id))
                questionnaireDAO.deleteById(id);
            else
                list.add(ServiceMessages.NOT_EXISTS);
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
                    .build() : new ResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // all -------------------------------

    @Override
    @Transactional
    public Object createAllQuestionnaire(List<Questionnaire> questionnaires) {
        try {
            recordDAO.findAll()
                    .forEach(el -> {
                        questionnaires.forEach(s -> {
                            questionnaireDAO.save(
                                    new Questionnaire(
                                            s,
                                            mapNamesToFullName(el.getOwner()),
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
    public Object deleteAllQuestionnaire() {
        try {
            questionnaireDAO.deleteAll();
            return new ResponseMessages(List.of(ServiceMessages.OK));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // other ------------------------------

    private String mapNamesToFullName(Owner o) {
        return o.getLastName() + " " + o.getFirstName() + " " + o.getSecondName();
    }

}
