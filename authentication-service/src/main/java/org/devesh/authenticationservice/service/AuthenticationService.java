package org.devesh.authenticationservice.service;

import org.devesh.authenticationservice.dto.ApplicationUserDTO;
import org.devesh.authenticationservice.entity.ApplicationUser;
import org.devesh.authenticationservice.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private JWtService jWtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createUser(ApplicationUserDTO applicationUserDTO){
        ApplicationUser applicationUser = mapApplicationUserDTOToApplicationUser(applicationUserDTO);
        ApplicationUser user1 = applicationUserRepository.save(applicationUser);
        return "Saved User " + user1.getName();
    }

    private ApplicationUser mapApplicationUserDTOToApplicationUser(ApplicationUserDTO applicationUserDTO) {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setName(applicationUserDTO.getName());
        applicationUser.setEmail(applicationUserDTO.getEmail());
        //We need to encode the password before saving it in DB
        applicationUser.setPassword(passwordEncoder.encode(applicationUserDTO.getPassword()));
        return applicationUser;
    }


    public String generateJWTToken(String userName){
        return jWtService.generateToken(userName);
    }

    public String validateToken(String token){
        jWtService.validateToken(token);
        return "Token is valid";
    }

}
