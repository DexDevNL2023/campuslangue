package net.ktccenter.campusApi.config.security.services;

import net.ktccenter.campusApi.dao.administration.UserRepository;
import net.ktccenter.campusApi.entities.administration.User;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(user);
    }

}
