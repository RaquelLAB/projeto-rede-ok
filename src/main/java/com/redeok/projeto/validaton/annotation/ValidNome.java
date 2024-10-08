package com.redeok.projeto.validaton.annotation;


import com.redeok.projeto.validaton.validator.NomeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NomeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNome {
    String message() default "Nome deve ter apenas letras e espaços";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
