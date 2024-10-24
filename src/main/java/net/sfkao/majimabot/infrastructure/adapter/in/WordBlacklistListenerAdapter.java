package net.sfkao.majimabot.infrastructure.adapter.in;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.extern.log4j.Log4j2;
import net.sfkao.majimabot.application.port.in.ProcessMessagePort;
import net.sfkao.majimabot.domain.MessageEvent;
import net.sfkao.majimabot.domain.Word;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.PermissionDeniedDataAccessException;
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

        MessageEvent messageEvent = new MessageEvent();

        String content = event.getMessage().getContent();

        if (event.getMember().isEmpty() || content.isEmpty()) {
            return Mono.empty();
        }

        messageEvent.setContent(content);
        messageEvent.setIdUsuario(event.getMember().get().getId().asLong());

        messageEvent = this.processMessage(messageEvent);

        if(messageEvent.getMatches()==null){
            if(messageEvent.getMessageResponse()!=null) {
                Mono<Void> then = event.getMember().get().edit()
                        .withCommunicationDisabledUntilOrNull(messageEvent.getBanUntil())
                        .onErrorResume(error -> {
                            if (!(error instanceof PermissionDeniedDataAccessException)) {
                                return Mono.empty();
                            }
                            return Mono.error(error);
                        }).then();

                MessageEvent finalMessageEvent = messageEvent;
                return event.getMessage().getChannel()
                        .flatMap(channel -> channel.createMessage(finalMessageEvent.getMessageResponse()))
                        .then(then);
            }
            return Mono.empty();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<@").append(messageEvent.getIdUsuario()).append("> ha sido silenciado por decir [");
        for (Word w : messageEvent.getMatches()) {
            sb.append("'").append(w.getPalabra()).append("' de ")
                    .append(event.getClient().getUserById(Snowflake.of(w.getUserId())).block().getUsername()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).append("]");

        Mono<Void> then = event.getMember().get().edit()
                .withCommunicationDisabledUntilOrNull(messageEvent.getBanUntil())
                .onErrorResume(error -> {
                    if (!(error instanceof PermissionDeniedDataAccessException)) {
                        return Mono.empty();
                    }
                    return Mono.error(error);
                }).then();

        return event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage(sb.toString()))
                .then(then);
    }

    @Override
    public Mono<Void> error(Throwable error) {
        log.error("Error leyendo un mensaje", error);
        return Mono.empty();
    }

    @Override
    public MessageEvent processMessage(MessageEvent event) {
        return processMessagePort.processMessage(event);
    }
}