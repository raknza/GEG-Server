package com.utils;

import com.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtHandler {

  private static Key key = null;
  public static final long JWT_TOKEN_VALIDITY = 3 * 60 * 60 * 1000;
  
  public JwtHandler() {
    key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }

  /**
   * create the jwt 
   * @return token
   */
  public String generateToken(String userName) {
    Map<String, Object> claims = new HashMap<>();
    return  doGenerateToken(claims, userName);
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {

    String jws = Jwts.builder().setClaims(claims).setAudience(subject).setIssuer("GitEducationGame")
            .setExpiration(new Date((new Date()).getTime() +  JWT_TOKEN_VALIDITY))
            .setId(UUID.randomUUID().toString()).signWith(key).compact(); // just an example id
    return jws;
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getAudience);
  }

  //check if the token has expired
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
  }

  //validate token
  public Boolean validateToken(String token, User user) {
    final String username = getUsernameFromToken(token);
    return (username.equals(user.getUsername()) && !isTokenExpired(token));
  }
  
}
