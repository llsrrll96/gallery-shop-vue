package org.javapp.backend.gallery.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.javapp.backend.gallery.entity.Member;
import org.javapp.backend.gallery.repository.MemberRepository;
import org.javapp.backend.gallery.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AccountController {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @PostMapping("/api/account/login")
    public ResponseEntity<Integer> login(@RequestBody Map<String,String> loginDto, HttpServletResponse httpServletResponse){
        Member member = memberRepository.findByEmailAndPassword(loginDto.get("email"), loginDto.get("password"))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        int id = member.getId();
        String token = jwtService.getToken("id",id);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/api/account/logout")
    public ResponseEntity<Boolean> logout(HttpServletResponse httpServletResponse){
        Cookie cookie = new Cookie("token", "0"); // 변경할 쿠키의 이름과 값 설정
        cookie.setMaxAge(0); // 쿠키의 유효기간 설정 (초단위, 예: 1시간 = 3600초)
        cookie.setPath("/"); // 쿠키의 유효 경로 설정 (루트 경로인 경우 "/")

        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token){
        Claims claims = jwtService.getClaims(token);
        if(claims != null){
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
