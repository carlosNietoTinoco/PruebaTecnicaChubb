package com.chubbTest.customer.infrastructure.rest.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.chubbTest.customer.domain.exception.CustomerNotFoundException;
import com.chubbTest.customer.domain.exception.CustomerStatusConflictException;
import com.chubbTest.customer.domain.exception.InvalidCustomerDataException;
import com.chubbTest.customer.infrastructure.rest.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class ResponseEntityExceptionHandlerImpl extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomerNotFoundException(CustomerNotFoundException e, WebRequest request) {
        return getResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(CustomerStatusConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomerStatusConflictException(CustomerStatusConflictException e, WebRequest request) {
        return getResponse(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    @ExceptionHandler(InvalidCustomerDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCustomerDataException(InvalidCustomerDataException e, WebRequest request) {
        return getResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String errorMessage = "El formato de la solicitud es inválido.";

        // Intenta extraer un mensaje más específico si es un error de formato de Enum o similar
        if (ex.getCause() instanceof InvalidFormatException ifx) {
            errorMessage = String.format("El valor '%s' es inválido para el campo '%s'.",
                    ifx.getValue(),
                    // Obtenemos el nombre del campo que falló
                    ifx.getPath().get(ifx.getPath().size() - 1).getFieldName());
        }
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            LocalDateTime.now(),
            status.value(),
            "Bad Request",
            errorMessage,
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    private ResponseEntity<ErrorResponseDTO> getResponse(HttpStatus status, String message, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}