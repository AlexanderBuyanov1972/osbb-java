package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfAnswer;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "surveys")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "date_dispatch")
    private LocalDate dateDispatch;
    @Column(name = "date_receiving")
    private LocalDate dateReceiving;
    @Column(name = "apartment")
    private String apartment;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "by_whom", nullable = false)
    private String byWhom;
    @Column(name = "question", nullable = false)
    private String question;
    @Column(name = "answer")
    @Enumerated(EnumType.STRING)
    private TypeOfAnswer answer;

    public Survey(Survey q, String fullName, String apartment) {
        this.title = q.getTitle();
        this.byWhom = q.getByWhom();
        this.dateDispatch = LocalDate.now();
        this.question = q.getQuestion();
        this.answer = null;
        this.dateReceiving = null;
        this.fullName = fullName;
        this.apartment = apartment;

    }

    public Survey() {
    }

    public Survey(long id, String title, LocalDate dateDispatch, LocalDate dateReceiving, String apartment, String fullName, String byWhom, String question, TypeOfAnswer answer) {
        this.id = id;
        this.title = title;
        this.dateDispatch = dateDispatch;
        this.dateReceiving = dateReceiving;
        this.apartment = apartment;
        this.fullName = fullName;
        this.byWhom = byWhom;
        this.question = question;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDateDispatch() {
        return dateDispatch;
    }

    public LocalDate getDateReceiving() {
        return dateReceiving;
    }

    public String getApartment() {
        return apartment;
    }

    public String getFullName() {
        return fullName;
    }

    public String getByWhom() {
        return byWhom;
    }

    public String getQuestion() {
        return question;
    }

    public TypeOfAnswer getAnswer() {
        return answer;
    }

    public Survey setId(long id) {
        this.id = id;
        return this;
    }

    public Survey setTitle(String title) {
        this.title = title;
        return this;
    }

    public Survey setDateDispatch(LocalDate dateDispatch) {
        this.dateDispatch = dateDispatch;
        return this;
    }

    public Survey setDateReceiving(LocalDate dateReceiving) {
        this.dateReceiving = dateReceiving;
        return this;
    }

    public Survey setApartment(String apartment) {
        this.apartment = apartment;
        return this;
    }

    public Survey setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Survey setByWhom(String byWhom) {
        this.byWhom = byWhom;
        return this;
    }

    public Survey setQuestion(String question) {
        this.question = question;
        return this;
    }

    public Survey setAnswer(TypeOfAnswer answer) {
        this.answer = answer;
        return this;
    }

    @Override
    public String toString() {
        return "Survey = { id = " + id + ", title = " + title + ", dateDispatch = " + dateDispatch +
                ", dateReceiving = " + dateReceiving + ", apartment = " + apartment + ", fullName = " + fullName +
                ", byWhom = " + byWhom + ", question = " + question + ", answer = " + answer + " }";
    }

    public boolean equals(Survey q) {
        return Objects.equals(getTitle(), q.getTitle())
                && Objects.equals(getQuestion(), q.getQuestion())
                && Objects.equals(getFullName(), q.getFullName())
                && Objects.equals(getApartment(), q.getApartment());
    }

}

//    id
//    title
//    dateDispatch
//    dateReceiving
//    apartment
//    fullName
//    byWhom
//    question
//    answer