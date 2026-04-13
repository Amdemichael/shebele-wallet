package com.shebele.wallet.application.port.out;

public interface EventPublisherPort {
    void publish(Object event);
}