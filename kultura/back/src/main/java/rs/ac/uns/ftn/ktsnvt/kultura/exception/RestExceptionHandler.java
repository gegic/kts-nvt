package rs.ac.uns.ftn.ktsnvt.kultura.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.management.InstanceAlreadyExistsException;
import java.security.InvalidKeyException;

@ControllerAdvice
public class RestExceptionHandler {



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> processIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ErrorMessage(400, "Bad request", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<ErrorMessage> processInvalidKeyException(InvalidKeyException ex) {
        return new ResponseEntity<>(new ErrorMessage(400, "Bad request", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceExistsException.class})
    public ResponseEntity<ErrorMessage> handleResourceAlreadyExistsException(ResourceExistsException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorMessage errorMessage = new ErrorMessage(409,"Conflict", e.getMessage());
        e.printStackTrace();

        return new ResponseEntity<>(errorMessage, status);
    }
}
