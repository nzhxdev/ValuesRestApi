package ru.nzhx.ValuesRestApi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.nzhx.ValuesRestApi.util.ResponseError;
import ru.nzhx.ValuesRestApi.util.ValueNotFoundException;
import ru.nzhx.ValuesRestApi.util.ValueNotUpdatedException;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseError handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseError handleException(ValueNotFoundException e) {
        return new ResponseError("Value with the specified 'id' doesn't exist.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseError handleException(ValueNotUpdatedException e) {
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseError handleException(DateTimeParseException e) {
        String errorMessage = " - Incorrect date format. The date should be in the format 'yyyyy-mm-dd hh:mm:ss'.";
        return new ResponseError(e.getParsedString() + errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseError handleException(NumberFormatException e) {
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
