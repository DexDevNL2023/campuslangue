package net.ktccenter.campusApi.config.security;

import net.ktccenter.campusApi.config.security.jwt.AuthEntryPointJwt;
import net.ktccenter.campusApi.config.security.jwt.AuthTokenFilter;
import net.ktccenter.campusApi.config.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ApplicationSecurity { // extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    private PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/webjars/**",
            "/static/**",
            "/error/**",
            "/swagger-ui/**",
            "/swagger-ui.html/**",
            "/signup/**",
            "/configuration/ui",
            "/configuration/security",

            // Public routes
            "/api/public/**",
            "/api/account/**",
            "/api/**",

            // Report paths downloading
            "/app/generated-reports/**",
            "/app/data/**",
            "/data/**",
            "/generated-reports/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
