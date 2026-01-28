package wallet.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import wallet.database.DatabaseTestContainer;
import wallet.dto.OperationType;
import wallet.dto.WalletRequest;
import wallet.entity.Wallet;
import wallet.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest extends DatabaseTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String postUrl = "/api/v1/wallet";
    private final String getUrl = "/api/v1/wallets/";

    private UUID walletId;

    @BeforeEach
    @Transactional
    void setUp() {
        walletId = UUID.randomUUID();

        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));

        walletRepository.save(wallet);
    }

    @Test
    @DisplayName(value = "Test Deposit Operation")
    void testDepositOperation() throws Exception {
        WalletRequest request = new WalletRequest(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(200));

        mockMvc.perform(MockMvcRequestBuilders.post(postUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

        mockMvc.perform(MockMvcRequestBuilders.get(getUrl + walletId))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("300.00"));
    }

    @Test
    @DisplayName(value = "Test Withdraw Operation")
    void testWithdrawOperation() throws Exception {
        WalletRequest request = new WalletRequest(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(50));

        mockMvc.perform(MockMvcRequestBuilders.post(postUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());

        mockMvc.perform(MockMvcRequestBuilders.get(getUrl + walletId))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("50.00"));
    }

    @Test
    @DisplayName(value = "Test Not Enough Exception")
    void testNotEnoughFundsException() throws Exception {
        WalletRequest request = new WalletRequest(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(500));

        mockMvc.perform(MockMvcRequestBuilders.post(postUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.errorCode")
                        .value("NOT_ENOUGH_FUNDS"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message")
                        .exists());
    }

    @Test
    @DisplayName(value = "Test Wallet Not Found Exception")
    void testWalletNotFoundException() throws Exception {
        UUID invalidId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get(getUrl + invalidId))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.errorCode")
                        .value("WALLET_NOT_FOUND"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message")
                        .exists());
    }

    @Test
    @DisplayName(value = "Test Invalid JSON Exception")
    void testInvalidJsonException() throws Exception {
        String invalidJson = "{ \"walletId\": \"" + walletId + "\", \"operationType\": \"DEPOSIT\", \"amount\": 500";

        mockMvc.perform(MockMvcRequestBuilders.post(postUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.errorCode")
                        .value("INVALID_JSON"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message")
                        .exists());
    }

    @Test
    @DisplayName(value = "Test Validation Exception")
    void testValidationException() throws Exception {
        WalletRequest request = new WalletRequest(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(-100));

        mockMvc.perform(MockMvcRequestBuilders.post(postUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.errorCode")
                        .value("VALIDATION_ERROR"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message")
                        .exists());

    }
}
