package com.au.example.busyflights.validation.validator;


import java.util.List;

public interface Validator<T> {

    public static final String FIELD_CANNOT_BE_EMPTY = "%1s cannot be empty";

    public static final String DATE_FORMAT_ERROR = "%1s %2s date unparsable";

    void validate(T param, List<String> errors);

}
