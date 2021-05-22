package com.mb.module.dto.validator;

import com.mb.module.enums.DirectionCode;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Component
public class DirectionCodeValidator implements ConstraintValidator<ValidateDirectionCode, DirectionCode> {

    private DirectionCode[] directionCode;

    @Override
    public void initialize(ValidateDirectionCode constraint) {
        this.directionCode = constraint.anyOf();
    }

    @Override
    public boolean isValid(DirectionCode value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(directionCode).contains(value);
    }
}
