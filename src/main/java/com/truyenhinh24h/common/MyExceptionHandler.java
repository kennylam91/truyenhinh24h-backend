package com.truyenhinh24h.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.truyenhinh24h.exception.InvalidInputException;

@ControllerAdvice
public class MyExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyExceptionHandler.class);

	@ExceptionHandler(value = { InvalidInputException.class })
	public ResponseEntity<Object> handleInvalidInputException(InvalidInputException ex) {

		LOGGER.error("InvalidInputException: {}", ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

		LOGGER.error("MethodArgumentNotValidException: {}", ex.getMessage());
		List<String> details = new ArrayList<String>();
        details = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getObjectName()+ " : " +error.getDefaultMessage())
                    .collect(Collectors.toList());

		return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception ex) {
		LOGGER.error("Exception: {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
