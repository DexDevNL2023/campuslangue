package net.ktccenter.campusApi.validators;

import net.ktccenter.campusApi.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthorizeUserValidatorConstraint implements ConstraintValidator<AuthorizeUser, Object> {

    private String actionKey;

    @Autowired
    private MainService mainService;

    @Override
    public void initialize(AuthorizeUser authorize) {
        this.actionKey = authorize.actionKey();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return true;//mainService.isAuthorized(this.actionKey);
    }
}