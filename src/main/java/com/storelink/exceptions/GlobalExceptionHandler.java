	package com.storelink.exceptions;

import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex,
			WebRequest request) 
	{
		log.error("ResourceNotFoundException: {}",ex.getMessage(),ex);
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) 
    {	
    	log.error("IllegalArgumentException: {}",ex.getMessage(),ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(
            BadRequestException ex, WebRequest request) 
    {
    	log.error("BadRequestException: {}",ex.getMessage(),ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    @SuppressWarnings("null")
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){
    	
    	log.error("DataIntegrityViolationException: {}",ex.getMessage(),ex);
    	
    	String message = "Database integrity violation occurred.";
    	
    	if(ex.getRootCause() != null) {
    		message += " "+ex.getRootCause().getMessage();
    	}
    	
    	ErrorDetails errorDetails = new ErrorDetails(
    			LocalDateTime.now(),
    			message,
    			request.getDescription(false)
    	);
    	
    	return new ResponseEntity<>(errorDetails,HttpStatus.CONFLICT);
    }
    
    @SuppressWarnings("null")
	@ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDetails> handleDataAccessException(
            DataAccessException ex, WebRequest request) 
    {
    	log.error("DataAccessException: {}",ex.getMessage(),ex);
    	
        String message = "A database error occurred.";
        if (ex.getRootCause() != null) {
            message += " " + ex.getRootCause().getMessage();
        }
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception ex, WebRequest request) 
    {	
    	log.error("Exception: {}",ex.getMessage(),ex);
    	
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
