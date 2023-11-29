package com.example.osbb.service.survey;

import com.example.osbb.controller.constants.MessageConstants;
import com.example.osbb.dao.SurveyDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.Survey;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SelectSurveyService implements ISelectSurveyService {
    private static final Logger log = Logger.getLogger(SelectSurveyService.class);
    private final String ERROR_SERVER = MessageConstants.ERROR_SERVER;

    @Autowired
    SurveyDAO surveyDAO;

    //  one -------------------------------------
    @Override
    public Object selectAllSurveyByTitle(String title) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByTitle(title)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllTitles() {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<String> list = surveyDAO.findAll()
                    .stream()
                    .map(Survey::getTitle)
                    .distinct()
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByQuestion(String question) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByQuestion(question)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByFullName(String fullname) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByFullName(fullname);
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByApartment(String apartment) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByApartment(apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByDateDispatch(LocalDate dateDispatch) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByDateDispatch(dateDispatch)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByDateReceiving(LocalDate dateReceiving) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByDateReceiving(dateReceiving)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // two ----------------------------------------

    @Override
    public Object selectAllSurveyByTitleAndQuestion(String title, String question) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByTitleAndQuestion(title, question)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByTitleAndApartment(String title, String apartment) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByTitleAndApartment(title, apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByTitleAndFullName(String title, String fullname) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByTitleAndFullName(title, fullname)
                    .stream()
                    .filter(el -> el.getAnswer() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByFullNameAndApartment(String fullName, String apartment) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByFullNameAndApartment(fullName, apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByTitleAndDateDispatch(String title, LocalDate dateDispatch) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByTitleAndDateDispatch(title, dateDispatch)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    @Override
    public Object selectAllSurveyByTitleAndDateReceiving(String title, LocalDate dateReceiving) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByTitleAndDateReceiving(title, dateReceiving);
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    // three ------------------------------------------

    @Override
    public Object selectAllSurveyByTitleAndFullNameAndApartment(String title, String fullName, String apartment) {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(messageEnter(methodName));
        try {
            List<Survey> list = surveyDAO.findByTitleAndFullNameAndApartment(title, fullName, apartment)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .toList();
            String messageResponse = "Получено " + list.size() + " объектов";
            log.info(messageResponse);
            log.info(messageExit(methodName));
            return Response
                    .builder()
                    .data(list)
                    .messages(List.of(messageResponse))
                    .build();
        } catch (Exception error) {
            log.error(ERROR_SERVER);
            log.error(error.getMessage());
            return new ErrorResponseMessages(List.of(ERROR_SERVER, error.getMessage()));
        }
    }

    private String messageEnter(String name) {
        return "Method " + name + " : enter";
    }

    private String messageExit(Object name) {
        return "Method " + name + " : exit";
    }

}
