package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.configuration.KeycloakConfiguration;
import fr.formationacademy.scpiinvestplusapi.dto.KeycloakUserDto;
import fr.formationacademy.scpiinvestplusapi.globalExceptionHandler.GlobalException;
import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@EnableRetry
public class KeycloakAdminService {

    private final UserService userService;
    private final KeycloakConfiguration keycloakConfiguration;
    private Keycloak keycloak;

    public KeycloakAdminService(UserService userService, KeycloakConfiguration keycloakConfiguration) {
        this.userService = userService;
        this.keycloakConfiguration = keycloakConfiguration;
    }

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakConfiguration.getServerUrl())
                .realm(keycloakConfiguration.getRealm())
                .clientId(keycloakConfiguration.getClientId())
                .username(keycloakConfiguration.getAdminUsername())
                .password(keycloakConfiguration.getAdminPassword())
                .build();

    }

    public void updateRoleForUser(String currentRole, String newRole) {

        RoleRepresentation newRoleRepresentation = keycloak.realm(keycloakConfiguration.getRealm()).roles().get(newRole).toRepresentation();
        RoleRepresentation roleToRemove = keycloak
                .realm(keycloakConfiguration.getRealm())
                .roles()
                .get(currentRole)
                .toRepresentation();
        keycloak.realm(keycloakConfiguration.getRealm()).users().get(userService.getUserId()).roles().realmLevel().remove(List.of(roleToRemove));
        keycloak.realm(keycloakConfiguration.getRealm()).users().get(userService.getUserId()).roles().realmLevel().add(List.of(newRoleRepresentation));

    }

    @Retryable(
            retryFor = {GlobalException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 30000)
    )
    public KeycloakUserDto getUserFromKeycloak(String userId) throws GlobalException {
        try {
            UserRepresentation userRepresentation = keycloak.realm(keycloakConfiguration.getRealm()).users().get(userId).toRepresentation();
            Map<String, List<String>> attributes = userRepresentation.getAttributes();

            return KeycloakUserDto.builder()
                    .email(userRepresentation.getEmail())
                    .firstName(userRepresentation.getFirstName())
                    .lastName(userRepresentation.getLastName())
                    .phoneNumber(getSingleAttribute(attributes, "phoneNumber"))
                    .dateOfBirth(LocalDate.parse(getSingleAttribute(attributes, "dateOfBirth")))
                    .maritalStatus(getSingleAttribute(attributes, "maritalStatus"))
                    .numberOfChildren(getSingleAttribute(attributes, "numberOfChildren"))
                    .annualIncome(Integer.valueOf(getSingleAttribute(attributes, "annualIncome")))
                    .build();

        } catch (Exception e) {
            throw new GlobalException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private String getSingleAttribute(Map<String, List<String>> attributes, String key) {
        return attributes.getOrDefault(key, List.of("")).stream().findFirst().orElse("");
    }
}

