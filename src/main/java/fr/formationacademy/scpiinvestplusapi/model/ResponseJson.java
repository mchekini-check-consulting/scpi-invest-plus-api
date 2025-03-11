package fr.formationacademy.scpiinvestplusapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseJson {
    private String name;
    private String surname;
    private String version;
}
