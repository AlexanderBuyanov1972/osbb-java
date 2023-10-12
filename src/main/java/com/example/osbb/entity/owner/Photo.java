package com.example.osbb.entity.owner;

import com.example.osbb.entity.owner.Owner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "photos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")})
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "url")
    @NotEmpty
    private String url;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "photo")
    @JsonIgnore
    private Owner owner;

}

//    id
//    url
//    name
//    owner