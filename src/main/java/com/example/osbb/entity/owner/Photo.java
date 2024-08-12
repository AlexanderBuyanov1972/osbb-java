package com.example.osbb.entity.owner;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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