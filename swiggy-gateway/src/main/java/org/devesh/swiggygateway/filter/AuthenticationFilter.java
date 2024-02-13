package org.devesh.swiggygateway.filter;
import org.devesh.swiggygateway.client.AuthenticationServiceClient;
import org.devesh.swiggygateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private AuthenticationServiceClient authenticationServiceClient;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            //Checking if the request contains secure endpoint
            if (routeValidator.isSecured.test(exchange.getRequest())){

                //Now checking if request contains AUTHORIZATION Header
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("Missing Authorization Header");
                }

                //Extract AUTHORIZATION Header
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String jwtToken = null;
                if (authHeader!= null && authHeader.startsWith("Bearer")){
                    //Extract JWt Token
                    jwtToken= authHeader.substring(7);
                }

                //Either use RestTemplate to validate token

                //                try{
                //                    authenticationServiceClient.validateToken(JWTtoken);
                //                    }


                //Or
                try {

                    jwtUtil.validateToken(jwtToken);

                }catch (Exception exception){

                    System.out.println(exception.getMessage());
                    throw new RuntimeException("Un-Authorized Access.");

                }


            }


            return chain.filter(exchange);
        }));
    }

    public static class Config{}



}
