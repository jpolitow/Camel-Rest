package com.pgs.validation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@SuppressWarnings("serial")
public class ResponseConstraintViolationException extends ConstraintViolationException {
    public ResponseConstraintViolationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }
}
