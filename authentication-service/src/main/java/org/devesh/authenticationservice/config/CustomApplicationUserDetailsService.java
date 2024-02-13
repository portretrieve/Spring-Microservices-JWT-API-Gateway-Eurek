package org.devesh.authenticationservice.config;

import org.devesh.authenticationservice.entity.ApplicationUser;
import org.devesh.authenticationservice.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<ApplicationUser> optionalApplicationUser = applicationUserRepository.findByName(username);
       return optionalApplicationUser.map(user->new CustomApplicationUserDetails(user))
               .orElseThrow(()->new UsernameNotFoundException("User not found with User Name: " + username));
    }
}
