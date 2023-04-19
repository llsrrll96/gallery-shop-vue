package org.javapp.backend.gallery.controller;

import lombok.RequiredArgsConstructor;
import org.javapp.backend.gallery.entity.Cart;
import org.javapp.backend.gallery.entity.Item;
import org.javapp.backend.gallery.repository.CartRepository;
import org.javapp.backend.gallery.repository.ItemRepository;
import org.javapp.backend.gallery.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CartController {

    private final JwtService jwtService;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    private boolean isTokenValid(String token){
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return true;
    }

    @GetMapping("/api/cart/items")
    public ResponseEntity<List<Item>> getCartItems(@CookieValue(value = "token", required = false) String token){
        isTokenValid(token);
        int memberId = jwtService.getId(token);
        List<Cart> carts = cartRepository.findByMemberId(memberId);
        List<Integer> itemIds = carts.stream().map(Cart::getItemId).toList();

        List<Item> items = itemRepository.findByIdIn(itemIds);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/api/cart/items/{itemId}")
    public ResponseEntity<Cart> pushCartItem(@PathVariable int itemId, @CookieValue(value = "token", required = false) String token){
        isTokenValid(token);
        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId).orElseGet(()->{
            Cart newCart = Cart.builder()
                    .memberId(memberId)
                    .itemId(itemId)
                    .build();
            cartRepository.save(newCart);
            return newCart;
        });
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity removeCartItem(@PathVariable int itemId,
                                         @CookieValue(value = "token", required = false)String token){
        isTokenValid(token);
        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId).orElseThrow();

        cartRepository.delete(cart);
        return new ResponseEntity(HttpStatus.OK);
    }
}
