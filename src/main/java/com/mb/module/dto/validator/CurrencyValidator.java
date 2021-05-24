package com.mb.module.dto.validator;

import com.mb.module.enums.TransactionCurrency;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@Component
public class CurrencyValidator implements ConstraintValidator<ValidateCurrency, TransactionCurrency> {

    private TransactionCurrency[] transactionCurrency;

    @Override
    public void initialize(ValidateCurrency constraint) {
        this.transactionCurrency = constraint.anyOf();
    }

    @Override
    public boolean isValid(TransactionCurrency currency, ConstraintValidatorContext constraintValidatorContext) {
        return currency == null || Arrays.asList(transactionCurrency).contains(currency);
    }
}
