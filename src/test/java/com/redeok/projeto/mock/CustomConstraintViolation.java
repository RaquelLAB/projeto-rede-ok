package com.redeok.projeto.mock;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;

public class CustomConstraintViolation<T> implements ConstraintViolation<T> {
    private final String message;

    public CustomConstraintViolation(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    // Implementações vazias para outros métodos
    @Override public T getRootBean() { return null; }
    @Override public Class<T> getRootBeanClass() { return null; }
    @Override public Object getLeafBean() { return null; }
    @Override public Object[] getExecutableParameters() { return new Object[0]; }
    @Override public Object getExecutableReturnValue() { return null; }
    @Override public Path getPropertyPath() { return null; }
    @Override public Object getInvalidValue() { return null; }
    @Override public ConstraintDescriptor<?> getConstraintDescriptor() { return null; }
    @Override public <U> U unwrap(Class<U> type) { return null; }
    @Override public String getMessageTemplate() { return null; }
}
