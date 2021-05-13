package com.appsdeveloperblog.app.ws.shared;

import com.appsdeveloperblog.app.ws.security.SecurityConsants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Service
public class Utils {

  private final Random RANDOM = new SecureRandom();
  private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private final int ITERATIONS = 10000;
  private final int KEY_LENGTH = 256;

  public String generateUserId(int length) {
    return generateRandomString(length);
  }
  public String generateAddressId(int length) {
    return generateRandomString(length);
  }

  private String generateRandomString(int length) {
    StringBuilder returnValue = new StringBuilder(length);

    for (int i = 0; i< length; i++){
      returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
    }

    return new String(returnValue);
  }

  public static boolean hasTokenExpired(String token){
    Claims claims = Jwts.parser()
            .setSigningKey(SecurityConsants.getTokenSecret())
            .parseClaimsJws( token ).getBody();

    Date tokenExpirationDate = claims.getExpiration();
    Date todayDate = new Date();

    return tokenExpirationDate.before(todayDate);
  }

  public String generateEmailVerificationToken(String userId){
    String token = Jwts.builder()
            .setSubject(userId)
            .setExpiration(new Date(System.currentTimeMillis() + SecurityConsants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConsants.getTokenSecret())
            .compact();
    return token;
  }

  public static String generatePasswordResetToken(String userId){
    String token = Jwts.builder()
            .setSubject(userId)
            .setExpiration(new Date(System.currentTimeMillis() + SecurityConsants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConsants.getTokenSecret())
            .compact();
    return token;
  }
}
