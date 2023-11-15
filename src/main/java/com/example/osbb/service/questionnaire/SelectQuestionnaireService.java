package com.example.osbb.service.questionnaire;

import com.example.osbb.dao.QuestionnaireDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.Questionnaire;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SelectQuestionnaireService implements ISelectQuestionnaireService {
    private static final Logger log = Logger.getLogger(SelectQuestionnaireService.class);

    @Autowired
    QuestionnaireDAO questionnaireDAO;

    //  one -------------------------------------
    @Override
    public Object selectAllQuestionnaireByTitle(String title) {
        log.info("Method selectAllQuestionnaireByTitle : enter");
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitle(title)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                    .toList();
            log.info("Получено " + list.size() + " объектов.");
            log.info("Method selectAllQuestionnaireByTitle : exit");
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByFullName(String fullname) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByFullName(fullname);
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // two ----------------------------------------

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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByTitleAndFullName(String title, String fullname) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndFullName(title, fullname)
                    .stream()
                    .filter(el -> el.getAnswer() == null)
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    @Override
    public Object selectAllQuestionnaireByFullNameAndApartment(String fullName, String apartment) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByFullNameAndApartment(fullName, apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            ;
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
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
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

    // three ------------------------------------------

    @Override
    public Object selectAllQuestionnaireByTitleAndFullNameAndApartment(String title, String fullName, String apartment) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndFullNameAndApartment(title, fullName, apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of("Получено " + list.size() + " объектов."))
                    .build();
        } catch (Exception error) {
            log.error("UNEXPECTED SERVER ERROR");
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of("UNEXPECTED SERVER ERROR", error.getMessage()));
        }
    }

}
