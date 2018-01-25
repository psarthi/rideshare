package com.digitusrevolution.rideshare.common.auth;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class.getName());
    //private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    		
    		String url = requestContext.getUriInfo().getAbsolutePath().toString();
    		logger.debug("URL:"+url);
        // Get the Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader
                            .substring(AUTHENTICATION_SCHEME.length()).trim();

        try {

            // Validate the token
            validateToken(token);

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    public boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    public void abortWithUnauthorized(ContainerRequestContext requestContext) {

    		// Original code, where Realm has been used whose purpose is not clear for 
    		// token perspective so commented and modified the response
    	
        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
    		/*
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, 
                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                        .build());
        */
    	
		String errorType = "INVALID_TOKEN";
		String errorMessage = PropertyReader.getInstance().getProperty("INVALID_TOKEN_ERROR_MESSAGE");

    		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
    				.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
    				.entity(new ErrorMessage(Status.UNAUTHORIZED.getStatusCode(), errorType, errorMessage))
    				.build());

    }

    private void validateToken(String token) throws Exception {
        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
    		if (!AuthService.getInstance().verifyToken(token)) {
    			throw new Exception();
    		}
    }
}