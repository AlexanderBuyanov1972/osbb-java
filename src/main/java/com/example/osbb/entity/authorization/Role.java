package com.example.osbb.entity.authorization;

import com.example.osbb.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
public class Role {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<User> users = new ArrayList<>();

}
