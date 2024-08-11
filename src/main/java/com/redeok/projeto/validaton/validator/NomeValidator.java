package com.redeok.projeto.validaton.validator;

import com.redeok.projeto.validaton.annotation.ValidNome;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NomeValidator implements ConstraintValidator<ValidNome, String> {

    @Override
    public void initialize(ValidNome constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return value.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$");
    }

}
