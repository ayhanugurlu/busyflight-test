package com.travix.medusa.busyflights.validation;


import com.travix.medusa.busyflights.exception.ValidationException;
import com.travix.medusa.busyflights.validation.validator.Validator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Component
public class BusyFlightValidationBinder {

    private static Logger logger = LoggerFactory.getLogger(BusyFlightValidationBinder.class);


    @SuppressWarnings("unchecked")
    @Before("execution(* com.travix.medusa.busyflights.controller..*Controller.*(..))")
    public void logBefore(JoinPoint joinPoint) throws IllegalAccessException, InstantiationException, ValidationException {

        // get request mapping from method if available
        List<Class<?>> collect = Stream.of(joinPoint.getArgs()).map(Object::getClass).collect(Collectors.toList());
        Class<?> argumentClasses[] = collect.toArray(new Class[collect.size()]);
        String methodName = joinPoint.getSignature().getName();
        Method method = null;
        try {
            // find the method
            method = joinPoint.getTarget().getClass().getMethod(methodName, argumentClasses);
        } catch (NoSuchMethodException e) {
            logger.error("will not handle this method: " + joinPoint.getSignature().getName());
        }
        if (method != null) {
            for (int i = 0; i < method.getParameters().length; i++) {
                Parameter parameter = method.getParameters()[i];
                for (Annotation annotation : parameter.getAnnotations()) {
                    if (annotation instanceof Validated) {
                        Class<?>[] validationClasses = ((Validated) annotation).value();
                        if (validationClasses != null) {
                            for (Class validationClass : validationClasses) {
                                if (Validator.class.isAssignableFrom(validationClass)) {
                                    Validator validator = (Validator) validationClass.newInstance();
                                    List<String> errors = new ArrayList<>();
                                    validator.validate(joinPoint.getArgs()[i], errors);
                                    if (!errors.isEmpty()) {
                                        throw new ValidationException(errors);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


    }

}