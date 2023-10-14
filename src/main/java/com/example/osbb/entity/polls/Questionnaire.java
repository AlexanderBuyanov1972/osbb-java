package com.example.osbb.entity.polls;

import com.example.osbb.enums.TypeOfAnswer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "questionnaires")
public class Questionnaire {
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

    public Questionnaire(Questionnaire q, String fullName, String apartment) {
        this.title = q.getTitle();
        this.byWhom = q.getByWhom();
        this.dateDispatch = LocalDate.now();
        this.question = q.getQuestion();
        this.answer = null;
        this.dateReceiving = null;
        this.fullName = fullName;
        this.apartment = apartment;

    }

    public boolean equals(Questionnaire q) {
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