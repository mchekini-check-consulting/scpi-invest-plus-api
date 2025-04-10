package fr.formationacademy.scpiinvestplusapi.service;

import fr.formationacademy.scpiinvestplusapi.configuration.KeycloakConfiguration;
import jakarta.annotation.PostConstruct;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakAdminService {

    private final UserService userService;
    private Keycloak keycloak;
    private final KeycloakConfiguration keycloakConfiguration;

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

}

