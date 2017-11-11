package com.digitusrevolution.rideshare.user;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.inf.AuthServiceInf;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JWTTest {

	public static void main(String args[]){
		

		
	//	String generateKey = JWTService.generateKey();		
	//	Key key = JWTService.getKey();
		
		AuthServiceInf authService = AuthService.getInstance();
		
		String token = authService.getToken(1);
		boolean status = authService.verifyToken(token);
		
		System.out.println("Token:"+token+"\nToken Valid:"+status);
		
		token = authService.getToken(2);
		status = authService.verifyToken(token);
		
		System.out.println("Token:"+token+"\nToken Valid:"+status);

		
	//	System.out.println(" generateKey:"+generateKey+"\n token:"+token);
		
	//	generateKey = generateKey +"dummy";
	//	key = JWTService.getKey(generateKey);
		
		
//		System.out.println(" generateKey:"+generateKey+"\n token:"+token+"\n status:"+status);
//		System.out.println("Algo:"+key.getAlgorithm()+"\nFormat:"+key.getFormat());
		
		
		
		/*
		Key key = JWTService.generateKey();
		byte[] encodedByte = key.getEncoded();
		String decodedKeyString = new String(encodedByte,StandardCharsets.UTF_8);
		String encodedBase64String = Base64.getEncoder().encodeToString(encodedByte);
		byte[] decodedBase64Byte = encodedBase64String.getBytes(StandardCharsets.UTF_8);
		String decodedBase64String = new String(decodedBase64Byte, StandardCharsets.UTF_8);
		
		System.out.println(" encodedByte:"+encodedByte+"\n encodedBase64String:"+encodedBase64String+"\n decodedBase64Byte:"+decodedBase64Byte+"\n decodedBase64String:"+decodedBase64String);
		
	//	String decodedKey = new String(Base64.getDecoder().decode(encodedByte),StandardCharsets.US_ASCII);
		
		System.out.println("Decoded String of Key is:" + decodedKeyString);
		
		String normalString = "username";
		String encodedBase64NormalString = Base64.getEncoder().encodeToString(normalString.getBytes());
		byte[] decodedData = Base64.getDecoder().decode(encodedBase64NormalString.getBytes());
		String decodedString = new String(decodedData);
		System.out.println("Normal String:"+normalString);
		System.out.println("Encoded String:"+encodedBase64NormalString);
		System.out.println("Decoded String:"+decodedString);
		
		
		Key key = JWTService.generateKey();
		byte[] encodedKey = key.getEncoded();
	//	byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		String sample = "SecretKey";
	//	encodedKey = sample.getBytes();
		SecretKey secretKey = new SecretKeySpec(encodedKey, "HmacSHA512");
		
		String encodedKeyBase64 = Base64.getEncoder().encodeToString(encodedKey);
		byte[] decodedKeyFromBase64 = Base64.getDecoder().decode(encodedKeyBase64);
		String decodedKeyBase64String = Base64.getEncoder().encodeToString(decodedKeyFromBase64);
		SecretKey secretKeyFromBase64 = new SecretKeySpec(decodedKeyFromBase64, "HmacSHA512");
		
		
		String token = JWTService.getToken(1, key);
		boolean verifyToken = JWTService.verifyToken(token, secretKeyFromBase64);

		System.out.println("Encoded Key in Byte Format:"+encodedKey);
		System.out.println("Encoded Key in Base64 String is:"+encodedKeyBase64);
		System.out.println("Decoded Key from Base64 String in Byte Format:"+decodedKeyFromBase64);
		System.out.println("Decoded Key from Base64 String in String Format:"+decodedKeyBase64String);
//		System.out.println("Decoded Key is:"+decodedKey);
		System.out.println("Secret Key is:"+Base64.getEncoder().encodeToString(secretKey.getEncoded()));
		System.out.println("Secret Key is:"+Base64.getEncoder().encodeToString(decodedKeyFromBase64));

	
		System.out.println("Key Algorithm is:"+key.getAlgorithm());
		System.out.println("Secret Key Algorithm is:"+secretKey.getAlgorithm());

		System.out.println("Token is:"+token);
		System.out.println("Token Status is:"+ verifyToken);

		
		// create new key
		try {
			SecretKey secretKey1 = KeyGenerator.getInstance("AES").generateKey();
			// get base64 encoded version of the key
			String encodedKey1 = Base64.getEncoder().encodeToString(secretKey1.getEncoded());
			
			// decode the base64 encoded string
			byte[] decodedKey1 = Base64.getDecoder().decode(encodedKey1);
			// rebuild key using SecretKeySpec
			SecretKey originalKey = new SecretKeySpec(decodedKey1, 0, decodedKey1.length, "AES"); 
			
			System.out.println("secretKey1:encodedKey1:decodedKey1:originalKey-"+ secretKey1 +":"+encodedKey1+":"+ decodedKey1+":"+originalKey);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Key key = MacProvider.generateKey();
		
		String compactJws = Jwts.builder()
				  .setSubject("Joe")
				  .signWith(SignatureAlgorithm.HS512, key)
				  .compact();
		
		System.out.println("JWT String is: "+compactJws);
		System.out.println("Key is: "+compactJws);
		
	//	compactJws = compactJws + "tampered";
		
		System.out.println("Tampered JWT String is: "+compactJws);
		
		assert Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject().equals("Joe");
		
		try {

			Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws);
		    System.out.println("Body: " + parseClaimsJws.getBody().getSubject());

		    //OK, we can trust this JWT

		} catch (SignatureException e) {

			e.printStackTrace();
		    //don't trust the JWT!
		}
		*/
	}
	
}
