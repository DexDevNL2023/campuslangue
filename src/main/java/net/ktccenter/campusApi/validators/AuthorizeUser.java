package net.ktccenter.campusApi.validators;

import javax.validation.Constraint;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AuthorizeUserValidatorConstraint.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface AuthorizeUser {
    String actionKey();
}
