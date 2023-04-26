package com.dk0124.respos.security.jwt;


import com.dk0124.respos.security.userDetails.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtils {

    private static String JWT_KEY;

    private static Long EXPIRATION_MS;

    private static String COOKIE_NAME;

    @Value("${app.dk0124.jwt.cookieName}")
    public void setCookieName(String name) {
        JwtUtils.COOKIE_NAME = name;
    }

    @Value("${app.dk0124.jwt.expirationMs}")
    public void setExpirationMs(Long expirationMs) {
        JwtUtils.EXPIRATION_MS = expirationMs;
    }

    @Value("${app.dk0124.jwt.secret}")
    public void setJwtKey(String key) {
        JwtUtils.JWT_KEY = key;
    }


    public static ResponseCookie generateTokenFromUserDetails(UserDetailsImpl userPrincipal) {
        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("roles", userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .compact();

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }


    public static String getUserNameFromJwt(String jwt) {
        return Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(jwt).getBody().getSubject();
    }

    public static boolean validateJwt(String jwt) {
        try {
            Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(jwt);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;

    }


    public static String getJwtFromCookies(HttpServletRequest req) {
        Cookie cookie = WebUtils.getCookie(req, COOKIE_NAME);
        if (cookie == null)
            return null;
        return cookie.getValue();
    }

}
