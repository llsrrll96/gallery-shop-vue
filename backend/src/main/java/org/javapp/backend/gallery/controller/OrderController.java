package org.javapp.backend.gallery.controller;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.javapp.backend.gallery.dto.OrderDto;
import org.javapp.backend.gallery.entity.Cart;
import org.javapp.backend.gallery.entity.Item;
import org.javapp.backend.gallery.entity.Order;
import org.javapp.backend.gallery.repository.CartRepository;
import org.javapp.backend.gallery.repository.ItemRepository;
import org.javapp.backend.gallery.repository.OrderRepository;
import org.javapp.backend.gallery.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final JwtService jwtService;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    private boolean isTokenValid(String token){
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return true;
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<Order>> getOrder(@CookieValue(value = "token",required = false) String token){
        isTokenValid(token);

        List<Order> orders = orderRepository.findByMemberIdOrderByIdDesc(jwtService.getId(token));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/api/orders")
    public ResponseEntity<Order> pushOrder(@RequestBody OrderDto orderDto,
                                           @CookieValue(value = "token", required = false)String token){
        isTokenValid(token);

        int memberId = jwtService.getId(token);
        Order newOrder = Order.builder()
                .memberId(memberId)
                .name(orderDto.getName())
                .address(orderDto.getAddress())
                .payment(orderDto.getPayment())
                .cardNumber(orderDto.getCardNumber())
                .items(orderDto.getItems())
                .build();
        orderRepository.save(newOrder);

        cartRepository.deleteByMemberId(memberId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
