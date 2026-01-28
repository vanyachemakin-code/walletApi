package wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wallet.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(WalletNotFoundException e) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse("WALLET_NOT_FOUND", e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotEnoughFundsException.class)
    public ResponseEntity<ErrorResponse> handleFunds(NotEnoughFundsException e) {
        return ResponseEntity.status(422)
                .body(new ErrorResponse("NOT_ENOUGH_FUNDS", e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotValidJSON(HttpMessageNotReadableException e) {
        return ResponseEntity.status(400)
                .body(new ErrorResponse("INVALID_JSON", "Check your Request Body."));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", "Input data validation error."));
    }
}
