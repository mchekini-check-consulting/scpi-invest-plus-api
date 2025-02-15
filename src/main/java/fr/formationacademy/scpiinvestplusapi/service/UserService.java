package fr.formationacademy.scpiinvestplusapi.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;


@Component
@Data
@AllArgsConstructor
@Builder
@Scope(scopeName = SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService {

    private String email;
    private String username;
    private String lastName;
    private String firstName;
    private String userId;


    public UserService() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        username = jwt.getClaim("preferred_username");
        email = jwt.getClaim("email");
        lastName = jwt.getClaim("family_name");
        firstName = jwt.getClaim("given_name");
        userId = jwt.getClaim("sub");
    }
}
