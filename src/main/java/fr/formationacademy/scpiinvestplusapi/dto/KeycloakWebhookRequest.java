package fr.formationacademy.scpiinvestplusapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeycloakWebhookRequest {
    public String type;
    public KeycloakUserDetails details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KeycloakUserDetails {
        public String email;
        @JsonProperty("last_name")
        private String lastName;
        @JsonProperty("first_name")
        private String firstName;
    }
}
