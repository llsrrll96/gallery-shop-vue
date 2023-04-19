package org.javapp.backend.gallery.repository;

import org.javapp.backend.gallery.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    List<Cart> findByMemberId(int memberId);
    Optional<Cart> findByMemberIdAndItemId(int memberId, int itemId);
    void deleteByMemberId(int memberId);
}
