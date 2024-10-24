package net.sfkao.majimabot.application.port.in;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import net.sfkao.majimabot.domain.Word;
import net.sfkao.majimabot.domain.exception.InvalidPalabraException;
import reactor.core.publisher.Mono;

public interface UpdateBlackListPort {
    Word processMessage(Word palabra) throws InvalidPalabraException;
}
