package com.redeok.projeto.Validaton.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.redeok.projeto.Validaton.validator.EnumTipoDocumentoValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumTipoDocumentoValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnumTipoDocumento {
    String message() default "Tipo de documento inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
