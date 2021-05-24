package com.mb.module.dto.validator;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class CountryCodeValidator implements ConstraintValidator<ValidateCountryCode, String> {

    private static final Set<String> ISO_COUNTRIES = new HashSet<String>
        (Arrays.asList(Locale.getISOCountries()));

    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        return ISO_COUNTRIES.contains(country);
    }
}
