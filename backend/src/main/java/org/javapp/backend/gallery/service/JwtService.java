package org.javapp.backend.gallery.service;


import io.jsonwebtoken.*;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private String secretKey = "javapp1234dlrjtdmstlzmfltzldlekdl&&rmflrhdlrjtdms256qlxmdltkddmlanswkdufdmfrkwlrhdlTdjdiehlsek";

    public String getToken(String key, Object value){
        Date expTime = new Date();
        expTime.setTime(expTime.getTime() + 1000 * 60 * 30); // 30분
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("type","JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expTime)
                .signWith(signKey, SignatureAlgorithm.HS256);
        return builder.compact();
    }

    public Claims getClaims(String token){
        if(token != null && !"".equals(token)){
            try{
                byte[] secretByToken = DatatypeConverter.parseBase64Binary(secretKey);
                Key signKey = new SecretKeySpec(secretByToken, SignatureAlgorithm.HS256.getJcaName());
                return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
            }catch (ExpiredJwtException e){
                // 만료됨
            }catch (JwtException e){
                // 유효하지 않음
            }
        }
        return null;
    }

    public boolean isValid(String token){
        return getClaims(token) != null;
    }

    public int getId(String token){
        Claims claims = getClaims(token);
        if(claims != null){
            return Integer.parseInt(claims.get("id").toString());
        }
        return 0;
    }
}
