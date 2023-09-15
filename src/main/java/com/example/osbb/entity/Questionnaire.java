package com.example.osbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "questionnaires")
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "apartment")
    private String apartment;
    @Column(name = "fio")
    private String fio;
    @Column(name = "date_dispatch")
    private LocalDate dateDispatch;
    @Column(name = "date_receiving")
    private LocalDate dateReceiving;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "owner_questionnaire",
            joinColumns = @JoinColumn(name = "questionnaire_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"))
    @JsonIgnore
    List<Owner> owners = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "questionnaire_select",
            joinColumns = @JoinColumn(name = "questionnaire_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "select_id", referencedColumnName = "id"))
    List<Select> selects = new ArrayList<>();


}
