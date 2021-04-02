package com.company.supershop.security;

import com.company.supershop.exception.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class AccessDeniedExceptionAdvice {

 private final Logger logger = Logger.getLogger(AccessDeniedExceptionAdvice.class.getName());

    @ExceptionHandler(AccessDeniedException.class) 
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
	public ResponseEntity<String> handleError(AccessDeniedException exception) {
		logger.log(Level.INFO, "Calling AccessDeniedExceptionAdvice.........");
		
    	return new ResponseEntity<String>("{\"status\": 403}", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class) 
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
	public ResponseEntity<String> handleError(AuthenticationException exception) {
		logger.log(Level.INFO, "Caught exception AuthenticationException.........");
		
    	return new ResponseEntity<String>("{\"status\": 401}", HttpStatus.UNAUTHORIZED);
    }
}
