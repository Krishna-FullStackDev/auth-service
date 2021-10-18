package com.krishlab.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

	private String token;
	private String refreshtoken;
	private Long id;
	private String username;
	private String email;
	private List<?> app;
	private List<?> comp;
	private long timestamp;
}

