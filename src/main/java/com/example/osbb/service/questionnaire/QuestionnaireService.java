package com.example.osbb.service.questionnaire;

import com.example.osbb.dao.*;
import com.example.osbb.dto.FullNameOwnerAndApartment;
import com.example.osbb.dto.Response;
import com.example.osbb.dto.ShareTotalAreaQuestionAnswer;
import com.example.osbb.dto.messages.ErrorResponseMessages;
import com.example.osbb.dto.messages.ResponseMessages;
import com.example.osbb.entity.Address;
import com.example.osbb.entity.Owner;
import com.example.osbb.entity.Ownership;
import com.example.osbb.entity.Questionnaire;
import com.example.osbb.enums.TypeOfAnswer;
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
            errors.add("Лист опроса м тикам id не существует");
            return errors.isEmpty() ?
                    Response.builder()
                            .data(one)
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
                if (questionnaireDAO.existsById(one.getId())) {
                    if (one.getAnswer() != null) {
                        one.setDateReceiving(LocalDate.now());
                        questionnaireDAO.save(one);
                        result.add(one);
                    }

                }
            }
            return result.isEmpty() ? new ResponseMessages(
                    List.of("Ни один из листов опроса создан не был. Листы опроса с такими ID уже существуют."))
                    : Response.builder()
                    .data(returnListSortedById(result))
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
                    .data(returnListSortedById(result))
                    .messages(List.of("Успешно обновлён " + result.size() + " лист опроса из " + questionnaires.size() + ".", "Удачного дня!"))
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
                    .messages(List.of("Получено " + list.size() + " объектов."))
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
                    .toList();;
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
                    .toList();;
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
                    .toList();;
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
                    .toList();;
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
                    .toList();;
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
        try {
            // базовый лист с которым работаем на счётчик голосов
            List<Questionnaire> baseList = questionnaireDAO.findByTitle(title)
                    .stream().filter(x -> x.getDateReceiving() != null).toList();
            // мапа для подсчёта голосов собственниками
            Map<String, Map<TypeOfAnswer, Long>> mapCountPeople = baseList.stream()
                    .collect(Collectors.groupingBy(Questionnaire::getQuestion,
                            Collectors.groupingBy(Questionnaire::getAnswer, Collectors.counting())));
            // итоги подсчёта голосов собственниками
            Map<String, Long> itogCountPeople = baseList.stream()
                    .collect(Collectors.groupingBy(Questionnaire::getQuestion, Collectors.counting()));

            //  мапа для подсчёта голосов квадратными метрами
            Map<String, Map<TypeOfAnswer, Double>> mapCountArea =
                    generateShareTotalAreaQuestionAnswer(baseList)
                            .stream()
                            .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
                                    Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getAnswer,
                                            Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea))));
            // итоги подсчёта голосов квадратными метрами
            Map<String, Double> itogCountArea =
                    generateShareTotalAreaQuestionAnswer(baseList)
                            .stream()
                            .collect(Collectors.groupingBy(ShareTotalAreaQuestionAnswer::getQuestion,
                                    Collectors.summingDouble(ShareTotalAreaQuestionAnswer::getShareTotalArea)));
            return Response
                    .builder()
                    .data(List.of(mapCountPeople, mapCountArea, itogCountPeople, itogCountArea))
                    .messages(List.of("Результаты опроса \"" + title + "\" обработаны.", "Удачного дня!"))
                    .build();
        } catch (Exception exception) {
            return new ErrorResponseMessages(List.of(exception.getMessage()));
        }

    }

    private List<ShareTotalAreaQuestionAnswer> generateShareTotalAreaQuestionAnswer(List<Questionnaire> baseList) {
        List<ShareTotalAreaQuestionAnswer> result = new ArrayList<>();
        baseList.forEach(q -> {
            Address address = addressDAO.findByApartment(q.getApartment());
            Double totalArea = address.getOwnership().getTotalArea();
            AtomicReference<Double> share = new AtomicReference<>(0.0);
            List<Owner> owners = address.getOwnership().getOwners();
            owners.forEach(y -> {
                String fullname = y.getLastName() + " " + y.getFirstName() + " " + y.getSecondName();
                if (q.getFullname().equals(fullname)) {
                    share.set(y.getShareInRealEstate());
                }
            });
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

            el.getOwners().forEach(x -> {

                String fullname = x.getLastName() + " " + x.getFirstName() + " " + x.getSecondName();

                Double share = x.getShareInRealEstate();

                result.add(new FullNameOwnerAndApartment(fullname, share, apartment, totalArea));
            });
        });

        return result;
    }

    // замена fullname в базе данных  на имена собственников из таблицы owners
    private void changeFullnameForListQuestionnaire(String title) {
        List<Ownership> ownershipList = ownershipDAO.findAll();
        List<Questionnaire> questionnaireList = questionnaireDAO.findByTitle(title);
        ownershipList.forEach(el -> {
            questionnaireList.forEach(que -> {
                if (el.getAddress().getApartment().equals(que.getApartment())) {
                    List<Owner> ownerList = el.getOwners();
                    List<Questionnaire> questionnaireList1 = questionnaireDAO.findByApartment(que.getApartment());
                    questionnaireList1.forEach(v -> {
                        for (Owner one : ownerList) {
                            v.setFullname(one.getLastName() + " " + one.getFirstName() + " " + one.getSecondName());
                            questionnaireDAO.save(v);
                        }
                    });
                }
            });
        });
    }
}
