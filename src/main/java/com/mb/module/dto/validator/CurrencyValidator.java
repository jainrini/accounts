package com.mb.module.dto.validator;

import com.mb.module.enums.TransactionCurrency;
import com.mb.module.exceptions.ApiException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

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

    public void validateListOfCurrency(List<String> currencies) throws ApiException {
        for (String c : currencies) {
            if (!contains(c)) {
                throw new ApiException("Invalid currency");
            }
        }
    }

    private boolean contains(String incomingCurrency) {
        for (TransactionCurrency c : TransactionCurrency.values()) {
            if (c.name().equals(incomingCurrency)) {
                return true;
            }
        }
        return false;
    }
}
