package fr.formationacademy.scpiinvestplusapi.globalExceptionHandler;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GlobalException  extends Exception {
    private HttpStatus httpStatus;

    public GlobalException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
