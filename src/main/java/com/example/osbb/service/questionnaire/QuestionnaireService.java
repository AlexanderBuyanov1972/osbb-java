package com.example.osbb.service.questionnaire;

import com.example.osbb.dao.*;
import com.example.osbb.dto.*;
import com.example.osbb.entity.Address;
import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;
import com.example.osbb.entity.Questionnaire;
import com.example.osbb.enums.TypeOfAnswer;
import com.example.osbb.service.ServiceMessages;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class QuestionnaireService implements IQuestionnaireService {
    @Autowired
    QuestionnaireDAO questionnaireDAO;
    @Autowired
    AddressDAO addressDAO;
    @Autowired
    OwnershipDAO ownershipDAO;

    // ----------------- one -------------------
    @Override
    @Transactional
    public Object createQuestionnaire(Questionnaire questionnaire) {
        List<String> errors = new ArrayList<>();
        Questionnaire one = null;
        try {
            if (questionnaireDAO.existsById(questionnaire.getId())) {
                if (questionnaire.getAnswer() != null) {
                    questionnaire.setDateReceiving(LocalDate.now());
                    one = questionnaireDAO.save(questionnaire);
                }
            }
            errors.add(ServiceMessages.NOT_EXISTS);
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
                errors.add(ServiceMessages.NOT_EXISTS);
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
                            .data(questionnaireDAO.findById(id).get())
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
            if (questionnaireDAO.existsById(id)) {
                questionnaireDAO.deleteById(id);
            } else {
                list.add(ServiceMessages.NOT_EXISTS);
            }
            return list.isEmpty() ? Response
                    .builder()
                    .data(id)
                    .messages(List.of(ServiceMessages.OK))
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
                if (questionnaireDAO.existsById(one.getId())) {
                    if (one.getAnswer() != null) {
                        one.setDateReceiving(LocalDate.now());
                        questionnaireDAO.save(one);
                        result.add(one);
                    }

                }
            }
            return result.isEmpty() ? new ResponseMessages(
                    List.of(ServiceMessages.NOT_CREATED))
                    : Response.builder()
                    .data(returnListSortedById(result))
                    .messages(List.of(ServiceMessages.OK))
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
                    List.of(ServiceMessages.NOT_UPDATED))
                    : Response.builder()
                    .data(returnListSortedById(result))
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
                    .toList();
            return Response
                    .builder()
                    .data(returnListSortedByApartment(list))
                    .messages(List.of(ServiceMessages.OK))
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

    // selects ************************************************
    // ---------------- one ---------------------
    @Override
    public Object selectAllQuestionnaireByTitle(String title) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitle(title)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            return Response
                    .builder()
                    .data(returnListSortedByApartment(list))
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllTitles() {
        try {
            List<String> list = questionnaireDAO.findAll()
                    .stream()
                    .map(Questionnaire::getTitle)
                    .distinct()
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByQuestion(String question) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByQuestion(question)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByFullname(String fullname) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByFullname(fullname);
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByApartment(String apartment) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByApartment(apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByDateDispatch(LocalDate dateDispatch) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByDateDispatch(dateDispatch)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            ;
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByDateReceiving(LocalDate dateReceiving) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByDateReceiving(dateReceiving)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            ;
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // ------------------------- two ---------------------

    @Override
    public Object selectAllQuestionnaireByTitleAndQuestion(String title, String question) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndQuestion(title, question)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            ;
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByTitleAndApartment(String title, String apartment) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndApartment(title, apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            ;
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByTitleAndFullname(String title, String fullname) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndFullname(title, fullname)
                    .stream()
                    .filter(el -> el.getAnswer() == null)
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByFullnameAndApartment(String fullname, String apartment) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByFullnameAndApartment(fullname, apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            ;
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByTitleAndDateDispatch(String title, LocalDate dateDispatch) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndDateDispatch(title, dateDispatch)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByTitleAndDateReceiving(String title, LocalDate dateReceiving) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndDateReceiving(title, dateReceiving);
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // three *****************************************************************************

    @Override
    public Object selectAllQuestionnaireByTitleAndFullnameAndApartment(String title, String fullname, String apartment) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndFullnameAndApartment(title, fullname, apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    // sorted ****************************************************************************
    private List<Questionnaire> returnListSortedById(List<Questionnaire> list) {
        return list.stream().sorted((a, b) -> (int) (a.getId() - b.getId())).collect(Collectors.toList());
    }

    private List<Questionnaire> returnListSortedByApartment(List<Questionnaire> list) {
        return list.stream()
                .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                .collect(Collectors.toList());
    }


    //  result ***************************************************************
    @Override
    public Object getResultOfQuestionnaireByTitle(String title) {
        List<String> messages = new ArrayList<>();
        try {
            // базовый лист с которым работаем на счётчик голосов
            List<Questionnaire> baseList = questionnaireDAO.findByTitle(title)
                    .stream().filter(x -> x.getDateReceiving() != null).toList();

            // мапа для подсчёта голосов собственниками
            Map<String, Map<TypeOfAnswer, Long>> mapCountPeople = baseList.stream()
                    .collect(Collectors.groupingBy(Questionnaire::getQuestion,
                            Collectors.groupingBy(Questionnaire::getAnswer, Collectors.counting())));


            //  мапа для подсчёта голосов квадратными метрами
            Map<String, Map<TypeOfAnswer, Double>> mapCountArea =
                    generateShareTotalAreaQuestionAnswer(baseList)
                            .stream()
                            .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
                                    Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getAnswer,
                                            Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea))));
            if (mapCountPeople.isEmpty() || mapCountArea.isEmpty()) {
                messages.add("Нет данных для обработки результатов голосования.");
            }

            return messages.isEmpty() ? Response
                    .builder()
                    .data(List.of(mapCountPeople, mapCountArea))
                    .messages(List.of("Результаты опроса \"" + title + "\" обработаны.", "Удачного дня!"))
                    .build() : new ResponseMessages(messages);
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<ShareTotalAreaQuestionAnswer> generateShareTotalAreaQuestionAnswer(List<Questionnaire> baseList) {
        List<ShareTotalAreaQuestionAnswer> result = new ArrayList<>();
        baseList.forEach(q -> {
            Double totalArea = addressDAO.findByApartment(q.getApartment()).getOwnership().getTotalArea();
            AtomicReference<Double> share = new AtomicReference<>(0.0);
            Owner owner =  ownershipDAO.findByAddressApartment(q.getApartment()).stream().findFirst().get().getOwner();
            String fullname = owner.getLastName() + " " + owner.getFirstName() + " " + owner.getSecondName();
            if (q.getFullname().equals(fullname)) {
                share.set(owner.getShareInRealEstate());
            }
              result.add(new ShareTotalAreaQuestionAnswer(q.getQuestion(), q.getAnswer(), share.get() * totalArea));
        });
        return result;
    }

    // generate ***************************************************************
    @Override
    public Object generateListQuestionnaire(List<Questionnaire> questionnaires) {
        try {
            getListFullNameOwnerAndApartmentForHouse().forEach(el -> {
                questionnaires.forEach(x -> {
                    questionnaireDAO.save(makeQuestionnaire(x, el.getFullname(), el.getApartment()));
                });
            });
            return Response
                    .builder()
                    .data(List.of())
                    .messages(List.of("Генерация прошла успешно.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }
    }

    private Questionnaire makeQuestionnaire(Questionnaire questionnaire, String fullname, String apartment) {
        return Questionnaire
                .builder()
                .title(questionnaire.getTitle())
                .byWhom(questionnaire.getByWhom())
                .dateDispatch(LocalDate.now())
                .question(questionnaire.getQuestion())
                .answer(null)
                .dateReceiving(null)
                .fullname(fullname)
                .apartment(apartment)
                .build();
    }


    // формирует лист объектов собственник-квартира **************************************
    private List<FullNameOwnerAndApartment> getListFullNameOwnerAndApartmentForHouse() {
        List<FullNameOwnerAndApartment> result = new ArrayList<>();
        ownershipDAO.findAll().forEach(el -> {
            String apartment = el.getAddress().getApartment();
            Double totalArea = el.getTotalArea();
            String fullname = el.getOwner().getLastName() + " " + el.getOwner().getFirstName() + " " + el.getOwner().getSecondName();
            Double share = el.getOwner().getShareInRealEstate();
            result.add(new FullNameOwnerAndApartment(fullname, share, apartment, totalArea));
        });
        return result;
    }

}
