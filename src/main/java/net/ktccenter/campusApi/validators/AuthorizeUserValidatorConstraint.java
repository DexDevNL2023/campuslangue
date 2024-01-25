package net.ktccenter.campusApi.validators;

import net.ktccenter.campusApi.service.administration.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthorizeUserValidatorConstraint implements ConstraintValidator<AuthorizeUser, Object> {

    private final UserService userService;
    private String actionKey;

    public AuthorizeUserValidatorConstraint(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(AuthorizeUser authorize) {
        this.actionKey = authorize.actionKey();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return userService.isAuthorized(this.actionKey);
    }
}