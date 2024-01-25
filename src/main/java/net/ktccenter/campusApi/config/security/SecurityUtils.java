package net.ktccenter.campusApi.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * @return PK ( ID ) of current id
     */
    public static String getCurrentUsername() {

        User user = getCurrentUserDetails();
        if (user instanceof UserDetails) {
            return user.getUsername();
        }
        return null;
    }

    public static User getCurrentUserDetails() {
        return getCurrentUserDetails(SecurityContextHolder.getContext().getAuthentication());
    }

    public static User getCurrentUserDetails(Authentication authentication) {
        User userDetails = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            userDetails = (User) authentication.getPrincipal();
        }
        return userDetails;
    }
}
