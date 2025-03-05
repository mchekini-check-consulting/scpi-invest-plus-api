package fr.formationacademy.scpiinvestplusapi.globalExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTemplate {
    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String path;
}
