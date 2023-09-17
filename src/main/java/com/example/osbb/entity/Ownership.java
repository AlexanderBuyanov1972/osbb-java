package com.example.osbb.entity;

import com.example.osbb.enums.TypeOfRoom;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "ownerships")
public class Ownership {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    // тип помещения (квартира или нежилое помещение)
    @Column(name = "type_room", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfRoom typeRoom;

    // общая площадь квартиры или нежилого помещения
    @Column(name = "total_area")
    private double totalArea;

    // жилая площадь квартиры или нежилого помещения
    @Column(name = "living_area")
    private double livingArea;

    // документ о праве собственности
    @Column(name = "document_confirms_right_own")
    private String documentConfirmsRightOwn;

    // количество комнат в квартире
    @Column(name = "number_rooms")
    private int numberRooms;

    // есть ли балкон
    @Column(name = "loggia", nullable = false)
    private boolean loggia;

    // по какому адресу находится
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    // список собственников
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "owner_ownership",
            joinColumns = @JoinColumn(name = "owner_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ownership_id", referencedColumnName = "id"))
    private List<Owner> owners = new ArrayList<>();
}