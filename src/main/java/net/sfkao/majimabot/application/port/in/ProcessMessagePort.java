package net.sfkao.majimabot.application.port.in;

import discord4j.core.event.domain.message.MessageCreateEvent;
import net.sfkao.majimabot.domain.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

public interface ProcessMessagePort {
    Mono<Void> processMessage(MessageCreateEvent event) throws UserNotFoundException;
}
