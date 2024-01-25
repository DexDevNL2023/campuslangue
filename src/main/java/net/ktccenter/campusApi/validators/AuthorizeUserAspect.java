package net.ktccenter.campusApi.validators;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.entities.administration.Role;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.service.administration.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
@Slf4j
public class AuthorizeUserAspect {

    private final UserService userService;

    public AuthorizeUserAspect(UserService userService) {
        this.userService = userService;
    }

    @Around("@annotation(AuthorizeUser)")
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {

        // AVANT L'EXÉCUTION DE LA MÉTHODE
        User account = userService.getCurrentUser();
        log.info("ID de l'utilisateur : " + account.getId());

        // Seul l'ID utilisateur 33 est autorisé à se connecter, les autres utilisateurs ne sont pas des utilisateurs valides.
        if (hasAuthorized(account.getRoles(), "Author")) {
            // écrire la logique métier de vérification d'autorisation
            log.info("L'utilisateur n'a pas l'authorisation nécésaire pour effectuer cette action : " + joinPoint.toShortString());
            return new ResourceNotFoundException("L'utilisateur n'a pas l'authorisation nécésaire pour effectuer cette action : " + joinPoint.toShortString());
        }

        // C'est là que la MÉTHODE RÉELLE sera invoquée
        Object result = joinPoint.proceed();

        // APRÈS L'EXÉCUTION DE LA MÉTHODE
        log.info(result.toString());
        return result;
    }

    private boolean hasAuthorized(Set<Role> authoritie, String actionKey) {
        for (Role role : authoritie) {
            if (role.getIsGrant() || role.getIsSuper()) {
                return true;
            } else {
                for (RoleDroit permission : role.getPermissions()) {
                    if (permission.getHasDroit() && permission.getDroit().getKey().equals(actionKey)) return true;
                }
            }
        }
        return false;
    }
}
