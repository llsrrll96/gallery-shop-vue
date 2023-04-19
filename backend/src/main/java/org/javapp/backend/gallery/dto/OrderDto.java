package org.javapp.backend.gallery.dto;

import lombok.Data;

@Data
public class OrderDto {
    private String name;
    private String address;
    private String payment;
    private String cardNumber;
    private String items;

}
