package com.mb.module.dto.validator;

import com.mb.module.enums.DirectionCode;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DirectionCodeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateDirectionCode {

    DirectionCode[] anyOf();

    String message() default "Invalid direction code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
