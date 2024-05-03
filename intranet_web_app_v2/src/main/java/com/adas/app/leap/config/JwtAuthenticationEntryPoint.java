package com.adas.app.leap.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		
		System.out.println("IN entry point------------------------");

		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Exception exception = (Exception) request.getAttribute("exception");

		String message;
		

		if (exception != null) {			
			
			
			
			String errorMessage = exception.toString().split(":")[0].toUpperCase();
			
			System.out.println(errorMessage);
			
			if(errorMessage.equals("IO.JSONWEBTOKEN.EXPIREDJWTEXCEPTION")) {
				response.sendError(501,"EXPIRED_TOKEN");
			}else if(errorMessage.equals("IO.JSONWEBTOKEN.MALFORMEDJWTEXCEPTION")) {
				response.sendError(502,"MALFORMED_TOKEN");
			}else if(errorMessage.equals("IO.JSONWEBTOKEN.SIGNATUREEXCEPTION")){
				response.sendError(503,"SIGNATURE_EXCEPTION");
			}

		} else {
			
			if(authException.getMessage().toUpperCase().equals("BAD CREDENTIALS")) {
				response.sendError(500,"INVALID_CREDENTIALS");
			}else {
			
				response.sendError(510,"UNAUTHORIZED");
			}


		}		
		
	}
}
