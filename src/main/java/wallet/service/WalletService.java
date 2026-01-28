package wallet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wallet.dto.WalletRequest;
import wallet.entity.Wallet;
import wallet.exception.NotEnoughFundsException;
import wallet.exception.WalletNotFoundException;
import wallet.repository.WalletRepository;
import wallet.dto.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional(readOnly = true)
    public BigDecimal getWalletBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }

    @Transactional
    public void walletOperation(WalletRequest request) {
        Wallet wallet = walletRepository.findByIdForUpdate(request.walletId())
                .orElseThrow(() -> new WalletNotFoundException(request.walletId()));

        if (request.operationType().equals(OperationType.DEPOSIT)) deposit(wallet, request.amount());
        if (request.operationType().equals(OperationType.WITHDRAW)) withdraw(wallet, request.amount());

        walletRepository.save(wallet);
    }

    private void deposit(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
    }

    private void withdraw(Wallet wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) throw new NotEnoughFundsException(wallet.getWalletId(), amount);

        wallet.setBalance(wallet.getBalance().subtract(amount));
    }
}
