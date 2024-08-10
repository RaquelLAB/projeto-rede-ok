package com.redeok.projeto.Validaton.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.redeok.projeto.Validaton.validator.TelefoneValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TelefoneValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTelefone {
    String message() default "Telefone inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
