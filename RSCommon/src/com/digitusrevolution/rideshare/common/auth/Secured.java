package com.digitusrevolution.rideshare.common.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
//IMP - If you remove "TYPE" then somehow all end points gets secured by its own 
//and you don't get selective secured for authentication
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Secured { 
	
}