package wallet.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(UUID walletId) {
        super(MessageFormat.format(
                "Wallet with ID {0} not found.",
                walletId
        ));
    }
}
