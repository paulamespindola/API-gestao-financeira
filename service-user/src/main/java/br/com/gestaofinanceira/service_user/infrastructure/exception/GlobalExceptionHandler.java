package br.com.gestaofinanceira.service_user.infrastructure.exception;

import br.com.gestaofinanceira.service_user.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErroResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException e, HttpServletRequest request){
        ApiErroResponse response = new ApiErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidCpfException.class)
    public ResponseEntity<ApiErroResponse> handleInvalidCpfException(
            InvalidCpfException e, HttpServletRequest request){
        ApiErroResponse response = new ApiErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiErroResponse> handleInvalidEmailException(
            InvalidEmailException e, HttpServletRequest request){
        ApiErroResponse response = new ApiErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UnderageUserException.class)
    public ResponseEntity<ApiErroResponse> handleUnderageUserException(
            UnderageUserException e, HttpServletRequest request){
        ApiErroResponse response = new ApiErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErroResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException e, HttpServletRequest request){
        ApiErroResponse response = new ApiErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserAlreadyInactiveException.class)
    public ResponseEntity<ApiErroResponse> handleUserAlreadyInactiveException(
            UserAlreadyInactiveException e, HttpServletRequest request){
        ApiErroResponse response = new ApiErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErroResponse> handleUserNotFoundException(
            UserAlreadyExistsException e, HttpServletRequest request){
        ApiErroResponse response = new ApiErroResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }



}
