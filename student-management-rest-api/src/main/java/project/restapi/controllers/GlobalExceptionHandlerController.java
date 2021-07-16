package project.restapi.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.restapi.domain.models.error.ErrorViewModel;
import project.restapi.exceptions.ObjectAlreadyExistsException;
import project.restapi.exceptions.ObjectNotFoundException;

import javax.security.sasl.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ObjectAlreadyExistsException.class})
    public ResponseEntity<Object> handleObjectAlreadyExistsException(ObjectAlreadyExistsException e, WebRequest request) {
        ErrorViewModel errorViewModel = new ErrorViewModel();
        errorViewModel.add(e.getMessage());
        return this.handleExceptionInternal(e, errorViewModel, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        ErrorViewModel errorViewModel = new ErrorViewModel();
        errorViewModel.add(e.getMessage());
        return this.handleExceptionInternal(e, errorViewModel, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e, WebRequest request) {
        ErrorViewModel errorViewModel = new ErrorViewModel();
        errorViewModel.add(e.getMessage());
        return this.handleExceptionInternal(e, errorViewModel, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler({ObjectNotFoundException.class})
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException e, WebRequest request) {
        ErrorViewModel errorViewModel = new ErrorViewModel();
        errorViewModel.add(e.getMessage());
        return this.handleExceptionInternal(e, errorViewModel, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleAuthenticationException(BadCredentialsException e, WebRequest request) {
        ErrorViewModel errorViewModel = new ErrorViewModel();
        errorViewModel.add(e.getMessage());
        return this.handleExceptionInternal(e, errorViewModel, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
}