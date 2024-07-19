package com.brenogomes.todosimple.exceptions;

import java.net.http.HttpHeaders;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.brenogomes.todosimple.services.execptions.DataBindingViolationException;
import com.brenogomes.todosimple.services.execptions.ObjectNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Global_Exception_Handler")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	@Value("${server.error.include-exception}")
	private boolean printStackTrace;
	
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
		ErrorResponse errorREsponse = new ErrorResponse(
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
				"Validation error. check 'errors' field for details.");
		for(FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()){
			errorREsponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return ResponseEntity.unprocessableEntity().body(errorREsponse);
	}
	
	 @ExceptionHandler(Exception.class)
	    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	    public ResponseEntity<Object> handleAllUncaughtException(
	            Exception exception,
	            WebRequest request) {
	        final String errorMessage = "Unknown error occurred";
	        log.error(errorMessage, exception);
	        return buildErrorResponse(
	                exception,
	                errorMessage,
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                request);
	    }
	 
	 @ExceptionHandler(DataIntegrityViolationException.class)
	    @ResponseStatus(HttpStatus.CONFLICT)
	    public ResponseEntity<Object> handleDataIntegrityViolationException(
	            DataIntegrityViolationException dataIntegrityViolationException,
	            WebRequest request) {
	        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
	        log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);
	        return buildErrorResponse(
	                dataIntegrityViolationException,
	                errorMessage,
	                HttpStatus.CONFLICT,
	                request);
	    }
	 
	 @ExceptionHandler(ConstraintViolationException.class)
	    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	    public ResponseEntity<Object> handleConstraintViolationException(
	            ConstraintViolationException constraintViolationException,
	            WebRequest request) {
	        log.error("Failed to validate element", constraintViolationException);
	        return buildErrorResponse(
	                constraintViolationException,
	                HttpStatus.UNPROCESSABLE_ENTITY,
	                request);
	    }
	 
	 @ExceptionHandler(ObjectNotFoundException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    public ResponseEntity<Object> handleObjectNotFoundException(
	            ObjectNotFoundException objectNotFoundException,
	            WebRequest request) {
	        log.error("Failed to find the requested element", objectNotFoundException);
	        return buildErrorResponse(
	                objectNotFoundException,
	                HttpStatus.NOT_FOUND,
	                request);
	    }
	 
	 @ExceptionHandler(DataBindingViolationException.class)
	    @ResponseStatus(HttpStatus.CONFLICT)
	    public ResponseEntity<Object> handleDataBindingViolationException(
	            DataBindingViolationException dataBindingViolationException,
	            WebRequest request) {
	        log.error("Failed to save entity with associated data", dataBindingViolationException);
	        return buildErrorResponse(
	                dataBindingViolationException,
	                HttpStatus.CONFLICT,
	                request);
	    }
	 
	 private ResponseEntity<Object> buildErrorResponse(
	            Exception exception,
	            HttpStatus httpStatus,
	            WebRequest request) {
	        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
	    }
	 
	 private ResponseEntity<Object> buildErrorResponse(
		 Exception exception,
		 String message,
		 HttpStatus httpStatus,
		 WebRequest request){
		 ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
		 if(this.printStackTrace) {
			 errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
		 }
		 return ResponseEntity.status(httpStatus).body(errorResponse);
	 }
	 
	 
}
