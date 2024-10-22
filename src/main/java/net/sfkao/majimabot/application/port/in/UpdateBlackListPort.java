package net.sfkao.majimabot.application.port.in;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public interface UpdateBlackListPort {
    Mono<Void> processMessage(ChatInputInteractionEvent event);
}
