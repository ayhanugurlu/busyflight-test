package com.travix.medusa.busyflights.validator;


import java.util.List;

public interface Validator<T> {

    void validate(T param, List<String> errors);

}
