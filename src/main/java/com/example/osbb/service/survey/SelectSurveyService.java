package com.example.osbb.service.survey;

import com.example.osbb.dao.OwnershipDAO;
import com.example.osbb.dao.SurveyDAO;
import com.example.osbb.dto.Response;
import com.example.osbb.entity.Survey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class SelectSurveyService implements ISelectSurveyService {

    @Autowired
    SurveyDAO surveyDAO;
    @Autowired
    OwnershipDAO ownershipDAO;

    //  one -------------------------------------
    @Override
    public ResponseEntity<?> selectAllSurveyByTitle(String title) {
        List<Survey> list = surveyDAO.findByTitle(title)
                .stream()
                .filter(el -> el.getDateReceiving() == null)
                .sorted((a, b) -> Integer.parseInt(a.getApartment()) - Integer.parseInt(b.getApartment()))
                .toList();
        String message = "Получено по теме \"" + title + "\" " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllTitles() {
        List<String> list = surveyDAO.findAll().stream().map(Survey::getTitle).distinct().toList();
        String message = "Тем опросов зарегистрировано " + list.size() + " штук";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByQuestion(String question) {
        List<Survey> list = surveyDAO.findByQuestion(question)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По  вопросу \"" + question + "\" получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByFullName(String fullName) {
        List<Survey> list = surveyDAO.findByFullName(fullName);
        String message = "По ФИО : " + fullName + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByApartment(String apartment) {
        List<Survey> list = surveyDAO.findByApartment(apartment)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По помещению № " + apartment + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByDateDispatch(LocalDate dateDispatch) {
        List<Survey> list = surveyDAO.findByDateDispatch(dateDispatch)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По дате инициализации опроса  : " + dateDispatch.toString() + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByDateReceiving(LocalDate dateReceiving) {
        List<Survey> list = surveyDAO.findByDateReceiving(dateReceiving)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По дате прохождения опроса " + dateReceiving.toString()
                + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    // two ----------------------------------------

    @Override
    public ResponseEntity<?> selectAllSurveyByTitleAndQuestion(String title, String question) {
        List<Survey> list = surveyDAO.findByTitleAndQuestion(title, question)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По вопросу : \"" + question + "\" получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByTitleAndId(String title, Long id) {
        String apartment = ownershipDAO.findById(id).get().getAddress().getApartment();
        List<Survey> list = surveyDAO.findByTitleAndApartment(title, apartment)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По помещению № " + apartment + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByTitleAndFullName(String title, String fullname) {
        List<Survey> list = surveyDAO.findByTitleAndFullName(title, fullname)
                .stream().filter(el -> el.getAnswer() == null).toList();
        String message = "По ФИО " + fullname + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));

    }

    @Override
    public ResponseEntity<?> selectAllSurveyByFullNameAndApartment(String fullName, String apartment) {
        List<Survey> list = surveyDAO.findByFullNameAndApartment(fullName, apartment)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По ФИО : " + fullName + " и помещению № "
                + apartment + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByTitleAndDateDispatch(String title, LocalDate dateDispatch) {
        List<Survey> list = surveyDAO.findByTitleAndDateDispatch(title, dateDispatch)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По теме опроса \"" + title
                + "\" и дате инициализации " + dateDispatch.toString()
                + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    @Override
    public ResponseEntity<?> selectAllSurveyByTitleAndDateReceiving(String title, LocalDate dateReceiving) {
        List<Survey> list = surveyDAO.findByTitleAndDateReceiving(title, dateReceiving);
        String message = "По теме опроса \"" + title
                + "\" и дате прохождения опроса " + dateReceiving.toString()
                + " получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }

    // three ------------------------------------------

    @Override
    public ResponseEntity<?> selectAllSurveyByTitleAndFullNameAndApartment(String title, String fullName, String
            apartment) {
        List<Survey> list = surveyDAO.findByTitleAndFullNameAndApartment(title, fullName, apartment)
                .stream().filter(el -> el.getDateReceiving() == null).toList();
        String message = "По теме опроса \"" + title + ", ФИО : " + fullName
                + " и помещению № " + apartment + "получено " + list.size() + " объектов";
        log.info(message);
        return ResponseEntity.ok(new Response(list, List.of(message)));
    }
}
