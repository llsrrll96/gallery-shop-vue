package org.javapp.backend.gallery.repository;

import org.javapp.backend.gallery.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    Optional<Member> findByEmailAndPassword(String email, String password);
}
