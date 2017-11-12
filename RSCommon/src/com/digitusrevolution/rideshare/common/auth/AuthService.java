package com.digitusrevolution.rideshare.common.auth;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.digitusrevolution.rideshare.common.inf.AuthServiceInf;
import com.digitusrevolution.rideshare.common.util.PropertyReader;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class AuthService implements AuthServiceInf{
	
	private static final AuthService AUTH_SERVICE = new AuthService();
	
	public static AuthService getInstance() {
		return AUTH_SERVICE;
	}
	
	public static String generateKey() {
		SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS512);	
		String encodedKeyBase64 = Base64.getEncoder().encodeToString(key.getEncoded());
		return encodedKeyBase64;
	}
	
	private Key getKey() {
		String JWT_SECRET_KEY = PropertyReader.getInstance().getProperty("JWT_SECRET_KEY");
		byte[] encodedKey = JWT_SECRET_KEY.getBytes();
		SecretKey secretKey = new SecretKeySpec(encodedKey, SignatureAlgorithm.HS512.toString());
		return secretKey;
	}
	

	public String getToken(int userId) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("id",String.valueOf(userId));
		
		String compactJws = Jwts.builder()
				  .setClaims(claims)
				  .signWith(SignatureAlgorithm.HS512, getKey())
				  .compact();
		
		return compactJws;
	}
	
	public boolean verifyToken(String token) {
		
		try {

		    Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token);
		    System.out.println("Id in payload is:"+parseClaimsJws.getBody().get("id"));
		    //OK, we can trust this JWT
		    return true;
		    
		} catch (SignatureException e) {
		    //don't trust the JWT!
			return false;
		}
	}
	
	/*
	 * This function is temporary and needs to be replaced with proper OTP generation logic
	 */
	public String getVerificationCode() {

		SecureRandom secureRandom = new SecureRandom();
		//This will generate 4 digit number always
		int number = 1000 + secureRandom.nextInt(9000);
		return Integer.toString(number);	
	}

}
































