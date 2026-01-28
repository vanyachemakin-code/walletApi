package wallet.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wallet.dto.WalletRequest;
import wallet.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallet")
    public ResponseEntity<Void> walletOperation(@Valid @RequestBody WalletRequest request) {
        walletService.walletOperation(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<BigDecimal> getWalletBalance(@PathVariable UUID walletId) {
        return ResponseEntity.ok(walletService.getWalletBalance(walletId));
    }
}
