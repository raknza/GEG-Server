package com.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

public class JwtConfig {
  private static Key key = null;
  private static JwtConfig jwtConfig = null;
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtConfig.class);
  
  private JwtConfig() {
    key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  }
  
  /**
   * 
   * @return jwt class
   */
  public static JwtConfig getInstance() {
    if (jwtConfig == null) {
      jwtConfig = new JwtConfig();
    }
    return jwtConfig;
  }
  
  /**
   * create the jwt 
   * @return token
   */
  public String generateToken(String userName) {
    
    String jws = Jwts.builder().setIssuer("GitEducationGame").setAudience(userName)
        .setExpiration(new Date((new Date()).getTime() +  3 * 60 * 60 * 1000))
        .setId(UUID.randomUUID().toString()).signWith(key).compact(); // just an example id
    return jws;
  }
  
  /**
   * 
   */
  public boolean validateToken(String jwsToken) {
    boolean isValidate = false;
    try {
      Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
      isValidate = true;
    } catch (JwtException e) {
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    } 
    return isValidate;
  }
  
  /**
   * using the key to encode the jwt
   * @param token string
   */
  public Claims decodeToken(String token) {
    if (this.validateToken(token)) {
      Claims claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
      return claimsJws;
    }
    return null;
  }
  
}
