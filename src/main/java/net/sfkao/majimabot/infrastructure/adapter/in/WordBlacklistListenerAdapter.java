package net.sfkao.majimabot.infrastructure.adapter.in;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.extern.log4j.Log4j2;
import net.sfkao.majimabot.application.port.in.ProcessMessagePort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@Log4j2
public class WordBlacklistListenerAdapter implements DiscordEvent<MessageCreateEvent>, ProcessMessagePort {

    private final ProcessMessagePort processMessagePort;

    public WordBlacklistListenerAdapter(@Qualifier("processMessageService") ProcessMessagePort processMessagePort) {
        this.processMessagePort = processMessagePort;
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return this.processMessage(event);
    }

    @Override
    public Mono<Void> error(Throwable error) {
        log.error("Error leyendo un mensaje", error);
        return Mono.empty();
    }

    @Override
    public Mono<Void> processMessage(MessageCreateEvent event) {
        return processMessagePort.processMessage(event);
    }
}