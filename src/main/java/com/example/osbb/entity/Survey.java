package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfAnswer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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