package net.ktccenter.campusApi.config;

import lombok.RequiredArgsConstructor;
import net.ktccenter.campusApi.config.security.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;


@RequiredArgsConstructor
public class AuditorResolver implements AuditorAware<String> {

    @Override
    @Transactional(readOnly = true)
    public Optional<String> getCurrentAuditor() {

        String username = SecurityUtils.getCurrentUsername();
        if (!StringUtils.hasLength(username)) {
            return Optional.ofNullable("Super");
        }

        return Optional.ofNullable(username);
    }
}
