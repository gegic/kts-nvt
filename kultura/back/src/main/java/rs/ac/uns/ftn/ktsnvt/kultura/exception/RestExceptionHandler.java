package rs.ac.uns.ftn.ktsnvt.kultura.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.sql.SQLIntegrityConstraintViolationException;

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
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorMessage errorMessage = new ErrorMessage(notFound.value(), "Not found", e.getMessage());
        return new ResponseEntity<>(errorMessage, notFound);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<ErrorMessage> handleDateIntegrityViolationException(
            DataIntegrityViolationException e) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        ErrorMessage errorMessage = new ErrorMessage(conflict.value(), "Conflict", e.getMessage());
        return new ResponseEntity<>(errorMessage, conflict);
    }
    @ExceptionHandler(value = {ForeignKeyException.class})
    public ResponseEntity<ErrorMessage> handleForeignKeyException(
            ForeignKeyException e) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        ErrorMessage errorMessage = new ErrorMessage(conflict.value(), "Conflict", e.getMessage());
        return new ResponseEntity<>(errorMessage, conflict);
    }
}
