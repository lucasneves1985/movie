package br.com.lcn.goldenRaspberryAwards.exception.handler;

import br.com.lcn.goldenRaspberryAwards.exception.BadRequestException;
import br.com.lcn.goldenRaspberryAwards.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequestException(final Exception ex, final WebRequest request) {

        System.out.println("Entrou no BadRequest");
        ErrorDetails error = ErrorDetails.builder()
                .title("API Error")
                .details(ex.getMessage())
                .status(400)
                .developerMessage(ex.getMessage())
                .build();
        return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
        System.out.println("Entrou no not found");
        ErrorDetails error = ErrorDetails.builder()
                .title("API Error")
                .details(ex.getMessage())
                .status(404)
                .developerMessage(ex.getMessage())
                .build();
        return new ResponseEntity<Object>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
