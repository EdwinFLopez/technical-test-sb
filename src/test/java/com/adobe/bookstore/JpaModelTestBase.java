package com.adobe.bookstore;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;

public abstract class JpaModelTestBase {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected static Validator getValidator() {
        return validator;
    }

    protected List<String> extractValidationMessages(Set<ConstraintViolation<Object>> violations) {
        List<String> violationMessages = violations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());
        return violationMessages;
    }
}
