package com.shebele.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.shebele.wallet.domain",
        "com.shebele.wallet.application",
        "com.shebele.wallet.infrastructure",
        "com.shebele.wallet.presentation",
        "com.shebele.wallet.shared"
})
@EntityScan(basePackages = {
        "com.shebele.wallet.infrastructure.persistence.entity"
})
@EnableJpaRepositories(basePackages = {
        "com.shebele.wallet.infrastructure.persistence.repository"
})
public class WalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }
}