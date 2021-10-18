package com.krishlab.dto;


import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private HttpStatus status;
	private String messgae; //general error message
	private String errorDetail; //specific errors detail in API request processing
	private long timestamp;
}
