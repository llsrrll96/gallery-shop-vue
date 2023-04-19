package org.javapp.backend.gallery.repository;

import org.javapp.backend.gallery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByMemberIdOrderByIdDesc(int memberId);
}
