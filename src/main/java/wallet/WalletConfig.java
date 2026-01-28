package wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class WalletConfig {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WalletConfig.class, args);
    }
}
