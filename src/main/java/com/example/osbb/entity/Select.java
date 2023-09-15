package com.example.osbb.entity;


import com.example.osbb.enums.Answer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "selects")
public class Select {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "question")
    private String question;
    @Column(name = "answer")
    @Enumerated(EnumType.STRING)
    private Answer answer;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "questionnaire_select",
            joinColumns = @JoinColumn(name = "select_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "questionnaire_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<Questionnaire> questionnaires = new ArrayList<>();

}
