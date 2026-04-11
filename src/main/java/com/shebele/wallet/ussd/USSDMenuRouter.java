package com.shebele.wallet.ussd;

import com.shebele.wallet.dto.response.UssdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class USSDMenuRouter {

    private final Map<USSDMenu, MenuHandler> handlers = new ConcurrentHashMap<>();
    private final UssdResponseBuilder responseBuilder;

    public void registerHandler(MenuHandler handler) {
        handlers.put(handler.getSupportedMenu(), handler);
        log.info("Registered handler for menu: {}", handler.getSupportedMenu());
    }

    public UssdResponse route(USSDContext context) {
        USSDMenu currentMenu = context.getCurrentMenu();
        MenuHandler handler = handlers.get(currentMenu);

        if (handler == null) {
            log.warn("No handler found for menu: {}", currentMenu);
            context.setCurrentMenu(USSDMenu.MAIN);
            return responseBuilder.mainMenu();
        }

        return handler.handle(context, responseBuilder);
    }
}