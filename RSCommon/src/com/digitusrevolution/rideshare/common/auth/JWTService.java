package com.digitusrevolution.rideshare.common.auth;

import java.security.Key;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.digitusrevolution.rideshare.common.inf.AuthService;
import com.digitusrevolution.rideshare.common.util.PropertyReader;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTService implements AuthService{
	
	private static final JWTService JWT_SERVICE = new JWTService();
	
	public static JWTService getInstance() {
		return JWT_SERVICE;
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
		
		String compactJws = Jwts.builder()
				  .setSubject(Integer.toString(userId))
				  .signWith(SignatureAlgorithm.HS512, getKey())
				  .compact();
		
		return compactJws;
	}
	
	public boolean verifyToken(String token) {
		
		try {

		    Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token);
		    //OK, we can trust this JWT
		    return true;
		    
		} catch (SignatureException e) {
		    //don't trust the JWT!
			return false;
		}
	}

}
