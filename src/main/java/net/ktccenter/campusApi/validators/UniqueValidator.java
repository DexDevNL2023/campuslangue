package net.ktccenter.campusApi.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER, TYPE, TYPE_USE, TYPE_PARAMETER, METHOD, CONSTRUCTOR, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UniqueValidatorConstraint.class)
@NotNull
@ReportAsSingleViolation
public @interface UniqueValidator {
    Class<? extends FieldValueExists> service();
    String serviceQualifier() default "";
    String fieldName();
    String message() default "ne doit être null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
