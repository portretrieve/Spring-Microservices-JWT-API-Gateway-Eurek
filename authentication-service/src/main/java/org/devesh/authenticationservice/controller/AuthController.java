package org.devesh.authenticationservice.controller;

import org.apache.catalina.User;
import org.devesh.authenticationservice.dto.ApplicationUserDTO;
import org.devesh.authenticationservice.dto.AuthRequestDTO;
import org.devesh.authenticationservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping
    public String test(){
        return "Auth Service is up and running";
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody ApplicationUserDTO applicationUserDTO){
        return new ResponseEntity<>(authenticationService.createUser(applicationUserDTO), HttpStatus.CREATED);
    }

    @PostMapping("/token")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (authRequestDTO.getUserName(), authRequestDTO.getPassword()));
        if (authentication.isAuthenticated()){
            String token = authenticationService.generateJWTToken(authRequestDTO.getUserName());
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        }else {
            throw new UsernameNotFoundException("Invalid Credentials");
        }

    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam(value = "token") String token){
        String validMessage = authenticationService.validateToken(token);
        return new ResponseEntity<>(validMessage, HttpStatus.OK);
    }
}
