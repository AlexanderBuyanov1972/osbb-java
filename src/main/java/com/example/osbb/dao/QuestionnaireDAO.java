package com.example.osbb.dao;

import com.example.osbb.entity.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@EnableJpaRepositories
public interface QuestionnaireDAO extends JpaRepository<Questionnaire, Long> {
    // ------------- one ------------------
    List<Questionnaire> findByTitle(String title);

    List<Questionnaire> findByQuestion(String question);

    List<Questionnaire> findByApartment(String apartment);

    List<Questionnaire> findByFullname(String fullname);

    List<Questionnaire> findByDateDispatch(LocalDate dateDispatch);

    List<Questionnaire> findByDateReceiving(LocalDate dateReceiving);

    // ------------- two ----------------------------

    List<Questionnaire> findByTitleAndQuestion(String title, String question);

    List<Questionnaire> findByTitleAndFullname(String title, String fullname);

    List<Questionnaire> findByTitleAndApartment(String title, String apartment);

    List<Questionnaire> findByFullnameAndApartment(String fullname, String apartment);

    List<Questionnaire> findByTitleAndDateDispatch(String title, LocalDate dateDispatch);

    List<Questionnaire> findByTitleAndDateReceiving(String title, LocalDate dateReceiving);

    // --------------- three ---------------------
    List<Questionnaire> findByTitleAndFullnameAndApartment(String title, String fullname, String apartment);


}
