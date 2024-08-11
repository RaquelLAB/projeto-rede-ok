package com.redeok.projeto.validaton.validator;

import com.redeok.projeto.validaton.annotation.ValidEnumTipoDocumento;
import com.redeok.projeto.enums.EnumTipoDocumento;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumTipoDocumentoValidator implements ConstraintValidator<ValidEnumTipoDocumento, String> {

    @Override
    public void initialize(ValidEnumTipoDocumento constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.equals(EnumTipoDocumento.CPF.getDescricao()) || value.equals(EnumTipoDocumento.CNPJ.getDescricao());
    }

}
