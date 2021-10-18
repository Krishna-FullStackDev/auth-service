package com.krishlab.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishlab.dto.ErrorResponse;

@Component
public class JwtAuthenticationExceptionFilter implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Instant instant = Instant.now();
		
		try {
				Exception ex1 = (Exception) request.getAttribute("ExpiredJwt");
				Exception ex2= (Exception) request.getAttribute("other");
				Exception ex3= (Exception) request.getAttribute("Illegal");
				Exception ex4= (Exception) request.getAttribute("exception");
				if(ex1 !=null) {
					byte[] body =new ObjectMapper().writeValueAsBytes(Collections.singleton(
							new ErrorResponse(HttpStatus.UNAUTHORIZED,"Token Expired!!!","Token is expired and not valid anymore",instant.toEpochMilli()))
							);
					response.getOutputStream().write(body);
				}
				if(ex2 !=null) {
					byte[] body =new ObjectMapper().writeValueAsBytes(Collections.singleton(
							new ErrorResponse(HttpStatus.UNAUTHORIZED,"Authentication Failed!!!","Invalid Signature.",instant.toEpochMilli()))
							);
					response.getOutputStream().write(body);
				}
				if(ex3 !=null) {
					byte[] body =new ObjectMapper().writeValueAsBytes(Collections.singleton(
							new ErrorResponse(HttpStatus.UNAUTHORIZED,"Authentication Failed!!!","An error occured during getting username from token.",instant.toEpochMilli()))
							);
					response.getOutputStream().write(body);
				}
				if(ex4 !=null) {
					byte[] body =new ObjectMapper().writeValueAsBytes(Collections.singleton(
							new ErrorResponse(HttpStatus.UNAUTHORIZED,"Error!!!",ex2.getMessage(),instant.toEpochMilli()))
							);
					response.getOutputStream().write(body);
				}

		
		}
		catch(Exception ex) {
			
		}
		
		
		
		/*
		Exception exception = (Exception) request.getAttribute("exception");
		Instant instant = Instant.now();
		String message=null;
		String messageDesc=null;
		if(exception !=null) {
			if(exception.getCause()!=null) {
				message=exception.getCause().toString();
				messageDesc=exception.getMessage();
			}
			else {
				message="Error";
				messageDesc=exception.getMessage();
			}
			byte[] body =new ObjectMapper().writeValueAsBytes(Collections.singleton(
					new ErrorResponse(HttpStatus.UNAUTHORIZED,message,messageDesc,instant.toEpochMilli()))
					);
			response.getOutputStream().write(body);
		}
		else {
			
			if(authException.getCause()!=null) {
				message=authException.getCause().toString() +" "+authException.getMessage();
			}else {
				message=authException.getMessage();
			}
			//byte[] body =new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
			byte[] body =new ObjectMapper().writeValueAsBytes(Collections.singleton(
					new ErrorResponse(HttpStatus.UNAUTHORIZED,"Authentication failed !!",message,instant.toEpochMilli()))
					);
			response.getOutputStream().write(body);
		}
		
		*/
	}

}
