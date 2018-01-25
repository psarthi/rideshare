package com.digitusrevolution.rideshare.common.auth;

import java.security.Key;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.exception.EmailExistExceptionMapper;
import com.digitusrevolution.rideshare.common.inf.AuthServiceInf;
import com.digitusrevolution.rideshare.common.util.PropertyReader;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class AuthService implements AuthServiceInf{

	private static final Logger logger = LogManager.getLogger(AuthService.class.getName());
	private static final AuthService AUTH_SERVICE = new AuthService();
	private static final String ID_KEY = "id";

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


	public String getToken(long userId) {

		Map<String, Object> claims = new HashMap<>();
		claims.put(ID_KEY,String.valueOf(userId));

		String compactJws = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, getKey())
				.setIssuedAt(new Date())
				.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(60).toInstant()))
				.compact();

		return compactJws;
	}

	public boolean verifyToken(String token) {

		try {
			Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token);
			logger.debug("Token Authentication successful for Id:"+parseClaimsJws.getBody().get(ID_KEY));
			//OK, we can trust this JWT
			return true;

		} catch (SignatureException e) {
			//don't trust the JWT!
			logger.debug("Token Authentication failed");
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

	public long getUserId(String token) {
		Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token);
		return Long.parseLong(parseClaimsJws.getBody().get(ID_KEY).toString());	
	}

	public boolean validateTokenClaims(long userId, ContainerRequestContext requestContext) {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		String AUTHENTICATION_SCHEME = "Bearer";
		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
		try {
			Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token);
			long tokenId = Long.parseLong(parseClaimsJws.getBody().get(ID_KEY).toString());
			if (tokenId == userId){
				logger.debug("Valid Token Claims for id:"+userId);
				return true;
			} else {
				if (tokenId ==Long.valueOf(PropertyReader.getInstance().getProperty("SYSTEM_INTERNAL_USER_ID"))) {
					logger.debug("System Internal User Id request, so ignoring validation for user Id:"+userId);
					return true;
				} else {
					logger.debug("Invalid Token Claims w.r.t the url parameters for id:"+userId);
					return false;						
				}
			}
		} catch (SignatureException e) {
			//don't trust the JWT!
			logger.debug("Invalid token for id:"+userId);
			return false;
		}
	}
}
































