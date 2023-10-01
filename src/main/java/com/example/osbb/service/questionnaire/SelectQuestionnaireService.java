package com.example.osbb.service.questionnaire;

import com.example.osbb.dao.AddressDAO;
import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.QuestionnaireDAO;
import com.example.osbb.dto.response.ErrorResponseMessages;
import com.example.osbb.dto.response.Response;
import com.example.osbb.entity.Questionnaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SelectQuestionnaireService implements ISelectQuestionnaireService {

    @Autowired
    QuestionnaireDAO questionnaireDAO;
    @Autowired
    AddressDAO addressDAO;
    @Autowired
    OwnershipDAO ownershipDAO;
    //  one -------------------------------------
    @Override
    public Object selectAllQuestionnaireByTitle(String title) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitle(title)
                    .stream()
                    .filter(el -> el.getDateReceiving() == null)
                    .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
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
            List<Questionnaire> list = questionnaireDAO.findByFullName(fullname);
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
            List<Questionnaire> list = questionnaireDAO.findByTitleAndFullName(title, fullname)
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
            List<Questionnaire> list = questionnaireDAO.findByFullNameAndApartment(fullname, apartment)
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

    // three ------------------------------------------

    @Override
    public Object selectAllQuestionnaireByTitleAndFullnameAndApartment(String title, String fullname, String apartment) {
        try {
            List<Questionnaire> list = questionnaireDAO.findByTitleAndFullNameAndApartment(title, fullname, apartment)
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

}
