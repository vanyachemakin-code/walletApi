package wallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.math.BigDecimal;
import java.util.UUID;

public record WalletRequest(

        @NotNull(message = "walletId can't be null")
        UUID walletId,

        @NotNull(message = "operationType can't be null")
        OperationType operationType,

        @NotNull(message = "amount can't be null")
        @Positive(message = "amount can't be negative")
        BigDecimal amount
) {
}
