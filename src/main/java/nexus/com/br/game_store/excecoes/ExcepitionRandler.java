package nexus.com.br.game_store.excecoes;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExcepitionRandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(), request.getRequestURI(), e.getMessage(), error, status.value());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ResourceAlreadyRegistered.class)
    public ResponseEntity<StandardError> ResourceAlreadyRegistered (ResourceAlreadyRegistered e, HttpServletRequest request) {
        String error = "Resource already registered";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(Instant.now(), request.getRequestURI(), e.getMessage(), error, status.value());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(EmailEnvioException.class)
    public ResponseEntity<StandardError> emailEnvioException(EmailEnvioException e, HttpServletRequest request) {
        String error = "Email sending error";
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        StandardError standardError = new StandardError(Instant.now(), request.getRequestURI(), e.getMessage(), error, status.value());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(GeolocalizacaoException.class)
    public ResponseEntity<StandardError> geolocalizacaoException(GeolocalizacaoException e, HttpServletRequest request) {
        String error = "Geolocation service error";
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        StandardError standardError = new StandardError(Instant.now(), request.getRequestURI(), e.getMessage(), error, status.value());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(TokenJwtException.class)
    public ResponseEntity<StandardError> tokenJwtException(TokenJwtException e, HttpServletRequest request) {
        String error = "JWT Token error";
        HttpStatus status = HttpStatus.UNAUTHORIZED; // 401 para problemas de token
        StandardError standardError = new StandardError(Instant.now(), request.getRequestURI(), e.getMessage(), error, status.value());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> businessException(BusinessException e, HttpServletRequest request) {
        String error = "Business rule violation";
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400 para regras de negócio violadas
        StandardError standardError = new StandardError(Instant.now(), request.getRequestURI(), e.getMessage(), error, status.value());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<StandardError> tokenExpiradoException(TokenExpiradoException e, HttpServletRequest request) {
        String error = "Token expired";
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400 ou 422 para token vencido
        StandardError standardError = new StandardError(Instant.now(), request.getRequestURI(), e.getMessage(), error, status.value());
        return ResponseEntity.status(status).body(standardError);
    }


}
