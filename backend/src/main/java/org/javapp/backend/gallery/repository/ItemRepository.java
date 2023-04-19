package org.javapp.backend.gallery.repository;

import org.javapp.backend.gallery.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Integer> {
    List<Item> findByIdIn(List<Integer> ids);
}
