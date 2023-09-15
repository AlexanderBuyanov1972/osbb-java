package com.example.osbb.service.questionnaire;

import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dao.OwnerDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.QuestionnaireDAO;
import com.example.osbb.dto.FioApartmentSelects;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ShareTotalAreaQuestionAnswer;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Questionnaire;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionnaireService implements IQuestionnaireService {
    @Autowired
    QuestionnaireDAO questionnaireDAO;
    @Autowired
    OwnerDAO ownerDAO;
    @Autowired
    OwnershipDAO ownershipDAO;

    @Autowired
    AddressDAO addressDAO;

    // ----------------- one -------------------
    @Override
    @Transactional
    public Object createQuestionnaire(Questionnaire questionnaire) {
        List<String> errors = new ArrayList<>();
        try {
            if (questionnaireDAO.existsById(questionnaire.getId()))
                errors.add("Лист опроса  с таким ID уже существует.");
            return errors.isEmpty() ?
                    Response.builder()
                            .data(questionnaireDAO.save(questionnaire))
                            .messages(List.of("Лист опроса успешно создан.", "Удачного дня!"))
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
                errors.add("Лист опроса  с таким ID не существует.");
            return errors.isEmpty() ?
                    Response.builder()
                            .data(questionnaireDAO.save(questionnaire))
                            .messages(List.of("Лист опроса успешно обновлён.", "Удачного дня!"))
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
                errors.add("Лист опроса  с таким ID не существует.");
            return errors.isEmpty() ?
                    Response.builder()
                            .data(questionnaireDAO.findById(id).get())
                            .messages(List.of("Лист опроса получен успешно.", "Удачного дня!"))
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
            if (questionnaireDAO.existsById(id)) {
                questionnaireDAO.deleteById(id);
            } else {
                list.add("Опросный лист с таким ID не существует.");
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of("Удаление опросного листа прошло успешно.", "Удачного дня!"))
                    .build() : new ResponseMessages(list);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    // --------------- all --------------
    @Override
    @Transactional
    public Object createAllQuestionnaire(List<Questionnaire> questionnaires) {
        try {
            List<Questionnaire> result = new ArrayList<>();
            for (Questionnaire one : questionnaires) {
                if (!questionnaireDAO.existsById(one.getId())) {
                    questionnaireDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ResponseMessages(
                    List.of("Ни один из листов опроса создан не был. Листы опроса с такими ID уже существуют."))
                    : Response.builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно создан " + result.size() + " лист опроса из " + questionnaires.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    @Transactional
    public Object updateAllQuestionnaire(List<Questionnaire> questionnaires) {
        try {
            List<Questionnaire> result = new ArrayList<>();
            for (Questionnaire one : questionnaires) {
                if (questionnaireDAO.existsById(one.getId())) {
                    questionnaireDAO.save(one);
                    result.add(one);
                }
            }
            return result.isEmpty() ? new ResponseMessages(
                    List.of("Ни один из листов опроса обновлён не был. Листы опроса с такими ID не существуют."))
                    : Response.builder()
                    .data(returnListSorted(result))
                    .messages(List.of("Успешно обновлён " + result.size() + " лист опроса из " + questionnaires.size() + ".", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getAllQuestionnaire() {
        try {
            List<Questionnaire> result = questionnaireDAO.findAll();
            return result.isEmpty() ?
                    new ResponseMessages(List.of("В базе данных нет ни одного листа опроса по вашему запросу."))
                    :
                    Response
                            .builder()
                            .data(returnListSorted(result))
                            .messages(List.of("Запрос выполнен успешно.", "Все листы опросов получены.", "Удачного дня!"))
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
            return new ResponseMessages(List.of("Все листы опроса удалены успешно.", "Удачного дня!"));
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object getResultOfQuestionnaireByTitle(String title) {
        try {
            // ------------  initialization variables ----------------------------
            Map<String, Map<String, Double>> resultDouble = new HashMap<>();
            Map<String, Map<String, Integer>> resultInteger = new HashMap<>();
            List<ShareTotalAreaQuestionAnswer> list = new ArrayList<>();
            //  all computes
            //  getting data from DB class Questionnaire
            List<Questionnaire> questionnaireList = questionnaireDAO.findAll();
            //  filtering by title
            List<FioApartmentSelects> fioApartmentSelectsList =
                    questionnaireList.stream()
                            .filter(el -> !el.getTitle().equals(title))
                            .map(el -> new FioApartmentSelects(el.getFio(),
                                    el.getApartment(), el.getSelects()))
                            .toList();
            // filling list for base computes
            fioApartmentSelectsList.forEach(el -> {
                // getting array names from field fio
                String[] fios = el.getFio().trim().split(" ");
                // getting share in real estate
                Double share = ownerDAO.findByLastNameAndFirstNameAndSecondName(fios[0], fios[1], fios[2])
                        .getShareInRealEstate();
                // getting id of ownership
                Long id = addressDAO.findByApartment(el.getApartment()).getOwnership().getId();
                // getting total area of ownership
                Double totalArea = ownershipDAO.findById(id).get().getTotalArea();

//                System.out.println("share ---> : " + share);
//                System.out.println("id ---> : " + id);
//                System.out.println("totalArea ---> : " + totalArea);

                // addition element to list for base computes
                el.getSelectList().forEach(element -> {
                    list.add(new ShareTotalAreaQuestionAnswer(element.getQuestion(),
                            element.getAnswer(), share * totalArea));
                });
            });
            //response
            return Response
                    .builder()
                    .data(List.of(getResultDouble(resultDouble, list), getResultInteger(resultInteger, list)))
                    .messages(List.of("Результаты опроса \"" + title + "\" обработаны.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    private List<Questionnaire> returnListSorted(List<Questionnaire> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    // -------------- counter by area m2 --------------------
    private Map<String, Map<String, Double>> getResultDouble(Map<String, Map<String, Double>> result, List<ShareTotalAreaQuestionAnswer> list) {
        list.forEach(el -> {
            Map<String, Double> map;
            if (!result.containsKey(el.getQuestion())) {
                map = new HashMap<>();
                map.put(el.getAnswer().toString(), el.getShareTotalArea());
            } else {
                map = result.get(el.getQuestion());
                if (!map.containsKey(el.getAnswer().toString())) {
                    map.put(el.getAnswer().toString(), el.getShareTotalArea());
                } else {
                    Double value = map.get(el.getAnswer().toString());
                    value += el.getShareTotalArea();
                    map.put(el.getAnswer().toString(), value);
                }
            }
            result.put(el.getQuestion(), map);
        });
        return result;
    }


    // ---------------  counter by peoples --------------------------
    private Map<String, Map<String, Integer>> getResultInteger(Map<String, Map<String, Integer>> result, List<ShareTotalAreaQuestionAnswer> list) {
        list.forEach(el -> {
            Map<String, Integer> map = null;
            if (!result.containsKey(el.getQuestion())) {
                map = new HashMap<>();
                map.put(el.getAnswer().toString(), 1);
            } else {
                map = result.get(el.getQuestion());
                if (!map.containsKey(el.getAnswer().toString())) {
                    map.put(el.getAnswer().toString(), 1);
                } else {
                    int value = map.get(el.getAnswer().toString());
                    value += 1;
                    map.put(el.getAnswer().toString(), value);
                }
            }
            result.put(el.getQuestion(), map);
        });
        return result;
    }
}
