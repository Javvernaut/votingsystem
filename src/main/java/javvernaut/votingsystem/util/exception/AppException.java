package javvernaut.votingsystem.util.exception;

import lombok.Getter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class AppException extends ResponseStatusException {
    private final ErrorAttributeOptions options;

    public AppException(HttpStatus status, String reason, ErrorAttributeOptions options) {
        super(status, reason);
        this.options = options;
    }
}
