package org.devesh.swiggygateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public String validateToken(String token){
        return restTemplate.getForObject("http://AUTHENTICATION-SERVICE/auth/validate?token="+token, String.class);
    }

}
