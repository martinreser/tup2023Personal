package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.business.DatoInvalidoException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadesNoAprobadasException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UtnResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    //.

    @ExceptionHandler(value
            = { MateriaNotFoundException.class, AlumnoNotFoundException.class, ProfesorNotFoundException.class,
            AsignaturaNotFoundException.class})
    protected ResponseEntity<Object> notFound(
            Exception ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = {CorrelatividadesNoAprobadasException.class, EstadoIncorrectoException.class,
            CambiarEstadoAsignaturaException.class, NotaNoValidaException.class, DatoInvalidoException.class,
            YaExistenteException.class})
    protected ResponseEntity<Object> notApproved(
            Exception ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = {ProfesorEliminadoCorrectamente.class, AlumnoEliminadoCorrectamente.class})
    protected ResponseEntity<Object> todosEliminados(
            Exception ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.OK, request);
    }

//    @ExceptionHandler(value
//            = { IllegalArgumentException.class, IllegalStateException.class })
//    protected ResponseEntity<Object> handleConflict(
//            RuntimeException ex, WebRequest request) {
//        String exceptionMessage = ex.getMessage();
//        CustomApiError error = new CustomApiError();
//        error.setErrorCode(1234);
//        error.setErrorMessage(exceptionMessage);
//        return handleExceptionInternal(ex, error,
//                new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            CustomApiError error = new CustomApiError();
            error.setErrorMessage(ex.getMessage());
            body = error;
        }

        return new ResponseEntity(body, headers, status);
    }


}
