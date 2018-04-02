package com.digitusrevolution.rideshare.common.auth;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.exception.EmailExistExceptionMapper;
import com.digitusrevolution.rideshare.common.exception.InvalidTokenException;
import com.digitusrevolution.rideshare.common.inf.AuthServiceInf;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

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


	//This would be used by external users
	public String getToken(long userId) {

		Map<String, Object> claims = new HashMap<>();
		claims.put(ID_KEY,String.valueOf(userId));

		String compactJws = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, getKey())
				.setIssuedAt(new Date())
				.setExpiration(Date.from(DateTimeUtil.getCurrentTimeInUTC().plusDays(30).toInstant()))
				.compact();

		return compactJws;
	}

	//This token would be used by internal system
	public String getSystemToken() {

		//Note - We are using -1 as user id so that we can create dummy token for internal use
		long systemUserId = Long.valueOf(PropertyReader.getInstance().getProperty("SYSTEM_INTERNAL_USER_ID"));

		Map<String, Object> claims = new HashMap<>();
		claims.put(ID_KEY,String.valueOf(systemUserId));

		String compactJws = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, getKey())
				.setIssuedAt(new Date())
				.setExpiration(Date.from(DateTimeUtil.getCurrentTimeInUTC().plusMinutes(5).toInstant()))
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
			logger.error("Token Authentication failed");
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

	//This function is not used any more, we will figure out usage later
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

	public String getTokenFromContext(ContainerRequestContext requestContext) {
		try {
			String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String AUTHENTICATION_SCHEME = "Bearer";
			String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
			//IMP - We are checking value as "null" as somehow the string itself contains value of null instead of normal java null
			if (token.equals("null")) return null; 
			return token;			
		} catch (Exception e) {
			//This will come into effect if no header is mentioned for Authorization
			throw new WebApplicationException("Invalid Authorization header", Status.UNAUTHORIZED);
		}
	}

	public boolean validateGoogleSignInToken(String email, String signInToken) {
		try {
			// Set up the HTTP transport and JSON factory
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			String CLIENT_ID = PropertyReader.getInstance().getProperty("GOOGLE_SIGNIN_CLIENT_ID");
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
					// Specify the CLIENT_ID of the app that accesses the backend:
					.setAudience(Collections.singletonList(CLIENT_ID))
					// Or, if multiple clients access the backend:
					//.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
					.build();

			GoogleIdToken idToken;
			idToken = verifier.verify(signInToken);
			if (idToken != null) {
				logger.debug("Valid Google SignIn Token for email id:"+email);
				return true;
			} else {
				logger.error("Invalid Google SignIn Token for email id:"+email);
				return false;
			}

		} catch (Exception e) {
			throw new WebApplicationException("Invalid Google Signin Token, Please try again", e);
		}
	}
}
































