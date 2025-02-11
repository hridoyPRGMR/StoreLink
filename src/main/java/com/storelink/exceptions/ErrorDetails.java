package com.storelink.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {
	
	 private LocalDateTime timestamp;
	 private String message;
	 private String details;
	
}
