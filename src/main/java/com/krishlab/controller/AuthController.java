package com.krishlab.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.krishlab.dto.AuthRequest;
import com.krishlab.dto.AuthResponse;
import com.krishlab.entity.Role;
import com.krishlab.entity.UserEntity;
import com.krishlab.service.UserService;
import com.krishlab.utility.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@RestController
public class AuthController {

	@Autowired
    private JwtUtils jwtUtils;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private UserService userService;
	
	@GetMapping("/")
	public String welcome() {
		return "Welcome to Auth Server!!";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}

		final UserEntity userDetails = userService.findUserByNameOrEmail(authRequest.getUsername(),authRequest.getUsername());

		final String token = jwtUtils.generateToken(userDetails);
		final String refresh_token = jwtUtils.generateRefreshToken(userDetails);
		Instant instant = Instant.now();
		List<?> app = userDetails.getRoles().stream().map(x -> x.getApps()).distinct().toList();
		return ResponseEntity
				.ok(new AuthResponse(token,refresh_token,userDetails.getId(),userDetails.getName(),userDetails.getEmail(), app, userDetails.getCompanies().stream().toList(), instant.toEpochMilli()));
	}
	
	@GetMapping("/token/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String authorization = request.getHeader("Authorization");
		String token = null;
		String refresh_token  = request.getHeader("refresh_token");
		String userName = null;
		if (null != authorization && null != refresh_token && authorization.startsWith("Bearer ")) {
			token = authorization.substring(7);
			try {
				
				userName = jwtUtils.getUsernameFromToken(token);
				System.out.print(jwtUtils.getClaimFromToken(token, Claims::getClass));
				
				if(!userName.equals(jwtUtils.getUsernameFromToken(refresh_token))) {
					throw new RuntimeException("Invalid token !!!!");
				}
				
				UserEntity userDetails = userService.findUserByNameOrEmail(userName,userName);
				
				token = jwtUtils.generateToken(userDetails);
				refresh_token = jwtUtils.generateRefreshToken(userDetails);
				
				Instant instant = Instant.now();
				List<?> app = userDetails.getRoles().stream().map(x -> x.getApps()).distinct().toList();
				
				return ResponseEntity
						.ok(new AuthResponse(token,refresh_token,userDetails.getId(),userDetails.getName(),userDetails.getEmail(), app, userDetails.getCompanies().stream().toList(), instant.toEpochMilli()));
			} 
			catch (Exception e) {
				request.setAttribute("exception", e);
			}
		}
		else {
			throw new RuntimeException("Token header are missing !!!");
		}

		return ResponseEntity.badRequest().body("Invalid Token!!!");
	}

}
