package project.restapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import project.restapi.constants.ErrorMessages;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = ErrorMessages.OBJECT_CANNOT_BE_FOUND)
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
