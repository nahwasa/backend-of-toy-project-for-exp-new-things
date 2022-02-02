package com.nahwasa.toy.expnewthings.backend.security;

import com.nahwasa.toy.expnewthings.backend.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    public String create(UserEntity userEntity) {
        return Jwts.builder()
                // header, 서명을 위한 secret key 설정
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // 이하 payload 설정
                .setSubject(userEntity.getId())
                .setIssuer("nahwasa toy - exp new things")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)  // parseClaimsJws가 Base64 디코딩 및 파싱 담당
                .getBody(); // body에서 payload 중 subject를 빼내기 위해

        return claims.getSubject();
    }
}
