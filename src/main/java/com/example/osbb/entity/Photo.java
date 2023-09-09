package com.example.osbb.entity;

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
    @Column(name = "id")
    private long id;

    @Column(name = "url")
    @NotEmpty
    private String url;

    @NotEmpty
    @Column(name = "name")
    private String name;


}
