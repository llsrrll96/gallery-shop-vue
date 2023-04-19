package org.javapp.backend.gallery.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


@Getter
@Table(name = "carts")
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int memberId;

    @Column
    private int itemId;

    public Cart() {
    }

    @Builder
    public Cart(int memberId, int itemId) {
        this.memberId = memberId;
        this.itemId = itemId;
    }
}
