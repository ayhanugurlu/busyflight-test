package com.travix.medusa.busyflights.exception;

import java.util.List;

public class ValidationException extends Exception {

    private final List<String> errors;

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
