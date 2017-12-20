package com.au.example.busyflights.handler;

import com.au.example.busyflights.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    public final static String VALIDATION_ERROR = "Validation Error";
    public final static String UNKNOWN_ERROR = "Unknown Error";


    @Value("${pref.error.code.header.key}")
    private String errorCodeHeaderKey;


    @Autowired
    Tracer tracer;


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.handleExceptionInternal(ex, (Object)null, headers, status, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrResponse> handleValidationException(HttpServletRequest request, ValidationException e) {
        String errorMessage = e.getErrors().stream().collect(Collectors.joining(", "));
        logger.error(errorMessage);
        ErrResponse response = new ErrResponse(tracer.getCurrentSpan().getTraceId(), HttpStatus.NOT_FOUND.value(), errorMessage);

        ArrayList<String> errorKeyAndValues = new ArrayList<>();
        errorKeyAndValues.add(VALIDATION_ERROR);
        errorKeyAndValues.add(e.getErrors().stream().collect(Collectors.joining(", ")));

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(errorCodeHeaderKey, errorKeyAndValues.toArray(new String[errorKeyAndValues.size()]))
                .body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrResponse> handleUnknownException(HttpServletRequest request, Exception ex) {
        String exceptionPoint = Stream.of(ex.getStackTrace()).map(stackTraceElement -> {
            String className = Stream.of(stackTraceElement.getClassName().split("\\.")).reduce((f, s) -> s).get();
            return className + "." + stackTraceElement.getMethodName() + ":" + stackTraceElement.getLineNumber();
        }).findFirst().get();
        String cause = ex.getCause() == null ? ex.toString() : ex.getCause().toString();
        String message = ex.getMessage() == null ? exceptionPoint : ex.getMessage();
        ErrResponse response = new ErrResponse(
                tracer.getCurrentSpan().getTraceId(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                String.format(UNKNOWN_ERROR, cause, message)
        );
        logger.error(String.format(UNKNOWN_ERROR, cause, message));
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(errorCodeHeaderKey, UNKNOWN_ERROR, message)
                .body(response);
    }
}
