package wallet.dto;

public record ErrorResponse(
        String errorCode,
        String message
) {
}
