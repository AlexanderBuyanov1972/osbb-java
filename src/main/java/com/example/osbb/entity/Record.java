package com.example.osbb.entity;

import com.example.osbb.entity.owner.Owner;
import com.example.osbb.entity.ownership.Ownership;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "records")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @JoinColumn(name = "create_at")
    private LocalDateTime createAt;

    @JoinColumn(name = "update_at")
    private LocalDateTime updateAt;

    @JoinColumn(name = "share")
    private Double share;


    // many to one --------------------------

    @ManyToOne
    @JoinColumn(name = "ownership_id")
    private Ownership ownership;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public Record() {
    }

    public Record(long id, LocalDateTime createAt, LocalDateTime updateAt, Double share, Ownership ownership, Owner owner) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.share = share;
        this.ownership = ownership;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public Double getShare() {
        return share;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public Owner getOwner() {
        return owner;
    }

    public Record setId(long id) {
        this.id = id;
        return this;
    }

    public Record setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public Record setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public Record setShare(Double share) {
        this.share = share;
        return this;
    }

    public Record setOwnership(Ownership ownership) {
        this.ownership = ownership;
        return this;
    }

    public Record setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "Record = { id = " + id + ", createAt = " + createAt + ", updateAt = " + updateAt +
                ", share = " + share + ", ownership = " + ownership + ", owner = " + owner + " }";
    }
}

//    id
//    createAt
//    updateAt
//    ownership
//    owner
