package com.eazybytes.accounts.exception;

import com.eazybytes.accounts.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {



//    This handler will trigger, when any validation fails
//    if user enters invalid data
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors(); // this gives us all validation errors

//        iterating thru validation erros
        validationErrorList.forEach((error) ->{
//            getting invalid fielname
            String fieldName = ((FieldError)error).getField();
//            getting message that we gave in message param in validation annotation
            String validationMessage = error.getDefaultMessage();
//            putting all erros inside map
            validationErrors.put(fieldName, validationMessage);
        });

//        finally returning map, as fieldName as keys & messages as values
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);

    }




    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,
                                                                                 WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),  // if passed false we'll get only api url, if true it will fetch more details
//                above is to retrieve and set apiPath
                HttpStatus.NOT_FOUND,
                resourceNotFoundException.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

        @ExceptionHandler(CustomerAlreadyExistsException.class)
        public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException customerAlreadyExistsException,
                WebRequest webRequest){
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                    webRequest.getDescription(false),  // if passed false we'll get only api url, if true it will fetch more details
//                above is to retrieve and set apiPath
                    HttpStatus.BAD_REQUEST,
                    customerAlreadyExistsException.getMessage(),
                    LocalDateTime.now());

            return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

//    to handle all kinds of runtime exceptions
//    when an exception is thrown spring will search for that particular handler method in controlleradvice
//    if that handler is not found then this globalHanlder will be triggered.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest webRequest){

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                LocalDateTime.now()
        );

//        new ResponseEntity<>(body, status)
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
