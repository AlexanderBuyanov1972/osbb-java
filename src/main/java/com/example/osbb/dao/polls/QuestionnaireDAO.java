package com.example.osbb.dao.polls;

import com.example.osbb.entity.polls.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.List;

@EnableJpaRepositories
public interface QuestionnaireDAO extends JpaRepository<Questionnaire, Long> {
    // one ------------------
    List<Questionnaire> findByTitle(String title);

    void deleteByTitle(String title);

    List<Questionnaire> findByQuestion(String question);

    List<Questionnaire> findByApartment(String apartment);

    List<Questionnaire> findByFullName(String fullName);

    List<Questionnaire> findByDateDispatch(LocalDate dateDispatch);

    List<Questionnaire> findByDateReceiving(LocalDate dateReceiving);

    // ------------- two ----------------------------

    List<Questionnaire> findByTitleAndQuestion(String title, String question);

    List<Questionnaire> findByTitleAndFullName(String title, String fullName);

    List<Questionnaire> findByTitleAndApartment(String title, String apartment);

    List<Questionnaire> findByFullNameAndApartment(String fullName, String apartment);

    void deleteByFullNameAndApartment(String fullName, String apartment);

    List<Questionnaire> findByTitleAndDateDispatch(String title, LocalDate dateDispatch);

    List<Questionnaire> findByTitleAndDateReceiving(String title, LocalDate dateReceiving);

    // --------------- three ---------------------
    List<Questionnaire> findByTitleAndFullNameAndApartment(String title, String fullName, String apartment);

    List<Questionnaire> findByTitleAndFullNameAndDateReceiving(String title, String fullName, LocalDate dateReceiving);
}
