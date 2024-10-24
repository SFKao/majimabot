package net.sfkao.majimabot.application.service;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import lombok.RequiredArgsConstructor;
import net.sfkao.majimabot.application.port.in.UpdateBlackListPort;
import net.sfkao.majimabot.application.port.out.WordBlackListDatabasePort;
import net.sfkao.majimabot.application.util.WordCleaner;
import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.domain.exception.InvalidPalabraException;
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
    public Word processMessage(Word palabra) throws InvalidPalabraException {

        if (palabra.getPalabra().isEmpty()) {
            throw new InvalidPalabraException("Se necesita una palabra jefe");
        }

        palabra.setPalabra(wordCleaner.clean(palabra.getPalabra()));

        return wordBlackListDatabasePort.save(palabra);
    }
}
