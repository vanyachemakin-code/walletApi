package wallet.exception;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.UUID;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException(UUID walletId, BigDecimal amount) {
        super(MessageFormat.format(
                "Wallet with ID {0} has not enough funds for operation with amount {1}.",
                walletId,
                amount
        ));
    }
}
