package com.example.osbb.entity.owner;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;


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

    public Photo() {
    }

    public Photo(long id, String url, String name, Owner owner) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public Owner getOwner() {
        return owner;
    }

    public Photo setId(long id) {
        this.id = id;
        return this;
    }

    public Photo setUrl(String url) {
        this.url = url;
        return this;
    }

    public Photo setName(String name) {
        this.name = name;
        return this;
    }

    public Photo setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "Photo = { id = " + id + ", url = " + url + ", name = " + name + ", owner=" + owner + " }";
    }
}

//    id
//    url
//    name
//    owner