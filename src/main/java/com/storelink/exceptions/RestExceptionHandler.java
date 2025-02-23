	package com.storelink.exceptions;

import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.storelink.apiresponse.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages="com.storelink.controllers.api")
public class RestExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest request) 
	{
		log.error("ResourceNotFoundException: {}",ex.getMessage(),ex);
		
		return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) 
    {	
    	log.error("IllegalArgumentException: {}",ex.getMessage(),ex);
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflict(ConflictException ex,WebRequest request)
    {
    	log.error("Database Conflict: {}",ex.getMessage(),ex);
    	return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null),HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(
            BadRequestException ex, WebRequest request) 
    {
    	log.error("BadRequestException: {}",ex.getMessage(),ex);
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
    
    @SuppressWarnings("null")
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){
    	
    	log.error("DataIntegrityViolationException: {}",ex.getMessage(),ex);
    	return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null),HttpStatus.CONFLICT);
    }
    
    @SuppressWarnings("null")
	@ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(
            DataAccessException ex, WebRequest request) 
    {
    	log.error("DataAccessException: {}",ex.getMessage(),ex);
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(
            Exception ex, WebRequest request) 
    {	
    	log.error("Exception: {}",ex.getMessage(),ex);
        return new ResponseEntity<>(new ApiResponse<>(false, ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
