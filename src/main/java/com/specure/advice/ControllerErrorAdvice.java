package com.specure.advice;

import com.specure.common.exception.MeasurementNotFoundByUuidException;
import com.specure.common.exception.MeasurementServerNotFoundException;
import com.specure.common.exception.PackageTypeException;
import com.specure.response.core.ErrorResponse;
import com.specure.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.zone.ZoneRulesException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerErrorAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ErrorResponse(
                ex.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ConstraintViolationException exception) {
        return new ErrorResponse(
                exception.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList())
                        .get(0));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProviderAlreadyExistsException.class)
    public ErrorResponse handleAttemptToCreateProviderWithExistedId(ProviderAlreadyExistsException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MeasurementServerNotFoundException.class)
    public ErrorResponse handleNotFoundMeasurementServer(MeasurementServerNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProviderNotFoundByIdException.class)
    public ErrorResponse handleNotFoundProviderById(ProviderNotFoundByIdException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MeasurementNotFoundByUuidException.class)
    public ErrorResponse handleSQLException(MeasurementNotFoundByUuidException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ZoneRulesException.class)
    public ErrorResponse handleBadZoneInRequestException(ZoneRulesException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PackageTypeException.class)
    public ErrorResponse handleBadPackageTypeInRequestException(PackageTypeException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MeasurementHistoryNotAccessibleException.class)
    public ErrorResponse handleEmptyUUIDRequestException(MeasurementHistoryNotAccessibleException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
