package org.javapp.backend.gallery.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "items")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 100)
    private String imgPath;
    @Column
    private int price;
    @Column
    private int discountPer;
}
