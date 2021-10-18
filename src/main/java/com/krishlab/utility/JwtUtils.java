package com.krishlab.utility;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.krishlab.entity.UserEntity;
import com.krishlab.entity.Company;
import com.krishlab.entity.Role;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils implements Serializable{

	private static final long serialVersionUID = 2901570265363345844L;

	@Value("${jwt.secret}")
    private String JWT_SECRET_KEY;
	
	@Value("${jwt.expiryinms}")
	private long JWT_TOKEN_VALIDITY;
	
	//retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
    }


    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateRefreshToken(UserEntity userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //generate token for user
    //public String generateToken(UserDetails userDetails) {
    public String generateToken(UserEntity userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));
        claims.put("companies", userDetails.getCompanies().stream().map(Company::getCompName).collect(Collectors.toList()));
        return doGenerateToken(claims, userDetails.getUsername());
    }


    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
        		.setClaims(claims)
        		.setSubject(subject)
        		.setIssuer("http://krishlab.com")
        		.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY ))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY).compact();
    }
    
    


    //validate token
    public Boolean validateToken(String token, UserEntity userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
