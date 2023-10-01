package com.example.osbb.entity;

import com.example.osbb.dto.polls.FullNameOwnerAndApartment;
import com.example.osbb.enums.TypeOfAnswer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @Column(name = "title" , nullable = false)
    private String title;
    @Column(name = "date_dispatch")
    private LocalDate dateDispatch;
    @Column(name = "date_receiving")
    private LocalDate dateReceiving;
    @Column(name = "apartment")
    private String apartment;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "by_whom" , nullable = false)
    private String byWhom;
    @Column(name = "question" , nullable = false)
    private String question;
    @Column(name = "answer")
    @Enumerated(EnumType.STRING)
    private TypeOfAnswer answer;

    public Questionnaire(Questionnaire q, FullNameOwnerAndApartment f){
                this.title= q.getTitle();
                this.byWhom= q.getByWhom();
                this.dateDispatch= LocalDate.now();
                this.question= q.getQuestion();
                this.answer= null;
                this.dateReceiving= null;
                this.fullName = f.getFullName();
                this.apartment= f.getApartment();

    }



}