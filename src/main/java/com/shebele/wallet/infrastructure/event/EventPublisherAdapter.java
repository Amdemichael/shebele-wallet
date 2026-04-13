package com.shebele.wallet.infrastructure.event;

import com.shebele.wallet.application.port.out.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisherAdapter implements EventPublisherPort {

    private final ApplicationEventPublisher springEventPublisher;

    @Override
    public void publish(Object event) {
        log.info("Publishing event: {}", event.getClass().getSimpleName());
        springEventPublisher.publishEvent(event);
    }
}