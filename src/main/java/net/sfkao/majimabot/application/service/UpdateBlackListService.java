package net.sfkao.majimabot.application.service;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.application.port.in.UpdateBlackListPort;
import net.sfkao.majimabot.application.port.out.WordBlackListDatabasePort;
import net.sfkao.majimabot.application.util.WordCleaner;
import net.sfkao.majimabot.domain.Word;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Qualifier("updateBlackListService")
public class UpdateBlackListService implements UpdateBlackListPort {

    private final WordBlackListDatabasePort wordBlackListDatabasePort;
    private final WordCleaner wordCleaner;


    @Override
    public Mono<Void> processMessage(ChatInputInteractionEvent event) {
        String palabra = event.getOption("palabra")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        if (palabra.isEmpty()) {
            return event.reply("Se necesita una palabra jefe");
        }

        palabra = wordCleaner.clean(palabra);

        Word word = new Word()
                .setPalabra(palabra)
                .setUserId(event.getInteraction().getUser().getUserData().id().asLong());

        return wordBlackListDatabasePort.save(word)
                .then(event.reply().withEphemeral(true).withContent("Se ha añadido la palabra \"" + palabra + "\" a la blacklist"));
    }
}
