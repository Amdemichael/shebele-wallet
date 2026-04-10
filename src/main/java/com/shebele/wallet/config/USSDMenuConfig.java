package com.shebele.wallet.config;

import com.shebele.wallet.ussd.USSDMenuRouter;
import com.shebele.wallet.ussd.MenuHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class USSDMenuConfig {

    private final USSDMenuRouter router;
    private final List<MenuHandler> handlers;

    @PostConstruct
    public void registerHandlers() {
        handlers.forEach(router::registerHandler);
    }
}