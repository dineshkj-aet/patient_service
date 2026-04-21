package com.assignment.patient.api.controller;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ProblemDetail handleRuntimeException(RuntimeException ex, WebRequest request) {
		return createProblemDetail(
				HttpStatus.BAD_REQUEST,
				"Business Logic Error",
				ex.getMessage(),
				request.getDescription(false).replace("uri=", ""),
				Map.of("error", ex.getMessage())
		);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		var errors = ex.getBindingResult().getFieldErrors()
				.stream()
				.collect(Collectors.toMap(
						FieldError::getField,
						fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value",
						(existing, replacement) -> existing
				));

		ProblemDetail pd = createProblemDetail(
				status,
				"Validation Failed",
				"One or more fields are invalid.",
				request.getDescription(false).replace("uri=", ""),
				errors
		);

		return ResponseEntity.status(status).body(pd);
	}

	private ProblemDetail createProblemDetail(HttpStatusCode status, String title, String detail, String instance, Map<String, String> errors) {
		ProblemDetail problemDetal = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetal.setTitle(title);
		problemDetal.setInstance(URI.create(instance));
		problemDetal.setProperty("timestamp", Instant.now());
		problemDetal.setProperty("errors", errors);
		return problemDetal;
	}
}
