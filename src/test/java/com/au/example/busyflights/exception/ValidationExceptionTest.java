package com.au.example.busyflights.exception;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ValidationExceptionTest {

    @Test
    public void errorMessageTest(){
        List<String> errors = new ArrayList<>();
        errors.add("First Error");
        errors.add("Second Error");
        ValidationException validationException = new ValidationException(errors);
        Assertions.assertThat(validationException.getErrors().size()).isEqualTo(2);
        Assertions.assertThat(validationException.getErrors().get(0)).isEqualTo("First Error");
        Assertions.assertThat(validationException.getErrors().get(1)).isEqualTo("Second Error");
    }
}
