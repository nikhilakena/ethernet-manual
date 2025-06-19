package com.btireland.talos.ethernet.engine.soap.auth;


import com.btireland.talos.ethernet.engine.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationHelper {

    public OperatorDetails getOperator() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            log.info("Request received for Operator OAO: {} ,Code : {} ,Name : {} " , jwtAuth.getToken().getClaim("operatorOao"),jwtAuth.getToken().getClaim("operatorCode"),jwtAuth.getToken().getClaim("operatorName"));

        return OperatorDetails.builder()
                .operatorOAO(jwtAuth.getToken().getClaim("operatorOao"))
                .operatorCode(jwtAuth.getToken().getClaim("operatorCode"))
                .operatorName(jwtAuth.getToken().getClaim("operatorName"))
                .build();
        }else{
            log.error("Couldn't extract the Operator Details from JWT Token received");
            throw new AuthenticationException("WS05:Authentication Failed.");
        }
           }


}
