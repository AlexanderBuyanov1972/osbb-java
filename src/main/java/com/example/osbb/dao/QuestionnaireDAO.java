package com.example.osbb.dao;

import com.example.osbb.entity.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface QuestionnaireDAO extends JpaRepository<Questionnaire, Long> {

}
