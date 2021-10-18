package com.krishlab.filter;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.krishlab.entity.UserEntity;
import com.krishlab.service.UserService;
import com.krishlab.utility.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		String authorization = httpServletRequest.getHeader("Authorization");
		String token = null;
		String userName = null;

		if (null != authorization && authorization.startsWith("Bearer ")) {
			token = authorization.substring(7);
			try {
				userName = jwtUtils.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				logger.error("An error occured during getting username from token", e);
				httpServletRequest.setAttribute("Illegal", e);
			} catch (ExpiredJwtException e) {
				logger.warn("Token is expired and not valid anymore", e);
				httpServletRequest.setAttribute("ExpiredJwt", e);
			} catch (SignatureException e) {
				logger.error("Authentication Failed. Invalid Signature.", e);
				httpServletRequest.setAttribute("other", e);
			} catch (BadCredentialsException e) {
				logger.error("Authentication Failed. Username or Password not valid.", e);
				httpServletRequest.setAttribute("exception", e);
			}
		} else {
			logger.warn("Couldn't find bearer string, will ignore the header");
		}

		if (null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserEntity userDetails = userService.findUserByNameOrEmail(userName,userName);
			if (jwtUtils.validateToken(token, userDetails)) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userService.getAuthority(userDetails));

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}

		}
		filterChain.doFilter(httpServletRequest, httpServletResponse); 

	}

}
