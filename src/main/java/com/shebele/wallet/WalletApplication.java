package com.shebele.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     Shebele Wallet is Running!         ║");
        System.out.println("║     http://localhost:8080              ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

}
