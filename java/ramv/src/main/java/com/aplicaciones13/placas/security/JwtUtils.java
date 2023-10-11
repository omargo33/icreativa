package com.aplicaciones13.placas.security;

import static io.jsonwebtoken.Jwts.parserBuilder;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Clase para generar el token y validar el token
 * 
 * @autor: omargo33@gmail.com
 * @since: 2023-10-08
 * 
 */
@Component
@Slf4j
public class JwtUtils {

  @Value("${bezkoder.app.jwtSecret}")
  private String jwtSecret;

  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  /**
   * Obtiene el nombre desde el token
   * 
   * @param username
   * @return
   */
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Obiene los roles desde el token
   * 
   * @return
   */
  public List<String> getRoleStringsFromToken(String token) {
    return parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().get("roles", List.class);
  }

  /**
   * Obtiene la llave
   * 
   * @return
   */
  private Key key() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Valida el token
   * 
   * @param authToken
   * @return
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
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

  /**
   * Obtiene el token desde el header
   * 
   * @param request
   * @return
   */
  public String getJwtFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }

  /**
   * Genera el token
   * 
   * @param username
   * @param roles
   * @return
   */
  public String generateTokenFromUsername(String username, Collection<? extends GrantedAuthority> roles) {
    return Jwts.builder().setHeaderParam("typ", "JWT")
        .setSubject(username)
        .claim("roles", roles)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
}