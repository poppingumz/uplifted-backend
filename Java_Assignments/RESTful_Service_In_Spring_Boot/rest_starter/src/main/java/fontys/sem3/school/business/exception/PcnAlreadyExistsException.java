package fontys.sem3.school.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PcnAlreadyExistsException extends ResponseStatusException {
    public PcnAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "PCN_ALREADY_EXISTS");
    }
}
