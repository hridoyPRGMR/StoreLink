package com.storelink.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = "com.storelink.controllers.web")
public class MvcExceptionHandler {
	
	// 404 page not found
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(HttpServletRequest request, NoHandlerFoundException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", "The page you are looking for does not exist.");
        mav.addObject("url", request.getRequestURL());
        return mav;
    }
		
	 // 403 - Access Denied
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessDenied(HttpServletRequest request, AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("errorMessage", "You do not have permission to access this page.");
        mav.addObject("url", request.getRequestURL());
        return mav;
    }
	
    // 405 - Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView handleMethodNotAllowed(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        ModelAndView mav = new ModelAndView("error/405");
        mav.addObject("errorMessage", "Request method not allowed.");
        mav.addObject("url", request.getRequestURL());
        return mav;
    }
    
    // 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneralError(HttpServletRequest request, Exception ex) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorMessage", "An unexpected error occurred.");
        mav.addObject("url", request.getRequestURL());
        return mav;
    }
}
