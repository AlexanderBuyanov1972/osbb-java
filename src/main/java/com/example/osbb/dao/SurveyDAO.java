package com.example.osbb.dao;

import com.example.osbb.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.List;

@EnableJpaRepositories
public interface SurveyDAO extends JpaRepository<Survey, Long> {
    // one ------------------
    List<Survey> findByTitle(String title);

    void deleteByTitle(String title);

    List<Survey> findByQuestion(String question);

    List<Survey> findByApartment(String apartment);

    List<Survey> findByFullName(String fullName);

    List<Survey> findByDateDispatch(LocalDate dateDispatch);

    List<Survey> findByDateReceiving(LocalDate dateReceiving);

    // ------------- two ----------------------------

    List<Survey> findByTitleAndQuestion(String title, String question);

    List<Survey> findByTitleAndFullName(String title, String fullName);

    List<Survey> findByTitleAndApartment(String title, String apartment);

    List<Survey> findByFullNameAndApartment(String fullName, String apartment);

    void deleteByFullNameAndApartment(String fullName, String apartment);

    List<Survey> findByTitleAndDateDispatch(String title, LocalDate dateDispatch);

    List<Survey> findByTitleAndDateReceiving(String title, LocalDate dateReceiving);

    // --------------- three ---------------------
    List<Survey> findByTitleAndFullNameAndApartment(String title, String fullName, String apartment);

    List<Survey> findByTitleAndFullNameAndDateReceiving(String title, String fullName, LocalDate dateReceiving);
}
