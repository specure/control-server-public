package com.specure.advice;

import com.specure.common.response.ErrorResponse;
import com.specure.constant.ErrorMessage;
import com.specure.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SahBackendAdvice extends ControllerErrorAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UpdateRawProviderMappingAlreadyStartedException.class)
    public ErrorResponse handleUpdateRawProviderMappingAlreadyStartedException(UpdateRawProviderMappingAlreadyStartedException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongTenantException.class)
    public ErrorResponse handleWrongTenantException(WrongTenantException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClientUuidMissingException.class)
    public ErrorResponse handleClientUuidMissingWhenShippedException(ClientUuidMissingException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TestTokenMissingException.class)
    public ErrorResponse handleTestTokenMissingWhenShippedException(TestTokenMissingException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MeasurementFormatException.class)
    public ErrorResponse handleWrongMeasurementFormatException(MeasurementFormatException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProviderNotFoundInPackageDataException.class)
    public ErrorResponse handleNotFoundProviderInServer(ProviderNotFoundInPackageDataException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleSQLException(DataIntegrityViolationException exception) {
        log.error("Data integrity exception", exception);
        return new ErrorResponse(ErrorMessage.ATTEMPT_TO_VIOLATE_DATA_INTEGRITY);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MeasurementServerNotAccessibleForOnNetMeasurementException.class)
    public ErrorResponse handleNotAccessibleMeasurementServerForOnNetException(MeasurementServerNotAccessibleForOnNetMeasurementException exception) {
        return new ErrorResponse(exception.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MeasurementServerNotFoundByProviderOnOffNetException.class)
    public ErrorResponse handleNotFoundMeasurementServerForOnNetException(MeasurementServerNotFoundByProviderOnOffNetException exception) {
        return new ErrorResponse(exception.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(QosMeasurementFromOnNetServerException.class)
    public ErrorResponse handleQosMeasurementFromOnNetServerExceptionException(QosMeasurementFromOnNetServerException exception) {
        return new ErrorResponse(exception.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadMobileTechnologyException.class)
    public ErrorResponse handleBadMobileTechnologyException(BadMobileTechnologyException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClientNotFoundException.class)
    public ErrorResponse handleClientNotFoundException(ClientNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GeoShapeNotFoundException.class)
    public ErrorResponse handleGeoShapeNotFoundException(GeoShapeNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
