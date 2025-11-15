package gabriel.dac.livraria.application.controller.exception;

import gabriel.dac.livraria.domain.services.exception.BookIsbnExists;
import gabriel.dac.livraria.domain.services.exception.EntityNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFound e, HttpServletRequest req){
        String error = "Not Found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error,e.getMessage(),req.getRequestURI());
        return ResponseEntity.status(status).body(err);

    }

    @ExceptionHandler(BookIsbnExists.class)
    public ResponseEntity<ErrorResponse> bookIsbnExists(BookIsbnExists e, HttpServletRequest req){
        String error = "Book isbn exists";
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse err = new ErrorResponse(Instant.now(), status.value(), error,e.getMessage(),req.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
